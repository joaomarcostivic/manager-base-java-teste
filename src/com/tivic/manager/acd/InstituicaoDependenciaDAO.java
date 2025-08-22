package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class InstituicaoDependenciaDAO{

	public static int insert(InstituicaoDependencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(InstituicaoDependencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_instituicao_dependencia", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDependencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_dependencia (cd_dependencia,"+
			                                  "cd_instituicao,"+
			                                  "cd_tipo_dependencia,"+
			                                  "nm_dependencia,"+
			                                  "txt_dependencia,"+
			                                  "st_dependencia,"+
			                                  "lg_permanente,"+
			                                  "tp_localizacao,"+
			                                  "vl_capacidade,"+
			                                  "lg_rampa_acesso,"+
			                                  "id_dependencia,"+
			                                  "cd_periodo_letivo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInstituicao());
			if(objeto.getCdTipoDependencia()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoDependencia());
			pstmt.setString(4,objeto.getNmDependencia());
			pstmt.setString(5,objeto.getTxtDependencia());
			pstmt.setInt(6,objeto.getStDependencia());
			pstmt.setInt(7,objeto.getLgPermanente());
			pstmt.setInt(8,objeto.getTpLocalizacao());
			pstmt.setInt(9,objeto.getVlCapacidade());
			pstmt.setInt(10,objeto.getLgRampaAcesso());
			pstmt.setString(11,objeto.getIdDependencia());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdPeriodoLetivo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDependenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDependenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InstituicaoDependencia objeto) {
		return update(objeto, 0, null);
	}

	public static int update(InstituicaoDependencia objeto, int cdDependenciaOld) {
		return update(objeto, cdDependenciaOld, null);
	}

	public static int update(InstituicaoDependencia objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(InstituicaoDependencia objeto, int cdDependenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_instituicao_dependencia SET cd_dependencia=?,"+
												      		   "cd_instituicao=?,"+
												      		   "cd_tipo_dependencia=?,"+
												      		   "nm_dependencia=?,"+
												      		   "txt_dependencia=?,"+
												      		   "st_dependencia=?,"+
												      		   "lg_permanente=?,"+
												      		   "tp_localizacao=?,"+
												      		   "vl_capacidade=?,"+
												      		   "lg_rampa_acesso=?,"+
												      		   "id_dependencia=?,"+
												      		   "cd_periodo_letivo=? WHERE cd_dependencia=?");
			pstmt.setInt(1,objeto.getCdDependencia());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInstituicao());
			if(objeto.getCdTipoDependencia()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoDependencia());
			pstmt.setString(4,objeto.getNmDependencia());
			pstmt.setString(5,objeto.getTxtDependencia());
			pstmt.setInt(6,objeto.getStDependencia());
			pstmt.setInt(7,objeto.getLgPermanente());
			pstmt.setInt(8,objeto.getTpLocalizacao());
			pstmt.setInt(9,objeto.getVlCapacidade());
			pstmt.setInt(10,objeto.getLgRampaAcesso());
			pstmt.setString(11,objeto.getIdDependencia());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdPeriodoLetivo());
			pstmt.setInt(13, cdDependenciaOld!=0 ? cdDependenciaOld : objeto.getCdDependencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDependenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDependenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDependencia) {
		return delete(cdDependencia, null);
	}

	public static int delete(int cdDependencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_instituicao_dependencia WHERE cd_dependencia=?");
			pstmt.setInt(1, cdDependencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDependenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDependenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InstituicaoDependencia get(int cdDependencia) {
		return get(cdDependencia, null);
	}

	public static InstituicaoDependencia get(int cdDependencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_dependencia WHERE cd_dependencia=?");
			pstmt.setInt(1, cdDependencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InstituicaoDependencia(rs.getInt("cd_dependencia"),
						rs.getInt("cd_instituicao"),
						rs.getInt("cd_tipo_dependencia"),
						rs.getString("nm_dependencia"),
						rs.getString("txt_dependencia"),
						rs.getInt("st_dependencia"),
						rs.getInt("lg_permanente"),
						rs.getInt("tp_localizacao"),
						rs.getInt("vl_capacidade"),
						rs.getInt("lg_rampa_acesso"),
						rs.getString("id_dependencia"),
						rs.getInt("cd_periodo_letivo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDependenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDependenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_dependencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDependenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDependenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InstituicaoDependencia> getList() {
		return getList(null);
	}

	public static ArrayList<InstituicaoDependencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InstituicaoDependencia> list = new ArrayList<InstituicaoDependencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InstituicaoDependencia obj = InstituicaoDependenciaDAO.get(rsm.getInt("cd_dependencia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoDependenciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_dependencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}