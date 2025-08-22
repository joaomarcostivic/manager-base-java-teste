package com.tivic.manager.wsdl.detran.ba.soap;

import javax.xml.soap.SOAPHeaderElement;

import com.tivic.manager.wsdl.interfaces.ElementoCabecalho;

public class ElementoCabecalhoBa implements ElementoCabecalho {

	private SOAPHeaderElement headerElement;

	public void setHeaderElement(SOAPHeaderElement headerElement){
		this.headerElement = headerElement;
	}
	
	public SOAPHeaderElement getHeaderElement(){
		return this.headerElement;
	}
	

}
