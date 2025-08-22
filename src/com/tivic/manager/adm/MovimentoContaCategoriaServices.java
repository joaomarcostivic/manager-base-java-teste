package com.tivic.manager.adm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;


import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

import com.tivic.manager.alm.DocumentoSaidaServices;
import com.tivic.manager.alm.GrupoServices;
import com.tivic.manager.alm.ProdutoEstoqueServices;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

public class MovimentoContaCategoriaServices	{
	public static final int COR_VERDE = 1;
	public static final int COR_VERMELHA = 2;
	public static final int TP_PRE_CLASSIFICACAO = 0;
	public static final int TP_JUROS 			 = 1;
	public static final int TP_MULTA 			 = 2;
	public static final int TP_DESCONTO 		 = 3;
	public static final int TP_ACRESCIMO 		 = 4;

	public static final String[] tiposMovimento = new String[] {"Pré-classificação", "Juros", "Multa", "Desconto", "Acréscimo"};

	public static int insert(MovimentoContaCategoria objeto) {
		return insert(objeto, null);
	}

	public static int insert(MovimentoContaCategoria objeto, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			return MovimentoContaCategoriaDAO.insert(objeto, connect);
		}
		catch(Exception e){
			com.tivic.manager.util.Util.registerLog(e);
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCategoriaServices.insert: " +  e);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
     * @author Hugo
     * @category Administração Financeira
     * @param cdConta Parte da chave primária do movimento da conta do qual se deseja ver a classificação
     * @param cdMovimentoConta  Parte da chave primária do movimento da conta do qual se deseja ver a classificação
     * @return ResultSetMap com as categorias nas quais está classificado o movimento
     */
	public static ResultSetMap getCategoriaOfMovimento(int cdConta, int cdMovimentoConta) {
		return getCategoriaOfMovimento(cdConta, cdMovimentoConta, null);
	}

	/**
     * @author Hugo
     * @category Administração Financeira
     * @param cdConta Parte da chave primária do movimento da conta do qual se deseja ver a classificação
     * @param cdMovimentoConta  Parte da chave primária do movimento da conta do qual se deseja ver a classificação
     * @param connect Conexão com o banco de dados
     * @return ResultSetMap com as categorias nas quais está classificado o movimento
     */
	public static ResultSetMap getCategoriaOfMovimento(int cdConta, int cdMovimentoConta, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					   "SELECT A.*, B.*, C.nm_centro_custo, C.nr_centro_custo " +
			           "FROM adm_movimento_conta_categoria A " +
			           "JOIN adm_categoria_economica B ON (A.cd_categoria_economica = B.cd_categoria_economica) "+
			           "LEFT OUTER JOIN ctb_centro_custo C ON (A.cd_centro_custo = C.cd_centro_custo) " +
			           "WHERE A.cd_conta = " +cdConta+
			           "  AND A.cd_movimento_conta = "+cdMovimentoConta);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(SQLException sqlExpt) {
			sqlExpt.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCateogriaServices.getCategoriaOfMovimento: " + sqlExpt);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCateogriaServices.getCategoriaOfMovimento: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
     * @author Hugo
     * @category Administração Financeira
     * @param cdCategoriaEconimica Categoria econômica da qual se deseja ver os detalhes
     * @param nrMes Mês do qual se deseja verificar o movimento
     * @param nrAno Ano do qual se deseja verificar o movimento
     * @return ResultSetMap Relação de contas classificadas com a categoria desejada
     */
	public static ResultSetMap getDetalheOfCategoriaPeriodo(int cdEmpresa, int cdCategoriaEconomica, int nrMes, int nrAno, int cdConta) {
		Connection connect = Conexao.conectar();
		try	{
			GregorianCalendar dtInicial = new GregorianCalendar(nrAno, nrMes, 1, 0, 0, 0);
			GregorianCalendar dtFinal   = new GregorianCalendar(nrAno, nrMes, dtInicial.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
			return getDetalheOfCategoriaPeriodo(cdEmpresa, cdCategoriaEconomica, dtInicial, dtFinal, cdConta, connect);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	/**
     * @author Hugo
     * @category Administração Financeira
     * @param cdCategoriaEconimica Categoria econômica da qual se deseja ver os detalhes
     * @param dtInicial Inicio do período do qual se deseja ver o detalhamento
     * @param dtFinal Final do período do qual se deseja ver o detalhamento
     * @return ResultSetMap Relação de contas classificadas com a categoria desejada
     */
	public static ResultSetMap getDetalheOfCategoriaPeriodo(int cdEmpresa, int cdCategoriaEconomica, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdConta, Connection connect) {
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					   "SELECT A.cd_conta, A.cd_movimento_conta, A.vl_movimento_categoria, A.cd_centro_custo, B.dt_movimento, B.vl_movimento," +
					   "       F.cd_conta_receber, C.cd_conta_pagar, A1.tp_conta, " +
					   "       C.vl_pago, C.vl_multa AS vl_multa_paga, C.vl_juros AS vl_juros_pago, C.vl_desconto AS vl_desconto_recebido, " +
					   "       D.dt_vencimento AS dt_vencimento_pagar, D.nr_documento AS nr_documento_pagar, " +
					   "       D.ds_historico AS ds_historico_pagar, E.nm_pessoa AS nm_favorecido, " +
					   "       A.vl_movimento_categoria AS vl_recebido, F.vl_multa AS vl_multa_recebida, F.vl_juros AS vl_juros_recebido, F.vl_desconto AS vl_desconto_concedido, " +
					   "       G.dt_vencimento AS dt_vencimento_receber, G.nr_documento AS nr_documento_receber, " +
					   "       g.ds_historico AS ds_historico_receber, H.nm_pessoa AS nm_sacado," +
					   "       A1.nm_conta, A1.nr_conta " +
			           "FROM adm_movimento_conta_categoria A " +
			           "JOIN adm_conta_financeira A1 ON (A.cd_conta = A1.cd_conta) " +
			           "JOIN adm_movimento_conta B ON (A.cd_conta = B.cd_conta " +
			           "                           AND A.cd_movimento_conta = B.cd_movimento_conta) "+
			           // Pagamentos
			           "LEFT OUTER JOIN adm_movimento_conta_pagar C ON (A.cd_conta = C.cd_conta " +
			           "                                            AND A.cd_movimento_conta = C.cd_movimento_conta " +
			           "											AND ((A.cd_conta_pagar = C.cd_conta_pagar " +
			           "											AND NOT A.cd_conta_pagar IS NULL) OR" +
			           "											(A.cd_conta_pagar IS NULL AND " +
			           "											 C.cd_conta_pagar IN (SELECT C1.cd_conta_pagar " +
			           "																 FROM adm_movimento_conta_pagar C1, adm_conta_pagar_categoria C2 " +
			           "																 WHERE C1.cd_conta = C.cd_conta " +
			           "																   AND C1.cd_movimento_conta = C.cd_movimento_conta " +
			           "																   AND C1.cd_conta_pagar = C2.cd_conta_pagar " +
			           "																   AND C2.cd_categoria_economica = A.cd_categoria_economica)))) "+
			           "LEFT OUTER JOIN adm_conta_pagar           D ON (D.cd_conta_pagar = C.cd_conta_pagar) "+
			           "LEFT OUTER JOIN grl_pessoa                E ON (E.cd_pessoa = D.cd_pessoa) "+
			           // Recebimentos
			           "LEFT OUTER JOIN adm_movimento_conta_receber F ON (A.cd_conta = F.cd_conta " +
			           "                                              AND A.cd_movimento_conta = F.cd_movimento_conta " +
			           "											  AND ((A.cd_conta_receber = F.cd_conta_receber AND " +
			           "													NOT A.cd_conta_receber IS NULL) OR " +
			           "											  (A.cd_conta_receber IS NULL AND " +
			           "											   F.cd_conta_receber IN (SELECT F1.cd_conta_receber " +
			           "																 FROM adm_movimento_conta_receber F1, adm_conta_receber_categoria F2 " +
			           "																 WHERE F1.cd_conta = F.cd_conta " +
			           "																   AND F1.cd_movimento_conta = F.cd_movimento_conta " +
			           "																   AND F1.cd_conta_receber = F2.cd_conta_receber " +
			           "																   AND F2.cd_categoria_economica = A.cd_categoria_economica)))) "+
			           "LEFT OUTER JOIN adm_conta_receber           G ON (G.cd_conta_receber = F.cd_conta_receber) "+
			           "LEFT OUTER JOIN grl_pessoa                  H ON (H.cd_pessoa = G.cd_pessoa) "+
			           "WHERE A.cd_categoria_economica = " +cdCategoriaEconomica+
			           (cdConta>0 ? " AND A.cd_conta = "+cdConta : "")+
			           (cdEmpresa>0 ? " AND A1.cd_empresa = "+cdEmpresa : "")+
			           "  AND B.st_movimento IN ("+MovimentoContaServices.ST_CONFERIDO+","+MovimentoContaServices.ST_CONCILIADO+","+
			                                       MovimentoContaServices.ST_LIQUIDADO+")"+
			           "  AND B.dt_movimento BETWEEN ? AND ? " +
			           "ORDER BY B.dt_movimento ");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			ResultSetMap rsmReturn = new ResultSetMap();
			while(rsm.next())	{
				HashMap<String,Object> register = new HashMap<String,Object>();
				register.put("CD_CONTA", rsm.getInt("cd_conta"));
				register.put("CD_MOVIMENTO_CONTA", rsm.getInt("cd_movimento_conta"));
				register.put("TP_CONTA", rsm.getObject("tp_conta"));
				register.put("NR_CONTA", rsm.getString("nr_conta"));
				register.put("NM_CONTA", rsm.getString("nm_conta"));
				register.put("DT_MOVIMENTO", rsm.getObject("dt_movimento"));
				register.put("VL_MOVIMENTO", rsm.getFloat("vl_movimento")+0);
				register.put("VL_MOVIMENTO_CATEGORIA", rsm.getFloat("vl_movimento_categoria")+0);
				register.put("CD_CENTRO_CUSTO", rsm.getInt("cd_centro_custo"));
				if(rsm.getInt("cd_conta_receber")>0)	{
					register.put("CD_CONTA_RECEBER", rsm.getInt("cd_conta_receber"));
					register.put("DT_VENCIMENTO", rsm.getObject("dt_vencimento_receber"));
					register.put("VL_PAGO", rsm.getFloat("vl_recebido")+0);
					register.put("VL_JUROS", rsm.getFloat("vl_juros_recebido")+0);
					register.put("VL_MULTA", rsm.getFloat("vl_multa_recebida")+0);
					register.put("VL_DESCONTO", rsm.getFloat("vl_desconto_concedido")+0);
					register.put("NM_PESSOA", rsm.getString("nm_sacado"));
					register.put("NR_DOCUMENTO", rsm.getString("nr_documento_receber"));
					register.put("DS_HISTORICO", rsm.getString("ds_historico_receber"));
				}
				if(rsm.getInt("cd_conta_pagar")>0)	{
					register.put("CD_CONTA_PAGAR", rsm.getInt("cd_conta_pagar"));
					register.put("DT_VENCIMENTO", rsm.getObject("dt_vencimento_pagar"));
					register.put("VL_PAGO", rsm.getFloat("vl_pago")+0);
					register.put("VL_JUROS", rsm.getFloat("vl_juros_pago")+0);
					register.put("VL_MULTA", rsm.getFloat("vl_multa_paga")+0);
					register.put("VL_DESCONTO", rsm.getFloat("vl_desconto_recebido")+0);
					register.put("NM_PESSOA", rsm.getString("nm_favorecido"));
					register.put("NR_DOCUMENTO", rsm.getString("nr_documento_pagar"));
					register.put("DS_HISTORICO", rsm.getString("ds_historico_pagar"));
				}
				rsmReturn.addRegister(register);
			}
			ArrayList<String> orderBy = new ArrayList<String>();
			orderBy.add("DT_MOVIMENTO");
			orderBy.add("NM_PESSOA");
			rsmReturn.orderBy(orderBy);
			rsmReturn.beforeFirst();
			return rsmReturn;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCateogriaServices.getDetalheOfCategoriaPeriodo: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
     * @author Hugo
     * @category Administração Financeira
     * @param cdEmpresa Código da empresa da qual se deseja ver os movimentos
     * @param nrMes Mês do qual se deseja verificar o movimento
     * @param nrAno Ano do qual se deseja verificar o movimento
     * @return Array com os totais agrupados em Receita e Despesa
     */
	public static float[] getDespesaReceita(int cdEmpresa, int nrMes, int nrAno) {
		Connection connect = Conexao.conectar();
		try	{
			GregorianCalendar dtInicial = new GregorianCalendar(nrAno, nrMes, 1, 0, 0, 0);
			GregorianCalendar dtFinal   = new GregorianCalendar(nrAno, nrMes, dtInicial.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
			return getDespesaReceita(cdEmpresa, dtInicial, dtFinal, connect);
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	/**
     * @author Hugo
     * @category Administração Financeira
     * @param cdEmpresa Código da empresa da qual se deseja ver os movimentos
     * @param dtInicial Data inicial do período desejado
     * @param dtFinal Data final do período desejado
     * @return Array com os totais agrupados em Receita e Despesa
     */
	public static float[] getDespesaReceita(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		PreparedStatement pstmt;
		try {
			pstmt = connect.prepareStatement(
					   "SELECT C.tp_categoria_economica, COUNT(*) AS qt_itens, SUM(TRUNC(vl_movimento_categoria*100)/100) AS vl_total "+
					   "FROM adm_movimento_conta_categoria A "+
					   "JOIN adm_conta_financeira    B ON (A.cd_conta     = B.cd_conta) "+
					   "JOIN adm_categoria_economica C ON (A.cd_categoria_economica = C.cd_categoria_economica) "+
					   "JOIN adm_movimento_conta     D ON (A.cd_conta     = D.cd_conta "+
					   "                               AND A.cd_movimento_conta = D.cd_movimento_conta) "+
					   "WHERE D.dt_movimento BETWEEN ? AND ? "+
					   (cdEmpresa>0 ? " AND B.cd_empresa = "+cdEmpresa : "")+
					   "GROUP BY C.tp_categoria_economica");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSet rs = pstmt.executeQuery();
			float vlReceita = 0, vlDespesa = 0;
			while(rs.next())	{
				if(rs.getInt("tp_categoria_economica")==CategoriaEconomicaServices.TP_RECEITA)
					vlReceita += rs.getFloat("vl_total");
				else if(rs.getInt("tp_categoria_economica")==CategoriaEconomicaServices.TP_DESPESA)
					vlDespesa += rs.getFloat("vl_total");
				else if(rs.getInt("tp_categoria_economica")==CategoriaEconomicaServices.TP_DEDUCAO_RECEITA)
					vlReceita -= rs.getFloat("vl_total");
				else if(rs.getInt("tp_categoria_economica")==CategoriaEconomicaServices.TP_DEDUCAO_DESPESA)
					vlDespesa -= rs.getFloat("vl_total");
			}
			return new float[] {vlReceita, vlDespesa};
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCateogriaServices.getDespesaReceita: " + e);
			return new float[] {0, 0};
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
     * @author Hugo
     * @category Administração Financeira
     * @param cdCategoriaEconomica Código da categoria economica da qual deseja receber as categorias inferiores
     * @param connect Conexão com o banco de dados
     * @return Array de inteiros com a lista de códigos das categorias inferiores
     */
	private static ArrayList<Integer> getCategoriasInferiores(int cdCategoriaEconomica, Connection connect)	{
		boolean isConnetionNull = connect==null;
		if(isConnetionNull)
			connect = Conexao.conectar();
		ArrayList<Integer> r = new ArrayList<Integer>();
		try {
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT cd_categoria_economica FROM adm_categoria_economica " +
					                                                     "WHERE cd_categoria_superior = "+cdCategoriaEconomica).executeQuery());
			while(rsm.next())	{
				r.add(new Integer(rsm.getInt("cd_categoria_economica")));
				r.addAll(getCategoriasInferiores(rsm.getInt("cd_categoria_economica"), connect));
			}
			return r;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if(isConnetionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
     * @author Hugo
     * @category Administração Financeira
     * @param cdEmpresa Código da empresa da qual se deseja ver a classificação dos movimentos
     * @param nrMes Mês do qual se deseja ver os movimentos classificados em categorias
     * @param nrAno Ano do qual se deseja ver os movimentos classificados em categorias
     * @param tpCategoriaEconomica Filtra os tipos de categorias que deseja ver
     * @param cdCategoriaReceita Indica a categoria de receita e despesa da qual deseja ver a movimentação
     * @param cdCategoriaDespesa Indica a categoria de despesa e despesa da qual deseja ver a movimentação
     * @param connect Conexão com o banco de dados
     * @return ResultSetMap com as categorias nas quais está classificado o movimento
     */
	@SuppressWarnings("unchecked")
	public static ResultSetMap getMovimentoByCategoria(int cdEmpresa, int nrMesInicial, int nrAnoInicial, int nrMesFinal, int nrAnoFinal,
			int tpCategoriaEconomica, int cdCategoriaReceita, int cdCategoriaDespesa, int nrNivelMaximo, int cdConta, boolean showDetalhe,
			int tpDetalhe)
	{
		Connection connect = Conexao.conectar();
		try	{
			boolean lgHistorico   = (tpDetalhe >= 3);
			if(lgHistorico) tpDetalhe -= 3;
			boolean lgNrDocumento = (tpDetalhe > 0);

			ResultSetMap rsm = new ResultSetMap();
			String previousField = "";
			for(int nrAno=nrAnoInicial, nrMes = nrMesInicial; (nrMes+nrAno*12)<=(nrMesFinal+nrAnoFinal*12);)	{
				GregorianCalendar dtInicial = new GregorianCalendar(nrAno, nrMes, 1, 0, 0, 0);
				GregorianCalendar dtFinal   = new GregorianCalendar(nrAno, nrMes, dtInicial.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
				ResultSetMap rsmTemp 		= getMovimentoByCategoria(cdEmpresa, dtInicial, dtFinal, tpCategoriaEconomica, cdCategoriaReceita, cdCategoriaDespesa, nrNivelMaximo, cdConta, connect);
				String fieldName 	 		= Util.fillNum(nrMes+1, 2)+"_"+Util.fillNum(nrAno, 4);
				while(rsmTemp.next())	{
					if(rsm.locate("cd_categoria_economica", rsmTemp.getInt("cd_categoria_economica")))	{
						rsm.setValueToField("VL_"+fieldName, rsmTemp.getFloat("vl_total_categoria"));
						rsm.setValueToField("QT_"+fieldName, rsmTemp.getInt("qt_itens"));
						rsm.setValueToField("DS_VL_"+fieldName, rsmTemp.getString("ds_vl_total_categoria"));
						float vlPrevious = rsm.getFloat("VL_"+previousField);
						rsm.setValueToField("PR_"+fieldName, new Float(vlPrevious > 0 ? (double)Math.round((rsmTemp.getFloat("vl_total_categoria")-vlPrevious) / vlPrevious * 100) : 0));
						rsm.setValueToField("DS_VL_TOTAL_CATEGORIA", Util.formatNumber(rsm.getFloat("vl_total_categoria")+rsmTemp.getFloat("vl_total_categoria"))+Util.fill("", (rsmTemp.getInt("nr_nivel"))*10, ' ', 'E'));
						rsm.setValueToField("VL_TOTAL_CATEGORIA", rsm.getFloat("vl_total_categoria")+rsmTemp.getFloat("vl_total_categoria"));
						rsm.setValueToField("VL_TOTAL_RECEITA", rsm.getFloat("vl_total_receita")+rsmTemp.getFloat("vl_total_receita"));
						rsm.setValueToField("VL_TOTAL_DESPESA", rsm.getFloat("vl_total_despesa")+rsmTemp.getFloat("vl_total_despesa"));
					}
					else	{
						HashMap<String,Object> reg = rsmTemp.getRegister();
						reg.put("VL_"+fieldName, rsmTemp.getFloat("vl_total_categoria"));
						reg.put("QT_"+fieldName, rsmTemp.getInt("qt_itens"));
						reg.put("DS_VL_"+fieldName, rsmTemp.getString("ds_vl_total_categoria"));
						reg.put("PR_"+fieldName, new Float(0));
						reg.put("VL_TOTAL_CATEGORIA", rsmTemp.getFloat("vl_total_categoria"));
						reg.put("DS_VL_TOTAL_CATEGORIA", Util.formatNumber(rsmTemp.getFloat("vl_total_categoria"))+Util.fill("", (rsmTemp.getInt("nr_nivel"))*10, ' ', 'E'));
						rsm.addRegister(reg);
						// Buscando detalhamento
						if(nrMesInicial==nrMesFinal && nrAnoInicial==nrAnoFinal && showDetalhe)	{
							ResultSetMap rsmDetalhe = getDetalheOfCategoriaPeriodo(cdEmpresa, rsmTemp.getInt("cd_categoria_economica"), dtInicial, dtFinal, cdConta, connect);
							float vlTotal = rsmTemp.getFloat("vl_total_categoria");
							int   qtItens = rsmTemp.getInt("qt_itens");
							rsmDetalhe.beforeFirst();
							while(rsmDetalhe.next())	{
								qtItens--;
								vlTotal -= rsmDetalhe.getFloat("vl_pago");
								HashMap<String,Object> regDetalhe = (HashMap<String,Object>)reg.clone();
								regDetalhe.put("CD_CONTA_PAGAR", rsmDetalhe.getInt("cd_conta_pagar"));
								regDetalhe.put("CD_CONTA_RECEBER", rsmDetalhe.getInt("cd_conta_receber"));
								regDetalhe.put("VL_TOTAL_CATEGORIA", null);
								regDetalhe.put("DS_VL_TOTAL_CATEGORIA", "");
								regDetalhe.put("VL_"+fieldName, null);
								regDetalhe.put("QT_"+fieldName, null);
								regDetalhe.put("DS_VL_"+fieldName, null);
								regDetalhe.put("PR_"+fieldName, null);
								regDetalhe.put("QT_ITENS", "");
								regDetalhe.put("LG_DETALHE", "1");
								regDetalhe.put("NR_CATEGORIA_ECONOMICA", regDetalhe.get("NR_CATEGORIA_ECONOMICA")+". Detalhe");
								String temp = (rsmDetalhe.getString("nm_pessoa")!=null?rsmDetalhe.getString("nm_pessoa"):"Sem Favorecido/Sacado ");
								boolean truncateDetalhes = false;
								temp = temp.length()>23 && truncateDetalhes ? temp.substring(0,22)+"..." : temp;
								String dsDetalhe = Util.fill("", (rsmTemp.getInt("nr_nivel"))*10+3, ' ', 'E')+ temp+" - "+
												   Util.formatDateTime(rsmDetalhe.getGregorianCalendar("dt_movimento"),"dd/MM/yyyy")+" - "+
												   Util.formatNumber(rsmDetalhe.getFloat("vl_pago"));
								if(lgNrDocumento)
									dsDetalhe += ", Doc: "+rsmDetalhe.getString("nr_documento");
								if(lgHistorico)	{
									temp = rsmDetalhe.getString("ds_historico")!=null ? ", "+rsmDetalhe.getString("ds_historico") : "";
									temp = temp.length()>30 && truncateDetalhes ? temp.substring(0,30)+"..." : temp;
									dsDetalhe += temp;
								}
								regDetalhe.put("DS_CATEGORIA_ECONOMICA", dsDetalhe);
								rsm.addRegister(regDetalhe);
							}
							// ERRO
							if((qtItens>0 || vlTotal>0.03) && rsmDetalhe.size()>0){
								HashMap<String,Object> regDetalhe = (HashMap<String,Object>)reg.clone();
								regDetalhe.put("VL_TOTAL_CATEGORIA", null);
								regDetalhe.put("DS_VL_TOTAL_CATEGORIA", "");
								regDetalhe.put("VL_"+fieldName, null);
								regDetalhe.put("QT_"+fieldName, null);
								regDetalhe.put("DS_VL_"+fieldName, null);
								regDetalhe.put("PR_"+fieldName, null);
								regDetalhe.put("QT_ITENS", "");
								regDetalhe.put("LG_DETALHE", "0");
								regDetalhe.put("NR_CATEGORIA_ECONOMICA", regDetalhe.get("NR_CATEGORIA_ECONOMICA")+". Erro");
								String temp = "Inconsistência ";
								String dsDetalhe = Util.fill("", (rsmTemp.getInt("nr_nivel"))*10+3, ' ', 'E')+ temp+" - "+
												   "Diferença: "+Util.formatNumber(vlTotal)+
												   ". Faltando "+qtItens+" iten(s).";
								regDetalhe.put("DS_CATEGORIA_ECONOMICA", dsDetalhe);
								rsm.addRegister(regDetalhe);
							}
						}
					}
				}
				previousField = fieldName;
				nrMes++;
				if(nrMes>12)	{
					nrMes = 1;
					nrAno++;
				}
			}
			ArrayList<String> orderBy = new ArrayList<String>();
			orderBy.add("NR_CATEGORIA_ECONOMICA");
			rsm.orderBy(orderBy);
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e)	{
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public static Result gerarRelatorioMovimentoByCategoria(HashMap filtros, int cdEmpresa, int nrMesInicial, int nrAnoInicial, int nrMesFinal, int nrAnoFinal,
			int tpCategoriaEconomica, int cdCategoriaReceita, int cdCategoriaDespesa, int nrNivelMaximo, int cdConta, boolean showDetalhe,
			int tpDetalhe){
		
		Connection connect = Conexao.conectar();
		try	{
			HashMap<String, Object> params = new HashMap<String, Object>();
			SimpleDateFormat mesAnoFormat = new SimpleDateFormat("MMMM/yyyy");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			HashMap<String, Object> register = new HashMap<String, Object>();
			GregorianCalendar dtPeriodoInicial=null, dtPeriodoFinal=null;
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			
			if( filtros.get("PERIODO_INICIAL") !=null )
				dtPeriodoInicial = Util.stringToCalendar( (String) filtros.get("PERIODO_INICIAL") ) ;
						
			if( filtros.get("PERIODO_FINAL") !=null )
				dtPeriodoFinal = Util.stringToCalendar( (String) filtros.get("PERIODO_FINAL") ) ;
			
			GregorianCalendar dtInicial = (dtPeriodoInicial !=null)? dtPeriodoInicial : new GregorianCalendar(nrAnoInicial, nrMesInicial, 1, 0, 0, 0);
			GregorianCalendar dtFinal   = (dtPeriodoFinal !=null)? dtPeriodoFinal : new GregorianCalendar(nrAnoFinal, nrMesFinal, dtInicial.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
			
			params.put("NM_PERIODO_INICIAL", dateFormat.format( dtInicial.getTime() )  );
			params.put("NM_PERIODO_FINAL", dateFormat.format( dtFinal.getTime() ) );
			StringBuffer strBuffer = new StringBuffer( mesAnoFormat.format( dtInicial.getTime() ) );
			strBuffer.append(" a ");
			strBuffer.append( mesAnoFormat.format( dtFinal.getTime() ) );
			params.put("MES_ANO", strBuffer.toString() );
		
			Result result = new Result(1);
			ResultSetMap rsm = new ResultSetMap();
			if( filtros.get("CATEGORIA_ECONOMICA") != null ){
				
				//RECEITAS ( Exceto Combustível )
				ResultSetMap rsmDespesas = new ResultSetMap();
				ResultSetMap rsmReceitas = Search.find(" SELECT D3.nm_grupo as NM_CATEGORIA_ECONOMICA, SUM(B.vl_unitario * B.qt_saida) as VL_TOTAL_CATEGORIA, "+ 
															" SUM(B.qt_saida) AS QT_ITENS, SUM( B.qt_saida * C2.vl_custo_medio ) as VL_CUSTO_CATEGORIA "+
															" FROM alm_documento_saida A "+
															" JOIN alm_documento_saida_item B ON (A.cd_documento_saida = B.cd_documento_saida ) "+
															" JOIN grl_produto_servico C ON (B.cd_produto_servico = C.cd_produto_servico ) "+
															" JOIN grl_produto_servico_empresa C2 ON (C.cd_produto_servico = C2.cd_produto_servico AND B.cd_empresa = C2.cd_empresa  ) "+
															" JOIN alm_produto_grupo D ON (C.cd_produto_servico = D.cd_produto_servico AND A.cd_empresa = D.cd_empresa) "+
															" JOIN alm_grupo D2 ON ( D.cd_grupo = D2.cd_grupo ) "+
															" JOIN alm_grupo D3 ON ( D2.cd_grupo_superior = D3.cd_grupo ) "+
															" WHERE A.st_documento_saida = "+DocumentoSaidaServices.ST_CONCLUIDO+
															" AND D2.cd_grupo NOT IN "+ GrupoServices.getAllCombustivel(cdEmpresa, connect) +" "+
															" AND ( A.DT_DOCUMENTO_SAIDA >= '"+sqlDateFormat.format( dtInicial.getTime() )+"' AND A.DT_DOCUMENTO_SAIDA <= '"+sqlDateFormat.format( dtFinal.getTime() )+"' ) ",
															" GROUP BY D3.NM_GRUPO ORDER BY D3.NM_GRUPO ", null, connect, false);
				
				
				//RECEITAS COMBUSTÍVEL
				ResultSetMap rsmEncerrantes = Search.find(" SELECT 'Combustível' AS NM_CATEGORIA_ECONOMICA , "+
												"  SUM(A.qt_litros) AS QT_ITENS, "+
												"  SUM(A.vl_preco * qt_litros) AS VL_TOTAL_CATEGORIA, SUM(A.qt_litros * DE.vl_custo_medio) AS VL_CUSTO_CATEGORIA"+
												" FROM pcb_bico_encerrante A "+
												" JOIN pcb_bico                    	B   ON (A.cd_bico = B.cd_bico) "+ 
												" JOIN pcb_tanque                  	C   ON (B.cd_tanque = C.cd_tanque) "+  
												" JOIN grl_produto_servico         	D   ON (C.cd_produto_servico = D.cd_produto_servico) "+  
												" JOIN grl_produto_servico_empresa 	DE  ON (C.cd_produto_servico = DE.cd_produto_servico AND DE.cd_empresa = "+cdEmpresa+" ) "+
												" JOIN adm_conta_fechamento         E   ON (A.cd_fechamento = E.cd_fechamento) " +
												" WHERE ( E.DT_FECHAMENTO >= '"+sqlDateFormat.format( dtInicial.getTime() )+"' AND E.DT_FECHAMENTO <= '"+sqlDateFormat.format( dtFinal.getTime() )+"' ) ",    
												" GROUP BY NM_CATEGORIA_ECONOMICA " ,
											 	null, connect, false);
				if( rsmEncerrantes.next() )
					rsmReceitas.getLines().add(0, rsmEncerrantes.getRegister() );
			
				rsmReceitas.beforeFirst();
				int nrCategoria = 1;
				while( rsmReceitas.next() ){
					rsmReceitas.setValueToField("NR_CATEGORIA_ECONOMICA", "1.1.1.001."+String.format("%04d", nrCategoria ));
					rsmReceitas.setValueToField("NM_CATEGORIA_ECONOMICA", Util.capitular( rsmReceitas.getString("NM_CATEGORIA_ECONOMICA") ) );
					rsmReceitas.setValueToField("NR_NIVEL", 5 );
					rsmReceitas.setValueToField("ESTILO", COR_VERDE);
					
					HashMap<String, Object> registerDespesa = new HashMap<String, Object>();
					registerDespesa.put("NR_CATEGORIA_ECONOMICA", "2.1.1.010."+String.format("%04d", nrCategoria ) );
					registerDespesa.put("NM_CATEGORIA_ECONOMICA", "Custo "+Util.capitular( rsmReceitas.getString("NM_CATEGORIA_ECONOMICA") )+" Vendido" );
					registerDespesa.put("VL_TOTAL_CATEGORIA", rsmReceitas.getDouble("VL_CUSTO_CATEGORIA") );
					registerDespesa.put("QT_ITENS", rsmReceitas.getDouble("QT_ITENS") );
					registerDespesa.put("ESTILO", COR_VERMELHA);
					registerDespesa.put("NR_NIVEL", 5 );
					rsmDespesas.addRegister(registerDespesa);
					nrCategoria++;
				}
				
				HashMap<String, Object> registerReceita = new HashMap<String, Object>();
				registerReceita.put("NR_CATEGORIA_ECONOMICA", "1");
				registerReceita.put("NM_CATEGORIA_ECONOMICA", "Receita");
				registerReceita.put("NR_NIVEL", 1);
				rsmReceitas.getLines().add( 0, registerReceita);
				
				HashMap<String, Object> registerReceitaBruta = new HashMap<String, Object>();
				registerReceitaBruta.put("NR_CATEGORIA_ECONOMICA", "1.1");
				registerReceitaBruta.put("NM_CATEGORIA_ECONOMICA", "Receita Bruta");
				registerReceitaBruta.put("NR_NIVEL", 2);
				rsmReceitas.getLines().add( 1, registerReceitaBruta);
				
				HashMap<String, Object> registerReceitaBrutaOperacional = new HashMap<String, Object>();
				registerReceitaBrutaOperacional.put("NR_CATEGORIA_ECONOMICA", "1.1.1");
				registerReceitaBrutaOperacional.put("NM_CATEGORIA_ECONOMICA", "Receita Bruta Operacional");
				registerReceitaBrutaOperacional.put("NR_NIVEL", 3);
				rsmReceitas.getLines().add( 2, registerReceitaBrutaOperacional);
				
				HashMap<String, Object> registerVendas = new HashMap<String, Object>();
				registerVendas.put("NR_CATEGORIA_ECONOMICA", "1.1.1.001");
				registerVendas.put("NM_CATEGORIA_ECONOMICA", "Vendas");
				registerVendas.put("NR_NIVEL", 4);
				rsmReceitas.getLines().add( 3, registerVendas);
				
				
				HashMap<String, Object> registerDespesa = new HashMap<String, Object>();
				registerDespesa.put("NR_CATEGORIA_ECONOMICA", "2.1.1.010");
				registerDespesa.put("NM_CATEGORIA_ECONOMICA", "Dedução da Receita Bruta");
				registerDespesa.put("ESTILO", COR_VERMELHA);
				registerDespesa.put("NR_NIVEL", 1);
				rsmReceitas.addRegister(registerDespesa);
				
				rsmReceitas.getLines().addAll( rsmDespesas.getLines() );
				
				register.put("CATEGORIA_ECONOMICA", rsmReceitas.getLines());
				params.put("CATEGORIA_ECONOMICA", rsmReceitas.getLines());
				/*
				 * Despesas operacionais
				 */
				params.put("DESPESAS", getDespesasDRE(criterios, connect).getLines());
				/*
				 * Despesas não operacionais
				 */
				params.put("RECEITA_BRUTA", getReceitaBrutaDRE(criterios, connect).getLines());
				/*
				 * Contas a receber
				 */
				params.put("CONTA_RECEBER", ContaReceberServices.getDreContasReceber(cdEmpresa, connect).getLines());
				/*
				 * Estoque
				 */
				ArrayList<ItemComparator> crt = new ArrayList<ItemComparator>();
				crt.add(new ItemComparator("cdEmpresa", String.valueOf(cdEmpresa), ItemComparator.EQUAL, Types.INTEGER));
				crt.add(new ItemComparator("dtFim", Util.convCalendarStringSql(dtFinal), ItemComparator.EQUAL, Types.DATE));
				params.put("ESTOQUE", ProdutoEstoqueServices.getDreEstoque(crt, connect).getLines());
			}
			/*
			 * Recebíveis, contas a receber que já foram efetuadas as vendas mas não recebeu um valor ainda por depender de operadora do crédito, ex.: cartão, cheques, etc.
			 * Entradas, entrou na lista para ser recebido
			 * Saidas, liquidou o valor então saiu da lista a ser recebido
			 */
			if( filtros.get("RECEBIVEIS_ENTRADAS") != null ){
				Integer nivelDetalheRecEntradas = ( filtros.get("NR_DETALHE_RECEBIVEIS_ENTRADAS") != null )? Integer.parseInt( (String)filtros.get("NR_DETALHE_RECEBIVEIS_ENTRADAS") ): null ;
				ResultSetMap rsmRecebiveisEntradas = ContaReceberServices.getDemonstrativoEntradasPorPeriodo(cdEmpresa, dtInicial, dtFinal, nivelDetalheRecEntradas, connect);
				params.put("RECEBIVEIS_ENTRADAS", rsmRecebiveisEntradas.getLines());
			}
			if( filtros.get("RECEBIVEIS_SAIDAS") != null ){
				Integer nivelDetalheRecSaidas = ( filtros.get("NR_DETALHE_RECEBIVEIS_SAIDAS") != null )? Integer.parseInt( (String)filtros.get("NR_DETALHE_RECEBIVEIS_SAIDAS") ): null ;
				ResultSetMap rsmRecebiveisSaidas = ContaReceberServices.getDemonstrativoSaidasPorPeriodo(cdEmpresa, dtInicial, dtFinal, nivelDetalheRecSaidas, connect);
				params.put("RECEBIVEIS_SAIDAS", rsmRecebiveisSaidas.getLines());
			}
			/*
			 * Ativos, saldo das contas financeiras
			 */
			if( filtros.get("ATIVOS") != null ){
				ResultSetMap rsmAtivos = ContaFinanceiraServices.getAtivosDRE(criterios, connect);
				params.put("ATIVOS", rsmAtivos.getLines());
			}
			/*
			 * Passivos, depesas
			 */
			if( filtros.get("PASSIVOS") != null ){
				ResultSetMap rsmPassivos = ContaFinanceiraServices.getPassivosDRE(criterios, connect);
				params.put("PASSIVOS", rsmPassivos.getLines());
			}
			
			rsm.addRegister( register );
			result.addObject("rsm", rsm);
			result.addObject("params", params);
			return result;
		}catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCateogriaServices.gerarRelatorioMovimentoByCategoria: " + e);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	/**
	 * Calcula o subrelatório de receitas 
	 * 
	 * @param object
	 * @param connect
	 * @return
	 */
	private static ResultSetMap getReceitaBrutaDRE(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect == null;
		connect = isConnectionNull ? Conexao.conectar() : connect;
		try	{
			ResultSetMap resultSetMap = Search.find("SELECT"
														+ " (SELECT COUNT(*) FROM adm_conta_receber A1 "
															+ " LEFT JOIN adm_conta_receber_categoria C1 ON C1.cd_conta_receber = A1.cd_conta_receber " 
															+ " LEFT JOIN adm_categoria_economica D1 ON D1.cd_categoria_economica = C1.cd_categoria_economica " 
															+ " WHERE D1.cd_categoria_economica = D.cd_categoria_economica "
														+ ") AS QT_ITENS, "
														+ " (SELECT SUM(A1.vl_conta) FROM adm_conta_receber A1 " 
															+ "	LEFT JOIN adm_conta_receber_categoria C1 ON C1.cd_conta_receber = A1.cd_conta_receber " 
															+ "	LEFT JOIN adm_categoria_economica D1 ON D1.cd_categoria_economica = C1.cd_categoria_economica " 
															+ " WHERE D1.cd_categoria_economica = D.cd_categoria_economica "
														+ " ) AS VL_TOTAL_CATEGORIA, "	
														+ " D.nr_categoria_economica, D.nm_categoria_economica, D.nr_nivel, D2.nm_categoria_economica AS nm_categoria_superior,"
														+ " D2.nr_categoria_economica AS nr_categoria_superior, D2.nr_nivel AS nr_nivel_superior"
													+ " FROM adm_conta_receber A "
													+ " LEFT JOIN alm_documento_saida B ON (B.cd_documento_saida = A.cd_documento_saida) "
													+ " LEFT JOIN adm_conta_receber_categoria C ON C.cd_conta_receber = A.cd_conta_receber "
													+ " LEFT JOIN adm_categoria_economica D ON D.cd_categoria_economica = C.cd_categoria_economica "
													+ " LEFT JOIN adm_categoria_economica D2 ON D2.cd_categoria_economica = D.cd_categoria_superior "
													+ " WHERE B.cd_documento_saida IS NULL",
													" GROUP BY D2.nm_categoria_economica, D2.nr_categoria_economica, D2.nr_nivel ,D.nr_categoria_economica, D.nm_categoria_economica, D.cd_categoria_economica  ORDER BY D2.nr_categoria_economica",
													criterios, connect, false);
			
			if(resultSetMap.next()){
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("NR_CATEGORIA_ECONOMICA", "1.1.2");
				register.put("NM_CATEGORIA_ECONOMICA", "RECEITA BRUTA NÃO OPERACIONAL");
				register.put("NR_NIVEL", 1);
				resultSetMap.getLines().add( 0, register);
			}
			resultSetMap.beforeFirst();
			return resultSetMap;
		}catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCateogriaServices.gerarRelatorioMovimentoByCategoria: " + e);
			if(isConnectionNull){
				Conexao.desconectar(connect);
			}
			return null;
		}
	}

	/**
	 * Calcula subrelatório de despesas do relatório de DRE
	 * 
	 * @param connect
	 * @return
	 */
	private static ResultSetMap getDespesasDRE(ArrayList<ItemComparator> criterios,Connection connect) {
		boolean isConnectionNull = connect == null;
		connect = isConnectionNull ? Conexao.conectar() : connect;
		try	{
			ResultSetMap resultSetMap = Search.find("SELECT"
														+ " (SELECT COUNT(*) FROM adm_conta_pagar A1 "
															+ " LEFT JOIN adm_conta_pagar_categoria C1 ON C1.cd_conta_pagar = A1.cd_conta_pagar " 
															+ " LEFT JOIN adm_categoria_economica D1 ON D1.cd_categoria_economica = C1.cd_categoria_economica " 
															+ " WHERE D1.cd_categoria_economica = D.cd_categoria_economica "
														+ ") AS QT_ITENS, "
														+ " (SELECT SUM(A1.vl_conta) FROM adm_conta_pagar A1 " 
															+ "	LEFT JOIN adm_conta_pagar_categoria C1 ON C1.cd_conta_pagar = A1.cd_conta_pagar " 
															+ "	LEFT JOIN adm_categoria_economica D1 ON D1.cd_categoria_economica = C1.cd_categoria_economica " 
															+ " WHERE D1.cd_categoria_economica = D.cd_categoria_economica "
														+ " ) AS VL_TOTAL_CATEGORIA, "	
														+ " D.nr_categoria_economica, D.nm_categoria_economica, D.nr_nivel "
													+ " FROM adm_conta_pagar A "
													+ " LEFT JOIN alm_documento_entrada B ON (B.cd_documento_entrada = A.cd_documento_entrada) "
													+ " LEFT JOIN adm_conta_pagar_categoria C ON C.cd_conta_pagar = A.cd_conta_pagar "
													+ " LEFT JOIN adm_categoria_economica D ON D.cd_categoria_economica = C.cd_categoria_economica "
													+ " WHERE B.cd_documento_entrada IS NULL",
													" GROUP BY D.nr_categoria_economica, D.nm_categoria_economica, D.cd_categoria_economica ORDER BY D.nr_categoria_economica",
													criterios, connect, false);
			
			
			
			if(resultSetMap != null && resultSetMap.next()){
				HashMap<String, Object> register = new HashMap<String, Object>();
				register.put("NR_CATEGORIA_ECONOMICA", "2.1");
				register.put("NM_CATEGORIA_ECONOMICA", "DESPESAS OPERACIONAIS");
				register.put("NR_NIVEL", 1);
				register.put("ESTILO", COR_VERMELHA);
				resultSetMap.getLines().add( 1, register);
				
				register = new HashMap<String, Object>();
				register.put("NR_CATEGORIA_ECONOMICA", "2.1.1");
				register.put("NR_NIVEL", 2);
				register.put("ESTILO", COR_VERMELHA);
				register.put("NM_CATEGORIA_ECONOMICA", "DESPESAS OPERACIONAIS ADMINISTRATIVA");
				resultSetMap.getLines().add( 2, register);
			}
			
			resultSetMap.beforeFirst();
			
			while(resultSetMap.next()){
				resultSetMap.setValueToField("NR_NIVEL", resultSetMap.getInt("NR_NIVEL") + 1);
				resultSetMap.setValueToField("ESTILO", COR_VERMELHA);
			}
			
			return resultSetMap;
		}catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCateogriaServices.gerarRelatorioMovimentoByCategoria: " + e);
			if(isConnectionNull){
				Conexao.desconectar(connect);
			}
			return null;
		}
	}
	
	public static ResultSetMap getMovimentoByCategoria(GregorianCalendar dtInicial, GregorianCalendar dtFinal, ArrayList<ItemComparator> criterios){
		return getMovimentoByCategoria(dtInicial, dtFinal, criterios, null);
	}
	
	public static ResultSetMap getMovimentoByCategoria(GregorianCalendar dtInicial, GregorianCalendar dtFinal, ArrayList<ItemComparator> criterios, Connection connect){
		boolean isConnectionNull = connect==null;
		connect = isConnectionNull ? Conexao.conectar() : connect;
		PreparedStatement pstmt;
		try {
			int cdConta = 0;
			int cdEmpresa = 0;
			for (int i = 0; criterios != null && i < criterios.size(); i++){
				if (criterios.get(i).getColumn().equalsIgnoreCase("CD_CONTA"))
					cdConta = Integer.parseInt(criterios.get(i).getValue());
				else if (criterios.get(i).getColumn().equalsIgnoreCase("CD_EMPRESA"))
					cdEmpresa = Integer.parseInt(criterios.get(i).getValue());
			}
			
			ResultSetMap rsm = new ResultSetMap( connect.prepareStatement(
					" SELECT * FROM ADM_CATEGORIA_ECONOMICA   "+
					" ORDER BY NR_CATEGORIA_ECONOMICA " ).executeQuery());
			rsm.beforeFirst();
			ResultSetMap rsmTotalCategoria;
			Double vlMovCategoriaAtual = 0.0;
			Double vlMovCategoriaSuperior = 0.0;
			int currentPointer = 0;
			DecimalFormat df = new DecimalFormat("##0.00 '%'");
			while( rsm.next() ){
				if( rsm.getInt("CD_CATEGORIA_ECONOMICA") == 0 ){
					continue;
				}
				
				rsm.setValueToField("PR_MOVIMENTO_CATEGORIA", "0 %");
				vlMovCategoriaAtual = 0.0;
				
				pstmt = connect.prepareStatement(
						   " SELECT COUNT(*) AS qt_itens, SUM(TRUNC(vl_movimento_categoria*100)/100) AS vl_total_categoria "+
						   " FROM adm_movimento_conta_categoria A "+
						   " JOIN adm_movimento_conta     B ON (A.cd_conta           = B.cd_conta " +
						   "                               AND A.cd_movimento_conta = B.cd_movimento_conta) "+
						   " JOIN adm_conta_financeira    C ON (A.cd_conta           = C.cd_conta ) "+
						   " WHERE B.dt_movimento BETWEEN ? AND ? "+
						   " AND B.st_movimento IN ("+MovimentoContaServices.ST_CONFERIDO+","+MovimentoContaServices.ST_CONCILIADO+","+MovimentoContaServices.ST_LIQUIDADO+")"+
						   " AND A.cd_categoria_economica = "+rsm.getInt("CD_CATEGORIA_ECONOMICA")+
						   (cdConta > 0             ? " AND A.cd_conta = "+cdConta: "")+
						   (cdEmpresa > 0           ? " AND C.cd_empresa = "+cdEmpresa: ""));
				pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
				pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
				rsmTotalCategoria = new ResultSetMap(pstmt.executeQuery());
				rsmTotalCategoria.next();
				
				vlMovCategoriaAtual = rsmTotalCategoria.getDouble("VL_TOTAL_CATEGORIA");
				ResultSetMap rsmTemp = getTotalCategoriasFilhas(
														cdEmpresa,
														dtInicial, 
														dtFinal, 
														rsm.getInt("CD_CATEGORIA_ECONOMICA"), 
														cdConta, connect);
				
				if(rsmTemp.next())	{
		            rsm.setValueToField("VL_MOVIMENTO_CATEGORIA", rsmTemp.getDouble("VL_TOTAL_CATEGORIA"));
		            if( rsmTemp.getDouble("VL_TOTAL_CATEGORIA") > 0 ){
		            	
		            	rsm.setValueToField("PR_MOVIMENTO_CATEGORIA", "100 %");
		            	if( rsm.getInt("CD_CATEGORIA_SUPERIOR") > 0 ){
		            		currentPointer = rsm.getPointer();
		            		rsm.locate("CD_CATEGORIA_ECONOMICA", rsm.getInt("CD_CATEGORIA_SUPERIOR") );
		            		if( rsm.getDouble("VL_MOVIMENTO_SUPERIOR") > 0 ){
		            			vlMovCategoriaSuperior = rsm.getDouble("VL_MOVIMENTO_SUPERIOR");
		            		}else{
		            			vlMovCategoriaSuperior = rsm.getDouble("VL_MOVIMENTO_CATEGORIA");
		            		}
		            		rsm.goTo(currentPointer);
		            		rsm.setValueToField("PR_MOVIMENTO_CATEGORIA", df.format((rsm.getDouble("VL_MOVIMENTO_CATEGORIA")*100)/vlMovCategoriaSuperior));
		            		rsm.setValueToField("VL_MOVIMENTO_SUPERIOR", vlMovCategoriaSuperior );
		            	}
		            }
		            
		            //Extrai o valor classificado para uma categoria raiz em uma linha separada
		            ArrayList<Integer> cats = getCategoriasInferiores(rsm.getInt("CD_CATEGORIA_ECONOMICA"),  connect);
		            if( cats.size() > 0 && vlMovCategoriaAtual > 0 ){
		            	HashMap<String, Object> newReg = new HashMap<String, Object>();
		            	newReg.put("NR_CATEGORIA_ECONOMICA", rsm.getString("NR_CATEGORIA_ECONOMICA") );
		            	newReg.put("NR_NIVEL", rsm.getInt("NR_NIVEL")+1 );
		            	newReg.put("NM_CATEGORIA_ECONOMICA", rsm.getString("NM_CATEGORIA_ECONOMICA")+" ( NÃO ESPECIFICADAS ) " );
		            	newReg.put("VL_MOVIMENTO_CATEGORIA", vlMovCategoriaAtual );
		            	newReg.put("PR_MOVIMENTO_CATEGORIA", df.format((vlMovCategoriaAtual*100)/rsm.getDouble("VL_MOVIMENTO_CATEGORIA")) );
		            	
		            	rsm.getLines().add( rsm.getPointer()+1, newReg);
		            }
		    	}
			}
			
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCateogriaServices.getMovimentoByCategoria: " + e);
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
     * @author Hugo
     * @category Administração Financeira
     * @param cdEmpresa Código da empresa da qual se deseja ver a classificação dos movimentos
     * @param dtInicial Data inicial do período desejado
     * @param dtFinal Data final do período desejado
     * @param tpCategoriaEconomica Filtra os tipos de categorias que deseja ver
     * @param cdCategoriaReceita Indica a categoria de receita e despesa da qual deseja ver a movimentação
     * @param cdCategoriaDespesa Indica a categoria de despesa e despesa da qual deseja ver a movimentação
     * @param nrNivelMaximo Limite do nível máximo até o qual deseja ver detalhado o relatório
     * @param connect Conexão com o banco de dados
     * @return ResultSetMap com as categorias nas quais está classificado o movimento
     **/
	public static ResultSetMap getMovimentoByCategoria(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			int tpCategoriaEconomica, int cdCategoriaReceita, int cdCategoriaDespesa, int nrNivelMaximo, int cdConta, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		connect = isConnectionNull ? Conexao.conectar() : connect;
		PreparedStatement pstmt;
		try {
			float[] vlReceitaDespesa = getDespesaReceita(cdEmpresa, dtInicial, dtFinal, connect);
			String categorias = "";
			if(cdCategoriaReceita>0)	{
				categorias = String.valueOf(cdCategoriaReceita);
				ArrayList<Integer> cats = getCategoriasInferiores(cdCategoriaReceita, connect);
				for(int i=0; i<cats.size(); i++)	{
					categorias += ","+cats.get(i).intValue();
				}
			}
			if(cdCategoriaDespesa>0)	{
				categorias += (categorias.equals("")?"":",")+String.valueOf(cdCategoriaDespesa);
				ArrayList<Integer> cats = getCategoriasInferiores(cdCategoriaDespesa, connect);
				for(int i=0; i<cats.size(); i++)	{
					categorias += ","+cats.get(i).intValue();
				}
			}

			pstmt = connect.prepareStatement(
					   " SELECT C.nr_nivel, C.cd_categoria_superior, C.tp_categoria_economica, "+
					   "       A.cd_categoria_economica, nr_categoria_economica, nm_categoria_economica, "+
					   "       COUNT(*) AS qt_itens, SUM(TRUNC(vl_movimento_categoria*100)/100) AS vl_total_categoria "+
					   " FROM  adm_categoria_economica C "+
					   " LEFT JOIN adm_movimento_conta_categoria    A ON (A.cd_categoria_economica = C.cd_categoria_economica) "+
					   " LEFT JOIN adm_conta_financeira            B ON (A.cd_conta = B.cd_conta) "+
					   " LEFT JOIN adm_movimento_conta             D ON (A.cd_conta = D.cd_conta " +
					   "                                         		 AND A.cd_movimento_conta = D.cd_movimento_conta "+
					   "                                        ) "+
					   "WHERE D.dt_movimento BETWEEN ? AND ? "+
					   "  AND D.st_movimento IN ("+MovimentoContaServices.ST_CONFERIDO+","+MovimentoContaServices.ST_CONCILIADO+","+MovimentoContaServices.ST_LIQUIDADO+")"+
					   (tpCategoriaEconomica>=0 ? " AND C.tp_categoria_economica = "+tpCategoriaEconomica : "")+
					   (!categorias.equals("")  ? " AND C.cd_categoria_economica IN ("+categorias+")": "")+
					   (cdConta > 0  ? " AND A.cd_conta = "+cdConta: "")+
					   (cdEmpresa > 0  ? " AND B.cd_empresa = "+cdEmpresa: "")+
					   " GROUP BY C.nr_nivel, C.cd_categoria_superior, C.tp_categoria_economica, "+
					   "          A.cd_categoria_economica, nr_categoria_economica, nm_categoria_economica " +
					   " ORDER BY C.nr_nivel DESC");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next())	{
				int nrPosition = rsm.getPosition();
		        if (rsm.getInt("cd_categoria_superior", 0) > 0)	{
		        	int cdCategoriaSuperior = rsm.getInt("cd_categoria_superior", 0);
		            HashMap<String,Object> register = new HashMap<String,Object>();
		        	if (!rsm.locate("cd_categoria_economica", rsm.getObject("cd_categoria_superior"))) {
		        		CategoriaEconomica categoria = CategoriaEconomicaDAO.get(cdCategoriaSuperior, connect);
			            register = new HashMap<String,Object>();
			            register.put("CD_CATEGORIA_ECONOMICA", new Integer(categoria.getCdCategoriaEconomica()));
			            register.put("NM_CATEGORIA_ECONOMICA", categoria.getNmCategoriaEconomica());
			            register.put("TP_CATEGORIA_ECONOMICA", new Integer(categoria.getTpCategoriaEconomica()));
			            register.put("CD_CATEGORIA_SUPERIOR", new Integer(categoria.getCdCategoriaSuperior()));
			            register.put("NR_CATEGORIA_ECONOMICA", categoria.getNrCategoriaEconomica());
			            register.put("NR_NIVEL", new Integer(categoria.getNrNivel()));
			            register.put("VL_TOTAL_CATEGORIA", new Double(0));
			            register.put("QT_ITENS", new Integer(0));
			            register.put("LG_INATIVO", categoria.getLgAtivo());
			            rsm.addRegister(register);
		        	}
		        	else
		        		register = rsm.getRegister();
		        	if(register.get("NOCALL")==null)	{
			        	ResultSetMap rsmTemp = getTotalCategoriasFilhas(cdEmpresa, dtInicial, dtFinal, cdCategoriaSuperior, cdConta, connect);
			        	if(rsmTemp.next())	{
				            register.put("VL_TOTAL_CATEGORIA", rsmTemp.getObject("VL_TOTAL_CATEGORIA"));
				            register.put("QT_ITENS", rsmTemp.getObject("QT_ITENS"));
				            register.put("NOCALL", new Integer(1));
			        	}
		        	}
		        }
		        rsm.goTo(nrPosition);
			}
			ResultSetMap rsmRetorno = new ResultSetMap();
			rsm.beforeFirst();
			while(rsm.next())	{
				if(nrNivelMaximo>0 && rsm.getInt("nr_nivel")>nrNivelMaximo)	{
					continue;
				}
				rsm.setValueToField("DS_CATEGORIA_ECONOMICA", Util.fill("", (rsm.getInt("nr_nivel"))*10, ' ', 'E')+rsm.getString("nm_categoria_economica"));
				rsm.setValueToField("DS_VL_TOTAL_CATEGORIA", Util.formatNumber(rsm.getFloat("vl_total_categoria"))+Util.fill("", (rsm.getInt("nr_nivel"))*10, ' ', 'E'));
				rsm.setValueToField("VL_TOTAL_RECEITA", new Float(vlReceitaDespesa[0]));
				rsm.setValueToField("VL_TOTAL_DESPESA", new Float(vlReceitaDespesa[1]));
				rsmRetorno.addRegister(rsm.getRegister());
			}
			ArrayList<String> orderBy = new ArrayList<String>();
			orderBy.add("NR_CATEGORIA_ECONOMICA");
			rsmRetorno.orderBy(orderBy);
			rsmRetorno.beforeFirst();
			return rsmRetorno;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCateogriaServices.getMovimentoByCategoria: " + e);
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	private static ResultSetMap getTotalCategoriasFilhas(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal,
			int cdCategoriaEconomica, int cdConta, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		connect = isConnectionNull ? Conexao.conectar() : connect;
		PreparedStatement pstmt;
		try {
			String categorias = "";
			categorias = String.valueOf(cdCategoriaEconomica);
			ArrayList<Integer> cats = getCategoriasInferiores(cdCategoriaEconomica, connect);
			for(int i=0; i<cats.size(); i++)
				categorias += ","+cats.get(i).intValue();
			pstmt = connect.prepareStatement(
					   "SELECT COUNT(*) AS qt_itens, SUM(TRUNC(vl_movimento_categoria*100)/100) AS vl_total_categoria "+
					   "FROM adm_movimento_conta_categoria A "+
					   "JOIN adm_movimento_conta     B ON (A.cd_conta           = B.cd_conta " +
					   "                               AND A.cd_movimento_conta = B.cd_movimento_conta) "+
					   "JOIN adm_conta_financeira    C ON (A.cd_conta           = C.cd_conta ) "+
					   "WHERE B.dt_movimento BETWEEN ? AND ? "+
					   "  AND B.st_movimento IN ("+MovimentoContaServices.ST_CONFERIDO+","+MovimentoContaServices.ST_CONCILIADO+","+MovimentoContaServices.ST_LIQUIDADO+")"+
					   (!categorias.equals("")  ? " AND A.cd_categoria_economica IN ("+categorias+")": "")+
					   (cdConta > 0             ? " AND A.cd_conta = "+cdConta: "")+
					   (cdEmpresa > 0           ? " AND C.cd_empresa = "+cdEmpresa: ""));
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! MovimentoContaCateogriaServices.getTotalCategoriasFilhas: " + e);
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		try{		
			String sql = "SELECT * FROM adm_movimento_conta_categoria WHERE 1 = 1 ";
			ResultSetMap rsm = Search.find(sql, "ORDER BY cd_movimento_conta_categoria", criterios, Conexao.conectar(), true);
			
			rsm.beforeFirst();
			return rsm;
		}catch(Exception e){
			Util.registerLog(e);
			return null;
		}
	}

}