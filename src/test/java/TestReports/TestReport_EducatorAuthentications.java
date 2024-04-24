package TestReports;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = {"src/test/resources/Features/EducatorAuthentication"}
                ,glue = "EducatorAuthentications"
                ,plugin = {"pretty", "html:target/Reports/TestReport_EducatorAuthentications.html"})
public class TestReport_EducatorAuthentications  extends AbstractTestNGCucumberTests {
}
