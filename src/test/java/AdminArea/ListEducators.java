package AdminArea;

import EducatorProfile.Educator_TestData;
import TestConfig.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;

import static org.hamcrest.Matchers.*;

public class ListEducators {
    TestBase test = new TestBase();
    Educator_TestData data = new Educator_TestData();
    CreateEducator educator_data = new CreateEducator();
    Response List_Educators;
    public Long educator_id;

    @Given("User Create New Educator")
    public void create_new_educator() {
        educator_data.Create_Educator();
    }

    @When("Performing The API of ListEducators")
    public void perform_ListEducators_API() {
        List_Educators = test.sendRequest("GET", "/admin/educators?educator-id=" + educator_data.Educator_ID, null, data.Admin_Token);
    }

    @Then("The Educator should return in response body")
    public void validate_educator_data() {
        List_Educators.then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("data.educator_id", hasItem(educator_data.Educator_ID))
                .body("data.educator_first_name", hasItem(educator_data.firstName), "data.educator_last_name", hasItems(educator_data.lastName))
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/ListEducators.json")));
    }

    @When("Performing the API of ListEducators With Invalid Token")
    public void List_educators_with_invalid_token() {
        List_Educators = test.sendRequest("GET", "/admin/educators?educator-id=" + educator_data.Educator_ID, null, data.refresh_token);
    }

    @Then("Response Code of ListEducators Is 403 And Body Returns With Error Message")
    public void validate_list_educators_with_invalid_token() {
        test.Validate_Error_Messages(List_Educators, HttpStatus.SC_FORBIDDEN, "Unauthorized", 4031);
    }

    @Given("User Send Invalid Educator_id In Request Params")
    public void define_invalid_educator_id() {
        educator_id = 123456789012L;
    }
    @When("Performing The API of ListEducators With Invalid Params")
    public void perform_List_Educators_With_invalid_param(){
        List_Educators = test.sendRequest("GET", "/admin/educators?educator-id="+educator_id, null, data.Admin_Token);
    }
    @Then("Response Code Of ListEducators Is 404 and body returns with error message")
    public void validate_List_educators_with_invalid_param(){
        test.Validate_Error_Messages(List_Educators, HttpStatus.SC_NOT_FOUND, "No educators found", 4042);
    }
    @Given("User Perform The Api Of ListEducators With Empty Educator_Id")
    public void List_educator_invalid_request(){
        List_Educators = test.sendRequest("GET", "/admin/educators?educator-id=", null, data.Admin_Token);
    }
    @Then("Response Code Of ListEducators Is 400 and body Returns With Error Message")
    public void validate_list_educators_invalid_request(){
        test.Validate_Error_Messages(List_Educators, HttpStatus.SC_BAD_REQUEST, "Invalid request. Please check the path parameters and request context for accuracy", 4002);

    }
}
