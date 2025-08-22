package com.tivic.manager.fta;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.Util;

public class PneuDAO{

	public static int insert(Pneu objeto) {
		return insert(objeto, null);
	}

	public static int insert(Pneu objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fta_pneu (cd_componente_pneu,"+
			                                  "cd_modelo,"+
			                                  "cd_posicao,"+
			                                  "nr_referencia,"+
			                                  "nr_serie,"+
			                                  "tp_aquisicao,"+
			                                  "dt_instalacao,"+
			                                  "cd_marca) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdComponentePneu()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdComponentePneu());
			if(objeto.getCdModelo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdModelo());
			if(objeto.getCdPosicao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPosicao());
			pstmt.setString(4,objeto.getNrReferencia());
			pstmt.setString(5,objeto.getNrSerie());
			pstmt.setInt(6,objeto.getTpAquisicao());
			if(objeto.getDtInstalacao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInstalacao().getTimeInMillis()));
			if(objeto.getCdMarca()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdMarca());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PneuDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PneuDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Pneu objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Pneu objeto, int cdComponentePneuOld) {
		return update(objeto, cdComponentePneuOld, null);
	}

	public static int update(Pneu objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Pneu objeto, int cdComponentePneuOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fta_pneu SET cd_componente_pneu=?,"+
												      		   "cd_modelo=?,"+
												      		   "cd_posicao=?,"+
												      		   "nr_referencia=?,"+
												      		   "nr_serie=?,"+
												      		   "tp_aquisicao=?,"+
												      		   "dt_instalacao=?,"+
												      		   "cd_marca=? WHERE cd_componente_pneu=?");
			pstmt.setInt(1,objeto.getCdComponentePneu());
			if(objeto.getCdModelo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdModelo());
			if(objeto.getCdPosicao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPosicao());
			pstmt.setString(4,objeto.getNrReferencia());
			pstmt.setString(5,objeto.getNrSerie());
			pstmt.setInt(6,objeto.getTpAquisicao());
			if(objeto.getDtInstalacao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInstalacao().getTimeInMillis()));
			if(objeto.getCdMarca()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdMarca());
			pstmt.setInt(9, cdComponentePneuOld!=0 ? cdComponentePneuOld : objeto.getCdComponentePneu());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PneuDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PneuDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdComponentePneu) {
		return delete(cdComponentePneu, null);
	}

	public static int delete(int cdComponentePneu, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fta_pneu WHERE cd_componente_pneu=?");
			pstmt.setInt(1, cdComponentePneu);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PneuDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PneuDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Pneu get(int cdComponentePneu) {
		return get(cdComponentePneu, null);
	}

	public static Pneu get(int cdComponentePneu, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fta_pneu WHERE cd_componente_pneu=?");
			pstmt.setInt(1, cdComponentePneu);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Pneu(rs.getInt("cd_componente_pneu"),
						rs.getInt("cd_modelo"),
						rs.getInt("cd_posicao"),
						rs.getString("nr_referencia"),
						rs.getString("nr_serie"),
						rs.getInt("tp_aquisicao"),
						(rs.getTimestamp("dt_instalacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_instalacao").getTime()),
						rs.getInt("cd_marca"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PneuDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PneuDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fta_pneu");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PneuDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PneuDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fta_pneu", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
