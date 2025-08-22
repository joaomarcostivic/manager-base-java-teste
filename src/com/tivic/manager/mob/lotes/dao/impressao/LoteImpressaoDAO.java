package com.tivic.manager.mob.lotes.dao.impressao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class LoteImpressaoDAO{

	public static int insert(LoteImpressao objeto) {
		return insert(objeto, null);
	}

	public static int insert(LoteImpressao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_lote_impressao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLoteImpressao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_lote_impressao (cd_lote_impressao,"+
			                                  "cd_lote,"+
			                                  "st_lote,"+
			                                  "tp_impressao) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdLote()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdLote());
			pstmt.setInt(3,objeto.getStLote());
			pstmt.setInt(4,objeto.getTpImpressao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LoteImpressao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(LoteImpressao objeto, int cdLoteImpressaoOld) {
		return update(objeto, cdLoteImpressaoOld, null);
	}

	public static int update(LoteImpressao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(LoteImpressao objeto, int cdLoteImpressaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_lote_impressao SET cd_lote_impressao=?,"+
												      		   "cd_lote=?,"+
												      		   "st_lote=?,"+
												      		   "tp_impressao=? WHERE cd_lote_impressao=?");
			pstmt.setInt(1,objeto.getCdLoteImpressao());
			if(objeto.getCdLote()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdLote());
			pstmt.setInt(3,objeto.getStLote());
			pstmt.setInt(4,objeto.getTpImpressao());
			pstmt.setInt(5, cdLoteImpressaoOld!=0 ? cdLoteImpressaoOld : objeto.getCdLoteImpressao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLoteImpressao) {
		return delete(cdLoteImpressao, null);
	}

	public static int delete(int cdLoteImpressao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_lote_impressao WHERE cd_lote_impressao=?");
			pstmt.setInt(1, cdLoteImpressao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LoteImpressao get(int cdLoteImpressao) {
		return get(cdLoteImpressao, null);
	}

	public static LoteImpressao get(int cdLoteImpressao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_lote_impressao WHERE cd_lote_impressao=?");
			pstmt.setInt(1, cdLoteImpressao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new LoteImpressao(
						rs.getInt("cd_lote_impressao"),
						rs.getInt("cd_lote"),
						rs.getInt("st_lote"),
						rs.getInt("tp_impressao")
					);
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_lote_impressao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<LoteImpressao> getList() {
		return getList(null);
	}

	public static ArrayList<LoteImpressao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<LoteImpressao> list = new ArrayList<LoteImpressao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				LoteImpressao obj = LoteImpressaoDAO.get(rsm.getInt("cd_lote_impressao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteImpressaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_lote_impressao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
