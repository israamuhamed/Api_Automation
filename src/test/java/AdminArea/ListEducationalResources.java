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

public class ListEducationalResources {
    TestBase test = new TestBase();
    Educator_TestData data = new Educator_TestData();
    AssignEducationalResources assign = new AssignEducationalResources();
    Response List_educationalResource;
    public Long session_id;
    public  Long educational_resource_id;
    @Given("User Create New Session and EducationalResource")
    public void create_valid_Data() throws SQLException, InterruptedException {
        assign.Create_new_Assign_resources();
    }
    @When("Performing The API of ListEducationalResource")
    public void get_list_educational_Resource() {
        System.out.println(assign.SessionID + " " + assign.ResourceId);
        List_educationalResource = test.sendRequest("GET", "/admin/educational-resources?session-id="+ assign.SessionID +"&educational-resource-id=" + assign.ResourceId +"", null, data.Admin_Token);
    }
    @Then("All The EducationalResources and its session should return in response body")
    public void validate_response_body() {
        List_educationalResource.prettyPrint();
        List_educationalResource
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("data.session_id", hasItem(assign.SessionID))
                .body("data.educational_resource_id", hasItem(assign.ResourceId))
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/ListEducationalResources.json")));
    }

    @When("Performing The API Of List Educational Resource With Invalid Token")
    public void send_invalid_token() {
        List_educationalResource = test.sendRequest("GET", "/admin/educational-resources?session-id=" + assign.SessionID + "&educational-resource-id=" + assign.ResourceId + "", null, data.refresh_token);
    }
    @Then("Response Code Is 403 And Body Returns With Error Message")
    public void validate_unauthorized_response(){
        test.Validate_Error_Messages(List_educationalResource,HttpStatus.SC_FORBIDDEN, "Unauthorized", 4031);
    }

    @Given("User Send Invalid data In Request Params")
    public void define_invalid_data(){
        session_id = 123456789012L;
        educational_resource_id= 123456789012L;
    }
    @When("Performing The API of ListEducationalResource With Invalid Params")
    public void send_invalid_params(){
        define_invalid_data();
        List_educationalResource = test.sendRequest("GET", "/admin/educational-resources?session-id="+ session_id +"&educational-resource-id=" + educational_resource_id + "", null, data.Admin_Token);
    }
    @Then("Response Code Is 200 and body returns with EmptyList")
    public void validate_invalid_data_response(){
        List_educationalResource.prettyPrint();
        List_educationalResource
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body("data",empty());
    }
}