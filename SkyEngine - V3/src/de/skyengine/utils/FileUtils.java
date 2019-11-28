package de.skyengine.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

	private File directory;
	
	public FileUtils() {
		this.directory = new File(System.getProperty("user.dir"));
	}
	
	public boolean createFile(File file) {
		try {
			return file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean deleteFile(File file) {
		return file.delete();
	}
	
	public boolean deleteDirectory(Path path) {
		try {
			Files.delete(path);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String loadAsString(String file) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String buffer = "";
			while ((buffer = reader.readLine()) != null) {
				result.append(buffer + '\n');
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
	
	public File getDirectory() {
		return directory;
	}
	
	public String getDirPath() {
		return this.directory.getAbsolutePath();
	}
}
