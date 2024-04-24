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

public class ListCountries {
    TestBase test = new TestBase();
    Database_Connection Connect = new Database_Connection();
    Student_TestData data = new Student_TestData();
    Response List_Countries;
    Long country_id ;
    String country_iso_code;
    String country_dial_code;
    String country_localization_key;
    String country_currency_iso_code;

    @Given("Performing the Api of List Countries")
    public void List_Countries() {
        List_Countries = test.sendRequest("GET", "/countries", null, data.Parent_refreshToken);
    }

    @And("Getting Countries List from database")
    public void getCountriesList () throws SQLException {
        ResultSet resultSet = Connect.connect_to_database(" select * from countries c " +
                "LIMIT 1");

        while (resultSet.next()) {
            country_id = resultSet.getLong("country_id");
            country_iso_code = resultSet.getString("country_iso_code");
            country_dial_code = resultSet.getString("country_dial_code");
            country_localization_key = resultSet.getString("country_localization_key");
            country_currency_iso_code = resultSet.getString("country_currency_iso_code");
        }
    }

    @Then("I verify the appearance of status code 200 and countries returned successfully")
    public void Validate_Response_of_List_Countries() {
        List_Countries.prettyPrint();
        List_Countries.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/LookupsData/ListCountries.json")))
                .body("[0].country_id", equalTo(country_id),"[0].country_iso_code",hasToString(country_iso_code),
                        "[0].country_currency_iso_code",hasToString(country_currency_iso_code),"[0].country_localization_key",hasToString(country_localization_key),
                        "[0].country_dial_code",hasToString(country_dial_code));
    }
}
