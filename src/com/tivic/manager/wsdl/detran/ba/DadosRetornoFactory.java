package com.tivic.manager.wsdl.detran.ba;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class DadosRetornoFactory {
	public static Object gerarDadosRetorno(DadosRetornoBA dadosRetorno, String xml){
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
            return null;
        }
	}
}
