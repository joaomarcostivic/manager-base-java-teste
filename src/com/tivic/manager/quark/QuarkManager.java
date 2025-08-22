package com.tivic.manager.quark;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.tivic.manager.util.ContextManager;
import com.tivic.manager.util.XmlServices;

import sol.util.Result;

public class QuarkManager {

	private static ArrayList<String> classes = new ArrayList<>(); 
	private static HashMap<String, String> classMap = new HashMap<>();
	
	public static void start() {
		//MAPEAMENTO DE SERVIÇOS
		Result   ret = XmlServices.loadXML("file://localhost/"+XmlServices.class.getResource("/com/tivic/manager/quark/services.xml").getPath().substring(1));
		Document doc = (Document)ret.getObjects().get("doc");
		
		NodeList  classNodeList = doc.getElementsByTagName("class");
		for (int i = 0; i < classNodeList.getLength(); i++) {
			
	        Node currentNode = classNodeList.item(i);
	        if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
	            
	        	String classId = currentNode.getAttributes().getNamedItem("id").getNodeValue();
	        	String className = currentNode.getAttributes().getNamedItem("name").getNodeValue();
	        	
	        	classes.add(classId + ": "+className);
	            classMap.put(classId, className);
	        	
	        	NodeList  methodNodeList = currentNode.getChildNodes();
	        	for (int j = 0; j < methodNodeList.getLength(); j++) {
	        		
	        		Node currentMethodNode = methodNodeList.item(j);
	        		
	    	        if (currentMethodNode.getNodeType() == Node.ELEMENT_NODE) {
	    	        	
	    	        	String methodId = currentMethodNode.getAttributes().getNamedItem("id").getNodeValue();
	    	        	String methodName = currentMethodNode.getAttributes().getNamedItem("name").getNodeValue();
	    	        	
	    	        	classMap.put(classId+"-"+methodId, methodName);
	    	        }
	        	}
	        }
	    }
		System.out.println("\t[QUARK] " + classes.size() + " classes mapeadas para " + ContextManager.getContextPath());
	}

	public static int getClassCount(){
		try{
			return classes.size();
		}
		catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	public static String getClassNameById(String classId){
		try{
			return classMap.get(classId);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getMethodNameById(String methodId){
		try{
			return classMap.get(methodId);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
