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

public class AreaDireitoServices {
	public static Result save(AreaDireito areaDireito){
		return save(areaDireito, null);
	}
	
	public static Result save(AreaDireito areaDireito, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(areaDireito==null)
				return new Result(-1, "Erro ao salvar. Área do direito é nulo");
			
			int retorno;
			if(areaDireito.getCdAreaDireito()==0){
				retorno = AreaDireitoDAO.insert(areaDireito, connect);
				areaDireito.setCdAreaDireito(retorno);
			}
			else {
				retorno = AreaDireitoDAO.update(areaDireito, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AREADIREITO", areaDireito);
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
	
	public static Result remove(int cdAreaDireito){
		return remove(cdAreaDireito, false, null);
	}
	
	public static Result remove(int cdAreaDireito, boolean cascade){
		return remove(cdAreaDireito, cascade, null);
	}
	
	public static Result remove(int cdAreaDireito, boolean cascade, Connection connect){
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
				retorno = AreaDireitoDAO.delete(cdAreaDireito, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta área do direito está vinculada a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Área do direito excluída com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir área do direito!");
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_area_direito ORDER BY nm_area_direito");
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


	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM prc_area_direito ", "ORDER BY nm_area_direito", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	/** 
	 * Buscar os relatórios separando-os por Área de Direito.
	 * @return ResultSetMap
	 * 
	 */
	
	public static ResultSetMap getAreaDireitoAgrupado(){
		return getAreaDireitoAgrupado(new ArrayList<Integer>(), null);
	}
	
	public static ResultSetMap getAreaDireitoAgrupado(ArrayList<Integer> processos){
		return getAreaDireitoAgrupado(processos, null);
	}
	
	public static ResultSetMap getAreaDireitoAgrupado(ArrayList<Integer> processos, Connection connect){
		boolean isConnectionNull = connect==null;
		ResultSetMap result = new ResultSetMap();
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT C.NM_AREA_DIREITO as NM_AGRUPAMENTO, COUNT(A.CD_PROCESSO) AS QT_PROCESSOS, (COUNT(A.CD_PROCESSO) / cast ((SELECT COUNT(*) FROM PRC_PROCESSO) as numeric(15,5)) * 100) AS QT_PERCENTUAL " +
					"FROM PRC_PROCESSO      A " + 
					"LEFT JOIN PRC_TIPO_PROCESSO B ON (A.CD_TIPO_PROCESSO = B.CD_TIPO_PROCESSO) " +
					"LEFT JOIN PRC_AREA_DIREITO  C ON (B.CD_AREA_DIREITO  = C.CD_AREA_DIREITO) " +
					(processos.size()>0 ? " WHERE A.cd_processo IN ("+Util.join(processos)+") " : "")+
					"GROUP BY C.NM_AREA_DIREITO "
					);
			result = new ResultSetMap(pstmt.executeQuery());
			return result;
		} catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AreaDireitoServices.findProcessoAreaDireito: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAreaDireitoTipoAcaoAgrupado(){
		return getAreaDireitoTipoAcaoAgrupado(new ArrayList<Integer>(), null);
	}
	
	public static ResultSetMap getAreaDireitoTipoAcaoAgrupado(ArrayList<Integer> processos){
		return getAreaDireitoTipoAcaoAgrupado(processos, null);
	}
	
	public static ResultSetMap getAreaDireitoTipoAcaoAgrupado(ArrayList<Integer> processos, Connection connect){
		boolean isConnectionNull = connect==null;
		ResultSetMap result = new ResultSetMap();
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.CD_TIPO_PROCESSO, C.NM_AREA_DIREITO || ': ' || B.NM_TIPO_PROCESSO as NM_AGRUPAMENTO, B.NM_TIPO_PROCESSO, COUNT(A.CD_PROCESSO) AS QT_PROCESSOS, (COUNT(A.CD_PROCESSO) / cast ((SELECT COUNT(*) FROM PRC_PROCESSO) as numeric(15,5)) * 100) AS QT_PERCENTUAL " +
					"FROM PRC_PROCESSO      A " + 
					"LEFT JOIN PRC_TIPO_PROCESSO B ON (A.CD_TIPO_PROCESSO = B.CD_TIPO_PROCESSO) " +
					"LEFT JOIN PRC_AREA_DIREITO  C ON (B.CD_AREA_DIREITO  = C.CD_AREA_DIREITO) " +
					(processos.size()>0 ? " WHERE A.cd_processo IN ("+Util.join(processos)+") " : "")+
					"GROUP BY C.NM_AREA_DIREITO, A.CD_TIPO_PROCESSO, B.NM_TIPO_PROCESSO " +
					"ORDER BY C.NM_AREA_DIREITO, B.NM_TIPO_PROCESSO "
					);
//			pstmt.setString(1, nrProcesso);
			result = new ResultSetMap(pstmt.executeQuery());
			return result;
		} catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AreaDireitoServices.findProcessoAreaDireitoTipoAcao: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}