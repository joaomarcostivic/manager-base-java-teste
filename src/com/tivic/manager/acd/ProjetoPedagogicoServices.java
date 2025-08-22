package com.tivic.manager.acd;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.seg.AuthData;

public class ProjetoPedagogicoServices {

	public static Result save(ProjetoPedagogico projetoPedagogico){
		return save(projetoPedagogico, null, null);
	}

	public static Result save(ProjetoPedagogico projetoPedagogico, AuthData authData){
		return save(projetoPedagogico, authData, null);
	}

	public static Result save(ProjetoPedagogico projetoPedagogico, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(projetoPedagogico==null)
				return new Result(-1, "Erro ao salvar. ProjetoPedagogico é nulo");

			int retorno;
			if(projetoPedagogico.getCdProjetoPedagogico()==0){
				projetoPedagogico.setDtCadastro(new GregorianCalendar());
				retorno = ProjetoPedagogicoDAO.insert(projetoPedagogico, connect);
				projetoPedagogico.setCdProjetoPedagogico(retorno);
			}
			else {
				retorno = ProjetoPedagogicoDAO.update(projetoPedagogico, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PROJETOPEDAGOGICO", projetoPedagogico);
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
	public static Result remove(int cdProjetoPedagogico, int cdInstituicao){
		return remove(cdProjetoPedagogico, cdInstituicao, false, null);
	}
	public static Result remove(int cdProjetoPedagogico, int cdInstituicao, boolean cascade){
		return remove(cdProjetoPedagogico, cdInstituicao, cascade, null);
	}
	public static Result remove(int cdProjetoPedagogico, int cdInstituicao, boolean cascade, Connection connect){
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
			retorno = ProjetoPedagogicoDAO.delete(cdProjetoPedagogico, cdInstituicao, connect);
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
			pstmt = connect.prepareStatement(""
					+ "SELECT A.*, C.nm_pessoa AS nm_instituicao, D.nm_periodo_letivo "
					+ "FROM acd_projeto_pedagogico A "
					+ "LEFT OUTER JOIN acd_instituicao B ON (A.cd_instituicao = B.cd_instituicao) "
					+ "LEFT OUTER JOIN grl_pessoa C ON (B.cd_instituicao = C.cd_pessoa) "
					+ "LEFT OUTER JOIN acd_instituicao_periodo D ON (A.cd_instituicao = D.cd_instituicao AND A.cd_periodo_letivo = D.cd_periodo_letivo)"
					+ "");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProjetoPedagogicoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProjetoPedagogicoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_projeto_pedagogico", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}