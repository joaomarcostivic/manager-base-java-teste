package com.tivic.manager.mob.alpr;

public class AlprFactory {
	
	public static final String openALPR = "OALPR";
	public static final String cateye = "CATEYE";
	public static final String pumatronix = "PUMATRONIX";
	
	public static AlprService get(String service) {
		switch (service) {
			case openALPR: return new OpenALPRServices();
			case cateye: return new CateyeALPRServices();
	
			default: return null;
		}
	}

}
