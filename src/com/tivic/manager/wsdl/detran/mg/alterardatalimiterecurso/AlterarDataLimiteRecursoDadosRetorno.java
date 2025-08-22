package com.tivic.manager.wsdl.detran.mg.alterardatalimiterecurso;

import java.util.GregorianCalendar;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.detran.mg.DadosRetornoMG;

public class AlterarDataLimiteRecursoDadosRetorno extends DadosRetornoMG {
	
	private GregorianCalendar dataLimiteRecursoAntiga;
	private GregorianCalendar novaDataLimiteRecurso;
	
	public AlterarDataLimiteRecursoDadosRetorno() {}
	
	public AlterarDataLimiteRecursoDadosRetorno(GregorianCalendar dataLimiteRecursoAntiga, GregorianCalendar novaDataLimiteRecurso) {
		super();
		this.dataLimiteRecursoAntiga = dataLimiteRecursoAntiga;
		this.novaDataLimiteRecurso = novaDataLimiteRecurso;
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
	       	 	case "data_limite_recurso_antiga":
	       	 		setDataLimiteRecursoAntiga(Util.convStringSemFormatacaoReverseSToGregorianCalendar(node.getTextContent()));
	       	 		break;
	       	 	case "nova_data_limite_recurso":
	       	 		setNovaDataLimiteRecurso(Util.convStringSemFormatacaoReverseSToGregorianCalendar(node.getTextContent()));
	       	 		break;
	       	 }
		}
	}

	public GregorianCalendar getDataLimiteRecursoAntiga() {
		return dataLimiteRecursoAntiga;
	}

	public void setDataLimiteRecursoAntiga(GregorianCalendar dataLimiteRecursoAntiga) {
		this.dataLimiteRecursoAntiga = dataLimiteRecursoAntiga;
	}

	public GregorianCalendar getNovaDataLimiteRecurso() {
		return novaDataLimiteRecurso;
	}

	public void setNovaDataLimiteRecurso(GregorianCalendar novaDataLimiteRecurso) {
		this.novaDataLimiteRecurso = novaDataLimiteRecurso;
	}
	
	public String toString() {
		try {
			JSONObject jsonAlterarDataRecurso = new JSONObject();
			jsonAlterarDataRecurso.put("codigo_retorno", getCodigoRetorno());
			jsonAlterarDataRecurso.put("mensagem_retorno", getMensagemRetorno());
			jsonAlterarDataRecurso.put("data_limite_recurso_antiga", getDataLimiteRecursoAntiga());
			jsonAlterarDataRecurso.put("nova_data_limite_recurso", getNovaDataLimiteRecurso());
			return jsonAlterarDataRecurso.toString();
		} catch (JSONException e) {
			e.printStackTrace();
			return getMensagemRetorno();
		}
	}
	
}
