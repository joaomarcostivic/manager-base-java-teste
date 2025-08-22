package com.tivic.manager.prc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class ProcessoAndamentoDAO{

	public static int insert(ProcessoAndamento objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProcessoAndamento objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_andamento");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_processo");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdProcesso()));
			int code = Conexao.getSequenceCode("prc_processo_andamento", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdAndamento(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO prc_processo_andamento (cd_andamento,"+
			                                  "cd_processo,"+
			                                  "cd_tipo_andamento,"+
			                                  "dt_andamento,"+
			                                  "dt_lancamento,"+
			                                  "txt_andamento,"+
			                                  "st_andamento,"+
			                                  "tp_instancia,"+
			                                  "txt_ata,"+
			                                  "cd_usuario,"+
			                                  "dt_alteracao,"+
			                                  "tp_origem,"+
			                                  "blb_ata,"+
			                                  "tp_visibilidade,"+
			                                  "tp_evento_financeiro,"+
			                                  "vl_evento_financeiro,"+
			                                  "cd_conta_pagar,"+
			                                  "cd_conta_receber,"+
			                                  "dt_atualizacao_edi,"+
			                                  "st_atualizacao_edi,"+
			                                  "txt_publicacao,"+
			                                  "cd_documento,"+
			                                  "cd_origem_andamento,"+
			                                  "cd_recorte) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdProcesso()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProcesso());
			if(objeto.getCdTipoAndamento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoAndamento());
			if(objeto.getDtAndamento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtAndamento().getTimeInMillis()));
			if(objeto.getDtLancamento()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtLancamento().getTimeInMillis()));
			pstmt.setString(6,objeto.getTxtAndamento());
			pstmt.setInt(7,objeto.getStAndamento());
			pstmt.setInt(8,objeto.getTpInstancia());
			pstmt.setString(9,objeto.getTxtAta());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdUsuario());
			if(objeto.getDtAlteracao()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtAlteracao().getTimeInMillis()));
			pstmt.setInt(12,objeto.getTpOrigem());
			if(objeto.getBlbAta()==null)
				pstmt.setNull(13, Types.BINARY);
			else
				pstmt.setBytes(13,objeto.getBlbAta());
			pstmt.setInt(14,objeto.getTpVisibilidade());
			pstmt.setInt(15,objeto.getTpEventoFinanceiro());
			if (objeto.getVlEventoFinanceiro()==null)
				pstmt.setNull(16, Types.INTEGER);
			else
			pstmt.setDouble(16,objeto.getVlEventoFinanceiro());
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdContaPagar());
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdContaReceber());
			if(objeto.getDtAtualizacaoEdi()==null)
				pstmt.setNull(19, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(19,new Timestamp(objeto.getDtAtualizacaoEdi().getTimeInMillis()));
			pstmt.setInt(20,objeto.getStAtualizacaoEdi());
			pstmt.setString(21,objeto.getTxtPublicacao());
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(22, Types.INTEGER);
			else
				pstmt.setInt(22,objeto.getCdDocumento());
			if(objeto.getCdOrigemAndamento()==0)
				pstmt.setNull(23, Types.INTEGER);
			else
				pstmt.setInt(23,objeto.getCdOrigemAndamento());
			if(objeto.getCdRecorte()==0)
				pstmt.setNull(24, Types.INTEGER);
			else
				pstmt.setInt(24,objeto.getCdRecorte());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAndamentoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAndamentoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProcessoAndamento objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ProcessoAndamento objeto, int cdAndamentoOld, int cdProcessoOld) {
		return update(objeto, cdAndamentoOld, cdProcessoOld, null);
	}

	public static int update(ProcessoAndamento objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ProcessoAndamento objeto, int cdAndamentoOld, int cdProcessoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE prc_processo_andamento SET cd_andamento=?,"+
												      		   "cd_processo=?,"+
												      		   "cd_tipo_andamento=?,"+
												      		   "dt_andamento=?,"+
												      		   "dt_lancamento=?,"+
												      		   "txt_andamento=?,"+
												      		   "st_andamento=?,"+
												      		   "tp_instancia=?,"+
												      		   "txt_ata=?,"+
												      		   "cd_usuario=?,"+
												      		   "dt_alteracao=?,"+
												      		   "tp_origem=?,"+
												      		   "blb_ata=?,"+
												      		   "tp_visibilidade=?,"+
												      		   "tp_evento_financeiro=?,"+
												      		   "vl_evento_financeiro=?,"+
												      		   "cd_conta_pagar=?,"+
												      		   "cd_conta_receber=?,"+
												      		   "dt_atualizacao_edi=?,"+
												      		   "st_atualizacao_edi=?,"+
												      		   "txt_publicacao=?,"+
												      		   "cd_documento=?,"+
												      		   "cd_origem_andamento=?,"+
												      		   "cd_recorte=? WHERE cd_andamento=? AND cd_processo=?");
			pstmt.setInt(1,objeto.getCdAndamento());
			pstmt.setInt(2,objeto.getCdProcesso());
			if(objeto.getCdTipoAndamento()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTipoAndamento());
			if(objeto.getDtAndamento()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtAndamento().getTimeInMillis()));
			if(objeto.getDtLancamento()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtLancamento().getTimeInMillis()));
			pstmt.setString(6,objeto.getTxtAndamento());
			pstmt.setInt(7,objeto.getStAndamento());
			pstmt.setInt(8,objeto.getTpInstancia());
			pstmt.setString(9,objeto.getTxtAta());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdUsuario());
			if(objeto.getDtAlteracao()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtAlteracao().getTimeInMillis()));
			pstmt.setInt(12,objeto.getTpOrigem());
			if(objeto.getBlbAta()==null)
				pstmt.setNull(13, Types.BINARY);
			else
				pstmt.setBytes(13,objeto.getBlbAta());
			pstmt.setInt(14,objeto.getTpVisibilidade());
			pstmt.setInt(15,objeto.getTpEventoFinanceiro());
			pstmt.setDouble(16,objeto.getVlEventoFinanceiro());
			if(objeto.getCdContaPagar()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdContaPagar());
			if(objeto.getCdContaReceber()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18,objeto.getCdContaReceber());
			if(objeto.getDtAtualizacaoEdi()==null)
				pstmt.setNull(19, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(19,new Timestamp(objeto.getDtAtualizacaoEdi().getTimeInMillis()));
			pstmt.setInt(20,objeto.getStAtualizacaoEdi());
			pstmt.setString(21,objeto.getTxtPublicacao());
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(22, Types.INTEGER);
			else
				pstmt.setInt(22,objeto.getCdDocumento());
			if(objeto.getCdOrigemAndamento()==0)
				pstmt.setNull(23, Types.INTEGER);
			else
				pstmt.setInt(23,objeto.getCdOrigemAndamento());
			if(objeto.getCdRecorte()==0)
				pstmt.setNull(24, Types.INTEGER);
			else
				pstmt.setInt(24,objeto.getCdRecorte());
			pstmt.setInt(25, cdAndamentoOld!=0 ? cdAndamentoOld : objeto.getCdAndamento());
			pstmt.setInt(26, cdProcessoOld!=0 ? cdProcessoOld : objeto.getCdProcesso());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAndamentoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAndamentoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAndamento, int cdProcesso) {
		return delete(cdAndamento, cdProcesso, null);
	}

	public static int delete(int cdAndamento, int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM prc_processo_andamento WHERE cd_andamento=? AND cd_processo=?");
			pstmt.setInt(1, cdAndamento);
			pstmt.setInt(2, cdProcesso);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAndamentoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAndamentoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProcessoAndamento get(int cdAndamento, int cdProcesso) {
		return get(cdAndamento, cdProcesso, null);
	}

	public static ProcessoAndamento get(int cdAndamento, int cdProcesso, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM prc_processo_andamento WHERE cd_andamento=? AND cd_processo=?");
			pstmt.setInt(1, cdAndamento);
			pstmt.setInt(2, cdProcesso);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProcessoAndamento(rs.getInt("cd_andamento"),
						rs.getInt("cd_processo"),
						rs.getInt("cd_tipo_andamento"),
						(rs.getTimestamp("dt_andamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_andamento").getTime()),
						(rs.getTimestamp("dt_lancamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_lancamento").getTime()),
						rs.getString("txt_andamento"),
						rs.getInt("st_andamento"),
						rs.getInt("tp_instancia"),
						rs.getString("txt_ata"),
						rs.getInt("cd_usuario"),
						(rs.getTimestamp("dt_alteracao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_alteracao").getTime()),
						rs.getInt("tp_origem"),
						rs.getBytes("blb_ata")==null?null:rs.getBytes("blb_ata"),
						rs.getInt("tp_visibilidade"),
						rs.getInt("tp_evento_financeiro"),
						rs.getDouble("vl_evento_financeiro"),
						rs.getInt("cd_conta_pagar"),
						rs.getInt("cd_conta_receber"),
						(rs.getTimestamp("dt_atualizacao_edi")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_atualizacao_edi").getTime()),
						rs.getInt("st_atualizacao_edi"),
						rs.getString("txt_publicacao"),
						rs.getInt("cd_documento"),
						rs.getInt("cd_origem_andamento"),
						rs.getInt("cd_recorte"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAndamentoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAndamentoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM prc_processo_andamento");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAndamentoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAndamentoDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ProcessoAndamento> getList() {
		return getList(null);
	}

	public static ArrayList<ProcessoAndamento> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ProcessoAndamento> list = new ArrayList<ProcessoAndamento>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ProcessoAndamento obj = ProcessoAndamentoDAO.get(rsm.getInt("cd_andamento"), rsm.getInt("cd_processo"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProcessoAndamentoDAO.getList: " + e);
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
		return Search.find("SELECT * FROM prc_processo_andamento", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}