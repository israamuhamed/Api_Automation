package TestReports;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/Features/EducatorsSessionsActions"
                ,glue = "EducatorsSessionsActions"
                ,plugin = {"pretty", "html:target/Reports/TestReport_EducatorsSessionsActions.html"})

public class TestReport_EducatorsSessionsActions extends AbstractTestNGCucumberTests {
}
