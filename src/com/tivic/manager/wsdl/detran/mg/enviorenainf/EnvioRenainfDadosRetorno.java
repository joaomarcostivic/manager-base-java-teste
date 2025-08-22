package com.tivic.manager.wsdl.detran.mg.enviorenainf;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tivic.manager.wsdl.detran.mg.DadosRetornoMG;

public class EnvioRenainfDadosRetorno extends DadosRetornoMG {

	public EnvioRenainfDadosRetorno() {
		
	}
	
	public EnvioRenainfDadosRetorno(int codigoRetorno, String mensagemRetorno) {
		super(codigoRetorno, mensagemRetorno);
	}
	
	@Override
	public String toString() {
		return "{\"codigoRetorno\":" + getCodigoRetorno() 
				+ ", \"mensagemRetorno\": \"" + getMensagemRetorno() + "}";
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

	       	 	case "mensagens":
	       	 		setMensagens(node.getChildNodes());
	       	 	break;

	       	 }
		}
	}
}
