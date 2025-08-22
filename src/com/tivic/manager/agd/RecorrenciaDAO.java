package com.tivic.manager.agd;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class RecorrenciaDAO{

	public static int insert(Recorrencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(Recorrencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("agd_recorrencia", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRecorrencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agd_recorrencia (cd_recorrencia,"+
			                                  "dt_inicio,"+
			                                  "dt_termino,"+
			                                  "tp_termino,"+
			                                  "nr_recorrencias,"+
			                                  "tp_recorrencia,"+
			                                  "tp_especificidade_recorrencia,"+
			                                  "lg_domingo,"+
			                                  "lg_segunda,"+
			                                  "lg_terca,"+
			                                  "lg_quarta,"+
			                                  "lg_quinta,"+
			                                  "lg_sexta,"+
			                                  "lg_sabado,"+
			                                  "qt_intervalo_recorrencia,"+
			                                  "nr_dia_recorrencia_mensal,"+
			                                  "nr_ordem_recorrencia_mensal,"+
			                                  "tp_ordem_recorrencia_mensal,"+
			                                  "nr_dia_recorrencia_anual,"+
			                                  "nr_mes_recorrencia_anual,"+
			                                  "tp_ordem_recorrencia_anual,"+
			                                  "nr_ordem_recorrencia_anual,"+
			                                  "cd_agenda) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getDtInicio()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			if(objeto.getDtTermino()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtTermino().getTimeInMillis()));
			pstmt.setInt(4,objeto.getTpTermino());
			pstmt.setInt(5,objeto.getNrRecorrencias());
			pstmt.setInt(6,objeto.getTpRecorrencia());
			pstmt.setInt(7,objeto.getTpEspecificidadeRecorrencia());
			pstmt.setInt(8,objeto.getLgDomingo());
			pstmt.setInt(9,objeto.getLgSegunda());
			pstmt.setInt(10,objeto.getLgTerca());
			pstmt.setInt(11,objeto.getLgQuarta());
			pstmt.setInt(12,objeto.getLgQuinta());
			pstmt.setInt(13,objeto.getLgSexta());
			pstmt.setInt(14,objeto.getLgSabado());
			pstmt.setInt(15,objeto.getQtIntervaloRecorrencia());
			pstmt.setInt(16,objeto.getNrDiaRecorrenciaMensal());
			pstmt.setInt(17,objeto.getNrOrdemRecorrenciaMensal());
			pstmt.setInt(18,objeto.getTpOrdemRecorrenciaMensal());
			pstmt.setInt(19,objeto.getNrDiaRecorrenciaAnual());
			pstmt.setInt(20,objeto.getNrMesRecorrenciaAnual());
			pstmt.setInt(21,objeto.getTpOrdemRecorrenciaAnual());
			pstmt.setInt(22,objeto.getNrOrdemRecorrenciaAnual());
			if(objeto.getCdAgenda()==0)
				pstmt.setNull(23, Types.INTEGER);
			else
				pstmt.setInt(23,objeto.getCdAgenda());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecorrenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RecorrenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Recorrencia objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Recorrencia objeto, int cdRecorrenciaOld) {
		return update(objeto, cdRecorrenciaOld, null);
	}

	public static int update(Recorrencia objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Recorrencia objeto, int cdRecorrenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE agd_recorrencia SET cd_recorrencia=?,"+
												      		   "dt_inicio=?,"+
												      		   "dt_termino=?,"+
												      		   "tp_termino=?,"+
												      		   "nr_recorrencias=?,"+
												      		   "tp_recorrencia=?,"+
												      		   "tp_especificidade_recorrencia=?,"+
												      		   "lg_domingo=?,"+
												      		   "lg_segunda=?,"+
												      		   "lg_terca=?,"+
												      		   "lg_quarta=?,"+
												      		   "lg_quinta=?,"+
												      		   "lg_sexta=?,"+
												      		   "lg_sabado=?,"+
												      		   "qt_intervalo_recorrencia=?,"+
												      		   "nr_dia_recorrencia_mensal=?,"+
												      		   "nr_ordem_recorrencia_mensal=?,"+
												      		   "tp_ordem_recorrencia_mensal=?,"+
												      		   "nr_dia_recorrencia_anual=?,"+
												      		   "nr_mes_recorrencia_anual=?,"+
												      		   "tp_ordem_recorrencia_anual=?,"+
												      		   "nr_ordem_recorrencia_anual=?,"+
												      		   "cd_agenda=? WHERE cd_recorrencia=?");
			pstmt.setInt(1,objeto.getCdRecorrencia());
			if(objeto.getDtInicio()==null)
				pstmt.setNull(2, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(2,new Timestamp(objeto.getDtInicio().getTimeInMillis()));
			if(objeto.getDtTermino()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtTermino().getTimeInMillis()));
			pstmt.setInt(4,objeto.getTpTermino());
			pstmt.setInt(5,objeto.getNrRecorrencias());
			pstmt.setInt(6,objeto.getTpRecorrencia());
			pstmt.setInt(7,objeto.getTpEspecificidadeRecorrencia());
			pstmt.setInt(8,objeto.getLgDomingo());
			pstmt.setInt(9,objeto.getLgSegunda());
			pstmt.setInt(10,objeto.getLgTerca());
			pstmt.setInt(11,objeto.getLgQuarta());
			pstmt.setInt(12,objeto.getLgQuinta());
			pstmt.setInt(13,objeto.getLgSexta());
			pstmt.setInt(14,objeto.getLgSabado());
			pstmt.setInt(15,objeto.getQtIntervaloRecorrencia());
			pstmt.setInt(16,objeto.getNrDiaRecorrenciaMensal());
			pstmt.setInt(17,objeto.getNrOrdemRecorrenciaMensal());
			pstmt.setInt(18,objeto.getTpOrdemRecorrenciaMensal());
			pstmt.setInt(19,objeto.getNrDiaRecorrenciaAnual());
			pstmt.setInt(20,objeto.getNrMesRecorrenciaAnual());
			pstmt.setInt(21,objeto.getTpOrdemRecorrenciaAnual());
			pstmt.setInt(22,objeto.getNrOrdemRecorrenciaAnual());
			if(objeto.getCdAgenda()==0)
				pstmt.setNull(23, Types.INTEGER);
			else
				pstmt.setInt(23,objeto.getCdAgenda());
			pstmt.setInt(24, cdRecorrenciaOld!=0 ? cdRecorrenciaOld : objeto.getCdRecorrencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecorrenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RecorrenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdRecorrencia) {
		return delete(cdRecorrencia, null);
	}

	public static int delete(int cdRecorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM agd_recorrencia WHERE cd_recorrencia=?");
			pstmt.setInt(1, cdRecorrencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecorrenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! RecorrenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Recorrencia get(int cdRecorrencia) {
		return get(cdRecorrencia, null);
	}

	public static Recorrencia get(int cdRecorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agd_recorrencia WHERE cd_recorrencia=?");
			pstmt.setInt(1, cdRecorrencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Recorrencia(rs.getInt("cd_recorrencia"),
						(rs.getTimestamp("dt_inicio")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio").getTime()),
						(rs.getTimestamp("dt_termino")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_termino").getTime()),
						rs.getInt("tp_termino"),
						rs.getInt("nr_recorrencias"),
						rs.getInt("tp_recorrencia"),
						rs.getInt("tp_especificidade_recorrencia"),
						rs.getInt("lg_domingo"),
						rs.getInt("lg_segunda"),
						rs.getInt("lg_terca"),
						rs.getInt("lg_quarta"),
						rs.getInt("lg_quinta"),
						rs.getInt("lg_sexta"),
						rs.getInt("lg_sabado"),
						rs.getInt("qt_intervalo_recorrencia"),
						rs.getInt("nr_dia_recorrencia_mensal"),
						rs.getInt("nr_ordem_recorrencia_mensal"),
						rs.getInt("tp_ordem_recorrencia_mensal"),
						rs.getInt("nr_dia_recorrencia_anual"),
						rs.getInt("nr_mes_recorrencia_anual"),
						rs.getInt("tp_ordem_recorrencia_anual"),
						rs.getInt("nr_ordem_recorrencia_anual"),
						rs.getInt("cd_agenda"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecorrenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecorrenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_recorrencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! RecorrenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! RecorrenciaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM agd_recorrencia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
