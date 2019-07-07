package org.cse6324.dropbox;

import java.util.Arrays;

import org.cse6324.dropbox.client.DropboxHTTPClient;
import org.cse6324.dropbox.common.FileInfo;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        DropboxHTTPClient httpClient = new DropboxHTTPClient("user0000", "./userdata", "localhost:8080");
        FileInfo[] fileInfos;
        fileInfos = httpClient.getExistingFiles();
        System.out.println(Arrays.toString(fileInfos));
        fileInfos = httpClient.getDeletedFiles();
        System.out.println(Arrays.toString(fileInfos));
        boolean status = httpClient.deleteFile("folder1\\ccs.pdf");
        System.out.println(status);
    }
}
