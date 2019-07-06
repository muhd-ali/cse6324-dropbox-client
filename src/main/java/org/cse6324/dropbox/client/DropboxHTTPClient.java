package org.cse6324.dropbox.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.HttpClient;
import org.cse6324.dropbox.common.FileInfo;

/**
 * HTTPClient
 */
public class DropboxHTTPClient {
    private String serverAddress;
    private String rootDirectory;
    private String username;
    private HttpClient httpclient = new 
    DropboxHTTPClient(String username, String rootDirectory, String serverAddress) {
        this.username = username;
        this.rootDirectory = rootDirectory;
        this.serverAddress = serverAddress;
    }

    private int sendGet() throws Exception {
        httpcli
        String url = String.format("http://192.168.137.1:8080/%d/directoryinfo/existing", username);

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        int responseCode = con.getResponseCode();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return responseCode;
    }

    FileInfo[] getExistingFiles() {
        return null;
    }

    FileInfo[] getDeletedFiles() {
        return null;
    }

    boolean uploadFile(String filepath) {
        return false;
    }

    boolean downloadFile(String filepath) {
        return false;
    }

    boolean deleteFile(String filepath) {
        return false;
    }
}