package com.tivic.manager.ptc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class DocumentoOcorrenciaDAO{

	public static int insert(DocumentoOcorrencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(DocumentoOcorrencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "CD_DOCUMENTO");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdDocumento()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "CD_OCORRENCIA");
			keys[1].put("IS_KEY_NATIVE", "YES");
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "CD_TIPO_OCORRENCIA");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdTipoOcorrencia()));
			int code = Conexao.getSequenceCode("PTC_DOCUMENTO_OCORRENCIA", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdOcorrencia(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PTC_DOCUMENTO_OCORRENCIA (CD_DOCUMENTO,"+
			                                  "CD_TIPO_OCORRENCIA,"+
			                                  "CD_OCORRENCIA,"+
			                                  "CD_USUARIO,"+
			                                  "DT_OCORRENCIA,"+
			                                  "TXT_OCORRENCIA,"+
			                                  "TP_VISIBILIDADE," + 
			                                  "TP_CONSISTENCIA) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdDocumento());
			if(objeto.getCdTipoOcorrencia()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoOcorrencia());
			pstmt.setInt(3, code);
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuario());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			pstmt.setString(6,objeto.getTxtOcorrencia());
			pstmt.setInt(7,objeto.getTpVisibilidade());
			if(objeto.getTpConsistencia()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8, objeto.getTpConsistencia());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoOcorrenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoOcorrenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(DocumentoOcorrencia objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(DocumentoOcorrencia objeto, int cdDocumentoOld, int cdOcorrenciaOld, int cdTipoOcorrenciaOld) {
		return update(objeto, cdDocumentoOld, cdOcorrenciaOld, cdTipoOcorrenciaOld, null);
	}

	public static int update(DocumentoOcorrencia objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(DocumentoOcorrencia objeto, int cdDocumentoOld, int cdOcorrenciaOld, int cdTipoOcorrenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PTC_DOCUMENTO_OCORRENCIA SET CD_DOCUMENTO=?,"+
												      		   "CD_TIPO_OCORRENCIA=?,"+
												      		   "CD_OCORRENCIA=?,"+
												      		   "CD_USUARIO=?,"+
												      		   "DT_OCORRENCIA=?,"+
												      		   "TXT_OCORRENCIA=?,"+
												      		   "TP_VISIBILIDADE=?," + 
												      		   "TP_CONSISTENCIA=? WHERE CD_DOCUMENTO=? AND CD_OCORRENCIA=? AND CD_TIPO_OCORRENCIA=?");
			pstmt.setInt(1,objeto.getCdDocumento());
			pstmt.setInt(2,objeto.getCdTipoOcorrencia());
			pstmt.setInt(3,objeto.getCdOcorrencia());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuario());
			if(objeto.getDtOcorrencia()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtOcorrencia().getTimeInMillis()));
			pstmt.setString(6,objeto.getTxtOcorrencia());
			pstmt.setInt(7,objeto.getTpVisibilidade());
			if(objeto.getTpConsistencia()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8, objeto.getTpConsistencia());
			pstmt.setInt(9, cdDocumentoOld!=0 ? cdDocumentoOld : objeto.getCdDocumento());
			pstmt.setInt(10, cdOcorrenciaOld!=0 ? cdOcorrenciaOld : objeto.getCdOcorrencia());
			pstmt.setInt(11, cdTipoOcorrenciaOld!=0 ? cdTipoOcorrenciaOld : objeto.getCdTipoOcorrencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoOcorrenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoOcorrenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDocumento, int cdOcorrencia, int cdTipoOcorrencia) {
		return delete(cdDocumento, cdOcorrencia, cdTipoOcorrencia, null);
	}

	public static int delete(int cdDocumento, int cdOcorrencia, int cdTipoOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM PTC_DOCUMENTO_OCORRENCIA WHERE CD_DOCUMENTO=? AND CD_OCORRENCIA=? AND CD_TIPO_OCORRENCIA=?");
			pstmt.setInt(1, cdDocumento);
			pstmt.setInt(2, cdOcorrencia);
			pstmt.setInt(3, cdTipoOcorrencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoOcorrenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoOcorrenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static DocumentoOcorrencia get(int cdDocumento, int cdOcorrencia, int cdTipoOcorrencia) {
		return get(cdDocumento, cdOcorrencia, cdTipoOcorrencia, null);
	}

	public static DocumentoOcorrencia get(int cdDocumento, int cdOcorrencia, int cdTipoOcorrencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PTC_DOCUMENTO_OCORRENCIA WHERE CD_DOCUMENTO=? AND CD_OCORRENCIA=? AND CD_TIPO_OCORRENCIA=?");
			pstmt.setInt(1, cdDocumento);
			pstmt.setInt(2, cdOcorrencia);
			pstmt.setInt(3, cdTipoOcorrencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DocumentoOcorrencia(rs.getInt("CD_DOCUMENTO"),
						rs.getInt("CD_TIPO_OCORRENCIA"),
						rs.getInt("CD_OCORRENCIA"),
						rs.getInt("CD_USUARIO"),
						(rs.getTimestamp("DT_OCORRENCIA")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_OCORRENCIA").getTime()),
						rs.getString("TXT_OCORRENCIA"),
						rs.getInt("TP_VISIBILIDADE"),
						rs.getInt("TP_CONSISTENCIA"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoOcorrenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoOcorrenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM PTC_DOCUMENTO_OCORRENCIA");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoOcorrenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoOcorrenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<DocumentoOcorrencia> getList() {
		return getList(null);
	}

	public static ArrayList<DocumentoOcorrencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<DocumentoOcorrencia> list = new ArrayList<DocumentoOcorrencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				DocumentoOcorrencia obj = DocumentoOcorrenciaDAO.get(rsm.getInt("CD_DOCUMENTO"), rsm.getInt("CD_OCORRENCIA"), rsm.getInt("CD_TIPO_OCORRENCIA"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoOcorrenciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM PTC_DOCUMENTO_OCORRENCIA", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
