package com.tivic.manager.fta;

import java.sql.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

public class EspecieVeiculoServices {
	
	public static ResultSetMap getSyncData() {
		return getSyncData(null);
	}
	
	public static ResultSetMap getSyncData(Connection connect) {
		
		boolean isConnectionNull = connect == null;
		
		if (isConnectionNull) {
			connect = Conexao.conectar();
		}
		
		try {
			ResultSetMap rsm = getAll(connect);
			return toStrEspecies(rsm);
		} catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		} finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
		
	}

	public static Result save(EspecieVeiculo especieVeiculo){
		return save(especieVeiculo, null);
	}

	public static Result save(EspecieVeiculo especieVeiculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			if(especieVeiculo==null)
				return new Result(-1, "Erro ao salvar. EspecieVeiculo é nulo");

			int retorno;
			if(especieVeiculo.getCdEspecie()==0){
				retorno = EspecieVeiculoDAO.insert(especieVeiculo, connect);
				especieVeiculo.setCdEspecie(retorno);
			}
			else {
				retorno = EspecieVeiculoDAO.update(especieVeiculo, connect);
			}

			if(retorno<=0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();

			return new Result(retorno, (retorno<=0)?"Erro ao salvar...":"Salvo com sucesso...", "ESPECIEVEICULO", especieVeiculo);
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
	public static Result remove(int cdEspecie){
		return remove(cdEspecie, false, null);
	}
	public static Result remove(int cdEspecie, boolean cascade){
		return remove(cdEspecie, cascade, null);
	}
	public static Result remove(int cdEspecie, boolean cascade, Connection connect){
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
			retorno = EspecieVeiculoDAO.delete(cdEspecie, connect);
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
		ResultSetMap rsm = new ResultSetMap();
		
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {

			if(!Util.isStrBaseAntiga()) {				
				pstmt = connect.prepareStatement("SELECT * FROM fta_especie_veiculo order by ds_especie");
				rsm = new ResultSetMap(pstmt.executeQuery());
			} else {
				pstmt = connect.prepareStatement("SELECT cod_especie AS cd_especie, ds_especie FROM especie_veiculo");
				rsm = new ResultSetMap(pstmt.executeQuery());
			}
			
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EspecieVeiculoServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EspecieVeiculoServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	

	public static EspecieVeiculo get(int cdEspecie) {
		return get(cdEspecie, null);
	}

	public static EspecieVeiculo get(int cdEspecie, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			if(!Util.isStrBaseAntiga()) {
				pstmt = connect.prepareStatement("SELECT * FROM fta_especie_veiculo WHERE cd_especie=?");
				pstmt.setInt(1, cdEspecie);
				rs = pstmt.executeQuery();
				if(rs.next()){
					return new EspecieVeiculo(rs.getInt("cd_especie"),
							rs.getString("ds_especie"));
				}
			} else {
				pstmt = connect.prepareStatement("SELECT * FROM especie_veiculo WHERE cod_especie=?");
				pstmt.setInt(1, cdEspecie);
				rs = pstmt.executeQuery();
				if(rs.next()){
					return new EspecieVeiculo(rs.getInt("cod_especie"),
							rs.getString("ds_especie"));
				}
			}
			
			return null;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EspecieVeiculoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EspecieVeiculoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}


	public static EspecieVeiculo getByNome(String nmEspecie) {
		return getByNome(nmEspecie, null);
	}

	public static EspecieVeiculo getByNome(String dsEspecie, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			if(!Util.isStrBaseAntiga()) {
				pstmt = connect.prepareStatement("SELECT * FROM fta_especie_veiculo WHERE ds_especie LIKE ?");
				pstmt.setString(1, dsEspecie);
				rs = pstmt.executeQuery();
				if(rs.next()){
					return new EspecieVeiculo(rs.getInt("cd_especie"),
							rs.getString("ds_especie"));
				}
			} else {
				pstmt = connect.prepareStatement("SELECT * FROM especie_veiculo WHERE ds_especie LIKE ?");
				pstmt.setString(1, dsEspecie);
				rs = pstmt.executeQuery();
				if(rs.next()){
					return new EspecieVeiculo(rs.getInt("cod_especie"),
							rs.getString("ds_especie"));
				}
			}
			
			return null;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EspecieVeiculoServices.getByNome: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EspecieVeiculoServices.getByNome: " + e);
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
		return Search.find("SELECT * FROM fta_especie_veiculo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}
	
	private static ResultSetMap toStrEspecies (ResultSetMap rsm) {
		if (Util.isStrBaseAntiga()) {
			ResultSetMap rsmOut = new ResultSetMap();
			
			while (rsm.next()) {
				
				EspecieVeiculo especieFta = new EspecieVeiculo(
						rsm.getInt("cd_especie"),
						rsm.getString("ds_especie")
						);
				
				com.tivic.manager.str.EspecieVeiculo conv = toStrEspecie(especieFta);
				
				HashMap<String, Object> hash = new HashMap<String, Object>();
				
				hash.put("cd_especie", conv.getCdEspecie());
				hash.put("ds_especie", conv.getDsEspecie());
				
				rsmOut.addRegister(hash);
				
			}
			
			return rsmOut;
			
		} else {
			
			return rsm;
			
		}
	}
	
	private static com.tivic.manager.str.EspecieVeiculo toStrEspecie (EspecieVeiculo especieVeiculo) {
		return new com.tivic.manager.str.EspecieVeiculo(
				especieVeiculo.getCdEspecie(),
				especieVeiculo.getDsEspecie()
				);
	}

}
