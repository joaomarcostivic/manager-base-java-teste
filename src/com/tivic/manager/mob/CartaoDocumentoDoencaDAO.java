package com.tivic.manager.mob;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class CartaoDocumentoDoencaDAO{

	public static int insert(CartaoDocumentoDoenca objeto) {
		return insert(objeto, null);
	}

	public static int insert(CartaoDocumentoDoenca objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("mob_cartao_documento_doenca", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCartaoDocumentoDoenca(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO mob_cartao_documento_doenca (cd_cartao_documento_doenca,"+
			                                  "cd_doenca,"+
			                                  "txt_descricao,"+
			                                  "cd_cartao_documento) VALUES (?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdDoenca()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDoenca());
			pstmt.setString(3,objeto.getTxtDescricao());
			if(objeto.getCdCartaoDocumento()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCartaoDocumento());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDoencaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDoencaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(CartaoDocumentoDoenca objeto) {
		return update(objeto, 0, null);
	}

	public static int update(CartaoDocumentoDoenca objeto, int cdCartaoDocumentoDoencaOld) {
		return update(objeto, cdCartaoDocumentoDoencaOld, null);
	}

	public static int update(CartaoDocumentoDoenca objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(CartaoDocumentoDoenca objeto, int cdCartaoDocumentoDoencaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE mob_cartao_documento_doenca SET cd_cartao_documento_doenca=?,"+
												      		   "cd_doenca=?,"+
												      		   "txt_descricao=?,"+
												      		   "cd_cartao_documento=? WHERE cd_cartao_documento_doenca=?");
			pstmt.setInt(1,objeto.getCdCartaoDocumentoDoenca());
			if(objeto.getCdDoenca()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDoenca());
			pstmt.setString(3,objeto.getTxtDescricao());
			if(objeto.getCdCartaoDocumento()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCartaoDocumento());
			pstmt.setInt(5, cdCartaoDocumentoDoencaOld!=0 ? cdCartaoDocumentoDoencaOld : objeto.getCdCartaoDocumentoDoenca());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDoencaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDoencaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdCartaoDocumentoDoenca) {
		return delete(cdCartaoDocumentoDoenca, null);
	}

	public static int delete(int cdCartaoDocumentoDoenca, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM mob_cartao_documento_doenca WHERE cd_cartao_documento_doenca=?");
			pstmt.setInt(1, cdCartaoDocumentoDoenca);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDoencaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDoencaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static CartaoDocumentoDoenca get(int cdCartaoDocumentoDoenca) {
		return get(cdCartaoDocumentoDoenca, null);
	}

	public static CartaoDocumentoDoenca get(int cdCartaoDocumentoDoenca, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_cartao_documento_doenca WHERE cd_cartao_documento_doenca=?");
			pstmt.setInt(1, cdCartaoDocumentoDoenca);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CartaoDocumentoDoenca(rs.getInt("cd_cartao_documento_doenca"),
						rs.getInt("cd_doenca"),
						rs.getString("txt_descricao"),
						rs.getInt("cd_cartao_documento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDoencaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDoencaDAO.get: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static CartaoDocumentoDoenca getDocumentoDoenca(int cdCartaoDocumento, int cdDoenca) {
		return getDocumentoDoenca(cdCartaoDocumento, cdDoenca, null);
	}

	public static CartaoDocumentoDoenca getDocumentoDoenca(int cdCartaoDocumento, int cdDoenca, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM mob_cartao_documento_doenca WHERE cd_cartao_documento=? and cd_doenca = ?");
			pstmt.setInt(1, cdCartaoDocumento);
			pstmt.setInt(2, cdDoenca);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new CartaoDocumentoDoenca(rs.getInt("cd_cartao_documento_doenca"),
						rs.getInt("cd_doenca"),
						rs.getString("txt_descricao"),
						rs.getInt("cd_cartao_documento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDoencaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDoencaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM mob_cartao_documento_doenca");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDoencaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDoencaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<CartaoDocumentoDoenca> getList() {
		return getList(null);
	}

	public static ArrayList<CartaoDocumentoDoenca> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<CartaoDocumentoDoenca> list = new ArrayList<CartaoDocumentoDoenca>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				CartaoDocumentoDoenca obj = CartaoDocumentoDoencaDAO.get(rsm.getInt("cd_cartao_documento_doenca"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! CartaoDocumentoDoencaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM mob_cartao_documento_doenca", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
