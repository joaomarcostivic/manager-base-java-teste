package com.tivic.manager.alm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class DocumentoEntradaOcorrenciaDAO{

	public static int insert(DocumentoEntradaOcorrencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(DocumentoEntradaOcorrencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("alm_documento_entrada_ocorrencia", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOcorrencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_documento_entrada_ocorrencia (cd_ocorrencia,"+
			                                  "cd_documento_entrada,"+
			                                  "cd_tipo_ocorrencia,"+
			                                  "dt_ocorrencia,"+
			                                  "txt_ocorrencia,"+
			                                  "st_ocorrencia) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDocumentoEntrada());
			if(objeto.getCdTipoOcorrencia()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoOcorrencia());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			pstmt.setString(5,objeto.getTxtOcorrencia());
			pstmt.setInt(6,objeto.getStOcorrencia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaOcorrenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaOcorrenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(DocumentoEntradaOcorrencia objeto) {
		return update(objeto, 0, null);
	}

	public static int update(DocumentoEntradaOcorrencia objeto, int cdOcorrenciaOld) {
		return update(objeto, cdOcorrenciaOld, null);
	}

	public static int update(DocumentoEntradaOcorrencia objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(DocumentoEntradaOcorrencia objeto, int cdOcorrenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_documento_entrada_ocorrencia SET cd_ocorrencia=?,"+
												      		   "cd_documento_entrada=?,"+
												      		   "cd_tipo_ocorrencia=?,"+
												      		   "dt_ocorrencia=?,"+
												      		   "txt_ocorrencia=?,"+
												      		   "st_ocorrencia=? WHERE cd_ocorrencia=?");
			pstmt.setInt(1,objeto.getCdOcorrencia());
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDocumentoEntrada());
			if(objeto.getCdTipoOcorrencia()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoOcorrencia());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			pstmt.setString(5,objeto.getTxtOcorrencia());
			pstmt.setInt(6,objeto.getStOcorrencia());
			pstmt.setInt(7, cdOcorrenciaOld!=0 ? cdOcorrenciaOld : objeto.getCdOcorrencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaOcorrenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaOcorrenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdOcorrencia) {
		return delete(cdOcorrencia, null);
	}

	public static int delete(int cdOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_documento_entrada_ocorrencia WHERE cd_ocorrencia=?");
			pstmt.setInt(1, cdOcorrencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaOcorrenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaOcorrenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static DocumentoEntradaOcorrencia get(int cdOcorrencia) {
		return get(cdOcorrencia, null);
	}

	public static DocumentoEntradaOcorrencia get(int cdOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_documento_entrada_ocorrencia WHERE cd_ocorrencia=?");
			pstmt.setInt(1, cdOcorrencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DocumentoEntradaOcorrencia(rs.getInt("cd_ocorrencia"),
						rs.getInt("cd_documento_entrada"),
						rs.getInt("cd_tipo_ocorrencia"),
						(rs.getTimestamp("dt_ocorrencia")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ocorrencia").getTime()),
						rs.getString("txt_ocorrencia"),
						rs.getInt("st_ocorrencia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaOcorrenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaOcorrenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_documento_entrada_ocorrencia");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaOcorrenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaOcorrenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<DocumentoEntradaOcorrencia> getList() {
		return getList(null);
	}

	public static ArrayList<DocumentoEntradaOcorrencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<DocumentoEntradaOcorrencia> list = new ArrayList<DocumentoEntradaOcorrencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				DocumentoEntradaOcorrencia obj = DocumentoEntradaOcorrenciaDAO.get(rsm.getInt("cd_ocorrencia"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaOcorrenciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM alm_documento_entrada_ocorrencia", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}