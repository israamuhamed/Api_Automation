package AdminArea;

import EducatorProfile.Educator_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.hamcrest.Matchers.*;

public class ListGrades {
    TestBase test = new TestBase();
    Database_Connection Connect = new Database_Connection();
    Educator_TestData data = new Educator_TestData();
    Response List_Grades;
    Integer total_grades;
    String localization_key;
    String country_iso_code;
    String stage_url_text;
    Integer grade_order;
    String grade_url_text;
    String grade_localization_key;
    Long grade_id;
    boolean is_active;

    @Given("Get Grades Data From Database")
    public void get_grades_data()throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select * from grades g \n" +
                "join stages s \n" +
                "on g.stage_id  = s.stage_id \n" +
                "join countries c \n" +
                "on c.country_id = s.country_id \n" +
                "ORDER BY RANDOM()\n" +
                "LIMIT 1;");

        while (resultSet.next()) {
            country_iso_code = resultSet.getString("country_iso_code");
            localization_key = resultSet.getString("stage_localization_key");
            grade_order = resultSet.getInt("grade_order");
            grade_id = resultSet.getLong("grade_id");
            stage_url_text = resultSet.getString("stage_url_text");
            grade_url_text = resultSet.getString("grade_url_text");
            grade_localization_key = resultSet.getString("grade_localization_key");
            is_active = resultSet.getBoolean("stage_is_active");
        }
    }

    @When("Performing The API of List Grades With Params")
    public void List_Grades(){
        List_Grades  = test.sendRequest("GET", "/admin/grades?country-iso-code="+ country_iso_code +"&stage-url-text="+ stage_url_text +"&grade-url-text="+ grade_url_text +"&is-active="+ is_active +"&grade-localization-key="+ grade_localization_key+"&grade-id="+ grade_id+"", null , data.Admin_Token);
    }

    @Then("The Grade Returned Successfully")
    public void validate_list_grades_response(){
        List_Grades.prettyPrint();
        List_Grades.then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("data.grade_id",hasItem(equalTo(grade_id)),"data.country_iso_code",hasItem(hasToString(country_iso_code)),
                        "data.stage_url_text",hasItem(hasToString(stage_url_text)),"data.grade_url_text",hasItem(hasToString(grade_url_text)),
                        "data.grade_localization_key",hasItem(hasToString(grade_localization_key)), "data.grade_order",hasItem(equalTo(grade_order)),
                        "data.grade_is_active",hasItem(equalTo(is_active)))
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/ListGrades.json")));
    }

    @When("Performing The API of List Grades with country not exist")
    public void List_Grades_with_country_notExist(){
        List_Grades  = test.sendRequest("GET", "/admin/grades?country-iso-code=country&stage-url-text="+ stage_url_text +"&grade-url-text="+ grade_url_text +"&is-active="+ is_active +"&grade-localization-key="+ grade_localization_key+"&grade-id="+ grade_id+"", null , data.Admin_Token);
    }

    @Then("I should see Status code 404 with error message country not exist")
    public void List_grades_unauthorized_Response(){
        test.Validate_Error_Messages(List_Grades,HttpStatus.SC_NOT_FOUND,"No data found for the specified country or stage or grade.",40417);
    }

    @Given("Get Grades count From Database")
    public void get_grades_count()throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select count(*) as total_grades from grades g  \n" +
                "where stage_id  notnull");

        while (resultSet.next()) {
            total_grades = resultSet.getInt("total_grades");
        }
    }

    @When("Performing The API of List Grades To List All Grades")
    public void List_All_Grades_(){
        List_Grades  = test.sendRequest("GET", "/admin/grades", null , data.Admin_Token);
    }

    @Then("validate the total number of active grades returned successfully")
    public void validate_total_no_of_grades(){
        List_Grades.prettyPrint();
        List_Grades.then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("total_count",equalTo(total_grades))
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/ListGrades.json")));
    }

}
