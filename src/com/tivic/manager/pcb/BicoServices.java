package com.tivic.manager.pcb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.manager.alm.DocumentoSaida;
import com.tivic.manager.alm.DocumentoSaidaItem;
import com.tivic.manager.alm.DocumentoSaidaItemServices;
import com.tivic.manager.alm.DocumentoSaidaServices;
import com.tivic.manager.amf.DestinationConfig;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.util.Util;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.dao.Search;
import sol.util.Result;

@DestinationConfig(enabled = false)
public class BicoServices {

	public static Result save(Bico objeto,int cdUsuario) {
		return save(objeto,cdUsuario, null);
	}
	
	public static Result save(Bico objeto) {
		return save(objeto,0, null);
	}
	
	public static Result save(Bico objeto, int cdUsuario, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			if(objeto.getNrEnderecoAutomacao()==0)
				objeto.setNrEnderecoAutomacao(Integer.parseInt(objeto.getIdBico()));
			if(objeto.getNrBicoAutomacao()==null || objeto.getNrBicoAutomacao().trim().equals(""))
				objeto.setNrBicoAutomacao(objeto.getIdBico());
			// Verifica pra que o número de ordem e id do bico não se repitam
			ResultSet rs = connect.prepareStatement("SELECT * FROM pcb_bico " +
					                                "WHERE cd_bico <> " +objeto.getCdBico()+
					                                "  AND (nr_ordem = "+objeto.getNrOrdem()+
					                                "   OR  id_bico  = \'"+objeto.getIdBico()+"\'" +
					                                "   OR  nr_endereco_automacao = "+objeto.getNrEnderecoAutomacao()+
					                                "   OR  nr_bico_automacao     = \'"+objeto.getNrBicoAutomacao()+"\')").executeQuery();
			if(rs.next())
				return new Result(-1, "O \"Codigo do Bico\", \"Numero de Ordem\", \"Codigo de Automacao\" e \"End. de Automacao\" devem ser unicos!");
			//
			if(objeto.getCdBico() <= 0)
				return new Result(BicoDAO.insert(objeto, connect)); // Se já tá dando o return não precisa do else
			else
				return new Result(BicoDAO.update(objeto, connect)); // Se já tá dando o return não precisa do else
		}
		catch(Exception e) {
			if (isConnectionNull)
				Conexao.rollback(connect);
			e.printStackTrace(System.out);
			return new Result(-1, "Falhar ao tentar salvar dados do bico!", e);
		}
		finally	{
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getAll(int cdEmpresa) {
		return getAll(cdEmpresa, null);
	}
	
	public static ResultSetMap getAll(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			String sql = "SELECT BI.*, BO.nm_bomba, BO.id_bomba, BO.nr_ilha, L.nm_local_armazenamento AS nm_tanque, " +
					     "       T.cd_produto_servico, PS.nm_produto_servico AS nm_combustivel, TP.nm_tabela_preco " +
				     	 "FROM pcb_bico BI " +
						 "JOIN pcb_bombas                  BO ON (BI.cd_bomba = BO.cd_bomba) " +
						 "JOIN pcb_tanque                   T ON (BI.cd_tanque = T.cd_tanque) " +
						 "JOIN alm_local_armazenamento      L ON (BI.cd_tanque = L.cd_local_armazenamento) "+ 
						 "JOIN grl_produto_servico         PS ON (T.cd_produto_servico = PS.cd_produto_servico) "+
						 "LEFT OUTER JOIN adm_tabela_preco TP ON (BI.cd_tabela_preco = TP.cd_tabela_preco) "+
						 "WHERE L.cd_empresa = "+cdEmpresa+
						 " ORDER BY BI.nr_ordem, BI.id_bico";
			return new ResultSetMap(connect.prepareStatement(sql).executeQuery());
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
	
	public static ResultSetMap getAllWithAbastPend(int cdEmpresa) {
		return getAllWithAbastPend(cdEmpresa, null);
	}
	
	public static ResultSetMap getAllWithAbastPend(int cdEmpresa, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			//
			String sql = "SELECT BI.*, BO.nm_bomba, BO.id_bomba, BO.nr_ilha, L.nm_local_armazenamento AS nm_tanque, " +
					     "       PS.nm_produto_servico AS nm_combustivel, TP.nm_tabela_preco " +
				     	 "FROM pcb_bico BI " +
						 "JOIN pcb_bombas                  BO ON (BI.cd_bomba = BO.cd_bomba) " +
						 "JOIN pcb_tanque                   T ON (BI.cd_tanque = T.cd_tanque) " +
						 "JOIN alm_local_armazenamento      L ON (BI.cd_tanque = L.cd_local_armazenamento) "+ 
						 "JOIN grl_produto_servico         PS ON (T.cd_produto_servico = PS.cd_produto_servico) "+
						 "LEFT OUTER JOIN adm_tabela_preco TP ON (BI.cd_tabela_preco = TP.cd_tabela_preco) "+
						 "WHERE L.cd_empresa = "+cdEmpresa+
						 " ORDER BY BI.nr_ordem, BI.id_bico";
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(sql).executeQuery());
			ResultSetMap rsmAbastPendentes = getAbastecimentosPendentes(cdEmpresa, null, connect);
			while(rsmAbastPendentes.next()){
				if(rsm.locate("cd_bico", rsmAbastPendentes.getInt("cd_bico"))){
					rsm.setValueToField("QT_ABASTECIMENTOS", rsmAbastPendentes.getInt("qt_abastecimentos"));
				}
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAbastecimentosPendentes(int cdEmpresa, String idBico) {
		return getAbastecimentosPendentes(cdEmpresa, idBico, null);
	}
	
	public static ResultSetMap getAbastecimentosPendentes(int cdEmpresa, String idBico, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			String sql = "SELECT B.cd_bico, B.qt_encerrante_final, C.nr_endereco_automacao, " +
					     "C.cd_bomba, C.nr_bico_automacao, COUNT(*) AS qt_abastecimentos " +
				     	 "FROM alm_documento_saida A, alm_documento_saida_item B, pcb_bico C " +
						 "WHERE A.cd_documento_saida  = B.cd_documento_saida " +
						 "  AND A.cd_empresa          = "+cdEmpresa+
						 "  AND A.st_documento_saida  = "+DocumentoSaidaServices.ST_EM_CONFERENCIA+
						 "  AND B.cd_bico             = C.cd_bico "+
						 "  AND B.dt_entrega_prevista IS NOT NULL " +
						 (idBico!=null && !idBico.equals("") ? " AND C.id_bico = \'"+idBico+"\'": "" )+
						 " GROUP BY B.cd_bico, C.nr_endereco_automacao, C.nr_bico_automacao, B.qt_encerrante_final, C.cd_bomba ";
			return new ResultSetMap(connect.prepareStatement(sql).executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getAbastecimentosPendentes(int cdBico) {
		return getAbastecimentosPendentes(cdBico, (Connection)null);
	}
	
	public static ResultSetMap getAbastecimentosPendentes(int cdBico, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			String nmProdutoServico = "";
			int    cdProdutoServico = 0;
			int    cdUnidadeMedida  = 0;
			String sgUnidadeMedida  = "";
			String idProdutoServico = "";
			String idReduzido       = "";
			String idBico           = "";
			ResultSet rs = connect.prepareStatement("SELECT B.cd_produto_servico, C.id_produto_servico, C.nm_produto_servico, E.id_reduzido, "+
					                                "       E.cd_unidade_medida, F.sg_unidade_medida, A.id_bico " +
					                                "FROM pcb_bico A " +
					                                "JOIN pcb_tanque                  B ON (A.cd_tanque           = B.cd_tanque) " +
					                                "JOIN grl_produto_servico         C ON (B.cd_produto_servico  = C.cd_produto_servico) " +
					                                "JOIN alm_local_armazenamento     D ON (A.cd_tanque           = D.cd_local_armazenamento) " +
					                                "JOIN grl_produto_servico_empresa E ON (B.cd_produto_servico  = E.cd_produto_servico" +
					                                "                                   AND D.cd_empresa          = E.cd_empresa) " +
					                                "LEFT OUTER JOIN grl_unidade_medida F ON (E.cd_unidade_medida = F.cd_unidade_medida) " +
					                                "WHERE A.cd_bico = "+cdBico).executeQuery();
			if(rs.next())	{
				nmProdutoServico = rs.getString("nm_produto_servico");
				cdProdutoServico = rs.getInt("cd_produto_servico");
				cdUnidadeMedida  = rs.getInt("cd_unidade_medida");
				sgUnidadeMedida  = rs.getString("sg_unidade_medida");
				idProdutoServico = rs.getString("id_produto_servico");
				idReduzido       = rs.getString("id_reduzido");
				idBico           = rs.getString("id_bico");
			}
			String sql = "SELECT B.* " +
				     	 "FROM alm_documento_saida A, alm_documento_saida_item B " +
						 "WHERE A.cd_documento_saida  = B.cd_documento_saida " +
						 "  AND A.st_documento_saida  = "+DocumentoSaidaServices.ST_EM_CONFERENCIA+
						 "  AND B.cd_bico             = "+cdBico+
						 "  AND B.dt_entrega_prevista IS NOT NULL ";
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(sql).executeQuery());
			while(rsm.next()) {
				rsm.setValueToField("NM_COMBUSTIVEL", nmProdutoServico);
				rsm.setValueToField("CD_PRODUTO_SERVICO", cdProdutoServico);
				rsm.setValueToField("CD_UNIDADE_MEDIDA", cdUnidadeMedida);
				rsm.setValueToField("SG_UNIDADE_MEDIDA", sgUnidadeMedida);
				rsm.setValueToField("ID_PRODUTO_SERVICO", idProdutoServico);
				rsm.setValueToField("ID_REDUZIDO", idReduzido);
				rsm.setValueToField("ID_BICO", idBico);
				rsm.setValueToField("HR_ENTREGA_PREVISTA", Util.formatDate(rsm.getTimestamp("dt_entrega_prevista"), "HH:mm"));
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	public static ResultSetMap getAbastPendentesByBicoEncerrante(int cdEmpresa, int cdBico, String idBico, float qtEncerranteFinal) {
		return getAbastPendentesByBicoEncerrante(cdEmpresa, cdBico, idBico, qtEncerranteFinal, null);
	}
	
	public static ResultSetMap getAbastPendentesByBicoEncerrante(int cdEmpresa, int cdBico, String idBico, float qtEncerranteFinal, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			String sql = "SELECT B.cd_bico, D.nm_produto_servico, B.qt_encerrante_final, C.nr_endereco_automacao, " +
					     "C.cd_bomba, C.nr_bico_automacao, B.dt_entrega_prevista, C.nr_bico_automacao " +
				     	 "FROM alm_documento_saida A, alm_documento_saida_item B, pcb_bico C, grl_produto_servico D " +
						 "WHERE A.cd_documento_saida  = B.cd_documento_saida " +
						 "  AND D.cd_produto_servico  = B.cd_produto_servico " +
						 "  AND A.cd_empresa          = "+cdEmpresa+
						 "  AND A.st_documento_saida  = "+DocumentoSaidaServices.ST_EM_CONFERENCIA+
						 "  AND B.cd_bico             = C.cd_bico "+
						 (cdBico > 0 ? "AND C.cd_bico = " + cdBico  : "" )+
						 "  AND ROUND(B.qt_encerrante_final::NUMERIC,2) = " + qtEncerranteFinal +
						 "  AND B.dt_entrega_prevista IS NOT NULL " +
						 "  AND C.id_bico = \'"+idBico+"\' "+
						 "GROUP BY B.cd_bico, C.nr_endereco_automacao, C.nr_bico_automacao, D.nm_produto_servico," +
						 "         B.qt_encerrante_final, C.cd_bomba, B.dt_entrega_prevista, C.nr_bico_automacao ";
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(sql).executeQuery());
			while(rsm.next())
				rsm.setValueToField("hr_entrega_prevista", Util.formatDate(rsm.getTimestamp("dt_entrega_prevista"), "HH:mm:ss"));
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getBicoByNrBicoAutomacao(int cdEmpresa, String nrIdBicoAutomacao) {
		return getBicoByNrBicoAutomacao(cdEmpresa, nrIdBicoAutomacao, null);
	}
	public static ResultSetMap getBicoByNrBicoAutomacao(int cdEmpresa, String nrIdBicoAutomacao, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			String sql = "SELECT * FROM pcb_bico WHERE nr_bico_automacao = \'" + nrIdBicoAutomacao +"\' ";
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(sql).executeQuery());
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
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
		String sql = "SELECT BI.*, BO.nm_bomba, BO.id_bomba, BO.nr_ilha, L.nm_local_armazenamento AS nm_tanque, " +
					 "       PS.nm_produto_servico AS nm_combustivel, TP.nm_tabela_preco " +
				     "FROM pcb_bico BI " +
					 "JOIN pcb_bombas                 BO ON(BI.cd_bomba = BO.cd_bomba) " +
					 "JOIN pcb_tanque                  T ON(BI.cd_tanque = T.cd_tanque) " +
					 "JOIN grl_produto_servico        PS ON(T.cd_produto_servico = PS.cd_produto_servico) "+
					 "JOIN adm_tabela_preco           TP ON (BI.cd_tabela_preco = TP.cd_tabela_preco) "+
					 "JOIN alm_local_armazenamento    L  ON (L.cd_local_armazenamento = T.cd_tanque) " +
					 "WHERE 1=1 ";
		return Search.find(sql, "ORDER BY BI.nr_ordem", criterios, connect!=null ? connect : Conexao.conectar(), connect==null);
	}

	public static ResultSetMap getBicoById(int cdEmpresa, int cdTabelaPreco, String idBico) {
		return getBicoById(cdEmpresa, cdTabelaPreco, idBico, null);
	}

	public static ResultSetMap getBicoById(int cdEmpresa, int cdTabelaPreco, String idBico, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			String sql = "SELECT BI.*, BO.nm_bomba, BO.id_bomba, BO.nr_ilha, L.nm_local_armazenamento AS nm_tanque, " +
					     "       PS.nm_produto_servico AS nm_combustivel, " +
					     // Dados do produto para o PDV
					     "       PS.cd_produto_servico, PS.nm_produto_servico, PS.txt_especificacao, PS.txt_dado_tecnico," +
					     "       PS.id_produto_servico, PSE.id_reduzido, PSE.cd_unidade_medida, UNM.sg_unidade_medida, UNM.nm_unidade_medida, " +
					     "       FAB.nm_pessoa AS nm_fabricante, PG.cd_grupo, " +
					     /* Dados do preço */
					     "       PSP.vl_preco, TP.cd_tabela_preco, TP.id_tabela_preco, " +
					     "       PSP2.vl_preco AS vl_preco_especifico, TP2.id_tabela_preco AS id_tabela_especifica," +
					     "       BI.cd_tabela_preco AS cd_tabela_especifica " +
					     "FROM pcb_bico                     BI " +
					     "JOIN pcb_bombas                   BO ON (BI.cd_bomba = BO.cd_bomba) " +
					     "JOIN pcb_tanque                    T ON (BI.cd_tanque = T.cd_tanque) " +
					     "JOIN alm_local_armazenamento       L ON (BI.cd_tanque = L.cd_local_armazenamento) "+
					     "JOIN grl_produto_servico          PS ON (T.cd_produto_servico = PS.cd_produto_servico) "+
//					     "LEFT OUTER JOIN grl_produto_codigo PC ON (PS.cd_produto_servico = PC.produto_servico)"+
					     "JOIN grl_produto_servico_empresa PSE ON (PSE.cd_produto_servico = T.cd_produto_servico" +
					     "                                     AND PSE.cd_empresa         = "+cdEmpresa+") "+
						 "JOIN grl_unidade_medida          UNM ON (PSE.cd_unidade_medida = UNM.cd_unidade_medida) " +
					     "JOIN adm_produto_servico_preco   PSP ON (PSP.cd_produto_servico = PS.cd_produto_servico " +
					     "                                     AND PSP.cd_tabela_preco    = " +cdTabelaPreco+
					     "                                     AND PSP.dt_termino_validade IS NULL) " +
					     "JOIN adm_tabela_preco             TP ON (TP.cd_tabela_preco = "+cdTabelaPreco+") "+
						 "LEFT OUTER JOIN grl_pessoa 	   FAB ON (PS.cd_fabricante = FAB.cd_pessoa) " +
						 "LEFT OUTER JOIN alm_produto_grupo PG ON (PS.cd_produto_servico = PG.cd_produto_servico " +
						 "                 				       AND PG.cd_empresa = " + cdEmpresa +
						 "                                     AND PG.lg_principal = 1) " +
						 "LEFT OUTER JOIN adm_tabela_preco            TP2 ON (TP2.cd_tabela_preco = BI.cd_tabela_preco) "+
					     "LEFT OUTER JOIN adm_produto_servico_preco  PSP2 ON (PSP2.cd_produto_servico = PS.cd_produto_servico " +
					     "                                                AND PSP2.cd_tabela_preco    = BI.cd_tabela_preco "+
					     "                                                AND PSP2.dt_termino_validade IS NULL) " +
						 "WHERE L.cd_empresa = " +cdEmpresa+
					     "  AND BI.id_bico   = \'"+idBico+"\' ";
			return new ResultSetMap(connect.prepareStatement(sql).executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static ResultSetMap getByBomba(int cdBomba) {
		return getByBomba(cdBomba, null);
	}

	public static ResultSetMap getByBomba(int cdBomba, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			String sql = "SELECT * " +
					     "FROM pcb_bico BI " +
						 "WHERE cd_bomba = " + cdBomba;
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement(sql).executeQuery());
			return rsm;
		
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result registrarAbastecimento(int cdEmpresa, int cdNaturezaOperacao, int cdTipoOperacao, int cdTabelaPreco, String nrBicoAutomacao, float qtLitros, float vlUnitario, float vlTotal, GregorianCalendar dtAbastecimento, 
            int nrRegistro, float nrEncerrante, String txtAbastecimento) {
		return registrarAbastecimento(cdEmpresa, cdNaturezaOperacao, cdTipoOperacao, cdTabelaPreco, nrBicoAutomacao, qtLitros, vlUnitario, vlTotal, dtAbastecimento, nrRegistro, nrEncerrante, txtAbastecimento, null);
	}
	
	public static Result registrarAbastecimento(int cdEmpresa, int cdNaturezaOperacao, int cdTipoOperacao, int cdTabelaPreco,   
			                                    String nrBicoAutomacao, float qtLitros, float vlUnitario, float vlTotal, GregorianCalendar dtAbastecimento, 
			                                    int nrRegistro, float nrEncerrante, String txtAbastecimento, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			// Do bico descubro o tanque, do tanque descubro o produto
			ResultSet rsBico = connect.createStatement().executeQuery(
					 "SELECT BI.*, PS.cd_produto_servico, PSE.cd_unidade_medida, PSP.vl_preco, PSP2.vl_preco AS vl_preco_especifico "+
				     "FROM pcb_bico                     BI " +
				     "JOIN pcb_tanque                    T ON (BI.cd_tanque           = T.cd_tanque) " +
				     "JOIN grl_produto_servico          PS ON (T.cd_produto_servico   = PS.cd_produto_servico) "+
				     "JOIN grl_produto_servico_empresa PSE ON (PSE.cd_produto_servico = T.cd_produto_servico" +
				     "                                     AND PSE.cd_empresa         = "+cdEmpresa+") "+
					 "JOIN adm_produto_servico_preco   PSP ON (PSP.cd_produto_servico = PS.cd_produto_servico " +
				     "                                     AND PSP.cd_tabela_preco    = " +cdTabelaPreco+
				     "                                     AND PSP.dt_termino_validade IS NULL) " +
				     "LEFT OUTER JOIN adm_produto_servico_preco  PSP2 ON (PSP2.cd_produto_servico = PS.cd_produto_servico " +
				     "                                                AND PSP2.cd_tabela_preco    = BI.cd_tabela_preco "+
				     "                                                AND PSP2.dt_termino_validade IS NULL) " +
					 "WHERE BI.nr_bico_automacao = \'"+nrBicoAutomacao+"\' ");

			if(!rsBico.next())
				return new Result(-1, "Falha ao tentar localizar bico: "+nrBicoAutomacao);
			connect.setAutoCommit(false);
			// INCLUINDO DOCUMENTO DE SAÍDA
			DocumentoSaida docSaida = new DocumentoSaida(0,0/*cdTransportadora*/,cdEmpresa,0/*cdCliente*/,dtAbastecimento, DocumentoSaidaServices.ST_EM_CONFERENCIA,
														 String.valueOf(nrRegistro), DocumentoSaidaServices.TP_DOC_NAO_FISCAL, DocumentoSaidaServices.SAI_VENDA,
														 ""/*nrConhecimento*/,(float)0/*vlDesconto*/,(float)0/*vlAcrescimo*/, new GregorianCalendar(),
														 DocumentoSaidaServices.FRT_CIF,""/*txtMensagem*/,txtAbastecimento,""/*nrPlacaVeiculo*/,
														 ""/*sgPlacaVeiculo*/,""/*nrVolumes*/,null/*dtSaidaTransportadora*/,""/*dsViaTransporte*/,
														 cdNaturezaOperacao,""/*txtCorpoNotaFiscal*/,0/*vlPesoLiquido*/,0/*vlPesoBruto*/,
														 ""/*dsEspecieVolumes*/,""/*dsMarcaVolumes*/,0/*qtVolumes*/,DocumentoSaidaServices.MOV_ESTOQUE_NAO_CONSIGNADO,
														 0/*cdVendedor*/,0/*cdMoeda*/,0/*cdReferenciaEcf*/,0/*cdSolicitacaoMaterial*/,cdTipoOperacao,vlTotal,
														 0/*cdContrato*/,0/*vlFrete*/,0/*vlSeguro*/,0/*cdDigitador*/,0/*cdDocumento*/,0/*cdConta*/,0/*cdTurno*/,
														 vlTotal /*vlTotalItens*/, 1 /*nrSerie*/);
			Result result = DocumentoSaidaServices.insert(docSaida, 0/*cdDocumentoSaida*/, connect);
			
			if(result.getCode() <= 0){
				result.setMessage("Falha ao tentar salvar documento no registro do abastecimento. "+result.getMessage());
				Conexao.rollback(connect);
				return result;
			}
			int cdDocumentoSaida = result.getCode();
			int cdUnidadeMedida  = rsBico.getInt("cd_unidade_medida");
			int cdProdutoServico = rsBico.getInt("cd_produto_servico");
			int cdBico           = rsBico.getInt("cd_bico");
			if(rsBico.getInt("cd_tabela_preco") > 0)
				cdTabelaPreco = rsBico.getInt("cd_tabela_preco"); 
			DocumentoSaidaItem item = new DocumentoSaidaItem(cdDocumentoSaida,cdProdutoServico,cdEmpresa, qtLitros /*qtSaida*/, vlUnitario,
									                    	 0/*vlAcrescimo*/,0/*vlDesconto*/, dtAbastecimento /*dtEntregaPrevista*/,
									                    	 cdUnidadeMedida,cdTabelaPreco,nrRegistro /*cdItem*/, cdBico, nrEncerrante/*qtEncerranteFinal*/);
			result = DocumentoSaidaItemServices.insert(item, rsBico.getInt("cd_tanque"), 0/*cdLocalDestino*/, true /*registerTributacao*/, connect);
			if(result.getCode() <= 0){
				result.setMessage("Falha ao tentar salvar registro do abastecimento. "+result.getMessage());
				Conexao.rollback(connect);
				return result;
			}
			connect.commit();
			// 
			int qtAbastPendentes = 0;
			String sql = "SELECT COUNT(*) AS qt_abastecimentos " +
				     	 "FROM alm_documento_saida A, alm_documento_saida_item B " +
						 "WHERE A.cd_documento_saida  = B.cd_documento_saida " +
						 "  AND A.cd_empresa          = "+cdEmpresa+
						 "  AND A.st_documento_saida  = "+DocumentoSaidaServices.ST_EM_CONFERENCIA+
						 "  AND B.dt_entrega_prevista IS NOT NULL " +
						 "  AND B.cd_bico             = "+cdBico;
			ResultSet rs = connect.prepareStatement(sql).executeQuery();
			if(rs.next())
				qtAbastPendentes = rs.getInt("qt_abastecimentos");
			result = new Result(cdDocumentoSaida);
			result.addObject("QT_ABASTECIMENTOS", qtAbastPendentes);
			result.addObject("NR_ENDERECO_AUTOMACAO", rsBico.getInt("nr_endereco_automacao"));
			result.addObject("NR_BICO_AUTOMACAO", rsBico.getString("nr_bico_automacao"));
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return new Result(-1, "Falha ao tentar registrar abastecimento!", e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public static ResultSetMap getVendasByBico(int cdEmpresa, int cdTurno, GregorianCalendar dtDocumentoSaida){
		return getVendasByBico(cdEmpresa,cdTurno,dtDocumentoSaida,null);
	}
	
	public static ResultSetMap getVendasByBico(int cdEmpresa, int cdTurno, GregorianCalendar dtDocumentoSaida, Connection connect){
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			String sqlBicos = "SELECT SUM(DSI.qt_saida) as qt_saida_total,PS.nm_produto_servico,DSI.vl_unitario "+
							  "FROM pcb_bico B "+
							  "JOIN pcb_tanque T ON(B.cd_tanque = T.cd_tanque) "+
							  "JOIN grl_produto_servico PS ON(T.cd_produto_servico = PS.cd_produto_servico) "+
						      "JOIN alm_documento_saida_item DSI ON(DSI.cd_produto_servico = PS.cd_produto_servico) "+
							  "JOIN alm_documento_saida DS ON(DS.cd_documento_saida = DSI.cd_documento_saida) "+
							  "WHERE B.st_bico = 1 AND DS.cd_turno = "+cdTurno+" AND DS.cd_empresa = "+ cdEmpresa +
							  " AND DS.dt_documento_saida =  ?"+
							  "GROUP BY PS.nm_produto_servico, DSI.vl_unitario ";
			PreparedStatement pstmt = connect.prepareStatement(sqlBicos);
			pstmt.setTimestamp(1, new Timestamp(dtDocumentoSaida.getTimeInMillis()));
			return new ResultSetMap(pstmt.executeQuery());
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return null;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	/**
	 * getVolRemanescenteByBico  Metodo que calcula o valor do volume remanescente considerado as saidas de 
	 *                               cupom fiscal, nota fiscal, afericao e substituicao de placa eletronica.
	 * @category PDV 
	 * @param cdReferenciaEcf    código do ecf cadastrado no sistema e utilizado na emissao do cupom fiscal
	 * @param cdBico             código do bico 
	 * @author Joao Marlon
	 * */	
	public static double getVolRemanescenteByBico(int cdBico, int cdEmpresa, int cdReferenciaEcf) {
		return getVolRemanescenteByBico(cdBico, cdEmpresa, cdReferenciaEcf, null);
	}
	public static double getVolRemanescenteByBico(int cdBico, int cdEmpresa, int cdReferenciaEcf, Connection connect) {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		try {
			double volRemanescente = 0;
			double qtTotalCupomNFe = 0;
			double qtTotalAfericao = 0;
			double qtInicial       = 0;
			double qtFinal         = 0;						
			// Captura os dados do primeiro abastecimento depois da ultima reducao Z emitido cupom fiscal e/ou nota fiscal pelo PDV, 
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM alm_documento_saida DS " +
				     										   "LEFT OUTER JOIN alm_documento_saida_item DSI ON (DS.cd_documento_saida = DSI.cd_documento_saida) " +
				     										   "WHERE DS.dt_documento_saida > ( SELECT dt_registro_ecf FROM fsc_registro_ecf  " +
				     										   "                                WHERE tp_registro_ecf LIKE 'R02'" +
				     										   "                                  AND cd_referencia_ecf = " + cdReferenciaEcf +
				     										   "                                ORDER BY dt_registro_ecf DESC" +
				     										   "                                LIMIT 1 ) " +
				     									       "  AND DSI.cd_bico           = " + cdBico+
				     										   "  AND DS.cd_empresa         = " + cdEmpresa+
				     										   "  AND DS.tp_documento_saida = " + DocumentoSaidaServices.TP_CUPOM_FISCAL+
				     										   "   OR DS.tp_documento_saida = " + DocumentoSaidaServices.TP_NOTA_FISCAL_VENDA+
				     										   "  AND DS.cd_referencia_ecf  = " + cdReferenciaEcf + 
				     										   "  AND DS.st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO +
				     										  " ORDER BY DSI.qt_encerrante_final, DS.dt_documento_saida ");			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			// Soma todas as saidas
			while(rsm.next()){
				qtTotalCupomNFe += rsm.getDouble("qt_saida");
				if(rsm.first())
					qtInicial    = rsm.getDouble("qt_encerrante_final");
				if(rsm.last())
					qtFinal      = rsm.getDouble("qt_encerrante_final");
			}
			//
			pstmt.close();
			// Captura os dados de afericao depois da ultima reducao Z 
			pstmt = connect.prepareStatement("SELECT * FROM alm_documento_saida DS " +
   										     "LEFT OUTER JOIN alm_documento_saida_item DSI ON (DS.cd_documento_saida = DSI.cd_documento_saida) " +
 										     "WHERE DS.dt_documento_saida > ( SELECT dt_registro_ecf FROM fsc_registro_ecf  " +
 										     "                                WHERE tp_registro_ecf LIKE 'R02'" +
 										     "                                  AND cd_referencia_ecf = " + cdReferenciaEcf +
 										     "                                ORDER BY dt_registro_ecf DESC" +
 										     "                                LIMIT 1 ) " +
 									         "  AND DSI.cd_bico           = " + cdBico+
 										     "  AND DS.cd_empresa         = " + cdEmpresa+
 										     "  AND DS.tp_documento_saida = " + DocumentoSaidaServices.TP_AFERICAO+ 										      
	     									" ORDER BY DSI.qt_encerrante_final, DS.dt_documento_saida ");			
			ResultSetMap rsmAfer = new ResultSetMap(pstmt.executeQuery());
			//soma todas as afericoes
			while(rsmAfer.next())
				qtTotalAfericao += rsmAfer.getDouble("qt_encerrante_final");
			volRemanescente = (qtInicial - qtFinal - qtTotalCupomNFe - qtTotalAfericao);			
			return volRemanescente;
		
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Util.registerLog(e);
			return -1;
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static Result gerarRelatorioAbastecimentoAutomacao(ArrayList<ItemComparator> criterios, HashMap<String, Object> params,  int cdEmpresa){
		boolean isConnectionNull = true;
		Connection connection = null;
		Result result = new Result(1);
		
		ResultSetMap rsmAbastecimentos = new ResultSetMap();
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String dtInicial =  dtFormat.format( new Timestamp( Util.stringToCalendar( params.get("DT_INICIAL").toString() ).getTimeInMillis() ) ) ;
		String dtFinal =  dtFormat.format( new Timestamp( Util.stringToCalendar( params.get("DT_FINAL").toString() ).getTimeInMillis() ) ) ;
		criterios.add(new ItemComparator( "A.dt_documento_saida", "'"+dtInicial+"'" , ItemComparator.GREATER_EQUAL , Types.TIME ) );
		criterios.add(new ItemComparator( "A.dt_documento_saida", "'"+dtFinal+"'", ItemComparator.MINOR_EQUAL, Types.TIME ) );
		String orderBy = "";
		if( params.get("ORDER_BY") != null )
			orderBy = "ORDER BY "+params.get("ORDER_BY").toString();
		
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			rsmAbastecimentos = Search.find("SELECT A.*, A2.id_turno, B.*, C.*, C2.nm_produto_servico, D.nm_conta, E.nm_pessoa, F.id_bico "+
											"FROM alm_documento_saida A "+
											"JOIN adm_turno A2 ON ( A.cd_turno = A2.cd_turno ) "+
											"JOIN alm_documento_saida_item B ON ( A.cd_documento_saida = B.cd_documento_saida ) "+
											"JOIN grl_produto_servico_empresa C ON ( B.cd_produto_servico = C.cd_produto_servico AND B.cd_empresa = C.cd_empresa ) "+
											"JOIN grl_produto_servico C2 ON ( C.cd_produto_servico = C2.cd_produto_servico ) "+
											"JOIN adm_conta_financeira D ON ( A.cd_conta = D.cd_conta ) "+
											"JOIN grl_pessoa E ON ( A.cd_vendedor = E.cd_pessoa ) "+
											"JOIN pcb_bico F ON ( B.cd_bico = F.cd_bico ) "+
											
											"",
											orderBy,
											criterios, connection, false);
			
			result.addObject("rsm", rsmAbastecimentos);
			result.addObject("params", params);
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
		
		
	}
	
	public static Result gerarRelatorioAfericaoCombustivel(ArrayList<ItemComparator> criterios, int cdEmpresa){
		boolean isConnectionNull = true;
		Connection connection = null;
		Result result = new Result(1);
		ResultSetMap rsmAfericao = new ResultSetMap();
		ResultSetMap rsmBicos = new ResultSetMap();
		HashMap<String, Object> params = new HashMap<String, Object>();
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			rsmAfericao = Search.find(" SELECT A.NM_PRODUTO_SERVICO, A.CD_PRODUTO_SERVICO, DSI.CD_PRODUTO_SERVICO, DSI.QT_SAIDA, DS.DT_DOCUMENTO_SAIDA, DS.CD_TURNO, DSI.CD_BICO, B.NM_TURNO " +
			                                " FROM alm_documento_saida DS " +
			                                " LEFT OUTER JOIN alm_documento_saida_item DSI ON (DS.cd_documento_saida = DSI.cd_documento_saida) " +
					                        " JOIN            grl_produto_servico      A   ON (A.cd_produto_servico = DSI.cd_produto_servico) " +
					                        " JOIN            adm_turno                B   ON (B.CD_TURNO = DS.CD_TURNO) " +
					                        " WHERE 1 = 1" +
					                        " AND DS.cd_empresa =" + cdEmpresa +
					                        " AND DS.tp_documento_saida = " + DocumentoSaidaServices.TP_AFERICAO+
					                        "  ",
					                        " ORDER BY A.NM_PRODUTO_SERVICO DESC ",
					                        criterios, connection, false);
			

			
			while(rsmAfericao.next()) {
				rsmAfericao.setValueToField("DT_DOCUMENTO_SAIDA_DMY", rsmAfericao.getDateFormat("DT_DOCUMENTO_SAIDA", "dd/MM/yyyy"));
			}
			
			rsmAfericao.beforeFirst();
			params.put("rsmBicos", rsmBicos.getLines());
			result.addObject("rsm", rsmAfericao);
			result.addObject("params", params);
			
			return result;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			Conexao.rollback(connection);
			return new Result(-1, "Erro: " + e);
		}
		finally {
			if (isConnectionNull)
				Conexao.desconectar(connection);
		}
		
		
	}
}