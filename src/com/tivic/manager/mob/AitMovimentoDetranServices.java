package com.tivic.manager.mob;

import java.sql.Connection;

import com.tivic.manager.seg.AuthData;
import com.tivic.manager.wsdl.AitDetranObject;

import sol.util.Result;

public class AitMovimentoDetranServices extends AitMovimentoServices {

	public static Result save(AitDetranObject aitDetranObject) throws Exception{
		return save(aitDetranObject, null, null);
	}

	public static Result save(AitDetranObject aitDetranObject, AuthData authData) throws Exception{
		return save(aitDetranObject, authData, null);
	}

	public static Result save(AitDetranObject aitDetranObject, AuthData authData, Connection connect) throws Exception{
		
		Result result = save(aitDetranObject.getAitMovimento(), authData, connect);
		if(result.getCode() < 0){
			throw new Exception(result.getMessage());
		}
		
		result = AitServices.save(aitDetranObject.getAit(), authData, connect);
		if(result.getCode() < 0){
			throw new Exception(result.getMessage());
		}
		
		result = ArquivoMovimentoServices.save(aitDetranObject.getArquivoMovimento(), authData, connect);
		if(result.getCode() < 0){
			throw new Exception(result.getMessage());
		}
		
		return result;
	}
	
}
