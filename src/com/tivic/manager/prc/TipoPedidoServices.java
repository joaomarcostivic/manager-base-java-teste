package com.tivic.manager.prc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class TipoPedidoServices {
	public static Result save(TipoPedido tipoPedido){
		return save(tipoPedido, null);
	}
	
	public static Result save(TipoPedido tipoPedido, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(tipoPedido==null)
				return new Result(-1, "Erro ao salvar. Tipo de pedido é nulo");
			
			int retorno;
			if(tipoPedido.getCdTipoPedido()==0){
				retorno = TipoPedidoDAO.insert(tipoPedido, connect);
				tipoPedido.setCdTipoPedido(retorno);
			}
			else {
				retorno = TipoPedidoDAO.update(tipoPedido, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOPEDIDO", tipoPedido);
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
	
	public static Result remove(int cdTipoPedido){
		return remove(cdTipoPedido, false, null);
	}
	
	public static Result remove(int cdTipoPedido, boolean cascade){
		return remove(cdTipoPedido, cascade, null);
	}
	
	public static Result remove(int cdTipoPedido, boolean cascade, Connection connect){
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
				retorno = TipoPedidoDAO.delete(cdTipoPedido, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este tipo de pedido está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Tipo de pedido excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir tipo de pedido!");
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_tipo_pedido ORDER BY nm_tipo_pedido");
			return new ResultSetMap(pstmt.executeQuery());
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
		String nmTipoPedido = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("NM_TIPO_PEDIDO")) {
				nmTipoPedido =	Util.limparTexto(criterios.get(i).getValue());
				nmTipoPedido = nmTipoPedido.trim();
				criterios.remove(i);
				i--;
			}
		}
		return Search.find( "SELECT * FROM prc_tipo_pedido "+
				"WHERE 1=1 "+
				(!nmTipoPedido.equals("") ?
				" AND TRANSLATE (nm_tipo_pedido, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', "+
						"					'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmTipoPedido)+"%' "
						: "") +
				"ORDER BY nm_tipo_pedido", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap find(String nmTipoPedido) {
		return find(nmTipoPedido, null);
	}

	public static ResultSetMap find(String nmTipoPedido, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * " +
					" FROM prc_tipo_pedido " +
					((nmTipoPedido!=null && !nmTipoPedido.equals(""))?" WHERE nm_tipo_pedido like '"+nmTipoPedido+"%' ":"")+
					" ORDER BY nm_tipo_pedido");
			return new ResultSetMap(pstmt.executeQuery());
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
}
