package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

public class ContaFechamentoArquivoDAO{

	public static int insert(ContaFechamentoArquivo objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContaFechamentoArquivo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_conta");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_fechamento");
			keys[1].put("IS_KEY_NATIVE", "YES");
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_arquivo");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdArquivo()));
			int code = Conexao.getSequenceCode("adm_conta_fechamento_arquivo", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_conta_fechamento_arquivo (cd_conta,"+
			                                  "cd_fechamento,"+
			                                  "cd_arquivo) VALUES (?, ?, ?)");
			pstmt.setInt(1, objeto.getCdConta());
			pstmt.setInt(2, objeto.getCdFechamento());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdArquivo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoArquivoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoArquivoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContaFechamentoArquivo objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(ContaFechamentoArquivo objeto, int cdContaOld, int cdFechamentoOld, int cdArquivoOld) {
		return update(objeto, cdContaOld, cdFechamentoOld, cdArquivoOld, null);
	}

	public static int update(ContaFechamentoArquivo objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(ContaFechamentoArquivo objeto, int cdContaOld, int cdFechamentoOld, int cdArquivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_conta_fechamento_arquivo SET cd_conta=?,"+
												      		   "cd_fechamento=?,"+
												      		   "cd_arquivo=? WHERE cd_conta=? AND cd_fechamento=? AND cd_arquivo=?");
			pstmt.setInt(1,objeto.getCdConta());
			pstmt.setInt(2,objeto.getCdFechamento());
			pstmt.setInt(3,objeto.getCdArquivo());
			pstmt.setInt(4, cdContaOld!=0 ? cdContaOld : objeto.getCdConta());
			pstmt.setInt(5, cdFechamentoOld!=0 ? cdFechamentoOld : objeto.getCdFechamento());
			pstmt.setInt(6, cdArquivoOld!=0 ? cdArquivoOld : objeto.getCdArquivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoArquivoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoArquivoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdConta, int cdFechamento, int cdArquivo) {
		return delete(cdConta, cdFechamento, cdArquivo, null);
	}

	public static int delete(int cdConta, int cdFechamento, int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_conta_fechamento_arquivo WHERE cd_conta=? AND cd_fechamento=? AND cd_arquivo=?");
			pstmt.setInt(1, cdConta);
			pstmt.setInt(2, cdFechamento);
			pstmt.setInt(3, cdArquivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoArquivoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoArquivoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaFechamentoArquivo get(int cdConta, int cdFechamento, int cdArquivo) {
		return get(cdConta, cdFechamento, cdArquivo, null);
	}

	public static ContaFechamentoArquivo get(int cdConta, int cdFechamento, int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_fechamento_arquivo WHERE cd_conta=? AND cd_fechamento=? AND cd_arquivo=?");
			pstmt.setInt(1, cdConta);
			pstmt.setInt(2, cdFechamento);
			pstmt.setInt(3, cdArquivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaFechamentoArquivo(rs.getInt("cd_conta"),
						rs.getInt("cd_fechamento"),
						rs.getInt("cd_arquivo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoArquivoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoArquivoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_fechamento_arquivo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoArquivoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoArquivoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ContaFechamentoArquivo> getList() {
		return getList(null);
	}

	public static ArrayList<ContaFechamentoArquivo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ContaFechamentoArquivo> list = new ArrayList<ContaFechamentoArquivo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ContaFechamentoArquivo obj = ContaFechamentoArquivoDAO.get(rsm.getInt("cd_conta"), rsm.getInt("cd_fechamento"), rsm.getInt("cd_arquivo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaFechamentoArquivoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM adm_conta_fechamento_arquivo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
