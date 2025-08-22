package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class ContaReceberDAO{

	public static int insert(ContaReceber objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContaReceber objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_conta_receber", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdContaReceber(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_conta_receber (cd_conta_receber,"+
			                                  "cd_pessoa,"+
			                                  "cd_empresa,"+
			                                  "cd_contrato,"+
			                                  "cd_conta_origem,"+
			                                  "cd_documento_saida,"+
			                                  "cd_conta_carteira,"+
			                                  "cd_conta,"+
			                                  "cd_frete,"+
			                                  "nr_documento,"+
			                                  "id_conta_receber,"+
			                                  "nr_parcela,"+
			                                  "nr_referencia,"+
			                                  "cd_tipo_documento,"+
			                                  "ds_historico,"+
			                                  "dt_vencimento,"+
			                                  "dt_emissao,"+
			                                  "dt_recebimento,"+
			                                  "dt_prorrogacao,"+
			                                  "vl_conta,"+
			                                  "vl_abatimento,"+
			                                  "vl_acrescimo,"+
			                                  "vl_recebido,"+
			                                  "st_conta,"+
			                                  "tp_frequencia,"+
			                                  "qt_parcelas,"+
			                                  "tp_conta_receber,"+
			                                  "cd_negociacao,"+
			                                  "txt_observacao,"+
			                                  "cd_plano_pagamento,"+
			                                  "cd_forma_pagamento,"+
			                                  "dt_digitacao,"+
			                                  "dt_vencimento_original,"+
			                                  "cd_turno,"+
			                                  "pr_juros,"+
			                                  "pr_multa,"+
			                                  "lg_protesto,"+
			                                  "lg_prioritaria,"+
			                                  "cd_forma_pagamento_preferencial,"+
			                                  "cd_conta_sacado,"+
			                                  "cd_usuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdContrato()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdContrato());
			if(objeto.getCdContaOrigem()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdContaOrigem());
			if(objeto.getCdDocumentoSaida()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdDocumentoSaida());
			if(objeto.getCdContaCarteira()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdContaCarteira());
			if(objeto.getCdConta()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdConta());
			if(objeto.getCdFrete()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdFrete());
			pstmt.setString(10,objeto.getNrDocumento());
			pstmt.setString(11,objeto.getIdContaReceber());
			pstmt.setInt(12,objeto.getNrParcela());
			pstmt.setString(13,objeto.getNrReferencia());
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdTipoDocumento());
			pstmt.setString(15,objeto.getDsHistorico());
			if(objeto.getDtVencimento()==null)
				pstmt.setNull(16, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(16,new Timestamp(objeto.getDtVencimento().getTimeInMillis()));
			if(objeto.getDtEmissao()==null)
				pstmt.setNull(17, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(17,new Timestamp(objeto.getDtEmissao().getTimeInMillis()));
			if(objeto.getDtRecebimento()==null)
				pstmt.setNull(18, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(18,new Timestamp(objeto.getDtRecebimento().getTimeInMillis()));
			if(objeto.getDtProrrogacao()==null)
				pstmt.setNull(19, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(19,new Timestamp(objeto.getDtProrrogacao().getTimeInMillis()));
			pstmt.setDouble(20,objeto.getVlConta());
			pstmt.setDouble(21,objeto.getVlAbatimento());
			pstmt.setDouble(22,objeto.getVlAcrescimo());
			pstmt.setDouble(23,objeto.getVlRecebido());
			pstmt.setInt(24,objeto.getStConta());
			pstmt.setInt(25,objeto.getTpFrequencia());
			pstmt.setInt(26,objeto.getQtParcelas());
			pstmt.setInt(27,objeto.getTpContaReceber());
			if(objeto.getCdNegociacao()==0)
				pstmt.setNull(28, Types.INTEGER);
			else
				pstmt.setInt(28,objeto.getCdNegociacao());
			pstmt.setString(29,objeto.getTxtObservacao());
			if(objeto.getCdPlanoPagamento()==0)
				pstmt.setNull(30, Types.INTEGER);
			else
				pstmt.setInt(30,objeto.getCdPlanoPagamento());
			if(objeto.getCdFormaPagamento()==0)
				pstmt.setNull(31, Types.INTEGER);
			else
				pstmt.setInt(31,objeto.getCdFormaPagamento());
			if(objeto.getDtDigitacao()==null)
				pstmt.setNull(32, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(32,new Timestamp(objeto.getDtDigitacao().getTimeInMillis()));
			if(objeto.getDtVencimentoOriginal()==null)
				pstmt.setNull(33, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(33,new Timestamp(objeto.getDtVencimentoOriginal().getTimeInMillis()));
			if(objeto.getCdTurno()==0)
				pstmt.setNull(34, Types.INTEGER);
			else
				pstmt.setInt(34,objeto.getCdTurno());
			pstmt.setDouble(35,objeto.getPrJuros());
			pstmt.setDouble(36,objeto.getPrMulta());
			pstmt.setInt(37,objeto.getLgProtesto());
			pstmt.setInt(38,objeto.getLgPrioritaria());
			if(objeto.getCdFormaPagamentoPreferencial()==0)
				pstmt.setNull(39, Types.INTEGER);
			else
				pstmt.setInt(39,objeto.getCdFormaPagamentoPreferencial());
			if(objeto.getCdContaSacado()==0)
				pstmt.setNull(40, Types.INTEGER);
			else
				pstmt.setInt(40,objeto.getCdContaSacado());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(41, Types.INTEGER);
			else
				pstmt.setInt(41,objeto.getCdUsuario());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContaReceber objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ContaReceber objeto, int cdContaReceberOld) {
		return update(objeto, cdContaReceberOld, null);
	}

	public static int update(ContaReceber objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ContaReceber objeto, int cdContaReceberOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_conta_receber SET cd_conta_receber=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_contrato=?,"+
												      		   "cd_conta_origem=?,"+
												      		   "cd_documento_saida=?,"+
												      		   "cd_conta_carteira=?,"+
												      		   "cd_conta=?,"+
												      		   "cd_frete=?,"+
												      		   "nr_documento=?,"+
												      		   "id_conta_receber=?,"+
												      		   "nr_parcela=?,"+
												      		   "nr_referencia=?,"+
												      		   "cd_tipo_documento=?,"+
												      		   "ds_historico=?,"+
												      		   "dt_vencimento=?,"+
												      		   "dt_emissao=?,"+
												      		   "dt_recebimento=?,"+
												      		   "dt_prorrogacao=?,"+
												      		   "vl_conta=?,"+
												      		   "vl_abatimento=?,"+
												      		   "vl_acrescimo=?,"+
												      		   "vl_recebido=?,"+
												      		   "st_conta=?,"+
												      		   "tp_frequencia=?,"+
												      		   "qt_parcelas=?,"+
												      		   "tp_conta_receber=?,"+
												      		   "cd_negociacao=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_plano_pagamento=?,"+
												      		   "cd_forma_pagamento=?,"+
												      		   "dt_digitacao=?,"+
												      		   "dt_vencimento_original=?,"+
												      		   "cd_turno=?,"+
												      		   "pr_juros=?,"+
												      		   "pr_multa=?,"+
												      		   "lg_protesto=?,"+
												      		   "lg_prioritaria=?,"+
												      		   "cd_forma_pagamento_preferencial=?,"+
												      		   "cd_conta_sacado=?,"+
												      		   "cd_usuario=? WHERE cd_conta_receber=?");
			pstmt.setInt(1,objeto.getCdContaReceber());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdPessoa());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdContrato()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdContrato());
			if(objeto.getCdContaOrigem()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdContaOrigem());
			if(objeto.getCdDocumentoSaida()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdDocumentoSaida());
			if(objeto.getCdContaCarteira()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdContaCarteira());
			if(objeto.getCdConta()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdConta());
			if(objeto.getCdFrete()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdFrete());
			pstmt.setString(10,objeto.getNrDocumento());
			pstmt.setString(11,objeto.getIdContaReceber());
			pstmt.setInt(12,objeto.getNrParcela());
			pstmt.setString(13,objeto.getNrReferencia());
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdTipoDocumento());
			pstmt.setString(15,objeto.getDsHistorico());
			if(objeto.getDtVencimento()==null)
				pstmt.setNull(16, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(16,new Timestamp(objeto.getDtVencimento().getTimeInMillis()));
			if(objeto.getDtEmissao()==null)
				pstmt.setNull(17, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(17,new Timestamp(objeto.getDtEmissao().getTimeInMillis()));
			if(objeto.getDtRecebimento()==null)
				pstmt.setNull(18, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(18,new Timestamp(objeto.getDtRecebimento().getTimeInMillis()));
			if(objeto.getDtProrrogacao()==null)
				pstmt.setNull(19, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(19,new Timestamp(objeto.getDtProrrogacao().getTimeInMillis()));
			pstmt.setDouble(20,objeto.getVlConta());
			pstmt.setDouble(21,objeto.getVlAbatimento());
			pstmt.setDouble(22,objeto.getVlAcrescimo());
			pstmt.setDouble(23,objeto.getVlRecebido());
			pstmt.setInt(24,objeto.getStConta());
			pstmt.setInt(25,objeto.getTpFrequencia());
			pstmt.setInt(26,objeto.getQtParcelas());
			pstmt.setInt(27,objeto.getTpContaReceber());
			if(objeto.getCdNegociacao()==0)
				pstmt.setNull(28, Types.INTEGER);
			else
				pstmt.setInt(28,objeto.getCdNegociacao());
			pstmt.setString(29,objeto.getTxtObservacao());
			if(objeto.getCdPlanoPagamento()==0)
				pstmt.setNull(30, Types.INTEGER);
			else
				pstmt.setInt(30,objeto.getCdPlanoPagamento());
			if(objeto.getCdFormaPagamento()==0)
				pstmt.setNull(31, Types.INTEGER);
			else
				pstmt.setInt(31,objeto.getCdFormaPagamento());
			if(objeto.getDtDigitacao()==null)
				pstmt.setNull(32, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(32,new Timestamp(objeto.getDtDigitacao().getTimeInMillis()));
			if(objeto.getDtVencimentoOriginal()==null)
				pstmt.setNull(33, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(33,new Timestamp(objeto.getDtVencimentoOriginal().getTimeInMillis()));
			if(objeto.getCdTurno()==0)
				pstmt.setNull(34, Types.INTEGER);
			else
				pstmt.setInt(34,objeto.getCdTurno());
			pstmt.setDouble(35,objeto.getPrJuros());
			pstmt.setDouble(36,objeto.getPrMulta());
			pstmt.setInt(37,objeto.getLgProtesto());
			pstmt.setInt(38,objeto.getLgPrioritaria());
			if(objeto.getCdFormaPagamentoPreferencial()==0)
				pstmt.setNull(39, Types.INTEGER);
			else
				pstmt.setInt(39,objeto.getCdFormaPagamentoPreferencial());
			if(objeto.getCdContaSacado()==0)
				pstmt.setNull(40, Types.INTEGER);
			else
				pstmt.setInt(40,objeto.getCdContaSacado());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(41, Types.INTEGER);
			else
				pstmt.setInt(41,objeto.getCdUsuario());
			pstmt.setInt(42, cdContaReceberOld!=0 ? cdContaReceberOld : objeto.getCdContaReceber());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContaReceber) {
		return delete(cdContaReceber, null);
	}

	public static int delete(int cdContaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_conta_receber WHERE cd_conta_receber=?");
			pstmt.setInt(1, cdContaReceber);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaReceber get(int cdContaReceber) {
		return get(cdContaReceber, null);
	}

	public static ContaReceber get(int cdContaReceber, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		
		boolean lgBaseAntiga = com.tivic.manager.util.Util.getConfManager().getProps().getProperty("STR_BASE_ANTIGA").equals("1");
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_receber WHERE cd_conta_receber=?");
			pstmt.setInt(1, cdContaReceber);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaReceber(rs.getInt("cd_conta_receber"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_contrato"),
						rs.getInt("cd_conta_origem"),
						rs.getInt("cd_documento_saida"),
						rs.getInt("cd_conta_carteira"),
						rs.getInt("cd_conta"),
						rs.getInt("cd_frete"),
						rs.getString("nr_documento"),
						rs.getString("id_conta_receber"),
						rs.getInt("nr_parcela"),
						rs.getString("nr_referencia"),
						rs.getInt("cd_tipo_documento"),
						rs.getString("ds_historico"),
						(rs.getTimestamp("dt_vencimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vencimento").getTime()),
						(rs.getTimestamp("dt_emissao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_emissao").getTime()),
						(rs.getTimestamp("dt_recebimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_recebimento").getTime()),
						(rs.getTimestamp("dt_prorrogacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_prorrogacao").getTime()),
						rs.getDouble("vl_conta"),
						rs.getDouble("vl_abatimento"),
						rs.getDouble("vl_acrescimo"),
						rs.getDouble("vl_recebido"),
						rs.getInt("st_conta"),
						rs.getInt("tp_frequencia"),
						rs.getInt("qt_parcelas"),
						rs.getInt("tp_conta_receber"),
						rs.getInt("cd_negociacao"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_plano_pagamento"),
						rs.getInt("cd_forma_pagamento"),
						(rs.getTimestamp("dt_digitacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_digitacao").getTime()),
						(rs.getTimestamp("dt_vencimento_original")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vencimento_original").getTime()),
						rs.getInt("cd_turno"),
						rs.getDouble("pr_juros"),
						rs.getDouble("pr_multa"),
						rs.getInt("lg_protesto"),
						(lgBaseAntiga ? 0 : rs.getInt("lg_prioritaria")),
						(lgBaseAntiga ? 0 : rs.getInt("cd_forma_pagamento_preferencial")),
						(lgBaseAntiga ? 0 : rs.getInt("cd_conta_sacado")),
						(lgBaseAntiga ? 0 : rs.getInt("cd_usuario")));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_receber");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ContaReceber> getList() {
		return getList(null);
	}

	public static ArrayList<ContaReceber> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ContaReceber> list = new ArrayList<ContaReceber>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ContaReceber obj = ContaReceberDAO.get(rsm.getInt("cd_conta_receber"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaReceberDAO.getList: " + e);
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
		return Search.find("SELECT * FROM adm_conta_receber", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}