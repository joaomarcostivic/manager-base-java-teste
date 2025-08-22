package com.tivic.manager.alm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class SaidaLocalItemDAO{

	public static int insert(SaidaLocalItem objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(SaidaLocalItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[6];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_saida");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_produto_servico");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdProdutoServico()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_documento_saida");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdDocumentoSaida()));
			keys[3] = new HashMap<String,Object>();
			keys[3].put("FIELD_NAME", "cd_empresa");
			keys[3].put("IS_KEY_NATIVE", "NO");
			keys[3].put("FIELD_VALUE", new Integer(objeto.getCdEmpresa()));
			keys[4] = new HashMap<String,Object>();
			keys[4].put("FIELD_NAME", "cd_local_armazenamento");
			keys[4].put("IS_KEY_NATIVE", "NO");
			keys[4].put("FIELD_VALUE", new Integer(objeto.getCdLocalArmazenamento()));
			keys[5] = new HashMap<String,Object>();
			keys[5].put("FIELD_NAME", "cd_item");
			keys[5].put("IS_KEY_NATIVE", "NO");
			keys[5].put("FIELD_VALUE", new Integer(objeto.getCdItem()));
			int code = Conexao.getSequenceCode("alm_saida_local_item", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdSaida(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_saida_local_item (cd_saida,"+
			                                  "cd_produto_servico,"+
			                                  "cd_documento_saida,"+
			                                  "cd_empresa,"+
			                                  "cd_local_armazenamento,"+
			                                  "cd_pedido_venda,"+
			                                  "dt_saida,"+
			                                  "qt_saida,"+
			                                  "qt_saida_consignada,"+
			                                  "st_saida_local_item,"+
			                                  "id_saida_local_item,"+
			                                  "cd_item) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProdutoServico());
			if(objeto.getCdDocumentoSaida()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdDocumentoSaida());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdEmpresa());
			if(objeto.getCdLocalArmazenamento()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdLocalArmazenamento());
			if(objeto.getCdPedidoVenda()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdPedidoVenda());
			if(objeto.getDtSaida()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtSaida().getTimeInMillis()));
			pstmt.setFloat(8,objeto.getQtSaida());
			pstmt.setFloat(9,objeto.getQtSaidaConsignada());
			pstmt.setInt(10,objeto.getStSaidaLocalItem());
			pstmt.setString(11,objeto.getIdSaidaLocalItem());
			if(objeto.getCdItem()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdItem());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SaidaLocalItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SaidaLocalItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(SaidaLocalItem objeto) {
		return update(objeto, 0, 0, 0, 0, 0, 0, null);
	}

	public static int update(SaidaLocalItem objeto, int cdSaidaOld, int cdProdutoServicoOld, int cdDocumentoSaidaOld, int cdEmpresaOld, int cdLocalArmazenamentoOld, int cdItemOld) {
		return update(objeto, cdSaidaOld, cdProdutoServicoOld, cdDocumentoSaidaOld, cdEmpresaOld, cdLocalArmazenamentoOld, cdItemOld, null);
	}

	public static int update(SaidaLocalItem objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, 0, 0, connect);
	}

	public static int update(SaidaLocalItem objeto, int cdSaidaOld, int cdProdutoServicoOld, int cdDocumentoSaidaOld, int cdEmpresaOld, int cdLocalArmazenamentoOld, int cdItemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_saida_local_item SET cd_saida=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_documento_saida=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_local_armazenamento=?,"+
												      		   "cd_pedido_venda=?,"+
												      		   "dt_saida=?,"+
												      		   "qt_saida=?,"+
												      		   "qt_saida_consignada=?,"+
												      		   "st_saida_local_item=?,"+
												      		   "id_saida_local_item=?,"+
												      		   "cd_item=? WHERE cd_saida=? AND cd_produto_servico=? AND cd_documento_saida=? AND cd_empresa=? AND cd_local_armazenamento=? AND cd_item=?");
			pstmt.setInt(1,objeto.getCdSaida());
			pstmt.setInt(2,objeto.getCdProdutoServico());
			pstmt.setInt(3,objeto.getCdDocumentoSaida());
			pstmt.setInt(4,objeto.getCdEmpresa());
			pstmt.setInt(5,objeto.getCdLocalArmazenamento());
			if(objeto.getCdPedidoVenda()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdPedidoVenda());
			if(objeto.getDtSaida()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtSaida().getTimeInMillis()));
			pstmt.setFloat(8,objeto.getQtSaida());
			pstmt.setFloat(9,objeto.getQtSaidaConsignada());
			pstmt.setInt(10,objeto.getStSaidaLocalItem());
			pstmt.setString(11,objeto.getIdSaidaLocalItem());
			pstmt.setInt(12,objeto.getCdItem());
			pstmt.setInt(13, cdSaidaOld!=0 ? cdSaidaOld : objeto.getCdSaida());
			pstmt.setInt(14, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.setInt(15, cdDocumentoSaidaOld!=0 ? cdDocumentoSaidaOld : objeto.getCdDocumentoSaida());
			pstmt.setInt(16, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(17, cdLocalArmazenamentoOld!=0 ? cdLocalArmazenamentoOld : objeto.getCdLocalArmazenamento());
			pstmt.setInt(18, cdItemOld!=0 ? cdItemOld : objeto.getCdItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SaidaLocalItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SaidaLocalItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdSaida, int cdProdutoServico, int cdDocumentoSaida, int cdEmpresa, int cdLocalArmazenamento, int cdItem) {
		return delete(cdSaida, cdProdutoServico, cdDocumentoSaida, cdEmpresa, cdLocalArmazenamento, cdItem, null);
	}

	public static int delete(int cdSaida, int cdProdutoServico, int cdDocumentoSaida, int cdEmpresa, int cdLocalArmazenamento, int cdItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_saida_local_item WHERE cd_saida=? AND cd_produto_servico=? AND cd_documento_saida=? AND cd_empresa=? AND cd_local_armazenamento=? AND cd_item=?");
			pstmt.setInt(1, cdSaida);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdDocumentoSaida);
			pstmt.setInt(4, cdEmpresa);
			pstmt.setInt(5, cdLocalArmazenamento);
			pstmt.setInt(6, cdItem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SaidaLocalItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SaidaLocalItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static SaidaLocalItem get(int cdSaida, int cdProdutoServico, int cdDocumentoSaida, int cdEmpresa, int cdLocalArmazenamento, int cdItem) {
		return get(cdSaida, cdProdutoServico, cdDocumentoSaida, cdEmpresa, cdLocalArmazenamento, cdItem, null);
	}

	public static SaidaLocalItem get(int cdSaida, int cdProdutoServico, int cdDocumentoSaida, int cdEmpresa, int cdLocalArmazenamento, int cdItem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_saida_local_item WHERE cd_saida=? AND cd_produto_servico=? AND cd_documento_saida=? AND cd_empresa=? AND cd_local_armazenamento=? AND cd_item=?");
			pstmt.setInt(1, cdSaida);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdDocumentoSaida);
			pstmt.setInt(4, cdEmpresa);
			pstmt.setInt(5, cdLocalArmazenamento);
			pstmt.setInt(6, cdItem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new SaidaLocalItem(rs.getInt("cd_saida"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_documento_saida"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_local_armazenamento"),
						rs.getInt("cd_pedido_venda"),
						(rs.getTimestamp("dt_saida")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_saida").getTime()),
						rs.getFloat("qt_saida"),
						rs.getFloat("qt_saida_consignada"),
						rs.getInt("st_saida_local_item"),
						rs.getString("id_saida_local_item"),
						rs.getInt("cd_item"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SaidaLocalItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SaidaLocalItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_saida_local_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SaidaLocalItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SaidaLocalItemDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM alm_saida_local_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
