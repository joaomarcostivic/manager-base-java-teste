package com.tivic.manager.bpm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class BemManutencaoComponenteDAO{

	public static int insert(BemManutencaoComponente objeto) {
		return insert(objeto, null);
	}

	public static int insert(BemManutencaoComponente objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO bpm_bem_manutencao_componente (cd_manutencao,"+
			                                  "cd_componente,"+
			                                  "qt_componente) VALUES (?, ?, ?)");
			if(objeto.getCdManutencao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdManutencao());
			if(objeto.getCdComponente()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdComponente());
			pstmt.setInt(3,objeto.getQtComponente());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BemManutencaoComponenteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BemManutencaoComponenteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(BemManutencaoComponente objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(BemManutencaoComponente objeto, int cdManutencaoOld, int cdComponenteOld) {
		return update(objeto, cdManutencaoOld, cdComponenteOld, null);
	}

	public static int update(BemManutencaoComponente objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(BemManutencaoComponente objeto, int cdManutencaoOld, int cdComponenteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE bpm_bem_manutencao_componente SET cd_manutencao=?,"+
												      		   "cd_componente=?,"+
												      		   "qt_componente=? WHERE cd_manutencao=? AND cd_componente=?");
			pstmt.setInt(1,objeto.getCdManutencao());
			pstmt.setInt(2,objeto.getCdComponente());
			pstmt.setInt(3,objeto.getQtComponente());
			pstmt.setInt(4, cdManutencaoOld!=0 ? cdManutencaoOld : objeto.getCdManutencao());
			pstmt.setInt(5, cdComponenteOld!=0 ? cdComponenteOld : objeto.getCdComponente());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BemManutencaoComponenteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BemManutencaoComponenteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdManutencao, int cdComponente) {
		return delete(cdManutencao, cdComponente, null);
	}

	public static int delete(int cdManutencao, int cdComponente, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM bpm_bem_manutencao_componente WHERE cd_manutencao=? AND cd_componente=?");
			pstmt.setInt(1, cdManutencao);
			pstmt.setInt(2, cdComponente);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BemManutencaoComponenteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! BemManutencaoComponenteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static BemManutencaoComponente get(int cdManutencao, int cdComponente) {
		return get(cdManutencao, cdComponente, null);
	}

	public static BemManutencaoComponente get(int cdManutencao, int cdComponente, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM bpm_bem_manutencao_componente WHERE cd_manutencao=? AND cd_componente=?");
			pstmt.setInt(1, cdManutencao);
			pstmt.setInt(2, cdComponente);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new BemManutencaoComponente(rs.getInt("cd_manutencao"),
						rs.getInt("cd_componente"),
						rs.getInt("qt_componente"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BemManutencaoComponenteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BemManutencaoComponenteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM bpm_bem_manutencao_componente");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! BemManutencaoComponenteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! BemManutencaoComponenteDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM bpm_bem_manutencao_componente", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
