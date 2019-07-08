package org.cse6324.dropbox;

import java.util.Arrays;

import org.cse6324.dropbox.client.DirectoryInformationManager;
import org.cse6324.dropbox.client.DropboxHTTPClient;
import org.cse6324.dropbox.client.LocalRemoteInformationMatcher;
import org.cse6324.dropbox.client.Synchronizer;
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

    static void testsForHttpClient() {
        DropboxHTTPClient httpClient = new DropboxHTTPClient("user0000", "./userdata", "localhost:8080");
        FileInfo[] fileInfos;
        fileInfos = httpClient.getExistingFiles();
        System.out.println(Arrays.toString(fileInfos));
        fileInfos = httpClient.getDeletedFiles();
        System.out.println(Arrays.toString(fileInfos));
        boolean status = httpClient.deleteFile("folder1\\ccs.pdf");
        System.out.println(status);
    }

    static void testsForDirectoryInfoManager() {
        DirectoryInformationManager manager = new DirectoryInformationManager("./userData");
        FileInfo[] fileInfos, newfileInfos;
        newfileInfos = manager.getNewInfo();
        System.out.println("new:" + Arrays.toString(newfileInfos));
        fileInfos = manager.getSavedInfo();
        System.out.println("saved:" + Arrays.toString(fileInfos));
        boolean status = manager.save(newfileInfos);
        System.out.println(status);
        fileInfos = manager.getSavedInfo();
        System.out.println(Arrays.toString(fileInfos));
        // manager.deleteFile("folder1\\ccs.pdf");
    }

    static void testsForLocalRemoteInformationMatcher() {
        // DirectoryInformationManager manager = new DirectoryInformationManager("./userData");
        // LocalRemoteInformationMatcher matcher = new LocalRemoteInformationMatcher(
        //     manager,
        //     new FileInfo[] {
        //         new FileInfo("folder2/file2.txt", "hash", Long.valueOf(1)),
        //         new FileInfo("folder1\\existing.json", "hash", Long.valueOf("1562560947710")),
        //     },
        //     new FileInfo[] {}
        // );
        // FileInfo[] fileInfos;
        // fileInfos = matcher.getFilesToBeAddedToServer();
        // System.out.println(Arrays.toString(fileInfos));
    }

    static void testsForSynchronizer() {
    }



    public static void main(String[] args) {
        String userName = "";
        String serverURL = "";
        String rootDirectory = "";
		if(args.length==3) {
			userName = args[0];
			serverURL = args[1];
			rootDirectory = args[2];
		}
		else {
            System.err.println("usage: client [userName] [serverURL] [rootDirectory]");
            return;
		}

		System.out.println("userName: "+ userName);
		System.out.println("serverURL: "+ serverURL);
		System.out.println("rootDirectory: "+ rootDirectory);
        Synchronizer synchronizer = new Synchronizer(serverURL, rootDirectory, userName);
        synchronizer.startLoop();
    }
}
