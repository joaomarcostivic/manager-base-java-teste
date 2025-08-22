package com.tivic.manager.mob.lotes.dao.publicacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.manager.mob.lotes.model.publicacao.LotePublicacao;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.dao.Util;

public class LotePublicacaoDAO{

	public static int insert(LotePublicacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(LotePublicacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_lote_publicacao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLotePublicacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_lote_publicacao (cd_lote_publicacao,"+
			                                  "cd_lote,"+
			                                  "st_lote,"+
			                                  "dt_publicacao,"+
			                                  "tp_publicacao) VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdLote()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdLote());
			pstmt.setInt(3,objeto.getStLote());
			if(objeto.getDtPublicacao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtPublicacao().getTimeInMillis()));
			pstmt.setInt(5,objeto.getTpPublicacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LotePublicacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LotePublicacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LotePublicacao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(LotePublicacao objeto, int cdLotePublicacaoOld) {
		return update(objeto, cdLotePublicacaoOld, null);
	}

	public static int update(LotePublicacao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(LotePublicacao objeto, int cdLotePublicacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_lote_publicacao SET cd_lote_publicacao=?,"+
												      		   "cd_lote=?,"+
												      		   "st_lote=?,"+
												      		   "dt_publicacao=?,"+
												      		   "tp_publicacao=? WHERE cd_lote_publicacao=?");
			pstmt.setInt(1,objeto.getCdLotePublicacao());
			if(objeto.getCdLote()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdLote());
			pstmt.setInt(3,objeto.getStLote());
			if(objeto.getDtPublicacao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtPublicacao().getTimeInMillis()));
			pstmt.setInt(5,objeto.getTpPublicacao());
			pstmt.setInt(6, cdLotePublicacaoOld!=0 ? cdLotePublicacaoOld : objeto.getCdLotePublicacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LotePublicacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LotePublicacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLotePublicacao) {
		return delete(cdLotePublicacao, null);
	}

	public static int delete(int cdLotePublicacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_lote_publicacao WHERE cd_lote_publicacao=?");
			pstmt.setInt(1, cdLotePublicacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LotePublicacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LotePublicacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LotePublicacao get(int cdLotePublicacao) {
		return get(cdLotePublicacao, null);
	}

	public static LotePublicacao get(int cdLotePublicacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_lote_publicacao WHERE cd_lote_publicacao=?");
			pstmt.setInt(1, cdLotePublicacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new LotePublicacao(rs.getInt("cd_lote_publicacao"),
						rs.getInt("cd_lote"),
						rs.getInt("st_lote"),
						(rs.getTimestamp("dt_publicacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_publicacao").getTime()),
						rs.getInt("tp_publicacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LotePublicacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LotePublicacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_lote_publicacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LotePublicacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LotePublicacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<LotePublicacao> getList() {
		return getList(null);
	}

	public static ArrayList<LotePublicacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<LotePublicacao> list = new ArrayList<LotePublicacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				LotePublicacao obj = LotePublicacaoDAO.get(rsm.getInt("cd_lote_publicacao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LotePublicacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_lote_publicacao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
