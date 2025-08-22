package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class TalonarioAITDAO{

	public static int insert(TalonarioAIT objeto) {
		return insert(objeto, null);
	}

	public static int insert(TalonarioAIT objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_talonario", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTalao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_talonario (cd_talao,"+
			                                  "nr_talao,"+
			                                  "nr_inicial,"+
			                                  "nr_final,"+
			                                  "cd_agente,"+
			                                  "dt_entrega,"+
			                                  "dt_devolucao,"+
			                                  "st_talao,"+
			                                  "tp_talao,"+
			                                  "sg_talao,"+
			                                  "st_login) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getNrTalao());
			pstmt.setInt(3,objeto.getNrInicial());
			pstmt.setInt(4,objeto.getNrFinal());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdAgente());
			if(objeto.getDtEntrega()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtEntrega().getTimeInMillis()));
			if(objeto.getDtDevolucao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtDevolucao().getTimeInMillis()));
			pstmt.setInt(8,objeto.getStTalao());
			pstmt.setInt(9,objeto.getTpTalao());
			pstmt.setString(10,objeto.getSgTalao());
			pstmt.setInt(11,objeto.getStLogin());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TalonarioAITDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TalonarioAITDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TalonarioAIT objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TalonarioAIT objeto, int cdTalaoOld) {
		return update(objeto, cdTalaoOld, null);
	}

	public static int update(TalonarioAIT objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TalonarioAIT objeto, int cdTalaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_talonario SET cd_talao=?,"+
												      		   "nr_talao=?,"+
												      		   "nr_inicial=?,"+
												      		   "nr_final=?,"+
												      		   "cd_agente=?,"+
												      		   "dt_entrega=?,"+
												      		   "dt_devolucao=?,"+
												      		   "st_talao=?,"+
												      		   "tp_talao=?,"+
												      		   "sg_talao=?,"+
												      		   "st_login=? WHERE cd_talao=?");
			pstmt.setInt(1,objeto.getCdTalao());
			pstmt.setInt(2,objeto.getNrTalao());
			pstmt.setInt(3,objeto.getNrInicial());
			pstmt.setInt(4,objeto.getNrFinal());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdAgente());
			if(objeto.getDtEntrega()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtEntrega().getTimeInMillis()));
			if(objeto.getDtDevolucao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtDevolucao().getTimeInMillis()));
			pstmt.setInt(8,objeto.getStTalao());
			pstmt.setInt(9,objeto.getTpTalao());
			pstmt.setString(10,objeto.getSgTalao());
			pstmt.setInt(11,objeto.getStLogin());
			pstmt.setInt(12, cdTalaoOld!=0 ? cdTalaoOld : objeto.getCdTalao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TalonarioAITDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TalonarioAITDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTalao) {
		return delete(cdTalao, null);
	}

	public static int delete(int cdTalao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_talonario WHERE cd_talao=?");
			pstmt.setInt(1, cdTalao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TalonarioAITDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TalonarioAITDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TalonarioAIT get(int cdTalao) {
		return get(cdTalao, null);
	}

	public static TalonarioAIT get(int cdTalao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_talonario WHERE cd_talao=?");
			pstmt.setInt(1, cdTalao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TalonarioAIT(rs.getInt("cd_talao"),
						rs.getInt("nr_talao"),
						rs.getInt("nr_inicial"),
						rs.getInt("nr_final"),
						rs.getInt("cd_agente"),
						(rs.getTimestamp("dt_entrega")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_entrega").getTime()),
						(rs.getTimestamp("dt_devolucao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_devolucao").getTime()),
						rs.getInt("st_talao"),
						rs.getInt("tp_talao"),
						rs.getString("sg_talao"),
						rs.getInt("st_login"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TalonarioAITDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TalonarioAITDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_talonario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TalonarioAITDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TalonarioAITDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TalonarioAIT> getList() {
		return getList(null);
	}

	public static ArrayList<TalonarioAIT> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TalonarioAIT> list = new ArrayList<TalonarioAIT>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TalonarioAIT obj = TalonarioAITDAO.get(rsm.getInt("cd_talao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TalonarioAITDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_talonario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
