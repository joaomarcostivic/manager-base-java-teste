package com.tivic.manager.seg;

import java.sql.*;
import java.util.ArrayList;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;

public class LicencaServices {

	public static Result save(Licenca licenca){
		return save(licenca, null);
	}

	public static Result save(Licenca licenca, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if(licenca==null)
				return new Result(-1, "Erro ao salvar. Licenca é nulo");			

			// Verifica se há o id único do mesmo já registrado na tabela.
			Licenca lic  = LicencaServices.getLicencaById(licenca.getIdUnico(), connect);
			if(lic!=null && lic.getCdLicenca()>0 && licenca.getCdLicenca() <= 0) {
				return new Result(-2, "Há uma licença registrada com id único.");
			}
			
			// Verifica se há uma pessoa registrada com o mesmo código na tabela.
			lic  = LicencaServices.getLicencaByPessoa(licenca.getCdPessoa(), connect);
			if(lic!=null && lic.getCdLicenca()>0 && licenca.getCdLicenca() <= 0) {
				return new Result(-3, "Há uma licença registrada para esta pessoa.");
			}
					
			int retorno = 0;
			if(licenca.getCdLicenca()==0){				
				retorno = LicencaDAO.insert(licenca, connect);
				licenca.setCdLicenca(retorno);
			}
			else {
				retorno = LicencaDAO.update(licenca, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso!", "LICENCA", licenca);
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
	public static Result remove(int cdLicenca){
		return remove(cdLicenca, false, null);
	}
	public static Result remove(int cdLicenca, boolean cascade){
		return remove(cdLicenca, cascade, null);
	}
	public static Result remove(int cdLicenca, boolean cascade, Connection connect){
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
			retorno = LicencaDAO.delete(cdLicenca, connect);
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_licenca");
				return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LicencaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LicencaServices.getAll: " + e);
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
		return Search.find("SELECT * FROM seg_licenca", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	public static ResultSetMap findAndLog(ArrayList<ItemComparator> criterios, Connection connect){
		return Search.findAndLog("SELECT * FROM seg_licenca", "", criterios,
				connect != null ? connect : Conexao.conectar(),
						connect == null);
	}
	
	public static ResultSetMap getAllLicenciados() {
		return getAllLicenciados(null);
	}
	
	public static ResultSetMap getAllKeysOfLicenciado(int cdLicenciado) {
		return getAllKeysOfLicenciado(cdLicenciado, null);
	}
	
	/**
	 * Buscar todos os licenciados do sistema com seus respectivos nomes e ids únicos.
	 * @author Edgard Hufelande
	 * @param connect
	 * @return
	 */
	
	public static ResultSetMap getAllLicenciados(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSetMap result;
		try {
			pstmt = connect.prepareStatement(
					" SELECT A.*, B.NM_PESSOA FROM seg_licenca A " +
					" JOIN grl_pessoa         		           B ON (A.cd_pessoa = B.cd_pessoa) " +
					"  "
					);
			result = new ResultSetMap(pstmt.executeQuery());
			return result;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LicencaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LicencaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Buscar todos os licenciados do sistema com seus respectivos nomes e ids únicos.
	 * @author Edgard Hufelande
	 * @param connect
	 * @return
	 */
	
	public static ResultSetMap getAllKeysOfLicenciado(int cdLicenca, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSetMap result;
		try {
			pstmt = connect.prepareStatement(
					" SELECT A.*, B.NM_PESSOA, C.* FROM seg_licenca A " +
					" JOIN grl_pessoa         		           B ON (A.cd_pessoa  = B.cd_pessoa) " +
					" JOIN seg_chave_licenca   		           C ON (C.cd_licenca = A.cd_licenca) " +
					" WHERE C.cd_licenca = " + cdLicenca
					);
			result = new ResultSetMap(pstmt.executeQuery());
			return result;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LicencaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LicencaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Buscar dados na tabela de licenças usando o idUnico.
	 * @author Edgard Hufelande
	 * @param idUnico
	 * @return
	 */	
	public static Licenca getLicencaById(String idUnico) {
		return getLicencaById(idUnico, null);
	}

	public static Licenca getLicencaById(String idUnico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_licenca WHERE id_unico=?");
			pstmt.setString(1, idUnico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Licenca(rs.getInt("cd_licenca"),
						rs.getInt("cd_pessoa"),
						rs.getString("id_unico"),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LicencaServices.getLicencaById: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LicencaServices.getLicencaById: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	
	/**
	 * Buscar dados na tabela de licenças usando o cdPessoa.
	 * @author Edgard Hufelande
	 * @param cdPessoa
	 * @return
	 */

	public static Licenca getLicencaByPessoa(String cdPessoa) {
		return getLicencaById(cdPessoa, null);
	}
	
	public static Licenca getLicencaByPessoa(int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_licenca WHERE cd_pessoa=?");
			pstmt.setInt(1, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Licenca(rs.getInt("cd_licenca"),
						rs.getInt("cd_pessoa"),
						rs.getString("id_unico"),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LicencaServices.getLicencaById: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LicencaServices.getLicencaById: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Licenca getLicencaByIdUnico(String idUnico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_licenca WHERE id_unico=?");
			pstmt.setString(1, idUnico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Licenca(rs.getInt("cd_licenca"),
						rs.getInt("cd_pessoa"),
						rs.getString("id_unico"),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LicencaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LicencaDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

}
