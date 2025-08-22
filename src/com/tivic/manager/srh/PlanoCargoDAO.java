package com.tivic.manager.srh;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class PlanoCargoDAO{

	public static int insert(PlanoCargo objeto) {
		return insert(objeto, null);
	}

	public static int insert(PlanoCargo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("srh_plano_cargo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPlano(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO srh_plano_cargo (cd_plano,"+
			                                  "nm_plano,"+
			                                  "vl_salario_base,"+
			                                  "ds_atributo,"+
			                                  "qt_vagas,"+
			                                  "cd_cbo,"+
			                                  "cd_empresa) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmPlano());
			pstmt.setDouble(3,objeto.getVlSalarioBase());
			pstmt.setString(4,objeto.getDsAtributo());
			pstmt.setInt(5,objeto.getQtVagas());
			if(objeto.getCdCbo()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdCbo());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdEmpresa());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PlanoCargo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(PlanoCargo objeto, int cdPlanoOld) {
		return update(objeto, cdPlanoOld, null);
	}

	public static int update(PlanoCargo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(PlanoCargo objeto, int cdPlanoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE srh_plano_cargo SET cd_plano=?,"+
												      		   "nm_plano=?,"+
												      		   "vl_salario_base=?,"+
												      		   "ds_atributo=?,"+
												      		   "qt_vagas=?,"+
												      		   "cd_cbo=?,"+
												      		   "cd_empresa=? WHERE cd_plano=?");
			pstmt.setInt(1,objeto.getCdPlano());
			pstmt.setString(2,objeto.getNmPlano());
			pstmt.setDouble(3,objeto.getVlSalarioBase());
			pstmt.setString(4,objeto.getDsAtributo());
			pstmt.setInt(5,objeto.getQtVagas());
			if(objeto.getCdCbo()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdCbo());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdEmpresa());
			pstmt.setInt(8, cdPlanoOld!=0 ? cdPlanoOld : objeto.getCdPlano());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPlano) {
		return delete(cdPlano, null);
	}

	public static int delete(int cdPlano, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM srh_plano_cargo WHERE cd_plano=?");
			pstmt.setInt(1, cdPlano);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PlanoCargo get(int cdPlano) {
		return get(cdPlano, null);
	}

	public static PlanoCargo get(int cdPlano, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM srh_plano_cargo WHERE cd_plano=?");
			pstmt.setInt(1, cdPlano);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PlanoCargo(rs.getInt("cd_plano"),
						rs.getString("nm_plano"),
						rs.getDouble("vl_salario_base"),
						rs.getString("ds_atributo"),
						rs.getInt("qt_vagas"),
						rs.getInt("cd_cbo"),
						rs.getInt("cd_empresa"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM srh_plano_cargo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PlanoCargo> getList() {
		return getList(null);
	}

	public static ArrayList<PlanoCargo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PlanoCargo> list = new ArrayList<PlanoCargo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PlanoCargo obj = PlanoCargoDAO.get(rsm.getInt("cd_plano"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PlanoCargoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM srh_plano_cargo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}