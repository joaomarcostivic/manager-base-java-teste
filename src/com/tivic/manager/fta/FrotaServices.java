package com.tivic.manager.fta;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

public class FrotaServices {
	
	public static final int ST_INATIVO = 0;
	public static final int ST_ATIVO   = 1;

	public static String[] situacaoVeiculo = {"Inativo","Ativo"};
	
	public static final int TP_PUBLICO = 0;
	public static final int TP_PARTICULAR   = 1;

	public static String[] tipoVeiculo = {"Público","Particular"};

	public static Result save(Frota frota){
		return save(frota, null);
	}

	public static Result save(Frota frota, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(frota==null)
				return new Result(-1, "Erro ao salvar. Frota é nulo");

			int retorno;
			if(frota.getCdFrota()==0){
				retorno = FrotaDAO.insert(frota, connect);
				frota.setCdFrota(retorno);
			}
			else {
				retorno = FrotaDAO.update(frota, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "FROTA", frota);
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
	public static Result remove(int cdFrota){
		return remove(cdFrota, false, null);
	}
	public static Result remove(int cdFrota, boolean cascade){
		return remove(cdFrota, cascade, null);
	}
	public static Result remove(int cdFrota, boolean cascade, Connection connect){
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
			retorno = FrotaDAO.delete(cdFrota, connect);
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
	
	public static ResultSetMap get(int cdFrota) {
		return get(cdFrota, null);
	}
	
	public static ResultSetMap get(int cdFrota, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("CD_FROTA", String.valueOf(cdFrota), ItemComparator.EQUAL, java.sql.Types.INTEGER));
			ResultSetMap rsm = find(criterios, connect);
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FrotaServices.get: " + e);
			return null;
		}
		finally {
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
			pstmt = connect.prepareStatement(
					"SELECT A.*, B.NM_PESSOA AS NM_RESPONSAVEL, C.NM_PESSOA AS NM_PROPRIETARIO "+
					"FROM fta_frota A "+
					"LEFT OUTER JOIN grl_pessoa B ON (A.CD_RESPONSAVEL = B.CD_PESSOA) "+
					"LEFT OUTER JOIN grl_pessoa C ON (A.CD_PROPRIETARIO = C.CD_PESSOA) ");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FrotaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FrotaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findFrotaByCodigo(int cdFrota) {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("A.CD_FROTA", String.valueOf(cdFrota), ItemComparator.EQUAL, java.sql.Types.INTEGER));
		return find(criterios);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		try{
			for (int i=0; criterios!=null && i<criterios.size(); i++){
				if (criterios.get(i).getColumn().equalsIgnoreCase("ST_FROTA") && 
						criterios.get(i).getValue().equalsIgnoreCase("-1") ) {
					criterios.remove(i);
					i--;
					continue;
				}
				else if (criterios.get(i).getColumn().equalsIgnoreCase("TP_FROTA") && 
						 criterios.get(i).getValue().equalsIgnoreCase("-1") ) {
					criterios.remove(i);
					i--;
					continue;
				}
			}
			return Search.find(
					" SELECT A.*, B.*, B.NM_PESSOA AS NM_PROPRIETARIO, C.NM_PESSOA AS NM_RESPONSAVEL " +
					" FROM FTA_FROTA  A "+
					" LEFT OUTER JOIN GRL_PESSOA B ON ( A.CD_PROPRIETARIO = B.CD_PESSOA )"+
					" LEFT OUTER JOIN GRL_PESSOA C ON ( A.CD_RESPONSAVEL  = C.CD_PESSOA )",
					" ",criterios, connect!=null ? connect : Conexao.conectar(), connect==null);	
					
		}catch(Exception e){
			Util.registerLog(e);
			return null;
		}
	}
	
}
