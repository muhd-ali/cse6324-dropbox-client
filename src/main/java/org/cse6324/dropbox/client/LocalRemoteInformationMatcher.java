package org.cse6324.dropbox.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cse6324.dropbox.common.*;

public class LocalRemoteInformationMatcher {
	private final DirectoryInformationManager directoryInformationManager;
	private final DropboxHTTPClient httpClient;
	
	public LocalRemoteInformationMatcher(
        DirectoryInformationManager directoryInformationManager,
        DropboxHTTPClient httpClient
    ) {
		this.directoryInformationManager = directoryInformationManager;
		this.httpClient = httpClient;
    }

    List<FileInfo> differenceOf(FileInfo[] a, FileInfo[] b) {
        List<FileInfo> diff = new ArrayList<>();
        for (FileInfo fa : a) {
            boolean found = false;
            for (FileInfo fb : b) {
                if (fa.hasSamePathAs(fb)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                diff.add(fa);
            }
        }
        return diff;
    }

    List<FileInfo> updatedInFirst(FileInfo[] a, FileInfo[] b) {
        List<FileInfo> updated = new ArrayList<>();
        // for (FileInfo fa : a) {
        //     for (FileInfo fb : b) {
        //         if (fa.hasSamePathAs(fb)) {
        //             if (fa.getLastModified() > fb.getLastModified()) {
        //                 updated.add(fa);
        //             }
        //         }
        //     }
        // }
        return updated;
    }

    public FileInfo[] getFilesToBeAddedToServer() {
        List<FileInfo> fileInfos = new ArrayList<>();
        fileInfos.addAll(
            differenceOf(
                directoryInformationManager.getNewInfo(),
                httpClient.getExistingFiles()
            )
        );
        fileInfos.addAll(
            updatedInFirst(
                directoryInformationManager.getNewInfo(),
                httpClient.getExistingFiles()
            )
        );
        return fileInfos.toArray(new FileInfo[fileInfos.size()]);
    }

    public FileInfo[] getFilesToBeDeletedFromServer() {
        List<FileInfo> fileInfos = new ArrayList<>();
        fileInfos.addAll(
            differenceOf(
                directoryInformationManager.getSavedInfo(),
                directoryInformationManager.getNewInfo()
            )
        );
        return fileInfos.toArray(new FileInfo[fileInfos.size()]);
    }

    public FileInfo[] getFilesToBeAddedToClient() {
        List<FileInfo> fileInfos = new ArrayList<>();
        fileInfos.addAll(
            differenceOf(
                httpClient.getExistingFiles(),
                directoryInformationManager.getNewInfo()
            )
        );
        fileInfos.addAll(
            updatedInFirst(
                httpClient.getExistingFiles(),
                directoryInformationManager.getNewInfo()
            )
        );
        return fileInfos.toArray(new FileInfo[fileInfos.size()]);
    }

    public FileInfo[] getFilesToBeDeletedFromClient() {
        return httpClient.getDeletedFiles();
    }
}