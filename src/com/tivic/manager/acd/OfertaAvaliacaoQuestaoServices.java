package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;
import com.tivic.manager.util.Util;

public class OfertaAvaliacaoQuestaoServices {

	/* Tipos de turnos */
	public static final int TP_OBJETIVA   	= 0;
	public static final int TP_DESCRITIVA 	= 1;
	public static final int TP_OBJETIVA_DESCRITIVA   		= 2;

	public static final String[] tiposQuestao = {"Objetiva",
		"Descritiva",
		"Objetiva/Descritiva"};
	
	public static Result save(OfertaAvaliacaoQuestao ofertaAvaliacaoQuestao){
		return save(ofertaAvaliacaoQuestao, null, null);
	}

	public static Result save(OfertaAvaliacaoQuestao ofertaAvaliacaoQuestao, AuthData authData){
		return save(ofertaAvaliacaoQuestao, authData, null);
	}

	public static Result save(OfertaAvaliacaoQuestao ofertaAvaliacaoQuestao, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(ofertaAvaliacaoQuestao==null)
				return new Result(-1, "Erro ao salvar. OfertaAvaliacaoQuestao é nulo");

			
			if(ofertaAvaliacaoQuestao.getTpQuestao() != TP_OBJETIVA && ofertaAvaliacaoQuestao.getTpQuestao() != TP_OBJETIVA_DESCRITIVA){
				int ret = connect.prepareStatement("DELETE FROM acd_oferta_avaliacao_questao_alternativa "
						+ "								WHERE cd_oferta_avaliacao_questao = " + ofertaAvaliacaoQuestao.getCdOfertaAvaliacaoQuestao()
						+ "								  AND cd_oferta_avaliacao = " + ofertaAvaliacaoQuestao.getCdOfertaAvaliacao()
						+ "								  AND cd_oferta = " + ofertaAvaliacaoQuestao.getCdOferta()).executeUpdate();
				if(ret < 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao remover alternativas");
				}
			}
			
			
			
			int retorno;
			if(ofertaAvaliacaoQuestao.getCdOfertaAvaliacaoQuestao()==0){
				
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_oferta_avaliacao", "" + ofertaAvaliacaoQuestao.getCdOfertaAvaliacao(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_oferta", "" + ofertaAvaliacaoQuestao.getCdOferta(), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmOfertaAvaliacaoQuestao = OfertaAvaliacaoQuestaoDAO.find(criterios, connect);
				
				ofertaAvaliacaoQuestao.setNrOrdem(rsmOfertaAvaliacaoQuestao.size());
				
				retorno = OfertaAvaliacaoQuestaoDAO.insert(ofertaAvaliacaoQuestao, connect);
				ofertaAvaliacaoQuestao.setCdOfertaAvaliacaoQuestao(retorno);
			}
			else {
				retorno = OfertaAvaliacaoQuestaoDAO.update(ofertaAvaliacaoQuestao, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "OFERTAAVALIACAOQUESTAO", ofertaAvaliacaoQuestao);
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
	public static Result remove(OfertaAvaliacaoQuestao ofertaAvaliacaoQuestao) {
		return remove(ofertaAvaliacaoQuestao.getCdOfertaAvaliacaoQuestao(), ofertaAvaliacaoQuestao.getCdOfertaAvaliacao(), ofertaAvaliacaoQuestao.getCdOferta());
	}
	public static Result remove(int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta){
		return remove(cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, false, null, null);
	}
	public static Result remove(int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, boolean cascade){
		return remove(cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, cascade, null, null);
	}
	public static Result remove(int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, boolean cascade, AuthData authData){
		return remove(cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, cascade, authData, null);
	}
	public static Result remove(int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, boolean cascade, AuthData authData, Connection connect){
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
			
			int ret = connect.prepareStatement("DELETE FROM acd_oferta_avaliacao_questao_alternativa "
					+ "								WHERE cd_oferta_avaliacao_questao = " + cdOfertaAvaliacaoQuestao 
					+ " 							  AND cd_oferta_avaliacao = " + cdOfertaAvaliacao 
					+ " 							  AND cd_oferta = " + cdOferta).executeUpdate();
			if(ret < 0){
				if(isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Erro ao remover alternativas");
			}
			
			OfertaAvaliacaoQuestao ofertaAvaliacaoQuestao = OfertaAvaliacaoQuestaoDAO.get(cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, connect);
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_oferta", "" + ofertaAvaliacaoQuestao.getCdOferta(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_oferta_avaliacao", "" + ofertaAvaliacaoQuestao.getCdOfertaAvaliacao(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = OfertaAvaliacaoQuestaoDAO.find(criterios, connect);
			
			for(int nrOrdem = ofertaAvaliacaoQuestao.getNrOrdem(); nrOrdem < rsm.size(); nrOrdem++){
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_oferta", "" + ofertaAvaliacaoQuestao.getCdOferta(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_oferta_avaliacao", "" + ofertaAvaliacaoQuestao.getCdOfertaAvaliacao(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("nr_ordem", "" + (nrOrdem+1), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmOfertaAvaliacaoQuestao = OfertaAvaliacaoQuestaoDAO.find(criterios, connect);
				if(rsmOfertaAvaliacaoQuestao.next()){
					OfertaAvaliacaoQuestao ofertaAvaliacaoQuestaoMaior = OfertaAvaliacaoQuestaoDAO.get(rsmOfertaAvaliacaoQuestao.getInt("cd_oferta_avaliacao_questao"), cdOfertaAvaliacao, cdOferta, connect);
					ofertaAvaliacaoQuestaoMaior.setNrOrdem(nrOrdem);
					if(OfertaAvaliacaoQuestaoDAO.update(ofertaAvaliacaoQuestaoMaior, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar ofertaAvaliacaoQuestao");
					}
				}
			}
			
			
			if(!cascade || retorno>0)
				retorno = OfertaAvaliacaoQuestaoDAO.delete(cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_oferta_avaliacao_questao");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_oferta_avaliacao_questao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getAllByOfertaAvaliacao(int cdOferta, int cdOfertaAvaliacao) {
		return getAllByOfertaAvaliacao(cdOferta, cdOfertaAvaliacao, 0, null);
	}

	public static ResultSetMap getAllByOfertaAvaliacao(int cdOferta, int cdOfertaAvaliacao, Connection connect) {
		return getAllByOfertaAvaliacao(cdOferta, cdOfertaAvaliacao, 0, null);
	}
	
	public static ResultSetMap getAllByOfertaAvaliacao(int cdOferta, int cdOfertaAvaliacao, int cdOfertaAvaliacaoQuestaoSuperior) {
		return getAllByOfertaAvaliacao(cdOferta, cdOfertaAvaliacao, cdOfertaAvaliacaoQuestaoSuperior, null);
	}

	public static ResultSetMap getAllByOfertaAvaliacao(int cdOferta, int cdOfertaAvaliacao, int cdOfertaAvaliacaoQuestaoSuperior, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_oferta_avaliacao_questao "
					+ "							WHERE cd_oferta = " + cdOferta 
					+ " 						  AND cd_oferta_avaliacao = " + cdOfertaAvaliacao 
					+ (cdOfertaAvaliacaoQuestaoSuperior > 0 ? " AND cd_oferta_avaliacao_questao_superior = " + cdOfertaAvaliacaoQuestaoSuperior : "")
					+ " 					  ORDER BY nr_ordem");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				rsm.setValueToField("CL_QUESTAO", tiposQuestao[rsm.getInt("TP_QUESTAO")]);
				rsm.setValueToField("CL_PESO", Util.formatNumber(rsm.getDouble("VL_PESO"), 2));
				rsm.setValueToField("NR_POSICAO", (rsm.getInt("NR_ORDEM")+1));
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

}