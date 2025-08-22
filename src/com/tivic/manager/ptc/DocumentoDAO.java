package com.tivic.manager.ptc;

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
	    return insert(objeto, connect, false);
	}
	
	public static int insert(Documento objeto, Connection connect, boolean sync){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = sync ? Conexao.getSequenceCodeSync("ptc_documento", connect) : Conexao.getSequenceCode("ptc_documento", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDocumento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO ptc_documento (cd_documento,"+
			                                  "cd_arquivo,"+
			                                  "cd_setor,"+
			                                  "cd_usuario,"+
			                                  "nm_local_origem,"+
			                                  "dt_protocolo,"+
			                                  "tp_documento,"+
			                                  "txt_observacao,"+
			                                  "id_documento,"+
			                                  "nr_documento,"+
			                                  "cd_tipo_documento,"+
			                                  "cd_fase,"+
			                                  "cd_setor_atual,"+
			                                  "cd_situacao_documento,"+
			                                  "cd_servico,"+
			                                  "cd_atendimento,"+
			                                  "txt_documento,"+
			                                  "cd_empresa,"+
			                                  "cd_processo,"+
			                                  "tp_prioridade,"+
			                                  "cd_documento_superior,"+
			                                  "ds_assunto,"+
			                                  "nr_atendimento,"+
			                                  "lg_notificacao,"+
			                                  "cd_tipo_documento_anterior,"+
			                                  "cd_documento_externo,"+
			                                  "nr_assunto,"+
			                                  "nr_documento_externo,"+
			                                  "nr_protocolo_externo,"+
			                                  "nr_ano_externo,"+
			                                  "tp_documento_externo,"+
			                                  "tp_interno_externo,"+
			                                  "nm_requerente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setInt(2,objeto.getCdArquivo());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdSetor());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuario());
			pstmt.setString(5,objeto.getNmLocalOrigem());
			if(objeto.getDtProtocolo()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtProtocolo().getTimeInMillis()));
			pstmt.setInt(7,objeto.getTpDocumento());
			pstmt.setString(8,objeto.getTxtObservacao());
			pstmt.setString(9,objeto.getIdDocumento());
			pstmt.setString(10,objeto.getNrDocumento());
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdTipoDocumento());
			if(objeto.getCdFase()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdFase());
			if(objeto.getCdSetorAtual()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdSetorAtual());
			if(objeto.getCdSituacaoDocumento()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdSituacaoDocumento());
			pstmt.setInt(15,objeto.getCdServico());
			if(objeto.getCdAtendimento()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdAtendimento());
			pstmt.setString(17,objeto.getTxtDocumento());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdEmpresa());
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdProcesso());
			pstmt.setInt(20,objeto.getTpPrioridade());
			if(objeto.getCdDocumentoSuperior()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdDocumentoSuperior());
			pstmt.setString(22,objeto.getDsAssunto());
			pstmt.setString(23,objeto.getNrAtendimento());
			pstmt.setInt(24,objeto.getLgNotificacao());
			if(objeto.getCdTipoDocumentoAnterior()==0)
				pstmt.setNull(25, Types.INTEGER);
			else
				pstmt.setInt(25,objeto.getCdTipoDocumentoAnterior());
			pstmt.setString(26,objeto.getCdDocumentoExterno());
			pstmt.setString(27,objeto.getNrAssunto());
			pstmt.setString(28,objeto.getNrDocumentoExterno());
			pstmt.setString(29,objeto.getNrProtocoloExterno());
			pstmt.setString(30,objeto.getNrAnoExterno());
			pstmt.setInt(31,objeto.getTpDocumentoExterno());
			pstmt.setInt(32,objeto.getTpInternoExterno());
			pstmt.setString(33,objeto.getNmRequerente());
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
												      		   "cd_arquivo=?,"+
												      		   "cd_setor=?,"+
												      		   "cd_usuario=?,"+
												      		   "nm_local_origem=?,"+
												      		   "dt_protocolo=?,"+
												      		   "tp_documento=?,"+
												      		   "txt_observacao=?,"+
												      		   "id_documento=?,"+
												      		   "nr_documento=?,"+
												      		   "cd_tipo_documento=?,"+
												      		   "cd_fase=?,"+
												      		   "cd_setor_atual=?,"+
												      		   "cd_situacao_documento=?,"+
												      		   "cd_servico=?,"+
												      		   "cd_atendimento=?,"+
												      		   "txt_documento=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_processo=?,"+
												      		   "tp_prioridade=?,"+
												      		   "cd_documento_superior=?,"+
												      		   "ds_assunto=?,"+
												      		   "nr_atendimento=?,"+
												      		   "lg_notificacao=?,"+
												      		   "cd_tipo_documento_anterior=?,"+
												      		   "cd_documento_externo=?,"+
												      		   "nr_assunto=?,"+
												      		   "nr_documento_externo=?,"+
												      		   "nr_protocolo_externo=?,"+
												      		   "nr_ano_externo=?,"+
												      		   "tp_documento_externo=?,"+
												      		   "tp_interno_externo=?,"+
												      		   "nm_requerente=? WHERE cd_documento=?");
			pstmt.setInt(1,objeto.getCdDocumento());
			pstmt.setInt(2,objeto.getCdArquivo());
			if(objeto.getCdSetor()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdSetor());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdUsuario());
			pstmt.setString(5,objeto.getNmLocalOrigem());
			if(objeto.getDtProtocolo()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtProtocolo().getTimeInMillis()));
			pstmt.setInt(7,objeto.getTpDocumento());
			pstmt.setString(8,objeto.getTxtObservacao());
			pstmt.setString(9,objeto.getIdDocumento());
			pstmt.setString(10,objeto.getNrDocumento());
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdTipoDocumento());
			if(objeto.getCdFase()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdFase());
			if(objeto.getCdSetorAtual()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdSetorAtual());
			if(objeto.getCdSituacaoDocumento()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdSituacaoDocumento());
			pstmt.setInt(15,objeto.getCdServico());
			if(objeto.getCdAtendimento()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdAtendimento());
			pstmt.setString(17,objeto.getTxtDocumento());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdEmpresa());
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(19, Types.INTEGER);
			else
				pstmt.setInt(19,objeto.getCdProcesso());
			pstmt.setInt(20,objeto.getTpPrioridade());
			if(objeto.getCdDocumentoSuperior()==0)
				pstmt.setNull(21, Types.INTEGER);
			else
				pstmt.setInt(21,objeto.getCdDocumentoSuperior());
			pstmt.setString(22,objeto.getDsAssunto());
			pstmt.setString(23,objeto.getNrAtendimento());
			pstmt.setInt(24,objeto.getLgNotificacao());
			if(objeto.getCdTipoDocumentoAnterior()==0)
				pstmt.setNull(25, Types.INTEGER);
			else
				pstmt.setInt(25,objeto.getCdTipoDocumentoAnterior());
			pstmt.setString(26,objeto.getCdDocumentoExterno());
			pstmt.setString(27,objeto.getNrAssunto());
			pstmt.setString(28,objeto.getNrDocumentoExterno());
			pstmt.setString(29,objeto.getNrProtocoloExterno());
			pstmt.setString(30,objeto.getNrAnoExterno());
			pstmt.setInt(31,objeto.getTpDocumentoExterno());
			pstmt.setInt(32,objeto.getTpInternoExterno());
			pstmt.setString(33,objeto.getNmRequerente());
			pstmt.setInt(34, cdDocumentoOld!=0 ? cdDocumentoOld : objeto.getCdDocumento());
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
						rs.getInt("cd_arquivo"),
						rs.getInt("cd_setor"),
						rs.getInt("cd_usuario"),
						rs.getString("nm_local_origem"),
						(rs.getTimestamp("dt_protocolo")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_protocolo").getTime()),
						rs.getInt("tp_documento"),
						rs.getString("txt_observacao"),
						rs.getString("id_documento"),
						rs.getString("nr_documento"),
						rs.getInt("cd_tipo_documento"),
						rs.getInt("cd_fase"),
						rs.getInt("cd_setor_atual"),
						rs.getInt("cd_situacao_documento"),
						rs.getInt("cd_servico"),
						rs.getInt("cd_atendimento"),
						rs.getString("txt_documento"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_processo"),
						rs.getInt("tp_prioridade"),
						rs.getInt("cd_documento_superior"),
						rs.getString("ds_assunto"),
						rs.getString("nr_atendimento"),
						rs.getInt("lg_notificacao"),
						rs.getInt("cd_tipo_documento_anterior"),
						rs.getString("cd_documento_externo"),
						rs.getString("nr_assunto"),
						rs.getString("nr_documento_externo"),
						rs.getString("nr_protocolo_externo"),
						rs.getString("nr_ano_externo"),
						rs.getInt("tp_documento_externo"),
						rs.getInt("tp_interno_externo"),
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
