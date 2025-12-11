package utils;


import com.google.cloud.secretmanager.v1.*;
import org.apache.commons.logging.Log;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import static org.testng.AssertJUnit.assertEquals;

public class GetGCPSecret {

    public static String  DefaultVersionId = "latest";
    public GetGCPSecret() throws IOException {

    }

    // Get an existing secret and output to file
    public static void getGCPSecretToFile(String projectId, String secretId,String version,String fileName) throws IOException {
        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        if(!version.isEmpty()){
            version=DefaultVersionId;
        }
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            SecretVersionName secretVersionName = SecretVersionName.of(projectId, secretId, version); // <-- here

            // Access the secret version.
            AccessSecretVersionResponse response = client.accessSecretVersion(secretVersionName);

            String payload = response.getPayload().getData().toStringUtf8();
            //System.out.printf("Plaintext: %s\n", payload);
            // Create the secret.
            Path path = Paths.get(fileName);
            byte[] strToBytes = payload.getBytes();

            Files.write(path, strToBytes);

        }
    }

    public static String getGCPSecretAsString(String projectId, String secretId,String version) throws IOException {
        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        if(!version.isEmpty()){
            version=DefaultVersionId;
        }
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            SecretVersionName secretVersionName = SecretVersionName.of(projectId, secretId, version); // <-- here

            // Access the secret version.
            AccessSecretVersionResponse response = client.accessSecretVersion(secretVersionName);
            SecretVersion version1 = client.getSecretVersion(secretVersionName);
            //System.out.printf("Secret version %s, state %s\n", version1.getName(), version1.getState());

            String payload = response.getPayload().getData().toStringUtf8();
           // System.out.printf("Plaintext: %s\n", payload);
            return payload;
           // return version1.get;
        }
    }




    // List all secrets for a project
    public static void listSecrets(String projectId) throws IOException {
        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            // Build the parent name.
            ProjectName projectName = ProjectName.of(projectId);

            // Get all secrets.
            SecretManagerServiceClient.ListSecretsPagedResponse pagedResponse = client.listSecrets(projectName);

            // List all secrets.
            pagedResponse
                    .iterateAll()
                    .forEach(
                            secret -> {
                                System.out.printf("Secret %s\n", secret.getName());
                            });
        }
    }

   public static void main(String[] args) throws Exception {
    }
}