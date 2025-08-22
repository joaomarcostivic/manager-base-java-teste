package com.tivic.manager.wsdl.detran.mg.soap;

import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;

import com.tivic.manager.wsdl.interfaces.Corpo;
import com.tivic.manager.wsdl.interfaces.Elemento;

public class CorpoMg implements Corpo {

	private SOAPBody soapBody;
	List<ElementoMg> elementos;
	
	public CorpoMg(SOAPBody soapBody){
		this.soapBody = soapBody;
		this.elementos = new ArrayList<ElementoMg>();
	}
	
	public Elemento addElementoPrincipal(String nome, String texto, String prefix) throws SOAPException {
		ElementoMg elementoMg = new ElementoMg(soapBody.addChildElement(nome, prefix), texto); 
		this.elementos.add(elementoMg);
		return elementoMg;
	}

	public Elemento addElemento(String nome, String texto) throws SOAPException {
		ElementoMg elementoMg = new ElementoMg(soapBody.addChildElement(nome), texto); 
		this.elementos.add(elementoMg);
		return elementoMg;
	}
}
