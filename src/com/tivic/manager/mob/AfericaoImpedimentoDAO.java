package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class AfericaoImpedimentoDAO{

	public static int insert(AfericaoImpedimento objeto) {
		return insert(objeto, null);
	}

	public static int insert(AfericaoImpedimento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_afericao_impedimento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAfericaoImpedimento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_afericao_impedimento (cd_afericao_impedimento,"+
			                                  "cd_impedimento_afericao,"+
			                                  "cd_afericao_catraca) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdImpedimentoAfericao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdImpedimentoAfericao());
			if(objeto.getCdAfericaoCatraca()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAfericaoCatraca());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AfericaoImpedimentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AfericaoImpedimentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AfericaoImpedimento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AfericaoImpedimento objeto, int cdAfericaoImpedimentoOld) {
		return update(objeto, cdAfericaoImpedimentoOld, null);
	}

	public static int update(AfericaoImpedimento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AfericaoImpedimento objeto, int cdAfericaoImpedimentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_afericao_impedimento SET cd_afericao_impedimento=?,"+
												      		   "cd_impedimento_afericao=?,"+
												      		   "cd_afericao_catraca=? WHERE cd_afericao_impedimento=?");
			pstmt.setInt(1,objeto.getCdAfericaoImpedimento());
			if(objeto.getCdImpedimentoAfericao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdImpedimentoAfericao());
			if(objeto.getCdAfericaoCatraca()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdAfericaoCatraca());
			pstmt.setInt(4, cdAfericaoImpedimentoOld!=0 ? cdAfericaoImpedimentoOld : objeto.getCdAfericaoImpedimento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AfericaoImpedimentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AfericaoImpedimentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAfericaoImpedimento) {
		return delete(cdAfericaoImpedimento, null);
	}

	public static int delete(int cdAfericaoImpedimento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_afericao_impedimento WHERE cd_afericao_impedimento=?");
			pstmt.setInt(1, cdAfericaoImpedimento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AfericaoImpedimentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AfericaoImpedimentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AfericaoImpedimento get(int cdAfericaoImpedimento) {
		return get(cdAfericaoImpedimento, null);
	}

	public static AfericaoImpedimento get(int cdAfericaoImpedimento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_afericao_impedimento WHERE cd_afericao_impedimento=?");
			pstmt.setInt(1, cdAfericaoImpedimento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AfericaoImpedimento(rs.getInt("cd_afericao_impedimento"),
						rs.getInt("cd_impedimento_afericao"),
						rs.getInt("cd_afericao_catraca"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AfericaoImpedimentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AfericaoImpedimentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_afericao_impedimento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AfericaoImpedimentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AfericaoImpedimentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AfericaoImpedimento> getList() {
		return getList(null);
	}

	public static ArrayList<AfericaoImpedimento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AfericaoImpedimento> list = new ArrayList<AfericaoImpedimento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AfericaoImpedimento obj = AfericaoImpedimentoDAO.get(rsm.getInt("cd_afericao_impedimento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AfericaoImpedimentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_afericao_impedimento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
