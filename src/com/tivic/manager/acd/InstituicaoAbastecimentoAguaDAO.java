package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class InstituicaoAbastecimentoAguaDAO{

	public static int insert(InstituicaoAbastecimentoAgua objeto) {
		return insert(objeto, null);
	}

	public static int insert(InstituicaoAbastecimentoAgua objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_abastecimento_agua (cd_instituicao,"+
			                                  "cd_abastecimento_agua,"+
			                                  "cd_periodo_letivo) VALUES (?, ?, ?)");
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdInstituicao());
			if(objeto.getCdAbastecimentoAgua()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAbastecimentoAgua());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoAbastecimentoAguaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoAbastecimentoAguaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InstituicaoAbastecimentoAgua objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(InstituicaoAbastecimentoAgua objeto, int cdInstituicaoOld, int cdAbastecimentoAguaOld, int cdPeriodoLetivoOld) {
		return update(objeto, cdInstituicaoOld, cdAbastecimentoAguaOld, cdPeriodoLetivoOld, null);
	}

	public static int update(InstituicaoAbastecimentoAgua objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(InstituicaoAbastecimentoAgua objeto, int cdInstituicaoOld, int cdAbastecimentoAguaOld, int cdPeriodoLetivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_instituicao_abastecimento_agua SET cd_instituicao=?,"+
												      		   "cd_abastecimento_agua=?,"+
												      		   "cd_periodo_letivo=? WHERE cd_instituicao=? AND cd_abastecimento_agua=? AND cd_periodo_letivo=?");
			pstmt.setInt(1,objeto.getCdInstituicao());
			pstmt.setInt(2,objeto.getCdAbastecimentoAgua());
			pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			pstmt.setInt(4, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.setInt(5, cdAbastecimentoAguaOld!=0 ? cdAbastecimentoAguaOld : objeto.getCdAbastecimentoAgua());
			pstmt.setInt(6, cdPeriodoLetivoOld!=0 ? cdPeriodoLetivoOld : objeto.getCdPeriodoLetivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoAbastecimentoAguaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoAbastecimentoAguaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdInstituicao, int cdAbastecimentoAgua, int cdPeriodoLetivo) {
		return delete(cdInstituicao, cdAbastecimentoAgua, cdPeriodoLetivo, null);
	}

	public static int delete(int cdInstituicao, int cdAbastecimentoAgua, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_instituicao_abastecimento_agua WHERE cd_instituicao=? AND cd_abastecimento_agua=? AND cd_periodo_letivo=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdAbastecimentoAgua);
			pstmt.setInt(3, cdPeriodoLetivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoAbastecimentoAguaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoAbastecimentoAguaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InstituicaoAbastecimentoAgua get(int cdInstituicao, int cdAbastecimentoAgua, int cdPeriodoLetivo) {
		return get(cdInstituicao, cdAbastecimentoAgua, cdPeriodoLetivo, null);
	}

	public static InstituicaoAbastecimentoAgua get(int cdInstituicao, int cdAbastecimentoAgua, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_abastecimento_agua WHERE cd_instituicao=? AND cd_abastecimento_agua=? AND cd_periodo_letivo=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdAbastecimentoAgua);
			pstmt.setInt(3, cdPeriodoLetivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InstituicaoAbastecimentoAgua(rs.getInt("cd_instituicao"),
						rs.getInt("cd_abastecimento_agua"),
						rs.getInt("cd_periodo_letivo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoAbastecimentoAguaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoAbastecimentoAguaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_abastecimento_agua");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoAbastecimentoAguaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoAbastecimentoAguaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InstituicaoAbastecimentoAgua> getList() {
		return getList(null);
	}

	public static ArrayList<InstituicaoAbastecimentoAgua> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InstituicaoAbastecimentoAgua> list = new ArrayList<InstituicaoAbastecimentoAgua>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InstituicaoAbastecimentoAgua obj = InstituicaoAbastecimentoAguaDAO.get(rsm.getInt("cd_instituicao"), rsm.getInt("cd_abastecimento_agua"), rsm.getInt("cd_periodo_letivo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoAbastecimentoAguaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_abastecimento_agua", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}