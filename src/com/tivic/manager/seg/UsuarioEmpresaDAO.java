package com.tivic.manager.seg;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class UsuarioEmpresaDAO{

	public static int insert(UsuarioEmpresa objeto) {
		return insert(objeto, null);
	}

	public static int insert(UsuarioEmpresa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_empresa");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdEmpresa()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_usuario");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdUsuario()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "nr_horario");
			keys[2].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("seg_usuario_empresa", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setNrHorario(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO seg_usuario_empresa (cd_empresa,"+
			                                  "cd_usuario,"+
			                                  "nr_horario,"+
			                                  "hr_inicial,"+
			                                  "hr_final) VALUES (?, ?, ?, ?, ?)");
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpresa());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdUsuario());
			pstmt.setInt(3, code);
			if(objeto.getHrInicial()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getHrInicial().getTimeInMillis()));
			if(objeto.getHrFinal()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getHrFinal().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioEmpresaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioEmpresaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(UsuarioEmpresa objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(UsuarioEmpresa objeto, int cdEmpresaOld, int cdUsuarioOld, int nrHorarioOld) {
		return update(objeto, cdEmpresaOld, cdUsuarioOld, nrHorarioOld, null);
	}

	public static int update(UsuarioEmpresa objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(UsuarioEmpresa objeto, int cdEmpresaOld, int cdUsuarioOld, int nrHorarioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE seg_usuario_empresa SET cd_empresa=?,"+
												      		   "cd_usuario=?,"+
												      		   "nr_horario=?,"+
												      		   "hr_inicial=?,"+
												      		   "hr_final=? WHERE cd_empresa=? AND cd_usuario=? AND nr_horario=?");
			pstmt.setInt(1,objeto.getCdEmpresa());
			pstmt.setInt(2,objeto.getCdUsuario());
			pstmt.setInt(3,objeto.getNrHorario());
			if(objeto.getHrInicial()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getHrInicial().getTimeInMillis()));
			if(objeto.getHrFinal()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getHrFinal().getTimeInMillis()));
			pstmt.setInt(6, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(7, cdUsuarioOld!=0 ? cdUsuarioOld : objeto.getCdUsuario());
			pstmt.setInt(8, nrHorarioOld!=0 ? nrHorarioOld : objeto.getNrHorario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioEmpresaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioEmpresaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEmpresa, int cdUsuario, int nrHorario) {
		return delete(cdEmpresa, cdUsuario, nrHorario, null);
	}

	public static int delete(int cdEmpresa, int cdUsuario, int nrHorario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM seg_usuario_empresa WHERE cd_empresa=? AND cd_usuario=? AND nr_horario=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdUsuario);
			pstmt.setInt(3, nrHorario);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioEmpresaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioEmpresaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static UsuarioEmpresa get(int cdEmpresa, int cdUsuario, int nrHorario) {
		return get(cdEmpresa, cdUsuario, nrHorario, null);
	}

	public static UsuarioEmpresa get(int cdEmpresa, int cdUsuario, int nrHorario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM seg_usuario_empresa WHERE cd_empresa=? AND cd_usuario=? AND nr_horario=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdUsuario);
			pstmt.setInt(3, nrHorario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new UsuarioEmpresa(rs.getInt("cd_empresa"),
						rs.getInt("cd_usuario"),
						rs.getInt("nr_horario"),
						(rs.getTimestamp("hr_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_inicial").getTime()),
						(rs.getTimestamp("hr_final")==null)?null:Util.longToCalendar(rs.getTimestamp("hr_final").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioEmpresaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioEmpresaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM seg_usuario_empresa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! UsuarioEmpresaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioEmpresaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<UsuarioEmpresa> getList() {
		return getList(null);
	}

	public static ArrayList<UsuarioEmpresa> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<UsuarioEmpresa> list = new ArrayList<UsuarioEmpresa>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				UsuarioEmpresa obj = UsuarioEmpresaDAO.get(rsm.getInt("cd_empresa"), rsm.getInt("cd_usuario"), rsm.getInt("nr_horario"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! UsuarioEmpresaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM seg_usuario_empresa", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}