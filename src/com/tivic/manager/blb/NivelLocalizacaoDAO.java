package com.tivic.manager.blb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class NivelLocalizacaoDAO{

	public static int insert(NivelLocalizacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(NivelLocalizacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("blb_nivel_localizacao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdNivelLocalizacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO blb_nivel_localizacao (cd_nivel_localizacao,"+
			                                  "nm_nivel_localizacao,"+
			                                  "id_nivel_localizacao) VALUES (?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmNivelLocalizacao());
			pstmt.setString(3,objeto.getIdNivelLocalizacao());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NivelLocalizacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NivelLocalizacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(NivelLocalizacao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(NivelLocalizacao objeto, int cdNivelLocalizacaoOld) {
		return update(objeto, cdNivelLocalizacaoOld, null);
	}

	public static int update(NivelLocalizacao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(NivelLocalizacao objeto, int cdNivelLocalizacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE blb_nivel_localizacao SET cd_nivel_localizacao=?,"+
												      		   "nm_nivel_localizacao=?,"+
												      		   "id_nivel_localizacao=? WHERE cd_nivel_localizacao=?");
			pstmt.setInt(1,objeto.getCdNivelLocalizacao());
			pstmt.setString(2,objeto.getNmNivelLocalizacao());
			pstmt.setString(3,objeto.getIdNivelLocalizacao());
			pstmt.setInt(4, cdNivelLocalizacaoOld!=0 ? cdNivelLocalizacaoOld : objeto.getCdNivelLocalizacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NivelLocalizacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NivelLocalizacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdNivelLocalizacao) {
		return delete(cdNivelLocalizacao, null);
	}

	public static int delete(int cdNivelLocalizacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM blb_nivel_localizacao WHERE cd_nivel_localizacao=?");
			pstmt.setInt(1, cdNivelLocalizacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NivelLocalizacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! NivelLocalizacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static NivelLocalizacao get(int cdNivelLocalizacao) {
		return get(cdNivelLocalizacao, null);
	}

	public static NivelLocalizacao get(int cdNivelLocalizacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM blb_nivel_localizacao WHERE cd_nivel_localizacao=?");
			pstmt.setInt(1, cdNivelLocalizacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new NivelLocalizacao(rs.getInt("cd_nivel_localizacao"),
						rs.getString("nm_nivel_localizacao"),
						rs.getString("id_nivel_localizacao"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NivelLocalizacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NivelLocalizacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM blb_nivel_localizacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NivelLocalizacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NivelLocalizacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<NivelLocalizacao> getList() {
		return getList(null);
	}

	public static ArrayList<NivelLocalizacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<NivelLocalizacao> list = new ArrayList<NivelLocalizacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				NivelLocalizacao obj = NivelLocalizacaoDAO.get(rsm.getInt("cd_nivel_localizacao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NivelLocalizacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM blb_nivel_localizacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
