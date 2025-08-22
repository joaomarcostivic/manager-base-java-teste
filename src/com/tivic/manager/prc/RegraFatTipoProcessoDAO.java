package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class RegraFatTipoProcessoDAO{

	public static int insert(RegraFatTipoProcesso objeto) {
		return insert(objeto, null);
	}

	public static int insert(RegraFatTipoProcesso objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PRC_REGRA_FAT_TIPO_PROCESSO (CD_REGRA_FATURAMENTO,"+
			                                  "CD_TIPO_PROCESSO) VALUES (?, ?)");
			if(objeto.getCdRegraFaturamento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdRegraFaturamento());
			if(objeto.getCdTipoProcesso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoProcesso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoProcessoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoProcessoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(RegraFatTipoProcesso objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(RegraFatTipoProcesso objeto, int cdRegraFaturamentoOld, int cdTipoProcessoOld) {
		return update(objeto, cdRegraFaturamentoOld, cdTipoProcessoOld, null);
	}

	public static int update(RegraFatTipoProcesso objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(RegraFatTipoProcesso objeto, int cdRegraFaturamentoOld, int cdTipoProcessoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PRC_REGRA_FAT_TIPO_PROCESSO SET CD_REGRA_FATURAMENTO=?,"+
												      		   "CD_TIPO_PROCESSO=? WHERE CD_REGRA_FATURAMENTO=? AND CD_TIPO_PROCESSO=?");
			pstmt.setInt(1,objeto.getCdRegraFaturamento());
			pstmt.setInt(2,objeto.getCdTipoProcesso());
			pstmt.setInt(3, cdRegraFaturamentoOld!=0 ? cdRegraFaturamentoOld : objeto.getCdRegraFaturamento());
			pstmt.setInt(4, cdTipoProcessoOld!=0 ? cdTipoProcessoOld : objeto.getCdTipoProcesso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoProcessoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoProcessoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRegraFaturamento, int cdTipoProcesso) {
		return delete(cdRegraFaturamento, cdTipoProcesso, null);
	}

	public static int delete(int cdRegraFaturamento, int cdTipoProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM PRC_REGRA_FAT_TIPO_PROCESSO WHERE CD_REGRA_FATURAMENTO=? AND CD_TIPO_PROCESSO=?");
			pstmt.setInt(1, cdRegraFaturamento);
			pstmt.setInt(2, cdTipoProcesso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoProcessoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoProcessoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static RegraFatTipoProcesso get(int cdRegraFaturamento, int cdTipoProcesso) {
		return get(cdRegraFaturamento, cdTipoProcesso, null);
	}

	public static RegraFatTipoProcesso get(int cdRegraFaturamento, int cdTipoProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PRC_REGRA_FAT_TIPO_PROCESSO WHERE CD_REGRA_FATURAMENTO=? AND CD_TIPO_PROCESSO=?");
			pstmt.setInt(1, cdRegraFaturamento);
			pstmt.setInt(2, cdTipoProcesso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new RegraFatTipoProcesso(rs.getInt("CD_REGRA_FATURAMENTO"),
						rs.getInt("CD_TIPO_PROCESSO"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoProcessoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoProcessoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM PRC_REGRA_FAT_TIPO_PROCESSO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoProcessoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoProcessoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<RegraFatTipoProcesso> getList() {
		return getList(null);
	}

	public static ArrayList<RegraFatTipoProcesso> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<RegraFatTipoProcesso> list = new ArrayList<RegraFatTipoProcesso>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				RegraFatTipoProcesso obj = RegraFatTipoProcessoDAO.get(rsm.getInt("CD_REGRA_FATURAMENTO"), rsm.getInt("CD_TIPO_PROCESSO"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoProcessoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM PRC_REGRA_FAT_TIPO_PROCESSO", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
