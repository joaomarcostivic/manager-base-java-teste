package com.tivic.manager.seg;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class ReleaseServices {

	public static Result save(Release release){
		return save(release, null);
	}
	
	public static Result save(Release release, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(release==null)
				return new Result(-1, "Erro ao salvar. Release é nulo");
			
			int retorno;
			if(release.getCdRelease()==0){
				retorno = ReleaseDAO.insert(release, connect);
				release.setCdRelease(retorno);
			}
			else {
				retorno = ReleaseDAO.update(release, connect);
			}
			
			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "RELEASE", release);
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
	
	public static Result remove(int cdRelease){
		return remove(cdRelease, false, null);
	}
	
	public static Result remove(int cdRelease, boolean cascade){
		return remove(cdRelease, cascade, null);
	}
	
	public static Result remove(int cdRelease, boolean cascade, Connection connect){
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
				retorno = ReleaseDAO.delete(cdRelease, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este release está vinculado a outros registros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Release excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir release!");
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
			pstmt = connect.prepareStatement("SELECT A.cd_release, A.nr_maior, A.nr_menor, A.nr_build, A.txt_descricao, A.dt_release, A.cd_sistema, A.lg_executado, " + 
					" CAST(CASE WHEN blb_release is null THEN 0 ELSE 1 END AS smallint) as lg_arquivo, B.nm_sistema " +
					" FROM seg_release A " +
					" LEFT OUTER JOIN seg_sistema B ON (A.cd_sistema = B.cd_sistema) " + 
					" ORDER BY nr_maior DESC, nr_menor DESC, nr_build DESC");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ReleaseServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static String getLastRelease() {
		return getLastRelease(1, null); //Sistema Padrão
	}
	
	public static String getLastRelease(int cdSistema) {
		return getLastRelease(cdSistema, null);
	}

	public static String getLastRelease(int cdSistema, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT nr_maior, nr_menor, nr_build FROM seg_release" +
					   " WHERE LG_EXECUTADO = 1 AND cd_sistema = "+ cdSistema +
					   " ORDER BY nr_maior DESC, nr_menor DESC, nr_build DESC");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next()) {
				return rsm.getInt("NR_MAIOR") + "." + rsm.getInt("NR_MENOR") + "." + rsm.getInt("NR_BUILD"); 
			}
			else {
				return "0.0.0";
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ReleaseServices.getLastRelease: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getReleases (int nrReleases){
		return getReleases(nrReleases, null);
	}
	
	public static ResultSetMap getReleases(int nrReleases, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.cd_release, A.nr_maior, A.nr_menor, A.nr_build, A.txt_descricao, A.dt_release, A.cd_sistema, A.lg_executado, " + 
					" CAST(CASE WHEN blb_release is null THEN 0 ELSE 1 END AS smallint) as lg_arquivo, B.nm_sistema " +
					" FROM seg_release A " +
					" LEFT OUTER JOIN seg_sistema B ON (A.cd_sistema = B.cd_sistema) " + 
					" ORDER BY nr_maior DESC, nr_menor DESC, nr_build DESC LIMIT ?");
			pstmt.setInt(1, nrReleases);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()) {
				
				GregorianCalendar dtRelease = rsm.getGregorianCalendar("dt_release");
				if(dtRelease != null)
					rsm.setValueToField("DT_NM_RELEASE", (Integer.parseInt(String.valueOf(dtRelease.get(Calendar.DAY_OF_MONTH))) > 9 ? dtRelease.get(Calendar.DAY_OF_MONTH) : "0" + dtRelease.get(Calendar.DAY_OF_MONTH)) + "/" + (Integer.parseInt(String.valueOf(dtRelease.get(Calendar.MONTH))) + 1 > 9 ? (Integer.parseInt(String.valueOf(dtRelease.get(Calendar.MONTH))) + 1) : "0" + (Integer.parseInt(String.valueOf(dtRelease.get(Calendar.MONTH))) + 1)) + "/" + dtRelease.get(Calendar.YEAR));
				
				
				rsm.setValueToField("NR_FULL_VERSION", rsm.getInt("NR_MAIOR") + "." + rsm.getInt("NR_MENOR") + "." + rsm.getInt("NR_BUILD")); 
			}
			
			return rsm;
			
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ReleaseServices.getAll: " + e);
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
		return Search.find("SELECT * FROM seg_release", "ORDER BY nr_maior, nr_menor, nr_build, nr_build", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
