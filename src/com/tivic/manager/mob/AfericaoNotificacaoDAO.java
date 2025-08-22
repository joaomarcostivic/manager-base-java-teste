package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class AfericaoNotificacaoDAO{

	public static int insert(AfericaoNotificacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(AfericaoNotificacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_afericao_notificacao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAfericaoNotificacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_afericao_notificacao (cd_afericao_notificacao,"+
			                                  "st_afericao_notificacao,"+
			                                  "dt_afericao_notificacao,"+
			                                  "cd_infracao,"+
			                                  "cd_motivo,"+
			                                  "cd_controle,"+
			                                  "cd_ait,"+
			                                  "ds_cancelamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getStAfericaoNotificacao());
			if(objeto.getDtAfericaoNotificacao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtAfericaoNotificacao().getTimeInMillis()));
			pstmt.setInt(4,objeto.getCdInfracao());
			pstmt.setInt(5,objeto.getCdMotivo());
			pstmt.setInt(6,objeto.getCdControle());
			pstmt.setInt(7,objeto.getCdAit());
			pstmt.setString(8,objeto.getDsCancelamento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AfericaoNotificacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AfericaoNotificacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AfericaoNotificacao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(AfericaoNotificacao objeto, int cdAfericaoNotificacaoOld) {
		return update(objeto, cdAfericaoNotificacaoOld, null);
	}

	public static int update(AfericaoNotificacao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(AfericaoNotificacao objeto, int cdAfericaoNotificacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_afericao_notificacao SET cd_afericao_notificacao=?,"+
												      		   "st_afericao_notificacao=?,"+
												      		   "dt_afericao_notificacao=?,"+
												      		   "cd_infracao=?,"+
												      		   "cd_motivo=?,"+
												      		   "cd_controle=?,"+
												      		   "cd_ait=?,"+
												      		   "ds_cancelamento=? WHERE cd_afericao_notificacao=?");
			pstmt.setInt(1,objeto.getCdAfericaoNotificacao());
			pstmt.setInt(2,objeto.getStAfericaoNotificacao());
			if(objeto.getDtAfericaoNotificacao()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtAfericaoNotificacao().getTimeInMillis()));
			pstmt.setInt(4,objeto.getCdInfracao());
			pstmt.setInt(5,objeto.getCdMotivo());
			pstmt.setInt(6,objeto.getCdControle());
			pstmt.setInt(7,objeto.getCdAit());
			pstmt.setString(8,objeto.getDsCancelamento());
			pstmt.setInt(9, cdAfericaoNotificacaoOld!=0 ? cdAfericaoNotificacaoOld : objeto.getCdAfericaoNotificacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AfericaoNotificacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AfericaoNotificacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAfericaoNotificacao) {
		return delete(cdAfericaoNotificacao, null);
	}

	public static int delete(int cdAfericaoNotificacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_afericao_notificacao WHERE cd_afericao_notificacao=?");
			pstmt.setInt(1, cdAfericaoNotificacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AfericaoNotificacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AfericaoNotificacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AfericaoNotificacao get(int cdAfericaoNotificacao) {
		return get(cdAfericaoNotificacao, null);
	}

	public static AfericaoNotificacao get(int cdAfericaoNotificacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_afericao_notificacao WHERE cd_afericao_notificacao=?");
			pstmt.setInt(1, cdAfericaoNotificacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AfericaoNotificacao(rs.getInt("cd_afericao_notificacao"),
						rs.getInt("st_afericao_notificacao"),
						(rs.getTimestamp("dt_afericao_notificacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_afericao_notificacao").getTime()),
						rs.getInt("cd_infracao"),
						rs.getInt("cd_motivo"),
						rs.getInt("cd_controle"),
						rs.getInt("cd_ait"),
						rs.getString("ds_cancelamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AfericaoNotificacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AfericaoNotificacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_afericao_notificacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AfericaoNotificacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AfericaoNotificacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<AfericaoNotificacao> getList() {
		return getList(null);
	}

	public static ArrayList<AfericaoNotificacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<AfericaoNotificacao> list = new ArrayList<AfericaoNotificacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				AfericaoNotificacao obj = AfericaoNotificacaoDAO.get(rsm.getInt("cd_afericao_notificacao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AfericaoNotificacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_afericao_notificacao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}