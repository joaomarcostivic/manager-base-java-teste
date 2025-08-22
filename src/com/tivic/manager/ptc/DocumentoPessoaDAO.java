package com.tivic.manager.ptc;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class DocumentoPessoaDAO{

	public static int insert(DocumentoPessoa objeto) {
		return insert(objeto, null);
	}

	public static int insert(DocumentoPessoa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_documento_pessoa (cd_documento,"+
			                                  "cd_pessoa,nm_qualificacao) VALUES (?, ?, ?)");
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdDocumento());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setString(3, objeto.getNmQualificacao());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(DocumentoPessoa objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(DocumentoPessoa objeto, int cdDocumentoOld, int cdPessoaOld) {
		return update(objeto, cdDocumentoOld, cdPessoaOld, null);
	}

	public static int update(DocumentoPessoa objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(DocumentoPessoa objeto, int cdDocumentoOld, int cdPessoaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_documento_pessoa SET cd_documento=?,"+
												      		   "cd_pessoa=?, nm_qualificacao=? WHERE cd_documento=? AND cd_pessoa=?");
			pstmt.setInt(1,objeto.getCdDocumento());
			pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setString(3, objeto.getNmQualificacao());
			pstmt.setInt(4, cdDocumentoOld!=0 ? cdDocumentoOld : objeto.getCdDocumento());
			pstmt.setInt(5, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDocumento, int cdPessoa) {
		return delete(cdDocumento, cdPessoa, null);
	}

	public static int delete(int cdDocumento, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_documento_pessoa WHERE cd_documento=? AND cd_pessoa=?");
			pstmt.setInt(1, cdDocumento);
			pstmt.setInt(2, cdPessoa);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static DocumentoPessoa get(int cdDocumento, int cdPessoa) {
		return get(cdDocumento, cdPessoa, null);
	}

	public static DocumentoPessoa get(int cdDocumento, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_documento_pessoa WHERE cd_documento=? AND cd_pessoa=?");
			pstmt.setInt(1, cdDocumento);
			pstmt.setInt(2, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DocumentoPessoa(rs.getInt("cd_documento"), rs.getInt("cd_pessoa"), rs.getString("nm_qualificacao"));
			}
			else
				return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_documento_pessoa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		return Search.find("SELECT * FROM ptc_documento_pessoa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
