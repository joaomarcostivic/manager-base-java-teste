package com.tivic.manager.wsdl.detran.sp.incluirautoinfracao;

import com.tivic.manager.wsdl.detran.sp.DadosRetornoSP;

public class IncluirAutoInfracaoDadosRetorno extends DadosRetornoSP {

	public IncluirAutoInfracaoDadosRetorno(String linhaAuto) {
		super(linhaAuto);
	}
	

	@Override
	public String toString() {
		return "{\"codigoRetorno\":" + getCodigoRetorno() 
				+ ", \"mensagemRetorno\": \"" + getMensagemRetorno() + "\""
				+ ", \"linhaAuto\": \"" + getLinhaAuto() + "\"}";
	}
	
}
