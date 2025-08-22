package com.tivic.manager.prc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class TipoProcessoServices {
	public static Result save(TipoProcesso tipoProcesso){
		return save(tipoProcesso, null);
	}
	
	public static Result save(TipoProcesso tipoProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(tipoProcesso==null)
				return new Result(-1, "Erro ao salvar. Tipo de Processo é nulo");
			
			int retorno;
			if(tipoProcesso.getCdTipoProcesso()==0){
				retorno = TipoProcessoDAO.insert(tipoProcesso, connect);
				tipoProcesso.setCdTipoProcesso(retorno);
			}
			else {
				retorno = TipoProcessoDAO.update(tipoProcesso, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOPROCESSO", tipoProcesso);
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
	
	public static Result remove(int cdTipoProcesso){
		return remove(cdTipoProcesso, false, null);
	}
	
	public static Result remove(int cdTipoProcesso, boolean cascade){
		return remove(cdTipoProcesso, cascade, null);
	}
	
	public static Result remove(int cdTipoProcesso, boolean cascade, Connection connect){
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
				retorno = TipoProcessoDAO.delete(cdTipoProcesso, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este tipo de processo está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Tipo de processo excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir tipo de processo!");
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
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_area_direito FROM prc_tipo_processo A " +
					" LEFT OUTER JOIN prc_area_direito B ON (B.cd_area_direito = A.cd_area_direito)" +
					" ORDER BY A.nm_tipo_processo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoProcessoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoProcessoDAO.getAll: " + e);
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
		String nmTipoProcesso = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("NM_TIPO_PROCESSO")) {
				nmTipoProcesso =	Util.limparTexto(criterios.get(i).getValue());
				nmTipoProcesso = nmTipoProcesso.trim();
				criterios.remove(i);
				i--;
			}
		}
		return Search.find("SELECT * FROM prc_tipo_processo WHERE 1=1 "+
				(!nmTipoProcesso.equals("") ?
						"				AND TRANSLATE (nm_tipo_processo, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', " + 
						"								'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmTipoProcesso)+"%' "
										: "") +
				"ORDER BY nm_tipo_processo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}