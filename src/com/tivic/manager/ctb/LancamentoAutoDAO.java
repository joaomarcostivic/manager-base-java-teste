package com.tivic.manager.ctb;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class LancamentoAutoDAO{

	public static int insert(LancamentoAuto objeto) {
		return insert(objeto, null);
	}

	public static int insert(LancamentoAuto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ctb_lancamento_auto", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLancamentoAuto(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ctb_lancamento_auto (cd_lancamento_auto,"+
			                                  "cd_conta_debito,"+
			                                  "cd_conta_credito,"+
			                                  "cd_historico,"+
			                                  "cd_empresa,"+
			                                  "nm_lancamento_auto,"+
			                                  "txt_historico,"+
			                                  "st_lancamento_auto,"+
			                                  "id_lancamento_auto,"+
			                                  "cd_centro_custo_debito,"+
			                                  "cd_centro_custo_credito) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdContaDebito()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdContaDebito());
			if(objeto.getCdContaCredito()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdContaCredito());
			if(objeto.getCdHistorico()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdHistorico());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEmpresa());
			pstmt.setString(6,objeto.getNmLancamentoAuto());
			pstmt.setString(7,objeto.getTxtHistorico());
			pstmt.setInt(8,objeto.getStLancamentoAuto());
			pstmt.setString(9,objeto.getIdLancamentoAuto());
			if(objeto.getCdCentroCustoDebito()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdCentroCustoDebito());
			if(objeto.getCdCentroCustoCredito()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdCentroCustoCredito());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LancamentoAutoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoAutoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LancamentoAuto objeto) {
		return update(objeto, 0, null);
	}

	public static int update(LancamentoAuto objeto, int cdLancamentoAutoOld) {
		return update(objeto, cdLancamentoAutoOld, null);
	}

	public static int update(LancamentoAuto objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(LancamentoAuto objeto, int cdLancamentoAutoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ctb_lancamento_auto SET cd_lancamento_auto=?,"+
												      		   "cd_conta_debito=?,"+
												      		   "cd_conta_credito=?,"+
												      		   "cd_historico=?,"+
												      		   "cd_empresa=?,"+
												      		   "nm_lancamento_auto=?,"+
												      		   "txt_historico=?,"+
												      		   "st_lancamento_auto=?,"+
												      		   "id_lancamento_auto=?,"+
												      		   "cd_centro_custo_debito=?,"+
												      		   "cd_centro_custo_credito=? WHERE cd_lancamento_auto=?");
			pstmt.setInt(1,objeto.getCdLancamentoAuto());
			if(objeto.getCdContaDebito()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdContaDebito());
			if(objeto.getCdContaCredito()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdContaCredito());
			if(objeto.getCdHistorico()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdHistorico());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdEmpresa());
			pstmt.setString(6,objeto.getNmLancamentoAuto());
			pstmt.setString(7,objeto.getTxtHistorico());
			pstmt.setInt(8,objeto.getStLancamentoAuto());
			pstmt.setString(9,objeto.getIdLancamentoAuto());
			if(objeto.getCdCentroCustoDebito()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdCentroCustoDebito());
			if(objeto.getCdCentroCustoCredito()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdCentroCustoCredito());
			pstmt.setInt(12, cdLancamentoAutoOld!=0 ? cdLancamentoAutoOld : objeto.getCdLancamentoAuto());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LancamentoAutoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoAutoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLancamentoAuto) {
		return delete(cdLancamentoAuto, null);
	}

	public static int delete(int cdLancamentoAuto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ctb_lancamento_auto WHERE cd_lancamento_auto=?");
			pstmt.setInt(1, cdLancamentoAuto);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LancamentoAutoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoAutoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LancamentoAuto get(int cdLancamentoAuto) {
		return get(cdLancamentoAuto, null);
	}

	public static LancamentoAuto get(int cdLancamentoAuto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ctb_lancamento_auto WHERE cd_lancamento_auto=?");
			pstmt.setInt(1, cdLancamentoAuto);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new LancamentoAuto(rs.getInt("cd_lancamento_auto"),
						rs.getInt("cd_conta_debito"),
						rs.getInt("cd_conta_credito"),
						rs.getInt("cd_historico"),
						rs.getInt("cd_empresa"),
						rs.getString("nm_lancamento_auto"),
						rs.getString("txt_historico"),
						rs.getInt("st_lancamento_auto"),
						rs.getString("id_lancamento_auto"),
						rs.getInt("cd_centro_custo_debito"),
						rs.getInt("cd_centro_custo_credito"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LancamentoAutoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoAutoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_lancamento_auto");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LancamentoAutoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoAutoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM ctb_lancamento_auto", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
