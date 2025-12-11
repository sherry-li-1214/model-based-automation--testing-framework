package utils;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;

//import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.openapi4j.core.exception.DecodeException;
import org.openapi4j.core.exception.EncodeException;
import org.openapi4j.core.exception.ResolutionException;
import org.openapi4j.core.model.v3.OAI3;
import org.openapi4j.core.util.TreeUtil;
import org.openapi4j.core.validation.ValidationException;
import org.openapi4j.parser.OpenApi3Parser;
import org.openapi4j.parser.OpenApiParser;
import org.openapi4j.parser.model.v3.OpenApi3;
import org.openapi4j.schema.validator.ValidationContext;
import org.openapi4j.schema.validator.ValidationData;
import org.openapi4j.schema.validator.v3.SchemaValidator;
import org.openapi4j.parser.model.v3.Schema;


import org.testng.log4testng.Logger;


import java.net.MalformedURLException;
import java.net.URL;

import java.util.Objects;

public class JsonSchemaValidator {
    private static final String JSON_SCHEMA_FILE_LOCATION = System.getProperty("os.name")
            .toLowerCase().contains("windows") ? "/jsonschemas/" : "\\jsonschemas\\";
    private static final Logger logger = Logger.getLogger(JSONManager.class);

    public static boolean isJsonSchemaValid(String validSchemaName, String json) {
        try {
            JSONObject jsonSchema = new JSONObject(
                    new JSONTokener(Objects.requireNonNull(JsonSchemaValidator.class
                            .getResourceAsStream(JSON_SCHEMA_FILE_LOCATION + validSchemaName))));

            JSONObject jsonSubject = new JSONObject(
                    new JSONTokener(json));

        } catch (JSONException ex) {
            logger.error("Error parsing JSON: {}"+validSchemaName);
            logger.error("Exception: ", ex);
            return false;
        }
        return true;
    }

    public static boolean validateSchemaFromAPISpec(String openAPISpecLoc,String response,String path, String method,String responseCode,String contentType) {
        try {

        URL specURL = new URL("file:" + openAPISpecLoc);
        OpenApi3 api = new OpenApi3Parser().parse(specURL, false);

        JsonNode contentNode = JSONManager.getJsonNodeFromString(response);
        ValidationContext<OAI3> validationContext = new ValidationContext<OAI3>(api.getContext());

        if(contentType==null | contentType=="null" | contentType==""){
            contentType="application/json";
        }
        Schema schema = api.getPath(path).getOperation(method.toLowerCase())
                    .getResponses().get(responseCode).getContentMediaType(contentType).getSchema();


        if(schema !=null && schema.toString()!="") {
            JsonNode schemaNode = schema.toNode();
            SchemaValidator schemaValidator = new SchemaValidator(validationContext, null, schemaNode);
            ValidationData<Void> validation = new ValidationData<>();
            schemaValidator.validate(contentNode, validation);
            if (validation.isValid()) {
                return true;
            } else {
                logger.error("path of "+path +" schema  is not match :expected {} "+schemaNode.toString() +"   actual schema is:{}"+contentNode);
                return false;
            }
        } else {
            logger.error("schema path is not existed in API spec: {}"+path);
           return false;
        }

    } catch(MalformedURLException| ResolutionException| ValidationException| EncodeException ex){
            //ex.printStackTrace();
            logger.error("Error parsing JSON: {}"+ex);
            logger.error("Exception: ", ex);
            return false;
        }
    }
}
