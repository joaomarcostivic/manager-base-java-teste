package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class LogradouroCepDAO{

	public static int insert(LogradouroCep objeto) {
		return insert(objeto, null);
	}

	public static int insert(LogradouroCep objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = 0;
			
			/* Alteração feita para ligar o cep a um endereço cadastrado na tabela
			 * grl_logradouro.
			 * Caso deseje alterar, favor avisar.
			 * @author Edgard Hufelande  
			 */
			if(objeto.getCdLogradouro() == 0){
				code = Conexao.getSequenceCode("grl_logradouro_cep", connect);
				if (code <= 0) {
					if (isConnectionNull)
						Conexao.rollback(connect);
					return -1;
				}
				objeto.setCdLogradouro(code);
			}
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_logradouro_cep (cd_logradouro,"+
			                                  "nr_cep,"+
			                                  "nr_endereco_inicial,"+
			                                  "nr_endereco_final) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCdLogradouro());
			pstmt.setString(2, objeto.getNrCep());
			pstmt.setString(3,objeto.getNrEnderecoInicial());
			pstmt.setString(4,objeto.getNrEnderecoFinal());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LogradouroCepDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LogradouroCepDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LogradouroCep objeto) {
		return update(objeto, 0, "", null);
	}

	public static int update(LogradouroCep objeto, int cdLogradouroOld, String nrCepOld) {
		return update(objeto, cdLogradouroOld, nrCepOld, null);
	}

	public static int update(LogradouroCep objeto, Connection connect) {
		return update(objeto, 0, "", connect);
	}

	public static int update(LogradouroCep objeto, int cdLogradouroOld, String nrCepOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_logradouro_cep SET cd_logradouro=?,"+
												      		   "nr_cep=?,"+
												      		   "nr_endereco_inicial=?,"+
												      		   "nr_endereco_final=? WHERE cd_logradouro=? AND nr_cep=?");
			pstmt.setInt(1,objeto.getCdLogradouro());
			pstmt.setString(2,objeto.getNrCep());
			pstmt.setString(3,objeto.getNrEnderecoInicial());
			pstmt.setString(4,objeto.getNrEnderecoFinal());
			pstmt.setInt(5, cdLogradouroOld!=0 ? cdLogradouroOld : objeto.getCdLogradouro());
			pstmt.setString(6, !nrCepOld.equals("") ? nrCepOld : objeto.getNrCep());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LogradouroCepDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LogradouroCepDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLogradouro, String nrCep) {
		return delete(cdLogradouro, nrCep, null);
	}

	public static int delete(int cdLogradouro, String nrCep, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_logradouro_cep WHERE cd_logradouro=? AND nr_cep=?");
			pstmt.setInt(1, cdLogradouro);
			pstmt.setString(2, nrCep);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LogradouroCepDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LogradouroCepDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LogradouroCep get(int cdLogradouro, int nrCep) {
		return get(cdLogradouro, nrCep, null);
	}

	public static LogradouroCep get(int cdLogradouro, int nrCep, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_logradouro_cep WHERE cd_logradouro=? AND nr_cep=?");
			pstmt.setInt(1, cdLogradouro);
			pstmt.setInt(2, nrCep);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new LogradouroCep(rs.getInt("cd_logradouro"),
						rs.getString("nr_cep"),
						rs.getString("nr_endereco_inicial"),
						rs.getString("nr_endereco_final"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LogradouroCepDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LogradouroCepDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_logradouro_cep");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LogradouroCepDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LogradouroCepDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<ItemComparator>  criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator>  criterios, Connection connect) {
		return Search.find("SELECT * FROM grl_logradouro_cep", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
