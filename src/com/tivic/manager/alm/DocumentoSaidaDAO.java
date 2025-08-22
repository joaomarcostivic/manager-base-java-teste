package com.tivic.manager.alm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class DocumentoSaidaDAO{

	public static int insert(DocumentoSaida objeto) {
		return insert(objeto, null);
	}

	public static int insert(DocumentoSaida objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("alm_documento_saida", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDocumentoSaida(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_documento_saida (cd_documento_saida,"+
			                                  "cd_transportadora,"+
			                                  "cd_empresa,"+
			                                  "cd_cliente,"+
			                                  "dt_documento_saida,"+
			                                  "st_documento_saida,"+
			                                  "nr_documento_saida,"+
			                                  "tp_documento_saida,"+
			                                  "tp_saida,"+
			                                  "nr_conhecimento,"+
			                                  "vl_desconto,"+
			                                  "vl_acrescimo,"+
			                                  "dt_emissao,"+
			                                  "tp_frete,"+
			                                  "txt_mensagem,"+
			                                  "txt_observacao,"+
			                                  "nr_placa_veiculo,"+
			                                  "sg_placa_veiculo,"+
			                                  "nr_volumes,"+
			                                  "dt_saida_transportadora,"+
			                                  "ds_via_transporte,"+
			                                  "cd_natureza_operacao,"+
			                                  "txt_corpo_nota_fiscal,"+
			                                  "vl_peso_liquido,"+
			                                  "vl_peso_bruto,"+
			                                  "ds_especie_volumes,"+
			                                  "ds_marca_volumes,"+
			                                  "qt_volumes,"+
			                                  "tp_movimento_estoque,"+
			                                  "cd_vendedor,"+
			                                  "cd_moeda,"+
			                                  "cd_referencia_ecf,"+
			                                  "cd_solicitacao_material,"+
			                                  "cd_tipo_operacao,"+
			                                  "vl_total_documento,"+
			                                  "cd_contrato,"+
			                                  "vl_frete,"+
			                                  "vl_seguro,"+
			                                  "cd_digitador,"+
			                                  "cd_documento," +
			                                  "cd_conta," +
			                                  "cd_turno," +
			                                  "vl_total_itens," +
			                                  "nr_serie," +
			                                  "cd_viagem," +
			                                  "cd_documento_entrada_origem) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdTransportadora()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTransportadora());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdCliente()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCliente());
			if(objeto.getDtDocumentoSaida()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtDocumentoSaida().getTimeInMillis()));
			pstmt.setInt(6,objeto.getStDocumentoSaida());
			pstmt.setString(7,objeto.getNrDocumentoSaida());
			pstmt.setInt(8,objeto.getTpDocumentoSaida());
			pstmt.setInt(9,objeto.getTpSaida());
			pstmt.setString(10,objeto.getNrConhecimento());
			pstmt.setDouble(11,objeto.getVlDesconto());
			pstmt.setDouble(12,objeto.getVlAcrescimo());
			if(objeto.getDtEmissao()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtEmissao().getTimeInMillis()));
			pstmt.setInt(14,objeto.getTpFrete());
			pstmt.setString(15,objeto.getTxtMensagem());
			pstmt.setString(16,objeto.getTxtObservacao());
			pstmt.setString(17,objeto.getNrPlacaVeiculo());
			pstmt.setString(18,objeto.getSgPlacaVeiculo());
			pstmt.setString(19,objeto.getNrVolumes());
			if(objeto.getDtSaidaTransportadora()==null)
				pstmt.setNull(20, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(20,new Timestamp(objeto.getDtSaidaTransportadora().getTimeInMillis()));
			pstmt.setString(21,objeto.getDsViaTransporte());
			if(objeto.getCdNaturezaOperacao()==0)
				pstmt.setNull(22, Types.INTEGER);
			else
				pstmt.setInt(22,objeto.getCdNaturezaOperacao());
			pstmt.setString(23,objeto.getTxtCorpoNotaFiscal());
			pstmt.setDouble(24,objeto.getVlPesoLiquido());
			pstmt.setDouble(25,objeto.getVlPesoBruto());
			pstmt.setString(26,objeto.getDsEspecieVolumes());
			pstmt.setString(27,objeto.getDsMarcaVolumes());
			pstmt.setDouble(28,objeto.getQtVolumes());
			pstmt.setInt(29,objeto.getTpMovimentoEstoque());
			if(objeto.getCdVendedor()==0)
				pstmt.setNull(30, Types.INTEGER);
			else
				pstmt.setInt(30,objeto.getCdVendedor());
			if(objeto.getCdMoeda()==0)
				pstmt.setNull(31, Types.INTEGER);
			else
				pstmt.setInt(31,objeto.getCdMoeda());
			if(objeto.getCdReferenciaEcf()==0)
				pstmt.setNull(32, Types.INTEGER);
			else
				pstmt.setInt(32,objeto.getCdReferenciaEcf());
			if(objeto.getCdSolicitacaoMaterial()==0)
				pstmt.setNull(33, Types.INTEGER);
			else
				pstmt.setInt(33,objeto.getCdSolicitacaoMaterial());
			if(objeto.getCdTipoOperacao()==0)
				pstmt.setNull(34, Types.INTEGER);
			else
				pstmt.setInt(34,objeto.getCdTipoOperacao());
			pstmt.setDouble(35,objeto.getVlTotalDocumento());
			if(objeto.getCdContrato()==0)
				pstmt.setNull(36, Types.INTEGER);
			else
				pstmt.setInt(36,objeto.getCdContrato());
			pstmt.setDouble(37,objeto.getVlFrete());
			pstmt.setDouble(38,objeto.getVlSeguro());
			if(objeto.getCdDigitador()==0)
				pstmt.setNull(39, Types.INTEGER);
			else
				pstmt.setInt(39,objeto.getCdDigitador());
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(40, Types.INTEGER);
			else
				pstmt.setInt(40,objeto.getCdDocumento());
			if(objeto.getCdConta()==0)
				pstmt.setNull(41, Types.INTEGER);
			else
				pstmt.setInt(41,objeto.getCdConta());
			if(objeto.getCdTurno()==0)
				pstmt.setNull(42, Types.INTEGER);
			else
				pstmt.setInt(42,objeto.getCdTurno());
			pstmt.setDouble(43,objeto.getVlTotalItens());
			pstmt.setInt(44,objeto.getNrSerie());
			if(objeto.getCdViagem()==0)
				pstmt.setNull(45, Types.INTEGER);
			else
				pstmt.setInt(45,objeto.getCdViagem());
			if(objeto.getCdDocumentoEntradaOrigem()==0)
				pstmt.setNull(46, Types.INTEGER);
			else
				pstmt.setInt(46,objeto.getCdDocumentoEntradaOrigem());
			pstmt.executeUpdate();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(DocumentoSaida objeto) {
		return update(objeto, 0, null);
	}

	public static int update(DocumentoSaida objeto, int cdDocumentoSaidaOld) {
		return update(objeto, cdDocumentoSaidaOld, null);
	}

	public static int update(DocumentoSaida objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(DocumentoSaida objeto, int cdDocumentoSaidaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_documento_saida SET cd_documento_saida=?,"+
												      		   "cd_transportadora=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_cliente=?,"+
												      		   "dt_documento_saida=?,"+
												      		   "st_documento_saida=?,"+
												      		   "nr_documento_saida=?,"+
												      		   "tp_documento_saida=?,"+
												      		   "tp_saida=?,"+
												      		   "nr_conhecimento=?,"+
												      		   "vl_desconto=?,"+
												      		   "vl_acrescimo=?,"+
												      		   "dt_emissao=?,"+
												      		   "tp_frete=?,"+
												      		   "txt_mensagem=?,"+
												      		   "txt_observacao=?,"+
												      		   "nr_placa_veiculo=?,"+
												      		   "sg_placa_veiculo=?,"+
												      		   "nr_volumes=?,"+
												      		   "dt_saida_transportadora=?,"+
												      		   "ds_via_transporte=?,"+
												      		   "cd_natureza_operacao=?,"+
												      		   "txt_corpo_nota_fiscal=?,"+
												      		   "vl_peso_liquido=?,"+
												      		   "vl_peso_bruto=?,"+
												      		   "ds_especie_volumes=?,"+
												      		   "ds_marca_volumes=?,"+
												      		   "qt_volumes=?,"+
												      		   "tp_movimento_estoque=?,"+
												      		   "cd_vendedor=?,"+
												      		   "cd_moeda=?,"+
												      		   "cd_referencia_ecf=?,"+
												      		   "cd_solicitacao_material=?,"+
												      		   "cd_tipo_operacao=?,"+
												      		   "vl_total_documento=?,"+
												      		   "cd_contrato=?,"+
												      		   "vl_frete=?,"+
												      		   "vl_seguro=?,"+
												      		   "cd_digitador=?,"+
												      		   "cd_documento=?," +
												      		   "cd_conta=?," +
												      		   "cd_turno=?," +
												      		   "vl_total_itens=?," +
												      		   "nr_serie=?," +
												      		   "cd_viagem=?,"+
												      		   "cd_documento_entrada_origem=? WHERE cd_documento_saida=?");
			pstmt.setInt(1,objeto.getCdDocumentoSaida());
			if(objeto.getCdTransportadora()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTransportadora());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdCliente()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdCliente());
			if(objeto.getDtDocumentoSaida()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtDocumentoSaida().getTimeInMillis()));
			pstmt.setInt(6,objeto.getStDocumentoSaida());
			pstmt.setString(7,objeto.getNrDocumentoSaida());
			pstmt.setInt(8,objeto.getTpDocumentoSaida());
			pstmt.setInt(9,objeto.getTpSaida());
			pstmt.setString(10,objeto.getNrConhecimento());
			pstmt.setDouble(11,objeto.getVlDesconto());
			pstmt.setDouble(12,objeto.getVlAcrescimo());
			if(objeto.getDtEmissao()==null)
				pstmt.setNull(13, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(13,new Timestamp(objeto.getDtEmissao().getTimeInMillis()));
			pstmt.setInt(14,objeto.getTpFrete());
			pstmt.setString(15,objeto.getTxtMensagem());
			pstmt.setString(16,objeto.getTxtObservacao());
			pstmt.setString(17,objeto.getNrPlacaVeiculo());
			pstmt.setString(18,objeto.getSgPlacaVeiculo());
			pstmt.setString(19,objeto.getNrVolumes());
			if(objeto.getDtSaidaTransportadora()==null)
				pstmt.setNull(20, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(20,new Timestamp(objeto.getDtSaidaTransportadora().getTimeInMillis()));
			pstmt.setString(21,objeto.getDsViaTransporte());
			if(objeto.getCdNaturezaOperacao()==0)
				pstmt.setNull(22, Types.INTEGER);
			else
				pstmt.setInt(22,objeto.getCdNaturezaOperacao());
			pstmt.setString(23,objeto.getTxtCorpoNotaFiscal());
			pstmt.setDouble(24,objeto.getVlPesoLiquido());
			pstmt.setDouble(25,objeto.getVlPesoBruto());
			pstmt.setString(26,objeto.getDsEspecieVolumes());
			pstmt.setString(27,objeto.getDsMarcaVolumes());
			pstmt.setDouble(28,objeto.getQtVolumes());
			pstmt.setInt(29,objeto.getTpMovimentoEstoque());
			if(objeto.getCdVendedor()==0)
				pstmt.setNull(30, Types.INTEGER);
			else
				pstmt.setInt(30,objeto.getCdVendedor());
			if(objeto.getCdMoeda()==0)
				pstmt.setNull(31, Types.INTEGER);
			else
				pstmt.setInt(31,objeto.getCdMoeda());
			if(objeto.getCdReferenciaEcf()==0)
				pstmt.setNull(32, Types.INTEGER);
			else
				pstmt.setInt(32,objeto.getCdReferenciaEcf());
			if(objeto.getCdSolicitacaoMaterial()==0)
				pstmt.setNull(33, Types.INTEGER);
			else
				pstmt.setInt(33,objeto.getCdSolicitacaoMaterial());
			if(objeto.getCdTipoOperacao()==0)
				pstmt.setNull(34, Types.INTEGER);
			else
				pstmt.setInt(34,objeto.getCdTipoOperacao());
			pstmt.setDouble(35,objeto.getVlTotalDocumento());
			if(objeto.getCdContrato()==0)
				pstmt.setNull(36, Types.INTEGER);
			else
				pstmt.setInt(36,objeto.getCdContrato());
			pstmt.setDouble(37,objeto.getVlFrete());
			pstmt.setDouble(38,objeto.getVlSeguro());
			if(objeto.getCdDigitador()==0)
				pstmt.setNull(39, Types.INTEGER);
			else
				pstmt.setInt(39,objeto.getCdDigitador());
			if(objeto.getCdDocumento()==0)
				pstmt.setNull(40, Types.INTEGER);
			else
				pstmt.setInt(40,objeto.getCdDocumento());
			if(objeto.getCdConta()==0)
				pstmt.setNull(41, Types.INTEGER);
			else
				pstmt.setInt(41,objeto.getCdConta());
			if(objeto.getCdTurno()==0)
				pstmt.setNull(42, Types.INTEGER);
			else
				pstmt.setInt(42,objeto.getCdTurno());
			pstmt.setDouble(43,objeto.getVlTotalItens());
			pstmt.setInt(44,objeto.getNrSerie());
			if(objeto.getCdViagem()==0)
				pstmt.setNull(45, Types.INTEGER);
			else
				pstmt.setInt(45,objeto.getCdViagem());
			if(objeto.getCdDocumentoEntradaOrigem()==0)
				pstmt.setNull(46, Types.INTEGER);
			else
				pstmt.setInt(46,objeto.getCdDocumentoEntradaOrigem());
			pstmt.setInt(47, cdDocumentoSaidaOld!=0 ? cdDocumentoSaidaOld : objeto.getCdDocumentoSaida());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDocumentoSaida) {
		return delete(cdDocumentoSaida, null);
	}

	public static int delete(int cdDocumentoSaida, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_documento_saida WHERE cd_documento_saida=?");
			pstmt.setInt(1, cdDocumentoSaida);
			pstmt.executeUpdate();
			return 1;
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

	public static DocumentoSaida get(int cdDocumentoSaida) {
		return get(cdDocumentoSaida, null);
	}

	public static DocumentoSaida get(int cdDocumentoSaida, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_documento_saida WHERE cd_documento_saida=?");
			pstmt.setInt(1, cdDocumentoSaida);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DocumentoSaida(rs.getInt("cd_documento_saida"),
						rs.getInt("cd_transportadora"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_cliente"),
						(rs.getTimestamp("dt_documento_saida")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_documento_saida").getTime()),
						rs.getInt("st_documento_saida"),
						rs.getString("nr_documento_saida"),
						rs.getInt("tp_documento_saida"),
						rs.getInt("tp_saida"),
						rs.getString("nr_conhecimento"),
						rs.getFloat("vl_desconto"),
						rs.getFloat("vl_acrescimo"),
						(rs.getTimestamp("dt_emissao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_emissao").getTime()),
						rs.getInt("tp_frete"),
						rs.getString("txt_mensagem"),
						rs.getString("txt_observacao"),
						rs.getString("nr_placa_veiculo"),
						rs.getString("sg_placa_veiculo"),
						rs.getString("nr_volumes"),
						(rs.getTimestamp("dt_saida_transportadora")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_saida_transportadora").getTime()),
						rs.getString("ds_via_transporte"),
						rs.getInt("cd_natureza_operacao"),
						rs.getString("txt_corpo_nota_fiscal"),
						rs.getFloat("vl_peso_liquido"),
						rs.getFloat("vl_peso_bruto"),
						rs.getString("ds_especie_volumes"),
						rs.getString("ds_marca_volumes"),
						rs.getFloat("qt_volumes"),
						rs.getInt("tp_movimento_estoque"),
						rs.getInt("cd_vendedor"),
						rs.getInt("cd_moeda"),
						rs.getInt("cd_referencia_ecf"),
						rs.getInt("cd_solicitacao_material"),
						rs.getInt("cd_tipo_operacao"),
						rs.getFloat("vl_total_documento"),
						rs.getInt("cd_contrato"),
						rs.getFloat("vl_frete"),
						rs.getFloat("vl_seguro"),
						rs.getInt("cd_digitador"),
						rs.getInt("cd_documento"),
						rs.getInt("cd_conta"),
						rs.getInt("cd_turno"),
						rs.getFloat("vl_total_itens"),
						rs.getInt("nr_serie"),
						rs.getInt("cd_viagem"),
						rs.getInt("cd_documento_entrada_origem"));
			}
			else{
				return null;
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoSaidaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_documento_saida");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
		return Search.find("SELECT * FROM alm_documento_saida", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
