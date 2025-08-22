package com.tivic.manager.grl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class TipoEnderecoServices {
	public static Result save(TipoEndereco tipoEndereco){
		return save(tipoEndereco, null);
	}
	
	public static Result save(TipoEndereco tipoEndereco, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(tipoEndereco==null)
				return new Result(-1, "Erro ao salvar. Tipo de endereço é nulo");
			
			int retorno;
			if(tipoEndereco.getCdTipoEndereco()==0){
				retorno = TipoEnderecoDAO.insert(tipoEndereco, connect);
				tipoEndereco.setCdTipoEndereco(retorno);
			}
			else {
				retorno = TipoEnderecoDAO.update(tipoEndereco, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOENDERECO", tipoEndereco);
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
	
	public static Result remove(int cdTipoEndereco){
		return remove(cdTipoEndereco, false, null);
	}
	
	public static Result remove(int cdTipoEndereco, boolean cascade){
		return remove(cdTipoEndereco, cascade, null);
	}
	
	public static Result remove(int cdTipoEndereco, boolean cascade, Connection connect){
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
				retorno = TipoEnderecoDAO.delete(cdTipoEndereco, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este tipo de endereço está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Tipo de endereço excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir tipo de endereço!");
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_endereco ORDER BY nm_tipo_endereco");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEnderecoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_tipo_endereco", "ORDER BY nm_tipo_endereco", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
