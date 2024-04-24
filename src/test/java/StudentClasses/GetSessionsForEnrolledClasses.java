package StudentClasses;

import AdminArea.CreateClass;
import AdminArea.CreateSession;
import TestConfig.Database_Connection;
import TestConfig.TestBase;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class GetSessionsForEnrolledClasses {
        TestBase test = new TestBase();
        Student_TestData data = new Student_TestData();
        Database_Connection connect =new Database_Connection();
        CreateClass classData = new CreateClass();
        CreateSession session = new CreateSession();
        String Student_refresh_token = data.Student_refresh_Token;
        Long student_id = data.student_Id;
        String class_payment_option_name;
        Integer class_payment_option_id;
        Integer class_seats_limit;
        Integer class_block_count;
        Float class_subject_retail_price;
        Float class_subject_session_price;
        Long educator_id;
        Long subject_id;
        Long class_Id;
        Long  session_Id;
        String  session_title;
        String class_title;
        Map<String,Object> pathParams = test.pathParams;
        Response get_sessions_for_enrolled_class;

        @And("Get Session for Enrolled Classes From Database")
        public void get_sessions_of_enrolled_classes_data() throws SQLException {
                ResultSet resultSet = connect.connect_to_database("select * from classes c \n" +
                        "                       join classes_students cs \n" +
                        "                        on c.class_id = cs.class_id \n" +
                        "                        join class_payment_options cpo \n" +
                        "                        on cpo.class_payment_option_id = c.class_payment_option_id \n" +
                        "                        join classes_subjects cs2 \n" +
                        "                        on cs2.class_id = cs.class_id \n" +
                        "                        join classes_subjects_sessions css \n" +
                        "                        on css.class_subject_id = cs2.class_subject_id\n" +
                        "                        join sessions s \n" +
                        "                        on s.session_id = css.session_id \n" +
                        "where cs.student_id ="+ student_id +" and c.class_id ="+ class_Id +"\n");

                while(resultSet.next()){
                    class_Id = resultSet.getLong("class_id");
                    class_title = resultSet.getString("class_title");
                    session_Id=  resultSet.getLong("session_id");
                    session_title = resultSet.getString("session_title");
                    class_payment_option_name = resultSet.getString("class_payment_option_name");
                    class_payment_option_id = resultSet.getInt("class_payment_option_id");
                    class_seats_limit = resultSet.getInt("class_seats_limit");
                    class_block_count = resultSet.getInt("class_block_count");
                    educator_id = resultSet.getLong("educator_id");
                    subject_id = resultSet.getLong("subject_id");
                    class_subject_retail_price = resultSet.getFloat("class_subject_retail_price");
                    class_subject_session_price = resultSet.getFloat("class_subject_session_price");
                }
            System.out.println("educator_id "+educator_id +"class_title "+class_title);

        }

        @Given("user send class contains sessions that user enrolled in")
        public void Get_Sessions_for_Enrolled_Classes () throws SQLException {
            session.Create_Session();
            class_Id = session.Class_ID;
            Response Enroll_Student_Into_Class = test.sendRequest("POST", "/students/"+ student_id +"/classes/"+ class_Id +"/enroll", null,Student_refresh_token);
            Enroll_Student_Into_Class.prettyPrint();

            pathParams.put("studentId", student_id);
            pathParams.put("classId", class_Id);
        }
        @When("Perform then api of get_sessions_for_enrolled_class")
        public void get_sessions_for_enrolled_class () {
            get_sessions_for_enrolled_class =  test.sendRequest("GET", "/students/{studentId}/classes/{classId}/sessions",null,Student_refresh_token);
        }

        @Then("I verify the appearance of status code 200 and all sessions of enrolled class")
        public void Validate_Response_of_Get_Sessions_for_Enrolled_Classes (){
            get_sessions_for_enrolled_class.prettyPrint();
            get_sessions_for_enrolled_class.then()
                    .statusCode(HttpStatus.SC_OK)
                    .assertThat()
                    .body("class_id",equalTo(class_Id),"class_title",hasToString(class_title),
                            "class_payment_option_name",hasToString(class_payment_option_name),"sessions_count",equalTo(1),
                            "class_payment_option_id",equalTo(class_payment_option_id), "class_seats_limit",equalTo(class_seats_limit),
                            "class_block_count",equalTo(null))
                    .body("educators.educator_id",hasItem(equalTo(educator_id)))
                    .body("sessions.session_id",hasItem(equalTo(session_Id)),"sessions.session_title",hasItem(hasToString(session_title))
                            ,"sessions.class_block_number",hasItem(equalTo(null)))
                    .body("subjects.subject_id",hasItem(equalTo(subject_id)),"subjects.class_subject_retail_price",hasItem(equalTo(class_subject_retail_price)),
                            "subjects.class_subject_discounted_price",hasItem(equalTo(null)),"subjects.class_subject_session_price",hasItem(equalTo(class_subject_session_price)));

        }
        @Given("User Send Class that has no sessions")
        public void Get_Class_has_no_sessions () {
            classData.Create_Class_per_session();
            class_Id = classData.Class_ID;
            Response Enroll_Student_Into_Class = test.sendRequest("POST", "/students/"+ student_id +"/classes/"+ class_Id +"/enroll", null,Student_refresh_token);
            Enroll_Student_Into_Class.prettyPrint();
            pathParams.put("studentId", student_id);
            pathParams.put("classId", class_Id);
        }

    @And("Get Empty Session array for Enrolled Classes From Database")
    public void get_EmptySessions_of_enrolled_classes_data() throws SQLException {
        ResultSet resultSet = connect.connect_to_database("select * from classes c \n" +
                "join classes_students cs \n" +
                "on c.class_id = cs.class_id \n" +
                "join classes_educators ce \n" +
                "on ce.class_id = c.class_id \n" +
                "join class_payment_options cpo \n" +
                "on cpo.class_payment_option_id = c.class_payment_option_id \n" +
                "join classes_subjects cs2 \n" +
                "on cs2.class_id = cs.class_id \n" +
                "where cs.student_id ="+ student_id +" and c.class_id ="+ class_Id +"");

        while(resultSet.next()){
            class_Id = resultSet.getLong("class_id");
            class_title = resultSet.getString("class_title");
            class_payment_option_name = resultSet.getString("class_payment_option_name");
            class_payment_option_id = resultSet.getInt("class_payment_option_id");
            class_seats_limit = resultSet.getInt("class_seats_limit");
            class_block_count = resultSet.getInt("class_block_count");
            educator_id = resultSet.getLong("educator_id");
            subject_id = resultSet.getLong("subject_id");
            class_subject_retail_price = resultSet.getFloat("class_subject_retail_price");
            class_subject_session_price = resultSet.getFloat("class_subject_session_price");
        }
        System.out.println("educator_id "+educator_id +"class_title "+class_title);

    }

        @Then("I verify the appearance of status code 200 and empty list of classes")
        public void Validate_Response_for_Class_has_no_sessions () {
            get_sessions_for_enrolled_class.prettyPrint();
            get_sessions_for_enrolled_class.then()
                    .statusCode(HttpStatus.SC_OK)
                    .assertThat()
                    .body("class_id", equalTo(class_Id),"sessions",empty())
                    .body("educators.educator_id",hasItem(equalTo(educator_id)))
                    .body("subjects.subject_id",hasItem(equalTo(subject_id)),"subjects.class_subject_retail_price",hasItem(equalTo(class_subject_retail_price)),
                            "subjects.class_subject_discounted_price",hasItem(equalTo(null)),"subjects.class_subject_session_price",hasItem(equalTo(class_subject_session_price)))
                    .body(JsonSchemaValidator.matchesJsonSchema(new File("src/test/resources/Schemas/StudentClassesSchemas/GetSessionsForEnrolledClasses.json")));
        }
        @Given("user send student is not enrolled in the class")
        public void unauthorized_student () {
            classData.Create_Class_per_session();
            class_Id = classData.Class_ID;
            pathParams.put("studentId", "123456789987");
            pathParams.put("classId", class_Id);
        }
        @Then("I verify the appearance of status code 403 and user unauthorized")
        public void Validate_Response_For_Student_NotEnrolled () {
            Response GetSessionForEnrolledClassesResponse = get_sessions_for_enrolled_class;
            test.Validate_Error_Messages(GetSessionForEnrolledClassesResponse,HttpStatus.SC_FORBIDDEN,"Unauthorized",4031);
        }
        @Given("user send class is not exist")
        public void Class_Not_Found () {
            pathParams.put("studentId", student_id);
            pathParams.put("classId", "123456789098");
        }
        @Then("I verify the appearance of status code 404 and class not found")
        public void Validate_Response_of_Class_Not_Found(){
            Response GetSessionForEnrolledClassesResponse = get_sessions_for_enrolled_class;
            test.Validate_Error_Messages(GetSessionForEnrolledClassesResponse,HttpStatus.SC_NOT_FOUND,"Class not found or not eligible for display.",4046);
        }

    }


