package com.tivic.manager.wsdl.detran.mg.builders;

public class AitBuilderSearchProdemge {
	
	protected static boolean validString(String data) {
		if(data != null && !data.trim().equals(""))
			return true;
		
		if(data != null && noZeroFill(data))
			return true;
		
		return false;
	}
	
	protected static boolean validUpdate(String aitData, String retornoData) {
		return !validString(aitData) && validString(retornoData);
	}
	
	protected static boolean noZeroFill(String data) {
		return data.replaceAll("[0]", "").length() > 0;
	}

}
