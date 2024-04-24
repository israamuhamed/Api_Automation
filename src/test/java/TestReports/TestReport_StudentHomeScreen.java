package TestReports;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src/test/resources/Features/StudentHomeScreen"
                ,glue = "StudentHomeScreen"
                , plugin = {"pretty", "html:target/Reports/TestReport_StudentHomeScreen.html"})

public class TestReport_StudentHomeScreen extends AbstractTestNGCucumberTests {
}
