package com.tools;

public class FilePathHandler {
	public static String pathNormalize(String path) {
		if (!path.endsWith("/"))
			path = path + "/";
		return path;
	}
}
