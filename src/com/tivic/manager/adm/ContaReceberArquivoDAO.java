package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ContaReceberArquivoDAO{

	public static int insert(ContaReceberArquivo objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContaReceberArquivo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_conta_receber_arquivo (cd_conta_receber,"+
			                                  "cd_arquivo,"+
			                                  "cd_nivel_cobranca,"+
			                                  "cd_cobranca) VALUES (?, ?, ?, ?)");
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdContaReceber());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdArquivo());
			if(objeto.getCdNivelCobranca()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdNivelCobranca());
			if(objeto.getCdCobranca()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCobranca());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberArquivoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberArquivoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContaReceberArquivo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ContaReceberArquivo objeto, int cdContaReceberOld, int cdArquivoOld) {
		return update(objeto, cdContaReceberOld, cdArquivoOld, null);
	}

	public static int update(ContaReceberArquivo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ContaReceberArquivo objeto, int cdContaReceberOld, int cdArquivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_conta_receber_arquivo SET cd_conta_receber=?,"+
												      		   "cd_arquivo=?,"+
												      		   "cd_nivel_cobranca=?,"+
												      		   "cd_cobranca=? WHERE cd_conta_receber=? AND cd_arquivo=?");
			pstmt.setInt(1,objeto.getCdContaReceber());
			pstmt.setInt(2,objeto.getCdArquivo());
			if(objeto.getCdNivelCobranca()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdNivelCobranca());
			if(objeto.getCdCobranca()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCobranca());
			pstmt.setInt(5, cdContaReceberOld!=0 ? cdContaReceberOld : objeto.getCdContaReceber());
			pstmt.setInt(6, cdArquivoOld!=0 ? cdArquivoOld : objeto.getCdArquivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberArquivoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberArquivoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContaReceber, int cdArquivo) {
		return delete(cdContaReceber, cdArquivo, null);
	}

	public static int delete(int cdContaReceber, int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_conta_receber_arquivo WHERE cd_conta_receber=? AND cd_arquivo=?");
			pstmt.setInt(1, cdContaReceber);
			pstmt.setInt(2, cdArquivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberArquivoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberArquivoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaReceberArquivo get(int cdContaReceber, int cdArquivo) {
		return get(cdContaReceber, cdArquivo, null);
	}

	public static ContaReceberArquivo get(int cdContaReceber, int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_receber_arquivo WHERE cd_conta_receber=? AND cd_arquivo=?");
			pstmt.setInt(1, cdContaReceber);
			pstmt.setInt(2, cdArquivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaReceberArquivo(rs.getInt("cd_conta_receber"),
						rs.getInt("cd_arquivo"),
						rs.getInt("cd_nivel_cobranca"),
						rs.getInt("cd_cobranca"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberArquivoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberArquivoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_receber_arquivo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberArquivoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberArquivoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ContaReceberArquivo> getList() {
		return getList(null);
	}

	public static ArrayList<ContaReceberArquivo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ContaReceberArquivo> list = new ArrayList<ContaReceberArquivo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ContaReceberArquivo obj = ContaReceberArquivoDAO.get(rsm.getInt("cd_conta_receber"), rsm.getInt("cd_arquivo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberArquivoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM adm_conta_receber_arquivo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
