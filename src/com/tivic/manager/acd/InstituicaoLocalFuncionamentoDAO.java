package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class InstituicaoLocalFuncionamentoDAO{

	public static int insert(InstituicaoLocalFuncionamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(InstituicaoLocalFuncionamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_local_funcionamento (cd_instituicao,"+
			                                  "cd_local_funcionamento,"+
			                                  "cd_periodo_letivo) VALUES (?, ?, ?)");
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdInstituicao());
			if(objeto.getCdLocalFuncionamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdLocalFuncionamento());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoLocalFuncionamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoLocalFuncionamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InstituicaoLocalFuncionamento objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(InstituicaoLocalFuncionamento objeto, int cdInstituicaoOld, int cdLocalFuncionamentoOld, int cdPeriodoLetivoOld) {
		return update(objeto, cdInstituicaoOld, cdLocalFuncionamentoOld, cdPeriodoLetivoOld, null);
	}

	public static int update(InstituicaoLocalFuncionamento objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(InstituicaoLocalFuncionamento objeto, int cdInstituicaoOld, int cdLocalFuncionamentoOld, int cdPeriodoLetivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_instituicao_local_funcionamento SET cd_instituicao=?,"+
												      		   "cd_local_funcionamento=?,"+
												      		   "cd_periodo_letivo=? WHERE cd_instituicao=? AND cd_local_funcionamento=? AND cd_periodo_letivo=?");
			pstmt.setInt(1,objeto.getCdInstituicao());
			pstmt.setInt(2,objeto.getCdLocalFuncionamento());
			pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			pstmt.setInt(4, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.setInt(5, cdLocalFuncionamentoOld!=0 ? cdLocalFuncionamentoOld : objeto.getCdLocalFuncionamento());
			pstmt.setInt(6, cdPeriodoLetivoOld!=0 ? cdPeriodoLetivoOld : objeto.getCdPeriodoLetivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoLocalFuncionamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoLocalFuncionamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdInstituicao, int cdLocalFuncionamento, int cdPeriodoLetivo) {
		return delete(cdInstituicao, cdLocalFuncionamento, cdPeriodoLetivo, null);
	}

	public static int delete(int cdInstituicao, int cdLocalFuncionamento, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_instituicao_local_funcionamento WHERE cd_instituicao=? AND cd_local_funcionamento=? AND cd_periodo_letivo=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdLocalFuncionamento);
			pstmt.setInt(3, cdPeriodoLetivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoLocalFuncionamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoLocalFuncionamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InstituicaoLocalFuncionamento get(int cdInstituicao, int cdLocalFuncionamento, int cdPeriodoLetivo) {
		return get(cdInstituicao, cdLocalFuncionamento, cdPeriodoLetivo, null);
	}

	public static InstituicaoLocalFuncionamento get(int cdInstituicao, int cdLocalFuncionamento, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_local_funcionamento WHERE cd_instituicao=? AND cd_local_funcionamento=? AND cd_periodo_letivo=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdLocalFuncionamento);
			pstmt.setInt(3, cdPeriodoLetivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InstituicaoLocalFuncionamento(rs.getInt("cd_instituicao"),
						rs.getInt("cd_local_funcionamento"),
						rs.getInt("cd_periodo_letivo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoLocalFuncionamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoLocalFuncionamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_local_funcionamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoLocalFuncionamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoLocalFuncionamentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InstituicaoLocalFuncionamento> getList() {
		return getList(null);
	}

	public static ArrayList<InstituicaoLocalFuncionamento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InstituicaoLocalFuncionamento> list = new ArrayList<InstituicaoLocalFuncionamento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InstituicaoLocalFuncionamento obj = InstituicaoLocalFuncionamentoDAO.get(rsm.getInt("cd_instituicao"), rsm.getInt("cd_local_funcionamento"), rsm.getInt("cd_periodo_letivo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoLocalFuncionamentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_local_funcionamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}