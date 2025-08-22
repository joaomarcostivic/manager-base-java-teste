package com.tivic.manager.ptc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.List;

public class DocumentoArquivoDAO{

	public static int insert(DocumentoArquivo objeto) {
		return insert(objeto, null);
	}

	public static int insert(DocumentoArquivo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_documento_arquivo (cd_arquivo,"+
			                                  "cd_documento) VALUES (?, ?)");
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdArquivo());
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDocumento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoArquivoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoArquivoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(DocumentoArquivo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(DocumentoArquivo objeto, int cdArquivoOld, int cdDocumentoOld) {
		return update(objeto, cdArquivoOld, cdDocumentoOld, null);
	}

	public static int update(DocumentoArquivo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(DocumentoArquivo objeto, int cdArquivoOld, int cdDocumentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_documento_arquivo SET cd_arquivo=?,"+
												      		   "cd_documento=? WHERE cd_arquivo=? AND cd_documento=?");
			pstmt.setInt(1,objeto.getCdArquivo());
			pstmt.setInt(2,objeto.getCdDocumento());
			pstmt.setInt(3, cdArquivoOld!=0 ? cdArquivoOld : objeto.getCdArquivo());
			pstmt.setInt(4, cdDocumentoOld!=0 ? cdDocumentoOld : objeto.getCdDocumento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoArquivoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoArquivoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdArquivo, int cdDocumento) {
		return delete(cdArquivo, cdDocumento, null);
	}

	public static int delete(int cdArquivo, int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_documento_arquivo WHERE cd_arquivo=? AND cd_documento=?");
			pstmt.setInt(1, cdArquivo);
			pstmt.setInt(2, cdDocumento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoArquivoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoArquivoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static DocumentoArquivo get(int cdArquivo, int cdDocumento) {
		return get(cdArquivo, cdDocumento, null);
	}

	public static DocumentoArquivo get(int cdArquivo, int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_documento_arquivo WHERE cd_arquivo=? AND cd_documento=?");
			pstmt.setInt(1, cdArquivo);
			pstmt.setInt(2, cdDocumento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DocumentoArquivo(rs.getInt("cd_arquivo"),
						rs.getInt("cd_documento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoArquivoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoArquivoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_documento_arquivo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoArquivoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoArquivoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<DocumentoArquivo> getList() {
		return getList(null);
	}

	public static ArrayList<DocumentoArquivo> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<DocumentoArquivo> list = new ArrayList<DocumentoArquivo>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				DocumentoArquivo obj = DocumentoArquivoDAO.get(rsm.getInt("cd_arquivo"), rsm.getInt("cd_documento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoArquivoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ptc_documento_arquivo", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static List<DocumentoArquivo> getArquivosDocumento(int cdDocumento) {
		return getArquivosDocumento( cdDocumento);
	}

}
