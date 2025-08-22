package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoVeiculoComponenteDAO{

	public static int insert(TipoVeiculoComponente objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoVeiculoComponente objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_tipo_veiculo_componente", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTipoVeiculoComponente(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_tipo_veiculo_componente (cd_tipo_veiculo_componente,"+
			                                  "cd_tipo_veiculo,"+
			                                  "nm_componente) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdTipoVeiculo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoVeiculo());
			pstmt.setString(3,objeto.getNmComponente());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoComponenteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoComponenteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoVeiculoComponente objeto) {
		return update(objeto, 0, null);
	}

	public static int update(TipoVeiculoComponente objeto, int cdTipoVeiculoComponenteOld) {
		return update(objeto, cdTipoVeiculoComponenteOld, null);
	}

	public static int update(TipoVeiculoComponente objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(TipoVeiculoComponente objeto, int cdTipoVeiculoComponenteOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_tipo_veiculo_componente SET cd_tipo_veiculo_componente=?,"+
												      		   "cd_tipo_veiculo=?,"+
												      		   "nm_componente=? WHERE cd_tipo_veiculo_componente=?");
			pstmt.setInt(1,objeto.getCdTipoVeiculoComponente());
			if(objeto.getCdTipoVeiculo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoVeiculo());
			pstmt.setString(3,objeto.getNmComponente());
			pstmt.setInt(4, cdTipoVeiculoComponenteOld!=0 ? cdTipoVeiculoComponenteOld : objeto.getCdTipoVeiculoComponente());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoComponenteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoComponenteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoVeiculoComponente) {
		return delete(cdTipoVeiculoComponente, null);
	}

	public static int delete(int cdTipoVeiculoComponente, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_tipo_veiculo_componente WHERE cd_tipo_veiculo_componente=?");
			pstmt.setInt(1, cdTipoVeiculoComponente);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoComponenteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoComponenteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoVeiculoComponente get(int cdTipoVeiculoComponente) {
		return get(cdTipoVeiculoComponente, null);
	}

	public static TipoVeiculoComponente get(int cdTipoVeiculoComponente, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_tipo_veiculo_componente WHERE cd_tipo_veiculo_componente=?");
			pstmt.setInt(1, cdTipoVeiculoComponente);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoVeiculoComponente(rs.getInt("cd_tipo_veiculo_componente"),
						rs.getInt("cd_tipo_veiculo"),
						rs.getString("nm_componente"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoComponenteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoComponenteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_tipo_veiculo_componente");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoComponenteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoComponenteDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoVeiculoComponente> getList() {
		return getList(null);
	}

	public static ArrayList<TipoVeiculoComponente> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoVeiculoComponente> list = new ArrayList<TipoVeiculoComponente>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoVeiculoComponente obj = TipoVeiculoComponenteDAO.get(rsm.getInt("cd_tipo_veiculo_componente"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoVeiculoComponenteDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_tipo_veiculo_componente", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
