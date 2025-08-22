package com.tivic.manager.util.manipulatefiles;

import java.io.File;
import java.util.ArrayList;

public interface IManipulateFolder {
	void createFolder(String path);
	ArrayList<String> listFiles(File file);
	void removeAllFiles(File fileExist);
}
