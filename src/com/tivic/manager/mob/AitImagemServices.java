package com.tivic.manager.mob;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.ImagemServices;
import com.tivic.manager.util.Util;

public class AitImagemServices {

	public static Result save(AitImagem aitImagem){
		return save(aitImagem, null, null);
	}

	public static Result save(AitImagem aitImagem, AuthData authData){
		return save(aitImagem, authData, null);
	}

	public static Result save(AitImagem aitImagem, Connection connect){
		return save(aitImagem, null, connect);
	}
	
	public static Result save(AitImagem aitImagem, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(aitImagem==null)
				return new Result(-1, "Erro ao salvar. AitImagem é nulo");

			int retorno;
			if(aitImagem.getCdImagem()==0){
				boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
				if(!lgBaseAntiga) {
					retorno = AitImagemDAO.insert(aitImagem, connect);					
				} else {
					HashMap<String,Object>[] keys = new HashMap[2];
					keys[0] = new HashMap<String,Object>();
					keys[0].put("FIELD_NAME", "cd_imagem");
					keys[0].put("IS_KEY_NATIVE", "YES");
					keys[1] = new HashMap<String,Object>();
					keys[1].put("FIELD_NAME", "cd_ait");
					keys[1].put("IS_KEY_NATIVE", "NO");
					keys[1].put("FIELD_VALUE", new Integer(aitImagem.getCdAit()));
					
					int code = Conexao.getSequenceCode("STR_AIT_IMAGEM", keys, connect);
					if (code <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						code = -1;
					}
					aitImagem.setCdImagem(code);
					PreparedStatement pstmt = connect.prepareStatement("INSERT INTO STR_AIT_IMAGEM (cd_imagem,"+
					                                  "cd_ait,"+
					                                  "blb_imagem) VALUES (?, ?, ?)");
					pstmt.setInt(1, code);
					if(aitImagem.getCdAit()==0)
						pstmt.setNull(2, Types.INTEGER);
					else
						pstmt.setInt(2,aitImagem.getCdAit());
					if(aitImagem.getBlbImagem()==null)
						pstmt.setNull(3, Types.BINARY);
					else
						pstmt.setBytes(3,aitImagem.getBlbImagem());
					pstmt.executeUpdate();
					retorno = code;
				}
				aitImagem.setCdImagem(retorno);
			}
			else {
				retorno = AitImagemDAO.update(aitImagem, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AITIMAGEM", aitImagem);
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
	

	public static Result save(FormDataBodyPart cdait, FormDataBodyPart files) {
		return save(cdait, files, null);
	}
	
	public static Result save(FormDataBodyPart cdAitBodyPart, FormDataBodyPart filesBodyPart, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int cdAit = Integer.parseInt(cdAitBodyPart.getEntityAs(String.class));
			
			InputStream is = filesBodyPart.getEntityAs(InputStream.class);
			AitImagem aitImagem = new AitImagem(0, cdAit, ImagemServices.writeToByteArray(is).toByteArray(), 0, 0);

			Result result = save(aitImagem, connect);
			if(result.getCode()<0) {
				if(isConnectionNull)
					connect.rollback();
				return result;
			}
			
			if(isConnectionNull)
				connect.commit();

			return new Result(1, "Salvo com sucesso...");
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
	
	
	
	public static Result remove(AitImagem aitImagem) {
		return remove(aitImagem.getCdImagem(), aitImagem.getCdAit());
	}
	public static Result remove(int cdImagem, int cdAit){
		return remove(cdImagem, cdAit, false, null, null);
	}
	public static Result remove(int cdImagem, int cdAit, boolean cascade){
		return remove(cdImagem, cdAit, cascade, null, null);
	}
	public static Result remove(int cdImagem, int cdAit, boolean cascade, AuthData authData){
		return remove(cdImagem, cdAit, cascade, authData, null);
	}
	public static Result remove(int cdImagem, int cdAit, boolean cascade, AuthData authData, Connection connect){
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
			retorno = AitImagemDAO.delete(cdImagem, cdAit, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro estÃ¡ vinculado a outros e nÃ£o pode ser excluÃ­do!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluÃ­do com sucesso!");
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_ait_imagem");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitImagemServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitImagemServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_ait_imagem", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getFromAit(int cdAit) {
		return getFromAit(cdAit, null);
	}

	public static ResultSetMap getFromAit(int cdAit, Connection connect) {
		boolean isConnectionNull = connect==null;
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA"); // Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			String sql = " SELECT A.cd_ait, A.cd_imagem, A.tp_imagem, A.lg_impressao, blb_imagem " 
					+ " FROM mob_ait_imagem A"
					+ " WHERE A.cd_ait="+cdAit;
			if(lgBaseAntiga)
				sql = " SELECT A.cd_ait, A.cd_imagem, blb_imagem " 
					+ " FROM str_ait_imagem A"
					+ " WHERE A.cd_ait="+cdAit;
			pstmt = connect.prepareStatement(sql);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AitImagemServices.getFromAit: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AitImagemServices.getFromAit: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
