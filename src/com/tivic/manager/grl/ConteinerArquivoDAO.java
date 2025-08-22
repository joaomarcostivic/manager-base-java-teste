package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ConteinerArquivoDAO{

	public static int insert(ConteinerArquivo objeto) {
		return insert(objeto, null);
	}

	public static int insert(ConteinerArquivo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_conteiner_arquivo (cd_conteiner,"+
			                                  "cd_arquivo) VALUES (?, ?)");
			if(objeto.getCdConteiner()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdConteiner());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdArquivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConteinerArquivoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConteinerArquivoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ConteinerArquivo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ConteinerArquivo objeto, int cdConteinerOld, int cdArquivoOld) {
		return update(objeto, cdConteinerOld, cdArquivoOld, null);
	}

	public static int update(ConteinerArquivo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ConteinerArquivo objeto, int cdConteinerOld, int cdArquivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_conteiner_arquivo SET cd_conteiner=?,"+
												      		   "cd_arquivo=? WHERE cd_conteiner=? AND cd_arquivo=?");
			pstmt.setInt(1,objeto.getCdConteiner());
			pstmt.setInt(2,objeto.getCdArquivo());
			pstmt.setInt(3, cdConteinerOld!=0 ? cdConteinerOld : objeto.getCdConteiner());
			pstmt.setInt(4, cdArquivoOld!=0 ? cdArquivoOld : objeto.getCdArquivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConteinerArquivoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConteinerArquivoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConteiner, int cdArquivo) {
		return delete(cdConteiner, cdArquivo, null);
	}

	public static int delete(int cdConteiner, int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_conteiner_arquivo WHERE cd_conteiner=? AND cd_arquivo=?");
			pstmt.setInt(1, cdConteiner);
			pstmt.setInt(2, cdArquivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConteinerArquivoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ConteinerArquivoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ConteinerArquivo get(int cdConteiner, int cdArquivo) {
		return get(cdConteiner, cdArquivo, null);
	}

	public static ConteinerArquivo get(int cdConteiner, int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_conteiner_arquivo WHERE cd_conteiner=? AND cd_arquivo=?");
			pstmt.setInt(1, cdConteiner);
			pstmt.setInt(2, cdArquivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ConteinerArquivo(rs.getInt("cd_conteiner"),
						rs.getInt("cd_arquivo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConteinerArquivoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConteinerArquivoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_conteiner_arquivo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ConteinerArquivoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ConteinerArquivoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_conteiner_arquivo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
