package com.tivic.manager.adm;

import java.sql.*;
import java.util.ArrayList;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.fsc.SituacaoTributariaServices;

import sol.dao.*;

public class TributoServices {
	
	public static final int _ESFERA_MUNICIPAL = 0;
	public static final int _ESFERA_ESTADUAL  = 1;
	public static final int _ESFERA_FEDERAL   = 2;

	public static final int _TRIBUTO_IMPOSTO 	  = 0;
	public static final int _TRIBUTO_CONTRIBUICAO = 1;
	public static final int _TRIBUTO_TAXA   	  = 2;

	public static final int _APLICACAO_VALOR 	  = 0;
	public static final int _APLICACAO_PERCENTUAL = 1;

	public static final int _INCIDENCIA_INDIRETA = 0;
	public static final int _INCIDENCIA_DIRETA   = 1;

	public static final String[] tipoEsfera = {"Municipal","Estadual","Federal"};

	public static final String[] tipoTributo = {"Imposto","Contribuição","Taxa"};

	public static final String[] tipoFator   = {"Valor", "Percentual"};

	public static ResultSetMap init()	{
		ArrayList<Tributo> impostos = new ArrayList<Tributo>();
		/*
		 * IMPOSTOS
		 */
		impostos.add(new Tributo(0 /*cdTributo*/, "II - Imposto sobre a importação",
								 "II", null, null, _TRIBUTO_IMPOSTO, 0 /*prAliquotaPadrao*/,
								 0 /*lgAliquotaProgressiva*/, _ESFERA_FEDERAL, 1 /*nrOrdemCalculo*/,
								 TributoAliquotaServices.OP_IGNORADA, 0 /*vlVariacaoBase*/, 0 /*vlVariacaoResultado*/,
								 0 /*tpFatorVariacaoBase*/, 0 /*tpFatorVariacaoResultado*/, _INCIDENCIA_DIRETA));
		impostos.add(new Tributo(0, "IE - Imposto sobre a exportação",
				"IE", null, null, _TRIBUTO_IMPOSTO, 0, 0, _ESFERA_FEDERAL, 2 /*nrOrdemCalculo*/,
				 TributoAliquotaServices.OP_IGNORADA, 0 /*vlVariacaoBase*/, 0 /*vlVariacaoResultado*/,
				 0 /*tpFatorVariacaoBase*/, 0 /*tpFatorVariacaoResultado*/, _INCIDENCIA_DIRETA));
		impostos.add(new Tributo(0, "IR - Imposto de Renda Pessoa Física",
				 "IRRF/PF", null, null, _TRIBUTO_IMPOSTO, 0, 1, _ESFERA_FEDERAL, 3 /*nrOrdemCalculo*/,
				 TributoAliquotaServices.OP_IGNORADA, 0 /*vlVariacaoBase*/, 0 /*vlVariacaoResultado*/,
				 0 /*tpFatorVariacaoBase*/, 0 /*tpFatorVariacaoResultado*/, _INCIDENCIA_DIRETA));
		impostos.add(new Tributo(0, "IR - Imposto de Renda Pessoa Jurídica",
				 "IRRF/PJ", null, null, _TRIBUTO_IMPOSTO, 0, 1, _ESFERA_FEDERAL, 4 /*nrOrdemCalculo*/,
				 TributoAliquotaServices.OP_IGNORADA, 0 /*vlVariacaoBase*/, 0 /*vlVariacaoResultado*/,
				 0 /*tpFatorVariacaoBase*/, 0 /*tpFatorVariacaoResultado*/, _INCIDENCIA_DIRETA));
		impostos.add(new Tributo(0, "IPI - Imposto s/ Produtos Industrializados",
				 "IPI", null, null, _TRIBUTO_IMPOSTO, 0, 0, _ESFERA_FEDERAL, 5 /*nrOrdemCalculo*/,
				 TributoAliquotaServices.OP_IGNORADA, 0 /*vlVariacaoBase*/, 0 /*vlVariacaoResultado*/,
				 0 /*tpFatorVariacaoBase*/, 0 /*tpFatorVariacaoResultado*/, _INCIDENCIA_INDIRETA));
		impostos.add(new Tributo(0, "IOF - Imposto s/ Op. de Crédito, Câmbio e Seguro",
				 "IOF", null, null, _TRIBUTO_IMPOSTO, 0, 0, _ESFERA_FEDERAL, 6 /*nrOrdemCalculo*/,
				 TributoAliquotaServices.OP_IGNORADA, 0 /*vlVariacaoBase*/, 0 /*vlVariacaoResultado*/,
				 0 /*tpFatorVariacaoBase*/, 0 /*tpFatorVariacaoResultado*/, _INCIDENCIA_DIRETA));
		impostos.add(new Tributo(0, "ITR - Imposto Territorial Rural",
				 "ITR", null, null, _TRIBUTO_IMPOSTO, 0, 1, _ESFERA_FEDERAL, 7 /*nrOrdemCalculo*/,
				 TributoAliquotaServices.OP_IGNORADA, 0 /*vlVariacaoBase*/, 0 /*vlVariacaoResultado*/,
				 0 /*tpFatorVariacaoBase*/, 0 /*tpFatorVariacaoResultado*/, _INCIDENCIA_DIRETA));
		// Estaduais
		impostos.add(new Tributo(0, "ICMS - Imposto s/ Circ. de Mercadorias e Serviços",
				 "ICMS", null, null, _TRIBUTO_IMPOSTO, 0, 0, _ESFERA_ESTADUAL, 8 /*nrOrdemCalculo*/,
				 TributoAliquotaServices.OP_IGNORADA, 0 /*vlVariacaoBase*/, 0 /*vlVariacaoResultado*/,
				 0 /*tpFatorVariacaoBase*/, 0 /*tpFatorVariacaoResultado*/, _INCIDENCIA_DIRETA));
		impostos.add(new Tributo(0, "IPVA - Imposto s/ Prop. de Veículos Automotores",
				 "IPVA", null, null, _TRIBUTO_IMPOSTO, 0, 0, _ESFERA_ESTADUAL, 9 /*nrOrdemCalculo*/,
				 TributoAliquotaServices.OP_IGNORADA, 0 /*vlVariacaoBase*/, 0 /*vlVariacaoResultado*/,
				 0 /*tpFatorVariacaoBase*/, 0 /*tpFatorVariacaoResultado*/, _INCIDENCIA_DIRETA));
		impostos.add(new Tributo(0, "ITCD - Imposto s/ Transmissões Causa Mortis",
				 "ITCD", null, null, _TRIBUTO_IMPOSTO, 0, 0, _ESFERA_ESTADUAL, 10 /*nrOrdemCalculo*/,
				 TributoAliquotaServices.OP_IGNORADA, 0 /*vlVariacaoBase*/, 0 /*vlVariacaoResultado*/,
				 0 /*tpFatorVariacaoBase*/, 0 /*tpFatorVariacaoResultado*/, _INCIDENCIA_DIRETA));
		// Municipais
		impostos.add(new Tributo(0, "IPTU - Imposto s/ a Propr. Predial e Terr. Urbana",
				 "IPTU", null, null, _TRIBUTO_IMPOSTO, 0, 1, _ESFERA_MUNICIPAL, 11 /*nrOrdemCalculo*/,
				 TributoAliquotaServices.OP_IGNORADA, 0 /*vlVariacaoBase*/, 0 /*vlVariacaoResultado*/,
				 0 /*tpFatorVariacaoBase*/, 0 /*tpFatorVariacaoResultado*/, _INCIDENCIA_DIRETA));
		impostos.add(new Tributo(0, "ITBI - Imposto sobre Transmissão Inter Vivos",
				 "ITBI", null, null, _TRIBUTO_IMPOSTO, 0, 0, _ESFERA_MUNICIPAL, 12 /*nrOrdemCalculo*/,
				 TributoAliquotaServices.OP_IGNORADA, 0 /*vlVariacaoBase*/, 0 /*vlVariacaoResultado*/,
				 0 /*tpFatorVariacaoBase*/, 0 /*tpFatorVariacaoResultado*/, _INCIDENCIA_DIRETA));
		impostos.add(new Tributo(0, "ISSQN - Impostos s/ Serviços de Qualquer Natureza",
				 "ISSQN", null, null, _TRIBUTO_IMPOSTO, 0, 0, _ESFERA_MUNICIPAL, 13 /*nrOrdemCalculo*/,
				 TributoAliquotaServices.OP_IGNORADA, 0 /*vlVariacaoBase*/, 0 /*vlVariacaoResultado*/,
				 0 /*tpFatorVariacaoBase*/, 0 /*tpFatorVariacaoResultado*/, _INCIDENCIA_DIRETA));
		/*
		 * CONTRIBUIÇÕES
		 */
		impostos.add(new Tributo(0 /*cdTributo*/, "COFINS - Contrib. p/ Fin. da Seg. Social",
				 "COFINS", null, null, _TRIBUTO_CONTRIBUICAO, 0 /*prAliquotaPadrao*/,
				 0 /*lgAliquotaProgressiva*/, _ESFERA_FEDERAL, 1 /*nrOrdemCalculo*/,
				 TributoAliquotaServices.OP_IGNORADA, 0 /*vlVariacaoBase*/, 0 /*vlVariacaoResultado*/,
				 0 /*tpFatorVariacaoBase*/, 0 /*tpFatorVariacaoResultado*/, _INCIDENCIA_DIRETA));
		impostos.add(new Tributo(0 /*cdTributo*/, "CSLL - Contribuição Social sobre o Lucro Líquido",
				 "CSLL", null, null, _TRIBUTO_CONTRIBUICAO, 0 /*prAliquotaPadrao*/,
				 0 /*lgAliquotaProgressiva*/, _ESFERA_FEDERAL, 1 /*nrOrdemCalculo*/,
				 TributoAliquotaServices.OP_IGNORADA, 0 /*vlVariacaoBase*/, 0 /*vlVariacaoResultado*/,
				 0 /*tpFatorVariacaoBase*/, 0 /*tpFatorVariacaoResultado*/, _INCIDENCIA_DIRETA));
		impostos.add(new Tributo(0 /*cdTributo*/, "PIS - Programa de Integração Social",
				 "PIS", null, null, _TRIBUTO_CONTRIBUICAO, 0 /*prAliquotaPadrao*/,
				 0 /*lgAliquotaProgressiva*/, _ESFERA_FEDERAL, 1 /*nrOrdemCalculo*/,
				 TributoAliquotaServices.OP_IGNORADA, 0 /*vlVariacaoBase*/, 0 /*vlVariacaoResultado*/,
				 0 /*tpFatorVariacaoBase*/, 0 /*tpFatorVariacaoResultado*/, _INCIDENCIA_DIRETA));
		impostos.add(new Tributo(0 /*cdTributo*/, "INSS - Instituto Nacional de Seguridade Social",
				 "INSS", null, null, _TRIBUTO_CONTRIBUICAO, 0 /*prAliquotaPadrao*/,
				 0 /*lgAliquotaProgressiva*/, _ESFERA_FEDERAL, 1 /*nrOrdemCalculo*/,
				 TributoAliquotaServices.OP_IGNORADA, 0 /*vlVariacaoBase*/, 0 /*vlVariacaoResultado*/,
				 0 /*tpFatorVariacaoBase*/, 0 /*tpFatorVariacaoResultado*/, _INCIDENCIA_DIRETA));
		impostos.add(new Tributo(0 /*cdTributo*/, "FGTS - Fundo de Garantia por Tempo de Serviço",
				 "FGTS", null, null, _TRIBUTO_CONTRIBUICAO, 0 /*prAliquotaPadrao*/,
				 0 /*lgAliquotaProgressiva*/, _ESFERA_FEDERAL, 1 /*nrOrdemCalculo*/,
				 TributoAliquotaServices.OP_IGNORADA, 0 /*vlVariacaoBase*/, 0 /*vlVariacaoResultado*/,
				 0 /*tpFatorVariacaoBase*/, 0 /*tpFatorVariacaoResultado*/, _INCIDENCIA_DIRETA));
		//
		Connection connect = Conexao.conectar();
		try	{
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM adm_tributo WHERE id_tributo = ?");
			for(int i=0; i<impostos.size(); i++)	{
				pstmt.setString(1, impostos.get(i).getIdTributo());
				if(!pstmt.executeQuery().next())
					TributoDAO.insert(impostos.get(i), connect);
			}
			// Ordenando
			ResultSet rs = connect.prepareStatement("SELECT MAX(nr_ordem_calculo) FROM adm_tributo WHERE nr_ordem_calculo IS NOT NULL ").executeQuery();
			int nrOrdemCalculo = rs.next() ? rs.getInt(1)+1 : 1;
			rs = connect.prepareStatement("SELECT * FROM adm_tributo " +
					                      "WHERE nr_ordem_calculo IS NULL " +
					                      "ORDER BY tp_esfera_aplicacao DESC ").executeQuery();
			while(rs.next())	{
				Tributo tributo = TributoDAO.get(rs.getInt("cd_tributo"), connect);
				tributo.setNrOrdemCalculo(nrOrdemCalculo);
				TributoDAO.update(tributo);
				nrOrdemCalculo++;
			}
			System.out.println("Inicialização de Tributos concluida!");
			SituacaoTributariaServices.init();
			return findCompleto(new ArrayList<ItemComparator>());
		}
		catch(Exception e){
			return null;
		}
		finally	{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllTributoBaseCalculo(int cdTributo) {
		return getAllTributoBaseCalculo(cdTributo, null);
	}

	public static ResultSetMap getAllTributoBaseCalculo(int cdTributo, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			return new ResultSetMap(connect.prepareStatement("SELECT A.*, B.nm_tributo, B.id_tributo " +
					                                         "FROM adm_tributo_base_calculo A, adm_tributo B " +
					                                         "WHERE A.cd_tributo = "+cdTributo+
					                                         "  AND A.cd_tributo_base = B.cd_tributo ").executeQuery());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! TributoDAO.getAllTributoBaseCalculo: " +  e);
			return null;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static int insertTributoBaseCalculo(int cdTributo, int cdTributoBase, float prAplicacao) {
		return insertTributoBaseCalculo(cdTributo, cdTributoBase, prAplicacao, null);
	}

	public static int insertTributoBaseCalculo(int cdTributo, int cdTributoBase, float prAplicacao, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ResultSet rs = connect.prepareStatement("SELECT * FROM adm_tributo_base_calculo " +
					                                "WHERE cd_tributo = "+cdTributo+
					                                "  AND cd_tributo_base = "+cdTributoBase).executeQuery();
			if(rs.next())
				return -1;
			return connect.prepareStatement("INSERT INTO adm_tributo_base_calculo (cd_tributo, cd_tributo_base, pr_aplicacao) " +
                                            " VALUES ("+cdTributo+","+cdTributoBase+","+prAplicacao+")").executeUpdate();
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

	  /* adiciona/remove determinado tributo na base de calculo de outros tributos */
	  public static int updateTributoBaseCalculo(int cdTributo, ArrayList<Integer> tributosBaseToAdd, ArrayList<Integer> tributosBaseToRemove){
		  Connection connect = Conexao.conectar();
		  try {
			  connect.setAutoCommit(false);

			  for(int i=0; i<tributosBaseToAdd.size(); i++)
				  insertTributoBaseCalculo(cdTributo, tributosBaseToAdd.get(i).intValue(), 0, connect);

			  for(int i=0; i<tributosBaseToRemove.size(); i++)
				  deleteTributoBaseCalculo(cdTributo, tributosBaseToRemove.get(i).intValue(), connect);

			  connect.commit();
			  return 1;
		  }
		  catch(Exception e){
			  e.printStackTrace(System.out);
			  System.err.println("Erro! TributoServices.updateTributoBaseCalculo: " +  e);
			  Conexao.rollback(connect);
			  return -1;
		  }
		  finally{
			  Conexao.desconectar(connect);
		  }
	  }

	public static int deleteTributoBaseCalculo(int cdTributo, int cdTributoBase) {
		return deleteTributoBaseCalculo(cdTributo, cdTributoBase, null);
	}

	public static int deleteTributoBaseCalculo(int cdTributo, int cdTributoBase, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			return connect.prepareStatement("DELETE FROM adm_tributo_base_calculo " +
					                        "WHERE cd_tributo = "+cdTributo+
					                        "  AND cd_tributo_base = "+cdTributoBase).executeUpdate();
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

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios) {
		return findCompleto(criterios, null);
	}

	public static ResultSetMap findCompleto(ArrayList<ItemComparator> criterios, Connection connection) {
		return Search.find("SELECT * FROM adm_tributo ", "ORDER BY nr_ordem_calculo", criterios, connection!=null ? connection : Conexao.conectar(), connection==null);
	}

	public static ResultSetMap getAll() {
		return findCompleto(new ArrayList<ItemComparator>());
	}

	public static ResultSetMap getAllAliquotas(int cdTributo) {
		return getAllAliquotas(cdTributo, null);
	}
	
	public static ResultSetMap getAllAliquotas(int cdTributo, int tpOperacao) {
		return getAllAliquotas(cdTributo, tpOperacao, null);
	}

	public static ResultSetMap getAllAliquotas(int cdTributo, Connection connection) {
		return getAllAliquotas(cdTributo, 0, connection);
	}
	
	public static ResultSetMap getAllAliquotas(int cdTributo, int tpOperacao, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT A.*, B.* FROM adm_tributo_aliquota A " +
					"LEFT OUTER JOIN fsc_situacao_tributaria B ON (A.cd_situacao_tributaria = B.cd_situacao_tributaria" +
					"                                          AND A.cd_tributo             = B.cd_tributo)" +
					"WHERE A.cd_tributo = " + cdTributo +
					(tpOperacao > 0 ? " AND tp_operacao = " + tpOperacao : "") +
					"ORDER BY tp_operacao, pr_aliquota");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static ResultSetMap getAllSituacoes(int cdTributo) {
		return getAllSituacoes(cdTributo, null);
	}

	public static ResultSetMap getAllSituacoes(int cdTributo, Connection connection) {
		boolean isConnectionNull = connection==null;
		try {
			if (isConnectionNull)
				connection = Conexao.conectar();
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT * FROM fsc_situacao_tributaria A " +
					"WHERE A.cd_tributo = " + cdTributo +
					"ORDER BY lg_simples, nr_situacao_tributaria");
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
	}

	public static int delete(int cdTributo) {
		return delete(cdTributo, null);
	}

	public static int delete(int cdTributo, Connection connect){
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			connect.prepareStatement("DELETE FROM adm_produto_servico_tributo WHERE cd_tributo = "+cdTributo).executeUpdate();

			connect.prepareStatement("DELETE FROM adm_tributo_aliquota WHERE cd_tributo = "+cdTributo).executeUpdate();

			connect.prepareStatement("DELETE FROM adm_tributo_empresa WHERE cd_tributo = "+cdTributo).executeUpdate();

			int ret = TributoDAO.delete(cdTributo, connect);

			if(ret <= 0)	{
				if(isConnectionNull)
					Conexao.rollback(connect);
			}
			else if (isConnectionNull)
				connect.commit();

			return ret;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return -1;
		}
		finally{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	/**
     * Busca os tributos a ser calculado numa determinada operação de venda levando em consideração a
     * o destino e o produto/serviço ou grupo do qual faça parte
     * @param item Item (produto/serviço) da Saída
     * @return Impostos e alíquotas que incidem sobre a operação de venda
     */
	public static ResultSetMap calcTributos(int tpOperacao, int cdNaturezaOperacao, int cdClassificacaoFiscal, int cdProdutoServico,
			int cdPais, int cdEstado, int cdCidade, float vlBaseCalculo, Connection connect)
	{
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			/*
			 *  Pesquisa alíquotas específicas para o produto
			 */
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(
					"SELECT A.*, B.* " +
					"FROM adm_tributo A, adm_tributo_aliquota B " +
					"WHERE A.cd_tributo  = B.cd_tributo " +
					"  AND B.tp_operacao = "+tpOperacao+
					"  AND EXISTS (SELECT * FROM adm_produto_servico_tributo C " +
					"              WHERE A.cd_tributo  = C.cd_tributo " +
					"                AND B.cd_tributo_aliquota = C.cd_tributo_aliquota " +
					"                " + (cdNaturezaOperacao > 0 ? "AND C.cd_natureza_operacao = "+cdNaturezaOperacao : "") +
					"                AND (C.cd_pais   = "+cdPais+" OR C.cd_pais IS NULL) "+
					"                AND (C.cd_estado = "+cdEstado+" OR C.cd_estado IS NULL) "+
					"                AND (C.cd_cidade = "+cdCidade+" OR C.cd_cidade IS NULL) "+
					"                AND C.cd_produto_servico = "+cdProdutoServico+")").executeQuery());
			/*
			 *  Pesquisa alíquotas configuradas para a classificação fiscal
			 */
			PreparedStatement pstmt = connect.prepareStatement(
					"SELECT A.*, B.* " +
					"FROM adm_tributo A, adm_tributo_aliquota B " +
					"WHERE A.cd_tributo  = B.cd_tributo " +
					"  AND B.tp_operacao = "+tpOperacao+
					"  AND EXISTS (SELECT * FROM adm_produto_servico_tributo C " +
					"              WHERE A.cd_tributo  = C.cd_tributo " +
					"                AND B.cd_tributo_aliquota = C.cd_tributo_aliquota " +
					"                " + (cdNaturezaOperacao > 0 ? "AND C.cd_natureza_operacao = "+cdNaturezaOperacao : "") +
					"                AND (C.cd_pais   = "+cdPais+" OR C.cd_pais   IS NULL) "+
					"                AND (C.cd_estado = "+cdEstado+" OR C.cd_estado IS NULL) "+
					"                AND (C.cd_cidade = "+cdCidade+" OR C.cd_cidade IS NULL) "+
					"                AND C.cd_classificacao_fiscal = "+cdClassificacaoFiscal+")");
			ResultSetMap rsm2 = new ResultSetMap(pstmt.executeQuery());
			while(rsm2.next())	{
				if(!rsm.locate("cd_tributo", rsm2.getInt("cd_tributo"))){
					rsm.addRegister(rsm2.getRegister());
				}
			}
			/*
			 *  Calculando alíquotas
			 */
			float vlBaseCalculoOriginal = vlBaseCalculo;
			ArrayList<String> orderBy = new ArrayList<String>();
			orderBy.add("nr_ordem_calculo");
			rsm.orderBy(orderBy);
			rsm.beforeFirst();
			while(rsm.next())	{
				vlBaseCalculo = vlBaseCalculoOriginal;
				/*
				 *  Verificando impostos que compõem a base de cálculo
				 */
				rsm2 = new ResultSetMap(connect.prepareStatement("SELECT * FROM adm_tributo_base_calculo " +
						                                         "WHERE cd_tributo = "+rsm.getInt("cd_tributo")).executeQuery());
				int pos = rsm.getPosition();
				while(rsm2.next()){ 
					if(rsm.locate("cd_tributo", rsm2.getInt("cd_tributo_base")))
						vlBaseCalculo += rsm.getFloat("vl_tributo");
				}
				rsm.goTo(pos);
				/*
				 * Alterando base de cálculo
				 */
				if(rsm.getFloat("vl_variacao_base")>0)	{
					if(rsm.getInt("tp_fator_variacao_base") == TributoAliquotaServices.FT_VALOR)
						vlBaseCalculo -= rsm.getFloat("vl_variacao_base");
					else
						vlBaseCalculo -= (rsm.getFloat("vl_variacao_base") * vlBaseCalculo / 100);
				}
				/*
				 * Realizando cálculo
				 */
				rsm.setValueToField("vl_base_calculo", new Float(vlBaseCalculo));
				float vlTributo = vlBaseCalculo * rsm.getFloat("pr_aliquota") / 100;
				/*
				 * Alterando resultado obtido
				 */
				if(rsm.getFloat("vl_variacao_resultado")>0)	{
					if(rsm.getInt("tp_fator_variacao_resultado")==TributoAliquotaServices.FT_VALOR)
						vlTributo -= rsm.getFloat("vl_variacao_resultado");
					else
						vlTributo -= (rsm.getFloat("vl_variacao_resultado") * vlBaseCalculo / 100);
				}
				rsm.setValueToField("vl_tributo", new Float(vlTributo));
			}
			//
			rsm.beforeFirst();
			return rsm;
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
			if (isConnectionNull)
				Conexao.rollback(connect);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static int getCdTributoById(String idTributo, Connection connect)	{
		boolean isConnNull = connect==null;
		try	{
			connect = isConnNull ? Conexao.conectar() : connect;
			
			ResultSet rs = connect.prepareStatement("SELECT * FROM adm_tributo WHERE id_tributo = \'"+idTributo+"\'").executeQuery();
			if(rs.next())
				return rs.getInt("cd_tributo");
			
			return 0;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return -1;
		}
		finally	{
			if(isConnNull)
				Conexao.desconectar(connect);
		}
	}
}
