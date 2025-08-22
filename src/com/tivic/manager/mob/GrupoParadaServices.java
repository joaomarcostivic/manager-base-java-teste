package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.geo.Ponto;
import com.tivic.manager.geo.PontoDAO;
import com.tivic.manager.geo.PontoServices;
import com.tivic.manager.geo.Referencia;
import com.tivic.manager.geo.ReferenciaDAO;
import com.tivic.manager.geo.ReferenciaServices;
import com.tivic.manager.util.Util;

public class GrupoParadaServices {
	
	public static final int TP_GRUPO_TERMINAL_TRANSBORDO = 0;
	public static final int TP_GRUPO_TERMINAL 	         = 1;
	public static final int TP_GRUPO_PLATAFORMA 		 = 2;
	public static final int TP_GRUPO_PRACA_TAXI          = 3;
	
	public static final String[] tiposGruposParadas = {"Terminal Transbordo", "Terminal", "Plataforma", "Praça (Táxi)"};

	public static Result save(GrupoParada grupoParada){
		return save(grupoParada, null);
	}
	public static Result save(GrupoParada grupoParada, Connection connect){
		return save(grupoParada, null, null, null);
	}
	
	public static Result save(GrupoParada grupoParada, Ponto ponto, Referencia referencia){
		return save(grupoParada, ponto, referencia, null);
	}
	
	public static Result save(GrupoParada grupoParada, Ponto ponto, Referencia referencia,  Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(grupoParada==null)
				return new Result(-1, "Erro ao salvar. GrupoParada não nulo");
			
			int retorno;
			
				if(grupoParada.getCdGrupoParada()==0){
					
					//mob_parada -> geo_referencia -> geo_ponto
					if (referencia != null && referencia.getCdReferencia() == 0){
						ReferenciaServices.save(referencia);
						grupoParada.setCdGeorreferencia(referencia.getCdReferencia());
						
						if(ponto != null && ponto.getCdPonto() == 0){
							ponto.setCdReferencia(referencia.getCdReferencia());
							PontoServices.save(ponto);
						}
					}
					
					if(numeroGrupoParadaExists(grupoParada.getNmGrupoParada())) {
						return new Result(-1, "Erro ao salvar. Praça já existe!");				
					} else {				
						retorno = GrupoParadaDAO.insert(grupoParada, connect);
						grupoParada.setCdGrupoParada(retorno);
					}
				}else {
					
					//mob_parada -> geo_referencia -> geo_ponto
					if (referencia != null && referencia.getCdReferencia() > 0){
						ReferenciaDAO.update(referencia, connect);
						grupoParada.setCdGeorreferencia(referencia.getCdReferencia());
						
						if(ponto != null && ponto.getCdPonto() > 0){
							ponto.setCdReferencia(referencia.getCdReferencia());
							PontoDAO.update(ponto, connect);
						}
					} else if (referencia != null && referencia.getCdReferencia() == 0){
							ReferenciaServices.save(referencia);
							grupoParada.setCdGeorreferencia(referencia.getCdReferencia());
							
							if(ponto != null && ponto.getCdPonto() == 0){
								ponto.setCdReferencia(referencia.getCdReferencia());
								PontoServices.save(ponto);
							}
					}
					
					retorno = GrupoParadaDAO.update(grupoParada, connect);
				}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "GRUPOPARADA", grupoParada);
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
	
	public static boolean numeroGrupoParadaExists(String nmGrupoParada) {
		return numeroGrupoParadaExists(nmGrupoParada, null);
	}
	public static boolean numeroGrupoParadaExists(String nmGrupoParada, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;		
		try {

			String sql =("SELECT nm_grupo_parada FROM mob_grupo_parada " +
						 "WHERE nm_grupo_parada = ? AND tp_grupo_parada = " + TP_GRUPO_PRACA_TAXI);
			
			pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, nmGrupoParada);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			return rsm.next();

		} catch (Exception e) {
			System.out.println("Erro!" + e.getMessage());
			return false;
		}
	}
	
	public static Result remove(int cdGrupoParada){
		return remove(cdGrupoParada, false, null);
	}
	public static Result remove(int cdGrupoParada, boolean cascade){
		return remove(cdGrupoParada, cascade, null);
	}
	public static Result remove(int cdGrupoParada, boolean cascade, Connection connect){
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
			retorno = GrupoParadaDAO.delete(cdGrupoParada, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e nãoo pode ser excluí­do!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro excluí­do com sucesso!");
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
	
	public static ResultSetMap getAllPracaTaxi() {
		return getAll(TP_GRUPO_PRACA_TAXI);
	}
	
	public static ResultSetMap getAllTerminalTransbordo() {
		return getAll(TP_GRUPO_TERMINAL_TRANSBORDO);
	}
	public static ResultSetMap getAllTerminal() {
		return getAll(TP_GRUPO_TERMINAL);
	}
	
	public static ResultSetMap getAllPlataformas() {
		return getAll(TP_GRUPO_PLATAFORMA);
	}
	
	public static ResultSetMap getAll() {
		return getAll(0, null);
	}

	public static ResultSetMap getAll(int tpGrupoParada) {
		return getAll(tpGrupoParada, null);
	}
	
	public static ResultSetMap getAll(Connection connect) {
		return getAll(0, connect);
	}
	public static ResultSetMap getAll(int tpGrupoParada, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			pstmt = connect.prepareStatement("SELECT A.*, A.nm_grupo_parada AS nr_ponto, "+
											 "B.nm_grupo_parada AS nm_grupo_parada_superior, " +
											 "C.cd_referencia, D.cd_ponto, D.vl_latitude, D.vl_longitude "+
											 "FROM mob_grupo_parada A " +
											 "LEFT JOIN mob_grupo_parada     B ON (A.cd_grupo_parada_superior = B.cd_grupo_parada) " +
											 "LEFT OUTER JOIN geo_referencia C ON (A.cd_georreferencia = C.cd_referencia) "+
											 "LEFT OUTER JOIN geo_ponto      D ON (C.cd_referencia = D.cd_referencia) "+
											(tpGrupoParada >= 0 ? "WHERE A.tp_grupo_parada = " + tpGrupoParada : "") + 
											" ORDER BY A.nm_grupo_parada ");
			
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}	
	
	public static ResultSetMap getAllByGrupoSuperior(int cdGrupoParadaSuperior) {
		return getAllByGrupoSuperior(cdGrupoParadaSuperior, null);
	}

	public static ResultSetMap getAllByGrupoSuperior(int cdGrupoParadaSuperior, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_grupo_parada WHERE cd_grupo_parada_superior = "+cdGrupoParadaSuperior);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaServices.getAllByGrupoSuperior: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaServices.getAllByGrupoSuperior: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static ResultSetMap getSyncData() {
		return getSyncData(null);
	}

	public static ResultSetMap getSyncData(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			String sql = "SELECT * FROM mob_grupo_parada";
					
//			if(Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1"))
//				sql = "SELECT * FROM mob_grupo_parada";
			
			pstmt = connect.prepareStatement(sql);
			
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

	
	public static HashMap<String, Object> getSync() {
		return getSync(null);
	}

	public static HashMap<String, Object> getSync(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt1;
		PreparedStatement pstmt2;
		try {			

			String sql = "SELECT * FROM mob_grupo_parada ORDER BY cd_grupo_parada";	
			pstmt1 = connect.prepareStatement(sql);

			sql = "SELECT * FROM mob_parada ORDER BY cd_parada";	
			pstmt2 = connect.prepareStatement(sql);

			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("GrupoParada", Util.resultSetToArrayList(pstmt1.executeQuery()));
			register.put("Parada", Util.resultSetToArrayList(pstmt2.executeQuery()));
			
			return register;
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
		try {
			ResultSetMap rsm = Search.find("SELECT A.*, A.ds_referencia AS NR_ORDEM, B.*, B.nm_grupo_parada AS NR_PONTO, "+
							   " C.tp_concessao, D.nm_grupo_parada AS nm_grupo_parada_superior, "+
							   " D.tp_grupo_parada AS tp_grupo_parada_superior, "+
							   " E.nm_logradouro, F.nm_tipo_logradouro, "+
							   " F.nm_tipo_logradouro || ' ' || E.nm_logradouro AS ds_logradouro " +
							   "FROM mob_parada A " +
						       "LEFT OUTER JOIN mob_grupo_parada 	B ON (A.cd_grupo_parada = B.cd_grupo_parada) "+
						       "LEFT OUTER JOIN mob_concessao    	C ON (A.cd_concessao = C.cd_concessao) " +
						       "LEFT JOIN mob_grupo_parada       	D ON (B.cd_grupo_parada_superior = D.cd_grupo_parada) " +
						       "LEFT OUTER JOIN grl_logradouro   	E ON (A.cd_logradouro = E.cd_logradouro)"+
						       "LEFT OUTER JOIN grl_tipo_logradouro F ON (E.cd_tipo_logradouro = F.cd_tipo_logradouro) ", 
						       "ORDER BY A.ds_referencia, B.cd_grupo_parada, C.cd_concessao ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);

			while(rsm.next()){
				rsm.setValueToField("DS_NM_GRUPO_PARADA", GrupoParadaServices.tiposGruposParadas[rsm.getInt("tp_grupo_parada")].toUpperCase() + " " + rsm.getString("nm_grupo_parada"));
				rsm.setValueToField("DS_NM_GRUPO_PARADA_SUPERIOR", (rsm.getString("nm_grupo_parada_superior") != null ? 
																	GrupoParadaServices.tiposGruposParadas[rsm.getInt("tp_grupo_parada_superior")].toUpperCase() + " " + rsm.getString("nm_grupo_parada_superior") : ""));
				rsm.setValueToField("DS_NM_GRUPO", GrupoParadaServices.tiposGruposParadas[rsm.getInt("tp_grupo_parada_superior")].toUpperCase() + " " + rsm.getString("nm_grupo_parada_superior")  + " - " +
						   GrupoParadaServices.tiposGruposParadas[rsm.getInt("tp_grupo_parada")].toUpperCase() + " " + rsm.getString("nm_grupo_parada") );
			}
			
			rsm.beforeFirst();
			

			return rsm;
			 
		}		
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaServices.getAllByGrupoSuperior: " + e);
			return null;
		}
	}
	
	public static ResultSetMap findPracaTaxi(ArrayList<ItemComparator> criterios) {
		return findPracaTaxi(criterios, null);
	}
	
	public static ResultSetMap findPracaTaxi(ArrayList<ItemComparator> criterios, Connection connect) {
		try {
			ResultSetMap rsm = Search.find("SELECT cd_grupo_parada, nm_grupo_parada, qt_vagas FROM mob_grupo_parada", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			
			return rsm;
		} catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoParada: " + e);
			return null;
		}
	}

}
