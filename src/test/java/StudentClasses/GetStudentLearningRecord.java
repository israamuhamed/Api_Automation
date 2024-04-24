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
import static org.hamcrest.Matchers.hasToString;

public class GetStudentLearningRecord {
    TestBase test = new TestBase();
    Student_TestData data = new Student_TestData();
    Long student_id = data.student_Id;
    String user_token = data.Student_refresh_Token;
    MarkLearningRecordAsDeleted DeletedRecord = new MarkLearningRecordAsDeleted();
    Database_Connection connect = new Database_Connection();
    GetDownloadURLsForEducationalResources resources = new GetDownloadURLsForEducationalResources();
    Long class_id;
    Long session_id;
    Long resource_id;
    String student_learning_record_md5;
    String student_learning_record_metadata;
    String student_learning_record;
    Integer student_learning_record_id;
    Integer session_educational_resource_id;
    Map<String,Object> PathParams = test.pathParams;
    public Response Get_Student_Learning_Record;

    public void get_learning_Record_of_student_from_db()throws SQLException {

        ResultSet result = connect.connect_to_database("SELECT *\n" +
                "FROM student_learning_records\n" +
                "WHERE student_id = "+ student_id +"\n" +
                "ORDER BY student_learning_record_created_at DESC\n" +
                "LIMIT 1;");

        while(result.next()){
            student_learning_record_id= result.getInt("student_learning_record_id");
            session_educational_resource_id= result.getInt("session_educational_resource_id");
            student_learning_record_md5 = result.getString("student_learning_record_md5");
            student_learning_record = result.getString("student_learning_record");
            student_learning_record_metadata = result.getString("student_learning_record_metadata");
        }
    }
    @When("Performing The API Of Get Student Learning Record")
    public void get_student_learning_record(){
        Get_Student_Learning_Record = test.sendRequest("GET" , "/students/{student_id}/classes/{class_id}/sessions/{session_id}/resources/{resource_id}/record" , null,user_token);
    }
    @Given("User Send Valid Parameters To Get Student Learning Records API")
    public void send_valid_data() throws SQLException, InterruptedException {
        resources.joinSessionToDownloadResources();
        class_id = resources.class_id;
        session_id = resources.session_id;
        resource_id = resources.resource_id;
        String request_body = "{\"student_learning_record\":\"{\\\"Submit\\\":\\\"studentLearningRecord\\\"}\",\"student_learning_record_metadata\":\"{\\\"Updated\\\":\\\"StudentLearningRecord\\\"}\"}";
        test.sendRequest("POST","/students/"+ student_id +"/classes/"+ class_id +"/sessions/"+ session_id +"/resources/"+ resource_id +"/record",request_body,user_token);
        PathParams.put("student_id", student_id);
        PathParams.put("class_id",class_id);
        PathParams.put("session_id",session_id);
        PathParams.put("resource_id",resource_id);
    }
    @Then("Response Status Code Is 200 And Response Body Contains Student Learning Record")
    public void validate_get_learning_record_response() throws SQLException {
        get_learning_Record_of_student_from_db();
        Get_Student_Learning_Record.prettyPrint();
        Get_Student_Learning_Record.then()
                .statusCode(HttpStatus.SC_OK)
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/GetStudentLearningRecord.json")))
                .body("student_learning_record_id",equalTo(student_learning_record_id),"session_educational_resource_id", equalTo(session_educational_resource_id),
                "student_learning_record_md5",hasToString(student_learning_record_md5),"student_learning_record",hasToString(student_learning_record),
                       "student_learning_record_metadata",hasToString(student_learning_record_metadata) );
    }
    @Given("User Send Invalid UserId To Get Student Learning Record API")
    public void get_learning_Record_unAuthorized_student() throws SQLException, InterruptedException {
        resources.joinSessionToDownloadResources();
        class_id = resources.class_id;
        session_id = resources.session_id;
        resource_id = resources.resource_id;

        PathParams.put("student_id", "123456654321");
        PathParams.put("class_id",class_id);
        PathParams.put("session_id",session_id);
        PathParams.put("resource_id",resource_id);
    }
    @Then("Response Status Code Is 403 And Body Returns Withe Error Message With No Learning Records")
    public void validate_unauthorized_response(){
        test.Validate_Error_Messages(Get_Student_Learning_Record ,HttpStatus.SC_FORBIDDEN , "Unauthorized" , 4031 );
    }
    @Given("User Send Invalid SessionId To Get Student Learning Record API")
    public void got_no_access_to_session() throws SQLException, InterruptedException {
        resources.joinSessionToDownloadResources();
        class_id = resources.class_id;
        session_id = resources.session_id;
        resource_id = resources.resource_id;

        PathParams.put("student_id", student_id);
        PathParams.put("class_id",class_id);
        PathParams.put("session_id","132123456765");
        PathParams.put("resource_id",resource_id);
    }
    @Then("Response Status Code Is 404 And Body Contains Have No Access To Session Error Message")
    public void validate_got_no_access_to_session_response(){
        test.Validate_Error_Messages(Get_Student_Learning_Record , HttpStatus.SC_FORBIDDEN , "Student does not have access to the resources of the requested session or class" ,4036);
    }
    @Given("User Send Archived ClassId To Get Student Learning Record API")
    public void class_not_found() throws SQLException, InterruptedException {
        resources.joinSessionToDownloadResources();
        session_id = resources.session_id;
        resource_id = resources.resource_id;

        PathParams.put("student_id", student_id);
        PathParams.put("class_id",data.Archived_Class);
        PathParams.put("session_id",session_id);
        PathParams.put("resource_id",resource_id);
    }
    @Then("Response Status Code Is 404 And Body Contains Class Not Found")
    public void validate_class_not_found_response(){
        test.Validate_Error_Messages(Get_Student_Learning_Record , HttpStatus.SC_NOT_FOUND ,"Class not found or not eligible for display", 4046 );
    }
    @Given("User send parameters of deleted Learning Record")
    public void learning_record_not_found() throws SQLException, InterruptedException {
        DeletedRecord.delete_student_learning_record();
        DeletedRecord.perform_mark_learning_record_as_deleted();
        session_id = DeletedRecord.session_id;
        class_id = DeletedRecord.class_id;
        resource_id = DeletedRecord.resource_id;

        PathParams.put("student_id", student_id);
        PathParams.put("class_id",class_id);
        PathParams.put("session_id",session_id);
        PathParams.put("resource_id",resource_id);
    }
    @Then("Response Status Code Is 404 And Body Have Deleted Learning Record Message")
    public void validate_deleted_learning_record_response(){
        test.Validate_Error_Messages(Get_Student_Learning_Record , HttpStatus.SC_NOT_FOUND , "Learning record not found or has been deleted" , 40411);
    }
}
