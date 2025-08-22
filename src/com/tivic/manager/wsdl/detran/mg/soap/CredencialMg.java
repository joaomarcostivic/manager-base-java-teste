package com.tivic.manager.wsdl.detran.mg.soap;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;

public class CredencialMg extends CabecalhoMg {

	private SOAPElement credencial;
	
	public CredencialMg(QName headerName, SOAPHeader soapHeader, String prefix) throws SOAPException{
		super(soapHeader);
		
		this.credencial = soapHeader.addChildElement("autenticar", prefix);
	}
	
	public SOAPElement getCredencialElement(){
		return this.credencial;
	}
	
	public void setCnpj(String cnpj) {
		try{
			SOAPElement cnpjElement = this.credencial.addChildElement("cnpj");
			cnpjElement.addTextNode(cnpj);
		}
		catch(SOAPException soapException){}
	}

	public void setChave(String chave) {
		try{
			SOAPElement cnpjElement = this.credencial.addChildElement("chave");
			cnpjElement.addTextNode(chave);
		}
		catch(SOAPException soapException){}
	}

}
