package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class PedidoVendaDAO{

	public static int insert(PedidoVenda objeto) {
		return insert(objeto, null);
	}

	public static int insert(PedidoVenda objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_pedido_venda", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPedidoVenda(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_pedido_venda (cd_pedido_venda,"+
			                                  "cd_cliente,"+
			                                  "dt_pedido,"+
			                                  "dt_limite_entrega,"+
			                                  "id_pedido_venda,"+
			                                  "vl_acrescimo,"+
			                                  "vl_desconto,"+
			                                  "tp_pedido_venda,"+
			                                  "st_pedido_venda,"+
			                                  "lg_web,"+
			                                  "txt_observacao,"+
			                                  "cd_endereco_entrega,"+
			                                  "cd_endereco_cobranca,"+
			                                  "cd_empresa,"+
			                                  "cd_vendedor,"+
			                                  "cd_tipo_operacao,"+
			                                  "nr_pedido_venda,"+
			                                  "cd_plano_pagamento,"+
			                                  "dt_autorizacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdCliente()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCliente());
			if(objeto.getDtPedido()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtPedido().getTimeInMillis()));
			if(objeto.getDtLimiteEntrega()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtLimiteEntrega().getTimeInMillis()));
			pstmt.setString(5,objeto.getIdPedidoVenda());
			pstmt.setFloat(6,objeto.getVlAcrescimo());
			pstmt.setFloat(7,objeto.getVlDesconto());
			pstmt.setInt(8,objeto.getTpPedidoVenda());
			pstmt.setInt(9,objeto.getStPedidoVenda());
			pstmt.setInt(10,objeto.getLgWeb());
			pstmt.setString(11,objeto.getTxtObservacao());
			if(objeto.getCdEnderecoEntrega()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdEnderecoEntrega());
			if(objeto.getCdEnderecoCobranca()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdEnderecoCobranca());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdEmpresa());
			if(objeto.getCdVendedor()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdVendedor());
			if(objeto.getCdTipoOperacao()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdTipoOperacao());
			pstmt.setString(17,objeto.getNrPedidoVenda());
			if(objeto.getCdPlanoPagamento()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdPlanoPagamento());
			if(objeto.getDtAutorizacao()==null)
				pstmt.setNull(19, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(19,new Timestamp(objeto.getDtAutorizacao().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PedidoVenda objeto) {
		return update(objeto, 0, null);
	}

	public static int update(PedidoVenda objeto, int cdPedidoVendaOld) {
		return update(objeto, cdPedidoVendaOld, null);
	}

	public static int update(PedidoVenda objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(PedidoVenda objeto, int cdPedidoVendaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_pedido_venda SET cd_pedido_venda=?,"+
												      		   "cd_cliente=?,"+
												      		   "dt_pedido=?,"+
												      		   "dt_limite_entrega=?,"+
												      		   "id_pedido_venda=?,"+
												      		   "vl_acrescimo=?,"+
												      		   "vl_desconto=?,"+
												      		   "tp_pedido_venda=?,"+
												      		   "st_pedido_venda=?,"+
												      		   "lg_web=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_endereco_entrega=?,"+
												      		   "cd_endereco_cobranca=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_vendedor=?,"+
												      		   "cd_tipo_operacao=?,"+
												      		   "nr_pedido_venda=?,"+
												      		   "cd_plano_pagamento=?,"+
												      		   "dt_autorizacao=? WHERE cd_pedido_venda=?");
			pstmt.setInt(1,objeto.getCdPedidoVenda());
			if(objeto.getCdCliente()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCliente());
			if(objeto.getDtPedido()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtPedido().getTimeInMillis()));
			if(objeto.getDtLimiteEntrega()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtLimiteEntrega().getTimeInMillis()));
			pstmt.setString(5,objeto.getIdPedidoVenda());
			pstmt.setFloat(6,objeto.getVlAcrescimo());
			pstmt.setFloat(7,objeto.getVlDesconto());
			pstmt.setInt(8,objeto.getTpPedidoVenda());
			pstmt.setInt(9,objeto.getStPedidoVenda());
			pstmt.setInt(10,objeto.getLgWeb());
			pstmt.setString(11,objeto.getTxtObservacao());
			if(objeto.getCdEnderecoEntrega()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdEnderecoEntrega());
			if(objeto.getCdEnderecoCobranca()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdEnderecoCobranca());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdEmpresa());
			if(objeto.getCdVendedor()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdVendedor());
			if(objeto.getCdTipoOperacao()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdTipoOperacao());
			pstmt.setString(17,objeto.getNrPedidoVenda());
			if(objeto.getCdPlanoPagamento()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdPlanoPagamento());
			if(objeto.getDtAutorizacao()==null)
				pstmt.setNull(19, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(19,new Timestamp(objeto.getDtAutorizacao().getTimeInMillis()));
			pstmt.setInt(20, cdPedidoVendaOld!=0 ? cdPedidoVendaOld : objeto.getCdPedidoVenda());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPedidoVenda) {
		return delete(cdPedidoVenda, null);
	}

	public static int delete(int cdPedidoVenda, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_pedido_venda WHERE cd_pedido_venda=?");
			pstmt.setInt(1, cdPedidoVenda);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PedidoVenda get(int cdPedidoVenda) {
		return get(cdPedidoVenda, null);
	}

	public static PedidoVenda get(int cdPedidoVenda, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_pedido_venda WHERE cd_pedido_venda=?");
			pstmt.setInt(1, cdPedidoVenda);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PedidoVenda(rs.getInt("cd_pedido_venda"),
						rs.getInt("cd_cliente"),
						(rs.getTimestamp("dt_pedido")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_pedido").getTime()),
						(rs.getTimestamp("dt_limite_entrega")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_limite_entrega").getTime()),
						rs.getString("id_pedido_venda"),
						rs.getFloat("vl_acrescimo"),
						rs.getFloat("vl_desconto"),
						rs.getInt("tp_pedido_venda"),
						rs.getInt("st_pedido_venda"),
						rs.getInt("lg_web"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_endereco_entrega"),
						rs.getInt("cd_endereco_cobranca"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_vendedor"),
						rs.getInt("cd_tipo_operacao"),
						rs.getString("nr_pedido_venda"),
						rs.getInt("cd_plano_pagamento"),
						(rs.getTimestamp("dt_autorizacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_autorizacao").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_pedido_venda");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoVendaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_pedido_venda", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
