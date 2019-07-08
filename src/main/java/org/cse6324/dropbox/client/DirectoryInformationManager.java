package org.cse6324.dropbox.client;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.cse6324.dropbox.common.FileInfo;
import org.json.JSONArray;

public class DirectoryInformationManager {
	Path rootDirectory;
	Path existingFileInfoPath;
	String directoryInfoFolderName = ".directoryInfo";
	
	public DirectoryInformationManager(String rootDirectoryString) {
		this.rootDirectory = Paths.get(rootDirectoryString);
		existingFileInfoPath = Paths.get(rootDirectoryString, directoryInfoFolderName, "existing.json");		
	}
	
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
			// e.printStackTrace();
		}
		return fileInfoArray;
	}

	public FileInfo[] getSavedInfo() {
		return readFileInfo(existingFileInfoPath);
	}

	public FileInfo[] getNewInfo() {
        List<FileInfo> fileInfos = new ArrayList<>();
        try {
			fileInfos = Files.walk(rootDirectory)
						.filter(f -> {
							boolean isFileFromDirectoryInfo = false;
							try {
								isFileFromDirectoryInfo = f.toFile().getCanonicalPath().contains(directoryInfoFolderName);
							} catch(Exception e) {
								e.printStackTrace();
							}
							return Files.isRegularFile(f) && !isFileFromDirectoryInfo;
						})
						.parallel().map(filepath -> new FileInfo(filepath, rootDirectory))
						.collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileInfos.toArray(new FileInfo[fileInfos.size()]);
	}

	public boolean save(FileInfo[] fileInfos) {
		File existingInfoFile = existingFileInfoPath.toFile();
		if (!existingInfoFile.exists()) {
			existingInfoFile.getParentFile().mkdirs();
			try {
				existingInfoFile.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		JSONArray array = new JSONArray();
		for (FileInfo f : fileInfos) {
			array.put(f.json());
		}
		try {
			FileUtils.writeStringToFile(existingInfoFile, array.toString());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteFile(String filepath) {
		boolean isDeleteSucessful = false;
		try {
			File file = rootDirectory.resolve(filepath).toFile();
			isDeleteSucessful = file.delete();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return isDeleteSucessful;
	}
}
