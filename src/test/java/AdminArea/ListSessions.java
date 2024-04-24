package AdminArea;

import EducatorProfile.Educator_TestData;
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

public class ListSessions {
    TestBase test = new TestBase();
    Educator_TestData data = new Educator_TestData();
    CreateSession session_data = new CreateSession();
    Response List_Sessions;
    public Long session_id;

    @Given("User Create New Session")
    public void create_new_session()throws SQLException {
        session_data.Create_Session();
    }
    @When("Performing The API of ListSessions")
    public void List_sessions(){
        List_Sessions = test.sendRequest("GET", "/admin/sessions?session-id="+ session_data.sessionId, null , data.Admin_Token);
    }
    @Then("The Session should return in response body")
    public void validate_list_session_response(){
        List_Sessions.then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("data.session_id",hasItem(session_data.sessionId))
                .body("data.session_title",hasItem(session_data.sessionTitle))
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/ListSessions.json")));
    }
    @When("Performing the API of ListSessions With Invalid Token")
    public void List_sessions_With_invalid_token(){
        List_Sessions = test.sendRequest("GET", "/admin/sessions?session-id="+ session_data.sessionId, null , data.refresh_token);
    }
    @Then("Response Code of ListSessions Is 403 And Body Returns With Error Message")
    public void List_sessions_unauthorized_Response(){
        test.Validate_Error_Messages(List_Sessions,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }
    @Given("User Send Invalid Session_id In Request Params")
    public void define_invalid_session_id(){
        session_id= 123456789000L;
    }
    @When("Performing The API of ListSessions With Invalid Params")
    public void List_sessions_with_invalid_parameter(){
        List_Sessions = test.sendRequest("GET", "/admin/sessions?session-id="+ session_id, null , data.Admin_Token);
    }
    @Then("Response Code Of ListSessions Is 404 and body returns with error message")
    public void invalid_paramter_validate_response(){
        test.Validate_Error_Messages(List_Sessions,HttpStatus.SC_NOT_FOUND,"Session not found or not eligible for display",4048);
    }
    @Given("User Perform The Api Of ListSessions With Empty Session_ID")
    public void List_sessions_invalid_request(){
        List_Sessions = test.sendRequest("GET", "/admin/sessions?session-id=", null , data.refresh_token);
    }
    @Then("Response Code Of ListSessions Is 400 and body Returns With Error Message")
    public void Validate_ListSessions_invalid_request_response(){
        test.Validate_Error_Messages(List_Sessions,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy",4002);
    }
}
