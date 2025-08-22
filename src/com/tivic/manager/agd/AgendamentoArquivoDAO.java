package com.tivic.manager.agd;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class AgendamentoArquivoDAO{

	public static int insert(AgendamentoArquivo objeto) {
		return insert(objeto, null);
	}

	public static int insert(AgendamentoArquivo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agd_agendamento_arquivo (cd_agendamento,"+
			                                  "cd_arquivo,"+
			                                  "cd_usuario) VALUES (?, ?, ?)");
			if(objeto.getCdAgendamento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAgendamento());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdArquivo());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdUsuario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoArquivoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoArquivoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AgendamentoArquivo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AgendamentoArquivo objeto, int cdAgendamentoOld, int cdArquivoOld) {
		return update(objeto, cdAgendamentoOld, cdArquivoOld, null);
	}

	public static int update(AgendamentoArquivo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AgendamentoArquivo objeto, int cdAgendamentoOld, int cdArquivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE agd_agendamento_arquivo SET cd_agendamento=?,"+
												      		   "cd_arquivo=?,"+
												      		   "cd_usuario=? WHERE cd_agendamento=? AND cd_arquivo=?");
			pstmt.setInt(1,objeto.getCdAgendamento());
			pstmt.setInt(2,objeto.getCdArquivo());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdUsuario());
			pstmt.setInt(4, cdAgendamentoOld!=0 ? cdAgendamentoOld : objeto.getCdAgendamento());
			pstmt.setInt(5, cdArquivoOld!=0 ? cdArquivoOld : objeto.getCdArquivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoArquivoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoArquivoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAgendamento, int cdArquivo) {
		return delete(cdAgendamento, cdArquivo, null);
	}

	public static int delete(int cdAgendamento, int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM agd_agendamento_arquivo WHERE cd_agendamento=? AND cd_arquivo=?");
			pstmt.setInt(1, cdAgendamento);
			pstmt.setInt(2, cdArquivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoArquivoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoArquivoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AgendamentoArquivo get(int cdAgendamento, int cdArquivo) {
		return get(cdAgendamento, cdArquivo, null);
	}

	public static AgendamentoArquivo get(int cdAgendamento, int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agd_agendamento_arquivo WHERE cd_agendamento=? AND cd_arquivo=?");
			pstmt.setInt(1, cdAgendamento);
			pstmt.setInt(2, cdArquivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AgendamentoArquivo(rs.getInt("cd_agendamento"),
						rs.getInt("cd_arquivo"),
						rs.getInt("cd_usuario"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoArquivoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoArquivoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_agendamento_arquivo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoArquivoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoArquivoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM agd_agendamento_arquivo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
