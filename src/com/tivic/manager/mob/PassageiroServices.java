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

public class PassageiroServices {

	public static Result save(Passageiro passageiro){
		return save(passageiro, null, null);
	}

	public static Result save(Passageiro passageiro, AuthData authData){
		return save(passageiro, authData, null);
	}

	public static Result save(Passageiro passageiro, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(passageiro==null)
				return new Result(-1, "Erro ao salvar. Passageiro é nulo");

			int retorno;
			if(passageiro.getCdConcessionarioPessoa()==0){
				retorno = PassageiroDAO.insert(passageiro, connect);
				passageiro.setCdConcessionarioPessoa(retorno);
			}
			else {
				retorno = PassageiroDAO.update(passageiro, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PASSAGEIRO", passageiro);
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
	public static Result remove(Passageiro passageiro) {
		return remove(passageiro.getCdConcessionarioPessoa(), passageiro.getCdHorario(), passageiro.getCdTabelaHorario(), passageiro.getCdLinha(), passageiro.getCdRota(), passageiro.getCdTrecho());
	}
	public static Result remove(int cdConcessionarioPessoa, int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdTrecho){
		return remove(cdConcessionarioPessoa, cdHorario, cdTabelaHorario, cdLinha, cdRota, cdTrecho, false, null, null);
	}
	public static Result remove(int cdConcessionarioPessoa, int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdTrecho, boolean cascade){
		return remove(cdConcessionarioPessoa, cdHorario, cdTabelaHorario, cdLinha, cdRota, cdTrecho, cascade, null, null);
	}
	public static Result remove(int cdConcessionarioPessoa, int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdTrecho, boolean cascade, AuthData authData){
		return remove(cdConcessionarioPessoa, cdHorario, cdTabelaHorario, cdLinha, cdRota, cdTrecho, cascade, authData, null);
	}
	public static Result remove(int cdConcessionarioPessoa, int cdHorario, int cdTabelaHorario, int cdLinha, int cdRota, int cdTrecho, boolean cascade, AuthData authData, Connection connect){
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
			retorno = PassageiroDAO.delete(cdConcessionarioPessoa, cdHorario, cdTabelaHorario, cdLinha, cdRota, cdTrecho, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_passageiro");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PassageiroServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PassageiroServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_passageiro", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}