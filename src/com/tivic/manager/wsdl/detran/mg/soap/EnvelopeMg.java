package com.tivic.manager.wsdl.detran.mg.soap;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.axis2.util.Base64;

import com.tivic.manager.mob.OrgaoServico;
import com.tivic.manager.mob.OrgaoServicoServices;
import com.tivic.manager.wsdl.DadosItem;
import com.tivic.manager.wsdl.detran.mg.DadosEntradaMG;
import com.tivic.manager.wsdl.detran.mg.DadosRetornoFactory;
import com.tivic.manager.wsdl.detran.mg.DadosRetornoMG;
import com.tivic.manager.wsdl.detran.mg.IArquivoConfiguracaoMg;
import com.tivic.manager.wsdl.detran.mg.soap.backup.Backup;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ArquivoConfiguracao;
import com.tivic.manager.wsdl.interfaces.Elemento;
import com.tivic.manager.wsdl.interfaces.Envelope;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.log.ManagerLog;

public class EnvelopeMg implements Envelope {

	private CorpoMg corpoMg;
	private CabecalhoMg cabecalhoMg;
	private CredencialMg credencialMg;
	private Elemento elementoPrincipal;
	private DadosEntradaMG dadosEntrada;
	
	private MessageFactory messageFactory;
	private SOAPMessage soapMessage;
	private SOAPPart soapPart;
	private SOAPConnection soapConnection;
	private SOAPEnvelope soapEnvelope;
	private Class classeRetorno;
	
	private IArquivoConfiguracaoMg arquivoConfiguracao;
	private Backup backup;
	private ManagerLog managerLog;
	
	public EnvelopeMg(String elementoPrincipal, DadosEntradaMG dadosEntrada, ArquivoConfiguracao arquivoConfiguracao, Class classeRetorno) throws Exception{
		try{
			this.arquivoConfiguracao = (IArquivoConfiguracaoMg) arquivoConfiguracao;
			this.backup = (Backup) BeansFactory.get(Backup.class);
			this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
			
			this.classeRetorno = classeRetorno;
			this.messageFactory = MessageFactory.newInstance();
	        this.soapMessage = messageFactory.createMessage();
	        String loginPassword = this.getLogin();
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
		this.cabecalhoMg = new CabecalhoMg(this.soapEnvelope.getHeader());
		this.addCredencial();
	}

	private void addCredencial() throws SOAPException {
		QName headerName = soapEnvelope.createQName("autenticar", arquivoConfiguracao.getPrefixNameSpace());
		SOAPHeaderElement headerElement = this.cabecalhoMg.getSoapHeader().addHeaderElement(headerName); 
		SOAPElement cnpjElement = headerElement.addChildElement("cnpj");
		cnpjElement.addTextNode(this.arquivoConfiguracao.getCnpj());
		SOAPElement chaveElement = headerElement.addChildElement("chave");
		chaveElement.addTextNode(this.arquivoConfiguracao.getChave());
	}

	private void addCorpo() throws SOAPException {
		this.corpoMg = new CorpoMg(this.soapEnvelope.getBody());
	}
	
	private Elemento addElementoPrincipal(String nome, String texto){
		try{
			return this.corpoMg.addElementoPrincipal(nome, texto, arquivoConfiguracao.getPrefixNameSpaceUri());
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
		return DadosRetornoFactory.gerarDadosRetorno((DadosRetornoMG)classeRetorno.newInstance(), xml);
	}
	
	private String sendRepository(){
		try{
			montarXmlEntrada();
			this.backup.gerarEntrada(dadosEntrada);
			String xmlRetorno = montarXmlRetorno();
			this.backup.gerarRetorno(dadosEntrada, xmlRetorno);
	        return xmlRetorno;
		}
		catch(Exception e){
			try {
				managerLog.showLog(e);
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				this.backup.gerarRetorno(dadosEntrada, sw.toString());
				pw.close();
				sw.close();
			} catch(Exception ex) {
				System.out.println("Erro ao registrar log");
				ex.printStackTrace();
			}
			return null;
		}
	}
	
	private void montarXmlEntrada() throws Exception{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		soapMessage.writeTo(out);
		String xmlEntrada = new String(out.toByteArray());
		managerLog.info("ENVIO DETRAN", xmlEntrada);
		this.dadosEntrada.setXml(xmlEntrada);
	}
	
	private String montarXmlRetorno() throws Exception{
		String xmlRetorno = "";
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
        for (int c; (c = in.read()) >= 0;)
        	xmlRetorno += ((char)c);
        managerLog.info("RETORNO DETRAN", xmlRetorno);
        return xmlRetorno;
	}
	
	@Override
	public void validate() throws ValidacaoException{
		for(String campo : this.dadosEntrada.getItens().keySet()){
			System.out.println(campo);
			DadosItem dadosItem = this.dadosEntrada.getItens().get(campo);
			dadosItem.validate();
			this.elementoPrincipal.addChild(campo, dadosItem);
		}
	}
	
	private String getLogin() {
		OrgaoServicoServices _service = new OrgaoServicoServices();
		OrgaoServico _servico = _service.getByNmServico("PRODEMGE");
		
		return _servico.getNmLogin() + ":" + _servico.getNmSenha();
	}
	
}
