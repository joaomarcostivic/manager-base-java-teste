package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class RegraFatTipoAndamentoDAO{

	public static int insert(RegraFatTipoAndamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(RegraFatTipoAndamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PRC_REGRA_FAT_TIPO_ANDAMENTO (CD_REGRA_FATURAMENTO,"+
			                                  "CD_TIPO_ANDAMENTO) VALUES (?, ?)");
			if(objeto.getCdRegraFaturamento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdRegraFaturamento());
			if(objeto.getCdTipoAndamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoAndamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoAndamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoAndamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(RegraFatTipoAndamento objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(RegraFatTipoAndamento objeto, int cdRegraFaturamentoOld, int cdTipoAndamentoOld) {
		return update(objeto, cdRegraFaturamentoOld, cdTipoAndamentoOld, null);
	}

	public static int update(RegraFatTipoAndamento objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(RegraFatTipoAndamento objeto, int cdRegraFaturamentoOld, int cdTipoAndamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PRC_REGRA_FAT_TIPO_ANDAMENTO SET CD_REGRA_FATURAMENTO=?,"+
												      		   "CD_TIPO_ANDAMENTO=? WHERE CD_REGRA_FATURAMENTO=? AND CD_TIPO_ANDAMENTO=?");
			pstmt.setInt(1,objeto.getCdRegraFaturamento());
			pstmt.setInt(2,objeto.getCdTipoAndamento());
			pstmt.setInt(3, cdRegraFaturamentoOld!=0 ? cdRegraFaturamentoOld : objeto.getCdRegraFaturamento());
			pstmt.setInt(4, cdTipoAndamentoOld!=0 ? cdTipoAndamentoOld : objeto.getCdTipoAndamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoAndamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoAndamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRegraFaturamento, int cdTipoAndamento) {
		return delete(cdRegraFaturamento, cdTipoAndamento, null);
	}

	public static int delete(int cdRegraFaturamento, int cdTipoAndamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM PRC_REGRA_FAT_TIPO_ANDAMENTO WHERE CD_REGRA_FATURAMENTO=? AND CD_TIPO_ANDAMENTO=?");
			pstmt.setInt(1, cdRegraFaturamento);
			pstmt.setInt(2, cdTipoAndamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoAndamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoAndamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static RegraFatTipoAndamento get(int cdRegraFaturamento, int cdTipoAndamento) {
		return get(cdRegraFaturamento, cdTipoAndamento, null);
	}

	public static RegraFatTipoAndamento get(int cdRegraFaturamento, int cdTipoAndamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PRC_REGRA_FAT_TIPO_ANDAMENTO WHERE CD_REGRA_FATURAMENTO=? AND CD_TIPO_ANDAMENTO=?");
			pstmt.setInt(1, cdRegraFaturamento);
			pstmt.setInt(2, cdTipoAndamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new RegraFatTipoAndamento(rs.getInt("CD_REGRA_FATURAMENTO"),
						rs.getInt("CD_TIPO_ANDAMENTO"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoAndamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoAndamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM PRC_REGRA_FAT_TIPO_ANDAMENTO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoAndamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoAndamentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<RegraFatTipoAndamento> getList() {
		return getList(null);
	}

	public static ArrayList<RegraFatTipoAndamento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<RegraFatTipoAndamento> list = new ArrayList<RegraFatTipoAndamento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				RegraFatTipoAndamento obj = RegraFatTipoAndamentoDAO.get(rsm.getInt("CD_REGRA_FATURAMENTO"), rsm.getInt("CD_TIPO_ANDAMENTO"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RegraFatTipoAndamentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM PRC_REGRA_FAT_TIPO_ANDAMENTO", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
