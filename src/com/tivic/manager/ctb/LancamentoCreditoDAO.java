package com.tivic.manager.ctb;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class LancamentoCreditoDAO{

	public static int insert(LancamentoCredito objeto) {
		return insert(objeto, null);
	}

	public static int insert(LancamentoCredito objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ctb_lancamento_credito (cd_lancamento,"+
			                                  "cd_conta_credito,"+
			                                  "cd_historico,"+
			                                  "nr_documento,"+
			                                  "vl_lancamento,"+
			                                  "txt_historico,"+
			                                  "txt_observacao,"+
			                                  "st_lancamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdLancamento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdLancamento());
			if(objeto.getCdContaCredito()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdContaCredito());
			if(objeto.getCdHistorico()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdHistorico());
			pstmt.setString(4,objeto.getNrDocumento());
			pstmt.setFloat(5,objeto.getVlLancamento());
			pstmt.setString(6,objeto.getTxtHistorico());
			pstmt.setString(7,objeto.getTxtObservacao());
			pstmt.setInt(8,objeto.getStLancamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LancamentoCreditoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoCreditoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LancamentoCredito objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(LancamentoCredito objeto, int cdLancamentoOld, int cdContaCreditoOld) {
		return update(objeto, cdLancamentoOld, cdContaCreditoOld, null);
	}

	public static int update(LancamentoCredito objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(LancamentoCredito objeto, int cdLancamentoOld, int cdContaCreditoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ctb_lancamento_credito SET cd_lancamento=?,"+
												      		   "cd_conta_credito=?,"+
												      		   "cd_historico=?,"+
												      		   "nr_documento=?,"+
												      		   "vl_lancamento=?,"+
												      		   "txt_historico=?,"+
												      		   "txt_observacao=?,"+
												      		   "st_lancamento=? WHERE cd_lancamento=? AND cd_conta_credito=?");
			pstmt.setInt(1,objeto.getCdLancamento());
			pstmt.setInt(2,objeto.getCdContaCredito());
			if(objeto.getCdHistorico()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdHistorico());
			pstmt.setString(4,objeto.getNrDocumento());
			pstmt.setFloat(5,objeto.getVlLancamento());
			pstmt.setString(6,objeto.getTxtHistorico());
			pstmt.setString(7,objeto.getTxtObservacao());
			pstmt.setInt(8,objeto.getStLancamento());
			pstmt.setInt(9, cdLancamentoOld!=0 ? cdLancamentoOld : objeto.getCdLancamento());
			pstmt.setInt(10, cdContaCreditoOld!=0 ? cdContaCreditoOld : objeto.getCdContaCredito());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LancamentoCreditoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoCreditoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLancamento, int cdContaCredito) {
		return delete(cdLancamento, cdContaCredito, null);
	}

	public static int delete(int cdLancamento, int cdContaCredito, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ctb_lancamento_credito WHERE cd_lancamento=? AND cd_conta_credito=?");
			pstmt.setInt(1, cdLancamento);
			pstmt.setInt(2, cdContaCredito);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LancamentoCreditoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoCreditoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LancamentoCredito get(int cdLancamento, int cdContaCredito) {
		return get(cdLancamento, cdContaCredito, null);
	}

	public static LancamentoCredito get(int cdLancamento, int cdContaCredito, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ctb_lancamento_credito WHERE cd_lancamento=? AND cd_conta_credito=?");
			pstmt.setInt(1, cdLancamento);
			pstmt.setInt(2, cdContaCredito);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new LancamentoCredito(rs.getInt("cd_lancamento"),
						rs.getInt("cd_conta_credito"),
						rs.getInt("cd_historico"),
						rs.getString("nr_documento"),
						rs.getFloat("vl_lancamento"),
						rs.getString("txt_historico"),
						rs.getString("txt_observacao"),
						rs.getInt("st_lancamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LancamentoCreditoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoCreditoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_lancamento_credito");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LancamentoCreditoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoCreditoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM ctb_lancamento_credito", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
