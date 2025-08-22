package com.tivic.manager.wsdl.detran.mg;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tivic.manager.wsdl.interfaces.DadosRetorno;

public abstract class DadosRetornoMG implements DadosRetorno {

	private int codigoRetorno;
	private String mensagemRetorno;
	private String xml;
	private List<String> mensagens;
	
	public DadosRetornoMG() {
		mensagens = new ArrayList<String>();
	}
	
	public DadosRetornoMG(int codigoRetorno, String mensagemRetorno) {
		super();
		this.codigoRetorno = codigoRetorno;
		this.mensagemRetorno = mensagemRetorno;
		mensagens = new ArrayList<String>();
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
	

	public List<String> getMensagens() {
		return mensagens;
	}
	
	public void setMensagens(NodeList mensagensList) {
		for( int i = 0; i  < mensagensList.getLength(); i++){
	       	 Node node = mensagensList.item(i);
	       	 mensagens.add(node.getChildNodes().item(1).getTextContent());//Descriï¿½ï¿½o da mensagem
		}
	}
	
	public void setMensagens(List<String> mensagens) {
		this.mensagens = mensagens;
	}
	

	public String getMensagensJson(){
		String json = "{";
		for(int i = 0; i < getMensagens().size(); i++){
			 String mensagem = getMensagens().get(i).replaceAll("\"", "");
			 json += "\"mensagem_"+i+"\": \""+mensagem+"\", ";
		}
		if(json.length() > 1)
			return json.substring(0, json.length()-2)+"}";
		return "{}";
	}

	
	public abstract void importData(NodeList nodeList, String xml);
}
