import com.aventstack.extentreports.MediaEntityBuilder;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.ArrayList;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainPlacesTest
{
    private static WebDriver driver;
    private static ArrayList<Triple<String,String,String>> allSqlData;
    private static ArrayList<Float[]> allAPIData = new ArrayList<Float[]>();
    private static ArrayList<String[]> allResultData = new ArrayList<String[]>();

    //Before class, initialize report class
    @BeforeClass
    public static void setPlacesTest()
    {
        new Reports();
    }

    //Suite to test data extraction from remote SQL.
    @Test
    public void test01_GetDataFromSql()
    {
        SqlDataGetter getLoc = new SqlDataGetter();
        getLoc.connectToDB();
        getLoc.readDBTable();
        allSqlData = getLoc.getAllDbData();
        Reports.reporter.pass(Reports.TEST_SUIT_PASS_MESSAGE + new Throwable().getStackTrace()[0].getMethodName());
    }

    //Suite to test request to Google places API according to the SQL data, and extracting of wanted values from the API.
    @Test
    public void test02_GetGooglePlacesData()
    {
        for (int i=0; i<allSqlData.size(); i++) {
            GetLatAndLngFromPlaces placeTofind = new GetLatAndLngFromPlaces(allSqlData.get(i).getLeft(), allSqlData.get(i).getMiddle());
            placeTofind.getFromApiLatAndLng();
            allAPIData.add(new Float[]{placeTofind.getLat(), placeTofind.getLng()});
        }
        Reports.reporter.pass(Reports.TEST_SUIT_PASS_MESSAGE + new Throwable().getStackTrace()[0].getMethodName());
    }

    //Suite to test finding the location which retrieved from the API on Google maps, using selenium web driver.
    @Test
    public void test03_TestLocOnGoogleMaps() throws IOException {
        System.setProperty(Consts.DRIVER_TYPE,Consts.DRIVER_PATH);
        driver = new ChromeDriver();
        SnapShot snap = new SnapShot(driver);

        for(int i=0;i<allAPIData.size();i++) {
            GoogleMapsWebTest tester = new GoogleMapsWebTest(driver, allAPIData.get(i)[0], allAPIData.get(i)[1], allSqlData.get(i).getRight());
            String location = tester.findLocationFromAPI();
            String[] currResult = tester.testLocationSearchOnMap(location);
            allResultData.add(currResult);
            Reports.reporter.pass(Reports.PAGE_FINISH_MSG, MediaEntityBuilder.createScreenCaptureFromPath(snap.takeScreenShot(Consts.GENERAL_FILE_PATH)).build()); //Take a screenshot.
        }
        Reports.reporter.pass(Reports.TEST_SUIT_PASS_MESSAGE + new Throwable().getStackTrace()[0].getMethodName());
        driver.close();
    }

    //Suit to test writing of the test status to a remote SQL DB table.
    @Test
    public void test04_WriteTestResultToDb()
    {
        SqlDataGetter setResult = new SqlDataGetter();
        for (int i = 0; i< allResultData.size(); i++) {
            setResult.connectToDB();
            setResult.writeStatusToDb(allResultData.get(i)[0], allResultData.get(i)[1]);
        }
        Reports.reporter.pass(Reports.TEST_SUIT_PASS_MESSAGE + new Throwable().getStackTrace()[0].getMethodName());
    }

    //After class, flush of the reports.
    @AfterClass
    public static void closePlacesTest()
    {
        Reports.createReport();
    }
}
