package com.tivic.manager.seg;

import java.text.ParseException;
import java.text.SimpleDateFormat;  
import java.sql.Connection;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.EmpresaServices;
import com.tivic.manager.grl.ParametroServices;

import sol.util.DateServices;
import sol.util.Result;

public class CertificadoServices {
    
	public static int A3 = 3; // 3 = A3 , 1 = A1
    public static int A1 = 1;
    
    public static PrivateKey privateKey;  
    public static KeyInfo    keyInfo;  
	
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
	
    public static String getCacertsFile(int cdEmpresa)	{
    	return ParametroServices.getValorOfParametro("NM_FILE_CACERTS", "C:/TIVIC/nfeCacertBA", cdEmpresa, Conexao.conectar());
    }

    public static String getPath(int cdEmpresa)	{
    	return ParametroServices.getValorOfParametroAsInteger("TP_CERTIFICADO", A3, cdEmpresa, Conexao.conectar()) == A3 ? "C:/Users/ADM/Desktop/atacadao.cer" : "C:/Users/ADM/Documents/A1_2011_11_3.pfx";
    }

    public static String getPass(int cdEmpresa)	{
    	return ParametroServices.getValorOfParametro("DS_PASS_CERTIFICADO", "1234", cdEmpresa, Conexao.conectar());
    }

    public static int getTipoCertificado(int cdEmpresa)	{
    	return ParametroServices.getValorOfParametroAsInteger("TP_CERTIFICADO", A3, cdEmpresa, Conexao.conectar());
    }

    public static void setInformacaoCertificado(String cdEstadoDefault, int cdEmpresa)	{
		try	{
	        /** 
	         * 1) codigoDoEstado = Código do Estado conforme tabela IBGE. 
	         * 
	         * 2) url = Endereço do WebService para cada Estado. 
	         *       Ver relação dos endereços em: 
	         *       Para Homologação: http://hom.nfe.fazenda.gov.br/PORTAL/WebServices.aspx 
	         *       Para Produção...: http://www.nfe.fazenda.gov.br/portal/WebServices.aspx 
	         * 
	         * 3) caminhoDoCertificadoDoCliente      = Caminho do Certificado do Cliente (A1). 
	         * 
	         * 4) senhaDoCertificadoDoCliente        = Senha do Certificado A1 do Cliente. 
	         * 
	         * 5) arquivoCacertsGeradoParaCadaEstado = Arquivo com os Certificados necessarios para acessar o WebService. Pode ser gerado com a Classe NFeBuildCacerts. 
	         */
	
	        /** 
	         * Informações do Certificado Digital. 
	         */  
	        System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");  
	        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());  
	
	        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");  

	        System.clearProperty("javax.net.ssl.keyStore");  
	        System.clearProperty("javax.net.ssl.keyStorePassword");  
	        System.clearProperty("javax.net.ssl.trustStore");
	        System.clearProperty("javax.net.ssl.trustStorePassword"); 
	
	        System.setProperty("javax.net.ssl.keyStore", getPath(cdEmpresa));  
	        System.setProperty("javax.net.ssl.keyStorePassword", getPass(cdEmpresa));  
	
	        System.setProperty("javax.net.ssl.trustStoreType", "JKS");  
	        System.setProperty("javax.net.ssl.trustStore", cdEstadoDefault);
		}
		catch (Exception e) {  
            e.printStackTrace(System.out);
            return;
        }  
	}
    
    /**
     * Obtém data de validade final de um certificado
     * 
     * @return
     * @author Luiz Romario Filho
     * @since 09/05/2015
     */
    public static Result getValidadeCertificadoDigital(int cdEmpresa){
    	String dataFinal = "";
    	try {
    		String nmFileCertificado = ParametroServices.getValorOfParametro("NM_FILE_CERTIFICADO", cdEmpresa);
    		if(nmFileCertificado != null){
	    		// caso a empresa tenha certificado do tipo A3
	    		if(CertificadoServices.getTipoCertificado(cdEmpresa) == CertificadoServices.A3){
					String fileCfg = nmFileCertificado;              
		            Provider p     = new sun.security.pkcs11.SunPKCS11(fileCfg);
		            Security.addProvider(p);
		            String senhaDoCertificadoDoCliente = CertificadoServices.getPass(cdEmpresa);
		            char[] pin = senhaDoCertificadoDoCliente.toCharArray();  
		            KeyStore keystore = KeyStore.getInstance("pkcs11", p);  
		            keystore.load(null, pin);  
		            Enumeration<String> eAliases = keystore.aliases();    
		                
		            while (eAliases.hasMoreElements()) {    
		                String alias = (String) eAliases.nextElement();    
		                Certificate certificado = (Certificate) keystore.getCertificate(alias);    
		                X509Certificate cert = (X509Certificate) certificado;    
		                dataFinal =  dateFormat.format(cert.getNotAfter());
		            }  
		            return new Result(1, dataFinal);  
	    		} else{ // caso a empresa tenha certificado do tipo A1
	    			KeyStore keystore = KeyStore.getInstance(("PKCS12"));
	    			keystore.load(new FileInputStream(nmFileCertificado), CertificadoServices.getPass(cdEmpresa).toCharArray());    
	    			Enumeration<String> eAliases = keystore.aliases();    
	    			while (eAliases.hasMoreElements()) {    
	    				String alias            = (String) eAliases.nextElement();    
	    				Certificate certificado = (Certificate) keystore.getCertificate(alias);    
	    				X509Certificate cert = (X509Certificate) certificado;    
	    				dataFinal = dateFormat.format(cert.getNotAfter());
	    			}    
	    		}
    		}
            return new Result(1, dataFinal);
        } 
        catch (Exception e) {
        	e.printStackTrace(System.out);
            error(e.toString());
            return new Result(-1, "Erro ao tentar verificar validade do certificado digital", e);
        }  
    }
	
    /**
     * Método que verifica se o certificado está válido.
     * 
     * @return
     * @author Luiz Romario Filho
     * @since 09/04/2015
     */
    public static Result validarCertificadoDigital(int cdEmpresa){
		Result result = getValidadeCertificadoDigital(cdEmpresa);
		String dataFinal = result.getMessage();
		if(!"".equals(dataFinal)){
			Long timeInMillis = null;
			try {
				timeInMillis = dateFormat.parse(dataFinal).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
				return new Result(-5, "Erro ao converter a data final do certificado.");
			}
					
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTimeInMillis(timeInMillis);
			int  countDays = DateServices.countDaysBetween(new GregorianCalendar(), calendar);
			if(countDays <= 10 && countDays > 0){
				return new Result(-2, "O período do seu certificado atual do sistema irá expirar em " + (countDays == 1 ? countDays + " dia" : countDays + " dias") + ". Entre em contato com a contabilidade.");
			}	
			if(countDays < 1){
				return new Result(-3, "O período do seu certificado digital expirou. Entre em contato com a contabilidade.");
			}
			return new Result(1, "Certificado válido.");
		} else{
			return new Result(-5, "Erro ao converter a data final do certificado.");
		}
    }
    
    
    public static Result getValidadeCertificadoDigitalA1(int cdEmpresa) {  
        try {  
            KeyStore keystore = KeyStore.getInstance(("PKCS12"));    
            keystore.load(new FileInputStream(CertificadoServices.getPath(cdEmpresa)), CertificadoServices.getPass(cdEmpresa).toCharArray());    
            String retorno = ""; 
            Enumeration<String> eAliases = keystore.aliases();    
            while (eAliases.hasMoreElements()) {    
                String alias            = (String) eAliases.nextElement();    
                Certificate certificado = (Certificate) keystore.getCertificate(alias);    
                retorno += "Aliais: " + alias;
                info("Aliais: " + alias);  
                X509Certificate cert = (X509Certificate) certificado;    
                  
                info(cert.getSubjectDN().getName());  
                info("Válido a partir de..: " + dateFormat.format(cert.getNotBefore()));  
                info("Válido até..........: " + dateFormat.format(cert.getNotAfter()));
                retorno += "\tNome: "+cert.getSubjectDN().getName();
                retorno += "\tVálido a partir de..: " + dateFormat.format(cert.getNotBefore());
                retorno += "\tVálido até..........: " + dateFormat.format(cert.getNotAfter());
                retorno += "\n";
            }    
            return new Result(1, retorno);
        } 
        catch (Exception e) {
        	e.printStackTrace(System.out);
            error(e.toString());
            return new Result(-1, "Erro ao tentar verificar validade do certificado digital A1", e);
        }  
    }
    
    public static Result getValidadeCertificadoDigitalA3(int cdEmpresa, Connection connect) {  
    	try {  
            String fileCfg = ParametroServices.getValorOfParametro("NM_FILE_CERTIFICADO", "c:\\SmartCard.cfg", cdEmpresa);              
            Provider p     = new sun.security.pkcs11.SunPKCS11(fileCfg);
            Security.addProvider(p);
            String senhaDoCertificadoDoCliente = CertificadoServices.getPass(cdEmpresa);
            char[] pin = senhaDoCertificadoDoCliente.toCharArray();  
            KeyStore keystore = KeyStore.getInstance("pkcs11", p);  
            keystore.load(null, pin);  
            String retorno = ""; 
            Enumeration<String> eAliases = keystore.aliases();    
                
            while (eAliases.hasMoreElements()) {    
                String alias = (String) eAliases.nextElement();    
                Certificate certificado = (Certificate) keystore.getCertificate(alias);    
            
                info("Aliais: " + alias);  
                X509Certificate cert = (X509Certificate) certificado;    
                  
                info(cert.getSubjectDN().getName());  
                info("Válido a partir de..: " + dateFormat.format(cert.getNotBefore()));  
                info("Válido até..........: " + dateFormat.format(cert.getNotAfter()));    
                retorno += "\tNome: "+cert.getSubjectDN().getName();
                retorno += "\tVálido a partir de..: " + dateFormat.format(cert.getNotBefore());
                retorno += "\tVálido até..........: " + dateFormat.format(cert.getNotAfter());
                retorno += "\n";
            }  
            return new Result(1, retorno);   
        } 
        catch (Exception e) {  
        	e.printStackTrace(System.out);
            error(e.toString());
            return new Result(-1, "Erro ao tentar verificar validade do certificado!", e);
        }  
    }
    
    public static void listaCertificadoRepositorioWindows() {
    	try {  
            KeyStore keyStore = KeyStore.getInstance("Windows-MY", "SunMSCAPI");  
            keyStore.load(null, null);  
              
            Enumeration <String> al = keyStore.aliases();  
            while (al.hasMoreElements()) {  
                String alias = al.nextElement();  
                info("--------------------------------------------------------");  
                if (keyStore.containsAlias(alias)) {  
                    info("Emitido para........: " + alias);  
  
                    X509Certificate cert = (X509Certificate) keyStore.getCertificate(alias);  
                    info("SubjectDN...........: " + cert.getSubjectDN().toString());  
                    info("Version.............: " + cert.getVersion());  
                    info("SerialNumber........: " + cert.getSerialNumber());  
                    info("SigAlgName..........: " + cert.getSigAlgName());  
                    info("Válido a partir de..: " + dateFormat.format(cert.getNotBefore()));  
                    info("Válido até..........: " + dateFormat.format(cert.getNotAfter()));    
                } 
                else {  
                    info("Alias doesn't exists : " + alias);  
                }  
            }  
        } catch (Exception e) {  
            error(e.toString());  
        }  
    }
    
    public static String verificarValidadeCertA3(String senhaDoCertificadoDoCliente){
    	return verificarValidadeCertA3(senhaDoCertificadoDoCliente, EmpresaServices.getDefaultEmpresa().getCdEmpresa());
    }
    
	public static String verificarValidadeCertA3(String senhaDoCertificadoDoCliente, int cdEmpresa){
		try	{
			String fileCfg = ParametroServices.getValorOfParametro("NM_FILE_CERTIFICADO", "c:\\SmartCard.cfg", cdEmpresa);   
            Provider p = new sun.security.pkcs11.SunPKCS11(fileCfg);  
            Security.addProvider(p);  
            char[] pin = senhaDoCertificadoDoCliente.toCharArray();  
            KeyStore keystore = KeyStore.getInstance("pkcs11", p);  
            keystore.load(null, pin);  
              
            Enumeration<String> eAliases = keystore.aliases();    
                
            while (eAliases.hasMoreElements()) {    
                String alias = (String) eAliases.nextElement();    
                Certificate certificado = (Certificate) keystore.getCertificate(alias);    
            
                info("Aliais: " + alias);  
                X509Certificate cert = (X509Certificate) certificado;    
                  
                info(cert.getSubjectDN().getName());  
                info("Válido a partir de..: " + dateFormat.format(cert.getNotBefore()));  
                info("Válido até..........: " + dateFormat.format(cert.getNotAfter()));
                
                return dateFormat.format(cert.getNotAfter());
            }  
            
            return null;
		}
		
		catch(Exception e) {return null;}
	}

    public static boolean loadCertificates(XMLSignatureFactory signatureFactory, int cdEmpresa) {
    	Connection connect = Conexao.conectar();
    	try	{
	        /** 
	         * Para Certificados A3 Cartao usar: SmartCard.cfg; 
	         * Para Certificados A3 Token usar: Token.cfg; 
	         */
	    	String senha           = getPass(cdEmpresa);
	    	String nmArquivoConfig = ParametroServices.getValorOfParametro("NM_FILE_CERTIFICADO", "SmartCard.cfg", cdEmpresa);
	    	System.out.println("nmArquivoConfig = " + nmArquivoConfig);
	    	System.out.println("senha = " + senha);
	    	Provider provider = new sun.security.pkcs11.SunPKCS11(nmArquivoConfig);  
	        Security.addProvider(provider);   
	        KeyStore ks = KeyStore.getInstance("pkcs11");  
	        try {  
	            ks.load(null, senha.toCharArray());  
	        } 
	        catch (IOException e) {
	        	e.printStackTrace(System.out);
	            throw new Exception("Senha do Certificado Digital incorreta ou Certificado inválido.");  
	        }  
	  
	        KeyStore.PrivateKeyEntry pkEntry = null;  
	        Enumeration<String> aliasesEnum = ks.aliases();  
	        while (aliasesEnum.hasMoreElements()) {  
	            String alias = (String) aliasesEnum.nextElement();  
	            if (ks.isKeyEntry(alias)) {  
	                pkEntry    = (KeyStore.PrivateKeyEntry) ks.getEntry(alias, new KeyStore.PasswordProtection(senha.toCharArray()));  
	                privateKey = pkEntry.getPrivateKey();  
	                break;  
	            }  
	        }  
	  
	        X509Certificate cert = (X509Certificate) pkEntry.getCertificate();  
//	        info("SubjectDN: " + cert.getSubjectDN().toString());  
	  
	        KeyInfoFactory keyInfoFactory = signatureFactory.getKeyInfoFactory();  
	        List<X509Certificate> x509Content = new ArrayList<X509Certificate>();  
	  
	        x509Content.add(cert);  
	        X509Data x509Data = keyInfoFactory.newX509Data(x509Content);  
	        keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(x509Data));
	        return true;
    	}
    	catch(Exception e) {
    		e.printStackTrace(System.out);
    		return false;
    	}
    	finally {
    		Conexao.desconectar(connect);
    	}
    }  
    
    public static void loadCertificates(String certificado,   
            XMLSignatureFactory signatureFactory, int cdEmpresa) throws Exception {  
    	try{
    		String senha           = getPass(cdEmpresa);
	        InputStream entrada = new FileInputStream(certificado);  
	        KeyStore ks = KeyStore.getInstance("pkcs12");  
	        try {  
	            ks.load(entrada, senha.toCharArray());  
	        } catch (IOException e) {  
	            throw new Exception("Senha do Certificado Digital incorreta ou Certificado inválido.");  
	        }  
	  
	        KeyStore.PrivateKeyEntry pkEntry = null;  
	        Enumeration<String> aliasesEnum = ks.aliases();  
	        while (aliasesEnum.hasMoreElements()) {  
	            String alias = (String) aliasesEnum.nextElement();  
	            if (ks.isKeyEntry(alias)) {  
	                pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias,  
	                        new KeyStore.PasswordProtection(senha.toCharArray()));  
	                privateKey = pkEntry.getPrivateKey();  
	                break;  
	            }  
	        }  
	  
	        X509Certificate cert = (X509Certificate) pkEntry.getCertificate();  
	        info("SubjectDN: " + cert.getSubjectDN().toString());
	  
	        KeyInfoFactory keyInfoFactory = signatureFactory.getKeyInfoFactory();  
	        List<X509Certificate> x509Content = new ArrayList<X509Certificate>();  
	  
	        x509Content.add(cert);  
	        X509Data x509Data = keyInfoFactory.newX509Data(x509Content);  
	        keyInfo = keyInfoFactory.newKeyInfo(Collections.singletonList(x509Data));  
    	}
    	catch(Exception e) {
    		e.printStackTrace(System.out);
    	}
    }  
  
    public static ArrayList<Transform> signatureFactory(XMLSignatureFactory signatureFactory) {
    	try	{
	        ArrayList<Transform> transformList = new ArrayList<Transform>();  
	        TransformParameterSpec tps = null;  
	        Transform envelopedTransform = signatureFactory.newTransform(Transform.ENVELOPED, tps);  
	        Transform c14NTransform = signatureFactory.newTransform("http://www.w3.org/TR/2001/REC-xml-c14n-20010315", tps);  
	  
	        transformList.add(envelopedTransform);  
	        transformList.add(c14NTransform);  
	        return transformList;
    	}
    	catch(Exception e) {
    		e.printStackTrace(System.out);
    		return null;
    	}
    }  
  
	/** 
     * Info. 
     * @param log 
     */  
    private static void info(String log) {  
        System.out.println("INFO: " + log);  
    }  
  
    /** 
     * Error. 
     * @param log 
     */  
    private static void error(String log) {  
        System.out.println("ERROR: " + log);  
   	}
}
