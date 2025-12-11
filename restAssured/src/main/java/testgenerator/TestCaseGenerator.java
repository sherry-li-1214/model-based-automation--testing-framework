package testgenerator;

import org.apache.commons.io.FilenameUtils;
import models.testcases.TestCase;
import utils.FileManager;
import utils.PropertiesReader;
import utils.RESTestException;
import utils.RESTAssuredWriter;
import org.testng.log4testng.Logger;

import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;

/**
 * This example shows how to generate a set of test cases using the template and write them to a file using the RESTAssured writer.
 * <p>
 * The resources for this example are located at src/main/resources/testdefinitions.
 */
public class TestCaseGenerator {

    private static final Logger logger = Logger.getLogger(TestCaseGenerator.class);

    private final static String TargetClassDir = "src/test/java";


    public static RESTAssuredWriter createWriter(String strAPIName, String strTestCaseDefPath, String outputClassName, boolean isInternallDLB, boolean isNeededjwt) {
        RESTAssuredWriter writer = new RESTAssuredWriter(strAPIName, outputClassName, isInternallDLB, isNeededjwt, TargetClassDir, true);
        writer.setAPIName(strAPIName);
        return writer;
    }

    private static boolean isFolder(String path) {
        File f = new File(path);
        if (f.isDirectory() && !( Objects.requireNonNull(f.list()).length == 0)){
            return true;

        }else if(f.isFile()){

           return false;

        }
        return true;
    }

    private static void writeCaseToFile(String defFileName,String outputFileName,String apiName,String internalAPI,String JWTNeeded){
        TestCase[] testCases = TestCaseLoadUtil.extractTestCaseFromFile(defFileName);

        boolean isInternallDLB = true;
        boolean isJWTNeeded = false;
        if (internalAPI!= null && internalAPI!="") {
            isInternallDLB = Boolean.parseBoolean(internalAPI);
        }
         if (JWTNeeded != null && JWTNeeded != "") {
            isJWTNeeded = Boolean.parseBoolean(JWTNeeded);
        }
        // Write (RestAssured) test cases
        RESTAssuredWriter writer = TestCaseGenerator.createWriter(apiName, defFileName, outputFileName, isInternallDLB, isJWTNeeded);
        writer.write(testCases);

        // if (logger.isLoggable(Level.INFO)) {
        String message = String.format("%d test cases generated and written to %s", testCases.length, TargetClassDir);
        logger.info(message);

    }
    public static void main(String[] args) throws RESTestException {
        /**
         * args 0 -API Name
         * args 1: test case definition path,folder or file path
         * args 2: outputClassName  -- only valid for file path. if folders, the same file name will def name will be created.
         * args 2: boolean .  If API in externalBLB , value is true. or eles if false
         * args3 : boolean .  iF need JWT, value is true . or else is false.

         */
        if (args.length < 5) {
            throw new RESTestException("Invalid number of arguments. Expected 5 arguments but received " + args.length);
        }
        String 	targetDirJava = PropertiesReader.getSystemProperty("test.target.dir");
        if(!FileManager.checkIfExists(targetDirJava+args[0].replace("-", "/"))) {
            FileManager.createDir(targetDirJava+args[0].replace("-", "/"));

        }
        //Load test cases from test case json files
        if(isFolder(args[1])){
            // Create target directory for test cases if it does not exist

            // File files = new File(args[1]);
            File[] files=new File(args[1]).listFiles();

            //output file name will similiar as defintion name , set prefix as Test
            for(File file : files) {
               String outputClassName= FilenameUtils.removeExtension(file.getName());
                if(!outputClassName.startsWith("Test")){
                    outputClassName="Test"+outputClassName;
                }
                writeCaseToFile(file.getPath(),outputClassName,args[0],args[3],args[4]);
             }

        } else {
            writeCaseToFile(args[1], args[2],args[0],args[3],args[4]);
        }


    }
}
