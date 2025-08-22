package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class ProdutoServicoPrecoServices {

	public static int insert(ProdutoServicoPreco objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProdutoServicoPreco objeto, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			objeto.setDtTerminoValidade(null);

			ResultSet rs = connection.prepareStatement("SELECT * FROM ADM_PRODUTO_SERVICO_PRECO " +
		                                               "WHERE cd_tabela_preco    = "+objeto.getCdTabelaPreco()+
		                                               "  AND cd_produto_servico = "+objeto.getCdProdutoServico()+
		                                               "  AND dt_termino_validade IS NULL").executeQuery();
			if (rs.next())	{
				PreparedStatement pstmt = connection.prepareStatement("UPDATE adm_produto_servico_preco SET dt_termino_validade = ? " +
                                                                      "WHERE cd_tabela_preco    = "+objeto.getCdTabelaPreco()+
                                                                      "  AND cd_produto_servico = "+objeto.getCdProdutoServico()+
                                                                      "  AND dt_termino_validade IS NULL");
				pstmt.setTimestamp(1, new java.sql.Timestamp(new GregorianCalendar().getTimeInMillis()));
				pstmt.executeUpdate();
			}
			return ProdutoServicoPrecoDAO.insert(objeto, connection);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int update(ProdutoServicoPreco objeto) {
		return update(objeto, null);
	}

	public static int update(ProdutoServicoPreco objeto, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			objeto.setDtTerminoValidade(null);
			// se o preço for o mesmo não precisa atualizar
			boolean insertNew = false;
			ResultSetMap rsm = new ResultSetMap(connection.prepareStatement(
									"SELECT cd_produto_servico_preco, vl_preco " +
									"FROM ADM_PRODUTO_SERVICO_PRECO " +
									"WHERE cd_produto_servico = " +objeto.getCdProdutoServico()+
									"  AND cd_tabela_preco    = " +objeto.getCdTabelaPreco()+
									"  AND dt_termino_validade IS NULL").executeQuery());
			while (rsm.next()) {
				ProdutoServicoPreco preco = new ProdutoServicoPreco(objeto.getCdTabelaPreco(), objeto.getCdProdutoServico(),
						rsm.getInt("cd_produto_servico_preco"), new GregorianCalendar(), rsm.getFloat("vl_preco"));
				insertNew = insertNew || preco.getVlPreco()!=objeto.getVlPreco();
				if (preco.getVlPreco()!=objeto.getVlPreco() && ProdutoServicoPrecoDAO.update(preco, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
				if (preco.getVlPreco()==objeto.getVlPreco() && objeto.getCdProdutoServicoPreco()<=0)
					objeto.setCdProdutoServicoPreco(rsm.getInt("cd_produto_servico_preco"));
			}

			if (rsm.size()==0)
				insertNew = true;
			if (insertNew) {
				objeto.setCdProdutoServicoPreco(ProdutoServicoPrecoDAO.insert(objeto, connection));
			}

			if (objeto.getCdProdutoServicoPreco() <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();

			return objeto.getCdProdutoServicoPreco();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoPrecoServices.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int delete(int cdTabelaPreco, int cdProdutoServico) {
		return delete(cdTabelaPreco, cdProdutoServico, null);
	}

	public static int delete(int cdTabelaPreco, int cdProdutoServico, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement("DELETE FROM adm_produto_servico_preco " +
					"WHERE cd_tabela_preco=? " +
					"  AND cd_produto_servico=?");
			pstmt.setInt(1, cdTabelaPreco);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.execute();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoPrecoServices.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios) {
		return findCompleto(criterios, false, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, boolean groupByTabela) {
		return findCompleto(criterios, groupByTabela, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, boolean groupByTabela, Connection connection) {
		ResultSetMap rsm = Search.find("SELECT A.*, B.cd_empresa, B.nm_tabela_preco, C.cd_moeda, C.nm_moeda, C.sg_moeda, D.vl_ultimo_custo, E.nm_produto_servico " +
				"FROM adm_produto_servico_preco A, adm_tabela_preco B, grl_moeda C, grl_produto_servico_empresa D, grl_produto_servico E " +
				"WHERE A.cd_tabela_preco = B.cd_tabela_preco " +
				"  AND B.cd_moeda = C.cd_moeda " + 
				"  AND D.cd_produto_servico = A.cd_produto_servico " + 
				"  AND D.cd_empresa = B.cd_empresa " +
				"  AND E.cd_produto_servico = A.cd_produto_servico ", criterios,
				true, connection!=null ? connection : Conexao.conectar(), connection==null);
		if (groupByTabela) {
			ResultSetMap rsmTemp = new ResultSetMap();
			for (int i=0; rsm!=null && i<rsm.size(); i++) {
				rsm.goTo(i);
				boolean found = rsmTemp.locate("CD_TABELA_PRECO", new Integer(rsm.getInt("CD_TABELA_PRECO")), false);
				if (!found) {
					HashMap<String,Object> newRegister = new HashMap<String,Object>();
					newRegister.put("CD_TABELA_PRECO", new Integer(rsm.getInt("CD_TABELA_PRECO")));
					newRegister.put("NM_TABELA_PRECO", rsm.getString("NM_TABELA_PRECO"));
					newRegister.put("rsmPrecosTabela", new ResultSetMap());
					rsmTemp.addRegister(newRegister);
					rsmTemp.last();
				}
				ResultSetMap rsmPrecosTabela = (ResultSetMap)rsmTemp.getRegister().get("rsmPrecosTabela");
				if (rsmPrecosTabela != null)
					rsmPrecosTabela.addRegister(rsm.getRegister());
			}
			rsm = rsmTemp;
		}
		return rsm;
	}

	public static int setPrecoProdutoServico(int cdProdutoServico, int cdTabelaPreco, float vlPreco, float prDesconto) {
		return setPrecoProdutoServico(cdProdutoServico, cdTabelaPreco, vlPreco, prDesconto, false, null);
	}

	public static int setPrecoProdutoServico(int cdProdutoServico, int cdTabelaPreco, float vlPreco, float prDesconto, boolean updateRegra) {
		return setPrecoProdutoServico(cdProdutoServico, cdTabelaPreco, vlPreco, prDesconto, updateRegra, null);
	}

	public static int setPrecoProdutoServico(int cdProdutoServico, int cdTabelaPreco, float vlPreco, float prDesconto,
			boolean updateRegra, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			if (ProdutoServicoPrecoServices.update(new ProdutoServicoPreco(cdTabelaPreco, cdProdutoServico, 0 /*cdProdutoServicoPreco*/,
					null /*dtTerminoValidade*/, vlPreco /*vlPreco*/), connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (updateRegra) {
				PreparedStatement pstmt = connection.prepareStatement("SELECT cd_regra " +
						"FROM adm_tabela_preco_regra " +
						"WHERE cd_produto_servico = ? " +
						"  AND cd_tabela_preco = ? " +
						"  AND cd_fornecedor IS NULL " +
						"  AND cd_grupo IS NULL " +
						"  AND cd_tabela_preco_base IS NULL");
				pstmt.setInt(1, cdProdutoServico);
				pstmt.setInt(2, cdTabelaPreco);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					TabelaPrecoRegra regra = TabelaPrecoRegraDAO.get(cdTabelaPreco, rs.getInt("cd_regra"), connection);
					regra.setPrDesconto(prDesconto);
					if (TabelaPrecoRegraDAO.update(regra, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return -1;
					}
				}
				else if (TabelaPrecoRegraServices.insert(new TabelaPrecoRegra(cdTabelaPreco,
						0 /*cdRegra*/,
						0 /*cdTabelaPrecoBase*/,
						cdProdutoServico,
						0 /*cdFornecedor*/,
						0 /*cdGrupo*/,
						prDesconto,
						0 /*prMargemLucro*/,
						0 /*prMargemMinima*/,
						0 /*prMargemMaxima*/,
						0 /*lgIncluirImpostos*/,
						0 /*lgPrecoMinimo*/,
						TabelaPrecoRegraServices.TP_SEM_APROX /*tpAproximacao*/,
						0 /*nrProioridade*/,
						TabelaPrecoRegraServices.TP_BASE_ULTIMO_CUSTO /*tpValorBase*/), connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static Result transfereTabela(int cdTabelaPadrao, int cdTabelaCliente) {
		return transfereTabela(cdTabelaPadrao, cdTabelaCliente, null);
	}
	public static Result transfereTabela(int cdTabelaPadrao, int cdTabelaCliente, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());			
			if ((cdTabelaPadrao > 0) && (cdTabelaCliente > 0)) {
				PreparedStatement pstmt = connection.prepareStatement("INSERT INTO adm_produto_servico_preco (cd_tabela_preco, cd_produto_servico, "
																					+ "cd_produto_servico_preco, vl_preco)"
																					+ "SELECT " +cdTabelaCliente+ ", cd_produto_servico, "
																					+ "cd_produto_servico_preco, vl_preco "
																					+ "FROM adm_produto_servico_preco A1 "
																					+ "WHERE cd_tabela_preco = " + cdTabelaPadrao
																					+ "  AND NOT EXISTS (SELECT * FROM  adm_produto_Servico_preco X "
																					+ "					  WHERE A1.cd_produto_servico = X.cd_produto_servico "
																					+ "					    AND X.cd_tabela_preco = "+cdTabelaCliente+ ")");
				
				
				pstmt.execute();			
			}
			
			if (isConnectionNull)
				connection.commit();
			
			return new Result(1, "Tabela transferida com sucesso.");
		
		}catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro ao tentar transferir Tabela: " +  e);
			return new Result(-1, "Erro ao tentar transferir Tabela");
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}	
	}

}
