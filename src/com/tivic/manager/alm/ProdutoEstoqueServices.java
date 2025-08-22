package com.tivic.manager.alm;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.manager.adm.ContaFechamentoDAO;
import com.tivic.manager.adm.ProdutoServicoPrecoDAO;
import com.tivic.manager.adm.TipoOperacao;
import com.tivic.manager.adm.TipoOperacaoDAO;
import com.tivic.manager.adm.TributoAliquotaServices;
import com.tivic.manager.adm.TributoServices;
import com.tivic.manager.adm.TurnoServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoDAO;
import com.tivic.manager.grl.ProdutoServicoEmpresa;
import com.tivic.manager.grl.ProdutoServicoEmpresaDAO;
import com.tivic.manager.grl.ProdutoServicoEmpresaServices;
import com.tivic.manager.pcb.MedicaoFisicaDAO;
import com.tivic.manager.pcb.TanqueServices;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.DateServices;
import sol.util.Result;

public class ProdutoEstoqueServices {

	public static final int ST_ESTOQUE_ABAIXO_PP = 0;
	public static final int ST_ESTOQUE_ACIMA_PP  = 1;

	public static final int TP_CLASSE_A = 0;
	public static final int TP_CLASSE_B = 1;
	public static final int TP_CLASSE_C = 2;

	public static String[] tiposPeriodosVendas = {"Últimos 30 dias", "Últimos 60 dias", "Últimos 90 dias", "Último semetre", "Último ano",
		                                          "Data de ínicio a informar"};

	public static final int PER_VEND_30_DIAS 	  = 0;
	public static final int PER_VEND_60_DIAS 	  = 1;
	public static final int PER_VEND_90_DIAS 	  = 2;
	public static final int PER_VEND_SEMESTRE 	  = 3;
	public static final int PER_VEND_ANO          = 4;
	public static final int PER_VEND_CUSTOMIZAVEL = 5;

	public static final int MOV_ENTRADA = 0;
	public static final int MOV_SAIDA   = 1;
	public static final int MOV_TODOS   = -1;

	public static final int CONSIG_ENTRADA = 0;
	public static final int CONSIG_SAIDA   = 1;

	public static final int ST_DESATIVADO = 0;
	public static final int ST_ATIVADO    = 1;
	
	public static final String[] tipoSituacao = {"Desativado", "Ativado"};

	public static Result save(ProdutoEstoque produtoEstoque){
		return save(produtoEstoque, null);
	}

	public static Result save(ProdutoEstoque produtoEstoque, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(produtoEstoque==null)
				return new Result(-1, "Erro ao salvar. ProdutoEstoque é nulo");

			int retorno;
			if(produtoEstoque.getCdLocalArmazenamento()==0){
				retorno = ProdutoEstoqueDAO.insert(produtoEstoque, connect);
				produtoEstoque.setCdLocalArmazenamento(retorno);
			}
			else {
				retorno = update(produtoEstoque, false, connect).getCode();
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PRODUTOESTOQUE", produtoEstoque);
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
	public static Result remove(int cdLocalArmazenamento, int cdProdutoServico, int cdEmpresa){
		return remove(cdLocalArmazenamento, cdProdutoServico, cdEmpresa, false, null);
	}
	public static Result remove(int cdLocalArmazenamento, int cdProdutoServico, int cdEmpresa, boolean cascade){
		return remove(cdLocalArmazenamento, cdProdutoServico, cdEmpresa, cascade, null);
	}
	public static Result remove(int cdLocalArmazenamento, int cdProdutoServico, int cdEmpresa, boolean cascade, Connection connect){
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
			retorno = ProdutoEstoqueDAO.delete(cdLocalArmazenamento, cdProdutoServico, cdEmpresa, connect);
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
	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_produto_estoque");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoEstoqueServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoEstoqueServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findSaldoConsignacao(int cdEmpresa, int cdLocalArmazenamento, ArrayList<ItemComparator> criterios) {
		return findSaldoConsignacao(cdEmpresa, cdLocalArmazenamento, CONSIG_ENTRADA, criterios, null);
	}

	public static ResultSetMap findSaldoConsignacao(int cdEmpresa, int cdLocalArmazenamento, int tpConsignacao, ArrayList<ItemComparator> criterios) {
		return findSaldoConsignacao(cdEmpresa, cdLocalArmazenamento, tpConsignacao, criterios, null);
	}

	public static ResultSetMap findSaldoConsignacao(int cdEmpresa, int cdLocalArmazenamento, int tpConsignacao, ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		boolean lgSaldoPositivo = false;
		int cdFornecedor = 0;
		int cdCliente = 0;
		int stProdutoEmpresa = 0;
		int cdFabricante = 0;
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("lgSaldoPositivo")) {
				lgSaldoPositivo = true;
				criterios.remove(i);
				i--;
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("cdCliente")) {
				cdCliente = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				i--;
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("cdFornecedor")) {
				cdFornecedor = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				i--;
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("stProdutoEmpresa")) {
				stProdutoEmpresa = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				i--;
			}
		}
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			boolean lgContabilizacaoAutomatica = ParametroServices.getValorOfParametroAsInteger("LG_CONTABILIZACAO_ESTOQUE", 0, cdEmpresa, connect)==1;
			
			ResultSetMap rsm = new ResultSetMap();
			if(lgContabilizacaoAutomatica){
				if(tpConsignacao == CONSIG_ENTRADA){
					rsm = Search.find(
							   "SELECT SUM(qt_entrada_consignada) AS QT_ENTRADA_CONSIGNADA, A.cd_produto_servico, C.nm_produto_servico, B.cd_fornecedor FROM alm_entrada_local_item A " +
							   "JOIN alm_documento_entrada B ON (A.cd_documento_entrada = B.cd_documento_entrada) " +
							   "JOIN grl_produto_servico C ON (A.cd_produto_servico = C.cd_produto_servico) " +
							   "JOIN grl_produto_servico_empresa D ON (D.cd_produto_servico = C.cd_produto_servico" +
							   "										AND D.cd_empresa= "+cdEmpresa+") " +
							   "WHERE B.cd_empresa = " + cdEmpresa + 
							   "  AND (B.tp_entrada = "+DocumentoEntradaServices.ENT_CONSIGNACAO+
							   "       OR B.tp_entrada = "+DocumentoEntradaServices.ENT_TRANSFERENCIA+ 
							   "       OR B.tp_entrada = "+DocumentoEntradaServices.ENT_DEVOLUCAO+") " +
							   (stProdutoEmpresa > 0 ? " AND D.st_produto_empresa = 1 " : "") +
							   (cdFabricante > 0 ? " AND C.cd_fabricante = " + cdFabricante : "") +
							   (cdFornecedor > 0 ? " AND B.cd_fornecedor = " + cdFornecedor : "") +
							   "GROUP BY A.cd_produto_servico, C.nm_produto_servico, B.cd_fornecedor " +
							   "ORDER BY A.cd_produto_servico, B.cd_fornecedor", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
					
					ResultSetMap rsm2 = new ResultSetMap(connect.prepareStatement("SELECT SUM(qt_saida_consignada) AS QT_SAIDA_CONSIGNADA, A.cd_produto_servico, C.nm_produto_servico, B.cd_cliente FROM alm_saida_local_item A " +
							   "JOIN alm_documento_saida B ON (A.cd_documento_saida = B.cd_documento_saida) " +
							   "JOIN grl_produto_servico C ON (A.cd_produto_servico = C.cd_produto_servico) " +
							   "WHERE B.cd_empresa = " + cdEmpresa +
							   "  AND (B.tp_saida = "+DocumentoSaidaServices.SAI_ACERTO_CONSIGNACAO+")" +
							   "GROUP BY A.cd_produto_servico, C.nm_produto_servico, B.cd_cliente " +
							   "ORDER BY A.cd_produto_servico, B.cd_cliente").executeQuery());
							   
					while(rsm.next()){
						Pessoa fornecedor = PessoaDAO.get(rsm.getInt("cd_fornecedor"));
						rsm.setValueToField("NM_FORNECEDOR", fornecedor.getNmPessoa());
						boolean entrou = false;
						while(rsm2.next()){
							if(rsm.getInt("cd_produto_servico") == rsm2.getInt("cd_produto_servico") && rsm.getInt("cd_fornecedor") == rsm2.getInt("cd_cliente")){
								rsm.setValueToField("QT_SALDO_CONSIGNADO", (rsm.getFloat("QT_ENTRADA_CONSIGNADA") - rsm2.getFloat("QT_SAIDA_CONSIGNADA")));
								entrou = true;
								break;
							}
						}
						if(!entrou)
							rsm.setValueToField("QT_SALDO_CONSIGNADO", rsm.getFloat("QT_ENTRADA_CONSIGNADA"));
						rsm2.beforeFirst();
					}
					rsm.beforeFirst();
					
					if (lgSaldoPositivo && rsm != null) {
						for (int i=0; i<rsm.getLines().size(); i++) {
							rsm.goTo(i);
							if (rsm.getFloat("QT_SALDO_CONSIGNADO") <= 0) {
								rsm.getLines().remove(i);
								i--;
							}
						}
					}
	
				}
				else if(tpConsignacao == CONSIG_SAIDA){
					rsm = Search.find("SELECT SUM(qt_saida_consignada) AS QT_SAIDA_CONSIGNADA, A.cd_produto_servico, C.nm_produto_servico, B.cd_cliente, " +
							   "SI.qt_saida, SI.vl_unitario, SI.vl_acrescimo, SI.vl_desconto, C.id_produto_servico, D.id_reduzido, UM.sg_unidade_medida "+	
							   "FROM alm_saida_local_item A " +
							   "JOIN alm_documento_saida 		 			B ON (A.cd_documento_saida = B.cd_documento_saida) " +							   
							   "JOIN grl_produto_servico 		 			C ON (A.cd_produto_servico = C.cd_produto_servico) " +
							   "JOIN grl_produto_servico_empresa 			D ON (D.cd_produto_servico = C.cd_produto_servico  " +
							   "									   			  AND D.cd_empresa="+cdEmpresa+") " +
							   "LEFT OUTER JOIN alm_documento_saida_item SI ON (B.cd_documento_saida = SI.cd_documento_saida " +
							   "												AND A.cd_produto_servico = SI.cd_produto_servico) " + 
							   "LEFT OUTER JOIN grl_unidade_medida       UM ON (D.cd_unidade_medida = UM.cd_unidade_medida) "+
							   "WHERE B.cd_empresa = " + cdEmpresa +
							   "  AND (B.tp_saida  = "+DocumentoSaidaServices.SAI_VENDA_CONSIGNACAO+")" +
							   (stProdutoEmpresa > 0 ? " AND D.st_produto_empresa = " + ProdutoServicoEmpresaServices.ST_ATIVO : "") +
							   (cdFabricante > 0 ?     " AND C.cd_fabricante      = " + cdFabricante : "") +
							   (cdCliente > 0 ?        " AND B.cd_cliente         = " + cdCliente : "") +
							   "GROUP BY A.cd_produto_servico, C.nm_produto_servico, B.cd_cliente, SI.qt_saida, SI.vl_unitario, " +
							   "SI.vl_acrescimo, SI.vl_desconto, C.id_produto_servico, D.id_reduzido, UM.sg_unidade_medida " +
							   "ORDER BY A.cd_produto_servico, B.cd_cliente", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
					
					ResultSetMap rsm2 = new ResultSetMap(connect.prepareStatement("SELECT SUM(qt_entrada_consignada) AS QT_ENTRADA_CONSIGNADA, A.cd_produto_servico, C.nm_produto_servico, B.cd_fornecedor FROM alm_entrada_local_item A " +
							   "JOIN alm_documento_entrada B ON (A.cd_documento_entrada = B.cd_documento_entrada) " +
							   "JOIN grl_produto_servico C ON (A.cd_produto_servico = C.cd_produto_servico) " +
							   "WHERE B.cd_empresa  = " + cdEmpresa + 
							   "  AND (B.tp_entrada = "+DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO+") " +
							   "GROUP BY A.cd_produto_servico, C.nm_produto_servico, B.cd_fornecedor " +
							   "ORDER BY A.cd_produto_servico, B.cd_fornecedor").executeQuery());
					while(rsm.next()){
						Pessoa cliente = PessoaDAO.get(rsm.getInt("cd_cliente"));
						rsm.setValueToField("NM_CLIENTE", cliente.getNmPessoa());
						boolean entrou = false;
						while(rsm2.next()){
							if(rsm.getInt("cd_produto_servico") == rsm2.getInt("cd_produto_servico") && rsm.getInt("cd_cliente") == rsm2.getInt("cd_fornecedor")){
								rsm.setValueToField("QT_SALDO_CONSIGNADO", (rsm.getFloat("QT_SAIDA_CONSIGNADA") - rsm2.getFloat("QT_ENTRADA_CONSIGNADA")));
								entrou = true;
								break;
							}
						}
						if(!entrou)
							rsm.setValueToField("QT_SALDO_CONSIGNADO", rsm.getFloat("QT_SAIDA_CONSIGNADA"));
						rsm2.beforeFirst();
						rsm2.beforeFirst();
					}
					rsm.beforeFirst();
					
					if (lgSaldoPositivo && rsm != null) {
						for (int i=0; i<rsm.getLines().size(); i++) {
							rsm.goTo(i);
							if (rsm.getFloat("QT_SALDO_CONSIGNADO") <= 0) {
								rsm.getLines().remove(i);
								i--;
							}
						}
					}
	
				}
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			Util.registerLog(e);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByArmazenamento(int cdEmpresa, int cdLocalArmazenamento) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		return findProduto(cdEmpresa, cdLocalArmazenamento, null, criterios, null, null, null);
	}
	
	public static ResultSetMap findProduto(ArrayList<ItemComparator> criterios) {
		int cdEmpresa = 0;
		int cdLocalArmazenamento = 0;

		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("CD_EMPRESA"))
				cdEmpresa = Integer.parseInt(criterios.get(i).getValue());
			else if (criterios.get(i).getColumn().equalsIgnoreCase("CD_LOCAL_ARMAZENAMENTO"))
				cdLocalArmazenamento = Integer.parseInt(criterios.get(i).getValue());
			else
				crt.add(criterios.get(i));				
		}
				
		return findProduto(cdEmpresa, cdLocalArmazenamento, null, crt, null, null, null);
	}

	public static ResultSetMap findProduto(int cdEmpresa, int cdLocalArmazenamento, ArrayList<ItemComparator> criterios) {
		return findProduto(cdEmpresa, cdLocalArmazenamento, null, criterios, null, null, null);
	}

	public static ResultSetMap findProduto(int cdEmpresa, ArrayList<ItemComparator> criterios) {
 		return findProduto(cdEmpresa, 0, null, criterios, null, null, null);
	}
	
	public static ResultSetMap findProduto(int cdEmpresa, String cdProduto, ArrayList<ItemComparator> criterios) {
 		String newCdProdutos[]         = cdProduto.split(";");
		int[]  cdProdutosServicos = new int[newCdProdutos.length];
		for (int i = 0; i < newCdProdutos.length; i++){
			cdProdutosServicos[i] = Integer.parseInt(newCdProdutos[i]);
		}
		return findProduto(cdEmpresa, 0, cdProdutosServicos, criterios, null, null, null);
	}
	
	public static ResultSetMap findProduto(int cdEmpresa, ArrayList<ItemComparator> criterios, ArrayList<String> orderByFields) {
		return findProduto(cdEmpresa, 0, null, criterios, orderByFields, null, null);
	}

	public static ResultSetMap findProduto(int cdEmpresa, ArrayList<ItemComparator> criterios, ArrayList<String> orderByFields,
			HashMap<String, Object> options) {
		return findProduto(cdEmpresa, 0, null, criterios, orderByFields, options, null);
	}

	public static ResultSetMap findProduto(int cdEmpresa, int cdLocalArmazenamento, int[] cdProdutosServicos,
			ArrayList<ItemComparator> criterios) {
		return findProduto(cdEmpresa, cdLocalArmazenamento, cdProdutosServicos, criterios, null, null, null);
	}

	public static ResultSetMap findProduto(int cdEmpresa, int cdLocalArmazenamento, int[] cdProdutosServicos,
			ArrayList<ItemComparator> criterios, Connection connect) {
		return findProduto(cdEmpresa, cdLocalArmazenamento, cdProdutosServicos, criterios, null, null, connect);
	}

	public static ResultSetMap findProduto(int cdEmpresa, int cdLocalArmazenamento, int[] cdProdutosServicos,
			ArrayList<ItemComparator> criterios, ArrayList<String> orderByFields) {
		return findProduto(cdEmpresa, cdLocalArmazenamento, cdProdutosServicos, criterios, orderByFields, null, null);
	}

	public static ResultSetMap findProduto(int cdEmpresa, int cdLocalArmazenamento, int[] cdProdutosServicos,
			ArrayList<ItemComparator> criterios, ArrayList<String> orderByFields, HashMap<String, Object> options, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			
			LogUtils.debug("ProdutoEstoqueService.findProduto");
			LogUtils.createTimer("ESTOQUE_FIND_TIMER");
			
			connect = isConnectionNull ? Conexao.conectar() : connect;
			/* consulta a configuracao de contabilizacao de estoque (manual ou automatica) */
			boolean notEstoqueNegativoOrZero = false;
			boolean lgContabAut              = ParametroServices.getValorOfParametroAsInteger("LG_CONTABILIZACAO_ESTOQUE", 0, cdEmpresa, connect)==1;
			int stPreco       = -1;
			int cdTabelaPreco = 0;
			int lgUltimoCusto = -1;
			int cdFornecedor  = 0;
			int lgEstoque     = -1;
			int tpEstoque     = -1;
			int qtLimite      = 500; // Adiciona um valor limite padrão, caso não tenha sido especificado ele especifica 100 para não travar a consulta
			int cdEmpresaNotCadastro = 0;
			String specialOrder = null;
			boolean lgFiscal = false;
			GregorianCalendar dtMovimento = Util.getDataAtual();
			int offSet = 0;
			boolean semEstoque = false;
			
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("lgUltimoCusto"))
					lgUltimoCusto = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdEmpresaNotCadastro"))
					cdEmpresaNotCadastro = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("tpEstoque")) 
					tpEstoque = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgEstoque")) 
					lgEstoque = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdFornecedor")) 
					cdFornecedor = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("stPreco")) 
					stPreco = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdTabelaPreco")) 
					cdTabelaPreco = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("notEstoqueNegativoOrZero"))
					notEstoqueNegativoOrZero = true;
				else if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) 
					qtLimite = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("lgFiscal"))
					lgFiscal = Boolean.parseBoolean(criterios.get(i).getValue());
//				else if (criterios.get(i).getColumn().equalsIgnoreCase("cdGrupo")) 
//					cdGrupo = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("specialOrder")) 
					specialOrder = criterios.get(i).getValue();
				else if( criterios.get(i).getColumn().equalsIgnoreCase("dtMovimento")) {
					dtMovimento = Util.convStringToCalendar(criterios.get(i).getValue());
					dtMovimento.set(Calendar.HOUR_OF_DAY, 23);
					dtMovimento.set(Calendar.MINUTE, 59);
					dtMovimento.set(Calendar.SECOND, 59);
					dtMovimento.set(Calendar.MILLISECOND, 0);
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("offSet")) 
					offSet = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("semEstoque"))
					semEstoque = Boolean.parseBoolean(criterios.get(i).getValue());
				else
					crt.add(criterios.get(i));
			}
			
			boolean noEstoque = options!=null && options.get("noEstoque")!=null && ((Boolean)options.get("noEstoque")).booleanValue()==true;
			if(semEstoque)
				noEstoque = true;
			String fields     = options!=null && options.get("fields")!=null ? options.get("fields").toString() : null;
			/* RELATIONS */
			String relations = "";
			for (int i=0; crt!=null && i<crt.size(); i++)
				relations += (i>0 ? " AND " : "") + (i+1);
			if (cdProdutosServicos!=null && cdProdutosServicos.length>0) {
				int countCriteriosOld = crt.size();
				String relationsTemp = "";
				for (int i=0; i<cdProdutosServicos.length; i++) {
					relationsTemp += (i>0 ? " OR " : "") + (countCriteriosOld+i+1);
					crt.add(new ItemComparator("A.cd_produto_servico", Integer.toString(cdProdutosServicos[i]), ItemComparator.EQUAL, java.sql.Types.INTEGER));
				}
				relations += (relations.equals("") ? "" : " AND ") + "(" + relationsTemp + ")";
			}

			// SQL	
			String[] limitSkip = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, offSet);
			String sql = "SELECT " + (fields!=null ? fields : 
				                      limitSkip[0]+" A.cd_produto_servico, A.nm_produto_servico, A.cd_fabricante, txt_especificacao, txt_dado_tecnico, B.tp_origem, " +
					                  " A.id_produto_servico, F.sg_unidade_medida, A.nr_referencia,B.qt_ideal AS qt_ideal_estoque, B.qt_minima AS qt_minima_estoque, B.pr_desconto_maximo, CASE B.tp_controle_estoque WHEN 0 THEN 'Q' WHEN 1 THEN 'L' WHEN 2 THEN 'I' END as tp_controle_estoque, " + 
					                  (lgContabAut ? "CAST(0 AS FLOAT) AS qt_estoque, CAST(0 AS FLOAT) AS qt_estoque_consignado, " : 
					                	             "C.qt_estoque, C.qt_estoque_consignado, ") + 
						 " C.qt_ideal, C.qt_minima, C.qt_maxima, C.qt_dias_estoque, C.qt_minima_ecommerce, F.cd_unidade_medida, I.cd_grupo, I.cd_grupo_superior, I.nm_grupo, I.id_grupo, J.cd_classificacao_fiscal, J.id_classificacao_fiscal, J.nm_classificacao_fiscal," +
						 " D.nm_pessoa AS nm_fabricante, D.nm_apelido AS sg_fabricante, B.vl_preco_medio, B.vl_custo_medio, B.vl_ultimo_custo, B.id_reduzido, B.st_produto_empresa, N.nr_ncm " +
						 (cdTabelaPreco>0?", G.vl_preco ":" ")) + 
						 "FROM grl_produto_servico A " +
						 "JOIN grl_produto                            E ON (A.cd_produto_servico = E.cd_produto_servico) " +
						 "LEFT OUTER JOIN grl_produto_servico_empresa B ON (E.cd_produto_servico = B.cd_produto_servico " +
						 "										        AND B.cd_empresa = "+cdEmpresa+") " +
						 "LEFT OUTER JOIN alm_produto_estoque         C ON (B.cd_produto_servico = C.cd_produto_servico " +
						 "										        AND B.cd_empresa = C.cd_empresa " +
						 "										        AND C.cd_local_armazenamento = " + cdLocalArmazenamento + ") " +
						 "LEFT OUTER JOIN grl_pessoa                  D ON (A.cd_fabricante = D.cd_pessoa) " +
						 "LEFT OUTER JOIN grl_unidade_medida          F ON (B.cd_unidade_medida = F.cd_unidade_medida) " +
						 "LEFT OUTER JOIN grl_ncm                     N ON (A.cd_ncm = N.cd_ncm) " +
						 /* RETORNAR PREÇO */
						 (cdTabelaPreco>0?"LEFT OUTER JOIN adm_produto_servico_preco G ON (A.cd_produto_servico = G.cd_produto_servico" +
						                  "                                            AND G.dt_termino_validade IS NULL " +
						                  "                                            AND G.cd_tabela_preco = "+cdTabelaPreco+") ":"") +
						 "LEFT OUTER JOIN alm_produto_grupo           H ON (B.cd_produto_servico = H.cd_produto_servico " +
						 "									            AND B.cd_empresa         = H.cd_empresa " +
//						 (cdGrupo > 0 ? " AND H.cd_grupo = " + cdGrupo : "") +
						 "									            AND H.lg_principal       = 1) "+
						 "LEFT OUTER JOIN alm_grupo                   I ON (H.cd_grupo = I.cd_grupo) "+						 
						 "LEFT OUTER JOIN adm_classificacao_fiscal    J ON (A.cd_classificacao_fiscal = J.cd_classificacao_fiscal) " +
						 "WHERE 1=1 " +						 
						 (cdEmpresaNotCadastro>0 ? "  AND NOT EXISTS (SELECT cd_empresa FROM grl_produto_servico_empresa " +
						                           "				  WHERE cd_produto_servico = A.cd_produto_servico " +
						                           "					AND cd_empresa = " + cdEmpresaNotCadastro + ") " : "") +
						 // Filtro por fornecedor
						 (cdFornecedor>0 ? "  AND EXISTS (SELECT * FROM alm_documento_entrada A1, alm_documento_entrada_item B1 " +
					 					   "			  WHERE B1.cd_produto_servico   = A.cd_produto_servico" +
					 					   "                AND B1.cd_documento_entrada = A1.cd_documento_entrada " +
			 							   "			    AND B1.cd_empresa           = " +cdEmpresa+
				 						   "			    AND A1.cd_empresa           = " +cdEmpresa+
		 								   "			    AND A1.st_documento_entrada = " +DocumentoEntradaServices.ST_LIBERADO+
	 									   " 		        AND A1.tp_entrada IN ("+DocumentoEntradaServices.ENT_COMPRA+ ","+DocumentoEntradaServices.ENT_CONSIGNACAO+")" +
										   "                AND A1.cd_fornecedor        = "+cdFornecedor+")" : "") +
						 // Ultimo Custo
						 (lgUltimoCusto!=-1 ? (lgUltimoCusto==0 ? " AND B.vl_ultimo_custo IS NOT NULL AND B.vl_ultimo_custo > 0 " :
							                                     " AND (B.vl_ultimo_custo IS NULL OR B.vl_ultimo_custo = 0) ") : "") +
						 (stPreco!=-1 ? " AND" + (stPreco==1 ? " NOT" : "") + " EXISTS (SELECT * FROM adm_produto_servico_preco G " +
						                                                      "		    WHERE G.cd_produto_servico = A.cd_produto_servico " +
						                                                      "			  AND G.dt_termino_validade IS NULL " +						 
						 (cdTabelaPreco>0?" AND G.cd_tabela_preco = " + cdTabelaPreco : "") + ")" : ""); 
//						 (lgFiscal ? " AND ("
//						 		+ "		EXISTS (SELECT ADSI.cd_produto_servico FROM alm_documento_saida_item ADSI "
//						 		+ "						JOIN alm_documento_saida ADS ON (ADS.cd_documento_saida = ADSI.cd_documento_saida) "
//						 		+ "						WHERE ADSI.cd_produto_servico = A.cd_produto_servico "
//						 		+ "						  AND ADS.tp_documento_saida IN (" + DocumentoSaidaServices.TP_CUPOM_FISCAL + ", " + DocumentoSaidaServices.TP_NFE + ", " + DocumentoSaidaServices.TP_NOTA_FISCAL_VENDA + ", " + DocumentoSaidaServices.TP_NOTA_FISCAL_02 + ")"
//						 		+ "				 LIMIT 1) "
//						 		+ "		OR EXISTS (SELECT ADEI.cd_produto_servico FROM alm_documento_entrada_item ADEI "
//						 		+ "						JOIN alm_documento_entrada ADE ON (ADE.cd_documento_entrada = ADEI.cd_documento_entrada) "
//						 		+ "						WHERE ADEI.cd_produto_servico = A.cd_produto_servico "
//						 		+ "						  AND ADE.tp_documento_entrada IN (" + DocumentoEntradaServices.TP_CUPOM_FISCAL + ", " + DocumentoEntradaServices.TP_NOTA_ELETRONICA  + ", " + DocumentoEntradaServices.TP_NOTA_FISCAL + ") "
//						 		+ "				    LIMIT 1) "
//						 		+ ")" : "");
//			
			/* 
			 * SEARCH
			 */

			LogUtils.debug("SQL:\n"+Search.getStatementSQL(sql, "nm_produto_servico, id_reduzido", crt, true));
			
			ResultSetMap rsm = Search.find(sql, "ORDER BY "+ (specialOrder != null ? specialOrder : "nm_produto_servico, id_reduzido ")+limitSkip[1], crt, relations, connect, connect==null, false, false);
//			ResultSetMap rsm = Search.findAndLog(sql, "ORDER BY "+ (specialOrder != null ? specialOrder : "nm_produto_servico, id_reduzido ")+limitSkip[1], crt, connect, connect==null);
			/* Fornecedor */
			
			LogUtils.logTimer("ESTOQUE_FIND_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
//			LogUtils.debug("ProdutoEstoqueService.findProduto: Iniciando injeção de dados adicionais...");
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT nm_pessoa AS nm_fornecedor FROM alm_documento_entrada D, alm_documento_entrada_item E, grl_pessoa F " +
				                                               "WHERE E.cd_produto_servico   = ? " +
				                                               "  AND E.cd_documento_entrada = D.cd_documento_entrada " +
				                                               "  AND E.cd_empresa           = " +cdEmpresa+
				                                               "  AND D.cd_empresa           = " +cdEmpresa+
				                                               "  AND D.st_documento_entrada = " +DocumentoEntradaServices.ST_LIBERADO+
				                                               "  AND D.tp_entrada IN ("+DocumentoEntradaServices.ENT_COMPRA+ ","+DocumentoEntradaServices.ENT_CONSIGNACAO+") " +
				                                               "  AND D.cd_fornecedor        = F.cd_pessoa " +
				                                               (lgFiscal ? " AND D.tp_documento_entrada = " + DocumentoEntradaServices.TP_NOTA_FISCAL : "") +
				                                               "ORDER BY dt_documento_entrada DESC " +
				                                               "LIMIT 1");
			

			LogUtils.logTimer("ESTOQUE_FIND_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
			
			rsm.beforeFirst();
			while (rsm.next()) {
				/* 
				 * CONTAGEM AUTOMÁTICA
				 */
				if (!noEstoque && lgContabAut) {
					criterios = new ArrayList<ItemComparator>();
					if(cdLocalArmazenamento > 0)
						criterios.add(new ItemComparator("cdLocalArmazenamento", "" + cdLocalArmazenamento, ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cdEmpresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cdProdutoServico", "" + rsm.getInt("cd_produto_servico"), ItemComparator.EQUAL, Types.INTEGER));
//					criterios.add(new ItemComparator("dtMovimento", "" + Util.formatDate(dtMovimento, "dd/MM/yyyy"), ItemComparator.EQUAL, Types.VARCHAR));
					criterios.add(new ItemComparator("lgFiscal", "" + lgFiscal, ItemComparator.EQUAL, Types.INTEGER));
					Result resultado = ProdutoEstoqueServices.getEstoqueAtual(criterios, connect);
					
					rsm.setValueToField("QT_ESTOQUE", resultado.getObjects().get("QT_ESTOQUE"));
					rsm.setValueToField("QT_ESTOQUE_CONSIGNADO", resultado.getObjects().get("QT_ESTOQUE_CONSIGNADO"));
					rsm.setValueToField("QT_ENTRADA", resultado.getObjects().get("QT_ENTRADA"));
					rsm.setValueToField("VL_ENTRADA", resultado.getObjects().get("VL_ENTRADA"));
					rsm.setValueToField("VL_ENTRADA_CONSIGNADA", resultado.getObjects().get("VL_ENTRADA_CONSIGNADA"));
					rsm.setValueToField("QT_ENTRADA_CONSIGNADA", resultado.getObjects().get("QT_ENTRADA_CONSIGNADA"));
					rsm.setValueToField("QT_SAIDA", resultado.getObjects().get("QT_SAIDA"));
					rsm.setValueToField("QT_SAIDA_CONSIGANADA", resultado.getObjects().get("QT_SAIDA_CONSIGANADA"));
					rsm.setValueToField("VL_SAIDA", resultado.getObjects().get("VL_SAIDA"));

				}
				/* CONTAGEM GERAL */
				else if (!noEstoque && cdLocalArmazenamento==0) {
					ResultSet rs = connect.prepareStatement("SELECT A.cd_produto_servico, SUM(A.qt_estoque) AS qt_estoque " +
				                                            "FROM alm_produto_estoque A " +
							                                "WHERE A.cd_empresa = " +cdEmpresa+
							                                "GROUP BY A.cd_produto_servico ").executeQuery();
					while (rs.next()) {
						if (rsm.locate("CD_PRODUTO_SERVICO", new Integer(rs.getInt("cd_produto_servico")), false))
							rsm.setValueToField("QT_ESTOQUE", rs.getFloat("QT_ESTOQUE"));
					}
				}
//				// Ultimo fornecedor
//				pstmt.setInt(1, rsm.getInt("cd_produto_servico"));
//				ResultSet rs = pstmt.executeQuery();
//				if (rs.next())
//					rsm.setValueToField("NM_FORNECEDOR", rs.getString("nm_fornecedor"));
//				//
//				if(!noEstoque){
//					if (((notEstoqueNegativoOrZero || lgEstoque==1) &&
//							((tpEstoque==-1 || tpEstoque==0 ? rsm.getFloat("QT_ESTOQUE") : 0) + (tpEstoque==-1 || tpEstoque==1 ? rsm.getFloat("QT_ESTOQUE_CONSIGNADO") : 0)) <= 0) ||
//							(lgEstoque==0 && (((tpEstoque==-1 || tpEstoque==0 ? rsm.getFloat("QT_ESTOQUE") : 0) + (tpEstoque==-1 || tpEstoque==1 ? rsm.getFloat("QT_ESTOQUE_CONSIGNADO") : 0)) > 0 ||
//											  ((tpEstoque==-1 || tpEstoque==0 ? rsm.getFloat("QT_ESTOQUE") : 0) + (tpEstoque==-1 || tpEstoque==1 ? rsm.getFloat("QT_ESTOQUE_CONSIGNADO") : 0)) < 0))) {
//						rsm.deleteRow();
//						if (rsm.getPointer()==0)'
//							rsm.beforeFirst();
//						else
//							rsm.previous();
//					}
//					else if (lgEstoque==2 &&
//							((tpEstoque==-1 || tpEstoque==0 ? rsm.getFloat("QT_ESTOQUE") : 0) + (tpEstoque==-1 || tpEstoque==1 ? rsm.getFloat("QT_ESTOQUE_CONSIGNADO") : 0)) >= 0) {
//						rsm.deleteRow();
//						if (rsm.getPointer()==0)
//							rsm.beforeFirst();
//						else
//							rsm.previous();
//					}else if ( lgEstoque==3 && ((tpEstoque==-1 || tpEstoque==0 ? rsm.getFloat("QT_ESTOQUE") : 0) + (tpEstoque==-1 || tpEstoque==1 ? rsm.getFloat("QT_ESTOQUE_CONSIGNADO") : 0)) > rsm.getFloat("QT_MINIMA_ESTOQUE") ){
//						rsm.deleteRow();
//						if (rsm.getPointer()==0)
//							rsm.beforeFirst();
//						else
//							rsm.previous();
//					}else if ( lgEstoque==4 && ((tpEstoque==-1 || tpEstoque==0 ? rsm.getFloat("QT_ESTOQUE") : 0) + (tpEstoque==-1 || tpEstoque==1 ? rsm.getFloat("QT_ESTOQUE_CONSIGNADO") : 0)) >= rsm.getFloat("QT_IDEAL_ESTOQUE") ) {
//						rsm.deleteRow();
//						if (rsm.getPointer()==0)
//							rsm.beforeFirst();
//						else
//							rsm.previous();
//					}
//				}
			}
			/* Ordenando */
			ArrayList<String> order = new ArrayList<String>();
			order.add("QT_ESTOQUE DESC");
			
			rsm.orderBy(order);
			
			
	 		rsm.beforeFirst();

			LogUtils.logTimer("ESTOQUE_FIND_TIMER", LogUtils.TIMER_MILLISECOND, LogUtils.VERBOSE_LEVEL_DEBUG);
			
	 		
	 		if (specialOrder == null & orderByFields != null && orderByFields.size()>0)
				rsm.orderBy(orderByFields);
			return rsm;
		}
		catch(Exception e) {
			Util.registerLog(e);			
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap recalcularUltimoCusto(int cdEmpresa, ArrayList<ItemComparator> criterios) {
		return recalcularUltimoCusto(cdEmpresa, criterios, null);
	}

	public static ResultSetMap recalcularUltimoCusto(int cdEmpresa, ArrayList<ItemComparator> criterios, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			ResultSetMap rsm = Search.find(
					"SELECT A.cd_produto_servico, A.nm_produto_servico, A.tp_produto_servico, " +
					"       A.id_produto_servico, F.sg_unidade_medida, D.nm_pessoa AS nm_fabricante, " +
				    "       B.vl_preco_medio, B.vl_custo_medio, B.vl_ultimo_custo " +
				    "FROM grl_produto_servico A " +
				    "JOIN grl_produto E ON (A.cd_produto_servico = E.cd_produto_servico) " +
				    "LEFT OUTER JOIN grl_produto_servico_empresa B ON (E.cd_produto_servico = B.cd_produto_servico AND " +
				    "										  B.cd_empresa = " + cdEmpresa + ") " +
				    "LEFT OUTER JOIN grl_unidade_medida F ON (B.cd_unidade_medida = F.cd_unidade_medida) " +
				    "LEFT OUTER JOIN grl_pessoa D ON (A.cd_fabricante = D.cd_pessoa) ", criterios, true,
				   connection!=null ? connection : Conexao.conectar(), connection==null);

			int cdProdutoServico = 0;
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("A.cd_produto_servico")) {
					cdProdutoServico = Integer.parseInt(criterios.get(i).getValue());
					break;
				}
			}

			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.vl_unitario, A.cd_produto_servico, G.nm_produto_servico, " +
					"       B.tp_frete, A.qt_entrada, A.vl_acrescimo, A.vl_desconto, " +
					"       B.vl_frete, B.vl_seguro, " +
					"      (SELECT SUM(H.pr_aliquota * vl_base_calculo / 100) " +
					"		FROM adm_entrada_item_aliquota H, adm_tributo H1 " +
					"		WHERE H.cd_tributo           = H1.cd_tributo " +
					"         AND H1.tp_cobranca         = "+TributoServices._INCIDENCIA_DIRETA+
					"         AND H.cd_produto_servico   = A.cd_produto_servico " +
					"		  AND H.cd_documento_entrada = A.cd_documento_entrada " +
					"		  AND H.cd_empresa           = A.cd_empresa) AS vl_tributos, " +
					"	   (SELECT SUM(I.qt_entrada * I.vl_unitario + vl_acrescimo - vl_desconto) " +
					"		FROM alm_documento_entrada_item I " +
					"		WHERE I.cd_documento_entrada = A.cd_documento_entrada " +
					"		  AND I.cd_empresa = A.cd_empresa) AS vl_total_documento " +
					"FROM alm_documento_entrada_item A, alm_documento_entrada B, grl_produto_servico G " +
					"WHERE A.cd_documento_entrada = B.cd_documento_entrada " +
					"  AND A.cd_empresa = B.cd_empresa " +
					"  AND A.cd_produto_servico = G.cd_produto_servico " +
					"  AND A.cd_empresa = ? " +
					"  AND (B.tp_entrada = ? OR B.tp_entrada = ?) " +
					"  AND B.st_documento_entrada = ? " +
					"  AND A.cd_documento_entrada = (SELECT MAX(C.cd_documento_entrada) " +
					"								 FROM alm_documento_entrada C, alm_documento_entrada_item D " +
					"								 WHERE C.cd_documento_entrada = D.cd_documento_entrada " +
					"								   AND C.cd_empresa = D.cd_empresa " +
					"								   AND A.cd_empresa = D.cd_empresa " +
					"								   AND A.cd_produto_servico = D.cd_produto_servico " +
					"								   AND C.st_documento_entrada = ? " +
					"								   AND (C.tp_entrada = ? OR C.tp_entrada = ?) " +
					"								   AND C.dt_documento_entrada = (SELECT MAX(E.dt_documento_entrada) " +
					"																 FROM alm_documento_entrada E, alm_documento_entrada_item F " +
					"								 								 WHERE E.cd_documento_entrada = F.cd_documento_entrada " +
					"								   								   AND E.cd_empresa = F.cd_empresa " +
					"								   								   AND D.cd_empresa = F.cd_empresa " +
					"								   								   AND D.cd_produto_servico = F.cd_produto_servico " +
					"																   AND E.st_documento_entrada = ? " +
					"																   AND (E.tp_entrada = ? OR E.tp_entrada = ?))) " +
					(cdProdutoServico>0 ? "  AND A.cd_produto_servico = ? " : ""));
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, DocumentoEntradaServices.ENT_COMPRA);
			pstmt.setInt(3, DocumentoEntradaServices.ENT_CONSIGNACAO);
			pstmt.setInt(4, DocumentoEntradaServices.ST_LIBERADO);
			pstmt.setInt(5, DocumentoEntradaServices.ST_LIBERADO);
			pstmt.setInt(6, DocumentoEntradaServices.ENT_COMPRA);
			pstmt.setInt(7, DocumentoEntradaServices.ENT_CONSIGNACAO);
			pstmt.setInt(8, DocumentoEntradaServices.ST_LIBERADO);
			pstmt.setInt(9, DocumentoEntradaServices.ENT_COMPRA);
			pstmt.setInt(10, DocumentoEntradaServices.ENT_CONSIGNACAO);
			if (cdProdutoServico>0)
				pstmt.setInt(11, cdProdutoServico);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ProdutoServicoEmpresa confProdutoServico = ProdutoServicoEmpresaDAO.get(cdEmpresa, rs.getInt("cd_produto_servico"), connection);
				if (confProdutoServico == null) {
					confProdutoServico = new ProdutoServicoEmpresa(cdEmpresa,
							rs.getInt("cd_produto_servico"),
							0 /*cdUnidadeMedida*/,
							"" /*idReduzido*/,
							0 /*vlPrecoMedio*/,
							0 /*vlCustoMedio*/,
							rs.getInt("vl_unitario") /*vlUltimoCusto*/,
							0 /*qtIdeal*/,
							0 /*qtMinima*/,
							0 /*qtMaxima*/,
							0 /*dtDiasEstoque*/,
							0 /*qtPrecisaoCusto*/,
							0 /*qtPrecisaoUnidade*/,
							0 /*qtDiasGarantia*/,
							ProdutoServicoEmpresaServices.TP_MANUAL /*tpReabastecimento*/,
							ProdutoServicoEmpresaServices.CTL_QUANTIDADE /*tpControleEstoque*/,
							ProdutoServicoEmpresaServices.TRANSP_COMUM /*tpTransporte*/,
							ProdutoServicoEmpresaServices.ST_ATIVO /*stAtivo*/,
							null /*dtDesativacao*/,
							"" /*nrOrdem*/,
							0 /*lgEstoqueNegativo*/);
					if (ProdutoServicoEmpresaDAO.insert(confProdutoServico, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return null;
					}
				}
				float vlTotalItem = rs.getFloat("qt_entrada") * rs.getFloat("vl_unitario") + rs.getFloat("vl_acrescimo") -
									rs.getFloat("vl_desconto");
				float vlTotalDocumento = rs.getFloat("vl_total_documento");
				float vlFreteItem = vlTotalDocumento==0 ? 0 : rs.getFloat("tp_frete") == DocumentoEntradaServices.FRT_CIF ? 0 :
					(vlTotalItem * rs.getFloat("vl_frete")) / vlTotalDocumento;
				float vlSeguroItem = vlTotalDocumento==0 ? 0 : (vlTotalItem * rs.getFloat("vl_seguro")) / vlTotalDocumento;
				float vlUnitario = rs.getFloat("vl_unitario");
				float vlFreteUnitario = rs.getFloat("qt_entrada")==0 ? 0 : rs.getFloat("tp_frete") == DocumentoEntradaServices.FRT_CIF ? 0 :
					vlFreteItem / rs.getFloat("qt_entrada");
				float vlSeguroUnitario = rs.getFloat("qt_entrada")==0 ? 0 : vlSeguroItem / rs.getFloat("qt_entrada");
				float vlTributosUnitario = ((float)1 * rs.getFloat("vl_tributos")) / rs.getFloat("qt_entrada");
				float vlUltimoCusto = vlUnitario + vlFreteUnitario + vlSeguroUnitario + vlTributosUnitario;
				confProdutoServico.setVlUltimoCusto(vlUltimoCusto);
				if (ProdutoServicoEmpresaDAO.update(confProdutoServico, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return null;
				}
				if (rsm.locate("cd_produto_servico", rs.getInt("cd_produto_servico"), false, false)) {
					rsm.getRegister().put("VL_ULTIMO_CUSTO", vlUltimoCusto);
				}
			}

			if (isConnectionNull)
				connection.commit();

			rsm.beforeFirst();

			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap recalcularValoresMedios(int cdEmpresa, ArrayList<ItemComparator> criterios) {
		return recalcularValoresMedios(cdEmpresa, criterios, null);
	}

	public static ResultSetMap recalcularValoresMedios(int cdEmpresa, ArrayList<ItemComparator> criterios, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			ResultSetMap rsm = Search.find("SELECT A.cd_produto_servico, A.nm_produto_servico, A.tp_produto_servico, " +
					" A.id_produto_servico, F.sg_unidade_medida, " +
					" D.nm_pessoa AS nm_fabricante, 0.00 AS vl_preco_medio, 0.00 AS vl_custo_medio " +
				    "FROM grl_produto_servico A " +
				    "JOIN grl_produto E ON (A.cd_produto_servico = E.cd_produto_servico) " +
				    "LEFT OUTER JOIN grl_produto_servico_empresa B ON (E.cd_produto_servico = B.cd_produto_servico AND " +
				    "										  B.cd_empresa = " + cdEmpresa + ") " +
				    "LEFT OUTER JOIN grl_unidade_medida F ON (B.cd_unidade_medida = F.cd_unidade_medida) " +
				    "LEFT OUTER JOIN grl_pessoa D ON (A.cd_fabricante = D.cd_pessoa) ", criterios, true,
				    connection!=null ? connection : Conexao.conectar(), connection==null);

			int cdProdutoServico = 0;
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("A.cd_produto_servico")) {
					cdProdutoServico = Integer.parseInt(criterios.get(i).getValue());
					break;
				}
			}

			PreparedStatement pstmt = connection.prepareStatement("SELECT A.cd_produto_servico, " +
					"(E.qt_entrada + E.qt_entrada_consignada) AS qt_item, " +
					"A.vl_unitario, A.vl_acrescimo, A.vl_desconto, B.dt_documento_entrada AS dt_movimento, B.vl_frete, " +
					"B.tp_entrada AS tp_operacao, " +
					"B.vl_seguro, 0 AS tp_movimento, B.tp_frete, (SELECT SUM((C.qt_entrada * C.vl_unitario) + C.vl_acrescimo - C.vl_desconto) " +
					"											  FROM alm_documento_entrada_item C " +
					"											  WHERE C.cd_documento_entrada = B.cd_documento_entrada " +
					"												AND C.cd_empresa = B.cd_empresa) AS vl_total_movimento, " +
					"											 (SELECT SUM(pr_aliquota * vl_base_calculo / 100) " +
					"											  FROM adm_entrada_item_aliquota D " +
					"											  WHERE D.cd_produto_servico = A.cd_produto_servico " +
					"												AND D.cd_documento_entrada = A.cd_documento_entrada " +
					"												AND D.cd_empresa = A.cd_empresa) AS vl_tributos " +
					"FROM alm_entrada_local_item E, alm_documento_entrada_item A, alm_documento_entrada B " +
					"WHERE E.cd_produto_servico = A.cd_produto_servico " +
					"  AND E.cd_documento_entrada = A.cd_documento_entrada " +
					"  AND E.cd_empresa = A.cd_empresa " +
					"  AND A.cd_documento_entrada = B.cd_documento_entrada " +
					"  AND B.cd_empresa = ? " +
					"  AND B.st_documento_entrada = ? " +
					"  AND B.tp_entrada <> ? " +
					(cdProdutoServico>0 ? "  AND A.cd_produto_servico = ?"  : "") +
					"UNION " +
					"SELECT A.cd_produto_servico, (C.qt_saida + C.qt_saida_consignada) AS qt_item, " +
					"A.vl_unitario, A.vl_acrescimo, A.vl_desconto, B.dt_documento_saida AS dt_movimento, B.vl_frete, " +
					"B.tp_saida AS tp_operacao, " +
					"B.vl_seguro, 1 AS tp_movimento, B.tp_frete, 0.00 AS vl_total_movimento, 0.00 AS vl_tributos " +
					"FROM alm_saida_local_item C, alm_documento_saida_item A, alm_documento_saida B " +
					"WHERE C.cd_produto_servico = A.cd_produto_servico " +
					"  AND C.cd_documento_saida = A.cd_documento_saida " +
					"  AND C.cd_empresa = A.cd_empresa " +
					"  AND A.cd_documento_saida = B.cd_documento_saida " +
					"  AND B.cd_empresa = ? " +
					"  AND B.st_documento_saida = ? " +
					(cdProdutoServico>0 ? "  AND A.cd_produto_servico = ?"  : ""));
			int i=1;
			pstmt.setInt(i++, cdEmpresa);
			pstmt.setInt(i++, DocumentoEntradaServices.ST_LIBERADO);
			pstmt.setInt(i++, DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO);
			if (cdProdutoServico>0)
				pstmt.setInt(i++, cdProdutoServico);
			pstmt.setInt(i++, cdEmpresa);
			pstmt.setInt(i++, DocumentoSaidaServices.ST_CONCLUIDO);
			if (cdProdutoServico>0)
				pstmt.setInt(i++, cdProdutoServico);
			ResultSetMap rsmTemp = new ResultSetMap(pstmt.executeQuery());
			ArrayList<String> fieldsTemp = new ArrayList<String>();
			fieldsTemp.add("cd_produto_servico");
			fieldsTemp.add("dt_movimento");
			rsmTemp.orderBy(fieldsTemp);
			while (rsmTemp.next()) {
				ProdutoServicoEmpresa confProdutoServico = ProdutoServicoEmpresaDAO.get(cdEmpresa, rsmTemp.getInt("cd_produto_servico"), connection);
				if (confProdutoServico == null) {
					confProdutoServico = new ProdutoServicoEmpresa(cdEmpresa,
							rsmTemp.getInt("cd_produto_servico"),
							0 /*cdUnidadeMedida*/,
							"" /*idReduzido*/,
							0 /*vlPrecoMedio*/,
							0 /*vlCustoMedio*/,
							rsmTemp.getInt("vl_unitario") /*vlUltimoCusto*/,
							0 /*qtIdeal*/,
							0 /*qtMinima*/,
							0 /*qtMaxima*/,
							0 /*dtDiasEstoque*/,
							0 /*qtPrecisaoCusto*/,
							0 /*qtPrecisaoUnidade*/,
							0 /*qtDiasGarantia*/,
							ProdutoServicoEmpresaServices.TP_MANUAL /*tpReabastecimento*/,
							ProdutoServicoEmpresaServices.CTL_QUANTIDADE /*tpControleEstoque*/,
							ProdutoServicoEmpresaServices.TRANSP_COMUM /*tpTransporte*/,
							ProdutoServicoEmpresaServices.ST_ATIVO /*stAtivo*/,
							null /*dtDesativacao*/,
							"" /*nrOrdem*/,
							0 /*lgEstoqueNegativo*/);
					if (ProdutoServicoEmpresaDAO.insert(confProdutoServico, connection) <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						return null;
					}
				}
				float vlPrecoMedio = 0;
				float vlCustoMedio = 0;
				float qtEstoque = 0;
				int cdProdutoServicoTemp = rsmTemp.getInt("cd_produto_servico");
				boolean isNext = true;
				while (cdProdutoServicoTemp == rsmTemp.getInt("cd_produto_servico") && isNext) {
					if (rsmTemp.getInt("tp_movimento") == 0) {
						if (rsmTemp.getInt("tp_operacao") == DocumentoEntradaServices.ENT_COMPRA ||
								rsmTemp.getInt("") == DocumentoEntradaServices.ENT_CONSIGNACAO) {
							float vlFrete = rsmTemp.getFloat("vl_frete");
							float vlSeguro = rsmTemp.getFloat("vl_seguro");
							float vlMovimento = rsmTemp.getFloat("vl_total_movimento");
							float vlTotalItem = (rsmTemp.getFloat("vl_unitario") + rsmTemp.getFloat("vl_acrescimo") - rsmTemp.getFloat("vl_desconto")) * rsmTemp.getFloat("qt_item");
							float vlFreteItem = 0;
							float vlSeguroItem = 0;
							if (rsmTemp.getInt("tp_frete") != DocumentoEntradaServices.FRT_CIF) {
								vlFreteItem = vlMovimento == 0 ? 0 : (vlTotalItem * vlFrete) / vlMovimento;
							}
							vlSeguroItem = vlMovimento==0 ? 0 : (vlTotalItem * vlSeguro) / vlMovimento;
							vlPrecoMedio = ((vlPrecoMedio * qtEstoque) + vlTotalItem) / (qtEstoque + rsmTemp.getFloat("qt_item"));
							vlCustoMedio = ((vlCustoMedio * qtEstoque) + vlTotalItem + vlFreteItem + vlSeguroItem + rsmTemp.getFloat("vl_tributos")) /
											(qtEstoque + rsmTemp.getFloat("qt_item"));
						}
						qtEstoque += rsmTemp.getFloat("qt_item");
					}
					else
						qtEstoque -= rsmTemp.getFloat("qt_item");
					if (rsmTemp.hasMore())
						rsmTemp.next();
					else
						isNext = false;
				}
				confProdutoServico.setVlPrecoMedio(vlPrecoMedio);
				confProdutoServico.setVlCustoMedio(vlCustoMedio);
				if (ProdutoServicoEmpresaDAO.update(confProdutoServico, connection) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return null;
				}
				if (isNext && cdProdutoServicoTemp != rsmTemp.getInt("cd_produto_servico"))
					rsmTemp.beforeFirst();
				if (rsm.locate("cd_produto_servico", cdProdutoServicoTemp, false, false)) {
					rsm.getRegister().put("VL_PRECO_MEDIO", vlPrecoMedio);
					rsm.getRegister().put("VL_CUSTO_MEDIO", vlCustoMedio);
				}
			}

			if (isConnectionNull)
				connection.commit();

			rsm.beforeFirst();

			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap findEstoqueProdutoByLocalArmazenamento(int cdEmpresa, int cdProdutoServico, ArrayList<ItemComparator> criterios) {
		return findEstoqueProdutoByLocalArmazenamento(cdEmpresa, cdProdutoServico, criterios, null);
	}

	public static ResultSetMap findEstoqueProdutoByLocalArmazenamento(int cdEmpresa, int cdProdutoServico, ArrayList<ItemComparator> criterios,
			Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			boolean lgContabilizacaoAutomatica = ParametroServices.getValorOfParametroAsInteger("LG_CONTABILIZACAO_ESTOQUE", 0, cdEmpresa, connect)==1;
			ResultSetMap rsm = Search.find(
					"SELECT A.*, "+(lgContabilizacaoAutomatica?" 0 AS ":" B.")+"qt_estoque, B.qt_estoque_consignado, B.qt_ideal, B.qt_minima, " +
					"       B.qt_maxima, B.qt_dias_estoque, B.qt_minima_ecommerce " +
					"FROM alm_local_armazenamento A " +
				    "LEFT OUTER JOIN alm_produto_estoque         B ON (A.cd_local_armazenamento = B.cd_local_armazenamento " +
				    "									           AND A.cd_empresa             = B.cd_empresa " +
				    "									           AND B.cd_produto_servico     = " + cdProdutoServico + ") " +
				    "LEFT OUTER JOIN grl_produto_servico_empresa C ON (B.cd_produto_servico     = C.cd_produto_servico " +
				    "										       AND B.cd_empresa             = C.cd_empresa " +
				    "										       AND C.cd_produto_servico     = " + cdProdutoServico + ") " +
				    "WHERE A.cd_empresa = " + cdEmpresa, criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
			if (lgContabilizacaoAutomatica) {
				/* entradas */
				PreparedStatement pstmt = connect.prepareStatement("SELECT A.cd_local_armazenamento, SUM(A.qt_entrada + A.qt_entrada_consignada) AS QT_ENTRADA " +
																   "FROM alm_entrada_local_item A, alm_documento_entrada B " +
																   "WHERE A.cd_empresa = " + cdEmpresa +
																   "  AND A.cd_documento_entrada = B.cd_documento_entrada " +
																   "  AND B.st_documento_entrada = " +DocumentoEntradaServices.ST_LIBERADO+
																   (cdProdutoServico==0 ? "" : "  AND A.cd_produto_servico = "+cdProdutoServico) +
																   " GROUP BY A.cd_local_armazenamento ");
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					if (rsm.locate("CD_LOCAL_ARMAZENAMENTO", new Integer(rs.getInt("cd_local_armazenamento")), false)) {
						HashMap<String,Object> register = rsm.getRegister();
						float qtEstoque = rs.getFloat("QT_ENTRADA");
						register.put("QT_ESTOQUE", new Float(qtEstoque));
						register.put("QT_ESTOQUE_CONSIGNADO", new Float(0));
					}
				}

				/* saidas */
				pstmt = connect.prepareStatement("SELECT A.cd_local_armazenamento, SUM(A.qt_saida + A.qt_saida_consignada) AS qt_saida " +
						   "FROM alm_saida_local_item A, alm_documento_saida B " +
						   "WHERE A.cd_documento_saida = B.cd_documento_saida " +
						   "  AND A.cd_empresa = " +cdEmpresa+
						   "  AND B.st_documento_saida = " +DocumentoSaidaServices.ST_CONCLUIDO+
						   (cdProdutoServico==0 ? "" : "  AND A.cd_produto_servico = "+cdProdutoServico) +
						   " GROUP BY A.cd_local_armazenamento ");
				rs = pstmt.executeQuery();
				while (rs.next()) {
					if (rsm.locate("CD_LOCAL_ARMAZENAMENTO", new Integer(rs.getInt("cd_local_armazenamento")), false)) {
						HashMap<String,Object> register = rsm.getRegister();
						float qtEstoqueTemp = rsm.getFloat("QT_ESTOQUE");
						float qtEstoque = qtEstoqueTemp - rs.getFloat("QT_SAIDA");
						register.put("QT_ESTOQUE", new Float(qtEstoque));
						register.put("QT_ESTOQUE_CONSIGNADO", new Float(0));
					}
				}
			}
			rsm.beforeFirst();
			while(rsm.next())
				if(rsm.getFloat("QT_ESTOQUE") == 0 && rsm.getFloat("QT_ESTOQUE_CONSIGNADO")==0) {
					rsm.deleteRow();
					rsm.beforeFirst();
				}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result update(ProdutoEstoque objeto){
		return update(objeto, false, null);
	}

	public static Result update(ProdutoEstoque objeto, boolean overSaldo){
		return update(objeto, overSaldo, null);
	}

	public static Result update(ProdutoEstoque objeto, boolean overSaldo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			int cdLocalArmazenamento = objeto.getCdLocalArmazenamento();
			int cdProdutoServico = objeto.getCdProdutoServico();
			int cdEmpresa = objeto.getCdEmpresa();

			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM grl_produto_servico_empresa " +
															   "WHERE cd_produto_servico = ? " +
															   "  AND cd_empresa = ?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.setInt(2, cdEmpresa);
			ResultSet rs = pstmt.executeQuery();
			
			if (!rs.next()) {
				ProdutoServicoEmpresa produto = new ProdutoServicoEmpresa(objeto.getCdEmpresa(),
											objeto.getCdProdutoServico(),
											0 /*cdUnidadeMedida*/,
											"" /*idReduzido*/,
											0 /*vlPrecoMedio*/,
											0 /*vlCustoMedio*/,
											0 /*vlUltimoCusto*/,
											0 /*qtIdeal*/,
											0 /*qtMinima*/,
											0 /*qtMaxima*/,
											0 /*qtDiasEstoque*/,
											0 /*qtPrecisaoCusto*/,
											0 /*qtPrecisaoUnidade*/,
											0 /*qtDiasGarantia*/,
											0 /*tpReabastecimento*/,
											0 /*tpControleEstoque*/,
											0 /*tpTransporte*/,
											ProdutoServicoEmpresaServices.ST_ATIVO /*stProdutoEmpresa*/,
											null /*dtDesativacao*/,
											"" /*nrOrdem*/,
											0 /*lgEstoqueNegativo*/);
				if (ProdutoServicoEmpresaDAO.insert(produto, connect) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(1);
				}
			}

			int idRetorno = 0;
			ProdutoEstoque estoque = ProdutoEstoqueDAO.get(cdLocalArmazenamento, cdProdutoServico, cdEmpresa, connect);
			if (estoque != null && !overSaldo)
				objeto.setQtEstoque(estoque.getQtEstoque());
			if (estoque != null && !overSaldo)
				objeto.setQtEstoqueConsignado(estoque.getQtEstoqueConsignado());
			if (estoque==null)
				idRetorno = ProdutoEstoqueDAO.insert(objeto, connect);
			else
				idRetorno = ProdutoEstoqueDAO.update(objeto, connect);

			if (idRetorno<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Houve um problema ao tentar atualizar o estoque.");
			}

			if (isConnectionNull)
				connect.commit();

			return new Result(1, "Estoque atualizado com sucesso!");
		}
		catch(Exception e){
			if (isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return new Result(-1, "Houve um problema ao tentar atualizar o estoque.");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap recalcularEstoque(int cdEmpresa, int cdProdutoServico, boolean lgApenasAtivos) {
		return recalcularEstoque(cdEmpresa, cdProdutoServico, lgApenasAtivos, null);
	}

	public static ResultSetMap recalcularEstoque(int cdEmpresa, int cdProdutoServico, boolean lgApenasAtivos, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap findProduto(int cdEmpresa, int cdLocalArmazenamento, int cdProdutoServico) {
		return findProduto(cdEmpresa, cdLocalArmazenamento, cdProdutoServico, null);
	}

	public static ResultSetMap findProduto(int cdEmpresa, int cdLocalArmazenamento, int cdProdutoServico, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_produto_servico", String.valueOf(cdProdutoServico), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_empresa", String.valueOf(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("CD_LOCAL_ARMAZENAMENTO", String.valueOf(cdLocalArmazenamento), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("findGrupoPrincipal", "1", ItemComparator.EQUAL, Types.INTEGER));
			
			return findCompleto(criterios);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	
	public static ResultSetMap findProduto(int cdEmpresa, int cdLocalArmazenamento, int codigoInicial, int codigoFinal, String letraInicial, String letraFinal, int cdTipoPesquisa, int quantidadePesquisa) {
		return findProduto(cdEmpresa, cdLocalArmazenamento, codigoInicial, codigoFinal, letraInicial, letraFinal, cdTipoPesquisa, quantidadePesquisa, null);
	}

	public static ResultSetMap findProduto(int cdEmpresa, int cdLocalArmazenamento, int codigoInicial, int codigoFinal, String letraInicial, String letraFinal, int cdTipoPesquisa, int quantidadePesquisa, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			
			if(codigoInicial > 0)
				criterios.add(new ItemComparator("A.id_produto_servico", String.valueOf(codigoInicial), ItemComparator.GREATER_EQUAL, Types.VARCHAR));
			if(codigoFinal > 0)	
				criterios.add(new ItemComparator("A.id_produto_servico", String.valueOf(codigoFinal), ItemComparator.MINOR_EQUAL, Types.VARCHAR));
			
			char lInicial = '0';
			char lFinal   = '0';
			
			if(letraInicial.length() == 1)
				lInicial = letraInicial.charAt(0);
			if(letraFinal.length() == 1)
				lFinal = letraFinal.charAt(0);
			
			if(lInicial != '0')
				criterios.add(new ItemComparator("A.nm_produto_servico", String.valueOf(lInicial), ItemComparator.GREATER_EQUAL, Types.VARCHAR));
			if(lFinal != '0')
				criterios.add(new ItemComparator("A.nm_produto_servico", String.valueOf((char)(lFinal+1)), ItemComparator.MINOR_EQUAL, Types.VARCHAR));
			
			
			criterios.add(new ItemComparator("qtLimite", String.valueOf(quantidadePesquisa), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_empresa", String.valueOf(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("CD_LOCAL_ARMAZENAMENTO", String.valueOf(cdLocalArmazenamento), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("ordenar", "1", ItemComparator.EQUAL, Types.INTEGER));
			
			return findCompleto(criterios);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios) {
		return findCompleto(criterios, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		int cdEmpresa = 0, cdGrupo = 0, qtLimite = 0, cdLocalArmazenamento = 0;
		boolean findGrupoPrincipal = false;
		boolean ordenar 		   = false;
		boolean findInLocalArmazenamento = false;
		String idProdutoServico = null;
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().toLowerCase().indexOf("cd_empresa")>=0) {
				cdEmpresa = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				i--;
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("CD_GRUPO")) {
				cdGrupo = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				i--;
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("CD_LOCAL_ARMAZENAMENTO")) {
				cdLocalArmazenamento = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				i--;
			}
			else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("findGrupoPrincipal")) {
				findGrupoPrincipal = true;
				criterios.remove(i);
				i--;
			}
			else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("findInLocalArmazenamento")) {
				findInLocalArmazenamento = true;
				criterios.remove(i);
				i--;
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite")) {
				qtLimite = Integer.parseInt(criterios.get(i).getValue());
				criterios.remove(i);
				i--;
			}
			else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("ordenar")) {
				ordenar = true;
				criterios.remove(i);
				i--;
			}
			else if (((ItemComparator)criterios.get(i)).getColumn().equalsIgnoreCase("id_produto_servico")) {
				idProdutoServico = criterios.get(i).getValue();
				criterios.remove(i);
				i--;
			}
			else if (criterios.get(i).getColumn().equalsIgnoreCase("nm_produto_servico") && criterios.get(i).getValue().length() > 0) {
				char ultimoCaractere = criterios.get(i).getValue().charAt(criterios.get(i).getValue().length() - 1);
				if(ultimoCaractere == ' '){
					criterios.get(i).setTypeComparation(ItemComparator.LIKE);
				}
			}
		}
		// Descobrindo tabela de preço do varejo
		int cdTipoOperacaoVarejo = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_VAREJO", 0, cdEmpresa);
		int cdTabelaPrecoVarejo  = 0;
		if(cdTipoOperacaoVarejo>0){
			TipoOperacao tipoOperacao = TipoOperacaoDAO.get(cdTipoOperacaoVarejo, connect);
			cdTabelaPrecoVarejo = tipoOperacao!=null ? tipoOperacao.getCdTabelaPreco() : 0;
		}
		// Descobrindo tabela de preço do atacado
		int cdTipoOperacaoAtacado = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_ATACADO", 0, cdEmpresa);
		int cdTabelaPrecoAtacado  = 0;
		if(cdTipoOperacaoAtacado>0)	{
			TipoOperacao tipoOperacao = TipoOperacaoDAO.get(cdTipoOperacaoAtacado, connect);
			cdTabelaPrecoAtacado = tipoOperacao!=null ? tipoOperacao.getCdTabelaPreco() : 0;
		}
		String[] limitSkip = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, 0);
		String sql =   "SELECT "+limitSkip[0]+" A.*, " +
					   "        B.cd_empresa, B.id_reduzido, B.cd_unidade_medida, B.vl_preco_medio, B.vl_custo_medio, " +
					   "        B.vl_ultimo_custo, B.qt_ideal, B.qt_minima, B.qt_maxima, " +
					   " 		B.qt_dias_estoque, B.qt_precisao_custo, B.qt_precisao_unidade, " +
					   "		B.qt_dias_garantia, B.tp_reabastecimento, B.tp_controle_estoque, " +
					   "		B.tp_transporte, B.st_produto_empresa, B.dt_desativacao, " +
					   "		B.nr_ordem, B.lg_estoque_negativo, B.tp_origem, B.id_fabrica, B.dt_cadastro, B.dt_ultima_alteracao, B.nr_serie, B.img_produto, B.pr_desconto_maximo, B.cd_local_armazenamento, " +
					   "		C.nm_pessoa as nm_fabricante, " +
					   "		D.id_classificacao_fiscal, D.nm_classificacao_fiscal, " +
					   "		E.vl_peso_unitario, E.vl_peso_unitario_embalagem, E.qt_embalagem, " +
					   "		E.vl_comprimento, E.vl_largura, E.vl_altura, E.vl_comprimento_embalagem, " +
					   "		E.vl_largura_embalagem, E.vl_altura_embalagem, " +
					   "		G.sg_unidade_medida, BP.*, NC.nm_ncm " +
					   (findGrupoPrincipal ? ", H.cd_grupo, H. lg_principal, I.nm_grupo, I.id_grupo ":"") +
//					   (cdTipoOperacaoVarejo>0 ? ",(SELECT vl_preco FROM adm_produto_servico_preco VRJ " +
//					   		                     " WHERE VRJ.cd_produto_servico = A.cd_produto_servico " +
//					   		                     "   AND dt_termino_validade IS NULL " +
//					   		                     "   AND VRJ.cd_tabela_preco    = "+cdTabelaPrecoVarejo+") AS vl_preco_"+cdTipoOperacaoVarejo : "")+
//					   (cdTipoOperacaoAtacado>0 ? ",(SELECT vl_preco FROM adm_produto_servico_preco ATD " +
//			                     				  " WHERE ATD.cd_produto_servico = A.cd_produto_servico " +
//						   		                  "   AND dt_termino_validade IS NULL " +
//			                     				  "   AND ATD.cd_tabela_preco    = "+cdTabelaPrecoAtacado+") AS vl_preco_"+cdTipoOperacaoAtacado : "")+
//			           (cdLocalArmazenamento>0 ? ",(SELECT SUM(D.qt_saida + D.qt_saida_consignada) " +
//		            							 "  FROM alm_saida_local_item D, alm_documento_saida E " +
//		            							 "  WHERE D.cd_documento_saida = E.cd_documento_saida " +
//		            							 "    AND E.st_documento_saida = " +DocumentoSaidaServices.ST_CONCLUIDO+
//		            							 "    AND D.cd_empresa         = "+cdEmpresa+
//		            							 "    AND D.cd_produto_servico = A.cd_produto_servico) AS qt_saida, " +
//		            							 " (SELECT SUM(D.qt_entrada + D.qt_entrada_consignada) " +
//		            							 "  FROM alm_entrada_local_item D, alm_documento_entrada E " +
//		            							 "  WHERE D.cd_documento_entrada = E.cd_documento_entrada " +
//		            							 "    AND E.st_documento_entrada = " +DocumentoSaidaServices.ST_CONCLUIDO+
//		            							 "    AND D.cd_empresa           = "+cdEmpresa+
//		            							 "    AND D.cd_produto_servico   = A.cd_produto_servico) AS qt_entrada " : "") +
		               " FROM grl_produto_servico A " +
					   "LEFT OUTER JOIN grl_produto                 E ON (A.cd_produto_servico = E.cd_produto_servico) " +
					   "LEFT OUTER JOIN grl_produto_servico_empresa B ON (E.cd_produto_servico = B.cd_produto_servico AND B.cd_empresa = " + cdEmpresa + ") " +
					   "LEFT OUTER JOIN grl_produto_servico_parametro BP ON (B.cd_produto_servico = BP.cd_produto_servico AND B.cd_empresa = BP.cd_empresa) " +
					   "LEFT OUTER JOIN grl_unidade_medida          G ON (G.cd_unidade_medida = B.cd_unidade_medida) " +
					   "LEFT OUTER JOIN grl_ncm                    NC ON (A.cd_ncm = NC.cd_ncm) ";
		if (cdGrupo > 0) {
			sql += "JOIN alm_produto_grupo H ON (A.cd_produto_servico = H.cd_produto_servico AND H.cd_empresa = " + cdEmpresa +
			       "  AND H.cd_grupo = " + cdGrupo + ")";
		}
		if (findGrupoPrincipal) {
			sql += "LEFT OUTER JOIN alm_produto_grupo H ON (A.cd_produto_servico = H.cd_produto_servico " +
					"                                   AND H.cd_empresa         = " + cdEmpresa +
			       "                                    AND H.lg_principal       = 1)" +
				   "LEFT OUTER JOIN alm_grupo I ON (H.cd_grupo = I.cd_grupo) ";
		}
		sql += " LEFT OUTER JOIN grl_pessoa C ON (A.cd_fabricante = C.cd_pessoa) " +
		       " LEFT OUTER JOIN adm_classificacao_fiscal D ON (A.cd_classificacao_fiscal = D.cd_classificacao_fiscal) " +
		       " WHERE 1 = 1 " + (idProdutoServico != null ? " AND (EXISTS (SELECT * FROM grl_produto_codigo GPC "
		       		+ "															WHERE GPC.cd_produto_servico = A.cd_produto_servico "
		       		+ "															  AND GPC.id_produto_servico LIKE '"+idProdutoServico+"' "
		       		+ "															  AND GPC.lg_codigo_barras = 1) "
		       		+ "													OR (A.id_produto_servico LIKE '"+idProdutoServico+"'))" : " ")
		       		+ (findInLocalArmazenamento && cdLocalArmazenamento > 0 ? " AND B.cd_local_armazenamento = " + cdLocalArmazenamento : "");
		
		ResultSetMap rsm = Search.find(sql, (ordenar ? " ORDER BY A.nm_produto_servico " : "") + limitSkip[1], criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
		
//		ResultSetMap rsm = Search.findAndLog(sql, criterios, connect!=null ? connect : Conexao.conectar());
		while(rsm.next()){
			criterios = new ArrayList<ItemComparator>();
			if(cdLocalArmazenamento > 0)
				criterios.add(new ItemComparator("cdLocalArmazenamento", "" + cdLocalArmazenamento, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cdEmpresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cdProdutoServico", "" + rsm.getInt("cd_produto_servico"), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("dtMovimento", "" + Util.formatDate(Util.getDataAtual(), "dd/MM/yyyy"), ItemComparator.EQUAL, Types.VARCHAR));
			Result registroEstoqueAtual = getEstoqueAtual(criterios, connect!=null ? connect : Conexao.conectar());
			
			if(registroEstoqueAtual.getObjects().get("QT_ESTOQUE") != null && registroEstoqueAtual.getObjects().get("QT_ESTOQUE_CONSIGNADO") != null) {
				rsm.setValueToField("QT_ESTOQUE", ((double)registroEstoqueAtual.getObjects().get("QT_ESTOQUE") + (double)registroEstoqueAtual.getObjects().get("QT_ESTOQUE_CONSIGNADO")));
			} else {
				rsm.setValueToField("QT_ESTOQUE", 0);
			}
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_produto_servico", rsm.getString("cd_produto_servico"), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("dt_termino_validade", null, ItemComparator.ISNULL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_tabela_preco", "" + cdTabelaPrecoVarejo, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmProdutoServicoPreco = ProdutoServicoPrecoDAO.find(criterios, connect);
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("cd_produto_servico_preco");
			rsmProdutoServicoPreco.orderBy(fields);
			while(rsmProdutoServicoPreco.next()){
				rsm.setValueToField("vl_preco_"+cdTipoOperacaoVarejo, rsmProdutoServicoPreco.getFloat("vl_preco"));
			}
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_produto_servico", rsm.getString("cd_produto_servico"), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("dt_termino_validade", null, ItemComparator.ISNULL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_tabela_preco", "" + cdTabelaPrecoAtacado, ItemComparator.EQUAL, Types.INTEGER));
			rsmProdutoServicoPreco = ProdutoServicoPrecoDAO.find(criterios, connect);
			rsmProdutoServicoPreco.orderBy(fields);
			while(rsmProdutoServicoPreco.next()){
				rsm.setValueToField("vl_preco_"+cdTipoOperacaoAtacado, rsmProdutoServicoPreco.getFloat("vl_preco"));
			}
			
		}
		rsm.beforeFirst();
		
		return rsm;
	}

	public static ResultSetMap findMovimentos(int cdEmpresa, int cdProdutoServico, ArrayList<ItemComparator> criterios) {
		return findMovimentos(cdEmpresa, cdProdutoServico, criterios, null);
	}

	public static ResultSetMap findMovimentos(int cdEmpresa, int cdProdutoServico, ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int tpMovimento = MOV_TODOS;
			ItemComparator item = ItemComparator.getItemComparatorByColumn(criterios, "TP_MOVIMENTO");
			if (item != null) {
				criterios.remove(item);
				tpMovimento = Integer.parseInt(item.getValue());
			}
			ResultSetMap rsm = new ResultSetMap();
			if (tpMovimento==MOV_TODOS || tpMovimento==MOV_ENTRADA) {
				/* entradas */
				ArrayList<ItemComparator> criteriosTemp = new ArrayList<ItemComparator>();
				for (int i=0; criterios!=null && i<criterios.size(); i++) {
					ItemComparator criterio = (ItemComparator)criterios.get(i).clone();
					if (criterio.getColumn().equalsIgnoreCase("DT_MOVIMENTO")) {
						criterio.setColumn("D.dt_documento_entrada");
						criterio.setColumn("dt_documento_entrada");
					}
					else if (criterio.getColumn().equalsIgnoreCase("TP_ORIGEM")) {
						criterio.setColumn("D.tp_entrada");
						criterio.setColumn("tp_entrada");
					}
					criteriosTemp.add(criterio);
				}
				String sql =
					    "SELECT A.cd_documento_entrada, D.nr_documento_entrada, A.cd_produto_servico, A.cd_empresa," +
						"       A.cd_local_armazenamento, A.qt_entrada, A.qt_entrada_consignada, B.nm_local_armazenamento, B.cd_setor," +
						"       C.nm_setor, D.dt_documento_entrada, E.nm_produto_servico, E.tp_produto_servico, G.vl_unitario, " +
						"       G.vl_acrescimo, G.vl_desconto, E.id_produto_servico, E.sg_produto_servico, F.st_produto_empresa, " +
						"       E.cd_classificacao_fiscal, D.tp_movimento_estoque, D.tp_entrada, H.nm_pessoa " +
						"FROM alm_entrada_local_item A " +
						"JOIN alm_documento_entrada                  D ON (A.cd_documento_entrada = D.cd_documento_entrada AND " +
						"								                   A.cd_empresa = D.cd_empresa AND " +
						"								                   D.st_documento_entrada = " + DocumentoEntradaServices.ST_LIBERADO + ") " +
						"JOIN alm_documento_entrada_item             G ON (G.cd_produto_servico = A.cd_produto_servico AND " +
						"									               G.cd_documento_entrada = A.cd_documento_entrada AND " +
						"									               G.cd_empresa = A.cd_empresa) " +
						"JOIN alm_local_armazenamento                B ON (A.cd_local_armazenamento = B.cd_local_armazenamento) " +
						"LEFT OUTER JOIN grl_setor                   C ON (B.cd_setor = C.cd_setor) " +
						"LEFT OUTER JOIN grl_produto_servico         E ON (A.cd_produto_servico = E.cd_produto_servico) " +
						"LEFT OUTER JOIN grl_produto_servico_empresa F ON (E.cd_produto_servico = F.cd_produto_servico " +
						(cdEmpresa>0 ? "  AND F.cd_empresa = " + cdEmpresa : "") + ")" +
						"LEFT OUTER JOIN grl_pessoa                  H ON (D.cd_fornecedor = H.cd_pessoa) " +
						"WHERE 1=1 " +
						(cdProdutoServico>0 ? "  AND A.cd_produto_servico = " + cdProdutoServico : "") +
						(cdEmpresa>0 ? "  AND A.cd_empresa = " + cdEmpresa : "");
				ResultSetMap rsmTemp = Search.find(sql, criteriosTemp, true, connect!=null ? connect : Conexao.conectar(), connect==null);
				while (rsmTemp!=null && rsmTemp.next()) {
					HashMap<String,Object> entrada = new HashMap<String,Object>();
					entrada.put("NM_PRODUTO_SERVICO", rsmTemp.getString("nm_produto_servico"));
					entrada.put("NM_PESSOA", rsmTemp.getString("nm_pessoa"));
					entrada.put("TP_MOVIMENTO", new Integer(MOV_ENTRADA));
					entrada.put("CD_MOVIMENTO", new Integer(rsmTemp.getInt("cd_documento_entrada")));
					entrada.put("QT_MOVIMENTO", new Float(rsmTemp.getFloat("qt_entrada")));
					entrada.put("VL_UNITARIO", new Float(rsmTemp.getFloat("VL_UNITARIO")));
					entrada.put("VL_MOVIMENTO", new Float(rsmTemp.getFloat("VL_UNITARIO") * rsmTemp.getFloat("qt_entrada") + rsmTemp.getFloat("vl_acrescimo") - rsmTemp.getFloat("vl_desconto")));
					entrada.put("QT_MOVIMENTO_CONSIGNADO", new Float(rsmTemp.getFloat("qt_entrada_consignada")));
					entrada.put("QT_MOVIMENTO_TOTAL", new Float(rsmTemp.getFloat("qt_entrada_consignada") + rsmTemp.getFloat("qt_entrada")));
					entrada.put("CD_LOCAL_ARMAZENAMENTO", new Integer(rsmTemp.getInt("cd_local_armazenamento")));
					entrada.put("NM_LOCAL_ARMAZENAMENTO", rsmTemp.getString("nm_local_armazenamento"));
					entrada.put("ID_MOVIMENTO", rsmTemp.getString("nr_documento_entrada"));
					entrada.put("CD_SETOR", new Integer(rsmTemp.getInt("cd_setor")));
					entrada.put("NM_SETOR", rsmTemp.getString("nm_setor"));
					entrada.put("DT_MOVIMENTO", Util.convTimestampToCalendar(rsmTemp.getTimestamp("dt_documento_entrada")));
					entrada.put("TP_ORIGEM", new Integer(rsmTemp.getInt("tp_entrada")));
					entrada.put("DS_TIPO_ORIGEM", DocumentoEntradaServices.tiposEntrada[rsmTemp.getInt("tp_entrada")] + (rsmTemp.getInt("tp_entrada")!=DocumentoEntradaServices.ENT_COMPRA && rsmTemp.getInt("tp_entrada")!=DocumentoEntradaServices.ENT_CONSIGNACAO && rsmTemp.getInt("tp_movimento_estoque")==DocumentoEntradaServices.MOV_ESTOQUE_CONSIGNADO ? " - Consignação" : ""));
					entrada.put("DS_ORDEM", Util.formatDate(rsmTemp.getGregorianCalendar("dt_documento_entrada"), "yyyyMMdd"));
					rsm.addRegister(entrada);
				}
			}

			/* saídas */
			if (tpMovimento==MOV_TODOS || tpMovimento==MOV_SAIDA){
				ArrayList<ItemComparator> criteriosTemp = new ArrayList<ItemComparator>();
				for (int i=0; criterios!=null && i<criterios.size(); i++) {
					ItemComparator criterio = (ItemComparator)criterios.get(i).clone();
					if (criterio.getColumn().equalsIgnoreCase("DT_MOVIMENTO")) {
						criterio.setColumn("D.dt_documento_saida");
						criterio.setColumn("dt_documento_saida");
					}
					else if (criterio.getColumn().equalsIgnoreCase("TP_ORIGEM")) {
						criterio.setColumn("D.tp_saida");
						criterio.setColumn("tp_saida");
					}
					criteriosTemp.add(criterio);
				}
				String sql = "SELECT A.cd_documento_saida, D.nr_documento_saida, A.cd_produto_servico, A.cd_empresa," +
				"A.cd_local_armazenamento, A.qt_saida, A.qt_saida_consignada, B.nm_local_armazenamento, B.cd_setor," +
				"C.nm_setor, D.dt_documento_saida, E.nm_produto_servico, E.tp_produto_servico, G.vl_unitario, G.vl_acrescimo, G.vl_desconto, " +
				"E.id_produto_servico, E.sg_produto_servico, F.st_produto_empresa, E.cd_classificacao_fiscal," +
				"D.tp_movimento_estoque, D.tp_saida, H.nm_pessoa " +
				"FROM alm_saida_local_item A " +
				"JOIN alm_documento_saida                    D ON (A.cd_documento_saida = D.cd_documento_saida AND " +
				"								                   A.cd_empresa = D.cd_empresa AND " +
				"								                   D.st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO + ") " +
				"JOIN alm_documento_saida_item               G ON (G.cd_produto_servico = A.cd_produto_servico AND " +
				"									               G.cd_documento_saida = A.cd_documento_saida AND " +
				"									               G.cd_empresa = A.cd_empresa) " +
				"JOIN alm_local_armazenamento                B ON (A.cd_local_armazenamento = B.cd_local_armazenamento) " +
				"LEFT OUTER JOIN grl_setor                   C ON (B.cd_setor = C.cd_setor) " +
				"LEFT OUTER JOIN grl_produto_servico         E ON (A.cd_produto_servico = E.cd_produto_servico)" +
				"LEFT OUTER JOIN grl_produto_servico_empresa F ON (E.cd_produto_servico = F.cd_produto_servico " +
				(cdEmpresa>0 ? "  AND F.cd_empresa = " + cdEmpresa : "") + ") " +
				"LEFT OUTER JOIN grl_pessoa                  H ON (D.cd_cliente = H.cd_pessoa) " +
				"WHERE 1=1 " +
				(cdProdutoServico>0 ? "  AND A.cd_produto_servico = " + cdProdutoServico : "") +
				(cdEmpresa>0 ? "  AND A.cd_empresa = " + cdEmpresa : "");
				
				ResultSetMap rsmTemp = Search.find(sql, criteriosTemp, true, connect!=null ? connect : Conexao.conectar(), connect==null);
				while (rsmTemp!=null && rsmTemp.next()) {
					HashMap<String,Object> saidas = new HashMap<String,Object>();
					saidas.put("TP_MOVIMENTO", new Integer(MOV_SAIDA));
					saidas.put("CD_MOVIMENTO", new Integer(rsmTemp.getInt("cd_documento_saida")));
					saidas.put("QT_MOVIMENTO", new Float(rsmTemp.getFloat("qt_saida")));
					saidas.put("VL_UNITARIO", new Float(rsmTemp.getFloat("VL_UNITARIO")));
					saidas.put("VL_MOVIMENTO", new Float(rsmTemp.getFloat("VL_UNITARIO") * rsmTemp.getFloat("qt_saida") + rsmTemp.getFloat("vl_acrescimo") - rsmTemp.getFloat("vl_desconto")));
					saidas.put("QT_MOVIMENTO_CONSIGNADO", new Float(rsmTemp.getFloat("qt_saida_consignada")));
					saidas.put("QT_MOVIMENTO_TOTAL", new Float(rsmTemp.getFloat("qt_saida") + rsmTemp.getFloat("qt_saida_consignada")));
					saidas.put("CD_LOCAL_ARMAZENAMENTO", new Integer(rsmTemp.getInt("cd_local_armazenamento")));
					saidas.put("NM_LOCAL_ARMAZENAMENTO", rsmTemp.getString("nm_local_armazenamento"));
					saidas.put("ID_MOVIMENTO", rsmTemp.getString("nr_documento_saida"));
					saidas.put("CD_SETOR", new Integer(rsmTemp.getInt("cd_setor")));
					saidas.put("NM_SETOR", rsmTemp.getString("nm_setor"));
					saidas.put("NM_PRODUTO_SERVICO", rsmTemp.getString("nm_produto_servico"));
					saidas.put("NM_PESSOA", rsmTemp.getString("nm_pessoa"));
					saidas.put("TP_ORIGEM", new Integer(rsmTemp.getInt("tp_saida")));
					saidas.put("DS_TIPO_ORIGEM", DocumentoSaidaServices.tiposSaida[rsmTemp.getInt("tp_saida")] + (rsmTemp.getInt("tp_saida")==DocumentoSaidaServices.SAI_VENDA || rsmTemp.getInt("tp_saida")==DocumentoSaidaServices.SAI_VENDA_CONSIGNACAO ? (rsmTemp.getInt("tp_movimento_estoque")==DocumentoSaidaServices.MOV_ESTOQUE_CONSIGNADO ? " - Estoque consignado" : "") : (rsmTemp.getInt("lg_estoque_consignado")==1 ? " - Consignação" : "")));
					saidas.put("DT_MOVIMENTO", Util.convTimestampToCalendar(rsmTemp.getTimestamp("dt_documento_saida")));
					saidas.put("DS_ORDEM", Util.formatDate(rsmTemp.getGregorianCalendar("dt_documento_saida"), "yyyyMMdd"));
					rsm.addRegister(saidas);
				}
			}

			ArrayList<String> criteriosOrdenacao = new ArrayList<String>();
			criteriosOrdenacao.add("DS_ORDEM");
			rsm.orderBy(criteriosOrdenacao);
			rsm.beforeFirst();

			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static HashMap<String, Object> findPontoPedido(int cdEmpresa, ArrayList<ItemComparator> criterios, ArrayList<String> orderByFields,
			int tpPeriodoVenda, GregorianCalendar dtInicialVenda, int qtDiasEntregaPadrao) {
		return findPontoPedido(cdEmpresa, criterios, orderByFields, tpPeriodoVenda, dtInicialVenda, qtDiasEntregaPadrao, null);
	}

	public static HashMap<String, Object> findPontoPedido(int cdEmpresa, ArrayList<ItemComparator> criterios, ArrayList<String> orderByFields,
			int tpPeriodoVenda, GregorianCalendar dtInicialVenda, int qtDiasEntregaPadrao, Connection connect) {
		ResultSetMap rsm = null;
		try {
			
			connect = connect !=null ? connect : Conexao.conectar();
			HashMap<String, Object> hash = new HashMap<String, Object>();
			int stEstoquePontoPedido = -1;
			for (int i=0; criterios!=null && i<criterios.size(); i++)
				if (criterios.get(i).getColumn().equalsIgnoreCase("stEstoquePontoPedido")) {
					stEstoquePontoPedido = Integer.parseInt(criterios.get(i).getValue());
					criterios.remove(i);
				}

			/* cadastro de produtos */
			rsm = Search.find("SELECT A.cd_produto_servico, A.nm_produto_servico, A.id_produto_servico, " +
					"B.id_reduzido, C.sg_unidade_medida, B.qt_minima, B.qt_maxima, D.cd_fornecedor, D.qt_dias_entrega, " +
					"E.nm_pessoa AS nm_fornecedor, F.nm_pessoa AS nm_fabricante, H.nm_grupo " +
					"FROM grl_produto_servico A " +
					"LEFT OUTER JOIN grl_produto_servico_empresa B ON (A.cd_produto_servico = B.cd_produto_servico AND " +
					"												   B.cd_empresa = " + cdEmpresa + ") " +
					"LEFT OUTER JOIN grl_unidade_medida C ON (B.cd_unidade_medida = C.cd_unidade_medida) " +
					"LEFT OUTER JOIN adm_produto_fornecedor D ON (B.cd_empresa = D.cd_empresa AND " +
					"											  B.cd_produto_servico = D.cd_produto_servico) " +
					"LEFT OUTER JOIN grl_pessoa E ON (D.cd_fornecedor = E.cd_pessoa) " +
					"LEFT OUTER JOIN grl_pessoa F ON (A.cd_fabricante = F.cd_pessoa) " +
					"LEFT OUTER JOIN alm_produto_grupo G ON (B.cd_produto_servico = G.cd_produto_servico AND " +
					"										 B.cd_empresa = G.cd_empresa AND " +
					"										 ((EXISTS (SELECT H.cd_grupo " +
					"												   FROM alm_produto_grupo H " +
					"												   WHERE B.cd_produto_servico = H.cd_produto_servico " +
					"												     AND B.cd_empresa = H.cd_empresa " +
					"												     AND H.lg_principal = 1) AND " +
					"										   G.cd_grupo = (SELECT MAX(H.cd_grupo) " +
					"													     FROM alm_produto_grupo H " +
					"													     WHERE B.cd_produto_servico = H.cd_produto_servico " +
					"														   AND B.cd_empresa = H.cd_empresa " +
					"														   AND H.lg_principal = 1)) OR " +
					"										  (NOT EXISTS (SELECT H.cd_grupo " +
					"													   FROM alm_produto_grupo H " +
					"													   WHERE B.cd_produto_servico = H.cd_produto_servico " +
					"														 AND B.cd_empresa = H.cd_empresa " +
					"														 AND H.lg_principal = 1) AND " +
					"										   G.cd_grupo = (SELECT MAX(H.cd_grupo) " +
					"														 FROM alm_produto_grupo H " +
					"														 WHERE H.cd_produto_servico = H.cd_produto_servico " +
					"														   AND H.cd_empresa = H.cd_empresa)))) " +
					"LEFT OUTER JOIN alm_grupo H ON (G.cd_grupo = H.cd_grupo) " +
					"WHERE NOT A.cd_produto_servico IN (SELECT D.cd_disciplina " +
					"									FROM acd_disciplina D " +
					"									WHERE D.cd_disciplina = A.cd_produto_servico) " +
					"  AND NOT A.cd_produto_servico IN (SELECT D.cd_curso " +
					"									FROM acd_curso D " +
					"									WHERE D.cd_curso = A.cd_produto_servico) " +
					"  AND NOT A.cd_produto_servico IN (SELECT E.cd_produto_servico_componente " +
					"									FROM acd_curso_periodo D, grl_produto_servico_composicao E " +
					"									WHERE D.cd_curso_periodo = E.cd_composicao " +
					"									  AND E.cd_produto_servico_componente = A.cd_produto_servico) " +
					"  AND NOT A.nm_produto_servico IS NULL " +
					"  AND A.nm_produto_servico <> '' " +
					"  AND NOT A.cd_produto_servico IN (SELECT D.cd_aplicacao " +
					"									FROM cms_aplicacao D " +
					"									WHERE D.cd_aplicacao = A.cd_produto_servico) ",
					criterios, true, connect, connect==null);

			/* saidas */
			ArrayList<ItemComparator> crtSaidas = new ArrayList<ItemComparator>();
			GregorianCalendar dtFinal  = new GregorianCalendar();
			GregorianCalendar dtInicial = (GregorianCalendar)dtFinal.clone();
			switch(tpPeriodoVenda) {
				case PER_VEND_30_DIAS:
					dtInicial.add(Calendar.MONTH, -1); break;
				case PER_VEND_60_DIAS:
					dtInicial.add(Calendar.MONTH, -2); break;
				case PER_VEND_90_DIAS:
					dtInicial.add(Calendar.MONTH, -3); break;
				case PER_VEND_SEMESTRE:
					dtInicial.add(Calendar.MONTH, -6); break;
				case PER_VEND_ANO:
					dtInicial.add(Calendar.YEAR, -1); break;
				case PER_VEND_CUSTOMIZAVEL:
					dtInicial = dtInicialVenda;
			}
			int countDias = DateServices.countDaysBetween(dtInicial, dtFinal);
			crtSaidas.add(new ItemComparator("A.dt_documento_saida", Util.formatDateTime(dtInicial, "MM/dd/yyyy"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
			crtSaidas.add(new ItemComparator("A.dt_documento_saida", Util.formatDateTime(dtFinal, "MM/dd/yyyy")+ " 23:59:99:999", ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
			ResultSetMap rsmVendas = Search.find("SELECT A.cd_produto_servico, SUM(qt_saida)/" + countDias + " AS VL_MEDIA_VENDAS, " +
					"SUM(qt_saida) AS qt_vendas " +
					"FROM alm_documento_saida_item A, alm_documento_saida B " +
					"WHERE A.cd_documento_saida = B.cd_documento_saida " +
					"  AND B.st_documento_saida = "  + DocumentoSaidaServices.ST_CONCLUIDO +
					"  AND (B.tp_saida = " + DocumentoSaidaServices.SAI_VENDA + " OR B.tp_saida = " + DocumentoSaidaServices.SAI_VENDA_CONSIGNACAO + ")" +
					"  AND B.cd_empresa = " + cdEmpresa, " GROUP BY A.cd_produto_servico", new ArrayList<ItemComparator>(), connect, connect == null);
			for(int i = 0; i < rsmVendas.size(); i++) {
				HashMap<String, Object> register = rsmVendas.getLines().get(i);
				int cdProdutoServico = (int) register.get("CD_PRODUTO_SERVICO");
				while(rsm.next()){
					if(rsm.getInt("CD_PRODUTO_SERVICO") == cdProdutoServico) {
						rsm.setValueToField("VL_MEDIA_VENDAS", (double)register.get("VL_MEDIA_VENDAS"));
						rsm.setValueToField("QT_VENDAS", (double)register.get("QT_VENDAS"));
					}
				}
				rsm.beforeFirst();
			}

			/* estoques - entradas */
			ResultSetMap rsmEntradas = Search.find("SELECT A.cd_produto_servico, SUM(A.qt_entrada) AS qt_entrada, " +
					"SUM(A.qt_entrada_consignada) AS qt_entrada_consignada " +
					"FROM alm_entrada_local_item A, alm_documento_entrada_item B, alm_documento_entrada C " +
					"WHERE A.cd_produto_servico = B.cd_produto_servico " +
					"  AND A.cd_documento_entrada = B.cd_documento_entrada " +
					"  AND A.cd_empresa = B.cd_empresa " +
					"  AND B.cd_documento_entrada = C.cd_documento_entrada " +
					"  AND C.st_documento_entrada = "  + DocumentoEntradaServices.ST_LIBERADO +
					"  AND C.cd_empresa = " + cdEmpresa,
					"GROUP BY A.cd_produto_servico", new ArrayList<ItemComparator>(), connect, connect==null);
			while (rsmEntradas.next()) {
				if (rsm.locate("cd_produto_servico", rsmEntradas.getInt("cd_produto_servico"), false, false)) {
					rsm.getRegister().put("QT_ENTRADA", rsmEntradas.getFloat("qt_entrada"));
					rsm.getRegister().put("QT_ENTRADA_CONSIGNADA", rsmEntradas.getFloat("qt_entrada_consignada"));
				}
			}

			/* estoques - entradas */
			ResultSetMap rsmSaidas = Search.find("SELECT A.cd_produto_servico, SUM(A.qt_saida) AS qt_saida, " +
					"SUM(A.qt_saida_consignada) AS qt_saida_consignada " +
					"FROM alm_saida_local_item A, alm_documento_saida_item B, alm_documento_saida C " +
					"WHERE A.cd_produto_servico = B.cd_produto_servico " +
					"  AND A.cd_documento_saida = B.cd_documento_saida " +
					"  AND A.cd_empresa = B.cd_empresa " +
					"  AND B.cd_documento_saida = C.cd_documento_saida " +
					"  AND C.st_documento_saida = "  + DocumentoSaidaServices.ST_CONCLUIDO +
					"  AND C.cd_empresa = " + cdEmpresa,
					"GROUP BY A.cd_produto_servico", new ArrayList<ItemComparator>(), connect, connect==null);
			while (rsmSaidas.next()) {
				if (rsm.locate("cd_produto_servico", rsmSaidas.getInt("cd_produto_servico"), false, false)) {
					rsm.getRegister().put("QT_SAIDA", rsmSaidas.getFloat("QT_SAIDA"));
					rsm.getRegister().put("QT_SAIDA_CONSIGNADA", rsmSaidas.getFloat("QT_SAIDA_CONSIGNADA"));
				}
			}

			rsm.beforeFirst();
			while (rsm.next()) {
				HashMap<String, Object> register = rsm.getRegister();
				register.put("QT_DIAS_ENTREGA", rsm.getInt("qt_dias_entrega")<=0 ? qtDiasEntregaPadrao : rsm.getInt("qt_dias_entrega"));
				float qtEstoque = rsm.getFloat("QT_ENTRADA") + rsm.getFloat("QT_ENTRADA_CONSIGNADA") -
									rsm.getFloat("QT_SAIDA") - rsm.getFloat("QT_SAIDA_CONSIGNADA");
				float qtPontoPedido = rsm.getFloat("VL_MEDIA_VENDAS") * rsm.getInt("qt_dias_entrega") + rsm.getFloat("qt_minima");
				register.put("QT_ESTOQUE", qtEstoque);
				register.put("QT_PONTO_PEDIDO", qtPontoPedido);
			}


			if (stEstoquePontoPedido!=-1) {
				rsm.beforeFirst();
				while (rsm.next()) {
					float qtEstoque = rsm.getFloat("QT_ESTOQUE");
					float qtPontoPedido = rsm.getFloat("QT_PONTO_PEDIDO");
					if (stEstoquePontoPedido==ST_ESTOQUE_ABAIXO_PP && qtEstoque>qtPontoPedido) {
						rsm.deleteRow();
						if (rsm.getPointer()==0)
							rsm.beforeFirst();
						else
							rsm.previous();
					}
					else if (stEstoquePontoPedido==ST_ESTOQUE_ACIMA_PP && qtEstoque<=qtPontoPedido) {
						rsm.deleteRow();
						if (rsm.getPointer()==0)
							rsm.beforeFirst();
						else
							rsm.previous();
					}
				}
			}

			if (orderByFields != null && orderByFields.size()>0)
				rsm.orderBy(orderByFields);
			rsm.beforeFirst();
			
			hash.put("resultSet", rsm);
			hash.put("countRegisters", rsm.size());
			return hash;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}

	public static ResultSetMap findCurvaABC(int cdEmpresa, ArrayList<ItemComparator> criterios, ArrayList<String> orderByFields) {
		return findCurvaABC(cdEmpresa, criterios, orderByFields, null);
	}

	public static ResultSetMap findCurvaABC(int cdEmpresa, ArrayList<ItemComparator> criterios, ArrayList<String> orderByFields,
			Connection connect) {
		ResultSetMap rsm = null;
		try {
			/* cadastro de produtos */
			String sql = "SELECT A.cd_produto_servico, B.id_reduzido, C.nm_produto_servico, " +
					"D.cd_pessoa AS cd_fabricante, D.nm_pessoa AS nm_fabricante, H.cd_grupo, H.nm_grupo, " +
					"E.cd_unidade_medida, E.sg_unidade_medida, " +
					"SUM(A.vl_unitario * A.qt_saida + A.vl_acrescimo - A.vl_desconto) AS vl_faturamento, " +
					"SUM(qt_saida) AS qt_vendas " +
					"FROM alm_documento_saida_item A " +
					"JOIN alm_documento_saida F ON (A.cd_documento_saida = F.cd_documento_saida) " +
					"JOIN grl_produto_servico_empresa B ON (A.cd_produto_servico = B.cd_produto_servico AND " +
					"										A.cd_empresa = B.cd_empresa) " +
					"JOIN grl_produto_servico C ON (A.cd_produto_servico = C.cd_produto_servico) " +
					"LEFT OUTER JOIN grl_pessoa D ON (C.cd_fabricante = D.cd_pessoa) " +
					"LEFT OUTER JOIN grl_unidade_medida E ON (B.cd_unidade_medida = E.cd_unidade_medida) " +
					"LEFT OUTER JOIN alm_produto_grupo G ON (B.cd_produto_servico = G.cd_produto_servico AND " +
					"										 B.cd_empresa = G.cd_empresa AND " +
					"										 ((EXISTS (SELECT H.cd_grupo " +
					"												   FROM alm_produto_grupo H " +
					"												   WHERE B.cd_produto_servico = H.cd_produto_servico " +
					"												     AND B.cd_empresa = H.cd_empresa " +
					"												     AND H.lg_principal = 1) AND " +
					"										   G.cd_grupo = (SELECT MAX(H.cd_grupo) " +
					"													     FROM alm_produto_grupo H " +
					"													     WHERE B.cd_produto_servico = H.cd_produto_servico " +
					"														   AND B.cd_empresa = H.cd_empresa " +
					"														   AND H.lg_principal = 1)) OR " +
					"										  (NOT EXISTS (SELECT H.cd_grupo " +
					"													   FROM alm_produto_grupo H " +
					"													   WHERE B.cd_produto_servico = H.cd_produto_servico " +
					"														 AND B.cd_empresa = H.cd_empresa " +
					"														 AND H.lg_principal = 1) AND " +
					"										   G.cd_grupo = (SELECT MAX(H.cd_grupo) " +
					"														 FROM alm_produto_grupo H " +
					"														 WHERE H.cd_produto_servico = H.cd_produto_servico " +
					"														   AND H.cd_empresa = H.cd_empresa)))) " +
					"LEFT OUTER JOIN alm_grupo H ON (G.cd_grupo = H.cd_grupo) " +
					"WHERE A.cd_empresa = " + cdEmpresa + " " +
					"  AND F.st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO + " " +
					"  AND (F.tp_saida = " + DocumentoSaidaServices.SAI_VENDA + " OR F.tp_saida = " + DocumentoSaidaServices.SAI_VENDA_CONSIGNACAO + ") ";
			rsm = Search.find(sql,
					"GROUP BY A.cd_produto_servico, B.id_reduzido, C.nm_produto_servico, " +
					"D.cd_pessoa, D.nm_pessoa, H.cd_grupo, H.nm_grupo, " +
					"E.cd_unidade_medida, E.sg_unidade_medida", criterios, connect!=null ? connect : Conexao.conectar(), connect==null,
							false);

			/* calculo do valor total faturado */
			rsm.beforeFirst();
			float vlTotalFaturamento = 0;
			while (rsm.next()) {
				vlTotalFaturamento += rsm.getFloat("vl_faturamento");
			}

			/* calculo de % de faturamento */
			rsm.beforeFirst();
			while (rsm.next()) {
				float prFaturamento = vlTotalFaturamento==0 ? 0 : rsm.getFloat("vl_faturamento") * (float)100 / vlTotalFaturamento;
				rsm.getRegister().put("PR_FATURAMENTO", prFaturamento);
			}

			/* ordenamento de itens por faturamento descrescente */
			rsm.beforeFirst();
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("vl_faturamento DESC");
			rsm.orderBy(fields);

			/* classificacao de produtos em classe, com adocao do criterio A = 20%, B = 30% e C = 50% */
			double[] topClasses = {(int)Math.round(0.2 * rsm.size()), (int)Math.round(0.5 * rsm.size()), (int)Math.round(1 * rsm.size())};
			rsm.beforeFirst();
			for (int i=1; rsm.next(); i++) {
				if (i>=0 && i<=topClasses[0])
					rsm.getRegister().put("TP_CLASSE", TP_CLASSE_A);
				else if (i>topClasses[0] && i<= topClasses[1])
					rsm.getRegister().put("TP_CLASSE", TP_CLASSE_B);
				else
					rsm.getRegister().put("TP_CLASSE", TP_CLASSE_C);
			}

			if (orderByFields != null && orderByFields.size()>0)
				rsm.orderBy(orderByFields);
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	/**
	 * 
	 * @param cdEmpresa
	 * @param criterios
	 * @return
	 * @see #gerarRelatorioConsolidacaoEstoqueProduto(int, ArrayList, Connection)
	 */
	public Result gerarRelatorioConsolicaoEstoqueProduto(int cdEmpresa, ArrayList<ItemComparator> criterios){
		return gerarRelatorioConsolidacaoEstoqueProduto(cdEmpresa, criterios, null);
	}
	
	/**
	 * 
	 * Método que gera o relatório de consolidação de estoque de produtos
	 * 
	 * @param cdEmpresa
	 * @param criterios
	 * @return consulta baseada nos critérios da tela
	 * @author Luiz Romario Filho
	 * @since 06/10/2014
	 */
	public Result gerarRelatorioConsolidacaoEstoqueProduto(int cdEmpresa, ArrayList<ItemComparator> criterios, Connection connect){
		String dataInicio = "";
		String dataFim ="";
		String cdLocalArmazenamento = "";
		String locais = "";
		String categorias = "";
		String subCategorias = "";
		String cdCategorias = "";
		String cdSubCategorias = "";
		int cdProduto = 0;
		int stPreco = -1;
		int cdTabelaPreco = 0;
		int situacao = -1;
		int cdFornecedor = -1;
		int ordemImpressao = -1;
		int cdGrupoCombustivel = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa);
		
		for (ItemComparator itemComparator : criterios) {
			if("dtMovimentoInicio".equalsIgnoreCase(itemComparator.getColumn())){
				dataInicio = itemComparator.getValue();
			} else if("dtMovimentoFim".equalsIgnoreCase(itemComparator.getColumn())){
				dataFim = itemComparator.getValue();
			} else if (itemComparator.getColumn().equalsIgnoreCase("ordemImpressao")) {
				ordemImpressao = Integer.parseInt(itemComparator.getValue());
			} else if("cdLocalArmazenamento".equalsIgnoreCase(itemComparator.getColumn())){
				cdLocalArmazenamento= itemComparator.getValue();
			} else if("nmLocalArmazenamento".equalsIgnoreCase(itemComparator.getColumn())){
				locais= itemComparator.getValue();
			} else if("nmCategoria".equalsIgnoreCase(itemComparator.getColumn())){
				categorias= itemComparator.getValue();
			} else if("nmSubCategoria".equalsIgnoreCase(itemComparator.getColumn())){
				subCategorias= itemComparator.getValue();
			} else if("cdCategoria".equalsIgnoreCase(itemComparator.getColumn())){
				cdCategorias= itemComparator.getValue();
			} else if("cdSubCategoria".equalsIgnoreCase(itemComparator.getColumn())){
				cdSubCategorias= itemComparator.getValue();
			} else if("cdProduto".equalsIgnoreCase(itemComparator.getColumn())){
				cdProduto= Integer.parseInt(itemComparator.getValue());
			} else if (itemComparator.getColumn().equalsIgnoreCase("stPreco")) {
				stPreco = Integer.parseInt(itemComparator.getValue());
			} else if (itemComparator.getColumn().equalsIgnoreCase("cdTabelaPreco")) {
				cdTabelaPreco = Integer.parseInt(itemComparator.getValue());
			} else if (itemComparator.getColumn().equalsIgnoreCase("situacao")) {
				situacao = Integer.parseInt(itemComparator.getValue());
			} else if (itemComparator.getColumn().equalsIgnoreCase("cdFornecedor")) {
				cdFornecedor = Integer.parseInt(itemComparator.getValue());
			}
		}
		boolean isConnectionNull = connect == null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			// pega os produtos ordenados e agrupados por categoria e subcategoria
			ResultSetMap rsm = getProdutosPorGrupoESubGrupo(cdEmpresa, connect, cdLocalArmazenamento, stPreco, cdTabelaPreco, cdFornecedor, ordemImpressao, cdGrupoCombustivel, cdCategorias, cdSubCategorias, cdProduto, situacao);
			
			
			
			Result result = new Result(1, "Sucesso!");
			HashMap<String, Object> param = new HashMap<String, Object>();
			
			
			GregorianCalendar dtMovimentoInicio = Util.convStringToCalendar(dataInicio);
			dtMovimentoInicio.add(Calendar.DAY_OF_MONTH, -1);
			dtMovimentoInicio.set(Calendar.HOUR_OF_DAY, 23);
			dtMovimentoInicio.set(Calendar.MINUTE, 59);
			dtMovimentoInicio.set(Calendar.SECOND, 59);
			
			GregorianCalendar dtMovimentoFim = Util.convStringToCalendar(dataFim);
			dtMovimentoFim.set(Calendar.HOUR_OF_DAY, 23);
			dtMovimentoFim.set(Calendar.MINUTE, 59);
			dtMovimentoFim.set(Calendar.SECOND, 59);
			
			while(rsm.next()){
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cdLocalArmazenamento", "" + cdLocalArmazenamento, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cdEmpresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cdProdutoServico", "" + rsm.getInt("cd_produto_servico"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("dtMovimento", "" + Util.formatDate(dtMovimentoInicio, "dd/MM/yyyy"), ItemComparator.EQUAL, Types.VARCHAR));
				Result resultado = ProdutoEstoqueServices.getEstoqueAtual(criterios);
				
				float qtEstoqueInicial = (Float)resultado.getObjects().get("QT_ESTOQUE") + (Float)resultado.getObjects().get("QT_ESTOQUE_CONSIGNADO");
				float qtCompras = ((Float)resultado.getObjects().get("QT_ENTRADA") + (Float)resultado.getObjects().get("QT_ENTRADA_CONSIGNADA"));
				float qtVendas = ((Float)resultado.getObjects().get("QT_SAIDA") + (resultado.getObjects().get("QT_SAIDA_CONSIGNADA") != null ? (Float)resultado.getObjects().get("QT_SAIDA_CONSIGNADA") : 0));
				
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cdLocalArmazenamento", "" + cdLocalArmazenamento, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cdEmpresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cdProdutoServico", "" + rsm.getInt("cd_produto_servico"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("dtMovimento", "" + Util.formatDate(dtMovimentoFim, "dd/MM/yyyy"), ItemComparator.EQUAL, Types.VARCHAR));
				resultado = ProdutoEstoqueServices.getEstoqueAtual(criterios);
				
				if(!Util.formatDate(dtMovimentoInicio, "dd/MM/yyyy").equals(Util.formatDate(dtMovimentoFim, "dd/MM/yyyy"))){
					qtCompras = ((Float)resultado.getObjects().get("QT_ENTRADA") + (Float)resultado.getObjects().get("QT_ENTRADA_CONSIGNADA")) - qtCompras;
					qtVendas  = ((Float)resultado.getObjects().get("QT_SAIDA") + (resultado.getObjects().get("QT_SAIDA_CONSIGNADA") != null ? (Float)resultado.getObjects().get("QT_SAIDA_CONSIGNADA") : 0)) - qtVendas;
				}
				
				float qtEstoqueFinal = qtEstoqueInicial + qtCompras - qtVendas;
				float vlEstoqueFinal = getValorEstoqueFinal(rsm.getInt("CD_PRODUTO_SERVICO"), qtEstoqueFinal, connect, cdEmpresa);
				
				rsm.setValueToField("QT_ESTOQUE_INICIAL", qtEstoqueInicial);
				rsm.setValueToField("QT_SAIDA", qtVendas);
				rsm.setValueToField("QT_ENTRADA", qtCompras);
				rsm.setValueToField("QT_ESTOQUE_FINAL", qtEstoqueFinal);
				rsm.setValueToField("VL_ESTOQUE_FINAL", vlEstoqueFinal);
				
				
			}
			rsm.beforeFirst();
	 		
			//Adiciona o período
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			param.put("dataMovimentoInicio", simpleDateFormat.format(Util.convStringToCalendar(dataInicio).getTime()));
			param.put("dataMovimentoFim", simpleDateFormat.format(Util.convStringToCalendar(dataFim).getTime()));
			
			
			// Adiciona o nome dos locais de armazenamento somente se selecionar algum
			if(locais == null || "".equalsIgnoreCase(locais)){
				param.put("localArmazenamento","Todos");
			} else{
				param.put("localArmazenamento",locais);
			}
			
			// Adiciona o nome dos tipos de combustível
			//se não veio nem categoria nem subcategoria
			if((categorias == null || "".equalsIgnoreCase(categorias)) && (subCategorias == null || "".equals(subCategorias))){
				param.put("tipoProduto","Todos os produtos");
			//se veio categoria mas não subcategoria
			} else if((categorias != null && !"".equalsIgnoreCase(categorias)) && (subCategorias == null && "".equals(subCategorias))){
				param.put("tipoProduto",categorias);
			//se não veio categoria mas veio subcategoria
			} else if((categorias == null || "".equalsIgnoreCase(categorias)) && (subCategorias != null && !"".equals(subCategorias))){
				param.put("tipoProduto",subCategorias);
			//se veio categoria e veio subcategoria
			} else {
				param.put("tipoProduto",categorias + ", " + subCategorias);
			}
			// Adiciona a ordem saída
			if(ordemImpressao == 1){
				param.put("ordemSaida","Descrição do produto");
			} else{
				param.put("ordemSaida","Código do produto");
			}
			
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			return result;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
	 * Método que calcula o valor do estoque final de um produto
	 * 
	 * @param cdProduto
	 * @param qtEstoqueFinal
	 * @return
	 * @author Luiz Romario Filho
	 * @throws SQLException 
	 * @since 28/10/2014
	 */
	private float getValorEstoqueFinal(int cdProduto, float qtEstoqueFinal, Connection connect, int cdEmpresa) throws SQLException {
		float valorEstoqueFinal = 0;
		PreparedStatement pstmt = connect.prepareStatement(
				"SELECT vl_custo_medio "
				+ " FROM grl_produto_servico_empresa"
				+ " WHERE cd_empresa = " + cdEmpresa 
				+ " AND cd_produto_servico = " + cdProduto);
		ResultSetMap rs = new ResultSetMap(pstmt.executeQuery());
		while (rs.next()) {
			valorEstoqueFinal = qtEstoqueFinal * rs.getFloat("VL_CUSTO_MEDIO");
		}
		rs.beforeFirst();
		return valorEstoqueFinal;
	}
	
	/**
	 * Retorna os produtos separados por grupos e subgrupos, filtrados pelos parametros passados
	 * 
	 * @param cdEmpresa
	 * @param connect
	 * @param cdLocalArmazenamento
	 * @param stPreco
	 * @param cdTabelaPreco
	 * @param cdFornecedor
	 * @param ordemImpressao
	 * @param cdGrupoCombustivel
	 * @return
	 * @author Luiz Romario Filho
	 * @since 22/10/2014
	 */
	private ResultSetMap getProdutosPorGrupoESubGrupo(int cdEmpresa, Connection connect, String cdLocalArmazenamento, int stPreco, int cdTabelaPreco, int cdFornecedor, int ordemImpressao,
			int cdGrupoCombustivel, String cdCategorias, String cdSubCategorias, int cdProduto, int situacao) throws Exception {
		String sql = "SELECT A.cd_produto_servico, B.id_reduzido, A.nm_produto_servico, " +
			 " F.sg_unidade_medida, F.cd_unidade_medida, I.cd_grupo, I.nm_grupo as NM_SUB_GRUPO, I2.nm_grupo AS NM_GRUPO, B.st_produto_empresa " +
			 (cdTabelaPreco>0?", G.vl_preco ":" ") + 
			 " FROM grl_produto_servico A " +
			 " JOIN grl_produto                            E ON (A.cd_produto_servico = E.cd_produto_servico) " +
			 " LEFT OUTER JOIN grl_produto_servico_empresa B ON (E.cd_produto_servico = B.cd_produto_servico " +
			 "										        AND B.cd_empresa = "+cdEmpresa+") " +
			 " LEFT OUTER JOIN grl_pessoa                  D ON (A.cd_fabricante = D.cd_pessoa) " +
			 " LEFT OUTER JOIN grl_unidade_medida          F ON (B.cd_unidade_medida = F.cd_unidade_medida) " +
			 /* RETORNAR PREÇO */
			 (cdTabelaPreco>0?" LEFT OUTER JOIN adm_produto_servico_preco G ON (A.cd_produto_servico = G.cd_produto_servico" +
			                  "                                            AND G.dt_termino_validade IS NULL " +
			                  "                                            AND G.cd_tabela_preco = "+cdTabelaPreco+") ":"") +
			 " LEFT OUTER JOIN alm_produto_grupo           H ON (B.cd_produto_servico = H.cd_produto_servico " +
			 "									            AND B.cd_empresa         = H.cd_empresa " +
			 "									            AND H.lg_principal       = 1) "+
			 " LEFT OUTER JOIN alm_grupo                   I ON (H.cd_grupo = I.cd_grupo AND I.cd_grupo_superior <>  "+ cdGrupoCombustivel + ") "+						 
			 " LEFT OUTER JOIN alm_grupo                   I2 ON (I2.cd_grupo = I.cd_grupo_superior AND I2.cd_grupo <>  "+ cdGrupoCombustivel + ") "+						 
			 " WHERE 1=1 AND I.cd_grupo_superior <>  "+ cdGrupoCombustivel + " AND I2.cd_grupo <>  "+ cdGrupoCombustivel +
			 (situacao == 0 ? " AND B.st_produto_empresa = 0" : " AND B.st_produto_empresa = 1 ") + //0 - ativo; 1 - inativo
			 (cdProduto > 0 ? " AND A.cd_produto_servico = " + cdProduto + " ": " ") + 
			 (cdCategorias != null && !"".equals(cdCategorias) ? "AND I.cd_grupo_superior IN ( "+ cdCategorias +" )" : "") + 
			 (cdSubCategorias != null && !"".equals(cdSubCategorias) ? "AND I.cd_grupo IN ( "+ cdSubCategorias +" )" : "") + 
			 // Filtro por fornecedor
			 (cdFornecedor>0 ? "  AND EXISTS (SELECT * FROM alm_documento_entrada A1, alm_documento_entrada_item B1 " +
							   "			  WHERE B1.cd_produto_servico   = A.cd_produto_servico" +
							   "                AND B1.cd_documento_entrada = A1.cd_documento_entrada " +
							   "			    AND B1.cd_empresa           = " +cdEmpresa+
							   "			    AND A1.cd_empresa           = " +cdEmpresa+
							   "			    AND A1.st_documento_entrada = " +DocumentoEntradaServices.ST_LIBERADO+
							   " 		        AND A1.tp_entrada IN ("+DocumentoEntradaServices.ENT_COMPRA+ ","+DocumentoEntradaServices.ENT_CONSIGNACAO+")" +
							   "                AND A1.cd_fornecedor        = "+cdFornecedor+")" : "") +
			 (stPreco!=-1 ? " AND" + (stPreco==1 ? " NOT" : "") + " EXISTS (SELECT * FROM adm_produto_servico_preco G " +
			                                                      "		    WHERE G.cd_produto_servico = A.cd_produto_servico " +
			                                                      "			  AND G.dt_termino_validade IS NULL " +						 
			 (cdTabelaPreco>0?" AND G.cd_tabela_preco = " + cdTabelaPreco : "") + ") " : "");
		
		
		/*
		 * ordem de impressão == 0 -> código
		 * ordem de impressão == 1 -> descrição
		 */
		String specialOrder = "";
		if(ordemImpressao!= -1){
			specialOrder = ordemImpressao == 0 ? " A.cd_produto_servico " : " A.nm_produto_servico ";
		}
		sql += " ORDER BY I2.nm_grupo, I.nm_grupo, "+ (specialOrder != null && !"".equals(specialOrder.trim()) ? specialOrder : " A.nm_produto_servico ");
		
		/* 
		 * SEARCH
		 */
		ResultSetMap rsm = Search.find(sql, null, false,  connect, false);
		return rsm;
	}
	
	/**
	 * 
	 * @param cdEmpresa
	 * @param criterios
	 * @return
	 * @see #gerarRelatorioConsolicaoEstoqueCombustivel(int, ArrayList, Connection)
	 */
	public Result gerarRelatorioConsolicaoEstoqueCombustivel(int cdEmpresa, ArrayList<ItemComparator> criterios){
		return gerarRelatorioConsolicaoEstoqueCombustivel(cdEmpresa, criterios, null);
	}
	
	/**
	 * 
	 * Método que gera o relatório de consolidação de estoque de combustível
	 * 
	 * @param cdEmpresa
	 * @param criterios
	 * @return consulta baseada nos critérios da tela
	 * @author Luiz Romario Filho
	 * @since 06/10/2014
	 */
	public Result gerarRelatorioConsolicaoEstoqueCombustivel(int cdEmpresa, ArrayList<ItemComparator> criterios, Connection connect){
		String dataInicio = "";
		String dataFim ="";
		String cdLocalArmazenamento = "";
		String nmLocais = "";
		String categorias = "";
		String cdCategorias = "";
		String produto = "";
		int stPreco = -1;
		int cdTabelaPreco = 0;
		int situacao = -1;
		int cdFornecedor = -1;
		GregorianCalendar dtInicio = new GregorianCalendar();
		GregorianCalendar dtFim    = new GregorianCalendar();
		
		for (ItemComparator itemComparator : criterios) {
			if("dtMovimentoInicio".equalsIgnoreCase(itemComparator.getColumn())){
				dataInicio = itemComparator.getValue();
				dtInicio = Util.convStringToCalendar(dataInicio);
			} else if("dtMovimentoFim".equalsIgnoreCase(itemComparator.getColumn())){
				dataFim = itemComparator.getValue();
				dtFim = Util.convStringToCalendar(dataFim);
				dtFim.add(Calendar.DAY_OF_MONTH, 1);
			} else if("cdLocalArmazenamento".equalsIgnoreCase(itemComparator.getColumn())){
				cdLocalArmazenamento= itemComparator.getValue();
			} else if("nmLocalArmazenamento".equalsIgnoreCase(itemComparator.getColumn())){
				nmLocais= itemComparator.getValue();
			} else if("nmSubCategoria".equalsIgnoreCase(itemComparator.getColumn())){
				categorias= itemComparator.getValue();
			} else if("cdSubCategoria".equalsIgnoreCase(itemComparator.getColumn())){
				cdCategorias += itemComparator.getValue();
			} else if("cdProduto".equalsIgnoreCase(itemComparator.getColumn())){
				produto= itemComparator.getValue();
			} else if (itemComparator.getColumn().equalsIgnoreCase("stPreco")) {
				stPreco = Integer.parseInt(itemComparator.getValue());
			} else if (itemComparator.getColumn().equalsIgnoreCase("cdTabelaPreco")) {
				cdTabelaPreco = Integer.parseInt(itemComparator.getValue());
			} else if (itemComparator.getColumn().equalsIgnoreCase("situacao")) {
				situacao = Integer.parseInt(itemComparator.getValue());
			} else if (itemComparator.getColumn().equalsIgnoreCase("cdFornecedor")) {
				cdFornecedor = Integer.parseInt(itemComparator.getValue());
			}
		}
		int cdGrupoCombustivel = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa);
		boolean isConnectionNull = connect == null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			double qtMedicaoFisica = 0.0;
			double qtMedicaoFisicaProximo = 0.0;
			double qtDiferenca = 0.0;
			// Obtem os combustiveis
			String sqlCombustiveis = "SELECT A.cd_produto_servico, A.nm_produto_servico "
										+ " FROM grl_produto_servico A "
										+ " JOIN alm_produto_grupo B ON b.cd_produto_servico = A.cd_produto_servico " 
										+ " JOIN alm_grupo C ON C.cd_grupo = B.cd_grupo "
										+ " LEFT JOIN grl_produto_servico_empresa E ON E.cd_produto_servico = A.cd_produto_servico and E.cd_empresa = " + cdEmpresa
										+ (cdTabelaPreco > 0 ? " LEFT OUTER JOIN adm_produto_servico_preco G ON (A.cd_produto_servico = G.cd_produto_servico AND G.dt_termino_validade IS NULL AND G.cd_tabela_preco = "+cdTabelaPreco+") ":"")
										+ " WHERE C.cd_grupo_superior = " + cdGrupoCombustivel
										// filtra por grupo
										+ (cdCategorias != null && !"".equals(cdCategorias) ? " AND C.cd_grupo IN ( " + cdCategorias + " ) " : "")
										+ (produto != null && !"".equals(produto.trim()) ? " AND A.cd_produto_servico = " + produto : "")
										+ (situacao != -1 ? " AND E.st_produto_empresa = " + situacao : "")
										+ (stPreco!=-1 ? " AND" + (stPreco==1 ? " NOT" : "") + " EXISTS (SELECT * FROM adm_produto_servico_preco G WHERE G.cd_produto_servico = A.cd_produto_servico AND G.dt_termino_validade IS NULL " +(cdTabelaPreco>0?" AND G.cd_tabela_preco = " + cdTabelaPreco : "") + ")" : "")
										// Filtro por fornecedor
										+ (cdFornecedor>0 ? "  AND EXISTS (SELECT * FROM alm_documento_entrada A1, alm_documento_entrada_item B1 " +
								 					   "			  WHERE B1.cd_produto_servico   = A.cd_produto_servico" +
								 					   "                AND B1.cd_documento_entrada = A1.cd_documento_entrada " +
						 							   "			    AND B1.cd_empresa           = " +cdEmpresa+
							 						   "			    AND A1.cd_empresa           = " +cdEmpresa+
					 								   "			    AND A1.st_documento_entrada = " +DocumentoEntradaServices.ST_LIBERADO+
				 									   " 		        AND A1.tp_entrada IN ("+DocumentoEntradaServices.ENT_COMPRA+ ","+DocumentoEntradaServices.ENT_CONSIGNACAO+")" +
													   "                AND A1.cd_fornecedor        = "+cdFornecedor+")" : "") 
										+ " ORDER BY A.nm_produto_servico ";
			ResultSetMap rsmCombustiveis = Search.find(sqlCombustiveis, null, false, connect, false);
			
			ResultSetMap rsmCombFinal = new ResultSetMap();
			while(rsmCombustiveis.next()){
				
				GregorianCalendar dtTemp = new GregorianCalendar();
				dtTemp.set(Calendar.DAY_OF_MONTH, dtInicio.get(Calendar.DAY_OF_MONTH));
				dtTemp.set(Calendar.MONTH, dtInicio.get(Calendar.MONTH));
				dtTemp.set(Calendar.YEAR, dtInicio.get(Calendar.YEAR));
				dtTemp.set(Calendar.HOUR_OF_DAY, dtInicio.get(Calendar.HOUR_OF_DAY));
				dtTemp.set(Calendar.MINUTE, dtInicio.get(Calendar.MINUTE));
				dtTemp.set(Calendar.SECOND, dtInicio.get(Calendar.SECOND));
				
				for(;dtTemp.before(dtFim);dtTemp.add(Calendar.DAY_OF_MONTH, 1)){
					
					HashMap<String, Object> register = new HashMap<String, Object>();
					
					register.put("CD_PRODUTO_SERVICO", rsmCombustiveis.getInt("cd_produto_servico"));
					register.put("NM_PRODUTO_SERVICO", rsmCombustiveis.getString("nm_produto_servico"));
					
					
					register.put("DT_FECHAMENTO", Util.convCalendarToTimestamp(dtTemp));
					
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cdLocalArmazenamento", "" + cdLocalArmazenamento, ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cdEmpresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cdProdutoServico", "" + rsmCombustiveis.getInt("cd_produto_servico"), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("dtMovimento", "" + Util.formatDate(dtTemp, "dd/MM/yyyy"), ItemComparator.EQUAL, Types.VARCHAR));
					Result resultado = ProdutoEstoqueServices.getEstoqueAtual(criterios);
					
					GregorianCalendar dtTemp2 = new GregorianCalendar();
					dtTemp2.set(Calendar.DAY_OF_MONTH, dtTemp.get(Calendar.DAY_OF_MONTH) + 1);
					dtTemp2.set(Calendar.MONTH, dtTemp.get(Calendar.MONTH));
					dtTemp2.set(Calendar.YEAR, dtTemp.get(Calendar.YEAR));
					dtTemp2.set(Calendar.HOUR_OF_DAY, dtTemp.get(Calendar.HOUR_OF_DAY));
					dtTemp2.set(Calendar.MINUTE, dtTemp.get(Calendar.MINUTE));
					dtTemp2.set(Calendar.SECOND, dtTemp.get(Calendar.SECOND));
					
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cdLocalArmazenamento", "" + cdLocalArmazenamento, ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cdEmpresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cdProdutoServico", "" + rsmCombustiveis.getInt("cd_produto_servico"), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("dtMovimento", "" + Util.formatDate(dtTemp2, "dd/MM/yyyy"), ItemComparator.EQUAL, Types.VARCHAR));
					Result resultado2 = ProdutoEstoqueServices.getEstoqueAtual(criterios);
					
					float qtCompras = ((Float)resultado.getObjects().get("QT_ENTRADA") + (Float)resultado.getObjects().get("QT_ENTRADA_CONSIGNADA"));
					float qtVendas = ((Float)resultado.getObjects().get("QT_SAIDA") + (resultado.getObjects().get("QT_SAIDA_CONSIGNADA") != null ? (Float)resultado.getObjects().get("QT_SAIDA_CONSIGNADA") : 0));
					
					register.put("QT_VENDAS", qtVendas);
					register.put("QT_COMPRAS", qtCompras);
					
					qtMedicaoFisica = (Float)resultado.getObjects().get("QT_ESTOQUE") + (Float)resultado.getObjects().get("QT_ESTOQUE_CONSIGNADO");
					qtMedicaoFisicaProximo = (Float)resultado2.getObjects().get("QT_ESTOQUE") + (Float)resultado2.getObjects().get("QT_ESTOQUE_CONSIGNADO");
					
					// Adiciona estoque escritural
					double estoqueEscritural = qtMedicaoFisica + qtCompras - qtVendas;
					qtDiferenca = qtMedicaoFisicaProximo - estoqueEscritural;
					register.put("QT_ABERTURA", qtMedicaoFisica);
					register.put("QT_FECHAMENTO",  estoqueEscritural + qtDiferenca);
					register.put("VL_DIFERENCA", qtDiferenca);
					
					rsmCombFinal.addRegister(register);
				}
			}
			rsmCombFinal.beforeFirst();		
			
			Result result = new Result(1, "Sucesso!");
			HashMap<String, Object> param = new HashMap<String, Object>();
			
			
			//Adiciona o período
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			param.put("dataMovimentoInicio", simpleDateFormat.format(Util.convStringToCalendar(dataInicio).getTime()));
			param.put("dataMovimentoFim", simpleDateFormat.format(Util.convStringToCalendar(dataFim).getTime()));
			
			
			// Adiciona o nome dos locais de armazenamento somente se selecionar algum
			if(nmLocais == null || "".equalsIgnoreCase(nmLocais)){
				param.put("localArmazenamento","Todos");
			} else{
				param.put("localArmazenamento",nmLocais);
			}
			
			// Adiciona o nome dos tipos de combustível
			if(cdCategorias == null || "".equalsIgnoreCase(cdCategorias)){
				param.put("tipoCombustivel","Todos");
			} else{
				param.put("tipoCombustivel",categorias);
			}
			
			result.addObject("rsm", rsmCombFinal);
			result.addObject("params", param);
			return result;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
    /**
     * 
     * @param cdEmpresa
     * @param criterios
     * @return
     * @see #gerarRelatorioEstoqueProduto(int, ArrayList, Connection)
	*/
	public Result gerarRelatorioEstoqueProduto(int cdEmpresa, ArrayList<ItemComparator> criterios){
		return gerarRelatorioEstoqueProduto(cdEmpresa, criterios, null);
	}
	
	/**
	 * 
	 * Método que gera o relatório de estoque de produtos
	 * 
	 * @param cdEmpresa
	 * @param criterios
	 * @return consulta baseada nos critérios da tela
	 * @author Luiz Romario Filho
	 * @since 04/09/2014
	 */
	public Result gerarRelatorioEstoqueProduto(int cdEmpresa, ArrayList<ItemComparator> criterios, Connection connect){
		int calculoCusto = -1;
		int ordemImpressao = -1;
		int impressaoPor = -1;
		int cdFornecedor = -1;
		int tpEstoque = -1;
		int lgEstoque = -1;
		String cdLocalArmazenamento = "";
		String dtEstoque = "";
		int cdTabelaPreco = 0;
		int stPreco = -1;
		boolean lgColunaVendaAVista = false;
		boolean lgColunaMargem = false;
		
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		for (ItemComparator itemComparator : criterios) {
			if (itemComparator.getColumn().equalsIgnoreCase("calculoCusto")) {
				calculoCusto = Integer.parseInt(itemComparator.getValue());
			} else if (itemComparator.getColumn().equalsIgnoreCase("ordemImpressao")) {
				ordemImpressao = Integer.parseInt(itemComparator.getValue());
			} else if (itemComparator.getColumn().equalsIgnoreCase("impressaoPor")) {
				impressaoPor = Integer.parseInt(itemComparator.getValue());
			} else if (itemComparator.getColumn().equalsIgnoreCase("cdLocalArmazenamento")) {
				cdLocalArmazenamento = itemComparator.getValue();
			} else if (itemComparator.getColumn().equalsIgnoreCase("dt_movimento")) {
				dtEstoque = itemComparator.getValue();
			} else if (itemComparator.getColumn().equalsIgnoreCase("cdFornecedor")) {
				cdFornecedor = Integer.parseInt(itemComparator.getValue());
			} else if (itemComparator.getColumn().equalsIgnoreCase("lgColunaVendaAVista")) {
				lgColunaVendaAVista = 1 == Integer.parseInt(itemComparator.getValue());
			} else if (itemComparator.getColumn().equalsIgnoreCase("lgColunaMargem")) {
				lgColunaMargem = 1 == Integer.parseInt(itemComparator.getValue());
			} else if (itemComparator.getColumn().equalsIgnoreCase("stPreco")) {
				stPreco = Integer.parseInt(itemComparator.getValue());
			} else if (itemComparator.getColumn().equalsIgnoreCase("cdTabelaPreco")) {
				cdTabelaPreco = Integer.parseInt(itemComparator.getValue());
			} else if (itemComparator.getColumn().equalsIgnoreCase("tpEstoque")) {
				tpEstoque = Integer.parseInt(itemComparator.getValue());
			} else if (itemComparator.getColumn().equalsIgnoreCase("lgEstoque")) {
				lgEstoque = Integer.parseInt(itemComparator.getValue());
			} else {
				crt.add(itemComparator);
			}
		}
		boolean isConnectionNull = connect == null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			StringBuilder fields = new StringBuilder();
			StringBuilder orderBy = new StringBuilder();
			StringBuilder groupBy = new StringBuilder();
			
			fields.append("A.*, B.*, D.*, E.*, I.nm_grupo");
			fields.append( (cdTabelaPreco > 0 ?", G.*":"") );
			
			
			/*
			 * calculo do custo == 0 -> ultimo custo
			 * calculo do custo == 1 -> custo medio
			 */
			fields.append(calculoCusto == 0 ? ", B.vl_ultimo_custo " : ", B.vl_custo_medio "  );
			
			/*
			 * ordem de impressão == 0 -> código
			 * ordem de impressão == 1 -> descrição
			 */
			orderBy.append(" order by I.cd_grupo, H.cd_grupo ");
			if(ordemImpressao!= -1){
				orderBy.append(ordemImpressao == 0 ? ", A.cd_produto_servico " : ", A.nm_produto_servico ");
			}
			/*
			 * imprimir por == 0 -> categoria
			 * imprimir por == 1 -> alíquota
			 */
			if(impressaoPor!= -1){
				groupBy.append(" group by ");
				groupBy.append(impressaoPor == 0 ? " H.cd_grupo " : " A.txt_produto_servico ");
			}
			
			
			StringBuilder sql = new StringBuilder();
				sql.append(" SELECT ")
					.append(fields != null && !"".equalsIgnoreCase(fields.toString().trim()) ? fields : " * ")
					.append(" FROM grl_produto_servico A ")
					.append(" JOIN grl_produto E ON (A.cd_produto_servico = E.cd_produto_servico) ")
					.append(" LEFT OUTER JOIN grl_produto_servico_empresa B ON (E.cd_produto_servico = B.cd_produto_servico AND B.cd_empresa = ").append(cdEmpresa).append(" ) "); 
				sql
				.append(" LEFT OUTER JOIN adm_classificacao_fiscal  D ON ( D.cd_classificacao_fiscal = A.cd_classificacao_fiscal ) ")
				.append(" LEFT OUTER JOIN alm_produto_grupo H ON (B.cd_produto_servico = H.cd_produto_servico AND B.cd_empresa = H.cd_empresa AND H.lg_principal = 1) ")
				.append(" LEFT OUTER JOIN alm_grupo I ON (H.cd_grupo = I.cd_grupo) ");
				// Filtro por fornecedor
				sql.append(
						(cdFornecedor > 0 
								? "  AND EXISTS "
									+ "(SELECT * FROM alm_documento_entrada A1, alm_documento_entrada_item B1  "
									+ "	WHERE B1.cd_produto_servico   = A.cd_produto_servico "
									+ " AND B1.cd_documento_entrada = A1.cd_documento_entrada  "
									+ " AND B1.cd_empresa ="   + cdEmpresa 
									+ " AND A1.cd_empresa = " + cdEmpresa 
									+ " AND A1.st_documento_entrada = " + DocumentoEntradaServices.ST_LIBERADO 
									+ " AND A1.tp_entrada IN (" + DocumentoEntradaServices.ENT_COMPRA + "," + DocumentoEntradaServices.ENT_CONSIGNACAO + ")"
									+ " AND A1.cd_fornecedor =  " + cdFornecedor + ")"
								: ""));
				
				// RETORNAR PREÇO
				sql.append((cdTabelaPreco > 0 ? "LEFT OUTER JOIN adm_produto_servico_preco G ON (A.cd_produto_servico = G.cd_produto_servico AND G.dt_termino_validade IS NULL AND G.cd_tabela_preco = "+cdTabelaPreco+") ":""));
				sql.append((stPreco!=-1 ? " AND" + (stPreco==1 ? " NOT" : "") + " EXISTS (SELECT * FROM adm_produto_servico_preco G WHERE G.cd_produto_servico = A.cd_produto_servico AND G.dt_termino_validade IS NULL " +(cdTabelaPreco>0?" AND G.cd_tabela_preco = " + cdTabelaPreco : "") + ")" : ""));
				sql.append(" WHERE 1=1 ");
				sql.append(groupBy);
				
			ResultSetMap rsm = Search.find(sql.toString(), orderBy.toString(), crt, connect, false);
			HashMap<String, Object> param = new HashMap<String, Object>();
			Result result = new Result(1, "Sucesso!");
			
			/**
			 * Estoque
			 */
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date date = formatter.parse(dtEstoque);
			GregorianCalendar dtMovimentoMinor = new GregorianCalendar();
			dtMovimentoMinor.setTime(date);
			boolean lgFiscal = false;
			while(rsm.next()){
				criterios = new ArrayList<ItemComparator>();
				if(cdLocalArmazenamento != null && !"".equalsIgnoreCase(cdLocalArmazenamento.trim()))
					criterios.add(new ItemComparator("cdLocalArmazenamento", "" + cdLocalArmazenamento, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cdEmpresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cdProdutoServico", "" + rsm.getInt("cd_produto_servico"), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("dtMovimento", "" + Util.formatDate(dtMovimentoMinor, "dd/MM/yyyy"), ItemComparator.EQUAL, Types.VARCHAR));
				Result resultado = ProdutoEstoqueServices.getEstoqueAtual(criterios, connect);
				
				rsm.setValueToField("QT_ESTOQUE", resultado.getObjects().get("QT_ESTOQUE"));
				rsm.setValueToField("QT_ESTOQUE_CONSIGNADO", resultado.getObjects().get("QT_ESTOQUE_CONSIGNADO"));
				rsm.setValueToField("QT_ENTRADA", resultado.getObjects().get("QT_ENTRADA"));
				rsm.setValueToField("VL_ENTRADA", resultado.getObjects().get("VL_ENTRADA"));
				rsm.setValueToField("VL_ENTRADA_CONSIGNADA", resultado.getObjects().get("VL_ENTRADA_CONSIGNADA"));
				rsm.setValueToField("QT_ENTRADA_CONSIGNADA", resultado.getObjects().get("QT_ENTRADA_CONSIGNADA"));
				rsm.setValueToField("QT_SAIDA", resultado.getObjects().get("QT_SAIDA"));
				rsm.setValueToField("QT_SAIDA_CONSIGANADA", resultado.getObjects().get("QT_SAIDA_CONSIGANADA"));
				rsm.setValueToField("VL_SAIDA", resultado.getObjects().get("VL_SAIDA"));
			}
			rsm.beforeFirst();
			
			/* Fornecedor */
			PreparedStatement pstmt = connect.prepareStatement("SELECT nm_pessoa AS nm_fornecedor FROM alm_documento_entrada D, alm_documento_entrada_item E, grl_pessoa F " +
				                                               "WHERE E.cd_produto_servico   = ? " +
				                                               "  AND E.cd_documento_entrada = D.cd_documento_entrada " +
				                                               "  AND E.cd_empresa           = " +cdEmpresa+
				                                               "  AND D.cd_empresa           = " +cdEmpresa+
				                                               "  AND D.st_documento_entrada = " +DocumentoEntradaServices.ST_LIBERADO+
				                                               "  AND D.tp_entrada IN ("+DocumentoEntradaServices.ENT_COMPRA+ ","+DocumentoEntradaServices.ENT_CONSIGNACAO+") " +
				                                               "  AND D.cd_fornecedor        = F.cd_pessoa " +
				                                               (lgFiscal ? " AND D.tp_documento_entrada = " + DocumentoEntradaServices.TP_NOTA_FISCAL : "") +
				                                               "ORDER BY dt_documento_entrada DESC " +
				                                               "LIMIT 1");
			rsm.beforeFirst();
			while (rsm.next()) {
				// Ultimo fornecedor
				pstmt.setInt(1, rsm.getInt("cd_produto_servico"));
				ResultSet rs = pstmt.executeQuery();
				if (rs.next())
					rsm.setValueToField("NM_FORNECEDOR", rs.getString("nm_fornecedor"));
				//
				if (((lgEstoque==1) &&
						((tpEstoque==-1 || tpEstoque==0 ? rsm.getFloat("QT_ESTOQUE") : 0) + (tpEstoque==-1 || tpEstoque==1 ? rsm.getFloat("QT_ESTOQUE_CONSIGNADO") : 0)) <= 0) ||
						(lgEstoque==0 && (((tpEstoque==-1 || tpEstoque==0 ? rsm.getFloat("QT_ESTOQUE") : 0) + (tpEstoque==-1 || tpEstoque==1 ? rsm.getFloat("QT_ESTOQUE_CONSIGNADO") : 0)) > 0 ||
										  ((tpEstoque==-1 || tpEstoque==0 ? rsm.getFloat("QT_ESTOQUE") : 0) + (tpEstoque==-1 || tpEstoque==1 ? rsm.getFloat("QT_ESTOQUE_CONSIGNADO") : 0)) < 0))) {
					rsm.deleteRow();
					if (rsm.getPointer()==0)
						rsm.beforeFirst();
					else
						rsm.previous();
				}
				else if (lgEstoque==2 &&
						((tpEstoque==-1 || tpEstoque==0 ? rsm.getFloat("QT_ESTOQUE") : 0) + (tpEstoque==-1 || tpEstoque==1 ? rsm.getFloat("QT_ESTOQUE_CONSIGNADO") : 0)) >= 0) {
					rsm.deleteRow();
					if (rsm.getPointer()==0)
						rsm.beforeFirst();
					else
						rsm.previous();
				}else if ( lgEstoque==3 && ((tpEstoque==-1 || tpEstoque==0 ? rsm.getFloat("QT_ESTOQUE") : 0) + (tpEstoque==-1 || tpEstoque==1 ? rsm.getFloat("QT_ESTOQUE_CONSIGNADO") : 0)) > rsm.getFloat("QT_MINIMA_ESTOQUE") ){
					rsm.deleteRow();
					if (rsm.getPointer()==0)
						rsm.beforeFirst();
					else
						rsm.previous();
				}else if ( lgEstoque==4 && ((tpEstoque==-1 || tpEstoque==0 ? rsm.getFloat("QT_ESTOQUE") : 0) + (tpEstoque==-1 || tpEstoque==1 ? rsm.getFloat("QT_ESTOQUE_CONSIGNADO") : 0)) >= rsm.getFloat("QT_IDEAL_ESTOQUE") ) {
					rsm.deleteRow();
					if (rsm.getPointer()==0)
						rsm.beforeFirst();
					else
						rsm.previous();
				}
				
			}
			
			/*
			 * Colunas dinâmicas
			 * Coluna margem ==> lgColunaMargem == 1 ? exibe no relatório : não exibe  
			 * Coluna venda a vista ==> lgColunaVendaAVista == 1 ? exibe no relatório : não exibe  
			 * Nome de subcategoria ==> lgSubcategoria == 1 ? exibe no relatório : não exibe  
			 */
				ArrayList<HashMap<String, Object>> dynamicColumns = new ArrayList<HashMap<String, Object>>();
				
				HashMap<String, Object> headerColumnCt = new HashMap<String, Object>();
				headerColumnCt.put("LABEL", "REFERENCIA");
				headerColumnCt.put("FIELD", "$F{NR_CT}");
				dynamicColumns.add( headerColumnCt );
				
				HashMap<String, Object> headerColumnIcms = new HashMap<String, Object>();
				headerColumnIcms.put("LABEL", "ICMS%");
				headerColumnIcms.put("FIELD", "$F{PR_ICMS}");
				headerColumnIcms.put("STYLE_FIELD", "Currency");
				dynamicColumns.add( headerColumnIcms );
				
				HashMap<String, Object> headerColumnQtEstoque = new HashMap<String, Object>();
				headerColumnQtEstoque.put("LABEL", "QTDE. ESTOQUE");
				headerColumnQtEstoque.put("FIELD", "$F{QT_ESTOQUE}");
				headerColumnQtEstoque.put("TOTAL_FIELD", "$V{qtSubTotalEstoque}");
				dynamicColumns.add( headerColumnQtEstoque );
				
				HashMap<String, Object> headerColumnQtMinima = new HashMap<String, Object>();
				headerColumnQtMinima.put("LABEL", "QTDE. MÍNIMA");
				headerColumnQtMinima.put("FIELD", "$F{QT_MINIMA}");
				dynamicColumns.add( headerColumnQtMinima );
				
				HashMap<String, Object> headerColumnPrecoCusto = new HashMap<String, Object>();
				headerColumnPrecoCusto.put("LABEL", (calculoCusto == 0 ? "ÚLTIMO DE CUSTO (R$)" : "CUSTO MÉDIO (R$)") );
				headerColumnPrecoCusto.put("FIELD", (calculoCusto == 0 ? "$F{VL_ULTIMO_CUSTO}" : "$F{VL_CUSTO_MEDIO}") );
				headerColumnPrecoCusto.put("TOTAL_FIELD",  (calculoCusto == 0 ?"$V{vlSubTotalPrecoCusto}" : "$V{vlSubTotalCustoMedio}") );
				headerColumnPrecoCusto.put("STYLE_FIELD", "Currency");
				dynamicColumns.add( headerColumnPrecoCusto );
				
				if( lgColunaVendaAVista ){
					HashMap<String, Object> headerColumnVenda = new HashMap<String, Object>();
					headerColumnVenda.put("LABEL", "VENDA À VISTA");
					headerColumnVenda.put("FIELD", "$F{VL_PRECO}");
					headerColumnVenda.put("TOTAL_FIELD", "$V{vlSubTotalVendaAVista}");
					headerColumnVenda.put("STYLE_FIELD", "Currency");
					dynamicColumns.add( headerColumnVenda );
				}
				if( lgColunaMargem ){
					HashMap<String, Object> headerColumnMargem = new HashMap<String, Object>();
					headerColumnMargem.put("LABEL", "MARGEM (%)");
					headerColumnMargem.put("FIELD", "$F{VL_MARGEM}");
					headerColumnMargem.put("STYLE_FIELD", "Currency");
					headerColumnMargem.put("TOTAL_FIELD", "$V{lblTotalMargem}");
					dynamicColumns.add( headerColumnMargem );
				}
				
				HashMap<String, Object> headerColumnEstoqueContab = new HashMap<String, Object>();
				headerColumnEstoqueContab.put("LABEL", "ESTOQUE CONTAB(R$)");
				headerColumnEstoqueContab.put("FIELD", "$F{VL_CONTABIL}");
				headerColumnEstoqueContab.put("TOTAL_FIELD", "$V{vlSubTotalContabil}");
				headerColumnEstoqueContab.put("STYLE_FIELD", "Currency");
				dynamicColumns.add( headerColumnEstoqueContab );
				
				param.put("DYNAMIC_COLUMNS", dynamicColumns);
				param.put("DYNAMIC_COLUMNS_HEADER_KEY", "DYNAMIC_COLUMNS_HEADER_KEY");
				param.put("DYNAMIC_COLUMNS_DETAIL_KEY", "DYNAMIC_COLUMNS_DETAIL_KEY");
				param.put("DYNAMIC_COLUMNS_TOTAL_KEY",  "DYNAMIC_COLUMNS_TOTAL_KEY");
			/*
			 * **************************************************************	
			 */
			
	 		/**
			 * ASSOCIANDO VALORES
			 */
				// Buscando informação de Situação Tributária
				int cdNaturezaOperacao = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_VAREJO", 0, cdEmpresa);
				int cdPais = 0, cdEstado = 0, cdCidade = 0;
				ResultSet rs = connect.prepareStatement("SELECT D.cd_cidade, B.cd_estado, C.cd_pais " +
														   "FROM grl_empresa A " +
														   "LEFT OUTER JOIN grl_pessoa_endereco D ON (A.cd_empresa = D.cd_pessoa AND D.lg_principal = 1) " +
													  	   "LEFT OUTER JOIN grl_cidade          B ON (D.cd_cidade = B.cd_cidade) " +
														   "LEFT OUTER JOIN grl_estado          C ON (B.cd_estado = C.cd_estado) " +
														   "WHERE A.cd_empresa = "+cdEmpresa).executeQuery();
				if(rs.next())	{
					cdCidade = rs.getInt("cd_cidade");
					cdEstado = rs.getInt("cd_estado");
					cdPais   = rs.getInt("cd_pais");
				}
				rsm.beforeFirst();
				ResultSetMap rsmProdutoReferencia = null;
				while( rsm.next() ){
					
					rsmProdutoReferencia = new ResultSetMap(connect.prepareStatement(
							"SELECT A.cd_produto_servico, A.cd_empresa, A.cd_local_armazenamento, C.nm_local_armazenamento, "+
							"		B.nm_referencia, B.dt_validade, SUM(A.qt_entrada + A.qt_entrada_consignada) AS qt_entrada "+
							"FROM alm_entrada_local_item A "+ 
							"	LEFT OUTER JOIN alm_produto_referencia B ON (A.cd_referencia = B.cd_referencia) " +
							"	LEFT OUTER JOIN alm_local_armazenamento C ON (B.cd_local_armazenamento = C.cd_local_armazenamento) "+
							"WHERE A.cd_empresa = "+cdEmpresa+
							"	   AND A.cd_produto_servico = "+rsm.getInt("cd_produto_servico")+
							"	   AND A.cd_empresa = B.cd_empresa "+
							"	   AND A.cd_produto_servico = B.cd_produto_servico "+
							"GROUP BY A.cd_produto_servico, B.nm_referencia, B.dt_validade, A.cd_empresa, A.cd_local_armazenamento, C.nm_local_armazenamento "+
							"ORDER BY B.nm_referencia, B.dt_validade").executeQuery());
					
					while(rsmProdutoReferencia.next()){
						rsmProdutoReferencia.setValueToField("QT_ENTRADA", rsmProdutoReferencia.getFloat("QT_ENTRADA"));
						rsmProdutoReferencia.setValueToField("NM_LOCAL_ARMAZENAMENTO", rsmProdutoReferencia.getString("NM_LOCAL_ARMAZENAMENTO"));
						rsmProdutoReferencia.setValueToField("DT_VALIDADE", rsmProdutoReferencia.getDateFormat("DT_VALIDADE", "dd/MM/yyyy"));
						rsmProdutoReferencia.setValueToField("NM_REFERENCIA", rsmProdutoReferencia.getString("NM_REFERENCIA"));
					}
					rsm.setValueToField("RSM_PRODUTO_REFERENCIA",rsmProdutoReferencia.getLines());
					ResultSetMap rsmAli = new ResultSetMap(connect.prepareStatement(
							"SELECT A.*, B.* " +
									"FROM adm_tributo A, adm_tributo_aliquota B " +
									"WHERE A.cd_tributo  = B.cd_tributo " +
									"  AND B.tp_operacao = "+TributoAliquotaServices.OP_VENDA+
									"  AND EXISTS (SELECT * FROM adm_produto_servico_tributo C " +
									"              WHERE A.cd_tributo  = C.cd_tributo " +
									"                AND B.cd_tributo_aliquota = C.cd_tributo_aliquota " +
									"                AND C.cd_natureza_operacao = "+cdNaturezaOperacao+
									"                AND (C.cd_pais   = "+cdPais+" OR C.cd_pais IS NULL) "+
									"                AND (C.cd_estado = "+cdEstado+" OR C.cd_estado IS NULL) "+
									"                AND (C.cd_cidade = "+cdCidade+" OR C.cd_cidade IS NULL) "+
									"                AND C.cd_classificacao_fiscal = "+rsm.getInt("cd_classificacao_fiscal")+")").executeQuery());
					float prAliquota = 0;
					rsmAli.beforeFirst();
					if(rsmAli.next()){
						prAliquota = rsmAli.getFloat("pr_aliquota");
					}
					rsm.setValueToField("PR_ICMS", prAliquota);
					rsm.setValueToField("NR_CT", rsm.getString("id_classificacao_fiscal") );//NR_CT
					Double vlMargem = ( rsm.getDouble("VL_PRECO") > 0.0 )?rsm.getDouble("VL_PRECO") - rsm.getDouble("VL_ULTIMO_CUSTO"):0.0;
					rsm.setValueToField("VL_MARGEM", prAliquota  );//PR_ICMS
					rsm.setValueToField("QT_ESTOQUE", (rsm.getString("QT_ESTOQUE") != null ? rsm.getFloat("QT_ESTOQUE") : 0) );//QT_ESTOQUE
					rsm.setValueToField("VL_ULTIMO_CUSTO", rsm.getFloat("VL_ULTIMO_CUSTO"));//VL_ULTIMO_CUSTO
					rsm.setValueToField("VL_PRECO", rsm.getFloat("VL_PRECO") );//VL_PRECO
					rsm.setValueToField("VL_MARGEM", vlMargem );//VL_MARGEM
					rsm.setValueToField("VL_CONTABIL", (rsm.getFloat("QT_ESTOQUE") * rsm.getFloat("VL_ULTIMO_CUSTO")) );//VL_CONTABIL
				}
			rsm.beforeFirst();
//			ArrayList<String> orderRsmBy = new ArrayList<String>();
//			orderRsmBy.add("NM_GRUPO");
//			rsm.orderBy( orderRsmBy  );
			
			param.put("dtEstoque", dtEstoque); 
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			return result;

		} catch (Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public Result gerarRelatorioEstoqueProduto(int cdEmpresa, int tpEstoque, int stProdutoEmpresa, int cdTabelaPreco, GregorianCalendar dtEstoque){
		return gerarRelatorioEstoqueProduto(cdEmpresa, tpEstoque, stProdutoEmpresa, cdTabelaPreco, dtEstoque, false, -1, -1, 0, 0, "", "", 0, -1, null);
	}
	
	public Result gerarRelatorioEstoqueProduto(int cdEmpresa, int tpEstoque, int stProdutoEmpresa, int cdTabelaPreco, GregorianCalendar dtEstoque, 
			   boolean lgFiscal, int lgUltimoCusto, int stPreco, int cdFornecedor, int cdGrupo, String txtEspecificacao, 
			   String txtDadoTecnico, int cdFabricante, int lgEstoque, ArrayList<String> orderByFields){
		return gerarRelatorioEstoqueProduto(cdEmpresa, tpEstoque, stProdutoEmpresa, cdTabelaPreco, dtEstoque, false, -1, -1, 0, 0, "", "", 0, -1, -1, -1, orderByFields);
	}
	
	public Result gerarRelatorioEstoqueProduto(int cdEmpresa, int tpEstoque, int stProdutoEmpresa, int cdTabelaPreco, GregorianCalendar dtEstoque, 
											   boolean lgFiscal, int lgUltimoCusto, int stPreco, int cdFornecedor, int cdGrupo, String txtEspecificacao, 
											   String txtDadoTecnico, int cdFabricante, int lgEstoque, int qtLimite, int offSet, ArrayList<String> orderByFields){
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			TipoOperacao tipoOp = TipoOperacaoDAO.get(ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_VAREJO", 0, cdEmpresa, connection), connection);
			if(tipoOp == null)
				return new Result(-1, "Parametro de Tipo de Operação Varejo não configurado!");
		    if(cdTabelaPreco <= 0)
				cdTabelaPreco = tipoOp.getCdTabelaPreco();
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			if(orderByFields != null && !orderByFields.isEmpty()){
				String orderString = "";
				for(int i = 0; i < orderByFields.size(); i++){
					String campo = orderByFields.get(i);
					if(campo.equals("idreduzidoint"))
						orderString += "cast(substring(id_reduzido from '\\d+') as integer), ";
					else
						orderString += campo + ", ";
				}
				orderString = orderString.substring(0, orderString.length() - 2);
				criterios.add(new ItemComparator("specialOrder", orderString, ItemComparator.EQUAL, Types.VARCHAR));
			}
			if(tpEstoque != -1) 
				criterios.add(new ItemComparator("tpEstoque", Integer.toString(tpEstoque), ItemComparator.EQUAL, Types.INTEGER));
			if(lgEstoque != -1) 
				criterios.add(new ItemComparator("lgEstoque", Integer.toString(lgEstoque), ItemComparator.EQUAL, Types.INTEGER));
			if(stProdutoEmpresa != -1)
				criterios.add(new ItemComparator("B.st_produto_empresa", Integer.toString(stProdutoEmpresa), ItemComparator.EQUAL, Types.INTEGER));
			if(cdTabelaPreco > 0)
				criterios.add(new ItemComparator("cdTabelaPreco", Integer.toString(cdTabelaPreco), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("lgFiscal", "" + lgFiscal, ItemComparator.EQUAL, Types.INTEGER));
			if(lgUltimoCusto != -1)
				criterios.add(new ItemComparator("lgUltimoCusto", "" + lgUltimoCusto, ItemComparator.EQUAL, Types.INTEGER));
			if(stPreco != -1)
				criterios.add(new ItemComparator("stPreco", "" + stPreco, ItemComparator.EQUAL, Types.INTEGER));
			if(cdFornecedor != 0)
				criterios.add(new ItemComparator("cdFornecedor", "" + cdFornecedor, ItemComparator.EQUAL, Types.INTEGER));
			if(cdGrupo != 0)
				criterios.add(new ItemComparator("H.cd_grupo", "" + cdGrupo, ItemComparator.EQUAL, Types.INTEGER));
			if(txtEspecificacao!="")
				criterios.add(new ItemComparator("txt_especificacao", "" + txtEspecificacao, ItemComparator.LIKE_ANY, Types.VARCHAR));
			if(txtDadoTecnico!="")
				criterios.add(new ItemComparator("txt_dado_tecnico", "" + txtDadoTecnico, ItemComparator.LIKE_ANY, Types.VARCHAR));
			if(cdFabricante != 0)
				criterios.add(new ItemComparator("A.cd_fabricante", "" + cdFabricante, ItemComparator.EQUAL, Types.INTEGER));
			if(dtEstoque!=null)
				criterios.add(new ItemComparator("dtMovimento", Util.formatDate(dtEstoque, "dd/MM/yyyy"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
			if(qtLimite > 0)
				criterios.add(new ItemComparator("qtLimite", "" + qtLimite, ItemComparator.EQUAL, Types.INTEGER));
			if(offSet > 0)
				criterios.add(new ItemComparator("offSet", "" + offSet, ItemComparator.EQUAL, Types.INTEGER));
			
			
			// Colocando a data atual
			if(dtEstoque==null)
				dtEstoque = Util.getDataAtual();
			// Buscando informação de Situação Tributária
			int cdNaturezaOperacao = ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_VAREJO", 0, cdEmpresa);
			int cdPais = 0, cdEstado = 0, cdCidade = 0;
			ResultSet rs = connection.prepareStatement("SELECT D.cd_cidade, B.cd_estado, C.cd_pais " +
													   "FROM grl_empresa A " +
													   "LEFT OUTER JOIN grl_pessoa_endereco D ON (A.cd_empresa = D.cd_pessoa AND D.lg_principal = 1) " +
												  	   "LEFT OUTER JOIN grl_cidade          B ON (D.cd_cidade = B.cd_cidade) " +
													   "LEFT OUTER JOIN grl_estado          C ON (B.cd_estado = C.cd_estado) " +
													   "WHERE A.cd_empresa = "+cdEmpresa).executeQuery();
			if(rs.next())	{
				cdCidade = rs.getInt("cd_cidade");
				cdEstado = rs.getInt("cd_estado");
				cdPais   = rs.getInt("cd_pais");
			}
			
			if (orderByFields != null && orderByFields.size()>0){
				for(int i = 0; i < orderByFields.size(); i++){
					String campo = orderByFields.get(i);
					if(campo.equals("id_reduzidoint"))
						orderByFields.set(i, "cast(substring(id_reduzido from '\\d+') as integer)");
				}
			}
			
			ResultSetMap rsm = new ResultSetMap();
//			ResultSetMap rsmFinal = new ResultSetMap();
			
//			int offset 	 = 0;
//			int qtLimite = 500;
//			
//			do{
			
				criterios.add(new ItemComparator("qtLimite", "" + qtLimite, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("offSet", "" + offSet, ItemComparator.EQUAL, Types.INTEGER));
//				offset += qtLimite;
				
				rsm = findProduto(cdEmpresa, criterios, (orderByFields != null && orderByFields.size()>0 ? orderByFields : new ArrayList<String>()));
				ResultSetMap rsmProdutoReferencia = null;
				
				
				while(rsm.next()){
					if(rsm.getString("NM_GRUPO") == null || rsm.getString("NM_GRUPO").equals("null") || rsm.getString("NM_GRUPO").equals(""))
						rsm.setValueToField("NM_GRUPO", "SEM GRUPO");
					rsm.setValueToField("VL_PRECO", rsm.getFloat("VL_PRECO"));
					rsm.setValueToField("DS_GRUPO", rsm.getString("NM_GRUPO"));
					rsm.setValueToField("NR_REFERENCIA", rsm.getString("NR_REFERENCIA"));
					rsm.setValueToField("PR_ICMS", 0);
					rsm.setValueToField("VL_ULTIMO_CUSTO", rsm.getFloat("VL_ULTIMO_CUSTO"));
					rsm.setValueToField("VL_PRECO", rsm.getFloat("VL_PRECO"));
					rsm.setValueToField("QT_MINIMA", (rsm.getString("QT_MINIMA_ESTOQUE") != null ? rsm.getFloat("QT_MINIMA_ESTOQUE") : 0));
					rsm.setValueToField("QT_ESTOQUE", (rsm.getString("QT_ESTOQUE") != null ? rsm.getFloat("QT_ESTOQUE") : 0));
					rsm.setValueToField("VL_MARGEM", (rsm.getFloat("VL_PRECO") - rsm.getFloat("VL_ULTIMO_CUSTO")));
					rsm.setValueToField("PR_MARGEM", (rsm.getFloat("VL_MARGEM") / (rsm.getFloat("VL_ULTIMO_CUSTO") == 0 ? 1 : rsm.getFloat("VL_ULTIMO_CUSTO")) * 100));
					rsm.setValueToField("VL_CONTABIL", (rsm.getFloat("QT_ESTOQUE") * rsm.getFloat("VL_ULTIMO_CUSTO")));
					
					rsmProdutoReferencia = new ResultSetMap(connection.prepareStatement(
											"SELECT A.cd_produto_servico, A.cd_empresa, A.cd_local_armazenamento, C.nm_local_armazenamento, "+
											"		B.nm_referencia, B.dt_validade, SUM(A.qt_entrada + A.qt_entrada_consignada) AS qt_entrada "+
											"FROM alm_entrada_local_item A "+ 
											"	LEFT OUTER JOIN alm_produto_referencia B ON (A.cd_referencia = B.cd_referencia) " +
											"	LEFT OUTER JOIN alm_local_armazenamento C ON (B.cd_local_armazenamento = C.cd_local_armazenamento) "+
											"WHERE A.cd_empresa = "+cdEmpresa+
											"	   AND A.cd_produto_servico = "+rsm.getInt("cd_produto_servico")+
											"	   AND A.cd_empresa = B.cd_empresa "+
											"	   AND A.cd_produto_servico = B.cd_produto_servico "+
											"GROUP BY A.cd_produto_servico, B.nm_referencia, B.dt_validade, A.cd_empresa, A.cd_local_armazenamento, C.nm_local_armazenamento "+
											"ORDER BY B.nm_referencia, B.dt_validade").executeQuery());
					
					while(rsmProdutoReferencia.next()){
						rsmProdutoReferencia.setValueToField("QT_ENTRADA", rsmProdutoReferencia.getFloat("QT_ENTRADA"));
						rsmProdutoReferencia.setValueToField("NM_LOCAL_ARMAZENAMENTO", rsmProdutoReferencia.getString("NM_LOCAL_ARMAZENAMENTO"));
						rsmProdutoReferencia.setValueToField("DT_VALIDADE", rsmProdutoReferencia.getDateFormat("DT_VALIDADE", "dd/MM/yyyy"));
						rsmProdutoReferencia.setValueToField("NM_REFERENCIA", rsmProdutoReferencia.getString("NM_REFERENCIA"));
					}
					rsm.setValueToField("RSM_PRODUTO_REFERENCIA",rsmProdutoReferencia.getLines());
					ResultSetMap rsmAli = new ResultSetMap(connection.prepareStatement(
							"SELECT A.*, B.* " +
									"FROM adm_tributo A, adm_tributo_aliquota B " +
									"WHERE A.cd_tributo  = B.cd_tributo " +
									"  AND B.tp_operacao = "+TributoAliquotaServices.OP_VENDA+
									"  AND EXISTS (SELECT * FROM adm_produto_servico_tributo C " +
									"              WHERE A.cd_tributo  = C.cd_tributo " +
									"                AND B.cd_tributo_aliquota = C.cd_tributo_aliquota " +
									"                AND C.cd_natureza_operacao = "+cdNaturezaOperacao+
									"                AND (C.cd_pais   = "+cdPais+" OR C.cd_pais IS NULL) "+
									"                AND (C.cd_estado = "+cdEstado+" OR C.cd_estado IS NULL) "+
									"                AND (C.cd_cidade = "+cdCidade+" OR C.cd_cidade IS NULL) "+
									"                AND C.cd_classificacao_fiscal = "+rsm.getInt("cd_classificacao_fiscal")+")").executeQuery());
					float prAliquota = 0;
					if(rsmAli.next()){
						prAliquota = rsmAli.getFloat("pr_aliquota");
					}
					rsm.setValueToField("PR_ICMS", prAliquota);
					rsm.setValueToField("NR_CT", rsm.getString("id_classificacao_fiscal"));
					
				}
				rsm.beforeFirst();
			
//				while(rsm.next()){
//					rsmFinal.addRegister(rsm.getRegister());
//				}
//				rsm.beforeFirst();
				
//			}while(rsm.size() > 0);
			
			
			if (orderByFields == null || orderByFields.isEmpty()){
				ArrayList<String> fields = new ArrayList<String>();
				fields.add("NM_GRUPO");
				fields.add("CD_PRODUTO_SERVICO");
				rsm.orderBy(fields);
			}
			
			rsm.beforeFirst();
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("dtEstoque", Util.convCalendarString(dtEstoque));
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			if (isConnectionNull)
				connection.commit();
	
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public Result gerarRelatorioEstoqueProdutoPrecoCsv(int cdEmpresa, int tpEstoque,
			int stProdutoEmpresa, GregorianCalendar dtEstoque, boolean lgFiscal, int lgUltimoCusto,
			int stPreco, int cdFornecedor, int cdGrupo,
			String txtEspecificacao, String txtDadoTecnico, int cdFabricante,
			int lgEstoque, ArrayList<String> orderByFields) {
		
		ResultSetMap rsm = (ResultSetMap)gerarRelatorioEstoqueProdutoPreco(cdEmpresa, tpEstoque, stProdutoEmpresa, dtEstoque, lgFiscal, lgUltimoCusto, stPreco, cdFornecedor, cdGrupo, txtEspecificacao, txtDadoTecnico, cdFabricante, lgEstoque, -1, 0, orderByFields).getObjects().get("rsm");
		System.out.println("Numero de registro " + rsm.size());
		String registro = "COD. BARRAS;ID;PRODUTO;GRUPO;TAMANHO;COR;NCM;ORIGEM;CF;VAREJO;ATACADO\r\n";
		
		while(rsm.next()){
			String novoRegistro = rsm.getString("ID_PRODUTO_SERVICO") + ";" +
					    rsm.getString("ID_REDUZIDO") + ";" +
					    rsm.getString("NM_PRODUTO_SERVICO") + ";" +
					    rsm.getString("NM_GRUPO") + ";" +
					    rsm.getString("TXT_DADO_TECNICO") + ";" +
					    rsm.getString("TXT_ESPECIFICACAO") + ";" +
					    rsm.getString("NR_NCM") + ";" +
					    rsm.getString("CL_ORIGEM") + ";" +
					    rsm.getString("NM_CLASSIFICACAO_FISCAL") + ";" +
					    (rsm.getString("VL_PRECO_VAREJO")!= null ? rsm.getString("VL_PRECO_VAREJO").replace(".", ",") : "") + ";" +
					    (rsm.getString("VL_PRECO_ATACADO")!= null ? rsm.getString("VL_PRECO_ATACADO").replace(".", ",") : "") + ";" +
						(rsm.getString("PR_DESCONTO_MAXIMO")!=null ? rsm.getString("PR_DESCONTO_MAXIMO").replace(".", ",") : "") + "\r\n";
			
			System.out.println(novoRegistro);
			
			registro += novoRegistro;
		}
		
		try{
			//
			FileOutputStream gravar;
			File arquivo = new File("relatorio_produto.csv"); 
			//
			File diretorio = new File("C:\\TIVIC");
			if(!diretorio.exists()){
				diretorio.mkdir();
			}
			//
			gravar = new FileOutputStream("C:\\TIVIC\\" + arquivo);
			gravar.write(registro.getBytes());
			gravar.close();
			
		}
		catch(Exception e){System.out.println("ERRO na gravacao: " + e); return new Result(-1, "Falha na gravação do arquivo");}
		
		return new Result(1, "Arquivo gravado com sucesso");
	}
	
	public Result gerarRelatorioEstoqueProdutoPreco(int cdEmpresa, int tpEstoque,
			int stProdutoEmpresa, GregorianCalendar dtEstoque, boolean lgFiscal, int lgUltimoCusto,
			int stPreco, int cdFornecedor, int cdGrupo,
			String txtEspecificacao, String txtDadoTecnico, int cdFabricante,
			int lgEstoque, int qtLimite, int offSet, ArrayList<String> orderByFields) {
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			if (orderByFields != null && !orderByFields.isEmpty()) {
				String orderString = "";
				for (int i = 0; i < orderByFields.size(); i++) {
					String campo = orderByFields.get(i);
					if (campo.equals("idreduzidoint"))
						orderString += "cast(substring(id_reduzido from '\\d+') as integer), ";
					else
						orderString += campo + ", ";
				}
				orderString = orderString
						.substring(0, orderString.length() - 2);
				criterios.add(new ItemComparator("specialOrder", orderString,
						ItemComparator.EQUAL, Types.VARCHAR));
			}
			if (tpEstoque != -1)
				criterios.add(new ItemComparator("tpEstoque", Integer.toString(tpEstoque), ItemComparator.EQUAL, Types.INTEGER));
			if (lgEstoque != -1)
				criterios.add(new ItemComparator("lgEstoque", Integer.toString(lgEstoque), ItemComparator.EQUAL, Types.INTEGER));
			if (stProdutoEmpresa != -1)
				criterios.add(new ItemComparator("B.st_produto_empresa", Integer.toString(stProdutoEmpresa), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("lgFiscal", "" + lgFiscal, ItemComparator.EQUAL, Types.INTEGER));
			if (lgUltimoCusto != -1)
				criterios.add(new ItemComparator("lgUltimoCusto", "" + lgUltimoCusto, ItemComparator.EQUAL, Types.INTEGER));
			if (stPreco != -1)
				criterios.add(new ItemComparator("stPreco", "" + stPreco, ItemComparator.EQUAL, Types.INTEGER));
			if (cdFornecedor != 0)
				criterios.add(new ItemComparator("cdFornecedor", "" + cdFornecedor, ItemComparator.EQUAL, Types.INTEGER));
			if (cdGrupo != 0)
				criterios.add(new ItemComparator("H.cd_grupo", "" + cdGrupo, ItemComparator.EQUAL, Types.INTEGER));
			if (txtEspecificacao != null && !txtEspecificacao.trim().equals(""))
				criterios.add(new ItemComparator("txt_especificacao", "" + txtEspecificacao, ItemComparator.LIKE_ANY, Types.VARCHAR));
			if (txtDadoTecnico != null && !txtDadoTecnico.trim().equals(""))
				criterios.add(new ItemComparator("txt_dado_tecnico", "" + txtDadoTecnico, ItemComparator.LIKE_ANY, Types.VARCHAR));
			if (cdFabricante != 0)
				criterios.add(new ItemComparator("A.cd_fabricante", "" + cdFabricante, ItemComparator.EQUAL, Types.INTEGER));
			if (dtEstoque != null)
				criterios.add(new ItemComparator("dtMovimento", Util.formatDate(dtEstoque, "dd/MM/yyyy"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
			if(qtLimite > 0)
				criterios.add(new ItemComparator("qtLimite", "" + qtLimite, ItemComparator.EQUAL, Types.INTEGER));
			else if(qtLimite == -1)
				criterios.add(new ItemComparator("qtLimite", "" + qtLimite, ItemComparator.EQUAL, Types.INTEGER));
			if(offSet > 0)
				criterios.add(new ItemComparator("offSet", "" + offSet, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("semEstoque", "" + true, ItemComparator.EQUAL, Types.INTEGER));
			// Colocando a data atual
			if (dtEstoque == null)
				dtEstoque = Util.getDataAtual();
			// Buscando informação de Situação Tributária
			if (orderByFields != null && orderByFields.size() > 0) {
				for (int i = 0; i < orderByFields.size(); i++) {
					String campo = orderByFields.get(i);
					if (campo.equals("id_reduzidoint"))
						orderByFields.set(i, "cast(substring(id_reduzido from '\\d+') as integer)");
				}
			}

			ResultSetMap rsm = new ResultSetMap();
			
			rsm = findProduto(cdEmpresa, criterios, (orderByFields != null && orderByFields.size() > 0 ? orderByFields : new ArrayList<String>()));
			
			
			int cdTipoOperacaoVarejo  = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_VAREJO", 0, cdEmpresa);
			int cdTipoOperacaoAtacado = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_ATACADO", 0, cdEmpresa);
			TipoOperacao tipoOperacaoVarejo  = TipoOperacaoDAO.get(cdTipoOperacaoVarejo, connection);
			TipoOperacao tipoOperacaoAtacado = TipoOperacaoDAO.get(cdTipoOperacaoAtacado, connection);
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM adm_produto_servico_preco WHERE cd_produto_servico = ? AND cd_tabela_preco = ? AND dt_termino_validade IS NULL");
			ResultSetMap rsmPreco = new ResultSetMap();
			while (rsm.next()) {
				pstmt.setInt(1, rsm.getInt("CD_PRODUTO_SERVICO"));
				pstmt.setInt(2, tipoOperacaoVarejo.getCdTabelaPreco());
				rsmPreco = new ResultSetMap(pstmt.executeQuery());
				if(rsmPreco.next()){
					rsm.setValueToField("VL_PRECO_VAREJO", rsmPreco.getFloat("VL_PRECO"));
				}
				else{
					rsm.setValueToField("VL_PRECO_VAREJO", 0);
				}
				
				pstmt.setInt(2, tipoOperacaoAtacado.getCdTabelaPreco());
				rsmPreco = new ResultSetMap(pstmt.executeQuery());
				if(rsmPreco.next()){
					rsm.setValueToField("VL_PRECO_ATACADO", rsmPreco.getFloat("VL_PRECO"));
				}
				else{
					rsm.setValueToField("VL_PRECO_ATACADO", 0);
				}
				
				
				if(rsm.getString("ID_REDUZIDO") == null || rsm.getString("ID_REDUZIDO").equals(""))
					rsm.setValueToField("ID_REDUZIDO", rsm.getString("NR_REFERENCIA"));
				
				rsm.setValueToField("CL_ORIGEM", ProdutoServicoEmpresaServices.tiposOrigem[rsm.getInt("TP_ORIGEM")]);
			}
			rsm.beforeFirst();

			if (orderByFields == null || orderByFields.isEmpty()) {
				ArrayList<String> fields = new ArrayList<String>();
				fields.add("CD_GRUPO");
				fields.add("NM_PRODUTO_SERVICO");
				rsm.orderBy(fields);
			}

			rsm.beforeFirst();
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("dtEstoque", Util.convCalendarString(dtEstoque));
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			if (isConnectionNull)
				connection.commit();

			return result;
		} 
		catch (Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		} 
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static float getSaldoEstoqueOf(int cdEmpresa, int cdProdutoServico, int cdLocalArmazenamento, GregorianCalendar dtSaldo){
		boolean isConnectionNull = true;
		Connection connection = null;
		try {
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			// ENTRADAS
			PreparedStatement pstmt = connection.prepareStatement("SELECT SUM(D.qt_entrada + D.qt_entrada_consignada) AS qt_entrada " +
													   			  "FROM alm_entrada_local_item D, alm_documento_entrada E " +
													   			  "WHERE D.cd_documento_entrada = E.cd_documento_entrada " +
													   			  "  AND D.cd_empresa           = " +cdEmpresa+
													   			  "  AND D.cd_produto_servico   = " +cdProdutoServico+
													   			  (cdLocalArmazenamento>0 ? " AND D.cd_local_armazenamento = "+cdLocalArmazenamento : "")+
													   			  (dtSaldo!=null          ? " AND E.dt_documento_entrada  <= ? " : ""));
			if(dtSaldo!=null)
				pstmt.setTimestamp(1, new Timestamp(dtSaldo.getTimeInMillis()));
			ResultSet rs    = pstmt.executeQuery();
			float qtEstoque = rs.next() ? rs.getFloat("qt_entrada") : 0;
			// SAÍDAS
			pstmt = connection.prepareStatement("SELECT SUM(D.qt_saida + D.qt_saida_consignada) AS qt_saida " +
								   			    "FROM alm_saida_local_item D, alm_documento_saida E " +
								   			    "WHERE D.cd_documento_saida = E.cd_documento_saida " +
								   			    "  AND D.cd_empresa         = " +cdEmpresa+
								   			    "  AND D.cd_produto_servico = " +cdProdutoServico+
								   			    (cdLocalArmazenamento>0 ? " AND D.cd_local_armazenamento = "+cdLocalArmazenamento : "")+
								   			    (dtSaldo!=null          ? " AND E.dt_documento_saida    <= ? " : ""));
			if(dtSaldo!=null)
				pstmt.setTimestamp(1, new Timestamp(dtSaldo.getTimeInMillis()));
			rs         = pstmt.executeQuery();
			qtEstoque -= rs.next() ? rs.getFloat("qt_saida") : 0;
			
			return qtEstoque;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.desconectar(connection);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static Result gerarEstoqueCombustivel(int cdEmpresa, GregorianCalendar dtMovimento){
		return gerarEstoqueCombustivel(cdEmpresa, dtMovimento, null);
	}
	
	public static Result gerarEstoqueCombustivel(int cdEmpresa, GregorianCalendar dtMovimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
//			dtMovimento.set(Calendar.HOUR, 24);
//			dtMovimento.set(Calendar.MINUTE, 59);
//			dtMovimento.set(Calendar.SECOND, 59);
//			dtMovimento.set(Calendar.MILLISECOND, 0);
//			
//			int cdGrupoCombustivel = ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0, cdEmpresa);
//			
			/*
			 * Estoque de Abertura
			 */
			String sqlEstoqueAbertura = "SELECT LA.id_local_armazenamento, PS.nm_produto_servico, PS.cd_produto_servico, PE.vl_ultimo_custo, CF.dt_fechamento, TT.qt_capacidade, T.cd_tanque  " +
										"FROM pcb_medicao_fisica MF " +
										"LEFT OUTER JOIN adm_conta_fechamento    	 CF ON (MF.cd_conta      = CF.cd_conta " +
										"                                           	 AND MF.cd_fechamento =  CF.cd_fechamento) " +
										"LEFT OUTER JOIN pcb_tanque              	 T  ON (MF.cd_tanque = T.cd_tanque) " +
										"LEFT OUTER JOIN pcb_tipo_tanque           	 TT  ON (TT.cd_tipo_tanque = T.cd_tipo_tanque) " +
										"LEFT OUTER JOIN alm_local_armazenamento 	 LA ON (T.cd_tanque = LA.cd_local_armazenamento) " +
										"LEFT OUTER JOIN grl_produto_servico     	 PS ON (T.cd_produto_servico = PS.cd_produto_servico) " +
										"LEFT OUTER JOIN grl_produto_servico_empresa PE ON (T.cd_produto_servico = PE.cd_produto_servico AND PE.cd_empresa = "+cdEmpresa+") " +
										"WHERE CAST(CF.dt_fechamento AS DATE) = ? " +
										"  AND CF.cd_turno                    = " +TurnoServices.getPrimeiroTurno() + 
										" GROUP BY LA.id_local_armazenamento, PS.nm_produto_servico, PS.cd_produto_servico, PE.vl_ultimo_custo, CF.dt_fechamento, TT.qt_capacidade, T.cd_tanque " +
										" ORDER BY LA.id_local_armazenamento ";
			PreparedStatement pstmtEstoqueAbertura = connect.prepareStatement(sqlEstoqueAbertura);
			pstmtEstoqueAbertura.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			
			String sqlEncerrante = "SELECT SUM(BE.qt_litros) AS qt_litros " +
					               "FROM pcb_bico_encerrante     BE " +
								   "JOIN pcb_bico                B  ON (BE.cd_bico                = B.cd_bico) " +
								   "JOIN pcb_tanque              T  ON (T.cd_tanque               = B.cd_tanque) "+
								   "JOIN alm_local_armazenamento LA ON (LA.cd_local_armazenamento = T.cd_tanque) " +
								   "JOIN adm_conta_fechamento    CF ON (BE.cd_conta               = CF.cd_conta " +
								   "                                AND BE.cd_fechamento          = CF.cd_fechamento) " +
								   "WHERE CAST(CF.dt_fechamento AS DATE) = ? " +
								   "  AND LA.cd_empresa        = " +cdEmpresa+
								   "  AND T.cd_tanque = ? ";
			PreparedStatement pstmtEncerrante = connect.prepareStatement(sqlEncerrante);
			
			
			
			ResultSetMap rsmEstoqueAbertura = new ResultSetMap(pstmtEstoqueAbertura.executeQuery());
			
			rsmEstoqueAbertura.beforeFirst();
			
			while(rsmEstoqueAbertura.next()){
				rsmEstoqueAbertura.setValueToField("CD_PRODUTO_SERVICO", rsmEstoqueAbertura.getInt("cd_produto_servico"));
				rsmEstoqueAbertura.setValueToField("NM_PRODUTO_SERVICO", rsmEstoqueAbertura.getString("nm_produto_servico"));
				rsmEstoqueAbertura.setValueToField("NR_TANQUE", rsmEstoqueAbertura.getString("id_local_armazenamento"));
				rsmEstoqueAbertura.setValueToField("VL_PRECO_CUSTO", rsmEstoqueAbertura.getDouble("vl_ultimo_custo"));
				rsmEstoqueAbertura.setValueToField("QT_CAPACIDADE", rsmEstoqueAbertura.getDouble("qt_capacidade"));
				pstmtEncerrante.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
				pstmtEncerrante.setInt(2, rsmEstoqueAbertura.getInt("cd_tanque"));
				ResultSetMap rsEncerrantes = new ResultSetMap(pstmtEncerrante.executeQuery());
				if(rsEncerrantes.next()){
					rsmEstoqueAbertura.setValueToField("QT_TANQUE", rsEncerrantes.getDouble("qt_litros"));
				}
				rsmEstoqueAbertura.setValueToField("VL_CONTABIL", (rsmEstoqueAbertura.getDouble("vl_ultimo_custo") * rsmEstoqueAbertura.getDouble("QT_TANQUE")));
				rsmEstoqueAbertura.setValueToField("QT_ESTOQUE_COMPLEMENTAR", (rsmEstoqueAbertura.getDouble("QT_CAPACIDADE") - rsmEstoqueAbertura.getDouble("QT_TANQUE")));
			}
			rsmEstoqueAbertura.beforeFirst();
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("dtEstoque", Util.convCalendarString(dtMovimento));
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsmEstoqueAbertura);
			result.addObject("params", param);
			if (isConnectionNull)
				connect.commit();
	
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result gerarRelatorioProdutoEstoque(int cdEmpresa){
		return gerarRelatorioProdutoEstoque(cdEmpresa, new ArrayList<ItemComparator>(), null);
	}
	public static Result gerarRelatorioProdutoEstoque(int cdEmpresa, int stProduto, int lgEstoque, int tpEstoque, int cdGrupo){
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		if(stProduto >= 0 ){
			criterios.add(new ItemComparator("B.st_produto_empresa", Integer.toString(stProduto), ItemComparator.EQUAL, Types.INTEGER));
		}
		criterios.add(new ItemComparator("lgEstoque", Integer.toString(lgEstoque), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("tpEstoque", Integer.toString(tpEstoque), ItemComparator.EQUAL, Types.INTEGER));
		if(cdGrupo > 0){
			criterios.add(new ItemComparator("H.cd_grupo", Integer.toString(cdGrupo), ItemComparator.EQUAL, Types.INTEGER));
		}
		
		return gerarRelatorioProdutoEstoque(cdEmpresa, criterios, null);
	}
	
	public static Result gerarRelatorioProdutoEstoqueCsv(int cdEmpresa, int stProduto, int lgEstoque, int tpEstoque, int cdGrupo){
		try{
			Result result = gerarRelatorioProdutoEstoque(cdEmpresa, stProduto, lgEstoque, tpEstoque, cdGrupo);
			ResultSetMap rsm = (ResultSetMap) result.getObjects().get("rsm"); 
			
			String registro = "N. LINHA;ID;PRODUTO/SERVCO;Fiscal;V.Compra;Transf.;Atacado;Varejo;Orig;T. av.\r\n";
			rsm.beforeFirst();
			while(rsm.next()){
				String novoRegistro = rsm.getString("NR_LINHA") + ";" +
						    rsm.getString("ID_PRODUTO") + ";" +
						    rsm.getString("NM_PRODUTO_SERVICO")+ ";" +
						    rsm.getString("QT_PRODUTO_SOFTWARE")+ ";" +
						    rsm.getString("VL_UNITARIO")+ ";" +
						    rsm.getString("VL_UNITARIO")+ ";" +
						    rsm.getString("VL_ATACADO")+ ";" +
						    rsm.getString("VL_VAREJO")+ ";" +
						    ProdutoServicoEmpresaServices.tiposOrigem[rsm.getInt("TP_ORIGEM")]+ ";" +
						    rsm.getString("AVARIA_VALOR")+"\r\n";
				
				registro += novoRegistro;
			}
			
			
			FileOutputStream gravar;
			File arquivo = new File("relatorio_produto_estoque.csv"); 
			//
			File diretorio = new File("C:\\TIVIC");
			if(!diretorio.exists()){
				diretorio.mkdir();
			}
			//
			gravar = new FileOutputStream("C:\\TIVIC\\" + arquivo);
			gravar.write(registro.getBytes());
			gravar.close();
			
		}
		catch(Exception e){System.out.println("ERRO na gravacao: " + e); return new Result(-1, "Falha na gravação do arquivo");}
		
		return new Result(1, "Arquivo gravado com sucesso");
	}
	
	public static Result gerarRelatorioProdutoEstoque(int cdEmpresa, ArrayList<ItemComparator> criterios){
		return gerarRelatorioProdutoEstoque(cdEmpresa, criterios, null);
	}
	
	public static Result gerarRelatorioProdutoEstoque(int cdEmpresa, ArrayList<ItemComparator> criterios, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			int qtItensPositvos     = 0;
			int qtItensNegativos    = 0;
			int qtItensZerados      = 0;
			int qtTotalItens        = 0;
			int qtProdutosPositivos = 0;
			int qtProdutosNegativos = 0;
			
			criterios.add(new ItemComparator("vl_ultimo_custo", "0", ItemComparator.GREATER, Types.DOUBLE));
			//criterios.add(new ItemComparator("st_produto_empresa", "1", ItemComparator.EQUAL, Types.INTEGER));
			
			ResultSetMap rsm = findProduto(cdEmpresa, criterios);
			
			int cdTipoOperacaoVarejo  = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_VAREJO", 0, cdEmpresa);
			int cdTipoOperacaoAtacado = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_ATACADO", 0, cdEmpresa);
			PreparedStatement pstmtPrecoProduto = connect.prepareStatement("SELECT * FROM adm_produto_servico_preco " +
														"						WHERE cd_produto_servico = ?" +
														"						  AND dt_termino_validade IS NULL" +
														"						  AND (cd_tabela_preco = ? OR cd_tabela_preco = ?)");
			
			int cdTabelaPrecoAtacado = 0;
			if(cdTipoOperacaoAtacado > 0)
				cdTabelaPrecoAtacado = TipoOperacaoDAO.get(cdTipoOperacaoAtacado).getCdTabelaPreco();
			
			int cdTabelaPrecoVarejo = 0;
			if(cdTipoOperacaoVarejo > 0)
				cdTabelaPrecoVarejo = TipoOperacaoDAO.get(cdTipoOperacaoVarejo).getCdTabelaPreco();
			
			ResultSetMap rsmProdutoPreco = new ResultSetMap();
			while(rsm.next()){
				rsm.setValueToField("ID_PRODUTO", (rsm.getString("ID_REDUZIDO") != null && !rsm.getString("ID_REDUZIDO").equals("") ? rsm.getString("ID_REDUZIDO") : 
												   rsm.getString("ID_PRODUTO_SERVICO") != null && !rsm.getString("ID_PRODUTO_SERVICO").equals("") ? rsm.getString("ID_PRODUTO_SERVICO") :
												   rsm.getString("CD_PRODUTO_SERVICO")));
				rsm.setValueToField("NM_PRODUTO_SERVICO", rsm.getString("NM_PRODUTO_SERVICO").trim());
				rsm.setValueToField("QT_PRODUTO_SOFTWARE", rsm.getFloat("QT_ESTOQUE"));
				rsm.setValueToField("VL_UNITARIO", rsm.getFloat("VL_ULTIMO_CUSTO"));
				rsm.setValueToField("VL_TOTAL", (rsm.getFloat("QT_ESTOQUE") * rsm.getFloat("VL_ULTIMO_CUSTO")));
				rsm.setValueToField("D_FUNC_VALOR", "0,00");
				rsm.setValueToField("AVARIA_VALOR", "0,00");
				rsm.setValueToField("ST_PRODUTO_SERVICO", ProdutoServicoEmpresaServices.situacoesProduto[rsm.getInt("ST_PRODUTO_EMPRESA")]);
				
				pstmtPrecoProduto.setInt(1, rsm.getInt("cd_produto_servico"));
				pstmtPrecoProduto.setInt(2, cdTabelaPrecoAtacado);
				pstmtPrecoProduto.setInt(3, cdTabelaPrecoVarejo);
				//
				if (rsm.getFloat("QT_ESTOQUE") > 0){
					qtItensPositvos += 1;
					qtProdutosPositivos += rsm.getFloat("QT_ESTOQUE");
				}
				if (rsm.getFloat("QT_ESTOQUE") < 0){				
					qtItensNegativos += 1;
					qtProdutosNegativos += rsm.getFloat("QT_ESTOQUE");
				}
				if (rsm.getFloat("QT_ESTOQUE") == 0)
					qtItensZerados += 1;
			
				//
				rsmProdutoPreco = new ResultSetMap(pstmtPrecoProduto.executeQuery());
				while(rsmProdutoPreco.next()){
					if(rsmProdutoPreco.getInt("cd_tabela_preco") == cdTabelaPrecoAtacado){
						rsm.setValueToField("VL_ATACADO", rsmProdutoPreco.getFloat("vl_preco"));
						rsm.setValueToField("VL_TOTAL_ATACADO", (rsm.getFloat("QT_ESTOQUE") * rsm.getFloat("VL_ATACADO")));
					}
					if(rsmProdutoPreco.getInt("cd_tabela_preco") == cdTabelaPrecoVarejo){
						rsm.setValueToField("VL_VAREJO", rsmProdutoPreco.getFloat("vl_preco"));
						rsm.setValueToField("VL_TOTAL_VAREJO", (rsm.getFloat("QT_ESTOQUE") * rsm.getFloat("VL_VAREJO")));
					}
				}
				
				if(rsm.getString("VL_ATACADO") == null)
					rsm.setValueToField("VL_ATACADO", 0);
				if (rsm.getString("VL_VAREJO") == null)
					rsm.setValueToField("VL_VAREJO", 0);
				
			}
			rsm.setValueToField("QT_TOTAL_POSITIVOS", (qtItensPositvos));
			rsm.setValueToField("QT_TOTAL_NEGATIVOS", (qtItensNegativos));
			rsm.setValueToField("QT_TOTAL_ZERADOS", (qtItensZerados));
			qtTotalItens = qtItensNegativos + qtItensPositvos + qtItensZerados;
			rsm.setValueToField("QT_TOTAL", (qtTotalItens));
			rsm.setValueToField("QT_TOTAL_PRODUTOS_POS", (qtProdutosPositivos));
			rsm.setValueToField("QT_TOTAL_PRODUTOS_NEG", (qtProdutosNegativos));
			rsm.beforeFirst();
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("NM_PRODUTO_SERVICO");
			rsm.orderBy(fields);
			rsm.beforeFirst();
			
			int count = 1;
			while(rsm.next()){
				rsm.setValueToField("NR_LINHA", count++);
			}
			HashMap<String, Object> param = new HashMap<String, Object>();
			param.put("DT_EMISSAO", Util.formatDate(Util.getDataAtual(), "dd/MM/yyyy HH:mm:ss"));
			PessoaEndereco empresaEndereco = PessoaEnderecoDAO.get(1, cdEmpresa);
			Cidade cidadeEmpresa = CidadeDAO.get(empresaEndereco.getCdCidade());
			param.put("NM_CIDADE", cidadeEmpresa.getNmCidade());
			param.put("SG_ESTADO", cidadeEmpresa.getSgCidade());
			
//			if (rsmProdutoReferencia!=null)
//				param.put("rsmProdutoReferencia", rsmProdutoReferencia.getLines());
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsm);
			result.addObject("params", param);
			if (isConnectionNull)
				connect.commit();
	
			return result;
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	
	public static Result gerarRelatorioNecessidadeCompra( int cdEmpresa, ArrayList<ItemComparator> criterios, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		return gerarRelatorioNecessidadeCompra(cdEmpresa, criterios, dtInicial, dtFinal, null);
	}
	public static Result gerarRelatorioNecessidadeCompra( int cdEmpresa, ArrayList<ItemComparator> criterios, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			
			dtInicial.set(Calendar.HOUR_OF_DAY, 0);
			dtInicial.set(Calendar.MINUTE, 0);
			dtInicial.set(Calendar.SECOND, 0);
			
			dtFinal.set(Calendar.HOUR_OF_DAY, 23);
			dtFinal.set(Calendar.MINUTE, 59);
			dtFinal.set(Calendar.SECOND, 59);
			
			int qtDiasPeriodo = Util.getQuantidadeDias(dtInicial, dtFinal);
			
			HashMap<String, Object> param = new HashMap<String, Object>();
			criterios.add( new ItemComparator("lgEstoque", "4", ItemComparator.EQUAL, Types.INTEGER) );
			
			int cdProdutoServico = 0;
			String conjuntoSubCategorias = "";
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for (int i=0; criterios!=null && i<criterios.size(); i++)	{
				if(criterios.get(i).getColumn().equalsIgnoreCase("D_PRODUTO_SERVICO"))
					cdProdutoServico = Integer.valueOf(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("CD_GRUPO"))
					conjuntoSubCategorias = criterios.get(i).getValue();
				else	{
					crt.add(criterios.get(i));
				}
			}
			
//			ItemComparator dtMovimento = new ItemComparator("dtMovimento", new SimpleDateFormat("dd/MM/yyyy").format( dtInicial.getTime() ), ItemComparator.EQUAL, Types.DATE);
//			criterios.add( dtMovimento );
//			ResultSetMap rsmPeriodoInicial = findProduto(cdEmpresa, criterios);
//			dtMovimento.setValue( new SimpleDateFormat("dd/MM/yyyy").format( dtInicial.getTime() ) );
//			criterios.remove( criterios.size()-1 );
//			criterios.add(dtMovimento);
//			ResultSetMap rsmPeriodoFinal = findProduto(cdEmpresa, criterios);
			
			
//			rsmPeriodoFinal.beforeFirst();
//			rsmGrupos.beforeFirst();
			
//			while( rsmPeriodoFinal.next() ){
//				while( rsmGrupos.next() ){
//					if( rsmPeriodoFinal.getInt("CD_GRUPO_SUPERIOR") == rsmGrupos.getInt("CD_GRUPO") ){
//						rsmPeriodoFinal.setValueToField("NM_GRUPO_SUPERIOR", rsmGrupos.getString("NM_GRUPO"));
//						break;
//					}
//				}
//				rsmGrupos.beforeFirst();
//			}
			
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.cd_local_armazenamento, E.nm_local_armazenamento, C.id_reduzido, B.cd_produto_servico, B.nm_produto_servico, D.sg_unidade_medida, A.qt_minima, " +
					"												   C.vl_ultimo_custo, G.cd_grupo, G.nm_grupo, H.cd_grupo AS cd_grupo_superior, H.nm_grupo AS nm_grupo_superior FROM alm_produto_estoque A " + 
					"											  JOIN grl_produto_servico B ON (A.cd_produto_servico = B.cd_produto_servico) " +
					"											  JOIN grl_produto_servico_empresa C ON (A.cd_produto_servico = C.cd_produto_servico " + 
					"																						AND C.cd_empresa = "+cdEmpresa+") " +
					"											  JOIN grl_unidade_medida D ON (C.cd_unidade_medida = D.cd_unidade_medida) " +
					"											  JOIN alm_local_armazenamento E ON (A.cd_local_armazenamento = E.cd_local_armazenamento) " +
					"											  LEFT OUTER JOIN alm_produto_grupo F ON (A.cd_produto_servico = F.cd_produto_servico " + 
					"																						AND	F.lg_principal = 1) " +
					"											  JOIN alm_grupo G ON (F.cd_grupo = G.cd_grupo) " +
					"											  JOIN alm_grupo H ON (G.cd_grupo_superior = H.cd_grupo) " +
					"											WHERE (A.cd_local_armazenamento_origem IS NULL OR A.cd_local_armazenamento_origem = 0) " + 
					(cdProdutoServico > 0 ? "					  AND A.cd_produto_servico = " + cdProdutoServico : "") + 
					(!conjuntoSubCategorias.equals("") ? "		  AND F.cd_grupo IN (" + conjuntoSubCategorias + ")" : ""));
			
			PreparedStatement pstmtVendaPeriodo = connect.prepareStatement("SELECT SUM(A.qt_saida) AS qt_saida FROM alm_documento_saida_item A " + 
					"														  JOIN alm_documento_saida B ON (A.cd_documento_saida = B.cd_documento_saida) " + 
					"														WHERE A.cd_produto_servico = ? " + 
					"														  AND B.dt_documento_saida >= ? " + 
					"														  AND B.dt_documento_saida <= ? " + 
					"														  AND B.st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			ResultSetMap rsmFinal = new ResultSetMap();
			while(rsm.next()){
				
				pstmtVendaPeriodo.setInt(1, rsm.getInt("cd_produto_servico"));
				pstmtVendaPeriodo.setTimestamp(2, new java.sql.Timestamp(dtInicial.getTimeInMillis()));
				pstmtVendaPeriodo.setTimestamp(3, new java.sql.Timestamp(dtFinal.getTimeInMillis()));
				ResultSetMap rsmVendaPeriodo = new ResultSetMap(pstmtVendaPeriodo.executeQuery());
				if(rsmVendaPeriodo.next()){
					rsm.setValueToField("QT_SAIDA", rsmVendaPeriodo.getFloat("QT_SAIDA"));
					rsm.setValueToField("QT_MEDIA_DIARIA", (rsm.getFloat("QT_SAIDA") / qtDiasPeriodo));
				}
				
				ArrayList<ItemComparator> criteriosEstoqueAtual = new ArrayList<ItemComparator>();
				criteriosEstoqueAtual.add(new ItemComparator("cdLocalArmazenamento", "" + rsm.getInt("cd_local_armazenamento"), ItemComparator.EQUAL, Types.INTEGER));
				criteriosEstoqueAtual.add(new ItemComparator("cdEmpresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
				criteriosEstoqueAtual.add(new ItemComparator("cdProdutoServico", "" + rsm.getInt("cd_produto_servico"), ItemComparator.EQUAL, Types.INTEGER));
				criteriosEstoqueAtual.add(new ItemComparator("dtMovimento", "" + Util.formatDate(Util.getDataAtual(), "dd/MM/yyyy"), ItemComparator.EQUAL, Types.VARCHAR));
				Result rsmEstoqueAtual = getEstoqueAtual(criteriosEstoqueAtual, connect);
				rsm.setValueToField("QT_ESTOQUE", ((Float)rsmEstoqueAtual.getObjects().get("QT_ESTOQUE") + (Float)rsmEstoqueAtual.getObjects().get("QT_ESTOQUE_CONSIGNADO")));
				
				rsm.setValueToField("QT_SUGESTAO_COMPRA", (rsm.getFloat("QT_MINIMA") - rsm.getFloat("QT_ESTOQUE")));
				
				rsm.setValueToField("QT_PROJECAO_VENDA", (rsm.getFloat("QT_MEDIA_DIARIA") * 10));
				
				rsm.setValueToField("QT_ESTOQUE_FUTURO", (rsm.getFloat("QT_ESTOQUE") + rsm.getFloat("QT_SUGESTAO_COMPRA") - rsm.getFloat("QT_PROJECAO_VENDA")));
				
				rsm.setValueToField("VL_COMPRA", (rsm.getFloat("QT_SUGESTAO_COMPRA") * rsm.getFloat("VL_ULTIMO_CUSTO")));
				
				if(rsm.getFloat("QT_SUGESTAO_COMPRA") > 0)
					rsmFinal.addRegister(rsm.getRegister());
				
			}
			rsmFinal.beforeFirst();
			ArrayList<String> orderBy = new ArrayList<String>();
			orderBy.add("NM_GRUPO_SUPERIOR");
			orderBy.add("NM_GRUPO");
			orderBy.add("NM_PRODUTO_SERVICO");
			rsmFinal.orderBy(orderBy);
			
			Result result = new Result(1, "Sucesso!");
			result.addObject("rsm", rsmFinal);
			result.addObject("params", param);
			if (isConnectionNull)
				connect.commit();
			
			return result;
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByProduto(int cdProdutoServico, int cdEmpresa){
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("cd_produto_servico", "" + cdProdutoServico, ItemComparator.EQUAL, java.sql.Types.INTEGER));
		criterios.add(new ItemComparator("cd_empresa", "" + cdEmpresa, ItemComparator.EQUAL, java.sql.Types.INTEGER));
		ResultSetMap rsm = ProdutoEstoqueDAO.find(criterios);
		while(rsm.next()){
			rsm.setValueToField("NM_LOCAL_ARMAZENAMENTO", LocalArmazenamentoDAO.get(rsm.getInt("cd_local_armazenamento")).getNmLocalArmazenamento());
			if(rsm.getInt("cd_local_armazenamento_origem") > 0)
				rsm.setValueToField("NM_LOCAL_ARMAZENAMENTO_ORIGEM", LocalArmazenamentoDAO.get(rsm.getInt("cd_local_armazenamento_origem")).getNmLocalArmazenamento());
			rsm.setValueToField("CL_TP_REABASTECIMENTO", ProdutoServicoEmpresaServices.tiposAbastecimento[rsm.getInt("tp_abastecimento")]);
			rsm.setValueToField("CL_ST_ESTOQUE", tipoSituacao[rsm.getInt("st_estoque")]);
			
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cdLocalArmazenamento", "" + rsm.getInt("cd_local_armazenamento"), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cdEmpresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cdProdutoServico", "" + cdProdutoServico, ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("dtMovimento", "" + Util.formatDate(Util.getDataAtual(), "dd/MM/yyyy"), ItemComparator.EQUAL, Types.VARCHAR));
			Result resultado = ProdutoEstoqueServices.getEstoqueAtual(criterios);
			rsm.setValueToField("QT_ESTOQUE", ((Float)resultado.getObjects().get("QT_ESTOQUE") + (Float)resultado.getObjects().get("QT_ESTOQUE_CONSIGNADO")));
		}
		rsm.beforeFirst();
		return rsm;
	}
	
	public static ResultSetMap getProdutoEstoque(int cdProdutoServico, int cdEmpresa, int cdLocalArmazenamento){
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("cd_produto_servico", "" + cdProdutoServico, ItemComparator.EQUAL, java.sql.Types.INTEGER));
		criterios.add(new ItemComparator("cd_empresa", "" + cdEmpresa, ItemComparator.EQUAL, java.sql.Types.INTEGER));
		criterios.add(new ItemComparator("cd_local_armazenamento", "" + cdLocalArmazenamento, ItemComparator.EQUAL, java.sql.Types.INTEGER));
		ResultSetMap rsm = ProdutoEstoqueDAO.find(criterios);
		while(rsm.next()){
			rsm.setValueToField("NM_LOCAL_ARMAZENAMENTO", LocalArmazenamentoDAO.get(rsm.getInt("cd_local_armazenamento")).getNmLocalArmazenamento());
			if(rsm.getInt("cd_local_armazenamento_origem") > 0)
				rsm.setValueToField("NM_LOCAL_ARMAZENAMENTO_ORIGEM", LocalArmazenamentoDAO.get(rsm.getInt("cd_local_armazenamento_origem")).getNmLocalArmazenamento());
			else
				rsm.setValueToField("NM_LOCAL_ARMAZENAMENTO_ORIGEM", "");
			rsm.setValueToField("CL_TP_REABASTECIMENTO", ProdutoServicoEmpresaServices.tiposAbastecimento[rsm.getInt("tp_abastecimento")]);
			rsm.setValueToField("CL_ST_ESTOQUE", tipoSituacao[rsm.getInt("st_estoque")]);
		}
		rsm.beforeFirst();
		return rsm;
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		ResultSetMap rsm = Search.find("SELECT A.*, B.nm_local_armazenamento, C.nm_local_armazenamento AS nm_local_armazenamento_origem FROM alm_produto_estoque A "
				+ "			  JOIN alm_local_armazenamento B ON (A.cd_local_armazenamento = B.cd_local_armazenamento) "
				+ "			  LEFT OUTER JOIN alm_local_armazenamento C ON (A.cd_local_armazenamento_origem = C.cd_local_armazenamento) ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
		
		while(rsm.next()){
			rsm.setValueToField("CL_TP_REABASTECIMENTO", ProdutoServicoEmpresaServices.tiposAbastecimento[rsm.getInt("tp_abastecimento")]);
			rsm.setValueToField("CL_ST_ESTOQUE", tipoSituacao[rsm.getInt("st_estoque")]);
		}
		rsm.beforeFirst();
		return rsm;
	}
	
	public static void main(String aaa[]){
		ItemComparator i = new ItemComparator("lgEstoque", "1", ItemComparator.EQUAL, java.sql.Types.INTEGER);
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(i);
		findProduto(3201, criterios, null);
	}

	public static Result getEstoqueAtual(ArrayList<ItemComparator> criterios) {
		return getEstoqueAtual(criterios, null);
	}
	
	public static Result getEstoqueAtual(ArrayList<ItemComparator> criterios, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			//
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			int cdEmpresa = 0;
			String cdLocalArmazenamento = "";
			int cdProdutoServico = 0;
			GregorianCalendar dtMovimento = null;
			GregorianCalendar dtBalanco   = null;
			boolean lgFiscal = false;
			ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
			for( ItemComparator item : criterios ){
				if( item.getColumn().equalsIgnoreCase("cdEmpresa")) {
					cdEmpresa = Integer.parseInt(item.getValue());
				}
				else if( item.getColumn().equalsIgnoreCase("cdLocalArmazenamento")) {
					cdLocalArmazenamento = item.getValue();
				}
				else if( item.getColumn().equalsIgnoreCase("cdProdutoServico")) {
					cdProdutoServico = Integer.parseInt(item.getValue());
				}
				else if( item.getColumn().equalsIgnoreCase("dtMovimento")) {
					dtMovimento = Util.convStringToCalendar(item.getValue());
					dtMovimento.set(Calendar.HOUR_OF_DAY, 23);
					dtMovimento.set(Calendar.MINUTE, 59);
					dtMovimento.set(Calendar.SECOND, 59);
					dtMovimento.set(Calendar.MILLISECOND, 0);
				}
				else if( item.getColumn().equalsIgnoreCase("lgFiscal")) {
					lgFiscal = Boolean.parseBoolean(item.getValue());
				}
				else{
					crt.add(item);
				}
			}
			
			PreparedStatement pstmtBalanco = connection.prepareStatement(
				"  SELECT A.*, B.* "
			  + "    FROM alm_balanco_produto_servico A "
			  + "    JOIN alm_balanco                 B ON (A.cd_balanco = B.cd_balanco) "
			  + "   WHERE A.cd_produto_servico = ? "
			  + "     AND B.st_balanco = ? "
			  + "ORDER BY B.cd_balanco DESC "
			  + "   LIMIT 1"
			);			
			pstmtBalanco.setInt(1, cdProdutoServico);
			pstmtBalanco.setInt(2, BalancoServices.ST_CONCLUIDO);
			
			ResultSetMap rsmBalanco = new ResultSetMap(pstmtBalanco.executeQuery());
			
			Double qtBalanco = 0.0;
			while(rsmBalanco.next()){
				dtBalanco = rsmBalanco.getGregorianCalendar("DT_BALANCO");
				qtBalanco = rsmBalanco.getDouble("QT_ESTOQUE_BALANCO");
			}
			rsmBalanco.beforeFirst();
			
			// ENTRADAS
			PreparedStatement pstmt = connection.prepareStatement("SELECT SUM(D.qt_entrada) AS qt_entrada, " +
																  "	SUM(D.qt_entrada * C.vl_unitario + (C.vl_acrescimo * (D.qt_entrada/C.qt_entrada))) AS vl_entrada " +
													   			  "FROM alm_entrada_local_item D, alm_documento_entrada E, alm_documento_entrada_item C " +
													   			  "WHERE D.cd_documento_entrada = E.cd_documento_entrada " +
													   			  "  AND D.cd_documento_entrada = C.cd_documento_entrada " +
													   			  "  AND D.cd_item = C.cd_item " +
															      "  AND D.cd_produto_servico   = C.cd_produto_servico " +
															      "  AND D.cd_empresa           = C.cd_empresa " +
													   			  "  AND E.st_documento_entrada = " + DocumentoEntradaServices.ST_LIBERADO +
													   			  "  AND E.tp_entrada NOT IN (" + DocumentoEntradaServices.ENT_CONSIGNACAO + ", " + DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO + ")" + 
													   			  (cdEmpresa>0 ? "  AND D.cd_empresa         = " +cdEmpresa : "") +
													   			  (cdProdutoServico>0 ? "  AND D.cd_produto_servico   = " +cdProdutoServico : "") +
													   			  (cdLocalArmazenamento != null && !cdLocalArmazenamento.equals("") ? "  AND D.cd_local_armazenamento IN ("+cdLocalArmazenamento + " )" : "") +
													   			  (dtMovimento!=null          ? " AND E.dt_documento_entrada  <= ? " : "") +
												   			      (dtBalanco!=null            ? " AND E.dt_documento_entrada  >= ? " : "") +
													   			  (lgFiscal ? " AND (E.tp_documento_entrada IN (" + DocumentoEntradaServices.TP_CUPOM_FISCAL + ", " + DocumentoEntradaServices.TP_NOTA_ELETRONICA + ", " + DocumentoEntradaServices.TP_NOTA_FISCAL + ") " + 
														   					"		OR EXISTS (SELECT FD.cd_documento_entrada " +
															   			    "				FROM fsc_nota_fiscal_doc_vinculado FD " +
															   			    "			 WHERE FD.cd_documento_entrada = E.cd_documento_entrada))" : "") + 
															   	  " AND NOT EXISTS (SELECT * FROM pcb_tanque TAN WHERE TAN.cd_tanque = D.cd_local_armazenamento)");
			if(dtMovimento!=null)
				pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			if(dtMovimento==null && dtBalanco!=null)
				pstmt.setTimestamp(1, new Timestamp(dtBalanco.getTimeInMillis()));
			
			ResultSetMap rs    = new ResultSetMap(pstmt.executeQuery());
			Double qtEstoque = 0.0;
			Double qtEntrada = 0.0; 
			Double vlEntrada = 0.0;
			if(rs.next()){
				qtEstoque = rs.getDouble("qt_entrada") + (qtBalanco != null ? qtBalanco : 0.0);
				qtEntrada = rs.getDouble("qt_entrada");
				vlEntrada = rs.getDouble("vl_entrada");
			}
			
			// ENTRADAS CONSIGNADAS
			pstmt = connection.prepareStatement("SELECT SUM(D.qt_entrada_consignada) AS qt_entrada, " +
												"	SUM(D.qt_entrada * C.vl_unitario + (C.vl_acrescimo * (D.qt_entrada/C.qt_entrada))) AS vl_entrada " +
													   			  "FROM alm_entrada_local_item D, alm_documento_entrada E, alm_documento_entrada_item C " +
													   			  "WHERE D.cd_documento_entrada = E.cd_documento_entrada " +
													   			  "  AND D.cd_documento_entrada = C.cd_documento_entrada " +
													   			  "  AND D.cd_item = C.cd_item " +
															      "  AND D.cd_produto_servico   = C.cd_produto_servico " +
															      "  AND D.cd_empresa           = C.cd_empresa " +
													   			  "  AND E.st_documento_entrada = " + DocumentoEntradaServices.ST_LIBERADO +
													   			  "  AND E.tp_entrada IN (" + DocumentoEntradaServices.ENT_CONSIGNACAO + ", " + DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO + ")" + 
													   			  (cdEmpresa>0 ? "  AND D.cd_empresa         = " +cdEmpresa : "") +
													   			  (cdProdutoServico>0 ? "  AND D.cd_produto_servico   = " +cdProdutoServico : "") +
													   			  (cdLocalArmazenamento != null && !cdLocalArmazenamento.equals("") ? "  AND D.cd_local_armazenamento IN ("+cdLocalArmazenamento + " )" : "") +		
													   			  (dtMovimento!=null          ? " AND E.dt_documento_entrada  <= ? " : "") +
													   			  (dtBalanco!=null            ? " AND E.dt_documento_entrada  >= ? " : "") +
													   			  (lgFiscal ? " AND (E.tp_documento_entrada IN (" + DocumentoEntradaServices.TP_CUPOM_FISCAL + ", " + DocumentoEntradaServices.TP_NOTA_ELETRONICA + ", " + DocumentoEntradaServices.TP_NOTA_FISCAL + ") " +
														   					"		OR EXISTS (SELECT FD.cd_documento_entrada " +
															   			    "				FROM fsc_nota_fiscal_doc_vinculado FD " +
															   			    "			 WHERE FD.cd_documento_entrada = E.cd_documento_entrada))" : "") + 
																  " AND NOT EXISTS (SELECT * FROM pcb_tanque TAN WHERE TAN.cd_tanque = D.cd_local_armazenamento)");
			if(dtMovimento!=null)
				pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			if(dtMovimento==null && dtBalanco!=null)
				pstmt.setTimestamp(1, new Timestamp(dtBalanco.getTimeInMillis()));
			
			rs    = new ResultSetMap(pstmt.executeQuery());
			Double qtEstoqueConsignado = 0.0;
			Double qtEntradaConsignada = 0.0;
			Double vlEntradaConsignada = 0.0;
			if(rs.next()){
				qtEstoqueConsignado = rs.getDouble("qt_entrada");
				qtEntradaConsignada = rs.getDouble("qt_entrada");
				vlEntradaConsignada = rs.getDouble("vl_entrada");
			}
			
			// SAÍDAS
			pstmt = connection.prepareStatement("SELECT SUM(D.qt_saida) AS qt_saida, " +
												"       SUM(D.qt_saida * C.vl_unitario + (C.vl_acrescimo * (D.qt_saida/C.qt_saida))) AS vl_saida " +
								   			    "FROM alm_saida_local_item D, alm_documento_saida E, alm_documento_saida_item C " +
								   			    "WHERE D.cd_documento_saida = E.cd_documento_saida " +
								   			    "  AND D.cd_documento_saida = C.cd_documento_saida " +
								   			    "  AND D.cd_item = C.cd_item " +
										        "  AND D.cd_produto_servico   = C.cd_produto_servico " +
										        "  AND D.cd_empresa           = C.cd_empresa " +
								   			    "  AND E.st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO +
								   			    "  AND E.tp_saida NOT IN (" + DocumentoSaidaServices.SAI_VENDA_CONSIGNACAO + ", " + DocumentoSaidaServices.SAI_ACERTO_CONSIGNACAO + ")" + 
								   			    (cdEmpresa>0 ? "  AND D.cd_empresa         = " +cdEmpresa : "") +
								   			    (cdProdutoServico>0 ? "  AND D.cd_produto_servico   = " +cdProdutoServico : "") +
								   			    (cdLocalArmazenamento != null && !cdLocalArmazenamento.equals("") ? "  AND D.cd_local_armazenamento IN ("+cdLocalArmazenamento + " )" : "") +	 
								   			    (dtMovimento!=null          ? " AND E.dt_documento_saida    <= ? " : "") + 
								   			    (dtBalanco!=null            ? " AND E.dt_documento_saida  >= ? " : "") +
								   			    (lgFiscal ? " AND (E.tp_documento_saida IN (" + DocumentoSaidaServices.TP_CUPOM_FISCAL + ", " + DocumentoSaidaServices.TP_NFE + ", " + DocumentoSaidaServices.TP_NOTA_FISCAL_VENDA + ", " + DocumentoSaidaServices.TP_NOTA_FISCAL_02 + ") "+ 
											   			    "		OR EXISTS (SELECT FD.cd_documento_saida " +
											   			    "				FROM fsc_nota_fiscal_doc_vinculado FD " +
											   			    "			 WHERE FD.cd_documento_saida = E.cd_documento_saida))" : "") + 
												" AND NOT EXISTS (SELECT * FROM pcb_tanque TAN WHERE TAN.cd_tanque = D.cd_local_armazenamento)");
			
			if(dtMovimento!=null)
				pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			if(dtMovimento==null && dtBalanco!=null)
				pstmt.setTimestamp(1, new Timestamp(dtBalanco.getTimeInMillis()));
			
			rs         = new ResultSetMap(pstmt.executeQuery());
			Double qtSaida = 0.0; 
			Double vlSaida = 0.0;
			if(rs.next()){
				qtEstoque -= rs.getDouble("qt_saida");
				qtSaida    = rs.getDouble("qt_saida");
				vlSaida    = rs.getDouble("vl_saida");
			}
			
			// SAÍDAS CONSIGNADAS
			pstmt = connection.prepareStatement("SELECT SUM(D.qt_saida_consignada) AS qt_saida " +
								   			    "FROM alm_saida_local_item D, alm_documento_saida E " +
								   			    "WHERE D.cd_documento_saida = E.cd_documento_saida " +
								   			    "  AND E.st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO +
								   			    "  AND E.tp_saida IN (" + DocumentoSaidaServices.SAI_VENDA_CONSIGNACAO + ", " + DocumentoSaidaServices.SAI_ACERTO_CONSIGNACAO + ")" + 
								   			    (cdEmpresa>0 ? "  AND D.cd_empresa         = " +cdEmpresa : "") +
								   			    (cdProdutoServico>0 ? "  AND D.cd_produto_servico   = " +cdProdutoServico : "") +
								   			    (cdLocalArmazenamento != null && !cdLocalArmazenamento.equals("") ? "  AND D.cd_local_armazenamento IN ("+cdLocalArmazenamento + " )" : "") + 
								   			    (dtMovimento!=null          ? " AND E.dt_documento_saida    <= ? " : "") +
								   			    (dtBalanco!=null            ? " AND E.dt_documento_saida  >= ? " : "") +
								   			    (lgFiscal ? " AND (E.tp_documento_saida IN (" + DocumentoSaidaServices.TP_CUPOM_FISCAL + ", " + DocumentoSaidaServices.TP_NFE + ", " + DocumentoSaidaServices.TP_NOTA_FISCAL_VENDA + ", " + DocumentoSaidaServices.TP_NOTA_FISCAL_02 + ") " +
									   			    		"		OR EXISTS (SELECT FD.cd_documento_saida " +
											   			    "				FROM fsc_nota_fiscal_doc_vinculado FD " +
											   			    "			 WHERE FD.cd_documento_saida = E.cd_documento_saida))" : "") + 
												" AND NOT EXISTS (SELECT * FROM pcb_tanque TAN WHERE TAN.cd_tanque = D.cd_local_armazenamento)");
			if(dtMovimento!=null)
				pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
			if(dtMovimento==null && dtBalanco!=null)
				pstmt.setTimestamp(1, new Timestamp(dtBalanco.getTimeInMillis()));
			
			rs         = new ResultSetMap(pstmt.executeQuery());
			float qtSaidaConsignada = rs.next() ? rs.getFloat("qt_saida") : 0;
			qtEstoqueConsignado -= qtSaidaConsignada;
			
			// Estoque Combustível
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_produto_servico", "" + cdProdutoServico, ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmProdutoGrupo = ProdutoGrupoDAO.find(criterios, connection);
			// Verifique se estamos trabalhando com combustiveis..
			
			ArrayList<Integer> cdGrupoCombustivel = GrupoServices.getAllCombustivelAsArray(cdEmpresa, connection);

			boolean isCombustivel = false;
			while(rsmProdutoGrupo.next()){
				for(int i = 0; i < cdGrupoCombustivel.size(); i++){
					if(rsmProdutoGrupo.getInt("cd_grupo") == cdGrupoCombustivel.get(i).intValue()){
						isCombustivel = true;
						break;
					}
						
				}
				if(isCombustivel)
					break;
			}
			if(isCombustivel){
				GregorianCalendar diaAnterior = Util.getDataAtual();
				if(dtMovimento != null){
					dtMovimento.set(Calendar.HOUR, 0);
					dtMovimento.set(Calendar.MINUTE, 0);
					dtMovimento.set(Calendar.SECOND, 0);
					dtMovimento.set(Calendar.MILLISECOND, 0);
					
					diaAnterior.set(Calendar.DAY_OF_MONTH, dtMovimento.get(Calendar.DAY_OF_MONTH));
					diaAnterior.set(Calendar.MONTH, dtMovimento.get(Calendar.MONTH));
					diaAnterior.set(Calendar.YEAR, dtMovimento.get(Calendar.YEAR));
					diaAnterior.set(Calendar.HOUR, dtMovimento.get(Calendar.HOUR));
					diaAnterior.set(Calendar.MINUTE, dtMovimento.get(Calendar.MINUTE));
					diaAnterior.set(Calendar.SECOND, dtMovimento.get(Calendar.SECOND));
					diaAnterior.set(Calendar.MILLISECOND, dtMovimento.get(Calendar.MILLISECOND));
				}
				
				diaAnterior.add(Calendar.DAY_OF_MONTH, -1);
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("CAST(dt_fechamento AS DATE)", Util.formatDate(diaAnterior, "dd/MM/yyyy"), ItemComparator.EQUAL, Types.TIMESTAMP));
				ResultSetMap rsmContaFechamento = ContaFechamentoDAO.find(criterios, connection);
				int contagem = 365;
				qtEstoque = 0.0;
				while(!rsmContaFechamento.next() && contagem > 0){
					criterios = new ArrayList<ItemComparator>();
					diaAnterior.add(Calendar.DAY_OF_MONTH, -1);
					criterios.add(new ItemComparator("CAST(dt_fechamento AS DATE)", Util.formatDate(diaAnterior, "dd/MM/yyyy"), ItemComparator.EQUAL, Types.TIMESTAMP));
					rsmContaFechamento = ContaFechamentoDAO.find(criterios, connection);
					contagem--;
				}
				do{
					if(contagem == 0)
						break;
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("cd_fechamento", "" + rsmContaFechamento.getInt("cd_fechamento"), ItemComparator.EQUAL, Types.INTEGER));
					criterios.add(new ItemComparator("cd_combustivel", "" + cdProdutoServico, ItemComparator.EQUAL, Types.INTEGER));
					if(cdLocalArmazenamento != null && !cdLocalArmazenamento.equals("") && TanqueServices.verificaLocalTanque(cdLocalArmazenamento))
						criterios.add(new ItemComparator("MF.cd_tanque", "" + cdLocalArmazenamento, ItemComparator.IN, Types.INTEGER));
					else if(cdLocalArmazenamento != null && !cdLocalArmazenamento.equals("") && !TanqueServices.verificaLocalTanque(cdLocalArmazenamento))
						criterios.add(new ItemComparator("MF.cd_tanque", "0", ItemComparator.IN, Types.INTEGER));
					ResultSetMap rsmMedicaFisica = MedicaoFisicaDAO.find(criterios, connection);
					while(rsmMedicaFisica.next()){
						qtEstoque += rsmMedicaFisica.getFloat("qt_volume");
					}	
				}while(rsmContaFechamento.next());
				
				//Entrada de Combustivel
				pstmt = connection.prepareStatement("SELECT SUM(D.qt_entrada) AS qt_entrada, " +
										  "	SUM(D.qt_entrada * C.vl_unitario + (C.vl_acrescimo * (D.qt_entrada/C.qt_entrada))) AS vl_entrada " +
							   			  "FROM alm_entrada_local_item D, alm_documento_entrada E, alm_documento_entrada_item C " +
							   			  "WHERE D.cd_documento_entrada = E.cd_documento_entrada " +
							   			  "  AND D.cd_documento_entrada = C.cd_documento_entrada " +
									      "  AND D.cd_produto_servico   = C.cd_produto_servico " +
									      "  AND D.cd_empresa           = C.cd_empresa " +
							   			  "  AND E.st_documento_entrada = " + DocumentoEntradaServices.ST_LIBERADO +
							   			  "  AND E.tp_entrada NOT IN (" + DocumentoEntradaServices.ENT_CONSIGNACAO + ", " + DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO + ")" + 
							   			  (cdEmpresa>0 ? "  AND D.cd_empresa         = " +cdEmpresa : "") +
							   			  (cdProdutoServico>0 ? "  AND D.cd_produto_servico   = " +cdProdutoServico : "") +
							   			  (cdLocalArmazenamento != null && !cdLocalArmazenamento.equals("") && TanqueServices.verificaLocalTanque(cdLocalArmazenamento) ? "  AND D.cd_local_armazenamento IN ("+cdLocalArmazenamento + " )" : (cdLocalArmazenamento != null && !cdLocalArmazenamento.equals("") && !TanqueServices.verificaLocalTanque(cdLocalArmazenamento) ? " AND D.cd_local_armazenamento = 0" : "")) + 
							   			  (dtMovimento!=null          ? " AND CAST(E.dt_documento_entrada AS DATE) = ? " : "") +
							   			  (lgFiscal ? " AND (E.tp_documento_entrada IN (" + DocumentoEntradaServices.TP_CUPOM_FISCAL + ", " + DocumentoEntradaServices.TP_NOTA_ELETRONICA + ", " + DocumentoEntradaServices.TP_NOTA_FISCAL + ") " + 
								   					"		OR EXISTS (SELECT FD.cd_documento_entrada " +
									   			    "				FROM fsc_nota_fiscal_doc_vinculado FD " +
									   			    "			 WHERE FD.cd_documento_entrada = E.cd_documento_entrada))" : ""));
				if(dtMovimento!=null)
					pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
					rs    = new ResultSetMap(pstmt.executeQuery());
					qtEntrada = 0.0; 
					vlEntrada = 0.0;
					if(rs.next()){
					qtEstoque += rs.getDouble("qt_entrada");
					qtEntrada  = rs.getDouble("qt_entrada");
					vlEntrada  = rs.getDouble("vl_entrada");
				}
					
					
				// SAÍDAS de Combustível
				pstmt = connection.prepareStatement("SELECT SUM(D.qt_saida) AS qt_saida, " +
													"       SUM(D.qt_saida * C.vl_unitario + (C.vl_acrescimo * (D.qt_saida/C.qt_saida))) AS vl_saida " +
									   			    "FROM alm_saida_local_item D, alm_documento_saida E, alm_documento_saida_item C " +
									   			    "WHERE D.cd_documento_saida = E.cd_documento_saida " +
									   			    "  AND D.cd_documento_saida = C.cd_documento_saida " +
											        "  AND D.cd_produto_servico   = C.cd_produto_servico " +
											        "  AND D.cd_empresa           = C.cd_empresa " +
									   			    "  AND E.st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO +
									   			    "  AND E.tp_saida NOT IN (" + DocumentoSaidaServices.SAI_VENDA_CONSIGNACAO + ", " + DocumentoSaidaServices.SAI_ACERTO_CONSIGNACAO + ")" + 
									   			    (cdEmpresa>0 ? "  AND D.cd_empresa         = " +cdEmpresa : "") +
									   			    (cdProdutoServico>0 ? "  AND D.cd_produto_servico   = " +cdProdutoServico : "") +
									   			    (cdLocalArmazenamento != null && !cdLocalArmazenamento.equals("") && TanqueServices.verificaLocalTanque(cdLocalArmazenamento) ? "  AND D.cd_local_armazenamento IN ("+cdLocalArmazenamento + " )" : (cdLocalArmazenamento != null && !cdLocalArmazenamento.equals("") && !TanqueServices.verificaLocalTanque(cdLocalArmazenamento) ? " AND D.cd_local_armazenamento = 0" : "")) +
									   			    (dtMovimento!=null          ? " AND CAST(E.dt_documento_saida AS DATE) = ? " : "") + 
									   			    (lgFiscal ? " AND (E.tp_documento_saida IN (" + DocumentoSaidaServices.TP_CUPOM_FISCAL + ", " + DocumentoSaidaServices.TP_NFE + ", " + DocumentoSaidaServices.TP_NOTA_FISCAL_VENDA + ", " + DocumentoSaidaServices.TP_NOTA_FISCAL_02 + ") "+ 
												   			    "		OR EXISTS (SELECT FD.cd_documento_saida " +
												   			    "				FROM fsc_nota_fiscal_doc_vinculado FD " +
												   			    "			 WHERE FD.cd_documento_saida = E.cd_documento_saida))" : ""));
				
				if(dtMovimento!=null)
					pstmt.setTimestamp(1, new Timestamp(dtMovimento.getTimeInMillis()));
				rs         = new ResultSetMap(pstmt.executeQuery());
				qtSaida = 0.0; 
				vlSaida = 0.0;
				if(rs.next()){
					qtEstoque -= rs.getDouble("qt_saida");
					qtSaida = rs.getDouble("qt_saida");
					vlSaida = rs.getDouble("vl_saida");
				}	
				
			}
			
			Result resultado = new Result(1);
			
			resultado.addObject("QT_ESTOQUE", qtEstoque);
			resultado.addObject("QT_ESTOQUE_CONSIGNADO", qtEstoqueConsignado);
			resultado.addObject("QT_ENTRADA", qtEntrada);
			resultado.addObject("VL_ENTRADA", vlEntrada);
			resultado.addObject("VL_ENTRADA_CONSIGNADA", vlEntradaConsignada);
			resultado.addObject("QT_ENTRADA_CONSIGNADA", qtEntradaConsignada);
			resultado.addObject("QT_SAIDA", qtSaida);
			resultado.addObject("QT_SAIDA_CONSIGANADA", qtSaidaConsignada);
			resultado.addObject("VL_SAIDA", vlSaida);
			
			return resultado;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return new Result(-1);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	/**
	 * Calcula o estoque e multiplica pelo custo médio
	 * 
	 * @param criterios
	 * @param connect
	 * @return
	 * @author Luiz Romario Filho
	 */
	public static ResultSetMap getDreEstoque(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect == null;
		int cdEmpresa = 0;
		String dtFim = null;
		for (ItemComparator item : criterios) {
			if(item.getColumn().equalsIgnoreCase("cdEmpresa")){
				cdEmpresa = Integer.parseInt(item.getValue());
			} else if(item.getColumn().equalsIgnoreCase("dtFim")){
				dtFim = item.getValue();
			}
		}
		
		try {
			ResultSetMap resultSetMap = new ResultSetMap();
			
			// Busca as entradas
			String sqlEntrada = "SELECT DISTINCT SUM(qt_entrada) AS qt_entrada, prod.cd_produto_servico "
								+ " ,(SELECT vl_unitario " 
								+ " 	FROM alm_documento_entrada_item i  "
								+ " 	WHERE i.cd_produto_servico = prod.cd_produto_servico "
								+ " 	ORDER BY i.cd_documento_entrada DESC LIMIT 1 ),"
								+ " grupo_superior.cd_grupo, grupo_superior.nm_grupo  "
								+ " FROM alm_documento_entrada_item item "
								+ " JOIN grl_produto_servico prod ON prod.cd_produto_servico = item.cd_produto_servico "
								+ " JOIN alm_documento_entrada doc ON doc.cd_documento_entrada = item.cd_documento_entrada "
								+ " JOIN alm_produto_grupo pg ON pg.cd_produto_servico = prod.cd_produto_servico "
								+ " JOIN alm_grupo grupo ON grupo.cd_grupo = pg.cd_grupo "
								+ " JOIN alm_grupo grupo_superior ON grupo_superior.cd_grupo = grupo.cd_grupo_superior "
								+ " WHERE 1=1 "
								+ (dtFim!= null && !dtFim.equals("") ? " AND doc.dt_documento_entrada <= '" + dtFim + "'" : "")
								+ " AND doc.st_documento_entrada = 1 "
								+ " GROUP BY prod.cd_produto_servico, grupo_superior.cd_grupo, grupo_superior.nm_grupo "
								+ " ORDER BY grupo_superior.cd_grupo";
			ResultSetMap rsmEntrada = new ResultSetMap(connect.prepareStatement(sqlEntrada).executeQuery());
			
			// Busca as saidas
			String sqlSaida =  "SELECT SUM(qt_saida) AS qt_saida, prod.cd_produto_servico, grupo_superior.cd_grupo, grupo_superior.nm_grupo "
								+ " FROM alm_documento_saida_item item "
								+ " JOIN grl_produto_servico prod ON prod.cd_produto_servico = item.cd_produto_servico "
								+ " JOIN alm_documento_saida doc ON doc.cd_documento_saida = item.cd_documento_saida "
								+ " JOIN alm_produto_grupo pg ON pg.cd_produto_servico = prod.cd_produto_servico "
								+ " JOIN alm_grupo grupo ON grupo.cd_grupo = pg.cd_grupo "
								+ " JOIN alm_grupo grupo_superior ON grupo_superior.cd_grupo = grupo.cd_grupo_superior "
								+ " WHERE 1=1 "
								+ (dtFim!= null && !dtFim.equals("") ? " AND doc.dt_documento_saida <= '" + dtFim + "'" : "")
								+ " AND doc.st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO
								+ " GROUP BY prod.cd_produto_servico, grupo_superior.cd_grupo, grupo_superior.nm_grupo"
								+ " ORDER BY grupo_superior.cd_grupo";
			
			ResultSetMap rsmSaida = new ResultSetMap(connect.prepareStatement(sqlSaida).executeQuery());
			ResultSetMap rsmEstoque = new ResultSetMap();
			
			// Itera sobre a entrada e a saida para calcular o estoque
			while(rsmEntrada.next()){
				while(rsmSaida.next()){
					if(rsmEntrada.getInt("CD_PRODUTO_SERVICO") == rsmSaida.getInt("CD_PRODUTO_SERVICO")){
						HashMap<String, Object> register = new HashMap<String, Object>();
						register.put("QT_ESTOQUE", rsmEntrada.getDouble("QT_ENTRADA") - rsmSaida.getDouble("QT_SAIDA"));
						register.put("CD_PRODUTO_SERVICO", rsmEntrada.getInt("CD_PRODUTO_SERVICO"));
						register.put("VL_UNITARIO", rsmEntrada.getDouble("VL_UNITARIO"));
						register.put("CD_GRUPO", rsmEntrada.getInt("CD_GRUPO"));
						register.put("NM_GRUPO", rsmEntrada.getString("NM_GRUPO"));
						rsmEstoque.getLines().add(register);
						rsmEntrada.deleteRow(); // exclui a linha da entrada para não duplicar quando adicionar no estoque
						break;
					}
				}
				rsmSaida.beforeFirst();
			}
			rsmEstoque.getLines().addAll(rsmEntrada.getLines()); // adiciona os produtos que tiveram entrada mas não tiveram saídas
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("CD_GRUPO");
			rsmEstoque.orderBy(fields);
			
			ResultSetMap rsm = new ResultSetMap();
			
			// Itera sobre o estoque para calcular o valor monetário
			while(rsmEstoque.next()){
				HashMap<String, Object> register = new HashMap<String, Object>();
				double vlEstoque = rsmEstoque.getObject("QT_ESTOQUE") != null 
									? rsmEstoque.getDouble("VL_UNITARIO") * rsmEstoque.getDouble("QT_ESTOQUE") 
									: rsmEstoque.getDouble("VL_UNITARIO") * rsmEstoque.getDouble("QT_ENTRADA");
				register.put("VL_ESTOQUE", vlEstoque);
				register.put("CD_GRUPO", rsmEstoque.getInt("CD_GRUPO"));
				register.put("NM_GRUPO", rsmEstoque.getString("NM_GRUPO"));
				rsm.addRegister(register);
				
			}
			
			// Itera sobre o rsm final para agrupar pelo Grupo de Produto
			int cdGrupo = 0;
			String nmGrupo = "";
			double vlTotalGrupo = 0;
			
			while(rsm.next()) {
				if(rsm.getInt("CD_GRUPO")!=cdGrupo) {
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("NM_GRUPO", nmGrupo);
					register.put("VL_TOTAL", vlTotalGrupo);
					resultSetMap.addRegister(register);
					
					vlTotalGrupo = 0;
					cdGrupo = rsm.getInt("CD_GRUPO");
					nmGrupo = rsm.getString("NM_GRUPO");
				}
				vlTotalGrupo += rsm.getDouble("VL_ESTOQUE");
			}
			
			resultSetMap.beforeFirst();
			
			// Itera no rsm para organizar os níveis
			int nrCategoria = 0;
			while(resultSetMap.next()){
				if(resultSetMap.getPosition() == 0){
					resultSetMap.deleteRow();
				}
				resultSetMap.setValueToField("NR_CATEGORIA", "7.1.2.001." + String.format("%04d", ++nrCategoria));
				resultSetMap.setValueToField("NR_NIVEL", 5);
			}
			resultSetMap.beforeFirst();
			// Adiciona os primeiros níveis
			if(resultSetMap.next()){
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("NM_GRUPO", "ESTOQUE");
				register.put("NR_NIVEL", 3);
				register.put("NR_CATEGORIA", "7.1.2");
				resultSetMap.getLines().add(0, register);

				register = new HashMap<String, Object>();
				register.put("NM_GRUPO", "ESTOQUE ATUAL");
				register.put("NR_NIVEL", 4);
				register.put("NR_CATEGORIA", "7.1.2.001");
				resultSetMap.getLines().add(1, register);
			}
			
			resultSetMap.beforeFirst();
			return resultSetMap;
		} catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new ResultSetMap();
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
