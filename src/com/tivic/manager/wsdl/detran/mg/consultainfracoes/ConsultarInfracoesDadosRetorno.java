package com.tivic.manager.wsdl.detran.mg.consultainfracoes;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.detran.mg.DadosRetornoMG;

public class ConsultarInfracoesDadosRetorno extends DadosRetornoMG {

	private int qtdeOcorrencias;
	private List<ConsultaInfracaoOcorrenciaDTO> listConsultaInfracaoOcorrenciaDTO;
	
	public ConsultarInfracoesDadosRetorno() {
		listConsultaInfracaoOcorrenciaDTO = new ArrayList<ConsultaInfracaoOcorrenciaDTO>();
	}
	
	public int getQtdeOcorrencias() {
		return qtdeOcorrencias;
	}

	public void setQtdeOcorrencias(int qtdeOcorrencias) {
		this.qtdeOcorrencias = qtdeOcorrencias;
	}

	public void setListConsultaInfracaoOcorrenciaDTO(
			List<ConsultaInfracaoOcorrenciaDTO> listConsultaInfracaoOcorrenciaDTO) {
		this.listConsultaInfracaoOcorrenciaDTO = listConsultaInfracaoOcorrenciaDTO;
	}
	
	public List<ConsultaInfracaoOcorrenciaDTO> getListConsultaInfracaoOcorrenciaDTO() {
		return listConsultaInfracaoOcorrenciaDTO;
	}
	
	public void addListConsultaInfracaoOcorrenciaDTO(ConsultaInfracaoOcorrenciaDTO consultaInfracaoOcorrenciaDTO) {
		this.listConsultaInfracaoOcorrenciaDTO.add(consultaInfracaoOcorrenciaDTO);
	}

	@Override
	public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } 
        catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
	}
	

	@Override
	public void importData(NodeList nodeList, String xml) {
		setXml(xml);
		for( int i = 0; i  < nodeList.getLength(); i++){
	       	 Node node = nodeList.item(i);
	       	 String value = node.getTextContent();
	       	 try {
	       		switch(node.getNodeName()){
		       	 	case "codigo_retorno":
		       	 		setCodigoRetorno(Integer.parseInt(value));
		       	 		break;
		       	 	case "mensagem_retorno":
		       	 		setMensagemRetorno(value);
		       	 		break;
		       	 	case "quantidade_ocorrencias":
		       	 		setQtdeOcorrencias(Integer.parseInt(value));
		       	 		break;
		       	 	case "ocorrencias":
		       	 		NodeList nodeListChilds = node.getChildNodes();
		       	 		for( int j = 0; j  < nodeListChilds.getLength(); j++){
		       	 			ConsultaInfracaoOcorrenciaDTO consultaInfracaoOcorrenciaDTO = new ConsultaInfracaoOcorrenciaDTO();
		       	 			Node nodeChild = nodeListChilds.item(j);
		       	 			NodeList nodeListChildChilds = nodeChild.getChildNodes();
		       	 			for( int k = 0; k  < nodeListChildChilds.getLength(); k++){
		       	 				Node nodeChildChild = nodeListChildChilds.item(k);
		       	 				String valueChildChild = nodeChildChild.getTextContent();
		       	 				try {
					   	       		switch(nodeChildChild.getNodeName()){
					   	       		   	case "orgao_autuador":
					   	       		   		consultaInfracaoOcorrenciaDTO.setOrgao(valueChildChild);
							       	 		break;
							       	 	case "placa_veiculo":
							       	 		consultaInfracaoOcorrenciaDTO.setPlaca(valueChildChild);
							       	 		break;
							       	 	case "ait":
							       	 		consultaInfracaoOcorrenciaDTO.setAit(valueChildChild);
							       	 		break;
							       	 	case "codigo_infracao":
							       	 		consultaInfracaoOcorrenciaDTO.setCodigoInfracao(Integer.parseInt(valueChildChild));
							       	 		break;
							       	 	case "desdobramento":
							       	 		consultaInfracaoOcorrenciaDTO.setDesdobramento(Integer.parseInt(valueChildChild));
							       	 		break;
							       	 	case "numero_processamento":
							       	 		consultaInfracaoOcorrenciaDTO.setNumeroProcessamento(Integer.parseInt(valueChildChild));
							       	 		break;
							       	 	case "data_infracao":
							       	 		consultaInfracaoOcorrenciaDTO.setDataInfracao(Util.convStringSemFormatacaoReverseSToGregorianCalendar(valueChildChild));
							       	 		break;
							       	 	case "gravidade":
							       	 		consultaInfracaoOcorrenciaDTO.setGravidade(valueChildChild);
							       	 		break;
							       	 	case "convertida_advertencia":
							       	 		consultaInfracaoOcorrenciaDTO.setConvertidaAdvertencia(valueChildChild);
							       	 		break;
							       	 	case "pontuacao_renainf":
							       	 		consultaInfracaoOcorrenciaDTO.setPontuacaoRenainf(valueChildChild);
							       	 		break;
					   		   		}
					   	       		
					   	       	 } catch (NumberFormatException nfe) {
						       		 System.out.println(nfe.getMessage());
						       	 }
		       	 			}
		       	 			addListConsultaInfracaoOcorrenciaDTO(consultaInfracaoOcorrenciaDTO);
						}
		       	 		
		       	 		break;
	       		}
	       	 } catch (NumberFormatException nfe) {
	       		 System.out.println(nfe.getMessage());
	       	 }
		}
	}
	
}

