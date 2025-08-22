package com.tivic.manager.alm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class DevolucaoItemDAO{

	public static int insert(DevolucaoItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(DevolucaoItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_devolucao_item (cd_documento_saida,"+
			                                  "cd_produto_servico,"+
			                                  "cd_empresa,"+
			                                  "cd_item,"+
			                                  "cd_documento_entrada," +
			                                  "cd_item_entrada,"+
			                                  "qt_devolvida,"+
			                                  "cd_unidade_medida) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
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
			if(objeto.getCdItem()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdItem());
			if(objeto.getCdDocumentoEntrada()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdDocumentoEntrada());
			if(objeto.getCdItemEntrada()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdItemEntrada());
			pstmt.setFloat(7,objeto.getQtDevolvida());
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdUnidadeMedida());
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

	public static int update(DevolucaoItem objeto) {
		return update(objeto, 0, 0, 0, 0, 0, 0, null);
	}

	public static int update(DevolucaoItem objeto, int cdDocumentoSaidaOld, int cdProdutoServicoOld, int cdEmpresaOld, int cdItemOld, int cdDocumentoEntradaOld, int cdItemEntradaOld) {
		return update(objeto, cdDocumentoSaidaOld, cdProdutoServicoOld, cdEmpresaOld, cdItemOld, cdDocumentoEntradaOld, cdItemEntradaOld, null);
	}

	public static int update(DevolucaoItem objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, 0, 0, connect);
	}

	public static int update(DevolucaoItem objeto, int cdDocumentoSaidaOld, int cdProdutoServicoOld, int cdEmpresaOld, int cdItemOld, int cdDocumentoEntradaOld, int cdItemEntradaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_devolucao_item SET cd_documento_saida=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_item=?,"+
												      		   "cd_documento_entrada=?," +
												      		   "cd_item_entrada=?,"+
												      		   "qt_devolvida=?,"+
												      		   "cd_unidade_medida=? WHERE cd_documento_saida=? AND cd_produto_servico=? AND cd_empresa=? AND cd_item=? AND cd_documento_entrada=? AND cd_item_entrada=?");
			pstmt.setInt(1,objeto.getCdDocumentoSaida());
			pstmt.setInt(2,objeto.getCdProdutoServico());
			pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setInt(4,objeto.getCdItem());
			pstmt.setInt(5,objeto.getCdDocumentoEntrada());
			pstmt.setInt(6,objeto.getCdItemEntrada());
			pstmt.setFloat(7,objeto.getQtDevolvida());
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdUnidadeMedida());
			pstmt.setInt(9, cdDocumentoSaidaOld!=0 ? cdDocumentoSaidaOld : objeto.getCdDocumentoSaida());
			pstmt.setInt(10, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.setInt(11, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(12, cdItemOld!=0 ? cdItemOld : objeto.getCdItem());
			pstmt.setInt(13, cdDocumentoEntradaOld!=0 ? cdDocumentoEntradaOld : objeto.getCdDocumentoEntrada());
			pstmt.setInt(14, cdItemEntradaOld!=0 ? cdItemEntradaOld : objeto.getCdItemEntrada());
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

	public static int delete(int cdDocumentoSaida, int cdProdutoServico, int cdEmpresa, int cdItem, int cdDocumentoEntrada, int cdItemEntrada) {
		return delete(cdDocumentoSaida, cdProdutoServico, cdEmpresa, cdItem, cdDocumentoEntrada, cdItemEntrada, null);
	}

	public static int delete(int cdDocumentoSaida, int cdProdutoServico, int cdEmpresa, int cdItem, int cdDocumentoEntrada, int cdItemEntrada, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_devolucao_item WHERE cd_documento_saida=? AND cd_produto_servico=? AND cd_empresa=? AND cd_item=? AND cd_documento_entrada=? AND cd_item_entrada=?");
			pstmt.setInt(1, cdDocumentoSaida);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdEmpresa);
			pstmt.setInt(4, cdItem);
			pstmt.setInt(5, cdDocumentoEntrada);
			pstmt.setInt(6, cdItemEntrada);
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

	public static DevolucaoItem get(int cdDocumentoSaida, int cdProdutoServico, int cdEmpresa, int cdItem, int cdDocumentoEntrada, int cdItemEntrada) {
		return get(cdDocumentoSaida, cdProdutoServico, cdEmpresa, cdItem, cdDocumentoEntrada, cdItemEntrada, null);
	}

	public static DevolucaoItem get(int cdDocumentoSaida, int cdProdutoServico, int cdEmpresa, int cdItem, int cdDocumentoEntrada, int cdItemEntrada, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_devolucao_item WHERE cd_documento_saida=? AND cd_produto_servico=? AND cd_empresa=? AND cd_item=? AND cd_documento_entrada=? AND cd_item_entrada = ?");
			pstmt.setInt(1, cdDocumentoSaida);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdEmpresa);
			pstmt.setInt(4, cdItem);
			pstmt.setInt(5, cdDocumentoEntrada);
			pstmt.setInt(6, cdItemEntrada);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new DevolucaoItem(rs.getInt("cd_documento_saida"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_item"),
						rs.getInt("cd_documento_entrada"),
						rs.getInt("cd_item_entrada"),
						rs.getFloat("qt_devolvida"),
						rs.getInt("cd_unidade_medida"));
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_devolucao_item");
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
		return Search.find("SELECT * FROM alm_devolucao_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
