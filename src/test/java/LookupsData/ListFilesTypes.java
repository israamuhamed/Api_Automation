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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;

public class ListFilesTypes {
    TestBase test = new TestBase();
    Database_Connection Connect = new Database_Connection();
    Student_TestData data = new Student_TestData();
    Response List_Files_Types;
    Integer file_type_id ;
    String file_type;


    @Given("Performing the Api of List Files Types")
    public void List_Countries() {
        List_Files_Types = test.sendRequest("GET", "/files/types", null, data.Parent_refreshToken);
    }

    @And("Getting Files Types from database")
    public void getFileTypes () throws SQLException {
        ResultSet FileTypes = Connect.connect_to_database("select * from files_types ft where file_type_id = 1");

        while (FileTypes.next()) {
            file_type_id = FileTypes.getInt("file_type_id");
            file_type = FileTypes.getString("file_type");

        }
    }

    @Then("I verify the appearance of status code 200 and file types returned successfully")
    public void Validate_Response_of_List_Countries() {
        List_Files_Types.prettyPrint();
        List_Files_Types.then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/LookupsData/FilesTypes.json")))
                .body("file_type_id[0]", equalTo(file_type_id),"file_type[0]",hasToString(file_type));
    }

}
