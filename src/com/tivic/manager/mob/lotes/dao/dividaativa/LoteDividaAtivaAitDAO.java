package com.tivic.manager.mob.lotes.dao.dividaativa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.manager.mob.lotes.model.dividaativa.LoteDividaAtivaAit;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.Util;


public class LoteDividaAtivaAitDAO{

	public static int insert(LoteDividaAtivaAit objeto) {
		return insert(objeto, null);
	}

	public static int insert(LoteDividaAtivaAit objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_lote_divida_ativa_ait (cd_lote_divida_ativa,"+
			                                  "cd_ait,"+
			                                  "lg_erro,"+
			                                  "dt_envio) VALUES (?, ?, ?, ?)");
			
			pstmt.setInt(1,objeto.getCdLoteDividaAtiva());
			pstmt.setInt(2,objeto.getCdAit());
			pstmt.setInt(3,objeto.getLgErro());
			if(objeto.getDtEnvio()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtEnvio().getTimeInMillis()));
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteDividaAtivaAitDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDividaAtivaAitDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LoteDividaAtivaAit objeto) {
		return update(objeto, null);
	}

	public static int update(LoteDividaAtivaAit objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_lote_divida_ativa_ait SET cd_lote_divida_ativa=?,"+
												      		   "cd_ait=?,"+
												      		   "lg_erro=?, "+
												      		   "dt_envio=? WHERE cd_lote_divida_ativa=? AND cd_ait=?");
			pstmt.setInt(1,objeto.getCdLoteDividaAtiva());
			pstmt.setInt(2,objeto.getCdAit());
			pstmt.setInt(3,objeto.getLgErro());
			if(objeto.getDtEnvio()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtEnvio().getTimeInMillis()));
			pstmt.setInt(5,objeto.getCdLoteDividaAtiva());
			pstmt.setInt(6,objeto.getCdAit());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteDividaAtivaAitDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDividaAtivaAitDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLoteDividaAtiva, int cdAit) {
		return delete(cdLoteDividaAtiva, cdAit, null);
	}

	public static int delete(int cdLoteDividaAtiva, int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_lote_divida_ativa_ait WHERE cd_lote_divida_ativa=? and cd_ait=?");
			pstmt.setInt(1, cdLoteDividaAtiva);
			pstmt.setInt(2, cdAit);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteDividaAtivaAitDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDividaAtivaAitDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LoteDividaAtivaAit get(int cdLoteDividaAtiva, int cdAit) {
		return get(cdLoteDividaAtiva, cdAit, null);
	}

	public static LoteDividaAtivaAit get(int cdLoteDividaAtiva, int cdAit, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_lote_divida_ativa_ait WHERE cd_lote_divida_ativa=? AND cd_ait=?");
			pstmt.setInt(1, cdLoteDividaAtiva);
			pstmt.setInt(2, cdAit);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new LoteDividaAtivaAit(rs.getInt("cd_lote_divida_ativa"),
						rs.getInt("cd_ait"),
						rs.getInt("lg_erro"),
						(rs.getTimestamp("dt_envio")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_envio").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteDividaAtivaAitDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDividaAtivaAitDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_lote_divida_ativa_ait");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteDividaAtivaAitDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDividaAtivaAitDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<LoteDividaAtivaAit> getList() {
		return getList(null);
	}

	public static ArrayList<LoteDividaAtivaAit> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<LoteDividaAtivaAit> list = new ArrayList<LoteDividaAtivaAit>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				LoteDividaAtivaAit obj = LoteDividaAtivaAitDAO.get(rsm.getInt("cd_lote_divida_ativa"), rsm.getInt("cd_ait"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDividaAtivaAitDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_lote_divida_ativa_ait", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
