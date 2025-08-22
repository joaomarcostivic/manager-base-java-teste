package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class RegraClassificacaoDAO{

	public static int insert(RegraClassificacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(RegraClassificacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_regra_classificacao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRegra(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_regra_classificacao (cd_regra,"+
			                                  "cd_criterio,"+
			                                  "tp_operador_relacional,"+
			                                  "vl_referencia,"+
			                                  "lg_relativo) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdCriterio()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCriterio());
			pstmt.setInt(3,objeto.getTpOperadorRelacional());
			pstmt.setFloat(4,objeto.getVlReferencia());
			pstmt.setInt(5,objeto.getLgRelativo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraClassificacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraClassificacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(RegraClassificacao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(RegraClassificacao objeto, int cdRegraOld) {
		return update(objeto, cdRegraOld, null);
	}

	public static int update(RegraClassificacao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(RegraClassificacao objeto, int cdRegraOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_regra_classificacao SET cd_regra=?,"+
												      		   "cd_criterio=?,"+
												      		   "tp_operador_relacional=?,"+
												      		   "vl_referencia=?,"+
												      		   "lg_relativo=? WHERE cd_regra=?");
			pstmt.setInt(1,objeto.getCdRegra());
			if(objeto.getCdCriterio()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCriterio());
			pstmt.setInt(3,objeto.getTpOperadorRelacional());
			pstmt.setFloat(4,objeto.getVlReferencia());
			pstmt.setInt(5,objeto.getLgRelativo());
			pstmt.setInt(6, cdRegraOld!=0 ? cdRegraOld : objeto.getCdRegra());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraClassificacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraClassificacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRegra) {
		return delete(cdRegra, null);
	}

	public static int delete(int cdRegra, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_regra_classificacao WHERE cd_regra=?");
			pstmt.setInt(1, cdRegra);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraClassificacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraClassificacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static RegraClassificacao get(int cdRegra) {
		return get(cdRegra, null);
	}

	public static RegraClassificacao get(int cdRegra, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_regra_classificacao WHERE cd_regra=?");
			pstmt.setInt(1, cdRegra);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new RegraClassificacao(rs.getInt("cd_regra"),
						rs.getInt("cd_criterio"),
						rs.getInt("tp_operador_relacional"),
						rs.getFloat("vl_referencia"),
						rs.getInt("lg_relativo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraClassificacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraClassificacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_regra_classificacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraClassificacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraClassificacaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_regra_classificacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
