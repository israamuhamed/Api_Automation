package StudentProfile;

import EducatorProfile.Educator_TestData;
import StudentClasses.Student_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.And;
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

import static org.hamcrest.Matchers.hasToString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteStudent {
    TestBase test = new TestBase();
    Student_TestData studentData = new Student_TestData();
    CreateStudent student = new CreateStudent();
    Database_Connection Connect = new Database_Connection();
    Educator_TestData data = new Educator_TestData();
    Response delete_student;
    Long StudentID;
    String refreshToken;
    public Map<String, Object> pathParams = test.pathParams;

    @When("Performing the Api of delete student")
    public void Delete_student(){
        student.getStudent_refresh_token();
        refreshToken = student.student_refreshToken;
        System.out.println("token " + refreshToken);
        delete_student = test.sendRequest("DELETE", "/students/{student_id}", null,refreshToken);
    }

    @Given("User Send valid student Id to delete student")
    public void Sending_valid_StudentId_Delete_student() throws SQLException {
        student.Create_Student();
        StudentID = student.studentId;
        pathParams.put("student_id",StudentID);
    }

    @Then("I verify the appearance of status code 200 and student deleted successfully")
    public void Validate_Response_of_deactivate_student() throws SQLException {
        delete_student.prettyPrint();
        delete_student.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentProfile/DeleteStudent.json")))
                .body("message", hasToString("Student account deleted successfully."));
    }

    @And("make sure that the student deleted from database")
    public void get_data_of_deleted_student_from_database() throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select student_is_deleted,student_first_name,student_last_name,student_email,student_mobile_number,\n" +
                "student_apple_account_id,student_facebook_account_id,student_google_account_id \n" +
                "from students s where student_id ="+ StudentID +"");

        while (resultSet.next()) {
            String studentFirstName = resultSet.getString("student_first_name");
            String studentLastName = resultSet.getString("student_last_name");
            String studentEmail = resultSet.getString("student_email");
            String student_mobile_number = resultSet.getString("student_mobile_number");
            String student_apple_account_id = resultSet.getString("student_apple_account_id");
            String student_facebook_account_id = resultSet.getString("student_facebook_account_id");
            String student_google_account_id = resultSet.getString("student_google_account_id");
            boolean student_is_deleted = resultSet.getBoolean("student_is_deleted");
            assertEquals(studentFirstName,null);
            assertEquals(studentLastName,null);
            assertEquals(studentEmail,null);
            assertEquals(student_mobile_number,null);
            assertEquals(student_apple_account_id,null);
            assertEquals(student_facebook_account_id,null);
            assertEquals(student_google_account_id,null);
            assertEquals(student_is_deleted,true);
        }
    }

    @Given("User Send Invalid student Id to delete student")
    public void Sending_Invalid_StudentId_Delete_student()  {
        StudentID = student.studentId;
        pathParams.put("student_id",studentData.student_not_exist);
    }

    @When("Performing the Api of delete student with student not exist")
    public void delete_student_with_student_not_exist() throws SQLException {
        delete_student = test.sendRequest("DELETE", "/students/{student_id}", null,studentData.student_refreshToken_not_exist);
    }

    @Then("I verify the appearance of 404 status code and student id is not found")
    public void Validate_Response_of_update_student_Profile_NotFound_student() {
        Response invalid_data = delete_student;
        test.Validate_Error_Messages(invalid_data,HttpStatus.SC_NOT_FOUND,"Student with the specified ID does not exist or is not active.",4041);
    }

    @When("Performing the Api of delete student with not valid token")
    public void delete_student_with_not_valid_token() throws SQLException {
        delete_student = test.sendRequest("DELETE", "/students/{student_id}", null,data.refresh_token_for_deletedEducator);
    }

    @Then("I verify the appearance of status code 403 and this student account is unauthorized")
    public void Validate_Response_of_delete_student_Profile_invalid_student() {
        Response invalid_data = delete_student;
        test.Validate_Error_Messages(invalid_data,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }

    @Given("User Send special characters in student Id to delete account")
    public void delete_student_with_invalid_id() {
        StudentID = student.studentId;
        pathParams.put("student_id","123456789045@##");
    }

    @Then("I verify the appearance of status code 400 and the student_id is Invalid")
    public void Validate_Response_of_delete_student_invalidData() {
        Response invalid_data = delete_student;
        test.Validate_Error_Messages(invalid_data,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }
}
