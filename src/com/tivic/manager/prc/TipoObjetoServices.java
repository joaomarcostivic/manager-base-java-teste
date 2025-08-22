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

public class TipoObjetoServices {
	public static Result save(TipoObjeto tipoObjeto){
		return save(tipoObjeto, null);
	}
	
	public static Result save(TipoObjeto tipoObjeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(tipoObjeto==null)
				return new Result(-1, "Erro ao salvar. Objeto da ação é nulo");
			
			int retorno;
			if(tipoObjeto.getCdTipoObjeto()==0){
				retorno = TipoObjetoDAO.insert(tipoObjeto, connect);
				tipoObjeto.setCdTipoObjeto(retorno);
			}
			else {
				retorno = TipoObjetoDAO.update(tipoObjeto, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOOBJETO", tipoObjeto);
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
	
	public static Result remove(int cdTipoObjeto){
		return remove(cdTipoObjeto, false, null);
	}
	
	public static Result remove(int cdTipoObjeto, boolean cascade){
		return remove(cdTipoObjeto, cascade, null);
	}
	
	public static Result remove(int cdTipoObjeto, boolean cascade, Connection connect){
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
				retorno = TipoObjetoDAO.delete(cdTipoObjeto, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este objeto da ação está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Objeto da ação excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir objeto da ação!");
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_tipo_objeto ORDER BY nm_tipo_objeto");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoObjetoDAO.getAll: " + e);
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
		String nmTipoObjeto = "";
		for (int i=0; criterios!=null && i<criterios.size(); i++) {
			if (criterios.get(i).getColumn().equalsIgnoreCase("NM_TIPO_OBJETO")) {
				nmTipoObjeto =	Util.limparTexto(criterios.get(i).getValue());
				nmTipoObjeto = nmTipoObjeto.trim();
				criterios.remove(i);
				i--;
			}
		}
		return Search.find("SELECT * FROM prc_tipo_objeto WHERE 1=1"+
				(!nmTipoObjeto.equals("") ?
				"AND TRANSLATE (nm_tipo_objeto, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', " + 
				"				'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmTipoObjeto)+"%' "
				: "") + 
				"ORDER BY nm_tipo_objeto", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
}
