package com.tivic.manager.alm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class DocumentoEntradaItemDAO{

	public static int insert(DocumentoEntradaItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(DocumentoEntradaItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[4];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_documento_entrada");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdDocumentoEntrada()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_produto_servico");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdProdutoServico()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_empresa");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdEmpresa()));
			keys[3] = new HashMap<String,Object>();
			keys[3].put("FIELD_NAME", "cd_item");
			keys[3].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("alm_documento_entrada_item", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdItem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_documento_entrada_item (cd_documento_entrada,"+
			                                  "cd_produto_servico,"+
			                                  "cd_empresa,"+
			                                  "qt_entrada,"+
			                                  "vl_unitario,"+
			                                  "vl_acrescimo,"+
			                                  "vl_desconto,"+
			                                  "cd_unidade_medida,"+
			                                  "dt_entrega_prevista,"+
			                                  "cd_natureza_operacao,"+
			                                  "cd_adicao,"+
			                                  "cd_item,"+
			                                  "vl_vucv,"+
			                                  "vl_desconto_geral,"+
			                                  "cd_tipo_credito) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdDocumentoEntrada());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProdutoServico());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setDouble(4,objeto.getQtEntrada());
			pstmt.setDouble(5,objeto.getVlUnitario());
			pstmt.setDouble(6,objeto.getVlAcrescimo());
			pstmt.setDouble(7,objeto.getVlDesconto());
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdUnidadeMedida());
			if(objeto.getDtEntregaPrevista()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtEntregaPrevista().getTimeInMillis()));
			if(objeto.getCdNaturezaOperacao()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdNaturezaOperacao());
			if(objeto.getCdAdicao()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdAdicao());
			pstmt.setInt(12, code);
			pstmt.setDouble(13,objeto.getVlVucv());
			pstmt.setDouble(14,objeto.getVlDescontoGeral());
			if(objeto.getCdTipoCredito()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdTipoCredito());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(DocumentoEntradaItem objeto) {
		return update(objeto, 0, 0, 0, 0, null);
	}

	public static int update(DocumentoEntradaItem objeto, int cdDocumentoEntradaOld, int cdProdutoServicoOld, int cdEmpresaOld, int cdItemOld) {
		return update(objeto, cdDocumentoEntradaOld, cdProdutoServicoOld, cdEmpresaOld, cdItemOld, null);
	}

	public static int update(DocumentoEntradaItem objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, connect);
	}

	public static int update(DocumentoEntradaItem objeto, int cdDocumentoEntradaOld, int cdProdutoServicoOld, int cdEmpresaOld, int cdItemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_documento_entrada_item SET cd_documento_entrada=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_empresa=?,"+
												      		   "qt_entrada=?,"+
												      		   "vl_unitario=?,"+
												      		   "vl_acrescimo=?,"+
												      		   "vl_desconto=?,"+
												      		   "cd_unidade_medida=?,"+
												      		   "dt_entrega_prevista=?,"+
												      		   "cd_natureza_operacao=?,"+
												      		   "cd_adicao=?,"+
												      		   "cd_item=?,"+
												      		   "vl_vucv=?,"+
												      		   "vl_desconto_geral=?,"+
												      		   "cd_tipo_credito=? WHERE cd_documento_entrada=? AND cd_produto_servico=? AND cd_empresa=? AND cd_item=?");
			pstmt.setInt(1,objeto.getCdDocumentoEntrada());
			pstmt.setInt(2,objeto.getCdProdutoServico());
			pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setDouble(4,objeto.getQtEntrada());
			pstmt.setDouble(5,objeto.getVlUnitario());
			pstmt.setDouble(6,objeto.getVlAcrescimo());
			pstmt.setDouble(7,objeto.getVlDesconto());
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdUnidadeMedida());
			if(objeto.getDtEntregaPrevista()==null)
				pstmt.setNull(9, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(9,new Timestamp(objeto.getDtEntregaPrevista().getTimeInMillis()));
			if(objeto.getCdNaturezaOperacao()==0)
				pstmt.setNull(10, Types.INTEGER);
			else
				pstmt.setInt(10,objeto.getCdNaturezaOperacao());
			if(objeto.getCdAdicao()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdAdicao());
			pstmt.setInt(12,objeto.getCdItem());
			pstmt.setDouble(13,objeto.getVlVucv());
			pstmt.setDouble(14,objeto.getVlDescontoGeral());
			if(objeto.getCdTipoCredito()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdTipoCredito());
			pstmt.setInt(16, cdDocumentoEntradaOld!=0 ? cdDocumentoEntradaOld : objeto.getCdDocumentoEntrada());
			pstmt.setInt(17, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.setInt(18, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setDouble(19, cdItemOld!=0 ? cdItemOld : objeto.getCdItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdDocumentoEntrada, int cdProdutoServico, int cdEmpresa, int cdItem) {
		return delete(cdDocumentoEntrada, cdProdutoServico, cdEmpresa, cdItem, null);
	}

	public static int delete(int cdDocumentoEntrada, int cdProdutoServico, int cdEmpresa, int cdItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_documento_entrada_item WHERE cd_documento_entrada=? AND cd_produto_servico=? AND cd_empresa=? AND cd_item=?");
			pstmt.setInt(1, cdDocumentoEntrada);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdEmpresa);
			pstmt.setInt(4, cdItem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static int delete(int cdDocumentoEntrada, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_documento_entrada_item WHERE cd_documento_entrada=?");
			pstmt.setInt(1, cdDocumentoEntrada);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static DocumentoEntradaItem get(int cdDocumentoEntrada, int cdProdutoServico, int cdEmpresa, int cdItem) {
		return get(cdDocumentoEntrada, cdProdutoServico, cdEmpresa, cdItem, null);
	}

	public static DocumentoEntradaItem get(int cdDocumentoEntrada, int cdProdutoServico, int cdEmpresa, int cdItem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_documento_entrada_item WHERE cd_documento_entrada=? AND cd_produto_servico=? AND cd_empresa=? AND cd_item=?");
			pstmt.setInt(1, cdDocumentoEntrada);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdEmpresa);
			pstmt.setInt(4, cdItem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DocumentoEntradaItem(rs.getInt("cd_documento_entrada"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_empresa"),
						rs.getFloat("qt_entrada"),
						rs.getFloat("vl_unitario"),
						rs.getFloat("vl_acrescimo"),
						rs.getFloat("vl_desconto"),
						rs.getInt("cd_unidade_medida"),
						(rs.getTimestamp("dt_entrega_prevista")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_entrega_prevista").getTime()),
						rs.getInt("cd_natureza_operacao"),
						rs.getInt("cd_adicao"),
						rs.getInt("cd_item"),
						rs.getFloat("vl_vucv"),
						rs.getFloat("vl_desconto_geral"),
						rs.getInt("cd_tipo_credito"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_documento_entrada_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaItemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ArrayList<DocumentoEntradaItem> getList() {
		return getList(null);
	}

	public static ArrayList<DocumentoEntradaItem> getList(Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			ArrayList<DocumentoEntradaItem> list = new ArrayList<DocumentoEntradaItem>();
			ResultSetMap rsm = getAll(connect);
			while(rsm.next()){
				DocumentoEntradaItem obj = DocumentoEntradaItemDAO.get(rsm.getInt("cd_documento_entrada"), rsm.getInt("cd_produto_servico"), rsm.getInt("cd_empresa"), rsm.getInt("cd_item"), connect);
				list.add(obj);
			}
			return list;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! DocumentoEntradaItemDAO.getList: " + e);
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
		return Search.find("SELECT * FROM alm_documento_entrada_item", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}