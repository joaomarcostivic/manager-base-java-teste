package com.tivic.manager.util;

import java.util.HashMap;

public class ProcessManager extends sol.admin.ProcessManager {

	public static HashMap<String,Object> getInfoProcess(int codeProcess) {
		return sol.admin.ProcessManager.getInfoProcess(codeProcess);
	}

}