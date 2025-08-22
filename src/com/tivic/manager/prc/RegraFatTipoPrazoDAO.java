package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class RegraFatTipoPrazoDAO{

	public static int insert(RegraFatTipoPrazo objeto) {
		return insert(objeto, null);
	}

	public static int insert(RegraFatTipoPrazo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PRC_REGRA_FAT_TIPO_PRAZO (CD_REGRA_FATURAMENTO,"+
			                                  "CD_TIPO_PRAZO) VALUES (?, ?)");
			if(objeto.getCdRegraFaturamento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdRegraFaturamento());
			if(objeto.getCdTipoPrazo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoPrazo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoPrazoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoPrazoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(RegraFatTipoPrazo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(RegraFatTipoPrazo objeto, int cdRegraFaturamentoOld, int cdTipoPrazoOld) {
		return update(objeto, cdRegraFaturamentoOld, cdTipoPrazoOld, null);
	}

	public static int update(RegraFatTipoPrazo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(RegraFatTipoPrazo objeto, int cdRegraFaturamentoOld, int cdTipoPrazoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PRC_REGRA_FAT_TIPO_PRAZO SET CD_REGRA_FATURAMENTO=?,"+
												      		   "CD_TIPO_PRAZO=? WHERE CD_REGRA_FATURAMENTO=? AND CD_TIPO_PRAZO=?");
			pstmt.setInt(1,objeto.getCdRegraFaturamento());
			pstmt.setInt(2,objeto.getCdTipoPrazo());
			pstmt.setInt(3, cdRegraFaturamentoOld!=0 ? cdRegraFaturamentoOld : objeto.getCdRegraFaturamento());
			pstmt.setInt(4, cdTipoPrazoOld!=0 ? cdTipoPrazoOld : objeto.getCdTipoPrazo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoPrazoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoPrazoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRegraFaturamento, int cdTipoPrazo) {
		return delete(cdRegraFaturamento, cdTipoPrazo, null);
	}

	public static int delete(int cdRegraFaturamento, int cdTipoPrazo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM PRC_REGRA_FAT_TIPO_PRAZO WHERE CD_REGRA_FATURAMENTO=? AND CD_TIPO_PRAZO=?");
			pstmt.setInt(1, cdRegraFaturamento);
			pstmt.setInt(2, cdTipoPrazo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoPrazoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoPrazoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static RegraFatTipoPrazo get(int cdRegraFaturamento, int cdTipoPrazo) {
		return get(cdRegraFaturamento, cdTipoPrazo, null);
	}

	public static RegraFatTipoPrazo get(int cdRegraFaturamento, int cdTipoPrazo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PRC_REGRA_FAT_TIPO_PRAZO WHERE CD_REGRA_FATURAMENTO=? AND CD_TIPO_PRAZO=?");
			pstmt.setInt(1, cdRegraFaturamento);
			pstmt.setInt(2, cdTipoPrazo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new RegraFatTipoPrazo(rs.getInt("CD_REGRA_FATURAMENTO"),
						rs.getInt("CD_TIPO_PRAZO"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoPrazoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoPrazoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM PRC_REGRA_FAT_TIPO_PRAZO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoPrazoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoPrazoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<RegraFatTipoPrazo> getList() {
		return getList(null);
	}

	public static ArrayList<RegraFatTipoPrazo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<RegraFatTipoPrazo> list = new ArrayList<RegraFatTipoPrazo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				RegraFatTipoPrazo obj = RegraFatTipoPrazoDAO.get(rsm.getInt("CD_REGRA_FATURAMENTO"), rsm.getInt("CD_TIPO_PRAZO"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoPrazoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM PRC_REGRA_FAT_TIPO_PRAZO", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
