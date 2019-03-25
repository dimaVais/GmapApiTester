import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

public class SnapShot
{
    private WebDriver driver;
    private static final String PIC_FORMAT = ".png";

    //Constructor, initialize with web driver.
    SnapShot(WebDriver helperDriver)
    {
        driver = helperDriver;
    }

    //Takes screenshot of the current screen, used for screenshots of the found locations on google maps.
    public String takeScreenShot(String ImagesPath)
    {
        ImagesPath = ImagesPath+getCurrentTime();
        TakesScreenshot takesScreenshot = (TakesScreenshot) this.driver;
        File screenShotFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
        File destinationFile = new File(ImagesPath+PIC_FORMAT);
        try {
            FileUtils.copyFile(screenShotFile, destinationFile);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return ImagesPath+PIC_FORMAT;
    }

    //Private function to get current time for the screenshot name.
    private String getCurrentTime()
    {
        return  String.valueOf(System.currentTimeMillis());
    }
}
