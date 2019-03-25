import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;

public class GetLatAndLngFromPlaces
{
    private static final String QUERY_PARAM = "query";
    private String searchCriteria;
    private String urlPlaces;
    private Float lat;
    private Float lng;
    private static final String LAT_PATH = "results[0].geometry.location.lat";
    private static final String LNG_PATH = "results[0].geometry.location.lng";

    //Constructor, receives url and search criteria which was extracted from the DB.
    GetLatAndLngFromPlaces(String urlPlaces, String searchCriteria)
    {
        this.urlPlaces=urlPlaces;
        this.searchCriteria =searchCriteria;
    }

    //Sends Http get request to Google Places API according to url and search criteria and extracts the wanted data of lat and lng.
    public void getFromApiLatAndLng()
    {
        Reports.reporter.info(Reports.FUNCTION_START_MSG + new Throwable().getStackTrace()[0].getMethodName());
        try {
            Response response = given().
                    param(QUERY_PARAM, searchCriteria).
            when().
                    get(urlPlaces).then().extract().response();
            Reports.reporter.info(Reports.REQUEST_SEND_MSG+this.urlPlaces+QUERY_PARAM+"="+this.searchCriteria);
            JsonPath jsonPath = response.body().jsonPath();
            Reports.reporter.info(Reports.JSON_PATH_MSG+jsonPath);
            this.lat = jsonPath.getFloat(LAT_PATH);
            Reports.reporter.pass(Reports.JSON_DATA_MSG+this.lat);
            this.lng =jsonPath.getFloat(LNG_PATH);
            Reports.reporter.pass(Reports.JSON_DATA_MSG+this.lng);
            Reports.reporter.info(Reports.FUNCTION_COMPETE_MSG + new Throwable().getStackTrace()[0].getMethodName());
        }
        catch (Exception ex) {Reports.reporter.fail(new Throwable().getStackTrace()[0].getMethodName()+Reports.FUNC_ERROR_MSG +ex);}
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }
}
