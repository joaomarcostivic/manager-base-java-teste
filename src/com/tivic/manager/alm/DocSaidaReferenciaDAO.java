package com.tivic.manager.alm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class DocSaidaReferenciaDAO{

	public static int insert(DocSaidaReferencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(DocSaidaReferencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("alm_doc_saida_referencia", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDocSaidaReferencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_doc_saida_referencia (cd_doc_saida_referencia,"+
			                                  "cd_produto_servico,"+
			                                  "cd_empresa,"+
			                                  "cd_saida,"+
			                                  "cd_documento_saida,"+
			                                  "cd_local_armazenamento,"+
			                                  "cd_item,"+
			                                  "cd_referencia) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProdutoServico());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdSaida()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdSaida());
			if(objeto.getCdDocumentoSaida()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdDocumentoSaida());
			if(objeto.getCdLocalArmazenamento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdLocalArmazenamento());
			if(objeto.getCdItem()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdItem());
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdReferencia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocSaidaReferenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocSaidaReferenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(DocSaidaReferencia objeto) {
		return update(objeto, 0, null);
	}

	public static int update(DocSaidaReferencia objeto, int cdDocSaidaReferenciaOld) {
		return update(objeto, cdDocSaidaReferenciaOld, null);
	}

	public static int update(DocSaidaReferencia objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(DocSaidaReferencia objeto, int cdDocSaidaReferenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_doc_saida_referencia SET cd_doc_saida_referencia=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_saida=?,"+
												      		   "cd_documento_saida=?,"+
												      		   "cd_local_armazenamento=?,"+
												      		   "cd_item=?,"+
												      		   "cd_referencia=? WHERE cd_doc_saida_referencia=?");
			pstmt.setInt(1,objeto.getCdDocSaidaReferencia());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProdutoServico());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdSaida()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdSaida());
			if(objeto.getCdDocumentoSaida()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdDocumentoSaida());
			if(objeto.getCdLocalArmazenamento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdLocalArmazenamento());
			if(objeto.getCdItem()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdItem());
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdReferencia());
			pstmt.setInt(9, cdDocSaidaReferenciaOld!=0 ? cdDocSaidaReferenciaOld : objeto.getCdDocSaidaReferencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocSaidaReferenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocSaidaReferenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDocSaidaReferencia) {
		return delete(cdDocSaidaReferencia, null);
	}

	public static int delete(int cdDocSaidaReferencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_doc_saida_referencia WHERE cd_doc_saida_referencia=?");
			pstmt.setInt(1, cdDocSaidaReferencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocSaidaReferenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocSaidaReferenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static DocSaidaReferencia get(int cdDocSaidaReferencia) {
		return get(cdDocSaidaReferencia, null);
	}

	public static DocSaidaReferencia get(int cdDocSaidaReferencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_doc_saida_referencia WHERE cd_doc_saida_referencia=?");
			pstmt.setInt(1, cdDocSaidaReferencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DocSaidaReferencia(rs.getInt("cd_doc_saida_referencia"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_saida"),
						rs.getInt("cd_documento_saida"),
						rs.getInt("cd_local_armazenamento"),
						rs.getInt("cd_item"),
						rs.getInt("cd_referencia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocSaidaReferenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocSaidaReferenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_doc_saida_referencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocSaidaReferenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocSaidaReferenciaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM alm_doc_saida_referencia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
