import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.apache.commons.lang3.SystemUtils;

//Class which handles the extent report system during the test.
class Reports
{
    public static ExtentTest reporter ;

    //General report messages.
    public static final String FUNCTION_START_MSG = "Starting well Test Function: ";
    public static final String FUNCTION_COMPETE_MSG = "Test Function Complete Well! Function is: ";
    public static final String PAGE_FINISH_MSG = "Page TEST FINISHED SUCCESSFULLY! Screenshot is Taken! ";
    public static final String TEST_SUIT_PASS_MESSAGE = "TEST SUITE PASS WELL!! SUITE NAME IS: ";
    public static final String FUNC_ERROR_MSG = " FUNCTION FAILED Because of an Exception! The Error is: ";
    //Web Testing report messages.
    public static final String WEB_ELEMENT_FOUND_MSG = "Web Element Found Well! Locator is: ";
    public static final String WEB_ELEMENT_FOUND_AND_CLICK_MSG = "WEb Element Found and Click Well!! Locator is: ";
    public static final String VALUES_SET_MSG = "Value Set well! Value is: ";
    public static final String FIND_TEXT_MGS = "The set text was found well! Text is: ";
    public static final String TEST_PASS_MSG = "Test Pass! The Found location is: ";
    public static final String TEST_FAIL_MSG = "Test Fail! The Found location is: ";
    public static final String EXPECTED_RES_MSG= ", The Expected Result is: ";
    //DB Testing report messages.
    public static final String CONNECTION_DONE_WELL_MSG = "The connection to DB established well! DB is: ";
    public static final String DATA_GET_FROM_DB_MSG = "Data successfully retrieved from DB! The data is: ";
    public static final String DATA_INSERT_MSG = "Data successfully inserted to DB! The data is: ";
    //REST API Testing report messages.
    public static final String REQUEST_SEND_MSG = "Http request send well! Request is: ";
    public static final String JSON_PATH_MSG = "Json path retrieved well! path is: ";
    public static final String JSON_DATA_MSG = "JSON data retrieved well! data is: ";
    //General report objects and variables.
    private static final String REPORT_PATH = "C:\\Users\\dima\\Last_Run_Report.html";
    private static ExtentReports extent ;
    private static final String[] SYSTEM_KEYS = {"OS", "OS_version","Time Zone"};
    private static String[] SYSTEM_VALUES;
    private static final String TITLE =  "Google Places API, SQL and Google Maps Web Testing";
    private static final String DESCRIPTION =  "Google Places API, SQL and Google Maps Web Testing";

    //Constructor, the only initialize part of mainly static functions.
    // Sets the system properties for the report and initializes reporting objects.
    Reports()
    {
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(REPORT_PATH);
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        reporter = extent.createTest(TITLE, DESCRIPTION);
        SYSTEM_VALUES = new String[]{SystemUtils.OS_NAME, SystemUtils.OS_VERSION,SystemUtils.USER_TIMEZONE};

        for(int i=0;i<SYSTEM_KEYS.length;i++)
        {
            extent.setSystemInfo(SYSTEM_KEYS[i], SYSTEM_VALUES[i]);
        }
    }

    //Function for report flush.
    public static void createReport()
    {
        extent.flush();
    }
}
