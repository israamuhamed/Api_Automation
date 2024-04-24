package NagwaPagesAPIs;

import EducatorProfile.Educator_TestData;
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

public class SubjectPage {
    TestBase test = new TestBase();

    Student_TestData data = new Student_TestData();

    Educator_TestData data_admin = new Educator_TestData();
    Response SubjectsPage;
    Long student_id = data.student_Id;
    String user_token = data.Student_refresh_Token;
    Database_Connection connect = new Database_Connection();
    Long subject_id;
    String subject_name;
    Long country_id;
    String country_iso_code;

    public SubjectPage() throws SQLException{
        get_data ();
    }

    public void get_data () throws SQLException {
        ResultSet resultSet = connect.connect_to_database("select * from public.countries c join stages s on s.country_id = c.country_id join grades g on g.stage_id = s.stage_id join subjects s2 \n" +
                "on s2.grade_id  = g.grade_id \n" +
                "where c.country_iso_code  = 'eg' and s2.subject_url_text  = 'mathematics-en' and g.grade_url_text  = '10' and s.stage_url_text  = 'secondary'  \n" +
                "");
        while (resultSet.next()){
            subject_id = resultSet.getLong("subject_id");
            subject_name= resultSet.getString("subject_name");
            country_id = resultSet.getLong("country_id");
            country_iso_code= resultSet.getString("country_iso_code");
        }

    }
 @Given("User Send Valid Country Iso Code To The Request")
  public void enter_country_iso_code_subjectPage(){
         SubjectsPage = test.sendRequest("GET","web/subject-page?country-iso-code=eg&stage-url-text=secondary&grade-url-text=10&subject-url-text=mathematics-en", null , data_admin.refresh_token);
    }
 @Then("All Subjects Of The Country Return In Response Body")
 public void validate_subject_of_country(){
     SubjectsPage.prettyPrint();
     SubjectsPage.then().assertThat().statusCode(HttpStatus.SC_OK)
             .body("subject_name",containsString(subject_name))
             .body("subject_id",equalTo(subject_id))
             .body("country_id",equalTo(country_id))
             .body("country_iso_code",containsString(country_iso_code))
             .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/NagwaPagesAPIs/SubjectPage.json")));
 }
 @Given("User send validId and data in query params of the request")
    public void enter_student_id_in_query_param(){
     SubjectsPage = test.sendRequest("GET","web/subject-page?country-iso-code=eg&stage-url-text=secondary&grade-url-text=10&subject-url-text=mathematics-en&student_id="+student_id, null , user_token);

 }
 @Then("Subjects should return and info of the student")
    public void validate_response_of_Request_with_student(){
        SubjectsPage.prettyPrint();
 }
 @Given("User execute request with missing query params")
    public void send_missing_queryParams(){
     SubjectsPage = test.sendRequest("GET","web/subject-page?country-iso-code=eg", null , data_admin.refresh_token);
 }
 @Then("Response code is 400 and body returns with error message")
    public void validate_response_missing_param(){
        test.Validate_Error_Messages(SubjectsPage , HttpStatus.SC_BAD_REQUEST , "Invalid request. Please check the path parameters and request context for accuracy",4002);
 }
 @Given("User enter invalid value in query params")
    public void enter_invalid_queryParam_value(){
     SubjectsPage = test.sendRequest("GET","web/subject-page?country-iso-code=null&stage-url-text=secondary&grade-url-text=10&subject-url-text=mathematics-en&student_id="+student_id, null , user_token);
 }
 @Then("Response code of Subject Page is 404 and body returns with error message that no subjects found")
    public void validate_invalid_value_queryparam_response(){
        test.Validate_Error_Messages(SubjectsPage,HttpStatus.SC_NOT_FOUND , "No subjects found.",4045);
 }



}
