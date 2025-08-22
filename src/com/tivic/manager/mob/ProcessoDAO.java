package com.tivic.manager.mob;

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

public class ProcessoDAO{

	public static int insert(Processo objeto) {
		return insert(objeto, null);
	}

	public static int insert(Processo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_processo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdProcesso(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_processo (cd_processo,"+
			                                  "cd_ait,"+
			                                  "nr_processo,"+
			                                  "dt_processo,"+
			                                  "ds_parecer,"+
			                                  "st_processo,"+
			                                  "tp_processo,"+
			                                  "st_impressao,"+
			                                  "tp_origem,"+
			                                  "cd_estado_origem,"+
			                                  "nr_orgao_origem,"+
			                                  "cd_usuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdAit()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAit());
			pstmt.setString(3,objeto.getNrProcesso());
			if(objeto.getDtProcesso()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtProcesso().getTimeInMillis()));
			pstmt.setString(5,objeto.getDsParecer());
			pstmt.setInt(6,objeto.getStProcesso());
			pstmt.setInt(7,objeto.getTpProcesso());
			pstmt.setInt(8,objeto.getStImpressao());
			pstmt.setInt(9,objeto.getTpOrigem());
			if(objeto.getCdEstadoOrigem()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdEstadoOrigem());
			pstmt.setString(11,objeto.getNrOrgaoOrigem());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdUsuario());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Processo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Processo objeto, int cdProcessoOld) {
		return update(objeto, cdProcessoOld, null);
	}

	public static int update(Processo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Processo objeto, int cdProcessoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_processo SET cd_processo=?,"+
												      		   "cd_ait=?,"+
												      		   "nr_processo=?,"+
												      		   "dt_processo=?,"+
												      		   "ds_parecer=?,"+
												      		   "st_processo=?,"+
												      		   "tp_processo=?,"+
												      		   "st_impressao=?,"+
												      		   "tp_origem=?,"+
												      		   "cd_estado_origem=?,"+
												      		   "nr_orgao_origem=?,"+
												      		   "cd_usuario=? WHERE cd_processo=?");
			pstmt.setInt(1,objeto.getCdProcesso());
			if(objeto.getCdAit()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAit());
			pstmt.setString(3,objeto.getNrProcesso());
			if(objeto.getDtProcesso()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtProcesso().getTimeInMillis()));
			pstmt.setString(5,objeto.getDsParecer());
			pstmt.setInt(6,objeto.getStProcesso());
			pstmt.setInt(7,objeto.getTpProcesso());
			pstmt.setInt(8,objeto.getStImpressao());
			pstmt.setInt(9,objeto.getTpOrigem());
			if(objeto.getCdEstadoOrigem()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdEstadoOrigem());
			pstmt.setString(11,objeto.getNrOrgaoOrigem());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdUsuario());
			pstmt.setInt(13, cdProcessoOld!=0 ? cdProcessoOld : objeto.getCdProcesso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProcesso) {
		return delete(cdProcesso, null);
	}

	public static int delete(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_processo WHERE cd_processo=?");
			pstmt.setInt(1, cdProcesso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Processo get(int cdProcesso) {
		return get(cdProcesso, null);
	}

	public static Processo get(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_processo WHERE cd_processo=?");
			pstmt.setInt(1, cdProcesso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Processo(rs.getInt("cd_processo"),
						rs.getInt("cd_ait"),
						rs.getString("nr_processo"),
						(rs.getTimestamp("dt_processo")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_processo").getTime()),
						rs.getString("ds_parecer"),
						rs.getInt("st_processo"),
						rs.getInt("tp_processo"),
						rs.getInt("st_impressao"),
						rs.getInt("tp_origem"),
						rs.getInt("cd_estado_origem"),
						rs.getString("nr_orgao_origem"),
						rs.getInt("cd_usuario"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_processo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Processo> getList() {
		return getList(null);
	}

	public static ArrayList<Processo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Processo> list = new ArrayList<Processo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Processo obj = ProcessoDAO.get(rsm.getInt("cd_processo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_processo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
