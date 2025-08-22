package com.tivic.manager.adm;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.alm.DocumentoEntrada;
import com.tivic.manager.alm.DocumentoEntradaDAO;
import com.tivic.manager.alm.DocumentoEntradaItem;
import com.tivic.manager.alm.DocumentoEntradaItemDAO;
import com.tivic.manager.alm.DocumentoEntradaItemServices;
import com.tivic.manager.alm.DocumentoEntradaServices;
import com.tivic.manager.alm.EntradaLocalItemDAO;
import com.tivic.manager.alm.EntradaLocalItemServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ProdutoServico;
import com.tivic.manager.grl.ProdutoServicoDAO;
import com.tivic.manager.grl.ProdutoServicoEmpresa;
import com.tivic.manager.grl.ProdutoServicoEmpresaDAO;

public class CompraEntradaItemServices {

	public static Result saveEmMassa(ArrayList<CompraEntradaItem> compraEntradaItem){
		Connection connect = null;
		int retorno = 0;
		
		try {
			connect = Conexao.conectar();
			connect.setAutoCommit(false);
			
			for (CompraEntradaItem item : compraEntradaItem){
				retorno = save(item, 0, 0, connect).getCode();

				if(retorno<=0) {
					Conexao.rollback(connect);
					return new Result(retorno, "Erro ao salvar");
				}
				
				
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "COMPRAENTRADAITEM", compraEntradaItem);
		}
		catch(Exception e){
			e.printStackTrace();
			Conexao.rollback(connect);
			return new Result(-1, e.getMessage());
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	public static Result save(CompraEntradaItem compraEntradaItem, int cdLocalArmazenamento, int cdUnidadeMedida){
		return save(compraEntradaItem, cdLocalArmazenamento, cdUnidadeMedida, null);
	}
	
	public static Result save(CompraEntradaItem compraEntradaItem, int cdLocalArmazenamento, int cdUnidadeMedida, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(compraEntradaItem==null)
				return new Result(-1, "Erro ao salvar. CompraEntradaItem é nulo");

			ResultSetMap rsmComprasItens = new ResultSetMap(connect.prepareStatement("SELECT * FROM adm_compra_entrada_item WHERE cd_ordem_compra = " + compraEntradaItem.getCdOrdemCompra() + " AND cd_empresa = " + compraEntradaItem.getCdEmpresa() + " AND cd_produto_servico = " + compraEntradaItem.getCdProdutoServico()).executeQuery());
			float qtTotalRecebida = 0;
			while(rsmComprasItens.next()){
				if(rsmComprasItens.getInt("cd_item") != compraEntradaItem.getCdItem() || rsmComprasItens.getInt("cd_documento_entrada") != compraEntradaItem.getCdDocumentoEntrada())
					qtTotalRecebida += rsmComprasItens.getFloat("qt_recebida");
			}
			qtTotalRecebida += compraEntradaItem.getQtRecebida();
			
			OrdemCompraItem ordemCompraItem = OrdemCompraItemDAO.get(compraEntradaItem.getCdOrdemCompra(), compraEntradaItem.getCdEmpresa(), compraEntradaItem.getCdProdutoServico(), connect);
			
			if(ordemCompraItem!=null && (qtTotalRecebida > ordemCompraItem.getQtComprada())){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Quantidade recebida ultrapassa a comprada no contrato");
			}
			
			DocumentoEntrada DocumentoEntrada = DocumentoEntradaDAO.get(compraEntradaItem.getCdDocumentoEntrada(), connect);
			DocumentoEntradaItem documentoEntradaItem = DocumentoEntradaItemDAO.get(compraEntradaItem.getCdDocumentoEntrada(), compraEntradaItem.getCdProdutoServico(), compraEntradaItem.getCdEmpresa(), compraEntradaItem.getCdItem(), connect);
			if(documentoEntradaItem == null){
				ProdutoServicoEmpresa produto = ProdutoServicoEmpresaDAO.get(DocumentoEntrada.getCdEmpresa(), compraEntradaItem.getCdProdutoServico(), connect);
				documentoEntradaItem = new DocumentoEntradaItem(compraEntradaItem.getCdDocumentoEntrada(), compraEntradaItem.getCdProdutoServico(), compraEntradaItem.getCdEmpresa(), compraEntradaItem.getQtRecebida(), 
						0/*vlUnitario*/, 0/*vlAcrescimo*/, 0/*vlDesconto*/, produto.getCdUnidadeMedida(), null/*dtEntregaPrevista*/, 0, 0, compraEntradaItem.getCdItem(), 0, 0, 0);
			}
			else{
				documentoEntradaItem.setQtEntrada(compraEntradaItem.getQtRecebida());
				documentoEntradaItem.setCdUnidadeMedida(cdUnidadeMedida);
			}
			Result resultado = null;
			
			cdLocalArmazenamento = DocumentoEntrada.getTpEntrada() == DocumentoEntradaServices.ENT_ENTRADA_DIRETA ? 0 : cdLocalArmazenamento;
			
			if(documentoEntradaItem.getCdItem() ==0)
				resultado = DocumentoEntradaItemServices.insert(documentoEntradaItem, cdLocalArmazenamento, connect);
			else
				resultado = DocumentoEntradaItemServices.update(documentoEntradaItem, cdLocalArmazenamento, false, connect);
			
			if(resultado.getCode() <= 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(resultado.getCode(), resultado.getMessage());
			}
			
			compraEntradaItem.setCdItem(documentoEntradaItem.getCdItem());
			
			int retorno;
			if(CompraEntradaItemDAO.get(compraEntradaItem.getCdEmpresa(), compraEntradaItem.getCdOrdemCompra(), compraEntradaItem.getCdDocumentoEntrada(), compraEntradaItem.getCdProdutoServico(), compraEntradaItem.getCdItem())==null){
				retorno = CompraEntradaItemDAO.insert(compraEntradaItem, connect);
				compraEntradaItem.setCdEmpresa(retorno);
			}
			else {
				retorno = CompraEntradaItemDAO.update(compraEntradaItem, connect);
			}

			
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "COMPRAENTRADAITEM", compraEntradaItem);
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
	public static Result remove(int cdEmpresa, int cdOrdemCompra, int cdDocumentoEntrada, int cdProdutoServico, int cdItem, int cdLocalArmazenamento){
		return remove(cdEmpresa, cdOrdemCompra, cdDocumentoEntrada, cdProdutoServico, cdItem, cdLocalArmazenamento, false, null);
	}
	public static Result remove(int cdEmpresa, int cdOrdemCompra, int cdDocumentoEntrada, int cdProdutoServico, int cdItem, int cdLocalArmazenamento, boolean cascade){
		return remove(cdEmpresa, cdOrdemCompra, cdDocumentoEntrada, cdProdutoServico, cdItem, cdLocalArmazenamento, cascade, null);
	}
	public static Result remove(int cdEmpresa, int cdOrdemCompra, int cdDocumentoEntrada, int cdProdutoServico, int cdItem, int cdLocalArmazenamento, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			if(cascade){
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_produto_servico", "" + cdProdutoServico, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_documento_entrada", "" + cdDocumentoEntrada, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_empresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_local_armazenamento", "" + cdLocalArmazenamento, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_item", "" + cdItem, ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmLocaisArmazenamento = EntradaLocalItemDAO.find(criterios, connect);
				while(rsmLocaisArmazenamento.next()){
					Result resultado = new Result(EntradaLocalItemServices.delete(cdProdutoServico, cdDocumentoEntrada, cdEmpresa, cdLocalArmazenamento, rsmLocaisArmazenamento.getInt("cd_entrada_local_item"), cdItem, connect));
					if(resultado.getCode() <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao deletar entrada local armazenamento");
					}
				}
				Result resultado = DocumentoEntradaItemServices.delete(cdDocumentoEntrada, cdProdutoServico, cdEmpresa, cdItem, connect);
				retorno = resultado.getCode();
			}
			if(!cascade || retorno>0)
				retorno = CompraEntradaItemDAO.delete(cdEmpresa, cdOrdemCompra, cdDocumentoEntrada, cdProdutoServico, cdItem, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_compra_entrada_item");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CompraEntradaItemServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CompraEntradaItemServices.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_compra_entrada_item", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
