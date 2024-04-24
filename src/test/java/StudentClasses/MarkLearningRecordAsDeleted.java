package StudentClasses;

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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class MarkLearningRecordAsDeleted {
    TestBase test = new TestBase();
    Student_TestData data = new Student_TestData();
    Long student_id = data.student_Id;
    Long class_id ;
    public Long resource_id;
    Long session_id;
    String refresh_Token =data.Student_refresh_Token;
    SubmitStudentLearningRecord submit = new SubmitStudentLearningRecord();
    Integer record_id;
    Map<String, Object> PathParams = test.pathParams;
    Response Mark_Student_Learning_Record_As_Deleted;

    @When("Performing The API Of Mark Learning Record As Deleted API")
    public void perform_mark_learning_record_as_deleted() {
        Mark_Student_Learning_Record_As_Deleted = test.sendRequest("DELETE", "/students/{student_id}/classes/{class_id}/sessions/{session_id}/records/{record_id}", null,refresh_Token);
    }
    @Given("User Send Valid Data To MarkLearningRecordAsDeleted API")
    public void delete_student_learning_record() throws SQLException, InterruptedException {
        submit.Send_valid_param();
        submit.submit_student_learning_Record_request();
        class_id = submit.class_id;
        session_id = submit.session_id;
        resource_id = submit.resource_id;
        record_id = submit.student_learning_record_id;

        PathParams.put("student_id", student_id);
        PathParams.put("class_id", class_id);
        PathParams.put("session_id", session_id);
        PathParams.put("record_id", record_id);
    }
    @Then("Response Status Code Is 200 And Body Contains Learning Record Is Deleted Successfully")
    public void validate_mark_learning_Record_response() {
        Mark_Student_Learning_Record_As_Deleted.prettyPrint();
        Mark_Student_Learning_Record_As_Deleted.then()
                .statusCode(HttpStatus.SC_OK)
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/MarkLearningRecordAsDeleted.json")))
                .body("message", containsString("Learning record successfully marked as deleted"))
                .body("record_id", equalTo((record_id)));
    }
    @Given("User Send Invalid Student Id To Mark Learning Record Request")
    public void mark_learning_record_unAuthorizedStudent() throws SQLException, InterruptedException {
        submit.Send_valid_param();
        submit.submit_student_learning_Record_request();
        class_id = submit.class_id;
        session_id = submit.session_id;
        record_id = submit.student_learning_record_id;

        PathParams.put("student_id", "134565433123");
        PathParams.put("class_id", class_id);
        PathParams.put("session_id", session_id);
        PathParams.put("record_id", record_id);
    }
    @Then("Status Code Of Mark Request Is 403 And Body Have Error Message")
    public void validate_mark_learning_Record_unauthorized() {
        test.Validate_Error_Messages(Mark_Student_Learning_Record_As_Deleted, HttpStatus.SC_FORBIDDEN, "Unauthorized", 4031);
    }
    @Given("User Send Archived ClassId To Mark Learning Record As Deleted API")
    public void mark_learning_Record_invalid_class() throws SQLException, InterruptedException {
        submit.Send_valid_param();
        submit.submit_student_learning_Record_request();
        class_id = submit.class_id;
        session_id = submit.session_id;
        record_id = submit.student_learning_record_id;

        PathParams.put("student_id",student_id);
        PathParams.put("class_id", data.Archived_Class);
        PathParams.put("session_id",session_id);
        PathParams.put("record_id", record_id);
    }
    @Then("Response Code is 404 And Body Have Class Not Found Message")
    public void validate_mark_learning_record_invalid_classId() {
        test.Validate_Error_Messages(Mark_Student_Learning_Record_As_Deleted, HttpStatus.SC_NOT_FOUND, "Class not found or not eligible for display", 4046);
    }
    @Then("Response Code Is 404 And Body Have Learning Record Is Deleted Message")
    public void validate_deleted_learning_record() {
        test.Validate_Error_Messages(Mark_Student_Learning_Record_As_Deleted, HttpStatus.SC_NOT_FOUND, "Learning record not found or has been deleted.", 40411);
    }
}
