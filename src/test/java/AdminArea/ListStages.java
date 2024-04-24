package AdminArea;

import EducatorProfile.Educator_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Given;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.Matchers.*;

public class ListStages {
    TestBase test = new TestBase();
    Database_Connection Connect = new Database_Connection();
    Educator_TestData data = new Educator_TestData();
    Response List_Stages;
    Number stage_id;
    String localization_key;
    String country_iso_code;
    String stage_url_text;
    Integer stage_order;
    boolean is_active;
    Integer total_stages;

    @Given("Get Stage Data From Database")
    public void get_stages_data()throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("SELECT * FROM stages s\n" +
                "join countries c \n" +
                "on c.country_id = s.country_id \n" +
                "ORDER BY RANDOM()\n" +
                "LIMIT 1;\n");

        while (resultSet.next()) {
            country_iso_code = resultSet.getString("country_iso_code");
            localization_key = resultSet.getString("stage_localization_key");
            stage_order = resultSet.getInt("stage_order");
            stage_id = resultSet.getLong("stage_id");
            stage_url_text = resultSet.getString("stage_url_text");
            is_active = resultSet.getBoolean("stage_is_active");
        }
    }
    @When("Performing The API of List Stages")
    public void List_stages(){
        List_Stages = test.sendRequest("GET", "/admin/stages?country-iso-code="+ country_iso_code +"& active= "+ is_active +" & stage-id="+ stage_id +"& stage-localization-key="+ localization_key, null , data.Admin_Token);
    }
    @Then("The Stages Returned Successfully")
    public void validate_list_session_response(){
        List_Stages.then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("data.stage_id",hasItem(equalTo(stage_id)),"data.country_iso_code",hasItem(hasToString(country_iso_code)),
                        "data.stage_url_text",hasItem(hasToString(stage_url_text)),"data.stage_localization_key",hasItem(hasToString(localization_key)),
                        "data.stage_order",hasItem(equalTo(stage_order)),"data.stage_is_active",hasItem(equalTo(is_active)))
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/ListStages.json")));
    }
    @When("Performing The API of List Stages with stage not exist")
    public void List_stages_with_stage_not_exist(){
        List_Stages = test.sendRequest("GET", "/admin/stages?stage-id=1000 & country-iso-code="+ country_iso_code +" & active= "+ is_active +" & stage-id=1000 & stage-localization-key="+ localization_key, null , data.Admin_Token);
    }
    @Then("I should see Status code 404 with error message stage not exist")
    public void List_sessions_unauthorized_Response(){
        test.Validate_Error_Messages(List_Stages,HttpStatus.SC_NOT_FOUND,"No data found for the specified country or stage or grade.",40417);
    }

    @Given("Get Stages count From Database")
    public void get_grades_count()throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select count(*) as total_stages from stages s ");

        while (resultSet.next()) {
            total_stages = resultSet.getInt("total_stages");
        }
    }

    @When("Performing The API of List Stages To List All Stages")
    public void List_All_Grades_(){
        List_Stages  = test.sendRequest("GET", "/admin/stages", null , data.Admin_Token);
    }

    @Then("validate the total number of stages returned successfully")
    public void validate_total_no_of_grades(){
        List_Stages.prettyPrint();
        List_Stages.then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("total_count",equalTo(total_stages))
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/ListStages.json")));
    }

}
