package com.tivic.manager.bpm;

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

public class MarcaAgrupamentoServices {

	public static Result save(MarcaAgrupamento marcaAgrupamento){
		return save(marcaAgrupamento, null, null);
	}

	public static Result save(MarcaAgrupamento marcaAgrupamento, AuthData authData){
		return save(marcaAgrupamento, authData, null);
	}

	public static Result save(MarcaAgrupamento marcaAgrupamento, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(marcaAgrupamento==null)
				return new Result(-1, "Erro ao salvar. MarcaAgrupamento é nulo");

			int retorno;
			if(marcaAgrupamento.getCdGrupo()==0){
				retorno = MarcaAgrupamentoDAO.insert(marcaAgrupamento, connect);
				marcaAgrupamento.setCdGrupo(retorno);
			}
			else {
				retorno = MarcaAgrupamentoDAO.update(marcaAgrupamento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "MARCAAGRUPAMENTO", marcaAgrupamento);
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
	public static Result remove(MarcaAgrupamento marcaAgrupamento) {
		return remove(marcaAgrupamento.getCdGrupo(), marcaAgrupamento.getCdMarca());
	}
	public static Result remove(int cdGrupo, int cdMarca){
		return remove(cdGrupo, cdMarca, false, null, null);
	}
	public static Result remove(int cdGrupo, int cdMarca, boolean cascade){
		return remove(cdGrupo, cdMarca, cascade, null, null);
	}
	public static Result remove(int cdGrupo, int cdMarca, boolean cascade, AuthData authData){
		return remove(cdGrupo, cdMarca, cascade, authData, null);
	}
	public static Result remove(int cdGrupo, int cdMarca, boolean cascade, AuthData authData, Connection connect){
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
			retorno = MarcaAgrupamentoDAO.delete(cdGrupo, cdMarca, connect);
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
			pstmt = connect.prepareStatement("SELECT bma.* FROM bpm_marca_agrupamento bma "+
					 							" LEFT JOIN marca m ONn (m.cd_marca = bma.cd_marca) ");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MarcaAgrupamentoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MarcaAgrupamentoServices.getAll: " + e);
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
		return Search.find("SELECT * FROM bpm_marca_agrupamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
