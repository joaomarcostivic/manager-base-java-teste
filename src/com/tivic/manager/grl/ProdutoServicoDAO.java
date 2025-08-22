package com.tivic.manager.grl;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

public class ProdutoServicoDAO{

	public static int insert(ProdutoServico objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProdutoServico objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("grl_produto_servico", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}

			// Não permite gravar produto com nome nulo ou vazio
			if(objeto.getNmProdutoServico()==null || objeto.getNmProdutoServico().trim().equals(""))
				return -1;
			//

			objeto.setCdProdutoServico(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_produto_servico (cd_produto_servico,"+
			                                  "cd_categoria_economica,"+
			                                  "nm_produto_servico,"+
			                                  "txt_produto_servico,"+
			                                  "txt_especificacao,"+
			                                  "txt_dado_tecnico,"+
			                                  "txt_prazo_entrega,"+
			                                  "tp_produto_servico,"+
			                                  "id_produto_servico,"+
			                                  "sg_produto_servico,"+
			                                  "cd_classificacao_fiscal,"+
			                                  "cd_fabricante,"+
			                                  "cd_marca,"+
			                                  "nm_modelo," +
			                                  "cd_ncm," +
			                                  "nr_referencia," + 
			                                  "cd_categoria_receita," +
			                                  "cd_categoria_despesa," +
			                                  "vl_servico,"+
			                                  "lg_reembolsavel) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdCategoriaEconomica()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCategoriaEconomica());
			pstmt.setString(3,objeto.getNmProdutoServico());
			pstmt.setString(4,objeto.getTxtProdutoServico());
			pstmt.setString(5,objeto.getTxtEspecificacao());
			pstmt.setString(6,objeto.getTxtDadoTecnico());
			pstmt.setString(7,objeto.getTxtPrazoEntrega());
			pstmt.setInt(8,objeto.getTpProdutoServico());
			pstmt.setString(9,objeto.getIdProdutoServico());
			pstmt.setString(10,objeto.getSgProdutoServico());
			if(objeto.getCdClassificacaoFiscal()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdClassificacaoFiscal());
			if(objeto.getCdFabricante()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdFabricante());
			if(objeto.getCdMarca()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdMarca());
			pstmt.setString(14,objeto.getNmModelo());
			if(objeto.getCdNcm()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdNcm());
			pstmt.setString(16,objeto.getNrReferencia());
			if(objeto.getCdCategoriaReceita()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17, objeto.getCdCategoriaReceita());
			if(objeto.getCdCategoriaDespesa()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18, objeto.getCdCategoriaDespesa());
			
			pstmt.setDouble(19, objeto.getVlServico());
			pstmt.setInt(20, objeto.getLgReembolsavel());
				
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProdutoServico objeto) {
		return update(objeto, 0, null);
	}

	public static int update(ProdutoServico objeto, int cdProdutoServicoOld) {
		return update(objeto, cdProdutoServicoOld, null);
	}

	public static int update(ProdutoServico objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(ProdutoServico objeto, int cdProdutoServicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_produto_servico SET cd_produto_servico=?,"+
												      		   "cd_categoria_economica=?,"+
												      		   "nm_produto_servico=?,"+
												      		   "txt_produto_servico=?,"+
												      		   "txt_especificacao=?,"+
												      		   "txt_dado_tecnico=?,"+
												      		   "txt_prazo_entrega=?,"+
												      		   "tp_produto_servico=?,"+
												      		   "id_produto_servico=?,"+
												      		   "sg_produto_servico=?,"+
												      		   "cd_classificacao_fiscal=?,"+
												      		   "cd_fabricante=?,"+
												      		   "cd_marca=?,"+
												      		   "nm_modelo=?," +
												      		   "cd_ncm=?," +
												      		   "nr_referencia=?," +
												      		   "cd_categoria_receita=?," +
												      		   "cd_categoria_despesa=?," +
												      		   "vl_servico=?, "+
												      		   "lg_reembolsavel=? WHERE cd_produto_servico=?");
			pstmt.setInt(1,objeto.getCdProdutoServico());
			if(objeto.getCdCategoriaEconomica()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdCategoriaEconomica());
			pstmt.setString(3,objeto.getNmProdutoServico());
			pstmt.setString(4,objeto.getTxtProdutoServico());
			pstmt.setString(5,objeto.getTxtEspecificacao());
			pstmt.setString(6,objeto.getTxtDadoTecnico());
			pstmt.setString(7,objeto.getTxtPrazoEntrega());
			pstmt.setInt(8,objeto.getTpProdutoServico());
			pstmt.setString(9,objeto.getIdProdutoServico());
			pstmt.setString(10,objeto.getSgProdutoServico());
			if(objeto.getCdClassificacaoFiscal()==0)
				pstmt.setNull(11, Types.INTEGER);
			else
				pstmt.setInt(11,objeto.getCdClassificacaoFiscal());
			if(objeto.getCdFabricante()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdFabricante());
			if(objeto.getCdMarca()==0)
				pstmt.setNull(13, Types.INTEGER);
			else
				pstmt.setInt(13,objeto.getCdMarca());
			pstmt.setString(14,objeto.getNmModelo());
			if(objeto.getCdNcm()==0)
				pstmt.setNull(15, Types.INTEGER);
			else
				pstmt.setInt(15,objeto.getCdNcm());
			pstmt.setString(16,objeto.getNrReferencia());
			if(objeto.getCdCategoriaReceita()==0)
				pstmt.setNull(17, Types.INTEGER);
			else
				pstmt.setInt(17, objeto.getCdCategoriaReceita());
			if(objeto.getCdCategoriaDespesa()==0)
				pstmt.setNull(18, Types.INTEGER);
			else
				pstmt.setInt(18, objeto.getCdCategoriaDespesa());
			pstmt.setDouble(19, objeto.getVlServico());
			pstmt.setInt(20, objeto.getLgReembolsavel());
			pstmt.setInt(21, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProdutoServico) {
		return delete(cdProdutoServico, null);
	}

	public static int delete(int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_produto_servico WHERE cd_produto_servico=?");
			pstmt.setInt(1, cdProdutoServico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProdutoServico get(int cdProdutoServico) {
		return get(cdProdutoServico, null);
	}

	public static ProdutoServico get(int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_produto_servico WHERE cd_produto_servico=?");
			pstmt.setInt(1, cdProdutoServico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProdutoServico(rs.getInt("cd_produto_servico"),
						rs.getInt("cd_categoria_economica"),
						rs.getString("nm_produto_servico"),
						rs.getString("txt_produto_servico"),
						rs.getString("txt_especificacao"),
						rs.getString("txt_dado_tecnico"),
						rs.getString("txt_prazo_entrega"),
						rs.getInt("tp_produto_servico"),
						rs.getString("id_produto_servico"),
						rs.getString("sg_produto_servico"),
						rs.getInt("cd_classificacao_fiscal"),
						rs.getInt("cd_fabricante"),
						rs.getInt("cd_marca"),
						rs.getString("nm_modelo"),
						rs.getInt("cd_ncm"),
						rs.getString("nr_referencia"),
						rs.getInt("cd_categoria_receita"),
						rs.getInt("cd_categoria_despesa"),
						rs.getDouble("vl_servico"),
						rs.getInt("lg_reembolsavel"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_produto_servico");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_produto_servico", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
