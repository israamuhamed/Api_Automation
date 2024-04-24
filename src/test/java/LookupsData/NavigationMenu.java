package LookupsData;

import StudentClasses.Student_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import StudentClasses.Student_TestData;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.Matchers.*;

public class NavigationMenu {
    TestBase test = new TestBase();
    Database_Connection Connect = new Database_Connection();
    Student_TestData data = new Student_TestData();
    Response Navigation_Menu;
    Long language_id ;
    String language_name;
    String language_iso_code;
    Integer language_order;
    Long country_id;
    String country_iso_code;
    String country_currency_iso_code;
    String country_localization_key;
    Integer stage_id;
    String stage_localization_key;
    String stage_color;
    String stage_url_text;
    Integer stage_order;



    @Given("Performing the Api of Navigation Menu")
    public void Navigation_Menu() {
        Navigation_Menu = test.sendRequest("GET", "/navigation-menu", null, data.Parent_refreshToken);
    }

    @And("Getting Countries ,stages and languages  from database")
    public void getCountries_Stages_Languages () throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("  select * from countries c \n" +
                " join stages s \n" +
                " on s.country_id = c.country_id \n" +
                " where c.country_iso_code = 'eg' and s.stage_order = 3");

        while (resultSet.next()) {
            country_id = resultSet.getLong("country_id");
            country_iso_code = resultSet.getString("country_iso_code");
            country_localization_key = resultSet.getString("country_localization_key");
            country_currency_iso_code = resultSet.getString("country_currency_iso_code");
            stage_id = resultSet.getInt("stage_id");
            stage_localization_key = resultSet.getString("stage_localization_key");
            stage_color = resultSet.getString("stage_color");
            stage_url_text = resultSet.getString("stage_url_text");
            stage_order = resultSet.getInt("stage_order");

        }

        ResultSet ui_languages = Connect.connect_to_database("select * from ui_languages ul where ui_language_order = 2");

        while (ui_languages.next()) {
            language_id = ui_languages.getLong("ui_language_id");
            language_name = ui_languages.getString("language_name");
            language_iso_code = ui_languages.getString("ui_language_iso_code");
            language_order = ui_languages.getInt("ui_language_order");
        }
    }

    @Then("I verify the appearance of status code 200 and Menu returned successfully")
    public void Validate_Response_of_Navigation_Menu() {
        Navigation_Menu.prettyPrint();
        Navigation_Menu.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/LookupsData/NavigationMenu.json")))
                .body("languages.language_id[1]", equalTo(language_id),"languages.language_name[1]",hasToString(language_name),
                        "languages.language_iso_code[1]",hasToString(language_iso_code),"languages.language_order[1]",equalTo(language_order))
                .body("countries.country_id[0]", equalTo(country_id),"countries.country_iso_code[0]",hasToString(country_iso_code),
                        "countries.country_currency_iso_code[0]",hasToString(country_currency_iso_code),"countries.country_localization_key[0]",hasToString(country_localization_key))
                .body("countries.stages[0].stage_id", hasItem(equalTo(stage_id)),"countries.stages[0].stage_localization_key",hasItem(hasToString(stage_localization_key)),
                        "countries.stages[0].stage_color",hasItem(hasToString(stage_color)),"countries.stages[0].stage_url_text",hasItem(hasToString(stage_url_text)),
                        "countries.stages[0].stage_order",hasItem(equalTo(stage_order)));
    }

}
