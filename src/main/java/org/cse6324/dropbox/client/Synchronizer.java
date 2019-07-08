package org.cse6324.dropbox.client;

import java.util.Arrays;

import org.cse6324.dropbox.common.FileInfo;

/**
 * Synchronizer
 */
public class Synchronizer {
    String serverURL;
    String rootDirectory;
    String userName;
    LocalRemoteInformationMatcher localRemoteInformationMatcher;
    DropboxHTTPClient httpClient;
    DirectoryInformationManager directoryInformationManager;
    FileInfo[] locaNewFileInfos;

    public Synchronizer(String serverURL, String rootDirectory, String userName) {
        this.serverURL = serverURL;
        this.rootDirectory = rootDirectory;
        this.userName = userName;
        this.httpClient = new DropboxHTTPClient(userName, rootDirectory, serverURL);
        directoryInformationManager = new DirectoryInformationManager(rootDirectory);
    }

    void loopInit() {
        locaNewFileInfos = directoryInformationManager.getNewInfo();
        localRemoteInformationMatcher = new LocalRemoteInformationMatcher(directoryInformationManager, httpClient);
    }

    void addAtClient() {
        FileInfo[] fileInfos = localRemoteInformationMatcher.getFilesToBeAddedToClient();
        System.out.println("addAtClient: " + Arrays.toString(fileInfos));
        for (FileInfo f : fileInfos) {
            httpClient.downloadFile(f.getFilepath());
        }
    }
    void deleteAtClient() {
        FileInfo[] fileInfos = localRemoteInformationMatcher.getFilesToBeDeletedFromClient();
        System.out.println("deleteAtClient: " + Arrays.toString(fileInfos));
        for (FileInfo f : fileInfos) {
            directoryInformationManager.deleteFile(f.getFilepath());
        }
    }
    void addAtServer() {
        FileInfo[] fileInfos = localRemoteInformationMatcher.getFilesToBeAddedToServer();
        System.out.println("addAtServer: " + Arrays.toString(fileInfos));
        for (FileInfo f : fileInfos) {
            httpClient.uploadFile(f.getFilepath());
        }
    }
    void deleteAtServer() {
        FileInfo[] fileInfos = localRemoteInformationMatcher.getFilesToBeDeletedFromServer();
        System.out.println("deleteAtServer: " + Arrays.toString(fileInfos));
        for (FileInfo f : fileInfos) {
            httpClient.deleteFile(f.getFilepath());
        }
    }

    public void startLoop() {
        Runnable runnable = () -> {
            while (true) {
                System.out.println("loop");
                loopInit();
                deleteAtServer();
                deleteAtClient();
                addAtServer();
                addAtClient();
                directoryInformationManager.save(locaNewFileInfos);
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t = new Thread(runnable);
        t.run();
    }
}