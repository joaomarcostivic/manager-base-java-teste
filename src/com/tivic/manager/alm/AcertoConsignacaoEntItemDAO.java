package com.tivic.manager.alm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class AcertoConsignacaoEntItemDAO{

	public static int insert(AcertoConsignacaoEntItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(AcertoConsignacaoEntItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO alm_acerto_consignacao_ent_item (cd_acerto_consignacao,"+
			                                  "cd_empresa,"+
			                                  "cd_produto_servico,"+
			                                  "qt_item,"+
			                                  "vl_item) VALUES (?, ?, ?, ?, ?)");
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
			pstmt.setFloat(4,objeto.getQtItem());
			pstmt.setFloat(5,objeto.getVlItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoEntItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoEntItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(AcertoConsignacaoEntItem objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(AcertoConsignacaoEntItem objeto, int cdAcertoConsignacaoOld, int cdEmpresaOld, int cdProdutoServicoOld) {
		return update(objeto, cdAcertoConsignacaoOld, cdEmpresaOld, cdProdutoServicoOld, null);
	}

	public static int update(AcertoConsignacaoEntItem objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(AcertoConsignacaoEntItem objeto, int cdAcertoConsignacaoOld, int cdEmpresaOld, int cdProdutoServicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE alm_acerto_consignacao_ent_item SET cd_acerto_consignacao=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "qt_item=?,"+
												      		   "vl_item=? WHERE cd_acerto_consignacao=? AND cd_empresa=? AND cd_produto_servico=?");
			pstmt.setInt(1,objeto.getCdAcertoConsignacao());
			pstmt.setInt(2,objeto.getCdEmpresa());
			pstmt.setInt(3,objeto.getCdProdutoServico());
			pstmt.setFloat(4,objeto.getQtItem());
			pstmt.setFloat(5,objeto.getVlItem());
			pstmt.setInt(6, cdAcertoConsignacaoOld!=0 ? cdAcertoConsignacaoOld : objeto.getCdAcertoConsignacao());
			pstmt.setInt(7, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(8, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoEntItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoEntItemDAO.update: " +  e);
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
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM alm_acerto_consignacao_ent_item WHERE cd_acerto_consignacao=? AND cd_empresa=? AND cd_produto_servico=?");
			pstmt.setInt(1, cdAcertoConsignacao);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdProdutoServico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoEntItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoEntItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static AcertoConsignacaoEntItem get(int cdAcertoConsignacao, int cdEmpresa, int cdProdutoServico) {
		return get(cdAcertoConsignacao, cdEmpresa, cdProdutoServico, null);
	}

	public static AcertoConsignacaoEntItem get(int cdAcertoConsignacao, int cdEmpresa, int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM alm_acerto_consignacao_ent_item WHERE cd_acerto_consignacao=? AND cd_empresa=? AND cd_produto_servico=?");
			pstmt.setInt(1, cdAcertoConsignacao);
			pstmt.setInt(2, cdEmpresa);
			pstmt.setInt(3, cdProdutoServico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new AcertoConsignacaoEntItem(rs.getInt("cd_acerto_consignacao"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_produto_servico"),
						rs.getFloat("qt_item"),
						rs.getFloat("vl_item"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoEntItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoEntItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM alm_acerto_consignacao_ent_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoEntItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! AcertoConsignacaoEntItemDAO.getAll: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<sol.dao.ItemComparator> criterios, Connection connect) {
		return Search.find("SELECT * FROM alm_acerto_consignacao_ent_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
