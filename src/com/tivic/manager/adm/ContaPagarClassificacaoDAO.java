package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContaPagarClassificacaoDAO{

	public static int insert(ContaPagarClassificacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContaPagarClassificacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_conta_pagar_classificacao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdContaPagarClassificacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_conta_pagar_classificacao (cd_conta_pagar_classificacao,"+
			                                  "cd_conta_pagar,"+
			                                  "cd_categoria_economica,"+
			                                  "cd_centro_custo,"+
			                                  "cd_lancamento,"+
			                                  "cd_conta_debito,"+
			                                  "cd_conta_credito,"+
			                                  "vl_conta_classificacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdContaPagar());
			if(objeto.getCdCategoriaEconomica()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCategoriaEconomica());
			if(objeto.getCdCentroCusto()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCentroCusto());
			if(objeto.getCdLancamento()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdLancamento());
			if(objeto.getCdContaDebito()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdContaDebito());
			if(objeto.getCdContaCredito()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdContaCredito());
			pstmt.setFloat(8,objeto.getVlContaClassificacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarClassificacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarClassificacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContaPagarClassificacao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ContaPagarClassificacao objeto, int cdContaPagarClassificacaoOld) {
		return update(objeto, cdContaPagarClassificacaoOld, null);
	}

	public static int update(ContaPagarClassificacao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ContaPagarClassificacao objeto, int cdContaPagarClassificacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_conta_pagar_classificacao SET cd_conta_pagar_classificacao=?,"+
												      		   "cd_conta_pagar=?,"+
												      		   "cd_categoria_economica=?,"+
												      		   "cd_centro_custo=?,"+
												      		   "cd_lancamento=?,"+
												      		   "cd_conta_debito=?,"+
												      		   "cd_conta_credito=?,"+
												      		   "vl_conta_classificacao=? WHERE cd_conta_pagar_classificacao=?");
			pstmt.setInt(1,objeto.getCdContaPagarClassificacao());
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdContaPagar());
			if(objeto.getCdCategoriaEconomica()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCategoriaEconomica());
			if(objeto.getCdCentroCusto()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCentroCusto());
			if(objeto.getCdLancamento()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdLancamento());
			if(objeto.getCdContaDebito()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdContaDebito());
			if(objeto.getCdContaCredito()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdContaCredito());
			pstmt.setFloat(8,objeto.getVlContaClassificacao());
			pstmt.setInt(9, cdContaPagarClassificacaoOld!=0 ? cdContaPagarClassificacaoOld : objeto.getCdContaPagarClassificacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarClassificacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarClassificacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContaPagarClassificacao) {
		return delete(cdContaPagarClassificacao, null);
	}

	public static int delete(int cdContaPagarClassificacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_conta_pagar_classificacao WHERE cd_conta_pagar_classificacao=?");
			pstmt.setInt(1, cdContaPagarClassificacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarClassificacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarClassificacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaPagarClassificacao get(int cdContaPagarClassificacao) {
		return get(cdContaPagarClassificacao, null);
	}

	public static ContaPagarClassificacao get(int cdContaPagarClassificacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_pagar_classificacao WHERE cd_conta_pagar_classificacao=?");
			pstmt.setInt(1, cdContaPagarClassificacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaPagarClassificacao(rs.getInt("cd_conta_pagar_classificacao"),
						rs.getInt("cd_conta_pagar"),
						rs.getInt("cd_categoria_economica"),
						rs.getInt("cd_centro_custo"),
						rs.getInt("cd_lancamento"),
						rs.getInt("cd_conta_debito"),
						rs.getInt("cd_conta_credito"),
						rs.getFloat("vl_conta_classificacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarClassificacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarClassificacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_pagar_classificacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarClassificacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarClassificacaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_conta_pagar_classificacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
