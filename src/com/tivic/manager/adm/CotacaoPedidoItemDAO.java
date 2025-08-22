package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class CotacaoPedidoItemDAO{

	public static int insert(CotacaoPedidoItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(CotacaoPedidoItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_cotacao_pedido_item", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCotacaoPedidoItem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_cotacao_pedido_item (cd_cotacao_pedido_item,"+
			                                  "cd_fornecedor,"+
			                                  "cd_empresa,"+
			                                  "cd_pedido_compra,"+
			                                  "cd_produto_servico,"+
			                                  "cd_plano_pagamento,"+
			                                  "cd_pessoa_contato,"+
			                                  "cd_pessoa_juridica,"+
			                                  "nr_posicao,"+
			                                  "dt_cotacao,"+
			                                  "qt_dias_garantia,"+
			                                  "qt_disponivel,"+
			                                  "vl_cotacao,"+
			                                  "vl_frete,"+
			                                  "vl_tributos,"+
			                                  "vl_desconto,"+
			                                  "tp_resultado,"+
			                                  "lg_assistencia_tecnica,"+
			                                  "lg_pronta_entrega,"+
			                                  "txt_observacao,"+
			                                  "qt_dias_entrega,"+
			                                  "qt_dias_validade) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdFornecedor()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFornecedor());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdPedidoCompra()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdPedidoCompra());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdProdutoServico());
			if(objeto.getCdPlanoPagamento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdPlanoPagamento());
			if(objeto.getCdPessoaContato()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdPessoaContato());
			if(objeto.getCdPessoaJuridica()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdPessoaJuridica());
			pstmt.setInt(9,objeto.getNrPosicao());
			if(objeto.getDtCotacao()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtCotacao().getTimeInMillis()));
			pstmt.setInt(11,objeto.getQtDiasGarantia());
			pstmt.setFloat(12,objeto.getQtDisponivel());
			pstmt.setFloat(13,objeto.getVlCotacao());
			pstmt.setFloat(14,objeto.getVlFrete());
			pstmt.setFloat(15,objeto.getVlTributos());
			pstmt.setFloat(16,objeto.getVlDesconto());
			pstmt.setInt(17,objeto.getTpResultado());
			pstmt.setInt(18,objeto.getLgAssistenciaTecnica());
			pstmt.setInt(19,objeto.getLgProntaEntrega());
			pstmt.setString(20,objeto.getTxtObservacao());
			pstmt.setInt(21,objeto.getQtDiasEntrega());
			pstmt.setInt(22,objeto.getQtDiasValidade());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CotacaoPedidoItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CotacaoPedidoItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CotacaoPedidoItem objeto) {
		return update(objeto, 0, null);
	}

	public static int update(CotacaoPedidoItem objeto, int cdCotacaoPedidoItemOld) {
		return update(objeto, cdCotacaoPedidoItemOld, null);
	}

	public static int update(CotacaoPedidoItem objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(CotacaoPedidoItem objeto, int cdCotacaoPedidoItemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_cotacao_pedido_item SET cd_cotacao_pedido_item=?,"+
												      		   "cd_fornecedor=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_pedido_compra=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_plano_pagamento=?,"+
												      		   "cd_pessoa_contato=?,"+
												      		   "cd_pessoa_juridica=?,"+
												      		   "nr_posicao=?,"+
												      		   "dt_cotacao=?,"+
												      		   "qt_dias_garantia=?,"+
												      		   "qt_disponivel=?,"+
												      		   "vl_cotacao=?,"+
												      		   "vl_frete=?,"+
												      		   "vl_tributos=?,"+
												      		   "vl_desconto=?,"+
												      		   "tp_resultado=?,"+
												      		   "lg_assistencia_tecnica=?,"+
												      		   "lg_pronta_entrega=?,"+
												      		   "txt_observacao=?,"+
												      		   "qt_dias_entrega=?,"+
												      		   "qt_dias_validade=? WHERE cd_cotacao_pedido_item=?");
			pstmt.setInt(1,objeto.getCdCotacaoPedidoItem());
			if(objeto.getCdFornecedor()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdFornecedor());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdPedidoCompra()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdPedidoCompra());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdProdutoServico());
			if(objeto.getCdPlanoPagamento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdPlanoPagamento());
			if(objeto.getCdPessoaContato()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdPessoaContato());
			if(objeto.getCdPessoaJuridica()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdPessoaJuridica());
			pstmt.setInt(9,objeto.getNrPosicao());
			if(objeto.getDtCotacao()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtCotacao().getTimeInMillis()));
			pstmt.setInt(11,objeto.getQtDiasGarantia());
			pstmt.setFloat(12,objeto.getQtDisponivel());
			pstmt.setFloat(13,objeto.getVlCotacao());
			pstmt.setFloat(14,objeto.getVlFrete());
			pstmt.setFloat(15,objeto.getVlTributos());
			pstmt.setFloat(16,objeto.getVlDesconto());
			pstmt.setInt(17,objeto.getTpResultado());
			pstmt.setInt(18,objeto.getLgAssistenciaTecnica());
			pstmt.setInt(19,objeto.getLgProntaEntrega());
			pstmt.setString(20,objeto.getTxtObservacao());
			pstmt.setInt(21,objeto.getQtDiasEntrega());
			pstmt.setInt(22,objeto.getQtDiasValidade());
			pstmt.setInt(23, cdCotacaoPedidoItemOld!=0 ? cdCotacaoPedidoItemOld : objeto.getCdCotacaoPedidoItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CotacaoPedidoItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CotacaoPedidoItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCotacaoPedidoItem) {
		return delete(cdCotacaoPedidoItem, null);
	}

	public static int delete(int cdCotacaoPedidoItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_cotacao_pedido_item WHERE cd_cotacao_pedido_item=?");
			pstmt.setInt(1, cdCotacaoPedidoItem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CotacaoPedidoItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CotacaoPedidoItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CotacaoPedidoItem get(int cdCotacaoPedidoItem) {
		return get(cdCotacaoPedidoItem, null);
	}

	public static CotacaoPedidoItem get(int cdCotacaoPedidoItem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_cotacao_pedido_item WHERE cd_cotacao_pedido_item=?");
			pstmt.setInt(1, cdCotacaoPedidoItem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CotacaoPedidoItem(rs.getInt("cd_cotacao_pedido_item"),
						rs.getInt("cd_fornecedor"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_pedido_compra"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_plano_pagamento"),
						rs.getInt("cd_pessoa_contato"),
						rs.getInt("cd_pessoa_juridica"),
						rs.getInt("nr_posicao"),
						(rs.getTimestamp("dt_cotacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cotacao").getTime()),
						rs.getInt("qt_dias_garantia"),
						rs.getFloat("qt_disponivel"),
						rs.getFloat("vl_cotacao"),
						rs.getFloat("vl_frete"),
						rs.getFloat("vl_tributos"),
						rs.getFloat("vl_desconto"),
						rs.getInt("tp_resultado"),
						rs.getInt("lg_assistencia_tecnica"),
						rs.getInt("lg_pronta_entrega"),
						rs.getString("txt_observacao"),
						rs.getInt("qt_dias_entrega"),
						rs.getInt("qt_dias_validade"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CotacaoPedidoItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CotacaoPedidoItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_cotacao_pedido_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CotacaoPedidoItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CotacaoPedidoItemDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_cotacao_pedido_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
