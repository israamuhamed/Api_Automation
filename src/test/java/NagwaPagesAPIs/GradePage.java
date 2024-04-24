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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class GradePage {

    TestBase test = new TestBase();
    Student_TestData data = new Student_TestData();
    Response GradePage;
    String user_token = data.Student_refresh_Token;
    Database_Connection connect = new Database_Connection();
    Long grade_id;
    String grade_localization_key;
    Integer grade_order;
    String stage_localization_key;

   public GradePage()throws SQLException{
    get_grade_data();
  }
    public void get_grade_data()throws SQLException {
        ResultSet resultSet = connect.connect_to_database("\n" + "\n" +
                "select * from public.countries c join stages s on c.country_id = s.country_id  join grades g on g.stage_id  = s.stage_id \n" +
                "where c.country_iso_code = 'eg' and s.stage_url_text = 'primary' and g.grade_url_text = '2'");
        while (resultSet.next()){
            grade_id = resultSet.getLong("grade_id");
            grade_localization_key = resultSet.getString("grade_localization_key");
            grade_order= resultSet.getInt("grade_order");
            stage_localization_key = resultSet.getString("stage_localization_key");
        }
    }

    @Given("User Send CountryIso Code And Data To Grade Page API")
    public void send_valid_data_to_gradePage(){
        GradePage = test.sendRequest("GET","/web/grade-page?country-iso-code=eg&stage-url-text=primary&grade-url-text=2",null , user_token);
    }
    @Then("Grade Of The Data In Query Param In Grade Page  API Return In Response Body")
    public void validate_valid_data_gradePage_Request(){
        GradePage.prettyPrint();
        GradePage.then().assertThat().statusCode(HttpStatus.SC_OK)
                .body("grade_localization_key",containsString(grade_localization_key))
                .body("grade_id",equalTo(grade_id))
                .body("grade_order",equalTo(grade_order))
                .body("stage_localization_key",containsString(stage_localization_key))
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/NagwaPagesAPIs/GradePage.json")));
    }
    @Given("User send Invalid Data In query params of the request Grade Page")
    public void send_invalid_data_to_grade_page(){
        GradePage = test.sendRequest("GET","/web/grade-page?country-iso-code=aa2&stage-url-text=primary&grade-url-text=2",null , user_token);
    }
    @Then("Response Code Of Grade Page Is 404 And Body Returns With Error Message")
    public void not_Exist_data_response(){
       test.Validate_Error_Messages(GradePage,HttpStatus.SC_NOT_FOUND,"No data found for the specified country or stage or grade",40417);
    }
    @Given("User execute request Grade Page with missing query params")
    public void send_gradePage_request_with_missing_param(){
        GradePage = test.sendRequest("GET","/web/grade-page?stage-url-text=primary&grade-url-text=2",null , user_token);
    }
    @Then("Response code of Grade Page is 400 and body returns with error message")
    public void validate_missing_param_response(){
        test.Validate_Error_Messages(GradePage,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy",4002);

    }


}
