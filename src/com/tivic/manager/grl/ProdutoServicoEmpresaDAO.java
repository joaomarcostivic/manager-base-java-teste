package com.tivic.manager.grl;

import java.sql.*;

import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;

import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class ProdutoServicoEmpresaDAO{

	public static int insert(ProdutoServicoEmpresa objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProdutoServicoEmpresa objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO grl_produto_servico_empresa (cd_empresa,"+
			                                  "cd_produto_servico,"+
			                                  "cd_unidade_medida,"+
			                                  "id_reduzido,"+
			                                  "vl_preco_medio,"+
			                                  "vl_custo_medio,"+
			                                  "vl_ultimo_custo,"+
			                                  "qt_ideal,"+
			                                  "qt_minima,"+
			                                  "qt_maxima,"+
			                                  "qt_dias_estoque,"+
			                                  "qt_precisao_custo,"+
			                                  "qt_precisao_unidade,"+
			                                  "qt_dias_garantia,"+
			                                  "tp_reabastecimento,"+
			                                  "tp_controle_estoque,"+
			                                  "tp_transporte,"+
			                                  "st_produto_empresa,"+
			                                  "dt_desativacao,"+
			                                  "nr_ordem,"+
			                                  "lg_estoque_negativo," +
			                                  "tp_origem," + 
			                                  "id_fabrica," + 
			                                  "dt_ultima_alteracao," +
			                                  "dt_cadastro," +
			                                  "nr_serie," +
			                                  "img_produto," +
			                                  "pr_desconto_maximo," +
			                                  "cd_formulario," +
			                                  "dt_versao," +
			                                  "cd_local_armazenamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdEmpresa()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdEmpresa());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProdutoServico());
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdUnidadeMedida());
			pstmt.setString(4,objeto.getIdReduzido());
			pstmt.setFloat(5,objeto.getVlPrecoMedio());
			pstmt.setFloat(6,objeto.getVlCustoMedio());
			pstmt.setFloat(7,objeto.getVlUltimoCusto());
			pstmt.setFloat(8,objeto.getQtIdeal());
			pstmt.setFloat(9,objeto.getQtMinima());
			pstmt.setFloat(10,objeto.getQtMaxima());
			pstmt.setFloat(11,objeto.getQtDiasEstoque());
			pstmt.setInt(12,objeto.getQtPrecisaoCusto());
			pstmt.setInt(13,objeto.getQtPrecisaoUnidade());
			pstmt.setInt(14,objeto.getQtDiasGarantia());
			pstmt.setInt(15,objeto.getTpReabastecimento());
			pstmt.setInt(16,objeto.getTpControleEstoque());
			pstmt.setInt(17,objeto.getTpTransporte());
			pstmt.setInt(18,objeto.getStProdutoEmpresa());
			if(objeto.getDtDesativacao()==null)
				pstmt.setNull(19, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(19,new Timestamp(objeto.getDtDesativacao().getTimeInMillis()));
			pstmt.setString(20,objeto.getNrOrdem());
			pstmt.setInt(21,objeto.getLgEstoqueNegativo());
			pstmt.setInt(22,objeto.getTpOrigem());
			pstmt.setString(23,objeto.getIdFabrica());
			if(objeto.getDtUltimaAlteracao()==null)
				pstmt.setNull(24, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(24,new Timestamp(objeto.getDtUltimaAlteracao().getTimeInMillis()));
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(25, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(25,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setString(26,objeto.getNrSerie());
			if(objeto.getImgProduto()==null)
				pstmt.setNull(27, Types.BINARY);
			else
				pstmt.setBytes(27,objeto.getImgProduto());
			pstmt.setFloat(28,objeto.getPrDescontoMaximo());
			if(objeto.getCdFormulario() == 0)
				pstmt.setNull(29, Types.INTEGER);
			else
				pstmt.setInt(29,objeto.getCdFormulario());
			if(objeto.getDtVersao() == null)
				pstmt.setNull(30, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(30, new Timestamp(objeto.getDtVersao().getTimeInMillis()));
			if(objeto.getCdLocalArmazenamento() == 0)
				pstmt.setNull(31, Types.INTEGER);
			else
				pstmt.setInt(31,objeto.getCdLocalArmazenamento());
			pstmt.executeUpdate();
			return 1;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			com.tivic.manager.util.Util.registerLog(e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProdutoServicoEmpresa objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(ProdutoServicoEmpresa objeto, int cdEmpresaOld, int cdProdutoServicoOld) {
		return update(objeto, cdEmpresaOld, cdProdutoServicoOld, null);
	}

	public static int update(ProdutoServicoEmpresa objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(ProdutoServicoEmpresa objeto, int cdEmpresaOld, int cdProdutoServicoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE grl_produto_servico_empresa SET cd_empresa=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_unidade_medida=?,"+
												      		   "id_reduzido=?,"+
												      		   "vl_preco_medio=?,"+
												      		   "vl_custo_medio=?,"+
												      		   "vl_ultimo_custo=?,"+
												      		   "qt_ideal=?,"+
												      		   "qt_minima=?,"+
												      		   "qt_maxima=?,"+
												      		   "qt_dias_estoque=?,"+
												      		   "qt_precisao_custo=?,"+
												      		   "qt_precisao_unidade=?,"+
												      		   "qt_dias_garantia=?,"+
												      		   "tp_reabastecimento=?,"+
												      		   "tp_controle_estoque=?,"+
												      		   "tp_transporte=?,"+
												      		   "st_produto_empresa=?,"+
												      		   "dt_desativacao=?,"+
												      		   "nr_ordem=?,"+
												      		   "lg_estoque_negativo=?," +
												      		   "tp_origem=?," + 
												      		   "id_fabrica=?," + 
												      		   "dt_ultima_alteracao=?," +
												      		   "dt_cadastro=?," +
												      		   "nr_serie=?," +
												      		   "img_produto=?," +
												      		   "pr_desconto_maximo=?," +
							                                   "cd_formulario=?," +
							                                   "dt_versao=?," +
							                                   "cd_local_armazenamento=? WHERE cd_empresa=? AND cd_produto_servico=?");
			pstmt.setInt(1,objeto.getCdEmpresa());
			pstmt.setInt(2,objeto.getCdProdutoServico());
			if(objeto.getCdUnidadeMedida()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdUnidadeMedida());
			pstmt.setString(4,objeto.getIdReduzido());
			pstmt.setFloat(5,objeto.getVlPrecoMedio());
			pstmt.setFloat(6,objeto.getVlCustoMedio());
			pstmt.setFloat(7,objeto.getVlUltimoCusto());
			pstmt.setFloat(8,objeto.getQtIdeal());
			pstmt.setFloat(9,objeto.getQtMinima());
			pstmt.setFloat(10,objeto.getQtMaxima());
			pstmt.setFloat(11,objeto.getQtDiasEstoque());
			pstmt.setInt(12,objeto.getQtPrecisaoCusto());
			pstmt.setInt(13,objeto.getQtPrecisaoUnidade());
			pstmt.setInt(14,objeto.getQtDiasGarantia());
			pstmt.setInt(15,objeto.getTpReabastecimento());
			pstmt.setInt(16,objeto.getTpControleEstoque());
			pstmt.setInt(17,objeto.getTpTransporte());
			pstmt.setInt(18,objeto.getStProdutoEmpresa());
			if(objeto.getDtDesativacao()==null)
				pstmt.setNull(19, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(19,new Timestamp(objeto.getDtDesativacao().getTimeInMillis()));
			pstmt.setString(20,objeto.getNrOrdem());
			pstmt.setInt(21,objeto.getLgEstoqueNegativo());
			pstmt.setInt(22,objeto.getTpOrigem());
			pstmt.setString(23,objeto.getIdFabrica());
			if(objeto.getDtUltimaAlteracao()==null)
				pstmt.setNull(24, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(24,new Timestamp(objeto.getDtUltimaAlteracao().getTimeInMillis()));
			if(objeto.getDtCadastro()==null)
				pstmt.setNull(25, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(25,new Timestamp(objeto.getDtCadastro().getTimeInMillis()));
			pstmt.setString(26,objeto.getNrSerie());
			if(objeto.getImgProduto()==null)
				pstmt.setNull(27, Types.BINARY);
			else
				pstmt.setBytes(27,objeto.getImgProduto());
			pstmt.setFloat(28,objeto.getPrDescontoMaximo());
			if(objeto.getCdFormulario() == 0)
				pstmt.setNull(29, Types.INTEGER);
			else
				pstmt.setInt(29,objeto.getCdFormulario());
			if(objeto.getDtVersao()==null)
				pstmt.setNull(30, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(30,new Timestamp(objeto.getDtVersao().getTimeInMillis()));
			if(objeto.getCdLocalArmazenamento() == 0)
				pstmt.setNull(31, Types.INTEGER);
			else
				pstmt.setInt(31,objeto.getCdLocalArmazenamento());
			pstmt.setInt(32, cdEmpresaOld!=0 ? cdEmpresaOld : objeto.getCdEmpresa());
			pstmt.setInt(33, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoEmpresaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoEmpresaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdEmpresa, int cdProdutoServico) {
		return delete(cdEmpresa, cdProdutoServico, null);
	}

	public static int delete(int cdEmpresa, int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM grl_produto_servico_empresa WHERE cd_empresa=? AND cd_produto_servico=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoEmpresaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoEmpresaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProdutoServicoEmpresa get(int cdEmpresa, int cdProdutoServico) {
		return get(cdEmpresa, cdProdutoServico, null);
	}

	public static ProdutoServicoEmpresa get(int cdEmpresa, int cdProdutoServico, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM grl_produto_servico_empresa WHERE cd_empresa=? AND cd_produto_servico=?");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, cdProdutoServico);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProdutoServicoEmpresa(rs.getInt("cd_empresa"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_unidade_medida"),
						rs.getString("id_reduzido"),
						rs.getFloat("vl_preco_medio"),
						rs.getFloat("vl_custo_medio"),
						rs.getFloat("vl_ultimo_custo"),
						rs.getFloat("qt_ideal"),
						rs.getFloat("qt_minima"),
						rs.getFloat("qt_maxima"),
						rs.getFloat("qt_dias_estoque"),
						rs.getInt("qt_precisao_custo"),
						rs.getInt("qt_precisao_unidade"),
						rs.getInt("qt_dias_garantia"),
						rs.getInt("tp_reabastecimento"),
						rs.getInt("tp_controle_estoque"),
						rs.getInt("tp_transporte"),
						rs.getInt("st_produto_empresa"),
						(rs.getTimestamp("dt_desativacao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_desativacao").getTime()),
						rs.getString("nr_ordem"),
						rs.getInt("lg_estoque_negativo"),
						rs.getInt("tp_origem"),
						rs.getString("id_fabrica"),
						(rs.getTimestamp("dt_ultima_alteracao")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_ultima_alteracao").getTime()),
						(rs.getTimestamp("dt_cadastro")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_cadastro").getTime()),
						rs.getString("nr_serie"),
						rs.getBytes("img_produto")==null?null:rs.getBytes("img_produto"),
						rs.getFloat("pr_desconto_maximo"),
						rs.getInt("cd_formulario"),
						(rs.getTimestamp("dt_versao")==null)?null:Util.longToCalendar(rs.getTimestamp("dtVersao").getTime()),
						rs.getInt("cd_local_armazenamento"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoEmpresaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoEmpresaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM grl_produto_servico_empresa");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoEmpresaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoEmpresaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM grl_produto_servico_empresa", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
