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

public class JuizoServices {
	public static Result save(Juizo juizo){
		return save(juizo, null);
	}
	
	public static Result save(Juizo juizo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(juizo==null)
				return new Result(-1, "Erro ao salvar. Juízo é nulo");
			
			int retorno;
			if(juizo.getCdJuizo()==0){
				retorno = JuizoDAO.insert(juizo, connect);
				juizo.setCdJuizo(retorno);
			}
			else {
				retorno = JuizoDAO.update(juizo, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "JUIZO", juizo);
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
	
	public static Result remove(int cdJuizo){
		return remove(cdJuizo, false, null);
	}
	
	public static Result remove(int cdJuizo, boolean cascade){
		return remove(cdJuizo, cascade, null);
	}
	
	public static Result remove(int cdJuizo, boolean cascade, Connection connect){
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
				retorno = JuizoDAO.delete(cdJuizo, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este juizo está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Juízo excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir juízo!");
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_juizo ORDER BY nm_juizo");
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
		boolean isConnectionNull = connect==null;
		String nmJuizo = "";
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("NM_JUIZO")) {
					nmJuizo =	Util.limparTexto(criterios.get(i).getValue());
					nmJuizo = nmJuizo.trim();
					criterios.remove(i);
					i--;
				}
			}
			
		return Search.find("SELECT * FROM PRC_JUIZO WHERE 1=1 " +
				(!nmJuizo.equals("") ?  
				"AND TRANSLATE (nm_juizo, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC') iLIKE '%"+Util.limparAcentos(nmJuizo)+"%' " 
				: ""), criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
		catch(Exception e) {
			Util.registerLog(e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findProcessoCidadeJuizo(){
		return findProcessoCidadeJuizo(null);
	}
	
	public static ResultSetMap findProcessoCidadeJuizo(Connection connect){
		boolean isConnectionNull = connect==null;
		ResultSetMap result = new ResultSetMap();
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT B.NM_CIDADE || ': ' || A.NR_JUIZO || ' ' || C.NM_JUIZO AS NM_AGRUPAMENTO, COUNT(A.CD_PROCESSO) AS QT_PROCESSOS, (COUNT(A.CD_PROCESSO) / CAST ((SELECT COUNT(*) FROM PRC_PROCESSO) AS numeric(15,5)) * 100) AS QT_PERCENTUAL " +
					"FROM PRC_PROCESSO A " + 
					"JOIN GRL_CIDADE B ON (A.CD_CIDADE = B.CD_CIDADE) " +
					"JOIN PRC_JUIZO C ON (C.CD_JUIZO = A.CD_JUIZO) " +
					"GROUP BY B.NM_CIDADE, A.NR_JUIZO, C.NM_JUIZO " +
					"ORDER BY B.NM_CIDADE "
//	                "WHERE nr_processo = ?"
					);
			result = new ResultSetMap(pstmt.executeQuery());
			return result;
		} catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! JuizoServices.findProcessoCidadeJuizo: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getCidadeJuizoAgrupado(String nmAgrupamento, ArrayList<Integer> processos, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					"SELECT B.NM_CIDADE || ': ' || A.NR_JUIZO || ' ' || C.NM_JUIZO AS NM_AGRUPAMENTO, COUNT(A.CD_PROCESSO) AS QT_PROCESSOS, (COUNT(A.CD_PROCESSO) / CAST ((SELECT COUNT(*) FROM PRC_PROCESSO) AS numeric(15,5)) * 100) AS QT_PERCENTUAL " +
							"FROM PRC_PROCESSO A " + 
							"JOIN GRL_CIDADE B ON (A.CD_CIDADE = B.CD_CIDADE) " +
							"JOIN PRC_JUIZO C ON (C.CD_JUIZO = A.CD_JUIZO) " +
							"WHERE A.NR_JUIZO IS NOT NULL " +
							(processos.size()>0 ? " AND A.cd_processo IN ("+Util.join(processos)+")" : "") +
							"GROUP BY B.NM_CIDADE, A.NR_JUIZO, C.NM_JUIZO " +
							"ORDER BY B.NM_CIDADE "
					);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! JuizoServices.getCidadeJuizoAgrupado: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}