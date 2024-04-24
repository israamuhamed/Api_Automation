package AdminArea;
import EducatorProfile.Educator_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import com.github.javafaker.Faker;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.hamcrest.Matchers.hasToString;

public class CreateGrade {
    TestBase test = new TestBase();
    Educator_TestData data = new Educator_TestData();
    Database_Connection Connect = new Database_Connection();
    String Admin_token = data.Admin_Token;
    Faker fakeDate = new Faker();
    String Grade_Title = "Test"+fakeDate.country().countryCode2();
    String Grade_url_text = "Test"+fakeDate.country().countryCode2();
    Integer Grade_Order = Integer.valueOf (fakeDate.number().digits(2));
    String grade_localization_key ="grade_" + fakeDate.country().countryCode2();
    Number Stage_id_DB;
    String Grade_title_DB;
    String Grade_url_text_DB;
    Integer Grade_Order_DB;
    Response Create_Grade;
    Response Unauthorized_Educator;

    @Given("Getting grade data from database")
    public void Get_Grades_Data () throws SQLException {
        ResultSet GradeData = Connect.connect_to_database("select * from grades g order by random()\n" +
                "limit 1");

        while (GradeData.next()) {
            Stage_id_DB = GradeData.getLong("stage_id");
            Grade_title_DB = GradeData.getString("grade_title");
            Grade_url_text_DB = GradeData.getString("grade_url_text");
            Grade_Order_DB = GradeData.getInt("grade_order");
            System.out.println("Stage_id_DB "+Stage_id_DB);
            System.out.println("Grade_Order_DB"+Grade_Order_DB);
        }
    }

    @And("Performing the Api of Create Grade With valid data")
    public void Create_Grade(){
        String body_of_create_grade ="{\"grade_title\":\""+Grade_Title+"\"," +
                "\"grade_icon_text\":\"1\"," +
                "\"grade_url_text\":\""+Grade_url_text+"\"," +
                "\"grade_order\":"+Grade_Order+"," +
                "\"grade_localization_key\":\""+grade_localization_key+"\"," +
                "\"stage_id\":"+ Stage_id_DB +"," +
                "\"grade_is_active\":false}";

        Create_Grade = test.sendRequest("POST","/admin/grades",body_of_create_grade,Admin_token);
        System.out.println("body_of_create_grade "+body_of_create_grade);
    }

    @Then("verify the appearance of status code 201 and grade created successfully")
    public void validate_response_of_create_grade_successfully(){
        Create_Grade.prettyPrint();
        Create_Grade.then()
                .statusCode(HttpStatus.SC_CREATED)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/CreateGrade.json")))
                .body("message", hasToString("Grade created successfully."));
    }

    @And("Performing the Api of Create Grade With grade title already exist")
    public void create_grade_with_grade_title_exist (){
        String body_of_grade_with_grade_title_already_exist ="{\"grade_title\":\""+Grade_title_DB+"\"," +
                "\"grade_icon_text\":\"1\"," +
                "\"grade_url_text\":\""+Grade_url_text+"\"," +
                "\"grade_order\":"+Grade_Order+"," +
                "\"grade_localization_key\":\""+grade_localization_key+"\"," +
                "\"stage_id\":"+ Stage_id_DB +"," +
                "\"grade_is_active\":false}";

        Create_Grade = test.sendRequest("POST","/admin/grades",body_of_grade_with_grade_title_already_exist,Admin_token);
        System.out.println("body_of_create_grade_with_grade_title_exist "+body_of_grade_with_grade_title_already_exist);

    }

    @Then("verify the appearance of status code 400 and grade title must be unique per stage")
    public void validate_response_of_create_grade_with_grade_title_exist(){
        test.Validate_Error_Messages(Create_Grade,HttpStatus.SC_BAD_REQUEST,"Create grade failed, 'grade_title' must be unique per stage.",40020);
    }

    @And("Performing the Api of Create Grade With grade url text already exist in same stage")
    public void create_grade_with_grade_url_text_exist(){
        String body_of_grade_with_grade_url_text_exist ="{\"grade_title\":\""+Grade_Title+"\"," +
                "\"grade_icon_text\":\"1\"," +
                "\"grade_url_text\":\""+Grade_url_text_DB+"\"," +
                "\"grade_order\":"+Grade_Order+"," +
                "\"grade_localization_key\":\""+grade_localization_key+"\"," +
                "\"stage_id\":"+ Stage_id_DB +"," +
                "\"grade_is_active\":false}";
        Create_Grade = test.sendRequest("POST","/admin/grades",body_of_grade_with_grade_url_text_exist,Admin_token);
        System.out.println("body_of_grade_with_grade_url_text_exist"+body_of_grade_with_grade_url_text_exist);
    }

    @Then("verify the appearance of status code 400 and grade url text must be unique per stage")
    public void validate_response_of_create_grade_with_grade_url_text_exist(){
        test.Validate_Error_Messages(Create_Grade,HttpStatus.SC_BAD_REQUEST,"Create grade failed, 'grade_url_text' must be unique per stage.",40020);
    }

    @And("Performing the Api of Create Grade With grade order already exist in same stage")
    public void create_grade_with_grade_order_exist(){
        String body_of_grade_with_grade_order_exist ="{\"grade_title\":\""+Grade_Title+"\"," +
                "\"grade_icon_text\":\"1\"," +
                "\"grade_url_text\":\""+Grade_url_text+"\"," +
                "\"grade_order\":"+Grade_Order_DB+"," +
                "\"grade_localization_key\":\""+grade_localization_key+"\"," +
                "\"stage_id\":"+ Stage_id_DB +"," +
                "\"grade_is_active\":false}";
        Create_Grade = test.sendRequest("POST","/admin/grades",body_of_grade_with_grade_order_exist,Admin_token);
        System.out.println("body_of_grade_with_grade_order_exist"+body_of_grade_with_grade_order_exist);
    }

    @Then("verify the appearance of status code 400 and grade order must be unique per stage")
    public void validate_response_of_create_grade_with_grade_order_exist(){
        test.Validate_Error_Messages(Create_Grade,HttpStatus.SC_BAD_REQUEST,"Create grade failed, 'grade_order' must be unique per stage.",40020);
    }

    @Given("Performing the Api of Create Grade With invalid parameter")
    public void create_Grade_with_invalid_body(){
        String invalid_body = "{\"grade_title\":\""+Grade_Title+"\"," +
                "\"grade_icon_text\":\"1\"," +
                "\"grade_url_text\":\""+Grade_url_text+"\"," +
                "\"grade_is_active\":false}";
        Create_Grade = test.sendRequest("POST", "/admin/grades", invalid_body, Admin_token);
    }

    @Then("verify the appearance status code 400 and error message invalid request")
    public void validate_response_of_invalid_body(){
        test.Validate_Error_Messages(Create_Grade,HttpStatus.SC_BAD_REQUEST,"Invalid request. Please check the path parameters and request context for accuracy.",4002);
    }

    @Given("Performing the Api of Create Grade With unauthorized data")
    public void Create_country_with_unauthorized_data(){
        String body_of_create_grade ="{\"grade_title\":\""+Grade_Title+"\"," +
                "\"grade_icon_text\":\"1\"," +
                "\"grade_url_text\":\""+Grade_url_text+"\"," +
                "\"grade_order\":"+Grade_Order+"," +
                "\"grade_localization_key\":\""+grade_localization_key+"\"," +
                "\"stage_id\":"+ Stage_id_DB +"," +
                "\"grade_is_active\":false}";
        Unauthorized_Educator = test.sendRequest("POST", "/admin/countries", body_of_create_grade,data.refresh_token_for_deletedEducator);
    }

    @Then("verify the appearance status code 403 and error message unauthorized")
    public void validate_response_of_unauthorized_token (){

        test.Validate_Error_Messages(Unauthorized_Educator,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
    }

}
