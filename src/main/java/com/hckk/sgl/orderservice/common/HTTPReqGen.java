package com.hckk.sgl.orderservice.common;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * Created by Sun GuoLei on 2018/06/22.
 */
public class HTTPReqGen {
    private static final Logger logger = LoggerFactory.getLogger(HTTPReqGen.class);

    private RequestSpecification reqSpec;
    private String callString;
    private String callType;
    private String body;
    private Map<String, String> headers = new HashMap<>();
//    private HashMap<String, String> cookieList = new HashMap<>();

    public void setCallString(String callString) {
        this.callString = callString;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * Constructor. Initializes the RequestSpecification (relaxedHTTPSValidation avoids certificate errors).
     */
    public HTTPReqGen() {
        reqSpec = given().relaxedHTTPSValidation();
    }

    /**
     * Performs the request using the stored request data and then returns the response.
     *
     * @return response Response, will contain entire response (response string and status code).
     */
    public Response perform_request() {
        Response response = null;
        try {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                reqSpec.header(entry.getKey(), entry.getValue());
            }
//
//            for (Map.Entry<String, String> entry : cookieList.entrySet()) {
//                reqSpec.cookie(entry.getKey(), entry.getValue());
//            }

            switch (callType) {
                case "GET": {
                    response = reqSpec.get(callString);
                    break;
                }
                case "POST": {
                    response = reqSpec.body(body).post(callString);
                    break;
                }
                case "PUT": {
                    response = reqSpec.body(body).put(callString);
                    break;
                }
                case "DELETE": {
                    response = reqSpec.delete(callString);
                    break;
                }
                default: {
                    logger.error("Unknown call type: [" + callType + "]");
                }
            }
        } catch (Exception e) {
            logger.error("Problem performing request: ", e);
        }
        return response;
    }

}
