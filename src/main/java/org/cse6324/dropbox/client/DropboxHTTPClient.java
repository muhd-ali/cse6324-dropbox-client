package org.cse6324.dropbox.client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.cse6324.dropbox.common.FileInfo;
import org.json.JSONArray;

/**
 * HTTPClient
 */
public class DropboxHTTPClient {
    private String serverAddress;
    private String rootDirectory;
    private String username;
    private CloseableHttpClient httpClient = HttpClients.createDefault();
    private String serverURLForUser;
    public DropboxHTTPClient(String username, String rootDirectory, String serverAddress) {
        this.username = username;
        this.rootDirectory = rootDirectory;
        this.serverAddress = serverAddress;
        serverURLForUser = String.format("http://%s/%s", serverAddress, username);
    }

    FileInfo[] getInfoFiles(String infoType) {
        HttpGet get = new HttpGet(serverURLForUser + String.format("/directoryinfo/%s", infoType));
        FileInfo[] fileInfos = null;
        try {
            CloseableHttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                String jsonText = EntityUtils.toString(entity);
                JSONArray json = new JSONArray(jsonText);
                fileInfos = new FileInfo[json.length()];
                for (int i = 0; i < json.length(); i++) {
                    fileInfos[i] = new FileInfo(json.getJSONObject(i));
                }
            }
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileInfos;
    }

    public FileInfo[] getExistingFiles() {
        return getInfoFiles("existing");
    }

    public FileInfo[] getDeletedFiles() {
        return getInfoFiles("deleted");
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