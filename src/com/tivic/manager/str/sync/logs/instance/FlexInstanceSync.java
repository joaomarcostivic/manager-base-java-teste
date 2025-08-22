package com.tivic.manager.str.sync.logs.instance;

import java.io.IOException;
import java.util.List;

import com.tivic.manager.str.sync.logs.FlexSyncConfig;

public interface FlexInstanceSync<T> {
	public void setData(List<T> data);
	public List<T> getData();
	public void setConfig(FlexSyncConfig config);
	public FlexSyncConfig getConfig();
	public void write() throws IOException, Exception;
}
