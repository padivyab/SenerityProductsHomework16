package com.products.productsinfo;

import com.products.constants.EndPoints;
import com.products.model.ProductsPojo;
import com.products.testbase.TestBase;
import com.products.utils.TestUtils;
import cucumber.api.java.en.Given;
import io.restassured.http.ContentType;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

import static net.serenitybdd.rest.RestRequests.given;
import static org.hamcrest.Matchers.hasValue;

public class ProductsCRUDTest extends TestBase {

    static String name = "Dyson" + TestUtils.getRandomValue();
    static String type = "Vaccume" + TestUtils.getRandomValue();
    static int price = 3576;
    static String upc = "041333216016";
    static int shipping = 567;
    static String description = "Power Preserve technology";
    static String manufacture = "dyson company";
    static String model = "MN1300R4Z";

    static int productid;

    @Title("This will create a new product")
    @Test
    public void test001()
    {
        ProductsPojo productsPojo =new ProductsPojo();
        productsPojo.setName(name);
        productsPojo.setType(type);
        productsPojo.setPrice(price);
        productsPojo.setUpc(upc);
        productsPojo.setShipping(shipping);
        productsPojo.setDescription(description);
        productsPojo.setManufacturer(manufacture);
        productsPojo.setModel(model);

        SerenityRest.given().log().all()
                .contentType(ContentType.JSON)
                .body(productsPojo)
                .when()
                .post()
                .then().log().all().statusCode(201);

    }

    @Test
    public void test002()
    {
        String p1 = "data.findAll{it.name='";
        String p2 = "'}.get(0)";

        //HashMap<String,Object> productMap = SerenityRest.given().log().all()
        HashMap<String,Object> productMap = SerenityRest.given().log().all()
                .when()
                //.get(EndPoints.GET_ALL_PRODUCT)
                .get()
                .then()
                .statusCode(200)
                .extract()
                .path(p1+name+p2);
                //.path("data.findAll{it.name='"+name+"'}.get(0)");
        Assert.assertThat(productMap, hasValue(name));
        productid = (int) productMap.get("id");
    }
    @Test
    public void test003()
    {
        name = name+"update";

        ProductsPojo productsPojo =new ProductsPojo();
        productsPojo.setName(name);
        productsPojo.setType(type);
        productsPojo.setPrice(price);
        productsPojo.setUpc(upc);
        productsPojo.setShipping(shipping);
        productsPojo.setDescription(description);
        productsPojo.setManufacturer(manufacture);
        productsPojo.setModel(model);

        SerenityRest.given().log().all()
                //.contentType(ContentType.JSON)
                .header("Content-Type","application/json; charset=UTF-8")
                .pathParam("productid",productid)
                .body(productsPojo)
                .when()
                .put(EndPoints.UPDATE_PRODUCT_BY_ID)
                .then().log().all().statusCode(200);

        String p1 = "data.findAll{it.name='";
        String p2 = "'}.get(0)";
        HashMap<String,Object> productMap = SerenityRest.given().log().all()
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .path(p1+name+p2);
        Assert.assertThat(productMap, hasValue(name));



    }

    @Title("Delete the product and verify if the product is deleted")
    @Test
    public void test004()
    {
        SerenityRest.given()
                .pathParam("productid",productid)
                .when()
                .delete(EndPoints.DELETE_PRODUCT_BY_ID)
                .then()
                .statusCode(200);
        given().log().all()
                .pathParam("productid",productid)
                .when()
                .get()
                .then()
                .statusCode(404);

    }
}

