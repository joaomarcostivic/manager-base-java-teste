package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.util.LogUtils;
import com.tivic.manager.util.Util;

public class LinhaServices {
	
	public static final String[] tipoLinha = {"Radial","Diametral","Perimetral", "Especial", "Escolar"};
	
	public static final String[] situacaoLinha = {"Inativo","Ativo"};
	
	public static final int TP_RADIAL     = 0;
	public static final int TP_DIAMETRAL  = 1;
	public static final int TP_PERIMETRAL = 2;
	public static final int TP_ESPECIAL   = 3;
	public static final int TP_ESCOLAR   = 4;
	
	public static final int ST_INATIVO  = 0;
	public static final int ST_ATIVO  	= 1;
	
	public static Result save(Linha linha){
		return save(linha, null);
	}
	
	public static Result save(Linha linha, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(linha==null)
				return new Result(-1, "Erro ao salvar. Linha é nulo");

			
			int retorno;			
			if(linha.getCdLinha()==0){
				if(idLinhaExists(linha.getIdLinha()))
					return new Result(-2, "O ID "+linha.getIdLinha()+" é utilizado por outra Linha.");
				
				retorno = LinhaDAO.insert(linha, connect);
				linha.setCdLinha(retorno);
			}
			else {
				retorno = LinhaDAO.update(linha, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "LINHA", linha);
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
	
	/**
	 * Salva 
	 * @param linhaRota
	 * @param authData
	 * @param connect
	 * @return
	 */
	public static Result saveEscolar(Linha linha){
		linha.setTpLinha(TP_ESCOLAR);
		return save(linha, null);
	}
	
	public static Result saveEscolar(Linha linha, Connection connect){
		linha.setTpLinha(TP_ESCOLAR);
		return save(linha, connect);
	}
	
	public static Result saveEscolar(Linha linha, LinhaRota linhaRota, ConcessaoLote concessaoLote, ResultSetMap rsmLinhasTrecho){
		return saveEscolar(linha, linhaRota, concessaoLote, rsmLinhasTrecho, null);
	}
	
	public static Result saveEscolar(Linha linha, LinhaRota linhaRota, ConcessaoLote concessaoLote, ResultSetMap rsmLinhasTrecho, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(linha==null || linhaRota==null || concessaoLote==null)
				return new Result(-1, "Erro ao salvar. Existem valores nulos");

			int retorno = 0;
			
			// Salvar linha
			if (linha.getCdLinha() <= 0) 
				linha.setTpLinha(TP_ESCOLAR);
			
			if(linha.getCdLinha()==0){
				retorno = LinhaDAO.insert(linha, connect);
				linha.setCdLinha(retorno);
			}
			else {
				retorno = LinhaDAO.update(linha, connect);
			}
			
			
			// Salvar rota
			if (retorno>0) {
				linhaRota.setCdLinha(linha.getCdLinha());
				retorno = LinhaRotaServices.save(linhaRota, null, connect).getCode();
				linhaRota.setCdRota(retorno);
			}
			
			// Salvar Lote
			if(retorno>0){
				concessaoLote.setCdLinha(linha.getCdLinha());
				retorno = ConcessaoLoteServices.save(concessaoLote, 0, null, connect).getCode();
			}
			
			// Salvar trechos
			if (retorno>0) {
				retorno = LinhaTrechoServices.saveTrechosOrdenando(linhaRota.getCdLinha(), linhaRota.getCdRota(), rsmLinhasTrecho, null, connect).getCode();
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "LINHA", linha);
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
	
	/**
	 * Verifica se já existe uma linha cadastrada com o id informado
	 * @author Alvaro
	 * @param idLinha
	 * @return
	 */
	private static boolean idLinhaExists(String idLinha){
		return idLinhaExists(idLinha, null);
	}
	
	private static boolean idLinhaExists(String idLinha, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			return connect.prepareStatement("SELECT * FROM MOB_LINHA WHERE ID_LINHA = '"+idLinha+"'").executeQuery().next();
			
		}
		catch(Exception e){
			e.printStackTrace();
			if (isConnectionNull)
				Conexao.rollback(connect);
			return true;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static Result remove(int cdLinha){
		return remove(cdLinha, false, null);
	}
	public static Result remove(int cdLinha, boolean cascade){
		return remove(cdLinha, cascade, null);
	}
	public static Result remove(int cdLinha, boolean cascade, Connection connect){
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
			retorno = LinhaDAO.delete(cdLinha, connect);
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
	
	public static ResultSetMap getAllByTipo(int tpConcessao) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("B.tp_concessao", "" + tpConcessao, ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios, null);
	}
	
	public static ResultSetMap getAllByConcessao(int cdConcessao) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("B.cd_concessao", "" + cdConcessao, ItemComparator.EQUAL, Types.INTEGER));
		return find(criterios, null);
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
			pstmt = connect.prepareStatement("SELECT A.*, B.*, C.* FROM mob_linha A" +
										   " LEFT OUTER JOIN mob_concessao B ON (A.cd_concessao = B.cd_concessao)" +
										   " LEFT OUTER JOIN grl_pessoa    C ON (B.cd_concessionario = C.cd_pessoa) " +
										   " ORDER BY a.nr_linha");
			ResultSetMap rsmLinhas = new ResultSetMap(pstmt.executeQuery());
			
			while (rsmLinhas.next()) {
				String sql = "SELECT COUNT(*) AS nr_tabelas FROM mob_tabela_horario_rota A" +
							" LEFT OUTER JOIN mob_tabela_horario B ON (A.cd_tabela_horario = B.cd_tabela_horario AND A.cd_linha = B.cd_linha)" +
							" LEFT OUTER JOIN mob_linha_rota C ON (A.cd_linha = C.cd_linha AND A.cd_rota = C.cd_rota)" +
							" WHERE A.cd_linha = " + rsmLinhas.getInt("CD_LINHA") +
							" AND B.st_tabela_horario = " + TabelaHorarioServices.ST_ATIVA +
							" AND C.tp_rota = " + LinhaRotaServices.TP_IDA;
				ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(sql).executeQuery());
				
				if (rsm.next())
					rsmLinhas.setValueToField("NR_VEICULOS", rsm.getInt("NR_TABELAS"));
				else
					rsmLinhas.setValueToField("NR_VEICULOS", 0);
			}
			
			return rsmLinhas;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllEscolar() {
		return getAllEscolar(null);
	}

	public static ResultSetMap getAllEscolar(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			
			
			pstmt = connect.prepareStatement("SELECT A.*, B.*, C.*, D.*, E.nm_distrito, F.* FROM mob_linha A" +
										   " LEFT OUTER JOIN mob_concessao		 	B ON (A.cd_concessao = B.cd_concessao)" +
										   " LEFT OUTER JOIN grl_pessoa    			C ON (B.cd_concessionario = C.cd_pessoa) " +
										   " LEFT OUTER JOIN mob_concessao_lote 	D ON (A.cd_linha = D.cd_linha) " +
										   " LEFT OUTER JOIN grl_distrito 			E ON (D.cd_distrito = E.cd_distrito and D.cd_cidade = E.cd_cidade) " +
										   " LEFT OUTER JOIN mob_linha_rota		 	F ON (A.cd_linha = F.cd_linha)" +
										   " WHERE F.tp_rota = " + LinhaRotaServices.TP_IDA +
										   " ORDER BY A.nr_linha");
			ResultSetMap rsmLinhas = new ResultSetMap(pstmt.executeQuery());
			return rsmLinhas;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LinhaServices.getAllEscolar: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaServices.getAllEscolar: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findEscolar(ArrayList<ItemComparator> criterios) {
		return findEscolar(criterios, null);
	}

	public static ResultSetMap findEscolar(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			String orderBy = " ORDER BY A.nr_linha";
			String groupBy = "";
			String sql = "";
			
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("ORDERBY")) {
					orderBy = " ORDER BY " + criterios.get(i).getValue().toString().trim();
					criterios.remove(i);
					i--;
				} else if (criterios.get(i).getColumn().equalsIgnoreCase("GROUPBY")) {
					groupBy = " GROUP BY " + criterios.get(i).getValue().toString().trim();					
					criterios.remove(i);
					i--;
				}
			}
			
			sql = "SELECT A.*, B.*, C.*, D.*, E.nm_distrito, F.* FROM mob_linha A" +
					   " LEFT OUTER JOIN mob_concessao		 	B ON (A.cd_concessao = B.cd_concessao)" +
					   " LEFT OUTER JOIN grl_pessoa    			C ON (B.cd_concessionario = C.cd_pessoa) " +
					   " LEFT OUTER JOIN mob_concessao_lote 	D ON (A.cd_linha = D.cd_linha) " +
					   " LEFT OUTER JOIN grl_distrito 			E ON (D.cd_distrito = E.cd_distrito and D.cd_cidade = E.cd_cidade) " +
					   " LEFT OUTER JOIN mob_linha_rota		 	F ON (A.cd_linha = F.cd_linha)" +
					   " WHERE F.tp_rota = " + LinhaRotaServices.TP_IDA;
			
			System.out.println("SQL:\n"+Search.getStatementSQL(sql, groupBy + orderBy, criterios, true));
			
			return Search.find(sql, groupBy + orderBy, criterios, connect, isConnectionNull);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaServices.find: " +  e);
			Conexao.rollback(connect);
			return null;
		}finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}		
	}
	
	/**
	 * Metodo para buscar as linhas escolares sem concessao 
	 * @return
	 */
	public static ResultSetMap getEscolarNoConcessao(ArrayList<ItemComparator> criterios) {
		return getEscolarNoConcessao(criterios, null);
	}

	public static ResultSetMap getEscolarNoConcessao(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ResultSetMap rsm = Search.find("SELECT A.*, B.*, C.nm_distrito FROM mob_linha A" +
					   " LEFT OUTER JOIN mob_concessao_lote 	B ON (A.cd_linha = B.cd_linha) " +
					   " LEFT OUTER JOIN grl_distrito 			C ON (B.cd_distrito = C.cd_distrito and B.cd_cidade = C.cd_cidade) " +
					   " WHERE A.cd_concessao IS NULL " +
					   " ORDER BY A.nr_linha","", criterios, connect, isConnectionNull);
			
			while(rsm.next()){
				rsm.setValueToField("NM_TP_VEICULO", ConcessaoLoteServices.tipoVeiculo[rsm.getInt("tp_veiculo")]);
				rsm.setValueToField("NM_TP_TRANSPORTADOS", ConcessaoLoteServices.tipoTransportados[rsm.getInt("tp_transportados")]);
				rsm.setValueToField("NM_TP_TURNO", ConcessaoLoteServices.tiposTurno[rsm.getInt("tp_turno")]);
			}
			
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaServices.getEscolarNoConcessao: " +  e);
			Conexao.rollback(connect);
			return null;
		}finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}		
	}


	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {

		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			
			Criterios    crt = new Criterios();
			int qtLimite = 0;
			int qtDeslocamento = 0;
			String orderBy = "";
			String groupBy = "";
			
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite"))
					qtLimite = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("qtDeslocamento"))
					qtDeslocamento = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("ORDERBY"))
					orderBy = " ORDER BY " + criterios.get(i).getValue().toString().trim();
				else if (criterios.get(i).getColumn().equalsIgnoreCase("GROUPBY"))
					groupBy = " GROUP BY " + criterios.get(i).getValue().toString().trim();
				else
					crt.add(criterios.get(i));
				
			}

			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, qtDeslocamento);
				
			ResultSetMap rsm = Search.find("SELECT  " + sqlLimit[0] + " A.*, B.*, C.* FROM mob_linha A " +
					   "LEFT OUTER JOIN mob_concessao B ON (A.cd_concessao = B.cd_concessao) " +
					   "LEFT OUTER JOIN grl_pessoa    C ON (B.cd_concessionario = C.cd_pessoa) ",
					   groupBy + orderBy + " " + sqlLimit[1], crt, connect!=null ? connect : Conexao.conectar(), connect==null);
			
			ResultSetMap rsmTotal = Search.find("SELECT COUNT(*) FROM mob_linha A " +
				   "LEFT OUTER JOIN mob_concessao B ON (A.cd_concessao = B.cd_concessao) " +
				   "LEFT OUTER JOIN grl_pessoa    C ON (B.cd_concessionario = C.cd_pessoa) ", "", crt, connect!=null ? connect : Conexao.conectar(), connect==null);

			if(rsmTotal.next())
				rsm.setTotal(rsmTotal.getInt("COUNT"));
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaServices.find: " +  e);
			Conexao.rollback(connect);
			return null;
		}finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}		
	}
	
	public static ResultSetMap getTabelas(int cdLinha, int cdTabelaHorario) {
		return getTabelas(cdLinha, cdTabelaHorario, null);
	} 
	
	public static ResultSetMap getTabelas(int cdLinha, int cdTabelaHorario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_linha", String.valueOf(cdLinha), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.cd_tabela_horario", String.valueOf(cdTabelaHorario), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmTabelaHorarioRota = TabelaHorarioRotaServices.find(criterios);
			
			while (rsmTabelaHorarioRota.next()) {
				ResultSetMap rsmHorarios = TabelaHorarioRotaServices.getHorarios(rsmTabelaHorarioRota.getInt("CD_TABELA_HORARIO"), cdLinha, rsmTabelaHorarioRota.getInt("CD_ROTA"));
				rsmTabelaHorarioRota.setValueToField("RSM_HORARIOS", rsmHorarios);
	
				
				ResultSetMap rsmParadas = LinhaRotaServices.getParadasOrdenadas(cdLinha, rsmTabelaHorarioRota.getInt("CD_ROTA"), true);
				rsmTabelaHorarioRota.setValueToField("RSM_PARADAS", rsmParadas);
				
				ResultSetMap rsmTabelaHorario = TabelaHorarioServices.find(new Criterios()
				   .add("A.cd_tabela_horario", Integer.toString(cdTabelaHorario), ItemComparator.EQUAL, Types.INTEGER)
				   .add("A.cd_linha", Integer.toString(cdLinha), ItemComparator.EQUAL, Types.INTEGER)
				);
				rsmTabelaHorarioRota.setValueToField("RSM_TABELA_HORARIO", rsmTabelaHorario);
			}
		
			return rsmTabelaHorarioRota;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaServices.getTabelas: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static ResultSetMap getTabelasHorarioRota(int cdLinha) {
		return getTabelasHorarioRota(cdLinha, null);
	} 
	public static ResultSetMap getTabelasHorarioRota(int cdLinha, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_linha", String.valueOf(cdLinha), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmTabelaHorarioRota = TabelaHorarioRotaServices.find(criterios);
			System.out.println("TABELAHORARIOROTA"+rsmTabelaHorarioRota);
			
			while (rsmTabelaHorarioRota.next()) {
				ResultSetMap rsmHorarios = TabelaHorarioRotaServices.getHorarios(rsmTabelaHorarioRota.getInt("CD_TABELA_HORARIO"), cdLinha, rsmTabelaHorarioRota.getInt("CD_ROTA"));
				rsmTabelaHorarioRota.setValueToField("RSM_HORARIOS", rsmHorarios);
	
				
				ResultSetMap rsmParadas = LinhaRotaServices.getParadasOrdenadas(cdLinha, rsmTabelaHorarioRota.getInt("CD_ROTA"), true);
				rsmTabelaHorarioRota.setValueToField("RSM_PARADAS", rsmParadas);
			}
			return rsmTabelaHorarioRota;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaServices.getTabelas: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result getRelatorioOS(int cdLinha) {
		return getRelatorioOS(cdLinha, null);
	} 
	
	public static Result getRelatorioOS(int cdLinha, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		try {
			
			Result result = new Result(1);
			
			/* ROTAS */
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.cd_linha", String.valueOf(cdLinha), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmRotas = LinhaRotaServices.find(criterios, connect);
			
			while (rsmRotas.next()) {
				
				/* VIAGENS */
				ResultSetMap rsmItinerario = LinhaRotaServices.getParadasOrdenadas(cdLinha, rsmRotas.getInt("CD_ROTA"));
				rsmRotas.setValueToField("RSM_ITINERARIO", rsmItinerario); //Itinerario
				
				/* DETALHE */
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("CD_ROTA", rsmRotas.getInt("CD_ROTA"));
				register.put("NM_ROTA", rsmRotas.getString("NM_ROTA"));
				register.put("VL_EXTENSAO", getExtensao(cdLinha, rsmRotas.getInt("CD_ROTA")));
				register.put("NR_VIAGENS", getNrViagens(cdLinha, rsmRotas.getInt("CD_ROTA"), connect));
				
				ResultSetMap rsmResumoHorario = getResumoHorarios(cdLinha, rsmRotas.getInt("CD_ROTA"));
				if (rsmResumoHorario!=null && rsmResumoHorario.next()) {
					register.put("HR_PRIMEIRO", rsmResumoHorario.getGregorianCalendar("HR_PRIMEIRO"));
					register.put("HR_ULTIMO", rsmResumoHorario.getGregorianCalendar("HR_ULTIMO"));
				}
				rsmRotas.setValueToField("REG_DETALHE", register);
				
				/* TABELAS HORARIO */
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("A.cd_linha", String.valueOf(cdLinha), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("A.cd_rota", String.valueOf(rsmRotas.getInt("CD_ROTA")), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmTabelaHorarioRota = TabelaHorarioRotaServices.find(criterios, connect);
				
				while (rsmTabelaHorarioRota.next()) {
					ResultSetMap rsmHorarios = TabelaHorarioRotaServices.getHorarios(rsmTabelaHorarioRota.getInt("CD_TABELA_HORARIO"), cdLinha, rsmTabelaHorarioRota.getInt("CD_ROTA"));
					rsmTabelaHorarioRota.setValueToField("RSM_HORARIOS", rsmHorarios);
				}
				rsmRotas.setValueToField("RSM_TABELA_HORARIO_ROTA", rsmTabelaHorarioRota);
			}
			
			rsmRotas.beforeFirst();
			result.addObject("RSM_ROTA", rsmRotas);
		
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LinhaServices.getProtocolarOS: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo para buscar o primeiro e o ultimo horario da rota
	 */
	public static float getNrViagens(int cdLinha, int cdRota) {
		return getNrViagens(cdLinha, cdRota, null);
	}
	public static float getNrViagens(int cdLinha, int cdRota, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();

		try {
			
			String sql = "SELECT (cd_tabela_horario) FROM mob_tabela_horario_rota WHERE CD_LINHA = " + cdLinha +  " AND CD_ROTA = " + cdRota + "GROUP BY cd_tabela_horario";
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(sql).executeQuery());
			float nrViagens = rsm.getLines().size();
			
			sql = "SELECT (cd_horario) FROM mob_horario WHERE CD_LINHA = " + cdLinha +  " AND CD_ROTA = " + cdRota + "GROUP BY cd_horario";
			rsm = new ResultSetMap(connect.prepareStatement(sql).executeQuery());
			nrViagens = nrViagens * rsm.getLines().size();
			
			return nrViagens;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo para buscar o primeiro e o ultimo horario da rota
	 */
	public static ResultSetMap getResumoHorarios(int cdLinha, int cdRota) {
		return getResumoHorarios(cdLinha, cdRota, null);
	}
	public static ResultSetMap getResumoHorarios(int cdLinha, int cdRota, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			String sql = "SELECT MIN(hr_partida) AS hr_primeiro, MAX(hr_partida) AS hr_ultimo FROM mob_horario" +
							" WHERE CD_LINHA = " + cdLinha + 
							" AND CD_ROTA = " + cdRota;
						
			pstmt = connect.prepareStatement(sql);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if (rsm.next()) {
				rsm.beforeFirst();
				return rsm;
			}
			
			return null;
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
	
	/**
	 * Metodo para calcular o tempo de viagem de uma rota de acordo uma tabela horario
	 */
	public static long getTempoViagem(int cdLinha, int cdRota, int cdTrechoInicial, int cdTrechoFinal, int cdTabelaHorario) {
		return getTempoViagem(cdLinha, cdRota, cdTrechoInicial, cdTrechoFinal, cdTabelaHorario, null);
	}
	public static long getTempoViagem(int cdLinha, int cdRota, int cdTrechoInicial, int cdTrechoFinal, int cdTabelaHorario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			String sql = "SELECT MIN(hr_partida) AS hr_primeiro, MAX(hr_partida) AS hr_ultimo FROM mob_horario" +
						 " WHERE cd_linha = " + cdLinha + 
						 " AND cd_rota = " + cdRota;
						
			pstmt = connect.prepareStatement(sql);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			sql = "SELECT hr_partida FROM mob_horario" +
				" WHERE cd_linha = " + cdLinha + 
				" AND cd_rota = " + cdRota +
				" AND cd_trecho = " + cdTrechoInicial +
				" AND cd_tabela_horario = " + cdTabelaHorario;
			
			GregorianCalendar horaInicial = new GregorianCalendar();
			if(rsm.next())
				horaInicial = rsm.getGregorianCalendar("HR_PARTIDA");
			
			sql = "SELECT hr_partida FROM mob_horario" +
					" WHERE cd_linha = " + cdLinha + 
					" AND cd_rota = " + cdRota +
					" AND cd_trecho = " + cdTrechoFinal +
					" AND cd_tabela_horario = " + cdTabelaHorario;
				
			GregorianCalendar horaFinal = new GregorianCalendar();
			if(rsm.next())
				horaFinal = rsm.getGregorianCalendar("HR_CHEGADA");
			
			long diferenca = horaFinal.getTimeInMillis() - horaInicial.getTimeInMillis();
			long minutos = (diferenca / 60000) % 60;
				
			return minutos;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return 0;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Metodo para calcular a extensao de uma determinada linha, completa ou por rota
	 */
	public static float getExtensao(int cdLinha) {
		return getExtensao(cdLinha, 0, null);
	}
	public static float getExtensao(int cdLinha, int cdRota) {
		return getExtensao(cdLinha, cdRota, null);
	}
	public static float getExtensao(int cdLinha, int cdRota, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			String sql = "SELECT SUM(qt_km) AS qt_extensao FROM mob_linha_trecho" +
						 " WHERE cd_linha = " + cdLinha + 
						 (cdRota!=0?" AND cd_rota = " + cdRota :"");
						
			pstmt = connect.prepareStatement(sql);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if (rsm.next())
				return rsm.getFloat("QT_EXTENSAO");
			else
				return 0;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int getQtdByConcessao(int cdConcessao) {
		return getQtdByConcessao(cdConcessao, null);
	}
	public static int getQtdByConcessao(int cdConcessao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();

		try {
			int qtdLinhas = 0;
			
			String sql = "SELECT count(*) as qtd_linhas FROM mob_linha WHERE cd_concessao = ?";
			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, cdConcessao);	
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(rsm.next())
				qtdLinhas = rsm.getInt("QTD_LINHAS");
				
			return qtdLinhas;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return 0;
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
			
			String sql = " SELECT cd_linha, cd_concessao, nr_linha " + 
			             " FROM mob_linha " +
					     " WHERE cd_concessao = 613 OR cd_concessao = 14;";
						
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
		PreparedStatement pstmt3;
		try {			

			String sql = "SELECT * FROM mob_linha order by cd_linha";	
			pstmt1 = connect.prepareStatement(sql);

			sql = "SELECT * FROM mob_linha_rota";	
			pstmt2 = connect.prepareStatement(sql);
			
			sql = "SELECT * FROM mob_linha_trecho";	
			pstmt3 = connect.prepareStatement(sql);

			HashMap<String, Object> register = new HashMap<String, Object>();
			register.put("Linha", Util.resultSetToArrayList(pstmt1.executeQuery()));
			register.put("LinhaRota", Util.resultSetToArrayList(pstmt2.executeQuery()));
			register.put("LinhaTrecho", Util.resultSetToArrayList(pstmt3.executeQuery()));
			
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
	
}
