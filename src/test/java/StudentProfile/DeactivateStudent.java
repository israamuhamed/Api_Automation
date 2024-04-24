package StudentProfile;

import EducatorProfile.Educator_TestData;
import TestConfig.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import StudentClasses.Student_TestData;

import java.io.File;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class DeactivateStudent {
    TestBase test = new TestBase();
    Student_TestData studentData = new Student_TestData();
    CreateStudent student = new CreateStudent();
    Educator_TestData data = new Educator_TestData();
    Response deactivate_student;
    Long StudentID;
    String refreshToken;
    public Map<String, Object> pathParams = test.pathParams;

    @When("Performing the Api of deactivate student")
    public void Deactivate_student(){
        student.getStudent_refresh_token();
        refreshToken = student.student_refreshToken;
        System.out.println("token " + refreshToken);
        deactivate_student = test.sendRequest("POST", "/students/{student_id}/deactivate", null,refreshToken);
    }

    @Given("User Send valid student Id to deactivate student")
    public void Sending_valid_StudentId_Deactivate_student() throws SQLException {
        student.Create_Student();
        StudentID = student.studentId;
        pathParams.put("student_id",StudentID);
    }

    @Then("I verify the appearance of status code 200 and student deactivated successfully")
    public void Validate_Response_of_deactivate_student() throws SQLException {
        deactivate_student.prettyPrint();
        deactivate_student.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentProfile/DeactivateStudent.json")))
                .body("message", hasToString("Student account has been successfully deactivated."));
    }

    @Given("User Send Invalid student Id to deactivate student")
    public void Sending_Invalid_StudentId_Deactivate_student()  {
        StudentID = student.studentId;
        pathParams.put("student_id",studentData.student_not_exist);
    }

    @When("Performing the Api of deactivate student with student not exist")
    public void deactivate_student_with_student_not_exist() {
        deactivate_student = test.sendRequest("POST", "/students/{student_id}/deactivate", null,studentData.student_refreshToken_not_exist);
    }

    @Then("I verify the appearance of 404 status code and student is not found")
    public void Validate_Response_of_update_student_Profile_NotFound_student() {
        Response invalid_data = deactivate_student;
        test.Validate_Error_Messages(invalid_data,HttpStatus.SC_NOT_FOUND,"Student with the specified ID does not exist or is not active.",4041);
    }

    @When("Performing the Api of deactivate student with not valid token")
    public void deactivate_student_with_not_valid_token() throws SQLException {
        deactivate_student = test.sendRequest("POST", "/students/{student_id}/deactivate", null,studentData.student_refreshToken_deleted);
    }

    @Then("I verify the appearance of status code 403 and this student is unauthorized")
    public void Validate_Response_of_deactivate_student_Profile_invalid_student() {
        Response invalid_data = deactivate_student;
        test.Validate_Error_Messages(invalid_data,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }

    @Given("User Send special characters in student Id to deactivate student")
    public void deactivate_student_with_invalid_id() throws SQLException {
        StudentID = student.studentId;
        pathParams.put("student_id","123456789045@##");
    }

    @Then("I verify the appearance of status code 400 and the path param is Invalid")
    public void Validate_Response_of_deactivate_student_invalidData() {
        Response invalid_data = deactivate_student;
        test.Validate_Error_Messages(invalid_data,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }

}
