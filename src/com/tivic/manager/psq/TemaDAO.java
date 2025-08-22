package com.tivic.manager.psq;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TemaDAO{

	public static int insert(Tema objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(Tema objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_tema");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_questionario");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdQuestionario()));
			int code = Conexao.getSequenceCode("psq_tema", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTema(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO psq_tema (cd_tema,"+
			                                  "cd_questionario,"+
			                                  "cd_tema_superior,"+
			                                  "nm_tema,"+
			                                  "txt_tema) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdQuestionario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdQuestionario());
			if(objeto.getCdTemaSuperior()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTemaSuperior());
			pstmt.setString(4,objeto.getNmTema());
			pstmt.setString(5,objeto.getTxtTema());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TemaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TemaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Tema objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Tema objeto, int cdTemaOld, int cdQuestionarioOld) {
		return update(objeto, cdTemaOld, cdQuestionarioOld, null);
	}

	public static int update(Tema objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Tema objeto, int cdTemaOld, int cdQuestionarioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE psq_tema SET cd_tema=?,"+
												      		   "cd_questionario=?,"+
												      		   "cd_tema_superior=?,"+
												      		   "nm_tema=?,"+
												      		   "txt_tema=? WHERE cd_tema=? AND cd_questionario=?");
			pstmt.setInt(1,objeto.getCdTema());
			pstmt.setInt(2,objeto.getCdQuestionario());
			if(objeto.getCdTemaSuperior()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTemaSuperior());
			pstmt.setString(4,objeto.getNmTema());
			pstmt.setString(5,objeto.getTxtTema());
			pstmt.setInt(6, cdTemaOld!=0 ? cdTemaOld : objeto.getCdTema());
			pstmt.setInt(7, cdQuestionarioOld!=0 ? cdQuestionarioOld : objeto.getCdQuestionario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TemaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TemaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTema, int cdQuestionario) {
		return delete(cdTema, cdQuestionario, null);
	}

	public static int delete(int cdTema, int cdQuestionario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM psq_tema WHERE cd_tema=? AND cd_questionario=?");
			pstmt.setInt(1, cdTema);
			pstmt.setInt(2, cdQuestionario);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TemaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TemaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Tema get(int cdTema, int cdQuestionario) {
		return get(cdTema, cdQuestionario, null);
	}

	public static Tema get(int cdTema, int cdQuestionario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM psq_tema WHERE cd_tema=? AND cd_questionario=?");
			pstmt.setInt(1, cdTema);
			pstmt.setInt(2, cdQuestionario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Tema(rs.getInt("cd_tema"),
						rs.getInt("cd_questionario"),
						rs.getInt("cd_tema_superior"),
						rs.getString("nm_tema"),
						rs.getString("txt_tema"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TemaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TemaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM psq_tema");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TemaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TemaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM psq_tema", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
