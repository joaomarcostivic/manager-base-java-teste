package com.tivic.manager.ptc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class DocumentoPendenciaDAO{

	public static int insert(DocumentoPendencia objeto) {
		return insert(objeto, null);
	}

	public static int insert(DocumentoPendencia objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO PTC_DOCUMENTO_PENDENCIA (CD_DOCUMENTO,"+
			                                  "CD_TIPO_PENDENCIA,"+
			                                  "DT_PENDENCIA,"+
			                                  "DT_BAIXA,"+
			                                  "TXT_PENDENCIA,"+
			                                  "CD_USUARIO_REGISTRO,"+
			                                  "CD_USUARIO_BAIXA,"+
			                                  "TXT_BAIXA) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdDocumento());
			if(objeto.getCdTipoPendencia()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTipoPendencia());
			if(objeto.getDtPendencia()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtPendencia().getTimeInMillis()));
			if(objeto.getDtBaixa()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtBaixa().getTimeInMillis()));
			pstmt.setString(5,objeto.getTxtPendencia());
			if(objeto.getCdUsuarioRegistro()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdUsuarioRegistro());
			if(objeto.getCdUsuarioBaixa()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdUsuarioBaixa());
			pstmt.setString(8,objeto.getTxtBaixa());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoPendenciaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoPendenciaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(DocumentoPendencia objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(DocumentoPendencia objeto, int cdDocumentoOld, int cdTipoPendenciaOld) {
		return update(objeto, cdDocumentoOld, cdTipoPendenciaOld, null);
	}

	public static int update(DocumentoPendencia objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(DocumentoPendencia objeto, int cdDocumentoOld, int cdTipoPendenciaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE PTC_DOCUMENTO_PENDENCIA SET CD_DOCUMENTO=?,"+
												      		   "CD_TIPO_PENDENCIA=?,"+
												      		   "DT_PENDENCIA=?,"+
												      		   "DT_BAIXA=?,"+
												      		   "TXT_PENDENCIA=?,"+
												      		   "CD_USUARIO_REGISTRO=?,"+
												      		   "CD_USUARIO_BAIXA=?, "+
												      		   "TXT_BAIXA=? WHERE CD_DOCUMENTO=? AND CD_TIPO_PENDENCIA=?");
			pstmt.setInt(1,objeto.getCdDocumento());
			pstmt.setInt(2,objeto.getCdTipoPendencia());
			if(objeto.getDtPendencia()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtPendencia().getTimeInMillis()));
			if(objeto.getDtBaixa()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtBaixa().getTimeInMillis()));
			pstmt.setString(5,objeto.getTxtPendencia());
			if(objeto.getCdUsuarioRegistro()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdUsuarioRegistro());
			if(objeto.getCdUsuarioBaixa()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdUsuarioBaixa());
			pstmt.setString(8,objeto.getTxtBaixa());
			pstmt.setInt(9, cdDocumentoOld!=0 ? cdDocumentoOld : objeto.getCdDocumento());
			pstmt.setInt(10, cdTipoPendenciaOld!=0 ? cdTipoPendenciaOld : objeto.getCdTipoPendencia());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoPendenciaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoPendenciaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDocumento, int cdTipoPendencia) {
		return delete(cdDocumento, cdTipoPendencia, null);
	}

	public static int delete(int cdDocumento, int cdTipoPendencia, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM PTC_DOCUMENTO_PENDENCIA WHERE CD_DOCUMENTO=? AND CD_TIPO_PENDENCIA=?");
			pstmt.setInt(1, cdDocumento);
			pstmt.setInt(2, cdTipoPendencia);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoPendenciaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoPendenciaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static DocumentoPendencia get(int cdDocumento, int cdTipoPendencia) {
		return get(cdDocumento, cdTipoPendencia, null);
	}

	public static DocumentoPendencia get(int cdDocumento, int cdTipoPendencia, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM PTC_DOCUMENTO_PENDENCIA WHERE CD_DOCUMENTO=? AND CD_TIPO_PENDENCIA=?");
			pstmt.setInt(1, cdDocumento);
			pstmt.setInt(2, cdTipoPendencia);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DocumentoPendencia(rs.getInt("CD_DOCUMENTO"),
						rs.getInt("CD_TIPO_PENDENCIA"),
						(rs.getTimestamp("DT_PENDENCIA")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_PENDENCIA").getTime()),
						(rs.getTimestamp("DT_BAIXA")==null)?null:Util.longToCalendar(rs.getTimestamp("DT_BAIXA").getTime()),
						rs.getString("TXT_PENDENCIA"),
						rs.getInt("CD_USUARIO_REGISTRO"),
						rs.getInt("CD_USUARIO_BAIXA"),
						rs.getString("TXT_BAIXA"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoPendenciaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoPendenciaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM PTC_DOCUMENTO_PENDENCIA");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoPendenciaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoPendenciaDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<DocumentoPendencia> getList() {
		return getList(null);
	}

	public static ArrayList<DocumentoPendencia> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<DocumentoPendencia> list = new ArrayList<DocumentoPendencia>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				DocumentoPendencia obj = DocumentoPendenciaDAO.get(rsm.getInt("CD_DOCUMENTO"), rsm.getInt("CD_TIPO_PENDENCIA"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoPendenciaDAO.getList: " + e);
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
		return Search.find("SELECT * FROM PTC_DOCUMENTO_PENDENCIA", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
