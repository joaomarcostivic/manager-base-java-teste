package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class ContaPagarArquivoDAO{

	public static int insert(ContaPagarArquivo objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContaPagarArquivo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_conta_pagar_arquivo (cd_conta_pagar,"+
			                                  "cd_arquivo) VALUES (?, ?)");
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdContaPagar());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdArquivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarArquivoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarArquivoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContaPagarArquivo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ContaPagarArquivo objeto, int cdContaPagarOld, int cdArquivoOld) {
		return update(objeto, cdContaPagarOld, cdArquivoOld, null);
	}

	public static int update(ContaPagarArquivo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ContaPagarArquivo objeto, int cdContaPagarOld, int cdArquivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_conta_pagar_arquivo SET cd_conta_pagar=?,"+
												      		   "cd_arquivo=? WHERE cd_conta_pagar=? AND cd_arquivo=?");
			pstmt.setInt(1,objeto.getCdContaPagar());
			pstmt.setInt(2,objeto.getCdArquivo());
			pstmt.setInt(3, cdContaPagarOld!=0 ? cdContaPagarOld : objeto.getCdContaPagar());
			pstmt.setInt(4, cdArquivoOld!=0 ? cdArquivoOld : objeto.getCdArquivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarArquivoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarArquivoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContaPagar, int cdArquivo) {
		return delete(cdContaPagar, cdArquivo, null);
	}

	public static int delete(int cdContaPagar, int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_conta_pagar_arquivo WHERE cd_conta_pagar=? AND cd_arquivo=?");
			pstmt.setInt(1, cdContaPagar);
			pstmt.setInt(2, cdArquivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarArquivoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarArquivoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaPagarArquivo get(int cdContaPagar, int cdArquivo) {
		return get(cdContaPagar, cdArquivo, null);
	}

	public static ContaPagarArquivo get(int cdContaPagar, int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_pagar_arquivo WHERE cd_conta_pagar=? AND cd_arquivo=?");
			pstmt.setInt(1, cdContaPagar);
			pstmt.setInt(2, cdArquivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaPagarArquivo(rs.getInt("cd_conta_pagar"),
						rs.getInt("cd_arquivo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarArquivoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarArquivoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_pagar_arquivo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarArquivoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarArquivoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ContaPagarArquivo> getList() {
		return getList(null);
	}

	public static ArrayList<ContaPagarArquivo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ContaPagarArquivo> list = new ArrayList<ContaPagarArquivo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ContaPagarArquivo obj = ContaPagarArquivoDAO.get(rsm.getInt("cd_conta_pagar"), rsm.getInt("cd_arquivo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarArquivoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM adm_conta_pagar_arquivo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
