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

public class GrupoProcessoServices {
	
	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO   = 1;
	
	public static Result save(GrupoProcesso grupoProcesso){
		return save(grupoProcesso, null);
	}
	
	public static Result save(GrupoProcesso grupoProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(grupoProcesso==null)
				return new Result(-1, "Erro ao salvar. Grupo de Processo é nulo");
			
			int retorno;
			if(grupoProcesso.getCdGrupoProcesso()==0){
				retorno = GrupoProcessoDAO.insert(grupoProcesso, connect);
				grupoProcesso.setCdGrupoProcesso(retorno);
			}
			else {
				retorno = GrupoProcessoDAO.update(grupoProcesso, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "GRUPOPROCESSO", grupoProcesso);
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
	
	public static Result remove(int cdGrupoProcesso){
		return remove(cdGrupoProcesso, false, null);
	}
	
	public static Result remove(int cdGrupoProcesso, boolean cascade){
		return remove(cdGrupoProcesso, cascade, null);
	}
	
	public static Result remove(int cdGrupoProcesso, boolean cascade, Connection connect){
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
				retorno = GrupoProcessoDAO.delete(cdGrupoProcesso, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este grupo de processo está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Grupo de processo excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir grupo de processo!");
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_grupo_processo ORDER BY nm_grupo_processo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAtivos() {
		return getAtivos(null);
	}

	public static ResultSetMap getAtivos(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					  " SELECT * "
					+ " FROM prc_grupo_processo"
					+ " WHERE st_grupo_processo = "+ST_ATIVO
					+ " ORDER BY nm_grupo_processo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByUsuario(int cdUsuario) {
		return getAllByUsuario(cdUsuario, -1, null);
	}
	
	public static ResultSetMap getAllByUsuario(int cdUsuario, int stGrupoProcesso) {
		return getAllByUsuario(cdUsuario, stGrupoProcesso, null);
	}

	public static ResultSetMap getAllByUsuario(int cdUsuario, int stGrupoProcesso, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {			
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_grupo_processo " +
					" FROM prc_usuario_grupo A " +
					" LEFT OUTER JOIN prc_grupo_processo B ON (B.cd_grupo_processo = A.cd_grupo_processo) " +
					" WHERE A.cd_usuario = ? " +
					(stGrupoProcesso>=0 ? " AND B.st_grupo_processo = "+stGrupoProcesso : "") +
					" ORDER BY B.nm_grupo_processo, B.st_grupo_processo DESC");
			pstmt.setInt(1, cdUsuario);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
						
			// Se não houver grupo para o usuario, retornar todos os grupos.
			if(rsm.size()==0) {
				if(stGrupoProcesso==ST_ATIVO)
					rsm = getAtivos(connect);
				else 
					rsm = getAll(connect);
			}
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		String nmGrupoProcesso = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("NM_GRUPO_PROCESSO")) {
				nmGrupoProcesso =	Util.limparTexto(criterios.get(i).getValue());
				nmGrupoProcesso = nmGrupoProcesso.trim();
				criterios.remove(i);
				i--;
			}
		}
		return Search.find("SELECT * FROM prc_grupo_processo WHERE 1=1 "+
				(!nmGrupoProcesso.equals("") ?
				"AND TRANSLATE (nm_grupo_processo, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', " + 
				"				'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmGrupoProcesso)+"%' "
				: "") +
				"ORDER BY nm_grupo_processo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}