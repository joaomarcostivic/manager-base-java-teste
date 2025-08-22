package com.tivic.manager.grl;

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

public class TipoOcorrenciaPessoaServices {

	public static Result save(TipoOcorrenciaPessoa tipoOcorrenciaPessoa){
		return save(tipoOcorrenciaPessoa, null, null);
	}

	public static Result save(TipoOcorrenciaPessoa tipoOcorrenciaPessoa, AuthData authData){
		return save(tipoOcorrenciaPessoa, authData, null);
	}

	public static Result save(TipoOcorrenciaPessoa tipoOcorrenciaPessoa, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tipoOcorrenciaPessoa==null)
				return new Result(-1, "Erro ao salvar. TipoOcorrenciaPessoa é nulo");

			int retorno;
			if(tipoOcorrenciaPessoa.getCdTipoOcorrencia()==0){
				retorno = TipoOcorrenciaPessoaDAO.insert(tipoOcorrenciaPessoa, connect);
				tipoOcorrenciaPessoa.setCdTipoOcorrencia(retorno);
			}
			else {
				retorno = TipoOcorrenciaPessoaDAO.update(tipoOcorrenciaPessoa, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPOOCORRENCIAPESSOA", tipoOcorrenciaPessoa);
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
	public static Result remove(TipoOcorrenciaPessoa tipoOcorrenciaPessoa) {
		return remove(tipoOcorrenciaPessoa.getCdTipoOcorrencia());
	}
	public static Result remove(int cdTipoOcorrencia){
		return remove(cdTipoOcorrencia, false, null, null);
	}
	public static Result remove(int cdTipoOcorrencia, boolean cascade){
		return remove(cdTipoOcorrencia, cascade, null, null);
	}
	public static Result remove(int cdTipoOcorrencia, boolean cascade, AuthData authData){
		return remove(cdTipoOcorrencia, cascade, authData, null);
	}
	public static Result remove(int cdTipoOcorrencia, boolean cascade, AuthData authData, Connection connect){
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
			retorno = TipoOcorrenciaPessoaDAO.delete(cdTipoOcorrencia, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_ocorrencia_pessoa");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaPessoaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoOcorrenciaPessoaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_tipo_ocorrencia_pessoa", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
