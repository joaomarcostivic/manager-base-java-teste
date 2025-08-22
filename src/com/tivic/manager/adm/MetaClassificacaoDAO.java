package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class MetaClassificacaoDAO{

	public static int insert(MetaClassificacao objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(MetaClassificacao objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			HashMap<String,Object>[] keys = new HashMap[3];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_tabela_comissao");
			keys[0].put("IS_KEY_NATIVE", "NO");
			keys[0].put("FIELD_VALUE", new Integer(objeto.getCdTabelaComissao()));
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_meta");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdMeta()));
			keys[2] = new HashMap<String,Object>();
			keys[2].put("FIELD_NAME", "cd_classificacao");
			keys[2].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("adm_meta_classificacao", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdClassificacao(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_meta_classificacao (cd_tabela_comissao,"+
			                                  "cd_meta,"+
			                                  "cd_classificacao,"+
			                                  "nm_classificacao,"+
			                                  "vl_inicial,"+
			                                  "vl_final,"+
			                                  "tp_calculo) VALUES (?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdTabelaComissao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTabelaComissao());
			if(objeto.getCdMeta()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdMeta());
			pstmt.setInt(3, code);
			pstmt.setString(4,objeto.getNmClassificacao());
			pstmt.setFloat(5,objeto.getVlInicial());
			pstmt.setFloat(6,objeto.getVlFinal());
			pstmt.setInt(7,objeto.getTpCalculo());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MetaClassificacaoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MetaClassificacaoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(MetaClassificacao objeto) {
		return update(objeto, 0, 0, 0, null);
	}

	public static int update(MetaClassificacao objeto, int cdTabelaComissaoOld, int cdMetaOld, int cdClassificacaoOld) {
		return update(objeto, cdTabelaComissaoOld, cdMetaOld, cdClassificacaoOld, null);
	}

	public static int update(MetaClassificacao objeto, Connection connect) {
		return update(objeto, 0, 0, 0, connect);
	}

	public static int update(MetaClassificacao objeto, int cdTabelaComissaoOld, int cdMetaOld, int cdClassificacaoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_meta_classificacao SET cd_tabela_comissao=?,"+
												      		   "cd_meta=?,"+
												      		   "cd_classificacao=?,"+
												      		   "nm_classificacao=?,"+
												      		   "vl_inicial=?,"+
												      		   "vl_final=?,"+
												      		   "tp_calculo=? WHERE cd_tabela_comissao=? AND cd_meta=? AND cd_classificacao=?");
			pstmt.setInt(1,objeto.getCdTabelaComissao());
			pstmt.setInt(2,objeto.getCdMeta());
			pstmt.setInt(3,objeto.getCdClassificacao());
			pstmt.setString(4,objeto.getNmClassificacao());
			pstmt.setFloat(5,objeto.getVlInicial());
			pstmt.setFloat(6,objeto.getVlFinal());
			pstmt.setInt(7,objeto.getTpCalculo());
			pstmt.setInt(8, cdTabelaComissaoOld!=0 ? cdTabelaComissaoOld : objeto.getCdTabelaComissao());
			pstmt.setInt(9, cdMetaOld!=0 ? cdMetaOld : objeto.getCdMeta());
			pstmt.setInt(10, cdClassificacaoOld!=0 ? cdClassificacaoOld : objeto.getCdClassificacao());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MetaClassificacaoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MetaClassificacaoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTabelaComissao, int cdMeta, int cdClassificacao) {
		return delete(cdTabelaComissao, cdMeta, cdClassificacao, null);
	}

	public static int delete(int cdTabelaComissao, int cdMeta, int cdClassificacao, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_meta_classificacao WHERE cd_tabela_comissao=? AND cd_meta=? AND cd_classificacao=?");
			pstmt.setInt(1, cdTabelaComissao);
			pstmt.setInt(2, cdMeta);
			pstmt.setInt(3, cdClassificacao);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MetaClassificacaoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MetaClassificacaoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static MetaClassificacao get(int cdTabelaComissao, int cdMeta, int cdClassificacao) {
		return get(cdTabelaComissao, cdMeta, cdClassificacao, null);
	}

	public static MetaClassificacao get(int cdTabelaComissao, int cdMeta, int cdClassificacao, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_meta_classificacao WHERE cd_tabela_comissao=? AND cd_meta=? AND cd_classificacao=?");
			pstmt.setInt(1, cdTabelaComissao);
			pstmt.setInt(2, cdMeta);
			pstmt.setInt(3, cdClassificacao);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new MetaClassificacao(rs.getInt("cd_tabela_comissao"),
						rs.getInt("cd_meta"),
						rs.getInt("cd_classificacao"),
						rs.getString("nm_classificacao"),
						rs.getFloat("vl_inicial"),
						rs.getFloat("vl_final"),
						rs.getInt("tp_calculo"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MetaClassificacaoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MetaClassificacaoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_meta_classificacao");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MetaClassificacaoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MetaClassificacaoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_meta_classificacao", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
