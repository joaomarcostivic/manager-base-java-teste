package com.tivic.manager.ctb;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class LancamentoDAO{

	public static int insert(Lancamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(Lancamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ctb_lancamento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLancamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ctb_lancamento (cd_lancamento,"+
			                                  "cd_lote,"+
			                                  "cd_lancamento_auto,"+
			                                  "dt_lancamento,"+
			                                  "vl_total,"+
			                                  "lg_provisao,"+
			                                  "cd_empresa,"+
			                                  "id_lancamento,"+
			                                  "cd_movimento_conta,"+
			                                  "cd_conta_financeira,"+
			                                  "cd_conta_receber,"+
			                                  "cd_conta_pagar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdLote()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdLote());
			if(objeto.getCdLancamentoAuto()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdLancamentoAuto());
			if(objeto.getDtLancamento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtLancamento().getTimeInMillis()));
			pstmt.setFloat(5,objeto.getVlTotal());
			pstmt.setInt(6,objeto.getLgProvisao());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdEmpresa());
			pstmt.setString(8,objeto.getIdLancamento());
			if(objeto.getCdMovimentoConta()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdMovimentoConta());
			if(objeto.getCdContaFinanceira()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdContaFinanceira());
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdContaReceber());
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdContaPagar());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LancamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Lancamento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Lancamento objeto, int cdLancamentoOld) {
		return update(objeto, cdLancamentoOld, null);
	}

	public static int update(Lancamento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Lancamento objeto, int cdLancamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ctb_lancamento SET cd_lancamento=?,"+
												      		   "cd_lote=?,"+
												      		   "cd_lancamento_auto=?,"+
												      		   "dt_lancamento=?,"+
												      		   "vl_total=?,"+
												      		   "lg_provisao=?,"+
												      		   "cd_empresa=?,"+
												      		   "id_lancamento=?,"+
												      		   "cd_movimento_conta=?,"+
												      		   "cd_conta_financeira=?,"+
												      		   "cd_conta_receber=?,"+
												      		   "cd_conta_pagar=? WHERE cd_lancamento=?");
			pstmt.setInt(1,objeto.getCdLancamento());
			if(objeto.getCdLote()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdLote());
			if(objeto.getCdLancamentoAuto()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdLancamentoAuto());
			if(objeto.getDtLancamento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtLancamento().getTimeInMillis()));
			pstmt.setFloat(5,objeto.getVlTotal());
			pstmt.setInt(6,objeto.getLgProvisao());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdEmpresa());
			pstmt.setString(8,objeto.getIdLancamento());
			if(objeto.getCdMovimentoConta()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdMovimentoConta());
			if(objeto.getCdContaFinanceira()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdContaFinanceira());
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdContaReceber());
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdContaPagar());
			pstmt.setInt(13, cdLancamentoOld!=0 ? cdLancamentoOld : objeto.getCdLancamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LancamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLancamento) {
		return delete(cdLancamento, null);
	}

	public static int delete(int cdLancamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ctb_lancamento WHERE cd_lancamento=?");
			pstmt.setInt(1, cdLancamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LancamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Lancamento get(int cdLancamento) {
		return get(cdLancamento, null);
	}

	public static Lancamento get(int cdLancamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ctb_lancamento WHERE cd_lancamento=?");
			pstmt.setInt(1, cdLancamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Lancamento(rs.getInt("cd_lancamento"),
						rs.getInt("cd_lote"),
						rs.getInt("cd_lancamento_auto"),
						(rs.getTimestamp("dt_lancamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_lancamento").getTime()),
						rs.getFloat("vl_total"),
						rs.getInt("lg_provisao"),
						rs.getInt("cd_empresa"),
						rs.getString("id_lancamento"),
						rs.getInt("cd_movimento_conta"),
						rs.getInt("cd_conta_financeira"),
						rs.getInt("cd_conta_receber"),
						rs.getInt("cd_conta_pagar"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LancamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ctb_lancamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LancamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LancamentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM ctb_lancamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
