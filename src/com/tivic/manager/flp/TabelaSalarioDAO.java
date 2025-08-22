package com.tivic.manager.flp;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TabelaSalarioDAO{

	public static int insert(TabelaSalario objeto) {
		return insert(objeto, null);
	}

	public static int insert(TabelaSalario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("flp_tabela_salario", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTabelaSalario(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO flp_tabela_salario (cd_tabela_salario,"+
			                                  "nm_tabela_salario,"+
			                                  "vl_salario,"+
			                                  "vl_carga_horaria,"+
			                                  "lg_proporcional_carga,"+
			                                  "cd_empresa,"+
			                                  "st_tabela_salario,"+
			                                  "id_tabela_salario) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTabelaSalario());
			pstmt.setFloat(3,objeto.getVlSalario());
			pstmt.setInt(4,objeto.getVlCargaHoraria());
			pstmt.setInt(5,objeto.getLgProporcionalCarga());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdEmpresa());
			pstmt.setInt(7,objeto.getStTabelaSalario());
			pstmt.setString(8,objeto.getIdTabelaSalario());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaSalarioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaSalarioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TabelaSalario objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TabelaSalario objeto, int cdTabelaSalarioOld) {
		return update(objeto, cdTabelaSalarioOld, null);
	}

	public static int update(TabelaSalario objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TabelaSalario objeto, int cdTabelaSalarioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE flp_tabela_salario SET cd_tabela_salario=?,"+
												      		   "nm_tabela_salario=?,"+
												      		   "vl_salario=?,"+
												      		   "vl_carga_horaria=?,"+
												      		   "lg_proporcional_carga=?,"+
												      		   "cd_empresa=?,"+
												      		   "st_tabela_salario=?,"+
												      		   "id_tabela_salario=? WHERE cd_tabela_salario=?");
			pstmt.setInt(1,objeto.getCdTabelaSalario());
			pstmt.setString(2,objeto.getNmTabelaSalario());
			pstmt.setFloat(3,objeto.getVlSalario());
			pstmt.setInt(4,objeto.getVlCargaHoraria());
			pstmt.setInt(5,objeto.getLgProporcionalCarga());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdEmpresa());
			pstmt.setInt(7,objeto.getStTabelaSalario());
			pstmt.setString(8,objeto.getIdTabelaSalario());
			pstmt.setInt(9, cdTabelaSalarioOld!=0 ? cdTabelaSalarioOld : objeto.getCdTabelaSalario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaSalarioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaSalarioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTabelaSalario) {
		return delete(cdTabelaSalario, null);
	}

	public static int delete(int cdTabelaSalario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM flp_tabela_salario WHERE cd_tabela_salario=?");
			pstmt.setInt(1, cdTabelaSalario);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaSalarioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaSalarioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TabelaSalario get(int cdTabelaSalario) {
		return get(cdTabelaSalario, null);
	}

	public static TabelaSalario get(int cdTabelaSalario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM flp_tabela_salario WHERE cd_tabela_salario=?");
			pstmt.setInt(1, cdTabelaSalario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TabelaSalario(rs.getInt("cd_tabela_salario"),
						rs.getString("nm_tabela_salario"),
						rs.getFloat("vl_salario"),
						rs.getInt("vl_carga_horaria"),
						rs.getInt("lg_proporcional_carga"),
						rs.getInt("cd_empresa"),
						rs.getInt("st_tabela_salario"),
						rs.getString("id_tabela_salario"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaSalarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaSalarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM flp_tabela_salario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaSalarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaSalarioDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TabelaSalario> getList() {
		return getList(null);
	}

	public static ArrayList<TabelaSalario> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TabelaSalario> list = new ArrayList<TabelaSalario>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TabelaSalario obj = TabelaSalarioDAO.get(rsm.getInt("cd_tabela_salario"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaSalarioDAO.getList: " + e);
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
		return Search.find("SELECT * FROM flp_tabela_salario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
