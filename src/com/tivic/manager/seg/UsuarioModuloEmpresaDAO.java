package com.tivic.manager.seg;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class UsuarioModuloEmpresaDAO{

	public static int insert(UsuarioModuloEmpresa objeto) {
		return insert(objeto, null);
	}

	public static int insert(UsuarioModuloEmpresa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO seg_usuario_modulo_empresa (cd_usuario,"+
			                                  "cd_empresa,"+
			                                  "cd_modulo,"+
			                                  "cd_sistema) VALUES (?, ?, ?, ?)");
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdUsuario());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdModulo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdModulo());
			if(objeto.getCdSistema()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdSistema());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloEmpresaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloEmpresaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(UsuarioModuloEmpresa objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(UsuarioModuloEmpresa objeto, int cdUsuarioOld, int cdEmpresaOld, int cdModuloOld, int cdSistemaOld) {
		return update(objeto, cdUsuarioOld, cdEmpresaOld, cdModuloOld, cdSistemaOld, null);
	}

	public static int update(UsuarioModuloEmpresa objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(UsuarioModuloEmpresa objeto, int cdUsuarioOld, int cdEmpresaOld, int cdModuloOld, int cdSistemaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE seg_usuario_modulo_empresa SET cd_usuario=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_modulo=?,"+
												      		   "cd_sistema=? WHERE cd_usuario=? AND cd_empresa=? AND cd_modulo=? AND cd_sistema=?");
			pstmt.setInt(1,objeto.getCdUsuario());
			pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setInt(3,objeto.getCdModulo());
			pstmt.setInt(4,objeto.getCdSistema());
			pstmt.setInt(5, cdUsuarioOld!=0 ? cdUsuarioOld : objeto.getCdUsuario());
			pstmt.setInt(6, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(7, cdModuloOld!=0 ? cdModuloOld : objeto.getCdModulo());
			pstmt.setInt(8, cdSistemaOld!=0 ? cdSistemaOld : objeto.getCdSistema());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloEmpresaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloEmpresaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdUsuario, int cdEmpresa, int cdModulo, int cdSistema) {
		return delete(cdUsuario, cdEmpresa, cdModulo, cdSistema, null);
	}

	public static int delete(int cdUsuario, int cdEmpresa, int cdModulo, int cdSistema, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM seg_usuario_modulo_empresa WHERE cd_usuario=? AND cd_empresa=? AND cd_modulo=? AND cd_sistema=?");
			pstmt.setInt(1, cdUsuario);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdModulo);
			pstmt.setInt(4, cdSistema);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloEmpresaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloEmpresaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static UsuarioModuloEmpresa get(int cdUsuario, int cdEmpresa, int cdModulo, int cdSistema) {
		return get(cdUsuario, cdEmpresa, cdModulo, cdSistema, null);
	}

	public static UsuarioModuloEmpresa get(int cdUsuario, int cdEmpresa, int cdModulo, int cdSistema, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_usuario_modulo_empresa WHERE cd_usuario=? AND cd_empresa=? AND cd_modulo=? AND cd_sistema=?");
			pstmt.setInt(1, cdUsuario);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdModulo);
			pstmt.setInt(4, cdSistema);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new UsuarioModuloEmpresa(rs.getInt("cd_usuario"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_modulo"),
						rs.getInt("cd_sistema"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloEmpresaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloEmpresaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_usuario_modulo_empresa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloEmpresaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloEmpresaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<UsuarioModuloEmpresa> getList() {
		return getList(null);
	}

	public static ArrayList<UsuarioModuloEmpresa> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<UsuarioModuloEmpresa> list = new ArrayList<UsuarioModuloEmpresa>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				UsuarioModuloEmpresa obj = UsuarioModuloEmpresaDAO.get(rsm.getInt("cd_usuario"), rsm.getInt("cd_empresa"), rsm.getInt("cd_modulo"), rsm.getInt("cd_sistema"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioModuloEmpresaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM seg_usuario_modulo_empresa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
