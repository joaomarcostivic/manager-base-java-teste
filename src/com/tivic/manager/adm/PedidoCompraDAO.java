package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.Util;

public class PedidoCompraDAO{

	public static int insert(PedidoCompra objeto) {
		return insert(objeto, null);
	}

	public static int insert(PedidoCompra objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_pedido_compra", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPedidoCompra(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_pedido_compra (cd_pedido_compra,"+
			                                  "cd_empresa,"+
			                                  "nm_pedido_compra,"+
			                                  "txt_pedido_compra,"+
			                                  "nr_prazo_entrega,"+
			                                  "dt_pedido_compra,"+
			                                  "dt_inicial,"+
			                                  "dt_final,"+
			                                  "lg_web,"+
			                                  "lg_fornecedor_selecionado,"+
			                                  "tp_pedido_compra,"+
			                                  "st_pedido_compra,"+
			                                  "id_pedido_compra,"+
			                                  "nr_pedido_compra) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setString(3,objeto.getNmPedidoCompra());
			pstmt.setString(4,objeto.getTxtPedidoCompra());
			pstmt.setInt(5,objeto.getNrPrazoEntrega());
			if(objeto.getDtPedidoCompra()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtPedidoCompra().getTimeInMillis()));
			if(objeto.getDtInicial()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setInt(9,objeto.getLgWeb());
			pstmt.setInt(10,objeto.getLgFornecedorSelecionado());
			pstmt.setInt(11,objeto.getTpPedidoCompra());
			pstmt.setInt(12,objeto.getStPedidoCompra());
			pstmt.setString(13,objeto.getIdPedidoCompra());
			pstmt.setString(14,objeto.getNrPedidoCompra());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PedidoCompra objeto) {
		return update(objeto, 0, null);
	}

	public static int update(PedidoCompra objeto, int cdPedidoCompraOld) {
		return update(objeto, cdPedidoCompraOld, null);
	}

	public static int update(PedidoCompra objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(PedidoCompra objeto, int cdPedidoCompraOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_pedido_compra SET cd_pedido_compra=?,"+
												      		   "cd_empresa=?,"+
												      		   "nm_pedido_compra=?,"+
												      		   "txt_pedido_compra=?,"+
												      		   "nr_prazo_entrega=?,"+
												      		   "dt_pedido_compra=?,"+
												      		   "dt_inicial=?,"+
												      		   "dt_final=?,"+
												      		   "lg_web=?,"+
												      		   "lg_fornecedor_selecionado=?,"+
												      		   "tp_pedido_compra=?,"+
												      		   "st_pedido_compra=?,"+
												      		   "id_pedido_compra=?,"+
												      		   "nr_pedido_compra=? WHERE cd_pedido_compra=?");
			pstmt.setInt(1,objeto.getCdPedidoCompra());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setString(3,objeto.getNmPedidoCompra());
			pstmt.setString(4,objeto.getTxtPedidoCompra());
			pstmt.setInt(5,objeto.getNrPrazoEntrega());
			if(objeto.getDtPedidoCompra()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtPedidoCompra().getTimeInMillis()));
			if(objeto.getDtInicial()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setInt(9,objeto.getLgWeb());
			pstmt.setInt(10,objeto.getLgFornecedorSelecionado());
			pstmt.setInt(11,objeto.getTpPedidoCompra());
			pstmt.setInt(12,objeto.getStPedidoCompra());
			pstmt.setString(13,objeto.getIdPedidoCompra());
			pstmt.setString(14,objeto.getNrPedidoCompra());
			pstmt.setInt(15, cdPedidoCompraOld!=0 ? cdPedidoCompraOld : objeto.getCdPedidoCompra());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPedidoCompra) {
		return delete(cdPedidoCompra, null);
	}

	public static int delete(int cdPedidoCompra, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_pedido_compra WHERE cd_pedido_compra=?");
			pstmt.setInt(1, cdPedidoCompra);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PedidoCompra get(int cdPedidoCompra) {
		return get(cdPedidoCompra, null);
	}

	public static PedidoCompra get(int cdPedidoCompra, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_pedido_compra WHERE cd_pedido_compra=?");
			pstmt.setInt(1, cdPedidoCompra);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PedidoCompra(rs.getInt("cd_pedido_compra"),
						rs.getInt("cd_empresa"),
						rs.getString("nm_pedido_compra"),
						rs.getString("txt_pedido_compra"),
						rs.getInt("nr_prazo_entrega"),
						(rs.getTimestamp("dt_pedido_compra")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_pedido_compra").getTime()),
						(rs.getTimestamp("dt_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()),
						rs.getInt("lg_web"),
						rs.getInt("lg_fornecedor_selecionado"),
						rs.getInt("tp_pedido_compra"),
						rs.getInt("st_pedido_compra"),
						rs.getString("id_pedido_compra"),
						rs.getString("nr_pedido_compra"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_pedido_compra");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PedidoCompraDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM adm_pedido_compra", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
