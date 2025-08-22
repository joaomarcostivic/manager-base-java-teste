package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.UnidadeConversaoServices;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class ProdutoServicoComposicaoServices {
	public static ResultSetMap getAllByEmpresaProdutoServico(int cdEmpresa, int cdProdutoServico) {
		return getAllByEmpresaProdutoServico(cdEmpresa, cdProdutoServico, null);
	}

	public static ResultSetMap getAllByEmpresaProdutoServico(int cdEmpresa, int cdProdutoServico, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();

			String sql = "SELECT A.*, B.*, C.*, " +
						 "	D.cd_referencia, D.nm_referencia, D.id_referencia, " +
						 "	A.cd_unidade_medida AS cd_unidade_medida_componente, " +
						 "	C.nm_produto_servico AS nm_produto_servico_componente, C.txt_produto_servico AS txt_produto_servico_componente, " +
			             "	C.id_produto_servico AS id_produto_servico_componente, " +
			             "  E.nm_unidade_medida, E.sg_unidade_medida " +
		                 "FROM grl_produto_servico_empresa B, grl_produto_servico C, grl_produto_servico_composicao A " +
		                 "LEFT OUTER JOIN alm_produto_referencia D " +
		                 "	ON (A.cd_referencia = D.cd_referencia AND D.st_referencia = 1) " +
		                 "LEFT OUTER JOIN grl_unidade_medida E " +
		                 "	ON (A.cd_unidade_medida = E.cd_unidade_medida) " +
		                 "WHERE (A.cd_produto_servico_componente = B.cd_produto_servico " +
		                 "       AND A.cd_empresa = B.cd_empresa)" +
		                 "  AND (A.cd_produto_servico_componente = C.cd_produto_servico) " +
		                 "  AND A.cd_empresa = ? " +
		                 "  AND A.cd_produto_servico = ? " +
		                 "ORDER BY C.nm_produto_servico";
			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdProdutoServico);

			return new ResultSetMap(pstmt.executeQuery());
			
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoComposicaoServices.getAllByEmpresaProdutoServico: " +  e);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllHierarquia(int cdEmpresa, int cdProdutoServico, boolean isPrimeiroNivel) {
		return getAllHierarquia(cdEmpresa, cdProdutoServico, isPrimeiroNivel, null);
	}
	
	public static ResultSetMap getAllHierarquia(int cdEmpresa, int cdProdutoServico, boolean isPrimeiroNivel, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			String sql = "SELECT A.*, B.*, C.*, " +
					"	D.cd_referencia, D.nm_referencia, D.id_referencia, " +
					"	A.cd_unidade_medida AS cd_unidade_medida_componente, " +
					"	C.nm_produto_servico AS nm_produto_servico_componente, C.txt_produto_servico AS txt_produto_servico_componente, " +
					"	C.id_produto_servico AS id_produto_servico_componente, " +
					"  E.nm_unidade_medida, E.sg_unidade_medida " +
					"FROM grl_produto_servico_empresa B, grl_produto_servico C, grl_produto_servico_composicao A " +
					"LEFT OUTER JOIN alm_produto_referencia D " +
					"	ON (A.cd_referencia = D.cd_referencia AND D.st_referencia = 1) " +
					"LEFT OUTER JOIN grl_unidade_medida E " +
					"	ON (A.cd_unidade_medida = E.cd_unidade_medida) " +
					"WHERE (A.cd_produto_servico_componente = B.cd_produto_servico " +
					"       AND A.cd_empresa = B.cd_empresa)" +
					"  AND (A.cd_produto_servico_componente = C.cd_produto_servico) " +
					"  AND A.cd_empresa = ? " +
					"  AND A.cd_produto_servico = ? " +
					"ORDER BY C.nm_produto_servico";
			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdProdutoServico);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if (!isPrimeiroNivel) {
				while(rsm.next()){
					ResultSetMap subRsm = getAllHierarquia(cdEmpresa, rsm.getInt("CD_PRODUTO_SERVICO"), false, connect);
					rsm.setValueToField("subResultSetMap", subRsm);
				}
			}
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoComposicaoServices.getAllHierarquia: " +  e);
			return  null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		int cdEmpresa = 0;
		String nmComponente = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().toUpperCase().indexOf("CD_EMPRESA") != -1) {
				cdEmpresa = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				i--;
			}
			else if (criterios.get(i).getColumn().toUpperCase().indexOf("NM_COMPONENTE") != -1) {
				nmComponente = (String)(criterios.get(i).getValue());
				criterios.remove(i);
				i--;
			}
		}
		String sql = "SELECT A.cd_produto_servico, A.nm_produto_servico, A.id_produto_servico, A.txt_produto_servico, " +
		   "B.cd_unidade_medida, B.st_produto_empresa, " +
		   "C.nm_pessoa AS nm_fabricante, " +
		   "H.cd_grupo, H. lg_principal, I.nm_grupo, " +
		   "D.cd_referencia " +
		   "FROM grl_produto_servico_empresa B, grl_produto_servico A " +
		   "LEFT OUTER JOIN alm_produto_referencia D ON (A.cd_produto_servico = D.cd_produto_servico AND D.cd_empresa = " + cdEmpresa + ") " +
		   "LEFT OUTER JOIN grl_produto E ON (A.cd_produto_servico = E.cd_produto_servico) " +
		   "LEFT OUTER JOIN alm_produto_grupo H ON (A.cd_produto_servico = H.cd_produto_servico AND H.cd_empresa = " + cdEmpresa +
			       "  AND H.lg_principal = 1 AND ((" +
				   "  EXISTS (SELECT H1.cd_grupo FROM alm_produto_grupo H1 " +
				   "    WHERE H1.cd_produto_servico = A.cd_produto_servico " +
				   "	  AND H1.lg_principal = 1)) " +
				   "	  OR H.cd_grupo = (SELECT MAX(H2.cd_grupo) FROM alm_produto_grupo H2 " +
				   "		WHERE H2.cd_produto_servico = A.cd_produto_servico))) " +
				   "LEFT OUTER JOIN alm_grupo I ON (H.cd_grupo = I.cd_grupo) " +
		   " LEFT OUTER JOIN grl_pessoa C ON (A.cd_fabricante = C.cd_pessoa) " +
	       " WHERE (A.nm_produto_servico LIKE '%" + nmComponente + "%')" +
		   " 	AND (A.cd_produto_servico = B.cd_produto_servico " +
		   "	AND B.cd_empresa = " + cdEmpresa +
		   "    AND B.st_produto_empresa = " + ProdutoServicoEmpresaServices.ST_ATIVO + ") " +

	       " UNION " +

	       "SELECT A.cd_produto_servico, D.nm_referencia AS nm_produto_servico, D.id_referencia AS id_produto_servico, A.txt_produto_servico, " +
		   "B.cd_unidade_medida, B.st_produto_empresa, " +
		   "C.nm_pessoa AS nm_fabricante, " +
		   "H.cd_grupo, H. lg_principal, I.nm_grupo, " +
		   "D.cd_referencia " +
		   "FROM grl_produto_servico_empresa B, alm_produto_referencia D " +
		   "LEFT OUTER JOIN grl_produto_servico A ON (D.cd_produto_servico = A.cd_produto_servico)" +
		   "LEFT OUTER JOIN grl_produto E ON (D.cd_produto_servico = E.cd_produto_servico) " +
		   "LEFT OUTER JOIN alm_produto_grupo H ON (D.cd_produto_servico = H.cd_produto_servico AND H.cd_empresa = " + cdEmpresa +
			       "  AND H.lg_principal = 1 AND ((" +
				   "  EXISTS (SELECT H1.cd_grupo FROM alm_produto_grupo H1 " +
				   "    WHERE H1.cd_produto_servico = D.cd_produto_servico " +
				   "	  AND H1.lg_principal = 1)) " +
				   "	  OR H.cd_grupo = (SELECT MAX(H2.cd_grupo) FROM alm_produto_grupo H2 " +
				   "		WHERE H2.cd_produto_servico = D.cd_produto_servico))) " +
				   "LEFT OUTER JOIN alm_grupo I ON (H.cd_grupo = I.cd_grupo) " +
		   " LEFT OUTER JOIN grl_pessoa C ON (A.cd_fabricante = C.cd_pessoa) " +
	       " WHERE (D.cd_empresa = " + cdEmpresa + ") " +
	       "   AND (D.nm_referencia LIKE '%" + nmComponente + "%')" +
		   "   AND (D.cd_produto_servico = B.cd_produto_servico AND B.cd_empresa = " + cdEmpresa +
		   "   AND B.st_produto_empresa = " + ProdutoServicoEmpresaServices.ST_ATIVO + ") ";

		ResultSetMap rsm = Search.find(sql, "", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		ArrayList<String> criteriosOrder = new ArrayList<String>();
		criteriosOrder.add("NM_PRODUTO_SERVICO");
		rsm.orderBy(criteriosOrder);
		return rsm;
	}

	public static int insert(ProdutoServicoComposicao produtoServicoComposicao, int cdEmpresa){
		return insert(produtoServicoComposicao, cdEmpresa, null);
	}

	public static int insert(ProdutoServicoComposicao produtoServicoComposicao, int cdEmpresa, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			int cdProdutoServico = produtoServicoComposicao.getCdProdutoServico();
			int cdProdutoServicoComponente = produtoServicoComposicao.getCdProdutoServicoComponente();
			int cdUnidadeMedidaOrigem = ProdutoServicoEmpresaDAO.get(cdEmpresa, cdProdutoServicoComponente).getCdUnidadeMedida();
			int cdUnidadeMedidaDestino = produtoServicoComposicao.getCdUnidadeMedida();

			if (cdProdutoServicoComponente <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (cdUnidadeMedidaOrigem != cdUnidadeMedidaDestino) {
				if (UnidadeConversaoServices.getConversaoUnidadeMedida(cdUnidadeMedidaOrigem, cdUnidadeMedidaDestino, connection).size() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -2;
				}
			}

			if (getComponente(cdProdutoServico, cdProdutoServicoComponente, cdEmpresa, connection).size() > 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -3;
			}

			ProdutoServicoEmpresa produtoServicoEmpresa = ProdutoServicoEmpresaDAO.get(cdEmpresa, cdProdutoServicoComponente, connection);
			if (produtoServicoEmpresa == null) {
				produtoServicoEmpresa = new ProdutoServicoEmpresa(cdEmpresa,
						cdProdutoServicoComponente,
						produtoServicoComposicao.getCdUnidadeMedida(),
						null, // idReduzido
						0, // vlPrecoMedio
						0, // vlCustoMedio
						0, // vlUltimoCusto
						0, // qtIdeal
						0, // qtMinima
						0, // qtMaxima
						0, // qtDiasEstoque
						0, // qtPrecisaoCusto
						0, // qtPrecisaoUnidade
						0, // qtDiasGarantia
						ProdutoServicoEmpresaServices.TP_MANUAL, // tpReabastecimento
						ProdutoServicoEmpresaServices.CTL_QUANTIDADE, // tpControleEstoque
						0, // tpTransporte
						ProdutoServicoEmpresaServices.ST_ATIVO, // stProdutoEmpresa
						null, // dtDesativacao
						null, // nrOrdem
						0 // lgEstoqueNegativo
				);
				if (ProdutoServicoEmpresaDAO.insert(produtoServicoEmpresa, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			if (ProdutoServicoComposicaoDAO.insert(produtoServicoComposicao, connection) <= 0) {
				if (isConnectionNull) {
					Conexao.rollback(connection);
				}
				return -1;
			}

			if (isConnectionNull)
				connection.commit();
			return cdProdutoServico;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int update(ProdutoServicoComposicao produtoServicoComposicao, int cdEmpresa){
		return update(produtoServicoComposicao, cdEmpresa, null);
	}

	public static int update(ProdutoServicoComposicao produtoServicoComposicao, int cdEmpresa, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			int cdProdutoServico = produtoServicoComposicao.getCdProdutoServicoComponente();
			if (cdProdutoServico <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			ProdutoServicoEmpresa produtoServicoEmpresa = ProdutoServicoEmpresaDAO.get(cdEmpresa, cdProdutoServico, connection);
			int cdProdutoServicoComponente = produtoServicoComposicao.getCdProdutoServicoComponente();
			int cdUnidadeMedidaOrigem = ProdutoServicoEmpresaDAO.get(cdEmpresa, cdProdutoServicoComponente).getCdUnidadeMedida();
			int cdUnidadeMedidaDestino = produtoServicoComposicao.getCdUnidadeMedida();

			if (cdUnidadeMedidaOrigem != cdUnidadeMedidaDestino) {
				if (UnidadeConversaoServices.getConversaoUnidadeMedida(cdUnidadeMedidaOrigem, cdUnidadeMedidaDestino, connection).size() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -2;
				}
			}

			if (produtoServicoEmpresa == null) {
				produtoServicoEmpresa = new ProdutoServicoEmpresa(cdEmpresa,
						cdProdutoServico,
						produtoServicoComposicao.getCdUnidadeMedida(),
						null, // idReduzido
						0, // vlPrecoMedio
						0, // vlCustoMedio
						0, // vlUltimoCusto
						0, // qtIdeal
						0, // qtMinima
						0, // qtMaxima
						0, // qtDiasEstoque
						0, // qtPrecisaoCusto
						0, // qtPrecisaoUnidade
						0, // qtDiasGarantia
						ProdutoServicoEmpresaServices.TP_MANUAL, // tpReabastecimento
						ProdutoServicoEmpresaServices.CTL_QUANTIDADE, // tpControleEstoque
						0, // tpTransporte
						ProdutoServicoEmpresaServices.ST_ATIVO, // stProdutoEmpresa
						null, // dtDesativacao
						null, // nrOrdem
						0 // lgEstoqueNegativo
				);
				if (ProdutoServicoEmpresaDAO.insert(produtoServicoEmpresa, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return -1;
				}
			}

			if (ProdutoServicoComposicaoDAO.update(produtoServicoComposicao, connection) <= 0) {
				if (isConnectionNull) {
					Conexao.rollback(connection);
				}
				return -1;
			}

			if (isConnectionNull)
				connection.commit();
			return cdProdutoServico;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getComponente(int cdProdutoServico, int cdProdutoServicoComponente, int cdEmpresa) {
		return getComponente(cdProdutoServico, cdProdutoServicoComponente, cdEmpresa, null);
	}

	public static ResultSetMap getComponente(int cdProdutoServico, int cdProdutoServicoComponente, int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.*, B.*, C.*, " +
															   "	D.cd_referencia, D.nm_referencia, D.id_referencia, " +
															   "	A.cd_unidade_medida AS cd_unidade_medida_componente, " +
															   "	C.nm_produto_servico AS nm_produto_servico_componente, C.txt_produto_servico AS txt_produto_servico_componente, " +
															   "	C.id_produto_servico AS id_produto_servico_componente, " +
															   "    E.nm_unidade_medida, E.sg_unidade_medida " +
															   "FROM grl_produto_servico_empresa B, grl_produto_servico C, grl_produto_servico_composicao A " + 
															   "LEFT OUTER JOIN alm_produto_referencia D " +
															   "	ON (A.cd_referencia = D.cd_referencia AND D.st_referencia = 1) " +
															   "LEFT OUTER JOIN grl_unidade_medida E " +
															   "	ON (A.cd_unidade_medida = E.cd_unidade_medida) " +
															   "WHERE (A.cd_produto_servico_componente = B.cd_produto_servico " +
															   "       AND A.cd_empresa = B.cd_empresa)" +
															   "  AND (A.cd_produto_servico_componente = C.cd_produto_servico) " +
															   "  AND A.cd_produto_servico = ? " +
															   "  AND A.cd_produto_servico_componente = ? " +
															   "  AND A.cd_empresa = ? " +
															   "ORDER BY C.nm_produto_servico");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.setInt(2, cdProdutoServicoComponente);
			pstmt.setInt(3, cdEmpresa);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoComposicaoServices.getComponente: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
