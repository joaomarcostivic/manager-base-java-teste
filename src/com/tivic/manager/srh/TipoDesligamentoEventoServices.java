package com.tivic.manager.srh;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class TipoDesligamentoEventoServices {

	public static Result save(TipoDesligamentoEvento tipoDesligamentoEvento){
		return save(tipoDesligamentoEvento, null);
	}

	public static Result save(TipoDesligamentoEvento tipoDesligamentoEvento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(tipoDesligamentoEvento==null)
				return new Result(-1, "Erro ao salvar. TipoDesligamentoEvento é nulo");

			int retorno;
			if(tipoDesligamentoEvento.getCdTipoDesligamento()==0){
				retorno = TipoDesligamentoEventoDAO.insert(tipoDesligamentoEvento, connect);
				tipoDesligamentoEvento.setCdTipoDesligamento(retorno);
			}
			else {
				retorno = TipoDesligamentoEventoDAO.update(tipoDesligamentoEvento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "TIPODESLIGAMENTOEVENTO", tipoDesligamentoEvento);
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
	public static Result remove(int cdTipoDesligamento, int cdEventoFinanceiro){
		return remove(cdTipoDesligamento, cdEventoFinanceiro, false, null);
	}
	public static Result remove(int cdTipoDesligamento, int cdEventoFinanceiro, boolean cascade){
		return remove(cdTipoDesligamento, cdEventoFinanceiro, cascade, null);
	}
	public static Result remove(int cdTipoDesligamento, int cdEventoFinanceiro, boolean cascade, Connection connect){
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
			retorno = TipoDesligamentoEventoDAO.delete(cdTipoDesligamento, cdEventoFinanceiro, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_tipo_desligamento_evento");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDesligamentoEventoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDesligamentoEventoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM srh_tipo_desligamento_evento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
