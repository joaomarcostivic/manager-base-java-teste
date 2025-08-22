package com.tivic.manager.prc;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.Util;

public class ProcessoInstanciaDAO{

	public static int insert(ProcessoInstancia objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProcessoInstancia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("INSERT INTO prc_processo_instancia (cd_processo,"+
			                                  "cd_tipo_processo,"+
			                                  "cd_orgao_judicial,"+
			                                  "cd_comarca,"+
			                                  "tp_instancia,"+
			                                  "dt_sentenca,"+
			                                  "st_processo,"+
			                                  "txt_sentenca,"+
			                                  "nr_processo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdProcesso());
			if(objeto.getCdTipoProcesso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoProcesso());
			if(objeto.getCdOrgaoJudicial()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdOrgaoJudicial());
			if(objeto.getCdComarca()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdComarca());
			pstmt.setInt(5,objeto.getTpInstancia());
			if(objeto.getDtSentenca()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtSentenca().getTimeInMillis()));
			pstmt.setInt(7,objeto.getStProcesso());
			pstmt.setString(8,objeto.getTxtSentenca());
			pstmt.setString(9,objeto.getNrProcesso());
			pstmt.executeUpdate();
			return objeto.getTpInstancia();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoInstanciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProcessoInstancia objeto) {
		return update(objeto, null);
	}

	public static int update(ProcessoInstancia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE prc_processo_instancia SET cd_tipo_processo=?,"+
			                                  "cd_orgao_judicial=?,"+
			                                  "cd_comarca=?,"+
			                                  "tp_instancia=?,"+
			                                  "dt_sentenca=?,"+
			                                  "st_processo=?,"+
			                                  "txt_sentenca=?,"+
			                                  "nr_processo=? WHERE cd_processo=?");
			if(objeto.getCdTipoProcesso()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTipoProcesso());
			if(objeto.getCdOrgaoJudicial()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdOrgaoJudicial());
			if(objeto.getCdComarca()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdComarca());
			pstmt.setInt(4,objeto.getTpInstancia());
			if(objeto.getDtSentenca()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtSentenca().getTimeInMillis()));
			pstmt.setInt(6,objeto.getStProcesso());
			pstmt.setString(7,objeto.getTxtSentenca());
			pstmt.setString(8,objeto.getNrProcesso());
			pstmt.setInt(9,objeto.getCdProcesso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoInstanciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoInstanciaDAO.update: " +  e);
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
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM prc_processo_instancia WHERE cd_processo=?");
			pstmt.setInt(1, cdProcesso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoInstanciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoInstanciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProcessoInstancia get(int cdProcesso) {
		return get(cdProcesso, null);
	}

	public static ProcessoInstancia get(int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_processo_instancia WHERE cd_processo=?");
			pstmt.setInt(1, cdProcesso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProcessoInstancia(rs.getInt("cd_processo"),
						rs.getInt("cd_tipo_processo"),
						rs.getInt("cd_orgao_judicial"),
						rs.getInt("cd_comarca"),
						rs.getInt("tp_instancia"),
						(rs.getTimestamp("dt_sentenca")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_sentenca").getTime()),
						rs.getInt("st_processo"),
						rs.getString("txt_sentenca"),
						rs.getString("nr_processo"));
			}
			else
				return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoInstanciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_processo_instancia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoInstanciaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM prc_processo_instancia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
