package com.tivic.manager.prc;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TipoPedidoDAO{

	public static int insert(TipoPedido objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoPedido objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("prc_tipo_pedido", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if(objeto.getCdTipoPedido()<=0)
				objeto.setCdTipoPedido(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO prc_tipo_pedido (cd_tipo_pedido,"+
			                                  "nm_tipo_pedido,"+
			                                  "id_tipo_pedido) VALUES (?, ?, ?)");
			pstmt.setInt(1, objeto.getCdTipoPedido());
			pstmt.setString(2,objeto.getNmTipoPedido());
			pstmt.setString(3,objeto.getIdTipoPedido());
			pstmt.executeUpdate();
			return objeto.getCdTipoPedido();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPedidoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoPedido objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoPedido objeto, int cdTipoPedidoOld) {
		return update(objeto, cdTipoPedidoOld, null);
	}

	public static int update(TipoPedido objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoPedido objeto, int cdTipoPedidoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_tipo_pedido SET cd_tipo_pedido=?,"+
												      		   "nm_tipo_pedido=?,"+
												      		   "id_tipo_pedido=? WHERE cd_tipo_pedido=?");
			pstmt.setInt(1,objeto.getCdTipoPedido());
			pstmt.setString(2,objeto.getNmTipoPedido());
			pstmt.setString(3,objeto.getIdTipoPedido());
			pstmt.setInt(4, cdTipoPedidoOld!=0 ? cdTipoPedidoOld : objeto.getCdTipoPedido());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoPedidoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPedidoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoPedido) {
		return delete(cdTipoPedido, null);
	}

	public static int delete(int cdTipoPedido, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_tipo_pedido WHERE cd_tipo_pedido=?");
			pstmt.setInt(1, cdTipoPedido);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoPedidoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPedidoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoPedido get(int cdTipoPedido) {
		return get(cdTipoPedido, null);
	}

	public static TipoPedido get(int cdTipoPedido, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_tipo_pedido WHERE cd_tipo_pedido=?");
			pstmt.setInt(1, cdTipoPedido);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoPedido(rs.getInt("cd_tipo_pedido"),
						rs.getString("nm_tipo_pedido"),
						rs.getString("id_tipo_pedido"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoPedidoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPedidoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_tipo_pedido");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoPedidoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoPedidoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM prc_tipo_pedido", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
