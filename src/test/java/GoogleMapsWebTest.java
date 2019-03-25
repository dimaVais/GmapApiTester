import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.concurrent.TimeUnit;

public class GoogleMapsWebTest
{
    private WebDriver driver;
    private static final String URL = "https://www.google.com/maps";
    private static final By MAP_COORDINATE_INPUT = By.id("searchboxinput");
    private static final By SEARCH_BAN = By.id("searchbox-searchbutton");
    private static final By FOUND_LOCATION_NAME = By.xpath("//*[@id='pane']/div/div[1]/div/div/div[5]/div/div[1]/span[3]/span[3]");
    private static final String NO_LOCATION = "NO Location";
    private static final String TEST_PASS = "TEST PASS";
    private static final String TEST_FAIL = "TEST FAIL";
    private String compareRes;

    private Float latForTest;
    private Float lngForTest;

    //Constructor, receives web driver, extracted lat and lng from google places API and expected result for adders from remote SQL DB.
    GoogleMapsWebTest(WebDriver helperDriver, Float latForTest, Float lngForTest, String compareRes)
    {
        this.driver = helperDriver;
        this.latForTest=latForTest;
        this.lngForTest =lngForTest;
        this.compareRes = compareRes;
        this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    //Opens Google maps, search for the lat and lng on the map and check the found address.
    public String findLocationFromAPI()
    {
        Reports.reporter.info(Reports.FUNCTION_START_MSG + new Throwable().getStackTrace()[0].getMethodName());
        String foundLocName=NO_LOCATION;
        try {
            driver.get(URL);
            WebElement searchInput = driver.findElement(MAP_COORDINATE_INPUT);
            Reports.reporter.info(Reports.WEB_ELEMENT_FOUND_MSG+MAP_COORDINATE_INPUT);
            searchInput.sendKeys(String.valueOf(this.latForTest) + "," + String.valueOf(this.lngForTest));
            Reports.reporter.info(Reports.VALUES_SET_MSG+String.valueOf(this.latForTest) + "," + String.valueOf(this.lngForTest));
            driver.findElement(SEARCH_BAN).click();
            Reports.reporter.info(Reports.WEB_ELEMENT_FOUND_AND_CLICK_MSG+SEARCH_BAN);
            foundLocName = driver.findElement(FOUND_LOCATION_NAME).getText();
            Reports.reporter.info(Reports.WEB_ELEMENT_FOUND_MSG+FOUND_LOCATION_NAME);
            Reports.reporter.info(Reports.FIND_TEXT_MGS+foundLocName);
            Reports.reporter.info(Reports.FUNCTION_COMPETE_MSG + new Throwable().getStackTrace()[0].getMethodName());
        }
        catch (Exception ex) {Reports.reporter.fail(new Throwable().getStackTrace()[0].getMethodName()+Reports.FUNC_ERROR_MSG +ex);}
        finally {return foundLocName; }
    }

    //Compares the found address with the expected result from the remote SQL DB. Returns test result and status.
    public String[] testLocationSearchOnMap(String foundLoc)
    {
        String[] testRes = new String[2];
        Reports.reporter.info(Reports.FUNCTION_START_MSG + new Throwable().getStackTrace()[0].getMethodName());
        if (foundLoc.equals(this.compareRes)) {
            Reports.reporter.pass(Reports.TEST_PASS_MSG+foundLoc+Reports.EXPECTED_RES_MSG+this.compareRes);
            testRes[0]=TEST_PASS;
            testRes[1]=Reports.TEST_PASS_MSG+foundLoc+Reports.EXPECTED_RES_MSG+this.compareRes;
        }
        else {
            Reports.reporter.fail(Reports.TEST_FAIL_MSG+foundLoc+Reports.EXPECTED_RES_MSG+this.compareRes);
            testRes[0]=TEST_FAIL;
            testRes[1]=Reports.TEST_FAIL_MSG+foundLoc+Reports.EXPECTED_RES_MSG+this.compareRes;
        }
        Reports.reporter.info(Reports.FUNCTION_COMPETE_MSG + new Throwable().getStackTrace()[0].getMethodName());
        return testRes;
    }
}
