package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ConcessaoCirculoDAO{

	public static int insert(ConcessaoCirculo objeto) {
		return insert(objeto, null);
	}

	public static int insert(ConcessaoCirculo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_concessao_circulo (cd_concessao,"+
			                                  "cd_circulo) VALUES (?, ?)");
			if(objeto.getCdConcessao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdConcessao());
			if(objeto.getCdCirculo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCirculo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoCirculoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoCirculoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ConcessaoCirculo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ConcessaoCirculo objeto, int cdConcessaoOld, int cdCirculoOld) {
		return update(objeto, cdConcessaoOld, cdCirculoOld, null);
	}

	public static int update(ConcessaoCirculo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ConcessaoCirculo objeto, int cdConcessaoOld, int cdCirculoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_concessao_circulo SET cd_concessao=?,"+
												      		   "cd_circulo=? WHERE cd_concessao=? AND cd_circulo=?");
			pstmt.setInt(1,objeto.getCdConcessao());
			pstmt.setInt(2,objeto.getCdCirculo());
			pstmt.setInt(3, cdConcessaoOld!=0 ? cdConcessaoOld : objeto.getCdConcessao());
			pstmt.setInt(4, cdCirculoOld!=0 ? cdCirculoOld : objeto.getCdCirculo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoCirculoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoCirculoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConcessao, int cdCirculo) {
		return delete(cdConcessao, cdCirculo, null);
	}

	public static int delete(int cdConcessao, int cdCirculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_concessao_circulo WHERE cd_concessao=? AND cd_circulo=?");
			pstmt.setInt(1, cdConcessao);
			pstmt.setInt(2, cdCirculo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoCirculoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoCirculoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ConcessaoCirculo get(int cdConcessao, int cdCirculo) {
		return get(cdConcessao, cdCirculo, null);
	}

	public static ConcessaoCirculo get(int cdConcessao, int cdCirculo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_concessao_circulo WHERE cd_concessao=? AND cd_circulo=?");
			pstmt.setInt(1, cdConcessao);
			pstmt.setInt(2, cdCirculo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ConcessaoCirculo(rs.getInt("cd_concessao"),
						rs.getInt("cd_circulo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoCirculoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoCirculoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_concessao_circulo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoCirculoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoCirculoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ConcessaoCirculo> getList() {
		return getList(null);
	}

	public static ArrayList<ConcessaoCirculo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ConcessaoCirculo> list = new ArrayList<ConcessaoCirculo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ConcessaoCirculo obj = ConcessaoCirculoDAO.get(rsm.getInt("cd_concessao"), rsm.getInt("cd_circulo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConcessaoCirculoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_concessao_circulo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}