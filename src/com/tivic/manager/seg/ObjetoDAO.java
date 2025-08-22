package com.tivic.manager.seg;

import java.sql.*;
import sol.dao.*;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class ObjetoDAO{

	public static int insert(Objeto objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(Objeto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_formulario");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdFormulario()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_objeto");
			keys[1].put("IS_KEY_NATIVE", "YES");
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_sistema");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdSistema()));
			int code = Conexao.getSequenceCode("seg_objeto", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			pstmt = connect.prepareStatement("INSERT INTO seg_objeto (cd_formulario,"+
			                                  "cd_objeto,"+
			                                  "cd_sistema,"+
			                                  "cd_acao,"+
			                                  "cd_modulo,"+
			                                  "tp_objeto,"+
			                                  "nm_hint,"+
			                                  "nm_objeto) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdFormulario()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdFormulario());
			pstmt.setInt(2, code);
			if(objeto.getCdSistema()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdSistema());
			if(objeto.getCdAcao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdAcao());
			if(objeto.getCdModulo()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdModulo());
			pstmt.setInt(6,objeto.getTpObjeto());
			pstmt.setString(7,objeto.getNmHint());
			pstmt.setString(8,objeto.getNmObjeto());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ObjetoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ObjetoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Objeto objeto) {
		return update(objeto, null);
	}

	public static int update(Objeto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE seg_objeto SET cd_acao=?,"+
			                                  "cd_modulo=?,"+
			                                  "tp_objeto=?,"+
			                                  "nm_hint=?,"+
			                                  "nm_objeto=? WHERE cd_formulario=? AND cd_objeto=? AND cd_sistema=?");
			if(objeto.getCdAcao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAcao());
			if(objeto.getCdModulo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdModulo());
			pstmt.setInt(3,objeto.getTpObjeto());
			pstmt.setString(4,objeto.getNmHint());
			pstmt.setString(5,objeto.getNmObjeto());
			pstmt.setInt(6,objeto.getCdFormulario());
			pstmt.setInt(7,objeto.getCdObjeto());
			pstmt.setInt(8,objeto.getCdSistema());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ObjetoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ObjetoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFormulario, int cdObjeto, int cdSistema) {
		return delete(cdFormulario, cdObjeto, cdSistema, null);
	}

	public static int delete(int cdFormulario, int cdObjeto, int cdSistema, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM seg_objeto WHERE cd_formulario=? AND cd_objeto=? AND cd_sistema=?");
			pstmt.setInt(1, cdFormulario);
			pstmt.setInt(2, cdObjeto);
			pstmt.setInt(3, cdSistema);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ObjetoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ObjetoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Objeto get(int cdFormulario, int cdObjeto, int cdSistema) {
		return get(cdFormulario, cdObjeto, cdSistema, null);
	}

	public static Objeto get(int cdFormulario, int cdObjeto, int cdSistema, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_objeto WHERE cd_formulario=? AND cd_objeto=? AND cd_sistema=?");
			pstmt.setInt(1, cdFormulario);
			pstmt.setInt(2, cdObjeto);
			pstmt.setInt(3, cdSistema);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Objeto(rs.getInt("cd_formulario"),
						rs.getInt("cd_objeto"),
						rs.getInt("cd_sistema"),
						rs.getInt("cd_acao"),
						rs.getInt("cd_modulo"),
						rs.getInt("tp_objeto"),
						rs.getString("nm_hint"),
						rs.getString("nm_objeto"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ObjetoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ObjetoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_objeto");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ObjetoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ObjetoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM seg_objeto", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
