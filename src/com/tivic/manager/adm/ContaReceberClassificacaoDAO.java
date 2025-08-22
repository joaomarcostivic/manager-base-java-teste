package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ContaReceberClassificacaoDAO{

	public static int insert(ContaReceberClassificacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContaReceberClassificacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_conta_receber_classificacao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdContaReceberClassificacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_conta_receber_classificacao (cd_conta_receber_classificacao,"+
			                                  "cd_conta_receber,"+
			                                  "cd_categoria_economica,"+
			                                  "cd_centro_custo,"+
			                                  "cd_lancamento,"+
			                                  "cd_conta_debito,"+
			                                  "cd_conta_credito,"+
			                                  "vl_conta_classificacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdContaReceber());
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
			System.err.println("Erro! ContaReceberClassificacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberClassificacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContaReceberClassificacao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ContaReceberClassificacao objeto, int cdContaReceberClassificacaoOld) {
		return update(objeto, cdContaReceberClassificacaoOld, null);
	}

	public static int update(ContaReceberClassificacao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ContaReceberClassificacao objeto, int cdContaReceberClassificacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_conta_receber_classificacao SET cd_conta_receber_classificacao=?,"+
												      		   "cd_conta_receber=?,"+
												      		   "cd_categoria_economica=?,"+
												      		   "cd_centro_custo=?,"+
												      		   "cd_lancamento=?,"+
												      		   "cd_conta_debito=?,"+
												      		   "cd_conta_credito=?,"+
												      		   "vl_conta_classificacao=? WHERE cd_conta_receber_classificacao=?");
			pstmt.setInt(1,objeto.getCdContaReceberClassificacao());
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdContaReceber());
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
			pstmt.setInt(9, cdContaReceberClassificacaoOld!=0 ? cdContaReceberClassificacaoOld : objeto.getCdContaReceberClassificacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberClassificacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberClassificacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContaReceberClassificacao) {
		return delete(cdContaReceberClassificacao, null);
	}

	public static int delete(int cdContaReceberClassificacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_conta_receber_classificacao WHERE cd_conta_receber_classificacao=?");
			pstmt.setInt(1, cdContaReceberClassificacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberClassificacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberClassificacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaReceberClassificacao get(int cdContaReceberClassificacao) {
		return get(cdContaReceberClassificacao, null);
	}

	public static ContaReceberClassificacao get(int cdContaReceberClassificacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_receber_classificacao WHERE cd_conta_receber_classificacao=?");
			pstmt.setInt(1, cdContaReceberClassificacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaReceberClassificacao(rs.getInt("cd_conta_receber_classificacao"),
						rs.getInt("cd_conta_receber"),
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
			System.err.println("Erro! ContaReceberClassificacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberClassificacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_receber_classificacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberClassificacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberClassificacaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_conta_receber_classificacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
