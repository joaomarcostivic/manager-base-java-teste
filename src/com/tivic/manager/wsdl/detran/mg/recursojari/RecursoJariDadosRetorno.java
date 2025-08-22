package com.tivic.manager.wsdl.detran.mg.recursojari;

import java.util.GregorianCalendar;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.DefinidorDataPrazo;
import com.tivic.manager.wsdl.detran.mg.DadosRetornoMG;

public class RecursoJariDadosRetorno extends DadosRetornoMG  {

	private String numeroProcessamento;
	private String placa;
	private GregorianCalendar dataLimiteRecurso;
	private int codigoOrgaoAutuador;
	private String ait;
	private int codigoInfracao;
	
	public RecursoJariDadosRetorno() {
		// TODO Auto-generated constructor stub
	}
	
	public RecursoJariDadosRetorno(String numeroProcessamento, String placa, GregorianCalendar dataLimiteRecurso, int codigoOrgaoAutuador, String ait,
			int codigoInfracao) {
		super();
		this.numeroProcessamento = numeroProcessamento;
		this.placa = placa;
		this.dataLimiteRecurso = dataLimiteRecurso;
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
				+ ", \"dataLimiteRecurso\": \"" + Util.formatDate(getDataLimiteRecurso(), "yyyyMMdd") + "\""
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

	public GregorianCalendar getDataLimiteRecurso() {
		return dataLimiteRecurso;
	}
	
	public void setDataLimiteRecurso(GregorianCalendar dataLimiteRecurso) {
		this.dataLimiteRecurso = dataLimiteRecurso;
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

	       	 	case "data_limite_recurso":
       	 			setDataLimiteRecurso(Util.convStringSemFormatacaoReverseSToGregorianCalendar(node.getTextContent()));
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
		try {
			setDataLimiteRecurso(new DefinidorDataPrazo().getDataLimiteRecurso(ait, getDataLimiteRecurso()));
		} catch (Exception e) {}
	}
}
