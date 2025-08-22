package com.tivic.manager.geo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tivic.manager.acd.Curso;
import com.tivic.manager.acd.CursoDAO;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

public class CamadaServices {
	public static Result save(Camada camada){
		return save(camada, null);
	}
	public static Result save(Camada camada, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			if(camada==null)
				return new Result(-1, "Erro ao salvar. Camada é nula");
			
			int retorno;
			if(camada.getCdCamada()==0){
				retorno = CamadaDAO.insert(camada, connect);
				camada.setCdCamada(retorno);
			}
			else {				
				retorno = CamadaDAO.update(camada, connect);
			}
			
			if(retorno<0)
				Conexao.rollback(connect);
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(camada.getCdCamada(), (retorno<0)?"Erro ao salvar...":"Salvo com sucesso...");
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
	
	public static ResultSetMap findAll() {
		return findAll(null);
	}

	public static ResultSetMap findAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_tipo, C.nm_mapa " +
					"FROM geo_camada A " +
					"LEFT OUTER JOIN geo_tipo_camada B ON (A.cd_tipo = B.cd_tipo) " +
					"LEFT OUTER JOIN geo_mapeamento C ON (A.cd_mapa = C.cd_mapa) " +
					"ORDER BY C.nm_mapa, A.nm_camada");
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CamadaServices.findAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CamadaServices.findAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap findAllByMapeamento(int cdMapa) {
		return findAllByMapeamento(cdMapa, null);
	}

	public static ResultSetMap findAllByMapeamento(int cdMapa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT A.*, B.nm_tipo " +
					"FROM geo_camada A " +
					"LEFT OUTER JOIN geo_tipo_camada B ON (A.cd_tipo = B.cd_tipo) " +
					"WHERE A.cd_mapa = ? " +
					"ORDER BY A.nm_camada");
			pstmt.setInt(1, cdMapa);
			
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CamadaServices.findAllByMapeamento: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CamadaServices.findAllByMapeamento: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ArrayList<Camada> getAll() {
		return getAll(null);
	}

	public static ArrayList<Camada> getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT cd_camada FROM geo_camada");
			
			ResultSet rs = pstmt.executeQuery();
			
			ArrayList<Camada> camadas = new ArrayList<Camada>();
			Camada c;
			while(rs.next()){
				c = get(rs.getInt("cd_referencia"), connect);
				camadas.add(c);
			}
			rs.close();
			
			return camadas;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CamadaServices.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CamadaServices.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ArrayList<Camada> getAllByMapeamento(int cdMapa) {
		return getAllByMapeamento(cdMapa, null);
	}

	public static ArrayList<Camada> getAllByMapeamento(int cdMapa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT cd_camada FROM geo_camada WHERE cd_mapa = ?");
			pstmt.setInt(1, cdMapa);
			
			ResultSet rs = pstmt.executeQuery();
			
			ArrayList<Camada> camadas = new ArrayList<Camada>();
			Camada c;
			while(rs.next()){
				c = get(rs.getInt("cd_referencia"), connect);
				camadas.add(c);
			}
			rs.close();
			
			return camadas;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CamadaServices.getAllByMapeamento: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CamadaServices.getAllByMapeamento: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Camada get(int cdCamada) {
		return get(cdCamada, null);
	}

	public static Camada get(int cdCamada, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			Camada camada = CamadaDAO.get(cdCamada, connect);
			
			camada.setReferencias(ReferenciaServices.getAllByCamada(cdCamada, true, connect));
			
			return camada;
		}
		catch(Exception e) {
			e.printStackTrace();
			System.err.println("Erro! CamadaServices.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result remove(int cdCamada){
		return remove(cdCamada, false, null);
	}
	
	public static Result remove(int cdCamada, boolean cascade){
		return remove(cdCamada, cascade, null);
	}
	
	public static Result remove(int cdCamada, boolean cascade, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			int retorno = 0;
			
			if(cascade)
				retorno = ReferenciaServices.removeAllByCamada(cdCamada, connect).getCode();
			
			if(!cascade || retorno>0)
				retorno = CamadaDAO.delete(cdCamada, connect);
			
			if(retorno<=0){
				Conexao.rollback(connect);
				return new Result(-2, "Esta Camada está vinculada a outros registros e não pode ser excluída!");
			}
			else if (isConnectionNull)
				connect.commit();
			
			return new Result(1, "Camada excluída com sucesso!");
		}
		catch(Exception e){
			System.out.println("Erro! CamadaServices.remove: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return new Result(-1, "Erro ao excluir Camada!");
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
		return CamadaDAO.find(criterios, connect);
	}
	
	public static Camada getByName(String nmCamada) {
		return getByName(nmCamada, null);
	}
	public static Camada getByName(String nmCamada, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM geo_camada WHERE nm_camada=?");
			pstmt.setString(1, nmCamada);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return CamadaDAO.get(rs.getInt("cd_camada"), connect);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CamadaServices.getByName: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CamadaServices.getByName: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}
