package com.tivic.manager.acd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CirculoDAO{

	public static int insert(Circulo objeto) {
		return insert(objeto, null);
	}

	public static int insert(Circulo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("acd_circulo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCirculo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO acd_circulo (cd_circulo,"+
			                                  "nm_circulo,"+
			                                  "tp_circulo,"+
			                                  "txt_observacao,"+
			                                  "id_circulo,"+
			                                  "cd_instituicao_principal) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmCirculo());
			pstmt.setInt(3,objeto.getTpCirculo());
			pstmt.setString(4,objeto.getTxtObservacao());
			pstmt.setString(5,objeto.getIdCirculo());
			if(objeto.getCdInstituicaoPrincipal()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdInstituicaoPrincipal());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CirculoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CirculoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Circulo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Circulo objeto, int cdCirculoOld) {
		return update(objeto, cdCirculoOld, null);
	}

	public static int update(Circulo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Circulo objeto, int cdCirculoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE acd_circulo SET cd_circulo=?,"+
												      		   "nm_circulo=?,"+
												      		   "tp_circulo=?,"+
												      		   "txt_observacao=?,"+
												      		   "id_circulo=?,"+
												      		   "cd_instituicao_principal=? WHERE cd_circulo=?");
			pstmt.setInt(1,objeto.getCdCirculo());
			pstmt.setString(2,objeto.getNmCirculo());
			pstmt.setInt(3,objeto.getTpCirculo());
			pstmt.setString(4,objeto.getTxtObservacao());
			pstmt.setString(5,objeto.getIdCirculo());
			if(objeto.getCdInstituicaoPrincipal()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdInstituicaoPrincipal());
			pstmt.setInt(7, cdCirculoOld!=0 ? cdCirculoOld : objeto.getCdCirculo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CirculoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CirculoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCirculo) {
		return delete(cdCirculo, null);
	}

	public static int delete(int cdCirculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM acd_circulo WHERE cd_circulo=?");
			pstmt.setInt(1, cdCirculo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CirculoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CirculoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Circulo get(int cdCirculo) {
		return get(cdCirculo, null);
	}

	public static Circulo get(int cdCirculo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM acd_circulo WHERE cd_circulo=?");
			pstmt.setInt(1, cdCirculo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Circulo(rs.getInt("cd_circulo"),
						rs.getString("nm_circulo"),
						rs.getInt("tp_circulo"),
						rs.getString("txt_observacao"),
						rs.getString("id_circulo"),
						rs.getInt("cd_instituicao_principal"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CirculoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CirculoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM acd_circulo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CirculoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CirculoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Circulo> getList() {
		return getList(null);
	}

	public static ArrayList<Circulo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Circulo> list = new ArrayList<Circulo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Circulo obj = CirculoDAO.get(rsm.getInt("cd_circulo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CirculoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM acd_circulo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
