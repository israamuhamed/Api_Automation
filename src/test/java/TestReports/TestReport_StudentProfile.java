package TestReports;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/Features/StudentProfile"
                ,glue = "StudentProfile"
                , plugin = {"pretty", "html:target/Reports/TestReport_StudentProfile.html"})


public class TestReport_StudentProfile extends AbstractTestNGCucumberTests {
}
