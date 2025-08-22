package com.tivic.manager.evt;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class EventoDAO{

	public static int insert(Evento objeto) {
		return insert(objeto, null);
	}

	public static int insert(Evento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("evt_evento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdEvento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO evt_evento (cd_evento,"+
			                                  "nm_evento,"+
			                                  "ds_evento,"+
			                                  "qt_vagas,"+
			                                  "dt_evento,"+
			                                  "tp_evento,"+
			                                  "id_evento,"+
			                                  "cd_local,"+
			                                  "cd_facilitador,"+
			                                  "dt_inicial,"+
			                                  "dt_final,"+
			                                  "dt_vencimento_boleto,"+
			                                  "cd_conta,"+
			                                  "cd_conta_carteira,"+
			                                  "cd_arquivo,"+
			                                  "cd_evento_principal,"+
			                                  "vl_inscricao,"+
			                                  "nm_url_edital) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmEvento());
			pstmt.setString(3,objeto.getDsEvento());
			pstmt.setInt(4,objeto.getQtVagas());
			if(objeto.getDtEvento()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtEvento().getTimeInMillis()));
			pstmt.setInt(6,objeto.getTpEvento());
			pstmt.setString(7,objeto.getIdEvento());
			if(objeto.getCdLocal()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdLocal());
			if(objeto.getCdFacilitador()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdFacilitador());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			if(objeto.getDtVencimentoBoleto()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtVencimentoBoleto().getTimeInMillis()));
			if(objeto.getCdConta()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdConta());
			if(objeto.getCdContaCarteira()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdContaCarteira());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdArquivo());
			if(objeto.getCdEventoPrincipal()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdEventoPrincipal());
			pstmt.setFloat(17,objeto.getVlInscricao());
			pstmt.setString(18,objeto.getNmUrlEdital());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Evento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Evento objeto, int cdEventoOld) {
		return update(objeto, cdEventoOld, null);
	}

	public static int update(Evento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Evento objeto, int cdEventoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE evt_evento SET cd_evento=?,"+
												      		   "nm_evento=?,"+
												      		   "ds_evento=?,"+
												      		   "qt_vagas=?,"+
												      		   "dt_evento=?,"+
												      		   "tp_evento=?,"+
												      		   "id_evento=?,"+
												      		   "cd_local=?,"+
												      		   "cd_facilitador=?,"+
												      		   "dt_inicial=?,"+
												      		   "dt_final=?,"+
												      		   "dt_vencimento_boleto=?,"+
												      		   "cd_conta=?,"+
												      		   "cd_conta_carteira=?,"+
												      		   "cd_arquivo=?,"+
												      		   "cd_evento_principal=?,"+
												      		   "vl_inscricao=?,"+
												      		   "nm_url_edital=? WHERE cd_evento=?");
			pstmt.setInt(1,objeto.getCdEvento());
			pstmt.setString(2,objeto.getNmEvento());
			pstmt.setString(3,objeto.getDsEvento());
			pstmt.setInt(4,objeto.getQtVagas());
			if(objeto.getDtEvento()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtEvento().getTimeInMillis()));
			pstmt.setInt(6,objeto.getTpEvento());
			pstmt.setString(7,objeto.getIdEvento());
			if(objeto.getCdLocal()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdLocal());
			if(objeto.getCdFacilitador()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdFacilitador());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			if(objeto.getDtVencimentoBoleto()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtVencimentoBoleto().getTimeInMillis()));
			if(objeto.getCdConta()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdConta());
			if(objeto.getCdContaCarteira()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdContaCarteira());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdArquivo());
			if(objeto.getCdEventoPrincipal()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdEventoPrincipal());
			pstmt.setFloat(17,objeto.getVlInscricao());
			pstmt.setString(18,objeto.getNmUrlEdital());
			pstmt.setInt(19, cdEventoOld!=0 ? cdEventoOld : objeto.getCdEvento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEvento) {
		return delete(cdEvento, null);
	}

	public static int delete(int cdEvento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM evt_evento WHERE cd_evento=?");
			pstmt.setInt(1, cdEvento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Evento get(int cdEvento) {
		return get(cdEvento, null);
	}

	public static Evento get(int cdEvento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM evt_evento WHERE cd_evento=?");
			pstmt.setInt(1, cdEvento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Evento(rs.getInt("cd_evento"),
						rs.getString("nm_evento"),
						rs.getString("ds_evento"),
						rs.getInt("qt_vagas"),
						(rs.getTimestamp("dt_evento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_evento").getTime()),
						rs.getInt("tp_evento"),
						rs.getString("id_evento"),
						rs.getInt("cd_local"),
						rs.getInt("cd_facilitador"),
						(rs.getTimestamp("dt_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()),
						(rs.getTimestamp("dt_vencimento_boleto")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vencimento_boleto").getTime()),
						rs.getInt("cd_conta"),
						rs.getInt("cd_conta_carteira"),
						rs.getInt("cd_arquivo"),
						rs.getInt("cd_evento_principal"),
						rs.getFloat("vl_inscricao"),
						rs.getString("nm_url_edital"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM evt_evento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Evento> getList() {
		return getList(null);
	}

	public static ArrayList<Evento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Evento> list = new ArrayList<Evento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Evento obj = EventoDAO.get(rsm.getInt("cd_evento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM evt_evento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
