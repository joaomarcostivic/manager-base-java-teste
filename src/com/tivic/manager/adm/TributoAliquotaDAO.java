package com.tivic.manager.adm;

import java.sql.*;
import com.tivic.sol.connection.Conexao;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.HashMap;
import java.util.ArrayList;

public class TributoAliquotaDAO{

	public static int insert(TributoAliquota objeto) {
		return insert(objeto, null);
	}

	public static int insert(TributoAliquota objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			@SuppressWarnings("unchecked")
			HashMap<String,Object>[] keys = new HashMap[2];
			keys[0] = new HashMap<String,Object>();
			keys[0].put("FIELD_NAME", "cd_tributo_aliquota");
			keys[0].put("IS_KEY_NATIVE", "YES");
			keys[1] = new HashMap<String,Object>();
			keys[1].put("FIELD_NAME", "cd_tributo");
			keys[1].put("IS_KEY_NATIVE", "NO");
			keys[1].put("FIELD_VALUE", new Integer(objeto.getCdTributo()));
			int code = Conexao.getSequenceCode("adm_tributo_aliquota", keys, connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTributoAliquota(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_tributo_aliquota (cd_tributo_aliquota,"+
			                                  "cd_tributo,"+
			                                  "pr_aliquota,"+
			                                  "pr_credito,"+
			                                  "st_tributaria,"+
			                                  "dt_inicio_validade,"+
			                                  "dt_final_validade,"+
			                                  "vl_inicio_faixa,"+
			                                  "vl_variacao_base,"+
			                                  "vl_variacao_resultado,"+
			                                  "tp_fator_variacao_base,"+
			                                  "tp_operacao,"+
			                                  "tp_fator_variacao_resultado,"+
			                                  "cd_situacao_tributaria,"+
			                                  "tp_base_calculo,"+
			                                  "pr_reducao_base,"+
			                                  "tp_motivo_desoneracao,"+
			                                  "pr_aliquota_substituicao,"+
			                                  "tp_base_calculo_substituicao,"+
			                                  "pr_reducao_base_substituicao,"+
			                                  "vl_variacao_base_substituicao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			if(objeto.getCdTributo()==0)
				pstmt.setNull(2, Types.INTEGER);
			else
				pstmt.setInt(2,objeto.getCdTributo());
			pstmt.setFloat(3,objeto.getPrAliquota());
			pstmt.setFloat(4,objeto.getPrCredito());
			pstmt.setInt(5,objeto.getStTributaria());
			if(objeto.getDtInicioValidade()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtInicioValidade().getTimeInMillis()));
			if(objeto.getDtFinalValidade()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtFinalValidade().getTimeInMillis()));
			pstmt.setFloat(8,objeto.getVlInicioFaixa());
			pstmt.setFloat(9,objeto.getVlVariacaoBase());
			pstmt.setFloat(10,objeto.getVlVariacaoResultado());
			pstmt.setInt(11,objeto.getTpFatorVariacaoBase());
			pstmt.setInt(12,objeto.getTpOperacao());
			pstmt.setInt(13,objeto.getTpFatorVariacaoResultado());
			pstmt.setInt(14,objeto.getCdSituacaoTributaria());
			pstmt.setInt(15,objeto.getTpBaseCalculo());
			pstmt.setFloat(16,objeto.getPrReducaoBase());
			pstmt.setInt(17,objeto.getTpMotivoDesoneracao());
			pstmt.setFloat(18,objeto.getPrAliquotaSubstituicao());
			pstmt.setInt(19,objeto.getTpBaseCalculoSubstituicao());
			pstmt.setFloat(20,objeto.getPrReducaoBaseSubstituicao());
			pstmt.setFloat(21,objeto.getVlVariacaoBaseSubstituicao());
			pstmt.executeUpdate();
			return code;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TributoAliquotaDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(TributoAliquota objeto) {
		return update(objeto, 0, 0, null);
	}

	public static int update(TributoAliquota objeto, int cdTributoAliquotaOld, int cdTributoOld) {
		return update(objeto, cdTributoAliquotaOld, cdTributoOld, null);
	}

	public static int update(TributoAliquota objeto, Connection connect) {
		return update(objeto, 0, 0, connect);
	}

	public static int update(TributoAliquota objeto, int cdTributoAliquotaOld, int cdTributoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_tributo_aliquota SET cd_tributo_aliquota=?,"+
												      		   "cd_tributo=?,"+
												      		   "pr_aliquota=?,"+
												      		   "pr_credito=?,"+
												      		   "st_tributaria=?,"+
												      		   "dt_inicio_validade=?,"+
												      		   "dt_final_validade=?,"+
												      		   "vl_inicio_faixa=?,"+
												      		   "vl_variacao_base=?,"+
												      		   "vl_variacao_resultado=?,"+
												      		   "tp_fator_variacao_base=?,"+
												      		   "tp_operacao=?,"+
												      		   "tp_fator_variacao_resultado=?,"+
												      		   "cd_situacao_tributaria=?,"+
												      		   "tp_base_calculo=?,"+
												      		   "pr_reducao_base=?,"+
												      		   "tp_motivo_desoneracao=?,"+
												      		   "pr_aliquota_substituicao=?,"+
												      		   "tp_base_calculo_substituicao=?,"+
												      		   "pr_reducao_base_substituicao=?,"+
												      		   "vl_variacao_base_substituicao=? WHERE cd_tributo_aliquota=? AND cd_tributo=?");
			pstmt.setInt(1,objeto.getCdTributoAliquota());
			pstmt.setInt(2,objeto.getCdTributo());
			pstmt.setFloat(3,objeto.getPrAliquota());
			pstmt.setFloat(4,objeto.getPrCredito());
			pstmt.setInt(5,objeto.getStTributaria());
			if(objeto.getDtInicioValidade()==null)
				pstmt.setNull(6, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(6,new Timestamp(objeto.getDtInicioValidade().getTimeInMillis()));
			if(objeto.getDtFinalValidade()==null)
				pstmt.setNull(7, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(7,new Timestamp(objeto.getDtFinalValidade().getTimeInMillis()));
			pstmt.setFloat(8,objeto.getVlInicioFaixa());
			pstmt.setFloat(9,objeto.getVlVariacaoBase());
			pstmt.setFloat(10,objeto.getVlVariacaoResultado());
			pstmt.setInt(11,objeto.getTpFatorVariacaoBase());
			pstmt.setInt(12,objeto.getTpOperacao());
			pstmt.setInt(13,objeto.getTpFatorVariacaoResultado());
			pstmt.setInt(14,objeto.getCdSituacaoTributaria());
			pstmt.setInt(15,objeto.getTpBaseCalculo());
			pstmt.setFloat(16,objeto.getPrReducaoBase());
			pstmt.setInt(17,objeto.getTpMotivoDesoneracao());
			pstmt.setFloat(18,objeto.getPrAliquotaSubstituicao());
			pstmt.setInt(19,objeto.getTpBaseCalculoSubstituicao());
			pstmt.setFloat(20,objeto.getPrReducaoBaseSubstituicao());
			pstmt.setFloat(21,objeto.getVlVariacaoBaseSubstituicao());
			pstmt.setInt(22, cdTributoAliquotaOld!=0 ? cdTributoAliquotaOld : objeto.getCdTributoAliquota());
			pstmt.setInt(23, cdTributoOld!=0 ? cdTributoOld : objeto.getCdTributo());
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTributoAliquota, int cdTributo) {
		return delete(cdTributoAliquota, cdTributo, null);
	}

	public static int delete(int cdTributoAliquota, int cdTributo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_tributo_aliquota WHERE cd_tributo_aliquota=? AND cd_tributo=?");
			pstmt.setInt(1, cdTributoAliquota);
			pstmt.setInt(2, cdTributo);
			return pstmt.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TributoAliquotaDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static TributoAliquota get(int cdTributoAliquota, int cdTributo) {
		return get(cdTributoAliquota, cdTributo, null);
	}

	public static TributoAliquota get(int cdTributoAliquota, int cdTributo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_tributo_aliquota WHERE cd_tributo_aliquota=? AND cd_tributo=?");
			pstmt.setInt(1, cdTributoAliquota);
			pstmt.setInt(2, cdTributo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new TributoAliquota(rs.getInt("cd_tributo_aliquota"),
						rs.getInt("cd_tributo"),
						rs.getFloat("pr_aliquota"),
						rs.getFloat("pr_credito"),
						rs.getInt("st_tributaria"),
						(rs.getTimestamp("dt_inicio_validade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio_validade").getTime()),
						(rs.getTimestamp("dt_final_validade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final_validade").getTime()),
						rs.getFloat("vl_inicio_faixa"),
						rs.getFloat("vl_variacao_base"),
						rs.getFloat("vl_variacao_resultado"),
						rs.getInt("tp_fator_variacao_base"),
						rs.getInt("tp_operacao"),
						rs.getInt("tp_fator_variacao_resultado"),
						rs.getInt("cd_situacao_tributaria"),
						rs.getInt("tp_base_calculo"),
						rs.getFloat("pr_reducao_base"),
						rs.getInt("tp_motivo_desoneracao"),
						rs.getFloat("pr_aliquota_substituicao"),
						rs.getInt("tp_base_calculo_substituicao"),
						rs.getFloat("pr_reducao_base_substituicao"),
						rs.getFloat("vl_variacao_base_substituicao"));
			}
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_tributo_aliquota");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TributoAliquotaDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_tributo_aliquota", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
