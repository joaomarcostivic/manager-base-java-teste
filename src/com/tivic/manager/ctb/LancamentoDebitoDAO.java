package com.tivic.manager.ctb;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class LancamentoDebitoDAO{

	public static int insert(LancamentoDebito objeto) {
		return insert(objeto, null);
	}

	public static int insert(LancamentoDebito objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ctb_lancamento_debito (cd_lancamento,"+
			                                  "cd_conta_debito,"+
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
			if(objeto.getCdContaDebito()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdContaDebito());
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
			System.err.println("Erro! LancamentoDebitoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoDebitoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LancamentoDebito objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(LancamentoDebito objeto, int cdLancamentoOld, int cdContaDebitoOld) {
		return update(objeto, cdLancamentoOld, cdContaDebitoOld, null);
	}

	public static int update(LancamentoDebito objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(LancamentoDebito objeto, int cdLancamentoOld, int cdContaDebitoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ctb_lancamento_debito SET cd_lancamento=?,"+
												      		   "cd_conta_debito=?,"+
												      		   "cd_historico=?,"+
												      		   "nr_documento=?,"+
												      		   "vl_lancamento=?,"+
												      		   "txt_historico=?,"+
												      		   "txt_observacao=?,"+
												      		   "st_lancamento=? WHERE cd_lancamento=? AND cd_conta_debito=?");
			pstmt.setInt(1,objeto.getCdLancamento());
			pstmt.setInt(2,objeto.getCdContaDebito());
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
			pstmt.setInt(10, cdContaDebitoOld!=0 ? cdContaDebitoOld : objeto.getCdContaDebito());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LancamentoDebitoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoDebitoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLancamento, int cdContaDebito) {
		return delete(cdLancamento, cdContaDebito, null);
	}

	public static int delete(int cdLancamento, int cdContaDebito, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ctb_lancamento_debito WHERE cd_lancamento=? AND cd_conta_debito=?");
			pstmt.setInt(1, cdLancamento);
			pstmt.setInt(2, cdContaDebito);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LancamentoDebitoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoDebitoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LancamentoDebito get(int cdLancamento, int cdContaDebito) {
		return get(cdLancamento, cdContaDebito, null);
	}

	public static LancamentoDebito get(int cdLancamento, int cdContaDebito, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ctb_lancamento_debito WHERE cd_lancamento=? AND cd_conta_debito=?");
			pstmt.setInt(1, cdLancamento);
			pstmt.setInt(2, cdContaDebito);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new LancamentoDebito(rs.getInt("cd_lancamento"),
						rs.getInt("cd_conta_debito"),
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
			System.err.println("Erro! LancamentoDebitoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoDebitoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_lancamento_debito");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LancamentoDebitoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoDebitoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM ctb_lancamento_debito", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
