package com.tivic.manager.adapter.base.antiga.talonario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.Util;

public class TalonarioOldDAO{

	public static int insert(TalonarioOld objeto) {
		return insert(objeto, null);
	}

	public static int insert(TalonarioOld objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("talonario", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCodTalao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO talonario (cod_talao,"+
			                                  "nr_inicial,"+
			                                  "cod_agente,"+
			                                  "nr_final,"+
			                                  "dt_entrega,"+
			                                  "dt_devolucao,"+
			                                  "st_talao,"+
			                                  "nr_talao,"+
			                                  "tp_talao,"+
			                                  "sg_talao,"+
			                                  "nr_ultimo_ait) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getNrInicial());
			pstmt.setInt(3,objeto.getCodAgente());
			pstmt.setInt(4,objeto.getNrFinal());
			if(objeto.getDtEntrega()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtEntrega().getTimeInMillis()));
			if(objeto.getDtDevolucao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtDevolucao().getTimeInMillis()));
			pstmt.setInt(7,objeto.getStTalao());
			pstmt.setInt(8,objeto.getNrTalao());
			pstmt.setInt(9,objeto.getTpTalao());
			pstmt.setString(10,objeto.getSgTalao());
			pstmt.setInt(11,objeto.getNrUltimoAit());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TalonarioOld objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TalonarioOld objeto, int codTalaoOld) {
		return update(objeto, codTalaoOld, null);
	}

	public static int update(TalonarioOld objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TalonarioOld objeto, int codTalaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE talonario SET cod_talao=?,"+
												      		   "nr_inicial=?,"+
												      		   "cod_agente=?,"+
												      		   "nr_final=?,"+
												      		   "dt_entrega=?,"+
												      		   "dt_devolucao=?,"+
												      		   "st_talao=?,"+
												      		   "nr_talao=?,"+
												      		   "tp_talao=?,"+
												      		   "sg_talao=?,"+
												      		   "nr_ultimo_ait=? WHERE cod_talao=?");
			pstmt.setInt(1,objeto.getCodTalao());
			pstmt.setInt(2,objeto.getNrInicial());
			pstmt.setInt(3,objeto.getCodAgente());
			pstmt.setInt(4,objeto.getNrFinal());
			if(objeto.getDtEntrega()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtEntrega().getTimeInMillis()));
			if(objeto.getDtDevolucao()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtDevolucao().getTimeInMillis()));
			pstmt.setInt(7,objeto.getStTalao());
			pstmt.setInt(8,objeto.getNrTalao());
			pstmt.setInt(9,objeto.getTpTalao());
			pstmt.setString(10,objeto.getSgTalao());
			pstmt.setInt(11,objeto.getNrUltimoAit());
			pstmt.setInt(12, codTalaoOld!=0 ? codTalaoOld : objeto.getCodTalao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int codTalao) {
		return delete(codTalao, null);
	}

	public static int delete(int codTalao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM talonario WHERE cod_talao=?");
			pstmt.setInt(1, codTalao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TalonarioOld get(int codTalao) {
		return get(codTalao, null);
	}

	public static TalonarioOld get(int codTalao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM talonario WHERE cod_talao=?");
			pstmt.setInt(1, codTalao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TalonarioOld(rs.getInt("cod_talao"),
						rs.getInt("nr_inicial"),
						rs.getInt("cod_agente"),
						rs.getInt("nr_final"),
						(rs.getTimestamp("dt_entrega")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_entrega").getTime()),
						(rs.getTimestamp("dt_devolucao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_devolucao").getTime()),
						rs.getInt("st_talao"),
						rs.getInt("nr_talao"),
						rs.getInt("tp_talao"),
						rs.getString("sg_talao"),
					    (rs.getObject("nr_ultimo_ait") != null && !((String)rs.getObject("nr_ultimo_ait")).trim().isEmpty()) 
				        ? Integer.parseInt((String)rs.getObject("nr_ultimo_ait"))
				        : 0);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM talonario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TalonarioOld> getList() {
		return getList(null);
	}

	public static ArrayList<TalonarioOld> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TalonarioOld> list = new ArrayList<>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TalonarioOld obj = TalonarioOldDAO.get(rsm.getInt("cod_talao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DAO.getList: " + e);
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
		return Search.find("SELECT * FROM talonario", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
