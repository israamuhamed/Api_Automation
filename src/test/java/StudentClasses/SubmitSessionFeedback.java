package StudentClasses;

import EducatorsSessionsActions.KickOutStudent;
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

public class SubmitSessionFeedback {
    TestBase test = new TestBase();
    Student_TestData data = new Student_TestData();
    JoinSession joinedSession =new JoinSession();
    KickOutStudent student_KickedOut = new KickOutStudent();
    String student_token = data.Student_refresh_Token;
    Long student_Id = data.student_Id;
    Long class_Id;
    Long session_id;
    Map<String,Object> pathParams = test.pathParams;
    public Response submit_session_feedback ;
    String valid_request_body = "{\"session_feedback\":1}";
    String Invalid_request_body = "{\"session_feedback\":5}";


    @Given("User Send feedback for session")
    public void successful_submission_of_feedback()throws SQLException{
        joinedSession.join_session_In_Enrolled_Class();
        joinedSession.Join_Session();
        class_Id = joinedSession.class_id;
        session_id = joinedSession.session_id;
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_Id);
        pathParams.put("session_id",session_id);
    }

    @When("Performing the Api of submit session feedback with valid score")
    public void submit_session_feedback() {
        submit_session_feedback =  test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/sessions/{session_id}/feedback",valid_request_body,student_token);
    }

    @Then("The Response should contains status code 200 and message Feedback successfully submitted")
    public void Validate_Response_of_success_submission_feedback (){
        submit_session_feedback.prettyPrint();
        submit_session_feedback.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/submitSessionFeedback.json")))
                .body("feedback_score" ,  equalTo(1),"session_id",hasToString(session_id.toString()),"message",hasToString("Feedback successfully submitted."));
    }

    @When("Performing the Api of submit session feedback with invalid score")
    public void submit_session_feedback_With_invalid_score() {
        submit_session_feedback =  test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/sessions/{session_id}/feedback",Invalid_request_body,student_token);
    }

    @Then("The Response should contains status code 400 and message Invalid feedback score")
    public void Validate_Response_Invalid_submission_feedback (){
        Response submit_feedback = submit_session_feedback;
        test.Validate_Error_Messages(submit_feedback,HttpStatus.SC_BAD_REQUEST,"Invalid feedback score. Score must be 0 or 1.",4005);
    }

    @Given("User Send Invalid StudentId to submit feedback")
    public void unauthorized_student ()throws SQLException{
        joinedSession.join_session_In_Enrolled_Class();
        joinedSession.Join_Session();
        class_Id = joinedSession.class_id;
        session_id = joinedSession.session_id;
        pathParams.put("student_id", "123456789987");
        pathParams.put("class_id", class_Id);
        pathParams.put("session_id", session_id);
    }

    @Then("The Response submit feedback  with invalid user Should Contain Status Code 403 And Error Message Unauthorized")
    public void Validate_Response_for_unauthorized_student (){
        Response submit_feedback = submit_session_feedback;
        test.Validate_Error_Messages(submit_feedback,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }

    @Given("User Send studentId kicked out from session to submit feedback")
    public void kickedOut_Student_submit_feedback () throws SQLException{
        student_KickedOut.student_join_session();
        student_KickedOut.Kick_Out();
        class_Id = student_KickedOut.class_id;
        session_id = student_KickedOut.session_id;
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_Id);
        pathParams.put("session_id", session_id);
    }

    @Then("The Response of invalid user Should Contain Status Code 403 And Error Message Unauthorized access")
    public void Validate_Response_kickedOut_Student_submit_feedback (){
        Response submit_feedback = submit_session_feedback;
        test.Validate_Error_Messages(submit_feedback,HttpStatus.SC_FORBIDDEN,"Unauthorized access. Student has been kicked-out from this session.",4039);
    }

    @Given("User Send studentId not participate into session to submit feedback")
    public void notParticipate_Student_submit_feedback()throws SQLException {
        joinedSession.Ended_Session ();
        class_Id = joinedSession.class_id;
        session_id = joinedSession.session_id;
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_Id);
        pathParams.put("session_id", session_id);

    }
    @Then("The Response of not participate student Should Contain Status Code 403 And Error Message Unauthorized access")
    public void Validate_Response_notParticipate_Student_submit_feedback (){
        Response submit_feedback = submit_session_feedback;
        test.Validate_Error_Messages(submit_feedback,HttpStatus.SC_FORBIDDEN,"Unauthorized access. Student did not participate in this session.",4037);
    }

}
