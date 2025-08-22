package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class BairroServices {
	public static Result save(Bairro bairro){
		return save(bairro, null);
	}
	
	public static Result save(Bairro bairro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(bairro==null)
				return new Result(-1, "Erro ao salvar. Bairro é nulo");
			
			int retorno;
			if(bairro.getCdBairro()==0){
				retorno = BairroDAO.insert(bairro, connect);
				bairro.setCdBairro(retorno);
			}
			else {
				retorno = BairroDAO.update(bairro, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "BAIRRO", bairro);
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
	
//	public static int save(Bairro bairro){
//		return save(bairro, null).getCode();
//	}

//	public static int save(Bairro bairro, Connection connect){
//		boolean isConnectionNull = connect==null;
//		try {
//			if (isConnectionNull) {
//				connect = Conexao.conectar();
//			}
//
//			int retorno = 1;
//			if(bairro.getCdBairro()==0){
//				retorno = BairroDAO.insert(bairro, connect);
//				if(retorno > 0){
//					bairro.setCdBairro(retorno);
//				}
//			}
//			else {
//				retorno = BairroDAO.update(bairro, connect);
//			}
//
//			return retorno;
//		}
//		catch(Exception e){
//			e.printStackTrace(System.out);
//			System.err.println("Erro! BairroServices.save: " +  e);
//			if (isConnectionNull)
//				Conexao.rollback(connect);
//			return -1;
//		}
//		finally{
//			if (isConnectionNull)
//				Conexao.desconectar(connect);
//		}
//	}

	public static Result remove(int cdBairro){
		return remove(cdBairro, false, null);
	}
	
	public static Result remove(int cdBairro, boolean cascade){
		return remove(cdBairro, cascade, null);
	}
	
	public static Result remove(int cdBairro, boolean cascade, Connection connect){
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
				retorno = BairroDAO.delete(cdBairro, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este bairro está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Bairro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir bairro!");
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_bairro ORDER BY nm_bairro");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BairroDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Bairro getBairroByNome(String nome, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			if(!Util.isStrBaseAntiga()) {
				pstmt = connect.prepareStatement("SELECT * FROM grl_bairro WHERE nm_bairro = ?");
				pstmt.setString(1, nome);
				rs = pstmt.executeQuery();
				if(rs.next()){
					return new Bairro(rs.getInt("cd_bairro"),
							rs.getInt("cd_distrito"),
							rs.getInt("cd_cidade"),
							rs.getString("nm_bairro"),
							rs.getString("id_bairro"),
							rs.getInt("cd_regiao"));
				}
				
				return null;
			} else {
				pstmt = connect.prepareStatement("SELECT * FROM bairro WHERE nm_bairro = ?");
				pstmt.setString(1, nome);
				rs = pstmt.executeQuery();
				if(rs.next()){
					return new Bairro(rs.getInt("cod_bairro"),
							0,
							rs.getInt("cod_municipio"),
							rs.getString("nm_bairro"),
							null,
							rs.getInt("cd_regiao"));
				}
				
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BairroDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BairroDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Bairro getBairroByNomeCidade(String nome, int cdCidade, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			
			String sql = Util.isStrBaseAntiga() ? "SELECT * FROM bairro WHERE nm_bairro = ? AND cd_cidade = ?" : "SELECT * FROM grl_bairro WHERE nm_bairro = ? AND cd_cidade = ?";			
			pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, nome);
			pstmt.setInt(2, cdCidade);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return BairroDAO.get(rs.getInt(Util.isStrBaseAntiga() ? "cod_bairro" : "cd_bairro"), connect);
			}
			
			return null;
			
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BairroServices.getBairroByNomeCidade: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BairroServices.getBairroByNomeCidade: " + e);
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
		return Search.find("SELECT A.*, " +
				   "	   B.nm_distrito, " +
				   "	   C.nm_cidade, " +
				   "	   D.sg_estado, " +
				   "	   E.nm_regiao " +
		           "FROM grl_bairro A " +
		           "	LEFT OUTER JOIN grl_distrito B ON (A.cd_distrito = B.cd_distrito AND A.cd_cidade = B.cd_cidade) " +
		           "	JOIN grl_cidade  C ON (A.cd_cidade  = C.cd_cidade) " +
				   "	LEFT OUTER JOIN grl_estado D ON (C.cd_estado = D.cd_estado) " +
				   "	LEFT OUTER JOIN grl_regiao E ON (A.cd_regiao = E.cd_regiao) WHERE 1=1 ",
		           criterios,connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}