package com.tivic.manager.adm;

import java.sql.Connection;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ProdutoServico;
import com.tivic.manager.grl.ProdutoServicoDAO;
import com.tivic.manager.grl.ProdutoServicoEmpresa;
import com.tivic.manager.grl.ProdutoServicoEmpresaDAO;
import com.tivic.manager.grl.UnidadeConversaoServices;


public class OrdemCompraItemServices {

	public static int ERR_UNIDADE_MEDIDA_CONVERSAO = -2;

	
	public static Result save(OrdemCompraItem ordemCompraItem){
		return save(ordemCompraItem, null);
	}

	public static Result save(OrdemCompraItem ordemCompraItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(ordemCompraItem==null)
				return new Result(-1, "Erro ao salvar. OrdemCompraItem é nulo");

			
			ProdutoServicoEmpresa produtoServico = ProdutoServicoEmpresaDAO.get(ordemCompraItem.getCdEmpresa(), ordemCompraItem.getCdProdutoServico(), connect);
			ordemCompraItem.setCdUnidadeMedida(produtoServico.getCdUnidadeMedida());
			
			int retorno;
			if(OrdemCompraItemDAO.get(ordemCompraItem.getCdOrdemCompra(), ordemCompraItem.getCdEmpresa(), ordemCompraItem.getCdProdutoServico())==null){
				retorno = OrdemCompraItemDAO.insert(ordemCompraItem, connect);
				ordemCompraItem.setCdOrdemCompra(retorno);
			}
			else {
				retorno = OrdemCompraItemDAO.update(ordemCompraItem, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ORDEMCOMPRAITEM", ordemCompraItem);
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int insert(OrdemCompraItem objeto){
		return insert(objeto, null);
	}

	public static int insert(OrdemCompraItem objeto, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			int cdEmpresa              = objeto.getCdEmpresa();
			int cdProdutoServico       = objeto.getCdProdutoServico();
			int cdUnidadeMedidaOrigem  = ProdutoServicoEmpresaDAO.get(cdEmpresa, cdProdutoServico).getCdUnidadeMedida();
			int cdUnidadeMedidaDestino = objeto.getCdUnidadeMedida();

			if (cdUnidadeMedidaOrigem != cdUnidadeMedidaDestino) {
				if (UnidadeConversaoServices.getConversaoUnidadeMedida(cdUnidadeMedidaOrigem, cdUnidadeMedidaDestino, connection).size() <= 0) {
					if (isConnectionNull)
						Conexao.desconectar(connection);
					return ERR_UNIDADE_MEDIDA_CONVERSAO;
				}
			}

			if (OrdemCompraItemDAO.insert(objeto, connection) <= 0) {
				if (isConnectionNull)
					Conexao.desconectar(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();
			
			return 1;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.desconectar(connection);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int update(OrdemCompraItem objeto){
		return update(objeto, null);
	}

	public static int update(OrdemCompraItem objeto, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			int cdEmpresa = objeto.getCdEmpresa();
			int cdProdutoServico = objeto.getCdProdutoServico();
			int cdUnidadeMedidaOrigem = ProdutoServicoEmpresaDAO.get(cdEmpresa, cdProdutoServico).getCdUnidadeMedida();
			int cdUnidadeMedidaDestino = objeto.getCdUnidadeMedida();

			if (cdUnidadeMedidaOrigem != cdUnidadeMedidaDestino) {
				if (UnidadeConversaoServices.getConversaoUnidadeMedida(cdUnidadeMedidaOrigem, cdUnidadeMedidaDestino, connection).size() <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return ERR_UNIDADE_MEDIDA_CONVERSAO;
				}
			}

			if (OrdemCompraItemDAO.update(objeto, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();
			return 1;
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
	
	public static Result remove(int cdOrdemCompra, int cdEmpresa, int cdProdutoServico){
		return remove(cdOrdemCompra, cdEmpresa, cdProdutoServico, false, null);
	}
	public static Result remove(int cdOrdemCompra, int cdEmpresa, int cdProdutoServico, boolean cascade){
		return remove(cdOrdemCompra, cdEmpresa, cdProdutoServico, cascade, null);
	}
	public static Result remove(int cdOrdemCompra, int cdEmpresa, int cdProdutoServico, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
			retorno = OrdemCompraItemDAO.delete(cdOrdemCompra, cdEmpresa, cdProdutoServico, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findProdutosOfEmpresaFornecedor(ArrayList<ItemComparator> criterios ) {
		Connection connect = Conexao.conectar();
		try	{
			
			int cdEmpresa = 0;
			int cdFornecedor = 0;
			int cdLocalArmazenamento = 0;
			
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for (int i = 0; criterios != null && i < criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("cd_fornecedor"))
					cdFornecedor = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().toLowerCase().indexOf("cd_empresa") >= 0)
					cdEmpresa = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().toLowerCase().indexOf("cd_local_armazenamento") >= 0)
					cdLocalArmazenamento = Integer.parseInt(criterios.get(i).getValue());
				else
					crt.add(criterios.get(i));
			}
			
			String sql = "SELECT A.*, OC.NR_ORDEM_COMPRA, " + 
						 "  A.QT_COMPRADA - (SELECT COALESCE(SUM(sA.qt_recebida), 0) FROM adm_compra_entrada_item sA WHERE sA.cd_ordem_compra = OC.cd_ordem_compra AND sA.cd_produto_servico = P.cd_produto_servico) as QT_RESTANTE, "
						 + " P.nm_produto_servico, PE.cd_unidade_medida, UM.sg_unidade_medida FROM adm_ordem_compra_item A " +
						 "  JOIN grl_produto_servico P ON (A.cd_produto_servico = P.cd_produto_servico) " +
						 "  JOIN grl_produto_servico_empresa PE ON (A.cd_produto_servico = PE.cd_produto_servico"+ 
						 "											AND A.cd_empresa = PE.cd_empresa) " +
						 "  LEFT OUTER JOIN grl_unidade_medida UM ON (PE.cd_unidade_medida = UM.cd_unidade_medida) " +
						 "  JOIN adm_ordem_compra OC ON (A.cd_ordem_compra = OC.cd_ordem_compra) "+ 
						 "  WHERE A.cd_empresa = " + cdEmpresa+ 
						 "    AND OC.cd_fornecedor = " + cdFornecedor +
						 "    AND PE.cd_local_armazenamento = " + cdLocalArmazenamento + 
						 "    AND OC.st_ordem_compra IN ("+OrdemCompraServices.ST_LIBERADA+", "+OrdemCompraServices.ST_PENDENTE+")";
			
			ResultSetMap rsm = Search.findAndLog(sql," ORDER BY P.nm_produto_servico", crt, connect!=null ? connect : Conexao.conectar(), connect==null);
			return rsm;
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return null;
		}
	}

}