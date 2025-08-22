package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class InstituicaoAbastecimentoEnergiaDAO{

	public static int insert(InstituicaoAbastecimentoEnergia objeto) {
		return insert(objeto, null);
	}

	public static int insert(InstituicaoAbastecimentoEnergia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_abastecimento_energia (cd_instituicao,"+
			                                  "cd_abastecimento_energia,"+
			                                  "cd_periodo_letivo) VALUES (?, ?, ?)");
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdInstituicao());
			if(objeto.getCdAbastecimentoEnergia()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAbastecimentoEnergia());
			if(objeto.getCdPeriodoLetivo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoAbastecimentoEnergiaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoAbastecimentoEnergiaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InstituicaoAbastecimentoEnergia objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(InstituicaoAbastecimentoEnergia objeto, int cdInstituicaoOld, int cdAbastecimentoEnergiaOld, int cdPeriodoLetivoOld) {
		return update(objeto, cdInstituicaoOld, cdAbastecimentoEnergiaOld, cdPeriodoLetivoOld, null);
	}

	public static int update(InstituicaoAbastecimentoEnergia objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(InstituicaoAbastecimentoEnergia objeto, int cdInstituicaoOld, int cdAbastecimentoEnergiaOld, int cdPeriodoLetivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_instituicao_abastecimento_energia SET cd_instituicao=?,"+
												      		   "cd_abastecimento_energia=?,"+
												      		   "cd_periodo_letivo=? WHERE cd_instituicao=? AND cd_abastecimento_energia=? AND cd_periodo_letivo=?");
			pstmt.setInt(1,objeto.getCdInstituicao());
			pstmt.setInt(2,objeto.getCdAbastecimentoEnergia());
			pstmt.setInt(3,objeto.getCdPeriodoLetivo());
			pstmt.setInt(4, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.setInt(5, cdAbastecimentoEnergiaOld!=0 ? cdAbastecimentoEnergiaOld : objeto.getCdAbastecimentoEnergia());
			pstmt.setInt(6, cdPeriodoLetivoOld!=0 ? cdPeriodoLetivoOld : objeto.getCdPeriodoLetivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoAbastecimentoEnergiaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoAbastecimentoEnergiaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdInstituicao, int cdAbastecimentoEnergia, int cdPeriodoLetivo) {
		return delete(cdInstituicao, cdAbastecimentoEnergia, cdPeriodoLetivo, null);
	}

	public static int delete(int cdInstituicao, int cdAbastecimentoEnergia, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_instituicao_abastecimento_energia WHERE cd_instituicao=? AND cd_abastecimento_energia=? AND cd_periodo_letivo=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdAbastecimentoEnergia);
			pstmt.setInt(3, cdPeriodoLetivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoAbastecimentoEnergiaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoAbastecimentoEnergiaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InstituicaoAbastecimentoEnergia get(int cdInstituicao, int cdAbastecimentoEnergia, int cdPeriodoLetivo) {
		return get(cdInstituicao, cdAbastecimentoEnergia, cdPeriodoLetivo, null);
	}

	public static InstituicaoAbastecimentoEnergia get(int cdInstituicao, int cdAbastecimentoEnergia, int cdPeriodoLetivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_abastecimento_energia WHERE cd_instituicao=? AND cd_abastecimento_energia=? AND cd_periodo_letivo=?");
			pstmt.setInt(1, cdInstituicao);
			pstmt.setInt(2, cdAbastecimentoEnergia);
			pstmt.setInt(3, cdPeriodoLetivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InstituicaoAbastecimentoEnergia(rs.getInt("cd_instituicao"),
						rs.getInt("cd_abastecimento_energia"),
						rs.getInt("cd_periodo_letivo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoAbastecimentoEnergiaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoAbastecimentoEnergiaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_abastecimento_energia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoAbastecimentoEnergiaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoAbastecimentoEnergiaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InstituicaoAbastecimentoEnergia> getList() {
		return getList(null);
	}

	public static ArrayList<InstituicaoAbastecimentoEnergia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InstituicaoAbastecimentoEnergia> list = new ArrayList<InstituicaoAbastecimentoEnergia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InstituicaoAbastecimentoEnergia obj = InstituicaoAbastecimentoEnergiaDAO.get(rsm.getInt("cd_instituicao"), rsm.getInt("cd_abastecimento_energia"), rsm.getInt("cd_periodo_letivo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoAbastecimentoEnergiaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_abastecimento_energia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}