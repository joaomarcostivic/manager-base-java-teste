package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class ContaFechamentoDAO{

	public static int insert(ContaFechamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContaFechamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_conta_fechamento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFechamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_conta_fechamento (cd_conta,"+
			                                  "cd_fechamento,"+
			                                  "dt_fechamento,"+
			                                  "cd_supervisor,"+
			                                  "cd_responsavel,"+
			                                  "vl_fechamento,"+
			                                  "vl_total_entradas,"+
			                                  "vl_total_saidas,"+
			                                  "txt_observacao,"+
			                                  "vl_saldo_anterior,"+
			                                  "cd_turno,"+
			                                  "st_fechamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCdConta());
			pstmt.setInt(2, objeto.getCdFechamento());
			if(objeto.getDtFechamento()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtFechamento().getTimeInMillis()));
			if(objeto.getCdSupervisor()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdSupervisor());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdResponsavel());
			pstmt.setDouble(6,objeto.getVlFechamento());
			pstmt.setDouble(7,objeto.getVlTotalEntradas());
			pstmt.setDouble(8,objeto.getVlTotalSaidas());
			pstmt.setString(9,objeto.getTxtObservacao());
			pstmt.setDouble(10,objeto.getVlSaldoAnterior());
			if(objeto.getCdTurno()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdTurno());
			pstmt.setInt(12,objeto.getStFechamento());
			
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContaFechamento objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ContaFechamento objeto, int cdContaOld, int cdFechamentoOld) {
		return update(objeto, cdContaOld, cdFechamentoOld, null);
	}

	public static int update(ContaFechamento objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ContaFechamento objeto, int cdContaOld, int cdFechamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_conta_fechamento SET cd_conta=?,"+
												      		   "cd_fechamento=?,"+
												      		   "dt_fechamento=?,"+
												      		   "cd_supervisor=?,"+
												      		   "cd_responsavel=?,"+
												      		   "vl_fechamento=?,"+
												      		   "vl_total_entradas=?,"+
												      		   "vl_total_saidas=?,"+
												      		   "txt_observacao=?,"+
												      		   "vl_saldo_anterior=?,"+
												      		   "cd_turno=?,"+
												      		   "st_fechamento=? WHERE cd_conta=? AND cd_fechamento=?");
			pstmt.setInt(1,objeto.getCdConta());
			pstmt.setInt(2,objeto.getCdFechamento());
			if(objeto.getDtFechamento()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtFechamento().getTimeInMillis()));
			if(objeto.getCdSupervisor()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdSupervisor());
			if(objeto.getCdResponsavel()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdResponsavel());
			pstmt.setDouble(6,objeto.getVlFechamento());
			pstmt.setDouble(7,objeto.getVlTotalEntradas());
			pstmt.setDouble(8,objeto.getVlTotalSaidas());
			pstmt.setString(9,objeto.getTxtObservacao());
			pstmt.setDouble(10,objeto.getVlSaldoAnterior());
			if(objeto.getCdTurno()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdTurno());
			pstmt.setInt(12,objeto.getStFechamento());
			
			pstmt.setInt(13, cdContaOld!=0 ? cdContaOld : objeto.getCdConta());
			pstmt.setInt(14, cdFechamentoOld!=0 ? cdFechamentoOld : objeto.getCdFechamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConta, int cdFechamento) {
		return delete(cdConta, cdFechamento, null);
	}

	public static int delete(int cdConta, int cdFechamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_conta_fechamento WHERE cd_conta=? AND cd_fechamento=?");
			pstmt.setInt(1, cdConta);
			pstmt.setInt(2, cdFechamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaFechamento get(int cdConta, int cdFechamento) {
		return get(cdConta, cdFechamento, null);
	}

	public static ContaFechamento get(int cdConta, int cdFechamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_fechamento WHERE cd_conta=? AND cd_fechamento=?");
			pstmt.setInt(1, cdConta);
			pstmt.setInt(2, cdFechamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaFechamento(rs.getInt("cd_conta"),
						rs.getInt("cd_fechamento"),
						(rs.getTimestamp("dt_fechamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_fechamento").getTime()),
						rs.getInt("cd_supervisor"),
						rs.getInt("cd_responsavel"),
						rs.getDouble("vl_fechamento"),
						rs.getDouble("vl_total_entradas"),
						rs.getDouble("vl_total_saidas"),
						rs.getString("txt_observacao"),
						rs.getDouble("vl_saldo_anterior"),
						rs.getInt("cd_turno"),
						rs.getInt("st_fechamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_fechamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ContaFechamento> getList() {
		return getList(null);
	}

	public static ArrayList<ContaFechamento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ContaFechamento> list = new ArrayList<ContaFechamento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ContaFechamento obj = ContaFechamentoDAO.get(rsm.getInt("cd_conta"), rsm.getInt("cd_fechamento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM adm_conta_fechamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
