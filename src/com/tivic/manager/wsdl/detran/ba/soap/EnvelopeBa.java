package com.tivic.manager.wsdl.detran.ba.soap;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.axis2.util.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.tivic.manager.wsdl.detran.ba.DadosEntradaBA;
import com.tivic.manager.wsdl.DadosItem;
import com.tivic.manager.wsdl.detran.ba.DadosRetornoFactory;
import com.tivic.manager.wsdl.detran.ba.DadosRetornoBA;
import com.tivic.manager.wsdl.detran.ba.consultarplaca.ConsultarPlacaDadosRetorno;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ArquivoConfiguracao;
import com.tivic.manager.wsdl.interfaces.Elemento;
import com.tivic.manager.wsdl.interfaces.Envelope;

public class EnvelopeBa implements Envelope {

	private CorpoBa corpoBa;
	private CabecalhoBa cabecalhoBa;
	private CredencialBa credencialBa;
	private Elemento elementoPrincipal;
	private DadosEntradaBA dadosEntrada;
	
	private MessageFactory messageFactory;
	private SOAPMessage soapMessage;
	private SOAPPart soapPart;
	private SOAPConnection soapConnection;
	private SOAPEnvelope soapEnvelope;
	private ArquivoConfiguracaoBa arquivoConfiguracao;
	private Class classeRetorno;
	
	public EnvelopeBa(String elementoPrincipal, DadosEntradaBA dadosEntrada, ArquivoConfiguracao arquivoConfiguracao, Class classeRetorno){
		try{
			this.arquivoConfiguracao = (ArquivoConfiguracaoBa)arquivoConfiguracao;
			this.classeRetorno = classeRetorno;
			this.messageFactory = MessageFactory.newInstance();
	        this.soapMessage = messageFactory.createMessage();
	        String loginPassword = "prefeituras:9CIlTyW0aG";
	        soapMessage.getMimeHeaders().addHeader("Authorization", "Basic " + new  String(Base64.encode(loginPassword.getBytes())));
	        this.soapPart = soapMessage.getSOAPPart();
	        
	        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
	        this.soapConnection = soapConnectionFactory.createConnection();
	        this.soapEnvelope = this.soapPart.getEnvelope();
			soapEnvelope.addNamespaceDeclaration(this.arquivoConfiguracao.getPrefixNameSpace(), this.arquivoConfiguracao.getNameSpace());
			soapEnvelope.addNamespaceDeclaration(this.arquivoConfiguracao.getPrefixNameSpaceUri(), this.arquivoConfiguracao.getNameSpaceUri());
			this.addCabecalho();
			this.addCorpo();
			this.elementoPrincipal = addElementoPrincipal(elementoPrincipal, "");
			this.dadosEntrada = dadosEntrada;
			
		}
		catch(SOAPException soapException){
			System.out.println("Erro ao inicializar envelope");
			soapException.printStackTrace();
		}
	}
	
	private void addCabecalho() throws SOAPException{
		this.cabecalhoBa = new CabecalhoBa(this.soapEnvelope.getHeader());
		this.addCredencial();
	}

	private void addCredencial() throws SOAPException {
		QName headerName = soapEnvelope.createQName("autenticar", arquivoConfiguracao.getPrefixNameSpace());
		SOAPHeaderElement headerElement = this.cabecalhoBa.getSoapHeader().addHeaderElement(headerName); 
		SOAPElement cnpjElement = headerElement.addChildElement("cnpj");
		cnpjElement.addTextNode(this.arquivoConfiguracao.getCnpj());
		SOAPElement chaveElement = headerElement.addChildElement("chave");
		chaveElement.addTextNode(this.arquivoConfiguracao.getChave());
	}

	private void addCorpo() throws SOAPException {
		this.corpoBa = new CorpoBa(this.soapEnvelope.getBody());
	}
	
	private Elemento addElementoPrincipal(String nome, String texto){
		try{
			return this.corpoBa.addElementoPrincipal(nome, texto, arquivoConfiguracao.getPrefixNameSpaceUri());
		}
		catch(SOAPException soapException){
			System.out.println("Erro ao adicionar elemento ao envelope");
			soapException.printStackTrace();
			return null;
		}
	}

	@Override
	public Object enviar() throws InstantiationException, IllegalAccessException, ValidacaoException {
		String xml = this.sendRepository();
		return DadosRetornoFactory.gerarDadosRetorno((DadosRetornoBA)classeRetorno.newInstance(), xml);
	}
	
	private String sendRepository(){
		try{
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			soapMessage.writeTo(out);
			this.dadosEntrada.setXml(new String(out.toByteArray()));
			System.out.println("xml de envio = " + new String(out.toByteArray()));
			String request        = this.arquivoConfiguracao.getUrl();
			URL    url            = new URL( request );
			HttpURLConnection conn= (HttpURLConnection) url.openConnection();           
			conn.setDoOutput( true );
			conn.setInstanceFollowRedirects( false );
			conn.setRequestMethod( "POST" );
			conn.setRequestProperty( "Content-Type", "application/xml"); 
			conn.setRequestProperty( "charset", "utf-8");
			conn.setUseCaches( false );
			try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
				soapMessage.writeTo(wr);
			}
			
			Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

			String xmlString = "";
			
	        for (int c; (c = in.read()) >= 0;)
	            xmlString += ((char)c);
	        
	        System.out.println("xml retorno = " + xmlString);
	        
	        return xmlString;
		}
		catch(Exception e){ return null; }
	}

	@Override
	public void validate() throws ValidacaoException {
		for(String campo : this.dadosEntrada.getItens().keySet()){
			DadosItem dadosItem = this.dadosEntrada.getItens().get(campo);
			dadosItem.validate();
			this.elementoPrincipal.addChild(campo, dadosItem);
		}
	}
	
}
