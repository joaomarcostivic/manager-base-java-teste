package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoVeiculoAtributoDAO{

	public static int insert(TipoVeiculoAtributo objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoVeiculoAtributo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_tipo_veiculo_atributo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoVeiculoAtributo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_tipo_veiculo_atributo (cd_tipo_veiculo_atributo,"+
			                                  "cd_tipo_veiculo_componente,"+
			                                  "nm_atributo) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdTipoVeiculoComponente()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoVeiculoComponente());
			pstmt.setString(3,objeto.getNmAtributo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoAtributoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoAtributoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoVeiculoAtributo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoVeiculoAtributo objeto, int cdTipoVeiculoAtributoOld) {
		return update(objeto, cdTipoVeiculoAtributoOld, null);
	}

	public static int update(TipoVeiculoAtributo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoVeiculoAtributo objeto, int cdTipoVeiculoAtributoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_tipo_veiculo_atributo SET cd_tipo_veiculo_atributo=?,"+
												      		   "cd_tipo_veiculo_componente=?,"+
												      		   "nm_atributo=? WHERE cd_tipo_veiculo_atributo=?");
			pstmt.setInt(1,objeto.getCdTipoVeiculoAtributo());
			if(objeto.getCdTipoVeiculoComponente()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoVeiculoComponente());
			pstmt.setString(3,objeto.getNmAtributo());
			pstmt.setInt(4, cdTipoVeiculoAtributoOld!=0 ? cdTipoVeiculoAtributoOld : objeto.getCdTipoVeiculoAtributo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoAtributoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoAtributoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoVeiculoAtributo) {
		return delete(cdTipoVeiculoAtributo, null);
	}

	public static int delete(int cdTipoVeiculoAtributo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_tipo_veiculo_atributo WHERE cd_tipo_veiculo_atributo=?");
			pstmt.setInt(1, cdTipoVeiculoAtributo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoAtributoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoAtributoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoVeiculoAtributo get(int cdTipoVeiculoAtributo) {
		return get(cdTipoVeiculoAtributo, null);
	}

	public static TipoVeiculoAtributo get(int cdTipoVeiculoAtributo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_tipo_veiculo_atributo WHERE cd_tipo_veiculo_atributo=?");
			pstmt.setInt(1, cdTipoVeiculoAtributo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoVeiculoAtributo(rs.getInt("cd_tipo_veiculo_atributo"),
						rs.getInt("cd_tipo_veiculo_componente"),
						rs.getString("nm_atributo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoAtributoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoAtributoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_tipo_veiculo_atributo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoAtributoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoAtributoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoVeiculoAtributo> getList() {
		return getList(null);
	}

	public static ArrayList<TipoVeiculoAtributo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoVeiculoAtributo> list = new ArrayList<TipoVeiculoAtributo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoVeiculoAtributo obj = TipoVeiculoAtributoDAO.get(rsm.getInt("cd_tipo_veiculo_atributo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoAtributoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_tipo_veiculo_atributo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
