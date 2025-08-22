package com.tivic.manager.str.sync.logs;

import com.tivic.sol.conf.ManagerConf;

public class FlexSyncConfig {
	
	private String logPath = ManagerConf.getInstance().get("PATH_FLEX_SYNC", "/tivic/log/flex");
				
	public FlexSyncConfig() { }
	
	public String getLogPath() {
		return logPath;
	}
	
}
