package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TribunalCidadeDAO{

	public static int insert(TribunalCidade objeto) {
		return insert(objeto, null);
	}

	public static int insert(TribunalCidade objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PRC_TRIBUNAL_CIDADE (CD_TRIBUNAL,"+
			                                  "CD_CIDADE,"+
			                                  "ID_UNIDADE) VALUES (?, ?, ?)");
			if(objeto.getCdTribunal()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTribunal());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCidade());
			pstmt.setString(3,objeto.getIdUnidade());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TribunalCidadeDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TribunalCidadeDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TribunalCidade objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TribunalCidade objeto, int cdCidadeOld, int cdTribunalOld) {
		return update(objeto, cdCidadeOld, cdTribunalOld, null);
	}

	public static int update(TribunalCidade objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TribunalCidade objeto, int cdCidadeOld, int cdTribunalOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PRC_TRIBUNAL_CIDADE SET CD_TRIBUNAL=?,"+
												      		   "CD_CIDADE=?,"+
												      		   "ID_UNIDADE=? WHERE CD_CIDADE=? AND CD_TRIBUNAL=?");
			pstmt.setInt(1,objeto.getCdTribunal());
			pstmt.setInt(2,objeto.getCdCidade());
			pstmt.setString(3,objeto.getIdUnidade());
			pstmt.setInt(4, cdCidadeOld!=0 ? cdCidadeOld : objeto.getCdCidade());
			pstmt.setInt(5, cdTribunalOld!=0 ? cdTribunalOld : objeto.getCdTribunal());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TribunalCidadeDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TribunalCidadeDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCidade, int cdTribunal) {
		return delete(cdCidade, cdTribunal, null);
	}

	public static int delete(int cdCidade, int cdTribunal, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM PRC_TRIBUNAL_CIDADE WHERE CD_CIDADE=? AND CD_TRIBUNAL=?");
			pstmt.setInt(1, cdCidade);
			pstmt.setInt(2, cdTribunal);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TribunalCidadeDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TribunalCidadeDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TribunalCidade get(int cdCidade, int cdTribunal) {
		return get(cdCidade, cdTribunal, null);
	}

	public static TribunalCidade get(int cdCidade, int cdTribunal, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PRC_TRIBUNAL_CIDADE WHERE CD_CIDADE=? AND CD_TRIBUNAL=?");
			pstmt.setInt(1, cdCidade);
			pstmt.setInt(2, cdTribunal);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TribunalCidade(rs.getInt("CD_TRIBUNAL"),
						rs.getInt("CD_CIDADE"),
						rs.getString("ID_UNIDADE"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TribunalCidadeDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TribunalCidadeDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM PRC_TRIBUNAL_CIDADE");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TribunalCidadeDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TribunalCidadeDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TribunalCidade> getList() {
		return getList(null);
	}

	public static ArrayList<TribunalCidade> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TribunalCidade> list = new ArrayList<TribunalCidade>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TribunalCidade obj = TribunalCidadeDAO.get(rsm.getInt("CD_CIDADE"), rsm.getInt("CD_TRIBUNAL"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TribunalCidadeDAO.getList: " + e);
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
		return Search.find("SELECT * FROM PRC_TRIBUNAL_CIDADE", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
