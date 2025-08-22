package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class EntradaItemAliquotaDAO{

	public static int insert(EntradaItemAliquota objeto) {
		return insert(objeto, null);
	}

	public static int insert(EntradaItemAliquota objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_entrada_item_aliquota (cd_produto_servico,"+
			                                  "cd_documento_entrada,"+
			                                  "cd_empresa,"+
			                                  "cd_tributo_aliquota,"+
			                                  "cd_tributo," +
			                                  "cd_item,"+
			                                  "pr_aliquota,"+
			                                  "vl_base_calculo," +
			                                  "cd_situacao_tributaria) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1,objeto.getCdProdutoServico());
			pstmt.setInt(2,objeto.getCdDocumentoEntrada());
			pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setInt(4,objeto.getCdTributoAliquota());
			pstmt.setInt(5,objeto.getCdTributo());
			pstmt.setInt(6,objeto.getCdItem());
			pstmt.setFloat(7,objeto.getPrAliquota());
			pstmt.setFloat(8,objeto.getVlBaseCalculo());
			pstmt.setInt(9, objeto.getCdSituacaoTributaria());
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

	public static int update(EntradaItemAliquota objeto) {
		return update(objeto, 0, 0, 0, 0, 0, 0, null);
	}

	public static int update(EntradaItemAliquota objeto, int cdProdutoServicoOld, int cdDocumentoEntradaOld, int cdEmpresaOld, int cdTributoAliquotaOld, int cdTributoOld, int cdItemOld) {
		return update(objeto, cdProdutoServicoOld, cdDocumentoEntradaOld, cdEmpresaOld, cdTributoAliquotaOld, cdTributoOld, cdItemOld, null);
	}

	public static int update(EntradaItemAliquota objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, 0, 0, connect);
	}

	public static int update(EntradaItemAliquota objeto, int cdProdutoServicoOld, int cdDocumentoEntradaOld, int cdEmpresaOld, int cdTributoAliquotaOld, int cdTributoOld, int cdItemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_entrada_item_aliquota SET cd_produto_servico=?,"+
												      		   "cd_documento_entrada=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_tributo_aliquota=?,"+
												      		   "cd_tributo=?," +
												      		   "cd_item=?,"+
												      		   "pr_aliquota=?,"+
												      		   "vl_base_calculo=?," +
												      		   "cd_situacao_tributaria=? WHERE cd_produto_servico=? AND cd_documento_entrada=? AND cd_empresa=? AND cd_tributo_aliquota=? AND cd_tributo=? AND cd_item=?");
			pstmt.setInt(1,objeto.getCdProdutoServico());
			pstmt.setInt(2,objeto.getCdDocumentoEntrada());
			pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setInt(4,objeto.getCdTributoAliquota());
			pstmt.setInt(5,objeto.getCdTributo());
			pstmt.setInt(6,objeto.getCdItem());
			pstmt.setFloat(7,objeto.getPrAliquota());
			pstmt.setFloat(8,objeto.getVlBaseCalculo());
			pstmt.setInt(9, objeto.getCdSituacaoTributaria());
			pstmt.setInt(10, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.setInt(11, cdDocumentoEntradaOld!=0 ? cdDocumentoEntradaOld : objeto.getCdDocumentoEntrada());
			pstmt.setInt(12, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(13, cdTributoAliquotaOld!=0 ? cdTributoAliquotaOld : objeto.getCdTributoAliquota());
			pstmt.setInt(14, cdTributoOld!=0 ? cdTributoOld : objeto.getCdTributo());
			pstmt.setInt(15, cdItemOld!=0 ? cdItemOld : objeto.getCdItem());
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

	public static int delete(int cdProdutoServico, int cdDocumentoEntrada, int cdEmpresa, int cdTributoAliquota, int cdTributo, int cdItem) {
		return delete(cdProdutoServico, cdDocumentoEntrada, cdEmpresa, cdTributoAliquota, cdTributo, cdItem, null);
	}

	public static int delete(int cdProdutoServico, int cdDocumentoEntrada, int cdEmpresa, int cdTributoAliquota, int cdTributo, int cdItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_entrada_item_aliquota WHERE cd_produto_servico=? AND cd_documento_entrada=? AND cd_empresa=? AND cd_tributo_aliquota=? AND cd_tributo=? AND cd_item=?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.setInt(2, cdDocumentoEntrada);
			pstmt.setInt(3, cdEmpresa);
			pstmt.setInt(4, cdTributoAliquota);
			pstmt.setInt(5, cdTributo);
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

	public static EntradaItemAliquota get(int cdProdutoServico, int cdDocumentoEntrada, int cdEmpresa, int cdTributoAliquota, int cdTributo, int cdItem) {
		return get(cdProdutoServico, cdDocumentoEntrada, cdEmpresa, cdTributoAliquota, cdTributo, cdItem, null);
	}

	public static EntradaItemAliquota get(int cdProdutoServico, int cdDocumentoEntrada, int cdEmpresa, int cdTributoAliquota, int cdTributo, int cdItem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_entrada_item_aliquota WHERE cd_produto_servico=? AND cd_documento_entrada=? AND cd_empresa=? AND cd_tributo_aliquota=? AND cd_tributo=?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.setInt(2, cdDocumentoEntrada);
			pstmt.setInt(3, cdEmpresa);
			pstmt.setInt(4, cdTributoAliquota);
			pstmt.setInt(5, cdTributo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new EntradaItemAliquota(rs.getInt("cd_produto_servico"),
						rs.getInt("cd_documento_entrada"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_tributo_aliquota"),
						rs.getInt("cd_tributo"),
						rs.getInt("cd_item"),
						rs.getFloat("pr_aliquota"),
						rs.getFloat("vl_base_calculo"),
						rs.getInt("cd_situacao_tributaria"));
			}
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! EntradaItemAliquotaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_entrada_item_aliquota");
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
		return Search.find("SELECT * FROM adm_entrada_item_aliquota", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
