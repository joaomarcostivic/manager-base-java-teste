package com.tivic.manager.mob;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.seg.AuthData;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

public class ViaServices {
	
	public static final String[] tpLocalizacao = {"Urbana", "Rural"};
	public static final String[] tpVia = {"De trânsito rápido", "Arterial", "Coletora", "Local", "Rodovia Pista Única", "Rodovia Pista Dupla", "Estrada"};

	public static Result save(Via via){
		return save(via, null, null, null);
	}

	public static Result save(Via via, ArrayList<Faixa> arrayFaixas, AuthData authData){
		return save(via, arrayFaixas, authData, null);
	}

	public static Result save(Via via, ArrayList<Faixa> arrayFaixas, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(via==null)
				return new Result(-1, "Erro ao salvar. Via é nulo");

			int retorno;
			if(via.getCdVia()==0){
				retorno = ViaDAO.insert(via, connect);
				via.setCdVia(retorno);
			}
			else {
				retorno = ViaDAO.update(via, connect);
			}
			
			if(retorno < 0) {
				if(isConnectionNull) {
					connect.rollback();
				}
				return new Result(-2, "Erro ao salvar via.");
			}
			
			if(arrayFaixas!=null) {
				retorno = connect.prepareStatement("DELETE FROM mob_faixa WHERE cd_via = "+via.getCdVia()).executeUpdate();
				if(retorno<0) {
					if(isConnectionNull) {
						connect.rollback();
					}
					return new Result(-3, "Erro ao atulizar faixas.");
				}
			}
			
			for (Faixa faixa : arrayFaixas) {
				faixa.setCdVia(via.getCdVia());
				Result result = FaixaServices.save(faixa, authData, connect);
				retorno = result.getCode();
				
				if(retorno<0) {
					if(isConnectionNull) {
						connect.rollback();
					}
					return result;
				}
				
				faixa.setCdFaixa(retorno);
			}

			if (isConnectionNull)
				connect.commit();

			Result result = new Result(1, "Salvo com sucesso...", "VIA", via);
			result.addObject("ARRAY_FAIXAS", arrayFaixas);
			
			return result;
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
	public static Result remove(Via via) {
		return remove(via.getCdVia());
	}
	public static Result remove(int cdVia){
		return remove(cdVia, false, null, null);
	}
	public static Result remove(int cdVia, boolean cascade){
		return remove(cdVia, cascade, null, null);
	}
	public static Result remove(int cdVia, boolean cascade, AuthData authData){
		return remove(cdVia, cascade, authData, null);
	}
	public static Result remove(int cdVia, boolean cascade, AuthData authData, Connection connect){
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
			retorno = ViaDAO.delete(cdVia, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_via");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllViasFaixas() {
		return getAllViasFaixas(null);
	}

	public static ResultSetMap getAllViasFaixas(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					" SELECT A.*, B.nm_logradouro, C.nm_orgao "
				  + " FROM mob_via A "
				  + " LEFT OUTER JOIN grl_logradouro B ON (A.cd_logradouro = B.cd_logradouro)"
				  + " LEFT OUTER JOIN mob_orgao C ON (A.cd_orgao = C.cd_orgao)");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()) {
				rsm.setValueToField("RSM_FAIXAS", new ResultSetMap(connect.prepareStatement("SELECT * FROM mob_faixa WHERE cd_via = "+rsm.getInt("cd_via")).executeQuery()));
				rsm.setValueToField("NM_TP_VIA", tpVia[rsm.getInt("tp_via")]);
			}
			rsm.beforeFirst();
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ViaServices.getAllViasFaixas: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViaServices.getAll: " + e);
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
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			
			ResultSetMap rsm = Search.find(
						" SELECT A.*, B.nm_logradouro "
					  + " FROM mob_via A "
					  + " LEFT OUTER JOIN grl_logradouro B ON (A.cd_logradouro = B.cd_logradouro)"
					  + " WHERE 1=1", 
					  criterios, connect, false);
			while(rsm.next()) {
				rsm.setValueToField("RSM_FAIXAS", new ResultSetMap(connect.prepareStatement("SELECT * FROM mob_faixa WHERE cd_via = "+rsm.getInt("cd_via")).executeQuery()));
				rsm.setValueToField("NM_TP_VIA", tpVia[rsm.getInt("tp_via")]);
			}
			rsm.beforeFirst();
			

			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ViaServices.find: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
