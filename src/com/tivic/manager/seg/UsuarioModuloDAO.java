package com.tivic.manager.seg;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class UsuarioModuloDAO{

	public static int insert(UsuarioModulo objeto) {
		return insert(objeto, null);
	}

	public static int insert(UsuarioModulo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO seg_usuario_modulo (cd_usuario,"+
			                                  "cd_modulo,"+
			                                  "cd_sistema,"+
			                                  "lg_natureza) VALUES (?, ?, ?, ?)");
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdUsuario());
			if(objeto.getCdModulo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdModulo());
			if(objeto.getCdSistema()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdSistema());
			pstmt.setInt(4,objeto.getLgNatureza());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(UsuarioModulo objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(UsuarioModulo objeto, int cdUsuarioOld, int cdModuloOld, int cdSistemaOld) {
		return update(objeto, cdUsuarioOld, cdModuloOld, cdSistemaOld, null);
	}

	public static int update(UsuarioModulo objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(UsuarioModulo objeto, int cdUsuarioOld, int cdModuloOld, int cdSistemaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE seg_usuario_modulo SET cd_usuario=?,"+
												      		   "cd_modulo=?,"+
												      		   "cd_sistema=?,"+
												      		   "lg_natureza=? WHERE cd_usuario=? AND cd_modulo=? AND cd_sistema=?");
			pstmt.setInt(1,objeto.getCdUsuario());
			pstmt.setInt(2,objeto.getCdModulo());
			pstmt.setInt(3,objeto.getCdSistema());
			pstmt.setInt(4,objeto.getLgNatureza());
			pstmt.setInt(5, cdUsuarioOld!=0 ? cdUsuarioOld : objeto.getCdUsuario());
			pstmt.setInt(6, cdModuloOld!=0 ? cdModuloOld : objeto.getCdModulo());
			pstmt.setInt(7, cdSistemaOld!=0 ? cdSistemaOld : objeto.getCdSistema());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdUsuario, int cdModulo, int cdSistema) {
		return delete(cdUsuario, cdModulo, cdSistema, null);
	}

	public static int delete(int cdUsuario, int cdModulo, int cdSistema, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM seg_usuario_modulo WHERE cd_usuario=? AND cd_modulo=? AND cd_sistema=?");
			pstmt.setInt(1, cdUsuario);
			pstmt.setInt(2, cdModulo);
			pstmt.setInt(3, cdSistema);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static UsuarioModulo get(int cdUsuario, int cdModulo, int cdSistema) {
		return get(cdUsuario, cdModulo, cdSistema, null);
	}

	public static UsuarioModulo get(int cdUsuario, int cdModulo, int cdSistema, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_usuario_modulo WHERE cd_usuario=? AND cd_modulo=? AND cd_sistema=?");
			pstmt.setInt(1, cdUsuario);
			pstmt.setInt(2, cdModulo);
			pstmt.setInt(3, cdSistema);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new UsuarioModulo(rs.getInt("cd_usuario"),
						rs.getInt("cd_modulo"),
						rs.getInt("cd_sistema"),
						rs.getInt("lg_natureza"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_usuario_modulo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<UsuarioModulo> getList() {
		return getList(null);
	}

	public static ArrayList<UsuarioModulo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<UsuarioModulo> list = new ArrayList<UsuarioModulo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				UsuarioModulo obj = UsuarioModuloDAO.get(rsm.getInt("cd_usuario"), rsm.getInt("cd_modulo"), rsm.getInt("cd_sistema"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloDAO.getList: " + e);
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
		return Search.find("SELECT * FROM seg_usuario_modulo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
