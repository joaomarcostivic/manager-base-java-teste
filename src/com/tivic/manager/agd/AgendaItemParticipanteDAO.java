package com.tivic.manager.agd;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class AgendaItemParticipanteDAO{

	public static int insert(AgendaItemParticipante objeto) {
		return insert(objeto, null);
	}

	public static int insert(AgendaItemParticipante objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO agd_agenda_item_participante (cd_agenda_item,"+
			                                  "cd_pessoa,"+
			                                  "st_participacao,"+
			                                  "tp_participacao,"+
			                                  "dt_cadastro,"+
			                                  "txt_observacao) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdAgendaItem()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAgendaItem());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setInt(3,objeto.getStParticipacao());
			pstmt.setInt(4,objeto.getTpParticipacao());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setString(6,objeto.getTxtObservacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemParticipanteDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemParticipanteDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AgendaItemParticipante objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(AgendaItemParticipante objeto, int cdAgendaItemOld, int cdPessoaOld) {
		return update(objeto, cdAgendaItemOld, cdPessoaOld, null);
	}

	public static int update(AgendaItemParticipante objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(AgendaItemParticipante objeto, int cdAgendaItemOld, int cdPessoaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE agd_agenda_item_participante SET cd_agenda_item=?,"+
												      		   "cd_pessoa=?,"+
												      		   "st_participacao=?,"+
												      		   "tp_participacao=?,"+
												      		   "dt_cadastro=?,"+
												      		   "txt_observacao=? WHERE cd_agenda_item=? AND cd_pessoa=?");
			pstmt.setInt(1,objeto.getCdAgendaItem());
			pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setInt(3,objeto.getStParticipacao());
			pstmt.setInt(4,objeto.getTpParticipacao());
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setString(6,objeto.getTxtObservacao());
			pstmt.setInt(7, cdAgendaItemOld!=0 ? cdAgendaItemOld : objeto.getCdAgendaItem());
			pstmt.setInt(8, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemParticipanteDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemParticipanteDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAgendaItem, int cdPessoa) {
		return delete(cdAgendaItem, cdPessoa, null);
	}

	public static int delete(int cdAgendaItem, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM agd_agenda_item_participante WHERE cd_agenda_item=? AND cd_pessoa=?");
			pstmt.setInt(1, cdAgendaItem);
			pstmt.setInt(2, cdPessoa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemParticipanteDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemParticipanteDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AgendaItemParticipante get(int cdAgendaItem, int cdPessoa) {
		return get(cdAgendaItem, cdPessoa, null);
	}

	public static AgendaItemParticipante get(int cdAgendaItem, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM agd_agenda_item_participante WHERE cd_agenda_item=? AND cd_pessoa=?");
			pstmt.setInt(1, cdAgendaItem);
			pstmt.setInt(2, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AgendaItemParticipante(rs.getInt("cd_agenda_item"),
						rs.getInt("cd_pessoa"),
						rs.getInt("st_participacao"),
						rs.getInt("tp_participacao"),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()),
						rs.getString("txt_observacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemParticipanteDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemParticipanteDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM agd_agenda_item_participante");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemParticipanteDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemParticipanteDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AgendaItemParticipante> getList() {
		return getList(null);
	}

	public static ArrayList<AgendaItemParticipante> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AgendaItemParticipante> list = new ArrayList<AgendaItemParticipante>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AgendaItemParticipante obj = AgendaItemParticipanteDAO.get(rsm.getInt("cd_agenda_item"), rsm.getInt("cd_pessoa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AgendaItemParticipanteDAO.getList: " + e);
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
		return Search.find("SELECT * FROM agd_agenda_item_participante", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
