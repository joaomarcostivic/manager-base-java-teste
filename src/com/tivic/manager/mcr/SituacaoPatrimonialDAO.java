package com.tivic.manager.mcr;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class SituacaoPatrimonialDAO{

	public static int insert(SituacaoPatrimonial objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(SituacaoPatrimonial objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			HashMap<String,Object>[] keys = new HashMap[1];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_empreendimento");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdEmpreendimento()));
			int code = Conexao.getSequenceCode("mcr_situacao_patrimonial", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO mcr_situacao_patrimonial (cd_empreendimento,"+
			                                  "vl_caixa,"+
			                                  "vl_receber,"+
			                                  "vl_estoque,"+
			                                  "vl_credito,"+
			                                  "vl_terreno,"+
			                                  "vl_imovel,"+
			                                  "vl_maquina,"+
			                                  "vl_veiculo,"+
			                                  "vl_moveis,"+
			                                  "vl_outros_ativo,"+
			                                  "vl_fornecedor,"+
			                                  "vl_imposto,"+
			                                  "vl_emprestimo,"+
			                                  "vl_credito_proprietarios,"+
			                                  "vl_divida,"+
			                                  "vl_adiantamento,"+
			                                  "lg_conta_empresarial,"+
			                                  "nr_conta_corrente,"+
			                                  "nm_banco_conta,"+
			                                  "nr_agencia_conta,"+
			                                  "vl_saldo_medio,"+
			                                  "vl_limite_cheque,"+
			                                  "nr_conta_poupanca,"+
			                                  "nm_banco_poupanca,"+
			                                  "nr_agencia_poupanca,"+
			                                  "vl_saldo_aplicacao,"+
			                                  "nm_credor,"+
			                                  "vl_prestacao,"+
			                                  "nr_parcela) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEmpreendimento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpreendimento());
			pstmt.setFloat(2,objeto.getVlCaixa());
			pstmt.setFloat(3,objeto.getVlReceber());
			pstmt.setFloat(4,objeto.getVlEstoque());
			pstmt.setFloat(5,objeto.getVlCredito());
			pstmt.setFloat(6,objeto.getVlTerreno());
			pstmt.setFloat(7,objeto.getVlImovel());
			pstmt.setFloat(8,objeto.getVlMaquina());
			pstmt.setFloat(9,objeto.getVlVeiculo());
			pstmt.setFloat(10,objeto.getVlMoveis());
			pstmt.setFloat(11,objeto.getVlOutrosAtivo());
			pstmt.setFloat(12,objeto.getVlFornecedor());
			pstmt.setFloat(13,objeto.getVlImposto());
			pstmt.setFloat(14,objeto.getVlEmprestimo());
			pstmt.setFloat(15,objeto.getVlCreditoProprietarios());
			pstmt.setFloat(16,objeto.getVlDivida());
			pstmt.setFloat(17,objeto.getVlAdiantamento());
			pstmt.setInt(18,objeto.getLgContaEmpresarial());
			pstmt.setString(19,objeto.getNrContaCorrente());
			pstmt.setString(20,objeto.getNmBancoConta());
			pstmt.setString(21,objeto.getNrAgenciaConta());
			pstmt.setFloat(22,objeto.getVlSaldoMedio());
			pstmt.setFloat(23,objeto.getVlLimiteCheque());
			pstmt.setString(24,objeto.getNrContaPoupanca());
			pstmt.setString(25,objeto.getNmBancoPoupanca());
			pstmt.setString(26,objeto.getNrAgenciaPoupanca());
			pstmt.setFloat(27,objeto.getVlSaldoAplicacao());
			pstmt.setString(28,objeto.getNmCredor());
			pstmt.setFloat(29,objeto.getVlPrestacao());
			pstmt.setInt(30,objeto.getNrParcela());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SituacaoPatrimonialDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoPatrimonialDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(SituacaoPatrimonial objeto) {
		return update(objeto, null);
	}

	public static int update(SituacaoPatrimonial objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE mcr_situacao_patrimonial SET vl_caixa=?,"+
			                                  "vl_receber=?,"+
			                                  "vl_estoque=?,"+
			                                  "vl_credito=?,"+
			                                  "vl_terreno=?,"+
			                                  "vl_imovel=?,"+
			                                  "vl_maquina=?,"+
			                                  "vl_veiculo=?,"+
			                                  "vl_moveis=?,"+
			                                  "vl_outros_ativo=?,"+
			                                  "vl_fornecedor=?,"+
			                                  "vl_imposto=?,"+
			                                  "vl_emprestimo=?,"+
			                                  "vl_credito_proprietarios=?,"+
			                                  "vl_divida=?,"+
			                                  "vl_adiantamento=?,"+
			                                  "lg_conta_empresarial=?,"+
			                                  "nr_conta_corrente=?,"+
			                                  "nm_banco_conta=?,"+
			                                  "nr_agencia_conta=?,"+
			                                  "vl_saldo_medio=?,"+
			                                  "vl_limite_cheque=?,"+
			                                  "nr_conta_poupanca=?,"+
			                                  "nm_banco_poupanca=?,"+
			                                  "nr_agencia_poupanca=?,"+
			                                  "vl_saldo_aplicacao=?,"+
			                                  "nm_credor=?,"+
			                                  "vl_prestacao=?,"+
			                                  "nr_parcela=? WHERE cd_empreendimento=?");
			pstmt.setFloat(1,objeto.getVlCaixa());
			pstmt.setFloat(2,objeto.getVlReceber());
			pstmt.setFloat(3,objeto.getVlEstoque());
			pstmt.setFloat(4,objeto.getVlCredito());
			pstmt.setFloat(5,objeto.getVlTerreno());
			pstmt.setFloat(6,objeto.getVlImovel());
			pstmt.setFloat(7,objeto.getVlMaquina());
			pstmt.setFloat(8,objeto.getVlVeiculo());
			pstmt.setFloat(9,objeto.getVlMoveis());
			pstmt.setFloat(10,objeto.getVlOutrosAtivo());
			pstmt.setFloat(11,objeto.getVlFornecedor());
			pstmt.setFloat(12,objeto.getVlImposto());
			pstmt.setFloat(13,objeto.getVlEmprestimo());
			pstmt.setFloat(14,objeto.getVlCreditoProprietarios());
			pstmt.setFloat(15,objeto.getVlDivida());
			pstmt.setFloat(16,objeto.getVlAdiantamento());
			pstmt.setInt(17,objeto.getLgContaEmpresarial());
			pstmt.setString(18,objeto.getNrContaCorrente());
			pstmt.setString(19,objeto.getNmBancoConta());
			pstmt.setString(20,objeto.getNrAgenciaConta());
			pstmt.setFloat(21,objeto.getVlSaldoMedio());
			pstmt.setFloat(22,objeto.getVlLimiteCheque());
			pstmt.setString(23,objeto.getNrContaPoupanca());
			pstmt.setString(24,objeto.getNmBancoPoupanca());
			pstmt.setString(25,objeto.getNrAgenciaPoupanca());
			pstmt.setFloat(26,objeto.getVlSaldoAplicacao());
			pstmt.setString(27,objeto.getNmCredor());
			pstmt.setFloat(28,objeto.getVlPrestacao());
			pstmt.setInt(29,objeto.getNrParcela());
			pstmt.setInt(30,objeto.getCdEmpreendimento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SituacaoPatrimonialDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoPatrimonialDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEmpreendimento) {
		return delete(cdEmpreendimento, null);
	}

	public static int delete(int cdEmpreendimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM mcr_situacao_patrimonial WHERE cd_empreendimento=?");
			pstmt.setInt(1, cdEmpreendimento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SituacaoPatrimonialDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoPatrimonialDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static SituacaoPatrimonial get(int cdEmpreendimento) {
		return get(cdEmpreendimento, null);
	}

	public static SituacaoPatrimonial get(int cdEmpreendimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mcr_situacao_patrimonial WHERE cd_empreendimento=?");
			pstmt.setInt(1, cdEmpreendimento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new SituacaoPatrimonial(rs.getInt("cd_empreendimento"),
						rs.getFloat("vl_caixa"),
						rs.getFloat("vl_receber"),
						rs.getFloat("vl_estoque"),
						rs.getFloat("vl_credito"),
						rs.getFloat("vl_terreno"),
						rs.getFloat("vl_imovel"),
						rs.getFloat("vl_maquina"),
						rs.getFloat("vl_veiculo"),
						rs.getFloat("vl_moveis"),
						rs.getFloat("vl_outros_ativo"),
						rs.getFloat("vl_fornecedor"),
						rs.getFloat("vl_imposto"),
						rs.getFloat("vl_emprestimo"),
						rs.getFloat("vl_credito_proprietarios"),
						rs.getFloat("vl_divida"),
						rs.getFloat("vl_adiantamento"),
						rs.getInt("lg_conta_empresarial"),
						rs.getString("nr_conta_corrente"),
						rs.getString("nm_banco_conta"),
						rs.getString("nr_agencia_conta"),
						rs.getFloat("vl_saldo_medio"),
						rs.getFloat("vl_limite_cheque"),
						rs.getString("nr_conta_poupanca"),
						rs.getString("nm_banco_poupanca"),
						rs.getString("nr_agencia_poupanca"),
						rs.getFloat("vl_saldo_aplicacao"),
						rs.getString("nm_credor"),
						rs.getFloat("vl_prestacao"),
						rs.getInt("nr_parcela"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SituacaoPatrimonialDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoPatrimonialDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mcr_situacao_patrimonial");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SituacaoPatrimonialDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SituacaoPatrimonialDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM mcr_situacao_patrimonial", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
