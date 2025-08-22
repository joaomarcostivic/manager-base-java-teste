package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.agd.AgendaItemServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

public class LinhaTrechoServices {

	public static Result save(LinhaTrecho linhaTrecho){
		return save(linhaTrecho, null, null);
	}

	public static Result save(LinhaTrecho linhaTrecho, AuthData authData){
		return save(linhaTrecho, authData, null);
	}

	public static Result save(LinhaTrecho linhaTrecho, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(linhaTrecho==null)
				return new Result(-1, "Erro ao salvar. LinhaTrecho é nulo");

			int retorno;
			if(linhaTrecho.getCdTrecho()==0){
				retorno = LinhaTrechoDAO.insert(linhaTrecho, connect);
				linhaTrecho.setCdTrecho(retorno);
			}
			else {
				retorno = LinhaTrechoDAO.update(linhaTrecho, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "LINHATRECHO", linhaTrecho);
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
	
	public static ResultSetMap getSyncData() {
		return getSyncData(null);
	}

	public static ResultSetMap getSyncData(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			String sql = "SELECT * FROM mob_linha_trecho";
					
			if(Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1"))
				sql = "SELECT * FROM mob_linha_trecho";
			
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
	
	public static Result saveTrechos(ArrayList<LinhaTrecho> linhasTrecho){
		return saveTrechos(linhasTrecho, null, null);
	}

	public static Result saveTrechos(ArrayList<LinhaTrecho> linhasTrecho, AuthData authData){
		return saveTrechos(linhasTrecho, authData, null);
	}
	
	public static Result saveTrechos(ArrayList<LinhaTrecho> linhasTrecho, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int retorno = 0;
			
			if(linhasTrecho==null)
				return new Result(-1, "Erro ao salvar. LinhaTrecho é nulo");

			for (LinhaTrecho linhaTrecho : linhasTrecho) {
				retorno = save(linhaTrecho, null, connect).getCode();
				
				if (retorno<0)
					break;
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno);
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
	
	public static Result saveTrechosOrdenando(int cdLinha, int cdRota, ResultSetMap rsmLinhaTrecho){
		return saveTrechosOrdenando(cdLinha, cdRota, rsmLinhaTrecho, null, null);
	}

	public static Result saveTrechosOrdenando(int cdLinha, int cdRota, ResultSetMap rsmLinhaTrecho, AuthData authData){
		return saveTrechosOrdenando(cdLinha, cdRota, rsmLinhaTrecho, authData, null);
	}
	
	public static Result saveTrechosOrdenando(int cdLinha, int cdRota, ResultSetMap rsmLinhaTrecho, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int retorno = 0;
			int lgDeletado = 0;
			
			if(rsmLinhaTrecho==null)
				return new Result(-1, "Erro ao salvar. LinhaTrecho é nulo");

			lgDeletado = LinhaTrechoDAO.deleteItinerario(cdLinha, cdRota, connect);
			
			if (lgDeletado>0) {
				rsmLinhaTrecho.beforeFirst();
				while (rsmLinhaTrecho.next()) {
					
					LinhaTrecho linhaTrecho = new LinhaTrecho();
					linhaTrecho.setCdLinha(cdLinha);
					linhaTrecho.setCdRota(cdRota);
					
					linhaTrecho.setCdParada(rsmLinhaTrecho.getInt("CD_PARADA"));
					linhaTrecho.setQtKm(0.0);
					linhaTrecho.setCdTrechoAnterior(retorno);
					
					retorno = save(linhaTrecho, null, connect).getCode();
					
					if (retorno<0)
						break;
				}
			}
			

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno);
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
	
	public static Result saveTrechosOrdenandoPorList(int cdLinha, int cdRota, List<LinhaTrecho> linhaTrechoList){
		return saveTrechosOrdenandoPorList(cdLinha, cdRota, linhaTrechoList, null, null);
	}

	public static Result saveTrechosOrdenandoPorList(int cdLinha, int cdRota, List<LinhaTrecho> linhaTrechoList, AuthData authData){
		return saveTrechosOrdenandoPorList(cdLinha, cdRota, linhaTrechoList, authData, null);
	}
	
	public static Result saveTrechosOrdenandoPorList(int cdLinha, int cdRota, List<LinhaTrecho> linhaTrechoList, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			int retorno = 0;
			int lgDeletado = 0;
			
			if(linhaTrechoList==null)
				return new Result(-1, "Erro ao salvar. LinhaTrecho é nulo");
			
			ResultSetMap rsmTrechosCadastrados = LinhaRotaServices.getParadasOrdenadas(cdLinha, cdRota, true);
			
			rsmTrechosCadastrados.beforeFirst();
			
			if(linhaTrechoList.size() == rsmTrechosCadastrados.size()) {
				int count = 0;
				while(rsmTrechosCadastrados.next()) {
					if(rsmTrechosCadastrados.getInt("CD_TRECHO") != linhaTrechoList.get(count).getCdTrecho()) {
						if(HorarioServices.validaExistenciaHorarios(cdLinha, cdRota)) {
							return new Result(-1, "N�o � possivel editar a rota com hor�rios cadastrados.");
						}else {
							break;
						}
					}
					count++;
				}
			}else {
				if(HorarioServices.validaExistenciaHorarios(cdLinha, cdRota)) 
					return new Result(-1, "N�o � possivel editar a rota com hor�rios cadastrados.");
			}
				

			lgDeletado = LinhaTrechoDAO.deleteItinerario(cdLinha, cdRota, connect);
			
			if (lgDeletado>0) {
				for(LinhaTrecho ln: linhaTrechoList) {
					
					LinhaTrecho linhaTrecho = new LinhaTrecho();
					linhaTrecho.setCdLinha(cdLinha);
					linhaTrecho.setCdRota(cdRota);
					
					linhaTrecho.setCdParada(ln.getCdParada());
					linhaTrecho.setQtKm(0.0);
					linhaTrecho.setCdTrechoAnterior(retorno);
					
					retorno = save(linhaTrecho, null, connect).getCode();
					
					if (retorno<0)
						break;
				}
			}
			

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno);
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
	
	public static Result remove(int cdLinha, int cdRota, int cdTrecho){
		return remove(cdLinha, cdRota, cdTrecho, false, null, null);
	}
	public static Result remove(int cdLinha, int cdRota, int cdTrecho, boolean cascade){
		return remove(cdLinha, cdRota, cdTrecho, cascade, null, null);
	}
	public static Result remove(int cdLinha, int cdRota, int cdTrecho, boolean cascade, AuthData authData){
		return remove(cdLinha, cdRota, cdTrecho, cascade, authData, null);
	}
	public static Result remove(int cdLinha, int cdRota, int cdTrecho, boolean cascade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		PreparedStatement pstmt;
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
			
			pstmt = connect.prepareStatement("SELECT * FROM mob_linha_trecho WHERE cd_linha = " + cdLinha + " AND cd_rota = " + cdRota + " AND cd_trecho_anterior = " + cdTrecho);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next()) {
				LinhaTrecho trechoAtual = LinhaTrechoDAO.get(cdLinha, cdRota, cdTrecho, connect);
				LinhaTrecho trechoAnterior = LinhaTrechoDAO.get(rsm.getInt("CD_LINHA"), rsm.getInt("CD_ROTA"), rsm.getInt("CD_TRECHO"), connect);
				
				trechoAnterior.setCdTrechoAnterior(trechoAtual.getCdTrechoAnterior());
				retorno = LinhaTrechoServices.save(trechoAnterior, null, connect).getCode();
			}
			
			if(!cascade || retorno>0)
			retorno = LinhaTrechoDAO.delete(cdLinha, cdRota, cdTrecho, connect);
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
			pstmt = connect.prepareStatement("SELECT A.*, B.cd_linha, B.nr_linha, C.cd_rota, C.nm_rota, "+
											 "       D.ds_referencia, E.nm_logradouro, F.nm_tipo_logradouro,  "+
											 "       D1.nm_grupo_parada, D1.tp_grupo_parada, "+
											 "       H.vl_latitude, H.vl_longitude  "+
											 "FROM mob_linha_trecho A "+
											 "LEFT OUTER JOIN mob_linha             B  ON (A.cd_linha = B.cd_linha) "+
											 "LEFT OUTER JOIN mob_linha_rota        C  ON (A.cd_rota  = C.cd_rota "+
											 " 										   AND B.cd_linha = C.cd_linha)"+
											 "LEFT OUTER JOIN mob_parada     	    D  ON (A.cd_parada = D.cd_parada) "+
											 "LEFT OUTER JOIN mob_grupo_parada      D1 ON (D.cd_grupo_parada = D1.cd_grupo_parada) "+
											 "LEFT OUTER JOIN grl_logradouro 	    E  ON (D.cd_logradouro = E.cd_logradouro) "+
											 "LEFT OUTER JOIN grl_tipo_logradouro   F  ON (E.cd_tipo_logradouro = F.cd_tipo_logradouro) "+
											 //Georreferencia da parada
											 "LEFT OUTER JOIN geo_referencia        G  ON (D.cd_georreferencia = G.cd_referencia) "+
											 "LEFT OUTER JOIN geo_ponto             H  ON (G.cd_referencia = H.cd_referencia) ");
				//return new ResultSetMap(pstmt.executeQuery());
				
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				
				while(rsm.next())
					rsm.setValueToField("nm_grupo", GrupoParadaServices.tiposGruposParadas[rsm.getInt("tp_grupo_parada")].toUpperCase() + " " + rsm.getString("nm_grupo_parada") );
				
				rsm.beforeFirst();
				
				return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaTrechoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaTrechoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getByRota(int cdLinha, int cdRota) {
		return getByRota(cdLinha, cdRota, null);
	}

	public static ResultSetMap getByRota(int cdLinha, int cdRota, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*," +
											" A.cd_trecho AS cd_trecho_partida," +
											" A2.nm_grupo_parada AS nm_grupo_parada_partida," +
											" A3.nm_grupo_parada AS nm_grupo_parada_partida_superior," +
											" B.cd_trecho as cd_trecho_chegada," +
											" B2.nm_grupo_parada AS nm_grupo_parada_chegada," +
											" B3.nm_grupo_parada AS nm_grupo_parada_chegada_superior" +
											
											" FROM mob_linha_trecho A" +
											" JOIN mob_linha_trecho B ON (A.cd_linha = B.cd_linha AND A.cd_rota = B.cd_rota AND A.cd_trecho = B.cd_trecho_anterior)" +
											
											" LEFT OUTER JOIN mob_parada     	  A1 ON (A.cd_parada = A1.cd_parada)" +
											" LEFT OUTER JOIN mob_grupo_parada    A2 ON (A1.cd_grupo_parada = A2.cd_grupo_parada)" +
											" LEFT OUTER JOIN mob_grupo_parada    A3 ON (A2.cd_grupo_parada_superior = A3.cd_grupo_parada)" +
											
											" LEFT OUTER JOIN mob_parada     	  B1 ON (B.cd_parada = B1.cd_parada)" +
											" LEFT OUTER JOIN mob_grupo_parada    B2 ON (B1.cd_grupo_parada = B2.cd_grupo_parada)" +
											" LEFT OUTER JOIN mob_grupo_parada    B3 ON (B2.cd_grupo_parada_superior = B3.cd_grupo_parada)" +
											" WHERE A.cd_linha = " + cdLinha + " AND A.cd_rota = " + cdRota +
											" ORDER BY A.cd_trecho");
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next())
				rsm.setValueToField("NM_TRECHO", rsm.getString("NM_GRUPO_PARADA_PARTIDA_SUPERIOR") + " - " + rsm.getString("NM_GRUPO_PARADA_CHEGADA_SUPERIOR"));
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaTrechoServices.getByRota: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaTrechoServices.getByLinhaRota: " + e);
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
			
			String sql = "SELECT A.*, B.cd_linha, B.nr_linha, C.cd_rota, C.nm_rota, "+
						 "       D.ds_referencia, E.nm_logradouro, F.nm_tipo_logradouro,  "+
						 "       D1.nm_grupo_parada, D1.tp_grupo_parada, D1.cd_grupo_parada_superior, "+
						 "       D2.nm_grupo_parada AS nm_grupo_parada_superior, D2.tp_grupo_parada AS tp_grupo_parada_superior, "+
						 "       H.vl_latitude, H.vl_longitude  "+
						 "FROM mob_linha_trecho A "+

						 "LEFT OUTER JOIN mob_linha             B  ON (A.cd_linha = B.cd_linha) "+
						 "LEFT OUTER JOIN mob_linha_rota        C  ON (A.cd_rota  = C.cd_rota "+
						 " 									       AND B.cd_linha = C.cd_linha)"+
						 "LEFT OUTER JOIN mob_parada     	    D  ON (A.cd_parada = D.cd_parada) "+
						 "LEFT OUTER JOIN mob_grupo_parada      D1 ON (D.cd_grupo_parada = D1.cd_grupo_parada) "+
						 "LEFT OUTER JOIN mob_grupo_parada      D2 ON (D1.cd_grupo_parada_superior = D2.cd_grupo_parada) "+
						 "LEFT OUTER JOIN grl_logradouro 	    E  ON (D.cd_logradouro = E.cd_logradouro) "+
						 "LEFT OUTER JOIN grl_tipo_logradouro   F  ON (E.cd_tipo_logradouro = F.cd_tipo_logradouro) "+
						 //Georreferencia da parada
						 "LEFT OUTER JOIN geo_referencia        G  ON (D.cd_georreferencia = G.cd_referencia) "+
						 "LEFT OUTER JOIN geo_ponto             H  ON (G.cd_referencia = H.cd_referencia) ";
			
			ResultSetMap rsm = Search.find(sql, " ORDER BY A.cd_trecho ", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			while(rsm.next()){
				rsm.setValueToField("nm_grupo", GrupoParadaServices.tiposGruposParadas[rsm.getInt("tp_grupo_parada")].toUpperCase() + " " + rsm.getString("nm_grupo_parada") );
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
			System.err.println("Erro! LinhaTrechoServices.find: " + e);
			return null;
		}		
	}
	
	public static ResultSetMap getLocalizacaoByLinhaRota(int cdLinha, int cdRota) {
		return getLocalizacaoByLinhaRota(cdLinha, cdRota, null);
	}

	public static ResultSetMap getLocalizacaoByLinhaRota(int cdLinha, int cdRota, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement(
									  "SELECT A.*, B.cd_parada, B.ds_referencia, C.nm_grupo_parada, C.tp_grupo_parada, "+
									  "D.nm_grupo_parada AS nm_grupo_parada_superior, D.tp_grupo_parada AS tp_grupo_parada_superior " +		  
									  "FROM mob_linha_trecho A "+
									  "LEFT OUTER JOIN mob_parada B ON (A.cd_parada = B.cd_parada) "+
									  "LEFT OUTER JOIN mob_grupo_parada C ON (B.cd_grupo_parada = C.cd_grupo_parada) "+
									  "LEFT OUTER JOIN mob_grupo_parada D ON (C.cd_grupo_parada_superior = D.cd_grupo_parada) "+
									  "WHERE A.cd_linha = ? AND A.cd_rota = ? ");
			pstmt.setInt(1, cdLinha);
			pstmt.setInt(2, cdRota);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			while(rsm.next()){
				rsm.setValueToField("nm_grupo", GrupoParadaServices.tiposGruposParadas[rsm.getInt("tp_grupo_parada")].toUpperCase() + " " + rsm.getString("nm_grupo_parada") );				
				rsm.setValueToField("nm_grupo_superior", (rsm.getString("nm_grupo_parada_superior") != null ? 
														  GrupoParadaServices.tiposGruposParadas[rsm.getInt("tp_grupo_parada_superior")].toUpperCase() + " " + rsm.getString("nm_grupo_parada_superior") : ""));
				
				ResultSetMap rsmParadas = ParadaServices.getLocalizacao(rsm.getInt("cd_parada"));
				if(rsmParadas.next()){
					rsm.setValueToField("VL_LATITUDE", rsmParadas.getDouble("vl_latitude"));
					rsm.setValueToField("VL_LONGITUDE", rsmParadas.getDouble("vl_longitude"));
				}
			}
			
			rsm.beforeFirst();
			
			return rsm;
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaTrechoServices.getLocalizacaoByTrecho: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap saveAllLinhaTrecho(int cdLinha, int cdRota, ArrayList<LinhaTrecho> linhaTrechos) {
		return saveAllLinhaTrecho(cdLinha, cdRota, linhaTrechos, null);
	}

	public static ResultSetMap saveAllLinhaTrecho(int cdLinha, int cdRota, ArrayList<LinhaTrecho> linhaTrechos, Connection connect) {
		try {
			
			Criterios crt = new Criterios();
			crt.add("A.cd_linha", Integer.toString(cdLinha), ItemComparator.EQUAL, Types.INTEGER);				
			crt.add("A.cd_rota", Integer.toString(cdRota), ItemComparator.EQUAL, Types.INTEGER);				


			
			String sql = "SELECT * FROM mob_linha_trecho A";
			
			ResultSetMap rsm = Search.find(sql, " ORDER BY A.cd_trecho ", crt, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			rsm.beforeFirst();
			
			return rsm;
			 
		}catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaTrechoServices.find: " + e);
			return null;
		}		
	}

	
	
	
	
	
	
}