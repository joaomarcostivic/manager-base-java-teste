package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class RegraFatEstadoDAO{

	public static int insert(RegraFatEstado objeto) {
		return insert(objeto, null);
	}

	public static int insert(RegraFatEstado objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PRC_REGRA_FAT_ESTADO (CD_REGRA_FATURAMENTO,"+
			                                  "CD_ESTADO) VALUES (?, ?)");
			if(objeto.getCdRegraFaturamento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdRegraFaturamento());
			if(objeto.getCdEstado()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEstado());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatEstadoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatEstadoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(RegraFatEstado objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(RegraFatEstado objeto, int cdEstadoOld, int cdRegraFaturamentoOld) {
		return update(objeto, cdEstadoOld, cdRegraFaturamentoOld, null);
	}

	public static int update(RegraFatEstado objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(RegraFatEstado objeto, int cdEstadoOld, int cdRegraFaturamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PRC_REGRA_FAT_ESTADO SET CD_REGRA_FATURAMENTO=?,"+
												      		   "CD_ESTADO=? WHERE CD_ESTADO=? AND CD_REGRA_FATURAMENTO=?");
			pstmt.setInt(1,objeto.getCdRegraFaturamento());
			pstmt.setInt(2,objeto.getCdEstado());
			pstmt.setInt(3, cdEstadoOld!=0 ? cdEstadoOld : objeto.getCdEstado());
			pstmt.setInt(4, cdRegraFaturamentoOld!=0 ? cdRegraFaturamentoOld : objeto.getCdRegraFaturamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatEstadoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatEstadoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEstado, int cdRegraFaturamento) {
		return delete(cdEstado, cdRegraFaturamento, null);
	}

	public static int delete(int cdEstado, int cdRegraFaturamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM PRC_REGRA_FAT_ESTADO WHERE CD_ESTADO=? AND CD_REGRA_FATURAMENTO=?");
			pstmt.setInt(1, cdEstado);
			pstmt.setInt(2, cdRegraFaturamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatEstadoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatEstadoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static RegraFatEstado get(int cdEstado, int cdRegraFaturamento) {
		return get(cdEstado, cdRegraFaturamento, null);
	}

	public static RegraFatEstado get(int cdEstado, int cdRegraFaturamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PRC_REGRA_FAT_ESTADO WHERE CD_ESTADO=? AND CD_REGRA_FATURAMENTO=?");
			pstmt.setInt(1, cdEstado);
			pstmt.setInt(2, cdRegraFaturamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new RegraFatEstado(rs.getInt("CD_REGRA_FATURAMENTO"),
						rs.getInt("CD_ESTADO"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatEstadoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatEstadoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM PRC_REGRA_FAT_ESTADO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatEstadoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatEstadoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<RegraFatEstado> getList() {
		return getList(null);
	}

	public static ArrayList<RegraFatEstado> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<RegraFatEstado> list = new ArrayList<RegraFatEstado>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				RegraFatEstado obj = RegraFatEstadoDAO.get(rsm.getInt("CD_ESTADO"), rsm.getInt("CD_REGRA_FATURAMENTO"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatEstadoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM PRC_REGRA_FAT_ESTADO", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
