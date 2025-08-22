package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ImpedimentoAfericaoDAO{

	public static int insert(ImpedimentoAfericao objeto) {
		return insert(objeto, null);
	}

	public static int insert(ImpedimentoAfericao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_impedimento_afericao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdImpedimentoAfericao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_impedimento_afericao (cd_impedimento_afericao,"+
			                                  "nm_impedimento,"+
			                                  "tp_referente) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmImpedimento());
			pstmt.setInt(3,objeto.getTpReferente());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ImpedimentoAfericaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ImpedimentoAfericaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ImpedimentoAfericao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ImpedimentoAfericao objeto, int cdImpedimentoAfericaoOld) {
		return update(objeto, cdImpedimentoAfericaoOld, null);
	}

	public static int update(ImpedimentoAfericao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ImpedimentoAfericao objeto, int cdImpedimentoAfericaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_impedimento_afericao SET cd_impedimento_afericao=?,"+
												      		   "nm_impedimento=?,"+
												      		   "tp_referente=? WHERE cd_impedimento_afericao=?");
			pstmt.setInt(1,objeto.getCdImpedimentoAfericao());
			pstmt.setString(2,objeto.getNmImpedimento());
			pstmt.setInt(3,objeto.getTpReferente());
			pstmt.setInt(4, cdImpedimentoAfericaoOld!=0 ? cdImpedimentoAfericaoOld : objeto.getCdImpedimentoAfericao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ImpedimentoAfericaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ImpedimentoAfericaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdImpedimentoAfericao) {
		return delete(cdImpedimentoAfericao, null);
	}

	public static int delete(int cdImpedimentoAfericao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_impedimento_afericao WHERE cd_impedimento_afericao=?");
			pstmt.setInt(1, cdImpedimentoAfericao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ImpedimentoAfericaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ImpedimentoAfericaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ImpedimentoAfericao get(int cdImpedimentoAfericao) {
		return get(cdImpedimentoAfericao, null);
	}

	public static ImpedimentoAfericao get(int cdImpedimentoAfericao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_impedimento_afericao WHERE cd_impedimento_afericao=?");
			pstmt.setInt(1, cdImpedimentoAfericao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ImpedimentoAfericao(rs.getInt("cd_impedimento_afericao"),
						rs.getString("nm_impedimento"),
						rs.getInt("tp_referente"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ImpedimentoAfericaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ImpedimentoAfericaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_impedimento_afericao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ImpedimentoAfericaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ImpedimentoAfericaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ImpedimentoAfericao> getList() {
		return getList(null);
	}

	public static ArrayList<ImpedimentoAfericao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ImpedimentoAfericao> list = new ArrayList<ImpedimentoAfericao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ImpedimentoAfericao obj = ImpedimentoAfericaoDAO.get(rsm.getInt("cd_impedimento_afericao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ImpedimentoAfericaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_impedimento_afericao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
