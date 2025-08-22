package com.tivic.manager.flp;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class FolhaEventoDAO{

	public static int insert(FolhaEvento objeto) {
		return insert(objeto, null);
	}

	public static int insert(FolhaEvento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO flp_folha_evento (cd_evento_financeiro,"+
			                                  "cd_matricula,"+
			                                  "cd_folha_pagamento,"+
			                                  "cd_rescisao,"+
			                                  "cd_ferias,"+
			                                  "tp_lancamento,"+
			                                  "qt_evento,"+
			                                  "vl_evento) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEventoFinanceiro()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEventoFinanceiro());
			if(objeto.getCdMatricula()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMatricula());
			if(objeto.getCdFolhaPagamento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdFolhaPagamento());
			if(objeto.getCdRescisao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdRescisao());
			if(objeto.getCdFerias()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdFerias());
			pstmt.setInt(6,objeto.getTpLancamento());
			pstmt.setFloat(7,objeto.getQtEvento());
			pstmt.setFloat(8,objeto.getVlEvento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaEventoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaEventoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(FolhaEvento objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(FolhaEvento objeto, int cdEventoFinanceiroOld, int cdMatriculaOld, int cdFolhaPagamentoOld) {
		return update(objeto, cdEventoFinanceiroOld, cdMatriculaOld, cdFolhaPagamentoOld, null);
	}

	public static int update(FolhaEvento objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(FolhaEvento objeto, int cdEventoFinanceiroOld, int cdMatriculaOld, int cdFolhaPagamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE flp_folha_evento SET cd_evento_financeiro=?,"+
												      		   "cd_matricula=?,"+
												      		   "cd_folha_pagamento=?,"+
												      		   "cd_rescisao=?,"+
												      		   "cd_ferias=?,"+
												      		   "tp_lancamento=?,"+
												      		   "qt_evento=?,"+
												      		   "vl_evento=? WHERE cd_evento_financeiro=? AND cd_matricula=? AND cd_folha_pagamento=?");
			pstmt.setInt(1,objeto.getCdEventoFinanceiro());
			pstmt.setInt(2,objeto.getCdMatricula());
			pstmt.setInt(3,objeto.getCdFolhaPagamento());
			if(objeto.getCdRescisao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdRescisao());
			if(objeto.getCdFerias()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdFerias());
			pstmt.setInt(6,objeto.getTpLancamento());
			pstmt.setFloat(7,objeto.getQtEvento());
			pstmt.setFloat(8,objeto.getVlEvento());
			pstmt.setInt(9, cdEventoFinanceiroOld!=0 ? cdEventoFinanceiroOld : objeto.getCdEventoFinanceiro());
			pstmt.setInt(10, cdMatriculaOld!=0 ? cdMatriculaOld : objeto.getCdMatricula());
			pstmt.setInt(11, cdFolhaPagamentoOld!=0 ? cdFolhaPagamentoOld : objeto.getCdFolhaPagamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaEventoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaEventoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEventoFinanceiro, int cdMatricula, int cdFolhaPagamento) {
		return delete(cdEventoFinanceiro, cdMatricula, cdFolhaPagamento, null);
	}

	public static int delete(int cdEventoFinanceiro, int cdMatricula, int cdFolhaPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM flp_folha_evento WHERE cd_evento_financeiro=? AND cd_matricula=? AND cd_folha_pagamento=?");
			pstmt.setInt(1, cdEventoFinanceiro);
			pstmt.setInt(2, cdMatricula);
			pstmt.setInt(3, cdFolhaPagamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaEventoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaEventoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static FolhaEvento get(int cdEventoFinanceiro, int cdMatricula, int cdFolhaPagamento) {
		return get(cdEventoFinanceiro, cdMatricula, cdFolhaPagamento, null);
	}

	public static FolhaEvento get(int cdEventoFinanceiro, int cdMatricula, int cdFolhaPagamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM flp_folha_evento WHERE cd_evento_financeiro=? AND cd_matricula=? AND cd_folha_pagamento=?");
			pstmt.setInt(1, cdEventoFinanceiro);
			pstmt.setInt(2, cdMatricula);
			pstmt.setInt(3, cdFolhaPagamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new FolhaEvento(rs.getInt("cd_evento_financeiro"),
						rs.getInt("cd_matricula"),
						rs.getInt("cd_folha_pagamento"),
						rs.getInt("cd_rescisao"),
						rs.getInt("cd_ferias"),
						rs.getInt("tp_lancamento"),
						rs.getFloat("qt_evento"),
						rs.getFloat("vl_evento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaEventoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaEventoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM flp_folha_evento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FolhaEventoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FolhaEventoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM flp_folha_evento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
