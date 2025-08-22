package com.tivic.manager.seg;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;

public class UsuarioChaveServices {

	public static final int TP_CHAVE_DSA_512 = 0;
	public static final int TP_CHAVE_DSA_1024 = 1;
	
	
	public static Result save(UsuarioChave usuarioChave){
		return save(usuarioChave, null, null);
	}

	public static Result save(UsuarioChave usuarioChave, AuthData authData){
		return save(usuarioChave, authData, null);
	}

	public static Result save(UsuarioChave usuarioChave, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(usuarioChave==null)
				return new Result(-1, "Erro ao salvar. UsuarioChave é nulo");

			int retorno;
			if(usuarioChave.getCdChave()==0){
				retorno = UsuarioChaveDAO.insert(usuarioChave, connect);
				usuarioChave.setCdChave(retorno);
			}
			else {
				retorno = UsuarioChaveDAO.update(usuarioChave, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "USUARIOCHAVE", usuarioChave);
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
	public static Result remove(int cdChave, int cdUsuario){
		return remove(cdChave, cdUsuario, false, null);
	}
	public static Result remove(int cdChave, int cdUsuario, boolean cascade){
		return remove(cdChave, cdUsuario, cascade, null);
	}
	public static Result remove(int cdChave, int cdUsuario, boolean cascade, Connection connect){
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
			retorno = UsuarioChaveDAO.delete(cdChave, cdUsuario, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_usuario_chave");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioChaveServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioChaveServices.getAll: " + e);
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
		return Search.find("SELECT * FROM seg_usuario_chave", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	
	public static Result gerarChaves(int cdUsuario){
		return gerarChaves(cdUsuario, null);
	}
	
	public static Result gerarChaves(int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			/**
			 * 1. GERAR AS CHAVES PUBLICA/PRIVADA
			 * 2. INATIVAR CHAVES ANTERIORES
			 * 3. GRAVAR CHAVES NOVAS 
			 */
			
			//1. GERAR CHAVES
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(1024, random);
			KeyPair pair = keyGen.generateKeyPair();
			
			PrivateKey privateKey = pair.getPrivate();
			PublicKey publicKey = pair.getPublic();
			
			//2. INATIVAR CHAVES
			PreparedStatement pstmt = connect.prepareStatement("UPDATE seg_usuario_chave SET st_chave=0, dt_inativacao=? WHERE cd_usuario=? AND st_chave=1");
			pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			pstmt.setInt(2, cdUsuario);
			pstmt.executeUpdate();
			
			//3. GRAVAR CHAVES NOVAS
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
			byte[] blbChavePublica = x509EncodedKeySpec.getEncoded();
			
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
			byte[] blbChavePrivada = pkcs8EncodedKeySpec.getEncoded();
			
			if(blbChavePublica==null || blbChavePublica.length==0) {
				Conexao.rollback(connect);
				return new Result(-3, "Chave publica não pode ser recuperada.");
			}
			
			if(blbChavePrivada==null || blbChavePrivada.length==0) {
				Conexao.rollback(connect);
				return new Result(-4, "Chave publica não pode ser recuperada.");
			}
			
			UsuarioChave usuarioChave = new UsuarioChave(0, cdUsuario, TP_CHAVE_DSA_1024, blbChavePublica, blbChavePrivada, new GregorianCalendar(), null, 1);
			Result r = save(usuarioChave, null, connect);
							
			if(r.getCode()<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Erro ao salvar chaves publica/privada do usuário!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			r.setMessage("Chaves geradas com sucesso!");
			return r;
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao gerar chaves!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
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

	
	public static UsuarioChave getChaveAtiva(int cdUsuario) {
		return getChaveAtiva(cdUsuario, null);
	}

	public static UsuarioChave getChaveAtiva(int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_usuario_chave WHERE st_chave = 1");
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				UsuarioChave usuarioChave = UsuarioChaveDAO.get(rs.getInt("CD_CHAVE"), cdUsuario, connect);
				return usuarioChave;
			}
			else 
				return null;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioChaveServices.getChaveAtiva: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioChaveServices.getChaveAtiva: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}