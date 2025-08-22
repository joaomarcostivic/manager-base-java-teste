package com.tivic.manager.mob;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.seg.AuthData;

public class CorreiosLoteServices {
	public static final int E_CARTAS = 0;
	public static final int REMESSA_ECONOMICA = 1;
	public static final int CARTA_REGISTRADA = 2;

	public static Result save(CorreiosLote correiosLote){
		return save(correiosLote, null, null);
	}

	public static Result save(CorreiosLote correiosLote, AuthData authData){
		return save(correiosLote, authData, null);
	}

	public static Result save(CorreiosLote correiosLote, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(correiosLote==null)
				return new Result(-1, "Erro ao salvar. CorreiosLote � nulo");

			int retorno;
			if(correiosLote.getCdLote()==0){
				retorno = CorreiosLoteDAO.insert(correiosLote, connect);
				correiosLote.setCdLote(retorno);
			}
			else {
				retorno = CorreiosLoteDAO.update(correiosLote, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CORREIOSLOTE", correiosLote);
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
	
	public static Result salvarLote(CorreiosLote correiosLote, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(correiosLote==null)
				return new Result(-1, "Erro ao salvar. Lote nulo");

			int retorno;
			if(correiosLote.getCdLote()==0){
				retorno = CorreiosLoteDAO.insert(correiosLote, connect);
				correiosLote.setCdLote(retorno);
				CorreiosEtiquetaServices.criarEtiquetas(correiosLote, connect);
			}
			else {
				if(verificarAtualizacao(correiosLote)) {
					retorno = CorreiosLoteDAO.update(correiosLote, connect);
					CorreiosEtiquetaServices.atualizarEtiquetas(correiosLote, connect);
				}
				else {
					retorno = CorreiosLoteDAO.update(correiosLote, connect);
				}
				
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "CORREIOSLOTE", correiosLote);
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
	
	
	
	
	public static Result remove(CorreiosLote correiosLote) {
		return remove(correiosLote.getCdLote());
	}
	public static Result remove(int cdLote){
		return remove(cdLote, false, null, null);
	}
	public static Result remove(int cdLote, boolean cascade){
		return remove(cdLote, cascade, null, null);
	}
	public static Result remove(int cdLote, boolean cascade, AuthData authData){
		return remove(cdLote, cascade, authData, null);
	}
	public static Result remove(int cdLote, boolean cascade, AuthData authData, Connection connect){
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
			retorno = CorreiosLoteDAO.delete(cdLote, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_correios_lote");
				
			ResultSetMap lotes = new ResultSetMap(pstmt.executeQuery()); 
			
			return lotes;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CorreiosLoteServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CorreiosLoteServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_correios_lote", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static List<CorreiosLote> findLotes(ArrayList<ItemComparator> criterios){
		try {
			ResultSetMap rsm = find(criterios);
			List<CorreiosLote> list = new ArrayList<CorreiosLote>();

			while (rsm.next()) {
				CorreiosLote correiosLote = new CorreiosLote();
				correiosLote.setCdLote(rsm.getInt("CD_LOTE"));
				correiosLote.setDtLote(rsm.getGregorianCalendar("DT_LOTE"));
				correiosLote.setNrInicial(rsm.getInt("NR_INICIAL"));
				correiosLote.setNrFinal(rsm.getInt("NR_FINAL"));
				correiosLote.setStLote(rsm.getInt("ST_LOTE"));
				correiosLote.setTpLote(rsm.getInt("TP_LOTE"));
				correiosLote.setSgLote(rsm.getString("SG_LOTE"));
				correiosLote.setDtVencimento(rsm.getGregorianCalendar("DT_VENCIMENTO"));
				
				list.add(correiosLote);
			}

			return list;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			throw ex;
		}
		
	}
	

	public static Result cancelarLote(CorreiosLote correiosLote) {
		
		correiosLote.setStLote(2);
		
		return save(correiosLote);
	}
	
	
	public static void verificarVencimento(List<CorreiosLoteDTO> correiosLote) {
		for(CorreiosLoteDTO c : correiosLote) { 
			if((c.getDtVencimento().getTimeInMillis() <= System.currentTimeMillis())&&(c.getStLote() != 2)) {
				c.setStLote(3);
				save(c);
			}
		}
	}
	
	
	public static void verificarConclusao(List<CorreiosLoteDTO> correiosLote) {
		for(CorreiosLoteDTO c : correiosLote) { 
			if((c.getQtdEtiquetasLivres() == 0)) {
				c.setStLote(1);
				save(c);
			}
		}
	}
	
	
	public static List<CorreiosLoteDTO> CreateListGetAll(List<CorreiosLote> correiosLotes){
		List<CorreiosLoteDTO> correiosLoteDto = new ArrayList<CorreiosLoteDTO>();

		for(CorreiosLote correiosLote : correiosLotes) {
			CorreiosLoteDTO correiosLoteDTO = new CorreiosLoteDTO.Builder(correiosLote).getEtiquetasLivres().build(); 
			correiosLoteDto.add(correiosLoteDTO);
		}

		return correiosLoteDto;
	}
	
	
	public static boolean verificarAtualizacao(CorreiosLote correiosLote) {
		Boolean retorno = false;
		if(
			!correiosLote.getSgLote().equals(CorreiosLoteDAO.get(correiosLote.getCdLote()).getSgLote()) || 
			(correiosLote.getNrInicial() != CorreiosLoteDAO.get(correiosLote.getCdLote()).getNrInicial()) ||
			(correiosLote.getNrFinal() != CorreiosLoteDAO.get(correiosLote.getCdLote()).getNrFinal())
		) {
			retorno = true;
		}
		else {
			retorno = false;
		}

		return retorno;
	}
	
	public static ResultSetMap getLoteEtiquetasLivres(Criterios criterios) {
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
			
			pstmt = "SELECT "+sqlLimit[0]+" A.*, B.qtd_etiquetas_livres " + 
					"FROM MOB_CORREIOS_LOTE AS A, (SELECT COUNT(*) - COUNT(cd_ait) as qtd_etiquetas_livres, " + 
					"cd_lote FROM MOB_CORREIOS_ETIQUETA GROUP BY CD_LOTE) AS B WHERE A.cd_lote = B.cd_lote";
	
			
			ResultSetMap rsm = Search.find(pstmt, " "+sqlLimit[1] ,crt, connect!=null ? connect : Conexao.conectar(), connect==null);					
			ResultSetMap rsmTotal = Search.find("SELECT COUNT(*) FROM MOB_CORREIOS_LOTE A ", "", crt, connect!=null ? connect : Conexao.conectar(), connect==null);

			if(rsmTotal.next())
				rsm.setTotal(rsmTotal.getInt("COUNT"));
			
			return rsm;
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			Conexao.desconectar(connect);
		}
		
	}


	

}
