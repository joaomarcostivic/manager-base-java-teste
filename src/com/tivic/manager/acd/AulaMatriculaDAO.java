package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class AulaMatriculaDAO{

	public static int insert(AulaMatricula objeto) {
		return insert(objeto, null);
	}

	public static int insert(AulaMatricula objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_aula_matricula (cd_aula,"+
			                                  "lg_presenca,"+
			                                  "txt_observacao,"+
			                                  "cd_matricula) VALUES (?, ?, ?, ?)");
			if(objeto.getCdAula()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAula());
			pstmt.setInt(2,objeto.getLgPresenca());
			pstmt.setString(3,objeto.getTxtObservacao());
			if(objeto.getCdMatricula()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdMatricula());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaMatriculaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaMatriculaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AulaMatricula objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AulaMatricula objeto, int cdAulaOld, int cdMatriculaOld) {
		return update(objeto, cdAulaOld, cdMatriculaOld, null);
	}

	public static int update(AulaMatricula objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AulaMatricula objeto, int cdAulaOld, int cdMatriculaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_aula_matricula SET cd_aula=?,"+
												      		   "lg_presenca=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_matricula=? WHERE cd_aula=? AND cd_matricula=?");
			pstmt.setInt(1,objeto.getCdAula());
			pstmt.setInt(2,objeto.getLgPresenca());
			pstmt.setString(3,objeto.getTxtObservacao());
			pstmt.setInt(4,objeto.getCdMatricula());
			pstmt.setInt(5, cdAulaOld!=0 ? cdAulaOld : objeto.getCdAula());
			pstmt.setInt(6, cdMatriculaOld!=0 ? cdMatriculaOld : objeto.getCdMatricula());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaMatriculaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaMatriculaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAula, int cdMatricula) {
		return delete(cdAula, cdMatricula, null);
	}

	public static int delete(int cdAula, int cdMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_aula_matricula WHERE cd_aula=? AND cd_matricula=?");
			pstmt.setInt(1, cdAula);
			pstmt.setInt(2, cdMatricula);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaMatriculaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaMatriculaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AulaMatricula get(int cdAula, int cdMatricula) {
		return get(cdAula, cdMatricula, null);
	}

	public static AulaMatricula get(int cdAula, int cdMatricula, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_aula_matricula WHERE cd_aula=? AND cd_matricula=?");
			pstmt.setInt(1, cdAula);
			pstmt.setInt(2, cdMatricula);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AulaMatricula(rs.getInt("cd_aula"),
						rs.getInt("lg_presenca"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_matricula"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaMatriculaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaMatriculaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_aula_matricula");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AulaMatriculaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaMatriculaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AulaMatricula> getList() {
		return getList(null);
	}

	public static ArrayList<AulaMatricula> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AulaMatricula> list = new ArrayList<AulaMatricula>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AulaMatricula obj = AulaMatriculaDAO.get(rsm.getInt("cd_aula"), rsm.getInt("cd_matricula"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AulaMatriculaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_aula_matricula", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}