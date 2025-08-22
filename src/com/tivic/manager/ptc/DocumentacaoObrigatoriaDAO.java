package com.tivic.manager.ptc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class DocumentacaoObrigatoriaDAO{

	public static int insert(DocumentacaoObrigatoria objeto) {
		return insert(objeto, null);
	}

	public static int insert(DocumentacaoObrigatoria objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PTC_DOCUMENTACAO_OBRIGATORIA (CD_TIPO_DOCUMENTO,"+
			                                  "CD_TIPO_DOCUMENTACAO) VALUES (?, ?)");
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTipoDocumento());
			if(objeto.getCdTipoDocumentacao()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoDocumentacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentacaoObrigatoriaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentacaoObrigatoriaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(DocumentacaoObrigatoria objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(DocumentacaoObrigatoria objeto, int cdTipoDocumentacaoOld, int cdTipoDocumentoOld) {
		return update(objeto, cdTipoDocumentacaoOld, cdTipoDocumentoOld, null);
	}

	public static int update(DocumentacaoObrigatoria objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(DocumentacaoObrigatoria objeto, int cdTipoDocumentacaoOld, int cdTipoDocumentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PTC_DOCUMENTACAO_OBRIGATORIA SET CD_TIPO_DOCUMENTO=?,"+
												      		   "CD_TIPO_DOCUMENTACAO=? WHERE CD_TIPO_DOCUMENTACAO=? AND CD_TIPO_DOCUMENTO=?");
			pstmt.setInt(1,objeto.getCdTipoDocumento());
			pstmt.setInt(2,objeto.getCdTipoDocumentacao());
			pstmt.setInt(3, cdTipoDocumentacaoOld!=0 ? cdTipoDocumentacaoOld : objeto.getCdTipoDocumentacao());
			pstmt.setInt(4, cdTipoDocumentoOld!=0 ? cdTipoDocumentoOld : objeto.getCdTipoDocumento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentacaoObrigatoriaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentacaoObrigatoriaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoDocumentacao, int cdTipoDocumento) {
		return delete(cdTipoDocumentacao, cdTipoDocumento, null);
	}

	public static int delete(int cdTipoDocumentacao, int cdTipoDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM PTC_DOCUMENTACAO_OBRIGATORIA WHERE CD_TIPO_DOCUMENTACAO=? AND CD_TIPO_DOCUMENTO=?");
			pstmt.setInt(1, cdTipoDocumentacao);
			pstmt.setInt(2, cdTipoDocumento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentacaoObrigatoriaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentacaoObrigatoriaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static DocumentacaoObrigatoria get(int cdTipoDocumentacao, int cdTipoDocumento) {
		return get(cdTipoDocumentacao, cdTipoDocumento, null);
	}

	public static DocumentacaoObrigatoria get(int cdTipoDocumentacao, int cdTipoDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PTC_DOCUMENTACAO_OBRIGATORIA WHERE CD_TIPO_DOCUMENTACAO=? AND CD_TIPO_DOCUMENTO=?");
			pstmt.setInt(1, cdTipoDocumentacao);
			pstmt.setInt(2, cdTipoDocumento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DocumentacaoObrigatoria(rs.getInt("CD_TIPO_DOCUMENTO"),
						rs.getInt("CD_TIPO_DOCUMENTACAO"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentacaoObrigatoriaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentacaoObrigatoriaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM PTC_DOCUMENTACAO_OBRIGATORIA");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentacaoObrigatoriaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentacaoObrigatoriaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<DocumentacaoObrigatoria> getList() {
		return getList(null);
	}

	public static ArrayList<DocumentacaoObrigatoria> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<DocumentacaoObrigatoria> list = new ArrayList<DocumentacaoObrigatoria>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				DocumentacaoObrigatoria obj = DocumentacaoObrigatoriaDAO.get(rsm.getInt("CD_TIPO_DOCUMENTACAO"), rsm.getInt("CD_TIPO_DOCUMENTO"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentacaoObrigatoriaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM PTC_DOCUMENTACAO_OBRIGATORIA", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
