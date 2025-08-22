package com.tivic.manager.wsdl.detran.mg.baixas;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tivic.manager.util.JsonToStringBuilder;
import com.tivic.manager.wsdl.detran.mg.DadosRetornoMG;

public class BaixaDadosRetorno extends DadosRetornoMG {
	
	private int numeroProcessamento;
	private String placa;
	private int codigoOrgaoAutuador;
	private String ait;
	private int codigoInfracao;
	private int indicadorExigibilidade;

	@Override
	public void importData(NodeList nodeList, String xml) {
		setXml(xml);
		
		for( int i = 0; i  < nodeList.getLength(); i++){
	       	 Node node = nodeList.item(i);
	       	 
	       	 String value = node.getTextContent();
	       	 
	       	 switch(node.getNodeName()){
	       	 	case "codigo_retorno":
	       	 		try {
		       	 		setCodigoRetorno(Integer.parseInt(value));       	 			
	       	 		} catch(NumberFormatException nfe) {}
	       	 		break;
	       	 	case "mensagem_retorno":
	       	 		setMensagemRetorno(value);
	       	 		break;
	       	 	case "numero_processamento":
	       	 		try {
		       	 		setNumeroProcessamento(Integer.parseInt(value));     	 			
	       	 		} catch(NumberFormatException nfe) {}
	       	 		break;
	       	 	case "placa":
	       	 		setPlaca(value);
	       	 		break;
	       	 	case "codigo_orgao_autuador":
	       	 		try {
		       	 		setCodigoOrgaoAutuador(Integer.parseInt(value));       	 			
	       	 		} catch(NumberFormatException nfe) {}
	       	 		break;
	       	 	case "ait":
	       	 		setAit(value);
	       	 		break;
	       	 	case "codigo_infracao":
	       	 		try {
		       	 		setCodigoInfracao(Integer.parseInt(value));	       	 			
	       	 		} catch(NumberFormatException nfe) {}
	       	 		break;
	       	 	case "indicador_exigibilidade":
	       	 		try {
		       	 		setIndicadorExigibilidade(Integer.parseInt(value));    	 			
	       	 		} catch(NumberFormatException nfe) {}
	       	 		break;
	       	 }
		}
	}

	@Override
	public String toString() {
		JsonToStringBuilder builder = new JsonToStringBuilder(this);
		builder.append("codigoRetorno", getCodigoRetorno());
		builder.append("mensagemRetorno", getMensagemRetorno());
		builder.append("numeroProcessamento", numeroProcessamento);
		builder.append("placa", placa);
		builder.append("codigoOrgaoAutuador", codigoOrgaoAutuador);
		builder.append("ait", ait);
		builder.append("codigoInfracao", codigoInfracao);
		builder.append("indicadorExigibilidade", indicadorExigibilidade);
		return builder.toString();
	}

	public int getNumeroProcessamento() {
		return numeroProcessamento;
	}

	public void setNumeroProcessamento(int numeroProcessamento) {
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
	
	

}
