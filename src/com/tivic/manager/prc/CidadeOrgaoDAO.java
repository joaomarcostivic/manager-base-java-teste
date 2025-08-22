package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CidadeOrgaoDAO{

	public static int insert(CidadeOrgao objeto) {
		return insert(objeto, null);
	}

	public static int insert(CidadeOrgao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PRC_CIDADE_ORGAO (CD_ORGAO,"+
			                                  "CD_CIDADE,"+
			                                  "QT_DISTANCIA,"+ 
			                                  "LG_PRINCIPAL) VALUES (?, ?, ?, ?)");
			if(objeto.getCdOrgao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdOrgao());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCidade());
			pstmt.setFloat(3,objeto.getQtDistancia());
			if(objeto.getLgPrincipal()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getLgPrincipal());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CidadeOrgao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(CidadeOrgao objeto, int cdCidadeOld, int cdOrgaoOld) {
		return update(objeto, cdCidadeOld, cdOrgaoOld, null);
	}

	public static int update(CidadeOrgao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(CidadeOrgao objeto, int cdCidadeOld, int cdOrgaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PRC_CIDADE_ORGAO SET CD_ORGAO=?,"+
												      		   "CD_CIDADE=?,"+
												      		   "QT_DISTANCIA=?, " + 
												      		   "LG_PRINCIPAL=? WHERE CD_CIDADE=? AND CD_ORGAO=?");
			pstmt.setInt(1,objeto.getCdOrgao());
			pstmt.setInt(2,objeto.getCdCidade());
			pstmt.setFloat(3,objeto.getQtDistancia());
			pstmt.setInt(4,objeto.getLgPrincipal());
			pstmt.setInt(5, cdCidadeOld!=0 ? cdCidadeOld : objeto.getCdCidade());
			pstmt.setInt(6, cdOrgaoOld!=0 ? cdOrgaoOld : objeto.getCdOrgao());
			
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCidade, int cdOrgao) {
		return delete(cdCidade, cdOrgao, null);
	}

	public static int delete(int cdCidade, int cdOrgao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM PRC_CIDADE_ORGAO WHERE CD_CIDADE=? AND CD_ORGAO=?");
			pstmt.setInt(1, cdCidade);
			pstmt.setInt(2, cdOrgao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CidadeOrgao get(int cdCidade, int cdOrgao) {
		return get(cdCidade, cdOrgao, null);
	}

	public static CidadeOrgao get(int cdCidade, int cdOrgao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PRC_CIDADE_ORGAO WHERE CD_CIDADE=? AND CD_ORGAO=?");
			pstmt.setInt(1, cdCidade);
			pstmt.setInt(2, cdOrgao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CidadeOrgao(rs.getInt("CD_ORGAO"),
						rs.getInt("CD_CIDADE"),
						rs.getFloat("QT_DISTANCIA"),
						rs.getInt("LG_PRINCIPAL"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM PRC_CIDADE_ORGAO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CidadeOrgao> getList() {
		return getList(null);
	}

	public static ArrayList<CidadeOrgao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CidadeOrgao> list = new ArrayList<CidadeOrgao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CidadeOrgao obj = CidadeOrgaoDAO.get(rsm.getInt("CD_CIDADE"), rsm.getInt("CD_ORGAO"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CidadeOrgaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM PRC_CIDADE_ORGAO", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
