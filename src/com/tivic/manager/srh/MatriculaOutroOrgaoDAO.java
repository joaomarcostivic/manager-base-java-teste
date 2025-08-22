package com.tivic.manager.srh;

import java.sql.*;
import sol.dao.*;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class MatriculaOutroOrgaoDAO{

	public static int insert(MatriculaOutroOrgao objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(MatriculaOutroOrgao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_matricula");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdMatricula()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_outro_orgao");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("srh_matricula_outro_orgao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOutroOrgao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO srh_matricula_outro_orgao (cd_matricula,"+
			                                  "cd_outro_orgao,"+
			                                  "dt_inicial,"+
			                                  "dt_final,"+
			                                  "nm_orgao,"+
			                                  "lg_licenca_premio,"+
			                                  "lg_contagem_tempo,"+
			                                  "lg_anuidade,"+
			                                  "tp_orgao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdMatricula()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdMatricula());
			pstmt.setInt(2, code);
			if(objeto.getDtInicial()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setString(5,objeto.getNmOrgao());
			pstmt.setInt(6,objeto.getLgLicencaPremio());
			pstmt.setInt(7,objeto.getLgContagemTempo());
			pstmt.setInt(8,objeto.getLgAnuidade());
			pstmt.setInt(9,objeto.getTpOrgao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaOutroOrgaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaOutroOrgaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MatriculaOutroOrgao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(MatriculaOutroOrgao objeto, int cdMatriculaOld, int cdOutroOrgaoOld) {
		return update(objeto, cdMatriculaOld, cdOutroOrgaoOld, null);
	}

	public static int update(MatriculaOutroOrgao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(MatriculaOutroOrgao objeto, int cdMatriculaOld, int cdOutroOrgaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE srh_matricula_outro_orgao SET cd_matricula=?,"+
												      		   "cd_outro_orgao=?,"+
												      		   "dt_inicial=?,"+
												      		   "dt_final=?,"+
												      		   "nm_orgao=?,"+
												      		   "lg_licenca_premio=?,"+
												      		   "lg_contagem_tempo=?,"+
												      		   "lg_anuidade=?,"+
												      		   "tp_orgao=? WHERE cd_matricula=? AND cd_outro_orgao=?");
			pstmt.setInt(1,objeto.getCdMatricula());
			pstmt.setInt(2,objeto.getCdOutroOrgao());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setString(5,objeto.getNmOrgao());
			pstmt.setInt(6,objeto.getLgLicencaPremio());
			pstmt.setInt(7,objeto.getLgContagemTempo());
			pstmt.setInt(8,objeto.getLgAnuidade());
			pstmt.setInt(9,objeto.getTpOrgao());
			pstmt.setInt(10, cdMatriculaOld!=0 ? cdMatriculaOld : objeto.getCdMatricula());
			pstmt.setInt(11, cdOutroOrgaoOld!=0 ? cdOutroOrgaoOld : objeto.getCdOutroOrgao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaOutroOrgaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaOutroOrgaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMatricula, int cdOutroOrgao) {
		return delete(cdMatricula, cdOutroOrgao, null);
	}

	public static int delete(int cdMatricula, int cdOutroOrgao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM srh_matricula_outro_orgao WHERE cd_matricula=? AND cd_outro_orgao=?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdOutroOrgao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaOutroOrgaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaOutroOrgaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MatriculaOutroOrgao get(int cdMatricula, int cdOutroOrgao) {
		return get(cdMatricula, cdOutroOrgao, null);
	}

	public static MatriculaOutroOrgao get(int cdMatricula, int cdOutroOrgao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM srh_matricula_outro_orgao WHERE cd_matricula=? AND cd_outro_orgao=?");
			pstmt.setInt(1, cdMatricula);
			pstmt.setInt(2, cdOutroOrgao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MatriculaOutroOrgao(rs.getInt("cd_matricula"),
						rs.getInt("cd_outro_orgao"),
						(rs.getTimestamp("dt_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()),
						rs.getString("nm_orgao"),
						rs.getInt("lg_licenca_premio"),
						rs.getInt("lg_contagem_tempo"),
						rs.getInt("lg_anuidade"),
						rs.getInt("tp_orgao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaOutroOrgaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaOutroOrgaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_matricula_outro_orgao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MatriculaOutroOrgaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MatriculaOutroOrgaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM srh_matricula_outro_orgao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
