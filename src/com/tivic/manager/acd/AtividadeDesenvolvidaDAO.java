package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class AtividadeDesenvolvidaDAO{

	public static int insert(AtividadeDesenvolvida objeto) {
		return insert(objeto, null);
	}

	public static int insert(AtividadeDesenvolvida objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_atividade_desenvolvida", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAtividadeDesenvolvida(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_atividade_desenvolvida (cd_atividade_desenvolvida,"+
			                                  "dt_atividade_desenvolvida,"+
			                                  "txt_atividade_desenvolvida,"+
			                                  "qt_aulas,"+
			                                  "cd_turma,"+
			                                  "cd_oferta,"+
			                                  "cd_professor) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getDtAtividadeDesenvolvida()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtAtividadeDesenvolvida().getTimeInMillis()));
			pstmt.setString(3,objeto.getTxtAtividadeDesenvolvida());
			pstmt.setInt(4,objeto.getQtAulas());
			if(objeto.getCdTurma()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTurma());
			if(objeto.getCdOferta()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdOferta());
			if(objeto.getCdProfessor()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdProfessor());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtividadeDesenvolvidaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtividadeDesenvolvidaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AtividadeDesenvolvida objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AtividadeDesenvolvida objeto, int cdAtividadeDesenvolvidaOld) {
		return update(objeto, cdAtividadeDesenvolvidaOld, null);
	}

	public static int update(AtividadeDesenvolvida objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AtividadeDesenvolvida objeto, int cdAtividadeDesenvolvidaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_atividade_desenvolvida SET cd_atividade_desenvolvida=?,"+
												      		   "dt_atividade_desenvolvida=?,"+
												      		   "txt_atividade_desenvolvida=?,"+
												      		   "qt_aulas=?,"+
												      		   "cd_turma=?,"+
												      		   "cd_oferta=?,"+
												      		   "cd_professor=? WHERE cd_atividade_desenvolvida=?");
			pstmt.setInt(1,objeto.getCdAtividadeDesenvolvida());
			if(objeto.getDtAtividadeDesenvolvida()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtAtividadeDesenvolvida().getTimeInMillis()));
			pstmt.setString(3,objeto.getTxtAtividadeDesenvolvida());
			pstmt.setInt(4,objeto.getQtAulas());
			if(objeto.getCdTurma()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTurma());
			if(objeto.getCdOferta()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdOferta());
			if(objeto.getCdProfessor()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdProfessor());
			pstmt.setInt(8, cdAtividadeDesenvolvidaOld!=0 ? cdAtividadeDesenvolvidaOld : objeto.getCdAtividadeDesenvolvida());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtividadeDesenvolvidaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtividadeDesenvolvidaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAtividadeDesenvolvida) {
		return delete(cdAtividadeDesenvolvida, null);
	}

	public static int delete(int cdAtividadeDesenvolvida, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_atividade_desenvolvida WHERE cd_atividade_desenvolvida=?");
			pstmt.setInt(1, cdAtividadeDesenvolvida);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtividadeDesenvolvidaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtividadeDesenvolvidaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AtividadeDesenvolvida get(int cdAtividadeDesenvolvida) {
		return get(cdAtividadeDesenvolvida, null);
	}

	public static AtividadeDesenvolvida get(int cdAtividadeDesenvolvida, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_atividade_desenvolvida WHERE cd_atividade_desenvolvida=?");
			pstmt.setInt(1, cdAtividadeDesenvolvida);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AtividadeDesenvolvida(rs.getInt("cd_atividade_desenvolvida"),
						(rs.getTimestamp("dt_atividade_desenvolvida")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_atividade_desenvolvida").getTime()),
						rs.getString("txt_atividade_desenvolvida"),
						rs.getInt("qt_aulas"),
						rs.getInt("cd_turma"),
						rs.getInt("cd_oferta"),
						rs.getInt("cd_professor"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtividadeDesenvolvidaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtividadeDesenvolvidaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_atividade_desenvolvida");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtividadeDesenvolvidaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtividadeDesenvolvidaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AtividadeDesenvolvida> getList() {
		return getList(null);
	}

	public static ArrayList<AtividadeDesenvolvida> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AtividadeDesenvolvida> list = new ArrayList<AtividadeDesenvolvida>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AtividadeDesenvolvida obj = AtividadeDesenvolvidaDAO.get(rsm.getInt("cd_atividade_desenvolvida"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtividadeDesenvolvidaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_atividade_desenvolvida", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}