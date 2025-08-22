package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CartaoDocumentoDAO{

	public static int insert(CartaoDocumento objeto) {
		return insert(objeto, null);
	}

	public static int insert(CartaoDocumento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_cartao_documento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCartaoDocumento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_cartao_documento (cd_cartao_documento,"+
			                                  "cd_documento,"+
			                                  "cd_cartao) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getCdDocumento());
			pstmt.setInt(3,objeto.getCdCartao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CartaoDocumento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(CartaoDocumento objeto, int cdCartaoDocumentoOld) {
		return update(objeto, cdCartaoDocumentoOld, null);
	}

	public static int update(CartaoDocumento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(CartaoDocumento objeto, int cdCartaoDocumentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_cartao_documento SET cd_cartao_documento=?,"+
												      		   "cd_documento=?,"+
												      		   "cd_cartao=? WHERE cd_cartao_documento=?");
			pstmt.setInt(1,objeto.getCdCartaoDocumento());
			pstmt.setInt(2,objeto.getCdDocumento());
			pstmt.setInt(3,objeto.getCdCartao());
			pstmt.setInt(4, cdCartaoDocumentoOld!=0 ? cdCartaoDocumentoOld : objeto.getCdCartaoDocumento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCartaoDocumento) {
		return delete(cdCartaoDocumento, null);
	}

	public static int delete(int cdCartaoDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_cartao_documento WHERE cd_cartao_documento=?");
			pstmt.setInt(1, cdCartaoDocumento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CartaoDocumento get(int cdCartaoDocumento) {
		return get(cdCartaoDocumento, null);
	}

	public static CartaoDocumento get(int cdCartaoDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_cartao_documento WHERE cd_cartao_documento=?");
			pstmt.setInt(1, cdCartaoDocumento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CartaoDocumento(rs.getInt("cd_cartao_documento"),
						rs.getInt("cd_documento"),
						rs.getInt("cd_cartao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static CartaoDocumento get(int cdCartao, int cdDocumento) {
		return get(cdCartao, cdDocumento, null);
	}

	public static CartaoDocumento get(int cdCartao, int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_cartao_documento WHERE cd_cartao=? and cd_documento=?");
			pstmt.setInt(1, cdCartao);
			pstmt.setInt(2, cdDocumento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CartaoDocumento(rs.getInt("cd_cartao_documento"),
						rs.getInt("cd_documento"),
						rs.getInt("cd_cartao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_cartao_documento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CartaoDocumento> getList() {
		return getList(null);
	}

	public static ArrayList<CartaoDocumento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CartaoDocumento> list = new ArrayList<CartaoDocumento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CartaoDocumento obj = CartaoDocumentoDAO.get(rsm.getInt("cd_cartao_documento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_cartao_documento", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
