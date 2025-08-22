package com.tivic.manager.fsc;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

public class NotaFiscalDAO{

	public static int insert(NotaFiscal objeto) {
		return insert(objeto, null);
	}

	public static int insert(NotaFiscal objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("fsc_nota_fiscal", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdNotaFiscal(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO fsc_nota_fiscal (cd_nota_fiscal,"+
			                                  "cd_empresa,"+
			                                  "cd_endereco_retirada,"+
			                                  "cd_natureza_operacao,"+
			                                  "cd_cidade,"+
			                                  "cd_destinatario,"+
			                                  "cd_endereco_destinatario,"+
			                                  "cd_endereco_entrega,"+
			                                  "cd_lote,"+
			                                  "tp_modelo,"+
			                                  "nr_serie,"+
			                                  "nr_nota_fiscal,"+
			                                  "st_nota_fiscal,"+
			                                  "dt_emissao,"+
			                                  "tp_movimento,"+
			                                  "dt_movimentacao,"+
			                                  "tp_pagamento,"+
			                                  "tp_emissao,"+
			                                  "tp_finalidade,"+
			                                  "tp_danfe,"+
			                                  "vl_total_produto,"+
			                                  "vl_seguro,"+
			                                  "vl_outras_despesas,"+
			                                  "vl_total_nota,"+
			                                  "tp_modalidade_frete,"+
			                                  "txt_observacao,"+
			                                  "txt_informacao_fisco,"+
			                                  "lg_danfe_impresso,"+
			                                  "nr_chave_acesso,"+
			                                  "nr_protocolo_autorizacao,"+
			                                  "dt_autorizacao,"+
			                                  "cd_transportador,"+
			                                  "cd_natureza_operacao_frete,"+
			                                  "vl_frete,"+
			                                  "vl_frete_base_icms,"+
			                                  "vl_frete_icms_retido,"+
			                                  "nr_recebimento,"+
			                                  "nr_dv,"+
			                                  "nr_placa,"+
			                                  "qt_volume,"+
			                                  "ds_especie,"+
			                                  "ds_marca,"+
			                                  "ds_numeracao,"+
			                                  "vl_peso_bruto,"+
			                                  "vl_peso_liquido,"+
			                                  "cd_veiculo," +
			                                  "sg_uf_veiculo," +
			                                  "nr_rntc, " +
			                                  "cd_motivo_cancelamento," +
			                                  "txt_xml," +
			                                  "pr_desconto,"+
			                                  "lg_consumidor_final,"+
			                                  "tp_venda_presenca,"+
			                                  "nr_chave_acesso_referencia) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdEnderecoRetirada()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEnderecoRetirada());
			if(objeto.getCdNaturezaOperacao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdNaturezaOperacao());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdCidade());
			if(objeto.getCdDestinatario()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdDestinatario());
			if(objeto.getCdEnderecoDestinatario()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdEnderecoDestinatario());
			if(objeto.getCdEnderecoEntrega()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdEnderecoEntrega());
			pstmt.setInt(9,objeto.getCdLote());
			pstmt.setInt(10,objeto.getTpModelo());
			pstmt.setString(11,objeto.getNrSerie());
			pstmt.setString(12,objeto.getNrNotaFiscal());
			pstmt.setInt(13,objeto.getStNotaFiscal());
			if(objeto.getDtEmissao()==null)
				pstmt.setNull(14, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(14,new Timestamp(objeto.getDtEmissao().getTimeInMillis()));
			pstmt.setInt(15,objeto.getTpMovimento());
			if(objeto.getDtMovimentacao()==null)
				pstmt.setNull(16, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(16,new Timestamp(objeto.getDtMovimentacao().getTimeInMillis()));
			pstmt.setInt(17,objeto.getTpPagamento());
			pstmt.setInt(18,objeto.getTpEmissao());
			pstmt.setInt(19,objeto.getTpFinalidade());
			pstmt.setInt(20,objeto.getTpDanfe());
			pstmt.setFloat(21,objeto.getVlTotalProduto());
			pstmt.setFloat(22,objeto.getVlSeguro());
			pstmt.setFloat(23,objeto.getVlOutrasDespesas());
			pstmt.setFloat(24,objeto.getVlTotalNota());
			pstmt.setInt(25,objeto.getTpModalidadeFrete());
			pstmt.setString(26,objeto.getTxtObservacao());
			pstmt.setString(27,objeto.getTxtInformacaoFisco());
			pstmt.setInt(28,objeto.getLgDanfeImpresso());
			pstmt.setString(29,objeto.getNrChaveAcesso());
			pstmt.setString(30,objeto.getNrProtocoloAutorizacao());
			if(objeto.getDtAutorizacao()==null)
				pstmt.setNull(31, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(31,new Timestamp(objeto.getDtAutorizacao().getTimeInMillis()));
			if(objeto.getCdTransportador()==0)
				pstmt.setNull(32, Types.INTEGER);
			else
				pstmt.setInt(32,objeto.getCdTransportador());
			if(objeto.getCdNaturezaOperacaoFrete()==0)
				pstmt.setNull(33, Types.INTEGER);
			else
				pstmt.setInt(33,objeto.getCdNaturezaOperacaoFrete());
			pstmt.setFloat(34,objeto.getVlFrete());
			pstmt.setFloat(35,objeto.getVlFreteBaseIcms());
			pstmt.setFloat(36,objeto.getVlFreteIcmsRetido());
			pstmt.setString(37,objeto.getNrRecebimento());
			pstmt.setInt(38,objeto.getNrDv());
			pstmt.setString(39,objeto.getNrPlaca());
			pstmt.setString(40,objeto.getQtVolume());
			pstmt.setString(41,objeto.getDsEspecie());
			pstmt.setString(42,objeto.getDsMarca());
			pstmt.setString(43,objeto.getDsNumeracao());
			pstmt.setFloat(44,objeto.getVlPesoBruto());
			pstmt.setFloat(45,objeto.getVlPesoLiquido());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(46, Types.INTEGER);
			else
				pstmt.setInt(46,objeto.getCdVeiculo());
			pstmt.setString(47,objeto.getSgUfVeiculo());
			pstmt.setString(48,objeto.getNrRntc());
			if(objeto.getCdMotivoCancelamento()==0)
				pstmt.setNull(49, Types.INTEGER);
			else
				pstmt.setInt(49,objeto.getCdMotivoCancelamento());
			pstmt.setString(50,objeto.getTxtXml());
			pstmt.setFloat(51, objeto.getPrDesconto());
			pstmt.setInt(52, objeto.getLgConsumidorFinal());
			pstmt.setInt(53, objeto.getTpVendaPresenca());
			pstmt.setString(54, objeto.getNrChaveAcessoReferencia());
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

	public static int update(NotaFiscal objeto) {
		return update(objeto, 0, null);
	}

	public static int update(NotaFiscal objeto, int cdNotaFiscalOld) {
		return update(objeto, cdNotaFiscalOld, null);
	}

	public static int update(NotaFiscal objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(NotaFiscal objeto, int cdNotaFiscalOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE fsc_nota_fiscal SET cd_nota_fiscal=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_endereco_retirada=?,"+
												      		   "cd_natureza_operacao=?,"+
												      		   "cd_cidade=?,"+
												      		   "cd_destinatario=?,"+
												      		   "cd_endereco_destinatario=?,"+
												      		   "cd_endereco_entrega=?,"+
												      		   "cd_lote=?,"+
												      		   "tp_modelo=?,"+
												      		   "nr_serie=?,"+
												      		   "nr_nota_fiscal=?,"+
												      		   "st_nota_fiscal=?,"+
												      		   "dt_emissao=?,"+
												      		   "tp_movimento=?,"+
												      		   "dt_movimentacao=?,"+
												      		   "tp_pagamento=?,"+
												      		   "tp_emissao=?,"+
												      		   "tp_finalidade=?,"+
												      		   "tp_danfe=?,"+
												      		   "vl_total_produto=?,"+
												      		   "vl_seguro=?,"+
												      		   "vl_outras_despesas=?,"+
												      		   "vl_total_nota=?,"+
												      		   "tp_modalidade_frete=?,"+
												      		   "txt_observacao=?,"+
												      		   "txt_informacao_fisco=?,"+
												      		   "lg_danfe_impresso=?,"+
												      		   "nr_chave_acesso=?,"+
												      		   "nr_protocolo_autorizacao=?,"+
												      		   "dt_autorizacao=?,"+
												      		   "cd_transportador=?,"+
												      		   "cd_natureza_operacao_frete=?,"+
												      		   "vl_frete=?,"+
												      		   "vl_frete_base_icms=?,"+
												      		   "vl_frete_icms_retido=?,"+
												      		   "nr_recebimento=?,"+
												      		   "nr_dv=?,"+
												      		   "nr_placa=?,"+
												      		   "qt_volume=?,"+
												      		   "ds_especie=?,"+
												      		   "ds_marca=?,"+
												      		   "ds_numeracao=?,"+
												      		   "vl_peso_bruto=?,"+
												      		   "vl_peso_liquido=?,"+
												      		   "cd_veiculo=?," +
												      		   "sg_uf_veiculo=?," +
												      		   "nr_rntc=?, " +
												      		   "cd_motivo_cancelamento=?," +
												      		   "txt_xml=?," +
												      		   "pr_desconto=?,"+
												      		   "lg_consumidor_final=?, " +
												      		   "tp_venda_presenca=?,"+
												      		   "nr_chave_acesso_referencia=? WHERE cd_nota_fiscal=?");
			pstmt.setInt(1,objeto.getCdNotaFiscal());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdEnderecoRetirada()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEnderecoRetirada());
			if(objeto.getCdNaturezaOperacao()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdNaturezaOperacao());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdCidade());
			if(objeto.getCdDestinatario()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdDestinatario());
			if(objeto.getCdEnderecoDestinatario()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdEnderecoDestinatario());
			if(objeto.getCdEnderecoEntrega()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdEnderecoEntrega());
			pstmt.setInt(9,objeto.getCdLote());
			pstmt.setInt(10,objeto.getTpModelo());
			pstmt.setString(11,objeto.getNrSerie());
			pstmt.setString(12,objeto.getNrNotaFiscal());
			pstmt.setInt(13,objeto.getStNotaFiscal());
			if(objeto.getDtEmissao()==null)
				pstmt.setNull(14, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(14,new Timestamp(objeto.getDtEmissao().getTimeInMillis()));
			pstmt.setInt(15,objeto.getTpMovimento());
			if(objeto.getDtMovimentacao()==null)
				pstmt.setNull(16, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(16,new Timestamp(objeto.getDtMovimentacao().getTimeInMillis()));
			pstmt.setInt(17,objeto.getTpPagamento());
			pstmt.setInt(18,objeto.getTpEmissao());
			pstmt.setInt(19,objeto.getTpFinalidade());
			pstmt.setInt(20,objeto.getTpDanfe());
			pstmt.setFloat(21,objeto.getVlTotalProduto());
			pstmt.setFloat(22,objeto.getVlSeguro());
			pstmt.setFloat(23,objeto.getVlOutrasDespesas());
			pstmt.setFloat(24,objeto.getVlTotalNota());
			pstmt.setInt(25,objeto.getTpModalidadeFrete());
			pstmt.setString(26,objeto.getTxtObservacao());
			pstmt.setString(27,objeto.getTxtInformacaoFisco());
			pstmt.setInt(28,objeto.getLgDanfeImpresso());
			pstmt.setString(29,objeto.getNrChaveAcesso());
			pstmt.setString(30,objeto.getNrProtocoloAutorizacao());
			if(objeto.getDtAutorizacao()==null)
				pstmt.setNull(31, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(31,new Timestamp(objeto.getDtAutorizacao().getTimeInMillis()));
			if(objeto.getCdTransportador()==0)
				pstmt.setNull(32, Types.INTEGER);
			else
				pstmt.setInt(32,objeto.getCdTransportador());
			if(objeto.getCdNaturezaOperacaoFrete()==0)
				pstmt.setNull(33, Types.INTEGER);
			else
				pstmt.setInt(33,objeto.getCdNaturezaOperacaoFrete());
			pstmt.setFloat(34,objeto.getVlFrete());
			pstmt.setFloat(35,objeto.getVlFreteBaseIcms());
			pstmt.setFloat(36,objeto.getVlFreteIcmsRetido());
			pstmt.setString(37,objeto.getNrRecebimento());
			pstmt.setInt(38,objeto.getNrDv());
			pstmt.setString(39,objeto.getNrPlaca());
			pstmt.setString(40,objeto.getQtVolume());
			pstmt.setString(41,objeto.getDsEspecie());
			pstmt.setString(42,objeto.getDsMarca());
			pstmt.setString(43,objeto.getDsNumeracao());
			pstmt.setFloat(44,objeto.getVlPesoBruto());
			pstmt.setFloat(45,objeto.getVlPesoLiquido());
			if(objeto.getCdVeiculo()==0)
				pstmt.setNull(46, Types.INTEGER);
			else
				pstmt.setInt(46,objeto.getCdVeiculo());
			pstmt.setString(47,objeto.getSgUfVeiculo());
			pstmt.setString(48,objeto.getNrRntc());
			if(objeto.getCdMotivoCancelamento()==0)
				pstmt.setNull(49, Types.INTEGER);
			else
				pstmt.setInt(49,objeto.getCdMotivoCancelamento());
			pstmt.setString(50,objeto.getTxtXml());
			pstmt.setFloat(51, objeto.getPrDesconto());
			pstmt.setInt(52, objeto.getLgConsumidorFinal());
			pstmt.setInt(53, objeto.getTpVendaPresenca());
			pstmt.setString(54, objeto.getNrChaveAcessoReferencia());
			pstmt.setInt(55, cdNotaFiscalOld!=0 ? cdNotaFiscalOld : objeto.getCdNotaFiscal());
			return pstmt.executeUpdate();
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

	public static int delete(int cdNotaFiscal) {
		return delete(cdNotaFiscal, null);
	}

	public static int delete(int cdNotaFiscal, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM fsc_nota_fiscal WHERE cd_nota_fiscal=?");
			pstmt.setInt(1, cdNotaFiscal);
			return pstmt.executeUpdate();
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

	public static NotaFiscal get(int cdNotaFiscal) {
		return get(cdNotaFiscal, null);
	}

	public static NotaFiscal get(int cdNotaFiscal, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM fsc_nota_fiscal WHERE cd_nota_fiscal=?");
			pstmt.setInt(1, cdNotaFiscal);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new NotaFiscal(rs.getInt("cd_nota_fiscal"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_endereco_retirada"),
						rs.getInt("cd_natureza_operacao"),
						rs.getInt("cd_cidade"),
						rs.getInt("cd_destinatario"),
						rs.getInt("cd_endereco_destinatario"),
						rs.getInt("cd_endereco_entrega"),
						rs.getInt("cd_lote"),
						rs.getInt("tp_modelo"),
						rs.getString("nr_serie"),
						rs.getString("nr_nota_fiscal"),
						rs.getInt("st_nota_fiscal"),
						(rs.getTimestamp("dt_emissao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_emissao").getTime()),
						rs.getInt("tp_movimento"),
						(rs.getTimestamp("dt_movimentacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_movimentacao").getTime()),
						rs.getInt("tp_pagamento"),
						rs.getInt("tp_emissao"),
						rs.getInt("tp_finalidade"),
						rs.getInt("tp_danfe"),
						rs.getFloat("vl_total_produto"),
						rs.getFloat("vl_seguro"),
						rs.getFloat("vl_outras_despesas"),
						rs.getFloat("vl_total_nota"),
						rs.getInt("tp_modalidade_frete"),
						rs.getString("txt_observacao"),
						rs.getString("txt_informacao_fisco"),
						rs.getInt("lg_danfe_impresso"),
						rs.getString("nr_chave_acesso"),
						rs.getInt("nr_dv"),
						rs.getString("nr_protocolo_autorizacao"),
						(rs.getTimestamp("dt_autorizacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_autorizacao").getTime()),
						rs.getInt("cd_transportador"),
						rs.getInt("cd_natureza_operacao_frete"),
						rs.getFloat("vl_frete"),
						rs.getFloat("vl_frete_base_icms"),
						rs.getFloat("vl_frete_icms_retido"),
						rs.getString("nr_recebimento"),
						rs.getString("nr_placa"),
						rs.getString("qt_volume"),
						rs.getString("ds_especie"),
						rs.getString("ds_marca"),
						rs.getString("ds_numeracao"),
						rs.getFloat("vl_peso_bruto"),
						rs.getFloat("vl_peso_liquido"),
						rs.getInt("cd_veiculo"),
						rs.getString("sg_uf_veiculo"),
						rs.getString("nr_rntc"),
						rs.getInt("cd_motivo_cancelamento"),
						rs.getString("txt_xml"),
						rs.getFloat("pr_desconto"),
						rs.getInt("lg_consumidor_final"),
						rs.getInt("tp_venda_presenca"),
						rs.getString("nr_chave_acesso_referencia"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM fsc_nota_fiscal");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! NotaFiscalDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM fsc_nota_fiscal", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
