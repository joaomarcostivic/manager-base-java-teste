package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
import com.tivic.manager.grl.Logradouro;
import com.tivic.manager.grl.LogradouroDAO;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.Util;

public class ParadaServices {

	public static Result save(Parada parada){
		return save(parada, null, null);
	}
	
	public static Result save(Parada parada, GrupoParada grupoParada) {
		return save(parada, null, null, grupoParada, null);
	}

	public static Result save(Parada parada, Connection connect){
		return save(parada, null, null, null, connect);
	}
	
	public static Result save(Parada parada, Ponto ponto, Referencia referencia){
		return save(parada, ponto, referencia, null, null);
	}
	
	public static Result save(Parada parada, Ponto ponto, Referencia referencia, GrupoParada grupoParada, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(parada==null)
				return new Result(-1, "Erro ao salvar. Parada é nulo");			

			int retorno;
				
			if(parada.getCdParada()==0){
				//mob_parada -> geo_referencia -> geo_ponto
				if (referencia != null && referencia.getCdReferencia() == 0){
					ReferenciaServices.save(referencia);
					parada.setCdGeorreferencia(referencia.getCdReferencia());
					
					if(ponto != null && ponto.getCdPonto() == 0){
						ponto.setCdReferencia(referencia.getCdReferencia());
						PontoServices.save(ponto);
					}
				} if( grupoParada != null && grupoParada.getQtVagas() > 0) {
					grupoParada.setQtVagas(grupoParada.getQtVagas() - 1);
					grupoParada.setNmGrupoParada(grupoParada.getNmGrupoParada());
					grupoParada.setTpGrupoParada(GrupoParadaServices.TP_GRUPO_PRACA_TAXI);
					
						GrupoParadaServices.save(grupoParada);
					
				}
				if(numeroOrdemExists(parada.getDsReferencia(), grupoParada.getNmGrupoParada())) {
					return new Result(-1, "Erro ao salvar. Número de ordem já existe!");				
				} else {			
						retorno = ParadaDAO.insert(parada, connect);
						parada.setCdParada(retorno);
					}
			}
			else {
				//mob_parada -> geo_referencia -> geo_ponto
				if (referencia != null && referencia.getCdReferencia() > 0){
					ReferenciaDAO.update(referencia, connect);
					parada.setCdGeorreferencia(referencia.getCdReferencia());
					
					if(ponto != null && ponto.getCdPonto() > 0){
						ponto.setCdReferencia(referencia.getCdReferencia());
						PontoDAO.update(ponto, connect);
					}
				}else{
					if (referencia!=null && referencia.getCdReferencia() == 0){
						ReferenciaServices.save(referencia);
						parada.setCdGeorreferencia(referencia.getCdReferencia());
						
						if(ponto != null && ponto.getCdPonto() == 0){
							ponto.setCdReferencia(referencia.getCdReferencia());
							PontoServices.save(ponto);
						}
					}
				}
					
				retorno = ParadaDAO.update(parada, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PARADA", parada);
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
	
	
	
	public static boolean numeroOrdemExists(String nmGrupoParada, String dsReferencia) {
		return numeroOrdemExists(nmGrupoParada, dsReferencia, null);
	}
	public static boolean numeroOrdemExists(String nmGrupoParada,  String dsReferencia, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;		
		try {

			String sql =("SELECT A.ds_referencia, B.nm_grupo_parada FROM mob_parada A " + 
					"JOIN mob_grupo_parada B ON (A.CD_GRUPO_PARADA = B.CD_GRUPO_PARADA) " +
					"WHERE A.ds_referencia = ? OR B.nm_grupo_parada = ?  AND B.tp_grupo_parada = " + GrupoParadaServices.TP_GRUPO_PRACA_TAXI);
			
			pstmt = connect.prepareStatement(sql);
			pstmt.setString(1, dsReferencia);
			pstmt.setString(2, nmGrupoParada);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			return rsm.next();

		} catch (Exception e) {
			System.out.println("Erro!" + e.getMessage());
			return false;
		}
	}
	
	
	public static Result remove(int cdParada){
		return remove(cdParada, false, null);
	}
	public static Result remove(int cdParada, boolean cascade){
		return remove(cdParada, cascade, null);
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
			
			String sql = "SELECT * FROM mob_parada";
					
			if(Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1"))
				sql = "SELECT * FROM mob_parada";
			
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
	
	public static Result remove(int cdParada, boolean cascade, Connection connect){
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
			retorno = ParadaDAO.delete(cdParada, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro está vinculado a outros e nãoo pode ser excluído!");
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
	
	public static String getNrOrdem(int cdConcessao) {		
		
		Criterios crt = new Criterios();
		crt.add("C.cd_concessao", Integer.toString(cdConcessao), ItemComparator.EQUAL, Types.INTEGER);
		
		ResultSetMap rsm = find(crt);
		if (rsm!=null && rsm.next())
			return rsm.getString("NR_ORDEM");
			
		return "";
	}
	
	public static int setConcessao(int cdParada, int cdConcessao, Connection connect) {		
		
		Parada parada = ParadaDAO.get(cdParada);
		parada.setCdConcessao(cdConcessao);
		
		return ParadaDAO.insert(parada, connect);
	}
	
	public static String getNrPonto(int cdConcessao) {		
		
		Criterios crt = new Criterios();
		crt.add("C.cd_concessao", Integer.toString(cdConcessao), ItemComparator.EQUAL, Types.INTEGER);
		
		ResultSetMap rsm = find(crt);
		if (rsm!=null && rsm.next())
			return rsm.getString("NR_PONTO");
			
		return "";
	}
	
	public static Parada getByConcessao(int cdConcessao) {
		Criterios criterios = new Criterios();
		criterios.add("cd_concessao", Integer.toString(cdConcessao), ItemComparator.EQUAL, Types.INTEGER);

		ResultSetMap rsm = ParadaDAO.find(criterios);
		if (rsm!=null && rsm.next())
			return ParadaDAO.get(rsm.getInt("CD_PARADA"));
		
		return null;
	}
	
	public static ResultSetMap getAllByPontoTaxi() {
		return getAll(GrupoParadaServices.TP_GRUPO_PRACA_TAXI, null);
	}
	public static ResultSetMap getAllByPontoTaxi(ArrayList<ItemComparator> criterios) {
		return getAll(GrupoParadaServices.TP_GRUPO_PRACA_TAXI, null);
	}
	
	public static ResultSetMap getAllByTerminalTransbordo() {		
		return getAll(GrupoParadaServices.TP_GRUPO_TERMINAL_TRANSBORDO, null);
	}
	
	public static ResultSetMap getAllByTerminal() {		
		return getAll(GrupoParadaServices.TP_GRUPO_TERMINAL, null);
	}
	
	public static ResultSetMap getAllByPlataforma() {		
		return getAll(GrupoParadaServices.TP_GRUPO_PLATAFORMA, null);
	}
	
	public static ResultSetMap getAll() {
		return getAll(null);
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
			
			String sql = "SELECT A.*, A.ds_referencia AS nr_ordem, "+
					 "B.cd_tipo_logradouro, B.nm_logradouro, B1.nm_tipo_logradouro, "+
					 "B1.nm_tipo_logradouro || ' ' || B.nm_logradouro AS ds_logradouro, " +
					 "C.nm_grupo_parada, C.nm_grupo_parada AS nr_ponto, C.tp_grupo_parada, "+
					 "D.cd_referencia, E.cd_ponto, E.vl_latitude, E.vl_longitude, "+
					 "G.nm_pessoa AS NM_CONCESSIONARIO, "+
					 "H.nm_grupo_parada AS nm_grupo_parada_superior, H.tp_grupo_parada AS tp_grupo_parada_superior "+
					 "  FROM mob_parada A "+
					 "LEFT OUTER JOIN grl_logradouro  	  B ON (A.cd_logradouro = B.cd_logradouro) "+
					 "LEFT OUTER JOIN grl_tipo_logradouro B1 ON (B.cd_tipo_logradouro = B1.cd_tipo_logradouro) " +
					 "LEFT OUTER JOIN mob_grupo_parada 	  C ON (A.cd_grupo_parada = C.cd_grupo_parada) " +
					 "LEFT OUTER JOIN geo_referencia      D ON (A.cd_georreferencia = D.cd_referencia) "+
					 "LEFT OUTER JOIN geo_ponto           E ON (D.cd_referencia = E.cd_referencia) "+
					 "LEFT OUTER JOIN mob_concessao       F ON (A.cd_concessao = F.cd_concessao) "+
					 "LEFT OUTER JOIN grl_pessoa          G ON (F.cd_concessionario = G.cd_pessoa) " +
					 "LEFT JOIN mob_grupo_parada          H ON (C.cd_grupo_parada_superior = H.cd_grupo_parada) "+
					 (tpGrupoParada >= 0 ? "WHERE C.tp_grupo_parada = " + tpGrupoParada : "") + 
					 " ORDER BY A.ds_referencia, C.cd_grupo_parada, F.cd_concessao ";
			
			pstmt = connect.prepareStatement(sql);

			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()){
				rsm.setValueToField("DS_NM_GRUPO_PARADA", GrupoParadaServices.tiposGruposParadas[rsm.getInt("tp_grupo_parada")].toUpperCase() + " " + rsm.getString("nm_grupo_parada"));
				rsm.setValueToField("DS_NM_GRUPO_PARADA_SUPERIOR", (rsm.getString("nm_grupo_parada_superior") != null ? 
																	GrupoParadaServices.tiposGruposParadas[rsm.getInt("tp_grupo_parada_superior")].toUpperCase() + " " + rsm.getString("nm_grupo_parada_superior") : ""));
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParadaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParadaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static GrupoParada getGrupoParada(int cdConcessao) {
		Criterios crt = new Criterios();
		crt.add("C.tp_concessao", Integer.toString(ConcessaoServices.TP_TAXI), ItemComparator.EQUAL, Types.INTEGER);
		crt.add("A.cd_concessao", Integer.toString(cdConcessao), ItemComparator.EQUAL, Types.INTEGER);

		GrupoParada grupoParada = new GrupoParada();
		
		ResultSetMap rsm = find(crt);
		if (rsm!=null && rsm.next())
			grupoParada = GrupoParadaDAO.get(rsm.getInt("CD_GRUPO_PARADA"));
		
		return grupoParada;
	}
	
	public static ResultSetMap getAllByGrupoParada(int cdGrupoParada) {
		return getAllByGrupoParada(cdGrupoParada, null);
	}

	public static ResultSetMap getAllByGrupoParada(int cdGrupoParada, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_parada WHERE cd_grupo_parada "+(cdGrupoParada==-1 ? " is null " :" = "+cdGrupoParada));
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParadaServices.getAllByGrupoParada: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParadaServices.getAllByGrupoParada: " + e);
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
							   " F.nm_tipo_logradouro || ' ' || E.nm_logradouro AS ds_logradouro, " +
							   "G.nm_pessoa AS NM_CONCESSIONARIO, C.cd_concessionario, G.cd_pessoa "+
							   "FROM mob_parada A " +
						       "LEFT OUTER JOIN mob_grupo_parada 	B ON (A.cd_grupo_parada = B.cd_grupo_parada) "+
						       "LEFT OUTER JOIN mob_concessao    	C ON (A.cd_concessao = C.cd_concessao) " +
						       "LEFT JOIN mob_grupo_parada       	D ON (B.cd_grupo_parada_superior = D.cd_grupo_parada) " +
						       "LEFT OUTER JOIN grl_logradouro   	E ON (A.cd_logradouro = E.cd_logradouro)"+
						       "LEFT OUTER JOIN grl_tipo_logradouro F ON (E.cd_tipo_logradouro = F.cd_tipo_logradouro) " +
					           "LEFT OUTER JOIN grl_pessoa          G ON (C.cd_concessionario = G.cd_pessoa) ", 
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
		}catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParadaServices.find: " + e);
			return null;
		}
	}
	
	public static  List<ParadaDTO> findDTO(ArrayList<ItemComparator> criterios) throws Exception{
		return findDTO(criterios, null);
	}
	
	public static List<ParadaDTO> findDTO(ArrayList<ItemComparator> criterios, Connection connect){
		ResultSetMap rsm = find(criterios);
		
		List<ParadaDTO> list = new ArrayList<ParadaDTO>();

		while(rsm.next()) {
			
			
			ParadaDTO dto = new ParadaDTO();
			
			Parada parada = ParadaDAO.get(rsm.getInt("CD_PARADA"));
			dto.setParada(parada);
			
			GrupoParada grupoParada = GrupoParadaDAO.get(rsm.getInt("CD_GRUPO_PARADA"));
			dto.setGrupoParada(grupoParada);
			
			Concessao concessao = ConcessaoDAO.get(rsm.getInt("CD_CONCESSAO"));
			dto.setConcessao(concessao);
			
			Logradouro logradouro = LogradouroDAO.get(rsm.getInt("CD_LOGRADOURO"));
			dto.setLogradouro(logradouro);
			
			Pessoa pessoa = PessoaDAO.get(rsm.getInt("CD_PESSOA"));
			dto.setPessoa(pessoa);
			
			
			list.add(dto);
		}
		
		
		return list;
	}

	
	public static ResultSetMap getHierarquia(int cdGrupoParada) {
		return getHierarquia(cdGrupoParada, null);
	}

	public static ResultSetMap getHierarquia(int cdGrupoParada, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {

			//GRUPOS DE PARADA
			pstmt = connect.prepareStatement("SELECT * FROM mob_grupo_parada WHERE cd_grupo_parada_superior "+(cdGrupoParada==-1 ? " IS NULL" : " = "+cdGrupoParada));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()) {
				rsm.setValueToField("HIERARQUIA_PARADA", getHierarquia(rsm.getInt("CD_GRUPO_PARADA"), connect));
			}
			
			//PARADAS
			ResultSetMap rsmParadas = getAllByGrupoParada(cdGrupoParada, connect);
			while(rsmParadas.next()) {
				rsm.addRegister(rsmParadas.getRegister());
			}
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaServices.getHierarquia: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! GrupoParadaServices.getHierarquia: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getLocalizacao(int cdParada) {
		return getLocalizacao(cdParada, null);
	}

	public static ResultSetMap getLocalizacao(int cdParada, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.*, C.vl_latitude, C.vl_longitude " +
																  "FROM mob_parada A " +
																  "JOIN geo_referencia B ON (A.cd_georreferencia = B.cd_referencia) " +
																  "JOIN geo_ponto C ON (B.cd_referencia = C.cd_referencia) " +
																  "WHERE A.cd_parada = ? ");
			pstmt.setInt(1, cdParada);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
}
