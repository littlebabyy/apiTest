package com.restassured.apitest.demo;

import io.restassured.http.ContentType;
import org.junit.Test;
import org.w3c.dom.events.EventException;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static java.util.Arrays.asList;


public class PracticeRequest {

    //  Practice using Parameters in Request.
    //  Google Book API reference  https://developers.google.com/books/docs/v1/reference/volumes/list
    //  Google Sheets API reference  https://developers.google.com/sheets/reference/rest/

    private static String token = "ya29.CjAgA6VXsseWe5_KRy1w7zHYTMv5cBxaj8JApmkyCAEA0fibo63sR3U1yP5jKjNtBEE";

    @Test
    public void testGoogleBookAPIDataInURL() throws EventException {
        given()
        .when()
                .get("https://www.googleapis.com/books/v1/volumes?q=cucumber&maxResults=2")
        .then()
                .log().all()
                .contentType(ContentType.JSON)
                .assertThat()
                    .statusCode(200);
    }

    @Test
    public void testGoogleBookAPIDataInParameters() throws EventException {
        given()
                .param("q", "cucumber")
                .param("maxResults", 1)
                .param("download", "epub")
                .param("langRestrict", "chinese")
                .param("libraryRestrict", "no-restrict")
        .when()
                .get("https://www.googleapis.com/books/v1/volumes")
        .then()
                .log().all()
                .contentType(ContentType.JSON)
                .assertThat()
                .statusCode(200);

    }

    @Test
    public void testGoogleSheetsAPIDataInBody() throws EventException {
        String postData = "{\n" +
                "  \"range\": \"People!A5:E5\",\n" +
                "  \"majorDimension\": \"ROWS\",\n" +
                "  \"values\": [\n" +
                "  [\"Benjamin\",\n" +
                "  \"Puxley\",\n" +
                "  \"Male\",\n" +
                "  \"Green\",\n" +
                "  \"Apple\"\n" +
                "  ]]\n" +
                "}";

        given()
                .auth().oauth2(this.token)
                .pathParam("spreadsheet_id", "14jSUH8DoGN3k-QqIV6qIocW-ZYlN_RL507SXjYN7AgM")
                .pathParam("range", "People!A5:E5")
                .param("valueInputOption", "USER_ENTERED")
                .body(postData)
                .when()
                .put("https://sheets.googleapis.com/v4/spreadsheets/{spreadsheet_id}/values/{range}")
                .then()
                .log().all()
                .contentType(ContentType.JSON)
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void testGoogleSheetsAPIMapDataInBody() throws EventException {
        Map<String, Object> postData = new HashMap<String, Object>();
        postData.put("range", "Book!A5:E5");
        postData.put("majorDimension", "ROWS");
        postData.put("values", asList(asList("J.K Rowling", "fairy tale", "280", "", "Harry Potter")));

        given()
                .log().all()
                .auth().oauth2(this.token)
                .pathParam("spreadsheet_id", "14jSUH8DoGN3k-QqIV6qIocW-ZYlN_RL507SXjYN7AgM")
                .pathParam("range", "Book!A5:E5")
                .param("valueInputOption", "USER_ENTERED")
                .body(postData)
                .when()
                .put("https://sheets.googleapis.com/v4/spreadsheets/{spreadsheet_id}/values/{range}")
                .then()
                .log().all()
                .contentType(ContentType.JSON)
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void testGoogleSheetsAPIObjectDataInBody() throws EventException {
        RequestData postData = new RequestData();
        postData.setRange("demoSheet!B8:E8");
        postData.setMajorDimension("ROWS");
        postData.setValues(asList(asList("Miya8", "1st May", "Star", "HeHe")));

        given()
                .auth().oauth2(this.token)
                .pathParam("spreadsheet_id", "1bJsN2ji2kZKmOMVqn4eaoxve-qqJVP65nQqX6GIg2i4")
                .pathParam("range", "demoSheet!B8:E8")
                .param("valueInputOption", "USER_ENTERED")
                .body(postData)
        .when()
                .put("https://sheets.googleapis.com/v4/spreadsheets/{spreadsheet_id}/values/{range}")
        .then()
                .log().all()
                .contentType(ContentType.JSON)
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void testGoogleSheetsPost() throws EventException {
        String postData = "{\n" +
                "  \"requests\": [\n" +
                "    {\n" +
                "      \"updateCells\": {\n" +
                "        \"start\": {\n" +
                "          \"sheetId\": 910726641,\n" +
                "          \"rowIndex\": 7,\n" +
                "          \"columnIndex\": 1\n" +
                "        },\n" +
                "        \"rows\": [\n" +
                "          {\n" +
                "            \"values\": [\n" +
                "              {\n" +
                "                \"userEnteredFormat\": {\"backgroundColor\": {\"red\": 0.8,\n" +
                "                  \"green\": 0.9,\n" +
                "                  \"blue\": 0.6}}\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        ],\n" +
                "        \"fields\": \"userEnteredFormat.backgroundColor\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        given()
                .log().all()
                .urlEncodingEnabled(false)
                .pathParam("spreadsheet_id", "1bJsN2ji2kZKmOMVqn4eaoxve-qqJVP65nQqX6GIg2i4")
                .auth().oauth2(this.token)
                .body(postData)
                .when()
                .post("https://sheets.googleapis.com/v4/spreadsheets/{spreadsheet_id}:batchUpdate")
                .then()
                .log().all()
                .contentType(ContentType.JSON)
                .assertThat()
                .statusCode(200);
    }

}