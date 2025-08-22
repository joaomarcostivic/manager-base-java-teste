package com.tivic.manager.ptc;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class DocumentoTramitacaoDAO{

	public static int insert(DocumentoTramitacao objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(DocumentoTramitacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_tramitacao");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_documento");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdDocumento()));
			int code = Conexao.getSequenceCode("ptc_documento_tramitacao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTramitacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_documento_tramitacao (cd_tramitacao,"+
			                                  "cd_documento,"+
			                                  "cd_setor_destino,"+
			                                  "cd_usuario_destino,"+
			                                  "cd_setor_origem,"+
			                                  "cd_usuario_origem,"+
			                                  "dt_tramitacao,"+
			                                  "dt_recebimento,"+
			                                  "nm_local_destino,"+
			                                  "txt_tramitacao,"+
			                                  "cd_situacao_documento,"+
			                                  "cd_fase,"+
			                                  "nm_local_origem,"+
			                                  "nr_remessa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDocumento());
			if(objeto.getCdSetorDestino()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdSetorDestino());
			if(objeto.getCdUsuarioDestino()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuarioDestino());
			if(objeto.getCdSetorOrigem()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdSetorOrigem());
			if(objeto.getCdUsuarioOrigem()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdUsuarioOrigem());
			if(objeto.getDtTramitacao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtTramitacao().getTimeInMillis()));
			if(objeto.getDtRecebimento()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtRecebimento().getTimeInMillis()));
			pstmt.setString(9,objeto.getNmLocalDestino());
			pstmt.setString(10,objeto.getTxtTramitacao());
			if(objeto.getCdSituacaoDocumento()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdSituacaoDocumento());
			if(objeto.getCdFase()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdFase());
			pstmt.setString(13, objeto.getNmLocalOrigem());
			pstmt.setString(14, objeto.getNrRemessa());
			pstmt.executeUpdate();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoTramitacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(DocumentoTramitacao objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(DocumentoTramitacao objeto, int cdTramitacaoOld, int cdDocumentoOld) {
		return update(objeto, cdTramitacaoOld, cdDocumentoOld, null);
	}

	public static int update(DocumentoTramitacao objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(DocumentoTramitacao objeto, int cdTramitacaoOld, int cdDocumentoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE ptc_documento_tramitacao SET cd_tramitacao=?,"+
												      		   "cd_documento=?,"+
												      		   "cd_setor_destino=?,"+
												      		   "cd_usuario_destino=?,"+
												      		   "cd_setor_origem=?,"+
												      		   "cd_usuario_origem=?,"+
												      		   "dt_tramitacao=?,"+
												      		   "dt_recebimento=?,"+
												      		   "nm_local_destino=?,"+
												      		   "txt_tramitacao=?,"+
												      		   "cd_situacao_documento=?,"+
												      		   "cd_fase=?, " +
												      		   "nm_local_origem=?, "+
												      		   "nr_remessa=? WHERE cd_tramitacao=? AND cd_documento=?");
			pstmt.setInt(1,objeto.getCdTramitacao());
			pstmt.setInt(2,objeto.getCdDocumento());
			if(objeto.getCdSetorDestino()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdSetorDestino());
			if(objeto.getCdUsuarioDestino()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuarioDestino());
			if(objeto.getCdSetorOrigem()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdSetorOrigem());
			if(objeto.getCdUsuarioOrigem()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdUsuarioOrigem());
			if(objeto.getDtTramitacao()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtTramitacao().getTimeInMillis()));
			if(objeto.getDtRecebimento()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtRecebimento().getTimeInMillis()));
			pstmt.setString(9,objeto.getNmLocalDestino());
			pstmt.setString(10,objeto.getTxtTramitacao());
			if(objeto.getCdSituacaoDocumento()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdSituacaoDocumento());
			if(objeto.getCdFase()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdFase());
			pstmt.setString(13, objeto.getNmLocalOrigem());
			pstmt.setString(14, objeto.getNrRemessa());
			pstmt.setInt(15, cdTramitacaoOld!=0 ? cdTramitacaoOld : objeto.getCdTramitacao());
			pstmt.setInt(16, cdDocumentoOld!=0 ? cdDocumentoOld : objeto.getCdDocumento());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoTramitacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTramitacao, int cdDocumento) {
		return delete(cdTramitacao, cdDocumento, null);
	}

	public static int delete(int cdTramitacao, int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM ptc_documento_tramitacao WHERE cd_tramitacao=? AND cd_documento=?");
			pstmt.setInt(1, cdTramitacao);
			pstmt.setInt(2, cdDocumento);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoTramitacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static DocumentoTramitacao get(int cdTramitacao, int cdDocumento) {
		return get(cdTramitacao, cdDocumento, null);
	}

	public static DocumentoTramitacao get(int cdTramitacao, int cdDocumento, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM ptc_documento_tramitacao WHERE cd_tramitacao=? AND cd_documento=?");
			pstmt.setInt(1, cdTramitacao);
			pstmt.setInt(2, cdDocumento);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DocumentoTramitacao(rs.getInt("cd_tramitacao"),
						rs.getInt("cd_documento"),
						rs.getInt("cd_setor_destino"),
						rs.getInt("cd_usuario_destino"),
						rs.getInt("cd_setor_origem"),
						rs.getInt("cd_usuario_origem"),
						(rs.getTimestamp("dt_tramitacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_tramitacao").getTime()),
						(rs.getTimestamp("dt_recebimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_recebimento").getTime()),
						rs.getString("nm_local_destino"),
						rs.getString("txt_tramitacao"),
						rs.getInt("cd_situacao_documento"),
						rs.getInt("cd_fase"),
						rs.getString("nm_local_origem"),
						rs.getString("nr_remessa"));
			}
			else{
				return null;
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoTramitacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM ptc_documento_tramitacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoTramitacaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM ptc_documento_tramitacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
