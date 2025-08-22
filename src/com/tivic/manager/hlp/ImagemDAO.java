package com.tivic.manager.hlp;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ImagemDAO{

	public static int insert(Imagem objeto) {
		return insert(objeto, null);
	}

	public static int insert(Imagem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("hlp_imagem", connect);
			if (code <= 0) {
				if (isConnectionNull)
					sol.dao.Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdImagem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO hlp_imagem (cd_imagem,"+
			                                  "id_imagem,"+
			                                  "blb_imagem,"+
			                                  "nm_titulo,"+
			                                  "txt_rotulo) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getIdImagem());
			if(objeto.getBlbImagem()==null)
				pstmt.setNull(3, Types.BINARY);
			else
				pstmt.setBytes(3,objeto.getBlbImagem());
			pstmt.setString(4,objeto.getNmTitulo());
			pstmt.setString(5,objeto.getTxtRotulo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ImagemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ImagemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Imagem objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Imagem objeto, int cdImagemOld) {
		return update(objeto, cdImagemOld, null);
	}

	public static int update(Imagem objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Imagem objeto, int cdImagemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE hlp_imagem SET cd_imagem=?,"+
												      		   "id_imagem=?,"+
												      		   "blb_imagem=?,"+
												      		   "nm_titulo=?,"+
												      		   "txt_rotulo=? WHERE cd_imagem=?");
			pstmt.setInt(1,objeto.getCdImagem());
			pstmt.setString(2,objeto.getIdImagem());
			if(objeto.getBlbImagem()==null)
				pstmt.setNull(3, Types.BINARY);
			else
				pstmt.setBytes(3,objeto.getBlbImagem());
			pstmt.setString(4,objeto.getNmTitulo());
			pstmt.setString(5,objeto.getTxtRotulo());
			pstmt.setInt(6, cdImagemOld!=0 ? cdImagemOld : objeto.getCdImagem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ImagemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ImagemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdImagem) {
		return delete(cdImagem, null);
	}

	public static int delete(int cdImagem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM hlp_imagem WHERE cd_imagem=?");
			pstmt.setInt(1, cdImagem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ImagemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ImagemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Imagem get(int cdImagem) {
		return get(cdImagem, null);
	}

	public static Imagem get(int cdImagem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM hlp_imagem WHERE cd_imagem=?");
			pstmt.setInt(1, cdImagem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Imagem(rs.getInt("cd_imagem"),
						rs.getString("id_imagem"),
						rs.getBytes("blb_imagem")==null?null:rs.getBytes("blb_imagem"),
						rs.getString("nm_titulo"),
						rs.getString("txt_rotulo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ImagemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ImagemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM hlp_imagem");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ImagemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ImagemDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM hlp_imagem", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
