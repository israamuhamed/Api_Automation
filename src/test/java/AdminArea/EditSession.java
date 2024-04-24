package AdminArea;

import EducatorProfile.Educator_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import static org.hamcrest.Matchers.*;


public class EditSession {
    TestBase test = new TestBase();
    Educator_TestData data = new Educator_TestData();
    CreateEducator educator_data = new CreateEducator();
    CreateSession session_data = new CreateSession();
    Long new_educator_id;
    Database_Connection connect = new Database_Connection();
    public Map<String, Object> pathParams = test.pathParams;
    Response Edit_Session;
    Long invalid_session_id;
    Long not_exist_session_id;
    String valid_body;

    String invalid_body;
  @Given("Admin Send Valid SessionId And Body To EditSession API")
    public void send_valid_data()throws SQLException {
      session_data.Create_Session();
      educator_data.Create_Educator();
      pathParams.put("session_id",session_data.sessionId);
      valid_body= "{\"educator_id\":"+educator_data.Educator_ID+"}";
  }
  @When("Performing The API of EditSession")
    public void execute_edit_session_request(){
      Edit_Session = test.sendRequest("PATCH","/admin/sessions/{session_id}",valid_body,data.Admin_Token);
  }
  @Then("Response code of EditSession is 200 and body returns with success message")
    public void validate_success_response()throws SQLException{
      Edit_Session.prettyPrint();

      ResultSet resultSet = connect.connect_to_database("select s.educator_id from public.sessions s where s.session_id = "+session_data.sessionId);
      while (resultSet.next()){
          new_educator_id = resultSet.getLong("educator_id");
      }
      System.out.println(new_educator_id);
      Edit_Session.then().assertThat()
              .statusCode(HttpStatus.SC_OK)
              .body("message",containsString("session updated successfully."));
      assertEquals(educator_data.Educator_ID,new_educator_id);
  }
  @Given("User Send Invalid Session Id And Valid Body To The EditSession API")
    public void send_invalid_sessionId(){
      educator_data.Create_Educator();
      invalid_session_id = 123456789L;
      pathParams.put("session_id",invalid_session_id);
      valid_body= "{\"educator_id\":"+educator_data.Educator_ID+"}";
  }
  @Then("Response code of EditSession is 400 and body returns with error message")
    public void validate_invalid_sessionId_response(){
      test.Validate_Error_Messages(Edit_Session,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
  }
  @When("Performing The API of Edit Session With Invalid Token")
    public void perform_edit_session_with_invalid_token(){
      Edit_Session = test.sendRequest("PATCH","/admin/sessions/{session_id}",valid_body,data.refresh_token);
  }
  @Then("Response code of EditSession is 403 and body returns with error message")
    public void validate_invalid_token_response(){
      test.Validate_Error_Messages(Edit_Session,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
  }
  @Given("User Send SessionId that doesn't exist And Valid Body To EditSession API")
    public void send_sessionId_not_Exist(){
      not_exist_session_id = 123456789012L;
      educator_data.Create_Educator();
      pathParams.put("session_id",not_exist_session_id);
      valid_body= "{\"educator_id\":"+educator_data.Educator_ID+"}";
  }
  @Then("Response code of EditSession is 404 and body returns with error message That Session Doesnt Exist")
    public void validate_not_Exist_session_Response(){
      test.Validate_Error_Messages(Edit_Session,HttpStatus.SC_NOT_FOUND,"Session not found or not eligible for display.",4048);
  }
  @Given("User Send Educator that doesn't exist In Request Body To EditSession API")
    public void send_not_Exist_educatorId()throws SQLException{
     session_data.Create_Session();
     pathParams.put("session_id",session_data.sessionId);
     invalid_body= "{\"educator_id\":123456789012}";
  }
  @When("Performing The API of EditSession With Eductor Id That Doesn't Exist In Request Body")
    public void send_request_with_not_exist_Educator(){
      Edit_Session = test.sendRequest("PATCH","/admin/sessions/{session_id}",invalid_body,data.Admin_Token);
  }
  @Then("Response code of EditSession is 404 and body returns with error message That Educator Doesn't Exist")
    public void validate_response_not_exist_educator(){
      test.Validate_Error_Messages(Edit_Session,HttpStatus.SC_NOT_FOUND,"Educator with the specified ID does not exist or is not active.",40413);
  }

}
