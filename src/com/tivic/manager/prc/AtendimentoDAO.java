package com.tivic.manager.prc;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.Util;
import sol.dao.ItemComparator;

public class AtendimentoDAO{

	public static int insert(Atendimento objeto) {
		return insert(objeto, null);
	}

	public static int insert(Atendimento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			int code = Conexao.getSequenceCode("prc_atendimento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO prc_atendimento (cd_atendimento,"+
			                                  "cd_pessoa,"+
			                                  "cd_processo,"+
			                                  "dt_atendimento,"+
			                                  "st_atendimento,"+
			                                  "st_retorno,"+
			                                  "txt_atendimento,"+
			                                  "vl_orcamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdProcesso());
			if(objeto.getDtAtendimento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtAtendimento().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStAtendimento());
			pstmt.setInt(6,objeto.getStRetorno());
			pstmt.setString(7,objeto.getTxtAtendimento());
			pstmt.setFloat(8,objeto.getVlOrcamento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Atendimento objeto) {
		return update(objeto, null);
	}

	public static int update(Atendimento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE prc_atendimento SET cd_pessoa=?,"+
			                                  "cd_processo=?,"+
			                                  "dt_atendimento=?,"+
			                                  "st_atendimento=?,"+
			                                  "st_retorno=?,"+
			                                  "txt_atendimento=?,"+
			                                  "vl_orcamento=? WHERE cd_atendimento=?");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProcesso());
			if(objeto.getDtAtendimento()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtAtendimento().getTimeInMillis()));
			pstmt.setInt(4,objeto.getStAtendimento());
			pstmt.setInt(5,objeto.getStRetorno());
			pstmt.setString(6,objeto.getTxtAtendimento());
			pstmt.setFloat(7,objeto.getVlOrcamento());
			pstmt.setInt(8,objeto.getCdAtendimento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAtendimento) {
		return delete(cdAtendimento, null);
	}

	public static int delete(int cdAtendimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM prc_atendimento WHERE cd_atendimento=?");
			pstmt.setInt(1, cdAtendimento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Atendimento get(int cdAtendimento) {
		return get(cdAtendimento, null);
	}

	public static Atendimento get(int cdAtendimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_atendimento WHERE cd_atendimento=?");
			pstmt.setInt(1, cdAtendimento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Atendimento(rs.getInt("cd_atendimento"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_processo"),
						(rs.getTimestamp("dt_atendimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_atendimento").getTime()),
						rs.getInt("st_atendimento"),
						rs.getInt("st_retorno"),
						rs.getString("txt_atendimento"),
						rs.getFloat("vl_orcamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_atendimento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtendimentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM prc_atendimento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
