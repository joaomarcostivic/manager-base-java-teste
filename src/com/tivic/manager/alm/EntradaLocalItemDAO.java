package com.tivic.manager.alm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class EntradaLocalItemDAO{

	public static int insert(EntradaLocalItem objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(EntradaLocalItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[6];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_produto_servico");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdProdutoServico()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_documento_entrada");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdDocumentoEntrada()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_empresa");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdEmpresa()));
			keys[3] = new HashMap<String,Object>();
			keys[3].put("FIELD_NAME", "cd_item");
			keys[3].put("IS_KEY_NATIVE", "NO");
			keys[3].put("FIELD_VALUE", new Integer(objeto.getCdItem()));
			keys[4] = new HashMap<String,Object>();
			keys[4].put("FIELD_NAME", "cd_local_armazenamento");
			keys[4].put("IS_KEY_NATIVE", "NO");
			keys[4].put("FIELD_VALUE", new Integer(objeto.getCdLocalArmazenamento()));
			keys[5] = new HashMap<String,Object>();
			keys[5].put("FIELD_NAME", "cd_entrada_local_item");
			keys[5].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("alm_entrada_local_item", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdEntradaLocalItem(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_entrada_local_item (cd_produto_servico,"+
			                                  "cd_documento_entrada,"+
			                                  "cd_empresa,"+
			                                  "cd_local_armazenamento,"+
			                                  "qt_entrada,"+
			                                  "qt_entrada_consignada,"+
			                                  "cd_entrada_local_item," +
			                                  "cd_referencia," +
			                                  "cd_item) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1,objeto.getCdProdutoServico());
			pstmt.setInt(2,objeto.getCdDocumentoEntrada());
			pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setInt(4,objeto.getCdLocalArmazenamento());
			pstmt.setFloat(5,objeto.getQtEntrada());
			pstmt.setFloat(6,objeto.getQtEntradaConsignada());
			pstmt.setInt(7, code);
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8, objeto.getCdReferencia());
			pstmt.setInt(9,objeto.getCdItem());
			pstmt.executeUpdate();
			
			System.out.println("entradaLocalItem: "+objeto);
			
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

	public static int update(EntradaLocalItem objeto) {
		return update(objeto, 0, 0, 0, 0, 0, 0, null);
	}

	public static int update(EntradaLocalItem objeto, int cdProdutoServicoOld, int cdDocumentoEntradaOld, int cdEmpresaOld, int cdLocalArmazenamentoOld, int cdEntradaLocalItemOld, int cdItemOld) {
		return update(objeto, cdProdutoServicoOld, cdDocumentoEntradaOld, cdEmpresaOld, cdLocalArmazenamentoOld, cdEntradaLocalItemOld, cdItemOld, null);
	}

	public static int update(EntradaLocalItem objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, 0, 0, connect);
	}

	public static int update(EntradaLocalItem objeto, int cdProdutoServicoOld, int cdDocumentoEntradaOld, int cdEmpresaOld, int cdLocalArmazenamentoOld, int cdEntradaLocalItemOld, int cdItemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_entrada_local_item SET cd_produto_servico=?,"+
												      		   "cd_documento_entrada=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_local_armazenamento=?,"+
												      		   "qt_entrada=?,"+
												      		   "qt_entrada_consignada=?,"+
												      		   "cd_entrada_local_item=?, " +
												      		   "cd_referencia=?, " +
												      		   "cd_item=? WHERE cd_produto_servico=? AND cd_documento_entrada=? AND cd_empresa=? AND cd_local_armazenamento=? AND cd_entrada_local_item=? AND cd_item=?");
			pstmt.setInt(1,objeto.getCdProdutoServico());
			pstmt.setInt(2,objeto.getCdDocumentoEntrada());
			pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setInt(4,objeto.getCdLocalArmazenamento());
			pstmt.setFloat(5,objeto.getQtEntrada());
			pstmt.setFloat(6,objeto.getQtEntradaConsignada());
			pstmt.setInt(7,objeto.getCdEntradaLocalItem());
			if(objeto.getCdReferencia()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8, objeto.getCdReferencia());
			pstmt.setInt(9,objeto.getCdItem());
			pstmt.setInt(10, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.setInt(11, cdDocumentoEntradaOld!=0 ? cdDocumentoEntradaOld : objeto.getCdDocumentoEntrada());
			pstmt.setInt(12, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(13, cdLocalArmazenamentoOld!=0 ? cdLocalArmazenamentoOld : objeto.getCdLocalArmazenamento());
			pstmt.setFloat(14, cdEntradaLocalItemOld!=0 ? cdEntradaLocalItemOld : objeto.getCdEntradaLocalItem());
			pstmt.setFloat(15, cdItemOld!=0 ? cdItemOld : objeto.getCdItem());
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

	public static int delete(int cdProdutoServico, int cdDocumentoEntrada, int cdEmpresa, int cdLocalArmazenamento, int cdEntradaLocalItem, int cdItem) {
		return delete(cdProdutoServico, cdDocumentoEntrada, cdEmpresa, cdLocalArmazenamento, cdEntradaLocalItem, cdItem, null);
	}

	public static int delete(int cdProdutoServico, int cdDocumentoEntrada, int cdEmpresa, int cdLocalArmazenamento, int cdEntradaLocalItem, int cdItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_entrada_local_item WHERE cd_produto_servico=? AND cd_documento_entrada=? AND cd_empresa=? AND cd_local_armazenamento=? AND cd_entrada_local_item=? AND cd_item=?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.setInt(2, cdDocumentoEntrada);
			pstmt.setInt(3, cdEmpresa);
			pstmt.setInt(4, cdLocalArmazenamento);
			pstmt.setInt(5, cdEntradaLocalItem);
			pstmt.setInt(6, cdItem);
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

	public static EntradaLocalItem get(int cdProdutoServico, int cdDocumentoEntrada, int cdEmpresa, int cdLocalArmazenamento, int cdEntradaLocalItem, int cdItem) {
		return get(cdProdutoServico, cdDocumentoEntrada, cdEmpresa, cdLocalArmazenamento, cdEntradaLocalItem, cdItem, null);
	}

	public static EntradaLocalItem get(int cdProdutoServico, int cdDocumentoEntrada, int cdEmpresa, int cdLocalArmazenamento, int cdEntradaLocalItem, int cdItem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_entrada_local_item WHERE cd_produto_servico=? AND cd_documento_entrada=? AND cd_empresa=? AND cd_local_armazenamento=? AND cd_entrada_local_item=?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.setInt(2, cdDocumentoEntrada);
			pstmt.setInt(3, cdEmpresa);
			pstmt.setInt(4, cdLocalArmazenamento);
			pstmt.setInt(5, cdEntradaLocalItem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EntradaLocalItem(rs.getInt("cd_produto_servico"),
						rs.getInt("cd_documento_entrada"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_local_armazenamento"),
						rs.getFloat("qt_entrada"),
						rs.getFloat("qt_entrada_consignada"),
						rs.getInt("cd_entrada_local_item"),
						rs.getInt("cd_referencia"),
						rs.getInt("cd_item"));
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_entrada_local_item");
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
		return Search.find("SELECT * FROM alm_entrada_local_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
