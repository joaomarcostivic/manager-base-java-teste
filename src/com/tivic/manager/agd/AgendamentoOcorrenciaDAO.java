package com.tivic.manager.agd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class AgendamentoOcorrenciaDAO{

	public static int insert(AgendamentoOcorrencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(AgendamentoOcorrencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agd_agendamento_ocorrencia (cd_tipo_ocorrencia,"+
			                                  "cd_agendamento,"+
			                                  "cd_usuario,"+
			                                  "dt_ocorrencia,"+
			                                  "txt_ocorrencia,"+
			                                  "tp_visibilidade,"+
			                                  "dt_cadastro) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdTipoOcorrencia()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTipoOcorrencia());
			if(objeto.getCdAgendamento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAgendamento());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdUsuario());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			pstmt.setString(5,objeto.getTxtOcorrencia());
			pstmt.setInt(6,objeto.getTpVisibilidade());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoOcorrenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoOcorrenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AgendamentoOcorrencia objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AgendamentoOcorrencia objeto, int cdTipoOcorrenciaOld, int cdAgendamentoOld) {
		return update(objeto, cdTipoOcorrenciaOld, cdAgendamentoOld, null);
	}

	public static int update(AgendamentoOcorrencia objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AgendamentoOcorrencia objeto, int cdTipoOcorrenciaOld, int cdAgendamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE agd_agendamento_ocorrencia SET cd_tipo_ocorrencia=?,"+
												      		   "cd_agendamento=?,"+
												      		   "cd_usuario=?,"+
												      		   "dt_ocorrencia=?,"+
												      		   "txt_ocorrencia=?,"+
												      		   "tp_visibilidade=?,"+
												      		   "dt_cadastro=? WHERE cd_tipo_ocorrencia=? AND cd_agendamento=?");
			pstmt.setInt(1,objeto.getCdTipoOcorrencia());
			pstmt.setInt(2,objeto.getCdAgendamento());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdUsuario());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			pstmt.setString(5,objeto.getTxtOcorrencia());
			pstmt.setInt(6,objeto.getTpVisibilidade());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setInt(8, cdTipoOcorrenciaOld!=0 ? cdTipoOcorrenciaOld : objeto.getCdTipoOcorrencia());
			pstmt.setInt(9, cdAgendamentoOld!=0 ? cdAgendamentoOld : objeto.getCdAgendamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoOcorrenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoOcorrenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoOcorrencia, int cdAgendamento) {
		return delete(cdTipoOcorrencia, cdAgendamento, null);
	}

	public static int delete(int cdTipoOcorrencia, int cdAgendamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM agd_agendamento_ocorrencia WHERE cd_tipo_ocorrencia=? AND cd_agendamento=?");
			pstmt.setInt(1, cdTipoOcorrencia);
			pstmt.setInt(2, cdAgendamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoOcorrenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoOcorrenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AgendamentoOcorrencia get(int cdTipoOcorrencia, int cdAgendamento) {
		return get(cdTipoOcorrencia, cdAgendamento, null);
	}

	public static AgendamentoOcorrencia get(int cdTipoOcorrencia, int cdAgendamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agd_agendamento_ocorrencia WHERE cd_tipo_ocorrencia=? AND cd_agendamento=?");
			pstmt.setInt(1, cdTipoOcorrencia);
			pstmt.setInt(2, cdAgendamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AgendamentoOcorrencia(rs.getInt("cd_tipo_ocorrencia"),
						rs.getInt("cd_agendamento"),
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
			System.err.println("Erro! AgendamentoOcorrenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoOcorrenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_agendamento_ocorrencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoOcorrenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoOcorrenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AgendamentoOcorrencia> getList() {
		return getList(null);
	}

	public static ArrayList<AgendamentoOcorrencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AgendamentoOcorrencia> list = new ArrayList<AgendamentoOcorrencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AgendamentoOcorrencia obj = AgendamentoOcorrenciaDAO.get(rsm.getInt("cd_tipo_ocorrencia"), rsm.getInt("cd_agendamento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendamentoOcorrenciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM agd_agendamento_ocorrencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
