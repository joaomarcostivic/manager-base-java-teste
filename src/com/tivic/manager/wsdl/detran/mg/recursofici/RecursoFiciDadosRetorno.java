package com.tivic.manager.wsdl.detran.mg.recursofici;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tivic.manager.wsdl.detran.mg.DadosRetornoMG;

public class RecursoFiciDadosRetorno extends DadosRetornoMG {

	private String numeroProcessamento;
	private String placa;
	private int codigoOrgaoAutuador;
	private String ait;
	private int codigoInfracao;
	
	public RecursoFiciDadosRetorno() {}
	
	public RecursoFiciDadosRetorno(String numeroProcessamento, String placa, int codigoOrgaoAutuador, String ait,
			int codigoInfracao) {
		super();
		this.numeroProcessamento = numeroProcessamento;
		this.placa = placa;
		this.codigoOrgaoAutuador = codigoOrgaoAutuador;
		this.ait = ait;
		this.codigoInfracao = codigoInfracao;
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
				+ ", \"mensagens\": " + getMensagensJson() + "}";
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

	       	 	case "mensagens":
	       	 		setMensagens(node.getChildNodes());
	       	 	break;

	       	 }
		}
	}

}
