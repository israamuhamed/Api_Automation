package LookupsData;

import StudentProfile.CreateStudent;
import StudentClasses.Student_TestData;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;


import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class ListSubjectByGrade {
    Database_Connection Connect = new Database_Connection();
    TestBase test = new TestBase();
    Student_TestData data = new Student_TestData();
    CreateStudent student = new CreateStudent();
    Response List_Subject_ByGrade;
    Long grade_id ;
    Long subject_id;
    String subject_name;
    Long language_id;
    Long discipline_id;
    Integer subject_order ;
    Map<String, Object> pathParams = test.pathParams;

    @Given("Performing the Api of List Subject by Grade")
    public void List_Subject_ByGrade() throws SQLException {
        student.get_grade_from_database ();
        grade_id = student.Grade_ID;
        pathParams.put("grade_id",grade_id);
        List_Subject_ByGrade = test.sendRequest("GET", "/grades/{grade_id}/subjects", null, data.Parent_refreshToken);
    }

    @And("Getting Subjects by Grades from database")
    public void getSubjectByGrade () throws SQLException {
        ResultSet resultSet = Connect.connect_to_database(" select * from subjects s where grade_id ="+ grade_id +" and subject_order = 205");

        while (resultSet.next()) {
            subject_id = resultSet.getLong("subject_id");
            grade_id = resultSet.getLong("grade_id");
            subject_name = resultSet.getString("subject_name");
            language_id = resultSet.getLong("language_id");
            discipline_id = resultSet.getLong("discipline_id");
            subject_order = resultSet.getInt("subject_order");
        }
    }

    @Then("I verify the appearance of status code 200 and Subjects returned successfully")
    public void Validate_Response_of_List_Subjects() {
        List_Subject_ByGrade.prettyPrint();
        List_Subject_ByGrade.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/LookupsData/ListSubjectByGrade.json")))
                .body("subjects.subject_id[0]", equalTo(subject_id),"subjects.grade_id[0]", equalTo(grade_id),"subjects.subject_name[0]",hasToString(subject_name),
                        "subjects.language_id[0]",equalTo(language_id),"subjects.subject_order[0]",equalTo(subject_order),
                        "subjects.discipline_id[0]",equalTo(discipline_id));
    }


}
