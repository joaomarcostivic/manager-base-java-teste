package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class FormularioDAO{

	public static int insert(Formulario objeto) {
		return insert(objeto, null);
	}

	public static int insert(Formulario objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_formulario", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdFormulario(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_formulario (cd_formulario,"+
			                                  "nm_formulario,"+
			                                  "id_formulario,"+
			                                  "dt_versao,"+
			                                  "st_formulario,"+
			                                  "ds_formulario,"+
			                                  "nm_link_formulario,"+
			                                  "dt_inicio_formulario,"+
			                                  "dt_fim_formulario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmFormulario());
			pstmt.setString(3,objeto.getIdFormulario());
			if(objeto.getDtVersao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtVersao().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStFormulario());
			pstmt.setString(6,objeto.getDsFormulario());
			pstmt.setString(7,objeto.getNmLinkFormulario());
			if(objeto.getDtInicioFormulario()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtInicioFormulario().getTimeInMillis()));
			if(objeto.getDtFimFormulario()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtFimFormulario().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Formulario objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Formulario objeto, int cdFormularioOld) {
		return update(objeto, cdFormularioOld, null);
	}

	public static int update(Formulario objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Formulario objeto, int cdFormularioOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_formulario SET cd_formulario=?,"+
												      		   "nm_formulario=?,"+
												      		   "id_formulario=?,"+
												      		   "dt_versao=?,"+
												      		   "st_formulario=?,"+
												      		   "ds_formulario=?,"+
												      		   "nm_link_formulario=?,"+
												      		   "dt_inicio_formulario=?,"+
												      		   "dt_fim_formulario=? WHERE cd_formulario=?");
			pstmt.setInt(1,objeto.getCdFormulario());
			pstmt.setString(2,objeto.getNmFormulario());
			pstmt.setString(3,objeto.getIdFormulario());
			if(objeto.getDtVersao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtVersao().getTimeInMillis()));
			pstmt.setInt(5,objeto.getStFormulario());
			pstmt.setString(6,objeto.getDsFormulario());
			pstmt.setString(7,objeto.getNmLinkFormulario());
			if(objeto.getDtInicioFormulario()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtInicioFormulario().getTimeInMillis()));
			if(objeto.getDtFimFormulario()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtFimFormulario().getTimeInMillis()));
			pstmt.setInt(10, cdFormularioOld!=0 ? cdFormularioOld : objeto.getCdFormulario());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdFormulario) {
		return delete(cdFormulario, null);
	}

	public static int delete(int cdFormulario, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_formulario WHERE cd_formulario=?");
			pstmt.setInt(1, cdFormulario);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Formulario get(int cdFormulario) {
		return get(cdFormulario, null);
	}

	public static Formulario get(int cdFormulario, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_formulario WHERE cd_formulario=?");
			pstmt.setInt(1, cdFormulario);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Formulario(rs.getInt("cd_formulario"),
						rs.getString("nm_formulario"),
						rs.getString("id_formulario"),
						(rs.getTimestamp("dt_versao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_versao").getTime()),
						rs.getInt("st_formulario"),
						rs.getString("ds_formulario"),
						rs.getString("nm_link_formulario"),
						(rs.getTimestamp("dt_inicio_formulario")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio_formulario").getTime()),
						(rs.getTimestamp("dt_fim_formulario")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_fim_formulario").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_formulario");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! FormularioDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Formulario> getList() {
		return getList(null);
	}

	public static ArrayList<Formulario> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Formulario> list = new ArrayList<Formulario>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Formulario obj = FormularioDAO.get(rsm.getInt("cd_formulario"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! FormularioDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_formulario", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
