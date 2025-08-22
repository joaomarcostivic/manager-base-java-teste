package com.tivic.manager.wsdl.detran.sp;

import com.tivic.manager.wsdl.interfaces.DadosRetorno;

public class DadosRetornoSP implements DadosRetorno {

	private int codigoRetorno;
	private String mensagemRetorno;
	private String linhaAuto;
	
	public DadosRetornoSP(String linhaAuto) {
		super();
		this.linhaAuto = linhaAuto;
	}
	

	public DadosRetornoSP(int codigoRetorno, String mensagemRetorno) {
		super();
		this.codigoRetorno = codigoRetorno;
		this.mensagemRetorno = mensagemRetorno;
	}

	public String getLinhaAuto() {
		return linhaAuto;
	}
	

	public int getCodigoRetorno() {
		return codigoRetorno;
	}

	public void setCodigoRetorno(int codigoRetorno) {
		this.codigoRetorno = codigoRetorno;
	}

	public String getMensagemRetorno() {
		return mensagemRetorno;
	}

	public void setMensagemRetorno(String mensagemRetorno) {
		this.mensagemRetorno = mensagemRetorno;
	}

}
