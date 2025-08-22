package com.tivic.manager.util;

public class ValidatorUtils {

	public static void validate(Object campo, String mensagemErro) throws Exception{
		if(campo == null) {
			throw new Exception(mensagemErro);
		}
	}

	public static void validateString(String campo, String mensagemErro) throws Exception{
		if(campo == null || campo.trim().equals("")) {
			throw new Exception(mensagemErro);
		}
	}

	public static void validateInteger(Integer campo, boolean includeZero, String mensagemErro) throws Exception{
		if(campo == null || campo < 0 || (includeZero && campo == 0)) {
			throw new Exception(mensagemErro);
		}
	}
	
}
