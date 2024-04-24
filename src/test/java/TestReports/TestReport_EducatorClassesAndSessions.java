package TestReports;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = {"src/test/resources/Features/EducatorsClassesAndSessions"}
                ,glue = "EducatorsClassesAndSessions"
                ,plugin = {"pretty", "html:target/Reports/TestReport_EducatorClassesAndSessions.html"})

public class TestReport_EducatorClassesAndSessions  extends AbstractTestNGCucumberTests {
}
