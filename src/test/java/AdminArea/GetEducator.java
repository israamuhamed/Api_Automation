package AdminArea;

import EducatorProfile.Educator_TestData;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import TestConfig.TestBase;

import java.io.File;
import java.util.Map;

import static org.hamcrest.Matchers.hasToString;

public class GetEducator {
    TestBase test = new TestBase();
    Educator_TestData data = new Educator_TestData();
    CreateEducator educator =new CreateEducator();
    String Admin_token = data.Admin_Token;
    Map<String, Object> pathParams = test.pathParams;
    Long educatorID;
    Response Get_Educator;
    Response GetEducator_with_Invalid_token;
    Response NotActive_Educator_token;
    Response Deleted_Educator_token;
    public String EducatorEmail;


    @When("Performing the Api of Get Educator")
    public String Get_Educator() {
        Get_Educator = test.sendRequest("GET", "/admin/educators/{educator_id}", null,Admin_token);
        return EducatorEmail = Get_Educator.then().extract().path("educator_email");
    }
    @Given("User Send valid educator Id to get educator data")
    public void user_send_valid_educatorId() {
        educator.Create_Educator();
        System.out.println(educatorID = educator.Educator_ID);
        pathParams.put("educator_id", educatorID);
    }
    @Then("I verify the appearance of status code 200 and Educator data returned successfully")
    public void Validate_Response_of_Get_Educator() {
        Get_Educator.prettyPrint();
        Get_Educator.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/GetEducator.json")))
                .body("educator_first_name", hasToString(educator.firstName),"educator_last_name",hasToString(educator.lastName),"educator_email",hasToString(educator.Email));
    }

    @When("Performing the Api of Get Educator with invalid educator id")
    public void Get_Educator_with_invalid_ID() {
        Get_Educator = test.sendRequest("GET", "/admin/educators/{educator_id}", null,Admin_token);
    }

    @Given("User Send invalid educator Id to get educator data")
    public void user_send_Invalid_educatorId() {
        pathParams.put("educator_id", "11122334455678");
    }

    @Given("User Send special char in educator Id")
    public void user_send_Invalid_educatorId_with_special_char() {
        pathParams.put("educator_id", "?????");
    }

    @Then("I verify the appearance of status code 400 and path is incorrect")
    public void Validate_Response_of_invalid_EducatorID() {
        Response Invalid_ID = Get_Educator;
        test.Validate_Error_Messages(Invalid_ID,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }

    @Given("User Send unauthorized educatorID to GetEducator Api")
    public void send_unauthorized_educatorId() {pathParams.put("educator_id",data.deleted_educator);
    }
    @When("performing the api of GetEducator with invalid token")
    public void send_unauthorized_educator(){
        GetEducator_with_Invalid_token = test.sendRequest("GET", "/educators/{educator_id}/profile", null, data.refresh_token_for_notActiveEducator);
    }
    @Then("I verify the appearance of status code 403 and EducatorID is unauthorized")
    public void Validate_Response_of_unauthorized_EducatorId() {
        Response unauthorizedEducator = GetEducator_with_Invalid_token;
        test.Validate_Error_Messages(unauthorizedEducator,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }

    @Given("User Send not active educatorID to GetEducator")
    public void user_send_notActive_educatorId() {pathParams.put("educator_id", data.deleted_educator);
    }
    @When("performing the api of GetEducator with notActive educator token")
    public void send_notActive_educator_token(){
         NotActive_Educator_token = test.sendRequest("GET", "/educators/{educator_id}/profile", null,data.refresh_token_for_deletedEducator);
    }
    @Then("I verify the appearance of status code 404 and Educator is not active")
    public void Validate_Response_of_notActive_EducatorId() {
        Response InActiveEducator = NotActive_Educator_token;
        test.Validate_Error_Messages(InActiveEducator,HttpStatus.SC_NOT_FOUND,"Educator with the specified ID does not exist or is not active.",40413);
    }
    @Given("User Send deleted educatorID")
    public void user_send_deleted_educatorId() {pathParams.put("educator_id", data.deleted_educator);
    }
    @When("performing the api of GetEducator with deleted educator")
    public void send_deleted_educator_token(){
        Deleted_Educator_token = test.sendRequest("GET", "/educators/{educator_id}/profile", null,data.refresh_token_for_deletedEducator);
    }
    @Then("I verify the appearance of status code 404 and EducatorID is deleted")
    public void Validate_Response_of_deleted_EducatorId() {
        Response DeletedEducator =Deleted_Educator_token;
        test.Validate_Error_Messages(DeletedEducator,HttpStatus.SC_NOT_FOUND,"Educator with the specified ID does not exist or is not active.",40413);
    }

}
