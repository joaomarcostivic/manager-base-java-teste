package com.tivic.manager.str;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

public class GeoTrackerServices {

	public static ResultSetMap track(String idOrgao, String nrMatriculaAgente, GregorianCalendar dtInicial, GregorianCalendar dtFinal, boolean lastPosition) {
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		
		if (idOrgao != null && !idOrgao.equals(""))
			criterios.add(new ItemComparator("A.id_orgao", idOrgao, ItemComparator.EQUAL, Types.VARCHAR));
		if (nrMatriculaAgente != null && !nrMatriculaAgente.equals(""))
			criterios.add(new ItemComparator("A.nr_matricula_agente", nrMatriculaAgente, ItemComparator.EQUAL, Types.VARCHAR));
		if (dtInicial != null)
			criterios.add(new ItemComparator("A.dt_historico", Util.formatDateTime(dtInicial, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
		if (dtFinal != null)
			criterios.add(new ItemComparator("A.dt_historico", Util.formatDateTime(dtFinal, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
		
		criterios.add(new ItemComparator("A.id_equipamento", "", ItemComparator.DIFFERENT, Types.VARCHAR));
				
		return find(criterios, lastPosition, null);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios, boolean lastPosition) {
		return find(criterios, lastPosition, null);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, false, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, boolean lastPosition, Connection connect) {
		
		return Search.find("SELECT A.*, B.nm_orgao, C.nm_agente, C.tp_agente, D.nm_cidade FROM str_geo_tracker A " +
				" LEFT OUTER JOIN str_orgao B ON (A.id_orgao = B.id_orgao) " +
				" LEFT OUTER JOIN grl_cidade D ON (B.cd_cidade = D.cd_cidade) " +
				" LEFT OUTER JOIN str_agente C ON (A.nr_matricula_agente = C.nr_matricula) " +
				(lastPosition ? "WHERE A.cd_tracker IN (SELECT DISTINCT ON(id_orgao, nr_matricula_agente) cd_tracker " +
														"FROM str_geo_tracker " +
														"WHERE nr_matricula_agente <> '' " +
														"ORDER BY id_orgao, nr_matricula_agente, dt_historico DESC) " : ""), 
				"ORDER BY A.id_orgao, A.nr_matricula_agente, A.dt_historico ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap trackVeiculos(String idOrgao, GregorianCalendar dtInicial, GregorianCalendar dtFinal, boolean lastPosition) {
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		
		if (idOrgao != null && !idOrgao.equals(""))
			criterios.add(new ItemComparator("E.id_orgao", idOrgao, ItemComparator.EQUAL, Types.VARCHAR));		
		if (dtInicial != null)
			criterios.add(new ItemComparator("F.dt_historico", Util.formatDateTime(dtInicial, "dd/MM/yyyy HH:mm:ss"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
		if (dtFinal != null)
			criterios.add(new ItemComparator("F.dt_historico", Util.formatDateTime(dtFinal, "dd/MM/yyyy HH:mm:ss"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
		
		criterios.add(new ItemComparator("D.id_equipamento", "", ItemComparator.DIFFERENT, Types.VARCHAR));
		
		return findVeiculos(criterios, lastPosition, null);
	}

	public static ResultSetMap findVeiculos(ArrayList<ItemComparator> criterios, boolean lastPosition) {
		return findVeiculos(criterios, lastPosition, null);
	}
	
	public static ResultSetMap findVeiculos(ArrayList<ItemComparator> criterios, boolean lastPosition, Connection connect) {
		
		return Search.find("SELECT A.*, B.nr_prefixo, C.nr_placa, C.cd_tipo_veiculo, D.id_equipamento, E.*, F.*, G.nm_cidade "+
						   "FROM mob_veiculo_equipamento A " +
						   "LEFT OUTER JOIN mob_concessao_veiculo B ON (A.cd_veiculo = B.cd_veiculo) " +
						   "LEFT OUTER JOIN fta_veiculo           C ON (A.cd_veiculo = C.cd_veiculo) " + 
						   "LEFT OUTER JOIN grl_equipamento       D ON (A.cd_equipamento = D.cd_equipamento) " +
						   "LEFT OUTER JOIN str_orgao             E ON (D.cd_orgao = E.cd_orgao) "+
						   "LEFT OUTER JOIN str_geo_tracker       F ON (E.id_orgao = F.id_orgao  AND D.id_equipamento = F.id_equipamento) "+
						   "LEFT OUTER JOIN grl_cidade            G ON (E.cd_cidade = G.cd_cidade) "+
						   (lastPosition ? "WHERE G.cd_tracker IN (SELECT DISTINCT ON(id_orgao, nr_matricula_agente) cd_tracker " +
														"FROM str_geo_tracker " +
														"WHERE nr_matricula_agente = '' " +
														"ORDER BY id_orgao, nr_matricula_agente, dt_historico DESC) " : ""), 
						   "ORDER BY E.id_orgao, B.nr_prefixo, F.dt_historico ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static Result save(GeoTracker geoTracker){
		return save(geoTracker, null, null);
	}

	public static Result save(GeoTracker geoTracker, AuthData authData){
		return save(geoTracker, authData, null);
	}

	public static Result save(GeoTracker geoTracker, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(geoTracker==null)
				return new Result(-1, "Erro ao salvar. GeoTracker é nulo");

			int retorno;
			if(geoTracker.getCdTracker()==0){
				retorno = GeoTrackerDAO.insert(geoTracker, connect);
				geoTracker.setCdTracker(retorno);
			}
			else {
				retorno = GeoTrackerDAO.update(geoTracker, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "GEOTRACKER", geoTracker);
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
	public static Result remove(GeoTracker geoTracker) {
		return remove(geoTracker.getCdTracker());
	}
	public static Result remove(int cdTracker){
		return remove(cdTracker, false, null, null);
	}
	public static Result remove(int cdTracker, boolean cascade){
		return remove(cdTracker, cascade, null, null);
	}
	public static Result remove(int cdTracker, boolean cascade, AuthData authData){
		return remove(cdTracker, cascade, authData, null);
	}
	public static Result remove(int cdTracker, boolean cascade, AuthData authData, Connection connect){
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
			retorno = GeoTrackerDAO.delete(cdTracker, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e não pode ser excluído!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluído com sucesso!");
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir registro!");
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
			pstmt = connect.prepareStatement("SELECT * FROM str_geo_tracker");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GeoTrackerServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GeoTrackerServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
		
}
