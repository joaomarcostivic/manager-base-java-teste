package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class SolicitacaoMaterialItemDAO{

	public static int insert(SolicitacaoMaterialItem objeto) {
		return insert(objeto, null);
	}

	public static int insert(SolicitacaoMaterialItem objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_solicitacao_material_item (cd_produto_servico,"+
			                                  "cd_solicitacao_material,"+
			                                  "cd_unidade_medida,"+
			                                  "qt_solicitada) VALUES (?, ?, ?, ?)");
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdProdutoServico());
			if(objeto.getCdSolicitacaoMaterial()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdSolicitacaoMaterial());
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdUnidadeMedida());
			pstmt.setFloat(4,objeto.getQtSolicitada());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SolicitacaoMaterialItemDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SolicitacaoMaterialItemDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(SolicitacaoMaterialItem objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(SolicitacaoMaterialItem objeto, int cdProdutoServicoOld, int cdSolicitacaoMaterialOld) {
		return update(objeto, cdProdutoServicoOld, cdSolicitacaoMaterialOld, null);
	}

	public static int update(SolicitacaoMaterialItem objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(SolicitacaoMaterialItem objeto, int cdProdutoServicoOld, int cdSolicitacaoMaterialOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_solicitacao_material_item SET cd_produto_servico=?,"+
												      		   "cd_solicitacao_material=?,"+
												      		   "cd_unidade_medida=?,"+
												      		   "qt_solicitada=? WHERE cd_produto_servico=? AND cd_solicitacao_material=?");
			pstmt.setInt(1,objeto.getCdProdutoServico());
			pstmt.setInt(2,objeto.getCdSolicitacaoMaterial());
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdUnidadeMedida());
			pstmt.setFloat(4,objeto.getQtSolicitada());
			pstmt.setInt(5, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.setInt(6, cdSolicitacaoMaterialOld!=0 ? cdSolicitacaoMaterialOld : objeto.getCdSolicitacaoMaterial());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SolicitacaoMaterialItemDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SolicitacaoMaterialItemDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProdutoServico, int cdSolicitacaoMaterial) {
		return delete(cdProdutoServico, cdSolicitacaoMaterial, null);
	}

	public static int delete(int cdProdutoServico, int cdSolicitacaoMaterial, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_solicitacao_material_item WHERE cd_produto_servico=? AND cd_solicitacao_material=?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.setInt(2, cdSolicitacaoMaterial);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SolicitacaoMaterialItemDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SolicitacaoMaterialItemDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static SolicitacaoMaterialItem get(int cdProdutoServico, int cdSolicitacaoMaterial) {
		return get(cdProdutoServico, cdSolicitacaoMaterial, null);
	}

	public static SolicitacaoMaterialItem get(int cdProdutoServico, int cdSolicitacaoMaterial, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_solicitacao_material_item WHERE cd_produto_servico=? AND cd_solicitacao_material=?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.setInt(2, cdSolicitacaoMaterial);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new SolicitacaoMaterialItem(rs.getInt("cd_produto_servico"),
						rs.getInt("cd_solicitacao_material"),
						rs.getInt("cd_unidade_medida"),
						rs.getFloat("qt_solicitada"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SolicitacaoMaterialItemDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SolicitacaoMaterialItemDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_solicitacao_material_item");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SolicitacaoMaterialItemDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SolicitacaoMaterialItemDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_solicitacao_material_item", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
