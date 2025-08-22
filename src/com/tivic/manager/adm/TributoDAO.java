package com.tivic.manager.adm;

import java.sql.*;
import sol.dao.ResultSetMap;
import sol.dao.ItemComparator;
import sol.dao.Search;
import sol.dao.Util;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;

public class TributoDAO{

	public static int insert(Tributo objeto) {
		return insert(objeto, null);
	}

	public static int insert(Tributo objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			int code = Conexao.getSequenceCode("adm_tributo", connect);
			if (code <= 0) {
				if (isConnectionNull)
					Conexao.rollback(connect);
				return -1;
			}
			objeto.setCdTributo(code);
			PreparedStatement pstmt = connect.prepareStatement("INSERT INTO adm_tributo (cd_tributo,"+
			                                  "nm_tributo,"+
			                                  "id_tributo,"+
			                                  "dt_inicio_validade,"+
			                                  "dt_final_validade,"+
			                                  "tp_tributo,"+
			                                  "pr_aliquota_padrao,"+
			                                  "lg_aliquota_progressiva,"+
			                                  "tp_esfera_aplicacao,"+
			                                  "nr_ordem_calculo,"+
			                                  "tp_operacao,"+
			                                  "vl_variacao_base,"+
			                                  "vl_variacao_resultado,"+
			                                  "tp_fator_variacao_base,"+
			                                  "tp_fator_variacao_resultado,"+
			                                  "tp_cobranca) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, code);
			pstmt.setString(2,objeto.getNmTributo());
			pstmt.setString(3,objeto.getIdTributo());
			if(objeto.getDtInicioValidade()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtInicioValidade().getTimeInMillis()));
			if(objeto.getDtFinalValidade()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtFinalValidade().getTimeInMillis()));
			pstmt.setInt(6,objeto.getTpTributo());
			pstmt.setFloat(7,objeto.getPrAliquotaPadrao());
			pstmt.setInt(8,objeto.getLgAliquotaProgressiva());
			pstmt.setInt(9,objeto.getTpEsferaAplicacao());
			pstmt.setInt(10,objeto.getNrOrdemCalculo());
			pstmt.setInt(11,objeto.getTpOperacao());
			pstmt.setFloat(12,objeto.getVlVariacaoBase());
			pstmt.setFloat(13,objeto.getVlVariacaoResultado());
			pstmt.setInt(14,objeto.getTpFatorVariacaoBase());
			pstmt.setInt(15,objeto.getTpFatorVariacaoResultado());
			pstmt.setInt(16,objeto.getTpCobranca());
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TributoDAO.insert: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TributoDAO.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int update(Tributo objeto) {
		return update(objeto, 0, null);
	}

	public static int update(Tributo objeto, int cdTributoOld) {
		return update(objeto, cdTributoOld, null);
	}

	public static int update(Tributo objeto, Connection connect) {
		return update(objeto, 0, connect);
	}

	public static int update(Tributo objeto, int cdTributoOld, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("UPDATE adm_tributo SET cd_tributo=?,"+
												      		   "nm_tributo=?,"+
												      		   "id_tributo=?,"+
												      		   "dt_inicio_validade=?,"+
												      		   "dt_final_validade=?,"+
												      		   "tp_tributo=?,"+
												      		   "pr_aliquota_padrao=?,"+
												      		   "lg_aliquota_progressiva=?,"+
												      		   "tp_esfera_aplicacao=?,"+
												      		   "nr_ordem_calculo=?,"+
												      		   "tp_operacao=?,"+
												      		   "vl_variacao_base=?,"+
												      		   "vl_variacao_resultado=?,"+
												      		   "tp_fator_variacao_base=?,"+
												      		   "tp_fator_variacao_resultado=?,"+
												      		   "tp_cobranca=? WHERE cd_tributo=?");
			pstmt.setInt(1,objeto.getCdTributo());
			pstmt.setString(2,objeto.getNmTributo());
			pstmt.setString(3,objeto.getIdTributo());
			if(objeto.getDtInicioValidade()==null)
				pstmt.setNull(4, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(4,new Timestamp(objeto.getDtInicioValidade().getTimeInMillis()));
			if(objeto.getDtFinalValidade()==null)
				pstmt.setNull(5, Types.TIMESTAMP);
			else
				pstmt.setTimestamp(5,new Timestamp(objeto.getDtFinalValidade().getTimeInMillis()));
			pstmt.setInt(6,objeto.getTpTributo());
			pstmt.setFloat(7,objeto.getPrAliquotaPadrao());
			pstmt.setInt(8,objeto.getLgAliquotaProgressiva());
			pstmt.setInt(9,objeto.getTpEsferaAplicacao());
			pstmt.setInt(10,objeto.getNrOrdemCalculo());
			pstmt.setInt(11,objeto.getTpOperacao());
			pstmt.setFloat(12,objeto.getVlVariacaoBase());
			pstmt.setFloat(13,objeto.getVlVariacaoResultado());
			pstmt.setInt(14,objeto.getTpFatorVariacaoBase());
			pstmt.setInt(15,objeto.getTpFatorVariacaoResultado());
			pstmt.setInt(16,objeto.getTpCobranca());
			pstmt.setInt(17, cdTributoOld!=0 ? cdTributoOld : objeto.getCdTributo());
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TributoDAO.update: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TributoDAO.update: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int delete(int cdTributo) {
		return delete(cdTributo, null);
	}

	public static int delete(int cdTributo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("DELETE FROM adm_tributo WHERE cd_tributo=?");
			pstmt.setInt(1, cdTributo);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TributoDAO.delete: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TributoDAO.delete: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static Tributo get(int cdTributo) {
		return get(cdTributo, null);
	}

	public static Tributo get(int cdTributo, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		ResultSet rs;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM adm_tributo WHERE cd_tributo=?");
			pstmt.setInt(1, cdTributo);
			rs = pstmt.executeQuery();
			if(rs.next()){
				return new Tributo(rs.getInt("cd_tributo"),
						rs.getString("nm_tributo"),
						rs.getString("id_tributo"),
						(rs.getTimestamp("dt_inicio_validade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_inicio_validade").getTime()),
						(rs.getTimestamp("dt_final_validade")==null)?null:Util.longToCalendar(rs.getTimestamp("dt_final_validade").getTime()),
						rs.getInt("tp_tributo"),
						rs.getFloat("pr_aliquota_padrao"),
						rs.getInt("lg_aliquota_progressiva"),
						rs.getInt("tp_esfera_aplicacao"),
						rs.getInt("nr_ordem_calculo"),
						rs.getInt("tp_operacao"),
						rs.getFloat("vl_variacao_base"),
						rs.getFloat("vl_variacao_resultado"),
						rs.getInt("tp_fator_variacao_base"),
						rs.getInt("tp_fator_variacao_resultado"),
						rs.getInt("tp_cobranca"));
			}
			else{
				return null;
			}
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TributoDAO.get: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TributoDAO.get: " + e);
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
			pstmt = connect.prepareStatement("SELECT * FROM adm_tributo");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! TributoDAO.getAll: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! TributoDAO.getAll: " + e);
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
		return Search.find("SELECT * FROM adm_tributo", criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

}
