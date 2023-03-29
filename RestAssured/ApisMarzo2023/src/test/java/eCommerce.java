import com.github.javafaker.Faker;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import jdk.jfr.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


public class eCommerce {
    static private String url_base = "webapi.segundamano.mx";
    static private String email = "unamos@mailinator.com";
    static private String password = "12345";

    static private String account_id = "";
    static private String access_token = "";
    static private String uuid = "";
    static private String ad_id = "";


    //funcion extra
    @Name("obtener token")
    private String obtener_token() {

        RestAssured.baseURI = String.format("https://%s/nga/api/v1.1/private/accounts", url_base);

        String body_request = "{\"account\":{\"email\":\"" + email + "\"}}";

        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .contentType("application/json")
                .auth().preemptive().basic(email, password)
                .queryParam("lang", "es")
                .body(body_request)
                .post();

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response del token: " + body_response);

        JsonPath jsonResponse = response.jsonPath();

        access_token = jsonResponse.get("access_token");
        //pm.environment.set("token", pm.response.json().access_token)
        System.out.println("Token: " + access_token);

        account_id = jsonResponse.get("account.account_id");
        //pm.environment.set("account_id",pm.response.json().account.account_id)
        System.out.println("Account ID: " + account_id);

        uuid = jsonResponse.get("account.uuid");
        //pm.environment.set("uuid", pm.response.json().account.uuid
        System.out.println("UUID: " + uuid);

        return access_token;
    }

    ;


    // https://webapi.segundamano.mx/urls/v1/public/?lang=es%27%20\


    @Test
    @Order(1)
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Test case: validar el filtrado de categorias")

    public void obtener_Categorias_filtrado_200() {
        RestAssured.baseURI = String.format("https://%s/urls/v1/public/ad-listing", url_base);

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
                "}\n";

        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .queryParam("lang", "es")
                .contentType("application/json")
                .headers("Accept", "application/json,text/plain, */*'")
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
        assertTrue(tiempo < 2000);

        String headers_response = response.getHeaders().toString();
        assertTrue(headers_response.contains("application/json"));
    }

    @Test
    @Order(2)
    @Severity(SeverityLevel.TRIVIAL)
    @DisplayName("test case: Listado de anuncios")

    public void listado_de_anuncios_200() {
        RestAssured.baseURI = String.format("https://%s/highlights/v1", url_base);


        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .queryParam("prio", 1)
                .queryParam("cat", "2020")
                .queryParam("lim", 1)
                .contentType("application/json")
                .headers("Accept", "application/json,text/plain, */*'")
                .get("/public/highlights");

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);

    }

    @Test
    @Order(3)
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("test case: Crear usuario")

    public void crear_usuario_401() {
        RestAssured.baseURI = String.format("https://%s/nga/api/v1.1/private/accounts", url_base);

        String new_user = "agente_ventas_test" + (Math.floor(Math.random() * 6789)) + "@mailinator.com";
        String new_password = "12345";
        String body_request = "{\"account\":{\"email\":\"" + new_user + "\"}}";

        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .contentType("application/json")
                .auth().preemptive().basic(new_user, new_password)
                .queryParam("lang", "es")
                .body(body_request)
                .post();

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);

        //Junit5 - Pruebas

        //validar el status code (200)
        System.out.println("Status response: " + response.getStatusCode());
        assertEquals(401, response.getStatusCode());

        //validar que el body response no venga vacio

        assertNotNull(body_response);

        //validar que el body reponse contenga algunos campos

        assertTrue(body_response.contains("ACCOUNT_VERIFICATION_REQUIRED"));
        assertTrue(body_response.contains("error"));

        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 2000);

        String headers_response = response.getHeaders().toString();
        assertTrue(headers_response.contains("application/json"));


    }

    @Test
    @Order(4)
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("test case: Obtener información del usuario")

    public void obtener_info_usuario() {
        RestAssured.baseURI = String.format("https://%s/nga/api/v1.1/private/accounts", url_base);

        String body_request = "{\"account\":{\"email\":\"" + email + "\"}}";

        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .contentType("application/json")
                .auth().preemptive().basic(email, password)
                .queryParam("lang", "es")
                .body(body_request)
                .post();


        String body_response = response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);

        //Junit5 - Pruebas

        //validar el status code (200)
        System.out.println("Status response: " + response.getStatusCode());
        assertEquals(200, response.getStatusCode());

        //validar que el body response no venga vacio

        assertNotNull(body_response);

        //validar que el body reponse contenga algunos campos

        assertTrue(body_response.contains("account_id"));
        assertTrue(body_response.contains("token"));
        assertTrue(body_response.contains("uuid"));

        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 3000);

        String headers_response = response.getHeaders().toString();
        assertTrue(headers_response.contains("application/json"));

//
        //Asignar valores a nuestas variables globales

        JsonPath jsonResponse = response.jsonPath();

        access_token = jsonResponse.get("access_token");
        //pm.environment.set("token,pm.response.json(.access_token)
        System.out.println("Token:" + access_token);

        account_id = jsonResponse.get("account.account_id");
        //pm.environment.set("account_id",pm.response.json().account.account_id)
        System.out.println("Account ID:" + account_id);

        uuid = jsonResponse.get("account.uuid");
        //pm.environment.set("uuid", pm.response.json().account.uuid)
        System.out.println("UUID:" + uuid);

    }
    @Test
    @Order(5)
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("test case: Crear un anuncio")

    public void crear_un_anuncio() {




        String token = obtener_token();
        System.out.println(("Token en la prueba crear anuncio" + token));

        String body_request =  ("{\n" +
                "    \"images\": \"1150672355.jpg\",\n" +
                "    \"category\": \"4100\",\n" +
                "    \"subject\":\"Awesome estampillas para colección\",\n" +
                "    \"body\": \"Awesome Compro estampillas postales para coleccionarlas\",\n" +
                "    \"is_new\": \"0\",\n" +
                "    \"region\": \"13\",\n" +
                "    \"municipality\": \"359\",\n" +
                "    \"area\": \"148134\",\n" +
                "    \"price\": \"608\",\n" +
                "    \"phone_hidden\": \"true\",\n" +
                "    \"show_phone\": \"false\",\n" +
                "    \"contact_phone\": \"716-419-4570\"\n" +
                "}\n");
        RestAssured.baseURI = String.format("https://%s/v2/accounts/%s/up", url_base, uuid);

        Response response = given()
                .log().all()
                .header("x-source","PHOENIX_DESKTOP")
                .header("Accept","*/*")
                .header( "Content-type","application/json")
                .auth().preemptive().basic(uuid,access_token)
                .body(body_request)
                .post();

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);

        //Junit5 - Pruebas

        //validar el status code (200)
        System.out.println("Status response: " + response.getStatusCode());
        assertEquals(200, response.getStatusCode());

        //validar que el body response no venga vacio

        assertNotNull(body_response);

        //validar que el body reponse contenga algunos campos

        assertTrue(body_response.contains("ad_id"));
        assertTrue(body_response.contains("subject"));
        assertTrue(body_response.contains("category"));

        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 5000);

        String headers_response = response.getHeaders().toString();
        assertTrue(headers_response.contains("application/json"));

        JsonPath jsonResponse = response.jsonPath();
        ad_id = jsonResponse.get("data.ad.ad_id");

    }
    @Test
    @Order(6)
    @Severity(SeverityLevel.MINOR)
    @DisplayName("test case: Editar datos del usuario")

    public void editar_datos_del_usuario(){

        //Datos ramdom -fakerjs
        Faker faker = new Faker();
        String name = faker.name().fullName();
        String fname = faker.name().firstName();
        String lastName = faker.name().lastName();

        String randomData = faker.country().name();
        String phone = faker.phoneNumber().cellPhone();

        System.out.println("Nombre completo: " + name);

        String body_request = "{\n" +
                "    \"account\": {\n" +
                "        \"name\": \""+name+"\",\n" +
                "        \"phone\": \""+phone+"\",\n" +
                "        \"professional\": true,\n" +
                "        \"phone_hidden\": true\n" +
                "    }\n" +
                "}";



        String token = obtener_token();
        System.out.println(("Token en la prueba crear anuncio" + token));

        RestAssured.baseURI = String.format("https://%s/nga/api/v1%s?lang=es",url_base,account_id);

        Response response = given()
                .log().all()
                .filter(new AllureRestAssured())
                .contentType("application/json")
                .header("Accept","*/*")
                .header("Authorization","tag:scmcoord.com,2013:api %s"+token)
                .body(body_request)
                .patch();

        String body_response = response.getBody().prettyPrint();
        System.out.println("Response: " + body_response);

        //Junit5 - Pruebas

        //validar el status code (200)
        System.out.println("Status response: " + response.getStatusCode());
        System.out.println("Response: " + body_response);

        //Junit5 - Pruebas

        //validar el status code (200)
        System.out.println("Status response: " + response.getStatusCode());
        assertEquals(200, response.getStatusCode());

        //validar que el body response no venga vacio

        assertNotNull(body_response);

        //validar que el body reponse contenga algunos campos

        assertTrue(body_response.contains("ad_id"));
        assertTrue(body_response.contains("subject"));
        assertTrue(body_response.contains("category"));

        System.out.println("Tiempo respuesta: " + response.getTime());
        long tiempo = response.getTime();
        assertTrue(tiempo < 5000);

        String headers_response = response.getHeaders().toString();
        assertTrue(headers_response.contains("application/json"));




    }

    @Test
    @Order(7)
    public void crear_una_direccion(){

        String token = obtener_token();

        RestAssured.baseURI = String.format("https://%s/addresses/v1/create",url_base);

                Response response = given()
                        .log().all()
                        .filter(new AllureRestAssured())
                        .formParam("contact","agente de ventas")
                        .formParam("phone", "8765893082")
                        .formParam("RFC","XAXX010101000")
                        .formParam("zipCode","36257")
                        .formParam("exteriorInfo","callejon del bosque 10b")
                        .formParam("interiorInfo","3")
                        .formParam("region","13")
                        .formParam("municipality","359")
                        .formParam("area","43447")
                        .formParam("alias", "testing house")
                        .contentType("application/x-www-form-urlencoded")
                        .header("Accept","*/*")
        //application/json, text/plain, */*
                        .auth().preemptive().basic(uuid,token)
                        .post();

        String body_response = response.getBody().prettyPrint();
        System.out.println("BODY BONITO: " + body_response);




    }



    }
