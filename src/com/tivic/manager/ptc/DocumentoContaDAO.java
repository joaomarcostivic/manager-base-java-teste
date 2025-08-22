package com.tivic.manager.ptc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;

import java.util.HashMap;
import java.util.ArrayList;

public class DocumentoContaDAO{

	public static int insert(DocumentoConta objeto) {
		return insert(objeto, null);
	}

	public static int insert(DocumentoConta objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_documento");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdDocumento()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_documento_conta");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("ptc_documento_conta", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDocumentoConta(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_documento_conta (cd_documento,"+
			                                  "cd_documento_conta,"+
			                                  "cd_conta_receber,"+
			                                  "cd_conta_pagar,"+
			                                  "nr_documento,"+
			                                  "cod_movimento,"+
			                                  "vl_pagamento,"+
			                                  "dt_pagamento,"+
			                                  "st_documento_conta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdDocumento());
			pstmt.setInt(2, code);
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdContaReceber());
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdContaPagar());
			pstmt.setString(5,objeto.getNrDocumento());
			pstmt.setString(6,objeto.getCodMovimento());
			pstmt.setDouble(7,objeto.getVlPagamento());
			if(objeto.getDtPagamento()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtPagamento().getTimeInMillis()));
			pstmt.setInt(9,objeto.getStDocumentoConta());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoContaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoContaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(DocumentoConta objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(DocumentoConta objeto, int cdDocumentoOld, int cdDocumentoContaOld) {
		return update(objeto, cdDocumentoOld, cdDocumentoContaOld, null);
	}

	public static int update(DocumentoConta objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(DocumentoConta objeto, int cdDocumentoOld, int cdDocumentoContaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_documento_conta SET cd_documento=?,"+
												      		   "cd_documento_conta=?,"+
												      		   "cd_conta_receber=?,"+
												      		   "cd_conta_pagar=?,"+
												      		   "nr_documento=?,"+
												      		   "cod_movimento=?,"+
												      		   "vl_pagamento=?,"+
												      		   "dt_pagamento=?,"+
												      		   "st_documento_conta=? WHERE cd_documento=? AND cd_documento_conta=?");
			pstmt.setInt(1,objeto.getCdDocumento());
			pstmt.setInt(2,objeto.getCdDocumentoConta());
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdContaReceber());
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdContaPagar());
			pstmt.setString(5,objeto.getNrDocumento());
			pstmt.setString(6,objeto.getCodMovimento());
			pstmt.setDouble(7,objeto.getVlPagamento());
			if(objeto.getDtPagamento()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtPagamento().getTimeInMillis()));
			pstmt.setInt(9,objeto.getStDocumentoConta());
			pstmt.setInt(10, cdDocumentoOld!=0 ? cdDocumentoOld : objeto.getCdDocumento());
			pstmt.setInt(11, cdDocumentoContaOld!=0 ? cdDocumentoContaOld : objeto.getCdDocumentoConta());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoContaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoContaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDocumento, int cdDocumentoConta) {
		return delete(cdDocumento, cdDocumentoConta, null);
	}

	public static int delete(int cdDocumento, int cdDocumentoConta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_documento_conta WHERE cd_documento=? AND cd_documento_conta=?");
			pstmt.setInt(1, cdDocumento);
			pstmt.setInt(2, cdDocumentoConta);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoContaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoContaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static DocumentoConta get(int cdDocumento, int cdDocumentoConta) {
		return get(cdDocumento, cdDocumentoConta, null);
	}

	public static DocumentoConta get(int cdDocumento, int cdDocumentoConta, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_documento_conta WHERE cd_documento=? AND cd_documento_conta=?");
			pstmt.setInt(1, cdDocumento);
			pstmt.setInt(2, cdDocumentoConta);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DocumentoConta(rs.getInt("cd_documento"),
						rs.getInt("cd_documento_conta"),
						rs.getInt("cd_conta_receber"),
						rs.getInt("cd_conta_pagar"),
						rs.getString("nr_documento"),
						rs.getString("cod_movimento"),
						rs.getFloat("vl_pagamento"),
						(rs.getTimestamp("dt_pagamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_pagamento").getTime()),
						rs.getInt("st_documento_conta"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoContaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoContaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_documento_conta");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoContaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoContaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<DocumentoConta> getList() {
		return getList(null);
	}

	public static ArrayList<DocumentoConta> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<DocumentoConta> list = new ArrayList<DocumentoConta>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				DocumentoConta obj = DocumentoContaDAO.get(rsm.getInt("cd_documento"), rsm.getInt("cd_documento_conta"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoContaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ptc_documento_conta", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
