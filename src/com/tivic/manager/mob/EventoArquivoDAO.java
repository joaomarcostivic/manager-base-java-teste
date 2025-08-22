package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class EventoArquivoDAO{

	public static int insert(EventoArquivo objeto) {
		return insert(objeto, null);
	}

	public static int insert(EventoArquivo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_evento_arquivo (cd_evento,"+
			                                  "cd_arquivo,"+
			                                  "tp_arquivo,"+
			                                  "dt_arquivo,"+
			                                  "id_arquivo,"+
			                                  "tp_evento_foto,"+
			                                  "tp_foto," +
			                                  "lg_impressao) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEvento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEvento());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdArquivo());
			pstmt.setInt(3,objeto.getTpArquivo());
			if(objeto.getDtArquivo()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtArquivo().getTimeInMillis()));
			pstmt.setString(5,objeto.getIdArquivo());
			pstmt.setInt(6,objeto.getTpEventoFoto());
			pstmt.setInt(7,objeto.getTpFoto());
			pstmt.setInt(8,objeto.getLgImpressao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoArquivoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoArquivoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(EventoArquivo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(EventoArquivo objeto, int cdEventoOld, int cdArquivoOld) {
		return update(objeto, cdEventoOld, cdArquivoOld, null);
	}

	public static int update(EventoArquivo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(EventoArquivo objeto, int cdEventoOld, int cdArquivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_evento_arquivo SET cd_evento=?,"+
												      		   "cd_arquivo=?,"+
												      		   "tp_arquivo=?,"+
												      		   "dt_arquivo=?,"+
												      		   "id_arquivo=?,"+
												      		   "tp_evento_foto=?,"+
												      		   "tp_foto=?,"+
												      		   "lg_impressao=? WHERE cd_evento=? AND cd_arquivo=?");
			pstmt.setInt(1,objeto.getCdEvento());
			pstmt.setInt(2,objeto.getCdArquivo());
			pstmt.setInt(3,objeto.getTpArquivo());
			if(objeto.getDtArquivo()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtArquivo().getTimeInMillis()));
			pstmt.setString(5,objeto.getIdArquivo());
			pstmt.setInt(6,objeto.getTpEventoFoto());
			pstmt.setInt(7,objeto.getTpFoto());
			pstmt.setInt(8,objeto.getLgImpressao());
			pstmt.setInt(9, cdEventoOld!=0 ? cdEventoOld : objeto.getCdEvento());
			pstmt.setInt(10, cdArquivoOld!=0 ? cdArquivoOld : objeto.getCdArquivo()); 
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoArquivoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoArquivoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEvento, int cdArquivo) {
		return delete(cdEvento, cdArquivo, null);
	}

	public static int delete(int cdEvento, int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_evento_arquivo WHERE cd_evento=? AND cd_arquivo=?");
			pstmt.setInt(1, cdEvento);
			pstmt.setInt(2, cdArquivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoArquivoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoArquivoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static EventoArquivo get(int cdEvento, int cdArquivo) {
		return get(cdEvento, cdArquivo, null);
	}

	public static EventoArquivo get(int cdEvento, int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_evento_arquivo WHERE cd_evento=? AND cd_arquivo=?");
			pstmt.setInt(1, cdEvento);
			pstmt.setInt(2, cdArquivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EventoArquivo(rs.getInt("cd_evento"),
						rs.getInt("cd_arquivo"),
						rs.getInt("tp_arquivo"),
						(rs.getTimestamp("dt_arquivo")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_arquivo").getTime()),
						rs.getString("id_arquivo"),
						rs.getInt("tp_evento_foto"),
						rs.getInt("tp_foto"),
						rs.getInt("lg_impressao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoArquivoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoArquivoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_evento_arquivo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! EventoArquivoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoArquivoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<EventoArquivo> getList() {
		return getList(null);
	}

	public static ArrayList<EventoArquivo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<EventoArquivo> list = new ArrayList<EventoArquivo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				EventoArquivo obj = EventoArquivoDAO.get(rsm.getInt("cd_evento"), rsm.getInt("cd_arquivo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EventoArquivoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_evento_arquivo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}