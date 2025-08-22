package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ComponenteAtributoValorDAO{

	public static int insert(ComponenteAtributoValor objeto) {
		return insert(objeto, null);
	}

	public static int insert(ComponenteAtributoValor objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_componente_atributo_valor", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdComponenteAtributoValor(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_componente_atributo_valor (cd_componente_atributo_valor,"+
			                                  "cd_veiculo,"+
			                                  "cd_componente_veiculo,"+
			                                  "nm_atributo,"+
			                                  "vl_atributo) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdVeiculo());
			if(objeto.getCdComponenteVeiculo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdComponenteVeiculo());
			pstmt.setString(4,objeto.getNmAtributo());
			pstmt.setString(5,objeto.getVlAtributo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ComponenteAtributoValorDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ComponenteAtributoValorDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ComponenteAtributoValor objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ComponenteAtributoValor objeto, int cdComponenteAtributoValorOld) {
		return update(objeto, cdComponenteAtributoValorOld, null);
	}

	public static int update(ComponenteAtributoValor objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ComponenteAtributoValor objeto, int cdComponenteAtributoValorOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_componente_atributo_valor SET cd_componente_atributo_valor=?,"+
												      		   "cd_veiculo=?,"+
												      		   "cd_componente_veiculo=?,"+
												      		   "nm_atributo=?,"+
												      		   "vl_atributo=? WHERE cd_componente_atributo_valor=?");
			pstmt.setInt(1,objeto.getCdComponenteAtributoValor());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdVeiculo());
			if(objeto.getCdComponenteVeiculo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdComponenteVeiculo());
			pstmt.setString(4,objeto.getNmAtributo());
			pstmt.setString(5,objeto.getVlAtributo());
			pstmt.setInt(6, cdComponenteAtributoValorOld!=0 ? cdComponenteAtributoValorOld : objeto.getCdComponenteAtributoValor());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ComponenteAtributoValorDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ComponenteAtributoValorDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdComponenteAtributoValor) {
		return delete(cdComponenteAtributoValor, null);
	}

	public static int delete(int cdComponenteAtributoValor, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_componente_atributo_valor WHERE cd_componente_atributo_valor=?");
			pstmt.setInt(1, cdComponenteAtributoValor);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ComponenteAtributoValorDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ComponenteAtributoValorDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ComponenteAtributoValor get(int cdComponenteAtributoValor) {
		return get(cdComponenteAtributoValor, null);
	}

	public static ComponenteAtributoValor get(int cdComponenteAtributoValor, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_componente_atributo_valor WHERE cd_componente_atributo_valor=?");
			pstmt.setInt(1, cdComponenteAtributoValor);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ComponenteAtributoValor(rs.getInt("cd_componente_atributo_valor"),
						rs.getInt("cd_veiculo"),
						rs.getInt("cd_componente_veiculo"),
						rs.getString("nm_atributo"),
						rs.getString("vl_atributo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ComponenteAtributoValorDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ComponenteAtributoValorDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_componente_atributo_valor");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ComponenteAtributoValorDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ComponenteAtributoValorDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ComponenteAtributoValor> getList() {
		return getList(null);
	}

	public static ArrayList<ComponenteAtributoValor> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ComponenteAtributoValor> list = new ArrayList<ComponenteAtributoValor>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ComponenteAtributoValor obj = ComponenteAtributoValorDAO.get(rsm.getInt("cd_componente_atributo_valor"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ComponenteAtributoValorDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_componente_atributo_valor", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
