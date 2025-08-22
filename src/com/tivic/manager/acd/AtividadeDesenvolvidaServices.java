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

public class AtividadeDesenvolvidaServices {

	public static Result save(AtividadeDesenvolvida atividadeDesenvolvida){
		return save(atividadeDesenvolvida, null, null);
	}

	public static Result save(AtividadeDesenvolvida atividadeDesenvolvida, AuthData authData){
		return save(atividadeDesenvolvida, authData, null);
	}

	public static Result save(AtividadeDesenvolvida atividadeDesenvolvida, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(atividadeDesenvolvida==null)
				return new Result(-1, "Erro ao salvar. AtividadeDesenvolvida é nulo");

			int retorno;
			if(atividadeDesenvolvida.getCdAtividadeDesenvolvida()==0){
				retorno = AtividadeDesenvolvidaDAO.insert(atividadeDesenvolvida, connect);
				atividadeDesenvolvida.setCdAtividadeDesenvolvida(retorno);
			}
			else {
				retorno = AtividadeDesenvolvidaDAO.update(atividadeDesenvolvida, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ATIVIDADEDESENVOLVIDA", atividadeDesenvolvida);
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
	public static Result remove(AtividadeDesenvolvida atividadeDesenvolvida) {
		return remove(atividadeDesenvolvida.getCdAtividadeDesenvolvida());
	}
	public static Result remove(int cdAtividadeDesenvolvida){
		return remove(cdAtividadeDesenvolvida, false, null, null);
	}
	public static Result remove(int cdAtividadeDesenvolvida, boolean cascade){
		return remove(cdAtividadeDesenvolvida, cascade, null, null);
	}
	public static Result remove(int cdAtividadeDesenvolvida, boolean cascade, AuthData authData){
		return remove(cdAtividadeDesenvolvida, cascade, authData, null);
	}
	public static Result remove(int cdAtividadeDesenvolvida, boolean cascade, AuthData authData, Connection connect){
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
			retorno = AtividadeDesenvolvidaDAO.delete(cdAtividadeDesenvolvida, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_atividade_desenvolvida");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtividadeDesenvolvidaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtividadeDesenvolvidaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_atividade_desenvolvida", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap getAllByProfessorInstituicao(int cdProfessor, int cdInstituicao) {
		return getAllByProfessorInstituicao(cdProfessor, cdInstituicao, null);
	}

	public static ResultSetMap getAllByProfessorInstituicao(int cdProfessor, int cdInstituicao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			
			int cdPeriodoLetivoAtual = 0;
			ResultSetMap rsmPeriodoLetivoAtual = InstituicaoPeriodoServices.getPeriodoAtualOfInstituicao(cdInstituicao, connect);
			while(rsmPeriodoLetivoAtual.next()) {
				cdPeriodoLetivoAtual = rsmPeriodoLetivoAtual.getInt("cd_periodo_letivo");
			}
			
			
			pstmt = connect.prepareStatement("SELECT * FROM acd_atividade_desenvolvida ATV"
					+ "							LEFT OUTER JOIN acd_turma T ON (ATV.cd_turma = T.cd_turma)"
					+ "							LEFT OUTER JOIN acd_oferta O ON (ATV.cd_oferta = O.cd_oferta)"
					+ "						  WHERE ATV.cd_professor = " + cdProfessor
					+ "							AND (T.cd_periodo_letivo = "+cdPeriodoLetivoAtual+" OR O.cd_periodo_letivo = "+cdPeriodoLetivoAtual+")");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtividadeDesenvolvidaServices.getAllByProfessorInstituicao: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtividadeDesenvolvidaServices.getAllByProfessorInstituicao: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByOferta(int cdOferta ) {
		return getAllByOferta(cdOferta, null);
	}

	public static ResultSetMap getAllByOferta(int cdOferta, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, DIS.nm_produto_servico AS nm_disciplina FROM acd_atividade_desenvolvida A, acd_oferta O, grl_produto_servico DIS "+
											 "WHERE A.cd_oferta = "+cdOferta +
											 "  AND A.cd_oferta = O.cd_oferta" +
											 "  AND O.cd_disciplina = DIS.cd_produto_servico");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AtividadeDesenvolvidaServices.getAllByOferta: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AtividadeDesenvolvidaServices.getAllByOferta: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}