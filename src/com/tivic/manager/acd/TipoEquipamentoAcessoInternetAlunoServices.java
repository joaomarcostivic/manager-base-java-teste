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

public class TipoEquipamentoAcessoInternetAlunoServices {

	public static Result save(TipoEquipamentoAcessoInternetAluno tipoEquipamentoAcessoInternetAluno){
		return save(tipoEquipamentoAcessoInternetAluno, null, null);
	}

	public static Result save(TipoEquipamentoAcessoInternetAluno tipoEquipamentoAcessoInternetAluno, AuthData authData){
		return save(tipoEquipamentoAcessoInternetAluno, authData, null);
	}

	public static Result save(TipoEquipamentoAcessoInternetAluno tipoEquipamentoAcessoInternetAluno, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tipoEquipamentoAcessoInternetAluno==null)
				return new Result(-1, "Erro ao salvar. TipoEquipamentoAcessoInternetAluno é nulo");

			int retorno;
			if(tipoEquipamentoAcessoInternetAluno.getCdTipoEquipamentoAcessoInternetAluno()==0){
				retorno = TipoEquipamentoAcessoInternetAlunoDAO.insert(tipoEquipamentoAcessoInternetAluno, connect);
				tipoEquipamentoAcessoInternetAluno.setCdTipoEquipamentoAcessoInternetAluno(retorno);
			}
			else {
				retorno = TipoEquipamentoAcessoInternetAlunoDAO.update(tipoEquipamentoAcessoInternetAluno, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOEQUIPAMENTOACESSOINTERNETALUNO", tipoEquipamentoAcessoInternetAluno);
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
	public static Result remove(TipoEquipamentoAcessoInternetAluno tipoEquipamentoAcessoInternetAluno) {
		return remove(tipoEquipamentoAcessoInternetAluno.getCdTipoEquipamentoAcessoInternetAluno());
	}
	public static Result remove(int cdTipoEquipamentoAcessoInternetAluno){
		return remove(cdTipoEquipamentoAcessoInternetAluno, false, null, null);
	}
	public static Result remove(int cdTipoEquipamentoAcessoInternetAluno, boolean cascade){
		return remove(cdTipoEquipamentoAcessoInternetAluno, cascade, null, null);
	}
	public static Result remove(int cdTipoEquipamentoAcessoInternetAluno, boolean cascade, AuthData authData){
		return remove(cdTipoEquipamentoAcessoInternetAluno, cascade, authData, null);
	}
	public static Result remove(int cdTipoEquipamentoAcessoInternetAluno, boolean cascade, AuthData authData, Connection connect){
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
			retorno = TipoEquipamentoAcessoInternetAlunoDAO.delete(cdTipoEquipamentoAcessoInternetAluno, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_tipo_equipamento_acesso_internet_aluno");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoAcessoInternetAlunoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoEquipamentoAcessoInternetAlunoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM acd_tipo_equipamento_acesso_internet_aluno", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}