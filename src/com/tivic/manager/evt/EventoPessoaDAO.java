package com.tivic.manager.evt;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class EventoPessoaDAO{

	public static int insert(EventoPessoa objeto) {
		return insert(objeto, null);
	}

	public static int insert(EventoPessoa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_pessoa");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdPessoa()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_evento");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdEvento()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_inscricao");
			keys[2].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("evt_evento_pessoa", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdInscricao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO evt_evento_pessoa (cd_pessoa,"+
                    "cd_evento,"+
                    "tp_participacao,"+
                    "id_cadastro,"+
                    "nr_matricula,"+
                    "tp_cargo_publico,"+
                    "cd_conta_receber,"+
                    "cd_subevento,"+
                    "cd_inscricao,"+
                    "dt_inscricao,"+
                    "cd_local) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdPessoa());
			if(objeto.getCdEvento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEvento());
			pstmt.setInt(3,objeto.getTpParticipacao());
			pstmt.setString(4,objeto.getIdCadastro());
			pstmt.setString(5,objeto.getNrMatricula());
			pstmt.setInt(6,objeto.getTpCargoPublico());
			pstmt.setInt(7,objeto.getCdContaReceber());
			pstmt.setInt(8,objeto.getCdSubevento());
			pstmt.setInt(9, code);
			if(objeto.getDtInscricao()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtInscricao().getTimeInMillis()));
			pstmt.setInt(11,objeto.getCdLocal());
			
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoPessoaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoPessoaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EventoPessoa objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(EventoPessoa objeto, int cdPessoaOld, int cdEventoOld, int cdInscricaoOld) {
		return update(objeto, cdPessoaOld, cdEventoOld, cdInscricaoOld, null);
	}

	public static int update(EventoPessoa objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(EventoPessoa objeto, int cdPessoaOld, int cdEventoOld, int cdInscricaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE evt_evento_pessoa SET cd_pessoa=?,"+
												      		   "cd_evento=?,"+
												      		   "tp_participacao=?,"+
												      		   "id_cadastro=?,"+
												      		   "nr_matricula=?,"+
												      		   "tp_cargo_publico=?,"+
												      		   "cd_conta_receber=?,"+
												      		   "cd_subevento=?,"+
												      		   "cd_inscricao=?,"+
												      		   "dt_inscricao=?,"+ 
												      		   "cd_local=? WHERE cd_pessoa=? AND cd_evento=? AND cd_inscricao=?");
			pstmt.setInt(1,objeto.getCdPessoa());
			pstmt.setInt(2,objeto.getCdEvento());
			pstmt.setInt(3,objeto.getTpParticipacao());
			pstmt.setString(4,objeto.getIdCadastro());
			pstmt.setString(5,objeto.getNrMatricula());
			pstmt.setInt(6,objeto.getTpCargoPublico());
			pstmt.setInt(7,objeto.getCdContaReceber());
			pstmt.setInt(8,objeto.getCdSubevento());
			pstmt.setInt(9,objeto.getCdInscricao());
			if(objeto.getDtInscricao()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtInscricao().getTimeInMillis()));
			pstmt.setInt(11, objeto.getCdLocal());
			
			pstmt.setInt(12, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.setInt(13, cdEventoOld!=0 ? cdEventoOld : objeto.getCdEvento());
			pstmt.setInt(14, cdInscricaoOld!=0 ? cdInscricaoOld : objeto.getCdInscricao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoPessoaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoPessoaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdPessoa, int cdEvento, int cdInscricao) {
		return delete(cdPessoa, cdEvento, cdInscricao, null);
	}

	public static int delete(int cdPessoa, int cdEvento, int cdInscricao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM evt_evento_pessoa WHERE cd_pessoa=? AND cd_evento=? AND cd_inscricao=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdEvento);
			pstmt.setInt(3, cdInscricao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoPessoaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoPessoaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EventoPessoa get(int cdPessoa, int cdEvento, int cdInscricao) {
		return get(cdPessoa, cdEvento, cdInscricao, null);
	}

	public static EventoPessoa get(int cdPessoa, int cdEvento, int cdInscricao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM evt_evento_pessoa WHERE cd_pessoa=? AND cd_evento=? AND cd_inscricao=?");
			pstmt.setInt(1, cdPessoa);
			pstmt.setInt(2, cdEvento);
			pstmt.setInt(3, cdInscricao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EventoPessoa(rs.getInt("cd_pessoa"),
						rs.getInt("cd_evento"),
						rs.getInt("tp_participacao"),
						rs.getString("id_cadastro"),
						rs.getString("nr_matricula"),
						rs.getInt("tp_cargo_publico"),
						rs.getInt("cd_conta_receber"),
						rs.getInt("cd_subevento"),
						rs.getInt("cd_inscricao"),
						(rs.getTimestamp("dt_inscricao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inscricao").getTime()),
						rs.getInt("cd_local"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoPessoaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoPessoaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM evt_evento_pessoa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoPessoaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoPessoaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<EventoPessoa> getList() {
		return getList(null);
	}

	public static ArrayList<EventoPessoa> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<EventoPessoa> list = new ArrayList<EventoPessoa>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				EventoPessoa obj = EventoPessoaDAO.get(rsm.getInt("cd_pessoa"), rsm.getInt("cd_evento"), rsm.getInt("cd_inscricao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoPessoaDAO.getList: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	
	public static ResultSetMap getAllByEvento(int cdEvento) {
		return getAllByEvento(cdEvento, null);
	}

	public static ResultSetMap getAllByEvento(int cdEvento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM evt_evento_pessoa WHERE cd_evento=?");
			pstmt.setInt(1, cdEvento);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoPessoaDAO.getAllByEvento: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoPessoaDAO.getAllByEvento: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAllByParticipacao(int tpParticipacao, int cdEvento) {
		return getAllByParticipacao(tpParticipacao, cdEvento, null);
	}

	public static ResultSetMap getAllByParticipacao(int tpParticipacao, int cdEvento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM evt_evento_pessoa WHERE tp_participacao=? AND cd_evento=?");
			pstmt.setInt(1, tpParticipacao);
			pstmt.setInt(2, cdEvento);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoPessoaDAO.getAllByParticipacao: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoPessoaDAO.getAllByParticipacao: " + e);
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
		
		ResultSetMap rsm = Search.find("SELECT A.*, B.ID_PESSOA, B.NR_TELEFONE1, B.NR_CELULAR, B.NM_EMAIL, C.*, upper(nm_pessoa) AS NM_PESSOA FROM evt_evento_pessoa A, grl_pessoa B, grl_pessoa_fisica C WHERE A.cd_pessoa = B.cd_pessoa AND B.cd_pessoa = C.cd_pessoa ", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);			
		return rsm;
	}

}
