package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ClassificacaoDAO{

	public static int insert(Classificacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(Classificacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_classificacao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdClassificacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_classificacao (cd_classificacao,"+
			                                  "nm_classificacao,"+
			                                  "id_classificacao,"+
			                                  "txt_descricao,"+
			                                  "lg_ativo,"+
			                                  "lg_padrao,"+
			                                  "cd_cobranca) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmClassificacao());
			pstmt.setString(3,objeto.getIdClassificacao());
			pstmt.setString(4,objeto.getTxtDescricao());
			pstmt.setInt(5,objeto.getLgAtivo());
			pstmt.setInt(6,objeto.getLgPadrao());
			if(objeto.getCdCobranca()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdCobranca());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Classificacao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Classificacao objeto, int cdClassificacaoOld) {
		return update(objeto, cdClassificacaoOld, null);
	}

	public static int update(Classificacao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Classificacao objeto, int cdClassificacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_classificacao SET cd_classificacao=?,"+
												      		   "nm_classificacao=?,"+
												      		   "id_classificacao=?,"+
												      		   "txt_descricao=?,"+
												      		   "lg_ativo=?,"+
												      		   "lg_padrao=?,"+
												      		   "cd_cobranca=? WHERE cd_classificacao=?");
			pstmt.setInt(1,objeto.getCdClassificacao());
			pstmt.setString(2,objeto.getNmClassificacao());
			pstmt.setString(3,objeto.getIdClassificacao());
			pstmt.setString(4,objeto.getTxtDescricao());
			pstmt.setInt(5,objeto.getLgAtivo());
			pstmt.setInt(6,objeto.getLgPadrao());
			if(objeto.getCdCobranca()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdCobranca());
			pstmt.setInt(8, cdClassificacaoOld!=0 ? cdClassificacaoOld : objeto.getCdClassificacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdClassificacao) {
		return delete(cdClassificacao, null);
	}

	public static int delete(int cdClassificacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_classificacao WHERE cd_classificacao=?");
			pstmt.setInt(1, cdClassificacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Classificacao get(int cdClassificacao) {
		return get(cdClassificacao, null);
	}

	public static Classificacao get(int cdClassificacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_classificacao WHERE cd_classificacao=?");
			pstmt.setInt(1, cdClassificacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Classificacao(rs.getInt("cd_classificacao"),
						rs.getString("nm_classificacao"),
						rs.getString("id_classificacao"),
						rs.getString("txt_descricao"),
						rs.getInt("lg_ativo"),
						rs.getInt("lg_padrao"),
						rs.getInt("cd_cobranca"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoDAO.get: " + e);
			return null;
		}
		finally {
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_classificacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_classificacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
