package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContaFinanceiraDAO{

	public static int insert(ContaFinanceira objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContaFinanceira objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_conta_financeira", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdConta(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_conta_financeira (cd_conta,"+
			                                  "cd_responsavel,"+
			                                  "cd_empresa,"+
			                                  "cd_agencia,"+
			                                  "nm_conta,"+
			                                  "tp_conta,"+
			                                  "nr_conta,"+
			                                  "nr_dv,"+
			                                  "tp_operacao,"+
			                                  "vl_limite,"+
			                                  "dt_fechamento,"+
			                                  "id_conta,"+
			                                  "dt_vencimento_limite,"+
			                                  "vl_saldo_inicial,"+
			                                  "dt_saldo_inicial," +
			                                  "cd_turno) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdResponsavel());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdAgencia()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdAgencia());
			pstmt.setString(5,objeto.getNmConta());
			pstmt.setInt(6,objeto.getTpConta());
			pstmt.setString(7,objeto.getNrConta());
			pstmt.setString(8,objeto.getNrDv());
			pstmt.setInt(9,objeto.getTpOperacao());
			pstmt.setFloat(10,objeto.getVlLimite());
			if(objeto.getDtFechamento()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtFechamento().getTimeInMillis()));
			pstmt.setString(12,objeto.getIdConta());
			if(objeto.getDtVencimentoLimite()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtVencimentoLimite().getTimeInMillis()));
			pstmt.setFloat(14,objeto.getVlSaldoInicial());
			if(objeto.getDtSaldoInicial()==null)
				pstmt.setNull(15, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(15,new Timestamp(objeto.getDtSaldoInicial().getTimeInMillis()));
			if(objeto.getCdTurno()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdTurno());
			pstmt.executeUpdate();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContaFinanceira objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ContaFinanceira objeto, int cdContaOld) {
		return update(objeto, cdContaOld, null);
	}

	public static int update(ContaFinanceira objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ContaFinanceira objeto, int cdContaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_conta_financeira SET cd_conta=?,"+
												      		   "cd_responsavel=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_agencia=?,"+
												      		   "nm_conta=?,"+
												      		   "tp_conta=?,"+
												      		   "nr_conta=?,"+
												      		   "nr_dv=?,"+
												      		   "tp_operacao=?,"+
												      		   "vl_limite=?,"+
												      		   "dt_fechamento=?,"+
												      		   "id_conta=?,"+
												      		   "dt_vencimento_limite=?,"+
												      		   "vl_saldo_inicial=?,"+
												      		   "dt_saldo_inicial=?," +
												      		   "cd_turno=? WHERE cd_conta=?");
			pstmt.setInt(1,objeto.getCdConta());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdResponsavel());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdAgencia()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdAgencia());
			pstmt.setString(5,objeto.getNmConta());
			pstmt.setInt(6,objeto.getTpConta());
			pstmt.setString(7,objeto.getNrConta());
			pstmt.setString(8,objeto.getNrDv());
			pstmt.setInt(9,objeto.getTpOperacao());
			pstmt.setFloat(10,objeto.getVlLimite());
			if(objeto.getDtFechamento()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtFechamento().getTimeInMillis()));
			pstmt.setString(12,objeto.getIdConta());
			if(objeto.getDtVencimentoLimite()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtVencimentoLimite().getTimeInMillis()));
			pstmt.setFloat(14,objeto.getVlSaldoInicial());
			if(objeto.getDtSaldoInicial()==null)
				pstmt.setNull(15, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(15,new Timestamp(objeto.getDtSaldoInicial().getTimeInMillis()));
			if(objeto.getCdTurno()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdTurno());
			pstmt.setInt(17, cdContaOld!=0 ? cdContaOld : objeto.getCdConta());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConta) {
		return delete(cdConta, null);
	}

	public static int delete(int cdConta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_conta_financeira WHERE cd_conta=?");
			pstmt.setInt(1, cdConta);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFinanceiraDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaFinanceira get(int cdConta) {
		return get(cdConta, null);
	}

	public static ContaFinanceira get(int cdConta, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_financeira WHERE cd_conta=?");
			pstmt.setInt(1, cdConta);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaFinanceira(rs.getInt("cd_conta"),
						rs.getInt("cd_responsavel"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_agencia"),
						rs.getString("nm_conta"),
						rs.getInt("tp_conta"),
						rs.getString("nr_conta"),
						rs.getString("nr_dv"),
						rs.getInt("tp_operacao"),
						rs.getFloat("vl_limite"),
						(rs.getTimestamp("dt_fechamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_fechamento").getTime()),
						rs.getString("id_conta"),
						(rs.getTimestamp("dt_vencimento_limite")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vencimento_limite").getTime()),
						rs.getFloat("vl_saldo_inicial"),
						(rs.getTimestamp("dt_saldo_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_saldo_inicial").getTime()),
						rs.getInt("cd_turno"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			com.tivic.manager.util.Util.registerLog(sqlExpt);
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFinanceiraDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_financeira");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFinanceiraDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_conta_financeira", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
