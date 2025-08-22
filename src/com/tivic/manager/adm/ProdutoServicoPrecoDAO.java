package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class ProdutoServicoPrecoDAO{

	public static int insert(ProdutoServicoPreco objeto) {
		return insert(objeto, null);
	}

	public static int insert(ProdutoServicoPreco objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_tabela_preco");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdTabelaPreco()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_produto_servico");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdProdutoServico()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_produto_servico_preco");
			keys[2].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("adm_produto_servico_preco", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdProdutoServicoPreco(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_produto_servico_preco (cd_tabela_preco,"+
			                                  "cd_produto_servico,"+
			                                  "cd_produto_servico_preco,"+
			                                  "dt_termino_validade,"+
			                                  "vl_preco,"+
			                                  "vl_preco_minimo,"+
			                                  "vl_preco_maximo,"+
			                                  "vl_preco_base,"+
			                                  "vl_diferenca_preco_padrao,"+
			                                  "vl_diferenca_preco_minimo,"+
			                                  "vl_diferenca_preco_maximo,"+
			                                  "cd_tabela_preco_base) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdTabelaPreco()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTabelaPreco());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdProdutoServico());
			pstmt.setInt(3, code);
			if(objeto.getDtTerminoValidade()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtTerminoValidade().getTimeInMillis()));
			pstmt.setFloat(5,objeto.getVlPreco());
			pstmt.setFloat(6,objeto.getVlPrecoMinimo());
			pstmt.setFloat(7,objeto.getVlPrecoMaximo());
			pstmt.setFloat(8,objeto.getVlPrecoBase());
			pstmt.setFloat(9,objeto.getVlDiferencaPrecoPadrao());
			pstmt.setFloat(10,objeto.getVlDiferencaPrecoMinimo());
			pstmt.setFloat(11,objeto.getVlDiferencaPrecoMaximo());
			if(objeto.getCdTabelaPrecoBase()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdTabelaPrecoBase());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoPrecoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoPrecoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(ProdutoServicoPreco objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(ProdutoServicoPreco objeto, int cdTabelaPrecoOld, int cdProdutoServicoOld, int cdProdutoServicoPrecoOld) {
		return update(objeto, cdTabelaPrecoOld, cdProdutoServicoOld, cdProdutoServicoPrecoOld, null);
	}

	public static int update(ProdutoServicoPreco objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(ProdutoServicoPreco objeto, int cdTabelaPrecoOld, int cdProdutoServicoOld, int cdProdutoServicoPrecoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_produto_servico_preco SET cd_tabela_preco=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_produto_servico_preco=?,"+
												      		   "dt_termino_validade=?,"+
												      		   "vl_preco=?,"+
												      		   "vl_preco_minimo=?,"+
												      		   "vl_preco_maximo=?,"+
												      		   "vl_preco_base=?,"+
												      		   "vl_diferenca_preco_padrao=?,"+
												      		   "vl_diferenca_preco_minimo=?,"+
												      		   "vl_diferenca_preco_maximo=?,"+
												      		   "cd_tabela_preco_base=? WHERE cd_tabela_preco=? AND cd_produto_servico=? AND cd_produto_servico_preco=?");
			pstmt.setInt(1,objeto.getCdTabelaPreco());
			pstmt.setInt(2,objeto.getCdProdutoServico());
			pstmt.setInt(3,objeto.getCdProdutoServicoPreco());
			if(objeto.getDtTerminoValidade()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtTerminoValidade().getTimeInMillis()));
			pstmt.setFloat(5,objeto.getVlPreco());
			pstmt.setFloat(6,objeto.getVlPrecoMinimo());
			pstmt.setFloat(7,objeto.getVlPrecoMaximo());
			pstmt.setFloat(8,objeto.getVlPrecoBase());
			pstmt.setFloat(9,objeto.getVlDiferencaPrecoPadrao());
			pstmt.setFloat(10,objeto.getVlDiferencaPrecoMinimo());
			pstmt.setFloat(11,objeto.getVlDiferencaPrecoMaximo());
			if(objeto.getCdTabelaPrecoBase()==0)
				pstmt.setNull(12, Types.INTEGER);
			else
				pstmt.setInt(12,objeto.getCdTabelaPrecoBase());
			pstmt.setInt(13, cdTabelaPrecoOld!=0 ? cdTabelaPrecoOld : objeto.getCdTabelaPreco());
			pstmt.setInt(14, cdProdutoServicoOld!=0 ? cdProdutoServicoOld : objeto.getCdProdutoServico());
			pstmt.setInt(15, cdProdutoServicoPrecoOld!=0 ? cdProdutoServicoPrecoOld : objeto.getCdProdutoServicoPreco());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoPrecoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoPrecoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTabelaPreco, int cdProdutoServico, int cdProdutoServicoPreco) {
		return delete(cdTabelaPreco, cdProdutoServico, cdProdutoServicoPreco, null);
	}

	public static int delete(int cdTabelaPreco, int cdProdutoServico, int cdProdutoServicoPreco, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_produto_servico_preco WHERE cd_tabela_preco=? AND cd_produto_servico=? AND cd_produto_servico_preco=?");
			pstmt.setInt(1, cdTabelaPreco);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdProdutoServicoPreco);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoPrecoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoPrecoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProdutoServicoPreco get(int cdTabelaPreco, int cdProdutoServico, int cdProdutoServicoPreco) {
		return get(cdTabelaPreco, cdProdutoServico, cdProdutoServicoPreco, null);
	}

	public static ProdutoServicoPreco get(int cdTabelaPreco, int cdProdutoServico, int cdProdutoServicoPreco, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_produto_servico_preco WHERE cd_tabela_preco=? AND cd_produto_servico=? AND cd_produto_servico_preco=?");
			pstmt.setInt(1, cdTabelaPreco);
			pstmt.setInt(2, cdProdutoServico);
			pstmt.setInt(3, cdProdutoServicoPreco);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProdutoServicoPreco(rs.getInt("cd_tabela_preco"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_produto_servico_preco"),
						(rs.getTimestamp("dt_termino_validade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_termino_validade").getTime()),
						rs.getFloat("vl_preco"),
						rs.getFloat("vl_preco_minimo"),
						rs.getFloat("vl_preco_maximo"),
						rs.getFloat("vl_preco_base"),
						rs.getFloat("vl_diferenca_preco_padrao"),
						rs.getFloat("vl_diferenca_preco_minimo"),
						rs.getFloat("vl_diferenca_preco_maximo"),
						rs.getInt("cd_tabela_preco_base"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoPrecoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoPrecoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_produto_servico_preco");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoPrecoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoPrecoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_produto_servico_preco", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
