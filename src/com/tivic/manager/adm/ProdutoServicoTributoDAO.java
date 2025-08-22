package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;
import java.util.ArrayList;
import java.util.HashMap;

import com.tivic.sol.connection.Conexao;

public class ProdutoServicoTributoDAO{

	public static Result insert(ProdutoServicoTributo objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static Result insert(ProdutoServicoTributo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_produto_servico_tributo");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_tributo_aliquota");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdTributoAliquota()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_tributo");
			keys[2].put("IS_KEY_NATIVE", "NO");
			keys[2].put("FIELD_VALUE", new Integer(objeto.getCdTributo()));
			int code = Conexao.getSequenceCode("adm_produto_servico_tributo", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return new Result(-1, "Não foi possível calcular chave primária!");
			}
			objeto.setCdProdutoServicoTributo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_produto_servico_tributo (cd_produto_servico_tributo,"+
			                                  "cd_tributo_aliquota,"+
			                                  "cd_tributo,"+
			                                  "cd_produto_servico,"+
			                                  "cd_cidade,"+
			                                  "cd_estado,"+
			                                  "cd_pais,"+
			                                  "cd_natureza_operacao,"+
			                                  "cd_classificacao_fiscal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdTributoAliquota()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTributoAliquota());
			if(objeto.getCdTributo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdTributo());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdProdutoServico());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdCidade());
			if(objeto.getCdEstado()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdEstado());
			if(objeto.getCdPais()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdPais());
			if(objeto.getCdNaturezaOperacao()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdNaturezaOperacao());
			if(objeto.getCdClassificacaoFiscal()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdClassificacaoFiscal());
			pstmt.executeUpdate();
			return new Result(code);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoTributoDAO.insert: " +  e);
			return new Result(-1, "Erro! ProdutoServicoTributoDAO.insert: ", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Result update(ProdutoServicoTributo objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static Result update(ProdutoServicoTributo objeto, int cdProdutoServicoTributoOld, int cdTributoAliquotaOld, int cdTributoOld) {
		return update(objeto, cdProdutoServicoTributoOld, cdTributoAliquotaOld, cdTributoOld, null);
	}

	public static Result update(ProdutoServicoTributo objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static Result update(ProdutoServicoTributo objeto, int cdProdutoServicoTributoOld, int cdTributoAliquotaOld, int cdTributoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_produto_servico_tributo SET cd_produto_servico_tributo=?,"+
												      		   "cd_tributo_aliquota=?,"+
												      		   "cd_tributo=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_cidade=?,"+
												      		   "cd_estado=?,"+
												      		   "cd_pais=?,"+
												      		   "cd_natureza_operacao=?,"+
												      		   "cd_classificacao_fiscal=? WHERE cd_produto_servico_tributo=? AND cd_tributo_aliquota=? AND cd_tributo=?");
			pstmt.setInt(1,objeto.getCdProdutoServicoTributo());
			pstmt.setInt(2,objeto.getCdTributoAliquota());
			pstmt.setInt(3,objeto.getCdTributo());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdProdutoServico());
			if(objeto.getCdCidade()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdCidade());
			if(objeto.getCdEstado()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdEstado());
			if(objeto.getCdPais()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdPais());
			if(objeto.getCdNaturezaOperacao()==0)
				pstmt.setNull(8, Types.INTEGER);
			else
				pstmt.setInt(8,objeto.getCdNaturezaOperacao());
			if(objeto.getCdClassificacaoFiscal()==0)
				pstmt.setNull(9, Types.INTEGER);
			else
				pstmt.setInt(9,objeto.getCdClassificacaoFiscal());
			pstmt.setInt(10, cdProdutoServicoTributoOld!=0 ? cdProdutoServicoTributoOld : objeto.getCdProdutoServicoTributo());
			pstmt.setInt(11, cdTributoAliquotaOld!=0 ? cdTributoAliquotaOld : objeto.getCdTributoAliquota());
			pstmt.setInt(12, cdTributoOld!=0 ? cdTributoOld : objeto.getCdTributo());
			return new Result(pstmt.executeUpdate());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoTributoDAO.update: " +  e);
			return new Result(-1, "Erro! ProdutoServicoTributoDAO.update: ", e);
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdProdutoServicoTributo, int cdTributoAliquota, int cdTributo) {
		return delete(cdProdutoServicoTributo, cdTributoAliquota, cdTributo, null);
	}

	public static int delete(int cdProdutoServicoTributo, int cdTributoAliquota, int cdTributo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_produto_servico_tributo WHERE cd_produto_servico_tributo=? AND cd_tributo_aliquota=? AND cd_tributo=?");
			pstmt.setInt(1, cdProdutoServicoTributo);
			pstmt.setInt(2, cdTributoAliquota);
			pstmt.setInt(3, cdTributo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoTributoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoTributoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ProdutoServicoTributo get(int cdProdutoServicoTributo, int cdTributoAliquota, int cdTributo) {
		return get(cdProdutoServicoTributo, cdTributoAliquota, cdTributo, null);
	}

	public static ProdutoServicoTributo get(int cdProdutoServicoTributo, int cdTributoAliquota, int cdTributo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_produto_servico_tributo WHERE cd_produto_servico_tributo=? AND cd_tributo_aliquota=? AND cd_tributo=?");
			pstmt.setInt(1, cdProdutoServicoTributo);
			pstmt.setInt(2, cdTributoAliquota);
			pstmt.setInt(3, cdTributo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new ProdutoServicoTributo(rs.getInt("cd_produto_servico_tributo"),
						rs.getInt("cd_tributo_aliquota"),
						rs.getInt("cd_tributo"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_cidade"),
						rs.getInt("cd_estado"),
						rs.getInt("cd_pais"),
						rs.getInt("cd_natureza_operacao"),
						rs.getInt("cd_classificacao_fiscal"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoTributoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoTributoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_produto_servico_tributo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoTributoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ProdutoServicoTributoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_produto_servico_tributo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
