package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.mob.tabelashorarios.ParadaDTO;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class LinhaRotaServices {
	
	public static final int TP_IDA    = 0;
	public static final int TP_VOLTA  = 1;
	
	public static final String[] tipoLinhaRota = {"Ida","Volta"};
	
	public static final int ST_INATIVA = 0;
	public static final int ST_ATIVA   = 1;	
	
	public static final String[] situacaoLinhaRota = {"Inativa","Ativa"};
	
	public static Result save(LinhaRota linhaRota){
		return save(linhaRota, null, null);
	}

	public static Result save(LinhaRota linhaRota, AuthData authData){
		return save(linhaRota, authData, null);
	}

	public static Result save(LinhaRota linhaRota, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(linhaRota==null)
				return new Result(-1, "Erro ao salvar. LinhaRota é nulo");

			if(linhaRota.getCdLinha() <= 0)
				return new Result(-1, "Erro ao salvar. A linha não foi selecionada");
			
			int retorno;
			if(linhaRota.getCdRota()==0){
				retorno = LinhaRotaDAO.insert(linhaRota, connect);
				linhaRota.setCdRota(retorno);
			}
			else {
				retorno = LinhaRotaDAO.update(linhaRota, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "LINHAROTA", linhaRota);
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
			
			String sql = "SELECT * FROM mob_linha_rota";
					
			if(Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1"))
				sql = "SELECT * FROM mob_linha_rota";
			
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
	
	public static Result remove(int cdLinha, int cdRota){
		return remove(cdLinha, cdRota, false, null, null);
	}
	public static Result remove(int cdLinha, int cdRota, boolean cascade){
		return remove(cdLinha, cdRota, cascade, null, null);
	}
	public static Result remove(int cdLinha, int cdRota, boolean cascade, AuthData authData){
		return remove(cdLinha, cdRota, cascade, authData, null);
	}
	public static Result remove(int cdLinha, int cdRota, boolean cascade, AuthData authData, Connection connect){
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
			retorno = LinhaRotaDAO.delete(cdLinha, cdRota, connect);
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
		return getAll(null,false,null);
	}

	public static ResultSetMap getAll(String orderBy) {
		return getAll(orderBy,false,null);
	}
	
	public static ResultSetMap getLinhasAtivas(String orderBy) {
		return getAll(orderBy,true,null);
	}
	
	public static ResultSetMap getAll(String orderBy, Boolean stLinhaAtiva, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nr_linha, C.cd_concessao, C.cd_concessionario, D.nm_pessoa FROM mob_linha_rota A" +
											" LEFT OUTER JOIN mob_linha B ON (A.cd_linha = B.cd_linha)" +
											" LEFT OUTER JOIN mob_concessao C ON (B.cd_concessao = C.cd_concessao)" +
											" LEFT OUTER JOIN grl_pessoa    D ON (C.cd_concessionario = D.cd_pessoa)" +
											(stLinhaAtiva ? " WHERE B.st_linha = " + LinhaServices.ST_ATIVO : "" ) +
											(orderBy != null ? " ORDER BY "  + orderBy : ""));
				
			ResultSetMap rsmLinhaRota = new ResultSetMap(pstmt.executeQuery());
			
			while(rsmLinhaRota.next())
				rsmLinhaRota.setValueToField("NM_LINHA_ROTA", rsmLinhaRota.getString("NR_LINHA") + " - " + rsmLinhaRota.getString("NM_ROTA"));
			rsmLinhaRota.beforeFirst();
			
			return rsmLinhaRota;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaRotaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaRotaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getDetalhe(int cdLinha, int cdRota) {
		return getDetalhe(cdLinha, cdRota, null);
	}
	
	public static ResultSetMap getDetalhe(int cdLinha, int cdRota, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			// Buscar quantidade de veiculos por tipo de tabela de horario
			pstmt = connect.prepareStatement("select A.tp_tabela_horario, count(A.cd_tabela_horario) as nr_tabelas from mob_tabela_horario A" +
											" left outer join mob_tabela_horario_rota B on (A.cd_tabela_horario = B.cd_tabela_horario and A.cd_linha = B.cd_linha)" +
											" where A.cd_linha = " + cdLinha + " and B.cd_rota = " + cdRota +
											" group by A.tp_tabela_horario" +
											" order by A.tp_tabela_horario");
			ResultSetMap rsmTiposTabelas = new ResultSetMap(pstmt.executeQuery());
			
			while(rsmTiposTabelas.next()) {
		
				// Calculo da extensao da variacao
				pstmt = connect.prepareStatement("select sum(qt_km) as qt_km_total from mob_variacao where cd_variacao in" +
												" (select A.cd_variacao from mob_horario A" +
												" left outer join mob_tabela_horario B on (A.cd_tabela_horario = B.cd_tabela_horario and A.cd_linha = B.cd_linha)" +
												" where A.cd_linha = " + cdLinha + " and B.cd_rota = " + cdRota + " and A.cd_variacao <> 0 " +
												" and B.tp_tabela_horario = " + rsmTiposTabelas.getInt("TP_TABELA_HORARIO") +
												" group by A.cd_tabela_horario, A.cd_horario, A.cd_variacao)");
				ResultSetMap rsmVariacoes = new ResultSetMap(pstmt.executeQuery());
				if (rsmVariacoes.next())
					rsmTiposTabelas.setValueToField("KM_VARIACOES", rsmVariacoes.getDouble("QT_KM_TOTAL"));
				
				// Buscar o numero de viagens para cada tipo de tabela
				pstmt = connect.prepareStatement("select A.cd_horario from mob_horario A" +
												" join mob_tabela_horario_rota B on (A.cd_linha = B.cd_linha and A.cd_tabela_horario = B.cd_tabela_horario and A.cd_rota = B.cd_rota)" +
												" join mob_tabela_horario C on (B.cd_tabela_horario = C.cd_tabela_horario and B.cd_linha = C.cd_linha)" +
												" where A.cd_linha = " + cdLinha + " and B.cd_rota = " + cdRota + " and C.tp_tabela_horario = " + rsmTiposTabelas.getInt("TP_TABELA_HORARIO") +
												" group by A.cd_horario");

				ResultSetMap rsmHorarios = new ResultSetMap(pstmt.executeQuery());
				rsmTiposTabelas.setValueToField("NR_VIAGENS", rsmHorarios.getLines().size());
			}
			
			
			return rsmTiposTabelas;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaRotaServices.getDetalhe: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaRotaServices.getDetalhe: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Recebe o codigo da linha e da rota, busca os trechos e os ordena
	 * @param cdLinha
	 * @param cdRota
	 * @return
	 */
	public static ResultSetMap getParadasOrdenadas(int cdLinha, int cdRota) {
		return getParadasOrdenadas(cdLinha, cdRota, false);
	}
	
	public static ResultSetMap getParadasOrdenadas(int cdLinha, int cdRota, boolean lgOnlyTerminais) {
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.CD_LINHA", Integer.toString(cdLinha), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.CD_ROTA", Integer.toString(cdRota), ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsmTrechos = LinhaTrechoServices.find(criterios);
	
		ResultSetMap rsm = new ResultSetMap();
		
		// Laco para encontrar o primeiro trecho
		while (rsmTrechos.next()) {
			if (rsmTrechos.getInt("CD_TRECHO_ANTERIOR")==0) {
				rsm.addRegister(rsmTrechos.getRegister());
				rsm.next();
				break;
			}
		}
		
		// Laco para ordenas os trechos, a partir da segunda parada
		int i = rsmTrechos.getLines().size();
		while (i > 0) {
			rsmTrechos.beforeFirst();

			while (rsmTrechos.next()) {
				if (rsm.getInt("CD_TRECHO")==rsmTrechos.getInt("CD_TRECHO_ANTERIOR")) {
					rsm.addRegister(rsmTrechos.getRegister());
					rsm.next();
					break;
				}
			}
			i--;
		}
		
		
		// Selecionar apenas as paradas que sao terminais
		if (lgOnlyTerminais) {
			ResultSetMap rsmTerminais = new ResultSetMap();
			
			for (int j=0; rsm!=null && j<rsm.getLines().size(); j++) {
				if ((int)rsm.getLines().get(j).get("CD_GRUPO_PARADA_SUPERIOR")>0) {
					rsmTerminais.addRegister(rsm.getLines().get(j));
				}
			}
			
			rsm = rsmTerminais;
		}

		return rsm;
	}
	
	public static ResultSetMap getParadas(int cdLinha, int cdRota) {
		return getParadas(cdLinha, cdRota, false);
	}
	
	public static ResultSetMap getParadas(int cdLinha, int cdRota, boolean lgOnlyTerminais) {
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.CD_LINHA", Integer.toString(cdLinha), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.CD_ROTA", Integer.toString(cdRota), ItemComparator.EQUAL, Types.INTEGER));
		ResultSetMap rsmTrechos = LinhaTrechoServices.find(criterios);
	
		ResultSetMap rsm = new ResultSetMap();
		
		// Laco para encontrar o primeiro trecho
		while (rsmTrechos.next()) {
			if (rsmTrechos.getInt("CD_TRECHO_ANTERIOR")==0) {
				rsm.addRegister(rsmTrechos.getRegister());
				rsm.next();
				break;
			}
		}
		
		// Laco para ordenas os trechos, a partir da segunda parada
		int i = rsmTrechos.getLines().size();
		while (i > 0) {
			rsmTrechos.beforeFirst();

			while (rsmTrechos.next()) {
				if (rsm.getInt("CD_TRECHO")==rsmTrechos.getInt("CD_TRECHO_ANTERIOR")) {
					rsm.addRegister(rsmTrechos.getRegister());
					rsm.next();
					break;
				}
			}
			i--;
		}
		
		// Selecionar apenas as paradas que sao terminais
		if (lgOnlyTerminais) {
			ResultSetMap rsmTerminais = new ResultSetMap();
			
			for (int j=0; rsm!=null && j<rsm.getLines().size(); j++) {
				if ((int)rsm.getLines().get(j).get("CD_GRUPO_PARADA_SUPERIOR")>0) {
					rsmTerminais.addRegister(rsm.getLines().get(j));
				}
			}
			
			rsm = rsmTerminais;
		}
		
		return rsm;
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {	
		try {
			connect = (connect == null ? Conexao.conectar() : connect);

			String orderBy = "";
			String groupBy = "";

			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("ORDERBY")) {
					orderBy = " ORDER BY " + criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				}else if (criterios.get(i).getColumn().equalsIgnoreCase("GROUPBY")) {
					groupBy = " GROUP BY " + criterios.get(i).getValue().toString().trim();					
					criterios.remove(i);
					i--;
				}
			}
			
			ResultSetMap rsm = Search.find("SELECT A.*, B.nr_linha, B.cd_concessao FROM mob_linha_rota A " +
										   "LEFT OUTER JOIN mob_linha B ON (A.cd_linha = B.cd_linha) ",
										   groupBy + orderBy, criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
			//Pega os registros do rsm
			while(rsm.next()) {
				rsm.setValueToField("DS_ROTA", rsm.getString("nm_rota") + " - " + tipoLinhaRota[rsm.getInt("tp_rota")].toUpperCase());
				
				ResultSetMap rsmConcessoes = new ResultSetMap(connect.prepareStatement("SELECT B.nm_pessoa FROM mob_concessao A "
																						+ "LEFT OUTER JOIN grl_pessoa B ON (A.cd_concessionario = B.cd_pessoa)"
																						+ "WHERE A.cd_concessao = " + rsm.getInt("CD_CONCESSAO")).executeQuery());
				
				if(rsmConcessoes.next())
					rsm.setValueToField("NM_PESSOA", rsmConcessoes.getString("NM_PESSOA"));
				
				// Calcular extensao da rota
				ResultSetMap rsmItinerario = new ResultSetMap(connect.prepareStatement("SELECT sum(qt_km) as qt_km_total FROM mob_linha_trecho" +
																					 " WHERE cd_linha = " + rsm.getInt("CD_LINHA") + " AND cd_rota = " + rsm.getInt("CD_ROTA")).executeQuery());
				Double extensao = 0.0;
				if (rsmItinerario.next())
					extensao = rsmItinerario.getDouble("QT_KM_TOTAL");
				
				rsm.setValueToField("VL_EXTENSAO", extensao);
				
				// Pega intiner�rio organizado
				if(rsm.getInt("CD_LINHA") > 0 && rsm.getInt("CD_ROTA") > 0) {
					rsm.setValueToField("RSM_LINHA_TRECHOS", getParadasOrdenadas(rsm.getInt("CD_LINHA"), rsm.getInt("CD_ROTA")));
				}
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaRotaServices.find: " +  e);
			Conexao.rollback(connect);
			return null;
		}		
	}

}