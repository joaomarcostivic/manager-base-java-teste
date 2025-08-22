package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class InstituicaoProfissionaisEscolaresDAO{

	public static int insert(InstituicaoProfissionaisEscolares objeto) {
		return insert(objeto, null);
	}

	public static int insert(InstituicaoProfissionaisEscolares objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_profissionais_escolares (cd_instituicao,"+
			                                  "cd_tipo_profissionais_escolares,"+
			                                  "cd_periodo_letivo,"+
			                                  "qt_profissionais_escolares) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCdInstituicao());
			pstmt.setInt(2, objeto.getCdTipoProfissionaisEscolares());
			pstmt.setInt(3, objeto.getCdPeriodoLetivo());
			pstmt.setInt(4,objeto.getQtProfissionaisEscolares());
			pstmt.executeUpdate();
			
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoProfissionaisEscolaresDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoProfissionaisEscolaresDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InstituicaoProfissionaisEscolares objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(InstituicaoProfissionaisEscolares objeto, int cdInstituicaoOld, int cdTipoProfissionaisEscolaresOld, int cdPeriodoLetivoOld) {
		return update(objeto, cdInstituicaoOld, cdTipoProfissionaisEscolaresOld, cdPeriodoLetivoOld, null);
	}

	public static int update(InstituicaoProfissionaisEscolares objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(InstituicaoProfissionaisEscolares objeto, int cdInstituicaoOld, int cdTipoProfissionaisEscolaresOld, int cdPeriodoLetivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_instituicao_profissionais_escolares SET cd_instituicao=?,"+
												      		   "cd_tipo_profissionais_escolares=?,"+
												      		   "cd_periodo_letivo=?,"+
												      		   "qt_profissionais_escolares=? WHERE cd_instituicao=? AND cd_tipo_profissionais_escolares=? AND cd_periodo_letivo=?");
			pstmt.setInt(1,objeto.getCdInstituicao());
			pstmt.setInt(2,objeto.getCdTipoProfissionaisEscolares());
			pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			pstmt.setInt(4,objeto.getQtProfissionaisEscolares());
			pstmt.setInt(5, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.setInt(6, cdTipoProfissionaisEscolaresOld!=0 ? cdTipoProfissionaisEscolaresOld : objeto.getCdTipoProfissionaisEscolares());
			pstmt.setInt(7, cdPeriodoLetivoOld!=0 ? cdPeriodoLetivoOld : objeto.getCdPeriodoLetivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoProfissionaisEscolaresDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoProfissionaisEscolaresDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdInstituicao, int cdTipoProfissionaisEscolares, int cdPeriodoLetivo) {
		return delete(cdInstituicao, cdTipoProfissionaisEscolares, cdPeriodoLetivo, null);
	}

	public static int delete(int cdInstituicao, int cdTipoProfissionaisEscolares, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_instituicao_profissionais_escolares WHERE cd_instituicao=? AND cd_tipo_profissionais_escolares=? AND cd_periodo_letivo=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdTipoProfissionaisEscolares);
			pstmt.setInt(3, cdPeriodoLetivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoProfissionaisEscolaresDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoProfissionaisEscolaresDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InstituicaoProfissionaisEscolares get(int cdInstituicao, int cdTipoProfissionaisEscolares, int cdPeriodoLetivo) {
		return get(cdInstituicao, cdTipoProfissionaisEscolares, cdPeriodoLetivo, null);
	}

	public static InstituicaoProfissionaisEscolares get(int cdInstituicao, int cdTipoProfissionaisEscolares, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_profissionais_escolares WHERE cd_instituicao=? AND cd_tipo_profissionais_escolares=? AND cd_periodo_letivo=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdTipoProfissionaisEscolares);
			pstmt.setInt(3, cdPeriodoLetivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InstituicaoProfissionaisEscolares(rs.getInt("cd_instituicao"),
						rs.getInt("cd_tipo_profissionais_escolares"),
						rs.getInt("cd_periodo_letivo"),
						rs.getInt("qt_profissionais_escolares"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoProfissionaisEscolaresDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoProfissionaisEscolaresDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_profissionais_escolares");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoProfissionaisEscolaresDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoProfissionaisEscolaresDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InstituicaoProfissionaisEscolares> getList() {
		return getList(null);
	}

	public static ArrayList<InstituicaoProfissionaisEscolares> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InstituicaoProfissionaisEscolares> list = new ArrayList<InstituicaoProfissionaisEscolares>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InstituicaoProfissionaisEscolares obj = InstituicaoProfissionaisEscolaresDAO.get(rsm.getInt("cd_instituicao"), rsm.getInt("cd_tipo_profissionais_escolares"), rsm.getInt("cd_periodo_letivo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoProfissionaisEscolaresDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_profissionais_escolares", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}