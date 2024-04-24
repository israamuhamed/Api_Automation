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

public class CurriculumStructure {

    TestBase test = new TestBase();
    Database_Connection Connect = new Database_Connection();
    Student_TestData data = new Student_TestData();
    Response Curriculum_Structure;
    Long country_id =102123867837L ;
    String country_iso_code;
    String country_localization_key;
    Integer country_order;
    String stage_localization_key;
    Integer stage_id ;
    String stage_color;
    String stage_url_text;
    Integer stage_order;
    Long grade_id;
    String grade_localization_key ;
    String grade_icon_text;
    String grade_url_text ;
    Integer grade_order ;
    Long subject_id ;
    String subject_name ;
    Integer subject_order;
    boolean subject_is_extra_curriculum ;

    @Given("Performing the Api of Curriculum Structure")
    public void Curriculum_Structure() {
        Curriculum_Structure = test.sendRequest("GET", "/web/curriculum_structure", null, data.Parent_refreshToken);
    }

    @And("Getting Countries,Stages,Grades and Subjects from database")
    public void getCurriculum_Structure () throws SQLException {
        ResultSet resultSet = Connect.connect_to_database("select * from countries c \n" +
                "join stages s \n" +
                "on c.country_id = s.country_id \n" +
                "join grades g \n" +
                "on g.stage_id = s.stage_id \n" +
                "join subjects s2 \n" +
                "on s2.grade_id = g.grade_id \n" +
                "where c.country_id = "+ country_id +" and s.stage_order = 1 \n" +
                "and g.grade_order = 1 and s2.subject_order = 1\n");

        while (resultSet.next()) {
            country_id = resultSet.getLong("country_id");
            country_iso_code = resultSet.getString("country_iso_code");
            country_order = resultSet.getInt("country_order");
            country_localization_key = resultSet.getString("country_localization_key");

            stage_localization_key = resultSet.getString("stage_localization_key");
            stage_id = resultSet.getInt("stage_id");
            stage_color = resultSet.getString("stage_color");
            stage_url_text = resultSet.getString("stage_url_text");
            stage_order = resultSet.getInt("stage_order");

            grade_id = resultSet.getLong("grade_id");
            grade_localization_key = resultSet.getString("grade_localization_key");
            grade_icon_text = resultSet.getString("grade_icon_text");
            grade_url_text = resultSet.getString("grade_url_text");
            grade_order = resultSet.getInt("grade_order");

            subject_id = resultSet.getLong("subject_id");
            subject_name = resultSet.getString("subject_name");
            subject_order = resultSet.getInt("subject_order");
            subject_is_extra_curriculum = resultSet.getBoolean("subject_is_extra_curriculum");
        }
    }

    @Then("I verify the appearance of status code 200 and Curriculum Structure returned successfully")
    public void Validate_Response_of_Curriculum_Structure() {
        Curriculum_Structure.prettyPrint();
        Curriculum_Structure.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/LookupsData/CurriculumStructure.json")))
                .body("[0].country_id", equalTo(country_id),"[0].country_iso_code",hasToString(country_iso_code),
                        "[0].country_order",equalTo(country_order),"[0].country_localization_key",hasToString(country_localization_key))
                .body( "[0].stages[0].stage_id",equalTo(stage_id),"[0].stages[0].stage_localization_key",hasToString(stage_localization_key),
                        "[0].stages[0].stage_color",hasToString(stage_color),"[0].stages[0].stage_url_text",hasToString(stage_url_text),
                        "[0].stages[0].stage_order",equalTo(stage_order))
                .body("[0].stages[0].grades.grade_id[0]",equalTo(grade_id),
                        "[0].stages[0].grades.grade_url_text[0]",hasToString(grade_url_text),
                        "[0].stages[0].grades.grade_localization_key[0]",hasToString(grade_localization_key),
                        "[0].stages[0].grades.grade_order[0]",equalTo(grade_order));
    }
}
