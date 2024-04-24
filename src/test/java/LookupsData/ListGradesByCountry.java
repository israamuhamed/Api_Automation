package LookupsData;

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
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class ListGradesByCountry {
    Database_Connection Connect = new Database_Connection();
    TestBase test = new TestBase();
    Student_TestData data = new Student_TestData();
    Response List_Grades_ByCountry;
    Long grade_id ;
    String grade_url_text;
    String grade_title;
    String grade_localization_key;
    Integer grade_order;
    Long countryId = 102123867837L;
    Map<String, Object> pathParams = test.pathParams;

    @Given("Performing the Api of List Grade by Country")
    public void List_Countries() {
        pathParams.put("country_id",countryId);
        List_Grades_ByCountry = test.sendRequest("GET", "/countries/{country_id}/grades", null, data.Parent_refreshToken);
    }

    @And("Getting Grades by country from database")
    public void getGrade_ByCountriesList () throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("  select * from grades g \n" +
                " join stages s\n" +
                " on g.stage_id = s.stage_id\n" +
                " join countries c\n" +
                " on c.country_id = s.country_id\n" +
                " where c.country_id ="+ countryId +"\n" +
                " and g.grade_url_text ='1'");

        while (resultSet.next()) {
            grade_id = resultSet.getLong("grade_id");
            grade_url_text = resultSet.getString("grade_url_text");
            grade_title = resultSet.getString("grade_title");
            grade_localization_key = resultSet.getString("grade_localization_key");
            grade_order = resultSet.getInt("grade_order");
        }
    }

    @Then("I verify the appearance of status code 200 and Grades returned successfully")
    public void Validate_Response_of_List_Countries() {
        List_Grades_ByCountry.prettyPrint();
        List_Grades_ByCountry.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/LookupsData/ListGradesByCountries.json")))
                .body("[0].grade_id", equalTo(grade_id),"[0].grade_url_text",hasToString(grade_url_text),
                        "[0].grade_title",hasToString(grade_title),"[0].grade_localization_key",hasToString(grade_localization_key),
                        "[0].grade_order",equalTo(grade_order));
    }

}
