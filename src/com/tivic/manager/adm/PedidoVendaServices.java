package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.manager.alm.DocumentoSaida;
import com.tivic.manager.alm.DocumentoSaidaDAO;
import com.tivic.manager.alm.DocumentoSaidaItem;
import com.tivic.manager.alm.DocumentoSaidaItemServices;
import com.tivic.manager.alm.DocumentoSaidaServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.NumeracaoDocumentoServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoDAO;
import com.tivic.manager.grl.UnidadeConversaoServices;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class PedidoVendaServices {

	public static String[] situacaoPedido = {"Aberto", "Fechado", "Cancelado"};

	public static final int ST_ABERTO = 0;
	public static final int ST_FECHADO = 1;
	public static final int ST_CANCELADO = 2;

	public static String[] tipoPedido = {"Pedido de Venda", "Orçamento"};

	public static final int TP_PEDIDO_VENDA = 0;
	public static final int TP_ORCAMENTO = 1;

	public static int getModeloDocPadraoOfPedidoVenda(int cdEmpresa){
		return getModeloDocPadraoOfPedidoVenda(cdEmpresa, null);
	}

	public static int getModeloDocPadraoOfPedidoVenda(int cdEmpresa, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			return Integer.parseInt(ParametroServices.getValorOfParametro("CD_MODELO_DOC_PEDIDO_VENDA", "0", cdEmpresa, connection));
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaServices.getModeloDocPadraoOfPedidoVenda: " +  e);
			return 0;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int save(PedidoVenda pedido, ArrayList<PedidoVendaItem> itens){
		return save(pedido, null, null, itens, null).getCode();
	}

	public static int save(PedidoVenda pedido, PessoaEndereco enderecoEntrega, PessoaEndereco enderecoCobranca,
			ArrayList<PedidoVendaItem> itens){
		return save(pedido, enderecoEntrega, enderecoCobranca, itens, null).getCode();
	}

	public static Result save(PedidoVenda pedido, PessoaEndereco enderecoEntrega, PessoaEndereco enderecoCobranca, ArrayList<PedidoVendaItem> itens, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());
			HashMap<String, Object> hash = new HashMap<String, Object>();
			hash.put("pedido", pedido);

			if (enderecoEntrega!=null && pedido.getCdCliente()>0) {
				int cdEndereco = 0;
				enderecoEntrega.setCdPessoa(pedido.getCdCliente());
				if ((cdEndereco = PessoaEnderecoDAO.insert(enderecoEntrega, connect)) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao tentar salvar o endereço de entrega!");
				}
				pedido.setCdEnderecoEntrega(cdEndereco);
			}

			if (enderecoCobranca!=null && pedido.getCdCliente()>0) {
				int cdEndereco = 0;
				enderecoCobranca.setCdPessoa(pedido.getCdCliente());
				if ((cdEndereco = PessoaEnderecoDAO.insert(enderecoCobranca, connect)) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao tentar salvar o endereço de cobrança!");
				}
				pedido.setCdEnderecoCobranca(cdEndereco);
			}

			int cdPedidoVenda = 0;
			if(pedido.getCdPedidoVenda()==0){
				cdPedidoVenda = PedidoVendaDAO.insert(pedido, connect);
				if (cdPedidoVenda<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao tentar salvar dados do pedido!");
				}
			}
			else{
				if (PedidoVendaDAO.update(pedido, connect)<=0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao tentar atualizar dados do pedido!");
				}
			}

			//salvando itens
			for(int i=0; itens!=null && i<itens.size(); i++){
				int retorno = 0;
				PedidoVendaItem item = (PedidoVendaItem)itens.get(i);
				if(PedidoVendaItemDAO.get(item.getCdPedidoVenda(), item.getCdEmpresa(), item.getCdProdutoServico(), connect)==null){
					item.setCdPedidoVenda(pedido.getCdPedidoVenda());
					retorno = PedidoVendaItemDAO.insert(item, connect);
				}
				else
					retorno = PedidoVendaItemDAO.update(item, connect);
				if (retorno <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao tentar salvar dados dos itens do pedido!");
				}
			}

			if (isConnectionNull)
				connect.commit();

			return new Result(cdPedidoVenda);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao tentar salvar dados do pedido!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAsResultSet(int cdPedidoVenda){
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.cd_pedido_venda", Integer.toString(cdPedidoVenda), ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios);
	}

	public static ResultSetMap getAsResultSetForPrint(int cdPedidoVenda){
		return getAsResultSetForPrint(cdPedidoVenda, null);
	}

	public static ResultSetMap getAsResultSetForPrint(int cdPedidoVenda, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, " +
					"B.nm_pessoa AS nm_cliente, " +
					"C.nm_pessoa AS nm_vendedor, " +
					"E.nr_cpf, " +
					"D.nr_cnpj, " +
					"D.nr_inscricao_estadual, " +
					"E.nr_rg, " +
					"E.sg_orgao_rg, " +
					"F.sg_estado AS sg_estado_rg, " +
					"H.nm_cidade, " +
					"I.sg_estado, " +
					"J.nm_tipo_logradouro, " +
					"G.nm_logradouro, " +
					"G.nm_bairro, G.nr_endereco, " +
					"G.nm_complemento, " +
					"L.nm_plano_pagamento, " +
					"(SELECT SUM(E.qt_solicitada * E.vl_unitario + E.vl_acrescimo - E.vl_desconto) " +
					" FROM adm_pedido_venda_item E " +
					" WHERE E.cd_pedido_venda = A.cd_pedido_venda) AS vl_total_saida, " +
					"(SELECT SUM(F.vl_desconto) " +
					" FROM adm_pedido_venda_item F " +
					" WHERE F.cd_pedido_venda = A.cd_pedido_venda) AS vl_total_descontos, " +
					"(SELECT SUM(G.vl_acrescimo) " +
					" FROM adm_pedido_venda_item G " +
					" WHERE G.cd_pedido_venda = A.cd_pedido_venda) AS vl_total_acrescimos " +
					"FROM adm_pedido_venda A " +
					"LEFT OUTER JOIN grl_pessoa B ON (A.cd_cliente = B.cd_pessoa) " +
					"LEFT OUTER JOIN grl_pessoa_juridica D ON (B.cd_pessoa = D.cd_pessoa) " +
					"LEFT OUTER JOIN grl_pessoa_fisica E ON (B.cd_pessoa = E.cd_pessoa) " +
					"LEFT OUTER JOIN grl_estado F ON (E.cd_estado_rg = F.cd_estado) " +
					"LEFT OUTER JOIN grl_pessoa C ON (A.cd_vendedor = C.cd_pessoa) " +
					"LEFT OUTER JOIN grl_pessoa_endereco G ON (A.cd_cliente = G.cd_pessoa AND A.cd_endereco_entrega = G.cd_endereco) " +
					"LEFT OUTER JOIN grl_cidade H ON (G.cd_cidade = H.cd_cidade) " +
					"LEFT OUTER JOIN grl_estado I ON (H.cd_estado = I.cd_estado) " +
					"LEFT OUTER JOIN grl_tipo_logradouro J ON (G.cd_tipo_logradouro = J.cd_tipo_logradouro) " +
					"LEFT OUTER JOIN adm_plano_pagamento L ON (A.cd_plano_pagamento = L.cd_plano_pagamento) " +
					"WHERE A.cd_pedido_venda = ?");
			pstmt.setInt(1, cdPedidoVenda);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while (rsm.next())
				rsm.getRegister().put("SG_TP_PEDIDO_VENDA", rsm.getInt("tp_pedido_venda")==TP_ORCAMENTO ? "ORCAM" : "PEDIDO");
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaServices.getAsResultSetForPrint: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int delete(int cdPedidoVenda) {
		return delete(cdPedidoVenda, false, null);
	}

	public static int delete(int cdPedidoVenda, boolean forceDelete) {
		return delete(cdPedidoVenda, forceDelete, null);
	}

	public static int delete(int cdPedidoVenda, boolean forceDelete, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			int retorno;
			PedidoVenda pedido = forceDelete ? null : PedidoVendaDAO.get(cdPedidoVenda, connect);

			if((pedido==null && forceDelete) || pedido.getStPedidoVenda()==ST_ABERTO){
				PreparedStatement pstmt = connect.prepareStatement("SELECT DISTINCT cd_documento_saida " +
						"FROM adm_venda_saida_item " +
						"WHERE cd_pedido_venda = ?");
				pstmt.setInt(1, cdPedidoVenda);
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

				pstmt = connect.prepareStatement("DELETE " +
						"FROM adm_venda_saida_item " +
						"WHERE cd_pedido_venda = ?");
				pstmt.setInt(1, cdPedidoVenda);
				pstmt.execute();

				while (rsm.next()) {
					if (forceDelete) {
						pstmt = connect.prepareStatement("SELECT cd_conta_receber " +
								"FROM adm_conta_receber " +
								"WHERE cd_documento_saida = ?");
						pstmt.setInt(1, rsm.getInt("cd_documento_saida"));
						ResultSet rs = pstmt.executeQuery();
						while (rs.next()) {
							if (ContaReceberServices.delete(rs.getInt("cd_conta_receber"), true, true, connect) <= 0) {
								if (isConnectionNull)
									Conexao.rollback(connect);
								return -1;
							}
						}
					}

					if (DocumentoSaidaServices.delete(rsm.getInt("cd_documento_saida"), connect).getCode() <= 0) {
						if (isConnectionNull)
							Conexao.rollback(connect);
						return -1;
					}
				}

				pstmt = connect.prepareStatement("DELETE " +
						"FROM adm_pedido_venda_item " +
						"WHERE cd_pedido_venda=?");
				pstmt.setInt(1, cdPedidoVenda);
				pstmt.executeUpdate();

				retorno = PedidoVendaDAO.delete(cdPedidoVenda, connect);
			}
			else
				retorno = cancelarPedido(pedido.getCdPedidoVenda(), connect);

			if(retorno<0 && isConnectionNull) {
				Conexao.rollback(connect);
				return retorno;
			}

			if (isConnectionNull)
				connect.commit();

			return retorno;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaServices.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static String getProximoNrPedido(int cdEmpresa) {
		return getProximoNrPedido(cdEmpresa, null);
	}

	public static String getProximoNrPedido(int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			int nrAno = new GregorianCalendar().get(Calendar.YEAR);
			int nrDocumento = 0;

			if ((nrDocumento = NumeracaoDocumentoServices.getProximoNumero("PEDIDO_VENDA", nrAno, cdEmpresa, connection)) <= 0)
				return null;

			return new DecimalFormat("000000").format(nrDocumento) + "/" + nrAno;
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connection);
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT A.*, B.nm_pessoa AS nm_cliente, C.nm_pessoa AS nm_vendedor, " +
				"(SELECT SUM(E.qt_solicitada * E.vl_unitario + E.vl_acrescimo - E.vl_desconto) " +
				" FROM adm_pedido_venda_item E " +
				" WHERE E.cd_pedido_venda = A.cd_pedido_venda) AS vl_total_saida, " +
				"(SELECT SUM(F.vl_desconto) " +
				" FROM adm_pedido_venda_item F " +
				" WHERE F.cd_pedido_venda = A.cd_pedido_venda) AS vl_total_descontos, " +
				"(SELECT SUM(G.vl_acrescimo) " +
				" FROM adm_pedido_venda_item G " +
				" WHERE G.cd_pedido_venda = A.cd_pedido_venda) AS vl_total_acrescimos " +
				"FROM adm_pedido_venda A " +
				"LEFT OUTER JOIN grl_pessoa B ON (A.cd_cliente = B.cd_pessoa) " +
				"LEFT OUTER JOIN grl_pessoa C ON (A.cd_vendedor = C.cd_pessoa) " +
				"WHERE 1 = 1", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap getAllItens(int cdPedidoVenda) {
		return getAllItens(cdPedidoVenda, null);
	}

	public static ResultSetMap getAllItens(int cdPedidoVenda, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;

			PreparedStatement pstmt = connect.prepareStatement(
					 "SELECT A.*, A.vl_unitario as vl_preco_final, A.vl_desconto_promocao as vl_total_desconto_promocional, C.nm_produto_servico, C.txt_produto_servico, C.tp_produto_servico, C.id_produto_servico, " +
					 "       C.id_produto_servico, C.sg_produto_servico, B.st_produto_empresa, B.id_reduzido, " +
					 "       C.cd_classificacao_fiscal, " +
					 "		 F.nm_unidade_medida AS nm_unidade_medida_padrao, F.sg_unidade_medida AS sg_unidade_medida_padrao, " +
					 "		 F.cd_unidade_medida AS cd_unidade_medida_padrao, " +
					 "       E.sg_unidade_medida, E.nm_unidade_medida " +
					 "FROM adm_pedido_venda_item A " +
					 "JOIN grl_produto_servico_empresa B ON (A.cd_produto_servico = B.cd_produto_servico AND A.cd_empresa = B.cd_empresa) " +
					 "JOIN grl_produto_servico C ON (B.cd_produto_servico = C.cd_produto_servico) " +
					 "LEFT OUTER JOIN grl_produto D ON (C.cd_produto_servico = D.cd_produto_servico) " +
					 "LEFT OUTER JOIN grl_unidade_medida E ON (A.cd_unidade_medida = E.cd_unidade_medida) " +
					 "LEFT OUTER JOIN grl_unidade_medida F ON (B.cd_unidade_medida = F.cd_unidade_medida) "+
					 "WHERE A.cd_pedido_venda = ?");
			pstmt.setInt(1, cdPedidoVenda);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());

			while(rsm.next()){
				ResultSetMap rsmUnidades = UnidadeConversaoServices.getAllUnidadeDestino(rsm.getInt("cd_unidade_medida"), connect);
				if (rsm.getInt("cd_unidade_medida_padrao")>0 && rsmUnidades!=null & !rsmUnidades.locate("cd_unidade_medida_padrao", rsm.getInt("cd_unidade_medida_padrao"))) {
					HashMap<String, Object> reg = new HashMap<String, Object>();
					reg.put("CD_UNIDADE_MEDIDA", rsm.getInt("cd_unidade_medida_padrao"));
					reg.put("NM_UNIDADE_MEDIDA", rsm.getString("nm_unidade_medida_padrao"));
					reg.put("SG_UNIDADE_MEDIDA", rsm.getString("sg_unidade_medida_padrao"));
					rsmUnidades.addRegister(reg);
					rsmUnidades.beforeFirst();
				}
				rsm.getRegister().put("subResultSetMap", rsmUnidades);
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaServices.getAllItens: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result fecharPedido(int cdPedidoVenda) {
		return fecharPedido(cdPedidoVenda, false, null);
	}

	public static Result fecharPedido(int cdPedidoVenda, boolean confirmEntrega) {
		return fecharPedido(cdPedidoVenda, confirmEntrega, null);
	}

	public static Result fecharPedido(int cdPedidoVenda, boolean confirmEntrega, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			PedidoVenda pedido = PedidoVendaDAO.get(cdPedidoVenda, connect);
			pedido.setStPedidoVenda(PedidoVendaServices.ST_FECHADO);

			float vlTotalDesconto = 0;
			float vlTotalAcrescimo = 0;
			float vlTotalPedido = 0;

			ResultSetMap rsmItens = PedidoVendaServices.getAllItens(cdPedidoVenda, connect);
			while(rsmItens.next()){
				vlTotalDesconto += rsmItens.getFloat("vl_desconto");
				vlTotalAcrescimo += rsmItens.getFloat("vl_acrescimo");
				vlTotalPedido += rsmItens.getFloat("vl_unitario")*rsmItens.getFloat("qt_solicitada");
			}

			vlTotalDesconto  += pedido.getVlDesconto();
			vlTotalAcrescimo += pedido.getVlAcrescimo();
			vlTotalPedido    += vlTotalAcrescimo-vlTotalDesconto;

			DocumentoSaida saida = new DocumentoSaida(0 /*cdDocumentoSaida*/,
					0 /*cdTransportadora*/,
					pedido.getCdEmpresa(),
					pedido.getCdCliente(),
					new GregorianCalendar() /*dtDocumentoSaida*/,
					DocumentoSaidaServices.ST_EM_CONFERENCIA /*stDocumentoSaida*/,
					DocumentoSaidaServices.getProximoNrDocumento(pedido.getCdEmpresa()),
					DocumentoSaidaServices.TP_NOTA_FISCAL_VENDA /*tpDocumentoSaida*/,
					DocumentoSaidaServices.SAI_VENDA /*tpSaida*/,
					"" /*nrConhecimento*/,
					vlTotalDesconto,
					vlTotalAcrescimo,
					new GregorianCalendar() /*dtEmissao*/,
					DocumentoSaidaServices.FRT_CIF /*tpFrete*/,
					"" /*txtMensagem*/,
					"" /*txtObservacao*/,
					"" /*nrPlacaVeculo*/,
					"" /*sgPlacaVeiculo*/,
					"" /*nrVolumes*/,
					null /*dtSaidaTransportadora*/,
					"" /*dsViaTransporte*/,
					ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_PEDIDO_VENDA", 0) /*cdNaturezaOperacao*/,
					"" /*txtCorpoNotaFiscal*/,
					0 /*vlPesoLiquido*/,
					0 /*vlPesoBruto*/,
					"" /*dsEspecieVolumes*/,
					"" /*dsMarcaVolumes*/,
					0 /*qtVolumes*/,
					DocumentoSaidaServices.MOV_ESTOQUE_NAO_CONSIGNADO /*tpMovimentoEstoque*/,
					pedido.getCdVendedor(),
					0 /*cdMoeda*/,
					0 /*cdReferenciaEcf*/,
					0 /*cdSolicitacaoMaterial*/,
					pedido.getCdTipoOperacao(),
					vlTotalPedido /*vlTotalDocumento*/,
					0 /*cdContrato*/,
					0 /*vlFrete*/,
					0 /*vlSeguro*/,
					0 /*cdDigitador*/,
					0 /*cdDocumento*/,
					0 /*cdConta*/,
					0/*cdTurno*/,
					vlTotalPedido, 1 /*nrSerie*/);
			int cdDocumentoSaida = DocumentoSaidaDAO.insert(saida, connect);
			if (cdDocumentoSaida<=0)
				return new Result(cdDocumentoSaida, "Erro ao tentar salvar documento de saída no fechamento do pedido!");

			saida.setCdDocumentoSaida(cdDocumentoSaida);
			rsmItens.beforeFirst();
			while(rsmItens.next()){
				Result resultInsertItem = DocumentoSaidaItemServices.insert(new DocumentoSaidaItem(saida.getCdDocumentoSaida(),
														rsmItens.getInt("cd_produto_servico"),
														rsmItens.getInt("cd_empresa"),
														rsmItens.getFloat("qt_solicitada"),
														rsmItens.getFloat("vl_unitario"),
														rsmItens.getFloat("vl_acrescimo"),
														rsmItens.getFloat("vl_desconto"),
														rsmItens.getGregorianCalendar("dt_entrega_prevista"),
														rsmItens.getInt("cd_unidade_medida"),
														rsmItens.getInt("cd_tabela_preco"),
														0 /*cdItem*/,
														0/*cdBico*/),
														0 /*cdLocal*/,
														0 /*cdLocalDestino*/,
														true /*registerTributacao*/, connect);
				if (resultInsertItem.getCode() <= 0) {
					return resultInsertItem;
				}

				int cdItem    = resultInsertItem.getCode();
				int cdRetorno = VendaSaidaItemDAO.insert(new VendaSaidaItem(pedido.getCdPedidoVenda() /*cdPedidoVenda*/,
															rsmItens.getInt("cd_empresa") /*cdEmpresa*/,
															rsmItens.getInt("cd_produto_servico") /*cdProdutoServico*/,
															saida.getCdDocumentoSaida() /*cdDocumentoSaida*/,
															rsmItens.getFloat("qt_solicitada") /*qtSaida*/,
															cdItem), connect);
				if (cdRetorno<=0)
					return new Result(cdRetorno, "Erro ao salvar relação entre pedido e saída! [ERRO: "+cdRetorno+"]");
			}

			int cdRetorno = PedidoVendaDAO.update(pedido, connect);
			if (cdRetorno<=0) {
				return new Result(cdRetorno, "Erro ao tentar atualizar dados do pedido de venda!");
			}

			if (confirmEntrega) {
				rsmItens.beforeFirst();
				while (rsmItens.next()) {
					cdRetorno = PedidoVendaItemServices.registarEntrega(cdPedidoVenda, pedido.getCdEmpresa(), rsmItens.getInt("cd_produto_servico"),
																		rsmItens.getFloat("qt_solicitada"), new GregorianCalendar(), connect);
					if (cdRetorno<=0) {
						return new Result(cdRetorno, "Erro ao tentar registrar entrega!");
					}
				}
			}

			if (isConnectionNull)
				connect.commit();
			Result result = new Result(cdRetorno);
			result.addObject("docSaida", saida);

			return result;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao tentar fechar pedido!", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int cancelarPedido(int cdPedidoVenda) {
		return cancelarPedido(cdPedidoVenda, null);
	}

	public static int cancelarPedido(int cdPedidoVenda, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;

			PedidoVenda pedido = PedidoVendaDAO.get(cdPedidoVenda, connect);
			pedido.setStPedidoVenda(PedidoVendaServices.ST_CANCELADO);
			return PedidoVendaDAO.update(pedido, connect);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaServices.cancelarPedido: " +  e);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getDocumentosSaida(ArrayList<ItemComparator> criterios, int cdPedidoVenda) {
		return getDocumentosSaida(criterios, cdPedidoVenda, null);
	}

	public static ResultSetMap getDocumentosSaida(ArrayList<ItemComparator> criterios, int cdPedidoVenda, Connection connect) {
		return Search.find("SELECT A.*, B.nm_pessoa AS nm_cliente, C.nm_pessoa AS nm_transportadora, E.nr_cnpj AS nr_cnpj_transportadora, " +
						   "D.nm_natureza_operacao, D.nr_codigo_fiscal, " +
						   "(SELECT SUM(E.qt_saida * vl_unitario + vl_acrescimo - vl_desconto) FROM alm_documento_saida_item E " +
						   " WHERE E.cd_documento_saida = A.cd_documento_saida) AS vl_total_saida, " +
						   "(SELECT SUM(F.vl_desconto) " +
						   " FROM alm_documento_saida_item F " +
						   " WHERE F.cd_documento_saida = A.cd_documento_saida) AS vl_total_descontos, " +
						   "(SELECT SUM(G.vl_acrescimo) " +
						   " FROM alm_documento_saida_item G " +
						   " WHERE G.cd_documento_saida = A.cd_documento_saida) AS vl_total_acrescimos " +
						   "FROM alm_documento_saida A " +
						   "LEFT OUTER JOIN grl_pessoa B ON (A.cd_cliente = B.cd_pessoa) " +
						   "LEFT OUTER JOIN grl_pessoa C ON (A.cd_transportadora = C.cd_pessoa) " +
						   "LEFT OUTER JOIN grl_pessoa_juridica E ON (C.cd_pessoa = E.cd_pessoa) " +
						   "LEFT OUTER JOIN adm_natureza_operacao D ON (A.cd_natureza_operacao = D.cd_natureza_operacao) " +
						   "WHERE A.cd_documento_saida IN (SELECT cd_documento_saida FROM adm_venda_saida_item WHERE cd_pedido_venda = "+cdPedidoVenda+") ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
