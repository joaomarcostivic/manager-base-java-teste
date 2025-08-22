package com.tivic.manager.wsdl.detran.ba;

import java.util.List;

import org.json.JSONObject;
import org.w3c.dom.NodeList;

import com.tivic.manager.wsdl.interfaces.DadosRetorno;

public abstract class DadosRetornoBA implements DadosRetorno {

	private int codigoRetorno;
	private String mensagemRetorno;
	private String xml;
	
	public DadosRetornoBA() {
		// TODO Auto-generated constructor stub
	}
	
	public DadosRetornoBA(int codigoRetorno, String mensagemRetorno) {
		super();
		this.codigoRetorno = codigoRetorno;
		this.mensagemRetorno = mensagemRetorno;
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

	public String getXml() {
		return xml;
	}
	
	public void setXml(String xml) {
		this.xml = xml;
	}
	
	public abstract List<String> getMensagens();
	public abstract void importData(NodeList nodeList, String xml);
	public abstract String exportXml();
	public abstract JSONObject exportJson();
}
