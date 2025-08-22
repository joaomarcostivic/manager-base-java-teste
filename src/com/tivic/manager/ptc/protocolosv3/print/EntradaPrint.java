package com.tivic.manager.ptc.protocolosv3.print;

import java.util.HashMap;

public class EntradaPrint {
	private String nmReport;
	private HashMap<String, Object> params;
	
	public EntradaPrint() {
		// TODO Auto-generated constructor stub
	}

	public String getNmReport() {
		return nmReport;
	}

	public void setNmReport(String nmReport) {
		this.nmReport = nmReport;
	}

	public HashMap<String, Object> getParams() {
		return params;
	}

	public void setParams(HashMap<String, Object> params) {
		this.params = params;
	}
	
}
