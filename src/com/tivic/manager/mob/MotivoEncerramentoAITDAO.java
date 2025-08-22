package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class MotivoEncerramentoAITDAO{

	public static int insert(MotivoEncerramentoAIT objeto) {
		return insert(objeto, null);
	}

	public static int insert(MotivoEncerramentoAIT objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_motivos_encerramento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMotivoEncerramento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_motivos_encerramento (cd_motivo_encerramento,"+
			                                  "nm_motivo_encerramento) VALUES (?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmMotivoEncerramento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotivoEncerramentoAITDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoEncerramentoAITDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MotivoEncerramentoAIT objeto) {
		return update(objeto, 0, null);
	}

	public static int update(MotivoEncerramentoAIT objeto, int cdMotivoEncerramentoOld) {
		return update(objeto, cdMotivoEncerramentoOld, null);
	}

	public static int update(MotivoEncerramentoAIT objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(MotivoEncerramentoAIT objeto, int cdMotivoEncerramentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_motivos_encerramento SET cd_motivo_encerramento=?,"+
												      		   "nm_motivo_encerramento=? WHERE cd_motivo_encerramento=?");
			pstmt.setInt(1,objeto.getCdMotivoEncerramento());
			pstmt.setString(2,objeto.getNmMotivoEncerramento());
			pstmt.setInt(3, cdMotivoEncerramentoOld!=0 ? cdMotivoEncerramentoOld : objeto.getCdMotivoEncerramento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotivoEncerramentoAITDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoEncerramentoAITDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMotivoEncerramento) {
		return delete(cdMotivoEncerramento, null);
	}

	public static int delete(int cdMotivoEncerramento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_motivos_encerramento WHERE cd_motivo_encerramento=?");
			pstmt.setInt(1, cdMotivoEncerramento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotivoEncerramentoAITDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoEncerramentoAITDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MotivoEncerramentoAIT get(int cdMotivoEncerramento) {
		return get(cdMotivoEncerramento, null);
	}

	public static MotivoEncerramentoAIT get(int cdMotivoEncerramento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_motivos_encerramento WHERE cd_motivo_encerramento=?");
			pstmt.setInt(1, cdMotivoEncerramento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MotivoEncerramentoAIT(rs.getInt("cd_motivo_encerramento"),
						rs.getString("nm_motivo_encerramento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotivoEncerramentoAITDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoEncerramentoAITDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_motivos_encerramento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MotivoEncerramentoAITDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoEncerramentoAITDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<MotivoEncerramentoAIT> getList() {
		return getList(null);
	}

	public static ArrayList<MotivoEncerramentoAIT> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<MotivoEncerramentoAIT> list = new ArrayList<MotivoEncerramentoAIT>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				MotivoEncerramentoAIT obj = MotivoEncerramentoAITDAO.get(rsm.getInt("cd_motivo_encerramento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MotivoEncerramentoAITDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_motivos_encerramento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
