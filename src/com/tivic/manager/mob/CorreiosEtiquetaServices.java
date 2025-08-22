package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.ResultSetMapper;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class CorreiosEtiquetaServices {

	public static Result save(CorreiosEtiqueta correiosEtiqueta){
		return save(correiosEtiqueta, null, null);
	}

	public static Result save(CorreiosEtiqueta correiosEtiqueta, AuthData authData){
		return save(correiosEtiqueta, authData, null);
	}

	public static Result save(CorreiosEtiqueta correiosEtiqueta, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(correiosEtiqueta==null)
				return new Result(-1, "Erro ao salvar. CorreiosEtiqueta � nulo");

			int retorno;
			if(correiosEtiqueta.getCdEtiqueta()==0){
				retorno = CorreiosEtiquetaDAO.insert(correiosEtiqueta, connect);
				correiosEtiqueta.setCdEtiqueta(retorno);
			}
			else {
				retorno = CorreiosEtiquetaDAO.update(correiosEtiqueta, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CORREIOSETIQUETA", correiosEtiqueta);
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
	public static Result remove(CorreiosEtiqueta correiosEtiqueta) {
		return remove(correiosEtiqueta.getCdEtiqueta());
	}
	public static Result remove(int cdEtiqueta){
		return remove(cdEtiqueta, false, null, null);
	}
	public static Result remove(int cdEtiqueta, boolean cascade){
		return remove(cdEtiqueta, cascade, null, null);
	}
	public static Result remove(int cdEtiqueta, boolean cascade, AuthData authData){
		return remove(cdEtiqueta, cascade, authData, null);
	}
	public static Result remove(int cdEtiqueta, boolean cascade, AuthData authData, Connection connect){
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
			retorno = CorreiosEtiquetaDAO.delete(cdEtiqueta, connect);
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Este registro est� vinculado a outros e n�o pode ser exclu�do!");
			}
			else if (isConnectionNull)
				connect.commit();
			return new Result(1, "Registro exclu�do com sucesso!");
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_correios_etiqueta");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CorreiosEtiquetaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CorreiosEtiquetaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_correios_etiqueta", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getEtiquetas(Criterios criterios) {
		Connection connect = Conexao.conectar();
		String pstmt;
		
		try {

			Criterios    crt = new Criterios();
			int qtLimite = 0;
			int qtDeslocamento = 0;
			
			for (int i=0; criterios!=null && i<criterios.size(); i++) {
				if (criterios.get(i).getColumn().equalsIgnoreCase("qtLimite"))
					qtLimite = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("qtDeslocamento"))
					qtDeslocamento = Integer.parseInt(criterios.get(i).getValue());
				else
					crt.add(criterios.get(i));
					
			}

			String[] sqlLimit = com.tivic.manager.util.Util.getLimitAndSkip(qtLimite, qtDeslocamento);
			
			pstmt = "SELECT "+sqlLimit[0]+" A.*, B.id_ait " + 
					"FROM MOB_CORREIOS_ETIQUETA AS A LEFT JOIN (SELECT id_ait, cd_ait FROM MOB_AIT GROUP BY CD_AIT) AS B ON (A.cd_ait = B.cd_ait)";
				
			
			ResultSetMap rsm = Search.find(pstmt, " "+sqlLimit[1] ,crt, connect!=null ? connect : Conexao.conectar(), connect==null);					
			ResultSetMap rsmTotal = Search.find("SELECT COUNT(*) FROM MOB_CORREIOS_ETIQUETA A ", "", crt, connect!=null ? connect : Conexao.conectar(), connect==null);

			if(rsmTotal.next())
				rsm.setTotal(rsmTotal.getInt("COUNT"));

//			System.out.println(rsm.getLines());
			return rsm;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
		
	}

	
	
	
	
	
	
	
	public static void criarEtiquetas(CorreiosLote correiosLote, Connection connect) {
		int cdLote = correiosLote.getCdLote();
		int nrInicial = correiosLote.getNrInicial();
		int nrFinal = correiosLote.getNrFinal();
		String sgLote = correiosLote.getSgLote();
		int stLote = correiosLote.getStLote();
		int tpLote = correiosLote.getTpLote();
		GregorianCalendar dtLote = correiosLote.getDtLote();
		GregorianCalendar dtVencimento = correiosLote.getDtVencimento();
		
		for(int i = nrInicial; i <= nrFinal; i++) {
			CorreiosEtiqueta correiosEtiqueta = new CorreiosEtiqueta();
			correiosEtiqueta.setCdLote(cdLote);
			correiosEtiqueta.setNrEtiqueta(i);
			correiosEtiqueta.setSgServico(sgLote);
			correiosEtiqueta.setNrDigitoVerificador(gerarDigitoVerificador(i));
			
			save(correiosEtiqueta, null, connect);
			
		}
		

		
	}
	
	public static void atualizarEtiquetas(CorreiosLote correiosLote, Connection connect) throws IllegalArgumentException, SQLException, Exception {
		int cdLote = correiosLote.getCdLote();
		Criterios crt = new Criterios();
		crt.add("cd_lote", Integer.toString(cdLote), ItemComparator.EQUAL, Types.INTEGER);
		ResultSetMap etiquetasLote = find(crt);
		List<CorreiosEtiqueta> etiquetas = new ResultSetMapper<CorreiosEtiqueta>(etiquetasLote, CorreiosEtiqueta.class).toList();

		if(!etiquetas.get(0).getSgServico().equals(correiosLote.getSgLote())) {
		
			
				Criterios crtLote = new Criterios();			
				crtLote.add("A.cd_lote", Integer.toString(cdLote), ItemComparator.EQUAL, Types.INTEGER);
				ResultSetMap correiosLote2 = CorreiosLoteServices.getLoteEtiquetasLivres(crtLote);
				CorreiosLoteDTO correiosLoteDTO = new ResultSetMapper<CorreiosLoteDTO>(correiosLote2, CorreiosLoteDTO.class).toList().get(0);
		
				if(correiosLoteDTO.getQtdEtiquetasLivres() < (correiosLoteDTO.getNrFinal() - correiosLoteDTO.getNrInicial() + 1)) {
					for(CorreiosEtiqueta etiqueta: etiquetas) {
							etiqueta.setSgServico(correiosLote.getSgLote());
							save(etiqueta, null, connect);
					}
				}
				else if(correiosLoteDTO.getQtdEtiquetasLivres() == (correiosLoteDTO.getNrFinal() - correiosLoteDTO.getNrInicial() + 1)) {
				
					for(CorreiosEtiqueta etiqueta: etiquetas) {
						remove(etiqueta);
					}
					criarEtiquetas(correiosLote, connect);
					
				}
		}
		else {
			Criterios crtLote = new Criterios();			
			crtLote.add("A.cd_lote", Integer.toString(cdLote), ItemComparator.EQUAL, Types.INTEGER);
			ResultSetMap correiosLote2 = CorreiosLoteServices.getLoteEtiquetasLivres(crtLote);
			CorreiosLoteDTO correiosLoteDTO = new ResultSetMapper<CorreiosLoteDTO>(correiosLote2, CorreiosLoteDTO.class).toList().get(0);
			if(correiosLoteDTO.getQtdEtiquetasLivres() == (correiosLoteDTO.getNrFinal() - correiosLoteDTO.getNrInicial() + 1)) {
				
				for(CorreiosEtiqueta etiqueta: etiquetas) {
					remove(etiqueta);
				}
				criarEtiquetas(correiosLote, connect);
				
			}
		}

		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	private static int gerarDigitoVerificador(int numeroEtiqueta){
		String numero = Integer.toString(numeroEtiqueta);
		String retorno = numero;
		String dv;
		Integer[] multiplicadores = {8, 6, 4, 2, 3, 5, 9, 7};
		Integer soma = 0;
		// Preenche número com 0 à esquerda
		if(numero.length() < 8){
		String zeros = "";
		int diferenca = 8 - numero.length();
		for(int i=0; i<diferenca; i++){
		zeros += "0";
		}
		retorno = zeros + numero;
		}
		else{
		retorno = numero.substring(0, 8);
		}


		for(int i=0; i<8; i++){
		soma += new Integer(retorno.substring(i, (i+1))) * multiplicadores[i];
		}
		Integer resto = soma % 11;
		if(resto == 0){
		dv = "5";
		}else if(resto == 1){
		dv = "0";
		}else{
		dv = new Integer(11 - resto).toString();
		}
		retorno += dv;
		return Integer.parseInt(dv);
	}

	public static int etiquetasLivres(int cdLote) {
		Criterios crt = new Criterios();
		
		if(cdLote > 0) {
			crt.add("cd_lote", Integer.toString(cdLote), ItemComparator.EQUAL, Types.INTEGER);
			crt.add("cd_ait", "" , ItemComparator.ISNULL, Types.INTEGER);
		}
		
		ResultSetMap etiquetasLivres = find(crt);
		return etiquetasLivres.getLines().size();
	}

	public List<CorreiosEtiqueta> buscarEtiquetasMovimentos(int cdAit) throws Exception{
		List<CorreiosEtiqueta> correiosEtiquetasList = searchEtiquetasMovimentos(cdAit)
				.getList(CorreiosEtiqueta.class);
		for (CorreiosEtiqueta etiqueta: correiosEtiquetasList) {
			int dv = gerarDigitoVerificador(etiqueta.getNrEtiqueta());
			etiqueta.setNrDigitoVerificador(dv);
		}
		return correiosEtiquetasList;
	}
	
	private com.tivic.sol.search.Search<CorreiosEtiqueta> searchEtiquetasMovimentos(int cdAit) throws Exception{
		SearchCriterios searchCriterios = gerarCriteriosEtiquetaMovimento(cdAit);
		com.tivic.sol.search.Search<CorreiosEtiqueta> search = new SearchBuilder<CorreiosEtiqueta>("mob_correios_etiqueta A")
				.fields("*")
				.searchCriterios(searchCriterios)
				.build();
		return search;
	}
	
	private SearchCriterios gerarCriteriosEtiquetaMovimento(int cdAit) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit, true);
		return searchCriterios;
	}
}
