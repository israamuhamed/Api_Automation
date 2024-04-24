package AdminArea;

import EducatorProfile.Educator_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import com.github.javafaker.Faker;
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

public class     CreateCountry {
    TestBase test = new TestBase();
    Educator_TestData data = new Educator_TestData();
    Database_Connection Connect = new Database_Connection();
    String Admin_token = data.Admin_Token;
    Faker fakeDate = new Faker();
    String country_dial_code = "+" + fakeDate.number().digits(3);
    Integer Country_Order = Integer.valueOf(fakeDate.number().digits(3));
    String country_currency =fakeDate.country().currencyCode();
    String Country_Localization_Key ="country_" + fakeDate.country().countryCode2();
    String Country_ISO_Code = fakeDate.country().countryCode2();
    String country_iso_code_DB;
    Integer country_order_DB;
    String country_dial_code_DB;
    String country_localization_key_DB;
    Response Create_Country;
    Response Unauthorized_Educator;
    String body_of_Create_Country = "{\"country_iso_code\":\""+Country_ISO_Code+"\"," +
            "\"country_dial_code\":\""+country_dial_code+"\"," +
            "\"country_order\":"+Country_Order+"," +
            "\"country_currency_iso_code\":\""+country_currency+"\"," +
            "\"country_requires_mobile\":false," +
            "\"country_flag_s3_bucket\":\"https://contents-live.nagwa.com/\"," +
            "\"country_flag_s3_key\":\"content\"," +
            "\"country_flag_cdn\":\"test-cdn\"," +
            "\"country_localization_key\":\""+Country_Localization_Key+"\"}";


    @Given("Performing the Api of Create Country With valid data")
    public void Create_Country () {
    Create_Country = test.sendRequest("POST", "/admin/countries", body_of_Create_Country, Admin_token);
    }

    @Then("I verify the appearance of status code 200 and country created successfully")
    public void Validate_Response_of_create_Country_successfully() {
        Create_Country.prettyPrint();
        Create_Country.then()
                .statusCode(HttpStatus.SC_CREATED)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/CreateCountry.json")))
                .body("message", hasToString("Country created successfully."));
    }

    @Given("Getting data of countries from database")
    public void getCountryData () throws SQLException {
        ResultSet countryData = Connect.connect_to_database("select * from countries c order by random()\n" +
                "limit 1");

        while (countryData.next()) {
            country_iso_code_DB = countryData.getString("country_iso_code");
            country_order_DB = countryData.getInt("country_order");
            country_dial_code_DB = countryData.getString("country_dial_code");
            country_localization_key_DB =countryData.getString("country_localization_key");

        }
    }

    @When("Performing the Api of Create Country With country code already existed")
    public void Create_Country_with_country_iso_code_exist(){
        String Create_Country_with_country_iso_code_already_existed = "{\"country_iso_code\":\""+country_iso_code_DB+"\"," +
                "\"country_dial_code\":\""+country_dial_code+"\"," +
                "\"country_order\":"+Country_Order+"," +
                "\"country_currency_iso_code\":\""+country_currency+"\"," +
                "\"country_requires_mobile\":false," +
                "\"country_flag_s3_bucket\":\"https://contents-live.nagwa.com/\"," +
                "\"country_flag_s3_key\":\"content\"," +
                "\"country_flag_cdn\":\"test-cdn\"," +
                "\"country_localization_key\":\""+Country_Localization_Key+"\"}";

        Create_Country = test.sendRequest("POST", "/admin/countries", Create_Country_with_country_iso_code_already_existed, Admin_token);

    }

    @Then("verify the appearance of status code 400 and error message With country code already existed")
    public void Validate_Response_of_country_code_already_exist(){
        test.Validate_Error_Messages(Create_Country,HttpStatus.SC_BAD_REQUEST,"Create country failed, 'country_iso_code' is related to another country.",40023);
    }

    @When("Performing the Api of Create Country With country order already existed")
    public void create_country_with_country_order_exist(){
        String Create_Country_with_country_order_already_existed = "{\"country_iso_code\":\""+Country_ISO_Code+"\"," +
                "\"country_dial_code\":\""+country_dial_code+"\"," +
                "\"country_order\":"+country_order_DB+"," +
                "\"country_currency_iso_code\":\""+country_currency+"\"," +
                "\"country_requires_mobile\":false," +
                "\"country_flag_s3_bucket\":\"https://contents-live.nagwa.com/\"," +
                "\"country_flag_s3_key\":\"content\"," +
                "\"country_flag_cdn\":\"test-cdn\"," +
                "\"country_localization_key\":\""+Country_Localization_Key+"\"}";

        Create_Country = test.sendRequest("POST", "/admin/countries", Create_Country_with_country_order_already_existed, Admin_token);
    }

    @Then("verify the appearance of status code 400 and error message With country order already existed")
    public void Validate_Response_of_country_order_already_existed(){
        test.Validate_Error_Messages(Create_Country,HttpStatus.SC_BAD_REQUEST,"Create country failed, 'country_order' is related to another country.",40023);
    }

    @When("Performing the Api of Create Country With country dial code already existed")
    public void create_country_with_country_dial_code_exist(){
        String Create_Country_with_country_dial_code_already_existed = "{\"country_iso_code\":\""+Country_ISO_Code+"\"," +
                "\"country_dial_code\":\""+country_dial_code_DB+"\"," +
                "\"country_order\":"+Country_Order+"," +
                "\"country_currency_iso_code\":\""+country_currency+"\"," +
                "\"country_requires_mobile\":false," +
                "\"country_flag_s3_bucket\":\"https://contents-live.nagwa.com/\"," +
                "\"country_flag_s3_key\":\"content\"," +
                "\"country_flag_cdn\":\"test-cdn\"," +
                "\"country_localization_key\":\""+Country_Localization_Key+"\"}";

        Create_Country = test.sendRequest("POST", "/admin/countries", Create_Country_with_country_dial_code_already_existed, Admin_token);
    }

    @Then("verify the appearance of status code 400 and error message With country dial code already existed")
    public void Validate_Response_of_country_dial_code_already_existed(){
        test.Validate_Error_Messages(Create_Country,HttpStatus.SC_BAD_REQUEST,"Create country failed, 'country_dial_code' is related to another country.",40023);
    }

    @When("Performing the Api of Create Country With country localization key already existed")
    public void create_country_with_country_localization_key_existed(){
        String Create_Country_with_country_dial_code_already_existed = "{\"country_iso_code\":\""+Country_ISO_Code+"\"," +
                "\"country_dial_code\":\""+country_dial_code+"\"," +
                "\"country_order\":"+Country_Order+"," +
                "\"country_currency_iso_code\":\""+country_currency+"\"," +
                "\"country_requires_mobile\":false," +
                "\"country_flag_s3_bucket\":\"https://contents-live.nagwa.com/\"," +
                "\"country_flag_s3_key\":\"content\"," +
                "\"country_flag_cdn\":\"test-cdn\"," +
                "\"country_localization_key\":\""+country_localization_key_DB+"\"}";

        Create_Country = test.sendRequest("POST", "/admin/countries", Create_Country_with_country_dial_code_already_existed, Admin_token);
    }

    @Then("verify the appearance of status code 400 and error message With country localization key already existed")
    public void Validate_Response_of_country_localization_key_already_existed(){
        test.Validate_Error_Messages(Create_Country,HttpStatus.SC_BAD_REQUEST,"Create country failed, 'country_localization_key' is related to another country.",40023);
    }

    @Given("Performing the Api of Create Country With invalid parameter")
    public void create_country_with_invalid_body(){
        String invalid_body = "{\"country_iso_code\":\""+Country_ISO_Code+"\"," +
                "\"country_dial_code\":\""+country_dial_code+"\"," +
                "\"country_localization_key\":\""+country_localization_key_DB+"\"}";
        Create_Country = test.sendRequest("POST", "/admin/countries", invalid_body, Admin_token);

    }

    @Then("verify the appearance of status code 400 and error message invalid request")
    public void validate_response_of_invalid_body(){
        test.Validate_Error_Messages(Create_Country,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);

    }

    @Given("Performing the Api of Create Country With unauthorized data")
    public void Create_country_with_unauthorized_data(){
        Unauthorized_Educator = test.sendRequest("POST", "/admin/countries", body_of_Create_Country,data.refresh_token_for_deletedEducator);
    }

    @Then("verify the appearance of status code 403 and error message unauthorized")
    public void validate_response_of_unauthorized_token (){

        test.Validate_Error_Messages(Unauthorized_Educator,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);

    }

}





