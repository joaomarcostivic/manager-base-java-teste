package com.tivic.manager.sinc;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

public class LoteRegistroServices {

	public static final int TP_INSERT = 0;
	public static final int TP_UPDATE = 1;
	public static final int TP_DELETE = 2;
	
	public static Result save(LoteRegistro loteRegistro){
		return save(loteRegistro, null);
	}

	public static Result save(LoteRegistro loteRegistro, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(loteRegistro==null)
				return new Result(-1, "Erro ao salvar. LoteRegistro é nulo");

			int retorno;
			if(loteRegistro.getCdLoteRegistro()==0){
				retorno = LoteRegistroDAO.insert(loteRegistro, connect);
				loteRegistro.setCdLoteRegistro(retorno);
			}
			else {
				retorno = LoteRegistroDAO.update(loteRegistro, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "LOTEREGISTRO", loteRegistro);
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
	public static Result remove(int cdLoteRegistro){
		return remove(cdLoteRegistro, false, null);
	}
	public static Result remove(int cdLoteRegistro, boolean cascade){
		return remove(cdLoteRegistro, cascade, null);
	}
	public static Result remove(int cdLoteRegistro, boolean cascade, Connection connect){
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
			retorno = LoteRegistroDAO.delete(cdLoteRegistro, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM sinc_lote_registro");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteRegistroServices.getAll: " + e);
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
		
		return Search.find("SELECT * FROM sinc_lote_registro ",  "ORDER BY cd_lote_registro", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static HashMap<String, Object> getRegister(LoteRegistro loteRegistro){
		
		HashMap<String, Object> register = new HashMap<String, Object>();
		
		register.put("cd_lote_registro", loteRegistro.getCdLoteRegistro());
		register.put("cd_lote", loteRegistro.getCdLote());
		register.put("cd_tabela", loteRegistro.getCdTabela());
		register.put("dt_atualizacao", loteRegistro.getDtAtualizacao());
		register.put("id_origem", loteRegistro.getIdOrigem());
		register.put("nm_campo_alterado", loteRegistro.getNmCampoAlterado());
		register.put("nm_chave_referencia", loteRegistro.getNmChaveReferencia());
		register.put("nm_valor_chaves", loteRegistro.getNmValorChaves());
		register.put("tp_atualizacao", loteRegistro.getTpAtualizacao());
		
		
		return register;
	}

}
