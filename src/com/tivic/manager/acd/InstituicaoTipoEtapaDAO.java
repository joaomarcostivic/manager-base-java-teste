package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class InstituicaoTipoEtapaDAO{

	public static int insert(InstituicaoTipoEtapa objeto) {
		return insert(objeto, null);
	}

	public static int insert(InstituicaoTipoEtapa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_tipo_etapa (cd_etapa,"+
			                                  "cd_instituicao,"+
			                                  "cd_periodo_letivo) VALUES (?, ?, ?)");
			if(objeto.getCdEtapa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEtapa());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdInstituicao());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoTipoEtapaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoTipoEtapaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InstituicaoTipoEtapa objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(InstituicaoTipoEtapa objeto, int cdInstituicaoOld, int cdEtapaOld, int cdPeriodoLetivoOld) {
		return update(objeto, cdInstituicaoOld, cdEtapaOld, cdPeriodoLetivoOld, null);
	}

	public static int update(InstituicaoTipoEtapa objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(InstituicaoTipoEtapa objeto, int cdInstituicaoOld, int cdEtapaOld, int cdPeriodoLetivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_instituicao_tipo_etapa SET cd_etapa=?,"+
												      		   "cd_instituicao=?,"+
												      		   "cd_periodo_letivo=? WHERE cd_instituicao=? AND cd_etapa=? AND cd_periodo_letivo=?");
			pstmt.setInt(1,objeto.getCdEtapa());
			pstmt.setInt(2,objeto.getCdInstituicao());
			pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			pstmt.setInt(4, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.setInt(5, cdEtapaOld!=0 ? cdEtapaOld : objeto.getCdEtapa());
			pstmt.setInt(6, cdPeriodoLetivoOld!=0 ? cdPeriodoLetivoOld : objeto.getCdPeriodoLetivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoTipoEtapaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoTipoEtapaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdInstituicao, int cdEtapa, int cdPeriodoLetivo) {
		return delete(cdInstituicao, cdEtapa, cdPeriodoLetivo, null);
	}

	public static int delete(int cdInstituicao, int cdEtapa, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_instituicao_tipo_etapa WHERE cd_instituicao=? AND cd_etapa=? AND cd_periodo_letivo=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdEtapa);
			pstmt.setInt(3, cdPeriodoLetivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoTipoEtapaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoTipoEtapaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InstituicaoTipoEtapa get(int cdInstituicao, int cdEtapa, int cdPeriodoLetivo) {
		return get(cdInstituicao, cdEtapa, cdPeriodoLetivo, null);
	}

	public static InstituicaoTipoEtapa get(int cdInstituicao, int cdEtapa, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_tipo_etapa WHERE cd_instituicao=? AND cd_etapa=? AND cd_periodo_letivo=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdEtapa);
			pstmt.setInt(3, cdPeriodoLetivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InstituicaoTipoEtapa(rs.getInt("cd_etapa"),
						rs.getInt("cd_instituicao"),
						rs.getInt("cd_periodo_letivo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoTipoEtapaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoTipoEtapaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_tipo_etapa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoTipoEtapaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoTipoEtapaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InstituicaoTipoEtapa> getList() {
		return getList(null);
	}

	public static ArrayList<InstituicaoTipoEtapa> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InstituicaoTipoEtapa> list = new ArrayList<InstituicaoTipoEtapa>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InstituicaoTipoEtapa obj = InstituicaoTipoEtapaDAO.get(rsm.getInt("cd_instituicao"), rsm.getInt("cd_etapa"), rsm.getInt("cd_periodo_letivo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoTipoEtapaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_tipo_etapa", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}