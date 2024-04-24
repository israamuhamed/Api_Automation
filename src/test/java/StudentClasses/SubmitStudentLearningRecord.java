package StudentClasses;

import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

public class SubmitStudentLearningRecord {
    TestBase test = new TestBase();
    Student_TestData data = new Student_TestData();
    GetDownloadURLsForEducationalResources resources = new GetDownloadURLsForEducationalResources();
    Database_Connection connect = new Database_Connection();
    Long student_id = data.student_Id;
    public Long class_id;
    public Long session_id;
    String invalid_request_body = null;
    String student_Token = data.Student_refresh_Token;
    public Long resource_id;
    Integer student_learning_record_id;
    Map<String,Object> PathParams = test.pathParams;
    Response Submit_Student_Learning_Record;

    public void Get_Student_Learning_Records_From_DB () throws SQLException {
        ResultSet result = connect.connect_to_database("select MAX(student_learning_record_id) as student_learning_record_id from student_learning_records slr where student_id ="+ student_id  +" ");
        while(result.next()){
            student_learning_record_id= result.getInt("student_learning_record_id");
        }
    }
    @When("Performing The API Of Submit Student Learning Record")
    public Integer submit_student_learning_Record_request(){
        String request_body = "{\"student_learning_record\":\"{\\\"Submit\\\":\\\"studentLearningRecord\\\"}\",\"student_learning_record_metadata\":\"{\\\"Updated\\\":\\\"StudentLearningRecord\\\"}\"}";
        Submit_Student_Learning_Record = test.sendRequest("POST","/students/{student_id}/classes/{class_id}/sessions/{session_id}/resources/{resource_id}/record",request_body,student_Token);
        return student_learning_record_id = Submit_Student_Learning_Record.then().extract().path("student_learning_record_id");
    }
    @Given("User Send Valid Data To Submit Student Learning Record Request")
    public void Send_valid_param() throws SQLException, InterruptedException {
        resources.joinSessionToDownloadResources();
        class_id = resources.class_id;
        session_id = resources.session_id;
        resource_id = resources.resource_id;
        System.out.println("student_id "+student_id+" class_id "+class_id+" session_id "+session_id+" resource_id "+resource_id);

        PathParams.put("student_id", student_id);
        PathParams.put("class_id",class_id);
        PathParams.put("session_id",session_id);
        PathParams.put("resource_id",resource_id);
    }
    @Then("Response Status Code Is 200 And Body Have StudentLearning RecordId")
    public void validate_student_learning_Record_response()throws SQLException{
        Get_Student_Learning_Records_From_DB();
        Submit_Student_Learning_Record.prettyPrint();
        Submit_Student_Learning_Record.then().assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/SubmitStudentLearningRecord.json")))
                .body("student_learning_record_id",equalTo(student_learning_record_id));
    }
    @Given("User Send Invalid UserId To SubmitStudentLearningRecord Request")
    public void invalid_user_submit_student_learning_record() throws SQLException, InterruptedException {
        resources.joinSessionToDownloadResources();
        class_id = resources.class_id;
        session_id = resources.session_id;
        resource_id = resources.resource_id;

        PathParams.put("student_id", "109876547890");
        PathParams.put("class_id",class_id);
        PathParams.put("session_id",session_id);
        PathParams.put("resource_id",resource_id);
    }
    @Then("Response Status Code Of SubmitStudentLearningRecord Is 403 and Body Have Error Message")
    public void unauthorized_student_in_submit_learning_record(){
        test.Validate_Error_Messages(Submit_Student_Learning_Record, HttpStatus.SC_FORBIDDEN , "Unauthorized",4031);
    }
    @Given("User Send Invalid SessionId To SubmitStudentLearningRecord Request")
    public void invalid_session_id() throws SQLException, InterruptedException {
        resources.joinSessionToDownloadResources();
        class_id = resources.class_id;
        session_id = resources.session_id;
        resource_id = resources.resource_id;

        PathParams.put("student_id", student_id);
        PathParams.put("class_id",class_id);
        PathParams.put("session_id","123456765432");
        PathParams.put("resource_id",resource_id);
    }
    @Then("Response Status Code Is 403 And Error Message Contains No Access To Session")
    public void validate_invalid_session_id(){
        test.Validate_Error_Messages(Submit_Student_Learning_Record , HttpStatus.SC_FORBIDDEN ,"Unauthorized access. Student does not have access to the resources of the requested session or class", 4036 );
    }
    @Given("User Send Valid Parameters And Empty Body")
    public void send_invalid_body() throws SQLException, InterruptedException {
        resources.joinSessionToDownloadResources();
        class_id = resources.class_id;
        session_id = resources.session_id;
        resource_id = resources.resource_id;

        PathParams.put("student_id", student_id);
        PathParams.put("class_id",class_id);
        PathParams.put("session_id",session_id);
        PathParams.put("resource_id",resource_id);
    }
    @When("Performing The API With Empty Body")
    public void Execute_Request_With_Empty_Body(){
        Submit_Student_Learning_Record = test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/sessions/{session_id}/resources/{resource_id}/record\n" +
                "\n" ,invalid_request_body,student_Token );
    }
    @Then("Response Code Is 400 And Body Have Clear Error Message")
    public void Validate_invalid_body_Request(){
        test.Validate_Error_Messages(Submit_Student_Learning_Record, HttpStatus.SC_BAD_REQUEST , "Invalid request. Please check the path parameters and request context for accuracy", 4002);
    }
}
