package com.tivic.manager.fsc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import sol.dao.ResultSetMap;

import com.tivic.manager.adm.TributoServices;
import com.tivic.sol.connection.Conexao;

public class SituacaoTributariaServices {
	/*Tp Situacao Tributaria ECF*/
	public static final int ST_IGNORADA                = 0;
	public static final int ST_TRIBUTACAO_INTEGRAL 	   = 1;
	public static final int ST_SUBSTITUICAO_TRIBUTARIA = 2;
	public static final int ST_ISENTO 				   = 3;
	public static final int ST_SEM_TRIBUTACAO 		   = 4;
	
	public static final int TP_OPERACAO_COMPRAS        = 1;
	public static final int TP_OPERACAO_VENDAS         = 2;
	
	public static final String[] tipoSituacaoTributaria = {"Ignorada", "Tributado Integralmente", "Substituição Tributária", "Isento", "Sem Tributação"};
	public static final String[] tipoOperacao			= {"Ignorada", "Compras", "Vendas"};
	
	/*Lg Simples*/
	public static final int REG_NORMAL  = 0;
	public static final int REG_SIMPLES = 1;
	public static final int REG_AMBOS   = 2;
	
	public static void init()	{
		//
		
		Connection connect = Conexao.conectar();
		try	{
			ArrayList<SituacaoTributaria> csts = new ArrayList<SituacaoTributaria>();
			PreparedStatement pstmtCST     = connect.prepareStatement("SELECT * FROM fsc_situacao_tributaria WHERE cd_tributo = ? AND nr_situacao_tributaria = ?");
			/*
			 *  ICMS
			 */
			int cdTributo = TributoServices.getCdTributoById("ICMS", connect);
			// Tributação Normal
			csts.add(new SituacaoTributaria(cdTributo, 0, "00", "Tributada integralmente", null /*Sigla*/, ST_TRIBUTACAO_INTEGRAL, REG_NORMAL , 0 /*lgSubstituicao*/,  1/*lgGeraCredito*/, 0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "10", "Tributada e com cobrança do ICMS por substituição tributária", null /*Sigla*/, ST_SUBSTITUICAO_TRIBUTARIA, REG_NORMAL , 1 /*lgSubstituicao*/, 0 /*lgGeraCredito*/, 0 /*lgPartilha*/, 0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "20", "Com redução de base de cálculo", null /*Sigla*/, ST_TRIBUTACAO_INTEGRAL, REG_NORMAL ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 1 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "30", "Isenta ou não tributada e com cobrança do ICMS por substituição tributária", null /*Sigla*/, ST_SUBSTITUICAO_TRIBUTARIA, REG_NORMAL ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,1 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "40", "Isenta", null /*Sigla*/, ST_ISENTO, REG_NORMAL ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,1 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "41", "Não Tributada", null /*Sigla*/, ST_SEM_TRIBUTACAO, REG_NORMAL ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,1 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "50", "Suspensão", null /*Sigla*/, ST_SEM_TRIBUTACAO, REG_NORMAL ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,1 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "51", "Diferimento", null /*Sigla*/, ST_SEM_TRIBUTACAO, REG_NORMAL ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "60", "ICMS cobrado anteriormente por substituição tributária", null /*Sigla*/, ST_SUBSTITUICAO_TRIBUTARIA, REG_NORMAL ,1 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 1 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "70", "Com redução de base de cálculo e cobrança do ICMS por substituição tributária", null /*Sigla*/, ST_SUBSTITUICAO_TRIBUTARIA, REG_NORMAL ,1 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 1 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "90", "Outras", null /*Sigla*/, ST_IGNORADA, REG_NORMAL ,1 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			// Simples (CSOSN)
			csts.add(new SituacaoTributaria(cdTributo, 0, "101", "Tributada pelo Simples Nacional com permissão de crédito", null /*Sigla*/, ST_TRIBUTACAO_INTEGRAL, REG_SIMPLES ,0 /*lgSubstituicao*/,1 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "102", "Tributada pelo Simples Nacional sem permissão de crédito", null /*Sigla*/, ST_SUBSTITUICAO_TRIBUTARIA, REG_SIMPLES ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "103", "Isenção do ICMS no Simples Nacional para faixa de receita bruta", null /*Sigla*/, ST_TRIBUTACAO_INTEGRAL, REG_SIMPLES ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "201", "Tributada pelo Simples Nacional com permissão de crédito e com cobrança do ICMS por ST", null /*Sigla*/, ST_SUBSTITUICAO_TRIBUTARIA, REG_SIMPLES ,1 /*lgSubstituicao*/,1 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "202", "Tributada pelo Simples Nacional sem permissão de crédito e com cobrança do ICMS por ST", null /*Sigla*/, ST_ISENTO, REG_SIMPLES ,1 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "203", "Isenção do ICMS no Simples Nacional para faixa de receita bruta e com cobrança do ICMS por ST", null /*Sigla*/, ST_SEM_TRIBUTACAO, REG_SIMPLES ,1 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,1 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "300", "Imune", null /*Sigla*/, ST_SEM_TRIBUTACAO, REG_SIMPLES ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "400", "Não tributada pelo Simples Nacional", null /*Sigla*/, ST_SEM_TRIBUTACAO, REG_SIMPLES ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "500", "ICMS cobrado anteriormente por ST (substituído) ou por antecipação", null /*Sigla*/, ST_SUBSTITUICAO_TRIBUTARIA, REG_SIMPLES ,1 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 1 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "900", "Outros", null /*Sigla*/, ST_IGNORADA, REG_SIMPLES ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			/*
			 *  IPI
			 */
			cdTributo = TributoServices.getCdTributoById("IPI", connect);
			csts.add(new SituacaoTributaria(cdTributo, 0, "00", "Entrada com Recuperação de Crédito", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,1 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "01", "Entrada Tributável com Alíquota Zero", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "02", "Entrada Isenta", null, ST_IGNORADA, REG_AMBOS , 0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "03", "Entrada Não-Tributada", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,1 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "04", "Entrada Imune", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "05", "Entrada com Suspensão", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "49", "Outras Entradas", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "50", "Saída Tributada", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "51", "Saída Tributável com Alíquota Zero", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "52", "Saida Isenta", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "53", "Saída Não-Tributada", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "54", "Saída Imune", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "55", "Saída com Suspensão", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "99", "Outras Saídas", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));			
			/*
			 *  PIS
			 */
			cdTributo = TributoServices.getCdTributoById("PIS", connect);
			csts.add(new SituacaoTributaria(cdTributo, 0, "01", "Operação Tributável com Alíquota Básica", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "02", "Operação Tributável com Alíquota Diferenciada", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "03", "Operação Tributável com Alíquota por Unidade de Medida de Produto", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "04", "Operação Tributável Monofásica - Revenda a Alíquota Zero", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "05", "Operação Tributável por Substituição Tributária", null, ST_IGNORADA, REG_AMBOS ,1 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "06", "Operação Tributável a Alíquota Zero", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "07", "Operação Isenta da Contribuição", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "08", "Operação sem Incidência da Contribuição", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "09", "Operação com Suspensão da Contribuição", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "49", "Outras Operações de Saída", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "50", "Op. com Direito a Crédito - Vinculada Exclusivamente a Receita Tributada no Mercado Interno", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,1 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "51", "Op. com Direito a Crédito - Vinculada Exclusivamente a Receita Não Tributada no Mercado Interno", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,1 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "52", "Op. com Direito a Crédito - Vinculada Exclusivamente a Receita de Exportação", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,1 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "53", "Op. com Direito a Crédito - Vinculada a Receitas Tributadas e Não-Tributadas no Mercado Interno", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,1 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "54", "Op. com Direito a Crédito - Vinculada a Receitas Tributadas no Mercado Interno e de Exportação", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,1 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "55", "Op. com Direito a Crédito - Vinculada a Receitas Não-Tributadas no Mercado Interno e de Exportação", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,1 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "56", "Op. com Direito a Crédito - Vinculada a Rec. Trib. e Não-Tributadas no Mercado Interno, e de Exp.", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,1 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "60", "Créd. Presumido - Op. de Aquisição Vinc. Exc. a Receita Tributada no Mercado Interno", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "61", "Créd. Presumido - Op. de Aquisição Vinc. Exc. a Receita Não-Tributada no Mercado Interno", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "62", "Créd. Presumido - Op. de Aquisição Vinc. Exc. a Receita de Exportação", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "63", "Créd. Presumido - Op. de Aquisição Vinc. a Rec. Tributadas e Não-Tributadas no Mercado Interno", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "64", "Créd. Presumido - Op. de Aquisição Vinc. a Rec. Tributadas no Mercado Interno e de Exportação", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "65", "Créd. Presumido - Op. de Aquisição Vinc. a Rec. Não-Tributadas no Mercado Interno e de Exportação", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "66", "Créd. Presumido - Op. de Aquisição Vinc. a Rec. Trib. e Não-Tributadas no Merc. Interno, e de Exp", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "67", "Créd. Presumido - Outras Operações", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "70", "Operação de Aquisição sem Direito a Crédito", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "71", "Operação de Aquisição com Isenção", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "72", "Operação de Aquisição com Suspensão", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "73", "Operação de Aquisição a Alíquota Zero", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "74", "Operação de Aquisição sem Incidência da Contribuição", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "75", "Operação de Aquisição por Substituição Tributária", null, ST_IGNORADA, REG_AMBOS ,1 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "98", "Outras Operações de Entrada", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "99", "Outras Operações", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));			
			/*
			 *  COFINS
			 */
			cdTributo = TributoServices.getCdTributoById("COFINS", connect);
			csts.add(new SituacaoTributaria(cdTributo, 0, "01", "Operação Tributável com Alíquota Básica", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "02", "Operação Tributável com Alíquota Diferenciada", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "03", "Operação Tributável com Alíquota por Unidade de Medida de Produto", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "04", "Operação Tributável Monofásica - Revenda a Alíquota Zero", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "05", "Operação Tributável por Substituição Tributária", null, ST_IGNORADA, REG_AMBOS ,1 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "06", "Operação Tributável a Alíquota Zero", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "07", "Operação Isenta da Contribuição", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,1 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "08", "Operação sem Incidência da Contribuição", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "09", "Operação com Suspensão da Contribuição", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "49", "Outras Operações de Saída", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "50", "Op. com Direito a Crédito - Vinculada Exclusivamente a Receita Tributada no Mercado Interno", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,1 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "51", "Op. com Direito a Crédito - Vinculada Exclusivamente a Receita Não-Tributada no Mercado Interno", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,1 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "52", "Op. com Direito a Crédito - Vinculada Exclusivamente a Receita de Exportação", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,1 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "53", "Op. com Direito a Crédito - Vinculada a Rec. Tributadas e Não-Tributadas no Mercado Interno", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,1 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "54", "Op. com Direito a Crédito - Vinculada a Rec. Tributadas no Mercado Interno e de Exportação", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,1 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "55", "Op. com Direito a Crédito - Vinculada a Rec. Não Tributadas no Mercado Interno e de Exportação", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,1 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "56", "Op. com Direito a Créd. - Vinculada a Rec. Trib. e Não-Tributadas no Mercado Interno e de Exp.", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,1 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "60", "Créd. Presumido - Op. de Aquisição Vinc. Exc. a Receita Tributada no Mercado Interno", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "61", "Créd. Presumido - Op. de Aquisição Vinc. Exc. a Receita Não-Tributada no Mercado Interno", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "62", "Créd. Presumido - Op. de Aquisição Vinc. Exc. a Receita de Exportação", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "63", "Créd. Presumido - Op. de Aquisição Vinc. a Rec. Tributadas e Não-Tributadas no Mercado Interno", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "64", "Créd. Presumido - Op. de Aquisição Vinc. a Rec. Tributadas no Mercado Interno e de Exportação", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "65", "Créd. Presumido - Op. de Aquisição Vinc. a Rec. Não-Tributadas no Mercado Interno e de Exportação", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "66", "Créd. Presumido - Op. de Aquisição Vinc. a Rec. Trib. e Não-Trib. no Mercado Interno, e de Exp", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "67", "Crédito Presumido - Outras Operações", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "70", "Operação de Aquisição sem Direito a Crédito", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "71", "Operação de Aquisição com Isenção", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "72", "Operação de Aquisição com Suspensão", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "73", "Operação de Aquisição a Alíquota Zero", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "74", "Operação de Aquisição sem Incidência da Contribuição", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "75", "Operação de Aquisição por Substituição Tributária", null, ST_IGNORADA, REG_AMBOS, 1 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "98", "Outras Operações de Entrada", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			csts.add(new SituacaoTributaria(cdTributo, 0, "99", "Outras Operações", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));			
			/*
			 *  II
			 */
			cdTributo = TributoServices.getCdTributoById("II", connect);
			csts.add(new SituacaoTributaria(cdTributo, 0, "00", "Imposto de Importação", null, ST_IGNORADA, REG_AMBOS ,0 /*lgSubstituicao*/,0 /*lgGeraCredito*/,0 /*lgPartilha*/,0 /*lgMotivoIsencao*/, 0 /*lgReducaoBase*/, 0 /*lgRetido*/));
			/*
			 *  INCLUINDO
			 */
			for(int i=0; i<csts.size(); i++)	{
				pstmtCST.setInt(1, csts.get(i).getCdTributo());
				pstmtCST.setString(2, csts.get(i).getNrSituacaoTributaria());
				ResultSet rs = pstmtCST.executeQuery();
				if(!rs.next())
					SituacaoTributariaDAO.insert(csts.get(i), connect);
				else {
					csts.get(i).setCdSituacaoTributaria(rs.getInt("cd_situacao_tributaria"));
					SituacaoTributariaDAO.update(csts.get(i), connect);
				}
			}
			System.out.println("Inicialização de Situações Tributárias concluida!");
			return ;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
	
	
	
	public static SituacaoTributaria getByCst(String nrCst, int cdTributo){
		Connection connect = Conexao.conectar();
		try	{
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM fsc_situacao_tributaria WHERE nr_situacao_tributaria = '"+nrCst+" AND cd_tributo =  " + cdTributo).executeQuery());
			SituacaoTributaria situacaoTrib = null;
			while(rsm.next()){
				situacaoTrib = SituacaoTributariaDAO.get(cdTributo, rsm.getInt("cd_situacao_tributaria"));
			}
			
			return situacaoTrib;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}
}
