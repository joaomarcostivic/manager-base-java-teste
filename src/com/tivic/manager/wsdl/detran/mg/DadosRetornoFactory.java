package com.tivic.manager.wsdl.detran.mg;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class DadosRetornoFactory {
	public static Object gerarDadosRetorno(DadosRetornoMG dadosRetorno, String xml) throws ValidacaoException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try {  
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            NodeList nodeList = document.getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getChildNodes().item(0).getChildNodes();
            dadosRetorno.importData(nodeList, xml);
            return dadosRetorno;
            
        } catch (Exception e) {  
            e.printStackTrace();
            throw new ValidacaoException("Erro no retorno dos dados. Envie novamente");
        }
	}
}
