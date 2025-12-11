package testgenerator;

import com.google.gson.Gson;

import java.io.FileReader;
import java.util.HashMap;

import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.google.gson.stream.JsonReader;
import models.testcases.TestCase;

public class TestCaseLoadUtil {


    public static HashMap<String,String > mapping =new HashMap<String,String>();




    public static TestCase[]   extractTestCaseFromFile(String path) {


        if(null == path || path.equalsIgnoreCase(""))
            path = "src/test/resources/testdefinitions/TestCaseTemplate.json";
        try {
            JsonReader reader = new JsonReader(new FileReader(path));
           Gson gson= new GsonBuilder()
                      .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                      .create();
            TestCase[]  reviews =gson.fromJson(reader, TestCase[].class);
            //read file into stream, try-with-resources
            return reviews;
        } catch(java.io.FileNotFoundException e){
            e.printStackTrace();
            return null;
        }

    }

    public static void  main(String[] args){
      TestCase[]  tt=  TestCaseLoadUtil.extractTestCaseFromFile("src/test/resources/testdefinitions/***/PostDownloadURL.json");
    }
}
