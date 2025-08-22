package com.tivic.manager.fta;

import java.sql.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

public class CorServices {
	
	public static ResultSetMap getSyncData() {
		return getSyncData(null);
	}
	
	public static ResultSetMap getSyncData(Connection connect) {
		
		boolean isConnectionNull = connect == null;
		
		if(isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			ResultSetMap rsm = getAll(connect);
			return toStrCores(rsm);
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}

	public static Result save(Cor cor){
		return save(cor, null);
	}

	public static Result save(Cor cor, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(cor==null)
				return new Result(-1, "Erro ao salvar. Cor é nulo");

			int retorno;
			if(cor.getCdCor()==0){
				retorno = CorDAO.insert(cor, connect);
				cor.setCdCor(retorno);
			}
			else {
				retorno = CorDAO.update(cor, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "COR", cor);
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
	public static Result remove(int cdCor){
		return remove(cdCor, false, null);
	}
	public static Result remove(int cdCor, boolean cascade){
		return remove(cdCor, cascade, null);
	}
	public static Result remove(int cdCor, boolean cascade, Connection connect){
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
			retorno = CorDAO.delete(cdCor, connect);
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
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			if(lgBaseAntiga) {
				pstmt = connect.prepareStatement("SELECT cod_cor AS cd_cor, nm_cor FROM cor");
			} else {
				pstmt = connect.prepareStatement("SELECT * FROM fta_cor");				
			}
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CorServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CorServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

	public static Cor get(int cdCor) {
		return get(cdCor, null);
	}

	public static Cor get(int cdCor, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			if(!Util.isStrBaseAntiga()) {
				pstmt = connect.prepareStatement("SELECT * FROM fta_cor WHERE cd_cor=?");
				pstmt.setInt(1, cdCor);
				rs = pstmt.executeQuery();
				if(rs.next()){
					return new Cor(rs.getInt("cd_cor"),
							rs.getString("nm_cor"));
				}
			} else {
				pstmt = connect.prepareStatement("SELECT * FROM cor WHERE cod_cor=?");
				pstmt.setInt(1, cdCor);
				rs = pstmt.executeQuery();
				if(rs.next()){
					return new Cor(rs.getInt("cod_cor"),
							rs.getString("nm_cor"));
				}
			}
			
			return null;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CorDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CorDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Cor getByNome(String nmCor) {
		return getByNome(nmCor, null);
	}

	public static Cor getByNome(String nmCor, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			if(!Util.isStrBaseAntiga()) {
				pstmt = connect.prepareStatement("SELECT * FROM fta_cor WHERE nm_cor LIKE ?");
				pstmt.setString(1, nmCor);
				rs = pstmt.executeQuery();
				if(rs.next()){
					return new Cor(rs.getInt("cd_cor"),
							rs.getString("nm_cor"));
				}
			} else {
				pstmt = connect.prepareStatement("SELECT * FROM cor WHERE nm_cor LIKE ?");
				pstmt.setString(1, nmCor);
				rs = pstmt.executeQuery();
				if(rs.next()){
					return new Cor(rs.getInt("cod_cor"),
							rs.getString("nm_cor"));
				}
			}

			return null;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CorDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CorDAO.get: " + e);
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
		return Search.find("SELECT * FROM fta_cor", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	private static ResultSetMap toStrCores(ResultSetMap rsm) {		
		
		if (Util.isStrBaseAntiga()) {
			
			ResultSetMap rsmOut = new ResultSetMap();
			
			while(rsm.next()) {
				
				Cor corFta = new Cor(
						rsm.getInt("cd_cor"),
						rsm.getString("nm_cor")
						);
				
				com.tivic.manager.str.Cor conv = toStrCor(corFta);
				
				HashMap<String, Object> hash = new HashMap<String, Object>();
				
				hash.put("cd_cor", conv.getCdCor());
				hash.put("nm_cor", conv.getNmCor());
				
				rsmOut.addRegister(hash);
				
			}
			
			return rsmOut;
			
		} else {
			
			return rsm;
			
		}
	}
	
	private static com.tivic.manager.str.Cor toStrCor(Cor cor) {
		return new com.tivic.manager.str.Cor(
				cor.getCdCor(),
				cor.getNmCor()
				);
	}

}
