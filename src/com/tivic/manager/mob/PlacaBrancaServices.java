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

public class PlacaBrancaServices {

	public static Result save(PlacaBranca placaBranca){
		return save(placaBranca, null, null);
	}

	public static Result save(PlacaBranca placaBranca, AuthData authData){
		return save(placaBranca, authData, null);
	}

	public static Result save(PlacaBranca placaBranca, AuthData authData, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(placaBranca==null)
				return new Result(-1, "Erro ao salvar. PlacaBranca é nulo");

			int retorno;
			if(placaBranca.getCdPlaca()==0){
				retorno = PlacaBrancaDAO.insert(placaBranca, connect);
				placaBranca.setCdPlaca(retorno);
			}
			else {
				retorno = PlacaBrancaDAO.update(placaBranca, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "PLACABRANCA", placaBranca);
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
	public static Result remove(PlacaBranca placaBranca) {
		return remove(placaBranca.getCdPlaca());
	}
	public static Result remove(int cdPlaca){
		return remove(cdPlaca, false, null, null);
	}
	public static Result remove(int cdPlaca, boolean cascade){
		return remove(cdPlaca, cascade, null, null);
	}
	public static Result remove(int cdPlaca, boolean cascade, AuthData authData){
		return remove(cdPlaca, cascade, authData, null);
	}
	public static Result remove(int cdPlaca, boolean cascade, AuthData authData, Connection connect){
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
			retorno = PlacaBrancaDAO.delete(cdPlaca, connect);
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
			pstmt = connect.prepareStatement(
					" SELECT A.*,"
				  + " B.nm_orgao"
				  + " FROM mob_placa_branca A"
				  + " LEFT OUTER JOIN mob_orgao B ON (A.cd_orgao = B.cd_orgao)");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlacaBrancaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlacaBrancaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM mob_placa_branca", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap report(String idOrgao) {
		return report(idOrgao, null);
	}

	public static ResultSetMap report(String idOrgao, Connection connection) {
		boolean isConnectionNull = connection==null;
		if (isConnectionNull)
			connection = Conexao.conectar();
		PreparedStatement ps;
		ResultSet rs;
		
		try {			
			ps = connection.prepareStatement(
					  " SELECT * "
					+ " FROM mob_evento_equipamento A"
					+ " WHERE A.nr_placa IN ("
					+ "		SELECT nr_placa FROM mob_placa_branca"
					+ " )"
					+ " AND A.nm_orgao_autuador = ?");
			ps.setString(1, idOrgao);
			
			return new ResultSetMap(ps.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlacaBrancaServices.report: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}	
	
	public static Result check(String nrPlaca, int cdOrgao, Connection connection) {
		boolean isConnectionNull = connection==null;
		if (isConnectionNull)
			connection = Conexao.conectar();
		PreparedStatement ps;
		try {
			ps = connection.prepareStatement(
					" SELECT * "
				  + " FROM mob_placa_branca A"
				  + " WHERE nr_placa = ?"
				  + " AND cd_orgao = ?");
			ps.setString(1, nrPlaca.trim());
			ps.setInt(2, cdOrgao);
			
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return new Result(1, "", "LG_REJEITAR", 1);
			
			return new Result(1, "", "LG_REJEITAR", 0);
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlacaBrancaServices.check: " + e);
			return new Result(-1, e.getMessage());
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

}
