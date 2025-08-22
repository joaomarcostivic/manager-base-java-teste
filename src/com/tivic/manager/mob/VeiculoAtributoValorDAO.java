package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class VeiculoAtributoValorDAO{

	public static int insert(VeiculoAtributoValor objeto) {
		return insert(objeto, null);
	}

	public static int insert(VeiculoAtributoValor objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_veiculo_atributo_valor", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdVeiculoAtributoValor(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_veiculo_atributo_valor (cd_veiculo_atributo_valor,"+
			                                  "cd_veiculo,"+
			                                  "cd_tipo_veiculo_atributo,"+
			                                  "vl_atributo) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdVeiculo());
			if(objeto.getCdTipoVeiculoAtributo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoVeiculoAtributo());
			pstmt.setString(4,objeto.getVlAtributo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoAtributoValorDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoAtributoValorDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(VeiculoAtributoValor objeto) {
		return update(objeto, 0, null);
	}

	public static int update(VeiculoAtributoValor objeto, int cdVeiculoAtributoValorOld) {
		return update(objeto, cdVeiculoAtributoValorOld, null);
	}

	public static int update(VeiculoAtributoValor objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(VeiculoAtributoValor objeto, int cdVeiculoAtributoValorOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_veiculo_atributo_valor SET cd_veiculo_atributo_valor=?,"+
												      		   "cd_veiculo=?,"+
												      		   "cd_tipo_veiculo_atributo=?,"+
												      		   "vl_atributo=? WHERE cd_veiculo_atributo_valor=?");
			pstmt.setInt(1,objeto.getCdVeiculoAtributoValor());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdVeiculo());
			if(objeto.getCdTipoVeiculoAtributo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoVeiculoAtributo());
			pstmt.setString(4,objeto.getVlAtributo());
			pstmt.setInt(5, cdVeiculoAtributoValorOld!=0 ? cdVeiculoAtributoValorOld : objeto.getCdVeiculoAtributoValor());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoAtributoValorDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoAtributoValorDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdVeiculoAtributoValor) {
		return delete(cdVeiculoAtributoValor, null);
	}

	public static int delete(int cdVeiculoAtributoValor, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_veiculo_atributo_valor WHERE cd_veiculo_atributo_valor=?");
			pstmt.setInt(1, cdVeiculoAtributoValor);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoAtributoValorDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoAtributoValorDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static VeiculoAtributoValor get(int cdVeiculoAtributoValor) {
		return get(cdVeiculoAtributoValor, null);
	}

	public static VeiculoAtributoValor get(int cdVeiculoAtributoValor, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_veiculo_atributo_valor WHERE cd_veiculo_atributo_valor=?");
			pstmt.setInt(1, cdVeiculoAtributoValor);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new VeiculoAtributoValor(rs.getInt("cd_veiculo_atributo_valor"),
						rs.getInt("cd_veiculo"),
						rs.getInt("cd_tipo_veiculo_atributo"),
						rs.getString("vl_atributo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoAtributoValorDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoAtributoValorDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_veiculo_atributo_valor");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! VeiculoAtributoValorDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoAtributoValorDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<VeiculoAtributoValor> getList() {
		return getList(null);
	}

	public static ArrayList<VeiculoAtributoValor> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<VeiculoAtributoValor> list = new ArrayList<VeiculoAtributoValor>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				VeiculoAtributoValor obj = VeiculoAtributoValorDAO.get(rsm.getInt("cd_veiculo_atributo_valor"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! VeiculoAtributoValorDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_veiculo_atributo_valor", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
