package com.tivic.manager.sinc;

import java.sql.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class LoteRegistroLocalServices {

	public static Result save(LoteRegistroLocal loteRegistroLocal){
		return save(loteRegistroLocal, null);
	}

	public static Result save(LoteRegistroLocal loteRegistroLocal, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(loteRegistroLocal==null)
				return new Result(-1, "Erro ao salvar. LoteRegistroLocal é nulo");

			int retorno;
			if(loteRegistroLocal.getCdLoteRegistro()==0){
				retorno = LoteRegistroLocalDAO.insert(loteRegistroLocal, connect);
				loteRegistroLocal.setCdLoteRegistro(retorno);
			}
			else {
				retorno = LoteRegistroLocalDAO.update(loteRegistroLocal, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "LOTEREGISTROLOCAL", loteRegistroLocal);
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
	public static Result remove(int cdLoteRegistro, int cdLote, int cdLocal){
		return remove(cdLoteRegistro, cdLote, cdLocal, false, null);
	}
	public static Result remove(int cdLoteRegistro, int cdLote, int cdLocal, boolean cascade){
		return remove(cdLoteRegistro, cdLote, cdLocal, cascade, null);
	}
	public static Result remove(int cdLoteRegistro, int cdLote, int cdLocal, boolean cascade, Connection connect){
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
			retorno = LoteRegistroLocalDAO.delete(cdLoteRegistro, cdLote, cdLocal, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM sinc_lote_registro_local");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroLocalServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroLocalServices.getAll: " + e);
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
		return Search.find("SELECT A.*, C.* FROM sinc_lote_registro_local A "
				+ "			JOIN sinc_local B ON (A.cd_local = B.cd_local) "
				+ "			JOIN sinc_lote_registro C ON (A.cd_local = C.cd_lote_registro) ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
