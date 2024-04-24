package EducatorsSessionsActions;

import EducatorProfile.Educator_TestData;
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

public class EndSession {
    TestBase test = new TestBase();
    StartSession session = new StartSession();
    Educator_TestData data = new Educator_TestData();
    Map<String, Object> pathParams = test.pathParams;
    Long educator_Id;
    Response End_Session;
    String EducatorRefreshToken;
    Long session_Id;
    Response Start_Session;


    @Given("User Start new session")
    public void educator_start_session () throws SQLException, InterruptedException {
       session.Create_Session_for_educator ();
       session.Start_Session();
       session_Id = session.session_id;
       educator_Id = session.educatorID;
    }

    @When("Performing the Api of End Session")
    public Long End_Session() {
        EducatorRefreshToken = session.EducatorRefreshToken;
        pathParams.put("educator_id",educator_Id);
        pathParams.put("session_id",session_Id);
        System.out.println("session for end: " +session_Id + "educator to end: " + educator_Id);
        End_Session = test.sendRequest("POST", "/educators/{educator_id}/sessions/{session_id}/end", null,EducatorRefreshToken);
        End_Session.prettyPrint();
        return session_Id = End_Session.then().extract().path("session_id");
    }

    @Then("I verify the appearance of status code 200 and session Ended successfully")
    public void Validate_Response_of_end_session_successfully() {
        End_Session.prettyPrint();
        End_Session.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/EducatorsSessionsActions/EndSession.json")))
                .body("session_id",equalTo(session_Id),"message",hasToString("Session ended successfully."));
    }

    @When("Performing the Api of End Session with invalid token")
    public void End_Session_invalid_token() {
        pathParams.put("educator_id",educator_Id);
        pathParams.put("session_id",session_Id);
        End_Session = test.sendRequest("POST", "/educators/{educator_id}/sessions/{session_id}/end", null,data.Admin_Token);
    }

    @Then("I verify the appearance of status code 403 and educator is unauthorized")
    public void Validate_Response_end_session_with_invalid_token() {
        Response Invalid_token = End_Session;
        test.Validate_Error_Messages(Invalid_token,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);

    }

    @When("Performing the Api of End Session with session not exist")
    public void End_Session_session_not_exist() {
        EducatorRefreshToken = session.EducatorRefreshToken;
        pathParams.put("educator_id",educator_Id);
        pathParams.put("session_id","123456789012");
        End_Session = test.sendRequest("POST", "/educators/{educator_id}/sessions/{session_id}/end", null,EducatorRefreshToken);
    }

    @Then("I verify the appearance of status code 404 and session is found to end")
    public void Validate_Response_end_session_with_session_not_exist() {
        Response Invalid_session = End_Session;
        test.Validate_Error_Messages(Invalid_session,HttpStatus.SC_NOT_FOUND,"Session not found or not eligible for display.",4048);

    }

    @When("Performing the Api of Start Session with ended session")
    public void Start_Session_with_ended_session() throws SQLException, InterruptedException {
        educator_start_session ();
        End_Session();
        pathParams.put("educator_id", educator_Id);
        pathParams.put("session_id",session_Id);
        Thread.sleep(4000);
        Start_Session = test.sendRequest("POST", "/educators/{educator_id}/sessions/{session_id}/start", null,EducatorRefreshToken);
    }

    @Then("I verify the appearance of status code 404 and session is ended")
    public void Validate_Response_start_session_with_EndedSession() {
        Response ended_session = Start_Session;
        test.Validate_Error_Messages(ended_session,HttpStatus.SC_UNPROCESSABLE_ENTITY,"Cannot start the session. session ended.",42210);

    }



}
