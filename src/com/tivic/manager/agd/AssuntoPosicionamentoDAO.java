package com.tivic.manager.agd;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class AssuntoPosicionamentoDAO{

	public static int insert(AssuntoPosicionamento objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(AssuntoPosicionamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_agendamento");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdAgendamento()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_assunto");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdAssunto()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_posicionamento");
			keys[2].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("agd_assunto_posicionamento", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdPosicionamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agd_assunto_posicionamento (cd_agendamento,"+
			                                  "cd_assunto,"+
			                                  "cd_posicionamento,"+
			                                  "txt_observacao,"+
			                                  "lg_contrario,"+
			                                  "cd_agenda,"+
			                                  "cd_participante,"+
			                                  "cd_convidado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdAgendamento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAgendamento());
			if(objeto.getCdAssunto()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdAssunto());
			pstmt.setInt(3, code);
			pstmt.setString(4,objeto.getTxtObservacao());
			pstmt.setInt(5,objeto.getLgContrario());
			if(objeto.getCdAgenda()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdAgenda());
			if(objeto.getCdParticipante()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdParticipante());
			if(objeto.getCdConvidado()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdConvidado());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AssuntoPosicionamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AssuntoPosicionamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AssuntoPosicionamento objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(AssuntoPosicionamento objeto, int cdAgendamentoOld, int cdAssuntoOld, int cdPosicionamentoOld) {
		return update(objeto, cdAgendamentoOld, cdAssuntoOld, cdPosicionamentoOld, null);
	}

	public static int update(AssuntoPosicionamento objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(AssuntoPosicionamento objeto, int cdAgendamentoOld, int cdAssuntoOld, int cdPosicionamentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE agd_assunto_posicionamento SET cd_agendamento=?,"+
												      		   "cd_assunto=?,"+
												      		   "cd_posicionamento=?,"+
												      		   "txt_observacao=?,"+
												      		   "lg_contrario=?,"+
												      		   "cd_agenda=?,"+
												      		   "cd_participante=?,"+
												      		   "cd_convidado=? WHERE cd_agendamento=? AND cd_assunto=? AND cd_posicionamento=?");
			pstmt.setInt(1,objeto.getCdAgendamento());
			pstmt.setInt(2,objeto.getCdAssunto());
			pstmt.setInt(3,objeto.getCdPosicionamento());
			pstmt.setString(4,objeto.getTxtObservacao());
			pstmt.setInt(5,objeto.getLgContrario());
			if(objeto.getCdAgenda()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdAgenda());
			if(objeto.getCdParticipante()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdParticipante());
			if(objeto.getCdConvidado()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdConvidado());
			pstmt.setInt(9, cdAgendamentoOld!=0 ? cdAgendamentoOld : objeto.getCdAgendamento());
			pstmt.setInt(10, cdAssuntoOld!=0 ? cdAssuntoOld : objeto.getCdAssunto());
			pstmt.setInt(11, cdPosicionamentoOld!=0 ? cdPosicionamentoOld : objeto.getCdPosicionamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AssuntoPosicionamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AssuntoPosicionamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAgendamento, int cdAssunto, int cdPosicionamento) {
		return delete(cdAgendamento, cdAssunto, cdPosicionamento, null);
	}

	public static int delete(int cdAgendamento, int cdAssunto, int cdPosicionamento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM agd_assunto_posicionamento WHERE cd_agendamento=? AND cd_assunto=? AND cd_posicionamento=?");
			pstmt.setInt(1, cdAgendamento);
			pstmt.setInt(2, cdAssunto);
			pstmt.setInt(3, cdPosicionamento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AssuntoPosicionamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AssuntoPosicionamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AssuntoPosicionamento get(int cdAgendamento, int cdAssunto, int cdPosicionamento) {
		return get(cdAgendamento, cdAssunto, cdPosicionamento, null);
	}

	public static AssuntoPosicionamento get(int cdAgendamento, int cdAssunto, int cdPosicionamento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agd_assunto_posicionamento WHERE cd_agendamento=? AND cd_assunto=? AND cd_posicionamento=?");
			pstmt.setInt(1, cdAgendamento);
			pstmt.setInt(2, cdAssunto);
			pstmt.setInt(3, cdPosicionamento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AssuntoPosicionamento(rs.getInt("cd_agendamento"),
						rs.getInt("cd_assunto"),
						rs.getInt("cd_posicionamento"),
						rs.getString("txt_observacao"),
						rs.getInt("lg_contrario"),
						rs.getInt("cd_agenda"),
						rs.getInt("cd_participante"),
						rs.getInt("cd_convidado"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AssuntoPosicionamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AssuntoPosicionamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_assunto_posicionamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AssuntoPosicionamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AssuntoPosicionamentoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM agd_assunto_posicionamento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
