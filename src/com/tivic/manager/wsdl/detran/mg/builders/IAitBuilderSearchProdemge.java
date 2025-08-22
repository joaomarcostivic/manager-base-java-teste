package com.tivic.manager.wsdl.detran.mg.builders;

import java.sql.Connection;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.wsdl.interfaces.DadosRetorno;

public interface IAitBuilderSearchProdemge {
	
	public void build(Ait ait, DadosRetorno dadosRetorno) throws Exception;

}
