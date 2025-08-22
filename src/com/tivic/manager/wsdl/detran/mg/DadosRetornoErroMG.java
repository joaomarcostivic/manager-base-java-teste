package com.tivic.manager.wsdl.detran.mg;

import java.util.ArrayList;

import org.w3c.dom.NodeList;

public class DadosRetornoErroMG extends DadosRetornoMG {
	
	public DadosRetornoErroMG(int codigoRetorno, String mensagemRetorno) {
		super();
		setCodigoRetorno(codigoRetorno);
		setMensagemRetorno(mensagemRetorno);
		setMensagens(new ArrayList<String>());
	}
	
	@Override
	public void importData(NodeList nodeList, String xml) {}
}
