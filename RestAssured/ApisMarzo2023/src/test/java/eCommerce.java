import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class eCommerce {
static private String url_base = "webapi.segundamano.mx";

    // https://webapi.segundamano.mx/urls/v1/public/?lang=es%27%20\



    @Test
    @Order(1)
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Test case: validar el filtrado de categorias")

    public void obtener_Categorias_filtrado_200(){
        RestAssured.baseURI = String.format("https://%s/urls/v1/public/ad-listing",url_base);

        String body_request = "{\n" +
                "\t\"filters\": [{\n" +
                "\t\t\"price\": \"-60000\",\n" +
                "\t\t\"category\": \"2020\"\n" +
                "\t}, {\n" +
                "\t\t\"price\": \"60000-80000\",\n" +
                "\t\t\"category\": \"2020\"\n" +
                "\t}, {\n" +
                "\t\t\"price\": \"80000-100000\",\n" +
                "\t\t\"category\": \"2020\"\n" +
                "\t}, {\n" +
                "\t\t\"price\": \"100000-150000\",\n" +
                "\t\t\"category\": \"2020\"\n" +
                "\t}, {\n" +
                "\t\t\"price\": \"150000-\",\n" +
                "\t\t\"category\": \"2020\"\n" +
                "\t}]\n" +
                "}";

        Response response=given()
                .log().all()
                .filter(new AllureRestAssured())
                .queryParam("lang", "es")
                .contentType("application/json")
                .headers("Accept","application/json,text/plain, */*'")
                .body(body_request)
                .post("/ad-listing");

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);

        //Junit5 - Pruebas

        //validar el status code (200)
        System.out.println("Status response: " + response.getStatusCode());
        assertEquals(200, response.getStatusCode());

        //validar que el body response no venga vacio

        assertNotNull(body_response);

        //validar que el body reponse contenga algunos campos

        assertTrue(body_response.contains("data"));
        assertTrue(body_response.contains("urls"));

        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 2000 );

        String headers_response = response.getHeaders().toString();
        assertTrue(headers_response.contains("application/json"));
    }
    @Test
    @Order(2)
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("test case: Listado de anuncios")

    public void listado_de_anuncios_200(){
        RestAssured.baseURI = String.format("https://%s/highlights/v1",url_base);


        Response response=given()
                .log().all()
                .filter(new AllureRestAssured())
                .queryParam("prio", 1)
                .queryParam("cat", "2020")
                .queryParam("lim", 1)
                .contentType("application/json")
                .headers("Accept","application/json,text/plain, */*'")
                .get("/public/highlights");

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);



    }
}
