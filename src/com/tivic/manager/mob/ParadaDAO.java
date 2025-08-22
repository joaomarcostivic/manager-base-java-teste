package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ParadaDAO{

	public static int insert(Parada objeto) {
		return insert(objeto, null);
	}

	public static int insert(Parada objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_parada", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdParada(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_parada (cd_parada,"+
			                                  "cd_grupo_parada,"+
			                                  "cd_concessao,"+
			                                  "cd_logradouro,"+
			                                  "ds_referencia,"+
			                                  "cd_georreferencia,"+
			                                  "nm_ponto_referencia) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdGrupoParada()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdGrupoParada());
			if(objeto.getCdConcessao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdConcessao());
			if(objeto.getCdLogradouro()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdLogradouro());
			pstmt.setString(5,objeto.getDsReferencia());
			if(objeto.getCdGeorreferencia()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdGeorreferencia());
			pstmt.setString(7,objeto.getNmPontoReferencia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParadaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParadaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Parada objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Parada objeto, int cdParadaOld) {
		return update(objeto, cdParadaOld, null);
	}

	public static int update(Parada objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Parada objeto, int cdParadaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_parada SET cd_parada=?,"+
												      		   "cd_grupo_parada=?,"+
												      		   "cd_concessao=?,"+
												      		   "cd_logradouro=?,"+
												      		   "ds_referencia=?,"+
												      		   "cd_georreferencia=?,"+
												      		   "nm_ponto_referencia=? WHERE cd_parada=?");
			pstmt.setInt(1,objeto.getCdParada());
			if(objeto.getCdGrupoParada()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdGrupoParada());
			if(objeto.getCdConcessao()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdConcessao());
			if(objeto.getCdLogradouro()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdLogradouro());
			pstmt.setString(5,objeto.getDsReferencia());
			if(objeto.getCdGeorreferencia()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdGeorreferencia());
			pstmt.setString(7,objeto.getNmPontoReferencia());
			pstmt.setInt(8, cdParadaOld!=0 ? cdParadaOld : objeto.getCdParada());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParadaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParadaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdParada) {
		return delete(cdParada, null);
	}

	public static int delete(int cdParada, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_parada WHERE cd_parada=?");
			pstmt.setInt(1, cdParada);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParadaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ParadaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Parada get(int cdParada) {
		return get(cdParada, null);
	}

	public static Parada get(int cdParada, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_parada WHERE cd_parada=?");
			pstmt.setInt(1, cdParada);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Parada(rs.getInt("cd_parada"),
						rs.getInt("cd_grupo_parada"),
						rs.getInt("cd_concessao"),
						rs.getInt("cd_logradouro"),
						rs.getString("ds_referencia"),
						rs.getInt("cd_georreferencia"),
						rs.getString("nm_ponto_referencia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParadaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParadaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_parada");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ParadaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParadaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Parada> getList() {
		return getList(null);
	}

	public static ArrayList<Parada> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Parada> list = new ArrayList<Parada>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Parada obj = ParadaDAO.get(rsm.getInt("cd_parada"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ParadaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_parada", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}