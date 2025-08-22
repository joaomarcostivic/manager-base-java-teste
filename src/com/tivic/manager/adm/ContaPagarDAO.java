package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class ContaPagarDAO{

	public static int insert(ContaPagar objeto) {
		return insert(objeto, null);
	}

	public static int insert(ContaPagar objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_conta_pagar", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdContaPagar(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_conta_pagar (cd_conta_pagar,"+
			                                  "cd_contrato,"+
			                                  "cd_pessoa,"+
			                                  "cd_empresa,"+
			                                  "cd_conta_origem,"+
			                                  "cd_documento_entrada,"+
			                                  "cd_conta,"+
			                                  "cd_conta_bancaria,"+
			                                  "dt_vencimento,"+
			                                  "dt_emissao,"+
			                                  "dt_pagamento,"+
			                                  "dt_autorizacao,"+
			                                  "nr_documento,"+
			                                  "nr_referencia,"+
			                                  "nr_parcela,"+
			                                  "cd_tipo_documento,"+
			                                  "vl_conta,"+
			                                  "vl_abatimento,"+
			                                  "vl_acrescimo,"+
			                                  "vl_pago,"+
			                                  "ds_historico,"+
			                                  "st_conta,"+
			                                  "lg_autorizado,"+
			                                  "tp_frequencia,"+
			                                  "qt_parcelas,"+
			                                  "vl_base_autorizacao,"+
			                                  "cd_viagem,"+
			                                  "cd_manutencao,"+
			                                  "txt_observacao,"+
			                                  "dt_digitacao,"+
			                                  "dt_vencimento_original,"+
			                                  "cd_turno,"+
			                                  "cd_arquivo,"+
			                                  "lg_prioritaria,"+
			                                  "cd_forma_pagamento_preferencial,"+
			                                  "nr_codigo_barras,"+
			                                  "cd_conta_favorecido,"+
			                                  "cd_usuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdContrato()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdContrato());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPessoa());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdEmpresa());
			if(objeto.getCdContaOrigem()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdContaOrigem());
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdDocumentoEntrada());
			pstmt.setInt(7,objeto.getCdConta());
			if(objeto.getCdContaBancaria()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdContaBancaria());
			if(objeto.getDtVencimento()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtVencimento().getTimeInMillis()));
			if(objeto.getDtEmissao()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtEmissao().getTimeInMillis()));
			if(objeto.getDtPagamento()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtPagamento().getTimeInMillis()));
			if(objeto.getDtAutorizacao()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtAutorizacao().getTimeInMillis()));
			pstmt.setString(13,objeto.getNrDocumento());
			pstmt.setString(14,objeto.getNrReferencia());
			pstmt.setInt(15,objeto.getNrParcela());
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdTipoDocumento());
			pstmt.setDouble(17,objeto.getVlConta());
			pstmt.setDouble(18,objeto.getVlAbatimento());
			pstmt.setDouble(19,objeto.getVlAcrescimo());
			pstmt.setDouble(20,objeto.getVlPago());
			pstmt.setString(21,objeto.getDsHistorico());
			pstmt.setInt(22,objeto.getStConta());
			pstmt.setInt(23,objeto.getLgAutorizado());
			pstmt.setInt(24,objeto.getTpFrequencia());
			pstmt.setInt(25,objeto.getQtParcelas());
			pstmt.setDouble(26,objeto.getVlBaseAutorizacao());
			if(objeto.getCdViagem()==0)
				pstmt.setNull(27, Types.INTEGER);
			else
				pstmt.setInt(27,objeto.getCdViagem());
			if(objeto.getCdManutencao()==0)
				pstmt.setNull(28, Types.INTEGER);
			else
				pstmt.setInt(28,objeto.getCdManutencao());
			pstmt.setString(29,objeto.getTxtObservacao());
			if(objeto.getDtDigitacao()==null)
				pstmt.setNull(30, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(30,new Timestamp(objeto.getDtDigitacao().getTimeInMillis()));
			if(objeto.getDtVencimentoOriginal()==null)
				pstmt.setNull(31, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(31,new Timestamp(objeto.getDtVencimentoOriginal().getTimeInMillis()));
			if(objeto.getCdTurno()==0)
				pstmt.setNull(32, Types.INTEGER);
			else
				pstmt.setInt(32,objeto.getCdTurno());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(33, Types.INTEGER);
			else
				pstmt.setInt(33,objeto.getCdArquivo());
			pstmt.setInt(34,objeto.getLgPrioritaria());
			if(objeto.getCdFormaPagamentoPreferencial()==0)
				pstmt.setNull(35, Types.INTEGER);
			else
				pstmt.setInt(35,objeto.getCdFormaPagamentoPreferencial());
			pstmt.setString(36,objeto.getNrCodigoBarras());
			if(objeto.getCdContaFavorecido()==0)
				pstmt.setNull(37, Types.INTEGER);
			else
				pstmt.setInt(37,objeto.getCdContaFavorecido());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(38, Types.INTEGER);
			else
				pstmt.setInt(38,objeto.getCdUsuario());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ContaPagar objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ContaPagar objeto, int cdContaPagarOld) {
		return update(objeto, cdContaPagarOld, null);
	}

	public static int update(ContaPagar objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ContaPagar objeto, int cdContaPagarOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_conta_pagar SET cd_conta_pagar=?,"+
												      		   "cd_contrato=?,"+
												      		   "cd_pessoa=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_conta_origem=?,"+
												      		   "cd_documento_entrada=?,"+
												      		   "cd_conta=?,"+
												      		   "cd_conta_bancaria=?,"+
												      		   "dt_vencimento=?,"+
												      		   "dt_emissao=?,"+
												      		   "dt_pagamento=?,"+
												      		   "dt_autorizacao=?,"+
												      		   "nr_documento=?,"+
												      		   "nr_referencia=?,"+
												      		   "nr_parcela=?,"+
												      		   "cd_tipo_documento=?,"+
												      		   "vl_conta=?,"+
												      		   "vl_abatimento=?,"+
												      		   "vl_acrescimo=?,"+
												      		   "vl_pago=?,"+
												      		   "ds_historico=?,"+
												      		   "st_conta=?,"+
												      		   "lg_autorizado=?,"+
												      		   "tp_frequencia=?,"+
												      		   "qt_parcelas=?,"+
												      		   "vl_base_autorizacao=?,"+
												      		   "cd_viagem=?,"+
												      		   "cd_manutencao=?,"+
												      		   "txt_observacao=?,"+
												      		   "dt_digitacao=?,"+
												      		   "dt_vencimento_original=?,"+
												      		   "cd_turno=?,"+
												      		   "cd_arquivo=?,"+
												      		   "lg_prioritaria=?,"+
												      		   "cd_forma_pagamento_preferencial=?,"+
												      		   "nr_codigo_barras=?,"+
												      		   "cd_conta_favorecido=?,"+
												      		   "cd_usuario=? WHERE cd_conta_pagar=?");
			pstmt.setInt(1,objeto.getCdContaPagar());
			if(objeto.getCdContrato()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdContrato());
			if(objeto.getCdPessoa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdPessoa());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdEmpresa());
			if(objeto.getCdContaOrigem()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdContaOrigem());
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdDocumentoEntrada());
			pstmt.setInt(7,objeto.getCdConta());
			if(objeto.getCdContaBancaria()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdContaBancaria());
			if(objeto.getDtVencimento()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtVencimento().getTimeInMillis()));
			if(objeto.getDtEmissao()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtEmissao().getTimeInMillis()));
			if(objeto.getDtPagamento()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtPagamento().getTimeInMillis()));
			if(objeto.getDtAutorizacao()==null)
				pstmt.setNull(12, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(12,new Timestamp(objeto.getDtAutorizacao().getTimeInMillis()));
			pstmt.setString(13,objeto.getNrDocumento());
			pstmt.setString(14,objeto.getNrReferencia());
			pstmt.setInt(15,objeto.getNrParcela());
			if(objeto.getCdTipoDocumento()==0)
				pstmt.setNull(16, Types.INTEGER);
			else
				pstmt.setInt(16,objeto.getCdTipoDocumento());
			pstmt.setDouble(17,objeto.getVlConta());
			pstmt.setDouble(18,objeto.getVlAbatimento());
			pstmt.setDouble(19,objeto.getVlAcrescimo());
			pstmt.setDouble(20,objeto.getVlPago());
			pstmt.setString(21,objeto.getDsHistorico());
			pstmt.setInt(22,objeto.getStConta());
			pstmt.setInt(23,objeto.getLgAutorizado());
			pstmt.setInt(24,objeto.getTpFrequencia());
			pstmt.setInt(25,objeto.getQtParcelas());
			pstmt.setDouble(26,objeto.getVlBaseAutorizacao());
			if(objeto.getCdViagem()==0)
				pstmt.setNull(27, Types.INTEGER);
			else
				pstmt.setInt(27,objeto.getCdViagem());
			if(objeto.getCdManutencao()==0)
				pstmt.setNull(28, Types.INTEGER);
			else
				pstmt.setInt(28,objeto.getCdManutencao());
			pstmt.setString(29,objeto.getTxtObservacao());
			if(objeto.getDtDigitacao()==null)
				pstmt.setNull(30, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(30,new Timestamp(objeto.getDtDigitacao().getTimeInMillis()));
			if(objeto.getDtVencimentoOriginal()==null)
				pstmt.setNull(31, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(31,new Timestamp(objeto.getDtVencimentoOriginal().getTimeInMillis()));
			if(objeto.getCdTurno()==0)
				pstmt.setNull(32, Types.INTEGER);
			else
				pstmt.setInt(32,objeto.getCdTurno());
			if(objeto.getCdArquivo()==0)
				pstmt.setNull(33, Types.INTEGER);
			else
				pstmt.setInt(33,objeto.getCdArquivo());
			pstmt.setInt(34,objeto.getLgPrioritaria());
			if(objeto.getCdFormaPagamentoPreferencial()==0)
				pstmt.setNull(35, Types.INTEGER);
			else
				pstmt.setInt(35,objeto.getCdFormaPagamentoPreferencial());
			pstmt.setString(36,objeto.getNrCodigoBarras());
			if(objeto.getCdContaFavorecido()==0)
				pstmt.setNull(37, Types.INTEGER);
			else
				pstmt.setInt(37,objeto.getCdContaFavorecido());
			if(objeto.getCdUsuario()==0)
				pstmt.setNull(38, Types.INTEGER);
			else
				pstmt.setInt(38,objeto.getCdUsuario());
			pstmt.setInt(39, cdContaPagarOld!=0 ? cdContaPagarOld : objeto.getCdContaPagar());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdContaPagar) {
		return delete(cdContaPagar, null);
	}

	public static int delete(int cdContaPagar, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_conta_pagar WHERE cd_conta_pagar=?");
			pstmt.setInt(1, cdContaPagar);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ContaPagar get(int cdContaPagar) {
		return get(cdContaPagar, null);
	}

	public static ContaPagar get(int cdContaPagar, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_pagar WHERE cd_conta_pagar=?");
			pstmt.setInt(1, cdContaPagar);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ContaPagar(rs.getInt("cd_conta_pagar"),
						rs.getInt("cd_contrato"),
						rs.getInt("cd_pessoa"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_conta_origem"),
						rs.getInt("cd_documento_entrada"),
						rs.getInt("cd_conta"),
						rs.getInt("cd_conta_bancaria"),
						(rs.getTimestamp("dt_vencimento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vencimento").getTime()),
						(rs.getTimestamp("dt_emissao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_emissao").getTime()),
						(rs.getTimestamp("dt_pagamento")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_pagamento").getTime()),
						(rs.getTimestamp("dt_autorizacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_autorizacao").getTime()),
						rs.getString("nr_documento"),
						rs.getString("nr_referencia"),
						rs.getInt("nr_parcela"),
						rs.getInt("cd_tipo_documento"),
						rs.getDouble("vl_conta"),
						rs.getDouble("vl_abatimento"),
						rs.getDouble("vl_acrescimo"),
						rs.getDouble("vl_pago"),
						rs.getString("ds_historico"),
						rs.getInt("st_conta"),
						rs.getInt("lg_autorizado"),
						rs.getInt("tp_frequencia"),
						rs.getInt("qt_parcelas"),
						rs.getDouble("vl_base_autorizacao"),
						rs.getInt("cd_viagem"),
						rs.getInt("cd_manutencao"),
						rs.getString("txt_observacao"),
						(rs.getTimestamp("dt_digitacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_digitacao").getTime()),
						(rs.getTimestamp("dt_vencimento_original")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_vencimento_original").getTime()),
						rs.getInt("cd_turno"),
						rs.getInt("cd_arquivo"),
						rs.getInt("lg_prioritaria"),
						rs.getInt("cd_forma_pagamento_preferencial"),
						rs.getString("nr_codigo_barras"),
						rs.getInt("cd_conta_favorecido"),
						rs.getInt("cd_usuario"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_conta_pagar");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<ContaPagar> getList() {
		return getList(null);
	}

	public static ArrayList<ContaPagar> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<ContaPagar> list = new ArrayList<ContaPagar>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				ContaPagar obj = ContaPagarDAO.get(rsm.getInt("cd_conta_pagar"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ContaPagarDAO.getList: " + e);
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
		return Search.find("SELECT * FROM adm_conta_pagar", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}