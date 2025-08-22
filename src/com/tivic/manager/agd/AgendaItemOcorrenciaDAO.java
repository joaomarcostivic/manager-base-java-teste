package com.tivic.manager.agd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class AgendaItemOcorrenciaDAO{

	public static int insert(AgendaItemOcorrencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(AgendaItemOcorrencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_ocorrencia");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_agenda_item");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdAgendaItem()));
			int code = Conexao.getSequenceCode("agd_agenda_item_ocorrencia", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOcorrencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agd_agenda_item_ocorrencia (cd_ocorrencia,"+
			                                  "cd_agenda_item,"+
			                                  "cd_tipo_ocorrencia,"+
			                                  "cd_usuario,"+
			                                  "dt_ocorrencia,"+
			                                  "txt_ocorrencia,"+
			                                  "tp_visibilidade,"+
			                                  "dt_cadastro) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdAgendaItem()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAgendaItem());
			if(objeto.getCdTipoOcorrencia()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoOcorrencia());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuario());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			pstmt.setString(6,objeto.getTxtOcorrencia());
			pstmt.setInt(7,objeto.getTpVisibilidade());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemOcorrenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemOcorrenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AgendaItemOcorrencia objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AgendaItemOcorrencia objeto, int cdOcorrenciaOld, int cdAgendaItemOld) {
		return update(objeto, cdOcorrenciaOld, cdAgendaItemOld, null);
	}

	public static int update(AgendaItemOcorrencia objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AgendaItemOcorrencia objeto, int cdOcorrenciaOld, int cdAgendaItemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE agd_agenda_item_ocorrencia SET cd_ocorrencia=?,"+
												      		   "cd_agenda_item=?,"+
												      		   "cd_tipo_ocorrencia=?,"+
												      		   "cd_usuario=?,"+
												      		   "dt_ocorrencia=?,"+
												      		   "txt_ocorrencia=?,"+
												      		   "tp_visibilidade=?,"+
												      		   "dt_cadastro=? WHERE cd_ocorrencia=? AND cd_agenda_item=?");
			pstmt.setInt(1,objeto.getCdOcorrencia());
			pstmt.setInt(2,objeto.getCdAgendaItem());
			if(objeto.getCdTipoOcorrencia()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoOcorrencia());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuario());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			pstmt.setString(6,objeto.getTxtOcorrencia());
			pstmt.setInt(7,objeto.getTpVisibilidade());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setInt(9, cdOcorrenciaOld!=0 ? cdOcorrenciaOld : objeto.getCdOcorrencia());
			pstmt.setInt(10, cdAgendaItemOld!=0 ? cdAgendaItemOld : objeto.getCdAgendaItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemOcorrenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemOcorrenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOcorrencia, int cdAgendaItem) {
		return delete(cdOcorrencia, cdAgendaItem, null);
	}

	public static int delete(int cdOcorrencia, int cdAgendaItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM agd_agenda_item_ocorrencia WHERE cd_ocorrencia=? AND cd_agenda_item=?");
			pstmt.setInt(1, cdOcorrencia);
			pstmt.setInt(2, cdAgendaItem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemOcorrenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemOcorrenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AgendaItemOcorrencia get(int cdOcorrencia, int cdAgendaItem) {
		return get(cdOcorrencia, cdAgendaItem, null);
	}

	public static AgendaItemOcorrencia get(int cdOcorrencia, int cdAgendaItem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agd_agenda_item_ocorrencia WHERE cd_ocorrencia=? AND cd_agenda_item=?");
			pstmt.setInt(1, cdOcorrencia);
			pstmt.setInt(2, cdAgendaItem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AgendaItemOcorrencia(rs.getInt("cd_ocorrencia"),
						rs.getInt("cd_agenda_item"),
						rs.getInt("cd_tipo_ocorrencia"),
						rs.getInt("cd_usuario"),
						(rs.getTimestamp("dt_ocorrencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ocorrencia").getTime()),
						rs.getString("txt_ocorrencia"),
						rs.getInt("tp_visibilidade"),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemOcorrenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemOcorrenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_agenda_item_ocorrencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemOcorrenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemOcorrenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AgendaItemOcorrencia> getList() {
		return getList(null);
	}

	public static ArrayList<AgendaItemOcorrencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AgendaItemOcorrencia> list = new ArrayList<AgendaItemOcorrencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AgendaItemOcorrencia obj = AgendaItemOcorrenciaDAO.get(rsm.getInt("cd_ocorrencia"), rsm.getInt("cd_agenda_item"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemOcorrenciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM agd_agenda_item_ocorrencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
