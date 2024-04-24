package StudentHomeScreen;

import AdminArea.CreateClass;
import EducatorProfile.Educator_TestData;
import StudentProfile.CreateStudent;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class CancelStudentEnrollment {
    TestBase test = new TestBase();
    CreateClass classData = new CreateClass();
    CreateStudent student = new CreateStudent();
    Database_Connection Connect = new Database_Connection();
    Educator_TestData data = new Educator_TestData();
    Map<String, Object> pathParams = test.pathParams;
    Response Enroll_Student_Into_Class;
    Response Cancel_Student_Enrollment;
    Long Class_ID;
    String student_refreshToken;
    Long class_id;
    Long student_id;
    Long student_Id;

    @Given("Create new user and enroll him into class")
    public void enroll_student_into_class() throws SQLException {
        classData.Create_Class_per_session();
        Class_ID = classData.Class_ID;
        student.Create_Student();
        student_Id = student.studentId;
        pathParams.put("student_id",student_Id);
        pathParams.put("class_id",Class_ID);
        student_refreshToken = student.student_refreshToken;
        Enroll_Student_Into_Class = test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/enroll", null,student_refreshToken);
    }
    @When("Performing the Api of Cancel Enrollment")
    public void Cancel_Enrollment_of_student(){
        pathParams.put("student_id",student_Id);
        pathParams.put("class_id",Class_ID);
        student_refreshToken = student.student_refreshToken;
        Cancel_Student_Enrollment = test.sendRequest("DELETE", "/students/{student_id}/enrollments/{class_id}", null,student_refreshToken);
    }
    @Then("I verify the appearance of status code 200 and Enrollment canceled successfully")
    public void Validate_Response_cancel_enrollment(){
        Cancel_Student_Enrollment.prettyPrint();
        Cancel_Student_Enrollment.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentHomeScreen/CancelStudentEnrollment.json")))
                .body("message", hasToString("Enrollment successfully canceled."));
    }

    @Given("User Send Valid student Id and invalid class Id to cancel student enrollment")
    public void create_student_and_invalid_class_cancel_enrollment ()throws SQLException {
        student.Create_Student();
        student_Id = student.studentId;
        pathParams.put("student_id",student_Id);
        pathParams.put("class_id","123456789012");
        student_refreshToken = student.student_refreshToken;
        Cancel_Student_Enrollment = test.sendRequest("DELETE", "/students/{student_id}/enrollments/{class_id}", null,student_refreshToken);
    }

    @Then("I verify the appearance of status code 400 and class not exist for cancellation")
    public void Validate_Response_enroll_student_into_invalid_class(){
        Response Invalid_class = Cancel_Student_Enrollment;
        test.Validate_Error_Messages(Invalid_class,HttpStatus.SC_NOT_FOUND,"Student is not enrolled in the specified class.",4044);
    }

    @Given("User Send InValid student Id and invalid class Id to cancel student enrollment")
    public void Invalid_student_and_invalid_class_cancel_enrollment (){
        pathParams.put("student_id","studentId");
        pathParams.put("class_id","classId");
        Cancel_Student_Enrollment = test.sendRequest("DELETE", "/students/{student_id}/enrollments/{class_id}", null,data.Admin_Token);
    }

    @Then("I verify the appearance of status code 400 and class or student is not correct")
    public void Validate_Response_enroll_InvalidStudent_into_invalid_class(){
        Response Invalid_class = Cancel_Student_Enrollment;
        test.Validate_Error_Messages(Invalid_class,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }

    @Given("User Send invalid token to cancel student enrollment")
    public void Invalid_token_cancel_enrollment ()throws SQLException {
        pathParams.put("student_id","123456789111");
        pathParams.put("class_id","123456789222");
        Cancel_Student_Enrollment = test.sendRequest("DELETE", "/students/{student_id}/enrollments/{class_id}", null,data.Admin_Token);
    }
    @Then("I verify the appearance of status code 403 and token is not correct")
    public void Validate_Response_cancel_enrollment_invalidToken(){
        Response Invalid_token = Cancel_Student_Enrollment;
        test.Validate_Error_Messages(Invalid_token,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }

}
