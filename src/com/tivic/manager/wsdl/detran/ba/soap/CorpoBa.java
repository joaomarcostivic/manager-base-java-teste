package com.tivic.manager.wsdl.detran.ba.soap;

import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;

import com.tivic.manager.wsdl.interfaces.Corpo;
import com.tivic.manager.wsdl.interfaces.Elemento;

public class CorpoBa implements Corpo {

	private SOAPBody soapBody;
	List<ElementoBa> elementos;
	
	public CorpoBa(SOAPBody soapBody){
		this.soapBody = soapBody;
		this.elementos = new ArrayList<ElementoBa>();
	}
	
	public Elemento addElementoPrincipal(String nome, String texto, String prefix) throws SOAPException {
		ElementoBa elementoMg = new ElementoBa(soapBody.addChildElement(nome, prefix), texto); 
		this.elementos.add(elementoMg);
		return elementoMg;
	}

	public Elemento addElemento(String nome, String texto) throws SOAPException {
		ElementoBa elementoMg = new ElementoBa(soapBody.addChildElement(nome), texto); 
		this.elementos.add(elementoMg);
		return elementoMg;
	}
}
