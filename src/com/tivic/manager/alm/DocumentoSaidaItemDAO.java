package com.tivic.manager.alm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class DocumentoSaidaItemDAO{

	public static int insert(DocumentoSaidaItem objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(DocumentoSaidaItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_documento_saida");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdDocumentoSaida()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_item");
			keys[1].put("IS_KEY_NATIVE", "YES");

			int code = Conexao.getSequenceCode("alm_documento_saida_item", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdItem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_documento_saida_item (cd_documento_saida,"+
			                                  "cd_produto_servico,"+
			                                  "cd_empresa,"+
			                                  "qt_saida,"+
			                                  "vl_unitario,"+
			                                  "vl_acrescimo,"+
			                                  "vl_desconto,"+
			                                  "dt_entrega_prevista,"+
			                                  "cd_unidade_medida,"+
			                                  "cd_tabela_preco,"+
			                                  "cd_item," +
			                                  "cd_bico," +
			                                  "vl_desconto_geral," +
			                                  "cd_natureza_operacao," +
			                                  "vl_preco_tabela," +
			                                  "qt_encerrante_final," +
			                                  "cd_tipo_contribuicao_social) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdDocumentoSaida()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdDocumentoSaida());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProdutoServico());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setFloat(4,objeto.getQtSaida());
			pstmt.setFloat(5,objeto.getVlUnitario());
			pstmt.setFloat(6,objeto.getVlAcrescimo());
			pstmt.setFloat(7,objeto.getVlDesconto());
			if(objeto.getDtEntregaPrevista()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtEntregaPrevista().getTimeInMillis()));
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdUnidadeMedida());
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdTabelaPreco());
			pstmt.setInt(11, code);
			if(objeto.getCdBico()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdBico());
			pstmt.setFloat(13,objeto.getVlDescontoGeral());
			if(objeto.getCdNaturezaOperacao()<=0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdNaturezaOperacao());
			pstmt.setFloat(15,objeto.getVlPrecoTabela());
			pstmt.setFloat(16,objeto.getQtEncerranteFinal());
			if(objeto.getCdTipoContribuicaoSocial()<=0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdTipoContribuicaoSocial());
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

	public static int update(DocumentoSaidaItem objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(DocumentoSaidaItem objeto, int cdDocumentoSaidaOld, int cdProdutoServicoOld, int cdEmpresaOld, int cdItemOld) {
		return update(objeto, cdDocumentoSaidaOld, cdProdutoServicoOld, cdEmpresaOld, cdItemOld, null);
	}

	public static int update(DocumentoSaidaItem objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(DocumentoSaidaItem objeto, int cdDocumentoSaidaOld, int cdProdutoServicoOld, int cdEmpresaOld, int cdItemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_documento_saida_item SET cd_documento_saida=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_empresa=?,"+
												      		   "qt_saida=?,"+
												      		   "vl_unitario=?,"+
												      		   "vl_acrescimo=?,"+
												      		   "vl_desconto=?,"+
												      		   "dt_entrega_prevista=?,"+
												      		   "cd_unidade_medida=?,"+
												      		   "cd_tabela_preco=?,"+
												      		   "cd_item=?," +
												      		   "cd_bico=?," +
												      		   "vl_desconto_geral=?," +
												      		   "cd_natureza_operacao=?," +
												      		   "vl_preco_tabela=?," +
												      		   "qt_encerrante_final=?," +
												      		   "cd_tipo_contribuicao_social=? WHERE cd_documento_saida=? AND cd_produto_servico=? AND cd_empresa=? AND cd_item=?");
			pstmt.setInt(1,objeto.getCdDocumentoSaida());
			pstmt.setInt(2,objeto.getCdProdutoServico());
			pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setFloat(4,objeto.getQtSaida());
			pstmt.setFloat(5,objeto.getVlUnitario());
			pstmt.setFloat(6,objeto.getVlAcrescimo());
			pstmt.setFloat(7,objeto.getVlDesconto());
			if(objeto.getDtEntregaPrevista()==null)
				pstmt.setNull(8, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(8,new Timestamp(objeto.getDtEntregaPrevista().getTimeInMillis()));
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdUnidadeMedida());
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdTabelaPreco());
			pstmt.setInt(11,objeto.getCdItem());
			if(objeto.getCdBico()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdBico());
			pstmt.setFloat(13,objeto.getVlDescontoGeral());
			if(objeto.getCdNaturezaOperacao()<=0)
				pstmt.setNull(14, Types.INTEGER);
			else
				pstmt.setInt(14,objeto.getCdNaturezaOperacao());
			pstmt.setFloat(15,objeto.getVlPrecoTabela());
			pstmt.setFloat(16,objeto.getQtEncerranteFinal());
			if(objeto.getCdTipoContribuicaoSocial()<=0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17,objeto.getCdTipoContribuicaoSocial());
			pstmt.setInt(18, cdDocumentoSaidaOld!=0 ? cdDocumentoSaidaOld : objeto.getCdDocumentoSaida());
			pstmt.setInt(19, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.setInt(20, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setFloat(21, cdItemOld!=0 ? cdItemOld : objeto.getCdItem());
			return pstmt.executeUpdate();
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

	public static int delete(int cdDocumentoSaida, int cdProdutoServico, int cdEmpresa, int cdItem) {
		return delete(cdDocumentoSaida, cdProdutoServico, cdEmpresa, cdItem, null);
	}

	public static int delete(int cdDocumentoSaida, int cdProdutoServico, int cdEmpresa, int cdItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_documento_saida_item WHERE cd_documento_saida=? AND cd_produto_servico=? AND cd_empresa=? AND cd_item=?");
			pstmt.setInt(1, cdDocumentoSaida);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdEmpresa);
			pstmt.setInt(4, cdItem);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			System.err.println("Erro! DocumentoSaidaItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int delete(int cdDocumentoSaida, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_documento_saida_item WHERE cd_documento_saida=?");
			pstmt.setInt(1, cdDocumentoSaida);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			System.err.println("Erro! DocumentoSaidaItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static DocumentoSaidaItem get(int cdDocumentoSaida, int cdProdutoServico, int cdEmpresa, int cdItem) {
		return get(cdDocumentoSaida, cdProdutoServico, cdEmpresa, cdItem, null);
	}

	public static DocumentoSaidaItem get(int cdDocumentoSaida, int cdProdutoServico, int cdEmpresa, int cdItem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_documento_saida_item WHERE cd_documento_saida=? AND cd_produto_servico=? AND cd_empresa=? AND cd_item=?");
			pstmt.setInt(1, cdDocumentoSaida);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdEmpresa);
			pstmt.setInt(4, cdItem);
			rs = pstmt.executeQuery();

			if(rs.next()){
				return new DocumentoSaidaItem(rs.getInt("cd_documento_saida"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_empresa"),
						rs.getFloat("qt_saida"),
						rs.getFloat("vl_unitario"),
						rs.getFloat("vl_acrescimo"),
						rs.getFloat("vl_desconto"),
						(rs.getTimestamp("dt_entrega_prevista")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_entrega_prevista").getTime()),
						rs.getInt("cd_unidade_medida"),
						rs.getInt("cd_tabela_preco"),
						rs.getInt("cd_item"),
						rs.getInt("cd_bico"),
						rs.getFloat("vl_desconto_geral"),
						rs.getInt("cd_natureza_operacao"),
						rs.getFloat("vl_preco_tabela"),
						rs.getFloat("qt_encerrante_final"),
						rs.getInt("cd_tipo_contribuicao_social"));
			}
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
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
		try {
			return new ResultSetMap(connect.prepareStatement("SELECT * FROM alm_documento_saida_item").executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
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
		return Search.find("SELECT * FROM alm_documento_saida_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
