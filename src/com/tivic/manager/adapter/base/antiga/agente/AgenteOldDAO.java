package com.tivic.manager.adapter.base.antiga.agente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class AgenteOldDAO{

	public static int insert(AgenteOld objeto) {
		return insert(objeto, null);
	}

	public static int insert(AgenteOld objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("agente", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCodAgente(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agente (cod_agente,"+
			                                  "nm_agente,"+
			                                  "ds_endereco,"+
			                                  "bairro,"+
			                                  "nr_cep,"+
			                                  "cod_municipio,"+
			                                  "nr_matricula,"+
			                                  "cod_usuario,"+
			                                  "tp_agente,"+
			                                  "st_agente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmAgente());
			pstmt.setString(3,objeto.getDsEndereco());
			pstmt.setString(4,objeto.getBairro());
			pstmt.setString(5,objeto.getNrCep());
			if(objeto.getCodMunicipio()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCodMunicipio());
			pstmt.setString(7,objeto.getNrMatricula());
			if(objeto.getCodUsuario()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCodUsuario());
			pstmt.setInt(9,objeto.getTpAgente());
			pstmt.setInt(10,objeto.getStAgente());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteOldDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteOldDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AgenteOld objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AgenteOld objeto, int codAgenteOld) {
		return update(objeto, codAgenteOld, null);
	}

	public static int update(AgenteOld objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AgenteOld objeto, int codAgenteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE agente SET cod_agente=?,"+
												      		   "nm_agente=?,"+
												      		   "ds_endereco=?,"+
												      		   "bairro=?,"+
												      		   "nr_cep=?,"+
												      		   "cod_municipio=?,"+
												      		   "nr_matricula=?,"+
												      		   "cod_usuario=?,"+
												      		   "tp_agente=?,"+
												      		   "st_agente=? WHERE cod_agente=?");
			pstmt.setInt(1,objeto.getCodAgente());
			pstmt.setString(2,objeto.getNmAgente());
			pstmt.setString(3,objeto.getDsEndereco());
			pstmt.setString(4,objeto.getBairro());
			pstmt.setString(5,objeto.getNrCep());
			if(objeto.getCodMunicipio()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCodMunicipio());
			pstmt.setString(7,objeto.getNrMatricula());
			if(objeto.getCodUsuario()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCodUsuario());
			pstmt.setInt(9,objeto.getTpAgente());
			pstmt.setInt(10,objeto.getStAgente());
			pstmt.setInt(11, codAgenteOld!=0 ? codAgenteOld : objeto.getCodAgente());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteOldDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteOldDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int codAgente) {
		return delete(codAgente, null);
	}

	public static int delete(int codAgente, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM agente WHERE cod_agente=?");
			pstmt.setInt(1, codAgente);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteOldDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteOldDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AgenteOld get(int cdAgente) {
		return get(cdAgente, null);
	}

	public static AgenteOld get(int cdAgente, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agente WHERE cod_agente=?");
			pstmt.setInt(1, cdAgente);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AgenteOld(rs.getInt("cod_agente"),
						rs.getString("nm_agente"),
						rs.getString("ds_endereco"),
						rs.getString("bairro"),
						rs.getString("nr_cep"),
						rs.getInt("cod_municipio"),
						rs.getString("nr_matricula"),
						rs.getInt("cod_usuario"),
						rs.getInt("tp_agente"),
						rs.getInt("st_agente"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static AgenteOld getByCdUsuario(int cdUsuario) {
	    return getByCdUsuario(cdUsuario, null);
	}

	public static AgenteOld getByCdUsuario(int cdUsuario, Connection connect){
	    boolean isConnectionNull = connect == null;
	    if (isConnectionNull)
	        connect = Conexao.conectar();
	    PreparedStatement pstmt;
	    ResultSet rs;
	    try {
	        pstmt = connect.prepareStatement("SELECT * FROM agente WHERE cod_usuario=?");
	        pstmt.setInt(1, cdUsuario);
	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return new AgenteOld(rs.getInt("cod_agente"),
	                                 rs.getString("nm_agente"),
	                                 rs.getString("ds_endereco"),
	                                 rs.getString("bairro"),
	                                 rs.getString("nr_cep"),
	                                 rs.getInt("cod_municipio"),
	                                 rs.getString("nr_matricula"),
	                                 rs.getInt("cod_usuario"),
	                                 rs.getInt("tp_agente"),
	                                 rs.getInt("st_agente"));
	        } else {
	            return null;
	        }
	    } catch (SQLException sqlExpt) {
	        sqlExpt.printStackTrace(System.out);
	        System.err.println("Erro! AgenteOldDAO.getByCdUsuario: " + sqlExpt);
	        return null;
	    } catch (Exception e) {
	        e.printStackTrace(System.out);
	        System.err.println("Erro! AgenteOldDAO.getByCdUsuario: " + e);
	        return null;
	    } finally {
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
			pstmt = connect.prepareStatement("SELECT * FROM agente");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteOldDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteOldDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AgenteOld> getList() {
		return getList(null);
	}

	public static ArrayList<AgenteOld> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AgenteOld> list = new ArrayList<>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AgenteOld obj = get(rsm.getInt("cod_agente"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DAO.getList: " + e);
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
		return Search.find("SELECT * FROM agente", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
