package com.tivic.manager.mcr;

import java.sql.*;
import java.util.*;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.*;
import com.tivic.manager.util.Util;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;

public class ContratoServices extends com.tivic.manager.adm.ContratoServices {
	public static final String[] tipoEmprestimo = {"Capital de giro", "Capital fixo",
		                                           "Capital misto",   "Capital de giro - limite de crédito"};

	public static final String[] tipoGarantia = {"Sem garantia",
		 "Aval",
		 "Alienação fiduciária",
		 "Grupo solidário"};

	public static int insert(Contrato objeto) {
		return insert(objeto, null);
	}

	public static int insert(Contrato objeto, Connection connect) {
		if(objeto.getTpAmortizacao()==com.tivic.manager.adm.ContratoServices.PRICE)	{
			int nrDiasCarencia = Math.round((objeto.getDtAssinatura().getTimeInMillis() - objeto.getDtPrimeiraParcela().getTimeInMillis()) / (365*24*60*60*1000));
			float vlParcela = com.tivic.manager.adm.ContratoServices.getValorParcela(nrDiasCarencia, objeto.getNrParcelas(), objeto.getPrJuros(), objeto.getVlContrato());
			objeto.setVlParcelas(vlParcela);
		}
		return ContratoDAO.insert(objeto, connect);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios) {
		return find(criterios, null);
	}

	public static ResultSetMap find(ArrayList<ItemComparator> criterios, Connection connect) {
 		return Search.find("SELECT A.*, " +
						   "	   O.nm_razao_social AS nm_empresa, P.nm_pessoa AS nm_fantasia, " +
						   "	   C.nm_pessoa, " +
				           "       D.nm_pessoa AS nm_agente, " +
				           "	   E.nm_indicador, " +
				           " 	   F.nm_categoria_economica AS nm_categoria_parcelas, F.nr_categoria_economica AS nr_categoria_parcelas," +
				           " 	   S.nm_categoria_economica AS nm_categoria_adesao, S.nr_categoria_economica AS nr_categoria_adesao," +
				           "       G.sg_carteira, G.nm_carteira, " +
				           "	   H.nm_conta, H.nr_conta, H.nr_dv, " +
				           "       I.nr_agencia, " +
				           "       J.nm_banco, " +
				           "	   K.*, " +
						   "       L.nm_grupo_solidario, " +
						   "       M.cd_contrato AS cd_convenio, M.nr_contrato AS nr_convenio, M.id_contrato AS id_convenio, " +
						   "	   R.nm_pessoa AS nm_conveniado, " +
						   "	   N.nr_parcelas AS nr_parcelas_modelo, N.vl_adesao AS vl_adesao_modelo, " +
						   "	   N.pr_juros_mora AS pr_juros_mora_modelo, N.pr_multa_mora AS pr_multa_mora_modelo, " +
						   "	   N.pr_desconto AS pr_desconto_modelo, N.cd_indicador AS cd_indicador_modelo, " +
						   "	   Q.nm_modelo AS nm_modelo_contrato, Q.txt_conteudo AS txt_contrato_modelo, " +
						   "	   A.cd_contrato AS cd_contrato " +
				           "FROM adm_contrato A " +
				           "JOIN mcr_contrato K ON (A.cd_contrato = K.cd_contrato) " +
				           "JOIN grl_empresa B ON (A.cd_empresa = B.cd_empresa) " +
				           "JOIN grl_pessoa  C ON (A.cd_pessoa  = C.cd_pessoa) " +
				           "JOIN grl_pessoa_juridica O ON (B.cd_empresa  = O.cd_pessoa) " +
				           "JOIN grl_pessoa P ON (O.cd_pessoa  = P.cd_pessoa) " +
				           "LEFT OUTER JOIN grl_pessoa D ON (A.cd_agente = D.cd_pessoa) " +
				           "LEFT OUTER JOIN grl_indicador E ON (A.cd_indicador = E.cd_indicador) " +
				           "LEFT OUTER JOIN adm_categoria_economica F ON (A.cd_categoria_parcelas = F.cd_categoria_economica) " +
				           "LEFT OUTER JOIN adm_categoria_economica S ON (A.cd_categoria_adesao = S.cd_categoria_economica) " +
				           "LEFT OUTER JOIN adm_conta_carteira G ON (A.cd_conta = G.cd_conta " +
				           "	AND A.cd_conta_carteira = G.cd_conta_carteira) " +
				           "LEFT OUTER JOIN adm_conta_financeira   H ON (A.cd_conta = H.cd_conta) " +
				           "LEFT OUTER JOIN grl_agencia I ON (I.cd_agencia = H.cd_agencia) " +
				           "LEFT OUTER JOIN grl_banco   J ON (J.cd_banco = I.cd_banco) " +
				           "LEFT OUTER JOIN mcr_grupo_solidario L ON (L.cd_grupo_solidario = K.cd_grupo_solidario) " +
				           "LEFT OUTER JOIN adm_contrato M ON (M.cd_contrato = A.cd_convenio) " +
				           "LEFT OUTER JOIN grl_pessoa R ON (R.cd_pessoa = M.cd_pessoa) " +
				           "LEFT OUTER JOIN adm_modelo_contrato N ON (N.cd_modelo_contrato = A.cd_modelo_contrato) " +
				           "LEFT OUTER JOIN grl_modelo_documento Q ON (Q.cd_modelo = N.cd_modelo_contrato) ",
				           criterios, true, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap getAllAtas(int cdContrato) {
		return getAllAtas(cdContrato, null);
	}

	public static ResultSetMap getAllAtas(int cdContrato, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT A.* " +
				                             "FROM mcr_ata_comite A " +
											 "WHERE (A.cd_contrato = ?) ORDER BY A.DT_ATA");
			pstmt.setInt(1, cdContrato);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! mcr.ContratoServices.getAllAtas: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAllAvalistas(int cdContrato) {
		return getAllAvalistas(cdContrato, null);
	}

	public static ResultSetMap getAllAvalistas(int cdContrato, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			String sql =
				  "SELECT A.*, " +
				  "   B.nm_pessoa, B.dt_cadastro, B.id_pessoa, B.nr_telefone1, " +
				  "   C.nr_cpf, C.cd_naturalidade, C.cd_escolaridade, " +
				  "   C.dt_nascimento, C.nr_cpf, C.sg_orgao_rg, C.nm_mae, " +
				  "   C.nm_pai, E.sg_estado AS sg_uf_rg, C.tp_sexo, C.st_estado_civil, " +
				  "   C.nr_rg, C.nr_cnh, C.dt_validade_cnh, C.dt_primeira_habilitacao, " +
				  "   C.tp_categoria_habilitacao, C.tp_raca, C.lg_deficiente_fisico, " +
				  "   C.nm_forma_tratamento, D.nr_cnpj, D.nm_razao_social, D.nr_inscricao_estadual, " +
				  "   D.nr_inscricao_municipal, D.nr_funcionarios, D.dt_inicio_atividade, " +
				  "   D.tp_empresa, D.cd_natureza_juridica " +
				  "FROM ADM_AVALISTA_CONTRATO A, GRL_PESSOA B " +
				  "   LEFT OUTER JOIN GRL_PESSOA_FISICA C ON (B.CD_PESSOA = C.CD_PESSOA) " +
				  "	  LEFT OUTER JOIN grl_estado E ON (C.cd_estado_rg = E.cd_estado) " +
				  "   LEFT OUTER JOIN GRL_PESSOA_JURIDICA D ON (B.CD_PESSOA = D.CD_PESSOA) " +
   				  "WHERE (A.CD_PESSOA = B.CD_PESSOA) " +
   				  "   AND (A.cd_contrato = ?) ";
			PreparedStatement pstmt = connect.prepareStatement(sql);
			pstmt.setInt(1, cdContrato);
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! mcr.ContratoServices.getAllAvalista: " + e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findLiberacaoCredito(ArrayList<ItemComparator> criterios) {
		return findLiberacaoCredito(criterios, null);
	}

	public static ResultSetMap findLiberacaoCredito(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ResultSetMap rsm = Search.find("SELECT DISTINCT A.*, B.*, D.nm_pessoa, E.nm_pessoa AS nm_agente, G.nm_cidade, " +
						   "	H.sg_estado, " +
						   "	I.nm_grupo_solidario, " +
						   "    J.id_contrato AS id_convenio, " +
						   "    K.nm_pessoa AS nm_convenio, " +
						   "	(A.vl_contrato/A.nr_parcelas) AS vl_historico, " +
						   "	O.nr_cheque " +
						   "FROM adm_contrato A " +
						   "	JOIN mcr_contrato B ON (A.cd_contrato = B.cd_contrato) " +
						   "	JOIN grl_empresa C ON (A.cd_empresa = C.cd_empresa) " +
						   "	JOIN grl_pessoa  D ON (A.cd_pessoa  = D.cd_pessoa) " +
						   "	LEFT OUTER JOIN grl_pessoa E ON (A.cd_agente = E.cd_pessoa) " +
				           "	LEFT OUTER JOIN grl_pessoa_endereco F ON (D.cd_pessoa = F.cd_pessoa AND F.lg_principal = 1) " +
				           "	LEFT OUTER JOIN grl_cidade G ON (F.cd_cidade = G.cd_cidade) " +
				           "	LEFT OUTER JOIN grl_estado H ON (G.cd_estado = H.cd_estado) " +
						   "	LEFT OUTER JOIN mcr_grupo_solidario I ON (B.cd_grupo_solidario = I.cd_grupo_solidario) " +
				           "	LEFT OUTER JOIN adm_contrato J ON (A.cd_contrato = J.cd_convenio) " +
				           "	LEFT OUTER JOIN grl_pessoa K ON (J.cd_pessoa = K.cd_pessoa) " +
				           "	LEFT OUTER JOIN adm_conta_pagar L ON (L.cd_contrato = A.cd_contrato) " +
				           "	LEFT OUTER JOIN adm_movimento_conta_pagar M ON (M.cd_conta_pagar = L.cd_conta_pagar) " +
				           "	LEFT OUTER JOIN adm_movimento_conta N ON (N.cd_conta = M.cd_conta " +
				           "    	AND N.cd_movimento_conta = M.cd_movimento_conta) " +
				           "	LEFT OUTER JOIN adm_cheque O ON (N.cd_cheque = O.cd_cheque) " +
				           "WHERE (1 = 1) ", "",
						   criterios, Conexao.conectar(), true);
			int cdContrato = 0;
			float vlReceber = 0, vlParcela1 = 0, vlParcela2 = 0, vlBonus = 0;
			String sql = "";
			while(rsm != null && rsm.next())	{
				cdContrato = rsm.getInt("CD_CONTRATO");
				sql =
					"SELECT SUM(vl_conta-vl_abatimento+vl_acrescimo-vl_recebido) AS VL_RECEBER FROM adm_conta_receber A " +
					"	WHERE (A.cd_contrato = ?) GROUP BY A.cd_contrato";
				PreparedStatement pstmt2 = connect.prepareStatement(sql);
				pstmt2.setInt(1, cdContrato);
				ResultSetMap rsmParcela = new ResultSetMap(pstmt2.executeQuery());
				if(rsmParcela.next()) {
					vlReceber += rsmParcela.getFloat("VL_RECEBER");
				}
				sql =
					"SELECT vl_conta FROM adm_conta_receber A " +
		   			"	WHERE (A.cd_contrato = ?) AND (A.nr_parcela = 1)";
				pstmt2 = connect.prepareStatement(sql);
				pstmt2.setInt(1, cdContrato);
				rsmParcela = new ResultSetMap(pstmt2.executeQuery());
				if(rsmParcela.next())
					vlParcela1  = rsmParcela.getFloat("vl_conta");
				sql =
					"SELECT vl_conta FROM adm_conta_receber A " +
		   			"	WHERE (A.cd_contrato = ?) AND (A.nr_parcela = 2)";
				pstmt2 = connect.prepareStatement(sql);
				pstmt2.setInt(1, cdContrato);
				rsmParcela = new ResultSetMap(pstmt2.executeQuery());
				if(rsmParcela.next())
					vlParcela2  = rsmParcela.getFloat("vl_conta");
				vlBonus = vlParcela2 - vlParcela1;
				rsm.setValueToField("VL_RECEBER", new Float(vlReceber));
				rsm.setValueToField("VL_BONUS", new Float(vlBonus <= 0 ? 0 : vlBonus));
				vlReceber = 0;
				vlBonus = 0;
				vlParcela1 = 0;
				vlParcela2 = 0;
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.findLiberacaoCredito: " +  e);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findClientesAtivos(ArrayList<ItemComparator> criterios, String dtBase, int gnContrato) {
		return findClientesAtivos(criterios, dtBase, gnContrato, null);
	}
	@SuppressWarnings("unchecked")
	public static ResultSetMap findClientesAtivos(ArrayList<ItemComparator> criterios, String dtBase, int gnContrato, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ResultSetMap rsm = new ResultSetMap();
			String relations = "(";
			int numeroLinhas = criterios.size();
			ArrayList<ItemComparator> criteriosCnae = (ArrayList<ItemComparator>)criterios.clone();
			ArrayList<ItemComparator> criteriosCbo = (ArrayList<ItemComparator>)criterios.clone();
			for (int i = 0; criterios != null && i < numeroLinhas; i++) {
				ItemComparator itemCnae = (ItemComparator)((ItemComparator)criterios.get(i)).clone();
				ItemComparator itemCbo = (ItemComparator)((ItemComparator)criterios.get(i)).clone();
				if (itemCnae.getColumn().equalsIgnoreCase("E2.CD_ATIVIDADE")) {
					itemCnae.setColumn("E2.cd_cnae");
					itemCbo.setColumn("E2.cd_cbo");
					criteriosCnae.add(itemCnae);
					criteriosCbo.add(itemCbo);
				}
				relations += String.valueOf(i+1) + " AND ";
			}
			ItemComparator item = ItemComparator.getItemComparatorByColumn(criterios, "E2.CD_ATIVIDADE");
			if (item != null) {
				criteriosCnae.remove(item);
				criteriosCbo.remove(item);
			}

			ItemComparator item1 = new sol.dao.ItemComparator("A.st_conta", String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA), sol.dao.ItemComparator.DIFFERENT, Types.INTEGER);
			ItemComparator item2 = new sol.dao.ItemComparator("A.dt_recebimento", dtBase, sol.dao.ItemComparator.GREATER, Types.TIMESTAMP);
			criteriosCnae.add(item1);
			criteriosCnae.add(item2);
			criteriosCbo.add(item1);
			criteriosCbo.add(item2);
			numeroLinhas = criterios.size();
	        relations += "(" + String.valueOf(numeroLinhas + 1) + " OR " + String.valueOf(numeroLinhas + 2) + "))";

			//Seleciona CNAE
			String sql = "SELECT B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		   	   "    C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
			   "    E2.cd_cnae, E2.nm_cnae, " +
			   "	F.cd_pessoa AS cd_agente, F.nm_pessoa AS nm_agente, " +
			   "	H.cd_cidade, H.nm_cidade, " +
			   "	I.sg_estado, " +
			   "	J.nm_grupo_solidario, " +
			   "    K.id_contrato AS id_convenio, " +
			   "    L.nm_pessoa AS nm_convenio, " +
			   "    SUM(B.vl_contrato/B.nr_parcelas) AS vl_saldo_ca " +
			   "FROM adm_conta_receber A " +
			   "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
			   "	JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
			   "	JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
			   "	JOIN grl_pessoa  E ON (B.cd_pessoa  = E.cd_pessoa) " +
			   "	LEFT OUTER JOIN grl_cnae_pessoa_juridica E1 ON (E.cd_pessoa = E1.cd_pessoa AND E1.lg_principal = 1) " +
			   "	LEFT OUTER JOIN grl_cnae E2 ON (E1.cd_cnae = E2.cd_cnae) " +

			   "	LEFT OUTER JOIN grl_pessoa F ON (B.cd_agente = F.cd_pessoa) " +
	           "	LEFT OUTER JOIN grl_pessoa_endereco G ON (E.cd_pessoa = G.cd_pessoa AND G.lg_principal = 1) " +
	           "	LEFT OUTER JOIN grl_cidade H ON (G.cd_cidade = H.cd_cidade) " +
	           "	LEFT OUTER JOIN grl_estado I ON (H.cd_estado = I.cd_estado) " +
			   "	LEFT OUTER JOIN mcr_grupo_solidario J ON (C.cd_grupo_solidario = J.cd_grupo_solidario) " +
	           "	LEFT OUTER JOIN adm_contrato K ON (A.cd_contrato = K.cd_convenio) " +
	           "	LEFT OUTER JOIN grl_pessoa L ON (K.cd_pessoa = L.cd_pessoa) " +
	           "WHERE (A.st_conta <> " + String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA) + ") " +
	           "	AND (A.st_conta <> " + String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_PERDA) + ") ";
			String orderBy =
	           "GROUP BY B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		   	   "    C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
			   "    E2.cd_cnae, E2.nm_cnae, " +
			   "	F.cd_pessoa, F.nm_pessoa, " +
			   "	H.cd_cidade, H.nm_cidade, " +
			   "	I.sg_estado, " +
			   "	J.nm_grupo_solidario, " +
			   "    K.id_contrato, " +
			   "    L.nm_pessoa";
	        ResultSetMap rsmTemp = Search.find(sql, orderBy, criteriosCnae, relations, Conexao.conectar(), true, false, true);
			int cdPessoa, gnPessoa = 0;
			while(rsmTemp != null && rsmTemp.next())	{
				//Calcula saldo da carteira ativa para cada contrato
/*		        cdContrato = rsmTemp.getInt("CD_CONTRATO");
				sql = "SELECT SUM(B.vl_contrato/B.nr_parcelas) AS vl_saldo_ca " +
				   	  "FROM adm_conta_receber A " +
				   	  "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
				   	  "	   JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
				   	  "	   JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
				   	  "WHERE (A.cd_contrato = ?) " +
			          "	AND (A.st_conta <> ?) AND (A.st_conta <> ?)";
				PreparedStatement pstmt = connect.prepareStatement(sql);
				pstmt.setInt(1, cdContrato);
				pstmt.setInt(2, com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA);
				pstmt.setInt(3, com.tivic.manager.adm.ContaReceberServices.ST_PERDA);
				ResultSetMap rsmSaldo = new ResultSetMap(pstmt.executeQuery());
				float vlSaldoCA = 0;
				if(rsmSaldo.next())
					vlSaldoCA = rsmSaldo.getFloat("vl_saldo_ca");
				rsmTemp.setValueToField("VL_SALDO_CA", vlSaldoCA);*/

				//Calcula número de ciclos de cada cliente
				cdPessoa = rsmTemp.getInt("CD_PESSOA");
				sql =
					"SELECT COUNT(*) AS NR_CICLOS " +
					"	FROM adm_contrato " +
					"WHERE (cd_pessoa = ?) AND (gn_contrato = ?) " +
					"GROUP BY cd_pessoa";
				PreparedStatement pstmt = connect.prepareStatement(sql);
				pstmt.setInt(1, cdPessoa);
				pstmt.setInt(2, gnContrato);
				ResultSetMap rsmCiclos = new ResultSetMap(pstmt.executeQuery());
				int nrCiclos = 0;
				if(rsmCiclos.next())
					nrCiclos = rsmCiclos.getInt("NR_CICLOS");
				rsmTemp.setValueToField("NR_CICLOS", nrCiclos);

				//Cria colunas para armazenar a ATIVIDADE (CNAE/CBO) dependendo do tipo de pessoa
				gnPessoa = rsmTemp.getInt("GN_PESSOA");
				if (gnPessoa == PessoaServices.TP_FISICA) {
					rsmTemp.setValueToField("CD_ATIVIDADE", rsmTemp.getInt("CD_CBO"));
					rsmTemp.setValueToField("NM_ATIVIDADE", rsmTemp.getString("NM_CBO"));
				}
				else {
					rsmTemp.setValueToField("CD_ATIVIDADE", rsmTemp.getInt("CD_CNAE"));
					rsmTemp.setValueToField("NM_ATIVIDADE", rsmTemp.getString("NM_CNAE"));
				}
			}
			rsm = rsmTemp;
			//Seleciona CBO
			sql = "SELECT B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		   	   "    C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
			   "    E2.cd_cbo, E2.nm_cbo, " +
			   "	F.cd_pessoa AS cd_agente, F.nm_pessoa AS nm_agente, " +
			   "	H.cd_cidade, H.nm_cidade, " +
			   "	I.sg_estado, " +
			   "	J.nm_grupo_solidario, " +
			   "    K.id_contrato AS id_convenio, " +
			   "    L.nm_pessoa AS nm_convenio, " +
			   "    SUM(B.vl_contrato/B.nr_parcelas) AS vl_saldo_ca " +
			   "FROM adm_conta_receber A " +
			   "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
			   "	JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
			   "	JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
			   "	JOIN grl_pessoa  E ON (B.cd_pessoa  = E.cd_pessoa) " +
			   "	LEFT OUTER JOIN grl_cbo_pessoa_fisica E1 ON (E.cd_pessoa = E1.cd_pessoa AND E1.lg_principal = 1) " +
			   "	LEFT OUTER JOIN grl_cbo E2 ON (E1.cd_cbo = E2.cd_cbo) " +

			   "	LEFT OUTER JOIN grl_pessoa F ON (B.cd_agente = F.cd_pessoa) " +
	           "	LEFT OUTER JOIN grl_pessoa_endereco G ON (E.cd_pessoa = G.cd_pessoa AND G.lg_principal = 1) " +
	           "	LEFT OUTER JOIN grl_cidade H ON (G.cd_cidade = H.cd_cidade) " +
	           "	LEFT OUTER JOIN grl_estado I ON (H.cd_estado = I.cd_estado) " +
			   "	LEFT OUTER JOIN mcr_grupo_solidario J ON (C.cd_grupo_solidario = J.cd_grupo_solidario) " +
	           "	LEFT OUTER JOIN adm_contrato K ON (A.cd_contrato = K.cd_convenio) " +
	           "	LEFT OUTER JOIN grl_pessoa L ON (K.cd_pessoa = L.cd_pessoa) " +
	           "WHERE (A.st_conta <> " + String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA) + ") " +
	           "	AND (A.st_conta <> " + String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_PERDA) + ") ";
			orderBy =
	           "GROUP BY B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		   	   "    C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
			   "    E2.cd_cbo, E2.nm_cbo, " +
			   "	F.cd_pessoa, F.nm_pessoa, " +
			   "	H.cd_cidade, H.nm_cidade, " +
			   "	I.sg_estado, " +
			   "	J.nm_grupo_solidario, " +
			   "    K.id_contrato, " +
			   "    L.nm_pessoa";
	        rsmTemp = Search.find(sql, orderBy, criteriosCbo, relations, Conexao.conectar(), true, false, true);
			cdPessoa = 0;
			gnPessoa = 0;
			Boolean acheiRegistro = false;
			while (rsmTemp != null && rsmTemp.next()) {
				//Calcula saldo da carteira ativa para cada contrato
/*		        cdContrato = rsmTemp.getInt("CD_CONTRATO");
				sql = "SELECT SUM(B.vl_contrato/B.nr_parcelas) AS vl_saldo_ca " +
				   	  "FROM adm_conta_receber A " +
				   	  "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
				   	  "	   JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
				   	  "	   JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
				   	  "WHERE (A.cd_contrato = ?) " +
			          "	AND (A.st_conta <> ?) AND (A.st_conta <> ?)";
						PreparedStatement pstmt = connect.prepareStatement(sql);
						pstmt.setInt(1, cdContrato);
						pstmt.setInt(2, com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA);
						pstmt.setInt(3, com.tivic.manager.adm.ContaReceberServices.ST_PERDA);
				ResultSetMap rsmSaldo = new ResultSetMap(pstmt.executeQuery());
				float vlSaldoCA = 0;
				if(rsmSaldo.next())
					vlSaldoCA = rsmSaldo.getFloat("vl_saldo_ca");
				rsmTemp.setValueToField("VL_SALDO_CA", vlSaldoCA);*/

				//Calcula número de ciclos para cada cliente
				cdPessoa = rsmTemp.getInt("CD_PESSOA");
				sql =
					"SELECT COUNT(*) AS NR_CICLOS " +
					"	FROM adm_contrato " +
					"WHERE (cd_pessoa = ?) AND (gn_contrato = ?) " +
					"GROUP BY cd_pessoa";
				PreparedStatement pstmt = connect.prepareStatement(sql);
				pstmt.setInt(1, cdPessoa);
				pstmt.setInt(2, gnContrato);
				ResultSetMap rsmCiclos = new ResultSetMap(pstmt.executeQuery());
				int nrCiclos = 0;
				if(rsmCiclos.next())
					nrCiclos = rsmCiclos.getInt("NR_CICLOS");
				rsmTemp.setValueToField("NR_CICLOS", nrCiclos);
				//Cria colunas para armazenar a ATIVIDADE (CNAE/CBO) dependendo do tipo de pessoa
				gnPessoa = rsmTemp.getInt("GN_PESSOA");
				if (gnPessoa == PessoaServices.TP_FISICA) {
					rsmTemp.setValueToField("CD_ATIVIDADE", rsmTemp.getInt("CD_CBO"));
					rsmTemp.setValueToField("NM_ATIVIDADE", rsmTemp.getString("NM_CBO"));
				}
				else {
					rsmTemp.setValueToField("CD_ATIVIDADE", rsmTemp.getInt("CD_CNAE"));
					rsmTemp.setValueToField("NM_ATIVIDADE", rsmTemp.getString("NM_CNAE"));
				}

				acheiRegistro = (rsmTemp.getInt("cd_cbo") <= 0)?rsmTemp.locate("CD_CONTRATO", new Integer(rsmTemp.getInt("cd_contrato"))):false;
				if (!acheiRegistro)	{
					if (rsm.locate("CD_CONTRATO", new Integer(rsmTemp.getInt("cd_contrato"))))
						rsm.deleteRow();
					HashMap<String,Object> regCbo = new HashMap<String,Object>();
					regCbo.put("CD_CONTRATO", new Integer(rsmTemp.getInt("cd_contrato")));
					regCbo.put("CD_PESSOA", new Integer(rsmTemp.getInt("cd_pessoa")));
					regCbo.put("CD_CONVENIO", new Integer(rsmTemp.getInt("cd_convenio")));
					regCbo.put("CD_EMPRESA", new Integer(rsmTemp.getInt("cd_empresa")));
					regCbo.put("NR_CONTRATO", rsmTemp.getString("nr_contrato"));
					regCbo.put("VL_CONTRATO", new Float(rsmTemp.getFloat("vl_contrato")));
					regCbo.put("NR_PARCELAS", new Integer(rsmTemp.getInt("nr_parcelas")));
					regCbo.put("DT_LIBERACAO", Util.convTimestampToCalendar(rsmTemp.getTimestamp("dt_liberacao")));
					regCbo.put("TP_GARANTIA", new Integer(rsmTemp.getInt("tp_garantia")));
					regCbo.put("TP_EMPRESTIMO", new Integer(rsmTemp.getInt("tp_emprestimo")));
					regCbo.put("NM_PESSOA", rsmTemp.getString("nm_pessoa"));
					regCbo.put("NR_TELEFONE1", rsmTemp.getString("nr_telefone1"));
					regCbo.put("GN_PESSOA", new Integer(rsmTemp.getInt("gn_pessoa")));
					regCbo.put("CD_CBO", new Integer(rsmTemp.getInt("cd_cbo")));
					regCbo.put("NM_CBO", rsmTemp.getString("nm_cbo"));
					regCbo.put("CD_AGENTE", new Integer(rsmTemp.getInt("cd_agente")));
					regCbo.put("NM_AGENTE", rsmTemp.getString("nm_agente"));
					regCbo.put("CD_CIDADE", new Integer(rsmTemp.getInt("cd_cidade")));
					regCbo.put("NM_CIDADE", rsmTemp.getString("nm_cidade"));
					regCbo.put("SG_ESTADO", rsmTemp.getString("sg_estado"));
					regCbo.put("NM_GRUPO_SOLIDARIO", rsmTemp.getString("nm_grupo_solidario"));
					regCbo.put("ID_CONVENIO", rsmTemp.getString("id_convenio"));
					regCbo.put("NM_CONVENIO", rsmTemp.getString("nm_convenio"));
					regCbo.put("VL_SALDO_CA", new Float(rsmTemp.getFloat("vl_saldo_ca")));
					regCbo.put("CD_ATIVIDADE", new Integer(rsmTemp.getInt("cd_atividade")));
					regCbo.put("NM_ATIVIDADE", rsmTemp.getString("nm_atividade"));
					regCbo.put("NR_CICLOS", new Integer(rsmTemp.getInt("nr_ciclos")));
					rsm.addRegister(regCbo);
				}
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.findClientesAtivos: " +  e);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findClientesInativos(ArrayList<sol.dao.ItemComparator> criterios, String dtBase, int gnContrato) {
		return findClientesInativos(criterios, dtBase, gnContrato, null);
	}
	@SuppressWarnings("unchecked")
	public static ResultSetMap findClientesInativos(ArrayList<sol.dao.ItemComparator> criterios, String dtBase, int gnContrato, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			try	{
				if (Util.convStringCalendar(dtBase)==null){
					throw new Exception("Date bad formatted");
				}
			}
			catch(Exception e){
				e.printStackTrace(System.out);
				System.err.println("Erro! ContratoServices.findClientesInativos: " +  e);
				return null;
			}
			ResultSetMap rsm = new ResultSetMap();
			//Cria ItemComparators diferentes para cada tipo de atividades (CNAE/CBO)
			int numeroLinhas = criterios.size();
			ArrayList<ItemComparator> criteriosCnae = (ArrayList<ItemComparator>)criterios.clone();
			ArrayList<ItemComparator> criteriosCbo = (ArrayList<ItemComparator>)criterios.clone();
			for (int i = 0; criterios != null && i < numeroLinhas; i++) {
				ItemComparator itemCnae = (ItemComparator)((ItemComparator)criterios.get(i)).clone();
				ItemComparator itemCbo = (ItemComparator)((ItemComparator)criterios.get(i)).clone();
				if (itemCnae.getColumn().equalsIgnoreCase("E2.CD_ATIVIDADE")) {
					itemCnae.setColumn("E2.cd_cnae");
					itemCbo.setColumn("E2.cd_cbo");
					criteriosCnae.add(itemCnae);
					criteriosCbo.add(itemCbo);
				}
			}
			ItemComparator item = ItemComparator.getItemComparatorByColumn(criterios, "E2.CD_ATIVIDADE");
			if (item != null) {
				criteriosCnae.remove(item);
				criteriosCbo.remove(item);
			}

			numeroLinhas = criterios.size();
			//Seleciona CNAE
			String sql = "SELECT B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		   	   "    C.cd_grupo_solidario, C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
			   "    E2.cd_cnae, E2.nm_cnae, " +
			   "	F.cd_pessoa AS cd_agente, F.nm_pessoa AS nm_agente, " +
			   "	H.cd_cidade, H.nm_cidade, " +
			   "	I.sg_estado, " +
			   "	J.nm_grupo_solidario, " +
			   "    K.id_contrato AS id_convenio, " +
			   "    L.nm_pessoa AS nm_convenio " +
			   "FROM adm_contrato B " +
			   "	JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
			   "	JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
			   "	JOIN grl_pessoa  E ON (B.cd_pessoa  = E.cd_pessoa) " +
			   "	LEFT OUTER JOIN grl_cnae_pessoa_juridica E1 ON (E.cd_pessoa = E1.cd_pessoa AND E1.lg_principal = 1) " +
			   "	LEFT OUTER JOIN grl_cnae E2 ON (E1.cd_cnae = E2.cd_cnae) " +

			   "	LEFT OUTER JOIN grl_pessoa F ON (B.cd_agente = F.cd_pessoa) " +
	           "	LEFT OUTER JOIN grl_pessoa_endereco G ON (E.cd_pessoa = G.cd_pessoa AND G.lg_principal = 1) " +
	           "	LEFT OUTER JOIN grl_cidade H ON (G.cd_cidade = H.cd_cidade) " +
	           "	LEFT OUTER JOIN grl_estado I ON (H.cd_estado = I.cd_estado) " +
			   "	LEFT OUTER JOIN mcr_grupo_solidario J ON (C.cd_grupo_solidario = J.cd_grupo_solidario) " +
	           "	LEFT OUTER JOIN adm_contrato K ON (B.cd_contrato = K.cd_convenio) " +
	           "	LEFT OUTER JOIN grl_pessoa L ON (K.cd_pessoa = L.cd_pessoa) " +
	           "WHERE B.cd_pessoa NOT IN " +
	           "	(SELECT DISTINCT M.cd_pessoa " +
	           "	 FROM adm_conta_receber M " +
	           "	 WHERE (M.cd_pessoa IS NOT NULL) AND ((M.st_conta <> " + String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA) + ")" +
	           "            OR (M.dt_recebimento > \'" + Util.formatDateTime(Util.convStringCalendar(dtBase), "MM/dd/yyyy") + "\')))";

			String orderBy = "";
	        ResultSetMap rsmTemp = Search.find(sql, orderBy, criteriosCnae, Conexao.conectar(), true);
			int cdContrato, cdPessoa, gnPessoa = 0;
			while(rsmTemp != null && rsmTemp.next())	{
				//Calcula saldo da carteira ativa para cada contrato
		        cdContrato = rsmTemp.getInt("CD_CONTRATO");
				sql = "SELECT SUM(B.vl_contrato/B.nr_parcelas) AS vl_saldo_ca " +
				   	  "FROM adm_conta_receber A " +
				   	  "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
				   	  "	   JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
				   	  "	   JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
				   	  "WHERE (A.cd_contrato = ?) " +
			          "	AND (A.st_conta <> ?) AND (A.st_conta <> ?)";
				PreparedStatement pstmt = connect.prepareStatement(sql);
				pstmt.setInt(1, cdContrato);
				pstmt.setInt(2, com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA);
				pstmt.setInt(3, com.tivic.manager.adm.ContaReceberServices.ST_PERDA);
				ResultSetMap rsmSaldo = new ResultSetMap(pstmt.executeQuery());
				float vlSaldoCA = 0;
				if(rsmSaldo.next())
					vlSaldoCA = rsmSaldo.getFloat("vl_saldo_ca");
				rsmTemp.setValueToField("VL_SALDO_CA", vlSaldoCA);

				//Calcula número de ciclos de cada cliente
				cdPessoa = rsmTemp.getInt("CD_PESSOA");
				sql =
					"SELECT COUNT(*) AS NR_CICLOS " +
					"	FROM adm_contrato " +
					"WHERE (cd_pessoa = ?) AND (gn_contrato = ?) " +
					"GROUP BY cd_pessoa";
				pstmt = connect.prepareStatement(sql);
				pstmt.setInt(1, cdPessoa);
				pstmt.setInt(2, gnContrato);
				ResultSetMap rsmCiclos = new ResultSetMap(pstmt.executeQuery());
				int nrCiclos = 0;
				if(rsmCiclos.next())
					nrCiclos = rsmCiclos.getInt("NR_CICLOS");
				rsmTemp.setValueToField("NR_CICLOS", nrCiclos);
				//Cria colunas para armazenar a ATIVIDADE (CNAE/CBO) dependendo do tipo de pessoa
				gnPessoa = rsmTemp.getInt("GN_PESSOA");
				if (gnPessoa == PessoaServices.TP_FISICA) {
					rsmTemp.setValueToField("CD_ATIVIDADE", rsmTemp.getInt("CD_CBO"));
					rsmTemp.setValueToField("NM_ATIVIDADE", rsmTemp.getString("NM_CBO"));
				}
				else {
					rsmTemp.setValueToField("CD_ATIVIDADE", rsmTemp.getInt("CD_CNAE"));
					rsmTemp.setValueToField("NM_ATIVIDADE", rsmTemp.getString("NM_CNAE"));
				}
			}
			rsm = rsmTemp;
			//Seleciona CBO
			sql = "SELECT B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		   	   "    C.cd_grupo_solidario, C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
			   "    E2.cd_cbo, E2.nm_cbo, " +
			   "	F.cd_pessoa AS cd_agente, F.nm_pessoa AS nm_agente, " +
			   "	H.cd_cidade, H.nm_cidade, " +
			   "	I.sg_estado, " +
			   "	J.nm_grupo_solidario, " +
			   "    K.id_contrato AS id_convenio, " +
			   "    L.nm_pessoa AS nm_convenio " +
			   "FROM adm_contrato B " +
			   "	JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
			   "	JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
			   "	JOIN grl_pessoa  E ON (B.cd_pessoa  = E.cd_pessoa) " +
			   "	LEFT OUTER JOIN grl_cbo_pessoa_fisica E1 ON (E.cd_pessoa = E1.cd_pessoa AND E1.lg_principal = 1) " +
			   "	LEFT OUTER JOIN grl_cbo E2 ON (E1.cd_cbo = E2.cd_cbo) " +

			   "	LEFT OUTER JOIN grl_pessoa F ON (B.cd_agente = F.cd_pessoa) " +
	           "	LEFT OUTER JOIN grl_pessoa_endereco G ON (E.cd_pessoa = G.cd_pessoa AND G.lg_principal = 1) " +
	           "	LEFT OUTER JOIN grl_cidade H ON (G.cd_cidade = H.cd_cidade) " +
	           "	LEFT OUTER JOIN grl_estado I ON (H.cd_estado = I.cd_estado) " +
			   "	LEFT OUTER JOIN mcr_grupo_solidario J ON (C.cd_grupo_solidario = J.cd_grupo_solidario) " +
	           "	LEFT OUTER JOIN adm_contrato K ON (B.cd_contrato = K.cd_convenio) " +
	           "	LEFT OUTER JOIN grl_pessoa L ON (K.cd_pessoa = L.cd_pessoa) " +
	           "WHERE B.cd_pessoa NOT IN " +
	           "	(SELECT DISTINCT M.cd_pessoa " +
	           "	 FROM adm_conta_receber M " +
	           "	 WHERE (M.cd_pessoa IS NOT NULL) AND ((M.st_conta <> " + String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA) + ")" +
	           "            OR (M.dt_recebimento > \'" + Util.formatDateTime(Util.convStringCalendar(dtBase), "MM/dd/yyyy") + "\')))";

			orderBy = "";
	        rsmTemp = Search.find(sql, orderBy, criteriosCbo, Conexao.conectar(), true);
			cdContrato = 0;
			cdPessoa = 0;
			gnPessoa = 0;
			Boolean acheiRegistro = false;
			while (rsmTemp != null && rsmTemp.next()) {
				//Calcula saldo da carteira ativa para cada contrato
		        cdContrato = rsmTemp.getInt("CD_CONTRATO");
				sql = "SELECT SUM(B.vl_contrato/B.nr_parcelas) AS vl_saldo_ca " +
				   	  "FROM adm_conta_receber A " +
				   	  "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
				   	  "	   JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
				   	  "	   JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
				   	  "WHERE (A.cd_contrato = ?) " +
			          "	AND (A.st_conta <> ?) AND (A.st_conta <> ?)";
				PreparedStatement pstmt = connect.prepareStatement(sql);
				pstmt.setInt(1, cdContrato);
				pstmt.setInt(2, com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA);
				pstmt.setInt(3, com.tivic.manager.adm.ContaReceberServices.ST_PERDA);
				ResultSetMap rsmSaldo = new ResultSetMap(pstmt.executeQuery());
				float vlSaldoCA = 0;
				if(rsmSaldo.next())
					vlSaldoCA = rsmSaldo.getFloat("vl_saldo_ca");
				rsmTemp.setValueToField("VL_SALDO_CA", vlSaldoCA);

				//Calcula número de ciclos de cada cliente
				cdPessoa = rsmTemp.getInt("CD_PESSOA");
				sql =
					"SELECT COUNT(*) AS NR_CICLOS " +
					"	FROM adm_contrato " +
					"WHERE (cd_pessoa = ?) AND (gn_contrato = ?) " +
					"GROUP BY cd_pessoa";
				pstmt = connect.prepareStatement(sql);
				pstmt.setInt(1, cdPessoa);
				pstmt.setInt(2, gnContrato);
				ResultSetMap rsmCiclos = new ResultSetMap(pstmt.executeQuery());
				int nrCiclos = 0;
				if(rsmCiclos.next())
					nrCiclos = rsmCiclos.getInt("NR_CICLOS");
				rsmTemp.setValueToField("NR_CICLOS", nrCiclos);
				//Cria colunas para armazenar a ATIVIDADE (CNAE/CBO) dependendo do tipo de pessoa
				gnPessoa = rsmTemp.getInt("GN_PESSOA");
				if (gnPessoa == PessoaServices.TP_FISICA) {
					rsmTemp.setValueToField("CD_ATIVIDADE", rsmTemp.getInt("CD_CBO"));
					rsmTemp.setValueToField("NM_ATIVIDADE", rsmTemp.getString("NM_CBO"));
				}
				else {
					rsmTemp.setValueToField("CD_ATIVIDADE", rsmTemp.getInt("CD_CNAE"));
					rsmTemp.setValueToField("NM_ATIVIDADE", rsmTemp.getString("NM_CNAE"));
				}
				acheiRegistro = (rsmTemp.getInt("cd_cbo") <= 0)?rsmTemp.locate("CD_CONTRATO", new Integer(rsmTemp.getInt("cd_contrato"))):false;
				if (!acheiRegistro)	{
					if (rsm.locate("CD_CONTRATO", new Integer(rsmTemp.getInt("cd_contrato"))))
						rsm.deleteRow();

					HashMap<String,Object> regCbo = new HashMap<String,Object>();
					regCbo.put("CD_CONTRATO", new Integer(rsmTemp.getInt("cd_contrato")));
					regCbo.put("CD_PESSOA", new Integer(rsmTemp.getInt("cd_pessoa")));
					regCbo.put("CD_CONVENIO", new Integer(rsmTemp.getInt("cd_convenio")));
					regCbo.put("CD_EMPRESA", new Integer(rsmTemp.getInt("cd_empresa")));
					regCbo.put("NR_CONTRATO", rsmTemp.getString("nr_contrato"));
					regCbo.put("VL_CONTRATO", new Float(rsmTemp.getFloat("vl_contrato")));
					regCbo.put("NR_PARCELAS", new Integer(rsmTemp.getInt("nr_parcelas")));
					regCbo.put("CD_GRUPO_SOLIDARIO", new Integer(rsmTemp.getInt("cd_grupo_solidario")));
					regCbo.put("DT_LIBERACAO", Util.convTimestampToCalendar(rsmTemp.getTimestamp("dt_liberacao")));
					regCbo.put("TP_GARANTIA", new Integer(rsmTemp.getInt("tp_garantia")));
					regCbo.put("TP_EMPRESTIMO", new Integer(rsmTemp.getInt("tp_emprestimo")));
					regCbo.put("NM_PESSOA", rsmTemp.getString("nm_pessoa"));
					regCbo.put("NR_TELEFONE1", rsmTemp.getString("nr_telefone1"));
					regCbo.put("GN_PESSOA", new Integer(rsmTemp.getInt("gn_pessoa")));
					regCbo.put("CD_CBO", new Integer(rsmTemp.getInt("cd_cbo")));
					regCbo.put("NM_CBO", rsmTemp.getString("nm_cbo"));
					regCbo.put("CD_AGENTE", new Integer(rsmTemp.getInt("cd_agente")));
					regCbo.put("NM_AGENTE", rsmTemp.getString("nm_agente"));
					regCbo.put("CD_CIDADE", new Integer(rsmTemp.getInt("cd_cidade")));
					regCbo.put("NM_CIDADE", rsmTemp.getString("nm_cidade"));
					regCbo.put("SG_ESTADO", rsmTemp.getString("sg_estado"));
					regCbo.put("NM_GRUPO_SOLIDARIO", rsmTemp.getString("nm_grupo_solidario"));
					regCbo.put("ID_CONVENIO", rsmTemp.getString("id_convenio"));
					regCbo.put("NM_CONVENIO", rsmTemp.getString("nm_convenio"));
					regCbo.put("VL_SALDO_CA", new Float(rsmTemp.getFloat("vl_saldo_ca")));
					regCbo.put("CD_ATIVIDADE", new Integer(rsmTemp.getInt("cd_atividade")));
					regCbo.put("NM_ATIVIDADE", rsmTemp.getString("nm_atividade"));
					regCbo.put("NR_CICLOS", new Integer(rsmTemp.getInt("nr_ciclos")));
					rsm.addRegister(regCbo);
				}
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.findClientesInativos: " +  e);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findVisitasDiretoria(ArrayList<ItemComparator> criterios, String dtBase, int gnContrato) {
		return findVisitasDiretoria(criterios, dtBase, gnContrato, null);
	}
	@SuppressWarnings("unchecked")
	public static ResultSetMap findVisitasDiretoria(ArrayList<ItemComparator> criterios, String dtBase, int gnContrato, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ResultSetMap rsm = new ResultSetMap();
			String relations = "(";
			int numeroLinhas = criterios.size();
			ArrayList<ItemComparator> criteriosCnae = (ArrayList<ItemComparator>)criterios.clone();
			ArrayList<ItemComparator> criteriosCbo  = (ArrayList<ItemComparator>)criterios.clone();
			for (int i = 0; criterios != null && i < numeroLinhas; i++) {
				ItemComparator itemCnae = (ItemComparator)((ItemComparator)criterios.get(i)).clone();
				ItemComparator itemCbo = (ItemComparator)((ItemComparator)criterios.get(i)).clone();
				if (itemCnae.getColumn().equalsIgnoreCase("E2.CD_ATIVIDADE")) {
					itemCnae.setColumn("E2.cd_cnae");
					itemCbo.setColumn("E2.cd_cbo");
					criteriosCnae.add(itemCnae);
					criteriosCbo.add(itemCbo);
				}
				relations += String.valueOf(i+1) + " AND ";
			}
			/*
			ItemComparator item = ItemComparator.getItemComparatorByColumn(criterios, "E2.CD_ATIVIDADE");
			if (item != null) {
				criteriosCnae.remove(item);
				criteriosCbo.remove(item);
			}
			*/
			ItemComparator item1 = new sol.dao.ItemComparator("A.st_conta", String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA), sol.dao.ItemComparator.DIFFERENT, Types.INTEGER);
			ItemComparator item2 = new sol.dao.ItemComparator("A.dt_recebimento", dtBase, sol.dao.ItemComparator.GREATER, Types.TIMESTAMP);
			criteriosCnae.add(item1);
			criteriosCnae.add(item2);
			criteriosCbo.add(item1);
			criteriosCbo.add(item2);
			numeroLinhas = criterios.size();
	        relations += "(" + String.valueOf(numeroLinhas + 1) + " OR " + String.valueOf(numeroLinhas + 2) + "))";

			//Seleciona CNAE
			String sql = "SELECT B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		   	   "    C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
			   "    E2.cd_cnae, E2.nm_cnae, " +
			   "	F.cd_classificacao, F.nm_classificacao, " +
			   "	H.cd_cidade, H.nm_cidade, " +
			   "	I.sg_estado, " +
			   "	J.nm_grupo_solidario, " +
			   "    K.id_contrato AS id_convenio, " +
			   "    L.nm_pessoa AS nm_convenio " +
			   "FROM adm_conta_receber A " +
			   "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
			   "	JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
			   "	JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
			   "	JOIN grl_pessoa  E ON (B.cd_pessoa  = E.cd_pessoa) " +
			   "	LEFT OUTER JOIN grl_cnae_pessoa_juridica E1 ON (E.cd_pessoa = E1.cd_pessoa AND E1.lg_principal = 1) " +
			   "	LEFT OUTER JOIN grl_cnae E2 ON (E1.cd_cnae = E2.cd_cnae) " +

			   "	LEFT OUTER JOIN grl_classificacao_financeira F ON (E.cd_classificacao = F.cd_classificacao) " +
	           "	LEFT OUTER JOIN grl_pessoa_endereco G ON (E.cd_pessoa = G.cd_pessoa AND G.lg_principal = 1) " +
	           "	LEFT OUTER JOIN grl_cidade H ON (G.cd_cidade = H.cd_cidade) " +
	           "	LEFT OUTER JOIN grl_estado I ON (H.cd_estado = I.cd_estado) " +
			   "	LEFT OUTER JOIN mcr_grupo_solidario J ON (C.cd_grupo_solidario = J.cd_grupo_solidario) " +
	           "	LEFT OUTER JOIN adm_contrato K ON (A.cd_contrato = K.cd_convenio) " +
	           "	LEFT OUTER JOIN grl_pessoa L ON (K.cd_pessoa = L.cd_pessoa) " +
	           "WHERE (1 = 1) ";
			String orderBy =
	           "GROUP BY B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		   	   "    C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
			   "    E2.cd_cnae, E2.nm_cnae, " +
			   "	F.cd_classificacao, F.nm_classificacao, " +
			   "	H.cd_cidade, H.nm_cidade, " +
			   "	I.sg_estado, " +
			   "	J.nm_grupo_solidario, " +
			   "    K.id_contrato, " +
			   "    L.nm_pessoa";
	        ResultSetMap rsmTemp = Search.find(sql, orderBy, criteriosCnae, relations, Conexao.conectar(), true, false, true);
			int cdContrato, cdPessoa, gnPessoa = 0;
			while(rsmTemp != null && rsmTemp.next())	{
				//Calcula saldo da carteira ativa para cada contrato
		        cdContrato = rsmTemp.getInt("CD_CONTRATO");
				sql = "SELECT SUM(B.vl_contrato/B.nr_parcelas) AS vl_saldo_ca " +
				   	  "FROM adm_conta_receber A " +
				   	  "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
				   	  "	   JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
				   	  "	   JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
				   	  "WHERE (A.cd_contrato = ?) " +
			          "	AND (A.st_conta <> ?) AND (A.st_conta <> ?)";
				PreparedStatement pstmt = connect.prepareStatement(sql);
				pstmt.setInt(1, cdContrato);
				pstmt.setInt(2, com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA);
				pstmt.setInt(3, com.tivic.manager.adm.ContaReceberServices.ST_PERDA);
				ResultSetMap rsmSaldo = new ResultSetMap(pstmt.executeQuery());
				float vlSaldoCA = 0;
				if(rsmSaldo.next())
					vlSaldoCA = rsmSaldo.getFloat("vl_saldo_ca");
				rsmTemp.setValueToField("VL_SALDO_CA", vlSaldoCA);

				//Calcula número de ciclos de cada cliente
				cdPessoa = rsmTemp.getInt("CD_PESSOA");
				sql =
					"SELECT COUNT(*) AS NR_CICLOS " +
					"	FROM adm_contrato " +
					"WHERE (cd_pessoa = ?) AND (gn_contrato = ?) " +
					"GROUP BY cd_pessoa";
				pstmt = connect.prepareStatement(sql);
				pstmt.setInt(1, cdPessoa);
				pstmt.setInt(2, gnContrato);
				ResultSetMap rsmCiclos = new ResultSetMap(pstmt.executeQuery());
				int nrCiclos = 0;
				if(rsmCiclos.next())
					nrCiclos = rsmCiclos.getInt("NR_CICLOS");
				rsmTemp.setValueToField("NR_CICLOS", nrCiclos);
				//Cria colunas para armazenar a ATIVIDADE (CNAE/CBO) dependendo do tipo de pessoa
				gnPessoa = rsmTemp.getInt("GN_PESSOA");
				if (gnPessoa == PessoaServices.TP_FISICA) {
					rsmTemp.setValueToField("CD_ATIVIDADE", rsmTemp.getInt("CD_CBO"));
					rsmTemp.setValueToField("NM_ATIVIDADE", rsmTemp.getString("NM_CBO"));
				}
				else {
					rsmTemp.setValueToField("CD_ATIVIDADE", rsmTemp.getInt("CD_CNAE"));
					rsmTemp.setValueToField("NM_ATIVIDADE", rsmTemp.getString("NM_CNAE"));
				}
			}
			rsm = rsmTemp;
			//Seleciona CBO
			sql = "SELECT B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		   	   "    C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
			   "    E2.cd_cbo, E2.nm_cbo, " +
			   "	F.cd_classificacao, F.nm_classificacao, " +
			   "	H.cd_cidade, H.nm_cidade, " +
			   "	I.sg_estado, " +
			   "	J.nm_grupo_solidario, " +
			   "    K.id_contrato AS id_convenio, " +
			   "    L.nm_pessoa AS nm_convenio " +
			   "FROM adm_conta_receber A " +
			   "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
			   "	JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
			   "	JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
			   "	JOIN grl_pessoa  E ON (B.cd_pessoa  = E.cd_pessoa) " +
			   "	LEFT OUTER JOIN grl_cbo_pessoa_fisica E1 ON (E.cd_pessoa = E1.cd_pessoa AND E1.lg_principal = 1) " +
			   "	LEFT OUTER JOIN grl_cbo E2 ON (E1.cd_cbo = E2.cd_cbo) " +

			   "	LEFT OUTER JOIN grl_classificacao_financeira F ON (E.cd_classificacao = F.cd_classificacao) " +
	           "	LEFT OUTER JOIN grl_pessoa_endereco G ON (E.cd_pessoa = G.cd_pessoa AND G.lg_principal = 1) " +
	           "	LEFT OUTER JOIN grl_cidade H ON (G.cd_cidade = H.cd_cidade) " +
	           "	LEFT OUTER JOIN grl_estado I ON (H.cd_estado = I.cd_estado) " +
			   "	LEFT OUTER JOIN mcr_grupo_solidario J ON (C.cd_grupo_solidario = J.cd_grupo_solidario) " +
	           "	LEFT OUTER JOIN adm_contrato K ON (A.cd_contrato = K.cd_convenio) " +
	           "	LEFT OUTER JOIN grl_pessoa L ON (K.cd_pessoa = L.cd_pessoa) " +
	           "WHERE (1 = 1) ";
			orderBy =
	           "GROUP BY B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		   	   "    C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
			   "    E2.cd_cbo, E2.nm_cbo, " +
			   "	F.cd_classificacao, F.nm_classificacao, " +
			   "	H.cd_cidade, H.nm_cidade, " +
			   "	I.sg_estado, " +
			   "	J.nm_grupo_solidario, " +
			   "    K.id_contrato, " +
			   "    L.nm_pessoa";
	        rsmTemp = Search.find(sql, orderBy, criteriosCbo, relations, Conexao.conectar(), true, false, true);
			cdContrato = 0;
			cdPessoa = 0;
			gnPessoa = 0;
			Boolean acheiRegistro = false;
			while (rsmTemp != null && rsmTemp.next()) {
				//Calcula saldo da carteira ativa para cada contrato
		        cdContrato = rsmTemp.getInt("CD_CONTRATO");
				sql = "SELECT SUM(B.vl_contrato/B.nr_parcelas) AS vl_saldo_ca " +
				   	  "FROM adm_conta_receber A " +
				   	  "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
				   	  "	   JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
				   	  "	   JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
				   	  "WHERE (A.cd_contrato = ?) " +
			          "	AND (A.st_conta <> ?) AND (A.st_conta <> ?)";
				PreparedStatement pstmt = connect.prepareStatement(sql);
				pstmt.setInt(1, cdContrato);
				pstmt.setInt(2, com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA);
				pstmt.setInt(3, com.tivic.manager.adm.ContaReceberServices.ST_PERDA);
				ResultSetMap rsmSaldo = new ResultSetMap(pstmt.executeQuery());
				float vlSaldoCA = 0;
				if(rsmSaldo.next())
					vlSaldoCA = rsmSaldo.getFloat("vl_saldo_ca");
				rsmTemp.setValueToField("VL_SALDO_CA", vlSaldoCA);

				//Calcula número de ciclos de cada cliente
				cdPessoa = rsmTemp.getInt("CD_PESSOA");
				sql =
					"SELECT COUNT(*) AS NR_CICLOS " +
					"	FROM adm_contrato " +
					"WHERE (cd_pessoa = ?) AND (gn_contrato = ?) " +
					"GROUP BY cd_pessoa";
				pstmt = connect.prepareStatement(sql);
				pstmt.setInt(1, cdPessoa);
				pstmt.setInt(2, gnContrato);
				ResultSetMap rsmCiclos = new ResultSetMap(pstmt.executeQuery());
				int nrCiclos = 0;
				if(rsmCiclos.next())
					nrCiclos = rsmCiclos.getInt("NR_CICLOS");
				rsmTemp.setValueToField("NR_CICLOS", nrCiclos);
				//Cria colunas para armazenar a ATIVIDADE (CNAE/CBO) dependendo do tipo de pessoa
				gnPessoa = rsmTemp.getInt("GN_PESSOA");
				if (gnPessoa == PessoaServices.TP_FISICA) {
					rsmTemp.setValueToField("CD_ATIVIDADE", rsmTemp.getInt("CD_CBO"));
					rsmTemp.setValueToField("NM_ATIVIDADE", rsmTemp.getString("NM_CBO"));
				}
				else {
					rsmTemp.setValueToField("CD_ATIVIDADE", rsmTemp.getInt("CD_CNAE"));
					rsmTemp.setValueToField("NM_ATIVIDADE", rsmTemp.getString("NM_CNAE"));
				}
				acheiRegistro = (rsmTemp.getInt("cd_cbo") <= 0)?rsmTemp.locate("CD_CONTRATO", new Integer(rsmTemp.getInt("cd_contrato"))):false;
				if (!acheiRegistro)	{
					if (rsm.locate("CD_CONTRATO", new Integer(rsmTemp.getInt("cd_contrato"))))
						rsm.deleteRow();

					HashMap<String,Object> regCbo = new HashMap<String,Object>();
					regCbo.put("CD_CONTRATO", new Integer(rsmTemp.getInt("cd_contrato")));
					regCbo.put("CD_PESSOA", new Integer(rsmTemp.getInt("cd_pessoa")));
					regCbo.put("CD_CONVENIO", new Integer(rsmTemp.getInt("cd_convenio")));
					regCbo.put("CD_EMPRESA", new Integer(rsmTemp.getInt("cd_empresa")));
					regCbo.put("NR_CONTRATO", rsmTemp.getString("nr_contrato"));
					regCbo.put("VL_CONTRATO", new Float(rsmTemp.getFloat("vl_contrato")));
					regCbo.put("NR_PARCELAS", new Integer(rsmTemp.getInt("nr_parcelas")));
					regCbo.put("DT_LIBERACAO", Util.convTimestampToCalendar(rsmTemp.getTimestamp("dt_liberacao")));
					regCbo.put("TP_GARANTIA", new Integer(rsmTemp.getInt("tp_garantia")));
					regCbo.put("TP_EMPRESTIMO", new Integer(rsmTemp.getInt("tp_emprestimo")));
					regCbo.put("NM_PESSOA", rsmTemp.getString("nm_pessoa"));
					regCbo.put("NR_TELEFONE1", rsmTemp.getString("nr_telefone1"));
					regCbo.put("GN_PESSOA", new Integer(rsmTemp.getInt("gn_pessoa")));
					regCbo.put("CD_CBO", new Integer(rsmTemp.getInt("cd_cbo")));
					regCbo.put("NM_CBO", rsmTemp.getString("nm_cbo"));
					regCbo.put("CD_CLASSIFICACAO", new Integer(rsmTemp.getInt("cd_classificacao")));
					regCbo.put("NM_CLASSIFICACAO", rsmTemp.getString("nm_classificacao"));
					regCbo.put("CD_CIDADE", new Integer(rsmTemp.getInt("cd_cidade")));
					regCbo.put("NM_CIDADE", rsmTemp.getString("nm_cidade"));
					regCbo.put("SG_ESTADO", rsmTemp.getString("sg_estado"));
					regCbo.put("NM_GRUPO_SOLIDARIO", rsmTemp.getString("nm_grupo_solidario"));
					regCbo.put("ID_CONVENIO", rsmTemp.getString("id_convenio"));
					regCbo.put("NM_CONVENIO", rsmTemp.getString("nm_convenio"));
					regCbo.put("VL_SALDO_CA", new Float(rsmTemp.getFloat("vl_saldo_ca")));
					regCbo.put("VL_SALDO_DEVEDOR", new Float(rsmTemp.getFloat("vl_saldo_devedor")));
					regCbo.put("CD_ATIVIDADE", new Integer(rsmTemp.getInt("cd_atividade")));
					regCbo.put("NM_ATIVIDADE", rsmTemp.getString("nm_atividade"));
					regCbo.put("NR_CICLOS", new Integer(rsmTemp.getInt("nr_ciclos")));
					rsm.addRegister(regCbo);
				}
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.findVisitasDiretoria: " +  e);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findRisco(ArrayList<ItemComparator> criterios, String dtBase, int gnContrato) {
		return findRisco(criterios, dtBase, gnContrato, null);
	}
	@SuppressWarnings("unchecked")
	public static ResultSetMap findRisco(ArrayList<ItemComparator> criterios, String dtBase, int gnContrato, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ResultSetMap rsm = new ResultSetMap();
			String relations = "(";
			int numeroLinhas = criterios.size();
			ArrayList<ItemComparator> criteriosCnae = (ArrayList<ItemComparator>)criterios.clone();
			ArrayList<ItemComparator> criteriosCbo = (ArrayList<ItemComparator>)criterios.clone();
			for (int i = 0; criterios != null && i < numeroLinhas; i++) {
				ItemComparator itemCnae = (ItemComparator)((ItemComparator)criterios.get(i)).clone();
				ItemComparator itemCbo = (ItemComparator)((ItemComparator)criterios.get(i)).clone();
				if (itemCnae.getColumn().equalsIgnoreCase("E2.CD_ATIVIDADE")) {
					itemCnae.setColumn("E2.cd_cnae");
					itemCbo.setColumn("E2.cd_cbo");
					criteriosCnae.add(itemCnae);
					criteriosCbo.add(itemCbo);
				}
				relations += String.valueOf(i+1) + " AND ";
			}
			ItemComparator item = ItemComparator.getItemComparatorByColumn(criterios, "E2.CD_ATIVIDADE");
			if (item != null) {
				criteriosCnae.remove(item);
				criteriosCbo.remove(item);
			}

			ItemComparator item1 = new sol.dao.ItemComparator("A.st_conta", String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA), sol.dao.ItemComparator.DIFFERENT, Types.INTEGER);
			ItemComparator item2 = new sol.dao.ItemComparator("A.dt_recebimento", dtBase, sol.dao.ItemComparator.GREATER, Types.TIMESTAMP);
			criteriosCnae.add(item1);
			criteriosCnae.add(item2);
			criteriosCbo.add(item1);
			criteriosCbo.add(item2);
			numeroLinhas = criterios.size();
	        relations += "(" + String.valueOf(numeroLinhas + 1) + " OR " + String.valueOf(numeroLinhas + 2) + "))";

			//Seleciona CNAE
			String sql = "SELECT B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		   	   "    C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
			   "    E2.cd_cnae, E2.nm_cnae, " +
			   "	F.cd_pessoa AS cd_agente, F.nm_pessoa AS nm_agente, " +
			   "	H.cd_cidade, H.nm_cidade, " +
			   "	I.sg_estado, " +
			   "	J.nm_grupo_solidario, " +
			   "    K.id_contrato AS id_convenio, " +
			   "    L.nm_pessoa AS nm_convenio, " +
			   "	SUM(B.vl_contrato/B.nr_parcelas) AS vl_ca_risco " +
			   "FROM adm_conta_receber A " +
			   "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
			   "	JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
			   "	JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
			   "	JOIN grl_pessoa  E ON (B.cd_pessoa  = E.cd_pessoa) " +
			   "	LEFT OUTER JOIN grl_cnae_pessoa_juridica E1 ON (E.cd_pessoa = E1.cd_pessoa AND E1.lg_principal = 1) " +
			   "	LEFT OUTER JOIN grl_cnae E2 ON (E1.cd_cnae = E2.cd_cnae) " +

			   "	LEFT OUTER JOIN grl_pessoa F ON (B.cd_agente = F.cd_pessoa) " +
	           "	LEFT OUTER JOIN grl_pessoa_endereco G ON (E.cd_pessoa = G.cd_pessoa AND G.lg_principal = 1) " +
	           "	LEFT OUTER JOIN grl_cidade H ON (G.cd_cidade = H.cd_cidade) " +
	           "	LEFT OUTER JOIN grl_estado I ON (H.cd_estado = I.cd_estado) " +
			   "	LEFT OUTER JOIN mcr_grupo_solidario J ON (C.cd_grupo_solidario = J.cd_grupo_solidario) " +
	           "	LEFT OUTER JOIN adm_contrato K ON (A.cd_contrato = K.cd_convenio) " +
	           "	LEFT OUTER JOIN grl_pessoa L ON (K.cd_pessoa = L.cd_pessoa) " +
	           "WHERE ((CAST(\'" + Util.formatDateTime(Util.convStringCalendar(dtBase), "MM/dd/yyyy") + "\' AS TIMESTAMP) - A.dt_vencimento) > 30) " +
	           "	AND (A.st_conta <> " + String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_PERDA) + ") ";
			String orderBy =
		           "GROUP BY B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
			   	   "    C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
				   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
				   "    E2.cd_cnae, E2.nm_cnae, " +
				   "	F.cd_pessoa, F.nm_pessoa, " +
				   "	H.cd_cidade, H.nm_cidade, " +
				   "	I.sg_estado, " +
				   "	J.nm_grupo_solidario, " +
				   "    K.id_contrato, " +
				   "    L.nm_pessoa";

	        ResultSetMap rsmTemp = Search.find(sql, orderBy, criteriosCnae, relations, Conexao.conectar(), true, false, true);
			int cdContrato, gnPessoa = 0;
			float vlTotalCARisco = 0;
			while(rsmTemp != null && rsmTemp.next())	{
				//Calcula saldo da carteira ativa para cada contrato
		        cdContrato = rsmTemp.getInt("CD_CONTRATO");
				sql = "SELECT SUM(B.vl_contrato/B.nr_parcelas) AS vl_saldo_ca " +
				   	  "FROM adm_conta_receber A " +
				   	  "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
				   	  "	   JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
				   	  "	   JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
				   	  "WHERE (A.cd_contrato = ?) " +
			          "	AND (A.st_conta <> ?) AND (A.st_conta <> ?)";
				PreparedStatement pstmt = connect.prepareStatement(sql);
				pstmt.setInt(1, cdContrato);
				pstmt.setInt(2, com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA);
				pstmt.setInt(3, com.tivic.manager.adm.ContaReceberServices.ST_PERDA);
				ResultSetMap rsmSaldo = new ResultSetMap(pstmt.executeQuery());
				float vlSaldoCA = 0;
				if(rsmSaldo.next())
					vlSaldoCA = rsmSaldo.getFloat("vl_saldo_ca");
				rsmTemp.setValueToField("VL_SALDO_CA", vlSaldoCA);

				//Acumula o total da carteira ativa em risco
				vlTotalCARisco += rsmTemp.getFloat("VL_CA_RISCO");

				//Cria colunas para armazenar a ATIVIDADE (CNAE/CBO) dependendo do tipo de pessoa
				gnPessoa = rsmTemp.getInt("GN_PESSOA");
				if (gnPessoa == PessoaServices.TP_FISICA) {
					rsmTemp.setValueToField("CD_ATIVIDADE", rsmTemp.getInt("CD_CBO"));
					rsmTemp.setValueToField("NM_ATIVIDADE", rsmTemp.getString("NM_CBO"));
				}
				else {
					rsmTemp.setValueToField("CD_ATIVIDADE", rsmTemp.getInt("CD_CNAE"));
					rsmTemp.setValueToField("NM_ATIVIDADE", rsmTemp.getString("NM_CNAE"));
				}
			}
			rsm = rsmTemp;
			//Seleciona CBO
			sql = "SELECT B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		   	   "    C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
			   "    E2.cd_cbo, E2.nm_cbo, " +
			   "	F.cd_pessoa AS cd_agente, F.nm_pessoa AS nm_agente, " +
			   "	H.cd_cidade, H.nm_cidade, " +
			   "	I.sg_estado, " +
			   "	J.nm_grupo_solidario, " +
			   "    K.id_contrato AS id_convenio, " +
			   "    L.nm_pessoa AS nm_convenio, " +
			   "	SUM(B.vl_contrato/B.nr_parcelas) AS vl_ca_risco " +
			   "FROM adm_conta_receber A " +
			   "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
			   "	JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
			   "	JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
			   "	JOIN grl_pessoa  E ON (B.cd_pessoa  = E.cd_pessoa) " +
			   "	LEFT OUTER JOIN grl_cbo_pessoa_fisica E1 ON (E.cd_pessoa = E1.cd_pessoa AND E1.lg_principal = 1) " +
			   "	LEFT OUTER JOIN grl_cbo E2 ON (E1.cd_cbo = E2.cd_cbo) " +

			   "	LEFT OUTER JOIN grl_pessoa F ON (B.cd_agente = F.cd_pessoa) " +
	           "	LEFT OUTER JOIN grl_pessoa_endereco G ON (E.cd_pessoa = G.cd_pessoa AND G.lg_principal = 1) " +
	           "	LEFT OUTER JOIN grl_cidade H ON (G.cd_cidade = H.cd_cidade) " +
	           "	LEFT OUTER JOIN grl_estado I ON (H.cd_estado = I.cd_estado) " +
			   "	LEFT OUTER JOIN mcr_grupo_solidario J ON (C.cd_grupo_solidario = J.cd_grupo_solidario) " +
	           "	LEFT OUTER JOIN adm_contrato K ON (A.cd_contrato = K.cd_convenio) " +
	           "	LEFT OUTER JOIN grl_pessoa L ON (K.cd_pessoa = L.cd_pessoa) " +
	           "WHERE ((CAST(\'" + Util.formatDateTime(Util.convStringCalendar(dtBase), "MM/dd/yyyy") + "\' AS TIMESTAMP) - A.dt_vencimento) > 30) " +
	           "	AND (A.st_conta <> " + String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_PERDA) + ") ";
			orderBy =
		           "GROUP BY B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
			   	   "    C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
				   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
				   "    E2.cd_cbo, E2.nm_cbo, " +
				   "	F.cd_pessoa, F.nm_pessoa, " +
				   "	H.cd_cidade, H.nm_cidade, " +
				   "	I.sg_estado, " +
				   "	J.nm_grupo_solidario, " +
				   "    K.id_contrato, " +
				   "    L.nm_pessoa";
	        rsmTemp = Search.find(sql, orderBy, criteriosCbo, relations, Conexao.conectar(), true, false, true);
			cdContrato = 0;
			gnPessoa = 0;
			Boolean acheiRegistro = false;
			while (rsmTemp != null && rsmTemp.next()) {
				//Calcula saldo da carteira ativa para cada contrato
		        cdContrato = rsmTemp.getInt("CD_CONTRATO");
				sql = "SELECT SUM(B.vl_contrato/B.nr_parcelas) AS vl_saldo_ca " +
				   	  "FROM adm_conta_receber A " +
				   	  "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
				   	  "	   JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
				   	  "	   JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
				   	  "WHERE (A.cd_contrato = ?) " +
			          "	AND (A.st_conta <> ?) AND (A.st_conta <> ?)";
				PreparedStatement pstmt = connect.prepareStatement(sql);
				pstmt.setInt(1, cdContrato);
				pstmt.setInt(2, com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA);
				pstmt.setInt(3, com.tivic.manager.adm.ContaReceberServices.ST_PERDA);
				ResultSetMap rsmSaldo = new ResultSetMap(pstmt.executeQuery());
				float vlSaldoCA = 0;
				if(rsmSaldo.next())
					vlSaldoCA = rsmSaldo.getFloat("vl_saldo_ca");
				rsmTemp.setValueToField("VL_SALDO_CA", vlSaldoCA);

				//Acumula o total da carteira ativa em risco
				vlTotalCARisco += rsmTemp.getFloat("VL_CA_RISCO");

				//Cria colunas para armazenar a ATIVIDADE (CNAE/CBO) dependendo do tipo de pessoa
				gnPessoa = rsmTemp.getInt("GN_PESSOA");
				if (gnPessoa == PessoaServices.TP_FISICA) {
					rsmTemp.setValueToField("CD_ATIVIDADE", rsmTemp.getInt("CD_CBO"));
					rsmTemp.setValueToField("NM_ATIVIDADE", rsmTemp.getString("NM_CBO"));
				}
				else {
					rsmTemp.setValueToField("CD_ATIVIDADE", rsmTemp.getInt("CD_CNAE"));
					rsmTemp.setValueToField("NM_ATIVIDADE", rsmTemp.getString("NM_CNAE"));
				}

				acheiRegistro = (rsmTemp.getInt("cd_cbo") <= 0)?rsmTemp.locate("CD_CONTRATO", new Integer(rsmTemp.getInt("cd_contrato"))):false;
				if (!acheiRegistro)	{
					//Verifica se o registro está repetido da outra pesquisa
					if (rsm.locate("CD_CONTRATO", new Integer(rsmTemp.getInt("cd_contrato")))) {
						rsm.deleteRow();
						//Subtrai valor da parcela do total da carteira ativa em risco
						vlTotalCARisco -= rsmTemp.getFloat("VL_CA_RISCO");
					}
					HashMap<String,Object> regCbo = new HashMap<String,Object>();
					regCbo.put("CD_CONTRATO", new Integer(rsmTemp.getInt("cd_contrato")));
					regCbo.put("CD_PESSOA", new Integer(rsmTemp.getInt("cd_pessoa")));
					regCbo.put("CD_CONVENIO", new Integer(rsmTemp.getInt("cd_convenio")));
					regCbo.put("CD_EMPRESA", new Integer(rsmTemp.getInt("cd_empresa")));
					regCbo.put("NR_CONTRATO", rsmTemp.getString("nr_contrato"));
					regCbo.put("VL_CONTRATO", new Float(rsmTemp.getFloat("vl_contrato")));
					regCbo.put("NR_PARCELAS", new Integer(rsmTemp.getInt("nr_parcelas")));
					regCbo.put("DT_LIBERACAO", Util.convTimestampToCalendar(rsmTemp.getTimestamp("dt_liberacao")));
					regCbo.put("TP_GARANTIA", new Integer(rsmTemp.getInt("tp_garantia")));
					regCbo.put("TP_EMPRESTIMO", new Integer(rsmTemp.getInt("tp_emprestimo")));
					regCbo.put("NM_PESSOA", rsmTemp.getString("nm_pessoa"));
					regCbo.put("NR_TELEFONE1", rsmTemp.getString("nr_telefone1"));
					regCbo.put("GN_PESSOA", new Integer(rsmTemp.getInt("gn_pessoa")));
					regCbo.put("CD_CBO", new Integer(rsmTemp.getInt("cd_cbo")));
					regCbo.put("NM_CBO", rsmTemp.getString("nm_cbo"));
					regCbo.put("CD_AGENTE", new Integer(rsmTemp.getInt("cd_agente")));
					regCbo.put("NM_AGENTE", rsmTemp.getString("nm_agente"));
					regCbo.put("CD_CIDADE", new Integer(rsmTemp.getInt("cd_cidade")));
					regCbo.put("NM_CIDADE", rsmTemp.getString("nm_cidade"));
					regCbo.put("SG_ESTADO", rsmTemp.getString("sg_estado"));
					regCbo.put("NM_GRUPO_SOLIDARIO", rsmTemp.getString("nm_grupo_solidario"));
					regCbo.put("ID_CONVENIO", rsmTemp.getString("id_convenio"));
					regCbo.put("NM_CONVENIO", rsmTemp.getString("nm_convenio"));
					regCbo.put("VL_SALDO_CA", new Float(rsmTemp.getFloat("vl_saldo_ca")));
					regCbo.put("CD_ATIVIDADE", new Integer(rsmTemp.getInt("cd_atividade")));
					regCbo.put("NM_ATIVIDADE", rsmTemp.getString("nm_atividade"));
					rsm.addRegister(regCbo);
				}
			}

			//Calcula carteira ativa total para todos os contratos
	        ArrayList<ItemComparator> criteriosCA = new ArrayList<ItemComparator>();
	        criteriosCA.add(item1);
	        criteriosCA.add(item2);

			sql = "SELECT SUM(B.vl_contrato/B.nr_parcelas) AS vl_total_ca " +
			   	  "FROM adm_conta_receber A " +
			   	  "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
			   	  "	   JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
			   	  "	   JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
			   	  "WHERE (A.st_conta <> " + String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_PERDA) + ") ";
	        rsmTemp = Search.find(sql, "", criteriosCA, "(1 OR 2)", Conexao.conectar(), true, false, true);
	        if (rsmTemp != null && rsmTemp.next())
	        	rsm.setValueToField("VL_TOTAL_CA", rsmTemp.getFloat("VL_TOTAL_CA"));
        	rsm.setValueToField("VL_TOTAL_CA_RISCO", vlTotalCARisco);
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.findRisco: " +  e);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findMapaPontualidade(ArrayList<ItemComparator> criterios, boolean lgAgrupamento) {
		return findMapaPontualidade(criterios, lgAgrupamento, null);
	}
	@SuppressWarnings("unchecked")
	public static ResultSetMap findMapaPontualidade(ArrayList<ItemComparator> criterios, boolean lgAgrupamento, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			String sql = "SELECT A.dt_vencimento, A.st_conta, ";
			String orderBy = "GROUP BY A.dt_vencimento, A.st_conta ";
			if (lgAgrupamento) {
				sql +=	"F.cd_pessoa AS cd_agente, F.nm_pessoa AS nm_agente, ";
				orderBy += ", F.cd_pessoa, F.nm_pessoa ";
 			}
			sql += "	COUNT(*) AS NR_PARCELAS " +
				   "FROM adm_conta_receber A " +
				   "	JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
				   "	JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
				   "    JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
				   "	JOIN grl_pessoa  E ON (B.cd_pessoa  = E.cd_pessoa) " +
				   "	LEFT OUTER JOIN grl_pessoa F ON (B.cd_agente = F.cd_pessoa) " +
				   "WHERE (A.st_conta = 1) ";
			orderBy += "ORDER BY A.dt_vencimento";
	        ResultSetMap rsm = Search.find(sql, orderBy, criterios, null, Conexao.conectar(), true, false, true);
			//String dtVencimento = null;
			int totalRecebimentos = 0;
			while(rsm != null && rsm.next())	{
				String dtVencimento = Util.formatDateTime(rsm.getGregorianCalendar("DT_VENCIMENTO"), "dd/MM/yyyy");
				ArrayList<ItemComparator> criteriosRecebimentos = (ArrayList<ItemComparator>)criterios.clone();
				ItemComparator item1 = new sol.dao.ItemComparator("A.dt_vencimento", dtVencimento, sol.dao.ItemComparator.EQUAL, Types.TIMESTAMP);
				criteriosRecebimentos.add(item1);
				sql = "SELECT A.dt_vencimento, A.st_conta, ";
				orderBy = "GROUP BY A.dt_vencimento, A.st_conta ";
				if (lgAgrupamento) {
					sql +=	"F.cd_pessoa AS cd_agente, F.nm_pessoa AS nm_agente, ";
					orderBy += ", F.cd_pessoa, F.nm_pessoa ";
	 			}
				sql += "	COUNT(*) AS TOTAL_RECEBIMENTOS " +
					   "FROM adm_conta_receber A " +
					   "	JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
					   "	JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
					   "    JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
					   "	JOIN grl_pessoa  E ON (B.cd_pessoa  = E.cd_pessoa) " +
					   "	LEFT OUTER JOIN grl_pessoa F ON (B.cd_agente = F.cd_pessoa) " +
					   "WHERE (A.st_conta = 1) " +
					   "	AND ((A.dt_recebimento = A.dt_vencimento) OR (CAST(A.vl_recebido AS DECIMAL(7,2)) = CAST(A.vl_conta AS DECIMAL(7,2))))";
				orderBy += "ORDER BY A.dt_vencimento";
		        ResultSetMap rsmRecebimentos = Search.find(sql, orderBy, criteriosRecebimentos, Conexao.conectar(), true);
		        if (rsmRecebimentos != null && rsmRecebimentos.next())
		        	totalRecebimentos = rsmRecebimentos.getInt("TOTAL_RECEBIMENTOS");
				rsm.setValueToField("TOTAL_RECEBIMENTOS", totalRecebimentos);
			}
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.findMapaPontualidade: " +  e);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findPremioPontualidade(ArrayList<ItemComparator> criterios) {
		return findPremioPontualidade(criterios, null);
	}

	public static ResultSetMap findPremioPontualidade(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			String sql = "SELECT A.vl_conta AS VALOR_PARCELA_1, A2.vl_conta AS VALOR_PARCELA_2, " +
		   	   "	B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		   	   "    C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
			   "	H.cd_cidade, H.nm_cidade, " +
			   "	I.sg_estado, " +
			   "    K.id_contrato AS id_convenio, " +
			   "    L.nm_pessoa AS nm_convenio, " +
			   "    (A2.vl_conta - A.vl_conta) AS vl_premio " +
			   "FROM adm_conta_receber A2, adm_conta_receber A " +
			   "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
			   "	JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
			   "	JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
			   "	JOIN grl_pessoa  E ON (B.cd_pessoa  = E.cd_pessoa) " +
	           "	LEFT OUTER JOIN grl_pessoa_endereco G ON (E.cd_pessoa = G.cd_pessoa AND G.lg_principal = 1) " +
	           "	LEFT OUTER JOIN grl_cidade H ON (G.cd_cidade = H.cd_cidade) " +
	           "	LEFT OUTER JOIN grl_estado I ON (H.cd_estado = I.cd_estado) " +
	           "	LEFT OUTER JOIN adm_contrato K ON (A.cd_contrato = K.cd_convenio) " +
	           "	LEFT OUTER JOIN grl_pessoa L ON (K.cd_pessoa = L.cd_pessoa) " +
	           "WHERE (A.cd_contrato = A2.cd_contrato) " +
	           "	AND ((A.nr_parcela = 1) OR (A2.nr_parcela = 2)) " +
	           "    AND (A.vl_conta < A2.vl_conta) ";
			String orderBy =
		           "GROUP BY A.vl_conta ,A2.vl_conta, " +
		           "	B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		           "	C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
		           "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
		           "	H.cd_cidade, H.nm_cidade, " +
		           "	I.sg_estado, " +
		           " 	K.id_contrato, " +
		           "	L.nm_pessoa";
	        ResultSetMap rsm = Search.find(sql, orderBy, criterios, Conexao.conectar(), true);
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.findPremioPontualidade: " +  e);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findAtrasoPrimeira(ArrayList<ItemComparator> criterios, String dtBase, int gnContrato) {
		return findAtrasoPrimeira(criterios, dtBase, gnContrato, null);
	}
	public static ResultSetMap findAtrasoPrimeira(ArrayList<ItemComparator> criterios, String dtBase, int gnContrato, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ItemComparator item1 = new sol.dao.ItemComparator("A.st_conta", String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA), sol.dao.ItemComparator.DIFFERENT, Types.INTEGER);
			ItemComparator item2 = new sol.dao.ItemComparator("A.dt_recebimento", dtBase, sol.dao.ItemComparator.GREATER, Types.TIMESTAMP);
			String relations = "(";
			int numeroLinhas = criterios.size();
			for (int i = 0; criterios != null && i < numeroLinhas; i++) {
				relations += String.valueOf(i+1) + " AND ";
			}
			criterios.add(item1);
			criterios.add(item2);
	        relations += "(" + String.valueOf(numeroLinhas + 1) + " OR " + String.valueOf(numeroLinhas + 2) + "))";

			String sql = "SELECT A.vl_conta, A.dt_vencimento, " +
		   	   "	B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		   	   "    C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
			   "	F.cd_pessoa AS cd_agente, F.nm_pessoa AS nm_agente, " +
			   "	H.cd_cidade, H.nm_cidade, " +
			   "	I.sg_estado, " +
			   "	J.nm_grupo_solidario, " +
			   "    K.id_contrato AS id_convenio, " +
			   "    L.nm_pessoa AS nm_convenio " +
			   "FROM adm_conta_receber A " +
			   "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
			   "	JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
			   "	JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
			   "	JOIN grl_pessoa  E ON (B.cd_pessoa  = E.cd_pessoa) " +

			   "	LEFT OUTER JOIN grl_pessoa F ON (B.cd_agente = F.cd_pessoa) " +
	           "	LEFT OUTER JOIN grl_pessoa_endereco G ON (E.cd_pessoa = G.cd_pessoa AND G.lg_principal = 1) " +
	           "	LEFT OUTER JOIN grl_cidade H ON (G.cd_cidade = H.cd_cidade) " +
	           "	LEFT OUTER JOIN grl_estado I ON (H.cd_estado = I.cd_estado) " +
			   "	LEFT OUTER JOIN mcr_grupo_solidario J ON (C.cd_grupo_solidario = J.cd_grupo_solidario) " +
	           "	LEFT OUTER JOIN adm_contrato K ON (A.cd_contrato = K.cd_convenio) " +
	           "	LEFT OUTER JOIN grl_pessoa L ON (K.cd_pessoa = L.cd_pessoa) " +
	           "WHERE (A.st_conta <> " + String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_PERDA) + ") " +
	           "	AND (A.nr_parcela = 1) ";
	        return Search.find(sql, "", criterios, relations, Conexao.conectar(), true, false, true);
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.findAtrasoPrimeira: " +  e);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findSaldoClientes(ArrayList<ItemComparator> criterios, String dtBase, int gnContrato) {
		return findSaldoClientes(criterios, dtBase, gnContrato, null);
	}
	@SuppressWarnings("unchecked")
	public static ResultSetMap findSaldoClientes(ArrayList<ItemComparator> criterios, String dtBase, int gnContrato, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ResultSetMap rsm = new ResultSetMap();
			String relations = "(";
			int numeroLinhas = criterios.size();
			ArrayList<ItemComparator> criteriosCnae = (ArrayList<ItemComparator>)criterios.clone();
			ArrayList<ItemComparator> criteriosCbo = (ArrayList<ItemComparator>)criterios.clone();
			for (int i = 0; criterios != null && i < numeroLinhas; i++) {
				ItemComparator itemCnae = (ItemComparator)((ItemComparator)criterios.get(i)).clone();
				ItemComparator itemCbo = (ItemComparator)((ItemComparator)criterios.get(i)).clone();
				if (itemCnae.getColumn().equalsIgnoreCase("E2.CD_ATIVIDADE")) {
					itemCnae.setColumn("E2.cd_cnae");
					itemCbo.setColumn("E2.cd_cbo");
					criteriosCnae.add(itemCnae);
					criteriosCbo.add(itemCbo);
				}
				relations += String.valueOf(i+1) + " AND ";
			}
			ItemComparator item = ItemComparator.getItemComparatorByColumn(criterios, "E2.CD_ATIVIDADE");
			if (item != null) {
				criteriosCnae.remove(item);
				criteriosCbo.remove(item);
			}

			ItemComparator item1 = new sol.dao.ItemComparator("A.st_conta", String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA), sol.dao.ItemComparator.DIFFERENT, Types.INTEGER);
			ItemComparator item2 = new sol.dao.ItemComparator("A.dt_recebimento", dtBase, sol.dao.ItemComparator.GREATER, Types.TIMESTAMP);
			criteriosCnae.add(item1);
			criteriosCnae.add(item2);
			criteriosCbo.add(item1);
			criteriosCbo.add(item2);
			numeroLinhas = criterios.size();
	        relations += "(" + String.valueOf(numeroLinhas + 1) + " OR " + String.valueOf(numeroLinhas + 2) + "))";

			//Seleciona CNAE
			String sql = "SELECT B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		   	   "    C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
			   "    E2.cd_cnae, E2.nm_cnae, " +
			   "	F.cd_pessoa AS cd_agente, F.nm_pessoa AS nm_agente, " +
			   "	H.cd_cidade, H.nm_cidade, " +
			   "	I.sg_estado, " +
			   "	J.nm_grupo_solidario, " +
			   "    K.id_contrato AS id_convenio, " +
			   "    L.nm_pessoa AS nm_convenio " +
			   "FROM adm_conta_receber A " +
			   "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
			   "	JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
			   "	JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
			   "	JOIN grl_pessoa  E ON (B.cd_pessoa  = E.cd_pessoa) " +
			   "	LEFT OUTER JOIN grl_cnae_pessoa_juridica E1 ON (E.cd_pessoa = E1.cd_pessoa AND E1.lg_principal = 1) " +
			   "	LEFT OUTER JOIN grl_cnae E2 ON (E1.cd_cnae = E2.cd_cnae) " +

			   "	LEFT OUTER JOIN grl_pessoa F ON (B.cd_agente = F.cd_pessoa) " +
	           "	LEFT OUTER JOIN grl_pessoa_endereco G ON (E.cd_pessoa = G.cd_pessoa AND G.lg_principal = 1) " +
	           "	LEFT OUTER JOIN grl_cidade H ON (G.cd_cidade = H.cd_cidade) " +
	           "	LEFT OUTER JOIN grl_estado I ON (H.cd_estado = I.cd_estado) " +
			   "	LEFT OUTER JOIN mcr_grupo_solidario J ON (C.cd_grupo_solidario = J.cd_grupo_solidario) " +
	           "	LEFT OUTER JOIN adm_contrato K ON (A.cd_contrato = K.cd_convenio) " +
	           "	LEFT OUTER JOIN grl_pessoa L ON (K.cd_pessoa = L.cd_pessoa) " +
	           "WHERE (1 = 1) ";
			String orderBy =
	           "GROUP BY B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		   	   "    C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
			   "    E2.cd_cnae, E2.nm_cnae, " +
			   "	F.cd_pessoa, F.nm_pessoa, " +
			   "	H.cd_cidade, H.nm_cidade, " +
			   "	I.sg_estado, " +
			   "	J.nm_grupo_solidario, " +
			   "    K.id_contrato, " +
			   "    L.nm_pessoa";
	        ResultSetMap rsmTemp = Search.find(sql, orderBy, criteriosCnae, relations, Conexao.conectar(), true, false, true);
			int cdContrato, gnPessoa = 0;
			while(rsmTemp != null && rsmTemp.next())	{
				//Calcula saldo da carteira ativa/vigente para cada contrato
		        cdContrato = rsmTemp.getInt("CD_CONTRATO");
				sql = "SELECT SUM(B.vl_contrato/B.nr_parcelas) AS vl_saldo_ca, " +
					  "		  SUM(A.vl_conta) AS vl_saldo_cv " +
				   	  "FROM adm_conta_receber A " +
				   	  "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
				   	  "	   JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
				   	  "	   JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
				   	  "WHERE (A.cd_contrato = ?) " +
			          "	AND (A.st_conta <> ?) AND (A.st_conta <> ?)";
				PreparedStatement pstmt = connect.prepareStatement(sql);
				pstmt.setInt(1, cdContrato);
				pstmt.setInt(2, com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA);
				pstmt.setInt(3, com.tivic.manager.adm.ContaReceberServices.ST_PERDA);
				ResultSetMap rsmSaldo = new ResultSetMap(pstmt.executeQuery());
				float vlSaldoCA = 0, vlSaldoCV = 0;
				if(rsmSaldo.next())	{
					vlSaldoCA = rsmSaldo.getFloat("vl_saldo_ca");
					vlSaldoCV = rsmSaldo.getFloat("vl_saldo_cv");
				}
				rsmTemp.setValueToField("VL_SALDO_CA", vlSaldoCA);
				rsmTemp.setValueToField("VL_SALDO_CV", vlSaldoCV);

				//Cria colunas para armazenar a ATIVIDADE (CNAE/CBO) dependendo do tipo de pessoa
				gnPessoa = rsmTemp.getInt("GN_PESSOA");
				if (gnPessoa == PessoaServices.TP_FISICA) {
					rsmTemp.setValueToField("CD_ATIVIDADE", rsmTemp.getInt("CD_CBO"));
					rsmTemp.setValueToField("NM_ATIVIDADE", rsmTemp.getString("NM_CBO"));
				}
				else {
					rsmTemp.setValueToField("CD_ATIVIDADE", rsmTemp.getInt("CD_CNAE"));
					rsmTemp.setValueToField("NM_ATIVIDADE", rsmTemp.getString("NM_CNAE"));
				}
			}
			rsm = rsmTemp;
			//Seleciona CBO
			sql = "SELECT B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		   	   "    C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
			   "    E2.cd_cbo, E2.nm_cbo, " +
			   "	F.cd_pessoa AS cd_agente, F.nm_pessoa AS nm_agente, " +
			   "	H.cd_cidade, H.nm_cidade, " +
			   "	I.sg_estado, " +
			   "	J.nm_grupo_solidario, " +
			   "    K.id_contrato AS id_convenio, " +
			   "    L.nm_pessoa AS nm_convenio " +
			   "FROM adm_conta_receber A " +
			   "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
			   "	JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
			   "	JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
			   "	JOIN grl_pessoa  E ON (B.cd_pessoa  = E.cd_pessoa) " +
			   "	LEFT OUTER JOIN grl_cbo_pessoa_fisica E1 ON (E.cd_pessoa = E1.cd_pessoa AND E1.lg_principal = 1) " +
			   "	LEFT OUTER JOIN grl_cbo E2 ON (E1.cd_cbo = E2.cd_cbo) " +

			   "	LEFT OUTER JOIN grl_pessoa F ON (B.cd_agente = F.cd_pessoa) " +
	           "	LEFT OUTER JOIN grl_pessoa_endereco G ON (E.cd_pessoa = G.cd_pessoa AND G.lg_principal = 1) " +
	           "	LEFT OUTER JOIN grl_cidade H ON (G.cd_cidade = H.cd_cidade) " +
	           "	LEFT OUTER JOIN grl_estado I ON (H.cd_estado = I.cd_estado) " +
			   "	LEFT OUTER JOIN mcr_grupo_solidario J ON (C.cd_grupo_solidario = J.cd_grupo_solidario) " +
	           "	LEFT OUTER JOIN adm_contrato K ON (A.cd_contrato = K.cd_convenio) " +
	           "	LEFT OUTER JOIN grl_pessoa L ON (K.cd_pessoa = L.cd_pessoa) " +
	           "WHERE (1 = 1) ";
			orderBy =
	           "GROUP BY B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		   	   "    C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
			   "    E2.cd_cbo, E2.nm_cbo, " +
			   "	F.cd_pessoa, F.nm_pessoa, " +
			   "	H.cd_cidade, H.nm_cidade, " +
			   "	I.sg_estado, " +
			   "	J.nm_grupo_solidario, " +
			   "    K.id_contrato, " +
			   "    L.nm_pessoa";
	        rsmTemp = Search.find(sql, orderBy, criteriosCbo, relations, Conexao.conectar(), true, false, true);
			cdContrato = 0;
			gnPessoa = 0;
			Boolean acheiRegistro = false;
			while (rsmTemp != null && rsmTemp.next()) {
				//Calcula saldo da carteira ativa/vigente para cada contrato
		        cdContrato = rsmTemp.getInt("CD_CONTRATO");
				sql = "SELECT SUM(B.vl_contrato/B.nr_parcelas) AS vl_saldo_ca, " +
					  "		  SUM(A.vl_conta) AS vl_saldo_cv " +
				   	  "FROM adm_conta_receber A " +
				   	  "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
				   	  "	   JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
				   	  "	   JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
				   	  "WHERE (A.cd_contrato = ?) " +
			          "	AND (A.st_conta <> ?) AND (A.st_conta <> ?)";
				PreparedStatement pstmt = connect.prepareStatement(sql);
				pstmt.setInt(1, cdContrato);
				pstmt.setInt(2, com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA);
				pstmt.setInt(3, com.tivic.manager.adm.ContaReceberServices.ST_PERDA);
				ResultSetMap rsmSaldo = new ResultSetMap(pstmt.executeQuery());
				float vlSaldoCA = 0, vlSaldoCV = 0;
				if(rsmSaldo.next())	{
					vlSaldoCA = rsmSaldo.getFloat("vl_saldo_ca");
					vlSaldoCV = rsmSaldo.getFloat("vl_saldo_cv");
				}
				rsmTemp.setValueToField("VL_SALDO_CA", vlSaldoCA);
				rsmTemp.setValueToField("VL_SALDO_CV", vlSaldoCV);

				//Cria colunas para armazenar a ATIVIDADE (CNAE/CBO) dependendo do tipo de pessoa
				gnPessoa = rsmTemp.getInt("GN_PESSOA");
				if (gnPessoa == PessoaServices.TP_FISICA) {
					rsmTemp.setValueToField("CD_ATIVIDADE", rsmTemp.getInt("CD_CBO"));
					rsmTemp.setValueToField("NM_ATIVIDADE", rsmTemp.getString("NM_CBO"));
				}
				else {
					rsmTemp.setValueToField("CD_ATIVIDADE", rsmTemp.getInt("CD_CNAE"));
					rsmTemp.setValueToField("NM_ATIVIDADE", rsmTemp.getString("NM_CNAE"));
				}
				acheiRegistro = (rsmTemp.getInt("cd_cbo") <= 0)?rsmTemp.locate("CD_CONTRATO", new Integer(rsmTemp.getInt("cd_contrato"))):false;
				if (!acheiRegistro)	{
					if (rsm.locate("CD_CONTRATO", new Integer(rsmTemp.getInt("cd_contrato"))))
						rsm.deleteRow();

					HashMap<String,Object> regCbo = new HashMap<String,Object>();
					regCbo.put("CD_CONTRATO", new Integer(rsmTemp.getInt("cd_contrato")));
					regCbo.put("CD_PESSOA", new Integer(rsmTemp.getInt("cd_pessoa")));
					regCbo.put("CD_CONVENIO", new Integer(rsmTemp.getInt("cd_convenio")));
					regCbo.put("CD_EMPRESA", new Integer(rsmTemp.getInt("cd_empresa")));
					regCbo.put("NR_CONTRATO", rsmTemp.getString("nr_contrato"));
					regCbo.put("VL_CONTRATO", new Float(rsmTemp.getFloat("vl_contrato")));
					regCbo.put("NR_PARCELAS", new Integer(rsmTemp.getInt("nr_parcelas")));
					regCbo.put("DT_LIBERACAO", Util.convTimestampToCalendar(rsmTemp.getTimestamp("dt_liberacao")));
					regCbo.put("TP_GARANTIA", new Integer(rsmTemp.getInt("tp_garantia")));
					regCbo.put("TP_EMPRESTIMO", new Integer(rsmTemp.getInt("tp_emprestimo")));
					regCbo.put("NM_PESSOA", rsmTemp.getString("nm_pessoa"));
					regCbo.put("NR_TELEFONE1", rsmTemp.getString("nr_telefone1"));
					regCbo.put("GN_PESSOA", new Integer(rsmTemp.getInt("gn_pessoa")));
					regCbo.put("CD_CBO", new Integer(rsmTemp.getInt("cd_cbo")));
					regCbo.put("NM_CBO", rsmTemp.getString("nm_cbo"));
					regCbo.put("CD_AGENTE", new Integer(rsmTemp.getInt("cd_agente")));
					regCbo.put("NM_AGENTE", rsmTemp.getString("nm_agente"));
					regCbo.put("CD_CIDADE", new Integer(rsmTemp.getInt("cd_cidade")));
					regCbo.put("NM_CIDADE", rsmTemp.getString("nm_cidade"));
					regCbo.put("SG_ESTADO", rsmTemp.getString("sg_estado"));
					regCbo.put("NM_GRUPO_SOLIDARIO", rsmTemp.getString("nm_grupo_solidario"));
					regCbo.put("ID_CONVENIO", rsmTemp.getString("id_convenio"));
					regCbo.put("NM_CONVENIO", rsmTemp.getString("nm_convenio"));
					regCbo.put("VL_SALDO_CA", new Float(rsmTemp.getFloat("vl_saldo_ca")));
					regCbo.put("VL_SALDO_CV", new Float(rsmTemp.getFloat("vl_saldo_cv")));
					regCbo.put("CD_ATIVIDADE", new Integer(rsmTemp.getInt("cd_atividade")));
					regCbo.put("NM_ATIVIDADE", rsmTemp.getString("nm_atividade"));
					rsm.addRegister(regCbo);
				}
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.findSaldoClientes: " +  e);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findQuitados(ArrayList<ItemComparator> criterios) {
		return findQuitados(criterios, null);
	}

	public static ResultSetMap findQuitados(ArrayList<ItemComparator> criterios, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			String sql = "SELECT A.dt_vencimento, A.dt_recebimento, " +
			   "	B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, B.pr_juros, " +
		   	   "    C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
			   "	F.cd_pessoa AS cd_agente, F.nm_pessoa AS nm_agente, " +
			   "    (B.vl_parcelas * B.nr_parcelas) AS VL_TOTAL_CONTRATADO " +
			   "FROM adm_conta_receber A " +
			   "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
			   "	JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
			   "	JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
			   "	JOIN grl_pessoa  E ON (B.cd_pessoa  = E.cd_pessoa) " +
			   "	LEFT OUTER JOIN grl_pessoa F ON (B.cd_agente = F.cd_pessoa) " +
	           "WHERE (A.nr_parcela = B.nr_parcelas) " +
	           "	AND (A.st_conta = " + String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA) + ") ";
			ResultSetMap rsm = Search.find(sql, criterios, Conexao.conectar());
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.findQuitados: " +  e);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findRenovacoes(ArrayList<ItemComparator> criterios, int gnContrato) {
		return findRenovacoes(criterios, gnContrato, null);
	}
	public static ResultSetMap findRenovacoes(ArrayList<ItemComparator> criterios, int gnContrato, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			String sql = "SELECT A.vl_conta, A.dt_vencimento, A.nr_parcela, " +
		   	   "	B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		   	   "    C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.nr_celular, E.gn_pessoa, " +
			   "	F.cd_pessoa AS cd_agente, F.nm_pessoa AS nm_agente, " +
			   "	H.cd_cidade, H.nm_cidade, " +
			   "	I.sg_estado " +
			   "FROM adm_conta_receber A " +
			   "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
			   "	JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
			   "	JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
			   "	JOIN grl_pessoa  E ON (B.cd_pessoa  = E.cd_pessoa) " +

			   "	LEFT OUTER JOIN grl_pessoa F ON (B.cd_agente = F.cd_pessoa) " +
	           "	LEFT OUTER JOIN grl_pessoa_endereco G ON (E.cd_pessoa = G.cd_pessoa AND G.lg_principal = 1) " +
	           "	LEFT OUTER JOIN grl_cidade H ON (G.cd_cidade = H.cd_cidade) " +
	           "	LEFT OUTER JOIN grl_estado I ON (H.cd_estado = I.cd_estado) " +
	           "WHERE (A.st_conta = " + String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_EM_ABERTO) + ") " +
	           "	AND 1 = " +
	           "	(SELECT COUNT(A1.cd_contrato) " +
	           "		FROM adm_conta_receber A1 " +
               "		WHERE (A1.cd_contrato = A.cd_contrato) " +
               "			AND (A1.st_conta = " + String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_EM_ABERTO) + ")) ";
	        return Search.find(sql, criterios, Conexao.conectar());
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findLimiteCredito(ArrayList<ItemComparator> criterios, String dtBase, int gnContrato, int Ciclos) {
		return findLimiteCredito(criterios, dtBase, gnContrato, Ciclos, null);
	}
	@SuppressWarnings("unchecked")
	public static ResultSetMap findLimiteCredito(ArrayList<ItemComparator> criterios, String dtBase, int gnContrato, int Ciclos, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull)
				connect = Conexao.conectar();
			ResultSetMap rsm = new ResultSetMap();
			String relations = "(";
			int numeroLinhas = criterios.size();
			ArrayList<ItemComparator> criteriosCnae = (ArrayList<ItemComparator>)criterios.clone();
			ArrayList<ItemComparator> criteriosCbo = (ArrayList<ItemComparator>)criterios.clone();
			for (int i = 0; criterios != null && i < numeroLinhas; i++) {
				ItemComparator itemCnae = (ItemComparator)((ItemComparator)criterios.get(i)).clone();
				ItemComparator itemCbo = (ItemComparator)((ItemComparator)criterios.get(i)).clone();
				if (itemCnae.getColumn().equalsIgnoreCase("E2.CD_ATIVIDADE")) {
					itemCnae.setColumn("E2.cd_cnae");
					itemCbo.setColumn("E2.cd_cbo");
					criteriosCnae.add(itemCnae);
					criteriosCbo.add(itemCbo);
				}
				relations += String.valueOf(i+1) + " AND ";
			}
			ItemComparator item = ItemComparator.getItemComparatorByColumn(criterios, "E2.CD_ATIVIDADE");
			if (item != null) {
				criteriosCnae.remove(item);
				criteriosCbo.remove(item);
			}

			ItemComparator item1 = new sol.dao.ItemComparator("A.st_conta", String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA), sol.dao.ItemComparator.DIFFERENT, Types.INTEGER);
			ItemComparator item2 = new sol.dao.ItemComparator("A.dt_recebimento", dtBase, sol.dao.ItemComparator.GREATER, Types.TIMESTAMP);
			criteriosCnae.add(item1);
			criteriosCnae.add(item2);
			criteriosCbo.add(item1);
			criteriosCbo.add(item2);
			numeroLinhas = criterios.size();
	        relations += "(" + String.valueOf(numeroLinhas + 1) + " OR " + String.valueOf(numeroLinhas + 2) + "))";

			//Seleciona CNAE
			String sql = "SELECT B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		   	   "    C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
			   "    E2.cd_cnae, E2.nm_cnae, " +
			   " 	F.nm_classificacao " +
			   "FROM adm_conta_receber A " +
			   "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
			   "	JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
			   "	JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
			   "	JOIN grl_pessoa  E ON (B.cd_pessoa  = E.cd_pessoa) " +
			   "	LEFT OUTER JOIN grl_cnae_pessoa_juridica E1 ON (E.cd_pessoa = E1.cd_pessoa AND E1.lg_principal = 1) " +
			   "	LEFT OUTER JOIN grl_cnae E2 ON (E1.cd_cnae = E2.cd_cnae) " +
			   "	LEFT OUTER JOIN adm_classificacao F ON (E.cd_classificacao = F.cd_classificacao) " +
	           "WHERE (A.st_conta <> " + String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA) + ") " +
	           "	AND (A.st_conta <> " + String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_PERDA) + ") ";
			String orderBy =
	           "GROUP BY B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		   	   "    C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
			   "    E2.cd_cnae, E2.nm_cnae, " +
			   "	F.nm_classificacao " +
			   "ORDER BY B.cd_contrato, B.cd_pessoa ";
	        ResultSetMap rsmTemp = Search.find(sql, orderBy, criteriosCnae, relations, Conexao.conectar(), true, false, true);
			int cdPessoa, gnPessoa = 0;
			while(rsmTemp != null && rsmTemp.next())	{
				//Calcula número de ciclos de cada cliente
				cdPessoa = rsmTemp.getInt("CD_PESSOA");
				sql =
					"SELECT COUNT(*) AS NR_CICLOS " +
					"	FROM adm_contrato " +
					"WHERE (cd_pessoa = ?) AND (gn_contrato = ?) " +
					"GROUP BY cd_pessoa";
				PreparedStatement pstmt = connect.prepareStatement(sql);
				pstmt.setInt(1, cdPessoa);
				pstmt.setInt(2, gnContrato);
				ResultSetMap rsmCiclos = new ResultSetMap(pstmt.executeQuery());
				int nrCiclos = 0;
				if(rsmCiclos.next())
					nrCiclos = rsmCiclos.getInt("NR_CICLOS");
				rsmTemp.setValueToField("NR_CICLOS", nrCiclos);
				// Determina tipo de Limite de Crédito do clientes
/*				String nmClasse = "N";
				GregorianCalendar dataBase = Util.convStringCalendar(dtBase);
				if (nrCiclos >= Ciclos) {
					cdContrato = rsmTemp.getInt("CD_CONTRATO");
					sql =
						"SELECT A.nr_parcela, A.vl_conta, A.vl_recebido, A.dt_vencimento, A.dt_recebimento, A.st_conta " +
						"	FROM adm_conta_receber A " +
						"WHERE (A.cd_contrato = ?) " +
						"ORDER BY A.nr_parcela ";
					pstmt = connect.prepareStatement(sql);
					pstmt.setInt(1, cdContrato);
					ResultSetMap rsmClasse = new ResultSetMap(pstmt.executeQuery());
					int lgContinua = 1;
					while(rsmClasse != null && rsmClasse.next()) {
						if (lgContinua == 1) {
							System.out.println("cdContrato = "+cdContrato);
							if (rsmClasse.getInt("ST_CONTA") == com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA) {
								if (rsmClasse.getGregorianCalendar("DT_RECEBIMENTO").getTimeInMillis() <= rsmClasse.getGregorianCalendar("DT_VENCIMENTO").getTimeInMillis()) {
									if (rsmClasse.getInt("NR_PARCELA") == 1)
										nmClasse = "B";
								}
								else {
									if (Math.abs(rsmClasse.getFloat("VL_RECEBIDO") - rsmClasse.getFloat("VL_CONTA")) < 0.05) {
										if (rsmClasse.getInt("NR_PARCELA") == 1)
											nmClasse = "B";
									}
									else {
										lgContinua = 0;
										nmClasse = "N";
									}
								}
							}
							else {
								if ((rsmClasse.getInt("NR_PARCELA")-1) >= (rsmTemp.getInt("NR_PARCELAS")/2)) {
									if (rsmClasse.getGregorianCalendar("DT_VENCIMENTO").getTimeInMillis() >= dataBase.getTimeInMillis())
										if (nmClasse == "B")
											nmClasse = "A";
								}
							    lgContinua = 0;
							}
							System.out.println("Classe = " + nmClasse);
							if ((rsmClasse.getInt("NR_PARCELA")-1) >= (rsmTemp.getInt("NR_PARCELAS")/2))
								lgContinua = 0;

						}
						else
							break;
					}
					rsmTemp.setValueToField("CLASSE", nmClasse);
				}*/

				//Cria colunas para armazenar a ATIVIDADE (CNAE/CBO) dependendo do tipo de pessoa
				rsmTemp.setValueToField("CD_ATIVIDADE", 0);
				rsmTemp.setValueToField("NM_ATIVIDADE", null);
				gnPessoa = rsmTemp.getInt("GN_PESSOA");
				if (gnPessoa == PessoaServices.TP_JURIDICA) {
					rsmTemp.setValueToField("CD_ATIVIDADE", rsmTemp.getInt("CD_CNAE"));
					rsmTemp.setValueToField("NM_ATIVIDADE", rsmTemp.getString("NM_CNAE"));
				}
			}
			rsm = rsmTemp;
			System.out.println("rsm/CNAE = "+rsm.toString());
			//Seleciona CBO
			sql = "SELECT B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		   	   "    C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
			   "    E2.cd_cbo, E2.nm_cbo " +
			   "FROM adm_conta_receber A " +
			   "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
			   "	JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
			   "	JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
			   "	JOIN grl_pessoa  E ON (B.cd_pessoa  = E.cd_pessoa) " +
			   "	LEFT OUTER JOIN grl_cbo_pessoa_fisica E1 ON (E.cd_pessoa = E1.cd_pessoa AND E1.lg_principal = 1) " +
			   "	LEFT OUTER JOIN grl_cbo E2 ON (E1.cd_cbo = E2.cd_cbo) " +
	           "WHERE (A.st_conta <> " + String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA) + ") " +
	           "	AND (A.st_conta <> " + String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_PERDA) + ") ";
			orderBy =
	           "GROUP BY B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		   	   "    C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
			   "    E2.cd_cbo, E2.nm_cbo " +
			   "ORDER BY B.cd_contrato, B.cd_pessoa ";
	        rsmTemp = Search.find(sql, orderBy, criteriosCbo, relations, Conexao.conectar(), true, false, true);
			gnPessoa = 0;
			rsm.beforeFirst();
			while (rsmTemp != null && rsmTemp.next() && rsm != null && rsm.next()) {
				//Cria colunas para armazenar a ATIVIDADE (CNAE/CBO) dependendo do tipo de pessoa
				gnPessoa = rsmTemp.getInt("GN_PESSOA");
				if (gnPessoa == PessoaServices.TP_FISICA) {
					rsm.setValueToField("CD_ATIVIDADE", rsmTemp.getInt("CD_CBO"));
					rsm.setValueToField("NM_ATIVIDADE", rsmTemp.getString("NM_CBO"));
				}
			}
/*			rsm.beforeFirst();
			while (rsm != null && rsm.next()) {
				System.out.println("ENTREI AQUI - Opção/Classe: " + Opcao + "/" + rsm.getString("CLASSE"));
				if (!Opcao.equals("T")) {
					if (!Opcao.equals(rsm.getString("CLASSE"))) {
						rsm.delete();
					}
				}
			}*/
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.LimiteCredito: " +  e);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap findCarteira(ArrayList<ItemComparator> criterios, GregorianCalendar dtBase, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int gnContrato) {
		return findCarteira(criterios, dtBase, dtInicial, dtFinal, gnContrato, null);
	}

	public static ResultSetMap findCarteira(ArrayList<ItemComparator> criterios, GregorianCalendar dtBase, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int gnContrato, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			//variáveis que serão utilizadas para os cálculos
			//int diaPeriodo = dtBase.get(Calendar.DAY_OF_MONTH);
			// int mesPeriodo = dtBase.get(Calendar.MONTH);
			// int anoPeriodo = dtBase.get(Calendar.YEAR);
			// int mesAnterior = mesPeriodo == 0 ? 11 : mesPeriodo-1;
			// int anoAnterior = mesPeriodo == 0 ? anoPeriodo-1 : anoPeriodo;
			// GregorianCalendar dtInicialAnterior = new GregorianCalendar(anoAnterior, mesAnterior, 1);
			// GregorianCalendar dtFinalAnterior = com.tivic.manager.util.Util.getUltimoDiaMes(mesAnterior, anoAnterior);

			if (isConnectionNull)
				connect = Conexao.conectar();
/*
			String sql = "SELECT B.cd_contrato, B.cd_pessoa, B.cd_convenio, B.cd_empresa, B.nr_contrato, B.vl_contrato, B.nr_parcelas, " +
		   	   "    C.cd_grupo_solidario, C.dt_liberacao, C.tp_garantia, C.tp_emprestimo, " +
			   "	E.nm_pessoa, E.nr_telefone1, E.gn_pessoa, " +
			   "	F.cd_pessoa AS cd_agente, F.nm_pessoa AS nm_agente, " +
			   "	H.cd_cidade, H.nm_cidade, " +
			   "	I.sg_estado, " +
			   "	J.nm_grupo_solidario, " +
			   "    K.id_contrato AS id_convenio, " +
			   "    L.nm_pessoa AS nm_convenio " +
			   "FROM adm_contrato B " +
			   "	JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
			   "	JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
			   "	JOIN grl_pessoa  E ON (B.cd_pessoa  = E.cd_pessoa) " +
			   "	LEFT OUTER JOIN grl_pessoa F ON (B.cd_agente = F.cd_pessoa) " +
	           "	LEFT OUTER JOIN grl_pessoa_endereco G ON (E.cd_pessoa = G.cd_pessoa AND G.lg_principal = 1) " +
	           "	LEFT OUTER JOIN grl_cidade H ON (G.cd_cidade = H.cd_cidade) " +
	           "	LEFT OUTER JOIN grl_estado I ON (H.cd_estado = I.cd_estado) " +
			   "	LEFT OUTER JOIN mcr_grupo_solidario J ON (C.cd_grupo_solidario = J.cd_grupo_solidario) " +
	           "	LEFT OUTER JOIN adm_contrato K ON (B.cd_contrato = K.cd_convenio) " +
	           "	LEFT OUTER JOIN grl_pessoa L ON (K.cd_pessoa = L.cd_pessoa) " +
	           "WHERE (B.dt_liberacao <= \'" + Util.formatDateTime(dtBase, "MM/dd/yyyy") + "\')";
	           "WHERE (B.dt_liberacao >= \'" + Util.formatDateTime(dtInicialAnterior, "MM/dd/yyyy") +
	           "	\' AND B.dt_liberacao <= \'" + Util.formatDateTime(dtFinal, "MM/dd/yyyy") + "\')";
*/
/*	           "	AND B.cd_pessoa IN " +
	           "		(SELECT DISTINCT M.cd_pessoa " +
	           "	 	 FROM adm_conta_receber M " +
	           "	 	 WHERE (M.cd_pessoa IS NOT NULL) AND ((M.st_conta <> " + String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA) + ")" +
	           "         	OR (M.dt_recebimento > \'" + Util.formatDateTime(dtBase, "MM/dd/yyyy") + "\')))";
*/
			/*
	        String orderBy = "";
			ResultSetMap rsmContrato = Search.find(sql, orderBy, criterios, Conexao.conectar(), true);
	        int cdContrato = 0, iAtivos = 0, iAtraso = 0,
	            iNaoPagosMesAnterior = 0, iPagosDiaMesAnterior = 0,
	            iPagosAte30MesAnterior = 0, iPagosMais30MesAnterior = 0,
	            iRenovacoesMesAnterior = 0, iNovosMesAnterior = 0,
	            iCreditosGeral = 0, iPrazoMesBase = 0,
	            iRenovacoesMesBase = 0, iNovosMesBase = 0,
	            iRenovacoesGeral = 0; //, iQuitadosGeral;
	        float totalCA = 0, totalRenovacoesMesAnterior = 0, totalNovosMesAnterior = 0,
	        	  totalGeralMesAnterior = 0, totalGeral = 0, totalMesBase = 0,
	        	  totalRenovacoesMesBase = 0, totalNovosMesBase = 0; //totalGeralMesBase = 0;
			int cdTipoOperacaoRenovacao = ParametroServices.getValorOfParametroAsInteger("CD_TIPO_OPERACAO_RENOVACAO", 0);

			while(rsmContrato != null && rsmContrato.next()) {
		        cdContrato = rsmContrato.getInt("CD_CONTRATO");
				sql = "SELECT * " +
				   	  "FROM adm_conta_receber A " +
				   	  "    JOIN adm_contrato B ON (A.cd_contrato = B.cd_contrato) " +
				   	  "	   JOIN mcr_contrato C ON (B.cd_contrato = C.cd_contrato) " +
				   	  "	   JOIN grl_empresa D ON (B.cd_empresa = D.cd_empresa) " +
				   	  "WHERE (A.cd_contrato = ?) " +
			          "	AND (A.st_conta <> ?)";
				PreparedStatement pstmt = connect.prepareStatement(sql);
				pstmt.setInt(1, cdContrato);
				pstmt.setInt(2, com.tivic.manager.adm.ContaReceberServices.ST_PERDA);
				ResultSetMap rsmContaReceber = new ResultSetMap(pstmt.executeQuery());
				GregorianCalendar dtLiberacao = rsmContrato.getGregorianCalendar("DT_LIBERACAO");
				int cdPessoa = 0, cdPessoaAnterior = 0;
				while(rsmContaReceber != null && rsmContaReceber.next()) {
					cdPessoa = rsmContaReceber.getInt("CD_PESSOA");
					GregorianCalendar dtVencimento = rsmContaReceber.getGregorianCalendar("DT_VENCIMENTO");
					GregorianCalendar dtRecebimento = rsmContaReceber.getGregorianCalendar("DT_RECEBIMENTO");
					float vlConta = rsmContaReceber.getFloat("VL_CONTA");
					float vlRecebido = rsmContaReceber.getFloat("VL_RECEBIDO");
					//Parcelas a recebider no período
					if ((rsmContaReceber.getInt("ST_CONTA") != com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA) || (rsmContaReceber.getGregorianCalendar("DT_RECEBIMENTO").after(dtBase)) &&
					    (dtLiberacao.compareTo(dtInicial) >= 0 && dtLiberacao.compareTo(dtFinal) <= 0)) {
						if (cdPessoa != cdPessoaAnterior) {
							iAtivos ++;
							totalCA += rsmContrato.getFloat("VL_CONTRATO") / rsmContrato.getInt("NR_PARCELAS");
							//Atraso
							if (dtVencimento.before(dtBase)) {
								iAtraso ++;
							}
							cdPessoaAnterior = rsmContaReceber.getInt("CD_PESSOA");
						}
						if ((dtVencimento.compareTo(dtInicialAnterior) >= 0 && dtVencimento.compareTo(dtFinalAnterior) <= 0)) {
							iNaoPagosMesAnterior ++;
						}
						if (dtBase.compareTo(dtVencimento) >= 30) {
						}
					}
					//Parcelas recebidas no período
					if (rsmContaReceber.getInt("ST_CONTA") == com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA) {
						if ((dtRecebimento.compareTo(dtInicialAnterior) >= 0 && dtRecebimento.compareTo(dtFinalAnterior) <= 0)) {
							if (dtRecebimento.compareTo(dtVencimento) <= 0 || vlRecebido <= vlConta)
								iPagosDiaMesAnterior ++;
							if (dtRecebimento.compareTo(dtVencimento) <= 30)
								iPagosAte30MesAnterior ++;
							if (dtRecebimento.compareTo(dtVencimento) > 30)
								iPagosMais30MesAnterior ++;
						}

					}
				}
				//Créditos novos, renovados no mês anterior
				if ((dtLiberacao.compareTo(dtInicialAnterior) >= 0 && dtLiberacao.compareTo(dtFinalAnterior) <= 0)) {
					if (rsmContrato.getInt("CD_TIPO_OPERACAO") == cdTipoOperacaoRenovacao) {
						iRenovacoesMesAnterior ++;
						totalRenovacoesMesAnterior += rsmContrato.getFloat("VL_CONTRATO");
					}
					else {
						iNovosMesAnterior ++;
						totalNovosMesAnterior += rsmContrato.getFloat("VL_CONTRATO");
					}
					totalGeralMesAnterior += rsmContrato.getFloat("VL_CONTRATO");
				}
				//Créditos geral
				totalGeral += rsmContrato.getFloat("VL_CONTRATO");
				iCreditosGeral ++;
				//Créditos no mês
				if (dtLiberacao.get(Calendar.MONTH) == dtBase.get(Calendar.MONTH) && dtLiberacao.get(Calendar.YEAR) == dtBase.get(Calendar.YEAR)) {
					if (rsmContrato.getInt("CD_TIPO_OPERACAO") == cdTipoOperacaoRenovacao) {
						iRenovacoesMesBase ++;
						totalRenovacoesMesBase += rsmContrato.getFloat("VL_CONTRATO");
					}
					else {
						iNovosMesBase ++;
						totalNovosMesBase += rsmContrato.getFloat("VL_CONTRATO");
					}
					totalMesBase += rsmContrato.getFloat("VL_CONTRATO");
					iPrazoMesBase += rsmContrato.getInt("NR_PARCELAS");
				}
				if (rsmContrato.getInt("CD_TIPO_OPERACAO") == cdTipoOperacaoRenovacao) {
					iRenovacoesGeral ++;
				}
				//Créditos quitados
				sql = "SELECT COUNT(*) AS PACELAS_PAGAR " +
			   	  "FROM adm_conta_receber A " +
			   	  "WHERE (A.cd_contrato = ?) " +
		          "	AND ((A.st_conta <> ?) OR (A.dt_recebimento > ?))";
				pstmt = connect.prepareStatement(sql);
				pstmt.setInt(1, cdContrato);
				pstmt.setInt(2, com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA);
				pstmt.setTimestamp(3, new Timestamp(dtBase.getTimeInMillis()));
				rsmContaReceber = new ResultSetMap(pstmt.executeQuery());
				if (rsmContaReceber != null && rsmContaReceber.next()) {
					if (rsmContaReceber.getFloat("PARCELAS_PAGAR") == 0) {
//						iQuitadosGeral ++;
					}
				}
				float vlSaldoCA = 0;
				if(rsmContaReceber.next())
					vlSaldoCA = rsmContaReceber.getFloat("vl_saldo_ca");
				rsmContrato.setValueToField("VL_SALDO_CA", vlSaldoCA);
			}
			
	        String relations = "(";
			int numeroLinhas = criterios.size();
			@SuppressWarnings("unchecked")
			ArrayList<ItemComparator> criteriosCnae = (ArrayList<ItemComparator>)criterios.clone();
			@SuppressWarnings("unchecked")
			ArrayList<ItemComparator> criteriosCbo = (ArrayList<ItemComparator>)criterios.clone();
			for (int i = 0; criterios != null && i < numeroLinhas; i++) {
				ItemComparator itemCnae = (ItemComparator)criterios.get(i).clone();
				ItemComparator itemCbo  = (ItemComparator)criterios.get(i).clone();
				if (itemCnae.getColumn().equalsIgnoreCase("E2.CD_ATIVIDADE")) {
					itemCnae.setColumn("E2.cd_cnae");
					itemCbo.setColumn("E2.cd_cbo");
					criteriosCnae.add(itemCnae);
					criteriosCbo.add(itemCbo);
				}
				relations += String.valueOf(i+1) + " AND ";
			}
			ItemComparator item = ItemComparator.getItemComparatorByColumn(criterios, "E2.CD_ATIVIDADE");
			if (item != null) {
				criteriosCnae.remove(item);
				criteriosCbo.remove(item);
			}

			ItemComparator item1 = new sol.dao.ItemComparator("A.st_conta", String.valueOf(com.tivic.manager.adm.ContaReceberServices.ST_RECEBIDA), sol.dao.ItemComparator.DIFFERENT, Types.INTEGER);
			ItemComparator item2 = new sol.dao.ItemComparator("A.dt_recebimento", sol.dao.Util.convCalendarString(dtBase), sol.dao.ItemComparator.GREATER, Types.TIMESTAMP);
			criteriosCnae.add(item1);
			criteriosCnae.add(item2);
			criteriosCbo.add(item1);
			criteriosCbo.add(item2);
			numeroLinhas = criterios.size();
	        relations += "(" + String.valueOf(numeroLinhas + 1) + " OR " + String.valueOf(numeroLinhas + 2) + "))";
	        return rsmContrato;
	        */
	        return null;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			System.err.println("Erro! ContratoServices.LimiteCredito: " +  e);
			return null;
		}
		finally{
			Conexao.desconectar(connect);
		}
	}
}