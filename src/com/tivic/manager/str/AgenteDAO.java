package com.tivic.manager.str;

import java.sql.*;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;

import java.util.ArrayList;

public class AgenteDAO{

	public static int insert(Agente objeto) {
		return insert(objeto, null);
	}

	public static int insert(Agente objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("str_agente", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAgente(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO str_agente (cd_agente,"+
			                                  "nm_agente,"+
			                                  "ds_endereco,"+
			                                  "nm_bairro,"+
			                                  "nr_cep,"+
			                                  "cd_municipio,"+
			                                  "nr_matricula,"+
			                                  "cd_usuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmAgente());
			pstmt.setString(3,objeto.getDsEndereco());
			pstmt.setString(4,objeto.getNmBairro());
			pstmt.setString(5,objeto.getNrCep());
			pstmt.setInt(6,objeto.getCdMunicipio());
			pstmt.setString(7,objeto.getNrMatricula());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdUsuario());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Agente objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Agente objeto, int cdAgenteOld) {
		return update(objeto, cdAgenteOld, null);
	}

	public static int update(Agente objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Agente objeto, int cdAgenteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE str_agente SET cd_agente=?,"+
												      		   "nm_agente=?,"+
												      		   "ds_endereco=?,"+
												      		   "nm_bairro=?,"+
												      		   "nr_cep=?,"+
												      		   "cd_municipio=?,"+
												      		   "nr_matricula=?,"+
												      		   "cd_usuario=? WHERE cd_agente=?");
			pstmt.setInt(1,objeto.getCdAgente());
			pstmt.setString(2,objeto.getNmAgente());
			pstmt.setString(3,objeto.getDsEndereco());
			pstmt.setString(4,objeto.getNmBairro());
			pstmt.setString(5,objeto.getNrCep());
			pstmt.setInt(6,objeto.getCdMunicipio());
			pstmt.setString(7,objeto.getNrMatricula());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdUsuario());
			pstmt.setInt(9, cdAgenteOld!=0 ? cdAgenteOld : objeto.getCdAgente());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAgente) {
		return delete(cdAgente, null);
	}

	public static int delete(int cdAgente, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM str_agente WHERE cd_agente=?");
			pstmt.setInt(1, cdAgente);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Agente get(int cdAgente) {
		return get(cdAgente, null);
	}

	public static Agente get(int cdAgente, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			boolean lgBaseAntiga = Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
			
			if(lgBaseAntiga)
				pstmt = connect.prepareStatement("SELECT * FROM agente WHERE cod_agente=?");
			else
				pstmt = connect.prepareStatement("SELECT * FROM str_agente WHERE cd_agente=?");
			
			pstmt.setInt(1, cdAgente);
			rs = pstmt.executeQuery();
			if(rs.next()){
				if(lgBaseAntiga)
					return new Agente(rs.getInt("cod_agente"),
							rs.getString("nm_agente"),
							rs.getString("ds_endereco"),
							rs.getString("bairro"),
							rs.getString("nr_cep"),
							rs.getInt("cod_municipio"),
							rs.getString("nr_matricula"),
							rs.getInt("cod_usuario"),
							rs.getInt("tp_agente"));
				else
					return new Agente(rs.getInt("cd_agente"),
							rs.getString("nm_agente"),
							rs.getString("ds_endereco"),
							rs.getString("nm_bairro"),
							rs.getString("nr_cep"),
							rs.getInt("cd_municipio"),
							rs.getString("nr_matricula"),
							rs.getInt("cd_usuario"),
							rs.getInt("tp_agente"));
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

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM str_agente");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Agente> getList() {
		return getList(null);
	}

	public static ArrayList<Agente> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Agente> list = new ArrayList<Agente>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Agente obj = AgenteDAO.get(rsm.getInt("cd_agente"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgenteDAO.getList: " + e);
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
		return Search.find("SELECT * FROM str_agente", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
