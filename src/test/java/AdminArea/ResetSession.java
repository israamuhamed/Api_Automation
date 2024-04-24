package AdminArea;

import EducatorProfile.Educator_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.sql.ResultSet;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.sql.SQLException;
import java.util.Map;
import static org.hamcrest.Matchers.*;

public class ResetSession {
    TestBase test = new TestBase();
    Educator_TestData data = new Educator_TestData();
    CreateSession session_Data = new CreateSession();
    Database_Connection connect = new Database_Connection();
    Long invalid_session_id;
    Long not_exist_SessionId;
    String session_start_date;
    String session_end_data;
    Response reset_session;
    public Map<String, Object> pathParams = test.pathParams;

    String valid_body = "{\"session_start_date\":\"2026-05-01T18:00:00Z\"," +
                        "\"session_end_date\":\"2026-05-01T20:00:00Z\"}";

   @Given("User Send Valid SessionId And Body To The API")
    public void send_valid_data()throws SQLException {
       session_Data.Create_Session();
       pathParams.put("session_id",session_Data.sessionId);
   }
   @When("Performing The API of Reset Session")
    public void rest_session_request(){
       reset_session = test.sendRequest("POST","/admin/sessions/{session_id}/reset",valid_body,data.Admin_Token);
   }
   @Then("Response code of reset Session is 200 and body returns with success message and session dates is changed successfully")
    public void validate_reset_session_successfully()throws SQLException{
       reset_session.prettyPrint();

       ResultSet resultSet = connect.connect_to_database("select * from public.sessions s where s.session_id = "+session_Data.sessionId);
       while (resultSet.next()){
           session_start_date = resultSet.getString("session_start_date").replace(" ","T").concat("Z");
           session_end_data = resultSet.getString("session_end_date").replace(" ","T").concat("Z");
       }
       System.out.println(session_end_data+ " " +session_start_date);

       reset_session.then().assertThat()
                 .statusCode(HttpStatus.SC_OK)
                 .body("session_id",equalTo(session_Data.sessionId))
                 .body("message",containsString("Session reset successfully"));
       assertTrue(valid_body.contains(session_start_date));
       assertTrue(valid_body.contains(session_end_data));
   }
   @Given("User Send Invalid Session Id And Valid Body To The API")
    public void send_invalid_session_id(){
       invalid_session_id = 123456789L;
       pathParams.put("session_id",invalid_session_id);
   }
   @Then("Response code of reset session is 400 and body returns with error message")
    public void validate_invalid_sessionId_response(){
       test.Validate_Error_Messages(reset_session,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy",4002);
   }
   @When("Performing The API of Reset Session With Invalid Token")
    public void reset_session_with_invalid_toke(){
       reset_session = test.sendRequest("POST","/admin/sessions/{session_id}/reset",valid_body,data.refresh_token);
   }
   @Then("Response code of reset session is 403 and body returns with error message")
    public void validate_invalid_token_response(){
       test.Validate_Error_Messages(reset_session,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
   }
    @Given("User Send SessionId that doesn't exist And Valid Body To The API")
    public void send_not_exist_SessionId(){
       not_exist_SessionId = 123456789012L;
       pathParams.put("session_id",not_exist_SessionId);
    }
    @Then("Response code of reset session is 404 and body returns with error message")
    public void Validate_not_exist_sessionId_response(){
       test.Validate_Error_Messages(reset_session,HttpStatus.SC_NOT_FOUND,"Session not found or not eligible for display.",4048);
    }
}
