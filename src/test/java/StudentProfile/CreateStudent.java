package StudentProfile;

import EducatorProfile.Educator_TestData;
import StudentParentAuth.VerifyEmailOTP;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import com.github.javafaker.Faker;
import io.cucumber.cienvironment.internal.com.eclipsesource.json.Json;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class CreateStudent {
    TestBase test = new TestBase();
    Educator_TestData data =new Educator_TestData();
    Database_Connection Connect = new Database_Connection();
    Faker fakeDate =new Faker();
    String firstName = fakeDate.name().firstName();
    String lastName = fakeDate.name().lastName();
    String email ;
    VerifyEmailOTP verifyEmail = new VerifyEmailOTP();
    public Long studentId;
    public Long Grade_ID;
    Long walletId;
    String OTP;
    String Valid_body_request ;
    public String student_refresh_token;
    String CreateToken;
    public String student_refreshToken;
    Response Create_Student;
    Response Verify_Student_OTP;
    @Given("Get grades from database")
    public void get_grade_from_database () throws SQLException {
        ResultSet GradeResult = Connect.connect_to_database("SELECT * FROM public.grades g\n" +
                "join stages s \n" +
                "on s.stage_id = g.stage_id \n" +
                "where s.country_id = 102123867837 and g.grade_url_text = '11'");
        while (GradeResult.next()) {
            Grade_ID = GradeResult.getLong("grade_id");
        }

    }
    @And("Get student data from database")
    public void get_student_data_from_database () throws SQLException {
        ResultSet Student_Result = Connect.connect_to_database("select* from students s \n" +
                "join students_wallets sw \n" +
                "on s.student_id = sw.student_id \n" +
                "where s.student_id ="+ studentId +"");
        while (Student_Result.next()) {
            walletId = Student_Result.getLong("student_wallet_id");
            System.out.println(walletId);
        }


    }
    @When("Performing the Api of Create Student With valid data")
    public Long Create_Student() throws SQLException {
        get_grade_from_database ();
        verifyEmail.Verify_Student_OTP();
        CreateToken = verifyEmail.create_account_token;
        System.out.println("CreateToken "+CreateToken);
        email = verifyEmail.Email;
        OTP = verifyEmail.OTP;
        System.out.println(email +" "+ OTP);
        Valid_body_request = "{\"student_first_name\":\""+ firstName +"\",\"student_last_name\":\""+ lastName +"\",\"student_email\":\""+ email +"\"" +
                ",\"grade_id\":"+ Grade_ID +",\"social_media_id\":null}" ;
        System.out.println(Valid_body_request);
        Create_Student =  RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", CreateToken)
                .body(Valid_body_request)
                .when()
                .post("/students/create");
        Create_Student.prettyPrint();
        student_refreshToken = Create_Student.then().extract().path("tokens.refresh_token");
        return studentId = Create_Student.then().extract().path("data.user_id") ;

    }

    public String getStudent_refresh_token() {
        return student_refreshToken ;
    }
    @Then("I verify the appearance of status code 201 and Student created successfully")
    public void Validate_Response_of_create_Student_successfully() {
        Create_Student.prettyPrint();
        Create_Student.then()
                .statusCode(HttpStatus.SC_CREATED)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentProfile/CreateStudent.json")))
                .body("message", hasToString("Student account created successfully."),"data.user_id",equalTo(studentId),"data.student_wallet_id",equalTo(walletId));
    }

    @When("Performing the Api of Create Student With grade not exist")
    public Long Create_Student_with_InvalidGrade() throws SQLException {
        verifyEmail.Verify_Student_OTP();
        CreateToken = verifyEmail.create_account_token;
        email = verifyEmail.Email;
        System.out.println(CreateToken);
        Valid_body_request = "{\"student_first_name\":\""+ firstName +"\",\"student_last_name\":\""+ lastName +"\",\"student_email\":\""+ email +"\"" +
                ",\"grade_id\":183108573333,\"social_media_id\":null}" ;
        Create_Student =  RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", CreateToken)
                .body(Valid_body_request)
                .when()
                .post("/students/create");
        return studentId;
    }

    @Then("I verify the appearance of status code 400 and Invalid Grade")
    public void Validate_Response_Invalid_Grade() {
        Response Invalid_Grade = Create_Student;
        test.Validate_Error_Messages(Invalid_Grade,HttpStatus.SC_BAD_REQUEST,"Grade with the specified ID is not active.",40012);
    }

    @When("Performing the Api of Create Student With invalid data")
    public Long Create_Student_with_InvalidData() throws SQLException {
        verifyEmail.Verify_Student_OTP();
        CreateToken = verifyEmail.create_account_token;
        email = verifyEmail.Email;
        System.out.println(CreateToken);
        Valid_body_request = "{\"student_first_name\":\"\",\"student_last_name\":\"\",\"student_email\":\"\"" +
                ",\"grade_id\":183108573333,\"social_media_id\":null}" ;
        Create_Student =  RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", CreateToken)
                .body(Valid_body_request)
                .when()
                .post("/students/create");
        return studentId;
    }

    @Then("I verify the appearance of status code 400 and Invalid data")
    public void Validate_Response_Invalid_Data() {
        Response Invalid_Grade = Create_Student;
        test.Validate_Error_Messages(Invalid_Grade,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }

    @When("Performing the Api of Create Student With unauthorized student")
    public Long Create_Student_with_duplicated_Email() throws SQLException {
        verifyEmail.Verify_Student_OTP();
        CreateToken = verifyEmail.create_account_token;
        email = verifyEmail.Email;
        System.out.println(CreateToken);
        Valid_body_request = "{\"student_first_name\":\""+ firstName +"\",\"student_last_name\":\""+ lastName +"\",\"student_email\":\"nicolo.bazzi@nagwa.com\"" +
                ",\"grade_id\":183108573333,\"social_media_id\":null}" ;
        Create_Student =  RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Authorization", CreateToken)
                .body(Valid_body_request)
                .when()
                .post("/students/create");
        return studentId;
    }

    @Then("I verify the appearance of status code 400 and student is unauthorized")
    public void Validate_Response_duplicated_email() {
        Response duplicated_email = Create_Student;
        test.Validate_Error_Messages(duplicated_email,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }


    @And("Performing the Api of Verify Student OTP with already Auth Student")
    public String Verify_Student_OTP_already_Auth() {
        String Valid_body_request = "{\"email\":\""+ email +"\",\"otp\":\"" + OTP + "\"}";
        Verify_Student_OTP = test.sendRequest("POST", "/auth/verify-otp", Valid_body_request,data.Admin_Token);
            System.out.println(email + OTP);
        return student_refresh_token = Verify_Student_OTP.then().extract().path("tokens.refresh_token");
    }

    @Then("I verify the appearance of status code 200 and student already authenticated")
    public void Validate_Response_of_verify_Student_OTP_already_auth() {
        System.out.println(student_refresh_token + verifyEmail.studentEmail);
        Verify_Student_OTP.prettyPrint();
        Verify_Student_OTP.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentParentAuthSchemas/VerifyAleardyAuthStudent.json")))
                .body("message", hasToString("Existing user authenticated."),"message_id",equalTo(2001),
                        "data.email",hasToString(email),"data.first_name",hasToString(firstName),"data.last_name",hasToString(lastName),
                        "data.role",hasToString("student"));
    }

}
