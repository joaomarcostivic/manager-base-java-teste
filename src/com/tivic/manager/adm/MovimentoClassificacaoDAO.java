package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class MovimentoClassificacaoDAO{

	public static int insert(MovimentoClassificacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(MovimentoClassificacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_movimento_classificacao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMovimentoClassificacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_movimento_classificacao (cd_movimento_classificacao,"+
			                                  "cd_conta_financeira,"+
			                                  "cd_movimento_conta,"+
			                                  "cd_categoria_economica,"+
			                                  "cd_centro_custo,"+
			                                  "cd_lancamento,"+
			                                  "cd_conta_debito,"+
			                                  "cd_conta_credito,"+
			                                  "vl_movimento_classificacao,"+
			                                  "cd_movimento_conta_categoria) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdContaFinanceira()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdContaFinanceira());
			if(objeto.getCdMovimentoConta()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdMovimentoConta());
			if(objeto.getCdCategoriaEconomica()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCategoriaEconomica());
			if(objeto.getCdCentroCusto()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdCentroCusto());
			if(objeto.getCdLancamento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdLancamento());
			if(objeto.getCdContaDebito()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdContaDebito());
			if(objeto.getCdContaCredito()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdContaCredito());
			pstmt.setFloat(9,objeto.getVlMovimentoClassificacao());
			if(objeto.getCdMovimentoContaCategoria()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdMovimentoContaCategoria());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoClassificacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoClassificacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MovimentoClassificacao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(MovimentoClassificacao objeto, int cdMovimentoClassificacaoOld) {
		return update(objeto, cdMovimentoClassificacaoOld, null);
	}

	public static int update(MovimentoClassificacao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(MovimentoClassificacao objeto, int cdMovimentoClassificacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_movimento_classificacao SET cd_movimento_classificacao=?,"+
												      		   "cd_conta_financeira=?,"+
												      		   "cd_movimento_conta=?,"+
												      		   "cd_categoria_economica=?,"+
												      		   "cd_centro_custo=?,"+
												      		   "cd_lancamento=?,"+
												      		   "cd_conta_debito=?,"+
												      		   "cd_conta_credito=?,"+
												      		   "vl_movimento_classificacao=?,"+
												      		   "cd_movimento_conta_categoria=? WHERE cd_movimento_classificacao=?");
			pstmt.setInt(1,objeto.getCdMovimentoClassificacao());
			if(objeto.getCdContaFinanceira()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdContaFinanceira());
			if(objeto.getCdMovimentoConta()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdMovimentoConta());
			if(objeto.getCdCategoriaEconomica()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCategoriaEconomica());
			if(objeto.getCdCentroCusto()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdCentroCusto());
			if(objeto.getCdLancamento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdLancamento());
			if(objeto.getCdContaDebito()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdContaDebito());
			if(objeto.getCdContaCredito()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdContaCredito());
			pstmt.setFloat(9,objeto.getVlMovimentoClassificacao());
			if(objeto.getCdMovimentoContaCategoria()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdMovimentoContaCategoria());
			pstmt.setInt(11, cdMovimentoClassificacaoOld!=0 ? cdMovimentoClassificacaoOld : objeto.getCdMovimentoClassificacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoClassificacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoClassificacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMovimentoClassificacao) {
		return delete(cdMovimentoClassificacao, null);
	}

	public static int delete(int cdMovimentoClassificacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_movimento_classificacao WHERE cd_movimento_classificacao=?");
			pstmt.setInt(1, cdMovimentoClassificacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoClassificacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoClassificacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MovimentoClassificacao get(int cdMovimentoClassificacao) {
		return get(cdMovimentoClassificacao, null);
	}

	public static MovimentoClassificacao get(int cdMovimentoClassificacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_movimento_classificacao WHERE cd_movimento_classificacao=?");
			pstmt.setInt(1, cdMovimentoClassificacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MovimentoClassificacao(rs.getInt("cd_movimento_classificacao"),
						rs.getInt("cd_conta_financeira"),
						rs.getInt("cd_movimento_conta"),
						rs.getInt("cd_categoria_economica"),
						rs.getInt("cd_centro_custo"),
						rs.getInt("cd_lancamento"),
						rs.getInt("cd_conta_debito"),
						rs.getInt("cd_conta_credito"),
						rs.getFloat("vl_movimento_classificacao"),
						rs.getInt("cd_movimento_conta_categoria"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoClassificacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoClassificacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_movimento_classificacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoClassificacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoClassificacaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_movimento_classificacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
