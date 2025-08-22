package com.tivic.manager.crt;

import java.sql.*;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.tivic.manager.conexao.*;
import com.tivic.sol.connection.Conexao;

public class MetaPremiacaoServices {
	public static final int PROJECAO_EMPRESA  = 0;
	public static final int PROJECAO_OPERACAO = 1;
	public static final int PROJECAO_PARCEIRO = 2;
	public static final int META_EMPRESA  = 0;
	public static final int META_OPERACAO = 1;
	public static final int META_PARCEIRO = 2;

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT * FROM sce_meta_premiacao A "+
		                   "LEFT OUTER JOIN sce_tipo_operacao B ON (A.cd_operacao = B.cd_operacao) "+
		                   "LEFT OUTER JOIN grl_vinculo       C ON (A.cd_vinculo  = C.cd_vinculo)"+
		                   "LEFT OUTER JOIN grl_empresa D ON (A.cd_empresa = D.cd_empresa)"+
		                   "LEFT OUTER JOIN grl_pessoa  E ON (A.cd_pessoa = E.cd_pessoa)", criterios, Conexao.conectar());
	}

	public static int insertClassificacaoPremio(int cdMetaPremiacao, String nmClassificacaoPremio, int nrPosicao,
		int nrPosicaoFinal, float vlReferencia,
		int tpCalculo)
	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			int code = Conexao.getSequenceCode("SCE_CLASSIFICACAO_PREMIO");
			pstmt = connect.prepareStatement("INSERT INTO SCE_CLASSIFICACAO_PREMIO (CD_META_PREMIACAO,"+
			                                 "CD_CLASSIFICACAO_PREMIO,"+
			                                 "NM_CLASSIFICACAO_PREMIO,"+
			                                 "NR_POSICAO,"+
			                                 "NR_POSICAO_FINAL,"+
			                                 "VL_REFERENCIA,"+
			                                 "TP_CALCULO) VALUES (?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1,cdMetaPremiacao);
			pstmt.setInt(2, code);
			pstmt.setString(3,nmClassificacaoPremio);
			pstmt.setInt(4,nrPosicao);
			pstmt.setInt(5,nrPosicaoFinal);
			pstmt.setFloat(6,vlReferencia);
			pstmt.setInt(7,tpCalculo);
			pstmt.executeUpdate();
			return code;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MetaPremiacaoServices.insertClassificacaoPremio: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MetaPremiacaoServices.insertClassificacaoPremio: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static int updateClassificacaoPremio(int cdMetaPremiacao, int cdClassificacaoPremio,
			String nmClassificacaoPremio, int nrPosicao, int nrPosicaoFinal, float vlReferencia,
			int tpCalculo)
	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("UPDATE SCE_CLASSIFICACAO_PREMIO SET NM_CLASSIFICACAO_PREMIO=?,"+
			                                  "NR_POSICAO=?,"+
			                                  "NR_POSICAO_FINAL=?,"+
			                                  "VL_REFERENCIA=?,"+
			                                  "TP_CALCULO=? WHERE CD_META_PREMIACAO=? AND CD_CLASSIFICACAO_PREMIO=?");
			pstmt.setString(1,nmClassificacaoPremio);
			pstmt.setInt(2,nrPosicao);
			pstmt.setInt(3,nrPosicaoFinal);
			pstmt.setFloat(4,vlReferencia);
			pstmt.setInt(5,tpCalculo);
			pstmt.setInt(6,cdMetaPremiacao);
			pstmt.setInt(7,cdClassificacaoPremio);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MetaPremiacaoServices.updateClassificacaoPremio: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MetaPremiacaoServices.updateClassificacaoPremio: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static int deleteClassificacaoPremio(int cdMetaPremiacao, int cdClassificacaoPremio){
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("DELETE FROM SCE_CLASSIFICACAO_PREMIO WHERE CD_META_PREMIACAO=? AND CD_CLASSIFICACAO_PREMIO=?");
			pstmt.setInt(1, cdMetaPremiacao);
			pstmt.setInt(2, cdClassificacaoPremio);
			pstmt.executeUpdate();
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MetaPremiacaoServices.deleteClassificacaoPremio: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MetaPremiacaoServices.deleteClassificacaoPremio: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findClassificacaoPremio(ArrayList<ItemComparator> criterios) {
		return Search.find("SELECT * FROM SCE_CLASSIFICACAO_PREMIO A", criterios, Conexao.conectar());
	}

	public static int copyClassificacaoPremio(int cdMetaPremiacaoFrom, int cdMetaPremiacaoTo)
	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT * FROM sce_classificacao_premio A "+
			                                 "WHERE cd_meta_premiacao = ?");
			pstmt.setInt(1, cdMetaPremiacaoFrom);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())	{
				insertClassificacaoPremio(cdMetaPremiacaoTo, rs.getString("nm_classificacao_premio"),
										  rs.getInt("nr_posicao"), rs.getInt("nr_posicao_final"),
										  rs.getFloat("vl_referencia"),rs.getInt("tp_calculo"));
			}
			return 1;
		}
		catch(SQLException sqlExpt){
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MetaPremiacaoServices.copyClassificacaoPremio: " + sqlExpt);
			return (-1)*sqlExpt.getErrorCode();
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! MetaPremiacaoServices.copyClassificacaoPremio: " +  e);
			return -1;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static GregorianCalendar getLastDayOfMeta(int codigo, int tpMeta)
	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement("SELECT max(dt_final) AS dt_final FROM sce_meta_premiacao "+
					                         "WHERE "+(tpMeta==PROJECAO_PARCEIRO ? " cd_pessoa " : " cd_empresa ")+" = ? ");
			pstmt.setInt(1, codigo);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			rsm.next();
			GregorianCalendar dtInicial = new GregorianCalendar();
			dtInicial.set(GregorianCalendar.DAY_OF_MONTH, 1);
			if(rsm.getGregorianCalendar("dt_final")!=null)	{
				dtInicial = rsm.getGregorianCalendar("dt_final");
				dtInicial.add(GregorianCalendar.DATE, 1);
			}
			return dtInicial;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			System.err.println("Erro! MetaPremiacaoServices.getLastDayOfMeta: " +  e);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
	public static int deleteMetaPremiacao(int cdMetaPremiacao) {
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			connect.setAutoCommit(false);
			// Deletando Classificação / Premio
			pstmt = connect.prepareStatement("DELETE FROM sce_classificacao_premio "+
											 "WHERE cd_meta_premiacao = ? ");
			pstmt.setInt(1, cdMetaPremiacao);
			pstmt.executeUpdate();
			int ret = MetaPremiacaoDAO.delete(cdMetaPremiacao, connect);
			connect.commit();
			return ret;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MetaPremiacaoServices.deletePessoa: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	public static int insert(MetaPremiacao objeto)	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			connect.setAutoCommit(false);
			int cdMetaPremiacao = MetaPremiacaoDAO.insert(objeto, connect);
			if(cdMetaPremiacao>0){
				pstmt = connect.prepareStatement(
						"SELECT max(cd_meta_premiacao) "+
						"FROM SCE_META_PREMIACAO "+
						"WHERE cd_meta_premiacao <> ? ");
				pstmt.setInt(1, cdMetaPremiacao);
				ResultSet rs = pstmt.executeQuery();
				connect.commit();
				if(rs.next())
					copyClassificacaoPremio(rs.getInt(1), cdMetaPremiacao);
			}
			else
				connect.rollback();
			return cdMetaPremiacao;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MetaPremiacaoServices.insertMetaPremiacao: " + e);
			return -1;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
	public static ResultSetMap getProjecao(GregorianCalendar dtProjecao, int tpMeta, boolean lgConsolidado)	{
		Connection connect = Conexao.conectar();
		PreparedStatement pstmt, pstmtTotal;
		try {
			GregorianCalendar hoje = new GregorianCalendar();
			if(dtProjecao==null)
				dtProjecao = new GregorianCalendar();
			dtProjecao.set(Calendar.HOUR_OF_DAY, 0);
			dtProjecao.set(Calendar.MINUTE, 0);
			dtProjecao.set(Calendar.SECOND, 0);
			PreparedStatement pstmtClassificacao = connect.prepareStatement(
							"SELECT nm_classificacao_premio FROM sce_classificacao_premio A "+
							"WHERE A.cd_meta_premiacao = ? "+
							"  AND A.vl_referencia >= ? "+
							" ORDER BY vl_referencia");
			if(tpMeta == PROJECAO_PARCEIRO){
				pstmtTotal = connect.prepareStatement("SELECT sum(vl_financiado) AS vl_financiado, sum(vl_liberado) AS vl_liberado " +
													  "FROM sce_contrato A, sce_produto B, sce_situacao C "+
													  "WHERE A.cd_situacao = C.cd_situacao "+
													  "  AND C.lg_ignorar <> 1 "+
													  "  AND A.dt_contrato BETWEEN ? AND ? "+
													  "  AND A.cd_produto = B.cd_produto "+
													  "  AND B.cd_instituicao_financeira = ? ");

				pstmt = connect.prepareStatement(
						"SELECT A.nm_pessoa AS nm_parceiro, A.cd_pessoa AS cd_parceiro, B.* "+
                        "FROM grl_pessoa A, sce_meta_premiacao B, grl_pessoa_empresa C "+
		                "WHERE A.cd_pessoa = B.cd_pessoa "+
		                "  AND A.cd_pessoa = C.cd_pessoa "+
		                "  AND C.cd_vinculo = "+VinculoServices.PARCEIRO+
                        "  AND ? BETWEEN dt_inicial AND dt_final ");
			}
			else 	{
				pstmtTotal = connect.prepareStatement(
						  "SELECT sum(A.vl_financiado) AS vl_financiado, sum(A.vl_liberado) AS vl_liberado " +
						  "FROM sce_contrato A, sce_situacao B "+
						  "WHERE A.dt_contrato BETWEEN ? AND ? "+
						  "  AND A.cd_situacao = B.cd_situacao "+
						  "  AND B.lg_ignorar <> 1 "+
						  "  AND A.cd_empresa  = ? ");

				pstmt = connect.prepareStatement("SELECT * "+
                        "FROM grl_empresa A, sce_meta_premiacao B "+
                        "WHERE A.cd_empresa = B.cd_empresa "+
                        "  AND ? BETWEEN dt_inicial AND dt_final ");
			}
			pstmt.setTimestamp(1, new Timestamp(dtProjecao.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next())	{
				float vlMeta = rsm.getFloat("vl_meta");
				float vlAcumulado = 0;
				float vlPago = 0;
				int codigo = (tpMeta==PROJECAO_PARCEIRO) ? rsm.getInt("cd_pessoa") : rsm.getInt("cd_empresa");
				// Somando valor acumulado (Solicitado)
				pstmtTotal.setTimestamp(1, rsm.getTimestamp("dt_inicial"));
				pstmtTotal.setTimestamp(2, rsm.getTimestamp("dt_final"));
				pstmtTotal.setInt(3, codigo);
				ResultSet rs = pstmtTotal.executeQuery();
				if(rs.next())	{
					vlAcumulado = rs.getFloat(1);
					vlPago      = rs.getFloat(2);
				}
				float vlProjecao = 0, prProjecao = 0;
				if(hoje.after(rsm.getGregorianCalendar("dt_final")))	{
					vlProjecao = lgConsolidado ? vlPago : vlAcumulado;
					prProjecao = vlMeta>0 ? vlProjecao / vlMeta * 100 : 0;
				}
				else	{
					int nrTotalDias = com.tivic.manager.util.Util.getQuantidadeDiasUteis(rsm.getGregorianCalendar("dt_inicial"),
																  			 rsm.getGregorianCalendar("dt_final"), connect);
					int nrDias = com.tivic.manager.util.Util.getQuantidadeDiasUteis(rsm.getGregorianCalendar("dt_inicial"),
																  		dtProjecao, connect);
					vlProjecao = vlMeta>0 ? (vlAcumulado / nrDias * nrTotalDias) : 0;
					prProjecao = vlMeta>0 ? vlProjecao / vlMeta * 100 : 0;
				}
				rsm.setValueToField("VL_PROJECAO", new Float(vlProjecao));
				rsm.setValueToField("VL_PAGO", new Float(vlPago));
				rsm.setValueToField("PR_PROJECAO", new Float(prProjecao));
				rsm.setValueToField("VL_ACUMULADO", new Float(vlAcumulado));
				// Classificacao
				pstmtClassificacao.setInt(1, rsm.getInt("cd_meta_premiacao"));
				pstmtClassificacao.setFloat(2, prProjecao);
				ResultSetMap rsmClassificacao = new ResultSetMap(pstmtClassificacao.executeQuery());
				rsmClassificacao.first();
				if(rsmClassificacao.size()>0)
					rsm.setValueToField("NM_CLASSIFICACAO", rsmClassificacao.getString("nm_classificacao_premio"));
			}
			ArrayList<String> orderBy = new ArrayList<String>();
			orderBy.add("PR_PROJECAO DESC");
			rsm.orderBy(orderBy);
			rsm.beforeFirst();
			return rsm;
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MetaPremiacaoServices.getProjecao: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MetaPremiacaoServices.getProjecao: " + e);
			return null;
		}
		finally {
			Conexao.desconectar(connect);
		}
	}
}
