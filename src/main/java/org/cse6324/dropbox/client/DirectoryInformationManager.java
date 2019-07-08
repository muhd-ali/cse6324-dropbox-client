package org.cse6324.dropbox.client;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.cse6324.dropbox.common.FileInfo;
import org.json.JSONArray;

public class DirectoryInformationManager {
	static private FileInfo[] readFileInfo(Path infoFile) {
		FileInfo[] fileInfoArray = new FileInfo[0];
		try {
			String content = FileUtils.readFileToString(infoFile.toFile());
			JSONArray fileInfos = new JSONArray(content);
			fileInfoArray = new FileInfo[fileInfos.length()];
			for (int i = 0; i < fileInfoArray.length; i++) {
				fileInfoArray[i] = new FileInfo(fileInfos.getJSONObject(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileInfoArray;
	}

	FileInfo[] getSavedInfo() {
		Path path = Paths.get(".directoryInfo", "existing");
		return readFileInfo(path);
	}

	FileInfo[] getNewInfo() {
		Path path = Paths.get(".directoryInfo", "existing");
		return readFileInfo(path);
	}

	boolean save(FileInfo[] fileInfos) {
		return false;
	}

	boolean deleteFile(String filepath) {
		boolean isDeleteSucessful = false;
		try {
			File file = new File(filepath);
			isDeleteSucessful = file.delete();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return isDeleteSucessful;
	}
}
