package com.tivic.manager.adapter.base.antiga.usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class UsuarioOldDAO {
	
	public static int insert(UsuarioOld objeto) {
		return insert(objeto, null);
	}

	public static int insert(UsuarioOld objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("usuario", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			if(objeto.getCodUsuario()<=0)
				objeto.setCodUsuario(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO usuario (cod_usuario,"+
			                                  "nm_nick,"+
			                                  "nm_usuario,"+
			                                  "nr_nivel,"+
			                                  "nm_senha,"+
			                                  "st_usuario,"+
			                                  "st_login,"+
			                                  "cd_equipamento," +
			                                  "cd_pessoa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, objeto.getCodUsuario());
			pstmt.setString(2,objeto.getNmNick());
			pstmt.setString(3,objeto.getNmUsuario());
			pstmt.setInt(4,objeto.getNrNivel());
			pstmt.setString(5,objeto.getNmSenha());
			pstmt.setInt(6,objeto.getStUsuario());
			pstmt.setInt(7,objeto.getStLogin());
			pstmt.setInt(8,objeto.getCdEquipamento());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdPessoa());
			pstmt.executeUpdate();
			return objeto.getCodUsuario();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioOldDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(UsuarioOld objeto) {
		return update(objeto, 0, null);
	}

	public static int update(UsuarioOld objeto, int codUsuarioOld) {
		return update(objeto, codUsuarioOld, null);
	}

	public static int update(UsuarioOld objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(UsuarioOld objeto, int codUsuarioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE usuario SET cod_usuario=?,"+
												      		   "nm_nick=?,"+
												      		   "nm_senha=?,"+
												      		   "st_usuario=?,"+
												      		   "st_login=?,"+
												      		   "cd_equipamento=? WHERE cod_usuario=?");
			pstmt.setInt(1, objeto.getCodUsuario());
			pstmt.setString(2,objeto.getNmNick());
			pstmt.setString(3,objeto.getNmSenha());
			pstmt.setInt(4,objeto.getStUsuario());
			pstmt.setInt(5,objeto.getStLogin());
			if(objeto.getCdEquipamento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdEquipamento());
			pstmt.setInt(7, codUsuarioOld!=0 ? codUsuarioOld : objeto.getCodUsuario());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioOldDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int codUsuario) {
		return delete(codUsuario, null);
	}

	public static int delete(int codUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM usuario WHERE cod_usuario=?");
			pstmt.setInt(1, codUsuario);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioOldDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static UsuarioOld get(int codUsuario) {
		return get(codUsuario, null);
	}

	public static UsuarioOld get(int codUsuario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM usuario WHERE cod_usuario=?");
			pstmt.setInt(1, codUsuario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new UsuarioOld(rs.getInt("cod_usuario"),
						rs.getString("nm_nick"),
						rs.getString("nm_usuario"),
						rs.getInt("nr_nivel"),
						rs.getString("nm_senha"),
						rs.getInt("st_usuario"),
						rs.getInt("st_login"),
						rs.getInt("cd_equipamento"),
						rs.getInt("cd_pessoa")
						);
			}
			else
				return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioOldDAO.get: " + e);
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
		return Search.find("SELECT * FROM usuario ", " ORDER BY cod_usuario", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
