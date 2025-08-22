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

public class OfertaAvaliacaoQuestaoPlanoTopicoServices {

	public static Result save(OfertaAvaliacaoQuestaoPlanoTopico ofertaAvaliacaoQuestaoPlanoTopico){
		return save(ofertaAvaliacaoQuestaoPlanoTopico, null, null);
	}

	public static Result save(OfertaAvaliacaoQuestaoPlanoTopico ofertaAvaliacaoQuestaoPlanoTopico, AuthData authData){
		return save(ofertaAvaliacaoQuestaoPlanoTopico, authData, null);
	}

	public static Result save(OfertaAvaliacaoQuestaoPlanoTopico ofertaAvaliacaoQuestaoPlanoTopico, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(ofertaAvaliacaoQuestaoPlanoTopico==null)
				return new Result(-1, "Erro ao salvar. OfertaAvaliacaoQuestaoPlanoTopico é nulo");

			int retorno;
			if(OfertaAvaliacaoQuestaoPlanoTopicoDAO.get(ofertaAvaliacaoQuestaoPlanoTopico.getCdOfertaAvaliacaoQuestao(), ofertaAvaliacaoQuestaoPlanoTopico.getCdOfertaAvaliacao(), ofertaAvaliacaoQuestaoPlanoTopico.getCdOferta(), ofertaAvaliacaoQuestaoPlanoTopico.getCdPlano(), ofertaAvaliacaoQuestaoPlanoTopico.getCdSecao(), ofertaAvaliacaoQuestaoPlanoTopico.getCdTopico(), connect)==null){
				retorno = OfertaAvaliacaoQuestaoPlanoTopicoDAO.insert(ofertaAvaliacaoQuestaoPlanoTopico, connect);
				ofertaAvaliacaoQuestaoPlanoTopico.setCdOfertaAvaliacaoQuestao(retorno);
			}
			else {
				retorno = OfertaAvaliacaoQuestaoPlanoTopicoDAO.update(ofertaAvaliacaoQuestaoPlanoTopico, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "OFERTAAVALIACAOQUESTAOPLANOTOPICO", ofertaAvaliacaoQuestaoPlanoTopico);
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
	public static Result remove(OfertaAvaliacaoQuestaoPlanoTopico ofertaAvaliacaoQuestaoPlanoTopico) {
		return remove(ofertaAvaliacaoQuestaoPlanoTopico.getCdOfertaAvaliacaoQuestao(), ofertaAvaliacaoQuestaoPlanoTopico.getCdOfertaAvaliacao(), ofertaAvaliacaoQuestaoPlanoTopico.getCdOferta(), ofertaAvaliacaoQuestaoPlanoTopico.getCdPlano(), ofertaAvaliacaoQuestaoPlanoTopico.getCdSecao(), ofertaAvaliacaoQuestaoPlanoTopico.getCdTopico());
	}
	public static Result remove(int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, int cdPlano, int cdSecao, int cdTopico){
		return remove(cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, cdPlano, cdSecao, cdTopico, false, null, null);
	}
	public static Result remove(int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, int cdPlano, int cdSecao, int cdTopico, boolean cascade){
		return remove(cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, cdPlano, cdSecao, cdTopico, cascade, null, null);
	}
	public static Result remove(int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, int cdPlano, int cdSecao, int cdTopico, boolean cascade, AuthData authData){
		return remove(cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, cdPlano, cdSecao, cdTopico, cascade, authData, null);
	}
	public static Result remove(int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, int cdPlano, int cdSecao, int cdTopico, boolean cascade, AuthData authData, Connection connect){
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
			retorno = OfertaAvaliacaoQuestaoPlanoTopicoDAO.delete(cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, cdPlano, cdSecao, cdTopico, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_oferta_avaliacao_questao_plano_topico");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoPlanoTopicoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoPlanoTopicoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_oferta_avaliacao_questao_plano_topico", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap getAllByOfertaAvaliacaoQuestao(int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta) {
		return getAllByOfertaAvaliacaoQuestao(cdOfertaAvaliacaoQuestao, cdOfertaAvaliacao, cdOferta, null);
	}

	public static ResultSetMap getAllByOfertaAvaliacaoQuestao(int cdOfertaAvaliacaoQuestao, int cdOfertaAvaliacao, int cdOferta, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_oferta_avaliacao_questao_plano_topico A "
					+ "							 JOIN acd_oferta_avaliacao_questao B ON (A.cd_oferta_avaliacao_questao = B.cd_oferta_avaliacao_questao"
					+ "																	  	AND A.cd_oferta_avaliacao = B.cd_oferta_avaliacao"
					+ "																		AND A.cd_oferta = B.cd_oferta) "
					+ "							 JOIN acd_plano_topico C ON (A.cd_plano = C.cd_plano"
					+ "														  AND A.cd_secao = C.cd_secao"
					+ "														  AND A.cd_topico = C.cd_topico) "
					+ "							WHERE A.cd_oferta_avaliacao_questao = " + cdOfertaAvaliacaoQuestao
					+ " 						  AND A.cd_oferta_avaliacao = " + cdOfertaAvaliacao 
					+ " 						  AND A.cd_oferta = " + cdOferta);
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoPlanoTopicoServices.getAllByOfertaAvaliacaoQuestao: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! OfertaAvaliacaoQuestaoPlanoTopicoServices.getAllByOfertaAvaliacaoQuestao: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	
	
	
}