package NagwaPagesAPIs;

import StudentClasses.Student_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
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

public class LandingPage {

    TestBase test = new TestBase();
    Student_TestData data = new Student_TestData();
    Response Landing_Page;
    String user_token = data.Student_refresh_Token;
    Database_Connection connect = new Database_Connection();
    Long grade_id;
    Integer stage_order;
    String stage_localization_key;
    Integer stage_id;
    String stage_color;

    public LandingPage()throws SQLException{
        get_data_of_landing_page();
    }
    public void get_data_of_landing_page()throws SQLException {
        ResultSet resultSet_of_landing_page = connect.connect_to_database("\n" +
                "select * from public.countries c join stages s on c.country_id = s.country_id  join grades g on g.stage_id  = s.stage_id \n" +
                "where c.country_iso_code = 'eg' and s.stage_order=1");

        while (resultSet_of_landing_page.next()){
            stage_id = resultSet_of_landing_page.getInt("stage_id");
            stage_localization_key = resultSet_of_landing_page.getString("stage_localization_key");
            stage_color = resultSet_of_landing_page.getString("stage_color");
            stage_order = resultSet_of_landing_page.getInt("stage_order");
            grade_id= resultSet_of_landing_page.getLong("grade_id");
        }
    }
    @Given("User Send CountryIso Code To Landing Page API")
    public void send_valid_request_to_landingPage(){
        Landing_Page = test.sendRequest("GET", "/web/landing-page?country-iso-code=eg",null , user_token);
    }
    @Then("Stage and Grades Of The Data In Query Param In Landing Page API Return In Response Body")
    public void validate_sending_valid_request(){
        Landing_Page.prettyPrint();
        Landing_Page.then().assertThat().statusCode(HttpStatus.SC_OK)
                .body("[0].stage_localization_key",hasToString(stage_localization_key))
                .body("grades.grade_id[0]",hasItem(equalTo(grade_id)))
                .body("[0].stage_order",equalTo(stage_order))
                .body("[0].stage_localization_key",hasToString(stage_localization_key))
                .body("[0].stage_color",hasToString(stage_color))
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/NagwaPagesAPIs/LandingPage.json")));
    }
    @Given("User send Invalid Country Code In query params of the request Landing Page")
    public void send_invalid_Country_code(){
        Landing_Page = test.sendRequest("GET", "/web/landing-page?country-iso-code=3sed",null , user_token);
    }
    @Then("Response Code Of Landing Page Is 404 And Body Returns With Error Message")
    public void validate_invalid_country_Code_Response(){
        test.Validate_Error_Messages(Landing_Page,HttpStatus.SC_NOT_FOUND,"No data found for the specified country or stage or grade.",40417);
    }
    @Given("User execute request Landing Page with missing query params")
    public void missing_query_param_Request(){
        Landing_Page = test.sendRequest("GET", "/web/landing-page?country-iso-code=",null , user_token);
    }
    @Then("Response code of Landing Page is 400 and body returns with error message")
    public void validate_missing_param_response(){
        test.Validate_Error_Messages(Landing_Page,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }
}
