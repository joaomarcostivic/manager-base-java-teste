package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ClassificacaoRegraDAO{

	public static int insert(ClassificacaoRegra objeto) {
		return insert(objeto, null);
	}

	public static int insert(ClassificacaoRegra objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_classificacao_regra (cd_classificacao,"+
			                                  "cd_regra,"+
			                                  "nr_ordem,"+
			                                  "tp_operador) VALUES (?, ?, ?, ?)");
			if(objeto.getCdClassificacao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdClassificacao());
			if(objeto.getCdRegra()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdRegra());
			pstmt.setInt(3,objeto.getNrOrdem());
			pstmt.setInt(4,objeto.getTpOperador());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoRegraDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoRegraDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ClassificacaoRegra objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ClassificacaoRegra objeto, int cdClassificacaoOld, int cdRegraOld) {
		return update(objeto, cdClassificacaoOld, cdRegraOld, null);
	}

	public static int update(ClassificacaoRegra objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ClassificacaoRegra objeto, int cdClassificacaoOld, int cdRegraOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_classificacao_regra SET cd_classificacao=?,"+
												      		   "cd_regra=?,"+
												      		   "nr_ordem=?,"+
												      		   "tp_operador=? WHERE cd_classificacao=? AND cd_regra=?");
			pstmt.setInt(1,objeto.getCdClassificacao());
			pstmt.setInt(2,objeto.getCdRegra());
			pstmt.setInt(3,objeto.getNrOrdem());
			pstmt.setInt(4,objeto.getTpOperador());
			pstmt.setInt(5, cdClassificacaoOld!=0 ? cdClassificacaoOld : objeto.getCdClassificacao());
			pstmt.setInt(6, cdRegraOld!=0 ? cdRegraOld : objeto.getCdRegra());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoRegraDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoRegraDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdClassificacao, int cdRegra) {
		return delete(cdClassificacao, cdRegra, null);
	}

	public static int delete(int cdClassificacao, int cdRegra, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_classificacao_regra WHERE cd_classificacao=? AND cd_regra=?");
			pstmt.setInt(1, cdClassificacao);
			pstmt.setInt(2, cdRegra);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoRegraDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoRegraDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ClassificacaoRegra get(int cdClassificacao, int cdRegra) {
		return get(cdClassificacao, cdRegra, null);
	}

	public static ClassificacaoRegra get(int cdClassificacao, int cdRegra, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_classificacao_regra WHERE cd_classificacao=? AND cd_regra=?");
			pstmt.setInt(1, cdClassificacao);
			pstmt.setInt(2, cdRegra);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ClassificacaoRegra(rs.getInt("cd_classificacao"),
						rs.getInt("cd_regra"),
						rs.getInt("nr_ordem"),
						rs.getInt("tp_operador"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoRegraDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoRegraDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_classificacao_regra");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoRegraDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ClassificacaoRegraDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_classificacao_regra", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
