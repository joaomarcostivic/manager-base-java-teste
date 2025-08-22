package com.tivic.manager.mob.lotes.dao.dividaativa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.tivic.manager.mob.lotes.model.dividaativa.LoteDividaAtiva;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class LoteDividaAtivaDAO{

	public static int insert(LoteDividaAtiva objeto) {
		return insert(objeto, null);
	}

	public static int insert(LoteDividaAtiva objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_lote_divida_ativa", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLoteDividaAtiva(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_lote_divida_ativa (cd_lote_divida_ativa,"+
			                                  "cd_lote,"+
			                                  "st_lote,"+
			                                  "cd_arquivo_retorno) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getCdLote());
			pstmt.setInt(3,objeto.getStLote());
			if(objeto.getCdArquivoRetorno()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdArquivoRetorno());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteDividaAtivaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDividaAtivaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(LoteDividaAtiva objeto) {
		return update(objeto, 0, null);
	}

	public static int update(LoteDividaAtiva objeto, int cdLoteDividaAtivaOld) {
		return update(objeto, cdLoteDividaAtivaOld, null);
	}

	public static int update(LoteDividaAtiva objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(LoteDividaAtiva objeto, int cdLoteDividaAtivaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_lote_divida_ativa SET cd_lote_divida_ativa=?,"+
												      		   "cd_lote=?,"+
												      		   "st_lote=?,"+
												      		   "cd_arquivo_retorno=? WHERE cd_lote_divida_ativa=?");
			pstmt.setInt(1, objeto.getCdLoteDividaAtiva());
			pstmt.setInt(2,objeto.getCdLote());
			pstmt.setInt(3,objeto.getStLote());
			if(objeto.getCdArquivoRetorno()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdArquivoRetorno());
			pstmt.setInt(5, cdLoteDividaAtivaOld!=0 ? cdLoteDividaAtivaOld : objeto.getCdLoteDividaAtiva());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteDividaAtivaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDividaAtivaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLoteDividaAtiva) {
		return delete(cdLoteDividaAtiva, null);
	}

	public static int delete(int cdLoteDividaAtiva, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_lote_divida_ativa WHERE cd_lote_divida_ativa=?");
			pstmt.setInt(1, cdLoteDividaAtiva);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteDividaAtivaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDividaAtivaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static LoteDividaAtiva get(int cdLoteDividaAtiva) {
		return get(cdLoteDividaAtiva, null);
	}

	public static LoteDividaAtiva get(int cdLoteDividaAtiva, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_lote_divida_ativa WHERE cd_lote_divida_ativa=?");
			pstmt.setInt(1, cdLoteDividaAtiva);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new LoteDividaAtiva(rs.getInt("cd_lote_divida_ativa"),
						rs.getInt("cd_lote"),
						rs.getInt("st_lote"),
						rs.getInt("cd_arquivo_retorno"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteDividaAtivaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDividaAtivaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_lote_divida_ativa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LoteDividaAtivaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDividaAtivaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<LoteDividaAtiva> getList() {
		return getList(null);
	}

	public static ArrayList<LoteDividaAtiva> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<LoteDividaAtiva> list = new ArrayList<LoteDividaAtiva>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				LoteDividaAtiva obj = LoteDividaAtivaDAO.get(rsm.getInt("cd_lote_divida_ativa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LoteDividaAtivaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_lote_divida_ativa", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
