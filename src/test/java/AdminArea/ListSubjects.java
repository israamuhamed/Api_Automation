package AdminArea;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import EducatorProfile.Educator_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import static org.hamcrest.Matchers.*;


public class ListSubjects {
    Database_Connection Connect = new Database_Connection();
    Educator_TestData data = new Educator_TestData();
    String Admin_Token = data.Admin_Token;
    TestBase test = new TestBase();
    Integer subjects_count;
    String subject_name;
    Long subject_id;
    Integer subject_order;
    String subject_url_text;
    String subject_icon_s3_key;
    String subject_icon_cdn;
    Long grade_id;
    String grade_title;
    String grade_url_text;
    String grade_localization_key;
    Integer stage_id;
    String stage_url_text;
    String stage_localization_key;
    String country_localization_key;
    Long country_id;
    String country_iso_code;
    String country_currency_iso_code;
    String language_iso_code;
    Response List_Subjects;


    @Given("Getting subjects data from database")
    public void get_subject_data() throws SQLException{

        ResultSet resultSet = Connect.connect_to_database("select * from subjects sb\n" +
                "join languages l \n" +
                "on sb.language_id = l.language_id  \n" +
                "join grades g \n" +
                "on sb.grade_id = g.grade_id \n" +
                "join stages s \n" +
                "on g.stage_id  = s.stage_id \n" +
                "join countries c \n" +
                "on c.country_id = s.country_id \n" +
                "where g.grade_is_active = true  \n" +
                "and s.stage_is_active = true \n" +
                "ORDER BY RANDOM() \n" +
                "LIMIT 1");

        while (resultSet.next()) {
            subject_name              = resultSet.getString("subject_name");
            subject_id                = resultSet.getLong("subject_id");
            subject_order             = resultSet.getInt("subject_order");
            subject_url_text          = resultSet.getString("subject_url_text");
            subject_icon_s3_key       = resultSet.getString("subject_icon_s3_key");
            subject_icon_cdn          = resultSet.getString("subject_icon_cdn");
            grade_id                  = resultSet.getLong("grade_id");
            grade_title               = resultSet.getString("grade_title");
            grade_url_text            = resultSet.getString("grade_url_text");
            grade_localization_key    = resultSet.getString("grade_localization_key");
            stage_id                  = resultSet.getInt("stage_id");
            stage_url_text            = resultSet.getString("stage_url_text");
            stage_localization_key    = resultSet.getString("stage_localization_key");
            country_localization_key  = resultSet.getString("country_localization_key");
            country_id                = resultSet.getLong("country_id");
            country_iso_code          = resultSet.getString("country_iso_code");
            country_currency_iso_code = resultSet.getString("country_currency_iso_code");
            language_iso_code         = resultSet.getString("language_iso_code");
        }
    }


    @Given("Getting subjects count from database")
    public void get_subjects_count() throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select count(subject_id) as total_subjects_count from subjects sb\n" +
                "join grades g \n" +
                "on sb.grade_id = g.grade_id\n" +
                "join stages s \n" +
                "on g.stage_id  = s.stage_id \n" +
                "where g.grade_is_active = true \n" +
                "and s.stage_is_active = true");

        while (resultSet.next()) {
            subjects_count = resultSet.getInt("total_subjects_count");
        }
    }


    @When("Performing the API of List Subjects with Parameters")
    public void list_subjects(){
        List_Subjects = test.sendRequest("Get", "/admin/subjects?country-iso-code="+country_iso_code+"&stage-url-text="+stage_url_text+"&grade-url-text="+grade_url_text+"&subject-language="+language_iso_code+"&subject-id="+subject_id+"&subject-name="+subject_name, null,Admin_Token);
        System.out.println(List_Subjects);

    }


    @When("Performing The API of List Subjects with country, stage and grade not exist")
    public void list_subjects_not_exist(){
        List_Subjects = test.sendRequest("Get", "/admin/subjects?country-iso-code=country&stage-url-text=stage&grade-url-text=grade", null,Admin_Token);
        System.out.println(List_Subjects);
    }


    @When("Performing the API of List Subjects to list All subjects")
    public void list_all_subjects(){
        List_Subjects = test.sendRequest("Get", "/admin/subjects", null,Admin_Token);
    }


    @Then("Subject is returned successfully")
    public void validate_response_of_list_subjects(){
        List_Subjects.prettyPrint();
        List_Subjects.then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/ListSubjects.json")))
                .body("data.subject_name",hasItem(hasToString(subject_name)), "data.subject_id", hasItem(equalTo(subject_id)), "data.subject_order", hasItem(equalTo(subject_order)),
                         "data.subject_url_text", hasItem(anyOf(nullValue(), equalTo(subject_url_text))), "data.subject_icon_s3_key", hasItem(hasToString(subject_icon_s3_key)),
                         "data.subject_icon_cdn", hasItem(hasToString(subject_icon_cdn)), "data.grade_id", hasItem(equalTo(grade_id)), "data.grade_title", hasItem(hasToString(grade_title)),
                         "data.grade_url_text", hasItem(hasToString(grade_url_text)), "data.grade_localization_key", hasItem(hasToString(grade_localization_key)),
                         "data.stage_id", hasItem(equalTo(stage_id)), "data.stage_url_text", hasItem(hasToString(stage_url_text)), "data.stage_localization_key", hasItem(hasToString(stage_localization_key)),
                         "data.country_id", hasItem(equalTo(country_id)), "data.country_iso_code", hasItem(hasToString(country_iso_code)),
                         "data.country_currency_iso_code", hasItem(hasToString(country_currency_iso_code)), "data.subject_language", hasItem(hasToString(language_iso_code)));

    }


    @Then("I should see Status code 404 with error and no subjects found")
    public void list_subjects_not_exist_response(){
        test.Validate_Error_Messages(List_Subjects, HttpStatus.SC_NOT_FOUND, "No subjects found.", 4045);
    }


    @Then("Total number of subjects is returned successfully")
    public void validate_total_subjects_count(){
        List_Subjects.prettyPrint();
        List_Subjects.then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("total_count", equalTo(subjects_count))
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/AdminAreaSchemas/ListSubjects.json")));
    }

}
