package AdminArea;

import EducatorProfile.Educator_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import com.github.javafaker.Faker;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.hamcrest.Matchers.hasToString;


public class CreateStage {
    Database_Connection Connect = new Database_Connection();
    Educator_TestData data = new Educator_TestData();
    String Admin_Token = data.Admin_Token;
    TestBase test = new TestBase();
    Faker fakerData =new Faker();
    Long non_existing_CountryId = Long.valueOf(fakerData.number().randomNumber(12, true));
    Integer stageOrder = Integer.valueOf(fakerData.number().digits(3));
    String stageName = fakerData.lorem().word();
    String stageUrlText = stageName + "-school";
    String valid_body_request;
    String invalid_body_request;
    Response Create_Stage;
    Long countryId;
    String countryIso;



    @Given("Getting data of a created country from database")
    public void get_country_details () throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("SELECT * FROM countries c \n" +
                "ORDER BY RANDOM() \n" +
                "LIMIT 1");

        while (resultSet.next()) {
            countryId = resultSet.getLong("country_id");
            countryIso= resultSet.getString("country_iso_code");
        }
    }


    @Given("Getting data of a created stage from database")
    public void get_stage_details () throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("SELECT * FROM stages s \n" +
                "ORDER BY RANDOM() \n" +
                "LIMIT 1");

        while (resultSet.next()) {
            countryId = resultSet.getLong("country_id");
            stageOrder= resultSet.getInt("stage_order");
            stageUrlText = resultSet.getString("stage_url_text");
        }
    }


    @And("User send valid data to Create Stage API")
    public void create_stage() {
        valid_body_request = "{\"country_id\":"+ countryId +"," +
                "\"stage_localization_key\":\""+ countryIso +"_"+ stageName +"\"," +
                "\"stage_url_text\":\""+ stageUrlText +"\"," +
                "\"stage_order\":"+ stageOrder +"," +
                "\"stage_color\":\"color\"," +
                "\"stage_is_active\":false}";
        Create_Stage = test.sendRequest("POST", "/admin/stages", valid_body_request, Admin_Token);
    }


    @Given("Performing the Api of Create Stage With invalid body")
    public void create_stage_with_invalid_body() {
        invalid_body_request = "{\"country_id\":"+countryId+"," +
                "\"stage_localization_key\":\"\"," +
                "\"stage_url_text\":\"\"," +
                "\"stage_order\":\""+stageOrder+"\"," +
                "\"stage_color\":\"\"," +
                "\"stage_is_active\":false}";

        Create_Stage = test.sendRequest("POST", "/admin/stages", invalid_body_request, Admin_Token);
    }


    @Given("Performing the Api of Create Stage With invalid country id")
    public void create_stage_with_invalid_country() {
        invalid_body_request = "{\"country_id\":"+non_existing_CountryId+"," +
                "\"stage_localization_key\":\"eg_"+ stageName +"\"," +
                "\"stage_url_text\":\""+ stageUrlText +"\"," +
                "\"stage_order\":"+ stageOrder +"," +
                "\"stage_color\":\"color\"," +
                "\"stage_is_active\":false}";
        Create_Stage = test.sendRequest("POST", "/admin/stages", invalid_body_request, Admin_Token);
    }


    @Given("Performing the Api of Create Stage with invalid token")
    public void create_stage_with_invalid_token() {
        valid_body_request = "{\"country_id\":102123867837," +
                "\"stage_localization_key\":\"eg-kg1\"," +
                "\"stage_url_text\":\"kg1\"," +
                "\"stage_order\":66," +
                "\"stage_color\":\"blue\"," +
                "\"stage_is_active\":false}";
        Create_Stage = test.sendRequest("POST", "/admin/stages", valid_body_request,data.refresh_token_for_deletedEducator);
    }


    @Then("I verify the appearance of status code 201 and Stage created successfully")
    public void validate_response_of_create_stage_successfully() {
        Create_Stage.prettyPrint();
        Create_Stage.then()
                .statusCode(HttpStatus.SC_CREATED)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/CreateStage.json")))
                .body("message", hasToString("Stage created successfully."));
    }


    @Then("I verify the appearance of status code 400 and body data is incorrect")
    public void validate_response_of_invalid_request_body() {
        Response invalidBody = Create_Stage;
        test.Validate_Error_Messages(invalidBody, HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }


    @Then("I verify the appearance of status code 400 and country not exist")
    public void validate_response_of_invalid_country_id() {
        test.Validate_Error_Messages(Create_Stage, HttpStatus.SC_BAD_REQUEST,"Create stage failed, country_id is not exist.",40021);
    }


    @Then("I verify the appearance of status code 403 and user is unauthorized")
    public void validate_response_of_unauthorized_user() {
        test.Validate_Error_Messages(Create_Stage, HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }


    @When("Performing the Api of Create Stage With existing stage url")
    public void create_stage_with_existing_url() {
        valid_body_request = "{\"country_id\":"+ countryId +"," +
                "\"stage_localization_key\":\""+ countryIso +"_"+ stageName +"\"," +
                "\"stage_url_text\":\""+ stageUrlText +"\"," +
                "\"stage_order\":66," +
                "\"stage_color\":\"color\"," +
                "\"stage_is_active\":false}";
        Create_Stage = test.sendRequest("POST", "/admin/stages", valid_body_request, Admin_Token);
    }


    @When("Performing the Api of Create Stage With existing stage order")
    public void create_stage_with_existing_order() {
        valid_body_request = "{\"country_id\":"+ countryId +"," +
                "\"stage_localization_key\":\""+ countryIso +"_"+ stageName +"\"," +
                "\"stage_url_text\":\"kg1\"," +
                "\"stage_order\":"+ stageOrder +"," +
                "\"stage_color\":\"color\"," +
                "\"stage_is_active\":false}";
        Create_Stage = test.sendRequest("POST", "/admin/stages", valid_body_request, Admin_Token);
    }


    @Then("I verify the appearance of status code 400 and stage url must be unique")
    public void validate_response_stage_url_exists() {
        test.Validate_Error_Messages(Create_Stage, HttpStatus.SC_BAD_REQUEST,"Create stage failed, 'stage_url_text' must be unique per country.",40021);
    }


    @Then("I verify the appearance of status code 400 and stage order must be unique")
    public void validate_response_stage_order_exists() {
        test.Validate_Error_Messages(Create_Stage, HttpStatus.SC_BAD_REQUEST,"Create stage failed, 'stage_order' must be unique per country.",40021);
    }
}


