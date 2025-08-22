package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class MatriculaTipoTransporteDAO{

	public static int insert(MatriculaTipoTransporte objeto) {
		return insert(objeto, null);
	}

	public static int insert(MatriculaTipoTransporte objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_matricula_tipo_transporte (cd_matricula,"+
			                                  "cd_tipo_transporte) VALUES (?, ?)");
			if(objeto.getCdMatricula()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdMatricula());
			if(objeto.getCdTipoTransporte()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoTransporte());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoTransporteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoTransporteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MatriculaTipoTransporte objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(MatriculaTipoTransporte objeto, int cdMatriculaOld, int cdTipoTransporteOld) {
		return update(objeto, cdMatriculaOld, cdTipoTransporteOld, null);
	}

	public static int update(MatriculaTipoTransporte objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(MatriculaTipoTransporte objeto, int cdMatriculaOld, int cdTipoTransporteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_matricula_tipo_transporte SET cd_matricula=?,"+
												      		   "cd_tipo_transporte=? WHERE cd_matricula=? AND cd_tipo_transporte=?");
			pstmt.setInt(1,objeto.getCdMatricula());
			pstmt.setInt(2,objeto.getCdTipoTransporte());
			pstmt.setInt(3, cdMatriculaOld!=0 ? cdMatriculaOld : objeto.getCdMatricula());
			pstmt.setInt(4, cdTipoTransporteOld!=0 ? cdTipoTransporteOld : objeto.getCdTipoTransporte());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoTransporteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoTransporteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMatricula, int cdTipoTransporte) {
		return delete(cdMatricula, cdTipoTransporte, null);
	}

	public static int delete(int cdMatricula, int cdTipoTransporte, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_matricula_tipo_transporte WHERE cd_matricula=? AND cd_tipo_transporte=?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdTipoTransporte);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoTransporteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoTransporteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MatriculaTipoTransporte get(int cdMatricula, int cdTipoTransporte) {
		return get(cdMatricula, cdTipoTransporte, null);
	}

	public static MatriculaTipoTransporte get(int cdMatricula, int cdTipoTransporte, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_tipo_transporte WHERE cd_matricula=? AND cd_tipo_transporte=?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdTipoTransporte);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MatriculaTipoTransporte(rs.getInt("cd_matricula"),
						rs.getInt("cd_tipo_transporte"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoTransporteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoTransporteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_matricula_tipo_transporte");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoTransporteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoTransporteDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<MatriculaTipoTransporte> getList() {
		return getList(null);
	}

	public static ArrayList<MatriculaTipoTransporte> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<MatriculaTipoTransporte> list = new ArrayList<MatriculaTipoTransporte>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				MatriculaTipoTransporte obj = MatriculaTipoTransporteDAO.get(rsm.getInt("cd_matricula"), rsm.getInt("cd_tipo_transporte"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaTipoTransporteDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_matricula_tipo_transporte", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}