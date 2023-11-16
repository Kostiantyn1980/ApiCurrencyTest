import io.restassured.response.Response;
import static io.restassured.RestAssured.*;

import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;

public class CurrencyConversionApiTest {


@Test
public void responceCodeTest() {
    String baseURL = CurrencyApiConstants.BASE_URL;
    String apiKey = CurrencyApiConstants.API_KEY;

    Response response = given()
            .baseUri(baseURL)
            .queryParam("apikey", apiKey)
            .when()
            .get("/live");

    System.out.println(response.asString());
    response.then().statusCode(200);
}

    @Test
    public void testCurrencyConversionApi() {

        String baseURL = CurrencyApiConstants.BASE_URL;
        String apiKey = CurrencyApiConstants.API_KEY;


        String endpoint = "/live";


        String sourceCurrency = "USD";
        String currencies = "EUR,GBP,JPY";


        Response response = given()
                .baseUri(baseURL)
                .basePath(endpoint)
                .queryParam("apikey", apiKey)
                .queryParam("source", sourceCurrency)
                .queryParam("currencies", currencies)
                .when()
                .get();


       System.out.println("Response Status Code: " + response.getStatusCode());

        response.then().statusCode(200);

        response.then().body("success", equalTo(true));
        response.then().body("timestamp", notNullValue());
        response.then().body("source", equalTo(sourceCurrency));
        response.then().body("quotes.USDEUR", notNullValue());
        response.then().body("quotes.USDGBP", notNullValue());
        response.then().body("quotes.USDJPY", notNullValue());

    }
    @Test
    public void testUnauthorizedAccess() {
        String baseURL = CurrencyApiConstants.BASE_URL;

        String apiKey = "invalid-api-key";

        Response response = given()
                .baseUri(baseURL)
                .queryParam("apikey", apiKey)
                .when()
                .get("/live");

        System.out.println("Response Status Code: " + response.getStatusCode());

        response.then().statusCode(401);

        response.then().body("message", equalTo("Invalid authentication credentials"));
    }
    @Test
    public void testCurrencyRatesForSpecificDate() {
        String baseURL = CurrencyApiConstants.BASE_URL;
        String apiKey = CurrencyApiConstants.API_KEY;
        String endpoint = "/historical";
        String date = "2018-01-01";

        Response response = given()
                .baseUri(baseURL)
                .basePath(endpoint)
                .queryParam("apikey", apiKey)
                .queryParam("date", date)
                .when()
                .get();

        System.out.println("Response Status Code: " + response.getStatusCode());
        response.then().statusCode(200);

        response.then().body("success", equalTo(true));
        response.then().body("timestamp", notNullValue());

        response.then().body("quotes.USDCAD", notNullValue());
        response.then().body("quotes.USDEUR", notNullValue());
        //response.then().body("quotes.USDNIS", notNullValue());
        response.then().body("quotes.USDRUB", notNullValue());
    }

    @Test
    public void testCurrencyConversionWithInvalidDate() {
        String baseURL = CurrencyApiConstants.BASE_URL;
        String apiKey = CurrencyApiConstants.API_KEY;
        String endpoint = "/historical";
        String invalidDate = "invalid-date";

        Response response = given()
                .baseUri(baseURL)
                .basePath(endpoint)
                .queryParam("apikey", apiKey)
                .queryParam("date", invalidDate)
                .when()
                .get();

        System.out.println("Response Status Code: " + response.getStatusCode());


        response.then().statusCode(200);
        response.then().body("success", equalTo(false));


        response.then().body("error.code", equalTo(302));
        response.then().body("error.info", equalTo("You have entered an invalid date. [Required format: date=YYYY-MM-DD]"));
    }

    @Test
    public void testHistoricalConversionWithCurrencies() {
        String baseURL = CurrencyApiConstants.BASE_URL;
        String apiKey = CurrencyApiConstants.API_KEY;
        String endpoint = "/historical";
        String date = "2018-01-01";
        String currencies = "USDEUR,USDGBP";

        Response response = given()
                .baseUri(baseURL)
                .basePath(endpoint)
                .queryParam("apikey", apiKey)
                .queryParam("date", date)
                .queryParam("currencies", currencies)
                .when()
                .get();

        System.out.println("API Request URL: " + baseURL + endpoint);
        System.out.println("API Response Body: " + response.getBody().asString());


        response.then().statusCode(200);
        response.then().body("success", equalTo(false));


        response.then().body("error.code", equalTo(202));
        response.then().body("error.info", equalTo("You have provided one or more invalid Currency Codes. [Required format: currencies=EUR,USD,GBP,...]"));
    }


}





