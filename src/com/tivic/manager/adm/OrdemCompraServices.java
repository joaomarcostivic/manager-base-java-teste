package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.alm.DocumentoEntradaServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.NumeracaoDocumentoServices;
import com.tivic.manager.grl.OcorrenciaServices;
import com.tivic.manager.util.Util;

public class OrdemCompraServices {
	/* Situações (st_ordem_compra)*/
	public static final int ST_EM_ABERTO = 0;
	public static final int ST_LIBERADA = 1;
	public static final int ST_CANCELADA = 2;
	public static final int ST_FECHADA = 3;
	public static final int ST_PENDENTE = 4;

	public static final String[] situacaoOrdemCompra = {"Em aberto", "Liberada", "Cancelada", "Fechada", "Pendente"};

	public static final int ERR_DOCUMENTO_ENTRADA = -2;
	public static final int ERR_ITEM_ORDEM_COMPRA = -3;

	
	public static Result saveLiberar(OrdemCompra ordemCompra, int cdUsuario, int cdTipoOcorrencia){
		return saveLiberar(ordemCompra, cdUsuario, cdTipoOcorrencia, null);
	}
	public static Result saveLiberar(OrdemCompra ordemCompra, int cdUsuario, int cdTipoOcorrencia, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(ordemCompra.getStOrdemCompra() == ST_LIBERADA){
				return new Result(-1, "Esse contrato já está liberado");
			}
			
			if(ordemCompra.getStOrdemCompra() == ST_CANCELADA || ordemCompra.getStOrdemCompra() == ST_FECHADA){
				return new Result(-1, "Contratos cancelados ou fechados não podem ser liberados");
			}
			
			ordemCompra.setStOrdemCompra(ST_LIBERADA);
			if(OrdemCompraServices.save(ordemCompra, connect).getCode() <= 0){
				return new Result(-1, "Erro ao atualizar situação do contrato");
			}
			

			if(cdTipoOcorrencia > 0){
				String txtOcorrencia = "Liberação do contrato de número " + ordemCompra.getNrOrdemCompra();
				
				com.tivic.manager.grl.Ocorrencia ocorrencia = new com.tivic.manager.grl.Ocorrencia(0, 0, txtOcorrencia, Util.getDataAtual(), cdTipoOcorrencia, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario);
				if(OcorrenciaServices.save(ocorrencia, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia de liberação de contrato");
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Liberação realizada com sucesso");
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_ordem_compra", "" + ordemCompra.getCdOrdemCompra(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = find(criterios, connect);
			result.addObject("rsm", rsm);
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro na evasão");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result save(OrdemCompra ordemCompra){
		return save(ordemCompra, null);
	}

	public static Result save(OrdemCompra ordemCompra, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(ordemCompra==null)
				return new Result(-1, "Erro ao salvar. OrdemCompra é nulo");

			int retorno;
			if(ordemCompra.getCdOrdemCompra()==0){
				retorno = OrdemCompraDAO.insert(ordemCompra, connect);
				ordemCompra.setCdOrdemCompra(retorno);
				NumeracaoDocumentoServices.getProximoNumero("ORDEM_COMPRA", new GregorianCalendar().get(Calendar.YEAR), ordemCompra.getCdEmpresa(), connect);
			}
			else {
				retorno = OrdemCompraDAO.update(ordemCompra, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ORDEMCOMPRA", ordemCompra);
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
	
	public static String getProximoNrDocumento(int cdEmpresa) {
		return getProximoNrDocumento(cdEmpresa, null);
	}

	public static String getProximoNrDocumento(int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			int nrAno = new GregorianCalendar().get(Calendar.YEAR);
			int nrDocumento = 0;

			if ((nrDocumento = NumeracaoDocumentoServices.getProximoNumero2("ORDEM_COMPRA", nrAno, cdEmpresa, connection)) <= 0)
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
	
	public static String getProximoNrDocumentoDireta(int cdEmpresa) {
		return getProximoNrDocumentoDireta(cdEmpresa, null);
	}

	public static String getProximoNrDocumentoDireta(int cdEmpresa, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			int nrAno = new GregorianCalendar().get(Calendar.YEAR);
			int nrDocumento = 0;

			if ((nrDocumento = NumeracaoDocumentoServices.getProximoNumero2("ORDEM_COMPRA_DIRETA", nrAno, cdEmpresa, connection)) <= 0)
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

	public static int setSituacaoOrdemCompra(int cdOrdemCompra, int stOrdemCompra, int cdUsuarioAutorizacao) {
		return setSituacaoOrdemCompra(cdOrdemCompra, stOrdemCompra, cdUsuarioAutorizacao, null);
	}

	public static int setSituacaoOrdemCompra(int cdOrdemCompra, int stOrdemCompra,int cdUsuarioAutorizacao, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}
			OrdemCompra documento = OrdemCompraDAO.get(cdOrdemCompra, connection);
			int situacaoAtual = documento.getStOrdemCompra();
			if (stOrdemCompra == situacaoAtual)
				return 1;
			else {
				documento.setStOrdemCompra(stOrdemCompra);
				documento.setCdUsuarioAutorizacao(cdUsuarioAutorizacao);
				if (OrdemCompraDAO.update(documento, connection) <= 0) {
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
			System.err.println("Erro! OrdemCompraServices.setSituacaoOrdemCompra: " + e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllItens(int cdOrdemCompra) {
		return getAllItens(cdOrdemCompra, null);
	}

	public static ResultSetMap getAllItens(int cdOrdemCompra, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement(
					 "SELECT A.*, " +
					 "	B.id_reduzido, B.qt_ideal, B.qt_minima, B.qt_maxima, B.qt_dias_estoque, " +
					 "	C.nm_produto_servico, C.txt_produto_servico, C.tp_produto_servico, C.id_produto_servico, " +
					 "	C.id_produto_servico, C.sg_produto_servico, B.st_produto_empresa, B.id_reduzido, " +
					 "  C.cd_classificacao_fiscal, C.cd_fabricante, " +
					 "  E.sg_unidade_medida, E.nm_unidade_medida, " +
					 "	F.nm_pessoa AS nm_fabricante " +
					 "FROM adm_ordem_compra_item A " +
					 "	JOIN grl_produto_servico_empresa B ON (A.cd_produto_servico = B.cd_produto_servico AND A.cd_empresa = B.cd_empresa) " +
					 "	JOIN grl_produto_servico C ON (B.cd_produto_servico = C.cd_produto_servico) " +
					 "	LEFT OUTER JOIN grl_produto D ON (C.cd_produto_servico = D.cd_produto_servico) " +
					 "	LEFT OUTER JOIN grl_unidade_medida E ON (A.cd_unidade_medida = E.cd_unidade_medida) " +
					 "	LEFT OUTER JOIN grl_pessoa F ON (C.cd_fabricante = F.cd_pessoa) " +
					 "WHERE A.cd_ordem_compra = ?");
			pstmt.setInt(1, cdOrdemCompra);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraServices.getAllItens: " + e);
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
		int qtLimite = 0;
		ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite"))
				qtLimite = Integer.parseInt(criterios.get(i).getValue());
			else
				crt.add(criterios.get(i));
			
		}
		String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, 0);
		return Search.find("SELECT "+sqlLimit[0]+" A.*, B.nm_pessoa AS nm_fornecedor, C.nm_local_armazenamento AS nm_local_entrega " +
						   "FROM adm_ordem_compra A " +
						   " JOIN grl_pessoa B ON (A.cd_fornecedor = B.cd_pessoa)"+
						   " LEFT OUTER JOIN alm_local_armazenamento C ON (A.cd_local_entrega = C.cd_local_armazenamento) "+
						   "WHERE 1=1 ", "ORDER BY A.nr_ordem_compra DESC "+sqlLimit[1], crt, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getAllEntregasRealizadas(int cdOrdemCompra) {
		return getAllEntregasRealizadas(cdOrdemCompra, null);
	}

	public static ResultSetMap getAllEntregasRealizadas(int cdOrdemCompra, Connection connect) {
		ResultSetMap rsm = Search.find("SELECT A.*, B.nm_pessoa AS nm_fornecedor " +
						   "FROM alm_documento_entrada A " +
						   " JOIN grl_pessoa B ON (A.cd_fornecedor = B.cd_pessoa)"+
						   "WHERE 1=1 AND EXISTS (SELECT * FROM adm_compra_entrada_item CEI WHERE CEI.cd_ordem_compra = "+cdOrdemCompra+" AND A.cd_documento_entrada = CEI.cd_documento_entrada AND A.cd_empresa = CEI.cd_empresa)", "ORDER BY A.nr_documento_entrada DESC ", new ArrayList<ItemComparator>(), connect!=null ? connect : Conexao.conectar(), connect==null);
		
		while(rsm.next()){
			rsm.setValueToField("QT_ITENS", DocumentoEntradaServices.getAllItens(rsm.getInt("cd_documento_entrada"), connect).size());
		}
		rsm.beforeFirst();
		
		
		return rsm;
	}
	
	public static ResultSetMap getAllItensEntregues(int cdOrdemCompra) {
		return getAllItensEntregues(cdOrdemCompra, null);
	}

	public static ResultSetMap getAllItensEntregues(int cdOrdemCompra, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT P.cd_produto_servico, P.nm_produto_servico, SUM(A.qt_entrada) AS QT_ENTREGUE " +
							   "FROM alm_documento_entrada_item A " +
							   " JOIN adm_compra_entrada_item CEI ON (CEI.cd_ordem_compra = "+cdOrdemCompra+" "
							 + "										AND A.cd_documento_entrada = CEI.cd_documento_entrada "
							 + "										AND A.cd_item = CEI.cd_item "
							 + "										AND A.cd_produto_servico = CEI.cd_produto_servico "
							 + "										AND A.cd_empresa = CEI.cd_empresa)"
							 + " JOIN grl_produto_servico P ON (A.cd_produto_servico = P.cd_produto_servico)"
							 + " WHERE 1=1 GROUP BY P.cd_produto_servico, P.nm_produto_servico ORDER BY P.nm_produto_servico ").executeQuery());
			
			while(rsm.next()){
				ResultSetMap rsmOrdemCompraItem = new ResultSetMap(connect.prepareStatement("SELECT * FROM adm_ordem_compra_item WHERE cd_ordem_compra = " + cdOrdemCompra + " AND cd_produto_servico = " + rsm.getInt("cd_produto_servico")).executeQuery());
				if(rsmOrdemCompraItem.next()){
					rsm.setValueToField("QT_FALTANTE", (rsmOrdemCompraItem.getFloat("QT_COMPRADA") - rsm.getFloat("QT_ENTREGUE")));
				}
			}
			rsm.beforeFirst();
			
			
			return rsm;
		}
		catch(SQLException e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraServices.getAllItensEntregues: " + e);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraServices.getAllItensEntregues: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	
	public static Result remove(int cdOrdemCompra) {
		int ret = delete(cdOrdemCompra, null);
		return new Result(ret, (ret > 0 ? "Removido com sucesso" : "Erro ao remover"));
	}
	
	public static int delete(int cdOrdemCompra) {
		return delete(cdOrdemCompra, null);
	}

	public static int delete(int cdOrdemCompra, Connection connection){
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull) {
				connection = Conexao.conectar();
				connection.setAutoCommit(false);
			}

			OrdemCompra ordemCompra = OrdemCompraDAO.get(cdOrdemCompra, connection);
			int cdEmpresa = ordemCompra.getCdEmpresa();

			/* verifica se existem documentos de entrada relacionadas à ordem de compra */
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.cd_documento_entrada " +
					"FROM adm_compra_entrada_item A " +
					"WHERE A.cd_ordem_compra = ?");
			pstmt.setInt(1, cdOrdemCompra);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Conexao.rollback(connection);
				return ERR_DOCUMENTO_ENTRADA;
			}

			/* exclusão dos itens */
			pstmt = connection.prepareStatement("SELECT cd_produto_servico " +
					"FROM adm_ordem_compra_item A " +
					"WHERE A.cd_ordem_compra = ?");
			pstmt.setInt(1, cdOrdemCompra);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				if (OrdemCompraItemDAO.delete(cdOrdemCompra, cdEmpresa, rs.getInt("cd_produto_servico")) <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connection);
					return ERR_ITEM_ORDEM_COMPRA;
				}
			}

			if (OrdemCompraDAO.delete(cdOrdemCompra, connection) <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connection);
				return -1;
			}

			if (isConnectionNull)
				connection.commit();

			return 1;
		}
		catch(SQLException e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraServices.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connection);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getRecebimentos(int cdOrdemCompra) {
		return getRecebimentos(cdOrdemCompra, null);
	}

	public static ResultSetMap getRecebimentos(int cdOrdemCompra, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT DISTINCT A.qt_recebida, " +
											 "	B.qt_entrada, " +
											 "	C.qt_comprada, " +
											 "  D.nr_documento_entrada, D.dt_documento_entrada, D.st_documento_entrada, D.tp_documento _entrada, D.tp_entrada, " +
											 "	E.nm_produto_servico, " +
											 "	F.sg_unidade_medida, " +
											 "	G.nm_pessoa AS nm_fornecedor " +
											 "FROM adm_compra_entrada_item A  " +
											 "	JOIN alm_documento_entrada_item B " +
											 "		ON (A.cd_documento_entrada = B.cd_documento_entrada AND A.cd_produto_servico = B.cd_produto_servico AND A.cd_empresa = B.cd_empresa) " +
											 "	JOIN adm_ordem_compra_item C " +
											 "		ON (A.cd_ordem_compra = C.cd_ordem_compra AND A.cd_empresa = C.cd_empresa AND A.cd_produto_servico = C.cd_produto_servico) " +
											 "	JOIN alm_documento_entrada D " +
											 "		ON (A.cd_documento_entrada = D.cd_documento_entrada) " +
											 "	JOIN grl_produto_servico E " +
											 "		ON (A.cd_produto_servico = E.cd_produto_servico) " +
											 "	LEFT OUTER JOIN grl_unidade_medida F " +
											 "		ON (B.cd_unidade_medida = F.cd_unidade_medida) " +
											 "	LEFT OUTER JOIN grl_pessoa G " +
											 "		ON (D.cd_fornecedor = G.cd_pessoa) " +
											 "WHERE (A.cd_ordem_compra = ?) ");
			pstmt.setInt(1, cdOrdemCompra);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraServices.getRecebimentos: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findPedidoVenda(ArrayList<ItemComparator> criterios, int cdOrdemCompra) {
		return findPedidoVenda(criterios, cdOrdemCompra, null);
	}

	public static ResultSetMap findPedidoVenda(ArrayList<ItemComparator> criterios, int cdOrdemCompra, Connection connect) {
		ResultSetMap rsm = Search.find("SELECT A.*, " +
						   "	B.cd_produto_servico, B.cd_unidade_medida AS cd_unidade_medida_venda, B.qt_solicitada, B.vl_unitario, " +
						   "	B.vl_desconto, B.vl_acrescimo, B.lg_reserva_estoque, B.dt_entrega_prevista, " +
						   "	C.nm_pessoa AS nm_cliente, " +
						   "	D.nm_produto_servico, D.tp_produto_servico, " +
						   "	E.sg_unidade_medida AS sg_unidade_medida_venda, " +
						   "	F.nm_pessoa AS nm_vendedor, " +
						   "	COALESCE((SELECT SUM(N.qt_comprada) " +
						   "		FROM adm_ordem_compra_item N " +
						   "		WHERE (N.cd_pedido_venda = B.cd_pedido_venda AND N.cd_empresa = B.cd_empresa AND N.cd_produto_servico = B.cd_produto_servico)), 0) AS total_comprado " +
						   "FROM adm_pedido_venda A " +
						   "	JOIN adm_pedido_venda_item B " +
						   "		ON (A.cd_pedido_venda = B.cd_pedido_venda) " +
						   "	LEFT OUTER JOIN grl_pessoa C " +
						   "		ON (A.cd_cliente = C.cd_pessoa) " +
						   "	LEFT OUTER JOIN grl_produto_servico D " +
						   "		ON (B.cd_produto_servico = D.cd_produto_servico) " +
						   "	LEFT OUTER JOIN grl_unidade_medida E " +
						   "		ON (B.cd_unidade_medida = E.cd_unidade_medida) " +
						   "	LEFT OUTER JOIN grl_pessoa F " +
						   "		ON (A.cd_vendedor = F.cd_pessoa) " +
						   "WHERE B.cd_produto_servico NOT IN ( " +
						   "	SELECT M.cd_produto_servico " +
						   "	FROM adm_ordem_compra_item M " +
						   "    WHERE M.cd_ordem_compra = " + cdOrdemCompra + ")" +
						   "AND B.qt_solicitada > COALESCE((SELECT SUM(N.qt_comprada) " +
						   "	FROM adm_ordem_compra_item N " +
						   "	WHERE (N.cd_pedido_venda = B.cd_pedido_venda AND N.cd_empresa = B.cd_empresa AND N.cd_produto_servico = B.cd_produto_servico)), 0) ", criterios, true, connect != null ? connect : Conexao.conectar(), connect==null);
		while(rsm.next()){
			ResultSetMap subRsm = com.tivic.manager.grl.UnidadeConversaoServices.getAllUnidadeDestino(rsm.getInt("CD_UNIDADE_MEDIDA_VENDA"), connect != null ? connect : Conexao.conectar());
			rsm.setValueToField("subResultSetMap", subRsm);
		}
		return rsm;
	}

	public static int importarPedidoVenda(ArrayList<OrdemCompraItem> itens){
		return importarPedidoVenda(itens, null);
	}
	public static int importarPedidoVenda(ArrayList<OrdemCompraItem> itens, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			for(int i = 0; i < itens.size(); i++){
				OrdemCompraItem item = (OrdemCompraItem)itens.get(i);
				item.setDtPrevisaoEntrega(null);
				retorno = OrdemCompraItemDAO.insert(item, connect);
				if(retorno < 0)
					break;
			}
			if(retorno < 0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return retorno;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraServices.importarPedidoVenda: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraServices.importarPedidoVenda: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return  -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static String getUsuarioAutorizacao(int cdOrdemCompra){
		Connection connect=null;
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT nm_pessoa FROM grl_pessoa A, seg_usuario B, adm_ordem_compra C WHERE C.cd_usuario_autorizacao=B.cd_usuario AND B.cd_pessoa=A.cd_pessoa AND C.cd_ordem_compra=?");
			pstmt.setInt(1, cdOrdemCompra);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return rs.getString("nm_pessoa");

			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OrdemCompraDAO.get: " + sqlExpt);
			return null;
		}

	}


	public static Result renovar(OrdemCompra ordemCompra, int cdUsuario, int cdTipoOcorrencia){
		return renovar(ordemCompra, cdUsuario, cdTipoOcorrencia, null);
	}
	public static Result renovar(OrdemCompra ordemCompra, int cdUsuario, int cdTipoOcorrencia, Connection connect){
		boolean isConnectionNull = connect == null;
		try{
			
			if(isConnectionNull){
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(ordemCompra.getStOrdemCompra() == ST_CANCELADA || ordemCompra.getStOrdemCompra() == ST_EM_ABERTO || ordemCompra.getStOrdemCompra() == ST_PENDENTE){
				return new Result(-1, "Contratos cancelados, em aberto ou pendentes não podem ser renovados");
			}
			
			
			OrdemCompra ordemCompraNovo = new OrdemCompra(0, Util.getDataAtual(), ST_PENDENTE, null/*dtLimiteEntrega*/, null/*idOrdemCompra*/, getProximoNrDocumento(ordemCompra.getCdEmpresa()), ordemCompra.getCdFornecedor(), ordemCompra.getCdLocalEntrega(), 0/*cdMoeda*/, 0/*cdTabelaPreco*/, 0/*cdComprador*/, 0/*vlDesconto*/, 0/*vlAcrescimo*/, 0/*tpMovimentoEstoque*/, ordemCompra.getTxtObservacao(), 0/*vlTotalDocumento*/, ordemCompra.getCdEmpresa(), cdUsuario, ordemCompra.getCdOrdemCompra());
			
			Result resultado = save(ordemCompraNovo, connect);
			if(resultado.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return resultado;
			}
			

			ResultSetMap rsmItens = getAllItens(ordemCompra.getCdOrdemCompra(), connect);
			while(rsmItens.next()){
				OrdemCompraItem ordemCompraItem = new OrdemCompraItem(ordemCompraNovo.getCdOrdemCompra(), ordemCompraNovo.getCdEmpresa(), rsmItens.getInt("cd_produto_servico"), 0/*cdCotacaoPedidoItem*/, 0/*qtComprada*/, 0/*vlUnitario*/, 0/*vlDesconto*/, 0/*vlAcrescimo*/, null/*dtPrevisaoEntrega*/, 0/*cdPedidoVenda*/, rsmItens.getInt("cd_unidade_medida"));
				if(OrdemCompraItemDAO.insert(ordemCompraItem, connect) <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao inserir item em ordem de compra");
				}
			}
			
			if(cdTipoOcorrencia > 0){
				String txtOcorrencia = "Renovação do contrato de número " + ordemCompra.getNrOrdemCompra() + ", criação do contrato de número " + ordemCompraNovo.getNrOrdemCompra();
				
				com.tivic.manager.grl.Ocorrencia ocorrencia = new com.tivic.manager.grl.Ocorrencia(0, 0, txtOcorrencia, Util.getDataAtual(), cdTipoOcorrencia, OcorrenciaServices.ST_CONCLUIDO, 0, cdUsuario);
				if(OcorrenciaServices.save(ocorrencia, connect).getCode() <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao registrar ocorrencia de renovação de contrato");
				}
			}
			
			if(isConnectionNull)
				connect.commit();
			
			Result result = new Result(1, "Renovação realizada com sucesso");
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_ordem_compra", "" + ordemCompraNovo.getCdOrdemCompra(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = find(criterios, connect);
			result.addObject("rsm", rsm);
			return result;
		}
		
		catch(Exception e){
			if(isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro na evasão");
		}
		
		finally{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static String getProximoNumero(int cdEmpresa, boolean adiciona) {
		return getProximoNumero(cdEmpresa, adiciona, null);
	}

	public static String getProximoNumero(int cdEmpresa, boolean adiciona, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();

			int nrAno = new GregorianCalendar().get(Calendar.YEAR);
			int nrDocumento = 0;

			if(adiciona){
				if ((nrDocumento = NumeracaoDocumentoServices.getProximoNumero("ORDEM_COMPRA", nrAno, cdEmpresa, connection)) <= 0)
					return null;
			}
			else{
				if ((nrDocumento = NumeracaoDocumentoServices.getProximoNumero2("ORDEM_COMPRA", nrAno, cdEmpresa, connection)) <= 0)
					return null;
			}
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
	

}
