package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class TipoGrupoEscolarServices {
	public static Result save(TipoGrupoEscolar tipoGrupoEscolar){
		return save(tipoGrupoEscolar, null);
	}
	
	public static Result save(TipoGrupoEscolar tipoGrupoEscolar, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(tipoGrupoEscolar==null)
				return new Result(-1, "Erro ao salvar. Tipo de grupo escolar é nulo");
			
			int retorno;
			if(tipoGrupoEscolar.getCdTipoGrupo()==0){
				retorno = TipoGrupoEscolarDAO.insert(tipoGrupoEscolar, connect);
				tipoGrupoEscolar.setCdTipoGrupo(retorno);
			}
			else {
				retorno = TipoGrupoEscolarDAO.update(tipoGrupoEscolar, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOGRUPOESCOLAR", tipoGrupoEscolar);
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
	
	public static Result remove(int cdTipoGrupoEscolar){
		return remove(cdTipoGrupoEscolar, false, null);
	}
	
	public static Result remove(int cdTipoGrupoEscolar, boolean cascade){
		return remove(cdTipoGrupoEscolar, cascade, null);
	}
	
	public static Result remove(int cdTipoGrupoEscolar, boolean cascade, Connection connect){
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
				retorno = TipoGrupoEscolarDAO.delete(cdTipoGrupoEscolar, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este tipo de grupo escolar está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Tipo de grupo escolar excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir tipo de grupo escolar!");
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_grupo_escolar ORDER BY nm_grupo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoGrupoEscolarDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_tipo_grupo_escolar", "ORDER BY nm_grupo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
