package com.tivic.manager.acd;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class OfertaAvaliacaoQuestaoAlternativaServices {

	public static Result save(OfertaAvaliacaoQuestaoAlternativa ofertaAvaliacaoQuestaoAlternativa){
		return save(ofertaAvaliacaoQuestaoAlternativa, null, null);
	}

	public static Result save(OfertaAvaliacaoQuestaoAlternativa ofertaAvaliacaoQuestaoAlternativa, AuthData authData){
		return save(ofertaAvaliacaoQuestaoAlternativa, authData, null);
	}

	public static Result save(OfertaAvaliacaoQuestaoAlternativa ofertaAvaliacaoQuestaoAlternativa, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(ofertaAvaliacaoQuestaoAlternativa==null)
				return new Result(-1, "Erro ao salvar. OfertaAvaliacaoQuestaoAlternativa é nulo");

			
			int retorno;
			if(ofertaAvaliacaoQuestaoAlternativa.getCdAlternativa()==0){
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_oferta", "" + ofertaAvaliacaoQuestaoAlternativa.getCdOferta(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_oferta_avaliacao", "" + ofertaAvaliacaoQuestaoAlternativa.getCdOfertaAvaliacao(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_oferta_avaliacao_questao", "" + ofertaAvaliacaoQuestaoAlternativa.getCdOfertaAvaliacaoQuestao(), ItemComparator.EQUAL, Types.INTEGER));
				ofertaAvaliacaoQuestaoAlternativa.setNrOrdem(OfertaAvaliacaoQuestaoAlternativaDAO.find(criterios, connect).size());
				
				retorno = OfertaAvaliacaoQuestaoAlternativaDAO.insert(ofertaAvaliacaoQuestaoAlternativa, connect);
				ofertaAvaliacaoQuestaoAlternativa.setCdAlternativa(retorno);
			}
			else {
				retorno = OfertaAvaliacaoQuestaoAlternativaDAO.update(ofertaAvaliacaoQuestaoAlternativa, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "OFERTAAVALIACAOQUESTAOALTERNATIVA", ofertaAvaliacaoQuestaoAlternativa);
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
	public static Result remove(OfertaAvaliacaoQuestaoAlternativa ofertaAvaliacaoQuestaoAlternativa) {
		return remove(ofertaAvaliacaoQuestaoAlternativa.getCdAlternativa(), ofertaAvaliacaoQuestaoAlternativa.getCdOfertaAvaliacaoQuestao(), ofertaAvaliacaoQuestaoAlternativa.getCdOfertaAvaliacao(), ofertaAvaliacaoQuestaoAlternativa.getCdOferta());
	}
	public static Result remove(int cdAlternativa, int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta){
		return remove(cdAlternativa, cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, false, null, null);
	}
	public static Result remove(int cdAlternativa, int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, boolean cascade){
		return remove(cdAlternativa, cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, cascade, null, null);
	}
	public static Result remove(int cdAlternativa, int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, boolean cascade, AuthData authData){
		return remove(cdAlternativa, cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, cascade, authData, null);
	}
	public static Result remove(int cdAlternativa, int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, boolean cascade, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = 0;
			
			OfertaAvaliacaoQuestaoAlternativa ofertaAvaliacaoQuestaoAlternativa = OfertaAvaliacaoQuestaoAlternativaDAO.get(cdAlternativa, cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, connect);
			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("cd_oferta", "" + ofertaAvaliacaoQuestaoAlternativa.getCdOferta(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_oferta_avaliacao", "" + ofertaAvaliacaoQuestaoAlternativa.getCdOfertaAvaliacao(), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("cd_oferta_avaliacao_questao", "" + ofertaAvaliacaoQuestaoAlternativa.getCdOfertaAvaliacaoQuestao(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsm = OfertaAvaliacaoQuestaoAlternativaDAO.find(criterios, connect);
			
			for(int nrOrdem = ofertaAvaliacaoQuestaoAlternativa.getNrOrdem(); nrOrdem < rsm.size(); nrOrdem++){
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_oferta", "" + ofertaAvaliacaoQuestaoAlternativa.getCdOferta(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_oferta_avaliacao", "" + ofertaAvaliacaoQuestaoAlternativa.getCdOfertaAvaliacao(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_oferta_avaliacao_questao", "" + ofertaAvaliacaoQuestaoAlternativa.getCdOfertaAvaliacaoQuestao(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("nr_ordem", "" + (nrOrdem+1), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmOfertaAvaliacaoQuestaoAlternativa = OfertaAvaliacaoQuestaoAlternativaDAO.find(criterios, connect);
				if(rsmOfertaAvaliacaoQuestaoAlternativa.next()){
					OfertaAvaliacaoQuestaoAlternativa ofertaAvaliacaoQuestaoAlternativaMaior = OfertaAvaliacaoQuestaoAlternativaDAO.get(rsmOfertaAvaliacaoQuestaoAlternativa.getInt("cd_alternativa"), cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, connect);
					ofertaAvaliacaoQuestaoAlternativaMaior.setNrOrdem(nrOrdem);
					if(OfertaAvaliacaoQuestaoAlternativaDAO.update(ofertaAvaliacaoQuestaoAlternativaMaior, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar ofertaAvaliacaoQuestaoAlternativa");
					}
				}
			}
			
			if(cascade){
				/** SE HOUVER, REMOVER TABELAS ASSOCIADAS **/
				retorno = 1;
			}
			if(!cascade || retorno>0)
				retorno = OfertaAvaliacaoQuestaoAlternativaDAO.delete(cdAlternativa, cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_oferta_avaliacao_questao_alternativa");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoAlternativaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoAlternativaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_oferta_avaliacao_questao_alternativa", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getAllByQuestao(int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta) {
		return getAllByQuestao(cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, null);
	}

	public static ResultSetMap getAllByQuestao(int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_oferta_avaliacao_questao_alternativa WHERE cd_oferta_avaliacao_questao = " + cdOfertaAvaliacaoQuestao + " AND cd_oferta_avaliacao = " + cdOfertaAvaliacao + " AND cd_oferta = " + cdOferta + " ORDER BY nr_ordem");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				rsm.setValueToField("NM_LG_CORRETA", (rsm.getInt("LG_CORRETA")==0?"Não":"Sim"));
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoAlternativaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoAlternativaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}