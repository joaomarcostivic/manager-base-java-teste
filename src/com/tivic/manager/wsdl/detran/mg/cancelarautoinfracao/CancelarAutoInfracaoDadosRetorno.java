package com.tivic.manager.wsdl.detran.mg.cancelarautoinfracao;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.detran.mg.DadosRetornoMG;

public class CancelarAutoInfracaoDadosRetorno extends DadosRetornoMG {

	private int codigoRetorno;
	private String mensagemRetorno;
	private String numeroProcessamento;
	private String placa;
	private int codigoOrgaoAutuador;
	private String ait;
	private int codigoInfracao;
	private int indicadorExigibilidade;
	
	public CancelarAutoInfracaoDadosRetorno() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	public CancelarAutoInfracaoDadosRetorno(int codigoRetorno, String mensagemRetorno, String numeroProcessamento,
			String placa, int codigoOrgaoAutuador, String ait, int codigoInfracao, int indicadorExigibilidade) {
		super();
		this.codigoRetorno = codigoRetorno;
		this.mensagemRetorno = mensagemRetorno;
		this.numeroProcessamento = numeroProcessamento;
		this.placa = placa;
		this.codigoOrgaoAutuador = codigoOrgaoAutuador;
		this.ait = ait;
		this.codigoInfracao = codigoInfracao;
		this.indicadorExigibilidade = indicadorExigibilidade;
	}


	@Override
	public String toString() {
		return "{\"codigoRetorno\":" + getCodigoRetorno() 
				+ ", \"mensagemRetorno\": \"" + getMensagemRetorno() + "\""
				+ ", \"numeroProcessamento\": \"" + getNumeroProcessamento() + "\"" 
				+ ", \"placa\": \"" + getPlaca() + "\""
				+ ", \"codigoOrgaoAutuador\": " + getCodigoOrgaoAutuador() 
				+ ", \"ait\": \"" + getAit() + "\"" 
				+ ", \"codigoInfracao\": " + getCodigoInfracao()
				+ ", \"indicadorExigibilidade\": " + getIndicadorExigibilidade() 
				+ ", \"mensagens\": " + getMensagensJson() + "}";
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



	public String getNumeroProcessamento() {
		return numeroProcessamento;
	}



	public void setNumeroProcessamento(String numeroProcessamento) {
		this.numeroProcessamento = numeroProcessamento;
	}



	public String getPlaca() {
		return placa;
	}



	public void setPlaca(String placa) {
		this.placa = placa;
	}



	public int getCodigoOrgaoAutuador() {
		return codigoOrgaoAutuador;
	}



	public void setCodigoOrgaoAutuador(int codigoOrgaoAutuador) {
		this.codigoOrgaoAutuador = codigoOrgaoAutuador;
	}



	public String getAit() {
		return ait;
	}



	public void setAit(String ait) {
		this.ait = ait;
	}



	public int getCodigoInfracao() {
		return codigoInfracao;
	}



	public void setCodigoInfracao(int codigoInfracao) {
		this.codigoInfracao = codigoInfracao;
	}



	public int getIndicadorExigibilidade() {
		return indicadorExigibilidade;
	}



	public void setIndicadorExigibilidade(int indicadorExigibilidade) {
		this.indicadorExigibilidade = indicadorExigibilidade;
	}

	@Override
	public void importData(NodeList nodeList, String xml) {

		setXml(xml);
		
		for( int i = 0; i  < nodeList.getLength(); i++){
	       	 Node node = nodeList.item(i);
	       	 
	       	 if(node.getTextContent() == null || node.getTextContent().equals(""))
	       		 continue;
	       	 
	       	 switch(node.getNodeName()){
	       	 	case "codigo_retorno":
	       	 		setCodigoRetorno(Integer.parseInt(node.getTextContent()));
	       	 	break;
	       	 	
	       	 	case "mensagem_retorno":
	       	 		setMensagemRetorno(node.getTextContent());
	       	 	break;

	       	 	case "numero_processamento":
	       	 		setNumeroProcessamento(node.getTextContent());
	       	 	break;

	       	 	case "placa":
	       	 		setPlaca(node.getTextContent());
	       	 	break;

	       	 	case "codigo_orgao_autuador":
	       	 		setCodigoOrgaoAutuador(Integer.parseInt(node.getTextContent()));
	       	 	break;

	       	 	case "ait":
	       	 		setAit(node.getTextContent());
	       	 	break;

	       	 	case "codigo_infracao":
	       	 		setCodigoInfracao(Integer.parseInt(node.getTextContent()));
	       	 	break;

	       	 	case "indicador_exibilidade":
	       	 		setIndicadorExigibilidade(Integer.parseInt(node.getTextContent()));
	       	 	break;

	       	 	case "mensagens":
	       	 		setMensagens(node.getChildNodes());
	       	 	break;

	       	 }
		}
	}
}