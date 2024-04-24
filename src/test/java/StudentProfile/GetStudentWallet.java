package StudentProfile;

import EducatorProfile.Educator_TestData;
import StudentClasses.Student_TestData;
import TestConfig.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import StudentClasses.Student_TestData;

import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class GetStudentWallet {
    Student_TestData studentData = new Student_TestData();
    TestBase test = new TestBase();
    CreateStudent student = new CreateStudent();
    Educator_TestData data = new Educator_TestData();
    Response Get_Student_Wallet;
    Long StudentID;
    String refreshToken;
    public Map<String, Object> pathParams = test.pathParams;

    @When("Performing the Api of Get Student Wallet")
    public void Get_Student_Wallet(){
        student.getStudent_refresh_token();
        refreshToken = student.student_refreshToken;
        Get_Student_Wallet = test.sendRequest("GET", "/students/{student_id}/wallet", null,refreshToken);
    }

    @Given("User Send valid student Id to get wallet")
    public void Sending_valid_StudentId() throws SQLException {
        student.Create_Student();
        StudentID = student.studentId;
        pathParams.put("student_id",StudentID);
    }

    @Then("I verify the appearance of status code 200 and student wallet data returned")
    public void Validate_Response_of_Getting_student_wallet() throws SQLException {
        Get_Student_Wallet.prettyPrint();
        Get_Student_Wallet.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentProfile/GetStudentWallet.json")))
                .body("student_wallet.student_wallet_balance", equalTo(BigDecimal.valueOf(0.0).floatValue()), "student_wallet.currency", hasToString("EGP"), "wallet_transactions", empty());
    }

    @Given("User Send Invalid std Id to get student wallet")
    public void Sending_Invalid_StudentId_special_char() throws SQLException {
        pathParams.put("student_id","!!@@");
    }

    @Then("I verify the appearance of status code 400 and Id is incorrect")
    public void Validate_Response_of_invalid_student_ID() throws SQLException {
        Response Invalid_StudentID = Get_Student_Wallet;
        test.Validate_Error_Messages(Invalid_StudentID,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }

    @Given("User Send Invalid StudentId to get student wallet")
    public void Sending_Invalid_StudentId_to_getWallet() throws SQLException {
        pathParams.put("student_id",studentData.student_not_exist);
    }

    @When("Performing the Api of get wallet with student not exist")
    public void get_Student_wallet_student_not_exist() throws SQLException {
        Get_Student_Wallet = test.sendRequest("GET", "/students/{student_id}/wallet", null,studentData.student_refreshToken_not_exist);
    }

    @Then("I verify the appearance of status code 404 and student is not exist")
    public void Validate_Response_of_Get_student_Wallet_NotFound_student() {
        Response invalid_data = Get_Student_Wallet;
        test.Validate_Error_Messages(invalid_data,HttpStatus.SC_NOT_FOUND,"Student wallet not found. Please verify the student ID and try again.",4042);
    }

    @When("Performing the Api of get wallet with student that not authorized")
    public void get_Student_wallet_student_not_auth() {
        Get_Student_Wallet = test.sendRequest("GET", "/students/{student_id}/wallet", null,studentData.student_refreshToken_deleted);
    }

    @Then("I verify the appearance of status code 403 and student not auth")
    public void Validate_Response_of_Get_student_Wallet_unauth_student() {
        Response invalid_data = Get_Student_Wallet;
        test.Validate_Error_Messages(invalid_data,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }

}
