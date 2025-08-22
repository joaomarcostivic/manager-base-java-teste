package com.tivic.manager.seg;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class UsuarioSistemaEmpresaDAO{

	public static int insert(UsuarioSistemaEmpresa objeto) {
		return insert(objeto, null);
	}

	public static int insert(UsuarioSistemaEmpresa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO seg_usuario_sistema_empresa (cd_usuario,"+
			                                  "cd_sistema,"+
			                                  "cd_empresa) VALUES (?, ?, ?)");
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdUsuario());
			if(objeto.getCdSistema()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdSistema());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(UsuarioSistemaEmpresa objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(UsuarioSistemaEmpresa objeto, int cdUsuarioOld, int cdSistemaOld, int cdEmpresaOld) {
		return update(objeto, cdUsuarioOld, cdSistemaOld, cdEmpresaOld, null);
	}

	public static int update(UsuarioSistemaEmpresa objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(UsuarioSistemaEmpresa objeto, int cdUsuarioOld, int cdSistemaOld, int cdEmpresaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE seg_usuario_sistema_empresa SET cd_usuario=?,"+
												      		   "cd_sistema=?,"+
												      		   "cd_empresa=? WHERE cd_usuario=? AND cd_sistema=? AND cd_empresa=?");
			pstmt.setInt(1,objeto.getCdUsuario());
			pstmt.setInt(2,objeto.getCdSistema());
			pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setInt(4, cdUsuarioOld!=0 ? cdUsuarioOld : objeto.getCdUsuario());
			pstmt.setInt(5, cdSistemaOld!=0 ? cdSistemaOld : objeto.getCdSistema());
			pstmt.setInt(6, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdUsuario, int cdSistema, int cdEmpresa) {
		return delete(cdUsuario, cdSistema, cdEmpresa, null);
	}

	public static int delete(int cdUsuario, int cdSistema, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM seg_usuario_sistema_empresa WHERE cd_usuario=? AND cd_sistema=? AND cd_empresa=?");
			pstmt.setInt(1, cdUsuario);
			pstmt.setInt(2, cdSistema);
			pstmt.setInt(3, cdEmpresa);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static UsuarioSistemaEmpresa get(int cdUsuario, int cdSistema, int cdEmpresa) {
		return get(cdUsuario, cdSistema, cdEmpresa, null);
	}

	public static UsuarioSistemaEmpresa get(int cdUsuario, int cdSistema, int cdEmpresa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_usuario_sistema_empresa WHERE cd_usuario=? AND cd_sistema=? AND cd_empresa=?");
			pstmt.setInt(1, cdUsuario);
			pstmt.setInt(2, cdSistema);
			pstmt.setInt(3, cdEmpresa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new UsuarioSistemaEmpresa(rs.getInt("cd_usuario"),
						rs.getInt("cd_sistema"),
						rs.getInt("cd_empresa"));
			}
			else
				return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioSistemaEmpresaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_usuario_sistema_empresa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		return Search.find("SELECT * FROM seg_usuario_sistema_empresa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
