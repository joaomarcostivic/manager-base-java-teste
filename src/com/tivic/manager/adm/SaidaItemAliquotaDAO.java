package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class SaidaItemAliquotaDAO{

	public static int insert(SaidaItemAliquota objeto) {
		return insert(objeto, null);
	}

	public static int insert(SaidaItemAliquota objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_saida_item_aliquota (cd_produto_servico,"+
			                                  "cd_documento_saida,"+
			                                  "cd_empresa,"+
			                                  "cd_tributo_aliquota,"+
			                                  "cd_tributo,"+
			                                  "pr_aliquota,"+
			                                  "vl_base_calculo,"+
			                                  "cd_item) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdProdutoServico());
			if(objeto.getCdDocumentoSaida()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdDocumentoSaida());
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdEmpresa());
			if(objeto.getCdTributoAliquota()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdTributoAliquota());
			if(objeto.getCdTributo()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdTributo());
			pstmt.setFloat(6,objeto.getPrAliquota());
			pstmt.setFloat(7,objeto.getVlBaseCalculo());
			if(objeto.getCdItem()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SaidaItemAliquotaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SaidaItemAliquotaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(SaidaItemAliquota objeto) {
		return update(objeto, 0, 0, 0, 0, 0, 0, null);
	}

	public static int update(SaidaItemAliquota objeto, int cdProdutoServicoOld, int cdDocumentoSaidaOld, int cdEmpresaOld, int cdTributoAliquotaOld, int cdTributoOld, int cdItemOld) {
		return update(objeto, cdProdutoServicoOld, cdDocumentoSaidaOld, cdEmpresaOld, cdTributoAliquotaOld, cdTributoOld, cdItemOld, null);
	}

	public static int update(SaidaItemAliquota objeto, Connection connect) {
		return update(objeto, 0, 0, 0, 0, 0, 0, connect);
	}

	public static int update(SaidaItemAliquota objeto, int cdProdutoServicoOld, int cdDocumentoSaidaOld, int cdEmpresaOld, int cdTributoAliquotaOld, int cdTributoOld, int cdItemOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_saida_item_aliquota SET cd_produto_servico=?,"+
												      		   "cd_documento_saida=?,"+
												      		   "cd_empresa=?,"+
												      		   "cd_tributo_aliquota=?,"+
												      		   "cd_tributo=?,"+
												      		   "pr_aliquota=?,"+
												      		   "vl_base_calculo=?,"+
												      		   "cd_item=? WHERE cd_produto_servico=? AND cd_documento_saida=? AND cd_empresa=? AND cd_tributo_aliquota=? AND cd_tributo=? AND cd_item=?");
			pstmt.setInt(1,objeto.getCdProdutoServico());
			pstmt.setInt(2,objeto.getCdDocumentoSaida());
			pstmt.setInt(3,objeto.getCdEmpresa());
			pstmt.setInt(4,objeto.getCdTributoAliquota());
			pstmt.setInt(5,objeto.getCdTributo());
			pstmt.setFloat(6,objeto.getPrAliquota());
			pstmt.setFloat(7,objeto.getVlBaseCalculo());
			pstmt.setInt(8,objeto.getCdItem());
			pstmt.setInt(9, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.setInt(10, cdDocumentoSaidaOld!=0 ? cdDocumentoSaidaOld : objeto.getCdDocumentoSaida());
			pstmt.setInt(11, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(12, cdTributoAliquotaOld!=0 ? cdTributoAliquotaOld : objeto.getCdTributoAliquota());
			pstmt.setInt(13, cdTributoOld!=0 ? cdTributoOld : objeto.getCdTributo());
			pstmt.setFloat(14, cdItemOld!=0 ? cdItemOld : objeto.getCdItem());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SaidaItemAliquotaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SaidaItemAliquotaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProdutoServico, int cdDocumentoSaida, int cdEmpresa, int cdTributoAliquota, int cdTributo, int cdItem) {
		return delete(cdProdutoServico, cdDocumentoSaida, cdEmpresa, cdTributoAliquota, cdTributo, cdItem, null);
	}

	public static int delete(int cdProdutoServico, int cdDocumentoSaida, int cdEmpresa, int cdTributoAliquota, int cdTributo, int cdItem, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_saida_item_aliquota WHERE cd_produto_servico=? AND cd_documento_saida=? AND cd_empresa=? AND cd_tributo_aliquota=? AND cd_tributo=? AND cd_item=?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.setInt(2, cdDocumentoSaida);
			pstmt.setInt(3, cdEmpresa);
			pstmt.setInt(4, cdTributoAliquota);
			pstmt.setInt(5, cdTributo);
			pstmt.setInt(6, cdItem);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SaidaItemAliquotaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! SaidaItemAliquotaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static SaidaItemAliquota get(int cdProdutoServico, int cdDocumentoSaida, int cdEmpresa, int cdTributoAliquota, int cdTributo, int cdItem) {
		return get(cdProdutoServico, cdDocumentoSaida, cdEmpresa, cdTributoAliquota, cdTributo, cdItem, null);
	}

	public static SaidaItemAliquota get(int cdProdutoServico, int cdDocumentoSaida, int cdEmpresa, int cdTributoAliquota, int cdTributo, int cdItem, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_saida_item_aliquota WHERE cd_produto_servico=? AND cd_documento_saida=? AND cd_empresa=? AND cd_tributo_aliquota=? AND cd_tributo=? AND cd_item=?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.setInt(2, cdDocumentoSaida);
			pstmt.setInt(3, cdEmpresa);
			pstmt.setInt(4, cdTributoAliquota);
			pstmt.setInt(5, cdTributo);
			pstmt.setInt(6, cdItem);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new SaidaItemAliquota(rs.getInt("cd_produto_servico"),
						rs.getInt("cd_documento_saida"),
						rs.getInt("cd_empresa"),
						rs.getInt("cd_tributo_aliquota"),
						rs.getInt("cd_tributo"),
						rs.getFloat("pr_aliquota"),
						rs.getFloat("vl_base_calculo"),
						rs.getInt("cd_item"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SaidaItemAliquotaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SaidaItemAliquotaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_saida_item_aliquota");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! SaidaItemAliquotaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! SaidaItemAliquotaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_saida_item_aliquota", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
