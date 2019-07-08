package org.cse6324.dropbox.common;

import java.io.File;
import java.nio.file.Path;

import org.json.JSONObject;

/**
 * FileInfo
 */
public class FileInfo {
    String filepath;
    String hash;
    Long lastModified;

    public FileInfo(String filepath, String hash, Long lastModified) {
        this.filepath = filepath;
        this.hash = hash;
        this.lastModified = lastModified;
    }

    public FileInfo(Path filepath, Path rootPath) {
        this.filepath = rootPath.relativize(filepath).toString();
        File f = filepath.toFile();
        lastModified = f.lastModified();
        hash = "";
    }

    public FileInfo(JSONObject json) {
        filepath = json.getString("filepath");
        hash = json.getString("hash");
        lastModified = Long.parseLong(json.getString("lastModified"));
    }

    public JSONObject json() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("filepath", filepath);
        jsonObj.put("hash", hash);
        jsonObj.put("lastModified", lastModified.toString());
        return jsonObj;
    }

    @Override
    public String toString() {
        return json().toString();
    }

    boolean isSameAs(FileInfo other) {
        return (
            hasSamePathAs(other) &&
            hash.equals(other.hash) &&
            lastModified.equals(other.lastModified)
        );
    }

    public boolean hasSamePathAs(FileInfo other) {
        return (
            filepath.equals(other.filepath)
        );
    }

    /**
     * @return the filepath
     */
    public String getFilepath() {
        return filepath;
    }

    /**
     * @return the hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * @return the lastModified
     */
    public Long getLastModified() {
        return lastModified;
    }

    static FileInfo example() {
        return new FileInfo("foo/bar", "0x6469796547", Long.valueOf(1));
    }
}