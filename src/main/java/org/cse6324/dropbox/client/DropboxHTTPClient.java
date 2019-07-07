package org.cse6324.dropbox.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Base64.Encoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.cse6324.dropbox.common.FileInfo;
import org.json.JSONArray;

/**
 * HTTPClient
 */
public class DropboxHTTPClient {
    private String rootDirectory;
    private CloseableHttpClient httpClient = HttpClients.createDefault();
    private String serverURLForUser;
    public DropboxHTTPClient(String username, String rootDirectory, String serverAddress) {
        this.rootDirectory = rootDirectory;
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

    /**
     * @param   filepath  should be the path of file relative to root directory
     */
    boolean uploadFile(String filepath) {
        boolean returnStatus = false;
        HttpPost get = new HttpPost(serverURLForUser + String.format("/file/%s", encode(filepath)));
        try {
            CloseableHttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                returnStatus = true;
            }
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnStatus;
    }

    String encode(String filepath) {
        Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(filepath.getBytes());
    }

    /**
     * @param   filepath  should be the path of file relative to root directory
     */
    public boolean downloadFile(String filepath) {
        boolean returnStatus = false;
        HttpGet get = new HttpGet(serverURLForUser + String.format("/file/%s", encode(filepath)));
        try {
            CloseableHttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                BufferedInputStream bis = new BufferedInputStream(entity.getContent());
                Path localFile = Paths.get(rootDirectory, filepath);
                System.out.println(localFile);
                File file = localFile.toFile();
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                int inByte;
                while((inByte = bis.read()) != -1) bos.write(inByte);
                bis.close();
                bos.close();
                returnStatus = true;
            }
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnStatus;
    }

    /**
     * @param   filepath  should be the path of file relative to root directory
     */
    boolean deleteFile(String filepath) {
        boolean returnStatus = false;
        HttpDelete get = new HttpDelete(serverURLForUser + String.format("/file/%s", encode(filepath)));
        try {
            CloseableHttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                returnStatus = true;
            }
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnStatus;
    }
}