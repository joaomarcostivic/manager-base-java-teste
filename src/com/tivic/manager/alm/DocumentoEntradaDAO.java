package com.tivic.manager.alm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class DocumentoEntradaDAO{

	public static int insert(DocumentoEntrada objeto) {
		return insert(objeto, null);
	}

	public static int insert(DocumentoEntrada objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("alm_documento_entrada", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdDocumentoEntrada(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_documento_entrada (cd_documento_entrada,"+
			                                  "cd_empresa,"+
			                                  "cd_transportadora,"+
			                                  "cd_fornecedor,"+
			                                  "dt_emissao,"+
			                                  "dt_documento_entrada,"+
			                                  "st_documento_entrada,"+
			                                  "vl_desconto,"+
			                                  "vl_acrescimo,"+
			                                  "nr_documento_entrada,"+
			                                  "tp_documento_entrada,"+
			                                  "nr_conhecimento,"+
			                                  "tp_entrada,"+
			                                  "txt_observacao,"+
			                                  "cd_natureza_operacao,"+
			                                  "tp_frete,"+
			                                  "nr_placa_veiculo,"+
			                                  "sg_placa_veiculo,"+
			                                  "qt_volumes,"+
			                                  "dt_saida_transportadora,"+
			                                  "ds_via_transporte,"+
			                                  "txt_corpo_nota_fiscal,"+
			                                  "vl_peso_bruto,"+
			                                  "vl_peso_liquido,"+
			                                  "ds_especie_volumes,"+
			                                  "ds_marca_volumes,"+
			                                  "nr_volumes,"+
			                                  "tp_movimento_estoque,"+
			                                  "cd_moeda,"+
			                                  "cd_tabela_preco,"+
			                                  "vl_total_documento,"+
			                                  "cd_documento_saida_origem,"+
			                                  "vl_frete,"+
			                                  "vl_seguro,"+
			                                  "cd_digitador," +
			                                  "vl_total_itens," +
			                                  "nr_serie," +
			                                  "cd_viagem) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdTransportadora()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTransportadora());
			if(objeto.getCdFornecedor()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdFornecedor());
			if(objeto.getDtEmissao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtEmissao().getTimeInMillis()));
			if(objeto.getDtDocumentoEntrada()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtDocumentoEntrada().getTimeInMillis()));
			pstmt.setInt(7,objeto.getStDocumentoEntrada());
			pstmt.setDouble(8,objeto.getVlDesconto());
			pstmt.setDouble(9,objeto.getVlAcrescimo());
			pstmt.setString(10,objeto.getNrDocumentoEntrada());
			pstmt.setInt(11,objeto.getTpDocumentoEntrada());
			pstmt.setString(12,objeto.getNrConhecimento());
			pstmt.setInt(13,objeto.getTpEntrada());
			pstmt.setString(14,objeto.getTxtObservacao());
			if(objeto.getCdNaturezaOperacao()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdNaturezaOperacao());
			pstmt.setInt(16,objeto.getTpFrete());
			pstmt.setString(17,objeto.getNrPlacaVeiculo());
			pstmt.setString(18,objeto.getSgPlacaVeiculo());
			pstmt.setDouble(19,objeto.getQtVolumes());
			if(objeto.getDtSaidaTransportadora()==null)
				pstmt.setNull(20, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(20,new Timestamp(objeto.getDtSaidaTransportadora().getTimeInMillis()));
			pstmt.setString(21,objeto.getDsViaTransporte());
			pstmt.setString(22,objeto.getTxtCorpoNotaFiscal());
			pstmt.setDouble(23,objeto.getVlPesoBruto());
			pstmt.setDouble(24,objeto.getVlPesoLiquido());
			pstmt.setString(25,objeto.getDsEspecieVolumes());
			pstmt.setString(26,objeto.getDsMarcaVolumes());
			pstmt.setString(27,objeto.getNrVolumes());
			pstmt.setInt(28,objeto.getTpMovimentoEstoque());
			if(objeto.getCdMoeda()==0)
				pstmt.setNull(29, Types.INTEGER);
			else
				pstmt.setInt(29,objeto.getCdMoeda());
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(30, Types.INTEGER);
			else
				pstmt.setInt(30,objeto.getCdTabelaPreco());
			pstmt.setDouble(31,objeto.getVlTotalDocumento());
			if(objeto.getCdDocumentoSaidaOrigem()==0)
				pstmt.setNull(32, Types.INTEGER);
			else
				pstmt.setInt(32,objeto.getCdDocumentoSaidaOrigem());
			pstmt.setDouble(33,objeto.getVlFrete());
			pstmt.setDouble(34,objeto.getVlSeguro());
			if(objeto.getCdDigitador()==0)
				pstmt.setNull(35, Types.INTEGER);
			else
				pstmt.setInt(35,objeto.getCdDigitador());
			pstmt.setDouble(36,objeto.getVlTotalItens());
			pstmt.setInt(37,objeto.getNrSerie());
			if(objeto.getCdViagem()==0)
				pstmt.setNull(38, Types.INTEGER);
			else
				pstmt.setInt(38,objeto.getCdViagem());
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

	public static int update(DocumentoEntrada objeto) {
		return update(objeto, 0, null);
	}

	public static int update(DocumentoEntrada objeto, int cdDocumentoEntradaOld) {
		return update(objeto, cdDocumentoEntradaOld, null);
	}

	public static int update(DocumentoEntrada objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(DocumentoEntrada objeto, int cdDocumentoEntradaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_documento_entrada SET cd_documento_entrada=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_transportadora=?,"+
												      		   "cd_fornecedor=?,"+
												      		   "dt_emissao=?,"+
												      		   "dt_documento_entrada=?,"+
												      		   "st_documento_entrada=?,"+
												      		   "vl_desconto=?,"+
												      		   "vl_acrescimo=?,"+
												      		   "nr_documento_entrada=?,"+
												      		   "tp_documento_entrada=?,"+
												      		   "nr_conhecimento=?,"+
												      		   "tp_entrada=?,"+
												      		   "txt_observacao=?,"+
												      		   "cd_natureza_operacao=?,"+
												      		   "tp_frete=?,"+
												      		   "nr_placa_veiculo=?,"+
												      		   "sg_placa_veiculo=?,"+
												      		   "qt_volumes=?,"+
												      		   "dt_saida_transportadora=?,"+
												      		   "ds_via_transporte=?,"+
												      		   "txt_corpo_nota_fiscal=?,"+
												      		   "vl_peso_bruto=?,"+
												      		   "vl_peso_liquido=?,"+
												      		   "ds_especie_volumes=?,"+
												      		   "ds_marca_volumes=?,"+
												      		   "nr_volumes=?,"+
												      		   "tp_movimento_estoque=?,"+
												      		   "cd_moeda=?,"+
												      		   "cd_tabela_preco=?,"+
												      		   "vl_total_documento=?,"+
												      		   "cd_documento_saida_origem=?,"+
												      		   "vl_frete=?,"+
												      		   "vl_seguro=?,"+
												      		   "cd_digitador=?," +
												      		   "vl_total_itens=?," +
												      		   "nr_serie=?," +
												      		   "cd_viagem=? WHERE cd_documento_entrada=?");
			pstmt.setInt(1,objeto.getCdDocumentoEntrada());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdTransportadora()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTransportadora());
			if(objeto.getCdFornecedor()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdFornecedor());
			if(objeto.getDtEmissao()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtEmissao().getTimeInMillis()));
			if(objeto.getDtDocumentoEntrada()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtDocumentoEntrada().getTimeInMillis()));
			pstmt.setInt(7,objeto.getStDocumentoEntrada());
			pstmt.setDouble(8,objeto.getVlDesconto());
			pstmt.setDouble(9,objeto.getVlAcrescimo());
			pstmt.setString(10,objeto.getNrDocumentoEntrada());
			pstmt.setInt(11,objeto.getTpDocumentoEntrada());
			pstmt.setString(12,objeto.getNrConhecimento());
			pstmt.setInt(13,objeto.getTpEntrada());
			pstmt.setString(14,objeto.getTxtObservacao());
			if(objeto.getCdNaturezaOperacao()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdNaturezaOperacao());
			pstmt.setInt(16,objeto.getTpFrete());
			pstmt.setString(17,objeto.getNrPlacaVeiculo());
			pstmt.setString(18,objeto.getSgPlacaVeiculo());
			pstmt.setDouble(19,objeto.getQtVolumes());
			if(objeto.getDtSaidaTransportadora()==null)
				pstmt.setNull(20, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(20,new Timestamp(objeto.getDtSaidaTransportadora().getTimeInMillis()));
			pstmt.setString(21,objeto.getDsViaTransporte());
			pstmt.setString(22,objeto.getTxtCorpoNotaFiscal());
			pstmt.setDouble(23,objeto.getVlPesoBruto());
			pstmt.setDouble(24,objeto.getVlPesoLiquido());
			pstmt.setString(25,objeto.getDsEspecieVolumes());
			pstmt.setString(26,objeto.getDsMarcaVolumes());
			pstmt.setString(27,objeto.getNrVolumes());
			pstmt.setInt(28,objeto.getTpMovimentoEstoque());
			if(objeto.getCdMoeda()==0)
				pstmt.setNull(29, Types.INTEGER);
			else
				pstmt.setInt(29,objeto.getCdMoeda());
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(30, Types.INTEGER);
			else
				pstmt.setInt(30,objeto.getCdTabelaPreco());
			pstmt.setDouble(31,objeto.getVlTotalDocumento());
			if(objeto.getCdDocumentoSaidaOrigem()==0)
				pstmt.setNull(32, Types.INTEGER);
			else
				pstmt.setInt(32,objeto.getCdDocumentoSaidaOrigem());
			pstmt.setDouble(33,objeto.getVlFrete());
			pstmt.setDouble(34,objeto.getVlSeguro());
			if(objeto.getCdDigitador()==0)
				pstmt.setNull(35, Types.INTEGER);
			else
				pstmt.setInt(35,objeto.getCdDigitador());
			pstmt.setDouble(36,objeto.getVlTotalItens());
			pstmt.setInt(37,objeto.getNrSerie());
			if(objeto.getCdViagem()==0)
				pstmt.setNull(38, Types.INTEGER);
			else
				pstmt.setInt(38,objeto.getCdViagem());
			pstmt.setInt(39, cdDocumentoEntradaOld!=0 ? cdDocumentoEntradaOld : objeto.getCdDocumentoEntrada());
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

	public static int delete(int cdDocumentoEntrada) {
		return delete(cdDocumentoEntrada, null);
	}

	public static int delete(int cdDocumentoEntrada, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_documento_entrada WHERE cd_documento_entrada=?");
			pstmt.setInt(1, cdDocumentoEntrada);
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

	public static DocumentoEntrada get(int cdDocumentoEntrada) {
		return get(cdDocumentoEntrada, null);
	}

	public static DocumentoEntrada get(int cdDocumentoEntrada, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_documento_entrada WHERE cd_documento_entrada=?");
			pstmt.setInt(1, cdDocumentoEntrada);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DocumentoEntrada(rs.getInt("cd_documento_entrada"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_transportadora"),
						rs.getInt("cd_fornecedor"),
						(rs.getTimestamp("dt_emissao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_emissao").getTime()),
						(rs.getTimestamp("dt_documento_entrada")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_documento_entrada").getTime()),
						rs.getInt("st_documento_entrada"),
						rs.getFloat("vl_desconto"),
						rs.getFloat("vl_acrescimo"),
						rs.getString("nr_documento_entrada"),
						rs.getInt("tp_documento_entrada"),
						rs.getString("nr_conhecimento"),
						rs.getInt("tp_entrada"),
						rs.getString("txt_observacao"),
						rs.getInt("cd_natureza_operacao"),
						rs.getInt("tp_frete"),
						rs.getString("nr_placa_veiculo"),
						rs.getString("sg_placa_veiculo"),
						rs.getFloat("qt_volumes"),
						(rs.getTimestamp("dt_saida_transportadora")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_saida_transportadora").getTime()),
						rs.getString("ds_via_transporte"),
						rs.getString("txt_corpo_nota_fiscal"),
						rs.getFloat("vl_peso_bruto"),
						rs.getFloat("vl_peso_liquido"),
						rs.getString("ds_especie_volumes"),
						rs.getString("ds_marca_volumes"),
						rs.getString("nr_volumes"),
						rs.getInt("tp_movimento_estoque"),
						rs.getInt("cd_moeda"),
						rs.getInt("cd_tabela_preco"),
						rs.getFloat("vl_total_documento"),
						rs.getInt("cd_documento_saida_origem"),
						rs.getFloat("vl_frete"),
						rs.getFloat("vl_seguro"),
						rs.getInt("cd_digitador"),
						rs.getFloat("vl_total_itens"),
						rs.getInt("nr_serie"),
						rs.getInt("cd_viagem"));
			}
			return null;
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

	public static ResultSetMap getAll() {
		return getAll(null);
	}

	public static ResultSetMap getAll(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_documento_entrada");
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
		ResultSetMap rsm = Search.find("SELECT * FROM alm_documento_entrada", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
		return rsm;
	}

}
