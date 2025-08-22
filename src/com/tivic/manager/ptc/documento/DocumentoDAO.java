package com.tivic.manager.ptc.documento;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class DocumentoDAO{

	public static int insert(Documento objeto) {
		return insert(objeto, null);
	}

	public static int insert(Documento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("ptc_documento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDocumento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_documento (cd_documento,"+
			                                  "cd_usuario,"+
			                                  "dt_protocolo,"+
			                                  "txt_observacao,"+
			                                  "nr_documento,"+
			                                  "cd_tipo_documento,"+
			                                  "cd_fase,"+
			                                  "cd_situacao_documento,"+
			                                  "txt_documento,"+
			                                  "nm_requerente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdUsuario());
			if(objeto.getDtProtocolo()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtProtocolo().getTimeInMillis()));
			pstmt.setString(4,objeto.getTxtObservacao());
			pstmt.setString(5,objeto.getNrDocumento());
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdTipoDocumento());
			if(objeto.getCdFase()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdFase());
			if(objeto.getCdSituacaoDocumento()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdSituacaoDocumento());
			pstmt.setString(9,objeto.getTxtDocumento());
			pstmt.setString(10,objeto.getNmRequerente());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Documento objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Documento objeto, int cdDocumentoOld) {
		return update(objeto, cdDocumentoOld, null);
	}

	public static int update(Documento objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Documento objeto, int cdDocumentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_documento SET cd_documento=?,"+
												      		   "cd_usuario=?,"+
												      		   "dt_protocolo=?,"+
												      		   "txt_observacao=?,"+
												      		   "nr_documento=?,"+
												      		   "cd_tipo_documento=?,"+
												      		   "cd_fase=?,"+
												      		   "cd_situacao_documento=?,"+
												      		   "txt_documento=?,"+
												      		   "nm_requerente=? WHERE cd_documento=?");
			pstmt.setInt(1,objeto.getCdDocumento());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdUsuario());
			if(objeto.getDtProtocolo()==null)
				pstmt.setNull(3, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(3,new Timestamp(objeto.getDtProtocolo().getTimeInMillis()));
			pstmt.setString(4,objeto.getTxtObservacao());
			pstmt.setString(5,objeto.getNrDocumento());
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdTipoDocumento());
			if(objeto.getCdFase()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdFase());
			if(objeto.getCdSituacaoDocumento()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdSituacaoDocumento());
			pstmt.setString(9,objeto.getTxtDocumento());
			pstmt.setString(10,objeto.getNmRequerente());
			pstmt.setInt(11, cdDocumentoOld!=0 ? cdDocumentoOld : objeto.getCdDocumento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDocumento) {
		return delete(cdDocumento, null);
	}

	public static int delete(int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_documento WHERE cd_documento=?");
			pstmt.setInt(1, cdDocumento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Documento get(int cdDocumento) {
		return get(cdDocumento, null);
	}

	public static Documento get(int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_documento WHERE cd_documento=?");
			pstmt.setInt(1, cdDocumento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Documento(rs.getInt("cd_documento"),
						rs.getInt("cd_usuario"),
						(rs.getTimestamp("dt_protocolo")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_protocolo").getTime()),
						rs.getString("txt_observacao"),
						rs.getString("nr_documento"),
						rs.getInt("cd_tipo_documento"),
						rs.getInt("cd_fase"),
						rs.getInt("cd_situacao_documento"),
						rs.getString("txt_documento"),
						rs.getString("nm_requerente"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_documento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<Documento> getList() {
		return getList(null);
	}

	public static ArrayList<Documento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<Documento> list = new ArrayList<Documento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				Documento obj = DocumentoDAO.get(rsm.getInt("cd_documento"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM ptc_documento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
