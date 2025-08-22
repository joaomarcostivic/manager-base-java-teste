package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TabelaComissaoMetaDAO{

	public static int insert(TabelaComissaoMeta objeto) {
		return insert(objeto, null);
	}

	@SuppressWarnings("unchecked")
	public static int insert(TabelaComissaoMeta objeto, Connection connect){
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
			keys[1].put("FIELD_NAME", "cd_meta");
			keys[1].put("IS_KEY_NATIVE", "YES");
			int code = Conexao.getSequenceCode("adm_tabela_comissao_meta", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdMeta(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_tabela_comissao_meta (cd_tabela_comissao,"+
			                                  "cd_meta,"+
			                                  "cd_grupo,"+
			                                  "cd_produto_servico,"+
			                                  "cd_regiao,"+
			                                  "cd_agente,"+
			                                  "cd_tipo_operacao,"+
			                                  "tp_meta,"+
			                                  "tp_periodo,"+
			                                  "dt_inicial,"+
			                                  "dt_final,"+
			                                  "vl_meta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(objeto.getCdTabelaComissao()==0)
				pstmt.setNull(1, Types.INTEGER);
			else
				pstmt.setInt(1,objeto.getCdTabelaComissao());
			pstmt.setInt(2, code);
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdGrupo());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdProdutoServico());
			if(objeto.getCdRegiao()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdRegiao());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdAgente());
			if(objeto.getCdTipoOperacao()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTipoOperacao());
			pstmt.setInt(8,objeto.getTpMeta());
			pstmt.setInt(9,objeto.getTpPeriodo());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setFloat(12,objeto.getVlMeta());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoMetaDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoMetaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TabelaComissaoMeta objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TabelaComissaoMeta objeto, int cdTabelaComissaoOld, int cdMetaOld) {
		return update(objeto, cdTabelaComissaoOld, cdMetaOld, null);
	}

	public static int update(TabelaComissaoMeta objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TabelaComissaoMeta objeto, int cdTabelaComissaoOld, int cdMetaOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_tabela_comissao_meta SET cd_tabela_comissao=?,"+
												      		   "cd_meta=?,"+
												      		   "cd_grupo=?,"+
												      		   "cd_produto_servico=?,"+
												      		   "cd_regiao=?,"+
												      		   "cd_agente=?,"+
												      		   "cd_tipo_operacao=?,"+
												      		   "tp_meta=?,"+
												      		   "tp_periodo=?,"+
												      		   "dt_inicial=?,"+
												      		   "dt_final=?,"+
												      		   "vl_meta=? WHERE cd_tabela_comissao=? AND cd_meta=?");
			pstmt.setInt(1,objeto.getCdTabelaComissao());
			pstmt.setInt(2,objeto.getCdMeta());
			if(objeto.getCdGrupo()==0)
				pstmt.setNull(3, Types.INTEGER);
			else
				pstmt.setInt(3,objeto.getCdGrupo());
			if(objeto.getCdProdutoServico()==0)
				pstmt.setNull(4, Types.INTEGER);
			else
				pstmt.setInt(4,objeto.getCdProdutoServico());
			if(objeto.getCdRegiao()==0)
				pstmt.setNull(5, Types.INTEGER);
			else
				pstmt.setInt(5,objeto.getCdRegiao());
			if(objeto.getCdAgente()==0)
				pstmt.setNull(6, Types.INTEGER);
			else
				pstmt.setInt(6,objeto.getCdAgente());
			if(objeto.getCdTipoOperacao()==0)
				pstmt.setNull(7, Types.INTEGER);
			else
				pstmt.setInt(7,objeto.getCdTipoOperacao());
			pstmt.setInt(8,objeto.getTpMeta());
			pstmt.setInt(9,objeto.getTpPeriodo());
			if(objeto.getDtInicial()==null)
				pstmt.setNull(10, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(10,new Timestamp(objeto.getDtInicial().getTimeInMillis()));
			if(objeto.getDtFinal()==null)
				pstmt.setNull(11, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(11,new Timestamp(objeto.getDtFinal().getTimeInMillis()));
			pstmt.setFloat(12,objeto.getVlMeta());
			pstmt.setInt(13, cdTabelaComissaoOld!=0 ? cdTabelaComissaoOld : objeto.getCdTabelaComissao());
			pstmt.setInt(14, cdMetaOld!=0 ? cdMetaOld : objeto.getCdMeta());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoMetaDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoMetaDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTabelaComissao, int cdMeta) {
		return delete(cdTabelaComissao, cdMeta, null);
	}

	public static int delete(int cdTabelaComissao, int cdMeta, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_tabela_comissao_meta WHERE cd_tabela_comissao=? AND cd_meta=?");
			pstmt.setInt(1, cdTabelaComissao);
			pstmt.setInt(2, cdMeta);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoMetaDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoMetaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TabelaComissaoMeta get(int cdTabelaComissao, int cdMeta) {
		return get(cdTabelaComissao, cdMeta, null);
	}

	public static TabelaComissaoMeta get(int cdTabelaComissao, int cdMeta, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_tabela_comissao_meta WHERE cd_tabela_comissao=? AND cd_meta=?");
			pstmt.setInt(1, cdTabelaComissao);
			pstmt.setInt(2, cdMeta);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TabelaComissaoMeta(rs.getInt("cd_tabela_comissao"),
						rs.getInt("cd_meta"),
						rs.getInt("cd_grupo"),
						rs.getInt("cd_produto_servico"),
						rs.getInt("cd_regiao"),
						rs.getInt("cd_agente"),
						rs.getInt("cd_tipo_operacao"),
						rs.getInt("tp_meta"),
						rs.getInt("tp_periodo"),
						(rs.getTimestamp("dt_inicial")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicial").getTime()),
						(rs.getTimestamp("dt_final")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final").getTime()),
						rs.getFloat("vl_meta"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoMetaDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoMetaDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_tabela_comissao_meta");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoMetaDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TabelaComissaoMetaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_tabela_comissao_meta", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
