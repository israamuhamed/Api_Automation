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

public class StagePage {

    TestBase test = new TestBase();
    Student_TestData data = new Student_TestData();
    Response StagePage;
    String user_token = data.Student_refresh_Token;
    Database_Connection connect = new Database_Connection();
    Long grade_id;
    Integer stage_order;
    String stage_localization_key;
    Integer stage_id;
    String stage_color;


    public StagePage()throws SQLException{
        get_data();
    }

    public void get_data()throws SQLException {
        ResultSet resultSet = connect.connect_to_database("select *from public.countries c join stages s on c.country_id = s.country_id  join grades g on g.stage_id  = s.stage_id \n" +
                "where c.country_iso_code = 'eg' and s.stage_url_text = 'primary'");

        while (resultSet.next()){
            grade_id= resultSet.getLong("grade_id");
            stage_order = resultSet.getInt("stage_order");
            stage_id= resultSet.getInt("stage_id");
            stage_color = resultSet.getString("stage_color");
            stage_localization_key = resultSet.getString("stage_localization_key");
        }
    }


    @Given("User Send CountryIso Code And Data To Stage Page API")
    public void send_valid_data_stage_api(){
        StagePage = test.sendRequest("GET","/web/stage-page?country-iso-code=eg&stage-url-text=primary", null , user_token);
    }
    @Then("Stage Of The Data In Query Param In Stage Page  API Return In Response Body")
    public void validate_send_stagePage_request_sucessfully(){
        StagePage.prettyPrint();
        StagePage.then().assertThat().statusCode(HttpStatus.SC_OK)
                .body("stage_localization_key",containsString(stage_localization_key))
                .body("grades.grade_id",hasItem(grade_id))
                .body("stage_order",equalTo(stage_order))
                .body("stage_localization_key",containsString(stage_localization_key))
                .body("stage_color",containsString(stage_color))
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/NagwaPagesAPIs/StagePage.json")));
    }
    @Given("User send Invalid Data In query params of the request Stage Page")
    public void send_not_exist_data_stagePage(){
        StagePage = test.sendRequest("GET","/web/stage-page?country-iso-code=2wa&stage-url-text=primary", null , user_token);
    }
    @Then("Response Code Of Stage Page Is 404 And Body Returns With Error Message")
    public void validate_not_Exist_data_response(){
        test.Validate_Error_Messages(StagePage,HttpStatus.SC_NOT_FOUND,"No data found for the specified country or stage or grade.",40417);
    }
    @Given("User execute request Stage Page with missing query params")
    public void send_invalid_request(){
        StagePage = test.sendRequest("GET","/web/stage-page?country-iso-code=null", null , user_token);
    }
    @Then("Response code of Stage Page is 400 and body returns with error message")
    public void validate_invalid_request_response(){
        test.Validate_Error_Messages(StagePage,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }
}
