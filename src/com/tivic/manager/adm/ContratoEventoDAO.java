package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.Util;

public class ContratoEventoDAO{

	public static int insert(ContratoEvento objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContratoEvento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_contrato_evento (cd_evento_financeiro,"+
			                                  "cd_contrato,"+
			                                  "tp_repeticao,"+
			                                  "vl_evento_financeiro,"+
			                                  "qt_vezes,"+
			                                  "tp_calculo,"+
			                                  "dt_inicio_lancamento) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEventoFinanceiro()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEventoFinanceiro());
			if(objeto.getCdContrato()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdContrato());
			pstmt.setInt(3,objeto.getTpRepeticao());
			pstmt.setFloat(4,objeto.getVlEventoFinanceiro());
			pstmt.setInt(5,objeto.getQtVezes());
			pstmt.setInt(6,objeto.getTpCalculo());
			if(objeto.getDtInicioLancamento()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInicioLancamento().getTimeInMillis()));
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoEventoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoEventoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContratoEvento objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ContratoEvento objeto, int cdEventoFinanceiroOld, int cdContratoOld) {
		return update(objeto, cdEventoFinanceiroOld, cdContratoOld, null);
	}

	public static int update(ContratoEvento objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ContratoEvento objeto, int cdEventoFinanceiroOld, int cdContratoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_contrato_evento SET cd_evento_financeiro=?,"+
												      		   "cd_contrato=?,"+
												      		   "tp_repeticao=?,"+
												      		   "vl_evento_financeiro=?,"+
												      		   "qt_vezes=?,"+
												      		   "tp_calculo=?,"+
												      		   "dt_inicio_lancamento=? WHERE cd_evento_financeiro=? AND cd_contrato=?");
			pstmt.setInt(1,objeto.getCdEventoFinanceiro());
			pstmt.setInt(2,objeto.getCdContrato());
			pstmt.setInt(3,objeto.getTpRepeticao());
			pstmt.setFloat(4,objeto.getVlEventoFinanceiro());
			pstmt.setInt(5,objeto.getQtVezes());
			pstmt.setInt(6,objeto.getTpCalculo());
			if(objeto.getDtInicioLancamento()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInicioLancamento().getTimeInMillis()));
			pstmt.setInt(8, cdEventoFinanceiroOld!=0 ? cdEventoFinanceiroOld : objeto.getCdEventoFinanceiro());
			pstmt.setInt(9, cdContratoOld!=0 ? cdContratoOld : objeto.getCdContrato());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoEventoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoEventoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEventoFinanceiro, int cdContrato) {
		return delete(cdEventoFinanceiro, cdContrato, null);
	}

	public static int delete(int cdEventoFinanceiro, int cdContrato, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_contrato_evento WHERE cd_evento_financeiro=? AND cd_contrato=?");
			pstmt.setInt(1, cdEventoFinanceiro);
			pstmt.setInt(2, cdContrato);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoEventoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoEventoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContratoEvento get(int cdEventoFinanceiro, int cdContrato) {
		return get(cdEventoFinanceiro, cdContrato, null);
	}

	public static ContratoEvento get(int cdEventoFinanceiro, int cdContrato, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_contrato_evento WHERE cd_evento_financeiro=? AND cd_contrato=?");
			pstmt.setInt(1, cdEventoFinanceiro);
			pstmt.setInt(2, cdContrato);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContratoEvento(rs.getInt("cd_evento_financeiro"),
						rs.getInt("cd_contrato"),
						rs.getInt("tp_repeticao"),
						rs.getFloat("vl_evento_financeiro"),
						rs.getInt("qt_vezes"),
						rs.getInt("tp_calculo"),
						(rs.getTimestamp("dt_inicio_lancamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio_lancamento").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoEventoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoEventoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_contrato_evento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContratoEventoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoEventoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_contrato_evento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
