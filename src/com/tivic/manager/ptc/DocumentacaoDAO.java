package com.tivic.manager.ptc;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class DocumentacaoDAO{

	public static int insert(Documentacao objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(Documentacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_documentacao");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_documento");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdDocumento()));
			int code = Conexao.getSequenceCode("ptc_documentacao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDocumentacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_documentacao (cd_documentacao,"+
			                                  "cd_documento,"+
			                                  "cd_arquivo,"+
			                                  "dt_recepcao,"+
			                                  "dt_devolucao,"+
			                                  "lg_original,"+
			                                  "st_documento,"+
			                                  "dt_validade," +
			                                  "st_anexo," +
			                                  "cd_tipo_documentacao," +
			                                  "txt_documentacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDocumento());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdArquivo());
			if(objeto.getDtRecepcao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtRecepcao().getTimeInMillis()));
			if(objeto.getDtDevolucao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtDevolucao().getTimeInMillis()));
			pstmt.setInt(6,objeto.getLgOriginal());
			pstmt.setInt(7,objeto.getStDocumento());
			if(objeto.getDtValidade()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtValidade().getTimeInMillis()));
			pstmt.setInt(9, objeto.getStAnexo());
			if(objeto.getCdTipoDocumentacao()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdTipoDocumentacao());
			pstmt.setString(11,objeto.getTxtDocumentacao());

			pstmt.executeUpdate();
			return code;
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

	public static int update(Documentacao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(Documentacao objeto, int cdDocumentacaoOld, int cdDocumentoOld) {
		return update(objeto, cdDocumentacaoOld, cdDocumentoOld, null);
	}

	public static int update(Documentacao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(Documentacao objeto, int cdDocumentacaoOld, int cdDocumentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_documentacao SET cd_documentacao=?,"+
												      		   "cd_documento=?,"+
												      		   "cd_arquivo=?,"+
												      		   "dt_recepcao=?,"+
												      		   "dt_devolucao=?,"+
												      		   "lg_original=?,"+
												      		   "st_documento=?,"+
												      		   "dt_validade=?," +
												      		   "st_anexo=?," +
												      		   "cd_tipo_documentacao=?," +
												      		   "txt_documentacao=? WHERE cd_documentacao=? AND cd_documento=?");
			pstmt.setInt(1,objeto.getCdDocumentacao());
			pstmt.setInt(2,objeto.getCdDocumento());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdArquivo());
			if(objeto.getDtRecepcao()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtRecepcao().getTimeInMillis()));
			if(objeto.getDtDevolucao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtDevolucao().getTimeInMillis()));
			pstmt.setInt(6,objeto.getLgOriginal());
			pstmt.setInt(7,objeto.getStDocumento());
			if(objeto.getDtValidade()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtValidade().getTimeInMillis()));
			pstmt.setInt(9, objeto.getStAnexo());
			if(objeto.getCdTipoDocumentacao()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdTipoDocumentacao());
			pstmt.setString(11,objeto.getTxtDocumentacao());

			pstmt.setInt(12, cdDocumentacaoOld!=0 ? cdDocumentacaoOld : objeto.getCdDocumentacao());
			pstmt.setInt(13, cdDocumentoOld!=0 ? cdDocumentoOld : objeto.getCdDocumento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDocumentacao, int cdDocumento) {
		return delete(cdDocumentacao, cdDocumento, null);
	}

	public static int delete(int cdDocumentacao, int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_documentacao WHERE cd_documentacao=? AND cd_documento=?");
			pstmt.setInt(1, cdDocumentacao);
			pstmt.setInt(2, cdDocumento);
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Documentacao get(int cdDocumentacao, int cdDocumento) {
		return get(cdDocumentacao, cdDocumento, null);
	}

	public static Documentacao get(int cdDocumentacao, int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_documentacao WHERE cd_documentacao=? AND cd_documento=?");
			pstmt.setInt(1, cdDocumentacao);
			pstmt.setInt(2, cdDocumento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Documentacao(rs.getInt("cd_documentacao"),
						rs.getInt("cd_documento"),
						rs.getInt("cd_arquivo"),
						(rs.getTimestamp("dt_recepcao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_recepcao").getTime()),
						(rs.getTimestamp("dt_devolucao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_devolucao").getTime()),
						rs.getInt("lg_original"),
						rs.getInt("st_documento"),
						(rs.getTimestamp("dt_validade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_validade").getTime()), 
						rs.getInt("st_anexo"),
						rs.getInt("cd_tipo_documentacao"),
						rs.getString("txt_documentacao"));
			}
			else{
				return null;
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_documentacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentacaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM ptc_documentacao", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
