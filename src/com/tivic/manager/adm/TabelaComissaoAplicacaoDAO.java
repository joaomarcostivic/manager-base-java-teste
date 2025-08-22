package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TabelaComissaoAplicacaoDAO{

	public static int insert(TabelaComissaoAplicacao objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(TabelaComissaoAplicacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_tabela_comissao");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdTabelaComissao()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_aplicacao");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("adm_tabela_comissao_aplicacao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAplicacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_tabela_comissao_aplicacao (cd_tabela_comissao,"+
			                                  "cd_aplicacao,"+
			                                  "cd_categoria_economica,"+
			                                  "cd_grupo,"+
			                                  "cd_plano_pagamento,"+
			                                  "cd_forma_pagamento,"+
			                                  "cd_regiao,"+
			                                  "cd_agente,"+
			                                  "cd_cliente,"+
			                                  "cd_tipo_operacao,"+
			                                  "cd_produto_servico,"+
			                                  "lg_ativo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdTabelaComissao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTabelaComissao());
			pstmt.setInt(2, code);
			if(objeto.getCdCategoriaEconomica()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCategoriaEconomica());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdGrupo());
			if(objeto.getCdPlanoPagamento()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdPlanoPagamento());
			if(objeto.getCdFormaPagamento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdFormaPagamento());
			if(objeto.getCdRegiao()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdRegiao());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdAgente());
			if(objeto.getCdCliente()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdCliente());
			if(objeto.getCdTipoOperacao()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdTipoOperacao());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdProdutoServico());
			pstmt.setInt(12,objeto.getLgAtivo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoAplicacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoAplicacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TabelaComissaoAplicacao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TabelaComissaoAplicacao objeto, int cdTabelaComissaoOld, int cdAplicacaoOld) {
		return update(objeto, cdTabelaComissaoOld, cdAplicacaoOld, null);
	}

	public static int update(TabelaComissaoAplicacao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TabelaComissaoAplicacao objeto, int cdTabelaComissaoOld, int cdAplicacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_tabela_comissao_aplicacao SET cd_tabela_comissao=?,"+
												      		   "cd_aplicacao=?,"+
												      		   "cd_categoria_economica=?,"+
												      		   "cd_grupo=?,"+
												      		   "cd_plano_pagamento=?,"+
												      		   "cd_forma_pagamento=?,"+
												      		   "cd_regiao=?,"+
												      		   "cd_agente=?,"+
												      		   "cd_cliente=?,"+
												      		   "cd_tipo_operacao=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "lg_ativo=? WHERE cd_tabela_comissao=? AND cd_aplicacao=?");
			pstmt.setInt(1,objeto.getCdTabelaComissao());
			pstmt.setInt(2,objeto.getCdAplicacao());
			if(objeto.getCdCategoriaEconomica()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCategoriaEconomica());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdGrupo());
			if(objeto.getCdPlanoPagamento()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdPlanoPagamento());
			if(objeto.getCdFormaPagamento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdFormaPagamento());
			if(objeto.getCdRegiao()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdRegiao());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdAgente());
			if(objeto.getCdCliente()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdCliente());
			if(objeto.getCdTipoOperacao()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdTipoOperacao());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdProdutoServico());
			pstmt.setInt(12,objeto.getLgAtivo());
			pstmt.setInt(13, cdTabelaComissaoOld!=0 ? cdTabelaComissaoOld : objeto.getCdTabelaComissao());
			pstmt.setInt(14, cdAplicacaoOld!=0 ? cdAplicacaoOld : objeto.getCdAplicacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoAplicacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoAplicacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTabelaComissao, int cdAplicacao) {
		return delete(cdTabelaComissao, cdAplicacao, null);
	}

	public static int delete(int cdTabelaComissao, int cdAplicacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_tabela_comissao_aplicacao WHERE cd_tabela_comissao=? AND cd_aplicacao=?");
			pstmt.setInt(1, cdTabelaComissao);
			pstmt.setInt(2, cdAplicacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoAplicacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoAplicacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TabelaComissaoAplicacao get(int cdTabelaComissao, int cdAplicacao) {
		return get(cdTabelaComissao, cdAplicacao, null);
	}

	public static TabelaComissaoAplicacao get(int cdTabelaComissao, int cdAplicacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_tabela_comissao_aplicacao WHERE cd_tabela_comissao=? AND cd_aplicacao=?");
			pstmt.setInt(1, cdTabelaComissao);
			pstmt.setInt(2, cdAplicacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TabelaComissaoAplicacao(rs.getInt("cd_tabela_comissao"),
						rs.getInt("cd_aplicacao"),
						rs.getInt("cd_categoria_economica"),
						rs.getInt("cd_grupo"),
						rs.getInt("cd_plano_pagamento"),
						rs.getInt("cd_forma_pagamento"),
						rs.getInt("cd_regiao"),
						rs.getInt("cd_agente"),
						rs.getInt("cd_cliente"),
						rs.getInt("cd_tipo_operacao"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("lg_ativo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoAplicacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoAplicacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_tabela_comissao_aplicacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoAplicacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoAplicacaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_tabela_comissao_aplicacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
