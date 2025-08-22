package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class InstituicaoCirculoDAO{

	public static int insert(InstituicaoCirculo objeto) {
		return insert(objeto, null);
	}

	public static int insert(InstituicaoCirculo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_instituicao_circulo (cd_circulo,"+
			                                  "cd_instituicao) VALUES (?, ?)");
			pstmt.setInt(1, objeto.getCdCirculo());
			pstmt.setInt(2, objeto.getCdInstituicao());
			pstmt.executeUpdate();
			return objeto.getCdCirculo();
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCirculoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCirculoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(InstituicaoCirculo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(InstituicaoCirculo objeto, int cdCirculoOld, int cdInstituicaoOld) {
		return update(objeto, cdCirculoOld, cdInstituicaoOld, null);
	}

	public static int update(InstituicaoCirculo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(InstituicaoCirculo objeto, int cdCirculoOld, int cdInstituicaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_instituicao_circulo SET cd_circulo=?,"+
												      		   "cd_instituicao=? WHERE cd_circulo=? AND cd_instituicao=?");
			pstmt.setInt(1,objeto.getCdCirculo());
			pstmt.setInt(2,objeto.getCdInstituicao());
			pstmt.setInt(3, cdCirculoOld!=0 ? cdCirculoOld : objeto.getCdCirculo());
			pstmt.setInt(4, cdInstituicaoOld!=0 ? cdInstituicaoOld : objeto.getCdInstituicao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCirculoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCirculoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCirculo, int cdInstituicao) {
		return delete(cdCirculo, cdInstituicao, null);
	}

	public static int delete(int cdCirculo, int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_instituicao_circulo WHERE cd_circulo=? AND cd_instituicao=?");
			pstmt.setInt(1, cdCirculo);
			pstmt.setInt(2, cdInstituicao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCirculoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCirculoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static InstituicaoCirculo get(int cdCirculo, int cdInstituicao) {
		return get(cdCirculo, cdInstituicao, null);
	}

	public static InstituicaoCirculo get(int cdCirculo, int cdInstituicao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_circulo WHERE cd_circulo=? AND cd_instituicao=?");
			pstmt.setInt(1, cdCirculo);
			pstmt.setInt(2, cdInstituicao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new InstituicaoCirculo(rs.getInt("cd_circulo"),
						rs.getInt("cd_instituicao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCirculoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCirculoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_instituicao_circulo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCirculoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCirculoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<InstituicaoCirculo> getList() {
		return getList(null);
	}

	public static ArrayList<InstituicaoCirculo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<InstituicaoCirculo> list = new ArrayList<InstituicaoCirculo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				InstituicaoCirculo obj = InstituicaoCirculoDAO.get(rsm.getInt("cd_circulo"), rsm.getInt("cd_instituicao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! InstituicaoCirculoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_instituicao_circulo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
