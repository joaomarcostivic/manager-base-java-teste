package com.tivic.manager.mob.servlets;

import java.io.IOException;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import com.tivic.manager.wsdl.detran.ba.consultarplaca.ConsultarPlacaDadosRetorno;

public class XMLFactory {

	private ArquivoConfiguracaoDetranServlet arquivoConfiguracaoDetranServlet;
	private SOAPMessage soapMessage;
	private SOAPElement consultarVeiculosRequest;
	
	public XMLFactory(ConsultaDadosEntrada consultaDadosEntrada) throws SOAPException {
		arquivoConfiguracaoDetranServlet = new ArquivoConfiguracaoDetranServlet();
		this.createMessage();
		this.mountHeader();
		this.createEntrada(consultaDadosEntrada);
	}
	
	private void createMessage() throws SOAPException {
		MessageFactory messageFactory = MessageFactory.newInstance();
        soapMessage = messageFactory.createMessage();
	}
	
	private void mountHeader() throws SOAPException {
		SOAPPart soapPart = soapMessage.getSOAPPart();
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration("wsd", "http://www.wsdetranconsulta.com/");
		SOAPBody soapBody = envelope.getBody();
        SOAPElement consultarVeiculos = soapBody.addChildElement("consultaVeiculoDadosPropriet", "wsd");
        consultarVeiculosRequest = consultarVeiculos.addChildElement("consultaVeiculoDadosProprietRequest", "wsd");
	}
	
	private void createEntrada(ConsultaDadosEntrada consultaDadosEntrada) throws SOAPException  {
		addElement("codigoUsuario", consultaDadosEntrada.getCodigoUsuario());
    	addElement("senhaUsuario", consultaDadosEntrada.getSenhaUsuario());
    	addElement("codigoOperacao", consultaDadosEntrada.getCodigoOperacao());
    	addElement("paramPlaca", consultaDadosEntrada.getNrPlaca());
    	addElement("idModulo", consultaDadosEntrada.getIdModulo());
    	addElement("idOrgao", consultaDadosEntrada.getIdOrgao());
	}
	
	private void addElement(String element, String text) throws SOAPException {
		SOAPElement nodeElement = consultarVeiculosRequest.addChildElement(element, "wsd");
		nodeElement.addTextNode(text);
	}
	
	public ConsultarPlacaDadosRetorno enviarDados() throws SOAPException, IOException, Exception {
		soapMessage.saveChanges();
		soapMessage.writeTo(System.out);
    	SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
    	SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        SOAPMessage soapResponse = soapConnection.call(soapMessage,arquivoConfiguracaoDetranServlet.getUrlConsulta());
        return ConsultaDadosRetornoFactory.gerar(soapResponse);
	}
}
