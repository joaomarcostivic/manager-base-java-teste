package com.tivic.manager.alm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class AcertoConsignacaoSaiItemDAO{

	public static int insert(AcertoConsignacaoSaiItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(AcertoConsignacaoSaiItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_acerto_consignacao_sai_item (cd_acerto_consignacao,"+
			                                  "cd_empresa,"+
			                                  "cd_produto_servico,"+
			                                  "vl_item,"+
			                                  "qt_item_consignado,"+
			                                  "qt_item_nao_consignado) VALUES (?, ?, ?, ?, ?, ?)");
			if(objeto.getCdAcertoConsignacao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdAcertoConsignacao());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdEmpresa());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdProdutoServico());
			pstmt.setFloat(4,objeto.getVlItem());
			pstmt.setFloat(5,objeto.getQtItemConsignado());
			pstmt.setFloat(6,objeto.getQtItemNaoConsignado());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoSaiItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoSaiItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AcertoConsignacaoSaiItem objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(AcertoConsignacaoSaiItem objeto, int cdAcertoConsignacaoOld, int cdEmpresaOld, int cdProdutoServicoOld) {
		return update(objeto, cdAcertoConsignacaoOld, cdEmpresaOld, cdProdutoServicoOld, null);
	}

	public static int update(AcertoConsignacaoSaiItem objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(AcertoConsignacaoSaiItem objeto, int cdAcertoConsignacaoOld, int cdEmpresaOld, int cdProdutoServicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_acerto_consignacao_sai_item SET cd_acerto_consignacao=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "vl_item=?,"+
												      		   "qt_item_consignado=?,"+
												      		   "qt_item_nao_consignado=? WHERE cd_acerto_consignacao=? AND cd_empresa=? AND cd_produto_servico=?");
			pstmt.setInt(1,objeto.getCdAcertoConsignacao());
			pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setInt(3,objeto.getCdProdutoServico());
			pstmt.setFloat(4,objeto.getVlItem());
			pstmt.setFloat(5,objeto.getQtItemConsignado());
			pstmt.setFloat(6,objeto.getQtItemNaoConsignado());
			pstmt.setInt(7, cdAcertoConsignacaoOld!=0 ? cdAcertoConsignacaoOld : objeto.getCdAcertoConsignacao());
			pstmt.setInt(8, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(9, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoSaiItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoSaiItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdAcertoConsignacao, int cdEmpresa, int cdProdutoServico) {
		return delete(cdAcertoConsignacao, cdEmpresa, cdProdutoServico, null);
	}

	public static int delete(int cdAcertoConsignacao, int cdEmpresa, int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_acerto_consignacao_sai_item WHERE cd_acerto_consignacao=? AND cd_empresa=? AND cd_produto_servico=?");
			pstmt.setInt(1, cdAcertoConsignacao);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdProdutoServico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoSaiItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoSaiItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AcertoConsignacaoSaiItem get(int cdAcertoConsignacao, int cdEmpresa, int cdProdutoServico) {
		return get(cdAcertoConsignacao, cdEmpresa, cdProdutoServico, null);
	}

	public static AcertoConsignacaoSaiItem get(int cdAcertoConsignacao, int cdEmpresa, int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_acerto_consignacao_sai_item WHERE cd_acerto_consignacao=? AND cd_empresa=? AND cd_produto_servico=?");
			pstmt.setInt(1, cdAcertoConsignacao);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdProdutoServico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AcertoConsignacaoSaiItem(rs.getInt("cd_acerto_consignacao"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_produto_servico"),
						rs.getFloat("vl_item"),
						rs.getFloat("qt_item_consignado"),
						rs.getFloat("qt_item_nao_consignado"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoSaiItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoSaiItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_acerto_consignacao_sai_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoSaiItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoSaiItemDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM alm_acerto_consignacao_sai_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
