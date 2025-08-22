package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class InstituicaoTipoEquipamentoDAO{

	public static int insert(InstituicaoTipoEquipamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(InstituicaoTipoEquipamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_tipo_equipamento (cd_instituicao,"+
			                                  "cd_tipo_equipamento,"+
			                                  "qt_equipamento,"+
			                                  "cd_periodo_letivo) VALUES (?, ?, ?, ?)");
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdInstituicao());
			if(objeto.getCdTipoEquipamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoEquipamento());
			pstmt.setInt(3,objeto.getQtEquipamento());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdPeriodoLetivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoTipoEquipamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoTipoEquipamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InstituicaoTipoEquipamento objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(InstituicaoTipoEquipamento objeto, int cdInstituicaoOld, int cdTipoEquipamentoOld, int cdPeriodoLetivoOld) {
		return update(objeto, cdInstituicaoOld, cdTipoEquipamentoOld, cdPeriodoLetivoOld, null);
	}

	public static int update(InstituicaoTipoEquipamento objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(InstituicaoTipoEquipamento objeto, int cdInstituicaoOld, int cdTipoEquipamentoOld, int cdPeriodoLetivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_instituicao_tipo_equipamento SET cd_instituicao=?,"+
												      		   "cd_tipo_equipamento=?,"+
												      		   "qt_equipamento=?,"+
												      		   "cd_periodo_letivo=? WHERE cd_instituicao=? AND cd_tipo_equipamento=? AND cd_periodo_letivo=?");
			pstmt.setInt(1,objeto.getCdInstituicao());
			pstmt.setInt(2,objeto.getCdTipoEquipamento());
			pstmt.setInt(3,objeto.getQtEquipamento());
			pstmt.setInt(4,objeto.getCdPeriodoLetivo());
			pstmt.setInt(5, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.setInt(6, cdTipoEquipamentoOld!=0 ? cdTipoEquipamentoOld : objeto.getCdTipoEquipamento());
			pstmt.setInt(7, cdPeriodoLetivoOld!=0 ? cdPeriodoLetivoOld : objeto.getCdPeriodoLetivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoTipoEquipamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoTipoEquipamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdInstituicao, int cdTipoEquipamento, int cdPeriodoLetivo) {
		return delete(cdInstituicao, cdTipoEquipamento, cdPeriodoLetivo, null);
	}

	public static int delete(int cdInstituicao, int cdTipoEquipamento, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_instituicao_tipo_equipamento WHERE cd_instituicao=? AND cd_tipo_equipamento=? AND cd_periodo_letivo=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdTipoEquipamento);
			pstmt.setInt(3, cdPeriodoLetivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoTipoEquipamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoTipoEquipamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InstituicaoTipoEquipamento get(int cdInstituicao, int cdTipoEquipamento, int cdPeriodoLetivo) {
		return get(cdInstituicao, cdTipoEquipamento, cdPeriodoLetivo, null);
	}

	public static InstituicaoTipoEquipamento get(int cdInstituicao, int cdTipoEquipamento, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_tipo_equipamento WHERE cd_instituicao=? AND cd_tipo_equipamento=? AND cd_periodo_letivo=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdTipoEquipamento);
			pstmt.setInt(3, cdPeriodoLetivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InstituicaoTipoEquipamento(rs.getInt("cd_instituicao"),
						rs.getInt("cd_tipo_equipamento"),
						rs.getInt("qt_equipamento"),
						rs.getInt("cd_periodo_letivo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoTipoEquipamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoTipoEquipamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_tipo_equipamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoTipoEquipamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoTipoEquipamentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InstituicaoTipoEquipamento> getList() {
		return getList(null);
	}

	public static ArrayList<InstituicaoTipoEquipamento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InstituicaoTipoEquipamento> list = new ArrayList<InstituicaoTipoEquipamento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InstituicaoTipoEquipamento obj = InstituicaoTipoEquipamentoDAO.get(rsm.getInt("cd_instituicao"), rsm.getInt("cd_tipo_equipamento"), rsm.getInt("cd_periodo_letivo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoTipoEquipamentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_tipo_equipamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}