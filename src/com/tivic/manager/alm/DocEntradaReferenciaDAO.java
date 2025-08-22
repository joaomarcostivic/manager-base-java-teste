package com.tivic.manager.alm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class DocEntradaReferenciaDAO{

	public static int insert(DocEntradaReferencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(DocEntradaReferencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("alm_doc_entrada_referencia", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDocEntradaReferencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_doc_entrada_referencia (cd_doc_entrada_referencia,"+
			                                  "cd_produto_servico,"+
			                                  "cd_empresa,"+
			                                  "cd_documento_entrada,"+
			                                  "cd_local_armazenamento,"+
			                                  "cd_entrada_local_item,"+
			                                  "cd_referencia) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProdutoServico());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdDocumentoEntrada());
			if(objeto.getCdLocalArmazenamento()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdLocalArmazenamento());
			if(objeto.getCdEntradaLocalItem()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdEntradaLocalItem());
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdReferencia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocEntradaReferenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocEntradaReferenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(DocEntradaReferencia objeto) {
		return update(objeto, 0, null);
	}

	public static int update(DocEntradaReferencia objeto, int cdDocEntradaReferenciaOld) {
		return update(objeto, cdDocEntradaReferenciaOld, null);
	}

	public static int update(DocEntradaReferencia objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(DocEntradaReferencia objeto, int cdDocEntradaReferenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_doc_entrada_referencia SET cd_doc_entrada_referencia=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_documento_entrada=?,"+
												      		   "cd_local_armazenamento=?,"+
												      		   "cd_entrada_local_item=?,"+
												      		   "cd_referencia=? WHERE cd_doc_entrada_referencia=?");
			pstmt.setInt(1,objeto.getCdDocEntradaReferencia());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProdutoServico());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdDocumentoEntrada());
			if(objeto.getCdLocalArmazenamento()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdLocalArmazenamento());
			if(objeto.getCdEntradaLocalItem()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdEntradaLocalItem());
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdReferencia());
			pstmt.setInt(8, cdDocEntradaReferenciaOld!=0 ? cdDocEntradaReferenciaOld : objeto.getCdDocEntradaReferencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocEntradaReferenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocEntradaReferenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDocEntradaReferencia) {
		return delete(cdDocEntradaReferencia, null);
	}

	public static int delete(int cdDocEntradaReferencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_doc_entrada_referencia WHERE cd_doc_entrada_referencia=?");
			pstmt.setInt(1, cdDocEntradaReferencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocEntradaReferenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocEntradaReferenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static DocEntradaReferencia get(int cdDocEntradaReferencia) {
		return get(cdDocEntradaReferencia, null);
	}

	public static DocEntradaReferencia get(int cdDocEntradaReferencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_doc_entrada_referencia WHERE cd_doc_entrada_referencia=?");
			pstmt.setInt(1, cdDocEntradaReferencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DocEntradaReferencia(rs.getInt("cd_doc_entrada_referencia"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_documento_entrada"),
						rs.getInt("cd_local_armazenamento"),
						rs.getInt("cd_entrada_local_item"),
						rs.getInt("cd_referencia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocEntradaReferenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocEntradaReferenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_doc_entrada_referencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocEntradaReferenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocEntradaReferenciaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM alm_doc_entrada_referencia", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
