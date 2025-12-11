package utils;

import org.apache.commons.io.FileUtils;
import org.testng.log4testng.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import java.io.IOException;
import java.nio.charset.Charset;

import java.nio.file.Path;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
public class FileManager {

    private static Logger logger = Logger.getLogger(FileManager.class);

    public static Boolean createFileIfNotExists(String path) {
        File file = new File(path);
        try {
            return file.createNewFile();
        } catch (IOException e) {
            logger.error("Exception: ", e);
        }
        return null;
    }

    public static Boolean checkIfExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static Boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }

    // Create target dir if it does not exist
    public static Boolean createDir(String targetDir) {
        if (targetDir.charAt(targetDir.length()-1) != '/')
            targetDir += "/";
        File dir = new File(targetDir);
        return dir.mkdirs();
    }

    public static void deleteDir(String path) {
        File file = new File(path);
        try {
            FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            logger.error("Error deleting dir ");
            logger.error("Exception: ", e);
        }
    }

    public static String readFile(String path) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            logger.error("Exception: ", e);
        }
        return null;
    }

    public static void copyFile(String source, String destination) throws IOException {
        Files.copy(Paths.get(source), Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * This method writes a String into a file. If the file exists, it will be overwritten.
     * @param path the path to the file
     * @param data the text to be written
     */
    public static void writeFile(String path, String data) {
        try {
            Files.write(Paths.get(path), Collections.singleton(data));
        } catch (IOException e) {
            logger.error("Error writing in file: {}"+ path);
            logger.error("Exception: ", e);
        }
    }


    public static void replaceVariablesInFile(String filePath, String oldString, String newString)
    {
        File fileToBeModified = new File(filePath);

        String oldContent = "";

        BufferedReader reader = null;

        FileWriter writer = null;

        try
        {


            reader = new BufferedReader(new FileReader(fileToBeModified));

            //Reading all the lines of input text file into oldContent

            String line = reader.readLine();

            while (line != null)
            {
                oldContent = oldContent + line + System.lineSeparator();

                line = reader.readLine();
            }

            //Replacing oldString with newString in the oldContent

            String newContent = oldContent.replaceAll(oldString, newString);

            //Rewriting the input text file with newContent

            writer = new FileWriter(fileToBeModified);

            writer.write(newContent);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                //Closing the resources

                reader.close();

                writer.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    //replace strings in file by replace mapping
    public static void replaceVariablesInFile(String filePath, HashMap<String,String> variableMap)
    {
        Path fileToBeModified = Paths.get(filePath);
        //   Map<String,String> variableMap = fillMap();
        Stream<String> lines;
        try {
            lines = Files.lines(fileToBeModified,Charset.forName("UTF-8"));
            List<String> replacedLines = lines.map(line -> ReplaceVariables.replaceTag(line,variableMap))
                    .collect(Collectors.toList());
            Files.write(fileToBeModified, replacedLines, Charset.forName("UTF-8"));
            lines.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

