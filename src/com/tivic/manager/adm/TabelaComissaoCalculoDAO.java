package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TabelaComissaoCalculoDAO{

	public static int insert(TabelaComissaoCalculo objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(TabelaComissaoCalculo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_tabela_comissao");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdTabelaComissao()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_calculo");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("adm_tabela_comissao_calculo", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdCalculo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_tabela_comissao_calculo (cd_tabela_comissao,"+
			                                  "cd_calculo,"+
			                                  "cd_calculo_origem,"+
			                                  "vl_inicial,"+
			                                  "vl_final,"+
			                                  "tp_aplicacao,"+
			                                  "vl_aplicacao,"+
			                                  "vl_teto,"+
			                                  "tp_variacao_base,"+
			                                  "vl_variacao_base,"+
			                                  "tp_variacao_resultado,"+
			                                  "vl_variacao_resultado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdTabelaComissao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTabelaComissao());
			pstmt.setInt(2, code);
			if(objeto.getCdCalculoOrigem()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCalculoOrigem());
			pstmt.setFloat(4,objeto.getVlInicial());
			pstmt.setFloat(5,objeto.getVlFinal());
			pstmt.setInt(6,objeto.getTpAplicacao());
			pstmt.setFloat(7,objeto.getVlAplicacao());
			pstmt.setFloat(8,objeto.getVlTeto());
			pstmt.setInt(9,objeto.getTpVariacaoBase());
			pstmt.setFloat(10,objeto.getVlVariacaoBase());
			pstmt.setInt(11,objeto.getTpVariacaoResultado());
			pstmt.setFloat(12,objeto.getVlVariacaoResultado());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoCalculoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoCalculoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TabelaComissaoCalculo objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TabelaComissaoCalculo objeto, int cdTabelaComissaoOld, int cdCalculoOld) {
		return update(objeto, cdTabelaComissaoOld, cdCalculoOld, null);
	}

	public static int update(TabelaComissaoCalculo objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TabelaComissaoCalculo objeto, int cdTabelaComissaoOld, int cdCalculoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_tabela_comissao_calculo SET cd_tabela_comissao=?,"+
												      		   "cd_calculo=?,"+
												      		   "cd_calculo_origem=?,"+
												      		   "vl_inicial=?,"+
												      		   "vl_final=?,"+
												      		   "tp_aplicacao=?,"+
												      		   "vl_aplicacao=?,"+
												      		   "vl_teto=?,"+
												      		   "tp_variacao_base=?,"+
												      		   "vl_variacao_base=?,"+
												      		   "tp_variacao_resultado=?,"+
												      		   "vl_variacao_resultado=? WHERE cd_tabela_comissao=? AND cd_calculo=?");
			pstmt.setInt(1,objeto.getCdTabelaComissao());
			pstmt.setInt(2,objeto.getCdCalculo());
			if(objeto.getCdCalculoOrigem()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdCalculoOrigem());
			pstmt.setFloat(4,objeto.getVlInicial());
			pstmt.setFloat(5,objeto.getVlFinal());
			pstmt.setInt(6,objeto.getTpAplicacao());
			pstmt.setFloat(7,objeto.getVlAplicacao());
			pstmt.setFloat(8,objeto.getVlTeto());
			pstmt.setInt(9,objeto.getTpVariacaoBase());
			pstmt.setFloat(10,objeto.getVlVariacaoBase());
			pstmt.setInt(11,objeto.getTpVariacaoResultado());
			pstmt.setFloat(12,objeto.getVlVariacaoResultado());
			pstmt.setInt(13, cdTabelaComissaoOld!=0 ? cdTabelaComissaoOld : objeto.getCdTabelaComissao());
			pstmt.setInt(14, cdCalculoOld!=0 ? cdCalculoOld : objeto.getCdCalculo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoCalculoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoCalculoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTabelaComissao, int cdCalculo) {
		return delete(cdTabelaComissao, cdCalculo, null);
	}

	public static int delete(int cdTabelaComissao, int cdCalculo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_tabela_comissao_calculo WHERE cd_tabela_comissao=? AND cd_calculo=?");
			pstmt.setInt(1, cdTabelaComissao);
			pstmt.setInt(2, cdCalculo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoCalculoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoCalculoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TabelaComissaoCalculo get(int cdTabelaComissao, int cdCalculo) {
		return get(cdTabelaComissao, cdCalculo, null);
	}

	public static TabelaComissaoCalculo get(int cdTabelaComissao, int cdCalculo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_tabela_comissao_calculo WHERE cd_tabela_comissao=? AND cd_calculo=?");
			pstmt.setInt(1, cdTabelaComissao);
			pstmt.setInt(2, cdCalculo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TabelaComissaoCalculo(rs.getInt("cd_tabela_comissao"),
						rs.getInt("cd_calculo"),
						rs.getInt("cd_calculo_origem"),
						rs.getFloat("vl_inicial"),
						rs.getFloat("vl_final"),
						rs.getInt("tp_aplicacao"),
						rs.getFloat("vl_aplicacao"),
						rs.getFloat("vl_teto"),
						rs.getInt("tp_variacao_base"),
						rs.getFloat("vl_variacao_base"),
						rs.getInt("tp_variacao_resultado"),
						rs.getFloat("vl_variacao_resultado"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoCalculoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoCalculoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_tabela_comissao_calculo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoCalculoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoCalculoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_tabela_comissao_calculo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
