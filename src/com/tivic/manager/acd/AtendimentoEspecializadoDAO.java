package com.tivic.manager.acd;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class AtendimentoEspecializadoDAO{

	public static int insert(AtendimentoEspecializado objeto) {
		return insert(objeto, null);
	}

	public static int insert(AtendimentoEspecializado objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_atendimento_especializado", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAtendimentoEspecializado(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_atendimento_especializado (cd_atendimento_especializado,"+
			                                  "nm_atendimento_especializado,"+
			                                  "id_atendimento_especializado) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmAtendimentoEspecializado());
			pstmt.setString(3,objeto.getIdAtendimentoEspecializado());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoEspecializadoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoEspecializadoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AtendimentoEspecializado objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AtendimentoEspecializado objeto, int cdAtendimentoEspecializadoOld) {
		return update(objeto, cdAtendimentoEspecializadoOld, null);
	}

	public static int update(AtendimentoEspecializado objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AtendimentoEspecializado objeto, int cdAtendimentoEspecializadoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_atendimento_especializado SET cd_atendimento_especializado=?,"+
												      		   "nm_atendimento_especializado=?,"+
												      		   "id_atendimento_especializado=? WHERE cd_atendimento_especializado=?");
			pstmt.setInt(1,objeto.getCdAtendimentoEspecializado());
			pstmt.setString(2,objeto.getNmAtendimentoEspecializado());
			pstmt.setString(3,objeto.getIdAtendimentoEspecializado());
			pstmt.setInt(4, cdAtendimentoEspecializadoOld!=0 ? cdAtendimentoEspecializadoOld : objeto.getCdAtendimentoEspecializado());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoEspecializadoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoEspecializadoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAtendimentoEspecializado) {
		return delete(cdAtendimentoEspecializado, null);
	}

	public static int delete(int cdAtendimentoEspecializado, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_atendimento_especializado WHERE cd_atendimento_especializado=?");
			pstmt.setInt(1, cdAtendimentoEspecializado);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoEspecializadoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoEspecializadoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AtendimentoEspecializado get(int cdAtendimentoEspecializado) {
		return get(cdAtendimentoEspecializado, null);
	}

	public static AtendimentoEspecializado get(int cdAtendimentoEspecializado, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_atendimento_especializado WHERE cd_atendimento_especializado=?");
			pstmt.setInt(1, cdAtendimentoEspecializado);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AtendimentoEspecializado(rs.getInt("cd_atendimento_especializado"),
						rs.getString("nm_atendimento_especializado"),
						rs.getString("id_atendimento_especializado"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoEspecializadoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoEspecializadoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_atendimento_especializado");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoEspecializadoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoEspecializadoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_atendimento_especializado", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
