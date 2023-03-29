import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class ejemplo_api_test {

    @Test
    public void api_test() {
        RestAssured.baseURI = String.format("https://reqres.in/api/users?page=2");

        Response response = given()
                .log().all()
                .headers("Accept", "*/*")
                .get();

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);

        //Junit5 - Pruebas

        //validar el status code (200)
        System.out.println("Status response: " + response.getStatusCode());
        assertEquals(200, response.getStatusCode());

        //validar que el body response no venga vacio

        assertNotNull(body_response);

        //validar que el body reponse contenga algunos campos

        assertTrue(body_response.contains("email"));
        assertTrue(body_response.contains("total"));

        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 1000 );

        String headers_response = response.getHeaders().toString();
        assertTrue(headers_response.contains("application/json"));


    }



}
