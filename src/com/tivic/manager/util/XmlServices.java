package com.tivic.manager.util;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jdom.input.SAXBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import sol.util.Result;

public class XmlServices {
	
	/**
	 * Carrega um XML
	 * @param source O caminho de um arquivo XML
	 * @return
	 */
	public static Result loadXML(String source) {
		return loadXML(source, false); 
	}
	public static Result loadXML(String source, boolean isXML) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			 
			// enable XInclude processing
			factory.setNamespaceAware(true);
			factory.setXIncludeAware(true);
			 
			DocumentBuilder parser = factory.newDocumentBuilder();
			
			Document document;
			
			if(isXML)
				document = parser.parse(new InputSource(new ByteArrayInputStream(source.getBytes("utf-8"))));
			else
				document = parser.parse(source);
			
	    	return new Result(1, "XML carregado com sucesso!", "doc", document);
	    }
	    catch(Exception pce)	{
	    	pce.printStackTrace(System.out);
	    	return new Result(-1, "Erro ao tentar carregar ", pce);
	    }
	}
	
	public static Result loadJdomXML(String source) {
		try {
	    	SAXBuilder saxObj = new SAXBuilder();
			org.jdom.Document doc = saxObj.build(source);

	    	return new Result(1, "XML carregado com sucesso!", "doc", doc);
	    }
	    catch(Exception pce)	{
	    	pce.printStackTrace(System.out);
	    	return new Result(-1, "Erro ao tentar carregar ", pce);
	    }
	}

	public static String nodeToString(Node node) {
		StringWriter sw = new StringWriter();
		
		try {
			 Transformer t = TransformerFactory.newInstance().newTransformer();
			 t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			 t.setOutputProperty(OutputKeys.INDENT, "yes");
			 t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			 t.transform(new DOMSource(node), new StreamResult(sw));
			 
			 return sw.toString();
		} 
		catch (TransformerException te) {
			te.printStackTrace();
			return "";
		}
	}
	
	/**
	 * Retorna o valor de um atributo ou conteudo de texto de um elemento
	 * @param elem
	 * @param attrib
	 * @param nameSpace
	 * @return
	 */
//    public static String getXMLValue(Element elem, String attrib, String nameSpace) {  
//        try	{
//        	if(nameSpace!=null){
//        		Namespace ns  = Namespace.getNamespace(nameSpace);
//        		Element child = elem!=null ? elem.getChild(attrib, ns) : null;
//        		return child != null ? child.getText() : "";
//        	}
//    		Element child = elem!=null ? elem.getChild(attrib) : null;
//    		return child!=null ? child.getText() : "";
//        }
//        catch(Exception e){
//        	e.printStackTrace(System.out);
//        	return "ERRO";
//        }
//   	}  

}
