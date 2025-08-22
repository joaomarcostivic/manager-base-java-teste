package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TabelaPrecoRegraDAO{

	public static int insert(TabelaPrecoRegra objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(TabelaPrecoRegra objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_tabela_preco");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdTabelaPreco()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_regra");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("adm_tabela_preco_regra", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdRegra(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_tabela_preco_regra (cd_tabela_preco,"+
			                                  "cd_regra,"+
			                                  "cd_tabela_preco_base,"+
			                                  "cd_produto_servico,"+
			                                  "cd_fornecedor,"+
			                                  "cd_grupo,"+
			                                  "pr_desconto,"+
			                                  "pr_margem_lucro,"+
			                                  "pr_margem_minima,"+
			                                  "pr_margem_maxima,"+
			                                  "lg_incluir_impostos,"+
			                                  "lg_preco_minimo,"+
			                                  "tp_aproximacao,"+
			                                  "nr_prioridade,"+
			                                  "tp_valor_base) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTabelaPreco());
			pstmt.setInt(2, code);
			if(objeto.getCdTabelaPrecoBase()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTabelaPrecoBase());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdProdutoServico());
			if(objeto.getCdFornecedor()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdFornecedor());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdGrupo());
			pstmt.setFloat(7,objeto.getPrDesconto());
			pstmt.setFloat(8,objeto.getPrMargemLucro());
			pstmt.setFloat(9,objeto.getPrMargemMinima());
			pstmt.setFloat(10,objeto.getPrMargemMaxima());
			pstmt.setInt(11,objeto.getLgIncluirImpostos());
			pstmt.setInt(12,objeto.getLgPrecoMinimo());
			pstmt.setInt(13,objeto.getTpAproximacao());
			pstmt.setInt(14,objeto.getNrPrioridade());
			pstmt.setInt(15,objeto.getTpValorBase());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaPrecoRegraDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaPrecoRegraDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TabelaPrecoRegra objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TabelaPrecoRegra objeto, int cdTabelaPrecoOld, int cdRegraOld) {
		return update(objeto, cdTabelaPrecoOld, cdRegraOld, null);
	}

	public static int update(TabelaPrecoRegra objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TabelaPrecoRegra objeto, int cdTabelaPrecoOld, int cdRegraOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_tabela_preco_regra SET cd_tabela_preco=?,"+
												      		   "cd_regra=?,"+
												      		   "cd_tabela_preco_base=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_fornecedor=?,"+
												      		   "cd_grupo=?,"+
												      		   "pr_desconto=?,"+
												      		   "pr_margem_lucro=?,"+
												      		   "pr_margem_minima=?,"+
												      		   "pr_margem_maxima=?,"+
												      		   "lg_incluir_impostos=?,"+
												      		   "lg_preco_minimo=?,"+
												      		   "tp_aproximacao=?,"+
												      		   "nr_prioridade=?,"+
												      		   "tp_valor_base=? WHERE cd_tabela_preco=? AND cd_regra=?");
			pstmt.setInt(1,objeto.getCdTabelaPreco());
			pstmt.setInt(2,objeto.getCdRegra());
			if(objeto.getCdTabelaPrecoBase()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTabelaPrecoBase());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdProdutoServico());
			if(objeto.getCdFornecedor()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdFornecedor());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdGrupo());
			pstmt.setFloat(7,objeto.getPrDesconto());
			pstmt.setFloat(8,objeto.getPrMargemLucro());
			pstmt.setFloat(9,objeto.getPrMargemMinima());
			pstmt.setFloat(10,objeto.getPrMargemMaxima());
			pstmt.setInt(11,objeto.getLgIncluirImpostos());
			pstmt.setInt(12,objeto.getLgPrecoMinimo());
			pstmt.setInt(13,objeto.getTpAproximacao());
			pstmt.setInt(14,objeto.getNrPrioridade());
			pstmt.setInt(15,objeto.getTpValorBase());
			pstmt.setInt(16, cdTabelaPrecoOld!=0 ? cdTabelaPrecoOld : objeto.getCdTabelaPreco());
			pstmt.setInt(17, cdRegraOld!=0 ? cdRegraOld : objeto.getCdRegra());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaPrecoRegraDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaPrecoRegraDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTabelaPreco, int cdRegra) {
		return delete(cdTabelaPreco, cdRegra, null);
	}

	public static int delete(int cdTabelaPreco, int cdRegra, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_tabela_preco_regra WHERE cd_tabela_preco=? AND cd_regra=?");
			pstmt.setInt(1, cdTabelaPreco);
			pstmt.setInt(2, cdRegra);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaPrecoRegraDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaPrecoRegraDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TabelaPrecoRegra get(int cdTabelaPreco, int cdRegra) {
		return get(cdTabelaPreco, cdRegra, null);
	}

	public static TabelaPrecoRegra get(int cdTabelaPreco, int cdRegra, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_tabela_preco_regra WHERE cd_tabela_preco=? AND cd_regra=?");
			pstmt.setInt(1, cdTabelaPreco);
			pstmt.setInt(2, cdRegra);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TabelaPrecoRegra(rs.getInt("cd_tabela_preco"),
						rs.getInt("cd_regra"),
						rs.getInt("cd_tabela_preco_base"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_fornecedor"),
						rs.getInt("cd_grupo"),
						rs.getFloat("pr_desconto"),
						rs.getFloat("pr_margem_lucro"),
						rs.getFloat("pr_margem_minima"),
						rs.getFloat("pr_margem_maxima"),
						rs.getInt("lg_incluir_impostos"),
						rs.getInt("lg_preco_minimo"),
						rs.getInt("tp_aproximacao"),
						rs.getInt("nr_prioridade"),
						rs.getInt("tp_valor_base"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaPrecoRegraDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaPrecoRegraDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_tabela_preco_regra");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaPrecoRegraDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaPrecoRegraDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_tabela_preco_regra", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
