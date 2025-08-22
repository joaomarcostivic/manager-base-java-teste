package com.tivic.manager.agd;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class UsuarioDAO{

	public static int insert(Usuario objeto) {
		return insert(objeto, null);
	}

	public static int insert(Usuario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int code = com.tivic.manager.seg.UsuarioDAO.insert(objeto, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdUsuario(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agd_usuario (cd_usuario,"+
			                                  "nr_minutos_update,"+
			                                  "blb_audio) VALUES (?, ?, ?)");
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdUsuario());
			pstmt.setInt(2,objeto.getNrMinutosUpdate());
			if(objeto.getBlbAudio()==null)
				pstmt.setNull(3, Types.BINARY);
			else
				pstmt.setBytes(3,objeto.getBlbAudio());
			pstmt.executeUpdate();
			if (isConnectionNull)
				connect.commit();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioDAO.insert: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioDAO.insert: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Usuario objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Usuario objeto, int cdUsuarioOld) {
		return update(objeto, cdUsuarioOld, null);
	}

	public static int update(Usuario objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Usuario objeto, int cdUsuarioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = null;
			Usuario objetoTemp = get(objeto.getCdUsuario(), connect);
			if (objetoTemp == null)
				pstmt = connect.prepareStatement("INSERT INTO agd_usuario (cd_usuario,"+
			                                  "nr_minutos_update,"+
			                                  "blb_audio) VALUES (?, ?, ?)");
			else
				pstmt = connect.prepareStatement("UPDATE agd_usuario SET cd_usuario=?,"+
												      		   "nr_minutos_update=?,"+
												      		   "blb_audio=? WHERE cd_usuario=?");
			pstmt.setInt(1,objeto.getCdUsuario());
			pstmt.setInt(2,objeto.getNrMinutosUpdate());
			if(objeto.getBlbAudio()==null)
				pstmt.setNull(3, Types.BINARY);
			else
				pstmt.setBytes(3,objeto.getBlbAudio());
			if (objetoTemp != null) {
				pstmt.setInt(4, cdUsuarioOld!=0 ? cdUsuarioOld : objeto.getCdUsuario());
			}
			pstmt.executeUpdate();
			if (com.tivic.manager.seg.UsuarioDAO.update(objeto, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if (isConnectionNull)
				connect.commit();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioDAO.update: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioDAO.update: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdUsuario) {
		return delete(cdUsuario, null);
	}

	public static int delete(int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM agd_usuario WHERE cd_usuario=?");
			pstmt.setInt(1, cdUsuario);
			pstmt.executeUpdate();
			if (com.tivic.manager.seg.UsuarioDAO.delete(cdUsuario, connect)<=0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			if (isConnectionNull)
				connect.commit();

			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioDAO.delete: " + sqlExpt);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioDAO.delete: " +  e);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Usuario get(int cdUsuario) {
		return get(cdUsuario, null);
	}

	public static Usuario get(int cdUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agd_usuario A, seg_usuario B WHERE A.cd_usuario=B.cd_usuario AND A.cd_usuario=?");
			pstmt.setInt(1, cdUsuario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Usuario(rs.getInt("cd_usuario"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_pergunta_secreta"),
						rs.getString("nm_login"),
						rs.getString("nm_senha"),
						rs.getInt("tp_usuario"),
						rs.getString("nm_resposta_secreta"),
						rs.getInt("st_usuario"),
						rs.getInt("nr_minutos_update"),
						rs.getBytes("blb_audio")==null?null:rs.getBytes("blb_audio"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_usuario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM agd_usuario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
