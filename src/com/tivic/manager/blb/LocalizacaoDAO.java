package com.tivic.manager.blb;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class LocalizacaoDAO{

	public static int insert(Localizacao objeto) {
		return insert(objeto, null);
	}

	public static int insert(Localizacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("blb_localizacao", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdLocalizacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO blb_localizacao (cd_localizacao,"+
			                                  "nm_localizacao,"+
			                                  "nr_localizacao,"+
			                                  "nr_localizacao_final,"+
			                                  "cd_localizacao_superior,"+
			                                  "cd_instituicao,"+
			                                  "cd_dependencia) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmLocalizacao());
			pstmt.setString(3,objeto.getNrLocalizacao());
			pstmt.setInt(4,objeto.getNrLocalizacaoFinal());
			if(objeto.getCdLocalizacaoSuperior()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdLocalizacaoSuperior());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdInstituicao());
			if(objeto.getCdDependencia()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdDependencia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalizacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalizacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Localizacao objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Localizacao objeto, int cdLocalizacaoOld) {
		return update(objeto, cdLocalizacaoOld, null);
	}

	public static int update(Localizacao objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Localizacao objeto, int cdLocalizacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE blb_localizacao SET cd_localizacao=?,"+
												      		   "nm_localizacao=?,"+
												      		   "nr_localizacao=?,"+
												      		   "nr_localizacao_final=?,"+
												      		   "cd_localizacao_superior=?,"+
												      		   "cd_instituicao=?,"+
												      		   "cd_dependencia=? WHERE cd_localizacao=?");
			pstmt.setInt(1,objeto.getCdLocalizacao());
			pstmt.setString(2,objeto.getNmLocalizacao());
			pstmt.setString(3,objeto.getNrLocalizacao());
			pstmt.setInt(4,objeto.getNrLocalizacaoFinal());
			if(objeto.getCdLocalizacaoSuperior()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdLocalizacaoSuperior());
			if(objeto.getCdInstituicao()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdInstituicao());
			if(objeto.getCdDependencia()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdDependencia());
			pstmt.setInt(8, cdLocalizacaoOld!=0 ? cdLocalizacaoOld : objeto.getCdLocalizacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalizacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalizacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdLocalizacao) {
		return delete(cdLocalizacao, null);
	}

	public static int delete(int cdLocalizacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM blb_localizacao WHERE cd_localizacao=?");
			pstmt.setInt(1, cdLocalizacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalizacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalizacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Localizacao get(int cdLocalizacao) {
		return get(cdLocalizacao, null);
	}

	public static Localizacao get(int cdLocalizacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM blb_localizacao WHERE cd_localizacao=?");
			pstmt.setInt(1, cdLocalizacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Localizacao(rs.getInt("cd_localizacao"),
						rs.getString("nm_localizacao"),
						rs.getString("nr_localizacao"),
						rs.getInt("nr_localizacao_final"),
						rs.getInt("cd_localizacao_superior"),
						rs.getInt("cd_instituicao"),
						rs.getInt("cd_dependencia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalizacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalizacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM blb_localizacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! LocalizacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalizacaoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Localizacao> getList() {
		return getList(null);
	}

	public static ArrayList<Localizacao> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Localizacao> list = new ArrayList<Localizacao>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Localizacao obj = LocalizacaoDAO.get(rsm.getInt("cd_localizacao"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! LocalizacaoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM blb_localizacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
