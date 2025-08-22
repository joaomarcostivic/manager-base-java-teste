package com.tivic.manager.seg;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class UsuarioPermissaoAcaoDAO{

	public static int insert(UsuarioPermissaoAcao objeto) {
		return insert(objeto, null);
	}

	public static int insert(UsuarioPermissaoAcao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO seg_usuario_permissao_acao (cd_usuario,"+
			                                  "cd_acao,"+
			                                  "cd_modulo,"+
			                                  "cd_sistema,"+
			                                  "lg_natureza) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdUsuario());
			if(objeto.getCdAcao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAcao());
			if(objeto.getCdModulo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdModulo());
			if(objeto.getCdSistema()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdSistema());
			pstmt.setInt(5,objeto.getLgNatureza());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioPermissaoAcaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioPermissaoAcaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(UsuarioPermissaoAcao objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(UsuarioPermissaoAcao objeto, int cdUsuarioOld, int cdAcaoOld, int cdModuloOld, int cdSistemaOld) {
		return update(objeto, cdUsuarioOld, cdAcaoOld, cdModuloOld, cdSistemaOld, null);
	}

	public static int update(UsuarioPermissaoAcao objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(UsuarioPermissaoAcao objeto, int cdUsuarioOld, int cdAcaoOld, int cdModuloOld, int cdSistemaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE seg_usuario_permissao_acao SET cd_usuario=?,"+
												      		   "cd_acao=?,"+
												      		   "cd_modulo=?,"+
												      		   "cd_sistema=?,"+
												      		   "lg_natureza=? WHERE cd_usuario=? AND cd_acao=? AND cd_modulo=? AND cd_sistema=?");
			pstmt.setInt(1,objeto.getCdUsuario());
			pstmt.setInt(2,objeto.getCdAcao());
			pstmt.setInt(3,objeto.getCdModulo());
			pstmt.setInt(4,objeto.getCdSistema());
			pstmt.setInt(5,objeto.getLgNatureza());
			pstmt.setInt(6, cdUsuarioOld!=0 ? cdUsuarioOld : objeto.getCdUsuario());
			pstmt.setInt(7, cdAcaoOld!=0 ? cdAcaoOld : objeto.getCdAcao());
			pstmt.setInt(8, cdModuloOld!=0 ? cdModuloOld : objeto.getCdModulo());
			pstmt.setInt(9, cdSistemaOld!=0 ? cdSistemaOld : objeto.getCdSistema());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioPermissaoAcaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioPermissaoAcaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdUsuario, int cdAcao, int cdModulo, int cdSistema) {
		return delete(cdUsuario, cdAcao, cdModulo, cdSistema, null);
	}

	public static int delete(int cdUsuario, int cdAcao, int cdModulo, int cdSistema, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM seg_usuario_permissao_acao WHERE cd_usuario=? AND cd_acao=? AND cd_modulo=? AND cd_sistema=?");
			pstmt.setInt(1, cdUsuario);
			pstmt.setInt(2, cdAcao);
			pstmt.setInt(3, cdModulo);
			pstmt.setInt(4, cdSistema);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioPermissaoAcaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioPermissaoAcaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static UsuarioPermissaoAcao get(int cdUsuario, int cdAcao, int cdModulo, int cdSistema) {
		return get(cdUsuario, cdAcao, cdModulo, cdSistema, null);
	}

	public static UsuarioPermissaoAcao get(int cdUsuario, int cdAcao, int cdModulo, int cdSistema, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_usuario_permissao_acao WHERE cd_usuario=? AND cd_acao=? AND cd_modulo=? AND cd_sistema=?");
			pstmt.setInt(1, cdUsuario);
			pstmt.setInt(2, cdAcao);
			pstmt.setInt(3, cdModulo);
			pstmt.setInt(4, cdSistema);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new UsuarioPermissaoAcao(rs.getInt("cd_usuario"),
						rs.getInt("cd_acao"),
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
			System.err.println("Erro! UsuarioPermissaoAcaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioPermissaoAcaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_usuario_permissao_acao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioPermissaoAcaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioPermissaoAcaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM seg_usuario_permissao_acao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
