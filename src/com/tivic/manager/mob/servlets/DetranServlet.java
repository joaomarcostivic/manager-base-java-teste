package com.tivic.manager.mob.servlets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import com.tivic.manager.wsdl.detran.ba.DadosRetornoBA;

public class DetranServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  { 
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {
		try {
			List<DetranConsulta> lista = DetranConsultaFactory.createServerPriority();
			DadosRetornoBA dadosRetornoBa = EnvioConsulta.consultar(request, lista);
			montarResponse(response, dadosRetornoBa);
		} catch(Exception e) {
			enviarMensagem(response, exportXmlError());
		}
	}

	private void montarResponse(HttpServletResponse response, DadosRetornoBA dadosRetornoBa) throws Exception{
		System.out.println();
		System.out.println("XML de retorno = " + dadosRetornoBa.exportXml());
		enviarMensagem(response, dadosRetornoBa.exportXml());
	}
	
	private void enviarMensagem(HttpServletResponse response, String mensagem){
		try {
			OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
			writer.write(mensagem);
			writer.close();
		} catch(Exception e) {}
	}
	
	public String exportXmlError() { 
		try{
			MessageFactory messageFactory = MessageFactory.newInstance();
			SOAPMessage soapMessage = messageFactory.createMessage();
			SOAPPart soapPart = soapMessage.getSOAPPart();
			SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
			SOAPBody soapBody = soapEnvelope.getBody();
			SOAPElement consultarVeiculos = soapBody.addChildElement("consultaVeiculoDadosProprietResponse");
	        SOAPElement consultarVeiculosRequest = consultarVeiculos.addChildElement("consultaVeiculoDadosProprietResult");
	        SOAPElement element = consultarVeiculosRequest.addChildElement("codigoRetExec");
			element.addTextNode("402");
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			soapMessage.writeTo(out);
			return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + new String(out.toByteArray());
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
			
}
