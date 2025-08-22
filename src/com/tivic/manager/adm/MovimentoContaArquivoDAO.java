package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class MovimentoContaArquivoDAO{

	public static int insert(MovimentoContaArquivo objeto) {
		return insert(objeto, null);
	}

	public static int insert(MovimentoContaArquivo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_movimento_conta_arquivo (cd_movimento_conta,"+
			                                  "cd_conta,"+
			                                  "cd_arquivo) VALUES (?, ?, ?)");
			if(objeto.getCdMovimentoConta()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdMovimentoConta());
			if(objeto.getCdConta()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdConta());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdArquivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaArquivoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaArquivoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MovimentoContaArquivo objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(MovimentoContaArquivo objeto, int cdMovimentoContaOld, int cdContaOld, int cdArquivoOld) {
		return update(objeto, cdMovimentoContaOld, cdContaOld, cdArquivoOld, null);
	}

	public static int update(MovimentoContaArquivo objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(MovimentoContaArquivo objeto, int cdMovimentoContaOld, int cdContaOld, int cdArquivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_movimento_conta_arquivo SET cd_movimento_conta=?,"+
												      		   "cd_conta=?,"+
												      		   "cd_arquivo=? WHERE cd_movimento_conta=? AND cd_conta=? AND cd_arquivo=?");
			pstmt.setInt(1,objeto.getCdMovimentoConta());
			pstmt.setInt(2,objeto.getCdConta());
			pstmt.setInt(3,objeto.getCdArquivo());
			pstmt.setInt(4, cdMovimentoContaOld!=0 ? cdMovimentoContaOld : objeto.getCdMovimentoConta());
			pstmt.setInt(5, cdContaOld!=0 ? cdContaOld : objeto.getCdConta());
			pstmt.setInt(6, cdArquivoOld!=0 ? cdArquivoOld : objeto.getCdArquivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaArquivoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaArquivoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdMovimentoConta, int cdConta, int cdArquivo) {
		return delete(cdMovimentoConta, cdConta, cdArquivo, null);
	}

	public static int delete(int cdMovimentoConta, int cdConta, int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_movimento_conta_arquivo WHERE cd_movimento_conta=? AND cd_conta=? AND cd_arquivo=?");
			pstmt.setInt(1, cdMovimentoConta);
			pstmt.setInt(2, cdConta);
			pstmt.setInt(3, cdArquivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaArquivoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaArquivoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MovimentoContaArquivo get(int cdMovimentoConta, int cdConta, int cdArquivo) {
		return get(cdMovimentoConta, cdConta, cdArquivo, null);
	}

	public static MovimentoContaArquivo get(int cdMovimentoConta, int cdConta, int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_movimento_conta_arquivo WHERE cd_movimento_conta=? AND cd_conta=? AND cd_arquivo=?");
			pstmt.setInt(1, cdMovimentoConta);
			pstmt.setInt(2, cdConta);
			pstmt.setInt(3, cdArquivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MovimentoContaArquivo(rs.getInt("cd_movimento_conta"),
						rs.getInt("cd_conta"),
						rs.getInt("cd_arquivo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaArquivoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaArquivoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_movimento_conta_arquivo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaArquivoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaArquivoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ArrayList<MovimentoContaArquivo> getList() {
		return getList(null);
	}

	public static ArrayList<MovimentoContaArquivo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<MovimentoContaArquivo> list = new ArrayList<MovimentoContaArquivo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				MovimentoContaArquivo obj = MovimentoContaArquivoDAO.get(rsm.getInt("cd_movimento_conta"), rsm.getInt("cd_conta"), rsm.getInt("cd_arquivo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaArquivoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM adm_movimento_conta_arquivo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
