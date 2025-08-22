package com.tivic.manager.blb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class PublicacaoAutorDAO{

	public static int insert(PublicacaoAutor objeto) {
		return insert(objeto, null);
	}

	public static int insert(PublicacaoAutor objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO blb_publicacao_autor (cd_autor,"+
			                                  "cd_publicacao) VALUES (?, ?)");
			if(objeto.getCdAutor()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAutor());
			if(objeto.getCdPublicacao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPublicacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAutorDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAutorDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PublicacaoAutor objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(PublicacaoAutor objeto, int cdAutorOld, int cdPublicacaoOld) {
		return update(objeto, cdAutorOld, cdPublicacaoOld, null);
	}

	public static int update(PublicacaoAutor objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(PublicacaoAutor objeto, int cdAutorOld, int cdPublicacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE blb_publicacao_autor SET cd_autor=?,"+
												      		   "cd_publicacao=? WHERE cd_autor=? AND cd_publicacao=?");
			pstmt.setInt(1,objeto.getCdAutor());
			pstmt.setInt(2,objeto.getCdPublicacao());
			pstmt.setInt(3, cdAutorOld!=0 ? cdAutorOld : objeto.getCdAutor());
			pstmt.setInt(4, cdPublicacaoOld!=0 ? cdPublicacaoOld : objeto.getCdPublicacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAutorDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAutorDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAutor, int cdPublicacao) {
		return delete(cdAutor, cdPublicacao, null);
	}

	public static int delete(int cdAutor, int cdPublicacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM blb_publicacao_autor WHERE cd_autor=? AND cd_publicacao=?");
			pstmt.setInt(1, cdAutor);
			pstmt.setInt(2, cdPublicacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAutorDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAutorDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PublicacaoAutor get(int cdAutor, int cdPublicacao) {
		return get(cdAutor, cdPublicacao, null);
	}

	public static PublicacaoAutor get(int cdAutor, int cdPublicacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM blb_publicacao_autor WHERE cd_autor=? AND cd_publicacao=?");
			pstmt.setInt(1, cdAutor);
			pstmt.setInt(2, cdPublicacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PublicacaoAutor(rs.getInt("cd_autor"),
						rs.getInt("cd_publicacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAutorDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAutorDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM blb_publicacao_autor");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAutorDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAutorDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PublicacaoAutor> getList() {
		return getList(null);
	}

	public static ArrayList<PublicacaoAutor> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PublicacaoAutor> list = new ArrayList<PublicacaoAutor>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PublicacaoAutor obj = PublicacaoAutorDAO.get(rsm.getInt("cd_autor"), rsm.getInt("cd_publicacao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAutorDAO.getList: " + e);
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
		return Search.find("SELECT * FROM blb_publicacao_autor", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
