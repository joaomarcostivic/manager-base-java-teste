package com.tivic.manager.util.manipulatefiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PathFolderBuilder implements IPathFolderBuilder{

	private List<String> routerNavigateFolder;
	private String finalPath;
	
	public PathFolderBuilder() {
		this.routerNavigateFolder = new ArrayList<String>();
		this.finalPath = new String();
	}
	
	@Override
	public PathFolderBuilder add(String path) {
		this.routerNavigateFolder.add(path + File.separator);
		return this;
	}
	
	@Override
	public String getPath() {
		for (String path : this.routerNavigateFolder) {
			this.finalPath += path;
		}
		return finalPath;
	}
	
}
