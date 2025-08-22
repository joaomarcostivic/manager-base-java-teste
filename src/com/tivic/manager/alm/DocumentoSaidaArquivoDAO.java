package com.tivic.manager.alm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class DocumentoSaidaArquivoDAO{

	public static int insert(DocumentoSaidaArquivo objeto) {
		return insert(objeto, null);
	}

	public static int insert(DocumentoSaidaArquivo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_documento_saida_arquivo (cd_documento_saida,"+
			                                  "cd_arquivo," + 
			                                  "st_documento_saida_arquivo) VALUES (?, ?, ?)");
			if(objeto.getCdDocumentoSaida()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdDocumentoSaida());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdArquivo());
			pstmt.setInt(3, objeto.getStDocumentoSaidaArquivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaArquivoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaArquivoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(DocumentoSaidaArquivo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(DocumentoSaidaArquivo objeto, int cdDocumentoSaidaOld, int cdArquivoOld) {
		return update(objeto, cdDocumentoSaidaOld, cdArquivoOld, null);
	}

	public static int update(DocumentoSaidaArquivo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(DocumentoSaidaArquivo objeto, int cdDocumentoSaidaOld, int cdArquivoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_documento_saida_arquivo SET cd_documento_saida=?,"+
												      		   "cd_arquivo=?," + 
												      		   "st_documento_saida_arquivo=? WHERE cd_documento_saida=? AND cd_arquivo=?");
			pstmt.setInt(1,objeto.getCdDocumentoSaida());
			pstmt.setInt(2,objeto.getCdArquivo());
			pstmt.setInt(3, objeto.getStDocumentoSaidaArquivo());
			pstmt.setInt(4, cdDocumentoSaidaOld!=0 ? cdDocumentoSaidaOld : objeto.getCdDocumentoSaida());
			pstmt.setInt(5, cdArquivoOld!=0 ? cdArquivoOld : objeto.getCdArquivo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaArquivoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaArquivoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDocumentoSaida, int cdArquivo) {
		return delete(cdDocumentoSaida, cdArquivo, null);
	}

	public static int delete(int cdDocumentoSaida, int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_documento_saida_arquivo WHERE cd_documento_saida=? AND cd_arquivo=?");
			pstmt.setInt(1, cdDocumentoSaida);
			pstmt.setInt(2, cdArquivo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaArquivoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaArquivoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static DocumentoSaidaArquivo get(int cdDocumentoSaida, int cdArquivo) {
		return get(cdDocumentoSaida, cdArquivo, null);
	}

	public static DocumentoSaidaArquivo get(int cdDocumentoSaida, int cdArquivo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_documento_saida_arquivo WHERE cd_documento_saida=? AND cd_arquivo=?");
			pstmt.setInt(1, cdDocumentoSaida);
			pstmt.setInt(2, cdArquivo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DocumentoSaidaArquivo(rs.getInt("cd_documento_saida"),
						rs.getInt("cd_arquivo"),
						rs.getInt("st_documento_saida_arquivo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaArquivoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaArquivoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_documento_saida_arquivo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaArquivoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaArquivoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM alm_documento_saida_arquivo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
