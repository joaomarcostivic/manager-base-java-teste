package com.tivic.manager.grl;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

public class TipoDocumentacaoPessoaDAO{

	public static int insert(TipoDocumentacaoPessoa objeto) {
		return insert(objeto, null);
	}

	public static int insert(TipoDocumentacaoPessoa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_tipo_documentacao_pessoa (cd_tipo_documento,"+
			                                  "cd_pessoa,"+
			                                  "nr_documento) VALUES (?, ?, ?)");
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTipoDocumento());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setString(3,objeto.getNrDocumento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoPessoaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoPessoaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TipoDocumentacaoPessoa objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TipoDocumentacaoPessoa objeto, int cdTipoDocumentoOld, int cdPessoaOld) {
		return update(objeto, cdTipoDocumentoOld, cdPessoaOld, null);
	}

	public static int update(TipoDocumentacaoPessoa objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TipoDocumentacaoPessoa objeto, int cdTipoDocumentoOld, int cdPessoaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_tipo_documentacao_pessoa SET cd_tipo_documento=?,"+
												      		   "cd_pessoa=?,"+
												      		   "nr_documento=? WHERE cd_tipo_documento=? AND cd_pessoa=?");
			pstmt.setInt(1,objeto.getCdTipoDocumento());
			pstmt.setInt(2,objeto.getCdPessoa());
			pstmt.setString(3,objeto.getNrDocumento());
			pstmt.setInt(4, cdTipoDocumentoOld!=0 ? cdTipoDocumentoOld : objeto.getCdTipoDocumento());
			pstmt.setInt(5, cdPessoaOld!=0 ? cdPessoaOld : objeto.getCdPessoa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoPessoaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoPessoaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTipoDocumento, int cdPessoa) {
		return delete(cdTipoDocumento, cdPessoa, null);
	}

	public static int delete(int cdTipoDocumento, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_tipo_documentacao_pessoa WHERE cd_tipo_documento=? AND cd_pessoa=?");
			pstmt.setInt(1, cdTipoDocumento);
			pstmt.setInt(2, cdPessoa);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoPessoaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoPessoaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TipoDocumentacaoPessoa get(int cdTipoDocumento, int cdPessoa) {
		return get(cdTipoDocumento, cdPessoa, null);
	}

	public static TipoDocumentacaoPessoa get(int cdTipoDocumento, int cdPessoa, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_documentacao_pessoa WHERE cd_tipo_documento=? AND cd_pessoa=?");
			pstmt.setInt(1, cdTipoDocumento);
			pstmt.setInt(2, cdPessoa);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TipoDocumentacaoPessoa(rs.getInt("cd_tipo_documento"),
						rs.getInt("cd_pessoa"),
						rs.getString("nr_documento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoPessoaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoPessoaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_tipo_documentacao_pessoa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoPessoaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoPessoaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<TipoDocumentacaoPessoa> getList() {
		return getList(null);
	}

	public static ArrayList<TipoDocumentacaoPessoa> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<TipoDocumentacaoPessoa> list = new ArrayList<TipoDocumentacaoPessoa>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				TipoDocumentacaoPessoa obj = TipoDocumentacaoPessoaDAO.get(rsm.getInt("cd_tipo_documento"), rsm.getInt("cd_pessoa"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TipoDocumentacaoPessoaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM grl_tipo_documentacao_pessoa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
