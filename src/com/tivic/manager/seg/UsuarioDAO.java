package com.tivic.manager.seg;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
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
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("seg_usuario", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if(objeto.getCdUsuario()<=0)
				objeto.setCdUsuario(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO seg_usuario (cd_usuario,"+
			                                  "cd_pessoa,"+
			                                  "cd_pergunta_secreta,"+
			                                  "nm_login,"+
			                                  "nm_senha,"+
			                                  "tp_usuario,"+
			                                  "nm_resposta_secreta,"+
			                                  "st_usuario,"+
			                                  "st_login,"+
			                                  "cd_equipamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCdUsuario());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			if(objeto.getCdPerguntaSecreta()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPerguntaSecreta());
			pstmt.setString(4,objeto.getNmLogin());
			pstmt.setString(5,objeto.getNmSenha());
			pstmt.setInt(6,objeto.getTpUsuario());
			pstmt.setString(7,objeto.getNmRespostaSecreta());
			pstmt.setInt(8,objeto.getStUsuario());
			if(objeto.getStLogin()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getStLogin());
			if(objeto.getCdEquipamento()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdEquipamento());
			pstmt.executeUpdate();
			return objeto.getCdUsuario();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioDAO.insert: " +  e);
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
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE seg_usuario SET cd_usuario=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_pergunta_secreta=?,"+
												      		   "nm_login=?,"+
												      		   "nm_senha=?,"+
												      		   "tp_usuario=?,"+
												      		   "nm_resposta_secreta=?,"+
												      		   "st_usuario=?, "+ 
												      		   "st_login=?,"+
												      		   "cd_equipamento=? "+
												      		   "WHERE cd_usuario=?");
			pstmt.setInt(1,objeto.getCdUsuario());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			if(objeto.getCdPerguntaSecreta()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPerguntaSecreta());
			pstmt.setString(4,objeto.getNmLogin());
			pstmt.setString(5,objeto.getNmSenha());
			pstmt.setInt(6,objeto.getTpUsuario());
			pstmt.setString(7,objeto.getNmRespostaSecreta());
			pstmt.setInt(8,objeto.getStUsuario());
			pstmt.setInt(9,objeto.getStLogin());
			if(objeto.getCdEquipamento()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdEquipamento());
			pstmt.setInt(11, cdUsuarioOld!=0 ? cdUsuarioOld : objeto.getCdUsuario());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioDAO.update: " +  e);
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
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM seg_usuario WHERE cd_usuario=?");
			pstmt.setInt(1, cdUsuario);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioDAO.delete: " +  e);
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_usuario WHERE cd_usuario=?");
			pstmt.setInt(1, cdUsuario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Usuario(rs.getInt("cd_usuario"),
						rs.getInt("cd_pessoa"),
						rs.getString("nm_login"),
						rs.getInt("cd_pergunta_secreta"),
						rs.getInt("tp_usuario"),
						rs.getString("nm_senha"),
						rs.getInt("st_usuario"),
						rs.getString("nm_resposta_secreta"),
						rs.getInt("st_login"),
						rs.getInt("cd_equipamento"));
			}
			else
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_usuario");
			return new ResultSetMap(pstmt.executeQuery());
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
		return Search.find("SELECT * FROM seg_usuario ", " ORDER BY nm_login", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
