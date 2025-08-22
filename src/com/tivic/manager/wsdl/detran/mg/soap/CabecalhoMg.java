package com.tivic.manager.wsdl.detran.mg.soap;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;

import com.tivic.manager.wsdl.interfaces.Cabecalho;

public class CabecalhoMg implements Cabecalho {

	private SOAPHeader soapHeader;

	public CabecalhoMg(SOAPHeader soapHeader){
		this.soapHeader = soapHeader;
	}
	
	public SOAPHeader getSoapHeader(){
		return this.soapHeader;
	}
	
	public void addElemento(ElementoCabecalhoMg elementoCabecalhoMg, QName headerName) throws SOAPException {
		SOAPHeaderElement headerElement = this.soapHeader.addHeaderElement(headerName); 
		headerElement.addChildElement(elementoCabecalhoMg.getHeaderElement());
	}
	
	public void addCredencial(CredencialMg credencialMg, QName headerName) throws SOAPException {
		SOAPHeaderElement headerElement = this.soapHeader.addHeaderElement(headerName); 
		headerElement.addChildElement(credencialMg.getCredencialElement());
	}
	
	
}
