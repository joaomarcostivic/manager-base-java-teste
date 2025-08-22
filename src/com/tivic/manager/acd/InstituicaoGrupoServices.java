package com.tivic.manager.acd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class InstituicaoGrupoServices {
	
	public static Result save(InstituicaoGrupo instituicaoGrupo){
		return save(instituicaoGrupo, null);
	}
	
	public static Result save(InstituicaoGrupo instituicaoGrupo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(instituicaoGrupo==null)
				return new Result(-1, "Erro ao salvar. Grupo escolar é nulo");
			
			if(instituicaoGrupo.getDtCriacao().getTimeInMillis() > instituicaoGrupo.getDtExtincao().getTimeInMillis())
				return new Result(-1, "Erro ao Salvar Grupos! A data de Extinção não pode ser anterior a data de Criação");
			
			int retorno;
			if(instituicaoGrupo.getCdGrupo()==0){
				retorno = InstituicaoGrupoDAO.insert(instituicaoGrupo, connect);
				instituicaoGrupo.setCdGrupo(retorno);
			}
			else {
				retorno = InstituicaoGrupoDAO.update(instituicaoGrupo, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "INSTITUICAOGRUPO", instituicaoGrupo);
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
	
	public static Result remove(int cdGrupo, int cdInstituicao){
		return remove(cdGrupo, cdInstituicao, false, null);
	}
	
	public static Result remove(int cdGrupo, int cdInstituicao, boolean cascade){
		return remove(cdGrupo, cdInstituicao, cascade, null);
	}
	
	public static Result remove(int cdGrupo, int cdInstituicao, boolean cascade, Connection connect){
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
				retorno = InstituicaoGrupoDAO.delete(cdGrupo, cdInstituicao, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este grupo escolar está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Grupo escolar excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir grupo escolar!");
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
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_grupo as nm_tipo_grupo FROM acd_instituicao_grupo A " +
					"JOIN acd_tipo_grupo_escolar B ON (A.cd_tipo_grupo = B.cd_tipo_grupo) ORDER BY B.nm_grupo, A.nm_grupo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoGrupoDAO.getAll: " + e);
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
		return Search.find("SELECT A.*, B.nm_grupo as nm_tipo_grupo FROM acd_instituicao_grupo A " +
				"JOIN acd_tipo_grupo_escolar B ON (A.cd_tipo_grupo = B.cd_tipo_grupo)", "ORDER BY B.nm_grupo, A.nm_grupo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
