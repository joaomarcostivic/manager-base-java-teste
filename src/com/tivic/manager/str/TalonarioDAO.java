package com.tivic.manager.str;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class TalonarioDAO{

	public static int insert(Talonario objeto) {
		return insert(objeto, null);
	}

	public static int insert(Talonario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("TALONARIO", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTalao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO TALONARIO (COD_TALAO,"+
			                                  "COD_AGENTE,"+
			                                  "NR_INICIAL,"+
			                                  "NR_FINAL,"+
			                                  "DT_ENTREGA,"+
			                                  "DT_DEVOLUCAO,"+
			                                  "ST_TALAO,"+
			                                  "NR_TALAO,"+
			                                  "TP_TALAO,"+ 
			                                  "SG_TALAO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdAgente()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAgente());
			pstmt.setInt(3,objeto.getNrInicial());
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
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TalonarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TalonarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Talonario objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Talonario objeto, int cdTalaoOld) {
		return update(objeto, cdTalaoOld, null);
	}

	public static int update(Talonario objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Talonario objeto, int cdTalaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE TALONARIO SET COD_TALAO=?,"+
												      		   "COD_AGENTE=?,"+
												      		   "NR_INICIAL=?,"+
												      		   "NR_FINAL=?,"+
												      		   "DT_ENTREGA=?,"+
												      		   "DT_DEVOLUCAO=?,"+
												      		   "ST_TALAO=?,"+
												      		   "NR_TALAO=?,"+
												      		   "TP_TALAO=? WHERE COD_TALAO=?");
			pstmt.setInt(1,objeto.getCdTalao());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAgente());
			pstmt.setInt(3,objeto.getNrInicial());
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
			pstmt.setInt(10, cdTalaoOld!=0 ? cdTalaoOld : objeto.getCdTalao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TalonarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TalonarioDAO.update: " +  e);
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
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM TALONARIO WHERE COD_TALAO=?");
			pstmt.setInt(1, cdTalao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TalonarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TalonarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Talonario get(int cdTalao) {
		return get(cdTalao, null);
	}

	public static Talonario get(int cdTalao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM TALONARIO WHERE COD_TALAO=?");
			pstmt.setInt(1, cdTalao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Talonario(rs.getInt("COD_TALAO"),
						rs.getInt("COD_AGENTE"),
						rs.getInt("NR_INICIAL"),
						rs.getInt("NR_FINAL"),
						(rs.getTimestamp("DT_ENTREGA")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_ENTREGA").getTime()),
						(rs.getTimestamp("DT_DEVOLUCAO")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_DEVOLUCAO").getTime()),
						rs.getInt("ST_TALAO"),
						rs.getInt("NR_TALAO"),
						rs.getInt("TP_TALAO"),
						rs.getString("SG_TALAO"), 0);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TalonarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TalonarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM TALONARIO");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TalonarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TalonarioDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Talonario> getList() {
		return getList(null);
	}

	public static ArrayList<Talonario> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Talonario> list = new ArrayList<Talonario>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Talonario obj = TalonarioDAO.get(rsm.getInt("COD_TALAO"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TalonarioDAO.getList: " + e);
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
		return Search.find("SELECT * FROM TALONARIO", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
