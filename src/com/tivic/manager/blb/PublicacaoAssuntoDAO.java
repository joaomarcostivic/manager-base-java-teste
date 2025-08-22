package com.tivic.manager.blb;

import java.sql.*;
import sol.dao.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class PublicacaoAssuntoDAO{

	public static int insert(PublicacaoAssunto objeto) {
		return insert(objeto, null);
	}

	public static int insert(PublicacaoAssunto objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO blb_publicacao_assunto (cd_assunto,"+
			                                  "cd_publicacao) VALUES (?, ?)");
			if(objeto.getCdAssunto()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAssunto());
			if(objeto.getCdPublicacao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPublicacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAssuntoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAssuntoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(PublicacaoAssunto objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(PublicacaoAssunto objeto, int cdAssuntoOld, int cdPublicacaoOld) {
		return update(objeto, cdAssuntoOld, cdPublicacaoOld, null);
	}

	public static int update(PublicacaoAssunto objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(PublicacaoAssunto objeto, int cdAssuntoOld, int cdPublicacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE blb_publicacao_assunto SET cd_assunto=?,"+
												      		   "cd_publicacao=? WHERE cd_assunto=? AND cd_publicacao=?");
			pstmt.setInt(1,objeto.getCdAssunto());
			pstmt.setInt(2,objeto.getCdPublicacao());
			pstmt.setInt(3, cdAssuntoOld!=0 ? cdAssuntoOld : objeto.getCdAssunto());
			pstmt.setInt(4, cdPublicacaoOld!=0 ? cdPublicacaoOld : objeto.getCdPublicacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAssuntoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAssuntoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAssunto, int cdPublicacao) {
		return delete(cdAssunto, cdPublicacao, null);
	}

	public static int delete(int cdAssunto, int cdPublicacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM blb_publicacao_assunto WHERE cd_assunto=? AND cd_publicacao=?");
			pstmt.setInt(1, cdAssunto);
			pstmt.setInt(2, cdPublicacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAssuntoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAssuntoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static PublicacaoAssunto get(int cdAssunto, int cdPublicacao) {
		return get(cdAssunto, cdPublicacao, null);
	}

	public static PublicacaoAssunto get(int cdAssunto, int cdPublicacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM blb_publicacao_assunto WHERE cd_assunto=? AND cd_publicacao=?");
			pstmt.setInt(1, cdAssunto);
			pstmt.setInt(2, cdPublicacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new PublicacaoAssunto(rs.getInt("cd_assunto"),
						rs.getInt("cd_publicacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAssuntoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAssuntoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM blb_publicacao_assunto");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAssuntoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAssuntoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<PublicacaoAssunto> getList() {
		return getList(null);
	}

	public static ArrayList<PublicacaoAssunto> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<PublicacaoAssunto> list = new ArrayList<PublicacaoAssunto>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				PublicacaoAssunto obj = PublicacaoAssuntoDAO.get(rsm.getInt("cd_assunto"), rsm.getInt("cd_publicacao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! PublicacaoAssuntoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM blb_publicacao_assunto", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
