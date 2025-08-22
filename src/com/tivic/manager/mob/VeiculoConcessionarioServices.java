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

public class VeiculoConcessionarioServices {

	public static Result save(VeiculoConcessionario veiculoConcessionario){
		return save(veiculoConcessionario, null, null);
	}

	public static Result save(VeiculoConcessionario veiculoConcessionario, AuthData authData){
		return save(veiculoConcessionario, authData, null);
	}

	public static Result save(VeiculoConcessionario veiculoConcessionario, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(veiculoConcessionario==null)
				return new Result(-1, "Erro ao salvar. VeiculoConcessionario é nulo");

			int retorno;
			if(veiculoConcessionario.getCdConcessaoVeiculo()==0){
				retorno = VeiculoConcessionarioDAO.insert(veiculoConcessionario, connect);
				veiculoConcessionario.setCdConcessaoVeiculo(retorno);
			}
			else {
				retorno = VeiculoConcessionarioDAO.update(veiculoConcessionario, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "VEICULOCONCESSIONARIO", veiculoConcessionario);
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
	public static Result remove(VeiculoConcessionario veiculoConcessionario) {
		return remove(veiculoConcessionario.getCdConcessaoVeiculo(), veiculoConcessionario.getCdConcessionarioPessoa());
	}
	public static Result remove(int cdConcessaoVeiculo, int cdConcessionarioPessoa){
		return remove(cdConcessaoVeiculo, cdConcessionarioPessoa, false, null, null);
	}
	public static Result remove(int cdConcessaoVeiculo, int cdConcessionarioPessoa, boolean cascade){
		return remove(cdConcessaoVeiculo, cdConcessionarioPessoa, cascade, null, null);
	}
	public static Result remove(int cdConcessaoVeiculo, int cdConcessionarioPessoa, boolean cascade, AuthData authData){
		return remove(cdConcessaoVeiculo, cdConcessionarioPessoa, cascade, authData, null);
	}
	public static Result remove(int cdConcessaoVeiculo, int cdConcessionarioPessoa, boolean cascade, AuthData authData, Connection connect){
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
			retorno = VeiculoConcessionarioDAO.delete(cdConcessaoVeiculo, cdConcessionarioPessoa, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_veiculo_concessionario");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoConcessionarioServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoConcessionarioServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_veiculo_concessionario", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}