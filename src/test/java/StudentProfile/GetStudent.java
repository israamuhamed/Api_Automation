package StudentProfile;

import EducatorProfile.Educator_TestData;
import StudentClasses.Student_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import StudentClasses.Student_TestData;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class GetStudent {
    TestBase test = new TestBase();
    Student_TestData studentData = new Student_TestData();
    CreateStudent student = new CreateStudent();
    Educator_TestData data = new Educator_TestData();
    Database_Connection Connect = new Database_Connection();
    Response Get_Student_Profile;
    String refreshToken;
    public String studentFirstName;
    public String studentLastName;
    public String studentEmail;
    Long StudentID;
    public Long gradeId;

    public Map<String, Object> pathParams = test.pathParams;

    public void get_student_data_from_database() throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select student_first_name,student_last_name,student_email,grade_id from students s where student_id ="+ StudentID +"");

        while (resultSet.next()) {
            studentFirstName = resultSet.getString("student_first_name");
            studentLastName = resultSet.getString("student_last_name");
            studentEmail = resultSet.getString("student_email");
            gradeId = resultSet.getLong("grade_id");
        }
        System.out.println("studentFirstName "+studentFirstName);
    }

        @When("Performing the Api of Get Student Profile")
        public void Get_Student_Profile() throws SQLException {
        student.Verify_Student_OTP_already_Auth();
        refreshToken = student.student_refresh_token;
        System.out.println("token " + refreshToken);
            Get_Student_Profile = test.sendRequest("GET", "/students/{student_id}/profile", null,refreshToken);
        }

        @Given("User Send valid student Id to get student")
        public void Sending_valid_StudentId() throws SQLException {
            student.Create_Student();
            StudentID = student.studentId;
            System.out.println("StudentID "+ StudentID);
            pathParams.put("student_id",StudentID);
        }

        @Then("I verify the appearance of status code 200 and student data returned")
        public void Validate_Response_of_Getting_student_Profile() throws SQLException {
            get_student_data_from_database();
            Get_Student_Profile.prettyPrint();
            Get_Student_Profile.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentProfile/GetStudent.json")))
                .body("first_name", hasToString(studentFirstName), "last_name", hasToString(studentLastName), "email", hasToString(studentEmail),"user_id", equalTo(StudentID));
    }

    @Given("User Send Invalid student Id")
    public void Sending_Invalid_StudentId() throws SQLException {
        student.Create_Student();
        pathParams.put("student_id","12345678987666");
    }
    @Then("I verify the appearance of status code 422 and Id is incorrect")
    public void Validate_Response_of_invalid_student_ID() throws SQLException {
        Response Invalid_StudentID = Get_Student_Profile;
        test.Validate_Error_Messages(Invalid_StudentID,HttpStatus.SC_UNPROCESSABLE_ENTITY,"Entity ID must be a 12-digit number.",4221);
    }
    @Given("User Send unauthorized student Id")
    public void Sending_unauthorized_StudentId() throws SQLException {
        student.Create_Student();
        pathParams.put("student_id","123456789879");
    }

    @Then("I verify the appearance of status code 403 and Id is unauthorized")
    public void Validate_Response_of_unauth_student_ID() throws SQLException {
        Response unauth_StudentID = Get_Student_Profile;
        test.Validate_Error_Messages(unauth_StudentID,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }

    @Given("User Send inactive student Id")
    public void Sending_inactive_StudentId() throws SQLException {
        pathParams.put("student_id",studentData.student_not_exist);
    }

    @When("Performing the Api of Get Student Profile with inactive student")
    public void Get_Student_Profile_with_InactiveStudent() {
        Get_Student_Profile = test.sendRequest("GET", "/students/{student_id}/profile", null,studentData.student_refreshToken_not_exist);
    }

    @Then("I verify the appearance of status code 404 and Id is inactive")
    public void Validate_Response_of_Inactive_student_ID() throws SQLException {
        Response Inactive_StudentID = Get_Student_Profile;
        test.Validate_Error_Messages(Inactive_StudentID,HttpStatus.SC_NOT_FOUND,"Student with the specified ID does not exist or is not active.",4041);
    }


}
