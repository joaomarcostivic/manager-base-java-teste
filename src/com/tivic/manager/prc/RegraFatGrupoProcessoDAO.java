package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class RegraFatGrupoProcessoDAO{

	public static int insert(RegraFatGrupoProcesso objeto) {
		return insert(objeto, null);
	}

	public static int insert(RegraFatGrupoProcesso objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PRC_REGRA_FAT_GRUPO_PROCESSO (CD_REGRA_FATURAMENTO,"+
			                                  "CD_GRUPO_PROCESSO) VALUES (?, ?)");
			if(objeto.getCdRegraFaturamento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdRegraFaturamento());
			if(objeto.getCdGrupoProcesso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdGrupoProcesso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatGrupoProcessoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatGrupoProcessoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(RegraFatGrupoProcesso objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(RegraFatGrupoProcesso objeto, int cdGrupoProcessoOld, int cdRegraFaturamentoOld) {
		return update(objeto, cdGrupoProcessoOld, cdRegraFaturamentoOld, null);
	}

	public static int update(RegraFatGrupoProcesso objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(RegraFatGrupoProcesso objeto, int cdGrupoProcessoOld, int cdRegraFaturamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PRC_REGRA_FAT_GRUPO_PROCESSO SET CD_REGRA_FATURAMENTO=?,"+
												      		   "CD_GRUPO_PROCESSO=? WHERE CD_GRUPO_PROCESSO=? AND CD_REGRA_FATURAMENTO=?");
			pstmt.setInt(1,objeto.getCdRegraFaturamento());
			pstmt.setInt(2,objeto.getCdGrupoProcesso());
			pstmt.setInt(3, cdGrupoProcessoOld!=0 ? cdGrupoProcessoOld : objeto.getCdGrupoProcesso());
			pstmt.setInt(4, cdRegraFaturamentoOld!=0 ? cdRegraFaturamentoOld : objeto.getCdRegraFaturamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatGrupoProcessoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatGrupoProcessoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdGrupoProcesso, int cdRegraFaturamento) {
		return delete(cdGrupoProcesso, cdRegraFaturamento, null);
	}

	public static int delete(int cdGrupoProcesso, int cdRegraFaturamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM PRC_REGRA_FAT_GRUPO_PROCESSO WHERE CD_GRUPO_PROCESSO=? AND CD_REGRA_FATURAMENTO=?");
			pstmt.setInt(1, cdGrupoProcesso);
			pstmt.setInt(2, cdRegraFaturamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatGrupoProcessoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatGrupoProcessoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static RegraFatGrupoProcesso get(int cdGrupoProcesso, int cdRegraFaturamento) {
		return get(cdGrupoProcesso, cdRegraFaturamento, null);
	}

	public static RegraFatGrupoProcesso get(int cdGrupoProcesso, int cdRegraFaturamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PRC_REGRA_FAT_GRUPO_PROCESSO WHERE CD_GRUPO_PROCESSO=? AND CD_REGRA_FATURAMENTO=?");
			pstmt.setInt(1, cdGrupoProcesso);
			pstmt.setInt(2, cdRegraFaturamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new RegraFatGrupoProcesso(rs.getInt("CD_REGRA_FATURAMENTO"),
						rs.getInt("CD_GRUPO_PROCESSO"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatGrupoProcessoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatGrupoProcessoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM PRC_REGRA_FAT_GRUPO_PROCESSO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatGrupoProcessoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatGrupoProcessoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<RegraFatGrupoProcesso> getList() {
		return getList(null);
	}

	public static ArrayList<RegraFatGrupoProcesso> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<RegraFatGrupoProcesso> list = new ArrayList<RegraFatGrupoProcesso>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				RegraFatGrupoProcesso obj = RegraFatGrupoProcessoDAO.get(rsm.getInt("CD_GRUPO_PROCESSO"), rsm.getInt("CD_REGRA_FATURAMENTO"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatGrupoProcessoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM PRC_REGRA_FAT_GRUPO_PROCESSO", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
