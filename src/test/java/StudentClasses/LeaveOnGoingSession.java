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

import static org.hamcrest.Matchers.hasToString;

public class LeaveOnGoingSession {
    TestBase test = new TestBase();
    Student_TestData data = new Student_TestData();
    String student_token = data.Student_refresh_Token;
    JoinSession joinedSession = new JoinSession();
    Long student_Id = data.student_Id;
    Long class_Id;
    Long educator_id;
    String educator_token;
    Long session_id;
    Response join_session_again;
    Map<String,Object> pathParams = test.pathParams;
    public Response Leave_onGoing_session ;

    @When("Performing the Api leave on going session")
    public void leaveOngoingSession() {
        Leave_onGoing_session =  test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/sessions/{session_id}/leave",null,student_token);
    }
    @Given("user send valid data to leave the joined session")
    public void successful_submission_of_feedback() throws SQLException{
        joinedSession.join_session_In_Enrolled_Class();
        joinedSession.Join_Session();
        class_Id = joinedSession.class_id;
        session_id = joinedSession.session_id;
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_Id);
        pathParams.put("session_id",session_id);
    }
    @Then("The Response should contains status code 200 and message Successfully left the session")
    public void Validate_Response_of_success_submission_feedback (){
        Leave_onGoing_session.prettyPrint();
        Leave_onGoing_session.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/LeaveOnGoingSession.json")))
                .body("student_id" ,hasToString(student_Id.toString()),"session_id",hasToString(session_id.toString()),
                        "message",hasToString("Successfully left the session."));
    }
    @Given("User send session_id that he isn't part of")
    public void send_session_student_not_participate()throws SQLException{
        joinedSession.join_session_In_Enrolled_Class();
        joinedSession.Join_Session();
        class_Id = joinedSession.class_id;
        session_id = joinedSession.session_id;
        educator_id = joinedSession.educator_Id;
        educator_token = joinedSession.EducatorRefreshToken;

        test.sendRequest("POST", "/educators/"+ educator_id +"/sessions/"+ session_id +"/kickout/"+ student_Id +"",null,educator_token);

        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_Id);
        pathParams.put("session_id",session_id);
    }
    @Then("I verify status code 404 and message student is not currently part of the session")
    public void Validate_Response_of_student_is_not_currently_part_of_session (){
        Response Leave_Session = Leave_onGoing_session;
        test.Validate_Error_Messages(Leave_Session,HttpStatus.SC_NOT_FOUND,"Session not found or student is not currently part of the session.",40412);
    }
    @Given("User Send Invalid StudentId to leave session")
    public void unauthorized_student ()throws SQLException {
        joinedSession.join_session_In_Enrolled_Class();
        joinedSession.Join_Session();
        class_Id = joinedSession.class_id;
        session_id = joinedSession.session_id;
        pathParams.put("student_id", "123456789012");
        pathParams.put("class_id", class_Id);
        pathParams.put("session_id", session_id);
    }
    @Then("I verify Status Code 403 And Error Message user unauthorized")
    public void Validate_Response_for_unauthorized_student (){
        Response Leave_Session = Leave_onGoing_session;
        test.Validate_Error_Messages(Leave_Session,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }
    @Given("user send the same sessionId and class_id that he left from")
    public void Join_session_again ()throws SQLException{
        pathParams.put("student_id", student_Id);
        pathParams.put("class_id", class_Id);
        pathParams.put("session_id", session_id);
    }
    @When("Performing the Api of Joining Session Again")
    public void join_session_again(){
        join_session_again =  test.sendRequest("POST", "/students/{student_id}/classes/{class_id}/sessions/{session_id}/join",null,student_token);
        join_session_again.prettyPrint();
    }

}
