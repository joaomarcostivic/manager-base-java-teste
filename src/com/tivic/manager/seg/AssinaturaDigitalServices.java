package com.tivic.manager.seg;

import java.io.ByteArrayInputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.*;
import java.util.ArrayList;
import java.util.Enumeration;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

public class AssinaturaDigitalServices {

	public static Result save(AssinaturaDigital assinaturaDigital){
		return save(assinaturaDigital, null, null);
	}

	public static Result save(AssinaturaDigital assinaturaDigital, AuthData authData){
		return save(assinaturaDigital, authData, null);
	}

	public static Result save(AssinaturaDigital assinaturaDigital, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(assinaturaDigital==null)
				return new Result(-1, "Erro ao salvar. AssinaturaDigital é nulo");

			int retorno;
			if(assinaturaDigital.getCdAssinatura()==0){
				retorno = AssinaturaDigitalDAO.insert(assinaturaDigital, connect);
				assinaturaDigital.setCdAssinatura(retorno);
			}
			else {
				retorno = AssinaturaDigitalDAO.update(assinaturaDigital, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ASSINATURADIGITAL", assinaturaDigital);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(int cdAssinatura){
		return remove(cdAssinatura, false, null, null);
	}
	public static Result remove(int cdAssinatura, boolean cascade){
		return remove(cdAssinatura, cascade, null, null);
	}
	public static Result remove(int cdAssinatura, boolean cascade, AuthData authData){
		return remove(cdAssinatura, cascade, authData, null);
	}
	public static Result remove(int cdAssinatura, boolean cascade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = AssinaturaDigitalDAO.delete(cdAssinatura, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_assinatura_digital");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AssinaturaDigitalServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AssinaturaDigitalServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM seg_assinatura_digital", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static AssinaturaDigital getAssinaturaByUsuario(int cdUsuario) {
		return getAssinaturaByUsuario(cdUsuario, null);
	}

	public static AssinaturaDigital getAssinaturaByUsuario(int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if(isConnectionNull)
				connect = Conexao.conectar();
			
			AssinaturaDigital assinatura = null;
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("CD_USUARIO", Integer.toString(cdUsuario), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = AssinaturaDigitalServices.find(criterios, connect);
			if(rsm.next()) {
				assinatura = new AssinaturaDigital(rsm.getInt("cd_assinatura"), 
						rsm.getInt("cd_chave"), 
						cdUsuario, 
						Util.convTimestampToCalendar(rsm.getTimestamp("dt_assinatura")), 
						null);
			}
			
			return assinatura;
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Gera uma assinatura digital baseada em uma chave interna
	 * @param privateKeyBytes
	 * @param dataBytes
	 * @return
	 */
	public static Result gerarAssinatura(byte[] privateKeyBytes, byte[] dataBytes) {
		try {
						
			/**
			 * 1. INICALIZAR ASSINATURA COM CHAVE PRIVADA
			 * 2. CARREGAR DADOS A SEREM ASSINADOS
			 * 3. GERAR ASSINATURA DIGITAL
			 */
			
			//1. INICALIZAR ASSINATURA COM CHAVE PRIVADA
			if(privateKeyBytes==null || privateKeyBytes.length==0) {
				return new Result(-2, "Chave privada é nula.");
			}
			
			KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
			PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
			
			Signature signature = Signature.getInstance("DSA", "SUN");
			signature.initSign(privateKey);
			
			//2. CARREGAR DADOS A SEREM ASSINADOS
			if(dataBytes==null || dataBytes.length==0) {
				return new Result(-3, "Dados a serem assinados são nulos.");
			}
			
			signature.update(dataBytes);
			
			//3. GERAR ASSINATURA DIGITAL
			byte[] signatureBytes = signature.sign();
			
			if(signatureBytes==null || signatureBytes.length==0) {
				return new Result(-4, "Assinatura não pode ser gerada.");
			}
			
			Result r = new Result(1, "Assinatura gerada com sucesso!");
			r.addObject("SIGNATURE", signatureBytes);
			return r;
		}
		catch(Exception e){
			e.printStackTrace();
			return new Result(-1, "Erro ao gerar chaves!");
		}
	}
	
	/**
	 * Valida uma assinatura digital baseada em uma chave interna
	 * @param publicKeyBytes
	 * @param dataBytes
	 * @param signatureBytes
	 * @return
	 */
	public static Result validarAssinatura(byte[] publicKeyBytes, byte[] dataBytes, byte[] signatureBytes) {
		try {
						
			/**
			 * 1. INICALIZAR VERIFICACAO DE ASSINATURA COM CHAVE PUBLICA
			 * 2. CARREGAR DADOS A SEREM VERIFICADOS ASSINADOS
			 * 3. VERIFICAR ASSINATURA DIGITAL
			 */
			
			//1. INICALIZAR ASSINATURA COM CHAVE PRIVADA
			if(publicKeyBytes==null || publicKeyBytes.length==0) {
				return new Result(-2, "Chave publica é nula.");
			}
			
			KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
			PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
			
			Signature signature = Signature.getInstance("DSA", "SUN");
			signature.initVerify(publicKey);
			
			//2. CARREGAR DADOS A SEREM ASSINADOS
			if(dataBytes==null || dataBytes.length==0) {
				return new Result(-3, "Dados a serem verificados são nulos.");
			}
			
			signature.update(dataBytes);
			
			//3. VERIFICAR ASSINATURA DIGITAL
			if(signatureBytes==null || signatureBytes.length==0) {
				return new Result(-4, "Assinatura é nula.");
			}
			
			Result r;
			if(signature.verify(signatureBytes)) {
				r = new Result(1, "Arquivo assinado corretamente. Assinatura válida.");
			}
			else {
				r = new Result(-2, "Arquivo não pode ser validado. Assinatura não corresponde.");
			}
			
			return r;
		}
		catch(Exception e){
			e.printStackTrace();
			return new Result(-1, "Erro ao verificar asinatura!");
		}
	}
	
	/**
	 * ASSINATURA PFX
	 */

	public static final String pfxAlgorithm = "RSA";
    public static final String pfxSignatureAlgorithm = "MD5withRSA";
    
    /**
     * Gera uma assinatura digital baseada em um arquivo .pfx
     * @param pfxBytes
     * @param pfxPassword
     * @param dataBytes
     * @return
     */
    public static Result gerarAssinaturaPfx(byte[] pfxBytes, String pfxPassword, byte[] dataBytes) {
    	return gerarAssinaturaPfx(pfxBytes, null, pfxPassword, dataBytes);
    }
    
	public static Result gerarAssinaturaPfx(byte[] pfxBytes, String pfxAlias, String pfxPassword, byte[] dataBytes) {
		try {
						
			/**
			 * 1. CARREGAR PFX
			 * 2. CARREGAR ALIAS
			 * 3. CARREGAR CERTIFICADO
			 * 4. EXTRAIR CHAVE PRIVADA
			 * 5. EXTRAIR CHAVE PUBLICA
			 * 6. CARREGAR DADOS A SEREM ASSINADOS
			 * 7. GERAR ASSINATURA DIGITAL
			 */
			
			//1. CARREGAR PFX
			if(pfxBytes==null || pfxBytes.length==0) {
				return new Result(-2, "Arquivo PFX é nulo.");
			}
				
			KeyStore ks = KeyStore.getInstance("PKCS12");
		    ByteArrayInputStream bais = new ByteArrayInputStream(pfxBytes);
		    ks.load(bais, pfxPassword.toCharArray());
		    
		    //2.CARREGAR ALIAS
		    if(pfxAlias==null || pfxAlias.equals("")) {
		    	Enumeration<String> enumeration = ks.aliases();
		        while(enumeration.hasMoreElements()) {
		        	pfxAlias = (String)enumeration.nextElement();
		            System.out.println("alias name: " + pfxAlias);
		        }
		    }
		    //3. CARREGAR CERTIFICADO
		    Certificate certificate = ks.getCertificate(pfxAlias);
		    
		    //TODO: VALIDAR CERTIFICADO
		    //Result rCertificado = validarCertificado(certificate);
		    
		    //4. EXTRAIR CHAVE PRIVADA
		    PrivateKey privateKey = (PrivateKey) ks.getKey(pfxAlias, pfxPassword.toCharArray());
			
		    //5. EXTRAIR CHAVE PUBLICA
		    PublicKey publicKey = certificate.getPublicKey();
		    X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
			byte[] publicKeyBytes = x509EncodedKeySpec.getEncoded();
		    
			Signature signature = Signature.getInstance(pfxSignatureAlgorithm);
			signature.initSign(privateKey);
						
			//6. CARREGAR DADOS A SEREM ASSINADOS
			if(dataBytes==null || dataBytes.length==0) {
				return new Result(-3, "Dados a serem assinados são nulos.");
			}
			
			signature.update(dataBytes);
			
			//7. GERAR ASSINATURA DIGITAL
			byte[] signatureBytes = signature.sign();
			
			if(signatureBytes==null || signatureBytes.length==0) {
				return new Result(-4, "Assinatura não pode ser gerada.");
			}
			
			Result r = new Result(1, "Assinatura gerada com sucesso!");
			r.addObject("SIGNATURE", signatureBytes);
			r.addObject("PUBLICKEY", publicKeyBytes);
			return r;
		}
		catch (java.security.cert.CertificateException e) {
            e.printStackTrace();
            return new Result(-4, "Erro ao recuperar certificado!");
        } 
    	catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return new Result(-3, "Algoritmo não existe ou é inválido!");
        } 
    	catch (KeyStoreException e) {
            e.printStackTrace();
            return new Result(-2, "Erro ao recuperar repositório de chaves!");
        } 
		catch(Exception e){
			e.printStackTrace();
			return new Result(-1, "Erro ao gerar assinatura pfx!");
		}
	} 
	
//	private static Result validarCertificado(Certificate cert) {
//        try {
//            cert.checkValidity();
//            return new Result(1, "Certificado válido!");
//        } catch (CertificateExpiredException e) {
//            return new Result(-1, "Certificado expirado!");
//        } catch (CertificateNotYetValidException e) {
//            return new Result(-1, "Certificado inválido!");
//        }
//    }

	/**
	 * Valida uma assinatura digital baseada em um arquivo .pfx
	 * @param publicKeyBytes
	 * @param dataBytes
	 * @param signatureBytes
	 * @return
	 */
	public static Result validarAssinaturaPfx(byte[] publicKeyBytes, byte[] dataBytes, byte[] signatureBytes) {
		try {
						
			/**
			 * 1. INICALIZAR VERIFICACAO DE ASSINATURA COM CHAVE PUBLICA
			 * 2. CARREGAR DADOS A SEREM VERIFICADOS ASSINADOS
			 * 3. VERIFICAR ASSINATURA DIGITAL
			 */
			
			//1. INICALIZAR ASSINATURA COM CHAVE PRIVADA
			if(publicKeyBytes==null || publicKeyBytes.length==0) {
				return new Result(-2, "Chave publica é nula.");
			}
			
			KeyFactory keyFactory = KeyFactory.getInstance("PKCS12");
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
			PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
			
			Signature signature = Signature.getInstance(pfxSignatureAlgorithm);
			signature.initVerify(publicKey);
			
			//2. CARREGAR DADOS A SEREM ASSINADOS
			if(dataBytes==null || dataBytes.length==0) {
				return new Result(-3, "Dados a serem verificados são nulos.");
			}
			
			signature.update(dataBytes);
			
			//3. VERIFICAR ASSINATURA DIGITAL
			if(signatureBytes==null || signatureBytes.length==0) {
				return new Result(-4, "Assinatura é nula.");
			}
			
			Result r;
			if(signature.verify(signatureBytes)) {
				r = new Result(1, "Arquivo assinado corretamente. Assinatura válida.");
			}
			else {
				r = new Result(-2, "Arquivo não pode ser validado. Assinatura não corresponde.");
			}
			
			return r;
		}
		catch(Exception e){
			e.printStackTrace();
			return new Result(-1, "Erro ao verificar asinatura!");
		}
	}

}