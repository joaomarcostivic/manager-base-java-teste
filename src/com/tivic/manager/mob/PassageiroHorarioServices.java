package com.tivic.manager.mob;

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

public class PassageiroHorarioServices {

	public static Result save(PassageiroHorario passageiroHorario){
		return save(passageiroHorario, null, null);
	}

	public static Result save(PassageiroHorario passageiroHorario, AuthData authData){
		return save(passageiroHorario, authData, null);
	}

	public static Result save(PassageiroHorario passageiroHorario, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(passageiroHorario==null)
				return new Result(-1, "Erro ao salvar. PassageiroHorario é nulo");

			int retorno;
			if(passageiroHorario.getCdConcessionarioPessoa()==0){
				retorno = PassageiroHorarioDAO.insert(passageiroHorario, connect);
				passageiroHorario.setCdConcessionarioPessoa(retorno);
			}
			else {
				retorno = PassageiroHorarioDAO.update(passageiroHorario, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PASSAGEIROHORARIO", passageiroHorario);
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
	public static Result remove(PassageiroHorario passageiroHorario) {
		return remove(passageiroHorario.getCdConcessionarioPessoa(), passageiroHorario.getCdHorario(), passageiroHorario.getCdTabelaHorario(), passageiroHorario.getCdLinha(), passageiroHorario.getCdRota(), passageiroHorario.getCdTrecho(), passageiroHorario.getCdControle());
	}
	public static Result remove(int cdConcessionarioPessoa, int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdTrecho, int cdControle){
		return remove(cdConcessionarioPessoa, cdHorario, cdTabelaHorario, cdLinha, cdRota, cdTrecho, cdControle, false, null, null);
	}
	public static Result remove(int cdConcessionarioPessoa, int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdTrecho, int cdControle, boolean cascade){
		return remove(cdConcessionarioPessoa, cdHorario, cdTabelaHorario, cdLinha, cdRota, cdTrecho, cdControle, cascade, null, null);
	}
	public static Result remove(int cdConcessionarioPessoa, int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdTrecho, int cdControle, boolean cascade, AuthData authData){
		return remove(cdConcessionarioPessoa, cdHorario, cdTabelaHorario, cdLinha, cdRota, cdTrecho, cdControle, cascade, authData, null);
	}
	public static Result remove(int cdConcessionarioPessoa, int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdTrecho, int cdControle, boolean cascade, AuthData authData, Connection connect){
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
			retorno = PassageiroHorarioDAO.delete(cdConcessionarioPessoa, cdHorario, cdTabelaHorario, cdLinha, cdRota, cdTrecho, cdControle, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_passageiro_horario");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PassageiroHorarioServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PassageiroHorarioServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_passageiro_horario", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}