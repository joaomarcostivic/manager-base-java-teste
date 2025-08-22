package com.tivic.manager.acd;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class AreaConhecimentoServices {

	public static Result save(AreaConhecimento areaConhecimento){
		return save(areaConhecimento, null);
	}

	public static Result save(AreaConhecimento areaConhecimento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(areaConhecimento==null)
				return new Result(-1, "Erro ao salvar. AreaConhecimento é nulo");

			int retorno;
			if(areaConhecimento.getCdAreaConhecimento()==0){
				retorno = AreaConhecimentoDAO.insert(areaConhecimento, connect);
				areaConhecimento.setCdAreaConhecimento(retorno);
			}
			else {
				retorno = AreaConhecimentoDAO.update(areaConhecimento, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "AREACONHECIMENTO", areaConhecimento);
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
	public static Result remove(int cdAreaConhecimento){
		return remove(cdAreaConhecimento, false, null);
	}
	public static Result remove(int cdAreaConhecimento, boolean cascade){
		return remove(cdAreaConhecimento, cascade, null);
	}
	public static Result remove(int cdAreaConhecimento, boolean cascade, Connection connect){
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
			retorno = AreaConhecimentoDAO.delete(cdAreaConhecimento, connect);
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
	
	public static AreaConhecimento getAreaConhecimentoSuperior(int cdAreaConhecimento) {
		return getAreaConhecimentoSuperior(cdAreaConhecimento, null);
	}
	
	public static AreaConhecimento getAreaConhecimentoSuperior(int cdAreaConhecimento, Connection connect) {
		return AreaConhecimentoDAO.get(AreaConhecimentoDAO.get(cdAreaConhecimento, connect).getCdAreaConhecimentoSuperior(), connect);
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
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_area_conhecimento AS nm_area_conhecimento_superior FROM acd_area_conhecimento A " + 
											 "LEFT OUTER JOIN acd_area_conhecimento B ON (A.cd_area_conhecimento_superior = B.cd_area_conhecimento)");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AreaConhecimentoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AreaConhecimentoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	@Deprecated
	public static ResultSetMap getAllHierarquia(Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			ResultSetMap rsm = AreaConhecimentoDAO.find(criterios, connection);
			while (rsm != null && rsm.next()) {
				if (rsm.getInt("CD_AREA_CONHECIMENTO_SUPERIOR") != 0) {
					int pointer = rsm.getPointer();
					int cdAreaConhecimento = rsm.getInt("CD_AREA_CONHECIMENTO_SUPERIOR");
					HashMap<String,Object> register = rsm.getRegister();
					if (rsm.locate("CD_AREA_CONHECIMENTO", new Integer(rsm.getInt("CD_AREA_CONHECIMENTO_SUPERIOR")), false, true)) {
						HashMap<String,Object> parentNode = rsm.getRegister();
						boolean isFound = rsm.getInt("CD_AREA_CONHECIMENTO")==cdAreaConhecimento;
						ResultSetMap subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
						while (!isFound && subRsm!=null) {
							subRsm = parentNode==null ? null : (ResultSetMap)parentNode.get("subResultSetMap");
							parentNode = subRsm==null ? null : subRsm.getRegister();
							isFound = subRsm==null ? false : subRsm.getInt("CD_AREA_CONHECIMENTO")==cdAreaConhecimento;
						}
						subRsm = (ResultSetMap)parentNode.get("subResultSetMap");
						if (subRsm==null) {
							subRsm = new ResultSetMap();
							parentNode.put("subResultSetMap", subRsm);
						}
						subRsm.addRegister(register);
						rsm.getLines().remove(register);
						pointer--;
					}
					rsm.goTo(pointer);
				}
			}
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM acd_area_conhecimento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static AreaConhecimento getById(String idAreaConhecimento) {
		return getById(idAreaConhecimento, null);
	}

	public static AreaConhecimento getById(String idAreaConhecimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_area_conhecimento WHERE id_area=?");
			pstmt.setString(1, idAreaConhecimento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AreaConhecimento(rs.getInt("cd_area_conhecimento"),
						rs.getString("nm_area_conhecimento"),
						rs.getInt("cd_area_conhecimento_superior"),
						rs.getString("id_area"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AreaConhecimentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AreaConhecimentoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
