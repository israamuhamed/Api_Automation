package AdminArea;

import EducatorProfile.Educator_TestData;
import StudentProfile.CreateStudent;
import TestConfig.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.sql.SQLException;
import java.util.Map;

public class DeactivateStudentAccount {
    TestBase test = new TestBase();
    CreateStudent student = new CreateStudent();
    Educator_TestData data = new Educator_TestData();
    Response deactivate_student;
    Long Student_not_exist = 123456789012L;
    public Map<String, Object> pathParams = test.pathParams;

    @Given("Admin Send Valid Student Id In Request Parameter")
    public void send_valid_student_id() throws SQLException {
        student.Create_Student();
        pathParams.put("student_id",student.studentId);
    }
    @When("Performing The API Of Deactivate Student")
    public void perform_deactivate_student(){
        deactivate_student =  test.sendRequest("POST","/admin/students/{student_id}/deactivate",null,data.Admin_Token);
    }
    @Then("Response code of Deactivate account API is 200 and body returns with success message")
    public void validate_deactivate_request_valid_request(){
        deactivate_student.prettyPrint();
        deactivate_student.then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body("message",containsString("Student account has been successfully deactivated."))
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/DeactivateStudentAccount.json")));
    }
    @Given("Admin leave StudentId Empty in request parameters")
    public void send_empty_value_in_student_id(){
        pathParams.put("student_id"," ");
    }
    @Then("Response code of Deactivate account API is 400 and body returns with error message")
    public void validate_response_of_empty_student_id(){
       test.Validate_Error_Messages(deactivate_student,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy",4002);
    }
    @When("Performing The API Of Deactivate Student With Invalid Token")
    public void send_invalid_token_in_deactivate_account(){
        deactivate_student =  test.sendRequest("POST","/admin/students/{student_id}/deactivate",null,data.refresh_token);
    }
    @Then("Response code of Deactivate account API is 403 and body returns with error message")
    public void validate_response_of_invalid_token_request(){
        test.Validate_Error_Messages(deactivate_student,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }
    @Given("Admin Enter StudentId That Doesn't Exist")
    public void send_student_not_exist_to_Deactivate_account(){
        pathParams.put("student_id",Student_not_exist);
    }
    @Then("Response code of Deactivate account API is 404 and body returns with error message")
    public void validate_not_Exist_student_response(){
        test.Validate_Error_Messages(deactivate_student,HttpStatus.SC_NOT_FOUND,"Student with the specified ID does not exist or is not active",4041);
    }
}
