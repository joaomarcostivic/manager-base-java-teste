package com.tivic.manager.fsc;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;
import sol.util.SessionCounter;

import com.tivic.manager.adm.ContaFinanceira;
import com.tivic.manager.adm.ContaFinanceiraDAO;
import com.tivic.manager.adm.ContaFinanceiraServices;
import com.tivic.manager.adm.ContaPagar;
import com.tivic.manager.adm.ContaPagarDAO;
import com.tivic.manager.adm.ContaReceber;
import com.tivic.manager.adm.ContaReceberDAO;
import com.tivic.manager.adm.EntradaItemAliquota;
import com.tivic.manager.adm.EntradaItemAliquotaDAO;
import com.tivic.manager.adm.EntradaItemAliquotaServices;
import com.tivic.manager.adm.FormaPagamentoServices;
import com.tivic.manager.adm.NaturezaOperacao;
import com.tivic.manager.adm.NaturezaOperacaoDAO;
import com.tivic.manager.adm.SaidaItemAliquota;
import com.tivic.manager.adm.SaidaItemAliquotaDAO;
import com.tivic.manager.adm.SaidaItemAliquotaServices;
import com.tivic.manager.adm.SaidaTributoDAO;
import com.tivic.manager.adm.TipoDocumento;
import com.tivic.manager.adm.TipoDocumentoDAO;
import com.tivic.manager.adm.TituloCreditoDAO;
import com.tivic.manager.adm.TributoAliquotaServices;
import com.tivic.manager.alm.DocumentoEntrada;
import com.tivic.manager.alm.DocumentoEntradaDAO;
import com.tivic.manager.alm.DocumentoEntradaItem;
import com.tivic.manager.alm.DocumentoEntradaItemDAO;
import com.tivic.manager.alm.DocumentoEntradaServices;
import com.tivic.manager.alm.DocumentoSaida;
import com.tivic.manager.alm.DocumentoSaidaDAO;
import com.tivic.manager.alm.DocumentoSaidaItem;
import com.tivic.manager.alm.DocumentoSaidaItemDAO;
import com.tivic.manager.alm.DocumentoSaidaServices;
import com.tivic.manager.alm.ProdutoGrupo;
import com.tivic.manager.alm.ProdutoGrupoDAO;
import com.tivic.manager.bpm.Referencia;
import com.tivic.manager.bpm.ReferenciaDAO;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.EmpresaServices;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.grl.Ncm;
import com.tivic.manager.grl.NcmDAO;
import com.tivic.manager.grl.Pais;
import com.tivic.manager.grl.PaisDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaDAO;
import com.tivic.manager.grl.PessoaEndereco;
import com.tivic.manager.grl.PessoaEnderecoServices;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.PessoaFisicaDAO;
import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.grl.PessoaJuridicaDAO;
import com.tivic.manager.grl.PessoaServices;
import com.tivic.manager.grl.ProdutoServico;
import com.tivic.manager.grl.ProdutoServicoDAO;
import com.tivic.manager.grl.ProdutoServicoEmpresa;
import com.tivic.manager.grl.ProdutoServicoEmpresaDAO;
import com.tivic.manager.grl.ProdutoServicoServices;
import com.tivic.manager.grl.UnidadeMedida;
import com.tivic.manager.grl.UnidadeMedidaDAO;
import com.tivic.manager.pcb.Bico;
import com.tivic.manager.pcb.BicoDAO;
import com.tivic.manager.pcb.BicoServices;
import com.tivic.manager.pcb.BombasDAO;
import com.tivic.manager.pcb.BombasServices;
import com.tivic.manager.pcb.Tanque;
import com.tivic.manager.pcb.TanqueDAO;
import com.tivic.manager.util.Util;

public class SPEDServices {
	
	public static int COD_FIN_ORIGINAL    = 0;   //Remessa do arquivo original;
	public static int COD_FIN_SUBSTITUTO  = 1;   //Remessa do arquivo substituto;
	
	public static String IND_PERFIL_A     = "A"; //
	public static String IND_PERFIL_B     = "B"; //
	public static String IND_PERFIL_C     = "C"; //
	
	public static int IND_ATIV_INDUSTRIAL = 0;   //Remessa do arquivo original;
	public static int IND_ATIV_OUTROS     = 1;   //Remessa do arquivo substituto;
	
	public static int IND_MOV_DADOS       = 0;   //Bloco com dados informados
	public static int IND_MOV_SEM_DADOS   = 1;	 //Bloco sem dados informados
	
	
	public static int TP_ITEM_REVENDA     		= 0;
	public static int TP_ITEM_MATERIA_PRIMA    	= 1;
	public static int TP_ITEM_EMBALAGEM   		= 2;
	public static int TP_ITEM_PRODUTO_PROCESSO  = 3;
	public static int TP_ITEM_PRODUTO_ACABADO	= 4;
	public static int TP_ITEM_SUB_PRODUTO 		= 5;
	public static int TP_ITEM_PRODUTO_INTER		= 6;
	public static int TP_ITEM_MATERIAL_USOCON	= 7;
	public static int TP_ITEM_ATIVO_IMOBILIZADO	= 8;
	public static int TP_ITEM_SERVICOS     		= 9;
	public static int TP_ITEM_OUTROS_INSUMOS	= 10;
	public static int TP_ITEM_OUTROS     		= 99;
	
	
	public static int NAT_CONTA_ATIVOS		     = 1;
	public static int NAT_CONTA_PASSIVOS		 = 2;
	public static int NAT_CONTA_PATRIMONIO_ATIVO = 3;
	public static int NAT_CONTA_RESULTADOS	     = 4;
	public static int NAT_CONTA_COMPENSACAO	     = 5;
	public static int NAT_CONTA_OUTROS		     = 9;
	
	
	public static String IND_CTA_SINTETICA       = "S";
	public static String IND_CTA_ANALITICA       = "A";
	
	
	public static int IND_OPER_ENTRADA           = 0;
	public static int IND_OPER_SAIDA          	 = 1;
	
	public static int IND_EMIT_PROPRIA         	 = 0;
	public static int IND_EMIT_TERCEIROS         = 1;
	
	public static int IND_PAGTO_AVISTA			 = 0;
	public static int IND_PAGTO_APRAZO			 = 1;
	public static int IND_PAGTO_OUTROS			 = 2;
	
	public static int IND_FRT_EMITENTE			 = 0;
	public static int IND_FRT_DESTINATARIO		 = 1;
	public static int IND_FRT_TERCEIROS			 = 2;
	public static int IND_FRT_SEMFRETE			 = 9;
	
	public static int IND_OPER_COMBUSTIVEL       = 0;
	public static int IND_OPER_LEASING         	 = 1;
	
	public static int IND_PROC_SEFAZ			 = 0;
	public static int IND_PROC_JF				 = 1;
	public static int IND_PROC_JE				 = 2;
	public static int IND_PROC_SECEX			 = 3;
	public static int IND_PROC_OUTROS			 = 9;
	
	public static int COD_DA_DOC_ESTADUAL        = 0;
	public static int COD_DA_GNRE        		 = 1;
	
	public static int IND_CARGA_RODOVIARIO		 = 0;
	public static int IND_CARGA_FERROVIARIO		 = 1;
	public static int IND_CARGA_RODOFERROVIARIO	 = 2;
	public static int IND_CARGA_AQUAVIARIO		 = 3;
	public static int IND_CARGA_DUTOVIARIO		 = 4;
	public static int IND_CARGA_AEREO			 = 5;
	public static int IND_CARGA_OUTROS		 	 = 9;
	
	public static int COD_DOC_IMP_IMPORTACAO     = 0;
	public static int COD_DOC_IMP_SIMPLIFICADA   = 1;
	
	public static int IND_TIT_DUPLICATA			 = 00;
	public static int IND_TIT_CHEQUE		 	 = 01;
	public static int IND_TIT_PROMISSORIA		 = 02;
	public static int IND_TIT_RECIBO    		 = 03;
	public static int IND_TIT_OUTROS			 = 99;
	
	public static int IND_MOV_SIM                = 0;
	public static int IND_MOV_NAO                = 1;
	
	public static int IND_APUR_MENSAL            = 0;
	public static int IND_APUR_DECENDIAL         = 1;
	
	
	public static int CST_IPI_ENTRADA_RECUPERACAO = 00;
	public static int CST_IPI_ENTRADA_TRIBUTADA   = 01;
	public static int CST_IPI_ENTRADA_ISENTA	  = 02;
	public static int CST_IPI_ENTRADA_N_TRIBUTADA = 03;
	public static int CST_IPI_ENTRADA_IMUNE		  = 04;
	public static int CST_IPI_ENTRADA_C_SUSPENSAO = 05;
	public static int CST_IPI_ENTRADA_OUTRAS	  =	49;
	public static int CST_IPI_SAIDA_TRIBUTADA	  = 50;
	public static int CST_IPI_SAIDA_TRIBUTADA_ALIQUOTA = 51;
	public static int CST_IPI_SAIDA_ISENTA  	  = 52;
	public static int CST_IPI_SAIDA_N_TRIBUTADA	  = 53;
	public static int CST_IPI_SAIDA_IMUNE		  = 54;
	public static int CST_IPI_SAIDA_C_SUSPENSAO	  = 55;
	public static int CST_IPI_SAIDA_OUTRAS		  = 99;
	
	public static int COD_CONS_COMERCIAL          = 01;
	public static int COD_CONS_CONSUMOPROPRIO     = 02;
	public static int COD_CONS_ILUMINCACAO_PUBLICA= 03;
	public static int COD_CONS_INDUSTRIAL         = 04;
	public static int COD_CONS_PODER_PUBLICO      = 05;
	public static int COD_CONS_RESIDENCIAL        = 06;
	public static int COD_CONS_RURAL         	  = 07;
	public static int COD_CONS_SERV_PUBLICO       = 8;
	
	public static int TP_LIGACAO_MONOFASICO		  = 1;
	public static int TP_LIGACAO_BIFASICO		  = 2;
	public static int TP_LIGACAO_TRIFASICO		  = 3;
	
	public static int COD_GRUPO_TENSAO_A1         = 1;
	public static int COD_GRUPO_TENSAO_A2         = 2;
	public static int COD_GRUPO_TENSAO_A3         = 3;
	public static int COD_GRUPO_TENSAO_A3a        = 4;
	public static int COD_GRUPO_TENSAO_A4         = 5;
	public static int COD_GRUPO_TENSAO_AS         = 6;
	public static int COD_GRUPO_TENSAO_B1         = 7;
	public static int COD_GRUPO_TENSAO_B1_B_RENDA = 8;
	public static int COD_GRUPO_TENSAO_B2         = 9;
	public static int COD_GRUPO_TENSAO_B2_COOP    = 10;
	public static int COD_GRUPO_TENSAO_B2_SERV    = 11;
	public static int COD_GRUPO_TENSAO_B3_DEMAIS  = 12;
	public static int COD_GRUPO_TENSAO_B4a  	  = 13;
	public static int COD_GRUPO_TENSAO_B4b  	  = 14;	
	
	public static int IND_REC_PROPRIO 			  = 0;
	public static int IND_REC_TERCEIROS			  = 1;
	
	public static String COD_AJ_APUR_OUTROS_DEBITOS    = "0";
	public static String COD_AJ_APUR_ESTORNO_CREDITO   = "1";
	public static String COD_AJ_APUR_OUTROS_CREDITOS   = "2";
	public static String COD_AJ_APUR_ESTORNO_DEBITOS   = "3";
	public static String COD_AJ_APUR_DEDUCAO_IMPOSTO   = "4";
	public static String COD_AJ_APUR_DEBITOS_ESPECIAIS = "5";
	
	public static String TIPO_MOV_SAIDA_INICIAL         = "SI";
	public static String TIPO_MOV_IMOBILIZACAO          = "IM";
	public static String TIPO_MOV_IMOB_ANDAMENT         = "IA";
	public static String TIPO_MOV_CONCLUSAO_IMOB        = "CI";
	public static String TIPO_MOV_IMOB_ATIVO_CIRC       = "MC";
	public static String TIPO_MOV_BAIXA_BEM             = "BA";
	public static String TIPO_MOV_ALIENACAO_TRANSF      = "AT";
	public static String TIPO_MOV_PEREC_EXTRAVIO_DESVIO = "PE";
	public static String TIPO_MOV_OUTRA_SAIDA           = "OT";
	
	public static String MOT_INV_FINAL_PERIODO          = "01";
	public static String MOT_INV_FORMA_TRIBUTACAO       = "02";
	public static String MOT_INV_PARALISACAO            = "03";
	public static String MOT_INV_REGIME_PAGAMENTO       = "04";
	public static String MOT_INV_DETERMINACAO           = "05";
	
	public static String ST_DOCUMENTO_REGULAR             = "00";
	public static String ST_ESCRITURIZACAO_EXTEPORANEA_R  = "01";
	public static String ST_DOCUMENTO_CANCELADO           = "02";
	public static String ST_ESCRITURIZACAO_EXTEPORANEA_C  = "03";
	public static String ST_NFE_DENEGADO                  = "04";
	public static String ST_NFE_INUTILIZADO               = "05";
	public static String ST_DOCUMENTO_COMPLEMENTAR        = "06";
	public static String ST_ESCRITURIZACAO_EXTEPORANEA_DC = "07";
	public static String ST_DOCUMENTO_REGIME_ESPECIAL     = "08";
	
	public static int INC_TRIB_NAO_CUMULATIVO = 1;
	public static int INC_TRIB_CUMULATIVO     = 2;
	public static int INC_TRIB_AMBOS          = 3;
	
	public static int TP_SPED_ICMS_IPI   = 0;
	public static int TP_SPED_PIS_COFINS = 1;
	
	public static final String[] perfil = new String[] {"A", "B", "C"};
	
	public static final String[] finalidade = new String[] {"Original", "Substituto"};
	
	public static final String[] indicadorTributo = new String[] {"Selecione...", 
																  "Escrituração de operações com incidência exclusivamente no regime não-cumulativo" , 
																  "Escrituração de operações com incidência exclusivamente no regime cumulativo" , 
																  "Escrituração de operações com incidência nos regimes não-cumulativo e cumulativo"};
	
	public static final String[] tpSped = new String[] {"ICMS/IPI", "PIS/CONFINS"};
	
	public static String gerarSped(int cdEmpresa, int nrMes, int nrAno)	{
		return gerarSpedResultado(cdEmpresa, nrMes, nrAno).getMessage();
	}
	
	public static Result gerarSpedResultado(int cdEmpresa, int nrMes, int nrAno)	{
		return gerarSpedResultado(cdEmpresa, nrMes, nrAno, 1, 1);
	}
	
	public static String gerarSped(int cdEmpresa, int nrMes, int nrAno, int tpPerfil, int tpRemessa)	{
		return gerarSpedResultado(cdEmpresa, nrMes, nrAno, tpPerfil, tpRemessa).getMessage();
	}
	
	public static Result gerarSpedResultado(int cdEmpresa, int nrMes, int nrAno, int tpPerfil, int tpRemessa)	{
		return gerarSpedResultado(cdEmpresa, nrMes, nrAno, tpPerfil, tpRemessa, null);
	}
	
	public static Result gerarSpedResultado(int cdEmpresa, int nrMes, int nrAno, int tpPerfil, int tpRemessa, HttpSession session)	{
		GregorianCalendar dtInicial = new GregorianCalendar(nrAno, nrMes-1, 1);
		GregorianCalendar dtFinal   = (GregorianCalendar)dtInicial.clone();
		dtFinal.set(Calendar.DAY_OF_MONTH, dtFinal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return gerarSpedResultado(cdEmpresa, dtInicial, dtFinal, tpPerfil, tpRemessa, null, session);
	}
	
	public static String gerarSped(int cdEmpresa, GregorianCalendar dtSpedInicial, GregorianCalendar dtSpedFinal, String nmPathArq){
		return gerarSpedResultado(cdEmpresa, dtSpedInicial, dtSpedFinal, 1, 1, nmPathArq).getMessage();
	}
	
	public static String gerarSped(int cdEmpresa, GregorianCalendar dtSpedInicial, GregorianCalendar dtSpedFinal, int tpPerfil, int tpRemessa){
		return gerarSpedResultado(cdEmpresa, dtSpedInicial, dtSpedFinal, tpPerfil, tpRemessa, null).getMessage();
	}
	
	public static Result gerarSpedResultado(int cdEmpresa, GregorianCalendar dtSpedInicial, GregorianCalendar dtSpedFinal, int tpPerfil, int tpRemessa, String nmPathArq){
		return gerarSpedResultado(cdEmpresa, dtSpedInicial, dtSpedFinal, tpPerfil, tpRemessa, nmPathArq, null);
	}
	
	public static Result gerarSpedResultado(int cdEmpresa, GregorianCalendar dtSpedInicial, GregorianCalendar dtSpedFinal, int tpPerfil, int tpRemessa, String nmPathArq, HttpSession session){
		//Busca de Informações Empresa
		Pessoa empresa = PessoaDAO.get(cdEmpresa);
		PessoaJuridica pessoaJuridica = PessoaJuridicaDAO.get(cdEmpresa);
		String cnpjEmpresa = Util.limparFormatos(pessoaJuridica.getNrCnpj());
		String ieEmpresa   = Util.limparFormatos(pessoaJuridica.getNrInscricaoEstadual());
		String imEmpresa   = Util.limparFormatos(pessoaJuridica.getNrInscricaoMunicipal());
		PessoaEndereco empresaEndereco = PessoaEnderecoServices.getEnderecoPrincipal(cdEmpresa);
		Cidade cidadeEmpresa = CidadeDAO.get(empresaEndereco.getCdCidade());
		Estado estadoEmpresa = EstadoDAO.get(cidadeEmpresa.getCdEstado());
		Pais paisEmpresa = PaisDAO.get(estadoEmpresa.getCdPais());
		String cepEmpresa  = Util.limparFormatos(empresaEndereco.getNrCep());
		
		//Contagem de Registros por Bloco
		int nrLinhaBloco0=0;
		int nrLinhaBlocoC=0;
		int nrLinhaBlocoD=0;
		int nrLinhaBlocoE=0;
		int nrLinhaBlocoG=0;
		int nrLinhaBlocoH=0;
		int nrLinhaBlocoK=0;
		int nrLinhaBloco9=0;
		int nrLinhaBloco1=0;
		int nrTotalLinha =0;
		
		HashMap<String, Object> conjuntoRegistro = new HashMap<String, Object>();
		conjuntoRegistro.put("0000", new Integer(0));
		conjuntoRegistro.put("0001", new Integer(0));
		conjuntoRegistro.put("0005", new Integer(0));
		conjuntoRegistro.put("0100", new Integer(0));
		conjuntoRegistro.put("0150", new Integer(0));
		conjuntoRegistro.put("0190", new Integer(0));
		conjuntoRegistro.put("0200", new Integer(0));
		conjuntoRegistro.put("0206", new Integer(0));
		conjuntoRegistro.put("0450", new Integer(0));
//		conjuntoRegistro.put("0460", new Integer(0));
		conjuntoRegistro.put("0990", new Integer(0));
		conjuntoRegistro.put("C001", new Integer(0));
		conjuntoRegistro.put("C100", new Integer(0));
		conjuntoRegistro.put("C110", new Integer(0));
		conjuntoRegistro.put("C113", new Integer(0));
		conjuntoRegistro.put("C114", new Integer(0));
		conjuntoRegistro.put("C120", new Integer(0));
		conjuntoRegistro.put("C140", new Integer(0));
		conjuntoRegistro.put("C141", new Integer(0));
		conjuntoRegistro.put("C160", new Integer(0));
		conjuntoRegistro.put("C170", new Integer(0));
		conjuntoRegistro.put("C190", new Integer(0));
		conjuntoRegistro.put("C350", new Integer(0));
		conjuntoRegistro.put("C370", new Integer(0));
		conjuntoRegistro.put("C390", new Integer(0));
		conjuntoRegistro.put("C400", new Integer(0));
		conjuntoRegistro.put("C405", new Integer(0));
		conjuntoRegistro.put("C420", new Integer(0));
		conjuntoRegistro.put("C425", new Integer(0));
		conjuntoRegistro.put("C460", new Integer(0));
		conjuntoRegistro.put("C470", new Integer(0));
		conjuntoRegistro.put("C490", new Integer(0));
//		conjuntoRegistro.put("C495", new Integer(0));
		conjuntoRegistro.put("C990", new Integer(0));
		conjuntoRegistro.put("D001", new Integer(0));
		conjuntoRegistro.put("D990", new Integer(0));
		conjuntoRegistro.put("E001", new Integer(0));
		conjuntoRegistro.put("E100", new Integer(0));
		conjuntoRegistro.put("E110", new Integer(0));
//		conjuntoRegistro.put("E111", new Integer(0));
//		conjuntoRegistro.put("E112", new Integer(0));
//		conjuntoRegistro.put("E113", new Integer(0));
//		conjuntoRegistro.put("E115", new Integer(0));
		conjuntoRegistro.put("E116", new Integer(0));
		conjuntoRegistro.put("E200", new Integer(0));
		conjuntoRegistro.put("E210", new Integer(0));
//		conjuntoRegistro.put("E220", new Integer(0));
//		conjuntoRegistro.put("E230", new Integer(0));
//		conjuntoRegistro.put("E240", new Integer(0));
		conjuntoRegistro.put("E250", new Integer(0));
//		conjuntoRegistro.put("E500", new Integer(0));
//		conjuntoRegistro.put("E510", new Integer(0));
//		conjuntoRegistro.put("E520", new Integer(0));
//		conjuntoRegistro.put("E530", new Integer(0));
		conjuntoRegistro.put("E990", new Integer(0));
		conjuntoRegistro.put("G001", new Integer(0));
		conjuntoRegistro.put("G990", new Integer(0));
		conjuntoRegistro.put("H001", new Integer(0));
		conjuntoRegistro.put("H005", new Integer(0));
		conjuntoRegistro.put("H990", new Integer(0));
		conjuntoRegistro.put("K001", new Integer(0));
		conjuntoRegistro.put("K990", new Integer(0));
		conjuntoRegistro.put("1001", new Integer(0));
		conjuntoRegistro.put("1010", new Integer(0));
		conjuntoRegistro.put("1300", new Integer(0));
		conjuntoRegistro.put("1310", new Integer(0));
		conjuntoRegistro.put("1320", new Integer(0));
		conjuntoRegistro.put("1350", new Integer(0));
		conjuntoRegistro.put("1360", new Integer(0));
		conjuntoRegistro.put("1370", new Integer(0));
		conjuntoRegistro.put("1600", new Integer(0));
		conjuntoRegistro.put("1990", new Integer(0));
		conjuntoRegistro.put("9001", new Integer(0));
//		conjuntoRegistro.put("9900", new Integer(1));
//		conjuntoRegistro.put("9990", new Integer(1));
//		conjuntoRegistro.put("9999", new Integer(1));
				
		float vlIcmsDebito = 0;
		float vlIcmsCredito = 0;
		
		ResultSetMap rsmApuracaoIcms = new ResultSetMap();
		
		//Os registros que precisarem de consulta de tabela, poderão ser vistos no ATO COTEPE/ICMS Nº 9, DE 18 DE ABRIL DE 2008
		//Retirei o uso de 'fill' nos campos por que em todos os exemplos de EFD que vi não havia necessidade de completar os campos ate
		//o tamanho maximo, apenas nao ultrapassa-lo
				
		boolean entrouDs  = false;
		boolean entrouDs2 = false;
				
// ******************************** BLOCO 0 ***************************************************************************************************************				
		// VALIDAÇÃO - EMITENTE    	
		String dsValidacao  = "";
		String dsValidacao2 = "";
		String tpPerfilEmpresa = "";
		Result resultado = new Result(2);
		ResultSetMap rsm  = new ResultSetMap();
		ResultSetMap rsm2 = new ResultSetMap();	
		
		/*
		 * REGISTRO 0000 - Abertura e identificação da entidade
		 */
		/************************************** VALIDACAO *******************************************************/
		    // CNPJ
	    if(cnpjEmpresa == null || cnpjEmpresa.trim().equals(""))
	    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CNPJ";
	    	// IBGE
	    if(cidadeEmpresa.getIdIbge()==null || cidadeEmpresa.getIdIbge().trim().equals(""))
	    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Cód. IBGE da cidade";
	    	// Estado
	    if(estadoEmpresa.getSgEstado() == null)
	    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Estado";
	    	//IE
	    if(ieEmpresa == null || ieEmpresa.trim().equals(""))
	    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Inscrição Estadual";
	    
	    if(!dsValidacao.equals("")){
	    	HashMap<String, Object> register = new HashMap<String, Object>();
	    	register.put("ERRO", "Dados do emitente faltando: "+dsValidacao);
	    	rsm.addRegister(register);
	    
	    	dsValidacao = "";
	    	
	    	entrouDs  = true;
		}
	    
	    /************************************** VALIDACAO 2 *******************************************************/
	    if(!Util.isCNPJ(cnpjEmpresa))
	    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" CNPJ (validação acusou ERRO nos digitos)";
	    if(!Util.isIEBahia(pessoaJuridica.getNrInscricaoEstadual()))//Verificar se o metodo esta funcionando
	    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" Inscrição Estadual (validação acusou ERRO nos digitos)";
	    if(!dsValidacao2.equals("")){
	    	
	    	HashMap<String, Object> register = new HashMap<String, Object>();
	    	register.put("ERRO", "Dados do emitente inválidos: "+dsValidacao2);
	    	rsm.addRegister(register);
	    
	    	dsValidacao2 = "";
	    	
	    	entrouDs2 = true;
	    }
	    String registro   = "";
	    if(!entrouDs && !entrouDs2){
		   tpPerfilEmpresa = (tpPerfil == 0) ? IND_PERFIL_A : (tpPerfil == 1) ? IND_PERFIL_B : IND_PERFIL_C; // TODO: Mudar depois
		    //Nivel hierarquico 1
			registro          = "|0000"+ 												  	 		 // REG - Texto Fixo contendo 0000
								"|003"+//Obrigatoriedade(Inicio) 01/01/2013                          // COD_VER - Tabela
//								"|010"+//Obrigatoriedade(Inicio) 01/01/2013                          // COD_VER - Tabela
								"|" +((tpRemessa == COD_FIN_ORIGINAL) ? COD_FIN_ORIGINAL : COD_FIN_SUBSTITUTO)+ // COD_FIN - Código da finalidade do arquivo
								"|" +Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+				 // DT_INI - Data inicial das informações
								"|" +Util.formatDateTime(dtSpedFinal, "ddMMyyyy")+  				 // DT_FIN - Data final das informações
								"|" +((pessoaJuridica.getNmRazaoSocial().trim().length() > 100) ? pessoaJuridica.getNmRazaoSocial().trim().substring(0, 99) : pessoaJuridica.getNmRazaoSocial().trim())+      // NOME - Nome Empresarial da entidade
								"|" +cnpjEmpresa+				                                     // CNPJ                                                        - OC
	//							"|" +Util.fillAlpha("", 11)+                         			 	 // CPF                                                         - OC
								"|"+//Refere-se a CPF - A Empresa emissora da EFD nunca terá CPF
								"|" +estadoEmpresa.getSgEstado()+              	                     // UF
								"|" +((ieEmpresa.length() == 9) ? ieEmpresa.substring(1) : ieEmpresa)+ 	                                                 // Inscrição Estadual
								"|" +cidadeEmpresa.getIdIbge()+ 	                                 // COD_MUN - Código do municipio pelo IBGE
								"|" + ((imEmpresa != null && !imEmpresa.trim().equals("")) ? imEmpresa : "")+	 // Inscrição Municipal                             - OC
	//							"|" +Util.fillAlpha("", 9) +										 // SUFRAMA                                                     - OC
								"|" +//Refere-se a SUFRAMA - A Empresa emissora da EFD não tem SUFRAMA
								"|" + tpPerfilEmpresa +	// IND_PERFIL - Perfil de apresentação do arquivo fiscal
								"|" +IND_ATIV_OUTROS+						 	 	                 // IND_ATIV - Indicador de atividade
								"|" +"\r\n";
			nrLinhaBloco0++;
			conjuntoRegistro.put("0000", (((Integer) conjuntoRegistro.get("0000")) + 1));
	    }
	    entrouDs = false;
	    entrouDs2 = false;
		/*
		 * REGISTRO 0001 - Abertura do bloco 0
		 */
		//Nivel hierarquico 1
		registro         +=   "|0001" + 										 // REG - Texto fixo contendo 0001
				              "|" +IND_MOV_DADOS+								 // IND_MOV - indicador de movimento
							  "|" +"\r\n";
		nrLinhaBloco0++;
		conjuntoRegistro.put("0001", (((Integer) conjuntoRegistro.get("0001")) + 1));
		/*
		 * REGISTRO 0005 - Dados complementares da entidade
		 */		
		String telefone = Util.retirarEspacos(empresaEndereco.getNrTelefone());	    
		/************************************** VALIDACAO *******************************************************/
		    // CEP
	    if(cepEmpresa == null || cepEmpresa.trim().equals(""))
	    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CEP";
	    if(empresaEndereco.getNmLogradouro()==null || empresaEndereco.getNmLogradouro().trim().equals(""))
	    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Logradouro";
	    	// Bairro
	    if(empresaEndereco.getNmBairro()==null || empresaEndereco.getNmBairro().trim().equals(""))
	    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Bairro";
		
	    if(!dsValidacao.equals("")){
	    	HashMap<String, Object> register = new HashMap<String, Object>();
	    	register.put("ERRO", "Dados do endereço do emitente faltando: "+dsValidacao);
	    	rsm.addRegister(register);
	    
	    	dsValidacao = "";
		    
	    	//entrouDs = true;
	    }
	    /************************************** VALIDACAO 2 *******************************************************/
	    // CEP
	    if(cepEmpresa.length() != 8)
	    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" CEP (Numero de digitos é inválido)";
	    // Telefone
	    if(telefone != null && (telefone.length() > 11))
	    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" Telefone (Numero de digitos ultrapassa o permitido)";
	    // Fax
	    if(pessoaJuridica.getNrFax() != null && pessoaJuridica.getNrFax().length() > 11)
	    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" Fax (Numero de digitos ultrapassa o permitido)";
	    // Email
	    if(empresa.getNmEmail() != null && empresa.getNmEmail().length() > 50)
	    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" Email (Numero de digitos ultrapassa o permitido)";
	    if(!dsValidacao2.equals("")){
	    
	    	HashMap<String, Object> register = new HashMap<String, Object>();
	    	register.put("ERRO", "Dados do endereço do emitente inválidos: "+dsValidacao2);
	    	rsm.addRegister(register);
	    
	    	dsValidacao2 = "";
	    		    	
	    	entrouDs2 = true;
	    }
	    	    
	    if(!entrouDs && !entrouDs2){
			//Nivel hierarquico 2
			registro += "|0005" + 													 	 // REG - Texto fixo contendo 0005
								  "|" +((empresa.getNmPessoa().trim().length() > 60) ? empresa.getNmPessoa().trim().substring(0, 59) : empresa.getNmPessoa().trim())+  // FANTASIA - Nome fantasia
								  "|" +empresaEndereco.getNrCep()+ // CEP
								  "|" +((empresaEndereco.getNmLogradouro().trim().length() > 60) ? empresaEndereco.getNmLogradouro().trim().substring(0, 59) : empresaEndereco.getNmLogradouro().trim())+			 // LOGRADOURO
								  "|" +((empresaEndereco.getNrEndereco() == null || empresaEndereco.getNrEndereco().trim().equals("") || empresaEndereco.getNrEndereco().trim().length() > 10) ? "" : Util.limparFormatos(empresaEndereco.getNrEndereco().trim()))+			 // Num - numero do imovel                                                                            - OC
								  "|" +((empresaEndereco.getNmComplemento() == null || empresaEndereco.getNmComplemento().trim().equals("")) ? "" :  (empresaEndereco.getNmComplemento().trim().length() > 60) ? empresaEndereco.getNmComplemento().trim().substring(0, 59) : empresaEndereco.getNmComplemento().trim())+		 // COMPLEMENTO                       - OC
								  "|" +((empresaEndereco.getNmBairro().trim().length() > 60) ? empresaEndereco.getNmBairro().trim().substring(0, 59) : empresaEndereco.getNmBairro().trim())+			 // BAIRRO
								  "|" +((telefone == null) ? "" : telefone)+			 // TELEFONE (DDD+FONE)                          																														  - OC
								  "|" +((pessoaJuridica.getNrFax() == null || pessoaJuridica.getNrFax().trim().equals("")) ? "" : pessoaJuridica.getNrFax().trim())+			         // FAX                              																																  - OC
								  "|" +((empresa.getNmEmail() == null || empresa.getNmEmail().trim().equals("")) ? "" : empresa.getNmEmail().trim())+	           	         // EMAIL                            																																			  - OC
								  "|" +"\r\n";
			nrLinhaBloco0++;
			conjuntoRegistro.put("0005", (((Integer) conjuntoRegistro.get("0005")) + 1));
	    }
	    entrouDs  = false;
	    entrouDs2 = false;
	    /*
		 * REGISTRO 0015 - DADOS DO CONTRIBUINTE SUBSTITUTO OU RESPONSÁVEL PELO ICMS DESTINO
		 * Não obrigatório para postos
		 */
		/*
		 * REGISTRO 0100 - Dados do contabilista
		 */
		
		//Informações do Contador
		int cdContador 		   = ParametroServices.getValorOfParametroAsInteger("CD_CONTADOR", 0, cdEmpresa);
		int cdEmpresaContadora = ParametroServices.getValorOfParametroAsInteger("CD_EMPRESA_CONTABILIDADE", 0, cdEmpresa);
		String nrCrcContador   = ParametroServices.getValorOfParametro("NR_CRC", null, cdEmpresa);
		
		if(cdContador == 0 || nrCrcContador == null){
			HashMap<String, Object> register = new HashMap<String, Object>();
	    	register.put("ERRO", "Parametros do contador não configurados!");
	    	rsm.addRegister(register);
	    
		}
		else{
			PessoaFisica pessoaFisicaContador = PessoaFisicaDAO.get(cdContador);
			if(pessoaFisicaContador == null){
				HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("ERRO", "Para o parâmetro de contador esta configurado uma pessoa Juridica. Corrija");
		    	rsm.addRegister(register);
			}
			else{
				PessoaEndereco pessoaEnderecoContador = PessoaEnderecoServices.getEnderecoPrincipal(cdContador);
				Cidade cidadeContador = CidadeDAO.get(pessoaEnderecoContador.getCdCidade());
				String nrCnpjEmpresaContabilidade = "";
				if(cdEmpresaContadora != 0)
					nrCnpjEmpresaContabilidade = PessoaJuridicaDAO.get(cdEmpresaContadora).getNrCnpj();
				String cpf = Util.limparFormatos(pessoaFisicaContador.getNrCpf()).trim();
				String cep = Util.limparFormatos(pessoaEnderecoContador.getNrCep()).trim();
				/************************************** VALIDACAO *******************************************************/
					//Nome
				if(pessoaFisicaContador.getNmPessoa() == null || pessoaFisicaContador.getNmPessoa().trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Nome";
			    	// CPF
			    if(cpf==null || cpf.equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CPF";
			    	// CRC
			    if(nrCrcContador==null || nrCrcContador.trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CRC";
				
			    if(cidadeContador == null)
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Cidade";
			    
			    if(pessoaFisicaContador.getNmEmail() == null || pessoaFisicaContador.getNmEmail().trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Email";
			    	
			    if(!dsValidacao.equals("")){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados do contador faltando: "+dsValidacao);
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	
			    	//entrouDs = true;
			    }
			    /************************************** VALIDACAO 2 *******************************************************/
			    // CPF
			    if(!Util.isCpfValido(cpf))
			    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" CPF (validação acusou ERRO nos digitos)";
			    // CRC
			    if(nrCrcContador.trim().length() > 15)
			    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" CRC (Numero de digitos ultrapassa o permitido)";
			    // CNPJ Empresa de Contabilidade
			    if(cdEmpresaContadora != 0 && nrCnpjEmpresaContabilidade != null && !Util.isCNPJ(nrCnpjEmpresaContabilidade))
			    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" CNPJ da Empresa de Contabilidade  (validação acusou ERRO nos digitos)";
			    // CEP
			    if(cep != null && cep.length() != 8)
			    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" CEP (Numero de digitos é diferente de 8)";
			    // ID Cidade
			    if(cidadeContador.getIdIbge() != null && cidadeContador.getIdIbge().length() != 7)
			    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" ID Cidade (Número de dígitos é diferente de 7)";
			     // Email
			    if(pessoaFisicaContador.getNmEmail() != null && pessoaFisicaContador.getNmEmail().length() > 50)
			    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" Email (Numero de digitos ultrapassa o permitido)";
			    if(!dsValidacao2.equals("")){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados do contador inválidos: "+dsValidacao2);
			    	rsm.addRegister(register);
			    
			    	dsValidacao2 = "";
			    	
			    	entrouDs2 = true;
			    }
			    
			    if(!entrouDs && !entrouDs2){
				    //Nivel hierarquico 2
					registro += "|0100" + 													 	 	  // REG - Texto fixo contendo 0100
										  "|" +((pessoaFisicaContador.getNmPessoa().trim().length() > 100) ? pessoaFisicaContador.getNmPessoa().trim().substring(0, 99) : pessoaFisicaContador.getNmPessoa().trim())+			  // NOME
										  "|" +cpf+ // CPF
										  "|" +nrCrcContador.trim()+			      							  // CRC
										  "|" +((nrCnpjEmpresaContabilidade == null || nrCnpjEmpresaContabilidade.equals("")) ? "" : nrCnpjEmpresaContabilidade)+			      		  // CNPJ Empresa Contabilidade                                                                                                                                                                    -OC
										  "|" +((cep == null || cep.equals("")) ? "" : cep)+// CEP																																																																						   -OC
										  "|" +((pessoaEnderecoContador.getNmLogradouro() == null || pessoaEnderecoContador.getNmLogradouro().trim().equals("")) ? "" : (pessoaEnderecoContador.getNmLogradouro().trim().length() > 60) ? pessoaEnderecoContador.getNmLogradouro().trim().substring(0, 59) : pessoaEnderecoContador.getNmLogradouro().trim())+		  // LOGRADOURO		   -OC
										  "|" +((pessoaEnderecoContador.getNrEndereco() == null || pessoaEnderecoContador.getNrEndereco().trim().equals("")) ? "" : (pessoaEnderecoContador.getNrEndereco().trim().length() > 60) ? pessoaEnderecoContador.getNrEndereco().trim().substring(0, 9) : pessoaEnderecoContador.getNrEndereco().trim())+		  // LOGRADOURO		   -OC
										  "|" +((pessoaEnderecoContador.getNmComplemento() == null || pessoaEnderecoContador.getNmComplemento().trim().equals("")) ? "" : (pessoaEnderecoContador.getNmComplemento().trim().length() > 60) ? pessoaEnderecoContador.getNmComplemento().trim().substring(0, 59) : pessoaEnderecoContador.getNmComplemento().trim())+		  // COMPLEMENTO   -OC
										  "|" +((pessoaEnderecoContador.getNmBairro() == null || pessoaEnderecoContador.getNmBairro().trim().equals("")) ? "" : (pessoaEnderecoContador.getNmBairro().trim().length() > 60) ? pessoaEnderecoContador.getNmBairro().trim().substring(0, 59) : pessoaEnderecoContador.getNmBairro().trim())+	  		  // BAIRRO		  					   -OC
										  "|" +((pessoaEnderecoContador.getNrTelefone() == null || Util.retirarEspacos(pessoaEnderecoContador.getNrTelefone()).equals("") || Util.retirarEspacos(pessoaEnderecoContador.getNrTelefone()).length() > 11) ? "" : Util.retirarEspacos(pessoaEnderecoContador.getNrTelefone()))+		  // TELEFONE				   						   -OC
										  "|" +((pessoaFisicaContador.getNrFax() == null || pessoaFisicaContador.getNrFax().trim().equals("")) ? "" : (pessoaFisicaContador.getNrFax().trim().length() > 60) ? pessoaFisicaContador.getNrFax().trim().substring(0, 59) : pessoaFisicaContador.getNrFax().trim())+			      // FAX												   -OC
										  "|" +((pessoaFisicaContador.getNmEmail() == null || pessoaFisicaContador.getNmEmail().trim().equals("")) ? "" : pessoaFisicaContador.getNmEmail().trim())+		      // EMAIL																																								   -OC
										  "|" +((cidadeContador.getIdIbge() == null || cidadeContador.getIdIbge().trim().equals("")) ? "" : cidadeContador.getIdIbge().trim())+ 	  // COD_MUN - IBGE																																													   -OC
										  "|" +"\r\n";
					nrLinhaBloco0++;
					conjuntoRegistro.put("0100", (((Integer) conjuntoRegistro.get("0100")) + 1));
			    }
			    entrouDs  = false;
			    entrouDs2 = false;
			}
		}
		/*
		 * REGISTRO 0150 - Tabela de cadastro do participante
		 */
		ArrayList<Integer> codigosParticipante = getParticipantesDoPeriodo(cdEmpresa, dtSpedInicial, dtSpedFinal); 
		for(int i = 0; i < codigosParticipante.size(); i++){
			
			//Informações dos Participantes
			int cdParticipante = codigosParticipante.get(i);
			Pessoa pessoaParticipante = PessoaDAO.get(cdParticipante);
			PessoaJuridica pessoaJuridicaParticipante = null;
			String cnpjParticipante = null;
			String ieParticipante = null;
			PessoaFisica   pessoaFisicaParticipante = null;
			String cpfParticipante = null;
			if(pessoaParticipante.getGnPessoa() == 0){
				pessoaJuridicaParticipante = PessoaJuridicaDAO.get(cdParticipante);
				cnpjParticipante = Util.limparFormatos(pessoaJuridicaParticipante.getNrCnpj()).trim();
				ieParticipante = Util.limparFormatos(pessoaJuridicaParticipante.getNrInscricaoEstadual()).trim();
				ieParticipante = Util.apenasNumeros(ieParticipante);
			}
			else{
				pessoaFisicaParticipante = PessoaFisicaDAO.get(cdParticipante);
				cpfParticipante = Util.limparFormatos(pessoaFisicaParticipante.getNrCpf()).trim();
			}
			String codPais = "1058";
			PessoaEndereco pessoaEnderecoParticipante = PessoaEnderecoServices.getEnderecoPrincipal(cdParticipante);
			Cidade cidadeParticipante = null;
			Estado estadoParticipante = null;
			Pais paisParticipante = null;
			
			if(pessoaEnderecoParticipante != null){
				cidadeParticipante = CidadeDAO.get(pessoaEnderecoParticipante.getCdCidade());
				if(cidadeParticipante != null)
					estadoParticipante = EstadoDAO.get(cidadeParticipante.getCdEstado());
				if(estadoParticipante != null)
					paisParticipante = PaisDAO.get(estadoParticipante.getCdPais());
//				if(paisParticipante != null)
//					codPais = paisParticipante.getIdPais();
			}
			String idIbgeParticipante = null;
			/************************************** VALIDACAO *******************************************************/
			//Endereço
			if(pessoaEnderecoParticipante == null)
				dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Endereço";
			else{
				//Cidade
				if(cidadeParticipante == null)
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Cidade";
				else 
					idIbgeParticipante = Util.limparFormatos(cidadeParticipante.getIdIbge()).trim();
				// Logradouro
			    if(pessoaEnderecoParticipante.getNmLogradouro()==null || pessoaEnderecoParticipante.getNmLogradouro().trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Logradouro ";
			}
			//IBGE
			if(paisEmpresa != null && paisParticipante != null && paisEmpresa.getCdPais() == paisParticipante.getCdPais() && idIbgeParticipante == null)
				dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" ID IBGE";
			//CNPJ
		    if(paisEmpresa != null && paisParticipante != null && paisEmpresa.getCdPais() == paisParticipante.getCdPais() && pessoaParticipante.getGnPessoa() == 0 && (cnpjParticipante == null || cnpjParticipante.equals("")))
		    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CNPJ ";
		    //CPF
		    if(paisParticipante != null && pessoaParticipante.getGnPessoa() == 1 && (cpfParticipante == null || cpfParticipante.equals("")))
		    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CPF ";
		    if(!dsValidacao.equals("")){
		    	HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("ERRO", "Dados do participante "+pessoaParticipante.getNmPessoa()+" faltando: "+dsValidacao);
		    	rsm.addRegister(register);
		    
		    	dsValidacao = "";
		    	
		    	entrouDs = true;
		    }
		    /************************************** VALIDACAO 2 *******************************************************/
		    // CPF
		    if(cpfParticipante !=null && !Util.isCpfValido(cpfParticipante))
		    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" CPF do participante " + pessoaParticipante.getNmPessoa() + " (validação acusou ERRO nos digitos)";
		    // CNPJ
		    if(paisEmpresa != null && paisParticipante != null && paisEmpresa.getCdPais() == paisParticipante.getCdPais() && cnpjParticipante != null && !Util.isCNPJ(cnpjParticipante))
		    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" CNPJ do participante " + pessoaParticipante.getNmPessoa() + " (validação acusou ERRO nos digitos)";
		    // ID Municipio
		    if(paisEmpresa != null && paisParticipante != null && paisEmpresa.getCdPais() == paisParticipante.getCdPais() && idIbgeParticipante != null && idIbgeParticipante.length() != 7)
		    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" ID do Municipio do participante " + pessoaParticipante.getNmPessoa() + " (tamanho diferente de 7)";
		    if(pessoaEnderecoParticipante != null){
		    	// IE
		    	Cidade cidade = CidadeDAO.get(pessoaEnderecoParticipante.getCdCidade());
		    	Estado estado = null;
		    	if(cidade != null)
		    		estado = EstadoDAO.get(cidade.getCdEstado());
//		    	if(estado != null && estado.getSgEstado().equals("BA")){
//			    	if(estado != null && ieParticipante != null && !ieParticipante.trim().equals("") && estado.getSgEstado().equals("BA") && !Util.isIEBahia(ieParticipante))//Conferir para saber se a biblioteca faz a validação correta e colocar para outros estados
//				    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" IE do participante " + pessoaParticipante.getNmPessoa() + " (validação acusou ERRO nos digitos)";
//			    	if(estado == null && ieParticipante != null && !ieParticipante.trim().equals(""))//Conferir para saber se a biblioteca faz a validação correta e colocar para outros estados
//				    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" Estado do participante";
//		    	}
//		    	else{
//		    		ieParticipante = null;
//		    	}
			    // Numero do Endereço
			    if(pessoaEnderecoParticipante.getNrEndereco() != null && pessoaEnderecoParticipante.getNrEndereco().trim().length() > 10)
			    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" Nº do imóvel do participante " + pessoaParticipante.getNmPessoa() + " (tamanho maior do que 10)";
			    // Complemento
			    if(pessoaEnderecoParticipante.getNmComplemento()!=null && pessoaEnderecoParticipante.getNmComplemento().trim().length() > 60)
			    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" Complemento do Endereço do participante " + pessoaParticipante.getNmPessoa() + " (tamanho maior do que 60)";
			    // Bairro
			    if(pessoaEnderecoParticipante.getNmBairro()!=null && pessoaEnderecoParticipante.getNmBairro().trim().length() > 60)
			    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" Bairro do participante " + pessoaParticipante.getNmPessoa() + " (tamanho maior do que 60)";
		    }
		    if(!dsValidacao2.equals("")){
		    	HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("ERRO", "Dados do participante "+pessoaParticipante.getNmPessoa()+" inválidos: "+dsValidacao2);
		    	rsm.addRegister(register);
		    
		    	dsValidacao2 = "";
		    	
		    	entrouDs2 = true;
		    }
		    if(!entrouDs && !entrouDs2){
				registro += "|0150" + 															  //REG - Texto fixo contendo 0150
								 	  "|" +pessoaParticipante.getCdPessoa()+				  //COD_PART
									  "|" +((pessoaParticipante.getNmPessoa().trim().length() > 100) ? pessoaParticipante.getNmPessoa().trim().substring(0, 99) : pessoaParticipante.getNmPessoa().trim())+			  //NOME
									  "|" +codPais+			      							  //Cod país
									  ((pessoaParticipante.getGnPessoa() == 0) ?
									  "|" +cnpjParticipante + "|" : //CNPJ								        																																				  -OC
									  "||"+cpfParticipante	//CPF																																																  -OC
									  ) +
									  "|" +((ieParticipante != null) ? ieParticipante : "")+	//Inscrição estadual																																			  -OC
									  "|" +((idIbgeParticipante != null) ? idIbgeParticipante: "")+  //COD_MUN																																					  -OC
									  "|" +//Suframa																																																			  -OC
									  "|" +((pessoaEnderecoParticipante != null  && pessoaEnderecoParticipante.getNmLogradouro() != null ? (pessoaEnderecoParticipante.getNmLogradouro().trim().length() > 60) ? pessoaEnderecoParticipante.getNmLogradouro().trim().substring(0, 59) : pessoaEnderecoParticipante.getNmLogradouro().trim() : ""))+	  //Logradouro
									  "|" +((pessoaEnderecoParticipante != null  && pessoaEnderecoParticipante.getNrEndereco() != null) ? pessoaEnderecoParticipante.getNrEndereco().trim() : "")+	  //Numero do imovel												                                  -OC
									  "|" +((pessoaEnderecoParticipante != null  && pessoaEnderecoParticipante.getNmComplemento() != null) ? pessoaEnderecoParticipante.getNmComplemento().trim() : "")+  //Complemento														                          -OC
									  "|" +((pessoaEnderecoParticipante != null  && pessoaEnderecoParticipante.getNmBairro() != null) ? pessoaEnderecoParticipante.getNmBairro().trim() : "")+		  //Bairro  														                                  -OC
									  "|" +"\r\n";
				nrLinhaBloco0++;
				conjuntoRegistro.put("0150", (((Integer) conjuntoRegistro.get("0150")) + 1));
		    }
		    entrouDs  = false;
		    entrouDs2 = false;
		}
		
		/*
		 * REGISTRO 0175: ALTERAÇÃO DA TABELA DE CADASTRO DE PARTICIPANTE
		 */
		
		/*
		 * REGISTRO 0190 - Identificação das unidades de medidas
		 */
		Result resultadoUni = getUnidadesMedidaDoPeriodo(cdEmpresa, dtSpedInicial, dtSpedFinal);
		ArrayList<Integer> codigosUnidadesMedida = (ArrayList<Integer>) resultadoUni.getObjects().get("ARRAY");
		for(int i = 0; i < codigosUnidadesMedida.size(); i++){
			int cdUnidadeMedida = codigosUnidadesMedida.get(i);
			UnidadeMedida unidadeMedida = UnidadeMedidaDAO.get(cdUnidadeMedida);
			/************************************** VALIDACAO *******************************************************/
			//Descrição
			if(unidadeMedida.getNmUnidadeMedida()==null || unidadeMedida.getNmUnidadeMedida().trim().equals(""))
		    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Descrição ";
		    
		    if(!dsValidacao.equals("")){
		    	HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("ERRO", "Dados da unidade de medida "+unidadeMedida.getNmUnidadeMedida()+" faltando: "+dsValidacao);
		    	rsm.addRegister(register);
		    
		    	dsValidacao = "";
		    	
		    	//entrouDs = true;
		    }
		    if(!entrouDs && !entrouDs2){
			    registro +=      	"|" +"0190" + 															  //REG - Texto fixo contendo 0190
										"|" +unidadeMedida.getSgUnidadeMedida()+				  //UNID - Codigo da unidade de medida
						  				"|" +"Unidade " + unidadeMedida.getSgUnidadeMedida() +			  //DESCR - Descrição da unidade de medida
						  				"|" +"\r\n";
				nrLinhaBloco0++;
				conjuntoRegistro.put("0190", (((Integer) conjuntoRegistro.get("0190")) + 1));
		    }
		    
		    //entrouDs = false;
		}
		/*
		 * Registro 0200 - Tabela de identificação do item 
		 */		
		ResultSetMap rsmProds = (ResultSetMap) resultadoUni.getObjects().get("RSM");
		while(rsmProds.next()){
			int cdProdutoServico = rsmProds.getInt("CD_PRODUTO_SERVICO");
//			int cdDocumentoSaida = rsmDocItem.getInt("CD_DOCUMENTO_SAIDA");
//			int cdItem 			 = rsmDocItem.getInt("CD_ITEM");
			String prAliquota 	 = Util.formatNumber(rsmProds.getFloat("PR_ALIQUOTA"), "#.##");
			String codBarras     = rsmProds.getString("ID_PRODUTO_SERVICO");
			ProdutoServicoEmpresa produtoEmpresa = ProdutoServicoEmpresaDAO.get(cdEmpresa, cdProdutoServico);
			ProdutoServico produto = ProdutoServicoDAO.get(cdProdutoServico);
			Ncm ncm = NcmDAO.get(produto.getCdNcm());
//			ResultSetMap rsmAliquota = DocumentoSaidaItemServices.getAllAliquotas(cdDocumentoSaida, cdProdutoServico, cdItem);
//			float prAliquota = 0;
//			while(rsmAliquota.next()){
//				prAliquota = rsmAliquota.getFloat("pr_aliquota_tributo");
//			}
			
			/************************************** VALIDACAO *******************************************************/
			//Unidade de Medida
			if(produtoEmpresa == null || produtoEmpresa.getCdUnidadeMedida()==0)
		    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Unidade de medida ";
		    
		    if(!dsValidacao.equals("")){
		    	HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("ERRO", "Dados do produto "+produto.getNmProdutoServico() + " - " + codBarras+" faltando: "+dsValidacao);
		    	rsm.addRegister(register);
		    
		    	dsValidacao = "";
		    	
		    	entrouDs = true;
		    }
		    /************************************** VALIDACAO 2 *******************************************************/
		    if(ncm != null && ncm.getNrNcm().length() != 8)
		    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" NCM do produto " + produto.getNmProdutoServico() + " - " + codBarras + " (Número de digitos é diferente de 8)";
		    //Codigo de Barras
		    if(!dsValidacao2.equals("")){
		    	HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("ERRO", "Dados dos produtos inválidos: "+dsValidacao2);
		    	rsm.addRegister(register);
		    
		    	dsValidacao2 = "";
		    	
		    	entrouDs2 = true;
		    }
		    if(!entrouDs && !entrouDs2){
				registro += 	        "|" +"0200" +															  //REG 
										"|" +(rsmProds.getString("COD_ITEM") != null ? rsmProds.getString("COD_ITEM") : produto.getCdProdutoServico())+	  //Cód produto
										"|" +((produto.getNmProdutoServico().trim().length() > 255) ? produto.getNmProdutoServico().trim().substring(0, 254) : produto.getNmProdutoServico().trim())+	  //DESCRICAO
										"|" +((produto.getIdProdutoServico() != null) ? produto.getIdProdutoServico().trim() : "")+	  //Código de barra																			- OC			
										"|" +	  							  //Código de barra anterior																													- OC
										"|" +UnidadeMedidaDAO.get(produtoEmpresa.getCdUnidadeMedida()).getSgUnidadeMedida()+	  //Unidade de medida
										"|" +"00"+//Mercadoria para Revenda - Ver Tabela	 							 //Tipo do item
										"|" +((ncm == null) ? "" : ncm.getNrNcm())+	 //Código da nomeclatura comum mercosul																			- OC
										"|" + 							 //Código da nomeclatura conforme TIPI																												- OC
										"|" +((ncm != null) ? ncm.getNrNcm().substring(0, 2) : "")+	 							 //Código do genero do item - Tabela                        								- OC
										"|" +	 							 //Código do serviço																															- OC
	//									"|" +((prAliquota != 0) ? prAliquota : "")+	 							 //Aliquota do icms	                                       													- OC   
										"|" +prAliquota+	 							 //Aliquota do icms	 - OC
//										"|" +	 							 //CEST O campo CEST é válido a partir de 01/01/2017.
										"|" +"\r\n";
				nrLinhaBloco0++;
				conjuntoRegistro.put("0200", (((Integer) conjuntoRegistro.get("0200")) + 1));
		    }
			
		    entrouDs  = false;
		    entrouDs2 = false;
		    
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("CD_PRODUTO_SERVICO", Integer.toString(cdProdutoServico), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmProdGrup = ProdutoGrupoDAO.find(criterios);
			ArrayList<Integer> cdsGrupos = new ArrayList<Integer>();
			while(rsmProdGrup.next()){
				cdsGrupos.add(rsmProdGrup.getInt("cd_grupo"));
			}
			
			boolean isCombustivel = false;
			
			for(int j = 0; j < cdsGrupos.size(); j++){
				int cdGrupo = cdsGrupos.get(j);
				if(cdGrupo == com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0)){
					isCombustivel = true;
					break;
				}
			}
			
			/*
			 * REGISTRO 0205: ALTERAÇÃO DO ITEM
			 */
			
			/*
			 * Registro 0206 - Código de produtos conforme tabela publicada pela ANP
			 */
			if(isCombustivel){
				
				ProdutoServico 	produtoCombus	= produto;
				/************************************** VALIDACAO *******************************************************/
				//Codigo da tabela ANP
				if(produtoCombus.getIdProdutoServico()==null || produtoCombus.getIdProdutoServico().trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Codigo (ANP) ";
			    
			    if(!dsValidacao.equals("")){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados do produto "+produtoCombus.getNmProdutoServico()+" faltando: "+dsValidacao);
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    
			    	//entrouDs = true;
			    }
			    if(!entrouDs && !entrouDs2){
					registro += 	"|0206" +//Necessario vir logo depois do registro pai, por isso utilizar a mesma String de registro				  //REG 
											"|" +produtoCombus.getIdProdutoServico()+	  //Saber se o idProdutoServico para combustiveis esta de acordo a tabela ANP
											"|" +"\r\n";
					
					nrLinhaBloco0++;
					conjuntoRegistro.put("0206", (((Integer) conjuntoRegistro.get("0206")) + 1));
			    }
			    entrouDs  = false;			    
			}
		}		
		
		/*
		 * REGISTRO 0210: CONSUMO ESPECÍFICO PADRONIZADO
		 */
		
		/*
		 * REGISTRO 0220: FATORES DE CONVERSÃO DE UNIDADES
		 */
		
		/*
		 * REGISTRO 0300: CADASTRO DE BENS OU COMPONENTES DO ATIVO IMOBILIZADO
		 */
		
		/*
		 * REGISTRO 0305: INFORMAÇÃO SOBRE A UTILIZAÇÃO DO BEM
		 */
		
		/*
		 * Registro 0400 - Tabela de natureza da operação
		 */
		// TODO validar código 0400
//		String _0400 = "";
//		for(int i = 0; i < codigosNatOperacao.size(); i++){
//			int cdNaturezaOperacao = codigosNatOperacao.get(i);
//			NaturezaOperacao naturezaOperacao = NaturezaOperacaoDAO.get(cdNaturezaOperacao);
//			registro = 	       "0400" +															  //REG 
//									Util.fillAlpha(String.valueOf(naturezaOperacao.getCdNaturezaOperacao()), 10)+	  //Cód produto
//									Util.fillAlpha(naturezaOperacao.getNmNaturezaOperacao(),100)+
//									"\r\n";
//			nrLinhaBloco0++;
//		}
		ArrayList<Integer> cdNotasFiscais = getNotasFiscaisDoPeriodo(cdEmpresa, dtSpedInicial, dtSpedFinal);
		//INSERIR ESSES REGISTROS PARA DOCUMENTO DE SAIDA/ENTRADA
		
		for(int i = 0; i < cdNotasFiscais.size(); i++){
			int cdNotaFiscal = cdNotasFiscais.get(i);
			NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal);
			if(nota != null && nota.getTxtObservacao() != null && !nota.getTxtObservacao().equals("")){
				/*
				 * Registro 0450 - TABELA DE INFORMAÇÃO COMPLEMENTAR DO DOCUMENTO FISCAL
				 */
				String txtComplementar = NfeServices.removeAcentos(nota.getTxtObservacao().trim());
		    	txtComplementar = (txtComplementar.trim().length() > 255 ? txtComplementar.trim().substring(0, 255) : txtComplementar.trim());
				registro += 	        "|" +"0450" +															  //REG 
										"|" +nota.getCdNotaFiscal()+	  //Cód produto
										"|" +txtComplementar.trim()+
										"|" +"\r\n";
				nrLinhaBloco0++;
				conjuntoRegistro.put("0450", (((Integer) conjuntoRegistro.get("0450")) + 1));
			
			}
			
//			/*
//			 * Registro 0460 - TABELA DE OBSERVAÇÕES DO LANÇAMENTO FISCAL
//			 */
//			registro += 	        "|" +"0460" +															  //REG 
//									"|" +nota.getCdNotaFiscal()+	  //Cód produto
//									"|" +((nota.getTxtObservacao().trim().length() > 100) ? nota.getTxtObservacao().trim().substring(0, 99) : nota.getTxtObservacao().trim())+
//									"|" +"\r\n";
//			nrLinhaBloco0++;
//			conjuntoRegistro.put("0460", (((Integer) conjuntoRegistro.get("0460")) + 1));
			
		}		
//		/*
//		 * Registro 0500 - Plano de contas contabeis                           - RELATIVO AO REGISTRO 0300
//		 */
//		String 0500 =  "0500"+																 //REG
//							   Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+				 		//Data de inclusão ou alteração
//							   Util.fillNum(NAT_CONTA_ATIVOS, 2)+	 							 	//Código da natureza da conta
//							   Util.fillAlpha(IND_CTA_ANALITICA, 1)+	 							//INDICADOR TIPO CONTA
//							   Util.fillNum(10, 5)+								    				//Nivel da conta
//							   Util.fillAlpha("215454", 60)+	 							//Codigo tipo conta
//							   Util.fillAlpha("CONTA CAIXA", 60)+	 							//Nome da conta
//							   "\r\n";
//		nrLinhaBloco0++;
	
//		/*
//		 * Registro 0600 - Centro de custos                                    - RELATIVO AO REGISTRO 0305
//		 */
//		String 0600 = "0600"+																	//REG
//							  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+				 		//Data de inclusão ou alteração
//							  Util.fillAlpha("215454", 60)+	 									//Codigo do centro de custos
//							  Util.fillAlpha("CENTRO CUSTO", 60)+	 							//Nome do centro de custos
//							  "\r\n";
//		nrLinhaBloco0++;
		
		
		/*
		 * Registro 0990 - Encerramento do bloco 0
		 */
		
		nrLinhaBloco0++;//O registro 0990 também entra na conta
		
		registro +=  "|" +"0990" + 													//REG
							   "|" +nrLinhaBloco0+						//Quantidade de linhas
							   "|" +"\r\n";
		conjuntoRegistro.put("0990", (((Integer) conjuntoRegistro.get("0990")) + 1));
		
// ******************************** BLOCO C ***************************************************************************************************************				
		
		/*
		 * BLOCO C - C001 - Abertura
		 */
		//SAIDA
		ArrayList<Integer> codigosDocumentoSaida01 = new ArrayList<Integer>();
		codigosDocumentoSaida01 = getDocumentoEntradaSaida01DoPeriodo(cdEmpresa, dtSpedInicial, dtSpedFinal, 0);
		ArrayList<Integer> codigosDocumentoEntrada01 = new ArrayList<Integer>();
		codigosDocumentoEntrada01 = getDocumentoEntradaSaida01DoPeriodo(cdEmpresa, dtSpedInicial, dtSpedFinal, 1);
		ArrayList<Integer> codigosNotaFiscal = new ArrayList<Integer>();
		codigosNotaFiscal = getNotasFiscaisDoPeriodo(cdEmpresa, dtSpedInicial, dtSpedFinal);
		ArrayList<Integer> codigosDocumentoSaidaSemECF = new ArrayList<Integer>();
		codigosDocumentoSaidaSemECF = getDocumentoSaida02DoPeriodo(cdEmpresa, dtSpedInicial, dtSpedFinal);
		
		registro +=  "|" +"C001" + 													 // REG - Texto fixo contendo 0001
				 			   "|" +((codigosDocumentoSaida01.size() > 0 || codigosDocumentoEntrada01.size() > 0 || codigosNotaFiscal.size() > 0 || codigosDocumentoSaidaSemECF.size() > 0) ? IND_MOV_DADOS : IND_MOV_SEM_DADOS)+							 // IND_MOV - indicador de movimento
							   "|" +"\r\n";
		nrLinhaBlocoC++;
		conjuntoRegistro.put("C001", (((Integer) conjuntoRegistro.get("C001")) + 1));
				
		/*
		 * C100 - Nota Fiscal
		 */
				
		ArrayList<String> registroSaida = new ArrayList<String>();
		for(int i = 0; i < codigosDocumentoSaida01.size(); i++){
			//Código do documento
			int cdDocumentoSaida = codigosDocumentoSaida01.get(i);
			//Documento
			DocumentoSaida documento = DocumentoSaidaDAO.get(cdDocumentoSaida);
			//Numero do documento
			int nrDoc = 0;
			if(documento.getNrDocumentoSaida() != null && !documento.getNrDocumentoSaida().trim().equals("")){
				nrDoc = Integer.parseInt(Util.limparFormatos(documento.getNrDocumentoSaida(), 'N'));
			}
			String nrDocumento = ((nrDoc > 999999999) ? String.valueOf(nrDoc).substring(0, 9) : String.valueOf(nrDoc));
			//Valor do documento
			float vlTotalDocumento = DocumentoSaidaServices.getValorAllItens(cdDocumentoSaida);
			
			//Situação do documento
			String stDocumentoFiscal = ST_DOCUMENTO_REGULAR;
			if(documento.getStDocumentoSaida() == 2){
				stDocumentoFiscal = ST_DOCUMENTO_CANCELADO;
			}
//			//Código do documento se pertencer a uma nota fiscal eletronica - SERA ALTERADO PARA QUANDO O SISTEMA PUDER IMPORTAR NOTAS FISCAIS ELETRONICAS E ARMAZENA-LAS COMO NOTA FISCAL ELETRONICA
//			int cdNfe    = (documento.getTpDocumentoSaida() == 6) ? cdDocumentoSaida : 0;
			//Código do documento se pertencer a uma nota fiscal
//			int cdNota1A = (documento.getTpDocumentoSaida() == 0) ? cdDocumentoSaida : 0;
//			NotaFiscal nfe = null;
//			if(cdNfe > 0){
//				nfe = NotaFiscalDAO.get(cdNfe);
//				if(nfe.getStNotaFiscal() == NotaFiscalServices.CANCELADA)
//					stDocumentoFiscal = ST_DOCUMENTO_CANCELADO;
//				else if(nfe.getStNotaFiscal() == NotaFiscalServices.DENEGADA)
//					stDocumentoFiscal = ST_NFE_DENEGADO;
//			}
			
			//Contas a Receber pertencentes ao documento
			ArrayList<Integer> contasAReceber  = getContasAReceber(cdDocumentoSaida);
			
			//Selecionar o Tipo de Pagamento
			int tpPagamento = -1;
			if(contasAReceber.size() == 1)
				tpPagamento = IND_PAGTO_AVISTA;
			else
				tpPagamento = IND_PAGTO_APRAZO;
			
			//Valor Total do Itens
			float vlProd = DocumentoSaidaServices.getValorAllItens(cdDocumentoSaida);
			
			//Valor de Desconto do documento
			float vlDescontos = DocumentoSaidaServices.getValorDescontosAllItens(cdDocumentoSaida);
			
			//Valor das Aliquotas, Base e ICMS
//			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>(); 
//			criterios.add(new ItemComparator("CD_DOCUMENTO_SAIDA", Integer.toString(cdDocumentoSaida), ItemComparator.EQUAL, Types.INTEGER));
//			ResultSetMap rsmSaidaAliquota = SaidaTributoDAO.find(criterios);
			float vlBaseICMS = 0;
			float vlICMS = 0;
			float vlBaseICMSSub = 0;
			float vlICMSSub = 0;
			
			float vlBaseIPI = 0;
			float vlIPI = 0;
			float vlBaseIPISub = 0;
			float vlIPISub = 0;
			
			float vlBasePIS = 0;
			float vlPIS = 0;
			float vlBasePISSub = 0;
			float vlPISSub = 0;
			
			float vlBaseCOFINS = 0;
			float vlCOFINS = 0;
			float vlBaseCOFINSSub = 0;
			float vlCOFINSSub = 0;
//			while(rsmSaidaAliquota.next()){
//				vlBaseICMS += rsmSaidaAliquota.getFloat("vl_base_calculo"); 
//				vlICMS += rsmSaidaAliquota.getFloat("vl_tributo"); 
//			}
			
			//ResultSetMap com os itens do documento
			ResultSetMap rsmProdutos = DocumentoSaidaServices.getAllItens(cdDocumentoSaida, true);
			int erroAliq = 0;
			
			if(rsmProdutos.next()){
				vlICMS  	  = rsmProdutos.getFloat("vl_icms_documento");
				vlBaseICMS 	  = rsmProdutos.getFloat("vl_base_icms_documento");
				vlBaseICMSSub = rsmProdutos.getFloat("vl_base_icms_substituto_documento");
				vlICMSSub 	  = rsmProdutos.getFloat("vl_icms_substituto_documento");
				
				vlBaseIPI 	  = rsmProdutos.getFloat("vl_base_ipi_documento");
				vlIPI 		  = rsmProdutos.getFloat("vl_ipi_documento");
				vlBaseIPISub  = rsmProdutos.getFloat("vl_base_ipi_substituto_documento");
				vlIPISub 	  = rsmProdutos.getFloat("vl_ipi_substituto_documento");
				
				vlBasePIS 	  = rsmProdutos.getFloat("vl_base_pis_documento");
				vlPIS 		  = rsmProdutos.getFloat("vl_pis_documento");
				vlBasePISSub  = rsmProdutos.getFloat("vl_base_pis_substituto_documento");
				vlPISSub 	  = rsmProdutos.getFloat("vl_pis_substituto_documento");
				
				vlBaseCOFINS 	  = rsmProdutos.getFloat("vl_base_cofins_documento");
				vlCOFINS 		  = rsmProdutos.getFloat("vl_cofins_documento");
				vlBaseCOFINSSub  = rsmProdutos.getFloat("vl_base_cofins_substituto_documento");
				vlCOFINSSub 	  = rsmProdutos.getFloat("vl_cofins_substituto_documento");
				
				erroAliq = rsmProdutos.getInt("ErroAliq");
			}
			else{
				HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("ERRO", "Documento Nº "+documento.getNrDocumentoSaida()+" sem produtos!");
		    	rsm.addRegister(register);
		    
		    	dsValidacao = "";
		    	
		    	continue;
			}
//			//Natureza de Operacação do documento
//			NaturezaOperacao natOp = NaturezaOperacaoDAO.get(documento.getCdNaturezaOperacao());
			
//			//Resumo dos tributos do documento
//			criterios = new ArrayList<ItemComparator>(); 
//			criterios.add(new ItemComparator("CD_DOCUMENTO_SAIDA", Integer.toString(cdDocumentoSaida), ItemComparator.EQUAL, Types.INTEGER));
//			ResultSetMap rsmSaidaTrib = SaidaTributoDAO.find(criterios);
//			SaidaTributo saidaTrib = null;
//			if(rsmSaidaTrib.next()){
//				saidaTrib = SaidaTributoDAO.get(cdDocumentoSaida, rsmSaidaTrib.getInt("cd_tributo"));
//			}
			
			/************************************** VALIDACAO *******************************************************/
			//Codigo do cliente
			if(String.valueOf(documento.getCdCliente())==null || String.valueOf(documento.getCdCliente()).trim().equals(""))
		    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Codigo do cliente ";
			//Numero do Documento
			if(documento.getNrDocumentoSaida() ==null || documento.getNrDocumentoSaida().trim().equals(""))
		    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Numero ";
//			//Chave da Nota Fiscal Eletronica - APRESENTAR
//			if(nfe != null){
//				if(nfe.getNrChaveAcesso() == null || nfe.getNrChaveAcesso().trim().equals(""))
//					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Chave de Acesso da NFe";
//			}
			//Data de Emissao do Documento
			if(documento.getDtEmissao() == null || String.valueOf(documento.getDtEmissao()).trim().equals(""))
		    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Data de emissão ";
			
			//Valor Total do Documento
			if(String.valueOf(documento.getVlTotalDocumento()) == null || String.valueOf(documento.getVlTotalDocumento()).trim().equals(""))
		    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor total ";
			
			if(!dsValidacao.equals("")){
		    	HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("ERRO", "Dados do documento " + documento.getNrDocumentoSaida() + " faltando: "+dsValidacao);
		    	rsm.addRegister(register);
		    
		    	dsValidacao = "";
		    	
		    	//entrouDs = true;
		    }
		    
			
			
		    /*Validacao*/
		    boolean duplicidade = false;
		    String registroAtual = IND_OPER_SAIDA + nrDocumento + "01" + stDocumentoFiscal;
		    for(int ind = 0; ind < registroSaida.size(); ind++){
		    	if(registroAtual.equals(registroSaida.get(ind))){
		    		duplicidade = true;
		    		break;
		    	}
		    }
		    
		    if(duplicidade){
		    	HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("ERRO", "Dados do documento de saida " + documento.getNrDocumentoSaida() + ": Duplicidade com outro documento.");
		    	rsm.addRegister(register);
		    	//entrouDs = true;
		    }
		    else{
		    	registroSaida.add(registroAtual);
		    }
		    
		    GregorianCalendar data = new GregorianCalendar();
		    data.set(Calendar.YEAR, 2000);
		    data.set(Calendar.MONTH, 0);
		    data.set(Calendar.DAY_OF_MONTH, 1);
		    if(documento.getDtDocumentoSaida() == null || documento.getDtDocumentoSaida().before(data)){
		    	HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("ERRO", "Dados do documento de saida " + documento.getNrDocumentoSaida() + ": Data do documento não pode ser anterior a 01/01/2000");
		    	rsm.addRegister(register);
		    
		    	dsValidacao = "";
		    	
		    	//entrouDs = true;
		    }
		    
		    if(documento.getDtDocumentoSaida() == null || documento.getDtEmissao() == null || documento.getDtDocumentoSaida().before(documento.getDtEmissao())){
		    	HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("ERRO", "Dados do documento de saida " + documento.getNrDocumentoSaida() + ": Data do documento não pode ser anterior a Data de Emissão");
		    	rsm.addRegister(register);
		    
		    	dsValidacao = "";
		    	
		    	//entrouDs = true;
		    }
		    
		    if(documento.getDtDocumentoSaida() == null || documento.getDtEmissao() == null || dtSpedFinal.before(documento.getDtDocumentoSaida()) || dtSpedFinal.before(documento.getDtEmissao())){
				HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("ERRO", "Dados do documento de saida " + documento.getNrDocumentoSaida() + ": Data menor do que a data final de emissão do SPED");
		    	rsm.addRegister(register);
		    
		    	//entrouDs = true;
		     }
		    
		    if(erroAliq > 0){
		    	String imposto = "";
		    	switch(erroAliq){
		    		case 1:
		    			imposto = "ICMS";
		    			break;
		    		case 2:
		    			imposto = "IPI";
		    			break;
		    		case 3:
		    			imposto = "II";
		    			break;
		    		case 4:
		    			imposto = "PIS";
		    			break;
		    		case 5:
		    			imposto = "COFINS";
		    			break;
		    	}
		    	
		    	HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("ERRO", "Dados do documento de saida " + documento.getNrDocumentoSaida() + ": Problema nas aliquotas do imposto " + imposto);
		    	rsm.addRegister(register);
		    
		    	//entrouDs = true;
		    }
		    if(!entrouDs && !entrouDs2){
				registro += "|C100" + //REG
						  "|" +IND_OPER_SAIDA+
						  "|" +IND_EMIT_PROPRIA+
						  "|" +documento.getCdCliente()+                                                                 //Cód participante
	//					  "|" +((cdNota1A > 0) ? "01" : (cdNfe > 0) ? "55" : "")+	                                                        //Cód modelo do documento fiscal            - VERIFICAR PARA QUANDO NAO FOR 1A NEM Nfe
						  "|" +"01"+	                                                        //Cód modelo do documento fiscal            - VERIFICAR PARA QUANDO NAO FOR 1A NEM Nfe
						  "|" +stDocumentoFiscal+										                                                                    //Cód da situação do documento fiscal       - Tabela 4.1.2
	//					  "|" +((nfe != null) ? nfe.getNrSerie() : "")+	                                                                    //Serie do documento fiscal                 											   -OC
						  "|" + "001" + //Serie do documento
						  "|" +nrDocumento+	                                                        //Numero do documento fiscal            
	//					  "|" +((cdNfe > 0) ? Integer.parseInt(nfe.getNrChaveAcesso().substring(3)) : 0)+                                    //Chave da nota fiscal online
						  "|" + //chave de acesso
						  "|" +(documento.getDtEmissao() != null ? Util.formatDateTime(documento.getDtEmissao(), "ddMMyyyy") : "")+			                                                        //Data da emissão
						  "|" +(documento.getDtDocumentoSaida() != null ? Util.formatDateTime(documento.getDtDocumentoSaida(), "ddMMyyyy") : "")+			                                                    //Data de entrada ou saida 											   -OC
						  "|" +Util.formatNumber(vlTotalDocumento, "#.##")+                                    //Valor total do documento fiscal           
						  "|" +((tpPagamento == 1) ? IND_PAGTO_APRAZO : (tpPagamento == 0) ? IND_PAGTO_AVISTA : "")+		//Indicador do tipo de pagamento
						  "|" +(Util.formatNumber((vlDescontos <= 0 ? 0 : vlDescontos), "#.##").equals("-0") ? "0" : Util.formatNumber((vlDescontos <= 0 ? 0 : vlDescontos), "#.##"))+		         							//Valor total do desconto 																															-OC
						  "|" +	//Valor abatimento fiscal                    - VERIFICAR 																																				-OC
						  "|" +Util.formatNumber(vlProd, "#.##")+										                    //Valor total da mercadoria e serviços 																		-OC
						  "|" +(documento.getTpFrete() == DocumentoSaidaServices.FRT_SEM_COBRANCA ? IND_FRT_SEMFRETE : documento.getTpFrete())+                                                                          //Indicador do frete
						  "|" +Util.formatNumber(documento.getVlFrete(), "#.##")+									                            //Valor do frete                             - No Momento SEM FRETE como padrão 		-OC
						  "|" +Util.formatNumber(documento.getVlSeguro(), "#.##")+									                            //Valor do seguro do frete 																-OC
						  "|" +"0,00"+				   									                                                //Valor de outras despesas                   - SEM VALOR DE OUTRAS DESPESAS  							-OC
						  "|" +Util.formatNumber(vlBaseICMS, "#.##")+	                                    //Valor da base de calculo do icms 														-OC
						  "|" +Util.formatNumber(vlICMS, "#.##")+	                                    //Valor do icms 																		-OC
						  "|" +Util.formatNumber(vlBaseICMSSub, "#.##")+									    //Valor da base de calculo do icms substituicao tributaria                                                       - VERIFICAR SE EH PARA USAR 						-OC
						  "|" +Util.formatNumber(vlICMSSub, "#.##")+									    //Valor do ICMS retido por substituição tributaria                                                               - VERIFICAR SE EH PARA USAR 						-OC
						  "|" +Util.formatNumber(0, "#.##")+	                                    //Valor total do IPI 																			-OC
						  "|" +Util.formatNumber(vlPIS, "#.##")+									                                                                        //Valor total do PIS 																			-OC
						  "|" +Util.formatNumber(vlCOFINS, "#.##")+									                                                                        //Valor total da confins 																		-OC
						  "|" +Util.formatNumber(vlPISSub, "#.##")+									                                                                        //Valor total do pis retido por substituicao tributaria 										-OC
						  "|" +Util.formatNumber(vlCOFINSSub, "#.##")+									                                                                        //Valor total da CONFINS retido por substituicao tributaria 									-OC
						  "|" +"\r\n";
				
				nrLinhaBlocoC++;
				conjuntoRegistro.put("C100", (((Integer) conjuntoRegistro.get("C100")) + 1));
		    }
			
		    //entrouDs = false;
		    
//			if(cdNfe > 0){//Por Enquanto só será informado os dados de interesse do fisco de Notas Fiscais Eletronicas  - APRESENTAR
//				/*
//				 * C110 - Informação complementar da nota fiscal - Referente a C100
//				 */
//				registro += "|C110" + //REG
//									  "|" +nfe.getCdNotaFiscal()+ //Código da informação complementar - Codigo deve ser o mesmo do registro 0450
//									  "|" +nfe.getTxtInformacaoFisco()+ //Descrição complementar do código de referência 
//									  "|" +"\r\n";
//				
//				nrLinhaBlocoC++;
//			}
			
			
			
			
			
//			/*
//			 * C111 - Processo referenciado. - Referente a C110  - VERIFICAR COMO ESSE REGISTRO FUNCIONA - deverá ser retirado ????
//			 */
//			String C111 = "C111" + //REG
//								  Util.fillAlpha("",15)+	//Numero do processo
//								  Util.fillAlpha(String.valueOf(IND_PROC_JE),1)+
//								  "\r\n";
//			nrLinhaBlocoC++;
			
			
//			/*
//			 * C112 - Documento de arrecadação referenciado. - Refernte a C110  - VERIFICAR COMO ESSE REGISTRO FUNCIONA  - deverá ser retirado ????
//			 */
//			String C112 = "C112" + //REG
//								  Util.fillAlpha(String.valueOf(COD_DA_DOC_ESTADUAL),1)+	//Numero do processo
//								  Util.fillAlpha("",2)+	//Unidade federada beneficiária do recolhimento
//								  Util.fillAlpha("",10)+ //Numero do documento de arrecadação
//								  Util.fillAlpha("",10)+ //Código completo da autenticação bancária.
//								  Util.fillNum(0,10)+  //Valor do total do documento de arrecadação
//								  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+			//Data de vencimento
//								  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+			//Data de pagamento
//								  "\r\n";
//			nrLinhaBlocoC++;
			
			
//			/*
//			 * C113 - Documento Fiscal Referenciado - Refernte a C110 - VERIFICAR COMO ESSE REGISTRO FUNCIONA   - APRESENTAR
			
//				APENAS NFE e Documentos de entrada de devolução
			
			
//			 */
//			String C113 = "C113"+
//								  Util.fillAlpha(String.valueOf(IND_OPER_ENTRADA), 1)+ //Indicador do tipo de operacao
//								  Util.fillAlpha(String.valueOf(IND_EMIT_PROPRIA), 1)+ //Indicador do emitente do titulo
//								  Util.fillAlpha("", 60)+ //Código do participante emitente
//								  Util.fillAlpha("", 2)+ //Código do documento fiscal
//								  Util.fillAlpha("", 4)+ //Serie do documento fiscal
//								  Util.fillNum(0,3)+  //SubSerie do documento fiscal
//								  Util.fillNum(0,9)+  //Numero do documento fiscal
//								  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+ //Data da emissão do documento fiscal
//								  "\r\n";
//			nrLinhaBlocoC++;
			
			
//			/*
//			 * C114 - Cupom fiscal referenciado - Refernte a C110 - VERIFICAR COMO ESSE REGISTRO FUNCIONA    - APRESENTAR
//			 * 
//			 * //				APENAS NFE e Documentos de entrada de devolução
//			 * 
//			 * 
//			 */
//			registro += "C114"+
//								  Util.fillAlpha("", 2)+ //Código do modelo do documento fiscal
//								  Util.fillAlpha("", 20)+ //Número de serie de fabricação do ECF
//								  Util.fillNum(0, 3)+ //Número do caixa atribuido ao ECF 
//								  Util.fillNum(0, 6)+ //Número do documento fiscal
//								  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+ //Data da emissão do documento fiscal
//								  "\r\n";
//			nrLinhaBlocoC++;
			
			
//			/*
//			 * C115 - Local da coleta e/ou entrega - Refernte a C110 - VERIFICAR - deverá ser retirado ???? 
//			 */
//			String C115 = "C115"+
//								  Util.fillNum(IND_CARGA_AEREO, 1)+ //Indicador do tipo de transporte
//								  Util.fillNum(0, 14)+ //Número do CNPJ do contribuinte do local de coleta
//					  			  Util.fillAlpha("", 14)+ //Inscrição estadual do contribuinte do local da coleta
//					  			  Util.fillNum(0, 11)+ //Número do CPF do contribuinte do local de coleta
//					  			  Util.fillNum(0, 7)+ //Código do municipio do local de coleta
//					  			  Util.fillNum(0, 14)+ //Número do CNPJ do contribuinte do local de entrega
//					  			  Util.fillAlpha("", 14)+ //Inscrição estadual do contribuinte do local da entrega
//					  			  Util.fillNum(0, 11)+ //Número do CPF do contribuinte do local de entrega
//					  			  Util.fillNum(0, 7)+ //Código do municipio do local de entrega
//					  			  "\r\n";
//			nrLinhaBlocoC++;
			
			/*
			 * C116 - Cupom fiscal eletrônico referenciado - Refernte a C110 - VERIFICAR   - APRESENTAR
			 * 
			 * APENAS NFE e Documentos de entrada de devolução
			 * 
			 * 
			 */
//			registro += "C116"+
//								  Util.fillAlpha("", 2)+//Código do modelo do documento fiscal, conforme a Tabela 4.1.1
//								  Util.fillNum(0, 9)+ //Número de Série do equipamento SAT
//								  Util.fillNum(0, 44)+ //Chave do Cupom Fiscal Eletrônico
//								  Util.fillNum(0, 6)+ //Número do cupom fiscal eletrônico
//								  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+ //Data da emissão do documento fiscal
//								  "\r\n";
//			nrLinhaBlocoC++;
			
			
//			if(cdNota1A > 0){//Fazer outro teste para saber se é um documento de entrada de importação  - APRESENTAR
//				/*
//				 * C120 - OPERAÇÕES DE IMPORTAÇÃO (CÓDIGO 01) - Refernte a C100 - Apenas para Documentos de Entrada - Verificar como será implementado
//				 */
//				registro += "|C120"+
//						  "|" +COD_DOC_IMP_IMPORTACAO+ //Documento de importação
//						  "|" +nrDocumento+//Número do documento de Importação.
//						  "|" +//Valor pago de PIS na importação
//						  "|" +//Valor pago de COFINS na importação
//						  "|" +//Número do Ato Concessório do regime Drawback
//						  "|" +"\r\n";
//				nrLinhaBlocoC++;
//			}
//			/*
//			 * C130 - ISSQN, IRRF E PREVIDÊNCIA SOCIAL - Refernte a C100 - Apenas para Documentos de Saida - Ver de onde pegar esses dados no Banco de Dados - Por enquanto não irá utilizar
//			 */
//			String C130 = "C130"+
//					  Util.fillNum(0, 6)+ //Valor dos serviços sob não-incidência ou não tributados pelo ICMS
//					  Util.fillNum(0, 6)+ //Valor da base de cálculo do ISSQN
//					  Util.fillNum(0, 6)+ //Valor do ISSQN
//					  Util.fillNum(0, 6)+ //Valor da base de cálculo do Imposto de Renda Retido na Fonte
//					  Util.fillNum(0, 6)+ //Valor do Imposto de Renda - Retido na Fonte
//					  Util.fillNum(0, 6)+ //Valor da base de cálculo de retenção da Previdência Social
//					  Util.fillNum(0, 6)+ //Valor destacado para retenção da Previdência Social
//					  "\r\n";
			
			/*
			 * Registro C140 - Refernte a C100 - FATURA
			 */									
			//Tipo de Documento
			TipoDocumento tipoDoc = getTipoDocumentoSAI(contasAReceber);
			
			float valorTotalTit = 0; 
			
			for(int cr = 0 ; cr < contasAReceber.size(); cr++){
				ContaReceber conta = ContaReceberDAO.get(contasAReceber.get(cr));
				valorTotalTit += conta.getVlConta();
			}
			
			if(contasAReceber.size() > 1){
				
				if(!entrouDs && !entrouDs2){
				    registro += "|C140"+
							              "|" +IND_EMIT_PROPRIA+
										  "|" +(tipoDoc == null || tipoDoc.getIdSped() == null ? "99" : tipoDoc.getIdSped())+
										  "|" +//Descrição complementar do título de crédito 																					- OC
										  "|" +documento.getCdDocumentoSaida()+//Número ou código identificador dos títulos de crédito - CRIAR CODIGO PARA TITULO DE CREDITO
										  "|" +contasAReceber.size()+ //Quantidade de parcelas a receber/pagar
										  "|" +Util.formatNumber(valorTotalTit, "#.##")+ //Valor total dos títulos de créditos
										  "|" +"\r\n";
					nrLinhaBlocoC++;
					conjuntoRegistro.put("C140", (((Integer) conjuntoRegistro.get("C140")) + 1));
		    	}
		    	
		    	//entrouDs = false;
				
		    	for(int k = 0; k < contasAReceber.size(); k++){
					/*
					 * Registro C141 - Refernte a C140 - Vencimento de fatura
					 */
		    		ContaReceber conta = ContaReceberDAO.get(contasAReceber.get(k));
		    		/************************************** VALIDACAO *******************************************************/
					//Data de Vencimento
					if(conta.getDtVencimento() == null)
				    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Data de Vencimento da conta a receber ";
					//Valor do titulo de credito
					if(String.valueOf(conta.getVlConta()) == null || String.valueOf(conta.getVlConta()).trim().equals(""))
				    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor da conta a receber ";
				    if(!dsValidacao.equals("")){
				    	HashMap<String, Object> register = new HashMap<String, Object>();
				    	register.put("ERRO", "Dados da conta a receber Nº "+conta.getNrDocumento()+" faltando: "+dsValidacao);
				    	rsm.addRegister(register);
				    
				    	dsValidacao = "";
				    	//entrouDs = true;
				    }
				    if(!entrouDs && !entrouDs2){
					    registro += "|C141"+
											  "|" +(k + 1)+ //Número da parcela a receber/pagar
											  "|" +Util.formatDateTime(conta.getDtVencimento(), "ddMMyyyy")+ //Data de vencimento da parcela
											  "|" +Util.formatNumber(conta.getVlConta(), "#.##")+ //Valor da parcela a receber/pagar
											  "|" +"\r\n";
						nrLinhaBlocoC++;
						conjuntoRegistro.put("C141", (((Integer) conjuntoRegistro.get("C141")) + 1));
				    }
				    //entrouDs = false;
				}
			
			
		    }
			if(!isCombustivel(documento, cdEmpresa)){
				/*
				 * Registro C160 - Refernte a C100 - Volumes Transportados - Apresentar Somente para Saida - Não usado para combustiveis
				 */
				/************************************** VALIDACAO *******************************************************/
				//Quantidade Volume
				if(String.valueOf(documento.getQtVolumes()) == null || String.valueOf(documento.getQtVolumes()).trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Quantidade de Volume ";
				//Peso Bruto
				if(String.valueOf(documento.getVlPesoBruto()) == null || String.valueOf(documento.getVlPesoBruto()).trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Peso Bruto transportado ";
				//Peso Liquido
				if(String.valueOf(documento.getVlPesoLiquido()) == null || String.valueOf(documento.getVlPesoLiquido()).trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Peso Liquido transportado ";
				if(!dsValidacao.equals("")){
					
					HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados dos Volumes transportados do documento de saida Nº "+documento.getNrDocumentoSaida()+" faltando: "+dsValidacao);
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	
			    	//entrouDs = true;
			    }
			    
				if(!entrouDs && !entrouDs2){
					registro += "|C160"+
										  "|" +((documento.getCdTransportadora() != 0) ? documento.getCdTransportadora() : "")+//Código do participante - OC
							 			  "|" +((documento.getNrPlacaVeiculo() != null) ? documento.getNrPlacaVeiculo() : "")+//Placa de identificação do veículo automotor - OC
							 			  "|" +Util.formatNumber(documento.getQtVolumes(), "#.##")+ //Quantidade de volumes transportados
							 			  "|" +Util.formatNumber(documento.getVlPesoBruto(), "#.##")+ //Peso bruto dos volumes transportados (em Kg)
							 			  "|" +Util.formatNumber(documento.getVlPesoLiquido(), "#.##")+ //Peso líquido dos volumes transportados (em Kg)
							 			  "|" +((documento.getSgPlacaVeiculo() != null) ? documento.getSgPlacaVeiculo().trim() : "")+//Sigla da UF da placa do veículo - OC
										  "|" +"\r\n";
					
					
					nrLinhaBlocoC++;
					conjuntoRegistro.put("C160", (((Integer) conjuntoRegistro.get("C160")) + 1));
				}
				
				//entrouDs = false;
			}
			
//			else if(cdNota1A > 0){
//				/************************************** VALIDACAO *******************************************************/
//				//Placa do Veiculo
//				if(documento.getNrPlacaVeiculo() == null || documento.getNrPlacaVeiculo().trim().equals(""))
//			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Numero da Placa do Veiculo, Referente ao Documento " + documento.getNrDocumentoSaida();
//				if(!dsValidacao.equals(""))
//			    	return new Result(-1, "Dados dos Volumes transportados faltando: "+dsValidacao);
//				
//				/************************************** VALIDACAO 2 *******************************************************/
//				//Placa do Veiculo
//				if(documento.getNrPlacaVeiculo().trim().length() > 7)
//			    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" Numero da Placa do Veiculo (Quantidade de dígitos ultrapassa 7), Referente ao Documento " + documento.getNrDocumentoSaida();
//			    //Codigo de Barras
//			    if(!dsValidacao2.equals(""))
//			    	return new Result(-1, "Dados dos Volumes transportados inválidos: "+dsValidacao2);
//				
////				/* POSTOS DE COMBUSTIVEL NAO DEVEM APRESENTAR ESSE REGISTRO
////				 * Registro C165 - Refernte a C100 - Operações com combustiveis - Verificar como irá funcionar - Apresentar Somente para Saida
////				 */
////			    registro += "|C165"+
////									  "|" +documento.getCdCliente()+//Código do participante - OC
////									  "|" +documento.getNrPlacaVeiculo().trim()+//Placa de identificação do veículo
////									  "|" +//Código da autorização fornecido pela SEFAZ - OC - VERIFICAR Se para Documentos tipo 01 há codigo de autorização da SEFAZ
////									  "|" +//Número do Passe Fiscal - OC - VERIFICAR
////									  "|" +Util.formatDateTime(dtSpedInicial, "HHmmss")+//Hora da saída das mercadorias
////									  "|" + //Temperatura em graus Celsius utilizada para quantificação do volume de combustível - OC
////									  "|" +Util.formatNumber(documento.getQtVolumes(), "#.##") + //Quantidade de volumes transportados
////									  "|" +Util.formatNumber(documento.getVlPesoBruto(), "#.##")+ //Peso bruto dos volumes transportados (em Kg)
////									  "|" +Util.formatNumber(documento.getVlPesoLiquido(), "#.##")+ //Peso líquido dos volumes transportados (em Kg)
////									  "|" +//Nome do motorista - OC
////									  "|" + //CPF do motorista - OC
////									  "|" +((documento.getSgPlacaVeiculo() != null) ? documento.getSgPlacaVeiculo() : "")+//Sigla da UF da placa do veículo - OC
////									  "|" +"\r\n";
////				nrLinhaBlocoC++;
//				
//			}
			
//			float valorReduzidoTotal = 0;
//			float valorTotalItens = 0;
//			float valorBaseTotal = 0;
			
			
			float vlDescontoItens = 0;
			int cdItem = 1;
			do{
				/*
				 * Registro C170 - Refernte a C100 - Itens do documento
				 */
				/************************************** VALIDACAO *******************************************************/
				//Quantidade do Item
				if(String.valueOf(rsmProdutos.getFloat("qt_saida")) == null || String.valueOf(rsmProdutos.getFloat("qt_saida")).trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Quantidade ";
				
				//Unidade de Medida
				if(rsmProdutos.getString("cd_unidade_medida") == null || rsmProdutos.getString("cd_unidade_medida").trim().equals("") || rsmProdutos.getInt("cd_unidade_medida") == 0){
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Unidade de medida ";
				}
				//Valor Unitario
				if(String.valueOf(rsmProdutos.getFloat("vl_unitario")) == null || String.valueOf(rsmProdutos.getFloat("vl_unitario")).trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor unitario do item ";
				//CFOP
				if(rsmProdutos.getString("nr_codigo_fiscal") == null || rsmProdutos.getString("nr_codigo_fiscal").trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CFOP ";
				//Tributação
//				if(!ProdutoServicoServices.getAllTributos(rsmProdutos.getInt("cd_produto_servico")).next())
//			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Tributação do produto: " + rsmProdutos.getString("nm_produto_servico") + " - Documento de Saída Nº: " + documento.getNrDocumentoSaida();
			    if(!dsValidacao.equals("")){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados dos Itens do documento de saída Nº "+documento.getNrDocumentoSaida()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" faltando: "+dsValidacao);
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	//entrouDs = true;
			    }
			    
//			    valorTotalItens += (rsmProdutos.getFloat("qt_saida") * rsmProdutos.getFloat("vl_unitario"));
//			    valorBaseTotal  += rsmProdutos.getFloat("vl_base_calculo");
			    
			    vlICMS  	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_icms"));
				vlBaseICMS 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_base_icms"));
				vlBaseICMSSub = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_base_icms") : 0);
				vlICMSSub 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_icms") : 0);
				
				vlBaseIPI 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_base_ipi"));
				vlIPI 		  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_ipi"));
				vlBaseIPISub  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_base_ipi") : 0);
				vlIPISub 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_ipi") : 0);
				
				vlBasePIS 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_base_pis"));
				vlPIS 		  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_pis"));
				vlBasePISSub  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_base_pis") : 0);
				vlPISSub 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_pis") : 0);
				
				vlBaseCOFINS 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_base_cofins"));
				vlCOFINS 		  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_cofins"));
				vlBaseCOFINSSub  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_base_cofins") : 0);
				vlCOFINSSub 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_cofins") : 0);
				
				
				float prAliquotaICMSSub = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("pr_icms") : 0);
			    float prAliquotaIPI 	= rsmProdutos.getFloat("pr_ipi");
			    float prAliquotaPIS 	= rsmProdutos.getFloat("pr_pis");
			    float prAliquotaCOFINS  = rsmProdutos.getFloat("pr_cofins");
				
			    vlDescontoItens += rsmProdutos.getFloat("vl_desconto");
			    
			    String cst = ((vlBaseICMS > 0 || vlBaseICMSSub > 0) && rsmProdutos.getString("nr_situacao_tributaria_icms") != null && !rsmProdutos.getString("nr_situacao_tributaria_icms").equals("") ? rsmProdutos.getString("nr_situacao_tributaria_icms") : "041");
			    
			    String parteCst = cst.substring(1);
			    
			    if((parteCst.equals("00") || 
			    	parteCst.equals("10") || 
			    	parteCst.equals("20") || 
			    	parteCst.equals("70")) &&
			    	rsmProdutos.getFloat("pr_icms") == 0){
			    	
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("AVISO", "Dados dos Itens do documento de saída Nº "+documento.getNrDocumentoSaida()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : Se o CST termina em 00, 10, 20 ou 70 a Aliquota deve ser maior do que zero.");
			    	rsm2.addRegister(register);
			    
			    }
			    
			    if(Util.arredondar(rsmProdutos.getFloat("qt_saida"), (rsmProdutos.getInt("qt_precisao_custo") > 5 ? 5 : rsmProdutos.getInt("qt_precisao_custo"))) == 0){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados dos Itens do documento de saída Nº "+documento.getNrDocumentoSaida()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : Quantidade do item zerada.");
			    	rsm.addRegister(register);
			    }
			    
			    if(!entrouDs && !entrouDs2){
				    registro += "|C170"+
										  "|" +cdItem+++//Número sequencial do item no documento fiscal
										  "|" +(rsmProdutos.getString("cd_produto_servico"))+//Código do item                                                                                              - Codigo proprio do EFD - Implementar
										  "|" +((rsmProdutos.getString("txt_produto_servico") != null) ? rsmProdutos.getString("txt_produto_servico").trim() : "")+//Descrição complementar do item como adotado no documento fiscal 												-OC
										  "|" +Util.formatNumber(rsmProdutos.getFloat("qt_saida"), (rsmProdutos.getInt("qt_precisao_custo") > 5 ? 5 : rsmProdutos.getInt("qt_precisao_custo")))+//Quantidade do item
										  "|" +((rsmProdutos.getString("sg_unidade_medida").trim().length() > 6 ) ? rsmProdutos.getString("sg_unidade_medida").trim().substring(0, 6) : rsmProdutos.getString("sg_unidade_medida").trim())+//Unidade do item
										  "|" +Util.formatNumber((rsmProdutos.getFloat("qt_saida") * rsmProdutos.getFloat("vl_unitario") + rsmProdutos.getFloat("vl_acrescimo") - rsmProdutos.getFloat("vl_desconto")), "#.##")+//Valor total do item (mercadorias ou serviços)
										  "|" +((rsmProdutos.getFloat("vl_desconto") != 0) ? Util.formatNumber(rsmProdutos.getFloat("vl_desconto"), "#.##") : "0,00")+//Valor do desconto comercial 																					-OC
										  "|" +IND_MOV_SIM+//Movimentação física do ITEM/PRODUTO
							              "|" +(cst.length() < 3 ? "0" + cst : cst)+//Código da Situação Tributária referente ao ICMS, conforme a Tabela indicada no item 4.3.1 http://www.cosif.com.br/mostra.asp?arquivo=sinief-CST-anexo
							              "|" +rsmProdutos.getString("nr_codigo_fiscal")+//Código Fiscal de Operação e Prestação
							              "|" +//Código da natureza da operação                                                                                - Codigo do proprio EFD - IMplementar Campo 2 do Registro 0400
							              "|" +Util.formatNumber(vlBaseICMS, "#.##")+//Valor da base de cálculo do ICMS 																		-OC
							              "|" +((rsmProdutos.getFloat("pr_icms") != 0) ? Util.formatNumber(rsmProdutos.getFloat("pr_icms"), "#.##") : "0,00" )+//Alíquota do ICMS 																															-OC
							              "|" +Util.formatNumber(vlICMS, "#.##") +//Valor do ICMS creditado/debitado 											-OC
							              "|" +Util.formatNumber(vlBaseICMSSub, "#.##") +//"0,00" +//Valor da base de cálculo referente à substituição tributária 																																										-OC
							              "|" +Util.formatNumber(prAliquotaICMSSub, "#.##") +//Alíquota do ICMS da substituição tributária na unidade da federação de destino 																																					-OC
							              "|" +Util.formatNumber(vlICMSSub, "#.##") + //"0,00" +//Valor do ICMS referente à substituição tributária 																																													-OC
							              "|" +//Movimentação física do ITEM/PRODUTO 																																																-OC
							              "|" +((vlBaseIPI > 0 || vlBaseIPISub > 0) && !rsmProdutos.getString("nr_situacao_tributaria_ipi").equals("") && rsmProdutos.getString("nr_situacao_tributaria_ipi") != null ? rsmProdutos.getString("nr_situacao_tributaria_ipi") : "") +//Código da Situação Tributária referente ao IPI 																								-OC
							              "|" +//Código de enquadramento legal do IPI 																																																-OC
							              "|" +Util.formatNumber(vlBaseIPI, "#.##") + //"0,00" +//Valor da base de cálculo do IPI 																																																	-OC
							              "|" +Util.formatNumber(prAliquotaIPI, "#.##") +//Alíquota do IPI 																																																					-OC
							              "|" +Util.formatNumber(vlIPI, "#.##") +//Valor do IPI creditado/debitado 																																																	-OC
							              "|" +((vlBasePIS > 0 || vlBasePISSub > 0) && !rsmProdutos.getString("nr_situacao_tributaria_pis").equals("") && rsmProdutos.getString("nr_situacao_tributaria_pis") != null ? rsmProdutos.getString("nr_situacao_tributaria_pis") : "") +//Código da Situação Tributária referente ao PIS. 																																												-OC
							              "|" +Util.formatNumber(vlBasePIS, "#.##") + //"0,00" +//Valor da base de cálculo do PIS 																																																	-OC
							              "|" +Util.formatNumber(prAliquotaPIS, "#.####") +//Alíquota do PIS (em percentual) 																																																	-OC
							              "|" +((vlBasePIS > 0 || vlBasePISSub > 0) ? Util.formatNumber(rsmProdutos.getFloat("qt_saida"), "#.###") : "0,000") + //"0,000" +//Quantidade  Base de cálculo PIS 																																																	-OC
							              "|" + //"0,0000" +//Alíquota do PIS (em reais) 																																																		-OC
							              "|" +Util.formatNumber(vlPIS, "#.##") +// "0,00" +//Valor do PIS 																																																						-OC
							              "|" +((vlBaseCOFINS > 0 || vlBaseCOFINSSub > 0) && !rsmProdutos.getString("nr_situacao_tributaria_cofins").equals("") && rsmProdutos.getString("nr_situacao_tributaria_cofins") != null ? rsmProdutos.getString("nr_situacao_tributaria_cofins") : "") +//Código da Situação Tributária referente ao COFINS. 																																											-OC
							              "|" +Util.formatNumber(vlBaseCOFINS, "#.###") + //"0,00" +//Valor da base de cálculo da COFINS 																																																-OC
							              "|" +Util.formatNumber(prAliquotaCOFINS, "#.####") +//Alíquota do COFINS (em percentual) 																																																-OC
							              "|" +((vlBaseCOFINS > 0 || vlBaseCOFINSSub > 0) ? Util.formatNumber(rsmProdutos.getFloat("qt_saida"), "#.###") : "0,000") + //"0,000" +//Quantidade  Base de cálculo COFINS 																																																-OC
							              "|" + //"0,0000" +//Alíquota da COFINS (em reais) 																																																		-OC
							              "|" +Util.formatNumber(vlCOFINS, "#.####") + //"0,00" +//Valor da COFINS 																																																					-OC
							              "|" +//Código da conta analítica contábil debitada/creditada 																																												-OC			                                           	              
										  "|" +"\r\n";
					nrLinhaBlocoC++;
					conjuntoRegistro.put("C170", (((Integer) conjuntoRegistro.get("C170")) + 1));
			    }
			    
			    //entrouDs = false;
				
//				if(false){//Apenas Para Entradas  - APRESENTAR
//					
//					/*
//					 * Registro C171 - Refernte a C170 - Armazenamento de combustiveis                                 - Implementar
//					 */
//					criterios = new ArrayList<ItemComparator>();
//					criterios.add(new ItemComparator("CD_PRODUTO_SERVICO", Integer.toString(rsmProdutos.getInt("cd_produto_servico")), ItemComparator.EQUAL, Types.INTEGER));
//					ResultSetMap rsmTanque = TanqueDAO.find(criterios);
//					String nrTanque = "";
//					int cdTanque = 0;
//					String vlArmazenado = "";
//					while(rsmTanque.next()){
//						cdTanque = rsmTanque.getInt("cd_tanque");
//						vlArmazenado = "";//COLOCAR METODO PARA SABER QUANTO JA TEM ARMAZENADO NO TANQUE
//						nrTanque = rsmTanque.getString("cd_tanque");
//					}
//					registro += "|C171" +
//								"|" +nrTanque+//Tanque onde foi armazenado o combustível
//								"|" +vlArmazenado+//Quantidade ou volume armazenado
//								"|" +"\r\n";
//					nrLinhaBlocoC++;
//					
//				}
				
//				if(false){//Teste para saber se utiliza o ISSQN
//					/*
//					 * Registro C172 - OPERAÇÕES COM ISSQN
//					 */
//					String C172 = "C172" +
//										  Util.fillNum(0, 6)+//Valor da base de cálculo do ISSQN
//										  Util.fillNum(0, 6)+//Alíquota do ISSQN
//										  Util.fillNum(0, 6)+//Valor do ISSQN
//										  "\r\n";
//				}
				
//				/*
//				 * Registro C176 - Refernte a C170 - RESSARCIMENTO DE ICMS EM OPERAÇÕES COM SUBSTITUIÇÃO TRIBUTÁRIA                  - Ver como Implementar - Apenas em Saídas 
//				 */
//				registro += "|C176" +
//									  "|" +Util.fillAlpha("", 2)+//Código do modelo do documento fiscal relativa a ultima entrada
//									  "|" +Util.fillNum(0, 9)+//Número do documento fiscal relativa a última entrada
//									  "|" +Util.fillAlpha("", 3)+//Série do documento fiscal relativa a última entrada
//									  "|" +Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+ //Data relativa a última entrada da mercadoria
//									  "|" +Util.fillAlpha("", 60)+//Código do participante (do emitente do documento relativa a última entrada)
//									  "|" +Util.fillNum(0, 6)+//Quantidade do item relativa a última entrada
//									  "|" +Util.fillNum(0, 6)+//Valor unitário da mercadoria constante na NF relativa a última entrada inclusive despesas acessórias.
//									  "|" +Util.fillNum(0, 6)+//Valor unitário da base de cálculo do imposto pago por substituição.
//									  "|" +"\r\n";
//				nrLinhaBlocoC++;
				
				
//				/*
//				 * Registro C177 - OPERAÇÕES COM PRODUTOS SUJEITOS A SELO DE CONTROLE IPI.
//				 */
//				String C177 = "C177" +
//									  Util.fillAlpha("", 6)+//Código do selo de controle do IPI,
//									  Util.fillNum(0, 12)+//Quantidade de selo de controle do IPI aplicada
//									  "\r\n";
				
//				/*
//				 * Registro C178 - OPERAÇÕES COM PRODUTOS SUJEITOS À TRIBUTAÇÀO DE IPI POR UNIDADE OU QUANTIDADE DE PRODUTO.
//				 */
//				String C178 = "C178" +
//						 			  Util.fillAlpha("", 60)+//Código da classe de enquadramento do IPI
//						 			  Util.fillNum(0, 6)+//Valor por unidade padrão de tributação
//						 			  Util.fillNum(0, 6)+//Quantidade total de produtos na unidade padrão de tributação
//									  "\r\n";
				
//				/*
//				 * Registro C179 - Refernte a C170 - INFORMAÇÕES COMPLEMENTARES                  - Ver se irá implementar
//				 */
//				registro += "C179" +
//									  Util.fillNum(0, 6)+//Valor da base de cálculo ST na origem/destino em operações interestaduais.
//									  Util.fillNum(0, 6)+//Valor do ICMS-ST a repassar/deduzir em operações interestaduais
//						 			  Util.fillNum(0, 6)+//Valor do ICMS-ST a complementar à UF de destino
//						 			  Util.fillNum(0, 6)+//Valor da BC de retenção em remessa promovida por Substituído intermediário
//						 			  Util.fillNum(0, 6)+//Valor da parcela do imposto retido em remessa promovida por substituído intermediário
//						 			  "\r\n";
//				nrLinhaBlocoC++;			
				
			}while(rsmProdutos.next());
			
//			valorReduzidoTotal = valorTotalItens - valorBaseTotal;
			if(vlDescontos != vlDescontoItens){
				HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("AVISO", "Dados dos Itens do documento de saída Nº "+documento.getNrDocumentoSaida()+": Valor dos Descontos dos itens diferente do Valor de Desconto do Documento.");
		    	rsm2.addRegister(register);
			}
						
			ResultSetMap rsmTrib = getTributacao(cdDocumentoSaida, 0);
			ResultSetMap rsmTribByEstado = getTributacaoByEstado(cdDocumentoSaida, 0);
			/************************************** VALIDACAO *******************************************************/
			//Natureza de Operacao
			//CFOP
			
//			if(!rsmTrib.next())
//				return new Result(-1, "ERRO na tributação de um produto do Documento de Saída N." + documento.getNrDocumentoSaida());
			float vlTotalItens = 0;
			while(rsmTribByEstado.next()){
				boolean hasEstado = false;
				while(rsmApuracaoIcms.next()){
					if(rsmApuracaoIcms.getRegister().get("SG_ESTADO").equals(rsmTribByEstado.getString("sg_estado"))){
						hasEstado = true;
						rsmApuracaoIcms.setValueToField("VL_ICMS_DEBITO",  rsmApuracaoIcms.getFloat("VL_ICMS_DEBITO") + rsmTribByEstado.getFloat("vl_icms"));
						rsmApuracaoIcms.setValueToField("VL_ICMS_DEBITO_ST_RETENCAO",  rsmApuracaoIcms.getFloat("VL_ICMS_DEBITO_ST_RETENCAO") + rsmTribByEstado.getFloat("vl_icms_substituto"));
						rsmApuracaoIcms.setValueToField("VL_ICMS_DEBITO_ST_DEV_ANT",  rsmApuracaoIcms.getFloat("VL_ICMS_DEBITO_ST_DEV_ANT") + rsmTribByEstado.getFloat("vl_icms_substituto"));
						break;
					}
				}
				if(!hasEstado){
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("SG_ESTADO", rsmTribByEstado.getString("sg_estado"));
					register.put("VL_ICMS_DEBITO", rsmTribByEstado.getFloat("vl_icms"));
					register.put("VL_ICMS_DEBITO_ST_RETENCAO", rsmTribByEstado.getFloat("vl_icms_substituto"));
					register.put("VL_ICMS_DEBITO_ST_DEV_ANT", rsmTribByEstado.getFloat("vl_icms_substituto"));
					rsmApuracaoIcms.addRegister(register);
				}
				rsmApuracaoIcms.beforeFirst();
				
				
			}
			
			while(rsmTrib.next()){
				if(rsmTrib.getString("nr_codigo_fiscal") == null)
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CFOP ";
				if(!dsValidacao.equals("")){
					HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados Analiticos do Documento de saida Nº "+documento.getNrDocumentoSaida()+": "+dsValidacao);
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
					
			    	//entrouDs = true;
				}
				
				vlTotalItens += rsmTrib.getFloat("vl_itens"); 
				
				/*
				 * Registro C190 - Refernte a C100 - REGISTRO ANALÍTICO DO DOCUMENTO
				 */
				
				String cst = rsmTrib.getString("nr_situacao_tributaria") != null && !rsmTrib.getString("nr_situacao_tributaria").equals("") ? rsmTrib.getString("nr_situacao_tributaria") : "041";
				cst = (cst.length() < 3 ? "0" + cst : cst);
				
				if(!entrouDs){
					vlIcmsDebito   += rsmTrib.getFloat("vl_icms");
					registro += "|C190" +
										  "|" +cst+//Código da Situação Tributária. - http://www.cosif.com.br/mostra.asp?arquivo=sinief-CST-anexo
										  "|" +rsmTrib.getString("nr_codigo_fiscal")+//Código Fiscal de Operação e Prestação do agrupamento de itens.
										  "|" +((rsmTrib.getFloat("pr_aliquota") != 0) ? Util.formatNumber(rsmTrib.getFloat("pr_aliquota"), "#.##") : "0,00")+//Alíquota do ICMS
										  "|" +Util.formatNumber((Util.arredondar(rsmTrib.getFloat("vl_itens"), 2)), "#.##")+//Valor da operação na combinação de CST_ICMS, CFOP e alíquota do ICMS.
										  "|" +Util.formatNumber(rsmTrib.getFloat("vl_base_icms"), "#.##")+//Parcela correspondente ao "Valor da base de cálculo do ICMS" referente à combinação de CST_ICMS, CFOP e alíquota do ICMS. 			-OC
										  "|" +Util.formatNumber(rsmTrib.getFloat("vl_icms"), "#.##")+//Parcela correspondente ao "Valor do ICMS" referente à combinação de CST_ICMS, CFOP e alíquota do ICMS.
										  "|" +Util.formatNumber(rsmTrib.getFloat("vl_base_icms_substituto"), "#.##")+//Parcela correspondente ao "Valor da base de cálculo do ICMS" da substituição tributária referente à combinação de CST_ICMS, CFOP e alíquota do ICMS.
										  "|" +Util.formatNumber(rsmTrib.getFloat("vl_icms_substituto"), "#.##")+//Parcela correspondente ao valor creditado/debitado do ICMS da substituição tributária, referente à combinação de CST_ICMS, CFOP, e alíquota do ICMS.
										  "|" +"0,00"+//Valor não tributado em função da redução da base de cálculo do ICMS, referente à combinação de CST_ICMS, CFOP e alíquota do ICMS.
										  "|" +Util.formatNumber(rsmTrib.getFloat("vl_ipi"), "#.##")+//Parcela correspondente ao "Valor do IPI" referente à combinação CST_ICMS, CFOP e alíquota do ICMS.
		//								  "|" +Util.fillAlpha("", 6)+//Código da observação do lançamento fiscal   campo 2 do registro 0460        - Ver se na BA eh necessario preencher tal campo - PARA NFE POR ENQUANTO
										  "|" + //Codigo da obsercação do lançamento fiscal																																					-OC
										  "|" +"\r\n";
					nrLinhaBlocoC++;
					conjuntoRegistro.put("C190", (((Integer) conjuntoRegistro.get("C190")) + 1));
				}
				
				//entrouDs = false;
			}
			
			
//			if(!vlTotalDocumento.equals(Util.formatNumber(vlTotalItens, "#.##"))){
//				HashMap<String, Object> register = new HashMap<String, Object>();
//		    	register.put("AVISO", "Dados Analiticos do Documento de saida Nº "+documento.getNrDocumentoSaida()+": O valor total do documento fiscal deve ser igual a soma dos valores de operação VL_OPR do registro C190");
//		    	rsm2.addRegister(register);
//		    
//			}
			
//			/* POR ENQUANTO APENAS NFE TERA ESSE REGISTRO, DEPOIS ESTENDER PARA DOCUMENTOS DE ENTRADA/SAIDA
//			 * Registro C195 - Refernte a C100 - OBSERVAÇOES DO LANÇAMENTO FISCAL - Este registro deve ser informado quando, em decorrência da legislação estadual, houver ajustes nos documentos
//																  fiscais, informações sobre diferencial de alíquota, antecipação de imposto e outras situações
//			 */
//			registro += "C195" +
//								  Util.fillAlpha("", 6)+//Código da observação do lançamento fiscal
//								  Util.fillAlpha("", 30)+//Descrição complementar do código de observação.
//								  "\r\n";
//			nrLinhaBlocoC++;
			
			
//			/*
//			 * Registro C197 - Refernte a C195 - OUTRAS OBRIGAÇÕES TRIBUTÁRIAS, AJUSTES E INFORMAÇÕES DE VALORES PROVENIENTES DE DOCUMENTO FISCAL    - Verificar se será utilizado
//			 */
//			String C197 = "C197"+
//								  Util.fillAlpha("", 10)+//Código da observação do lançamento fiscal
//								  Util.fillAlpha("", 6)+//Descrição complementar do ajuste do documento fiscal
//								  Util.fillAlpha("", 60)+//Código do item
//								  Util.fillNum(0, 6)+//Base de cálculo do ICMS ou do ICMS ST
//								  Util.fillNum(0, 6)+//Alíquota do ICMS
//								  Util.fillNum(0, 6)+//Valor do ICMS ou do ICMS ST
//								  Util.fillNum(0, 6)+//Outros valores
//								  "\r\n";
//			nrLinhaBlocoC++;
			
		}
		
		ArrayList<String> registroEntrada = new ArrayList<String>();
		//ENTRADA
		for(int i = 0; i < codigosDocumentoEntrada01.size(); i++){
			//Código do documento
			int cdDocumentoEntrada = codigosDocumentoEntrada01.get(i);
			//Documento
			DocumentoEntrada documento = DocumentoEntradaDAO.get(cdDocumentoEntrada);
			//Numero do documento
			int nrDoc = 0;
			if(documento.getNrDocumentoEntrada() != null && !documento.getNrDocumentoEntrada().trim().equals("")){
				nrDoc = Integer.parseInt(Util.limparFormatos(documento.getNrDocumentoEntrada(), 'N'));
			}
			String nrDocumento = ((nrDoc > 999999999) ? String.valueOf(nrDoc).substring(0, 9) : String.valueOf(nrDoc));
			//Valor do documento
			float vlTotalDocumento = DocumentoEntradaServices.getValorAllItens(cdDocumentoEntrada);
			//Valor de Desconto do documento
			float vlDescontos = DocumentoEntradaServices.getValorDescontosAllItens(cdDocumentoEntrada);
			//Situação do documento
			String stDocumentoFiscal = ST_DOCUMENTO_REGULAR;
			if(documento.getStDocumentoEntrada() == 2){
				stDocumentoFiscal = ST_DOCUMENTO_CANCELADO;
			}
//					//Código do documento se pertencer a uma nota fiscal eletronica - SERA ALTERADO PARA QUANDO O SISTEMA PUDER IMPORTAR NOTAS FISCAIS ELETRONICAS E ARMAZENA-LAS COMO NOTA FISCAL ELETRONICA
//					int cdNfe    = (documento.getTpDocumentoSaida() == 6) ? cdDocumentoSaida : 0;
			//Código do documento se pertencer a uma nota fiscal
//			int cdNota1A = (documento.getTpDocumentoEntrada() == 0) ? cdDocumentoEntrada : 0;
//					NotaFiscal nfe = null;
//					if(cdNfe > 0){
//						nfe = NotaFiscalDAO.get(cdNfe);
//						if(nfe.getStNotaFiscal() == NotaFiscalServices.CANCELADA)
//							stDocumentoFiscal = ST_DOCUMENTO_CANCELADO;
//						else if(nfe.getStNotaFiscal() == NotaFiscalServices.DENEGADA)
//							stDocumentoFiscal = ST_NFE_DENEGADO;
//					}
			
			//Selecionar o Tipo de Pagamento
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
//			criterios.add(new ItemComparator("CD_DOCUMENTO_ENTRADA", Integer.toString(cdDocumentoEntrada), ItemComparator.EQUAL, Types.INTEGER));
//			ResultSetMap rsmPlanoDoc = PlanoPagtoDocumentoSaidaDAO.find(criterios);
//			int tpPagamento = -1;
//			if(rsmPlanoDoc.next()){
//				int cdPlano = rsmPlanoDoc.getInt("cd_plano_pagamento");
//				if(cdPlano == 1 || cdPlano == 15)
//					tpPagamento = 0;
//				else
//					tpPagamento = 1;
//			}
			
			//Valor Total do Itens
			float vlProd = DocumentoEntradaServices.getValorAllItens(cdDocumentoEntrada);
			
			//Contas a Receber pertencentes ao documento
			ArrayList<Integer> contasAPagar  = getContasAPagar(cdDocumentoEntrada);
			
			int tpPagamento = -1;
			if(contasAPagar.size() == 1)
				tpPagamento = IND_PAGTO_AVISTA;
			else
				tpPagamento = IND_PAGTO_APRAZO;
						
			
//			//Valor total dos titulos
//			float valorTotalTit = getValorTotalTitulos(contasAPagar);
			
			//ResultSetMap com os itens do documento
			ResultSetMap rsmProdutos = DocumentoEntradaServices.getAllItens(cdDocumentoEntrada, true);
			
			float vlBaseICMS = 0;
			float vlICMS = 0;
			float vlBaseICMSSub = 0;
			float vlICMSSub = 0;
			
			float vlBaseIPI = 0;
			float vlIPI = 0;
			float vlBaseIPISub = 0;
			float vlIPISub = 0;
			
			float vlBasePIS = 0;
			float vlPIS = 0;
			float vlBasePISSub = 0;
			float vlPISSub = 0;
			
			float vlBaseCOFINS = 0;
			float vlCOFINS = 0;
			float vlBaseCOFINSSub = 0;
			float vlCOFINSSub = 0;
			
			int erroAliq = 0;
			
			if(rsmProdutos.next()){
				vlICMS  	  = rsmProdutos.getFloat("vl_icms_documento");
				vlBaseICMS 	  = rsmProdutos.getFloat("vl_base_icms_documento");
				vlBaseICMSSub = rsmProdutos.getFloat("vl_base_icms_substituto_documento");
				vlICMSSub 	  = rsmProdutos.getFloat("vl_icms_substituto_documento");
				
				vlBaseIPI 	  = rsmProdutos.getFloat("vl_base_ipi_documento");
				vlIPI 		  = rsmProdutos.getFloat("vl_ipi_documento");
				vlBaseIPISub  = rsmProdutos.getFloat("vl_base_ipi_substituto_documento");
				vlIPISub 	  = rsmProdutos.getFloat("vl_ipi_substituto_documento");
				
				vlBasePIS 	  = rsmProdutos.getFloat("vl_base_pis_documento");
				vlPIS 		  = rsmProdutos.getFloat("vl_pis_documento");
				vlBasePISSub  = rsmProdutos.getFloat("vl_base_pis_substituto_documento");
				vlPISSub 	  = rsmProdutos.getFloat("vl_pis_substituto_documento");
				
				vlBaseCOFINS 	  = rsmProdutos.getFloat("vl_base_cofins_documento");
				vlCOFINS 		  = rsmProdutos.getFloat("vl_cofins_documento");
				vlBaseCOFINSSub  = rsmProdutos.getFloat("vl_base_cofins_substituto_documento");
				vlCOFINSSub 	  = rsmProdutos.getFloat("vl_cofins_substituto_documento");
				
				erroAliq = rsmProdutos.getInt("ErroAliq");
			}
			else{
				HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("ERRO", "Documento de entrada Nº "+documento.getNrDocumentoEntrada()+" sem produtos!");
		    	rsm.addRegister(register);
		    
		    	dsValidacao = "";
		    	
		    	continue;
			}
			
//			//Natureza de Operacação do documento
//			NaturezaOperacao natOp = NaturezaOperacaoDAO.get(documento.getCdNaturezaOperacao());
			
			//Resumo dos tributos do documento
//			criterios = new ArrayList<ItemComparator>(); 
//			criterios.add(new ItemComparator("CD_DOCUMENTO_ENTRADA", Integer.toString(cdDocumentoEntrada), ItemComparator.EQUAL, Types.INTEGER));
//			ResultSetMap rsmEntradaAliquota = EntradaTributoDAO.find(criterios);
//			EntradaTributo entradaTrib = null;
//			float vlBaseCalculo = 0;
//			float vlIcms = 0;
//			if(rsmEntradaAliquota.next()){
//				entradaTrib = EntradaTributoDAO.get(cdDocumentoEntrada, rsmEntradaAliquota.getInt("cd_tributo"));
//				vlBaseCalculo = entradaTrib.getVlBaseCalculo();
//				vlIcms = entradaTrib.getVlTributo();
//			}
			
			float result[] 		= getTributoOfDocumentoEntrada(documento.getCdDocumentoEntrada(), documento.getCdEmpresa());
			float vlIcms 		= result[0];
			float vlBaseCalculo = result[1];
			/************************************** VALIDACAO *******************************************************/
			//Numero do Documento
			if(documento.getNrDocumentoEntrada() ==null || documento.getNrDocumentoEntrada().trim().equals(""))
		    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Número ";
			//Codigo do cliente
			if(String.valueOf(documento.getCdFornecedor())==null || String.valueOf(documento.getCdFornecedor()).trim().equals(""))
		    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Fornecedor ";
//			//Tributo
//			if(entradaTrib == null)
//				dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Tributação do documento";
			
			
//					//Chave da Nota Fiscal Eletronica - APRESENTAR
//					if(nfe != null){
//						if(nfe.getNrChaveAcesso() == null || nfe.getNrChaveAcesso().trim().equals(""))
//							dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Chave de Acesso da NFe";
//					}
			//Data de Emissao do Documento
			if(documento.getDtEmissao() == null || String.valueOf(documento.getDtEmissao()).trim().equals(""))
		    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Data de emissão ";
			
			//Valor Total do Documento
			if(String.valueOf(documento.getVlTotalDocumento()) == null || String.valueOf(documento.getVlTotalDocumento()).trim().equals(""))
		    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor total ";
			
		    if(!dsValidacao.equals("")){
		    	HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("ERRO", "Dados do documento de entrada " + documento.getNrDocumentoEntrada() + " faltando: "+dsValidacao);
		    	rsm.addRegister(register);
		    
		    	dsValidacao = "";
		    	//entrouDs = true;
		    }
		    if(documento.getDtDocumentoEntrada() == null || documento.getDtEmissao() == null || documento.getDtDocumentoEntrada().before(documento.getDtEmissao())){
		    	HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("ERRO", "Dados do documento de Entrada " + documento.getNrDocumentoEntrada() + " inválido: Data de Entrada é menor que a Data de Emissão");
		    	rsm.addRegister(register);
		    	//entrouDs = true;
		    }
		    
		    /*Validacao*/
		    boolean duplicidade = false;
		    String registroAtual = IND_OPER_ENTRADA + nrDocumento + "01" + stDocumentoFiscal;
		    for(int ind = 0; ind < registroEntrada.size(); ind++){
		    	if(registroAtual.equals(registroEntrada.get(ind))){
		    		duplicidade = true;
		    		break;
		    	}
		    }
		    
		    if(duplicidade){
		    	HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("ERRO", "Dados do documento de entrada " + documento.getNrDocumentoEntrada() + ": Duplicidade com outro documento.");
		    	rsm.addRegister(register);
		    	//entrouDs = true;
		    }
		    else{
		    	registroEntrada.add(registroAtual);
		    }
		    
		    GregorianCalendar data = new GregorianCalendar();
		    data.set(Calendar.YEAR, 2000);
		    data.set(Calendar.MONTH, 0);
		    data.set(Calendar.DAY_OF_MONTH, 1);
		    if(documento.getDtDocumentoEntrada() == null || documento.getDtEmissao() == null || documento.getDtDocumentoEntrada().before(data) || documento.getDtEmissao().before(data)){
		    	HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("ERRO", "Dados do documento de entrada " + documento.getNrDocumentoEntrada() + ": Data do documento não pode ser anterior a 01/01/2000");
		    	rsm.addRegister(register);
		    
		    	dsValidacao = "";
		    	
		    	//entrouDs = true;
		    }
		    
		    if(documento.getDtDocumentoEntrada() == null || documento.getDtEmissao() == null || dtSpedFinal.before(documento.getDtDocumentoEntrada()) || dtSpedFinal.before(documento.getDtEmissao())){
				HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("ERRO", "Dados do documento de entrada " + documento.getNrDocumentoEntrada() + ": Data menor do que a data final de emissão do SPED");
		    	rsm.addRegister(register);
		    
		    	//entrouDs = true;
		     }
		    
		    if(erroAliq > 0){
		    	String imposto = "";
		    	switch(erroAliq){
		    		case 1:
		    			imposto = "ICMS";
		    			break;
		    		case 2:
		    			imposto = "IPI";
		    			break;
		    		case 3:
		    			imposto = "II";
		    			break;
		    		case 4:
		    			imposto = "PIS";
		    			break;
		    		case 5:
		    			imposto = "COFINS";
		    			break;
		    	}
		    	
		    	HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("ERRO", "Dados do documento de entrada " + documento.getNrDocumentoEntrada()+ ": Problema nas aliquotas do imposto " + imposto);
		    	rsm.addRegister(register);
		    
		    	//entrouDs = true;
		    }
		    
		    if(!entrouDs && !entrouDs2){
				registro += "|C100" + //REG
						  "|" +IND_OPER_ENTRADA+
						  "|" +IND_EMIT_PROPRIA+
						  "|" +documento.getCdFornecedor()+                                                                 //Cód participante
	//							  "|" +((cdNota1A > 0) ? "01" : (cdNfe > 0) ? "55" : "")+	                                                        //Cód modelo do documento fiscal            - VERIFICAR PARA QUANDO NAO FOR 1A NEM Nfe
						  "|" +"01"+	                                                        //Cód modelo do documento fiscal            - VERIFICAR PARA QUANDO NAO FOR 1A NEM Nfe
						  "|" +stDocumentoFiscal+										                                                                    //Cód da situação do documento fiscal       - Tabela 4.1.2
	//							  "|" +((nfe != null) ? nfe.getNrSerie() : "")+	                                                                    //Serie do documento fiscal                 - OC
						  "|" + "001" + //Serie do documento
						  "|" +nrDocumento+	                                                        //Numero do documento fiscal            
	//							  "|" +((cdNfe > 0) ? Integer.parseInt(nfe.getNrChaveAcesso().substring(3)) : 0)+                                    //Chave da nota fiscal online
						  "|" + //chave de acesso
						  "|" +(documento.getDtEmissao() != null ? Util.formatDateTime(documento.getDtEmissao(), "ddMMyyyy") : "")+			                                                        //Data da emissão
						  "|" +(documento.getDtDocumentoEntrada() != null ? Util.formatDateTime(documento.getDtDocumentoEntrada(), "ddMMyyyy") : "")+			                                                    //Data de entrada ou saida
						  "|" +Util.formatNumber(vlTotalDocumento, "#.##")+                                    //Valor total do documento fiscal           
						  "|" +tpPagamento+		//Indicador do tipo de pagamento
						  "|" +(Util.formatNumber((vlDescontos <= 0 ? 0 : vlDescontos), "#.##").equals("-0") ? "0" : Util.formatNumber((vlDescontos <= 0 ? 0 : vlDescontos), "#.##"))+		         							//Valor total do desconto
						  "|" +	//Valor abatimento fiscal                    - VERIFICAR
						  "|" +Util.formatNumber(vlProd, "#.##")+										                    //Valor total da mercadoria e serviços
						  "|" +IND_FRT_SEMFRETE+                                                                          //Indicador do frete
						  "|" +Util.formatNumber(documento.getVlFrete(), "#.##")+									                            //Valor do frete                             - No Momento SEM FRETE como padrão
						  "|" +Util.formatNumber(documento.getVlSeguro(), "#.##")+									                            //Valor do seguro do frete
						  "|" +				   									                                                //Valor de outras despesas                   - SEM VALOR DE OUTRAS DESPESAS  
						  "|" +Util.formatNumber(vlBaseICMS, "#.##")+	                                    //Valor da base de calculo do icms 														-OC
						  "|" +Util.formatNumber(vlICMS, "#.##")+	                                    //Valor do icms 																		-OC
						  "|" +Util.formatNumber(vlBaseICMSSub, "#.##")+									    //Valor da base de calculo do icms substituicao tributaria                                                       - VERIFICAR SE EH PARA USAR 						-OC
						  "|" +Util.formatNumber(vlICMSSub, "#.##")+									    //Valor do ICMS retido por substituição tributaria                                                               - VERIFICAR SE EH PARA USAR 						-OC
						  "|" +Util.formatNumber(0, "#.##")+	                                    //Valor total do IPI 																			-OC
						  "|" +Util.formatNumber(vlPIS, "#.##")+									                                                                        //Valor total do PIS 																			-OC
						  "|" +Util.formatNumber(vlCOFINS, "#.##")+									                                                                        //Valor total da confins 																		-OC
						  "|" +Util.formatNumber(vlPISSub, "#.##")+									                                                                        //Valor total do pis retido por substituicao tributaria 										-OC
						  "|" +Util.formatNumber(vlCOFINSSub, "#.##")+									                                                                        //Valor total da CONFINS retido por substituicao tributaria 									-OC
						  "|" +"\r\n";
				
				nrLinhaBlocoC++;
				conjuntoRegistro.put("C100", (((Integer) conjuntoRegistro.get("C100")) + 1));
		    }
		    
		    //entrouDs = false;
			
			
//					if(cdNfe > 0){//Por Enquanto só será informado os dados de interesse do fisco de Notas Fiscais Eletronicas  - APRESENTAR
//						/*
//						 * C110 - Informação complementar da nota fiscal - Referente a C100
//						 */
//						registro += "|C110" + //REG
//											  "|" +nfe.getCdNotaFiscal()+ //Código da informação complementar - Codigo deve ser o mesmo do registro 0450
//											  "|" +nfe.getTxtInformacaoFisco()+ //Descrição complementar do código de referência 
//											  "|" +"\r\n";
//						
//						nrLinhaBlocoC++;
//					}
			
			
			
			
			
//					/*
//					 * C111 - Processo referenciado. - Referente a C110  - VERIFICAR COMO ESSE REGISTRO FUNCIONA - deverá ser retirado ????
//					 */
//					String C111 = "C111" + //REG
//										  Util.fillAlpha("",15)+	//Numero do processo
//										  Util.fillAlpha(String.valueOf(IND_PROC_JE),1)+
//										  "\r\n";
//					nrLinhaBlocoC++;
			
			
//					/*
//					 * C112 - Documento de arrecadação referenciado. - Refernte a C110  - VERIFICAR COMO ESSE REGISTRO FUNCIONA  - deverá ser retirado ????
//					 */
//					String C112 = "C112" + //REG
//										  Util.fillAlpha(String.valueOf(COD_DA_DOC_ESTADUAL),1)+	//Numero do processo
//										  Util.fillAlpha("",2)+	//Unidade federada beneficiária do recolhimento
//										  Util.fillAlpha("",10)+ //Numero do documento de arrecadação
//										  Util.fillAlpha("",10)+ //Código completo da autenticação bancária.
//										  Util.fillNum(0,10)+  //Valor do total do documento de arrecadação
//										  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+			//Data de vencimento
//										  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+			//Data de pagamento
//										  "\r\n";
//					nrLinhaBlocoC++;
			
			
//			if(documento.getTpEntrada() == DocumentoEntradaServices.ENT_DEVOLUCAO){
//			
//
//				
//					DocumentoSaida docReferenciado = DocumentoSaidaDAO.get(documento.getCdDocumentoSaidaOrigem());
//				
//					
//					if(docReferenciado != null && docReferenciado.getTpDocumentoSaida() == DocumentoSaidaServices.TP_NOTA_FISCAL_VENDA){
//						
//						/*
//						 * C113 - Documento Fiscal Referenciado - Refernte a C110 - VERIFICAR COMO ESSE REGISTRO FUNCIONA   - APRESENTAR
//				
//							APENAS NFE e Documentos de entrada de devolução
//				
//				
//						 */						
//						registro += "|C113"+
//											  "|" +IND_OPER_ENTRADA+ //Indicador do tipo de operacao
//											  "|" +IND_EMIT_PROPRIA+ //Indicador do emitente do titulo
//											  "|" +documento.getCdFornecedor()+ //Código do participante emitente
//											  "|" +"01"+ //Código da modalidade do documento fiscal
//											  "|" +"001"+ //Serie do documento fiscal
//											  "|" +  //SubSerie do documento fiscal
//											  "|" +Util.limparFormatos(docReferenciado.getNrDocumentoSaida().substring(0, 9), 'N')+  //Numero do documento fiscal
//											  "|" +Util.formatDateTime(docReferenciado.getDtEmissao(), "ddMMyyyy")+ //Data da emissão do documento fiscal
//											  "|" +"\r\n";
//						nrLinhaBlocoC++;
//						conjuntoRegistro.put("C113", (((Integer) conjuntoRegistro.get("C113")) + 1));
//						
//					}
//					
//					if(docReferenciado != null && docReferenciado.getTpDocumentoSaida() == DocumentoSaidaServices.TP_CUPOM_FISCAL){
//						
//						/*
////						 * C114 - Cupom fiscal referenciado - Refernte a C110 - VERIFICAR COMO ESSE REGISTRO FUNCIONA    - APRESENTAR
////						 * 
////						 * //				APENAS NFE e Documentos de entrada de devolução
////						 * 
////						 * 
////						 */
//						registro += "|C114"+
//											  "|" +"02"+ //Código do modelo do documento fiscal
//											  "|" +Util.fillAlpha("", 20)+ //Número de serie de fabricação do ECF
//											  "|" +Util.fillNum(0, 3)+ //Número do caixa atribuido ao ECF 
//											  "|" +docReferenciado.getNrDocumentoSaida()+ //Número do documento fiscal
//											  "|" + Util.formatDateTime(docReferenciado.getDtEmissao(), "ddMMyyyy")+ //Data da emissão do documento fiscal
//											  "|" +"\r\n";
//						nrLinhaBlocoC++;
//						conjuntoRegistro.put("C114", (((Integer) conjuntoRegistro.get("C114")) + 1));
//						
//					}
//			}
			
		
			
//					/*
//					 * C115 - Local da coleta e/ou entrega - Refernte a C110 - VERIFICAR - deverá ser retirado ???? 
//					 */
//					String C115 = "C115"+
//										  Util.fillNum(IND_CARGA_AEREO, 1)+ //Indicador do tipo de transporte
//										  Util.fillNum(0, 14)+ //Número do CNPJ do contribuinte do local de coleta
//							  			  Util.fillAlpha("", 14)+ //Inscrição estadual do contribuinte do local da coleta
//							  			  Util.fillNum(0, 11)+ //Número do CPF do contribuinte do local de coleta
//							  			  Util.fillNum(0, 7)+ //Código do municipio do local de coleta
//							  			  Util.fillNum(0, 14)+ //Número do CNPJ do contribuinte do local de entrega
//							  			  Util.fillAlpha("", 14)+ //Inscrição estadual do contribuinte do local da entrega
//							  			  Util.fillNum(0, 11)+ //Número do CPF do contribuinte do local de entrega
//							  			  Util.fillNum(0, 7)+ //Código do municipio do local de entrega
//							  			  "\r\n";
//					nrLinhaBlocoC++;
			
			/*
			 * C116 - Cupom fiscal eletrônico referenciado - Refernte a C110 - VERIFICAR   - APRESENTAR
			 * 
			 * APENAS NFE e Documentos de entrada de devolução
			 * 
			 * 
			 */
//					registro += "C116"+
//										  Util.fillAlpha("", 2)+//Código do modelo do documento fiscal, conforme a Tabela 4.1.1
//										  Util.fillNum(0, 9)+ //Número de Série do equipamento SAT
//										  Util.fillNum(0, 44)+ //Chave do Cupom Fiscal Eletrônico
//										  Util.fillNum(0, 6)+ //Número do cupom fiscal eletrônico
//										  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+ //Data da emissão do documento fiscal
//										  "\r\n";
//					nrLinhaBlocoC++;
			
			
//					if(cdNota1A > 0){//Fazer outro teste para saber se é um documento de entrada de importação  - APRESENTAR
//						/*
//						 * C120 - OPERAÇÕES DE IMPORTAÇÃO (CÓDIGO 01) - Refernte a C100 - Apenas para Documentos de Entrada - Verificar como será implementado
//						 */
//						registro += "|C120"+
//								  "|" +COD_DOC_IMP_IMPORTACAO+ //Documento de importação
//								  "|" +nrDocumento+//Número do documento de Importação.
//								  "|" +//Valor pago de PIS na importação
//								  "|" +//Valor pago de COFINS na importação
//								  "|" +//Número do Ato Concessório do regime Drawback
//								  "|" +"\r\n";
//						nrLinhaBlocoC++;
//					}
//					/*
//					 * C130 - ISSQN, IRRF E PREVIDÊNCIA SOCIAL - Refernte a C100 - Apenas para Documentos de Saida - Ver de onde pegar esses dados no Banco de Dados
//					 */
//					String C130 = "C130"+
//							  Util.fillNum(0, 6)+ //Valor dos serviços sob não-incidência ou não tributados pelo ICMS
//							  Util.fillNum(0, 6)+ //Valor da base de cálculo do ISSQN
//							  Util.fillNum(0, 6)+ //Valor do ISSQN
//							  Util.fillNum(0, 6)+ //Valor da base de cálculo do Imposto de Renda Retido na Fonte
//							  Util.fillNum(0, 6)+ //Valor do Imposto de Renda - Retido na Fonte
//							  Util.fillNum(0, 6)+ //Valor da base de cálculo de retenção da Previdência Social
//							  Util.fillNum(0, 6)+ //Valor destacado para retenção da Previdência Social
//							  "\r\n";
			
			if(documento.getTpDocumentoEntrada() == DocumentoEntradaServices.TP_NOTA_FISCAL){
			
				/*
				 * Registro C140 - Refernte a C100 - FATURA
				 */
				
				//Tipo de Documento
				TipoDocumento tipoDoc = getTipoDocumentoENT(contasAPagar);
				
				//Valor total das contas a pagar
				float valorTotalContas = getValorTotalContasAPagar(contasAPagar);
				
				if(contasAPagar.size() > 1){
				
					/************************************** VALIDACAO *******************************************************/
					//Valor total das Parcelas
					if(String.valueOf(valorTotalContas) == null || String.valueOf(valorTotalContas).trim().equals(""))
				    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor total das parcelas, Documento número: " + documento.getNrDocumentoEntrada();
				    if(!dsValidacao.equals("")){
				    	HashMap<String, Object> register = new HashMap<String, Object>();
				    	register.put("ERRO", "Dados da fatura faltando: "+dsValidacao);
				    	rsm.addRegister(register);
				    
				    	dsValidacao = "";
				    	continue;
				    }
				    registro += "|C140"+
							              "|" +IND_EMIT_PROPRIA+
										  "|" +(tipoDoc == null || tipoDoc.getIdSped() == null ? "99" : tipoDoc.getIdSped())+
										  "|" +//Descrição complementar do título de crédito - OC
										  "|" +documento.getCdDocumentoEntrada()+//Número ou código identificador dos títulos de crédito - CRIAR CODIGO PARA TITULO DE CREDITO
										  "|" +contasAPagar.size()+ //Quantidade de parcelas a receber/pagar
										  "|" +Util.formatNumber(valorTotalContas, "#.##")+ //Valor total dos títulos de créditos
										  "|" +"\r\n";
					nrLinhaBlocoC++;
					conjuntoRegistro.put("C140", (((Integer) conjuntoRegistro.get("C140")) + 1));
					
					
					if(contasAPagar.size()==0){
				    	
				    	HashMap<String, Object> register = new HashMap<String, Object>();
				    	register.put("ERRO", "Dados do do documento de entrada Nº "+documento.getNrDocumentoEntrada()+": Nenhum Título de Crédito foi identificado para esse documento");
				    	rsm.addRegister(register);
				    
				    	dsValidacao = "";
				    	//entrouDs = true;
				    }
			    	
					
					
					for(int k = 0; k < contasAPagar.size(); k++){
						/*
						 * Registro C141 - Refernte a C140 - Vencimento de fatura
						 */
						int codigoConta = contasAPagar.get(k);
						ContaPagar conta = ContaPagarDAO.get(codigoConta);
						String vlConta = Util.formatNumber(conta.getVlConta(), "#.##");
						/************************************** VALIDACAO *******************************************************/
						//Data de Vencimento
						if(conta.getDtVencimento() == null)
					    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Data de Vencimento da conta a pagar ";
						//Valor do titulo de credito
						if(String.valueOf(conta.getVlConta()) == null || String.valueOf(conta.getVlConta()).trim().equals(""))
					    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor da conta a pagar ";
					    if(!dsValidacao.equals("")){
					    	HashMap<String, Object> register = new HashMap<String, Object>();
					    	register.put("ERRO", "Dados da conta a pagar Nº "+conta.getNrDocumento()+" faltando: "+dsValidacao);
					    	rsm.addRegister(register);
					    
					    	dsValidacao = "";
					    	//entrouDs = true;
					    }
					    if(!entrouDs && !entrouDs2){
						    registro += "|C141"+
												  "|" +(k + 1)+ //Número da parcela a receber/pagar
												  "|" +Util.formatDateTime(conta.getDtVencimento(), "ddMMyyyy")+ //Data de vencimento da parcela
												  "|" +vlConta+ //Valor da parcela a receber/pagar
												  "|" +"\r\n";
							nrLinhaBlocoC++;
							conjuntoRegistro.put("C141", (((Integer) conjuntoRegistro.get("C141")) + 1));
					    }
					    
					    //entrouDs = false;
					}
				}
			
			}
			
//			float valorReduzidoTotal = 0;
//			float valorTotalItens = 0;
//			float valorBaseTotal = 0;
//			
			float vlDescontoItens = 0;
			
			int cdItem = 1;
			do{
				/*
				 * Registro C170 - Refernte a C100 - Itens do documento
				 */
				/************************************** VALIDACAO *******************************************************/
				//Quantidade do Item
				if(String.valueOf(rsmProdutos.getFloat("qt_entrada")) == null || String.valueOf(rsmProdutos.getFloat("qt_entrada")).trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Quantidade ";
				
				//Unidade de Medida
				if(rsmProdutos.getString("cd_unidade_medida") == null || rsmProdutos.getString("cd_unidade_medida").trim().equals("") || rsmProdutos.getInt("cd_unidade_medida") == 0)
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Unidade de medida ";
				//Valor Unitario
				if(String.valueOf(rsmProdutos.getFloat("vl_unitario")) == null || String.valueOf(rsmProdutos.getFloat("vl_unitario")).trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor unitario ";
				//CFOP
				if(rsmProdutos.getString("nr_codigo_fiscal") == null || rsmProdutos.getString("nr_codigo_fiscal").trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CFOP ";
				//Tributação
//				if(!ProdutoServicoServices.getAllTributos(rsmProdutos.getInt("cd_produto_servico")).next())
//			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Tributação do produto: " + rsmProdutos.getString("nm_produto_servico") + " - Documento de Saída Nº: " + documento.getNrDocumentoEntrada();
				if(!dsValidacao.equals("")){
					HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados do Item "+rsmProdutos.getString("nm_produto_servico")+" no documento de entrada Nº "+documento.getNrDocumentoEntrada()+" faltando: "+dsValidacao);
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	//entrouDs = true;
				}
//			    valorTotalItens += (rsmProdutos.getFloat("qt_entrada") * rsmProdutos.getFloat("vl_unitario"));
//			    valorBaseTotal  += rsmProdutos.getFloat("vl_base_calculo");
//			    
				vlICMS  	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_icms"));
				vlBaseICMS 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_base_icms"));
				vlBaseICMSSub = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_base_icms") : 0);
				vlICMSSub 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_icms") : 0);
				
				vlBaseIPI 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_base_ipi"));
				vlIPI 		  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_ipi"));
				vlBaseIPISub  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_base_ipi") : 0);
				vlIPISub 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_ipi") : 0);
				
				vlBasePIS 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_base_pis"));
				vlPIS 		  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_pis"));
				vlBasePISSub  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_base_pis") : 0);
				vlPISSub 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_pis") : 0);
				
				vlBaseCOFINS 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_base_cofins"));
				vlCOFINS 		  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_cofins"));
				vlBaseCOFINSSub  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_base_cofins") : 0);
				vlCOFINSSub 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_cofins") : 0);
				
				
				float prAliquotaICMSSub = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("pr_icms") : 0);
			    float prAliquotaIPI 	= rsmProdutos.getFloat("pr_ipi");
			    float prAliquotaPIS 	= rsmProdutos.getFloat("pr_pis");
			    float prAliquotaCOFINS  = rsmProdutos.getFloat("pr_cofins");
				
			    vlDescontoItens += Util.roundFloat(rsmProdutos.getFloat("vl_desconto"), 2);
			    
			    
			    String cst = (rsmProdutos.getString("nr_situacao_tributaria_icms") != null && !rsmProdutos.getString("nr_situacao_tributaria_icms").equals("") ? rsmProdutos.getString("nr_situacao_tributaria_icms") : "041");
			    
			    String parteCst = cst.substring(1);
			    
			    if((parteCst.equals("00") || 
			    	parteCst.equals("10") || 
			    	parteCst.equals("20") || 
			    	parteCst.equals("70")) &&
			    	rsmProdutos.getFloat("pr_icms") == 0){
			    	
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("AVISO", "Dados dos Itens do documento de entrada Nº "+documento.getNrDocumentoEntrada()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : Se o CST termina em 00, 10, 20 ou 70 a Aliquota deve ser maior do que zero.");
			    	rsm2.addRegister(register);			    
			    }
			    
			    if(Util.arredondar(rsmProdutos.getFloat("qt_entrada"), (rsmProdutos.getInt("qt_precisao_custo") > 5 ? 5 : rsmProdutos.getInt("qt_precisao_custo"))) == 0){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados dos Itens do documento de entrada Nº "+documento.getNrDocumentoEntrada()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : Quantidade do item zerada.");
			    	rsm.addRegister(register);
			    }
			    			    
				if(!entrouDs && !entrouDs2){
					registro += "|C170"+
									  "|" +cdItem+++//Número sequencial do item no documento fiscal
									  "|" +(rsmProdutos.getString("cd_produto_servico"))+//Código do item 
									  "|" +((rsmProdutos.getString("txt_produto_servico") != null) ? rsmProdutos.getString("txt_produto_servico").trim() : "")+//Descrição complementar do item como adotado no documento fiscal 												-OC
									  "|" +Util.formatNumber(rsmProdutos.getFloat("qt_entrada"), (rsmProdutos.getInt("qt_precisao_custo") > 5 ? 5 : rsmProdutos.getInt("qt_precisao_custo")))+//Quantidade do item
									  "|" +((rsmProdutos.getString("sg_unidade_medida").trim().length() > 6 ) ? rsmProdutos.getString("sg_unidade_medida").trim().substring(0, 6) : rsmProdutos.getString("sg_unidade_medida").trim())+//Unidade do item
									  "|" +Util.formatNumber((Util.arredondar(rsmProdutos.getFloat("qt_entrada"), (rsmProdutos.getInt("qt_precisao_custo") > 5 ? 5 : rsmProdutos.getInt("qt_precisao_custo"))) * rsmProdutos.getFloat("vl_unitario") + rsmProdutos.getFloat("vl_acrescimo") - rsmProdutos.getFloat("vl_desconto")), "#.##")+//Valor total do item (mercadorias ou serviços)
									  "|" +((rsmProdutos.getFloat("vl_desconto") != 0) ? Util.formatNumber(rsmProdutos.getFloat("vl_desconto"), "#.##") : "0,00")+//Valor do desconto comercial 																					-OC
									  "|" +IND_MOV_SIM+//Movimentação física do ITEM/PRODUTO
						              "|" +(cst.length() < 3 ? "0" + cst : cst)+//Código da Situação Tributária referente ao ICMS, conforme a Tabela indicada no item 4.3.1 http://www.cosif.com.br/mostra.asp?arquivo=sinief-CST-anexo
						              "|" +rsmProdutos.getString("nr_codigo_fiscal")+//Código Fiscal de Operação e Prestação
						              "|" +//Código da natureza da operação                                                                                - Codigo do proprio EFD - IMplementar Campo 2 do Registro 0400
						              "|" +Util.formatNumber(vlBaseICMS, "#.##")+//Valor da base de cálculo do ICMS 																		-OC
						              "|" +((rsmProdutos.getFloat("pr_icms") != 0) ? Util.formatNumber(rsmProdutos.getFloat("pr_icms"), "#.##") : "0,00" )+//Alíquota do ICMS 																															-OC
						              "|" +Util.formatNumber(vlICMS, "#.##") +//Valor do ICMS creditado/debitado 											-OC
						              "|" +Util.formatNumber(vlBaseICMSSub, "#.##") +//"0,00" +//Valor da base de cálculo referente à substituição tributária 																																										-OC
						              "|" +Util.formatNumber(prAliquotaICMSSub, "#.##") +//Alíquota do ICMS da substituição tributária na unidade da federação de destino 																																					-OC
						              "|" +Util.formatNumber(vlICMSSub, "#.##") + //"0,00" +//Valor do ICMS referente à substituição tributária 																																													-OC
						              "|" +//Movimentação física do ITEM/PRODUTO 																																																-OC
						              "|" +((vlBaseIPI > 0 || vlBaseIPISub > 0) && !rsmProdutos.getString("nr_situacao_tributaria").equals("") && rsmProdutos.getString("nr_situacao_tributaria") != null ? rsmProdutos.getString("nr_situacao_tributaria") : "") +//Código da Situação Tributária referente ao IPI 																								-OC
						              "|" +//Código de enquadramento legal do IPI 																																																-OC
						              "|" +Util.formatNumber(vlBaseIPI, "#.##") + //"0,00" +//Valor da base de cálculo do IPI 																																																	-OC
						              "|" +Util.formatNumber(prAliquotaIPI, "#.##") +//Alíquota do IPI 																																																					-OC
						              "|" +Util.formatNumber(vlIPI, "#.##") +//Valor do IPI creditado/debitado 																																																	-OC
						              "|" +((vlBasePIS > 0 || vlBasePISSub > 0) && !rsmProdutos.getString("nr_situacao_tributaria").equals("") && rsmProdutos.getString("nr_situacao_tributaria") != null ? rsmProdutos.getString("nr_situacao_tributaria") : "") +//Código da Situação Tributária referente ao PIS. 																																												-OC
						              "|" +Util.formatNumber(vlBasePIS, "#.##") + //"0,00" +//Valor da base de cálculo do PIS 																																																	-OC
						              "|" +Util.formatNumber(prAliquotaPIS, "#.####") +//Alíquota do PIS (em percentual) 																																																	-OC
						              "|" +((vlBasePIS > 0 || vlBasePISSub > 0) ? Util.formatNumber(rsmProdutos.getFloat("qt_entrada"), "#.###") : "0,000") + //"0,000" +//Quantidade  Base de cálculo PIS 																																																	-OC
						              "|" + //"0,0000" +//Alíquota do PIS (em reais) 																																																		-OC
						              "|" +Util.formatNumber(vlPIS, "#.##") +// "0,00" +//Valor do PIS 																																																						-OC
						              "|" +((vlBaseCOFINS > 0 || vlBaseCOFINSSub > 0) && !rsmProdutos.getString("nr_situacao_tributaria").equals("") && rsmProdutos.getString("nr_situacao_tributaria") != null ? rsmProdutos.getString("nr_situacao_tributaria") : "") +//Código da Situação Tributária referente ao COFINS. 																																											-OC
						              "|" +Util.formatNumber(vlBaseCOFINS, "#.###") + //"0,00" +//Valor da base de cálculo da COFINS 																																																-OC
						              "|" +Util.formatNumber(prAliquotaCOFINS, "#.####") +//Alíquota do COFINS (em percentual) 																																																-OC
						              "|" +((vlBaseCOFINS > 0 || vlBaseCOFINSSub > 0) ? Util.formatNumber(rsmProdutos.getFloat("qt_entrada"), "#.###") : "0,000") + //"0,000" +//Quantidade  Base de cálculo COFINS 																																																-OC
						              "|" + //"0,0000" +//Alíquota da COFINS (em reais) 																																																		-OC
						              "|" +Util.formatNumber(vlCOFINS, "#.####") + //"0,00" +//Valor da COFINS 																																																					-OC
						              "|" +//Código da conta analítica contábil debitada/creditada 																																												-OC			                                           	              
									  "|" +"\r\n";
					nrLinhaBlocoC++;
					conjuntoRegistro.put("C170", (((Integer) conjuntoRegistro.get("C170")) + 1));
				}
				
				//entrouDs = false;
				
				if(isCombustivel(documento, cdEmpresa)){//Apenas Para Entradas  - APRESENTAR
					
					/*
					 * Registro C171 - Refernte a C170 - Armazenamento de combustiveis                                 - Implementar
					 */
					criterios = new ArrayList<ItemComparator>();
					criterios.add(new ItemComparator("CD_PRODUTO_SERVICO", Integer.toString(rsmProdutos.getInt("cd_produto_servico")), ItemComparator.EQUAL, Types.INTEGER));
					ResultSetMap rsmTanque = TanqueDAO.find(criterios);
					String nrTanque = "";
					String vlArmazenado = "";
					while(rsmTanque.next()){
						vlArmazenado = "";//COLOCAR METODO PARA SABER QUANTO JA TEM ARMAZENADO NO TANQUE
						nrTanque = rsmTanque.getString("cd_tanque");
					}
					registro += "|C171" +
								"|" +nrTanque+//Tanque onde foi armazenado o combustível
								"|" +vlArmazenado+//Quantidade ou volume armazenado
								"|" +"\r\n";
					nrLinhaBlocoC++;
					conjuntoRegistro.put("C171", (((Integer) conjuntoRegistro.get("C171")) + 1));
					
					
				}
				
//						if(false){//Teste para saber se utiliza o ISSQN
//							/*
//							 * Registro C172 - OPERAÇÕES COM ISSQN
//							 */
//							String C172 = "C172" +
//												  Util.fillNum(0, 6)+//Valor da base de cálculo do ISSQN
//												  Util.fillNum(0, 6)+//Alíquota do ISSQN
//												  Util.fillNum(0, 6)+//Valor do ISSQN
//												  "\r\n";
//						}
				
//						/*
//						 * Registro C177 - OPERAÇÕES COM PRODUTOS SUJEITOS A SELO DE CONTROLE IPI.
//						 */
//						String C177 = "C177" +
//											  Util.fillAlpha("", 6)+//Código do selo de controle do IPI,
//											  Util.fillNum(0, 12)+//Quantidade de selo de controle do IPI aplicada
//											  "\r\n";
				
//						/*
//						 * Registro C178 - OPERAÇÕES COM PRODUTOS SUJEITOS À TRIBUTAÇÀO DE IPI POR UNIDADE OU QUANTIDADE DE PRODUTO.
//						 */
//						String C178 = "C178" +
//								 			  Util.fillAlpha("", 60)+//Código da classe de enquadramento do IPI
//								 			  Util.fillNum(0, 6)+//Valor por unidade padrão de tributação
//								 			  Util.fillNum(0, 6)+//Quantidade total de produtos na unidade padrão de tributação
//											  "\r\n";
				
//						/*
//						 * Registro C179 - Refernte a C170 - INFORMAÇÕES COMPLEMENTARES                  - Ver se irá implementar
//						 */
//						registro += "C179" +
//											  Util.fillNum(0, 6)+//Valor da base de cálculo ST na origem/destino em operações interestaduais.
//											  Util.fillNum(0, 6)+//Valor do ICMS-ST a repassar/deduzir em operações interestaduais
//								 			  Util.fillNum(0, 6)+//Valor do ICMS-ST a complementar à UF de destino
//								 			  Util.fillNum(0, 6)+//Valor da BC de retenção em remessa promovida por Substituído intermediário
//								 			  Util.fillNum(0, 6)+//Valor da parcela do imposto retido em remessa promovida por substituído intermediário
//								 			  "\r\n";
//						nrLinhaBlocoC++;
				
				
				
			}while(rsmProdutos.next());
			
//			
//			valorReduzidoTotal = valorTotalItens - valorBaseTotal;
			/*
			 * Registro C190 - Refernte a C100 - REGISTRO ANALÍTICO DO DOCUMENTO
			 */
			if(vlDescontos > (vlDescontoItens + 0.01) || vlDescontoItens > (vlDescontos + 0.01)){
				HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("AVISO", "Dados dos Itens do documento de entrada Nº "+documento.getNrDocumentoEntrada()+": Valor dos Descontos dos itens diferente do Valor de Desconto do Documento.");
		    	rsm2.addRegister(register);
			}
			
			ResultSetMap rsmTrib = getTributacao(cdDocumentoEntrada, 1);
			ResultSetMap rsmTribByEstado = getTributacaoByEstado(cdDocumentoEntrada, 1);
			/************************************** VALIDACAO *******************************************************/
			//Natureza de Operacao
			//CFOP
			
			float vlItensTotal = 0;
			while(rsmTribByEstado.next()){
				String inicialCfop = rsmTribByEstado.getString("nr_codigo_fiscal").substring(0, 1);
				boolean hasEstado = false;
				while(rsmApuracaoIcms.next()){
					if(rsmApuracaoIcms.getRegister().get("SG_ESTADO").equals(rsmTribByEstado.getString("sg_estado"))){
						hasEstado = true;
						if(((inicialCfop.equals("1") || inicialCfop.equals("2") || inicialCfop.equals("3")) &&
						    	   (!rsmTribByEstado.getString("nr_codigo_fiscal").equals("1605"))) || rsmTribByEstado.getString("nr_codigo_fiscal").equals("5605")){
							rsmApuracaoIcms.setValueToField("VL_ICMS_CREDITO",  rsmApuracaoIcms.getFloat("VL_ICMS_CREDITO") + rsmTribByEstado.getFloat("vl_icms"));
							rsmApuracaoIcms.setValueToField("VL_ICMS_CREDITO_ST",  rsmApuracaoIcms.getFloat("VL_ICMS_CREDITO_ST") + rsmTribByEstado.getFloat("vl_icms_substituto"));
							rsmApuracaoIcms.setValueToField("VL_ICMS_CREDITO_ST_DEVOLUCAO",  rsmApuracaoIcms.getFloat("VL_ICMS_CREDITO_ST_DEVOLUCAO") + rsmTribByEstado.getFloat("vl_icms_substituto"));
						}
						if(rsmTribByEstado.getString("nr_codigo_fiscal").equals("1605")){
							rsmApuracaoIcms.setValueToField("VL_ICMS_DEBITO",  ((Float)rsmApuracaoIcms.getRegister().get("VL_ICMS_DEBITO")) + rsmTribByEstado.getFloat("vl_icms"));
				    	}
						break;
					}
				}
				if(!hasEstado){
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("SG_ESTADO", rsmTribByEstado.getString("sg_estado"));
					if(((inicialCfop.equals("1") || inicialCfop.equals("2") || inicialCfop.equals("3")) &&
					    	   (!rsmTribByEstado.getString("nr_codigo_fiscal").equals("1605"))) || rsmTribByEstado.getString("nr_codigo_fiscal").equals("5605")){
						register.put("VL_ICMS_CREDITO", rsmTribByEstado.getFloat("vl_icms"));
						register.put("VL_ICMS_CREDITO_ST", rsmTribByEstado.getFloat("vl_icms_substituto"));
						register.put("VL_ICMS_CREDITO_ST_DEVOLUCAO", rsmTribByEstado.getFloat("vl_icms_substituto"));
					}
					if(rsmTribByEstado.getString("nr_codigo_fiscal").equals("1605")){
						register.put("VL_ICMS_DEBITO", rsmTribByEstado.getFloat("vl_icms"));
					}
					rsmApuracaoIcms.addRegister(register);
				}
				rsmApuracaoIcms.beforeFirst();
				
		    	
		    	
			}
			
//			if(!rsmTrib.next())
//				return new Result(-1, "ERRO na tributação de um produto do Documento de Entrada N." + documento.getNrDocumentoEntrada());
			while(rsmTrib.next()){
				/************************************** VALIDACAO *******************************************************/
				//Natureza de Operacao
				//CFOP
				if(rsmTrib.getString("nr_codigo_fiscal") == null)
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CFOP ";
				
			    if(!dsValidacao.equals("")){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados Analiticos do Documento de entrada Nº "+documento.getNrDocumentoEntrada()+": "+dsValidacao);
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	//entrouDs = true;
			    }
			    
			    vlItensTotal += rsmTrib.getFloat("vl_itens");
			    
			    if(!entrouDs && !entrouDs2){
			    	String inicialCfop = rsmTrib.getString("nr_codigo_fiscal").substring(0, 1);
			    	if(((inicialCfop.equals("1") || inicialCfop.equals("2") || inicialCfop.equals("3")) &&
			    	   (!rsmTrib.getString("nr_codigo_fiscal").equals("1605"))) || rsmTrib.getString("nr_codigo_fiscal").equals("5605")){
			    		vlIcmsCredito   += rsmTrib.getFloat("vl_icms");
			    	}
			    	if(rsmTrib.getString("nr_codigo_fiscal").equals("1605")){
			    		vlIcmsDebito += rsmTrib.getFloat("vl_icms");
			    	}
			    	String cst = rsmTrib.getString("nr_situacao_tributaria") != null && !rsmTrib.getString("nr_situacao_tributaria").equals("") ? rsmTrib.getString("nr_situacao_tributaria") : "041";
					cst = (cst.length() < 3 ? "0" + cst : cst);
					
			    	registro += "|C190" +
										  "|" +cst+//Código da Situação Tributária. - http://www.cosif.com.br/mostra.asp?arquivo=sinief-CST-anexo
										  "|" +rsmTrib.getString("nr_codigo_fiscal")+//Código Fiscal de Operação e Prestação do agrupamento de itens.
										  "|" +((rsmTrib.getFloat("pr_aliquota") != 0) ? Util.formatNumber(rsmTrib.getFloat("pr_aliquota"), "#.##") : "0,00")+//Alíquota do ICMS
										  "|" +Util.formatNumber((Util.arredondar(rsmTrib.getFloat("vl_itens"), 2)), "#.##")+//Valor da operação na combinação de CST_ICMS, CFOP e alíquota do ICMS.
										  "|" +Util.formatNumber(rsmTrib.getFloat("vl_base_icms"), "#.##")+//Parcela correspondente ao "Valor da base de cálculo do ICMS" referente à combinação de CST_ICMS, CFOP e alíquota do ICMS. 			-OC
										  "|" +Util.formatNumber(rsmTrib.getFloat("vl_icms"), "#.##")+//Parcela correspondente ao "Valor do ICMS" referente à combinação de CST_ICMS, CFOP e alíquota do ICMS.
										  "|" +Util.formatNumber(rsmTrib.getFloat("vl_base_icms_substituto"), "#.##")+//Parcela correspondente ao "Valor da base de cálculo do ICMS" da substituição tributária referente à combinação de CST_ICMS, CFOP e alíquota do ICMS.
										  "|" +Util.formatNumber(rsmTrib.getFloat("vl_icms_substituto"), "#.##")+//Parcela correspondente ao valor creditado/debitado do ICMS da substituição tributária, referente à combinação de CST_ICMS, CFOP, e alíquota do ICMS.
										  "|" +"0,00"+//Valor não tributado em função da redução da base de cálculo do ICMS, referente à combinação de CST_ICMS, CFOP e alíquota do ICMS.
										  "|" +Util.formatNumber(rsmTrib.getFloat("vl_ipi"), "#.##")+//Parcela correspondente ao "Valor do IPI" referente à combinação CST_ICMS, CFOP e alíquota do ICMS.
			//								  "|" +Util.fillAlpha("", 6)+//Código da observação do lançamento fiscal   campo 2 do registro 0460        - Ver se na BA eh necessario preencher tal campo - PARA NFE POR ENQUANTO
										  "|" + //Codigo da obsercação do lançamento fiscal																																					-OC
										  "|" +"\r\n";
					nrLinhaBlocoC++;
					conjuntoRegistro.put("C190", (((Integer) conjuntoRegistro.get("C190")) + 1));
			    }
			    
			    //entrouDs = false;
			}			
			
			//Valor do total de Operacoes e Valor Total do Documento
//			if(!vlTotalDocumento.equals(Util.formatNumber(vlItensTotal, "#.##"))){
//				HashMap<String, Object> register = new HashMap<String, Object>();
//		    	register.put("AVISO", "Dados Analiticos do Documento de entrada Nº "+documento.getNrDocumentoEntrada()+": Valor do Total do Documento em C100 é diferente da soma dos valores de operação de C190");
//		    	rsm2.addRegister(register);
//		    
//		    	dsValidacao = "";
//		   }

		    //entrouDs = false;
			
//					/* POR ENQUANTO APENAS NFE TERA ESSE REGISTRO, DEPOIS ESTENDER PARA DOCUMENTOS DE ENTRADA/SAIDA
//					 * Registro C195 - Refernte a C100 - OBSERVAÇOES DO LANÇAMENTO FISCAL - Este registro deve ser informado quando, em decorrência da legislação estadual, houver ajustes nos documentos
//																		  fiscais, informações sobre diferencial de alíquota, antecipação de imposto e outras situações
//					 */
//					registro += "C195" +
//										  Util.fillAlpha("", 6)+//Código da observação do lançamento fiscal
//										  Util.fillAlpha("", 30)+//Descrição complementar do código de observação.
//										  "\r\n";
//					nrLinhaBlocoC++;
			
			
//					/*
//					 * Registro C197 - Refernte a C195 - OUTRAS OBRIGAÇÕES TRIBUTÁRIAS, AJUSTES E INFORMAÇÕES DE VALORES PROVENIENTES DE DOCUMENTO FISCAL    - Verificar se será utilizado
//					 */
//					String C197 = "C197"+
//										  Util.fillAlpha("", 10)+//Código da observação do lançamento fiscal
//										  Util.fillAlpha("", 6)+//Descrição complementar do ajuste do documento fiscal
//										  Util.fillAlpha("", 60)+//Código do item
//										  Util.fillNum(0, 6)+//Base de cálculo do ICMS ou do ICMS ST
//										  Util.fillNum(0, 6)+//Alíquota do ICMS
//										  Util.fillNum(0, 6)+//Valor do ICMS ou do ICMS ST
//										  Util.fillNum(0, 6)+//Outros valores
//										  "\r\n";
//					nrLinhaBlocoC++;
			
		}
		
		ArrayList<String> registroNota = new ArrayList<String>();
		//Nota Fiscal Eletronica
		for(int i = 0; i < codigosNotaFiscal.size(); i++){
			//Código do documento
			int cdNotaFiscal = codigosNotaFiscal.get(i);
			//Documento
				NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal);
				//Numero do documento
				int nrDoc = 0;
				if(nota.getNrNotaFiscal() != null && !nota.getNrNotaFiscal().trim().equals("")){
					nrDoc = Integer.parseInt(Util.limparFormatos(nota.getNrNotaFiscal(), 'N'));
				}
				String nrDocumento = ((nrDoc > 999999999) ? String.valueOf(nrDoc).substring(0, 9) : String.valueOf(nrDoc));
				//Valor do documento
				float vlTotalDocumento = NotaFiscalServices.getValorAllItens(nota.getCdNotaFiscal());
				//Valor de Desconto do documento
				String vlDescontos = Util.formatNumber(NotaFiscalServices.getVlDesconto(nota.getCdNotaFiscal()), "#.##");
				//Situação do documento
				String stDocumentoFiscal = ST_DOCUMENTO_REGULAR;
				
				if(nota.getStNotaFiscal() == NotaFiscalServices.CANCELADA){
					stDocumentoFiscal = ST_DOCUMENTO_CANCELADO;
				}
				else if(nota.getStNotaFiscal() == NotaFiscalServices.DENEGADA){
					stDocumentoFiscal = ST_NFE_DENEGADO;
				}		
				//Selecionar o Tipo de Pagamento
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("CD_NOTA_FISCAL", Integer.toString(nota.getCdNotaFiscal()), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmNotaDocVinc = NotaFiscalDocVinculadoDAO.find(criterios);
				int cdDocumentoSaida = 0;
				int cdDocumentoEntrada = 0;
				if(rsmNotaDocVinc.next()){
					cdDocumentoSaida = rsmNotaDocVinc.getInt("cd_documento_saida");
					cdDocumentoEntrada = rsmNotaDocVinc.getInt("cd_documento_entrada");
				}
				
	//			ArrayList<Integer> codigosTitulos = getTitulosDeCredito(cdDocumentoSaida);
				
				//Contas a Receber pertencentes ao documento
				ArrayList<Integer> contasAReceber  = getContasAReceber(cdDocumentoSaida);
				ArrayList<Integer> contasAPagar  = getContasAPagar(cdDocumentoEntrada);
	//			
	//			//Valor total dos titulos
	//			float valorTotalTit = getValorTotalTitulos(contasAReceber);
	//			
	//			//Tipo de Documento
	//			TipoDocumento tipoDoc = getTipoDocumentoSAI(contasAReceber);
	//			
				int tpPagamento = -1;
				
				if(contasAReceber.size() == 1 && cdDocumentoSaida > 0)
					tpPagamento = IND_PAGTO_AVISTA;
				else
					tpPagamento = IND_PAGTO_APRAZO;
				
				if(contasAPagar.size() == 1 && cdDocumentoEntrada > 0)
					tpPagamento = IND_PAGTO_AVISTA;
				else
					tpPagamento = IND_PAGTO_APRAZO;
			
				//Valor Total do Itens
				float vlProd = NotaFiscalServices.getValorAllItens(nota.getCdNotaFiscal());
				
	//			//Valor das Aliquotas, Base e ICMS
	//			criterios = new ArrayList<ItemComparator>(); 
	//			criterios.add(new ItemComparator("CD_DOCUMENTO_SAIDA", Integer.toString(cdDocumentoSaida), ItemComparator.EQUAL, Types.INTEGER));
	//			ResultSetMap rsmSaidaAliquota = SaidaTributoDAO.find(criterios);
	//			float vlBaseICMS = 0;
	//			float vlICMS = 0;
	//			while(rsmSaidaAliquota.next()){
	//				vlBaseICMS += rsmSaidaAliquota.getFloat("vl_base_calculo"); 
	//				vlICMS += rsmSaidaAliquota.getFloat("vl_tributo"); 
	//			}
				
				
				
				//Natureza de Operacação do documento
	//			NaturezaOperacao natOp = NaturezaOperacaoDAO.get(nota.getCdNaturezaOperacao());
				
				//Resumo dos tributos do documento
	//			criterios = new ArrayList<ItemComparator>(); 
	//			criterios.add(new ItemComparator("CD_DOCUMENTO_SAIDA", Integer.toString(cdDocumentoSaida), ItemComparator.EQUAL, Types.INTEGER));
	//			ResultSetMap rsmSaidaTrib = SaidaTributoDAO.find(criterios);
	//			SaidaTributo saidaTrib = null;
	//			if(rsmSaidaTrib.next()){
	//				saidaTrib = SaidaTributoDAO.get(cdDocumentoSaida, rsmSaidaTrib.getInt("cd_tributo"));
	//			}
				
				/************************************** VALIDACAO *******************************************************/
				//Codigo do cliente
				if(String.valueOf(nota.getCdDestinatario())==null || String.valueOf(nota.getCdDestinatario()).trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Cliente ";
				//Numero do Documento
				if(nota.getNrNotaFiscal() ==null || nota.getNrNotaFiscal().trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Numero do documento ";
				//Chave da Nota Fiscal Eletronica - APRESENTAR
				if(nota.getNrChaveAcesso() == null || nota.getNrChaveAcesso().trim().equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Chave de Acesso da NFe ";
						
				//Data de Emissao do Documento
				if(nota.getDtEmissao() == null || String.valueOf(nota.getDtEmissao()).trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Data de emissão ";
				
				//Valor Total do Documento
				if(String.valueOf(nota.getVlTotalNota()) == null || String.valueOf(nota.getVlTotalNota()).trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor total ";
				
			    if(!dsValidacao.equals("")){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados da nota fiscal eletronica Nº " + nota.getNrNotaFiscal() + " faltando: "+dsValidacao);
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	//entrouDs = true;
			    }
			    
			    /*Validacao*/
			    boolean duplicidade = false;
			    String registroAtual = IND_OPER_SAIDA + nrDocumento + "55" + stDocumentoFiscal + nota.getNrSerie() + nota.getNrChaveAcesso().substring(3);
			    for(int ind = 0; ind < registroNota.size(); ind++){
			    	if(registroAtual.equals(registroNota.get(ind))){
			    		duplicidade = true;
			    		break;
			    	}
			    }
			    
			    if(duplicidade){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados da Nota Fiscal Eletrônica " + nota.getNrNotaFiscal() + ": Duplicidade com outra nota.");
			    	rsm.addRegister(register);
			    	//entrouDs = true;
			    }
			    else{
			    	registroNota.add(registroAtual);
			    }
			    
			    
			    GregorianCalendar data = new GregorianCalendar();
			    data.set(Calendar.YEAR, 2000);
			    data.set(Calendar.MONTH, 0);
			    data.set(Calendar.DAY_OF_MONTH, 1);
			    if(nota.getDtAutorizacao() == null || nota.getDtAutorizacao().before(data)){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados da Nota Fiscal Eletrônica " + nota.getNrNotaFiscal() + ": Data da nota fiscal eletrônica não pode ser anterior a 01/01/2000");
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	
			    	//entrouDs = true;
			    }
			    
			    if(nota.getDtAutorizacao() == null || nota.getDtEmissao() == null || nota.getDtAutorizacao().before(nota.getDtEmissao())){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados da Nota Fiscal Eletrônica " + nota.getNrNotaFiscal() + ": Data da nota fiscal eletrônica não pode ser anterior a Data de Emissão");
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	
			    	//entrouDs = true;
			    }
			    
			    if(nota.getDtAutorizacao() == null || nota.getDtEmissao() == null || dtSpedFinal.before(nota.getDtAutorizacao()) || dtSpedFinal.before(nota.getDtEmissao())){
					HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados da Nota Fiscal Eletrônica " + nota.getNrNotaFiscal() + ": Data menor do que a data final de emissão do SPED");
			    	rsm.addRegister(register);
			    
			    	//entrouDs = true;
			     }
			    
			    float vlBaseICMS = 0;
				float vlICMS = 0;
				float vlBaseICMSSub = 0;
				float vlICMSSub = 0;
				
				float vlBaseIPI = 0;
				float vlIPI = 0;
				float vlBaseIPISub = 0;
				float vlIPISub = 0;
				
				float vlBasePIS = 0;
				float vlPIS = 0;
				float vlBasePISSub = 0;
				float vlPISSub = 0;
				
				float vlBaseCOFINS = 0;
				float vlCOFINS = 0;
				float vlBaseCOFINSSub = 0;
				float vlCOFINSSub = 0;
			    
				int erroAliq = 0;
				
	//			ResultSetMap com os itens do documento
				ResultSetMap rsmProdutos = NotaFiscalServices.getAllItens(cdNotaFiscal);
	
			    if(rsmProdutos.next()){
					vlICMS  	  = rsmProdutos.getFloat("vl_icms_documento");
					vlBaseICMS 	  = rsmProdutos.getFloat("vl_base_icms_documento");
					vlBaseICMSSub = rsmProdutos.getFloat("vl_base_icms_substituto_documento");
					vlICMSSub 	  = rsmProdutos.getFloat("vl_icms_substituto_documento");
					
					vlBaseIPI 	  = rsmProdutos.getFloat("vl_base_ipi_documento");
					vlIPI 		  = rsmProdutos.getFloat("vl_ipi_documento");
					vlBaseIPISub  = rsmProdutos.getFloat("vl_base_ipi_substituto_documento");
					vlIPISub 	  = rsmProdutos.getFloat("vl_ipi_substituto_documento");
					
					vlBasePIS 	  = rsmProdutos.getFloat("vl_base_pis_documento");
					vlPIS 		  = rsmProdutos.getFloat("vl_pis_documento");
					vlBasePISSub  = rsmProdutos.getFloat("vl_base_pis_substituto_documento");
					vlPISSub 	  = rsmProdutos.getFloat("vl_pis_substituto_documento");
					
					vlBaseCOFINS 	  = rsmProdutos.getFloat("vl_base_cofins_documento");
					vlCOFINS 		  = rsmProdutos.getFloat("vl_cofins_documento");
					vlBaseCOFINSSub  = rsmProdutos.getFloat("vl_base_cofins_substituto_documento");
					vlCOFINSSub 	  = rsmProdutos.getFloat("vl_cofins_substituto_documento");
					
					erroAliq = rsmProdutos.getInt("ErroAliq");
				}
				else{
					HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Nota Fiscal Eletrônica Nº "+nota.getNrNotaFiscal()+" sem produtos!");
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	
			    	continue;
				}
			    
			    if(erroAliq > 0){
			    	String imposto = "";
			    	switch(erroAliq){
			    		case 1:
			    			imposto = "ICMS";
			    			break;
			    		case 2:
			    			imposto = "IPI";
			    			break;
			    		case 3:
			    			imposto = "II";
			    			break;
			    		case 4:
			    			imposto = "PIS";
			    			break;
			    		case 5:
			    			imposto = "COFINS";
			    			break;
			    	}
			    	
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados da nota fiscal eletrônica Nº " + nota.getNrNotaFiscal()+ ": Problema nas aliquotas do imposto " + imposto);
			    	rsm.addRegister(register);
			    
			    	//entrouDs = true;
			    }
			    
			    
			    
			    if(!entrouDs && !entrouDs2){
					registro += "|C100" + //REG
							  "|" +(cdDocumentoSaida > 0 ? IND_OPER_SAIDA : IND_OPER_ENTRADA)+
							  "|" +IND_EMIT_PROPRIA+
							  "|" +nota.getCdDestinatario()+                                                                 //Cód participante
							  "|" +"55"+	                                                        //Cód modelo do documento fiscal            - VERIFICAR PARA QUANDO NAO FOR 1A NEM Nfe
							  "|" +stDocumentoFiscal+										                                                                    //Cód da situação do documento fiscal       - Tabela 4.1.2
							  "|" +nota.getNrSerie()+	                                                                    //Serie do documento fiscal                 - OC
							  "|" +nrDocumento+	                                                        //Numero do documento fiscal            
							  "|" +nota.getNrChaveAcesso().substring(3)+                                    //Chave da nota fiscal online
							  "|" +Util.formatDateTime(nota.getDtEmissao(), "ddMMyyyy")+			                                                        //Data da emissão
							  "|" +Util.formatDateTime(nota.getDtAutorizacao(), "ddMMyyyy")+			                                                    //Data de entrada ou saida
							  "|" +Util.formatNumber(vlTotalDocumento, "#.##")+                                    //Valor total do documento fiscal           
							  "|" +((tpPagamento == 1) ? IND_PAGTO_APRAZO : (tpPagamento == 0) ? IND_PAGTO_AVISTA : "")+		//Indicador do tipo de pagamento
							  "|" +(vlDescontos.equals("-0") ? "0" : vlDescontos)+		         							//Valor total do desconto
							  "|" +	//Valor abatimento fiscal                    - VERIFICAR
							  "|" +Util.formatNumber(vlProd, "#.##")+										                    //Valor total da mercadoria e serviços
							  "|" +IND_FRT_SEMFRETE+                                                                          //Indicador do frete
							  "|" +Util.formatNumber(nota.getVlFrete(), "#.##")+									                            //Valor do frete                             - No Momento SEM FRETE como padrão
							  "|" +Util.formatNumber(nota.getVlSeguro(), "#.##")+									                            //Valor do seguro do frete
							  "|" +Util.formatNumber(nota.getVlOutrasDespesas(), "#.##")+				   									                                                //Valor de outras despesas                   - SEM VALOR DE OUTRAS DESPESAS  
							  "|" +Util.formatNumber(vlBaseICMS, "#.##")+	                                    //Valor da base de calculo do icms 														-OC
							  "|" +Util.formatNumber(vlICMS, "#.##")+	                                    //Valor do icms 																		-OC
							  "|" +Util.formatNumber(vlBaseICMSSub, "#.##")+									    //Valor da base de calculo do icms substituicao tributaria                                                       - VERIFICAR SE EH PARA USAR 						-OC
							  "|" +Util.formatNumber(vlICMSSub, "#.##")+									    //Valor do ICMS retido por substituição tributaria                                                               - VERIFICAR SE EH PARA USAR 						-OC
							  "|" +Util.formatNumber(0, "#.##")+	                                    //Valor total do IPI 																			-OC
							  "|" +Util.formatNumber(vlPIS, "#.##")+									                                                                        //Valor total do PIS 																			-OC
							  "|" +Util.formatNumber(vlCOFINS, "#.##")+									                                                                        //Valor total da confins 																		-OC
							  "|" +Util.formatNumber(vlPISSub, "#.##")+									                                                                        //Valor total do pis retido por substituicao tributaria 										-OC
							  "|" +Util.formatNumber(vlCOFINSSub, "#.##")+									                                                                        //Valor total da CONFINS retido por substituicao tributaria 									-OC					                                                                        //Valor total da CONFINS retido por substituicao tributaria
							  "|" +"\r\n";
					
					nrLinhaBlocoC++;
					conjuntoRegistro.put("C100", (((Integer) conjuntoRegistro.get("C100")) + 1));
			    }
			    
			    //entrouDs = false;
				
				
				/*
				 * C110 - Informação complementar da nota fiscal - Referente a C100
				 */
			    boolean hasC110 = false;
			    if(nota.getTxtObservacao() != null && !nota.getTxtObservacao().equals("")){
			    	String txtComplementar = NfeServices.removeAcentos(nota.getTxtObservacao().trim());
			    	txtComplementar = (txtComplementar.trim().length() > 255 ? txtComplementar.trim().substring(0, 255) : txtComplementar.trim());
					registro += "|C110" + //REG
									  "|" +nota.getCdNotaFiscal()+ //Código da informação complementar - Codigo deve ser o mesmo do registro 0450
									  "|" +txtComplementar.trim()+ //Descrição complementar do código de referência 
									  "|" +"\r\n";
						
					nrLinhaBlocoC++;
					conjuntoRegistro.put("C110", (((Integer) conjuntoRegistro.get("C110")) + 1));
					
					hasC110 = true;
			    }
	//					/*
	//					 * C111 - Processo referenciado. - Referente a C110  - VERIFICAR COMO ESSE REGISTRO FUNCIONA - deverá ser retirado ????
	//					 */
	//					String C111 = "C111" + //REG
	//										  Util.fillAlpha("",15)+	//Numero do processo
	//										  Util.fillAlpha(String.valueOf(IND_PROC_JE),1)+
	//										  "\r\n";
	//					nrLinhaBlocoC++;
				
				
	//					/*
	//					 * C112 - Documento de arrecadação referenciado. - Refernte a C110  - VERIFICAR COMO ESSE REGISTRO FUNCIONA  - deverá ser retirado ????
	//					 */
	//					String C112 = "C112" + //REG
	//										  Util.fillAlpha(String.valueOf(COD_DA_DOC_ESTADUAL),1)+	//Numero do processo
	//										  Util.fillAlpha("",2)+	//Unidade federada beneficiária do recolhimento
	//										  Util.fillAlpha("",10)+ //Numero do documento de arrecadação
	//										  Util.fillAlpha("",10)+ //Código completo da autenticação bancária.
	//										  Util.fillNum(0,10)+  //Valor do total do documento de arrecadação
	//										  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+			//Data de vencimento
	//										  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+			//Data de pagamento
	//										  "\r\n";
	//					nrLinhaBlocoC++;
				
				ArrayList<Integer> contasAReceberNota  = getAllContasAReceber(cdNotaFiscal);
				ArrayList<Integer> contasAPagarNota    = getAllContasAPagar(cdNotaFiscal);
				
				ArrayList<DocumentoSaida> docSaidaReferenciado = NotaFiscalServices.getAllDocSaidaReferenciado(cdNotaFiscal);
				for(int in = 0; hasC110 && in < docSaidaReferenciado.size(); in++){
				
					DocumentoSaida docReferenciadoSaida = docSaidaReferenciado.get(in);
					
					
					if(docReferenciadoSaida != null && docReferenciadoSaida.getTpDocumentoSaida() != DocumentoSaidaServices.TP_CUPOM_FISCAL  && docReferenciadoSaida.getTpDocumentoSaida() != DocumentoSaidaServices.TP_DOC_NAO_FISCAL){
					
							/*
							 * C113 - Documento Fiscal Referenciado - Refernte a C110 - VERIFICAR COMO ESSE REGISTRO FUNCIONA   - APRESENTAR
					
								APENAS NFE e Documentos de entrada de devolução
					
					
							 */
//						registro += "|C113"+
//											  "|" +IND_OPER_SAIDA+ //Indicador do tipo de operacao
//											  "|" +IND_EMIT_PROPRIA+ //Indicador do emitente do titulo
//											  "|" +nota.getCdDestinatario()+ //Código do participante emitente
//											  "|" +"01"+ //Código da modalidade do documento fiscal
//											  "|" +"001"+ //Serie do documento fiscal
//											  "|" +  //SubSerie do documento fiscal
//											  "|" +Util.limparFormatos((docReferenciadoSaida.getNrDocumentoSaida().length() > 9 ? docReferenciadoSaida.getNrDocumentoSaida().substring(0, 9) : docReferenciadoSaida.getNrDocumentoSaida()), 'N')+  //Numero do documento fiscal
//											  "|" +Util.formatDateTime(docReferenciadoSaida.getDtEmissao(), "ddMMyyyy")+ //Data da emissão do documento fiscal
//											  "|" +"\r\n";
//						nrLinhaBlocoC++;
//						conjuntoRegistro.put("C113", (((Integer) conjuntoRegistro.get("C113")) + 1));
						
					}
					
					if(docReferenciadoSaida != null && docReferenciadoSaida.getTpDocumentoSaida() == DocumentoSaidaServices.TP_CUPOM_FISCAL){
							/*
							 * C114 - Cupom fiscal referenciado - Refernte a C110 - VERIFICAR COMO ESSE REGISTRO FUNCIONA    - APRESENTAR
							 * 
							 * //				APENAS NFE e Documentos de entrada de devolução
							 * 
							 * 
							 */
//							Referencia referenciaEcf = ReferenciaDAO.get(docReferenciadoSaida.getCdReferenciaEcf());
//							ContaFinanceira conta    = ContaFinanceiraDAO.get(docReferenciadoSaida.getCdConta());
//							String nrCaixaSped       = ContaFinanceiraServices.getNrCaixaSped(conta.getNrConta());
//							
//							registro += "|C114"+
//									 			"|" +"02"+ //Código do modelo do documento fiscal
//												"|" +referenciaEcf.getNrSerie()+ //Número de serie de fabricação do ECF
//												"|" +nrCaixaSped+ //Número do caixa atribuido ao ECF 
//												"|" +(docReferenciadoSaida.getNrDocumentoSaida().length() > 9 ? docReferenciadoSaida.getNrDocumentoSaida().substring(docReferenciadoSaida.getNrDocumentoSaida().length()-9) : docReferenciadoSaida.getNrDocumentoSaida())+ //Número do documento fiscal
//												"|" +(docReferenciadoSaida.getDtEmissao() != null ? Util.formatDateTime(docReferenciadoSaida.getDtEmissao(), "ddMMyyyy") : "")+ //Data da emissão do documento fiscal
//												 "|" + "\r\n";
//							nrLinhaBlocoC++;
//							conjuntoRegistro.put("C114", (((Integer) conjuntoRegistro.get("C114")) + 1));
							
					}	
				
				}
							
				ArrayList<DocumentoEntrada> docEntradaReferenciado = NotaFiscalServices.getAllDocEntradaReferenciado(cdNotaFiscal);
				
				for(int in = 0; hasC110 && in < docEntradaReferenciado.size(); in++){
				
					DocumentoEntrada docReferenciadoEntrada = docEntradaReferenciado.get(in);
					
					
				
					if(docReferenciadoEntrada != null && docReferenciadoEntrada.getTpDocumentoEntrada() != DocumentoEntradaServices.TP_CUPOM_FISCAL  && docReferenciadoEntrada.getTpDocumentoEntrada() != DocumentoEntradaServices.TP_DOC_NAO_FISCAL){
						
						/*
						 * C113 - Documento Fiscal Referenciado - Refernte a C110 - VERIFICAR COMO ESSE REGISTRO FUNCIONA   - APRESENTAR
				
							APENAS NFE e Documentos de entrada de devolução
				
				
						 */
//					registro += "|C113"+
//										  "|" +IND_OPER_ENTRADA+ //Indicador do tipo de operacao
//										  "|" +IND_EMIT_PROPRIA+ //Indicador do emitente do titulo
//										  "|" +nota.getCdDestinatario()+ //Código do participante emitente
//										  "|" +"01"+ //Código da modalidade do documento fiscal
//										  "|" +"001"+ //Serie do documento fiscal
//										  "|" +  //SubSerie do documento fiscal
//										  "|" +(docReferenciadoEntrada.getNrDocumentoEntrada().length() > 9 ? docReferenciadoEntrada.getNrDocumentoEntrada().substring(docReferenciadoEntrada.getNrDocumentoEntrada().length()-9) : docReferenciadoEntrada.getNrDocumentoEntrada())+  //Numero do documento fiscal
//										  "|" +(docReferenciadoEntrada.getDtEmissao() != null ? Util.formatDateTime(docReferenciadoEntrada.getDtEmissao(), "ddMMyyyy") : "")+ //Data da emissão do documento fiscal
//										  "|" +"\r\n";
//					nrLinhaBlocoC++;
//					conjuntoRegistro.put("C113", (((Integer) conjuntoRegistro.get("C113")) + 1));
				}
			}
							
				
	//					/*
	//					 * C115 - Local da coleta e/ou entrega - Refernte a C110 - VERIFICAR - deverá ser retirado ???? 
	//					 */
	//					String C115 = "C115"+
	//										  Util.fillNum(IND_CARGA_AEREO, 1)+ //Indicador do tipo de transporte
	//										  Util.fillNum(0, 14)+ //Número do CNPJ do contribuinte do local de coleta
	//							  			  Util.fillAlpha("", 14)+ //Inscrição estadual do contribuinte do local da coleta
	//							  			  Util.fillNum(0, 11)+ //Número do CPF do contribuinte do local de coleta
	//							  			  Util.fillNum(0, 7)+ //Código do municipio do local de coleta
	//							  			  Util.fillNum(0, 14)+ //Número do CNPJ do contribuinte do local de entrega
	//							  			  Util.fillAlpha("", 14)+ //Inscrição estadual do contribuinte do local da entrega
	//							  			  Util.fillNum(0, 11)+ //Número do CPF do contribuinte do local de entrega
	//							  			  Util.fillNum(0, 7)+ //Código do municipio do local de entrega
	//							  			  "\r\n";
	//					nrLinhaBlocoC++;
				
				/*
				 * C116 - Cupom fiscal eletrônico referenciado - Refernte a C110 - VERIFICAR   - APRESENTAR
				 * 
				 * APENAS NFE e Documentos de entrada de devolução
				 * 
				 * 
				 */
	//					registro += "C116"+
	//										  Util.fillAlpha("", 2)+//Código do modelo do documento fiscal, conforme a Tabela 4.1.1
	//										  Util.fillNum(0, 9)+ //Número de Série do equipamento SAT
	//										  Util.fillNum(0, 44)+ //Chave do Cupom Fiscal Eletrônico
	//										  Util.fillNum(0, 6)+ //Número do cupom fiscal eletrônico
	//										  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+ //Data da emissão do documento fiscal
	//										  "\r\n";
	//					nrLinhaBlocoC++;
				
				
				/*
				 * Registro C140 - Refernte a C100 - FATURA
				 */
	//			if(contasAReceberNota.size() > 1){
	//			
	//				/************************************** VALIDACAO *******************************************************/
	//				TipoDocumento tipoDoc = getTipoDocumentoSAI(contasAReceberNota);
	//			    
	//				float valorTotalTit = 0;
	//				for(int j = 0; j < contasAReceberNota.size(); j++){
	//					valorTotalTit += ContaReceberDAO.get(contasAReceberNota.get(j)).getVlConta();
	//				}
	//				
	//			    registro += "|C140"+
	//						              "|" +IND_EMIT_PROPRIA+
	//									  "|" +((tipoDoc == null) ? IND_TIT_OUTROS : tipoDoc.getIdTipoDocumento())+
	//									  "|" +//Descrição complementar do título de crédito - OC
	//									  "|" +nota.getCdNotaFiscal()+//Número ou código identificador dos títulos de crédito - CRIAR CODIGO PARA TITULO DE CREDITO
	//									  "|" +contasAReceberNota.size()+ //Quantidade de parcelas a receber/pagar
	//									  "|" +Util.formatNumber(valorTotalTit, "#.##")+ //Valor total dos títulos de créditos
	//									  "|" +"\r\n";
	//				nrLinhaBlocoC++;
	//				conjuntoRegistro.put("C140", (((Integer) conjuntoRegistro.get("C140")) + 1));
	//				
	//				
	//				for(int k = 0; k < contasAReceberNota.size(); k++){
	//					/*
	//					 * Registro C141 - Refernte a C140 - Vencimento de fatura
	//					 */
	//		    		ContaReceber conta = ContaReceberDAO.get(contasAReceberNota.get(k));
	//		    		/************************************** VALIDACAO *******************************************************/
	//					//Data de Vencimento
	//					if(conta.getDtVencimento() == null)
	//				    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Data de Vencimento da conta a receber ";
	//					//Valor do titulo de credito
	//					if(String.valueOf(conta.getVlConta()) == null || String.valueOf(conta.getVlConta()).trim().equals(""))
	//				    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor da conta a receber ";
	//				    if(!dsValidacao.equals("")){
	//				    	HashMap<String, Object> register = new HashMap<String, Object>();
	//				    	register.put("ERRO", "Dados da conta a receber Nº "+conta.getNrDocumento()+" faltando: "+dsValidacao);
	//				    	rsm.addRegister(register);
	//				    
	//				    	dsValidacao = "";
	//				    	//entrouDs = true;
	//				    }
	//				    if(!entrouDs && !entrouDs2){
	//					    registro += "|C141"+
	//											  "|" +(k + 1)+ //Número da parcela a receber/pagar
	//											  "|" +Util.formatDateTime(conta.getDtVencimento(), "ddMMyyyy")+ //Data de vencimento da parcela
	//											  "|" +Util.formatNumber(conta.getVlConta(), "#.##")+ //Valor da parcela a receber/pagar
	//											  "|" +"\r\n";
	//						nrLinhaBlocoC++;
	//						conjuntoRegistro.put("C141", (((Integer) conjuntoRegistro.get("C141")) + 1));
	//				    }
	//				    //entrouDs = false;
	//				}
	//				
	//			}
				
	//			if(contasAPagarNota.size() > 1){
	//				
	//				/************************************** VALIDACAO *******************************************************/
	//				TipoDocumento tipoDoc = getTipoDocumentoENT(contasAPagarNota);
	//			    
	//				float valorTotalTit = 0;
	//				for(int j = 0; j < contasAPagarNota.size(); j++){
	//					valorTotalTit += ContaPagarDAO.get(contasAPagarNota.get(j)).getVlConta();
	//				}
	//				
	//			    registro += "|C140"+
	//						              "|" +IND_EMIT_PROPRIA+
	//									  "|" +((tipoDoc == null) ? IND_TIT_OUTROS : tipoDoc.getIdTipoDocumento())+
	//									  "|" +//Descrição complementar do título de crédito - OC
	//									  "|" +nota.getCdNotaFiscal()+//Número ou código identificador dos títulos de crédito - CRIAR CODIGO PARA TITULO DE CREDITO
	//									  "|" +contasAPagarNota.size()+ //Quantidade de parcelas a receber/pagar
	//									  "|" +Util.formatNumber(valorTotalTit, "#.##")+ //Valor total dos títulos de créditos
	//									  "|" +"\r\n";
	//				nrLinhaBlocoC++;
	//				conjuntoRegistro.put("C140", (((Integer) conjuntoRegistro.get("C140")) + 1));
	//				
	//				
	//				for(int k = 0; k < contasAPagarNota.size(); k++){
	//					/*
	//					 * Registro C141 - Refernte a C140 - Vencimento de fatura
	//					 */
	//		    		ContaPagar conta = ContaPagarDAO.get(contasAPagarNota.get(k));
	//		    		/************************************** VALIDACAO *******************************************************/
	//					//Data de Vencimento
	//					if(conta.getDtVencimento() == null)
	//				    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Data de Vencimento da conta a receber ";
	//					//Valor do titulo de credito
	//					if(String.valueOf(conta.getVlConta()) == null || String.valueOf(conta.getVlConta()).trim().equals(""))
	//				    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor da conta a receber ";
	//				    if(!dsValidacao.equals("")){
	//				    	HashMap<String, Object> register = new HashMap<String, Object>();
	//				    	register.put("ERRO", "Dados da conta a receber Nº "+conta.getNrDocumento()+" faltando: "+dsValidacao);
	//				    	rsm.addRegister(register);
	//				    
	//				    	dsValidacao = "";
	//				    	//entrouDs = true;
	//				    }
	//				    if(!entrouDs && !entrouDs2){
	//					    registro += "|C141"+
	//											  "|" +(k + 1)+ //Número da parcela a receber/pagar
	//											  "|" +Util.formatDateTime(conta.getDtVencimento(), "ddMMyyyy")+ //Data de vencimento da parcela
	//											  "|" +Util.formatNumber(conta.getVlConta(), "#.##")+ //Valor da parcela a receber/pagar
	//											  "|" +"\r\n";
	//						nrLinhaBlocoC++;
	//						conjuntoRegistro.put("C141", (((Integer) conjuntoRegistro.get("C141")) + 1));
	//				    }
	//				    //entrouDs = false;
	//				}
	//				
	//			}
				
				
				
				
				float valorReduzidoTotal = 0;
				float valorTotalItens = 0;
				float valorBaseTotal = 0;
				
	
				
	//			do{
	//				
	//				/*
	//				 * Registro C170 - Refernte a C100 - Itens do documento
	//				 */
	//				/************************************** VALIDACAO *******************************************************/
	//				//Quantidade do Item
	//				if(String.valueOf(rsmProdutos.getFloat("qt_tributario")) == null || String.valueOf(rsmProdutos.getFloat("qt_tributario")).trim().equals(""))
	//			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Quantidade do item: " + rsmProdutos.getString("nm_produto_servico");
	//				
	//				//Unidade de Medida
	//				if(rsmProdutos.getString("nm_unidade_medida") == null || rsmProdutos.getString("nm_unidade_medida").trim().equals(""))
	//			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Unidade de medida do item: " + rsmProdutos.getString("nm_produto_servico") + "(" + rsmProdutos.getString("id_produto_servico") + ")" + " - Nota Fiscal Eletronica Nº: " + nota.getNrNotaFiscal();
	//				//Valor Unitario
	//				if(String.valueOf(rsmProdutos.getFloat("vl_unitario")) == null || String.valueOf(rsmProdutos.getFloat("vl_unitario")).trim().equals(""))
	//			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor unitario do item: " + rsmProdutos.getString("nm_produto_servico") + " - Nota Fiscal Eletronica Nº: " + nota.getNrNotaFiscal();
	//				//CFOP
	//				if(rsmProdutos.getString("nr_codigo_fiscal") == null || rsmProdutos.getString("nr_codigo_fiscal").trim().equals(""))
	//			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CFOP do item: " + rsmProdutos.getString("nm_produto_servico") + " - Nota Fiscal Eletronica Nº: " + nota.getNrNotaFiscal();
	//			    if(!dsValidacao.equals(""))
	//			    	return new Result(-1, "Dados dos Itens faltando: "+dsValidacao);
	//				
	//			    valorTotalItens += (rsmProdutos.getFloat("qt_tributario") * rsmProdutos.getFloat("vl_unitario"));
	//			    valorBaseTotal  += rsmProdutos.getFloat("vl_base_calculo");
	//			    
	//			    
	//			    
	//			    vlICMS  	  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? 0 : rsmProdutos.getFloat("vl_icms"));
	//				vlBaseICMS 	  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? 0 : rsmProdutos.getFloat("vl_base_icms"));
	//				vlBaseICMSSub = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? rsmProdutos.getFloat("vl_base_icms") : 0);
	//				vlICMSSub 	  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? rsmProdutos.getFloat("vl_icms") : 0);
	//				
	//				vlBaseIPI 	  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? 0 : rsmProdutos.getFloat("vl_base_ipi"));
	//				vlIPI 		  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? 0 : rsmProdutos.getFloat("vl_ipi"));
	//				vlBaseIPISub  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? rsmProdutos.getFloat("vl_base_ipi") : 0);
	//				vlIPISub 	  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? rsmProdutos.getFloat("vl_ipi") : 0);
	//				
	//				vlBasePIS 	  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? 0 : rsmProdutos.getFloat("vl_base_pis"));
	//				vlPIS 		  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? 0 : rsmProdutos.getFloat("vl_pis"));
	//				vlBasePISSub  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? rsmProdutos.getFloat("vl_base_pis") : 0);
	//				vlPISSub 	  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? rsmProdutos.getFloat("vl_pis") : 0);
	//				
	//				vlBaseCOFINS 	  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? 0 : rsmProdutos.getFloat("vl_base_cofins"));
	//				vlCOFINS 		  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? 0 : rsmProdutos.getFloat("vl_cofins"));
	//				vlBaseCOFINSSub  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? rsmProdutos.getFloat("vl_base_cofins") : 0);
	//				vlCOFINSSub 	  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? rsmProdutos.getFloat("vl_cofins") : 0);
	//				
	//			    float prAliquotaICMSSub = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? rsmProdutos.getFloat("pr_icms") : 0);
	//			    float prAliquotaIPI 	= rsmProdutos.getFloat("pr_ipi");
	//			    float prAliquotaPIS 	= rsmProdutos.getFloat("pr_pis");
	//			    float prAliquotaCOFINS  = rsmProdutos.getFloat("pr_cofins");
	//				
	////			    vlDescontoItens += rsmProdutos.getFloat("vl_desconto");
	//			    
	//			    String cst = ((vlBaseICMS > 0 || vlBaseICMSSub > 0) && rsmProdutos.getString("nr_situacao_tributaria") != null && !rsmProdutos.getString("nr_situacao_tributaria").equals("") ? rsmProdutos.getString("nr_situacao_tributaria") : "041");
	//			    
	//			    String parteCst = cst.substring(1);
	//			    
	//			    if((parteCst.equals("00") || 
	//			    	parteCst.equals("10") || 
	//			    	parteCst.equals("20") || 
	//			    	parteCst.equals("70")) &&
	//			    	rsmProdutos.getFloat("pr_icms") == 0){
	//			    	
	//			    	HashMap<String, Object> register = new HashMap<String, Object>();
	//			    	register.put("AVISO", "Dados dos Itens do nota fiscal eletrônica Nº "+nota.getNrNotaFiscal()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : Se o CST termina em 00, 10, 20 ou 70 a Aliquota deve ser maior do que zero.");
	//			    	rsm2.addRegister(register);
	//			    
	//			    }
	//			    
	//				registro += "|C170"+
	//									  "|" +rsmProdutos.getInt("cd_item")+//Número sequencial do item no documento fiscal
	//									  "|" +rsmProdutos.getInt("cd_produto_servico")+//Código do item                                                                                              - Codigo proprio do EFD - Implementar
	//									  "|" +((rsmProdutos.getString("txt_produto_servico") != null) ? rsmProdutos.getString("txt_produto_servico").trim() : "")+//Descrição complementar do item como adotado no documento fiscal -OC
	//									  "|" +Util.formatNumber(rsmProdutos.getFloat("qt_tributario"), "#.##")+//Quantidade do item
	//									  "|" +((rsmProdutos.getString("cd_unidade_medida").trim().length() > 6 ) ? rsmProdutos.getString("cd_unidade_medida").trim().substring(0, 6) : rsmProdutos.getString("cd_unidade_medida").trim())+//Unidade do item
	//									  "|" +Util.formatNumber((rsmProdutos.getFloat("qt_tributario") * rsmProdutos.getFloat("vl_unitario") + rsmProdutos.getFloat("vl_acrescimo") - rsmProdutos.getFloat("vl_desconto")), "#.##")+//Valor total do item (mercadorias ou serviços)
	//									  "|" +((rsmProdutos.getFloat("vl_desconto") != 0) ? Util.formatNumber(rsmProdutos.getFloat("vl_desconto"), "#.##") : "0,00")+//Valor do desconto comercial 																					-OC
	//									  "|" +IND_MOV_SIM+//Movimentação física do ITEM/PRODUTO
	//						              "|" +(cst.length() < 3 ? "0" + cst : cst)+//Código da Situação Tributária referente ao ICMS, conforme a Tabela indicada no item 4.3.1 http://www.cosif.com.br/mostra.asp?arquivo=sinief-CST-anexo
	//						              "|" +rsmProdutos.getString("nr_codigo_fiscal")+//Código Fiscal de Operação e Prestação
	//						              "|" +//Código da natureza da operação                                                                                - Codigo do proprio EFD - IMplementar Campo 2 do Registro 0400
	//						              "|" +Util.formatNumber(vlBaseICMS, "#.##")+//Valor da base de cálculo do ICMS 																		-OC
	//						              "|" +((rsmProdutos.getFloat("pr_icms") != 0) ? Util.formatNumber(rsmProdutos.getFloat("pr_icms"), "#.##") : "0,00" )+//Alíquota do ICMS 																															-OC
	//						              "|" +Util.formatNumber(vlICMS, "#.##") +//Valor do ICMS creditado/debitado 											-OC
	//						              "|" +Util.formatNumber(vlBaseICMSSub, "#.##") +//"0,00" +//Valor da base de cálculo referente à substituição tributária 																																										-OC
	//						              "|" +Util.formatNumber(prAliquotaICMSSub, "#.##") +//Alíquota do ICMS da substituição tributária na unidade da federação de destino 																																					-OC
	//						              "|" +Util.formatNumber(vlICMSSub, "#.##") + //"0,00" +//Valor do ICMS referente à substituição tributária 																																													-OC
	//						              "|" +//Movimentação física do ITEM/PRODUTO 																																																-OC
	//						              "|" +((vlBaseIPI > 0 || vlBaseIPISub > 0) && !rsmProdutos.getString("nr_situacao_tributaria").equals("") && rsmProdutos.getString("nr_situacao_tributaria") != null ? rsmProdutos.getString("nr_situacao_tributaria") : "") +//Código da Situação Tributária referente ao IPI 																								-OC
	//						              "|" +//Código de enquadramento legal do IPI 																																																-OC
	//						              "|" +Util.formatNumber(vlBaseIPI, "#.##") + //"0,00" +//Valor da base de cálculo do IPI 																																																	-OC
	//						              "|" +Util.formatNumber(prAliquotaIPI, "#.##") +//Alíquota do IPI 																																																					-OC
	//						              "|" +Util.formatNumber(vlPIS, "#.##") +//Valor do IPI creditado/debitado 																																																	-OC
	//						              "|" +((vlBasePIS > 0 || vlBasePISSub > 0) && !rsmProdutos.getString("nr_situacao_tributaria").equals("") && rsmProdutos.getString("nr_situacao_tributaria") != null ? rsmProdutos.getString("nr_situacao_tributaria") : "") +//Código da Situação Tributária referente ao PIS. 																																												-OC
	//						              "|" +Util.formatNumber(vlPIS, "#.##") + //"0,00" +//Valor da base de cálculo do PIS 																																																	-OC
	//						              "|" +Util.formatNumber(prAliquotaPIS, "#.####") +//Alíquota do PIS (em percentual) 																																																	-OC
	//						              "|" +((vlBasePIS > 0 || vlBasePISSub > 0) ? Util.formatNumber(rsmProdutos.getFloat("qt_tributario"), "#.###") : "0,000") + //"0,000" +//Quantidade  Base de cálculo PIS 																																																	-OC
	//						              "|" + //"0,0000" +//Alíquota do PIS (em reais) 																																																		-OC
	//						              "|" +Util.formatNumber(vlPIS, "#.##") +// "0,00" +//Valor do PIS 																																																						-OC
	//						              "|" +((vlBaseCOFINS > 0 || vlBaseCOFINSSub > 0) && !rsmProdutos.getString("nr_situacao_tributaria").equals("") && rsmProdutos.getString("nr_situacao_tributaria") != null ? rsmProdutos.getString("nr_situacao_tributaria") : "") +//Código da Situação Tributária referente ao COFINS. 																																											-OC
	//						              "|" +Util.formatNumber(vlBaseCOFINS, "#.###") + //"0,00" +//Valor da base de cálculo da COFINS 																																																-OC
	//						              "|" +Util.formatNumber(prAliquotaCOFINS, "#.####") +//Alíquota do COFINS (em percentual) 																																																-OC
	//						              "|" +((vlBaseCOFINS > 0 || vlBaseCOFINSSub > 0) ? Util.formatNumber(rsmProdutos.getFloat("qt_tributario"), "#.###") : "0,000") + //"0,000" +//Quantidade  Base de cálculo COFINS 																																																-OC
	//						              "|" + //"0,0000" +//Alíquota da COFINS (em reais) 																																																		-OC
	//						              "|" +Util.formatNumber(vlCOFINS, "#.####") + //"0,00" +//Valor da COFINS 																																																					-OC
	//						              "|" +//Código da conta analítica contábil debitada/creditada 																																												-OC			                                           	              
	//									  "|" +"\r\n";
	//				nrLinhaBlocoC++;
	//				conjuntoRegistro.put("C170", (((Integer) conjuntoRegistro.get("C170")) + 1));
	//				
	//
	//				
	////						if(false){//Teste para saber se utiliza o ISSQN
	////							/*
	////							 * Registro C172 - OPERAÇÕES COM ISSQN
	////							 */
	////							String C172 = "C172" +
	////												  Util.fillNum(0, 6)+//Valor da base de cálculo do ISSQN
	////												  Util.fillNum(0, 6)+//Alíquota do ISSQN
	////												  Util.fillNum(0, 6)+//Valor do ISSQN
	////												  "\r\n";
	////						}
	//				
	////						/*
	////						 * Registro C176 - Refernte a C170 - RESSARCIMENTO DE ICMS EM OPERAÇÕES COM SUBSTITUIÇÃO TRIBUTÁRIA                  - Ver como Implementar - Apenas em Saídas 
	////						 */
	////						registro += "|C176" +
	////											  "|" +Util.fillAlpha("", 2)+//Código do modelo do documento fiscal relativa a ultima entrada
	////											  "|" +Util.fillNum(0, 9)+//Número do documento fiscal relativa a última entrada
	////											  "|" +Util.fillAlpha("", 3)+//Série do documento fiscal relativa a última entrada
	////											  "|" +Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+ //Data relativa a última entrada da mercadoria
	////											  "|" +Util.fillAlpha("", 60)+//Código do participante (do emitente do documento relativa a última entrada)
	////											  "|" +Util.fillNum(0, 6)+//Quantidade do item relativa a última entrada
	////											  "|" +Util.fillNum(0, 6)+//Valor unitário da mercadoria constante na NF relativa a última entrada inclusive despesas acessórias.
	////											  "|" +Util.fillNum(0, 6)+//Valor unitário da base de cálculo do imposto pago por substituição.
	////											  "|" +"\r\n";
	////						nrLinhaBlocoC++;
	//				
	//				
	////						/*
	////						 * Registro C177 - OPERAÇÕES COM PRODUTOS SUJEITOS A SELO DE CONTROLE IPI.
	////						 */
	////						String C177 = "C177" +
	////											  Util.fillAlpha("", 6)+//Código do selo de controle do IPI,
	////											  Util.fillNum(0, 12)+//Quantidade de selo de controle do IPI aplicada
	////											  "\r\n";
	//				
	////						/*
	////						 * Registro C178 - OPERAÇÕES COM PRODUTOS SUJEITOS À TRIBUTAÇÀO DE IPI POR UNIDADE OU QUANTIDADE DE PRODUTO.
	////						 */
	////						String C178 = "C178" +
	////								 			  Util.fillAlpha("", 60)+//Código da classe de enquadramento do IPI
	////								 			  Util.fillNum(0, 6)+//Valor por unidade padrão de tributação
	////								 			  Util.fillNum(0, 6)+//Quantidade total de produtos na unidade padrão de tributação
	////											  "\r\n";
	//				
	////						/*
	////						 * Registro C179 - Refernte a C170 - INFORMAÇÕES COMPLEMENTARES                  - Ver se irá implementar
	////						 */
	////						registro += "C179" +
	////											  Util.fillNum(0, 6)+//Valor da base de cálculo ST na origem/destino em operações interestaduais.
	////											  Util.fillNum(0, 6)+//Valor do ICMS-ST a repassar/deduzir em operações interestaduais
	////								 			  Util.fillNum(0, 6)+//Valor do ICMS-ST a complementar à UF de destino
	////								 			  Util.fillNum(0, 6)+//Valor da BC de retenção em remessa promovida por Substituído intermediário
	////								 			  Util.fillNum(0, 6)+//Valor da parcela do imposto retido em remessa promovida por substituído intermediário
	////								 			  "\r\n";
	//						nrLinhaBlocoC++;
					
					
	//				
	//			}while(rsmProdutos.next());
			
			
//			valorReduzidoTotal = valorTotalItens - valorBaseTotal;
			/*
			 * Registro C190 - Refernte a C100 - REGISTRO ANALÍTICO DO DOCUMENTO
			 */
			
			ResultSetMap rsmTrib = getTributacao(cdNotaFiscal, 2);
			
			ResultSetMap rsmTribByEstado = getTributacaoByEstado(cdNotaFiscal, 2);
			/************************************** VALIDACAO *******************************************************/
			//Natureza de Operacao
			//CFOP
			
//			if(!rsmTrib.next())
//				return new Result(-1, "ERRO na tributação de um produto do Documento de Saída N." + documento.getNrDocumentoSaida());
			while(rsmTribByEstado.next()){
				String inicialCfop = rsmTribByEstado.getString("nr_codigo_fiscal").substring(0, 1);
				boolean hasEstado = false;
				while(rsmApuracaoIcms.next()){
					if(rsmApuracaoIcms.getRegister().get("SG_ESTADO").equals(rsmTribByEstado.getString("sg_estado"))){
						hasEstado = true;
						if(cdDocumentoSaida > 0){
							if(inicialCfop.equals("5") ||
					    	   inicialCfop.equals("6")){
								rsmApuracaoIcms.setValueToField("VL_ICMS_CREDITO_ST_RETENCAO",  (rsmApuracaoIcms.getFloat("VL_ICMS_CREDITO_ST_RETENCAO") + rsmTribByEstado.getFloat("vl_icms_substituto")));
							}
							if(inicialCfop.equals("5") ||
							   inicialCfop.equals("6") || 
							   inicialCfop.equals("7") ||
							   		rsmTribByEstado.getString("nr_codigo_fiscal").equals("1605")){
								rsmApuracaoIcms.setValueToField("VL_ICMS_DEBITO",  (rsmApuracaoIcms.getFloat("VL_ICMS_DEBITO") + rsmTribByEstado.getFloat("vl_icms")));
								rsmApuracaoIcms.setValueToField("VL_ICMS_DEBITO_ST_DEV_ANT",  (rsmApuracaoIcms.getFloat("VL_ICMS_DEBITO_ST_DEV_ANT") + rsmTribByEstado.getFloat("vl_icms_substituto")));
							}
						}
						else if(cdDocumentoEntrada > 0)
					    	if(inicialCfop.equals("1") &&
					    	   !rsmTribByEstado.getString("nr_codigo_fiscal").equals("1605")){
					    		rsmApuracaoIcms.setValueToField("VL_ICMS_CREDITO",  (rsmApuracaoIcms.getFloat("VL_ICMS_CREDITO") + rsmTribByEstado.getFloat("vl_icms")));
								rsmApuracaoIcms.setValueToField("VL_ICMS_CREDITO_ST",  (rsmApuracaoIcms.getFloat("VL_ICMS_CREDITO_ST") + rsmTribByEstado.getFloat("vl_icms_substituto")));
								rsmApuracaoIcms.setValueToField("VL_ICMS_CREDITO_ST_DEVOLUCAO",  (rsmApuracaoIcms.getFloat("VL_ICMS_CREDITO_ST_DEVOLUCAO") + rsmTribByEstado.getFloat("vl_icms_substituto")));
					    	}
						break;
					}
				}
				if(!hasEstado){
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("SG_ESTADO", rsmTribByEstado.getString("sg_estado"));
					if(cdDocumentoSaida > 0){
						if(inicialCfop.equals("5") ||
				    	   inicialCfop.equals("6")){
							register.put("VL_ICMS_CREDITO_ST_RETENCAO", rsmTribByEstado.getFloat("vl_icms_substituto"));
						}
						if(inicialCfop.equals("5") ||
						   inicialCfop.equals("6") || 
						   inicialCfop.equals("7") ||
						   		rsmTribByEstado.getString("nr_codigo_fiscal").equals("1605")){
							register.put("VL_ICMS_DEBITO", rsmTribByEstado.getFloat("vl_icms"));
							register.put("VL_ICMS_DEBITO_ST_DEV_ANT", rsmTribByEstado.getFloat("vl_icms_substituto"));
						}
					}
					
					else if(cdDocumentoEntrada > 0)
				    	if(inicialCfop.equals("1") &&
				    	   !rsmTribByEstado.getString("nr_codigo_fiscal").equals("1605")){
				    		register.put("VL_ICMS_CREDITO", rsmTribByEstado.getFloat("vl_icms"));
							register.put("VL_ICMS_CREDITO_ST", rsmTribByEstado.getFloat("vl_icms_substituto"));
							register.put("VL_ICMS_CREDITO_ST_DEVOLUCAO", rsmTribByEstado.getFloat("vl_icms_substituto"));
				    	}
					
					rsmApuracaoIcms.addRegister(register);
				}
				rsmApuracaoIcms.beforeFirst();
				
			}
			
			rsmApuracaoIcms.beforeFirst();
			
			
//			if(!rsmTrib.next())
//				return new Result(-1, "ERRO na tributação de um produto da Nota Fiscal Eletrônica N." + nota.getNrNotaFiscal());
			float vlTotalItens = 0;
			while(rsmTrib.next()){
				
				/************************************** VALIDACAO *******************************************************/
				//Natureza de Operacao
				//CFOP
				if(rsmTrib.getString("nr_codigo_fiscal") == null)
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CFOP ";
				if(!dsValidacao.equals("")){
					
					HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados Analíticos da nota Nº "+nota.getNrNotaFiscal()+": "+dsValidacao);
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	
			    	//entrouDs = true;
				}
				
				vlTotalItens += rsmTrib.getFloat("vl_itens");
				
				if(!entrouDs){
					String inicialCfop = rsmTrib.getString("nr_codigo_fiscal").substring(0, 1);
			    	if(cdDocumentoSaida > 0)
						if(inicialCfop.equals("5") ||
				    	   inicialCfop.equals("6")){
						}
			    		if(inicialCfop.equals("5") ||
						   inicialCfop.equals("6") || 
						   inicialCfop.equals("7") ||
								rsmTrib.getString("nr_codigo_fiscal").equals("1605")){
			    			vlIcmsDebito += rsmTrib.getFloat("vl_icms");
				    	}
					else if(cdDocumentoEntrada > 0)
						if(((inicialCfop.equals("1") || inicialCfop.equals("2") || inicialCfop.equals("3")) &&
						    	   (!rsmTrib.getString("nr_codigo_fiscal").equals("1605"))) || rsmTrib.getString("nr_codigo_fiscal").equals("5605")){
				    		vlIcmsCredito   += rsmTrib.getFloat("vl_icms");
				    	}
			    	
			    	String cst = rsmTrib.getString("nr_situacao_tributaria") != null && !rsmTrib.getString("nr_situacao_tributaria").equals("") ? rsmTrib.getString("nr_situacao_tributaria") : "041";
					cst = (cst.length() < 3 ? "0" + cst : cst);
								
					registro += "|C190" +
										  "|" +cst+//Código da Situação Tributária. - http://www.cosif.com.br/mostra.asp?arquivo=sinief-CST-anexo
										  "|" +rsmTrib.getString("nr_codigo_fiscal")+//Código Fiscal de Operação e Prestação do agrupamento de itens.
										  "|" +Util.formatNumber(rsmTrib.getFloat("pr_aliquota"), "#.##")+//Alíquota do ICMS
										  "|" +Util.formatNumber((Util.arredondar(rsmTrib.getFloat("vl_itens"), 2)), "#.##")+//Valor da operação na combinação de CST_ICMS, CFOP e alíquota do ICMS.
										  "|" +Util.formatNumber(rsmTrib.getFloat("vl_base_icms"), "#.##")+//Parcela correspondente ao "Valor da base de cálculo do ICMS" referente à combinação de CST_ICMS, CFOP e alíquota do ICMS. 			-OC
										  "|" +Util.formatNumber(rsmTrib.getFloat("vl_icms"), "#.##")+//Parcela correspondente ao "Valor do ICMS" referente à combinação de CST_ICMS, CFOP e alíquota do ICMS.
										  "|" +Util.formatNumber(rsmTrib.getFloat("vl_base_icms_substituto"), "#.##")+//Parcela correspondente ao "Valor da base de cálculo do ICMS" da substituição tributária referente à combinação de CST_ICMS, CFOP e alíquota do ICMS.
										  "|" +Util.formatNumber(rsmTrib.getFloat("vl_icms_substituto"), "#.##")+//Parcela correspondente ao valor creditado/debitado do ICMS da substituição tributária, referente à combinação de CST_ICMS, CFOP, e alíquota do ICMS.
										  "|" +"0,00"+//Valor não tributado em função da redução da base de cálculo do ICMS, referente à combinação de CST_ICMS, CFOP e alíquota do ICMS.
										  "|" +Util.formatNumber(rsmTrib.getFloat("vl_ipi"), "#.##")+//Parcela correspondente ao "Valor do IPI" referente à combinação CST_ICMS, CFOP e alíquota do ICMS.
		//								  "|" +Util.fillAlpha("", 6)+//Código da observação do lançamento fiscal   campo 2 do registro 0460        - Ver se na BA eh necessario preencher tal campo - PARA NFE POR ENQUANTO
										  "|" + //Codigo da obsercação do lançamento fiscal																																					-OC
										  "|" +"\r\n";
					nrLinhaBlocoC++;
					conjuntoRegistro.put("C190", (((Integer) conjuntoRegistro.get("C190")) + 1));
				}
				
				//entrouDs = false;
			}
			
//			if(!vlTotalDocumento.equals(Util.formatNumber(vlTotalItens, "#.##"))){
//				HashMap<String, Object> register = new HashMap<String, Object>();
//		    	register.put("AVISO", "Dados Analiticos do nota fiscal eletrônica Nº "+nota.getNrNotaFiscal()+": O valor total da nota fiscal deve ser igual a soma dos valores de operação VL_OPR do registro C190");
//		    	rsm2.addRegister(register);
//		    
//			}
			
//					/* POR ENQUANTO APENAS NFE TERA ESSE REGISTRO, DEPOIS ESTENDER PARA DOCUMENTOS DE ENTRADA/SAIDA
//					 * Registro C195 - Refernte a C100 - OBSERVAÇOES DO LANÇAMENTO FISCAL - Este registro deve ser informado quando, em decorrência da legislação estadual, houver ajustes nos documentos
//																		  fiscais, informações sobre diferencial de alíquota, antecipação de imposto e outras situações
//					 */
//					registro += "C195" +
//										  Util.fillAlpha("", 6)+//Código da observação do lançamento fiscal
//										  Util.fillAlpha("", 30)+//Descrição complementar do código de observação.
//										  "\r\n";
//					nrLinhaBlocoC++;
			
			
//					/*
//					 * Registro C197 - Refernte a C195 - OUTRAS OBRIGAÇÕES TRIBUTÁRIAS, AJUSTES E INFORMAÇÕES DE VALORES PROVENIENTES DE DOCUMENTO FISCAL    - Verificar se será utilizado
//					 */
//					String C197 = "C197"+
//										  Util.fillAlpha("", 10)+//Código da observação do lançamento fiscal
//										  Util.fillAlpha("", 6)+//Descrição complementar do ajuste do documento fiscal
//										  Util.fillAlpha("", 60)+//Código do item
//										  Util.fillNum(0, 6)+//Base de cálculo do ICMS ou do ICMS ST
//										  Util.fillNum(0, 6)+//Alíquota do ICMS
//										  Util.fillNum(0, 6)+//Valor do ICMS ou do ICMS ST
//										  Util.fillNum(0, 6)+//Outros valores
//										  "\r\n";
//					nrLinhaBlocoC++;
			
		}
		
		
		
		
		
		
		/*
		 * Registro C300 - RESUMO DIÁRIO DAS NOTAS FISCAIS DE VENDA A CONSUMIDOR - Apenas SAIDAS  - Ver como implementar
		 */
//		PARA PERFIL B
//		String C300 = "C300" +
//							  Util.fillAlpha("", 2)+//Código do modelo do documento fiscal   - Ver qual será o modelo para as notas que nao forem nem 1A nem 55
//							  Util.fillAlpha("", 4)+//Série do documento fiscal																- Saber o que colocar nessa serie
//							  Util.fillAlpha("", 3)+//Subsérie do documento fiscal
//							  Util.fill(nrInicial, 6, '0', 'E')+//Número do documento fiscal inicial
//							  Util.fill(nrFinal, 6, '0', 'E')+//Número do documento fiscal final
//							  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+ //Data da emissão dos documentos fiscais
//							  Util.fill(String.valueOf(valorTotal).replace('.', ','), 10, '0', 'E')+//Valor total dos documentos
//							  Util.fillNum(0, 10)+//Valor total do PIS
//							  Util.fillNum(0, 10)+//Valor total da COFINS
//							  Util.fillAlpha("", 60)+//Código da conta analítica contábil debitada/creditada                                 - Ver que conta eh essa
//							  "\r\n";
//		nrLinhaBlocoC++;
//		
//		
//		
//		/*
//		 * Registro C310 - DOCUMENTOS CANCELADOS DE NOTAS FISCAIS DE VENDA A CONSUMIDOR
//		 */
//		String C310 = "C310" +
//							  Util.fillNum(0, 6)+//Número do documento fiscal cancelado
//							  "\r\n";
//		
//		/*
//		 * Registro C320 - REGISTRO ANALÍTICO DO RESUMO DIÁRIO DAS NOTAS FISCAIS DE VENDA A CONSUMIDOR
//		 */
//		String C320 = "C320" +
//							  Util.fillNum(0, 3)+//Código da Situação Tributária
//							  Util.fillNum(0, 4)+//Código Fiscal de Operação e Prestação
//							  Util.fillNum(0, 6)+//Alíquota do ICMS
//							  Util.fillNum(0, 6)+//Valor total acumulado das operações correspondentes à combinação de CST_ICMS, CFOP e alíquota do ICMS
//							  Util.fillNum(0, 6)+//Valor acumulado da base de cálculo do ICMS, referente à combinação de CST_ICMS
//							  Util.fillNum(0, 6)+//Valor acumulado do ICMS, referente à combinação de CST_ICMS, CFOP e alíquota do ICMS
//							  Util.fillNum(0, 6)+//Valor não tributado em função da redução da base de cálculo do ICMS, referente à combinação de CST_ICMS, CFOP, e alíquota do ICMS.
//							  Util.fillAlpha("", 6)+//Código da observação do lançamento fiscal
//							  "\r\n";
//		
//		/*
//		 * Registro C321 - ITENS DO RESUMO DIÁRIO DOS DOCUMENTOS
//		 */
//		String C321 = "C321" +
//							  Util.fillAlpha("", 60)+//Código do item
//							  Util.fillNum(0, 6)+//Quantidade acumulada do item
//							  Util.fillAlpha("", 6)+//Unidade do item
//							  Util.fillNum(0, 6)+//Valor acumulado do item
//							  Util.fillNum(0, 6)+//Valor do desconto acumulado
//							  Util.fillNum(0, 6)+//Valor acumulado da base de cálculo do ICMS
//							  Util.fillNum(0, 6)+//Valor acumulado do ICMS debitado
//							  Util.fillNum(0, 6)+//Valor acumulado do PIS
//							  Util.fillNum(0, 6)+//Valor acumulado da COFINS
//							  "\r\n";
		
		for(int i = 0; i < codigosDocumentoSaidaSemECF.size(); i++){
			//Documento
			DocumentoSaida saida = DocumentoSaidaDAO.get(codigosDocumentoSaidaSemECF.get(i));
			//Numero do documento
			int nrDoc = 0;
			if(saida.getNrDocumentoSaida() != null && !saida.getNrDocumentoSaida().trim().equals("")){
				nrDoc = Integer.parseInt(Util.limparFormatos(saida.getNrDocumentoSaida(), 'N'));
			}
			String nrDocumento = ((nrDoc > 999999) ? String.valueOf(nrDoc).substring(0, 6) : String.valueOf(nrDoc));
			//Destinatario
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("A.CD_PESSOA", "" + saida.getCdCliente(), ItemComparator.EQUAL, Types.INTEGER));
			ResultSetMap rsmPessoa = PessoaServices.find(criterios);
			
			
			
			
			
			/************************************** VALIDACAO *******************************************************/
			//Natureza de Operacao
			//CFOP
			if(!rsmPessoa.next())
		    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Cliente ";
			
		    if(!dsValidacao.equals("")){
		    	HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("ERRO", "Faltando do Documento de saida Nº "+saida.getNrDocumentoSaida()+": "+dsValidacao);
		    	rsm.addRegister(register);
		    
		    	dsValidacao = "";
		    	entrouDs = true;
		    }
			
			
			
		    if(!entrouDs && !entrouDs2){
				//Valor total dos itens
				float valorProdutos = DocumentoSaidaServices.getValorAllItens(saida.getCdDocumentoSaida());
				
				//Tributo
				criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("CD_DOCUMENTO_SAIDA", "" + saida.getCdDocumentoSaida(), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmSaidaTrib = SaidaTributoDAO.find(criterios);
				rsmSaidaTrib.next();
				
//			NotaFiscal nfe = null;
//			
//			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
//			criterios.add(new ItemComparator("CD_DOCUMENTO_SAIDA", "" + saida.getCdDocumentoSaida(), ItemComparator.EQUAL, Types.INTEGER));
//			ResultSetMap rsmDocVinc = NotaFiscalDocVinculadoDAO.find(criterios);
//			
//			if(rsmDocVinc.next()){
//				nfe = NotaFiscalDAO.get(rsmDocVinc.getInt("cd_nota_fiscal"));
//			}
			
//			if((nfe != null && nfe.getStNotaFiscal() != NotaFiscalServices.CANCELADA) || nfe == null){
			
				/*
				 * Registro C350 - NOTA FISCAL DE VENDA A CONSUMIDOR - APENAS SAIDAS
				 */
				registro += "|C350" +
						  "|" +"001"+//Série do documento fiscal
						  "|" +//Subsérie do documento fiscal
						  "|" +nrDocumento+//Número do documento fiscal
						  "|" +Util.formatDate(saida.getDtEmissao(), "ddMMyyyy")+ //Data da emissão do documento fiscal
						  "|" +((rsmPessoa.getString("nr_cpf") != null) ? rsmPessoa.getString("nr_cpf") : (rsmPessoa.getString("nr_cnpj") != null) ? rsmPessoa.getString("nr_cnpj") : "")+//CNPJ ou CPF do destinatário
						  "|" +Util.formatNumber(valorProdutos, "#.##")+//Valor das mercadorias constantes no documento fiscal
						  "|" +Util.formatNumber(saida.getVlTotalDocumento(), "#.##")+//Valor total do documento fiscal
						  "|" +Util.formatNumber(saida.getVlDesconto(), "#.##")+//Valor total do desconto
						  "|" +//Valor total do PIS
						  "|" +//Valor total da COFINS
						  "|" +//Código da conta analítica contábil debitada/creditada
						  "|" +"\r\n";
				nrLinhaBlocoC++;
				conjuntoRegistro.put("C350", (((Integer) conjuntoRegistro.get("C350")) + 1));
//				
			
				/*
				 * Registro C370 - Refernte a C350 - ITENS DO DOCUMENTO
				 */
					
				ResultSetMap rsmItens = DocumentoSaidaServices.getAllItens(saida.getCdDocumentoSaida(), true);
				
				while(rsmItens.next()){
					registro	 += "|C370" +
										  "|" +rsmItens.getInt("cd_item")+//Número sequencial do item no documento fiscal
							 			  "|" +rsmItens.getInt("cd_produto_servico")+//Código do Item
							 			  "|" +Util.formatNumber(rsmItens.getFloat("qt_saida"), "#.##")+//Quantidade do item
							 			  "|" +rsmItens.getInt("sg_unidade_medida")+//Unidade do item
							 			  "|" +Util.formatNumber(((rsmItens.getFloat("vl_unitario") * rsmItens.getFloat("qt_saida")) + rsmItens.getFloat("vl_acrescimo") - rsmItens.getFloat("vl_desconto")), "#.##")+//Valor total do item
										  "|" +Util.formatNumber(rsmItens.getFloat("vl_desconto"), "#.##")+//Valor total do desconto no item	 			  
										  "|" +"\r\n";
					nrLinhaBlocoC++;
					conjuntoRegistro.put("C370", (((Integer) conjuntoRegistro.get("C370")) + 1));
					
				}
				/*
				 * Registro C390 - Refernte a C350 - REGISTRO ANALÍTICO DAS NOTAS FISCAIS DE VENDA A CONSUMIDOR - APENAS SAIDAS
				 */
				
				ResultSetMap rsmTrib = getTributacao(saida.getCdDocumentoSaida(), 0);
				
//				if(!rsmTrib.next())
//					return new Result(-1, "ERRO na tributação de um produto do Documento de Saída N." + saida.getNrDocumentoSaida());
				
				
				while(rsmTrib.next()){
					
					/************************************** VALIDACAO *******************************************************/
					//Natureza de Operacao
					//CFOP
					if(rsmTrib.getString("nr_codigo_fiscal") == null)
				    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CFOP ";
					
				    if(!dsValidacao.equals("")){
				    	HashMap<String, Object> register = new HashMap<String, Object>();
				    	register.put("ERRO", "Dados Analiticos do Documento Nº "+saida.getNrDocumentoSaida()+": "+dsValidacao);
				    	rsm.addRegister(register);
				    
				    	dsValidacao = "";
				    	//entrouDs = true;
				    }
				    
				    if(!entrouDs && !entrouDs2){
					    vlIcmsDebito += rsmTrib.getFloat("vl_icms");
//					    vlIcmsDebitoSTRetencao += rsmTrib.getFloat("vl_icms_substituto");
					    String cst = rsmTrib.getString("nr_situacao_tributaria");
					    registro += "|C390" + 
											  "|" +(cst.length() < 3 ? "0" + cst : cst)+//Código da Situação Tributária - http://www.cosif.com.br/mostra.asp?arquivo=sinief-CST-anexo
											  "|" +rsmTrib.getString("nr_codigo_fiscal")+//Código Fiscal de Operação e Prestação
											  "|" +Util.formatNumber((rsmTrib.getFloat("pr_aliquota") > 0 ? rsmTrib.getFloat("pr_aliquota") : rsmTrib.getFloat("pr_aliquota_substituto")), "#.##")+//Alíquota do ICMS
											  "|" +Util.formatNumber(saida.getVlTotalDocumento(), "#.##")+//Valor total acumulado das operações correspondentes à combinação de CST_ICMS, CFOP e alíquota do ICMS
											  "|" +Util.formatNumber((rsmTrib.getFloat("vl_base_calculo") > 0 ? rsmTrib.getFloat("vl_base_calculo") : rsmTrib.getFloat("vl_base_calculo_substituto")), "#.##")+//Valor acumulado da base de cálculo do ICMS, referente à combinação de CST_ICMS
											  "|" +Util.formatNumber((rsmTrib.getFloat("vl_icms") > 0 ? rsmTrib.getFloat("vl_icms") : rsmTrib.getFloat("vl_icms_substituto")), "#.##")+//Valor acumulado do ICMS, referente à combinação de CST_ICMS, CFOP e alíquota do ICMS
											  "|" +"0,00"+//Valor não tributado em função da redução da base de cálculo do ICMS, referente à combinação de CST_ICMS, CFOP, e alíquota do ICMS.
											  "|" +//Código da observação do lançamento fiscal - INCLUIR
											  "|" +"\r\n";
						nrLinhaBlocoC++;
						conjuntoRegistro.put("C390", (((Integer) conjuntoRegistro.get("C390")) + 1));
				    }
				    
				    //entrouDs = false;
				}
//			}
		    }
			
		}
		boolean hasC425 = false;
		
		try{
			Connection connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
										 "WHERE CAST(dt_registro_ecf AS DATE) BETWEEN ? AND ? " +
										 "  AND tp_registro_ecf = \'C400\' " +
										 "ORDER BY dt_registro_ecf, cd_registro_ecf ");
			pstmt.setTimestamp(1, new Timestamp(dtSpedInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtSpedFinal.getTimeInMillis()));
			
			PreparedStatement pstmtEcfSped = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
					                                                  "WHERE cd_registro_ecf = ?");
			
			PreparedStatement pstmtEcfSpedAux = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
																		"WHERE cd_registro_ecf > ? AND tp_registro_ecf LIKE 'C4%'");
			
			ResultSetMap rsmEcf = new ResultSetMap(pstmt.executeQuery());
			int cont = 0;
			while(rsmEcf.next()){
				int cdRegEcf = rsmEcf.getInt("cd_registro_ecf");
				String tokens[] = rsmEcf.getString("txt_registro_ecf").split("C405");
				String c400 = tokens[0].substring(0, tokens[0].length()-1);
	            String c405 = "|C405" + tokens[1];
				registro += c400 +"\r\n";
				registro += c405 +"\r\n";
				nrLinhaBlocoC++;
				nrLinhaBlocoC++;
				conjuntoRegistro.put("C400", (((Integer) conjuntoRegistro.get("C400")) + 1));
				conjuntoRegistro.put("C405", (((Integer) conjuntoRegistro.get("C405")) + 1));
				boolean proxEcf = false;
				while(!proxEcf){
					pstmtEcfSped.setInt(1, ++cdRegEcf);
					ResultSetMap rsmEcfAux = new ResultSetMap(pstmtEcfSped.executeQuery());
					if(rsmEcfAux.next()){
						if(rsmEcfAux.getString("tp_registro_ecf").startsWith("C4") && !rsmEcfAux.getString("tp_registro_ecf").equals("C400")){
							if((rsmEcfAux.getString("tp_registro_ecf").equals("C460") || rsmEcfAux.getString("tp_registro_ecf").equals("C470"))){
								if(tpPerfilEmpresa.equals(IND_PERFIL_A)){
									String regFinal = "";
									if(rsmEcfAux.getString("tp_registro_ecf").equals("C460")){
										String reg = rsmEcfAux.getString("txt_registro_ecf");
										String[] regCons = reg.split("\\|");
										if(regCons.length >= 9){
											boolean cpfCnpjInvalido = ((regCons[9].length() == 14 ? !Util.isCNPJ(regCons[9]) : true) && (regCons[9].length() == 11 ? !Util.isCpfValido(regCons[9]) : true));
											for(int i = 0; i < regCons.length; i++){
												if(i != 9 || !cpfCnpjInvalido)
													regFinal += regCons[i] + "|";
												else if(i == 9 && cpfCnpjInvalido){
													regFinal += "|";
												}
											}
										}
										else{
											regFinal = rsmEcfAux.getString("txt_registro_ecf");
										}
										char[] contagemPipes = regFinal.toCharArray();
										int contPipes = 0;
										for(int i = 0;i<contagemPipes.length;i++){
											if(contagemPipes[i] == '|'){
												contPipes++;
											}
										}
										if(contPipes <= 11){
											for(int i = 0; i < 11 - contPipes; i++){
												regFinal += "|";
											}
										}
									}
									else{
										regFinal = rsmEcfAux.getString("txt_registro_ecf");
									}
									registro += regFinal.replaceAll("00000000000000", "") +"\r\n";
									nrLinhaBlocoC++;
									conjuntoRegistro.put(rsmEcfAux.getString("tp_registro_ecf"), (((Integer) conjuntoRegistro.get(rsmEcfAux.getString("tp_registro_ecf"))) + 1));
								}
							}
							else if(rsmEcfAux.getString("tp_registro_ecf").equals("C425")){
								if(tpPerfilEmpresa.equals(IND_PERFIL_B)){
									hasC425 = true;
									registro += rsmEcfAux.getString("txt_registro_ecf") +"\r\n";
									nrLinhaBlocoC++;
									conjuntoRegistro.put(rsmEcfAux.getString("tp_registro_ecf"), (((Integer) conjuntoRegistro.get(rsmEcfAux.getString("tp_registro_ecf"))) + 1));
								}
							}
							else if(rsmEcfAux.getString("tp_registro_ecf").equals("C495")){
//								if(!hasC425){
//									registro += rsmEcfAux.getString("txt_registro_ecf") +"\r\n";
//									nrLinhaBlocoC++;
//									conjuntoRegistro.put(rsmEcfAux.getString("tp_registro_ecf"), (((Integer) conjuntoRegistro.get(rsmEcfAux.getString("tp_registro_ecf"))) + 1));
//								}
							}
							else{
								registro += rsmEcfAux.getString("txt_registro_ecf") +"\r\n";
								nrLinhaBlocoC++;
								conjuntoRegistro.put(rsmEcfAux.getString("tp_registro_ecf"), (((Integer) conjuntoRegistro.get(rsmEcfAux.getString("tp_registro_ecf"))) + 1));
							}
							
						}
						else
							proxEcf = true;
					}
					else{
						pstmtEcfSpedAux.setInt(1, cdRegEcf);
						ResultSetMap rsmEcfAuxAux = new ResultSetMap(pstmtEcfSpedAux.executeQuery());
						if(!rsmEcfAuxAux.next())
							proxEcf = true;
					}
				}
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		
//		for(int i = 0; i < codigosDocumentoSaidaComECF.size(); i++){
//			
//			
//			DocumentoSaida docSaida = DocumentoSaidaDAO.get(codigosDocumentoSaidaComECF.get(i));
//			Referencia referencia   = ReferenciaDAO.get(docSaida.getCdReferenciaEcf());
//			
//			
//			
//			
//			
//			/*
//			 * Registro C400 - EQUIPAMENTO ECF - APENAS PARA SAIDAS
//			 */
//			String C400 = "C400" +
//								  Util.fillAlpha("", 2)+//Código do modelo do documento fiscal - 02 ou 2D 
//								  Util.fillAlpha(referencia.getNmModelo(), 20)+//Modelo do equipamento
//								  Util.fillAlpha(referencia.getNrSerie() , 20)+//Número de série de fabricação do ECF
//								  Util.fillNum(numeroCaixa,3)+//Número do caixa atribuído ao ECF
//								  "\r\n";
//			nrLinhaBlocoC++;
//			conjuntoRegistro.put("C400", (((Integer) conjuntoRegistro.get("C400")) + 1));
//		
//		}
//		/*
//		 * Registro C405 - Refernte a C400 - REDUÇÃO Z - Referentes ao equipamento pelo que pude entender
//		 */
//		String C405 = "C405" +
//							  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+ //Data do movimento a que se refere a Redução Z
//							  Util.fillNum(0,3)+//Posição do Contador de Reinício de Operação
//							  Util.fillNum(0,6)+//Posição do Contador de Redução Z
//							  Util.fillNum(0,6)+//Número do Contador de Ordem de Operação do último documento emitido no dia. (Número do COO na Redução Z)
//							  Util.fillNum(0,6)+//Valor do Grande Total final
//							  Util.fillNum(0,6)+//Valor da venda bruta
//							  "\r\n";
//		nrLinhaBlocoC++;
//		conjuntoRegistro.put("C405", (((Integer) conjuntoRegistro.get("C405")) + 1));
//		
//		
////			/*
////			 * Registro C410 - PIS E COFINS TOTALIZADOS NO DIA - Referente a C405
////			 */
////			String C410 = "C410" +
////								  Util.fillNum(0,6)+//Valor total do PIS
////								  Util.fillNum(0,6)+//Valor total da COFINS
////								  "\r\n";
//		
//		/*
//		 * Registro C420 - Refernte a C405 - REGISTRO DOS TOTALIZADORES PARCIAIS DA REDUÇÃO Z - Referente a C405
//		 */
//		String C420 = "C420" +
//				 			  Util.fillAlpha("", 7)+//Código do totalizador
//				 			  Util.fillNum(0,6)+//Valor acumulado no totalizador, relativo à respectiva Redução Z.
//							  Util.fillNum(0,2)+//Número do totalizador quando ocorrer mais de uma situação com a mesma carga tributária efetiva
//							  Util.fillAlpha("", 30)+//Descrição da situação tributária relativa ao totalizador parcial, quando houver mais de um com a mesma carga tributária efetiva.
//							  "\r\n";
//		nrLinhaBlocoC++;
//		conjuntoRegistro.put("C420", (((Integer) conjuntoRegistro.get("C420")) + 1));
//		
//		
//		/*
//		 * Registro C425 - Refernte a C420 - RESUMO DE ITENS DO MOVIMENTO DIÁRIO - Referente ao C420
//		 */
//		String C425 = "C425" +
//							  Util.fillAlpha("", 60)+//Código do item
//							  Util.fillNum(0,6)+//Quantidade acumulada do item
//							  Util.fillAlpha("", 6)+//Unidade do item
//							  Util.fillNum(0,6)+//Valor acumulado do item
//							  Util.fillNum(0,6)+//Valor do PIS
//							  Util.fillNum(0,6)+//Valor da COFINS
//							  "\r\n";
//		nrLinhaBlocoC++;
//		conjuntoRegistro.put("C425", (((Integer) conjuntoRegistro.get("C425")) + 1));
//		
//		/*
//		 * Registro C460 - Refernte a C405 - DOCUMENTO FISCAL EMITIDO POR ECF - Referente a C405
//		 */
//		String C460 = "C460" +
//							  Util.fillAlpha("", 2)+//Código do modelo do documento fiscal
//							  Util.fillNum(0,2)+//Código da situação do documento fiscal
//							  Util.fillNum(0,6)+//Número do documento fiscal (COO)
//							  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+ //Data da emissão do documento fiscal
//							  Util.fillNum(0,6)+//Valor total do documento fiscal
//							  Util.fillNum(0,6)+//Valor do PIS
//							  Util.fillNum(0,6)+//Valor da COFINS
//							  Util.fillNum(0,14)+//CPF ou CNPJ do adquirente
//							  Util.fillAlpha("", 60)+//Nome do adquirente
//							  "\r\n";
//		nrLinhaBlocoC++;
//		conjuntoRegistro.put("C460", (((Integer) conjuntoRegistro.get("C460")) + 1));
//		
//		/*
//		 * Registro C470 - Refernte a C460 - ITENS DO DOCUMENTO FISCAL EMITIDO POR ECF - Referente a C460
//		 */
//		String C470 = "C470" +
//				 			  Util.fillAlpha("", 60)+//Código do item
//				 			  Util.fillNum(0,6)+//Quantidade do item
//				 			  Util.fillNum(0,6)+//Quantidade cancelada, no caso de cancelamento parcial de item
//				 			  Util.fillAlpha("", 6)+//Unidade do item
//				 			  Util.fillNum(0,6)+//Valor total do item
//							  Util.fillNum(0,3)+//Código da Situação Tributária
//							  Util.fillNum(0,4)+//Código Fiscal de Operação e Prestação
//							  Util.fillNum(0,6)+//Alíquota do ICMS  Carga tributária efetiva em percentual
//							  Util.fillNum(0,6)+//Valor do PIS
//							  Util.fillNum(0,6)+//Valor da COFINS
//							  "\r\n";
//		nrLinhaBlocoC++;
//		conjuntoRegistro.put("C470", (((Integer) conjuntoRegistro.get("C470")) + 1));
//		
//		
//		/*
//		 * Registro C490 - Refernte a C405 - REGISTRO ANALÍTICO DO MOVIMENTO DIÁRIO - Referente a C405
//		 */
//		String C490 = "C490" +
//							  Util.fillNum(0,3)+//Código da Situação Tributária
//							  Util.fillNum(0,4)+//Código Fiscal de Operação e Prestação
//							  Util.fillNum(0,6)+//Alíquota do ICMS
//							  Util.fillNum(0,6)+//Valor da operação correspondente à combinação de CST_ICMS, CFOP, e alíquota do ICMS, incluídas as despesas acessórias e acréscimos
//							  Util.fillNum(0,6)+//Valor acumulado da base de cálculo do ICMS,referente à combinação de CST_ICMS, CFOP, e alíquota do ICMS.
//							  Util.fillNum(0,6)+//Valor acumulado do ICMS, referente à combinação de CST_ICMS, CFOP e alíquota do ICMS.
//							  Util.fillAlpha("", 6)+//Código da observação do lançamento fiscal
//							  "\r\n";
//		nrLinhaBlocoC++;
//		conjuntoRegistro.put("C490", (((Integer) conjuntoRegistro.get("C490")) + 1));
//		
//		
//		
//		/*
//		 * Registro C495 - RESUMO MENSAL DE ITENS DO ECF POR ESTABELECIMENTO - Saber qual a diferença deste registro para o C170
//		 */
//		String C495 = "C495" +
//							  Util.fillNum(0,6)+//Alíquota do ICMS
//							  Util.fillAlpha("", 60)+//Código do item
//							  Util.fillNum(0,6)+//Quantidade acumulada do item
//							  Util.fillNum(0,6)+//Quantidade cancelada acumulada, no caso de cancelamento parcial de item
//							  Util.fillAlpha("", 6)+//Unidade do item
//							  Util.fillNum(0,6)+//Valor acumulado do item
//							  Util.fillNum(0,6)+//Valor acumulado dos descontos
//							  Util.fillNum(0,6)+//Valor acumulado dos cancelamentos
//							  Util.fillNum(0,6)+//Valor acumulado dos acréscimos
//							  Util.fillNum(0,6)+//Valor acumulado da base de cálculo do ICMS
//							  Util.fillNum(0,6)+//Valor acumulado do ICMS
//							  Util.fillNum(0,6)+//Valor das saídas isentas do ICMS
//							  Util.fillNum(0,6)+//Valor das saídas sob não-incidência ou não tributadas pelo ICMS
//							  Util.fillNum(0,6)+//Valor das saídas de mercadorias adquiridas com substituição tributária do ICMS
//							  "\r\n";
//		nrLinhaBlocoC++;
//		conjuntoRegistro.put("C495", (((Integer) conjuntoRegistro.get("C495")) + 1));
//			
			
//			/*
//			 * Registro C500 - NOTA FISCAL/CONTA DE ENERGIA ELÉTRICA (CÓDIGO 06), NOTA FISCAL/CONTA DE FORNECIMENTO D'ÁGUA CANALIZADA (CÓDIGO 29) E NOTA FISCAL CONSUMO FORNECIMENTO DE GÁS (CÓDIGO 28).
//			 */
//			String C500 = "C500" +
//					 			  Util.fillAlpha(String.valueOf(IND_OPER_ENTRADA), 1)+//Indicador do tipo de operação
//					 			  Util.fillAlpha(String.valueOf(IND_EMIT_PROPRIA), 1)+//Indicador do tipo de operação
//					 			  Util.fillAlpha("", 60)+//Código do participante
//					 			  Util.fillAlpha("", 2)+//Código do modelo do documento fiscal
//					 			  Util.fillNum(0,2)+//Código da situação do documento fiscal
//					 			  Util.fillAlpha("", 4)+//Série do documento fiscal
//					 			  Util.fillNum(0,3)+//Subsérie do documento fiscal
//					 			  Util.fillAlpha(String.valueOf(COD_CONS_COMERCIAL), 1)+//Indicador do tipo de operação
//					 			  Util.fillNum(0,9)+//Número do documento fiscal
//					 			  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+ //Data da emissão do documento fiscal
//					 			  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+ //Data da entrada ou da saída
//								  Util.fillNum(0,6)+//Valor total do documento fiscal
//								  Util.fillNum(0,6)+//Valor total do desconto
//								  Util.fillNum(0,6)+//Valor total fornecido/consumido
//								  Util.fillNum(0,6)+//Valor total dos serviços não-tributados pelo ICMS
//								  Util.fillNum(0,6)+//Valor total cobrado em nome de terceiros
//								  Util.fillNum(0,6)+//Valor total de despesas acessórias indicadas no documento fiscal
//								  Util.fillNum(0,6)+//Valor acumulado da base de cálculo do ICMS
//								  Util.fillNum(0,6)+//Valor acumulado do ICMS
//								  Util.fillNum(0,6)+//Valor acumulado da base de cálculo do ICMS substituição tributária
//								  Util.fillNum(0,6)+//Valor acumulado do ICMS retido por substituição tributária
//								  Util.fillAlpha("", 6)+//Código da informação complementar do documento fiscal
//								  Util.fillNum(0,6)+//Valor do PIS
//								  Util.fillNum(0,6)+//Valor da COFINS
//								  Util.fillNum(TP_LIGACAO_BIFASICO,1)+//Código de tipo de Ligação
//								  Util.fillAlpha(String.valueOf(COD_GRUPO_TENSAO_A1), 1)+//Código de grupo de tensão:
//					 			  "\r\n";
			
//			/*
//			 * Registro C510 - ITENS DO DOCUMENTO NOTA FISCAL/CONTA ENERGIA
//								ELÉTRICA (CÓDIGO 06), NOTA FISCAL/CONTA DE FORNECIMENTO
//								D'ÁGUA CANALIZADA (CÓDIGO 29) E NOTA FISCAL/CONTA DE
//								FORNECIMENTO DE GÁS (CÓDIGO 28)
//			 */
//			String C510 = "C510" +
//								  Util.fillNum(0,3)+//Número sequencial do item no documento fiscal
//								  Util.fillAlpha("", 60)+//Código do item
//								  Util.fillNum(0,4)+//Código de classificação do item de energia
//								  Util.fillNum(0,6)+//Quantidade do item
//								  Util.fillAlpha("",6)+//Unidade do item
//								  Util.fillNum(0,6)+//Valor do item
//								  Util.fillNum(0,6)+//Valor total do desconto
//								  Util.fillNum(0,3)+//Código da Situação Tributária
//								  Util.fillNum(0,4)+//Código Fiscal de Operação e Prestação
//								  Util.fillNum(0,6)+//Valor da base de cálculo do ICMS
//								  Util.fillNum(0,6)+//Alíquota do ICMS
//								  Util.fillNum(0,6)+//Valor do ICMS creditado/debitado
//								  Util.fillNum(0,6)+//Valor da base de cálculo referente à substituição tributária
//								  Util.fillNum(0,6)+//Alíquota do ICMS da substituição tributária na unidade da federação de destino
//								  Util.fillNum(0,6)+//Valor do ICMS referente à substituição tributária
//								  Util.fillAlpha(String.valueOf(IND_REC_PROPRIO),1)+//Valor total do desconto
//								  Util.fillAlpha("",60)+//Código do participante receptor da receita, terceiro da operação
//								  Util.fillNum(0,6)+//Valor do PIS
//								  Util.fillNum(0,6)+//Valor da COFINS
//								  Util.fillAlpha("",60)+//Código da conta analítica contábil debitada/creditada
//								  "\r\n";
//			/*
//			 * Registro C590 - REGISTRO ANALÍTICO DO DOCUMENTO- NOTA
//				FISCAL/CONTA DE ENERGIA ELÉTRICA (CÓDIGO 06), NOTA
//				FISCAL/CONTA DE FORNECIMENTO D'ÁGUA CANALIZADA (CÓDIGO 29)
//				E NOTA FISCAL CONSUMO FORNECIMENTO DE GÁS (CÓDIGO 28).
//			 */
//			String C590 = "C590" +
//								  Util.fillNum(0,3)+//Código da Situação Tributária
//								  Util.fillNum(0,4)+//Código Fiscal de Operação e Prestação do agrupamento de itens
//								  Util.fillNum(0,6)+//Alíquota do ICMS
//								  Util.fillNum(0,6)+//Valor da operação correspondente à combinação de CST_ICMS, CFOP, e alíquota do ICMS.
//								  Util.fillNum(0,6)+//Parcela correspondente ao "Valor da base de cálculo do ICMS" referente à combinação de CST_ICMS, CFOP e alíquota do ICMS
//								  Util.fillNum(0,6)+//Parcela correspondente ao "Valor do ICMS" referente à combinação de CST_ICMS, CFOP e alíquota do ICMS
//								  Util.fillNum(0,6)+//Parcela correspondente ao "Valor da base de cálculo do ICMS" da substituição tributária referente à combinação de CST_ICMS, CFOP e alíquota do ICMS.
//								  Util.fillNum(0,6)+//Parcela correspondente ao valor creditado/debitado do ICMS da substituição tributária, referente à combinação de CST_ICMS, CFOP, e alíquota do ICMS.
//								  Util.fillNum(0,6)+//Valor não tributado em função da redução da base de cálculo do ICMS, referente à combinação de CST_ICMS, CFOP e alíquota do ICMS.
//								  Util.fillAlpha("",6)+//Código da observação do lançamento fiscal
//								  "\r\n";
			
//			/*
//			 * Registro C600 - CONSOLIDAÇÃO DIÁRIA DE NOTAS FISCAIS/CONTAS DE
//	ENERGIA ELÉTRICA (CÓDIGO 06), NOTA FISCAL/CONTA DE
//	FORNECIMENTO D'ÁGUA CANALIZADA (CÓDIGO 29) E NOTA
//	FISCAL/CONTA DE FORNECIMENTO DE GÁS (CÓDIGO 28) (EMPRESAS
//	NÃO OBRIGADAS AO CONVÊNIO ICMS 115/03).
//			 */
//			String C600 = "C600" +
//								  Util.fillAlpha("",2)+//Código do modelo do documento fiscal
//								  Util.fillNum(0,7)+//Código do município dos pontos de consumo
//								  Util.fillAlpha("",4)+//Série do documento fiscal
//								  Util.fillNum(0,3)+//Subsérie do documento fiscal
//								  Util.fillAlpha(String.valueOf(COD_CONS_COMERCIAL), 1)+//Indicador do tipo de operação
//								  Util.fillNum(0,6)+//Quantidade de documentos consolidados neste registro
//								  Util.fillNum(0,6)+//Quantidade de documentos cancelados.
//								  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+ //Data dos documentos consolidados
//					 			  Util.fillNum(0,6)+//Valor total dos documentos
//								  Util.fillNum(0,6)+//Valor acumulado dos descontos
//								  Util.fillNum(0,6)+//Consumo total acumulado, em kWh (Código 06)
//								  Util.fillNum(0,6)+//Valor acumulado do fornecimento
//								  Util.fillNum(0,6)+//Valor acumulado dos serviços não-tributados pelo ICMS
//								  Util.fillNum(0,6)+//Valores cobrados em nome de terceiros
//								  Util.fillNum(0,6)+//Valor acumulado das despesas acessórias
//								  Util.fillNum(0,6)+//Valor acumulado da base de cálculo do ICMS
//								  Util.fillNum(0,6)+//Valor acumulado do ICMS
//								  Util.fillNum(0,6)+//Valor acumulado da base de cálculo do ICMS substituição tributária
//								  Util.fillNum(0,6)+//Valor acumulado do ICMS retido por substituição tributária
//								  Util.fillNum(0,6)+//Valor acumulado do PIS
//								  Util.fillNum(0,6)+//Valor acumulado COFINS
//								  "\r\n";
			
//			/*
//			 * Registro C601 - DOCUMENTOS CANCELADOS- CONSOLIDAÇÃO DIÁRIA DE
//	NOTAS FISCAIS/CONTAS DE ENERGIA ELÉTRICA (CÓDIGO 06), NOTA
//	FISCAL/CONTA DE FORNECIMENTO D'ÁGUA CANALIZADA (CÓDIGO 29)
//	E NOTA FISCAL/CONTA DE FORNECIMENTO DE GÁS (CÓDIGO 28)
//			 */
//			String C601 = "C601" +
//								  Util.fillNum(0,9)+//Número do documento fiscal cancelado
//								  "\r\n";
			
			
//			/*
//			 * Registro C610 - ITENS DO DOCUMENTO CONSOLIDADO(CÓDIGO 06), NOTA
//FISCAL/CONTA DE FORNECIMENTO D'ÁGUA CANALIZADA (CÓDIGO 29)
//E NOTA FISCAL/CONTA DE FORNECIMENTO DE GÁS (CÓDIGO 28)
//(EMPRESAS NÃO OBRIGADAS AO CONVÊNIO ICMS 115/03).
//			 */
//			String C610 = "C610" +
//								  Util.fillNum(0,4)+//Código de classificação do item de energia elétrica
//								  Util.fillAlpha("", 60)+//Código do item
//								  Util.fillNum(0,6)+//Quantidade acumulada do item
//								  Util.fillAlpha("", 6)+//Unidade do item
//								  Util.fillNum(0,6)+//Valor acumulado do ITEM
//								  Util.fillNum(0,6)+//Valor acumulado dos descontos
//								  Util.fillNum(0,3)+//Código da Situação Tributária
//								  Util.fillNum(0,4)+//Código Fiscal de Operação e Prestação
//								  Util.fillNum(0,6)+//Alíquota do ICMS
//								  Util.fillNum(0,6)+//Valor acumulado da base de cálculo do ICMS
//								  Util.fillNum(0,6)+//Valor acumulado do ICMS debitado
//								  Util.fillNum(0,6)+//Valor da base de cálculo do ICMS substituição tributária
//								  Util.fillNum(0,6)+//Valor do ICMS retido por substituição tributária
//								  Util.fillNum(0,6)+//Valor do PIS
//								  Util.fillNum(0,6)+//Valor da COFINS
//								  Util.fillAlpha("",60)+//Código da conta analítica contábil debitada/creditada
//								  "\r\n";
			
//			/*
//			 * Registro C690 - REGISTRO ANALÍTICO DOS DOCUMENTOS (NOTAS
//FISCAIS/CONTAS DE ENERGIA ELÉTRICA (CÓDIGO 06), NOTA
//FISCAL/CONTA DE FORNECIMENTO DÁGUA CANALIZADA (CÓDIGO 29)
//E NOTA FISCAL/CONTA DE FORNECIMENTO DE GÁS (CÓDIGO 28)
//			 */
//			String C690 = "C690" +
//					  			  Util.fillNum(0,3)+//Código da Situação Tributária
//					  			  Util.fillNum(0,4)+//Código Fiscal de Operação e Prestação do agrupamento de itens
//					  			  Util.fillNum(0,6)+//Alíquota do ICMS
//					  			  Util.fillNum(0,6)+//Valor da operação correspondente à combinação de CST_ICMS, CFOP, e alíquota do ICMS.
//					  			  Util.fillNum(0,6)+//Parcela correspondente ao "Valor da base de cálculo do ICMS" referente à combinação de CST_ICMS, CFOP e alíquota do ICMS
//					  			  Util.fillNum(0,6)+//Parcela correspondente ao "Valor do ICMS" referente à combinação de CST_ICMS, CFOP e alíquota do ICMS
//					  			  Util.fillNum(0,6)+//Parcela correspondente ao "Valor da base de cálculo do ICMS" da substituição tributária referente à combinação de CST_ICMS, CFOP e alíquota do ICMS.
//					  			  Util.fillNum(0,6)+//Parcela correspondente ao valor creditado/debitado do ICMS da substituição tributária, referente à combinação de CST_ICMS, CFOP, e alíquota do ICMS.
//					  			  Util.fillNum(0,6)+//Valor não tributado em função da redução da base de cálculo do ICMS, referente à combinação de CST_ICMS, CFOP e alíquota do ICMS.
//					  			  Util.fillAlpha("",6)+//Código da observação do lançamento fiscal
//					  			  "\r\n";	
			
//			/*
//			 * Registro C700 - CONSOLIDAÇÃO DOS DOCUMENTOS NF/CONTA ENERGIA
//ELÉTRICA (CÓD 06), EMITIDAS EM VIA ÚNICA (EMPRESAS OBRIGADAS À
//ENTREGA DO ARQUIVO PREVISTO NO CONVÊNIO ICMS 115/03) E NOTA
//FISCAL/CONTA DE FORNECIMENTO DE GÁS CANALIZADO (CÓDIGO 28)
//			 */
//			String C700 = "C700" +
//								  Util.fillAlpha("",2)+//Código do modelo do documento fiscal
//								  Util.fillAlpha("",4)+//Série do documento fiscal
//								  Util.fillNum(0,6)+//Número de ordem inicial
//					  			  Util.fillNum(0,6)+//Número de ordem final
//					  			  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+ //Data de emissão inicial dos documentos / Data inicial de vencimento da fatura
//					 			  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+ //Data de emissão final dos documentos / Data final do vencimento da fatura
//					 			  Util.fillAlpha("",15)+//Nome do arquivo Mestre de Documento Fiscal
//					 			  Util.fillAlpha("",32)+//Chave de codificação digital do arquivo Mestre de Documento Fiscal
//								  "\r\n";
//			/*
//			 * Registro C790 - REGISTRO ANALÍTICO DOS DOCUMENTOS - Referente a C700
//			 */
//			String C790 = "C790" +
//								  Util.fillNum(0,3)+//Código da Situação Tributária
//								  Util.fillNum(0,4)+//Código Fiscal de Operação e Prestação do agrupamento de itens
//								  Util.fillNum(0,6)+//Alíquota do ICMS
//								  Util.fillNum(0,6)+//Valor da operação correspondente à combinação de CST_ICMS, CFOP, e alíquota do ICMS.
//								  Util.fillNum(0,6)+//Parcela correspondente ao "Valor da base de cálculo do ICMS" referente à combinação de CST_ICMS, CFOP e alíquota do ICMS
//								  Util.fillNum(0,6)+//Parcela correspondente ao "Valor do ICMS" referente à combinação de CST_ICMS, CFOP e alíquota do ICMS
//								  Util.fillNum(0,6)+//Valor da base de cálculo do ICMS substituição tributária
//								  Util.fillNum(0,6)+//Valor do ICMS retido por substituição tributária
//								  Util.fillNum(0,6)+//Valor não tributado em função da redução da base de cálculo do ICMS, referente à combinação de CST_ICMS, CFOP e alíquota do ICMS.
//								  Util.fillAlpha("",6)+//Código da observação do lançamento fiscal
//								  "\r\n";
//			/*
//			 * Registro C791 - REGISTRO DE INFORMAÇÕES DE ST POR UF - Referente a C791
//			 */
//			String C791 = "C791" +
//							      Util.fillAlpha("",2)+//Sigla da unidade da federação a que se refere a retenção ST
//							      Util.fillNum(0,6)+//Valor da base de cálculo do ICMS substituição tributária
//								  Util.fillNum(0,6)+//Valor do ICMS retido por substituição tributária.
//								  "\r\n";
			
		
//		/*
//		 * Registro C800 - CUPOM FISCAL ELETRÔNICO
//		 */
//		String C800 = "C800" +
//							  Util.fillAlpha("",2)+//Código do modelo do documento fiscal
//							  Util.fillNum(0,2)+//Código da situação do documento fiscal
//							  Util.fillNum(0,6)+//Número do Cupom Fiscal Eletrônico
//							  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+ //Data da emissão do Cupom Fiscal Eletrônico
//				 			  Util.fillNum(0,6)+//Valor total do Cupom Fiscal Eletrônico
//							  Util.fillNum(0,6)+//Valor total do PIS
//							  Util.fillNum(0,6)+//Valor total da COFINS
//							  Util.fillNum(0,14)+//CNPJ ou CPF do destinatário
//							  Util.fillNum(0,9)+//Número de Série do equipamento SAT
//							  Util.fillNum(0,44)+//Chave do Cupom Fiscal Eletrônico
//							  Util.fillNum(0,6)+//Valor total de descontos
//							  Util.fillNum(0,6)+//Valor total das mercadorias e serviços
//							  Util.fillNum(0,6)+//Valor total de outras despesas acessórias e acréscimos
//							  Util.fillNum(0,6)+//Valor do ICMS
//							  Util.fillNum(0,6)+//Valor total do PIS retido por subst. trib.
//							  Util.fillNum(0,6)+//Valor total da COFINS retido por subst. trib.
//							  "\r\n";
//		nrLinhaBlocoC++;
		
		
		
//		/*
//		 * Registro C850 -  Referente a C800 - REGISTRO ANALÍTICO DO CF-E
//		 */
//		String C850 = "C850" +
//							  Util.fillNum(0,3)+//Código da Situação Tributária
//							  Util.fillNum(0,4)+//Código Fiscal de Operação e Prestação do agrupamento de itens
//							  Util.fillNum(0,6)+//Alíquota do ICMS
//							  Util.fillNum(0,6)+//"Valor total do CF-e" na combinação de CST_ICMS, CFOP e alíquota do ICMS, correspondente ao somatório do valor líquido dos itens.
//							  Util.fillNum(0,6)+//Valor acumulado da base de cálculo do ICMS, referente à combinação de CST_ICMS, CFOP, e alíquota do ICMS.
//							  Util.fillNum(0,6)+//Parcela correspondente ao "Valor do ICMS" referente à combinação de CST_ICMS, CFOP e alíquota do ICMS.
//							  Util.fillAlpha("",6)+//Código da observação do lançamento fiscal
//							  "\r\n";
//		nrLinhaBlocoC++;
		
		
//		/*
//		 * Registro C860 -  Referente a C800 - IDENTIFICAÇÃO DO EQUIPAMENTO SAT-CF-E
//		 */
//		String C860 = "C860" +
//							  Util.fillAlpha("",2)+//Código da observação do lançamento fiscal
//							  Util.fillNum(0,9)+//Número de Série do equipamento SAT
//							  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+ //Data de emissão dos documentos fiscais
//				 			  Util.fillNum(0,6)+//Número do documento inicial
//							  Util.fillNum(0,6)+//Número do documento final
//							  "\r\n";
//		nrLinhaBlocoC++;
		
//		/*
//		 * Registro C890  -  Referente a C800- RESUMO DIÁRIO DO CF-E
//		 */
//		String C890 = "C890" +
//							  Util.fillNum(0,3)+//Código da Situação Tributária
//							  Util.fillNum(0,4)+//Código Fiscal de Operação e Prestação do agrupamento de itens
//							  Util.fillNum(0,6)+//Alíquota do ICMS
//							  Util.fillNum(0,6)+//"Valor total do CF-e" na combinação de CST_ICMS, CFOP e alíquota do ICMS, correspondente ao somatório do valor líquido dos itens.
//							  Util.fillNum(0,6)+//Valor acumulado da base de cálculo do ICMS, referente à combinação de CST_ICMS, CFOP, e alíquota do ICMS.
//							  Util.fillNum(0,6)+//Parcela correspondente ao "Valor do ICMS" referente à combinação de CST_ICMS, CFOP e alíquota do ICMS.
//							  Util.fillAlpha("",6)+//Código da observação do lançamento fiscal
//							  "\r\n";
//		nrLinhaBlocoC++;
		
		
		nrLinhaBlocoC++;
		/*
		 * Registro C990 - ENCERRAMENTO DO BLOCO C
		 */
		registro          +=  "|C990" +
							 "|" + nrLinhaBlocoC+
							 "|" + "\r\n";
		
		conjuntoRegistro.put("C990", (((Integer) conjuntoRegistro.get("C990")) + 1));
		
		
// ******************************** BLOCO D ***************************************************************************************************************				
		
		
		/*
		 * Abertura do Bloco D - Registro D001
		 */
		registro  += "|D001" + 													 // REG - Texto fixo contendo E001
				  			  "|" + IND_MOV_SEM_DADOS+							 // IND_MOV - indicador de movimento
				  			  "|" + "\r\n";
		nrLinhaBlocoD++;
		conjuntoRegistro.put("D001", (((Integer) conjuntoRegistro.get("D001")) + 1));
		
		nrLinhaBlocoD++;
		
		/*
		 * Registro D990 - ENCERRAMENTO DO BLOCO D
		 */
		registro          +=  "|D990" +
							 "|" + nrLinhaBlocoD+
							 "|" + "\r\n";
		
		conjuntoRegistro.put("D990", (((Integer) conjuntoRegistro.get("D990")) + 1));

		
//// ******************************** BLOCO E ***************************************************************************************************************				
//		
//		 
//		/*
//		 * Abertura do Bloco E - Registro E001
//		 */
//		registro += "|E001" + 													 // REG - Texto fixo contendo E001
//						"|" + IND_MOV_DADOS+							 // IND_MOV - indicador de movimento
//				  		"|" + "\r\n";
//		nrLinhaBlocoE++;
//		conjuntoRegistro.put("E001", (((Integer) conjuntoRegistro.get("E001")) + 1));
//		
//		/*
//		 * Registro E100 - Período de Apuração do ICMS
//		 */
//		registro = "|E100" + 													 // REG - Texto fixo contendo E100
//							  "|" + Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+            //Data inicial a que a apuração se refere
//							  "|" + Util.formatDateTime(dtSpedFinal, "ddMMyyyy")+              //Data final a que a apuração se refere
//							  "|" + "\r\n";
//		nrLinhaBlocoE++;
//		conjuntoRegistro.put("E100", (((Integer) conjuntoRegistro.get("E100")) + 1));
//		
//		/*
//		 * Registro E110 - Apuração do ICMS - Operações Próprias
//		 */
//		registro += "|E110" +													 // Reg - Texto Fixo contendo E110
//				 			   "|" + Util.fillNum(0, 10)+										 // Valor total dos débitos por "Saídas e prestações com débito do imposto" - Casas decimais = 2
//							   "|" + Util.fillNum(0, 10)+										 // Valor total dos ajustes a débito decorrentes do documento fiscal. - Casas decimais = 2 
//							   "|" + Util.fillNum(0, 10)+										 // Valor total de "Ajustes a débito" - Casas decimais = 2
//							   "|" + Util.fillNum(0, 10)+										 // Valor total de Ajustes Estornos de créditos - Casas decimais = 2
//							   "|" + Util.fillNum(0, 10)+										 // Valor total dos créditos por "Entradas e aquisições com crédito do imposto" - Casas decimais = 2
//							   "|" + Util.fillNum(0, 10)+										 // Valor total dos ajustes a crédito decorrentes do documento fiscal. - Casas decimais = 2
//							   "|" + Util.fillNum(0, 10)+										 // Valor total de "Ajustes a crédito" - Casas decimais = 2
//							   "|" + Util.fillNum(0, 10)+										 // Valor total de Ajustes Estornos de Débitos - Casas decimais = 2
//							   "|" + Util.fillNum(0, 10)+										 // Valor total de "Saldo credor do período anterior" - Casas decimais = 2
//							   "|" + Util.fillNum(0, 10)+										 // Valor do saldo devedor apurado - Casas decimais = 2
//							   "|" + Util.fillNum(0, 10)+										 // Valor total de "Deduções" - Casas decimais = 2
//							   "|" + Util.fillNum(0, 10)+										 // Valor total de "ICMS a recolher (11-12) - Casas decimais = 2
//							   "|" + Util.fillNum(0, 10)+										 // Valor total de "Saldo credor a transportar para o período seguinte - Casas decimais = 2
//		 					   "|" + Util.fillNum(0, 10)+										 // Valores recolhidos ou a recolher, extraapuração. - Casas decimais = 2
//		 					   "|" + "\r\n";
//		nrLinhaBlocoE++;
//		conjuntoRegistro.put("E001", (((Integer) conjuntoRegistro.get("E001")) + 1));
//		
//		/*
//		 * Registro E111 - Ajuste/Benefício/Incentivo da Apuração do ICMS
//		 */
//		String E111 = "E111" +													//  REG - Texto fixo contendo E111
//							  Util.fillAlpha("",8)+                                     //Código do ajuste da apuração e dedução, conforme a Tabela indicada no item 5.1.1 -> COD_AJ_APUR
//							  Util.fillAlpha("",8)+                                     //Descrição complementar do ajuste da apuração.													 
//							  Util.fillNum(0, 10)+										//Valor do ajuste da apuração - Casas decimais = 2
//							  "\r\n";
//		
//		nrLinhaBlocoE++;
//		conjuntoRegistro.put("E111", (((Integer) conjuntoRegistro.get("E111")) + 1));
//		
//		/*
//		 * Registro E112 - Informações Adicionais dos Ajustes da Apuração do ICMS
//		 */
//		String E112 = "E112"+													//  REG - Texto fixo contendo E112
//				 			  Util.fillAlpha("",8)+                                     //Número do documento de arrecadação estadual, se houver
//				 			  Util.fillAlpha("",15)+                                    //Número do processo ao qual o ajuste está vinculado, se houver
//				 			  Util.fillAlpha("",1)+                                     //Indicador da origem do processo
//				 			  Util.fillAlpha("",100)+                                   //Descrição resumida do processo que embasou o lançamento
//				 			  Util.fillAlpha("",100)+                                   //Descrição complementar
//				 			  "\r\n";
//		nrLinhaBlocoE++;
//		conjuntoRegistro.put("E112", (((Integer) conjuntoRegistro.get("E112")) + 1));
//		
//		/*
//		 * Registro E113 - Informações Adicionais dos Ajustes da Apuração do ICMS - Identificação dos documentos fiscais
//		 */
//		String E113 = "E113"+													//  REG - Texto fixo contendo E113
//				 			  Util.fillAlpha("",60)+                                    //Código do participante (campo 02 do Registro 0150)
//				 			  Util.fillAlpha("",2)+                                     //Código do modelo do documento fiscal
//				 			  Util.fillAlpha("",4)+                                     //Série do documento fiscal
//				 			  Util.fillNum(0,3)+                                        //Subserie do documento fiscal
//				 			  Util.fillNum(0,9)+					                    //Número do documento fiscal
//				 			  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+           //Data da emissão do documento fiscal
//		 					  Util.fillAlpha("",60)+                                    //Código do item (campo 02 do Registro 0200)
//		 					  Util.fillNum(0,10 )+                                      //Valor do ajuste para a operação/item - Casas decimais = 2		
//		 					  "\r\n";
//		nrLinhaBlocoE++;
//		conjuntoRegistro.put("E113", (((Integer) conjuntoRegistro.get("E113")) + 1));
//		
//		/*
//		 * Registro E115 - Informações Adicionais da Apuração do ICMS - Valores Declaratórios
//		 */
//		String E115 = "E115"+                                                   //  REG - Texto fixo contendo E115
//							  Util.fillAlpha("",8)+                                     //Código da informação adicional
//							  Util.fillNum(0,10)+ 										//Valor referente à informação adicional - Casas decimais = 2	
//							  Util.fillAlpha("",100)+                                   //Descrição complementar do ajuste
//							  "\r\n";
//		nrLinhaBlocoE++;
//		conjuntoRegistro.put("E115", (((Integer) conjuntoRegistro.get("E115")) + 1));
//		
//		/*
//		 * Registro E116 - Obrigações do ICMS a Recolher - Obrigações Próprias
//		 */
//		String E116 = "E116"+													//  REG - Texto fixo contendo E116
//				 			  Util.fillAlpha("",3)+										//Código da obrigação a recolher
//				 			  Util.fillNum(0,10)+										//Valor da obrigação a recolher - Casas decimais = 2	
//				 			  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+           //Data de vencimento da obrigação
//				 			  Util.fillAlpha("",4)+                                     //Código de receita referente à obrigação, próprio da unidade da federação, conforme legislação estadual
//				 			  Util.fillAlpha("",15)+                                    //Número do processo ou auto de infração ao qual a obrigação está vinculada, se houver.
//				 			  Util.fillAlpha("",1)+					                    //Indicador da origem do processo -> IND_PROC
//				 			  Util.fillAlpha("",100)+                                   //Descrição resumida do processo que embasou o lançamento
//		 					  Util.fillAlpha("",60)+                                    //Descrição complementar das obrigações a recolher.
//		 					  Util.formatDateTime(dtSpedInicial, "MMyyyy")+             //Informe o mês de referência no formato mmaaaa
//		 					  "\r\n";
//		nrLinhaBlocoE++;
//		conjuntoRegistro.put("E116", (((Integer) conjuntoRegistro.get("E116")) + 1));
//		
//		/*
//		 * Registro E200 - Período de Apuração do ICMS - Substituição Tributária
//		 */
//		String E200 = "E200"+                                                   //  REG - Texto fixo contendo E200
//							  Util.fillAlpha("",2)+                                     //Sigla da unidade da federação a que se refere a apuração do ICMS ST
//							  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+			//Data inicial a que a apuração se refere
//							  Util.formatDateTime(dtSpedFinal, "ddMMyyyy")+				//Data final a que a apuração se refere
//							  "\r\n";
//		nrLinhaBlocoE++;
//		conjuntoRegistro.put("E200", (((Integer) conjuntoRegistro.get("E200")) + 1));
//		
//		/*
//		 * Registro E210 - Apuração do ICMS - Substituição Tributária
//		 */
//		String E210 = "E210" +													 // Reg - Texto Fixo contendo E210
//							  Util.fillAlpha("", 1)+									 // Indicador de movimento - IND_MOV_ST
//							  Util.fillNum(0, 10)+										 // Valor do "Saldo credor de período anterior  Substituição Tributária" - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Valor total do ICMS ST de devolução de mercadorias - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Valor total do ICMS ST de ressarcimentos - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Valor total de Ajustes "Outros créditos ST" e Estorno de débitos ST - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Valor total dos ajustes a crédito de ICMS ST, provenientes de ajustes do documento fiscal. - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Valor Total do ICMS retido por Substituição Tributária - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Valor Total dos ajustes "Outros débitos ST" " e Estorno de créditos ST - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Valor total dos ajustes a débito de ICMS ST, provenientes de ajustes do documento fiscal. - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Valor total de Saldo devedor antes das deduções - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Valor total dos ajustes "Deduções ST" - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Imposto a recolher ST - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Saldo credor de ST a transportar para o período seguinte [(03+04+05+06+07) (08+09+10)].
//							  Util.fillNum(0, 10)+										 // Valores recolhidos ou a recolher, extraapuração. - Casas decimais = 2
//							  "\r\n";
//		nrLinhaBlocoE++;
//		conjuntoRegistro.put("E210", (((Integer) conjuntoRegistro.get("E210")) + 1));
//		
//		/*
//		 * Registro E220 - Ajuste/Benefício/Incentivo da Apuração do ICMS - Substituição Tributária
//		 */
//		String E220 = "E220" +											//  REG - Texto fixo contendo E220
//							  Util.fillAlpha("",8)+                             //Código do ajuste da apuração e dedução, conforme a Tabela indicada no item 5.1.1 -> COD_AJ_APUR
//							  Util.fillAlpha("",8)+                             //Descrição complementar do ajuste da apuração.													 
//							  Util.fillNum(0, 10)+								//Valor do ajuste da apuração - Casas decimais = 2
//							  "\r\n";
//		nrLinhaBlocoE++;
//		conjuntoRegistro.put("E220", (((Integer) conjuntoRegistro.get("E220")) + 1));
//		
//		/*
//		 * Registro E230 - Informações Adicionais dos Ajustes da Apuração do ICMS Substituição Tributária
//		 */
//		String E230 = "E230"+										//  REG - Texto fixo contendo E230
//				 			  Util.fillAlpha("",8)+                         //Número do documento de arrecadação estadual, se houver
//				 			  Util.fillAlpha("",15)+                        //Número do processo ao qual o ajuste está vinculado, se houver
//				 			  Util.fillAlpha("",1)+                         //Indicador da origem do processo
//				 			  Util.fillAlpha("",100)+                       //Descrição resumida do processo que embasou o lançamento
//				 			  Util.fillAlpha("",100)+                       //Descrição complementar
//							  "\r\n";
//		nrLinhaBlocoE++;
//		conjuntoRegistro.put("E230", (((Integer) conjuntoRegistro.get("E230")) + 1));
//		
//		/*
//		 * Registro E240 - Informações Adicionais dos Ajustes da Apuração do ICMS Substituição Tributária - Identificação dos documentos fiscais
//		 */
//		String E240 = "E240"+										            //  REG - Texto fixo contendo E240
//				 			  Util.fillAlpha("",60)+                                    //Código do participante (campo 02 do Registro 0150)
//				 			  Util.fillAlpha("",2)+                                     //Código do modelo do documento fiscal
//				 			  Util.fillAlpha("",4)+                                     //Série do documento fiscal
//				 			  Util.fillNum(0,3)+                                        //Subserie do documento fiscal
//				 			  Util.fillNum(0,9)+					                    //Número do documento fiscal
//				 			  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+           //Data da emissão do documento fiscal
//							  Util.fillAlpha("",60)+                                    //Código do item (campo 02 do Registro 0200)
//							  Util.fillNum(0,10)+                                       //Valor do ajuste para a operação/item - Casas decimais = 2	
//							  "\r\n";
//		nrLinhaBlocoE++;
//		conjuntoRegistro.put("E240", (((Integer) conjuntoRegistro.get("E240")) + 1));
//		
//		/*
//		 * Registro E250 - Obrigações do ICMS a Recolher - Substituição Tributária
//		 */
//		String E250 = "E250"+													//  REG - Texto fixo contendo E250
//				 			  Util.fillAlpha("",3)+										//Código da obrigação a recolher
//				 			  Util.fillNum(0,10)+										//Valor da obrigação a recolher - Casas decimais = 2	
//				 			  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+           //Data de vencimento da obrigação
//				 			  Util.fillAlpha("",4)+                                     //Código de receita referente à obrigação, próprio da unidade da federação, conforme legislação estadual
//				 			  Util.fillAlpha("",15)+                                    //Número do processo ou auto de infração ao qual a obrigação está vinculada, se houver.
//				 			  Util.fillAlpha("",1)+					                    //Indicador da origem do processo -> IND_PROC
//				 			  Util.fillAlpha("",100)+                                   //Descrição resumida do processo que embasou o lançamento
//							  Util.fillAlpha("",60)+                                    //Descrição complementar das obrigações a recolher.
//							  Util.formatDateTime(dtSpedInicial, "MMyyyy")+             //Informe o mês de referência no formato mmaaaa
//							  "\r\n";
//		nrLinhaBlocoE++;
//		conjuntoRegistro.put("E250", (((Integer) conjuntoRegistro.get("E250")) + 1));
//		
//		/*
//		 * Registro E500 - Período de Apuração do IPI
//		 */
//		String E500 = "E500"+													//  REG - Texto fixo contendo E500
//				 			  Util.fillAlpha("",1)+										//Indicador de período de apuração do IPI:
//							  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+           //Data inicial a que a apuração se refere
//							  Util.formatDateTime(dtSpedFinal, "ddMMyyyy")+             //Data final a que a apuração se refere
//							  "\r\n";
//		nrLinhaBlocoE++;
//		conjuntoRegistro.put("E500", (((Integer) conjuntoRegistro.get("E500")) + 1));
//		
//		
//		/*
//		 * Registro E510 - Consolidação dos Valores de IPI
//		 */
//		String E510 = "E510"+													//  REG - Texto fixo contendo E510
//				 			  Util.fillNum(0,4)+										//Código Fiscal de Operação e Prestação do agrupamento de itens
//				 			  Util.fillAlpha("",2)+										//Código da Situação Tributária referente ao IPI, conforme a Tabela indicada no item 4.3.2.	
//				 			  Util.fillNum(0,10)+                                       //Parcela correspondente ao "Valor Contábil" referente ao CFOP e ao Código de Tributação do IPI. - Casas decimais = 2
//				 			  Util.fillNum(0,10)+                                       //Parcela correspondente ao "Valor da base de cálculo do IPI" referente ao CFOP e ao Código de Tributação do IPI, para operações tributadas - Casas decimais = 2
//				 			  Util.fillNum(0,10)+                                       //Parcela correspondente ao "Valor do IPI" referente ao CFOP e ao Código de Tributação do IPI, para operações tributadas - Casas decimais = 2
//							  "\r\n";
//		nrLinhaBlocoE++;
//		conjuntoRegistro.put("E510", (((Integer) conjuntoRegistro.get("E510")) + 1));
//		
//		/*
//		 * Registro E520 - Apuração do IPI
//		 */
//		String E520 = "E520" +													 // Reg - Texto Fixo contendo E520
//							  Util.fillNum(0, 10)+										 // Saldo credor do IPI transferido do período anterior - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Valor total dos débitos por "Saídas com débito do imposto" - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Valor total dos créditos por "Entradas e aquisições com crédito do imposto" - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Valor de "Outros débitos" do IPI (inclusive estornos de crédito) - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Valor de "Outros créditos" do IPI (inclusive estornos de débitos) - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Valor do saldo credor do IPI a transportar para o período seguinte - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Valor do saldo devedor do IPI a recolher - Casas decimais = 2
//							  "\r\n";
//		nrLinhaBlocoE++;
//		conjuntoRegistro.put("E520", (((Integer) conjuntoRegistro.get("E520")) + 1));
//		
//		/*
//		 * Registro E530 - Ajustes da Apuração do IPI
//		 */
//		String E530 = "E530"+													//  REG - Texto fixo contendo E530
//							  Util.fillAlpha("",2)+										//Indicador do tipo de ajuste
//				 			  Util.fillNum(0,10)+										//Valor do ajuste - Casas decimais = 2
//				 			  Util.fillAlpha("",3)+										//Código do ajuste da apuração	
//				 			  Util.fillAlpha("",1)+                                     //Indicador da origem do documento vinculado ao ajuste:
//				 			  Util.fillAlpha("",10)+                                    //Número do documento / processo / declaração ao qual o ajuste está vinculado, se houver
//				 			  Util.fillAlpha("",100)+                                   //Descrição detalhada do ajuste, com citação dos documentos fiscais.
//							  "\r\n";
//		nrLinhaBlocoE++;
//		conjuntoRegistro.put("E530", (((Integer) conjuntoRegistro.get("E530")) + 1));
//		
//		
//		
//		nrLinhaBlocoE++;
//		
//		/*
//		 * Registro E990 - Encerramento do Bloco E
//		 */
//		String E990 = "E990" +
//				  			  nrLinhaBlocoE+
//				  			  "\r\n";
//		conjuntoRegistro.put("E990", (((Integer) conjuntoRegistro.get("E990")) + 1));
//		
		
		//TEMPORARIO
		/*
		 * Abertura do Bloco E - Registro E001
		 */
		registro += "|E001" + 													 // REG - Texto fixo contendo E001
						"|" + IND_MOV_DADOS+							 // IND_MOV - indicador de movimento
				  		"|" + "\r\n";
		nrLinhaBlocoE++;
		conjuntoRegistro.put("E001", (((Integer) conjuntoRegistro.get("E001")) + 1));
		
		
		
		/*
		 * Registro E100 - Período de Apuração do ICMS
		 */
		registro += "|E100" + 													 // REG - Texto fixo contendo E100
							  "|" + Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+            //Data inicial a que a apuração se refere
							  "|" + Util.formatDateTime(dtSpedFinal, "ddMMyyyy")+              //Data final a que a apuração se refere
							  "|" + "\r\n";
		nrLinhaBlocoE++;
		conjuntoRegistro.put("E100", (((Integer) conjuntoRegistro.get("E100")) + 1));
		
		vlIcmsDebito += getVlC490(cdEmpresa, dtSpedInicial, dtSpedFinal);
		/*
		 * Registro E110 - Apuração do ICMS - Operações Próprias
		 */
		registro += "|E110" +													 // Reg - Texto Fixo contendo E110
				 			   "|" + Util.formatNumber(vlIcmsDebito, "#.##") +  	 // Valor total dos débitos por "Saídas e prestações com débito do imposto" - Casas decimais = 2
							   "|" + "0,00"+										 // Valor total dos ajustes a débito decorrentes do documento fiscal. - Casas decimais = 2 
							   "|" + "0,00"+										 // Valor total de "Ajustes a débito" - Casas decimais = 2
							   "|" + "0,00"+										 // Valor total de Ajustes Estornos de créditos - Casas decimais = 2
							   "|" + Util.formatNumber(vlIcmsCredito, "#.##") +   	 // Valor total dos créditos por "Entradas e aquisições com crédito do imposto" - Casas decimais = 2
							   "|" + "0,00"+										 // Valor total dos ajustes a crédito decorrentes do documento fiscal. - Casas decimais = 2
							   "|" + "0,00"+										 // Valor total de "Ajustes a crédito" - Casas decimais = 2
							   "|" + "0,00"+										 // Valor total de Ajustes Estornos de Débitos - Casas decimais = 2
							   "|" + "0,00"+										 // Valor total de "Saldo credor do período anterior" - Casas decimais = 2
							   "|" + Util.formatNumber(Util.arredondar(vlIcmsDebito, 2) - Util.arredondar(vlIcmsCredito, 2) < 0 ? 0 : Util.arredondar(vlIcmsDebito, 2) - Util.arredondar(vlIcmsCredito, 2), "#.##")+		 // Valor do saldo devedor apurado - Casas decimais = 2
							   "|" + "0,00"+										 // Valor total de "Deduções" - Casas decimais = 2
							   "|" + Util.formatNumber(Util.arredondar(vlIcmsDebito, 2) - Util.arredondar(vlIcmsCredito, 2) < 0 ? 0 : Util.arredondar(vlIcmsDebito, 2) - Util.arredondar(vlIcmsCredito, 2), "#.##") +  	 // Valor total de "ICMS a recolher (11-12) - Casas decimais = 2
							   "|" + Util.formatNumber(Util.arredondar(vlIcmsDebito, 2) - Util.arredondar(vlIcmsCredito, 2) < 0 ? Math.abs(Util.arredondar(vlIcmsDebito, 2) - Util.arredondar(vlIcmsCredito, 2)) : 0, "#.##")+										 // Valor total de "Saldo credor a transportar para o período seguinte - Casas decimais = 2
		 					   "|" + "0,00"+										 // Valores recolhidos ou a recolher, extraapuração. - Casas decimais = 2
		 					   "|" + "\r\n";
		nrLinhaBlocoE++;
		conjuntoRegistro.put("E110", (((Integer) conjuntoRegistro.get("E110")) + 1));
		
		/*
		 * Registro E116 - OBRIGAÇÕES DO ICMS RECOLHIDO OU A RECOLHER  OPERAÇÕES PRÓPRIAS
		 */
		registro += "|E116" +													 // Reg - Texto Fixo contendo E110
				 			   "|" + "000" +  	 // Código da obrigação a recolher, conforme a Tabela 5.4
							   "|" + Util.formatNumber(Util.arredondar(vlIcmsDebito, 2) - Util.arredondar(vlIcmsCredito, 2) < 0 ? 0 : Util.arredondar(vlIcmsDebito, 2) - Util.arredondar(vlIcmsCredito, 2), "#.##")+										 // Valor da obrigação a recolher 
							   "|" + Util.formatDateTime(dtSpedFinal, "ddMMyyyy")+										 // Data de vencimento da obrigação
							   "|" + "0709"+										 // Código de receita referente à obrigação, próprio da unidade da federação, conforme legislação estadual
							   "|" + 										 // Número do processo ou auto de infração ao qual a obrigação está vinculada, se houver.
							   "|" + 										 // Indicador da origem do processo: 0- SEFAZ; 1- Justiça Federal; 2- Justiça Estadual;9- Outros9- Outros
							   "|" + 										 // Descrição resumida do processo que embasou o lançamento
							   "|" + 										 // Descrição complementar das obrigações a recolher
							   "|" +Util.formatDateTime(dtSpedFinal, "MMyyyy")+ 										 // Informe o mês de referência no formato mmaaaa
							   "|" + "\r\n";
		nrLinhaBlocoE++;
		conjuntoRegistro.put("E116", (((Integer) conjuntoRegistro.get("E116")) + 1));
		
		
		while(rsmApuracaoIcms.next()){
			if(rsmApuracaoIcms.getFloat("VL_ICMS_DEBITO_ST_RETENCAO") + rsmApuracaoIcms.getFloat("VL_ICMS_DEBITO_ST_DEV_ANT") + rsmApuracaoIcms.getFloat("VL_ICMS_CREDITO_ST_DEVOLUCAO")== 0)
				continue;
			/*
			 * Registro E200 - Período de Apuração do ICMS Substituição Tributária
			 */
			registro += "|E200" + 													 // REG - Texto fixo contendo E100
								  "|" + rsmApuracaoIcms.getRegister().get("SG_ESTADO") + 
								  "|" + Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+            //Data inicial a que a apuração se refere
								  "|" + Util.formatDateTime(dtSpedFinal, "ddMMyyyy")+              //Data final a que a apuração se refere
								  "|" + "\r\n";
			nrLinhaBlocoE++;
			conjuntoRegistro.put("E200", (((Integer) conjuntoRegistro.get("E200")) + 1));
			
			/*
			 * Registro E210 - Apuração do ICMS - Operações Próprias
			 */
			registro += "|E210" +													     // Reg - Texto Fixo contendo E210
								   "|" + (rsmApuracaoIcms.getFloat("VL_ICMS_DEBITO_ST_RETENCAO") + rsmApuracaoIcms.getFloat("VL_ICMS_DEBITO_ST_DEV_ANT") + rsmApuracaoIcms.getFloat("VL_ICMS_CREDITO_ST_DEVOLUCAO")== 0 ? "0" : 1) +	
					 			   "|" + "0,00" +  	 								     
								   "|" + "0,00"+								 
								   "|" + "0,00"+										
								   "|" + Util.formatNumber(rsmApuracaoIcms.getFloat("VL_ICMS_CREDITO_ST_DEVOLUCAO"), "#.##") +										 
								   "|" + "0,00"+										 
								   "|" + Util.formatNumber(rsmApuracaoIcms.getFloat("VL_ICMS_DEBITO_ST_DEV_ANT"), "#.##")+										 
								   "|" + "0,00"+										
								   "|" + "0,00"+										
								   "|" + Util.formatNumber(rsmApuracaoIcms.getFloat("VL_ICMS_DEBITO_ST_DEV_ANT"), "#.##")+		
								   "|" + "0,00"+										
								   "|" + Util.formatNumber(rsmApuracaoIcms.getFloat("VL_ICMS_DEBITO_ST_DEV_ANT"), "#.##")+  					
								   "|" + Util.formatNumber(rsmApuracaoIcms.getFloat("VL_ICMS_CREDITO_ST_DEVOLUCAO"), "#.##") +											
			 					   "|" + "0,00"+										
			 					   "|" + "\r\n";
			nrLinhaBlocoE++;
			conjuntoRegistro.put("E210", (((Integer) conjuntoRegistro.get("E210")) + 1));
			
			/*
			 * Registro E250 - OBRIGAÇÕES DO ICMS RECOLHIDO OU A RECOLHER  OPERAÇÕES PRÓPRIAS
			 */
			
			registro += "|E250" +													 // Reg - Texto Fixo contendo E110
					 			   "|" + "001" +  	 // Código da obrigação a recolher, conforme a Tabela 5.4
					 			   "|" +Util.formatNumber(rsmApuracaoIcms.getFloat("VL_ICMS_DEBITO_ST_DEV_ANT"), "#.##")+										 // Valor da obrigação a recolher 
								   "|" + Util.formatDateTime(dtSpedFinal, "ddMMyyyy")+										 // Data de vencimento da obrigação
								   "|" + (rsmApuracaoIcms.getRegister().get("SG_ESTADO").equals("BA") ? "0709" : "3079")+										 // Código de receita referente à obrigação, próprio da unidade da federação, conforme legislação estadual
								   "|" + 										 // Número do processo ou auto de infração ao qual a obrigação está vinculada, se houver.
								   "|" + 										 // Indicador da origem do processo: 0- SEFAZ; 1- Justiça Federal; 2- Justiça Estadual;9- Outros9- Outros
								   "|" + 										 // Descrição resumida do processo que embasou o lançamento
								   "|" + 										 // Descrição complementar das obrigações a recolher
								   "|" +Util.formatDateTime(dtSpedFinal, "MMyyyy")+ 										 // Informe o mês de referência no formato mmaaaa
								   "|" + "\r\n";
			nrLinhaBlocoE++;
			conjuntoRegistro.put("E250", (((Integer) conjuntoRegistro.get("E250")) + 1));
		}
		
		
		
		
		nrLinhaBlocoE++;
		
		/*
		 * Registro E990 - Encerramento do Bloco E
		 */
		registro += 		  "|E990" +
							  "|" + nrLinhaBlocoE+
				  			  "|" + "\r\n";
		conjuntoRegistro.put("E990", (((Integer) conjuntoRegistro.get("E990")) + 1));
		
		
// ******************************** BLOCO G ***************************************************************************************************************						
		
		/*
		 * Abertura do Bloco G - Registro G001
		 */
		registro  += "|G001" + 													 // REG - Texto fixo contendo E001
				  			  "|" + IND_MOV_SEM_DADOS+							 // IND_MOV - indicador de movimento
				  			  "|" + "\r\n";
		nrLinhaBlocoG++;
		conjuntoRegistro.put("G001", (((Integer) conjuntoRegistro.get("G001")) + 1));
		
		nrLinhaBlocoG++;
		
		/*
		 * Registro G990 - ENCERRAMENTO DO BLOCO G
		 */
		registro          +=  "|G990" +
							 "|" + nrLinhaBlocoG+
							 "|" + "\r\n";
		conjuntoRegistro.put("G990", (((Integer) conjuntoRegistro.get("G990")) + 1));
		
// ******************************** BLOCO H ***************************************************************************************************************						
		
		/*
		 * Abertura do Bloco H - Registro H001
		 */
		registro  += "|H001" + 													 // REG - Texto fixo contendo E001
				  			  "|" + IND_MOV_DADOS+							 // IND_MOV - indicador de movimento
				  			  "|" + "\r\n";
		nrLinhaBlocoH++;
		conjuntoRegistro.put("H001", (((Integer) conjuntoRegistro.get("H001")) + 1));
		
		/*
		 * Registro H005 - Totais do Inventário
		 */
		registro 			+= "|H005" + 													 // REG - Texto fixo contendo G130
							  "|" + Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+            // Data do inventário
							  "|" + Util.fillNum(0, 10)+									     // Valor total do estoque - Casas decimais = 2			  
							  "|" + "02"+									 // Informe o motivo do Inventário
							  "|" + "\r\n";
		nrLinhaBlocoH++;
		conjuntoRegistro.put("H005", (((Integer) conjuntoRegistro.get("H005")) + 1));
		
		nrLinhaBlocoH++;
		/*
		 * Registro H990 - ENCERRAMENTO DO BLOCO H
		 */
		registro          +=  "|H990" +
							 "|" + nrLinhaBlocoH+
							 "|" + "\r\n";
		
		conjuntoRegistro.put("H990", (((Integer) conjuntoRegistro.get("H990")) + 1));
		
		
		
//		/*
//		 * Abertura do Bloco G - Registro G001
//		 */
//		String registroG001 = "G001" + 													 // REG - Texto fixo contendo G001
//							  Util.fillNum(IND_MOV_DADOS, 1)+							 // IND_MOV - indicador de movimento
//							  "\r\n";
//		/*
//		 * Registro G110 - ICMS - Ativo Permanente - CIAP
//		 */
//		String registroG110 = "G110" + 													 // REG - Texto fixo contendo G110
//							  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+            //Data inicial a que a apuração se refere
//							  Util.formatDateTime(dtSpedFinal, "ddMMyyyy")+              //Data final a que a apuração se refere
//							  Util.fillNum(0, 10)+										 // Saldo inicial de ICMS do CIAP - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Somatório das parcelas de ICMS passível de apropriação de cada bem (campo 10 do G125) - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Valor do somatório das saídas tributadas e saídas para exportação - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Valor total de saídas - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Índice de participação do valor do somatório das saídas tributadas e saídas para exportação no valor total de saídas - Casas decimais = 8
//							  Util.fillNum(0, 10)+										 // Valor de ICMS a ser apropriado na apuração do ICMS, correspondente á multiplicação do campo 05 pelo campo 08 - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Valor de outros créditos a ser apropriado na Apuração do ICMS, correspondente ao somatório do campo 09 do registro G126. - Casas decimais = 2
//							  "\r\n";
//		/*
//		 * Registro G125 - Movimentação de Bem do Ativo Imobilizado
//		 */
//		String registroG125 = "G125" + 													 // REG - Texto fixo contendo G125
//							  Util.fillAlpha("", 60)+									 // Código individualizado do bem ou componente adotado no controle patrimonial do estabelecimento informante
//							  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+            // Data da movimentação ou do saldo inicial
//							  Util.fillAlpha("", 2)+									 // Tipo de movimentação do bem ou componente
//							  Util.fillNum(0, 10)+										 // Valor do ICMS da Operação Própria na entrada do bem ou componente - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Valor do ICMS da Oper. por Sub. Tributária na entrada do bem ou componente - Casas decimais = 8
//							  Util.fillNum(0, 10)+										 // Valor do ICMS sobre Frete do Conhecimento de Transporte na entrada do bem ou componente - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Valor do ICMS - Diferencial de Alíquota, conforme Doc. de Arrecadação, na entrada do bem ou componente - Casas decimais = 2
//							  Util.fillNum(0, 3)+										 // Número da parcela do ICMS
//							  Util.fillNum(0, 10)+										 // Valor da parcela de ICMS passível de apropriação (antes da aplicação da participação percentual do valor das saídas tributadas/exportação sobre as saídas totais) - Casas decimais = 2
//							  "\r\n";
//		
//		/*
//		 * Registro G126 - Outros créditos CIAP
//		 */
//		String registroG126 = "G126" + 													 // REG - Texto fixo contendo G126
//				  			  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+            // Data inicial do período de apuração
//				  			  Util.formatDateTime(dtSpedFinal, "ddMMyyyy")+              // Data final do período de apuração
//							  Util.fillNum(0, 3)+									     // Número da parcela do ICMS
//							  Util.fillNum(0, 10)+										 // Valor da parcela de ICMS passível de apropriação - antes da aplicação da participação percentual do valor das saídas tributadas/exportação sobre as saídas totais - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Valor do somatório das saídas tributadas e saídas para exportação no período indicado neste registro - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Valor total de saídas no período indicado neste registro - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Índice de participação do valor do somatório das saídas tributadas e saídas para exportação no valor total de saídas - Casas decimais = 8
//							  Util.fillNum(0, 10)+										 // Valor de outros créditos de ICMS a ser apropriado na apuração (campo 05 vezes o campo 08) - Casas decimais = 2
//							  "\r\n";
//		
//		/*
//		 * Registro G130 - Identificação do documento fiscal
//		 */
//		String registroG130 = "G130" + 													 // REG - Texto fixo contendo G130
//							  Util.fillAlpha("", 1)+									 // Indicador do emitente do documento fiscal
//							  Util.fillAlpha("", 60)+									 // Código do participante :			  
//							  Util.fillAlpha("", 2)+									 // Código do modelo de documento fiscal
//							  Util.fillAlpha("", 3)+									 // Série do documento fiscal
//							  Util.fillNum(0, 9)+										 // Número de documento fiscal
//							  Util.fillNum(0, 44)+										 // Chave do documento fiscal eletrônico
//							  Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+            // Data da emissão do documento fiscal
//							  "\r\n";
//		/*
//		 * Registro G140 - Identificação do item do documento fiscal
//		 */
//		String registroG140 = "G140" + 													 // REG - Texto fixo contendo G140
//							  Util.fillNum(0, 9)+										 // Número sequencial do item no documento fiscal
//							  Util.fillAlpha("", 60)+									 // Código correspondente do bem no documento fiscal (campo 02 do registro 0200)
//							  "\r\n";
//		
//		/*
//		 * Registro G990 - Encerramento do Bloco G
//		 */
//		String registroG990 = "G990" +
//				  			  nrLinhaBlocoG+
//				  			  "\r\n";
//		
//		
//		/*
//		 * Abertura de Bloco H - Registro H001
//		 */
//		String registroH001 = "H001" + 													 // REG - Texto fixo contendo H001
//	  			  			  Util.fillNum(IND_MOV_DADOS, 1)+							 // IND_MOV - indicador de movimento
//	  			  			  "\r\n";
//		
		
//		
//		/*
//		 * Registro H010 - Inventário
//		 */
//		String registroH010 = "H010" + 													 // REG - Texto fixo contendo H010
//							  Util.fillAlpha("", 60)+									 // Código do item
//							  Util.fillAlpha("", 6)+									 // Unidade do item		  
//							  Util.fillNum(0, 10)+										 // Quantidade do item - Casas decimais = 3
//							  Util.fillNum(0, 10)+										 // Quantidade do item - Casas decimais = 6
//							  Util.fillNum(0, 10)+										 // Quantidade do item - Casas decimais = 2
//							  Util.fillAlpha("", 1)+									 // Indicador de propriedade/posse do item
//							  Util.fillAlpha("", 60)+									 // Código do participante (campo 02 do Registro 0150)
//							  Util.fillAlpha("", 100)+									 // Descrição complementar.
//							  Util.fillAlpha("", 100)+									 // Código da conta analítica contábil debitada/creditada
//							  "\r\n";
//		
//		/*
//		 * Registro H020 - Informação complementar do Inventário
//		 */
//		String registroH020 = "H020" + 													 // REG - Texto fixo contendo H020  
//							  Util.fillNum(0, 3)+										 // Código da Situação Tributária referente ao ICMS
//							  Util.fillNum(0, 10)+										 // Informe a base de cálculo do ICMS - Casas decimais = 2
//							  Util.fillNum(0, 10)+										 // Informe o valor do ICMS a ser debitado ou creditado - Casas decimais = 2
//							  "\r\n";
//		
//		/*
//		 * Registro H990 - Encerramento do Bloco H
//		 */
//		String registroH990 = "H990" +
//	  			  			  nrLinhaBlocoH+
//	  			              "\r\n";
		
// ******************************** BLOCO K ***************************************************************************************************************						

		/*
		 * Abertura do Bloco K - Registro K001
		 */
		registro  += "|K001" + 													 // REG - Texto fixo contendo E001
				  			  "|" + IND_MOV_SEM_DADOS+							 // IND_MOV - indicador de movimento
				  			  "|" + "\r\n";
		nrLinhaBlocoK++;
		conjuntoRegistro.put("K001", (((Integer) conjuntoRegistro.get("K001")) + 1));
		
		
		nrLinhaBlocoK++;
		
		/*
		 * Registro H990 - ENCERRAMENTO DO BLOCO H
		 */
		registro          +=  "|K990" +
							 "|" + nrLinhaBlocoK+
							 "|" + "\r\n";
		
		conjuntoRegistro.put("K990", (((Integer) conjuntoRegistro.get("K990")) + 1));		

		
		
// ******************************** BLOCO 1 ***************************************************************************************************************						
		
		/*
		 * Abertura Bloco 1 - Registro 1001
		 */
		//Movimentacao Diaria de Combustiveis		
		ResultSetMap rsmBicoHistorico = getMovimentacaoDiariaCombustivel(cdEmpresa, dtSpedInicial, dtSpedFinal);
		
		ResultSetMap rsmBombas = BombasDAO.getAll();	
		
		ResultSetMap rsmCartoes = getValorTotalCartaoNoPeriodo(cdEmpresa, dtSpedInicial, dtSpedFinal);
		
		registro += "|1001" + 													 // REG - Texto fixo contendo 1001
	  			  			  "|" + IND_MOV_DADOS +							 // IND_MOV - indicador de movimento
	  			  			  "|" + "\r\n";
		
		nrLinhaBloco1++;
		conjuntoRegistro.put("1001", (((Integer) conjuntoRegistro.get("1001")) + 1));
		
		/*
		 * Registro 1010
		 */
		registro += "|1010" + 													 // REG - Texto fixo contendo 1010
	  			  			  "|" +"N"+							 				// IND_EXP - Ocorreu averbação de exportação (conclusão) no periodo?
	  			  			  "|" +"N"+							 				// IND_CCRF - Existem informações acerca de créditos de ICMS a serem controlados, definidos pela SEFAZ?
	  			  			  "|" +((getHas1300(cdEmpresa, dtSpedInicial, dtSpedFinal)) ? "S" : "N")+// REG 1300/IND_COMB - É comercio varejista de combustivel? // Teste para saber se é posto de gasolina
	  			  			  "|" +"N"+							 				// IND_USINA - Usina de acucar/alcool - O estabelecimento eh produtor de acucar e/ou alcool carburante?
	  			  			  "|" +"N"+							 				// IND_VA   - Existem informações a serem prestadas nesse registro e o registro é obrigatório em sua federecao - PESQUISAR PARA SABER SE NO ESTADO DO ESTABELECIMENTO (BA) é obrigatorio
				  			  "|" +"N"+							 				// IND_EE - A empresa é distribuidora de energia e ocorreu fornecimento de energia eletrica para consumidores em outros estados
				  			  "|" +((getHas1600(cdEmpresa, dtSpedInicial, dtSpedFinal)) ? "S" : "N")+// REG 1600/IND_CART Realizou vendas com cartao de credito ou de debito
					  		  "|" +"N"+							 				// REG 1700/IND_FORM - É obrigatorio em sua unidade de federeção o controle de utilização de documentos fiscais em papel? (PESQUISAR BA)
					  		  "|" +"N"+							 				// IND_AER - A empresa prestou servicos de transporte aereo de carga e passageiros?
	  			  			  "|" + "\r\n";
		nrLinhaBloco1++;
		conjuntoRegistro.put("1010", (((Integer) conjuntoRegistro.get("1010")) + 1));
		
		
		//Fazer registro 1300 ate 1370 para Postos de Combustivel
		/**
		 * Registro 1300
		 * 
		 * */
		
		
		
		
		
		
		
//		//CFOP
//		if(rsmProdutos.getString("nr_codigo_fiscal") == null || rsmProdutos.getString("nr_codigo_fiscal").trim().equals(""))
//	    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CFOP ";
//		//Tributação
////		if(!ProdutoServicoServices.getAllTributos(rsmProdutos.getInt("cd_produto_servico")).next())
////	    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Tributação do produto: " + rsmProdutos.getString("nm_produto_servico") + " - Documento de Saída Nº: " + documento.getNrDocumentoSaida();
//	    if(!dsValidacao.equals("")){
//	    	HashMap<String, Object> register = new HashMap<String, Object>();
//	    	register.put("ERRO", "Dados dos Itens do documento de saída Nº "+documento.getNrDocumentoSaida()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" faltando: "+dsValidacao);
//	    	rsm.addRegister(register);
//	    
//	    	dsValidacao = "";
//	    }
//		
		
		if(getHas1300(cdEmpresa, dtSpedInicial, dtSpedFinal)){
		
			//Movimentacao Diaria de Combustiveis		
			rsmBicoHistorico = getMovimentacaoDiariaCombustivel(cdEmpresa, dtSpedInicial, dtSpedFinal);
			while(rsmBicoHistorico.next()){
				float qtEstoqueInicial 	   = Float.parseFloat(Util.formatNumber(rsmBicoHistorico.getFloat("qt_estoque_inicial"), "#.###").replaceAll(",", "."));
				float qtEstoqueInicialTemp = Float.parseFloat(Util.formatNumber((qtEstoqueInicial * 1000), "#").replaceAll(",", "."));
				float qtEntradas       	   = Float.parseFloat(Util.formatNumber(rsmBicoHistorico.getFloat("qt_entradas"), "#.###").replaceAll(",", "."));
				float qtEntradasTemp 	   = Float.parseFloat(Util.formatNumber((qtEntradas * 1000), "#").replaceAll(",", "."));
				float qtSaidas             = Float.parseFloat(Util.formatNumber(rsmBicoHistorico.getFloat("qt_saidas"), "#.###").replaceAll(",", "."));
				float qtSaidasTemp 		   = Float.parseFloat(Util.formatNumber((qtSaidas * 1000), "#").replaceAll(",", "."));
				float qtDisponivel 		   = (qtEstoqueInicialTemp + qtEntradasTemp) / 1000;
				qtDisponivel			   = Float.parseFloat(Util.formatNumber(qtDisponivel, "#.###").replaceAll(",", "."));
				float qtEscritural 		   = (qtEstoqueInicialTemp + qtEntradasTemp - qtSaidasTemp) / 1000;
				qtEscritural			   = Float.parseFloat(Util.formatNumber(qtEscritural, "#.###").replaceAll(",", "."));
				
				if(qtEstoqueInicial < 0)
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Estoque Inicial ";
				if(qtDisponivel < 0)
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Estoque Disponível ";
				if(qtEscritural < 0)
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Estoque Escritural ";
				if(rsmBicoHistorico.getFloat("qt_fechamento") < 0)
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Estoque de Fechamento ";
				
				if(!dsValidacao.equals("")){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	ProdutoServico produto = ProdutoServicoDAO.get(rsmBicoHistorico.getInt("cd_produto_servico"));
			    	register.put("ERRO", "Dados de movimentação diária de combustível negativos para o produto o "+produto.getNmProdutoServico()+" na data " +
			    			     Util.formatDateTime(Util.convTimestampToCalendar(rsmBicoHistorico.getTimestamp("dt_fechamento")), "dd/MM/yyyy")+": "+dsValidacao);
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	
			    	//entrouDs = true;
			    }
				
				
				if(!entrouDs){
					registro += "|1300" +					//REG - Texto fixo contendo 1300
								"|"     + (rsmBicoHistorico.getString("cd_produto_servico")) +      //COD_ITEM     - Código do Produto, constante no registro 0200
								"|"     + Util.formatDateTime(Util.convTimestampToCalendar(rsmBicoHistorico.getTimestamp("dt_fechamento")), "ddMMyyyy")+	                  //DT_FECH      - Data de Fechamento da Movimentação
								"|"     + Util.formatNumber(qtEstoqueInicial, "#.###") +                  //ESTQ_ABERT   - Estoque no inicio do dia, em litros
								"|"     + Util.formatNumber(qtEntradas, "#.###") +                  //VOL_ENTR     - Volume recebido no dia
								"|"     + Util.formatNumber(qtDisponivel, "#.###") +                  //VOL_DISP     - Volume disponível (Campo 04 + Campo 05), em litros
								"|"     + Util.formatNumber(qtSaidas, "#.###") +                  //VOL_SAIDAS   - Volume total das saídas, em litros - Saber se pega isso em adm_conta_fechamento, por pcb_bico_encerrante
								"|"     + Util.formatNumber(qtEscritural, "#.###") +                  //ESTQ_ESCR    - Estoque Escriturial (Campo 06 - Campo 07), em litros
								"|"     + Util.formatNumber(rsmBicoHistorico.getFloat("qt_perdas"), "#.###") +                  //VAL_AJ_PERDA - Valor da perda, em litros - Saber se há registro de perda
								"|"     + Util.formatNumber(rsmBicoHistorico.getFloat("qt_ganhos"), "#.###") +                  //VAL_AJ_GANHO - Valor da ganho, em litros- Saber se há registro de ganho
								"|"     + Util.formatNumber((rsmBicoHistorico.getFloat("qt_fechamento")), "#.###") +        //FECH_FISICO  - Estoque do fechamento, em litros
								"|" + "\r\n";
								
							
							
				        nrLinhaBloco1++;
						conjuntoRegistro.put("1300", (((Integer) conjuntoRegistro.get("1300")) + 1));
				}
				//Movimentacao Diaria de Combustiveis por Tanque	
				ResultSetMap  rsmBicoHistoricoTanque = getMovimentacaoDiariaCombustivelPorTanque(cdEmpresa, Util.convTimestampToCalendar(rsmBicoHistorico.getTimestamp("dt_fechamento")), rsmBicoHistorico.getInt("cd_produto_servico"));
				while(rsmBicoHistoricoTanque.next()){
					qtEstoqueInicial 	   = Float.parseFloat(Util.formatNumber(rsmBicoHistorico.getFloat("qt_estoque_inicial"), "#.###").replaceAll(",", "."));
					qtEstoqueInicialTemp   = Float.parseFloat(Util.formatNumber((qtEstoqueInicial * 1000), "#").replaceAll(",", "."));
					qtEntradas       	   = Float.parseFloat(Util.formatNumber(rsmBicoHistorico.getFloat("qt_entradas"), "#.###").replaceAll(",", "."));
					qtEntradasTemp 	       = Float.parseFloat(Util.formatNumber((qtEntradas * 1000), "#").replaceAll(",", "."));
					qtSaidas               = Float.parseFloat(Util.formatNumber(rsmBicoHistorico.getFloat("qt_saidas"), "#.###").replaceAll(",", "."));
					qtSaidasTemp 		   = Float.parseFloat(Util.formatNumber((qtSaidas * 1000), "#").replaceAll(",", "."));
					qtDisponivel 		   = (qtEstoqueInicialTemp + qtEntradasTemp) / 1000;
					qtDisponivel		   = Float.parseFloat(Util.formatNumber(qtDisponivel, "#.###").replaceAll(",", "."));
					qtEscritural 		   = (qtEstoqueInicialTemp + qtEntradasTemp - qtSaidasTemp) / 1000;
					qtEscritural		   = Float.parseFloat(Util.formatNumber(qtEscritural, "#.###").replaceAll(",", "."));
					
					 if(qtEstoqueInicial < 0)
						dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Estoque Inicial ";
					 if(qtDisponivel < 0)
						dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Estoque Disponível ";
					 if(qtEscritural < 0)
						dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Estoque Escritural ";
					 if(rsmBicoHistoricoTanque.getFloat("qt_fechamento") < 0)
						dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Estoque de Fechamento ";
						
					 if(!dsValidacao.equals("")){
						Tanque tanque = TanqueDAO.get(rsmBicoHistoricoTanque.getInt("cd_tanque")); 
				    	HashMap<String, Object> register = new HashMap<String, Object>();
				    	ProdutoServico produto = ProdutoServicoDAO.get(rsmBicoHistorico.getInt("cd_produto_servico"));
				    	register.put("ERRO", "Dados de movimentação diária de combustível para tanque negativos para o produto o "+produto.getNmProdutoServico()+", no tanque " +
				    			     tanque.getNmLocalArmazenamento() + ", na data " + Util.formatDateTime(Util.convTimestampToCalendar(rsmBicoHistorico.getTimestamp("dt_fechamento")), "dd/MM/yyyy")+": "+dsValidacao);
				    	rsm.addRegister(register);
				    
				    	dsValidacao = "";
				    	
				    	//entrouDs = true;
				     }
					
					 if(!entrouDs){
						 registro += "|1310" +					//REG - Texto fixo contendo 1310
								"|"     + rsmBicoHistoricoTanque.getInt("cd_tanque") +                  //NUM_TANQUE   - Número do tanque que armazena o combustivel
								"|"     + Util.formatNumber(qtEstoqueInicial, "#.###") +                    //ESTQ_ABERT   - Estoque no inicio do dia, em litros
								"|"     + Util.formatNumber(qtEntradas, "#.###") +                       //VOL_ENTR     - Volume recebido no dia
								"|"     + Util.formatNumber(qtDisponivel, "#.###") +                   //VOL_DISP     - Volume disponível (Campo 04 + Campo 05), em litros
								"|"     + Util.formatNumber(qtSaidas,"#.###") +                     //VOL_SAIDAS   - Volume total das saídas, em litros
								"|"     + Util.formatNumber(qtEscritural, "#.###") +                  //ESTQ_ESCR    - Estoque Escriturial (Campo 06 - Campo 07), em litros
								"|"     + Util.formatNumber(rsmBicoHistoricoTanque.getFloat("qt_perdas"), "#.###") +                  //VAL_AJ_PERDA - Valor da perda, em litros - Saber se há registro de perda
								"|"     + Util.formatNumber(rsmBicoHistoricoTanque.getFloat("qt_ganhos"), "#.###") +                  //VAL_AJ_GANHO - Valor da ganho, em litros
								"|"     + Util.formatNumber(rsmBicoHistoricoTanque.getFloat("qt_fechamento"), "#.###") +                  //FECH_FISICO  - Estoque do fechaento, em litros
								"|" + "\r\n";
							
					        nrLinhaBloco1++;
							conjuntoRegistro.put("1310", (((Integer) conjuntoRegistro.get("1310")) + 1));
							
							//entrouDs = false;
					 }
					//Volume de Vendas
					ResultSetMap rsmBicoHistoricoBico = getMovimentacaoDiariaCombustivelPorBico(cdEmpresa, Util.convTimestampToCalendar(rsmBicoHistorico.getTimestamp("dt_fechamento")), rsmBicoHistoricoTanque.getInt("cd_tanque"));	
							
					
					while(rsmBicoHistoricoBico.next()){		
						float vlEncerranteFinal = rsmBicoHistoricoBico.getFloat("qt_encerrante_final");
						float vlEncerranteInicial = rsmBicoHistoricoBico.getFloat("qt_encerrante_inicial");
						float vlAfericao = rsmBicoHistoricoBico.getFloat("vl_afericao");
						float vlVolVendas = vlEncerranteFinal - vlEncerranteInicial - vlAfericao;
						
						if(vlEncerranteFinal < 0)
							dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Encerrante Final ";
						 if(vlEncerranteInicial < 0)
							dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Encerrante Inicial ";
						 if(vlAfericao < 0)
							dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Aferição ";
						 if(vlVolVendas < 0)
							dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Volume de Vendas ";
						
						 if(!dsValidacao.equals("")){
							Tanque tanque = TanqueDAO.get(rsmBicoHistoricoTanque.getInt("cd_tanque")); 
							Bico bico     = BicoDAO.get(rsmBicoHistoricoBico.getInt("cd_bico"));
					    	HashMap<String, Object> register = new HashMap<String, Object>();
					    	ProdutoServico produto = ProdutoServicoDAO.get(rsmBicoHistorico.getInt("cd_produto_servico"));
					    	register.put("ERRO", "Dados de movimentação diária de combustível para tanque negativos para o produto o "+produto.getNmProdutoServico()+", no tanque" +
					    			     tanque.getNmLocalArmazenamento() + ", no bico " + bico.getIdBico() + ", na data " + 
					    			     Util.formatDateTime(Util.convTimestampToCalendar(rsmBicoHistorico.getTimestamp("dt_fechamento")), "dd/MM/yyyy")+": "+dsValidacao);
					    	rsm.addRegister(register);
					    
					    	dsValidacao = "";
					    	
					    	//entrouDs = true;
					     }
						
						 if(!entrouDs){
							registro += "|1320" +					//REG - Texto fixo contendo 1320
									"|"     + rsmBicoHistoricoBico.getInt("cd_bico") +                  //NUM_BICO   - Bico Ligado a Bomba
									"|"     +                   //NR_INTERV   - Numero da intervenção
									"|"     +                   //MOT_INTERV   - Motivo da intervenção
									"|"     +                   //NOM_INTERV     - Nome do Interventor
									"|"     +                   //CNPJ_INTERV   - CNPJ do Interventor
									"|"     +                   //CPF_INTERV    - CPF do Interventor
									"|"     + Util.formatNumber(vlEncerranteFinal, "#.###") +                  //VAL_FECHA - Valor da leitura final do contador, no fechamento do bico
									"|"     + Util.formatNumber(vlEncerranteInicial, "#.###") +                  //VAL_ABERT - Valor da leitura inicial do contador, na abertura do bico
									"|"     + Util.formatNumber(vlAfericao, "#.###") +                  //VAL_AFERI  - Aferições da Bomba, em litros
									"|"     + Util.formatNumber(vlVolVendas, "#.###") +                  //VOL_VENDAS  - Vendas (Campo 08 - Campo 09 - Campo 10) do bico, em litros
									"|" + "\r\n";
								
						        nrLinhaBloco1++;
								conjuntoRegistro.put("1320", (((Integer) conjuntoRegistro.get("1320")) + 1));
								
								//entrouDs = false;
						 }
					}
				}
			}
			
			rsmBombas = BombasDAO.getAll();	
			rsmBombas.beforeFirst();
			while(rsmBombas.next()){
				
				Pessoa pessoa = PessoaDAO.get(rsmBombas.getInt("cd_fabricante"));
				
				if(rsmBombas.getInt("cd_fabricante") <= 0){
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("ERRO", "Faltando o fabricante da bomba de Ordem " + rsmBombas.getString("nr_ordem") + ", modelo " + rsmBombas.getString("nm_modelo"));
					rsm.addRegister(register);
				}
				
				//Bombas	
				registro += "|1350" +					//REG - Texto fixo contendo 1350
						"|"+ rsmBombas.getString("nr_ordem")+                   //SERIE   - Numero de Série da Bomba
						"|"+ (pessoa != null ? pessoa.getNmPessoa() : null) +                   //FABRICANTE - Nome do Fabricante da Bomba
						"|"+ rsmBombas.getString("nm_modelo")+                   //MODELO      - Modelo da Bomba
						"|"+ "0" +                   //TIPO_MEDICAO   - Identificador de Medição (0 - analogico, 1 - digital) - VERIFICAR
						"|" + "\r\n";
					
			        nrLinhaBloco1++;
					conjuntoRegistro.put("1350", (((Integer) conjuntoRegistro.get("1350")) + 1));
				
				ResultSetMap rsmLacres = BombasServices.getAllLacres(rsmBombas.getInt("cd_bomba"));
				if(rsmLacres.size() == 0){
					HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados da Bomba de número "+rsmBombas.getString("nr_ordem")+": Sem registro de Lacre");
			    	rsm.addRegister(register);
				}
				while(rsmLacres.next()){
				
					GregorianCalendar data = new GregorianCalendar();
				    data.set(Calendar.YEAR, 2000);
				    data.set(Calendar.MONTH, 0);
				    data.set(Calendar.DAY_OF_MONTH, 1);
				    
					if(rsmLacres.getGregorianCalendar("dt_registro") == null || dtSpedFinal.before(rsmLacres.getGregorianCalendar("dt_registro")) || rsmLacres.getGregorianCalendar("dt_registro").before(data)){
						HashMap<String, Object> register = new HashMap<String, Object>();
				    	register.put("ERRO", "Dados do Lacre de número "+rsmLacres.getString("nr_lacre")+": Data de Registro deve ser menor que a data final do SPED e maior do que 01/01/2000");
				    	rsm.addRegister(register);
				    
				    	//entrouDs = true;
				     }
					
					if(!entrouDs){
						//Lacres da Bomba	
						registro += "|1360" +					//REG - Texto fixo contendo 1360
								"|" +rsmLacres.getString("nr_lacre")+                    //NUM_LACRE   - Número do Lacre associado na Bomba
								"|" +Util.formatDateTime(rsmLacres.getGregorianCalendar("dt_registro"), "ddMMyyyy")+                     //DT_APLICACAO - Data de Aplicação do Lacre
								"|" + "\r\n";
							
					        nrLinhaBloco1++;
							conjuntoRegistro.put("1360", (((Integer) conjuntoRegistro.get("1360")) + 1));
							
							//entrouDs = false;
					}
				}
				ResultSetMap rsmBicos = BicoServices.getByBomba(rsmBombas.getInt("cd_bomba"));	
				while(rsmBicos.next()){
					//Bicos da Bomba
					
					Tanque tanque = TanqueDAO.get(rsmBicos.getInt("cd_tanque"));
					ProdutoServico produto = ProdutoServicoDAO.get(tanque.getCdProdutoServico());
					
					registro += "|1370" +					//REG - Texto fixo contendo 1300
							"|"+rsmBicos.getString("id_bico")+                   //NUM_BICO   - Número Sequencial do Bico Ligado a Bomba
							"|"+(produto.getCdProdutoServico())+                   //COD_ITEM - Código do Produto, constante no registro 0200
							"|"+ tanque.getCdTanque()+                   //NUM_TANQUE      - Tanque que armazena o combustivel
							"|" + "\r\n";
						
				        nrLinhaBloco1++;
						conjuntoRegistro.put("1370", (((Integer) conjuntoRegistro.get("1370")) + 1));
				}
			}
		}
		
		
		/**
		 * Registro 1600
		 * 
		 * */
		rsmCartoes = getValorTotalCartaoNoPeriodo(cdEmpresa, dtSpedInicial, dtSpedFinal);
		
		while(rsmCartoes.next()){
			
			if(rsmCartoes.getString("vl_credito") != null && rsmCartoes.getString("vl_debito") != null ){
				registro += "|1600" +					//REG - Texto fixo contendo 1600
							"|" + rsmCartoes.getInt("cd_pessoa") +                      //COD_PART Código da administradora do cartão de crédito (Registro 0150) - Incluir a empresa administradora do cartão de credito ou debito nos dados
							"|" + (Util.formatNumber(rsmCartoes.getFloat("vl_credito"), "#.##")) +	//TOT_CREDITO Valor total das operações realizadas com cartão de crédito
							"|" + (Util.formatNumber(rsmCartoes.getFloat("vl_debito"), "#.##")) +						//TOT_DEBITO Valor total das operações realizadas com cartão de débito
							"|" + "\r\n";
				nrLinhaBloco1++;
				conjuntoRegistro.put("1600", (((Integer) conjuntoRegistro.get("1600")) + 1));
			}
		}
		
		/**
		 * Registro 1700 - COMEÇAR - Contribuintes da Bahia não devem apresentar esse registro
		 * 
		 * */
		
		
		
		
		
		
		
		nrLinhaBloco1++;
		/*
		 * Registro 1990 - Encerramento do Bloco 1
		 */
		
		registro         +=  "|1990" +
							"|" +  nrLinhaBloco1+
							"|" + "\r\n";
		
		conjuntoRegistro.put("1990", (((Integer) conjuntoRegistro.get("1990")) + 1));
		
// ******************************** BLOCO 9 ***************************************************************************************************************				
		
		/*
		 * Abertura Bloco 9 - Registro 9001
		 */
		registro        += "|9001" + 													 // REG - Texto fixo contendo 9001
	  			  			  "|" + IND_MOV_DADOS+							 // IND_MOV - indicador de movimento
	  			  			  "|" + "\r\n";
		nrLinhaBloco9++;
		conjuntoRegistro.put("9001", (((Integer) conjuntoRegistro.get("9001")) + 1));
		
		
		int quant9900 = 3;//Inicia de 3 pois depois dos registros que vêm antes de 9900 há mais 3 registros
		for(String key : conjuntoRegistro.keySet()){
			
//			if(key.equals("C495")){
//				if(!hasC425 && tpPerfilEmpresa.equals(IND_PERFIL_B)){
//					/*
//					 * Registro 9900 - Registros do Arquivo
//					 */
//					registro            += "|9900" + 													 // REG - Texto fixo contendo 9900 
//											  "|" + key+									 // Registro que será totalizado no próximo campo.
//											  "|" + conjuntoRegistro.get(key)+										 // Total de registros do tipo informado no campo anterior. 
//											  "|" + "\r\n";
//					nrLinhaBloco9++;
//					quant9900++;
//				}
//			}
//			else{
				/*
				 * Registro 9900 - Registros do Arquivo
				 */
				registro            += "|9900" + 													 // REG - Texto fixo contendo 9900 
										  "|" + key+									 // Registro que será totalizado no próximo campo.
										  "|" + conjuntoRegistro.get(key)+										 // Total de registros do tipo informado no campo anterior. 
										  "|" + "\r\n";
				nrLinhaBloco9++;
				quant9900++;
//			}
			
		}
		
		//Feito para contar quantos registros o registro 9900 tem no arquivo
		/*
		 * Registro 9900 - Registros do Arquivo
		 */
		registro            += "|9900" + 													 // REG - Texto fixo contendo 9900 
								  "|" + "9900"+									 // Registro que será totalizado no próximo campo.
								  "|" + quant9900+										 // Total de registros do tipo informado no campo anterior. 
								  "|" + "\r\n";
		nrLinhaBloco9++;
		
		/*
		 * Registro 9900 - Registros do Arquivo
		 */
		registro            += "|9900" + 													 // REG - Texto fixo contendo 9900 
								  "|" + "9990"+									 // Registro que será totalizado no próximo campo.
								  "|" + 1+										 // Total de registros do tipo informado no campo anterior. 
								  "|" + "\r\n";
		nrLinhaBloco9++;
		
		/*
		 * Registro 9900 - Registros do Arquivo
		 */
		registro            += "|9900" + 													 // REG - Texto fixo contendo 9900 
								  "|" + "9999"+									 // Registro que será totalizado no próximo campo.
								  "|" + 1+										 // Total de registros do tipo informado no campo anterior. 
								  "|" + "\r\n";
		nrLinhaBloco9++;
		
		
		/*
		 * Registro 9990 - Encerramento do Bloco 9
		 */
		nrLinhaBloco9++;
		nrLinhaBloco9++;
		registro           += "|9990" +
							  "|" + nrLinhaBloco9+
	  			  			  "|" + "\r\n";
		
		/*
		 * Registro 9999 - Encerramento do Arquivo Digital
		 */
		nrTotalLinha = nrLinhaBloco0 +  nrLinhaBlocoC +  nrLinhaBlocoE +  nrLinhaBlocoG +  nrLinhaBlocoH +  nrLinhaBlocoK +  nrLinhaBloco1 +  nrLinhaBloco9 + 2;
		
		registro          += "|9999" +
							  "|" + nrTotalLinha+
	  			  			  "|" + "\r\n";
		try{
			//
			FileOutputStream gravar;
			if ((nmPathArq == null) || (nmPathArq.equals(""))) {
				File arquivo = new File("teste.txt"); 
				//
				File diretorio = new File("C:\\TESTE");
				if(!diretorio.exists()){
					diretorio.mkdir();
				}
				//
				gravar = new FileOutputStream("C:\\TESTE\\" + arquivo);
				gravar.write(registro.getBytes());
				gravar.close();
			}else{
				gravar = new FileOutputStream(nmPathArq);
				gravar.write(registro.getBytes());
				gravar.close();
			}
		}
		catch(Exception e){System.out.println("ERRO na gravacao: " + e);}
		String file = "";
		/*
		String file = 0000+0001+0005+0100+0150+0190+0200+0206+0400+0450+0460+0500+0600+
					  0990+C001+C100+C105+C110+C111+C112+C113+C114+C115+C116+
					  C120+C130+C140+C141+C160+C165+C170+C171+C172+C176+C177+
					  C178+C179+C190+C195+C197+C300+C310+C320+C321+C350+C370+
					  C390+C400+C405+C410+C420+C425+C460+C470+C490+C495+C500+
					  C510+C590+C600+C601+C610+C690+C700+C790+C791+C800+C850+
					  C860+C890+C990;
					 */
		if(rsm.getLines().size()!=0){
			resultado.setCode(-1);
			resultado.setMessage("SPED contém Erros!");
			resultado.addObject("ERRO", rsm);
			resultado.addObject("AVISO", rsm2);
			return resultado;
			
		}
		else if(rsm2.getLines().size()!=0){
			session.setAttribute("TXT_SPED", registro);
			resultado.setCode(1);
			resultado.addObject("ERRO", rsm);
			resultado.addObject("AVISO", rsm2);
			return resultado;
		}
		else{
			session.setAttribute("TXT_SPED", registro);
			return resultado;
		
		}
		
	}
	
	
	public static String gerarSpedContribuicoes(int cdEmpresa, int nrMes, int nrAno)	{
		return gerarSpedContribuicoesResultado(cdEmpresa, nrMes, nrAno).getMessage();
	}
	
	public static Result gerarSpedContribuicoesResultado(int cdEmpresa, int nrMes, int nrAno)	{
		return gerarSpedContribuicoesResultado(cdEmpresa, nrMes, nrAno, 1, 1);
	}
	
	public static String gerarSpedContribuicoes(int cdEmpresa, int nrMes, int nrAno, int tpPerfil, int tpRemessa)	{
		return gerarSpedContribuicoesResultado(cdEmpresa, nrMes, nrAno, tpPerfil, tpRemessa).getMessage();
	}
	
	public static Result gerarSpedContribuicoesResultado(int cdEmpresa, int nrMes, int nrAno, int tpPerfil, int tpRemessa)	{
		return gerarSpedContribuicoesResultado(cdEmpresa, nrMes, nrAno, tpPerfil, tpRemessa, null);
	}
	
	public static Result gerarSpedContribuicoesResultado(int cdEmpresa, int nrMes, int nrAno, int tpPerfil, int tpRemessa, HttpSession session)	{
		GregorianCalendar dtInicial = new GregorianCalendar(nrAno, nrMes-1, 1);
		GregorianCalendar dtFinal   = (GregorianCalendar)dtInicial.clone();
		dtFinal.set(Calendar.DAY_OF_MONTH, dtFinal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return gerarSpedContribuicoesResultado(cdEmpresa, dtInicial, dtFinal, tpPerfil, tpRemessa, null, session);
	}
	
	public static Result gerarSpedContribuicoesResultado(int cdEmpresa, GregorianCalendar dtSpedInicial, GregorianCalendar dtSpedFinal, int tpPerfil, int tpRemessa, String nmPathArq){
		return gerarSpedContribuicoesResultado(cdEmpresa, dtSpedInicial, dtSpedFinal, tpPerfil, tpRemessa, nmPathArq, null);
	}
	
	public static Result gerarSpedContribuicoesResultado(int cdEmpresa, GregorianCalendar dtSpedInicial, GregorianCalendar dtSpedFinal, int tpPerfil, int tpRemessa, String nmPathArq, HttpSession session){
		int tpIndicadorTributo = 1;//Não cumlativo
		
		//Busca de Informações Empresa
		PessoaJuridica pessoaJuridica = PessoaJuridicaDAO.get(cdEmpresa);
		String cnpjEmpresa = Util.limparFormatos(pessoaJuridica.getNrCnpj());
		String ieEmpresa   = Util.limparFormatos(pessoaJuridica.getNrInscricaoEstadual());
		PessoaEndereco empresaEndereco = PessoaEnderecoServices.getEnderecoPrincipal(cdEmpresa);
		Cidade cidadeEmpresa = CidadeDAO.get(empresaEndereco.getCdCidade());
		Estado estadoEmpresa = EstadoDAO.get(cidadeEmpresa.getCdEstado());
		
		//Contagem de Registros por Bloco
		int nrLinhaBloco0=0;
		int nrLinhaBlocoA=0;
		int nrLinhaBlocoC=0;
		int nrLinhaBlocoD=0;
		int nrLinhaBlocoF=0;
		int nrLinhaBlocoI=0;
		int nrLinhaBlocoM=0;
		int nrLinhaBlocoP=0;
		int nrLinhaBloco1=0;
		int nrLinhaBloco9=0;
		int nrTotalLinha =0;
		
		HashMap<String, Object> conjuntoRegistro = new HashMap<String, Object>();
		conjuntoRegistro.put("0000", new Integer(0));
		conjuntoRegistro.put("0001", new Integer(0));
		conjuntoRegistro.put("0100", new Integer(0));
		conjuntoRegistro.put("0110", new Integer(0));
		conjuntoRegistro.put("0111", new Integer(0));
		conjuntoRegistro.put("0140", new Integer(0));
		conjuntoRegistro.put("0145", new Integer(0));
		conjuntoRegistro.put("0150", new Integer(0));
		conjuntoRegistro.put("0190", new Integer(0));
		conjuntoRegistro.put("0200", new Integer(0));
		conjuntoRegistro.put("0206", new Integer(0));
		conjuntoRegistro.put("0990", new Integer(0));
		conjuntoRegistro.put("C001", new Integer(0));
		conjuntoRegistro.put("C010", new Integer(0));
		conjuntoRegistro.put("C100", new Integer(0));
		conjuntoRegistro.put("C170", new Integer(0));
		conjuntoRegistro.put("C180", new Integer(0));
		conjuntoRegistro.put("C181", new Integer(0));
		conjuntoRegistro.put("C185", new Integer(0));
		conjuntoRegistro.put("C400", new Integer(0));
		conjuntoRegistro.put("C405", new Integer(0));
		conjuntoRegistro.put("C420", new Integer(0));
		conjuntoRegistro.put("C425", new Integer(0));
		conjuntoRegistro.put("C460", new Integer(0));
		conjuntoRegistro.put("C470", new Integer(0));
		conjuntoRegistro.put("C490", new Integer(0));
		conjuntoRegistro.put("C990", new Integer(0));
		conjuntoRegistro.put("D001", new Integer(0));
		conjuntoRegistro.put("D990", new Integer(0));
		conjuntoRegistro.put("F001", new Integer(0));
		conjuntoRegistro.put("F010", new Integer(0));
		conjuntoRegistro.put("F990", new Integer(0));
		conjuntoRegistro.put("M001", new Integer(0));
		conjuntoRegistro.put("M001", new Integer(0));
		conjuntoRegistro.put("M100", new Integer(0));
		conjuntoRegistro.put("M200", new Integer(0));
		conjuntoRegistro.put("M210", new Integer(0));
		conjuntoRegistro.put("M500", new Integer(0));
		conjuntoRegistro.put("M600", new Integer(0));
		conjuntoRegistro.put("M610", new Integer(0));
		conjuntoRegistro.put("M990", new Integer(0));
		conjuntoRegistro.put("P001", new Integer(0));
		conjuntoRegistro.put("P990", new Integer(0));
		conjuntoRegistro.put("1001", new Integer(0));
		conjuntoRegistro.put("1990", new Integer(0));
		conjuntoRegistro.put("9001", new Integer(0));
		
		boolean entrouDs  = false;
		boolean entrouDs2 = false;
		
		// VALIDAÇÃO - EMITENTE    	
		String dsValidacao  = "";
		String dsValidacao2 = "";
		String tpPerfilEmpresa = "";
		ResultSetMap rsm  = new ResultSetMap();
		ResultSetMap rsm2 = new ResultSetMap();
		Result resultado = new Result(2);
		String registro   = "";
        
		/**
		 *  Observações: Registro obrigatório, correspondente ao primeiro registro do arquivo da escrituração.
			Nível hierárquico - 0
			Ocorrência - um (por arquivo)
		 */
		/************************************** VALIDACAO *******************************************************/
	    // CNPJ
	    if(cnpjEmpresa == null || cnpjEmpresa.trim().equals(""))
	    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CNPJ";
	    // IBGE
	    if(cidadeEmpresa.getIdIbge()==null || cidadeEmpresa.getIdIbge().trim().equals(""))
	    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Cód. IBGE da cidade";
	    // Estado
	    if(estadoEmpresa.getSgEstado() == null)
	    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Estado";
	    
	    if(!dsValidacao.equals("")){
	    	HashMap<String, Object> register = new HashMap<String, Object>();
	    	register.put("ERRO", "Dados do emitente faltando: "+dsValidacao);
	    	rsm.addRegister(register);
	    
	    	dsValidacao = "";
	    	
	    	entrouDs  = true;
		}
	    
	    /************************************** VALIDACAO 2 *******************************************************/
	    if(!Util.isCNPJ(cnpjEmpresa))
	    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" CNPJ (validação acusou ERRO nos digitos)";
	    if(!dsValidacao2.equals("")){
	    	HashMap<String, Object> register = new HashMap<String, Object>();
	    	register.put("ERRO", "Dados do emitente inválidos: "+dsValidacao2);
	    	rsm.addRegister(register);
	    
	    	dsValidacao2 = "";
	    	
	    	entrouDs2 = true;
	    }
	    
	    if(!entrouDs && !entrouDs2){
			registro          = "|0000"+ 												  	 		 // REG - Texto Fixo contendo 0000
								"|003"+//003 2.01A ADE Cofis nº 20/2012 01/07/2012                          // COD_VER - Tabela
								"|" +((tpRemessa == COD_FIN_ORIGINAL) ? COD_FIN_ORIGINAL : COD_FIN_SUBSTITUTO)+ // COD_FIN - Código da finalidade do arquivo
								"|" + //IND_SIT_ESP - Indicador de situação especial
								"|" + //NUM_REC_ANTERIOR - Número do Recibo da Escrituração anterior a ser retificada, utilizado quando TIPO_ESCRIT for igual a 1
								"|" +Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+				 // DT_INI - Data inicial das informações
								"|" +Util.formatDateTime(dtSpedFinal, "ddMMyyyy")+  				 // DT_FIN - Data final das informações
								"|" +((pessoaJuridica.getNmRazaoSocial().trim().length() > 100) ? pessoaJuridica.getNmRazaoSocial().trim().substring(0, 99) : pessoaJuridica.getNmRazaoSocial().trim())+      // NOME - Nome Empresarial da entidade
								"|" +cnpjEmpresa+				                                     // CNPJ                                                        - OC
								"|" +estadoEmpresa.getSgEstado()+              	                     // UF
								"|" +cidadeEmpresa.getIdIbge()+ 	                                 // COD_MUN - Código do municipio pelo IBGE
								"|" +//Refere-se a SUFRAMA - A Empresa emissora da EFD não tem SUFRAMA
								"|" +// IND_NAT_PJ - Indicador de Natureza da pessoa jurídica
								"|" +IND_ATIV_OUTROS+						 	 	                 // IND_ATIV - Indicador de atividade
								"|" +"\r\n";
			nrLinhaBloco0++;
			conjuntoRegistro.put("0000", (((Integer) conjuntoRegistro.get("0000")) + 1));
	    }
		
	    entrouDs = false;
	    entrouDs2 = false;
		
	    /*
		 * REGISTRO 0001 - Abertura do bloco 0
		 */
		//Nivel hierarquico 1
		registro         +=   "|0001" + 										 // REG - Texto fixo contendo 0001
				              "|" +IND_MOV_DADOS+								 // IND_MOV - indicador de movimento
							  "|" +"\r\n";
		nrLinhaBloco0++;
		conjuntoRegistro.put("0001", (((Integer) conjuntoRegistro.get("0001")) + 1));
		
		
		/*
		 * REGISTRO 0100 - Dados do contabilista
		 */
		//Nivel hierarquico 2
		//Ocorrência - Vários (por arquivo)
		//Informações do Contador
		int cdContador 		   = ParametroServices.getValorOfParametroAsInteger("CD_CONTADOR", 0, cdEmpresa);
		int cdEmpresaContadora = ParametroServices.getValorOfParametroAsInteger("CD_EMPRESA_CONTABILIDADE", 0, cdEmpresa);
		String nrCrcContador   = ParametroServices.getValorOfParametro("NR_CRC", null, cdEmpresa);
		
		if(cdContador == 0 || nrCrcContador == null){
			HashMap<String, Object> register = new HashMap<String, Object>();
	    	register.put("ERRO", "Parametros do contador não configurados!");
	    	rsm.addRegister(register);
	    
		}
		else{
			PessoaFisica pessoaFisicaContador = PessoaFisicaDAO.get(cdContador);
			if(pessoaFisicaContador == null){
				HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("ERRO", "Para o parâmetro de contador esta configurado uma pessoa Juridica. Corrija");
		    	rsm.addRegister(register);
			}
			else{
				PessoaEndereco pessoaEnderecoContador = PessoaEnderecoServices.getEnderecoPrincipal(cdContador);
				Cidade cidadeContador = CidadeDAO.get(pessoaEnderecoContador.getCdCidade());
				String nrCnpjEmpresaContabilidade = "";
				if(cdEmpresaContadora != 0)
					nrCnpjEmpresaContabilidade = PessoaJuridicaDAO.get(cdEmpresaContadora).getNrCnpj();
				String cpf = Util.limparFormatos(pessoaFisicaContador.getNrCpf()).trim();
				String cep = Util.limparFormatos(pessoaEnderecoContador.getNrCep()).trim();
				/************************************** VALIDACAO *******************************************************/
					//Nome
				if(pessoaFisicaContador.getNmPessoa() == null || pessoaFisicaContador.getNmPessoa().trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Nome";
			    	// CPF
			    if(cpf==null || cpf.equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CPF";
			    	// CRC
			    if(nrCrcContador==null || nrCrcContador.trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CRC";
				
			    if(cidadeContador == null)
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Cidade";
			    
			    if(pessoaFisicaContador.getNmEmail() == null || pessoaFisicaContador.getNmEmail().trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Email";
			    	
			    if(!dsValidacao.equals("")){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados do contador faltando: "+dsValidacao);
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	
			    	entrouDs = true;
			    }
			    /************************************** VALIDACAO 2 *******************************************************/
			    // CPF
			    if(!Util.isCpfValido(cpf))
			    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" CPF (validação acusou ERRO nos digitos)";
			    // CRC
			    if(nrCrcContador.trim().length() > 15)
			    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" CRC (Numero de digitos ultrapassa o permitido)";
			    // CNPJ Empresa de Contabilidade
			    if(cdEmpresaContadora != 0 && nrCnpjEmpresaContabilidade != null && !Util.isCNPJ(nrCnpjEmpresaContabilidade))
			    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" CNPJ da Empresa de Contabilidade  (validação acusou ERRO nos digitos)";
			    // CEP
			    if(cep != null && cep.length() != 8)
			    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" CEP (Numero de digitos é diferente de 8)";
			    // ID Cidade
			    if(cidadeContador.getIdIbge() != null && cidadeContador.getIdIbge().length() != 7)
			    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" ID Cidade (Número de dígitos é diferente de 7)";
			     // Email
			    if(pessoaFisicaContador.getNmEmail() != null && pessoaFisicaContador.getNmEmail().length() > 50)
			    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" Email (Numero de digitos ultrapassa o permitido)";
			    if(!dsValidacao2.equals("")){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados do contador inválidos: "+dsValidacao2);
			    	rsm.addRegister(register);
			    
			    	dsValidacao2 = "";
			    	
			    	entrouDs2 = true;
			    }
			    
			    if(!entrouDs && !entrouDs2){
				    //Nivel hierarquico 2
					registro += "|0100" + 													 	 	  // REG - Texto fixo contendo 0100
										  "|" +((pessoaFisicaContador.getNmPessoa().trim().length() > 100) ? pessoaFisicaContador.getNmPessoa().trim().substring(0, 99) : pessoaFisicaContador.getNmPessoa().trim())+			  // NOME
										  "|" +cpf+ // CPF
										  "|" +nrCrcContador.trim()+			      							  // CRC
										  "|" +((nrCnpjEmpresaContabilidade == null || nrCnpjEmpresaContabilidade.equals("")) ? "" : nrCnpjEmpresaContabilidade)+			      		  // CNPJ Empresa Contabilidade                                                                                                                                                                    -OC
										  "|" +((cep == null || cep.equals("")) ? "" : cep)+// CEP																																																																						   -OC
										  "|" +((pessoaEnderecoContador.getNmLogradouro() == null || pessoaEnderecoContador.getNmLogradouro().trim().equals("")) ? "" : (pessoaEnderecoContador.getNmLogradouro().trim().length() > 60) ? pessoaEnderecoContador.getNmLogradouro().trim().substring(0, 59) : pessoaEnderecoContador.getNmLogradouro().trim())+		  // LOGRADOURO		   -OC
										  "|" +((pessoaEnderecoContador.getNrEndereco() == null || pessoaEnderecoContador.getNrEndereco().trim().equals("")) ? "" : (pessoaEnderecoContador.getNrEndereco().trim().length() > 60) ? pessoaEnderecoContador.getNrEndereco().trim().substring(0, 9) : pessoaEnderecoContador.getNrEndereco().trim())+		  // LOGRADOURO		   -OC
										  "|" +((pessoaEnderecoContador.getNmComplemento() == null || pessoaEnderecoContador.getNmComplemento().trim().equals("")) ? "" : (pessoaEnderecoContador.getNmComplemento().trim().length() > 60) ? pessoaEnderecoContador.getNmComplemento().trim().substring(0, 59) : pessoaEnderecoContador.getNmComplemento().trim())+		  // COMPLEMENTO   -OC
										  "|" +((pessoaEnderecoContador.getNmBairro() == null || pessoaEnderecoContador.getNmBairro().trim().equals("")) ? "" : (pessoaEnderecoContador.getNmBairro().trim().length() > 60) ? pessoaEnderecoContador.getNmBairro().trim().substring(0, 59) : pessoaEnderecoContador.getNmBairro().trim())+	  		  // BAIRRO		  					   -OC
										  "|" +((pessoaEnderecoContador.getNrTelefone() == null || Util.retirarEspacos(pessoaEnderecoContador.getNrTelefone()).equals("") || Util.retirarEspacos(pessoaEnderecoContador.getNrTelefone()).length() > 11) ? "" : Util.retirarEspacos(pessoaEnderecoContador.getNrTelefone()))+		  // TELEFONE				   						   -OC
										  "|" +((pessoaFisicaContador.getNrFax() == null || pessoaFisicaContador.getNrFax().trim().equals("")) ? "" : (pessoaFisicaContador.getNrFax().trim().length() > 60) ? pessoaFisicaContador.getNrFax().trim().substring(0, 59) : pessoaFisicaContador.getNrFax().trim())+			      // FAX												   -OC
										  "|" +((pessoaFisicaContador.getNmEmail() == null || pessoaFisicaContador.getNmEmail().trim().equals("")) ? "" : pessoaFisicaContador.getNmEmail().trim())+		      // EMAIL																																								   -OC
										  "|" +((cidadeContador.getIdIbge() == null || cidadeContador.getIdIbge().trim().equals("")) ? "" : cidadeContador.getIdIbge().trim())+ 	  // COD_MUN - IBGE																																													   -OC
										  "|" +"\r\n";
					nrLinhaBloco0++;
					conjuntoRegistro.put("0100", (((Integer) conjuntoRegistro.get("0100")) + 1));
			    }
			    entrouDs  = false;
			    entrouDs2 = false;
			}
		}
		
		
		/*
		 * REGISTRO 0110: REGIMES DE APURAÇÃO DA CONTRIBUIÇÃO SOCIAL E DE APROPRIAÇÃO DE CRÉDITO
		 */
		//Nivel hierarquico 2
		//Ocorrência  um (por arquivo)
		registro         +=   "|0110" + 										 // REG - Texto fixo contendo 0110
				              "|" +tpIndicadorTributo+							 // COD_INC_TRIB
				              "|" + "1" +
				              "|" + //COD_TIPO_CONT
				              "|" + //IND_REG_CUM
							  "|" +"\r\n";
		nrLinhaBloco0++;
		conjuntoRegistro.put("0110", (((Integer) conjuntoRegistro.get("0110")) + 1));
		
		
//		/*
//		 * REGISTRO 0111: TABELA DE RECEITA BRUTA MENSAL PARA FINS DE RATEIO DE CRÉDITOS COMUNS
//		 */
//		//Nivel hierarquico 3
//		//Ocorrência  1:1
//		registro         +=   "|0111" +// REG - Texto fixo contendo 0111
//				              "|" + // REC_BRU_NCUM_TRIB_MI
//				              "|" + //REC_BRU_ NCUM_NT_MI
//				              "|" + //REC_BRU_ NCUM_EXP
//				              "|" + //REC_BRU_CUM
//				              "|" + //REC_BRU_TOTAL
//							  "|" +"\r\n";
//		nrLinhaBloco0++;
//		conjuntoRegistro.put("0111", (((Integer) conjuntoRegistro.get("0111")) + 1));
//		
		
		ArrayList<Integer> cdEmpresaRegister = getEmpresasDoPeriodo(dtSpedInicial, dtSpedFinal);
		for(int cdEmpresaParticipante : cdEmpresaRegister){
			//Busca de Informações Empresa
			PessoaJuridica pessoaJuridicaParticipante = PessoaJuridicaDAO.get(cdEmpresaParticipante);
			String cnpjEmpresaParticipante = Util.limparFormatos(pessoaJuridicaParticipante.getNrCnpj());
			String ieEmpresaParticipante   = Util.limparFormatos(pessoaJuridicaParticipante.getNrInscricaoEstadual());
			String imEmpresaParticipante   = Util.limparFormatos(pessoaJuridicaParticipante.getNrInscricaoMunicipal());
			PessoaEndereco empresaEnderecoParticipante = PessoaEnderecoServices.getEnderecoPrincipal(cdEmpresaParticipante);
			Cidade cidadeEmpresaParticipante = CidadeDAO.get(empresaEnderecoParticipante.getCdCidade());
			Estado estadoEmpresaParticipante = null;
			if(cidadeEmpresaParticipante != null)
				estadoEmpresaParticipante = EstadoDAO.get(cidadeEmpresaParticipante.getCdEstado());
			/************************************** VALIDACAO *******************************************************/
		    // CNPJ
		    if(cnpjEmpresa == null || cnpjEmpresa.trim().equals(""))
		    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CNPJ";
		    	// IBGE
		    if(cidadeEmpresa.getIdIbge()==null || cidadeEmpresa.getIdIbge().trim().equals(""))
		    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Cód. IBGE da cidade";
		    	// Estado
		    if(estadoEmpresa.getSgEstado() == null)
		    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Estado";
		    	//IE
		    if(ieEmpresa == null || ieEmpresa.trim().equals(""))
		    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Inscrição Estadual";
		    
		    if(!dsValidacao.equals("")){
		    	HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("ERRO", "Dados do emitente faltando: "+dsValidacao);
		    	rsm.addRegister(register);
		    
		    	dsValidacao = "";
		    	
		    	entrouDs  = true;
			}
		    
		    /************************************** VALIDACAO 2 *******************************************************/
		    if(!Util.isCNPJ(cnpjEmpresa))
		    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" CNPJ (validação acusou ERRO nos digitos)";
		    if(!Util.isIEBahia(pessoaJuridica.getNrInscricaoEstadual()))//Verificar se o metodo esta funcionando
		    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" Inscrição Estadual (validação acusou ERRO nos digitos)";
		    if(!dsValidacao2.equals("")){
		    	
		    	HashMap<String, Object> register = new HashMap<String, Object>();
		    	register.put("ERRO", "Dados do emitente inválidos: "+dsValidacao2);
		    	rsm.addRegister(register);
		    
		    	dsValidacao2 = "";
		    	
		    	entrouDs2 = true;
		    }
		    if(!entrouDs && !entrouDs2){
		    	/*
				 * REGISTRO 0140: TABELA DE CADASTRO DE ESTABELECIMENTO
				 */
		    	//Nivel hierarquico 2
		    	//Ocorrência  Vários (por arquivo)
				registro          += "|0140"+ 												  	 		 // REG - Texto Fixo contendo 0140
									"|"+cdEmpresaParticipante+//COD_EST
									"|" +((pessoaJuridica.getNmRazaoSocial().trim().length() > 100) ? pessoaJuridica.getNmRazaoSocial().trim().substring(0, 99) : pessoaJuridica.getNmRazaoSocial().trim())+      // NOME - Nome Empresarial da entidade
									"|" +cnpjEmpresaParticipante+				                                     // CNPJ                                                        - OC
									"|" +(estadoEmpresaParticipante != null ? estadoEmpresaParticipante.getSgEstado() : "" )+              	                     // UF
									"|" +((ieEmpresaParticipante.length() == 9) ? ieEmpresaParticipante.substring(1) : ieEmpresaParticipante)+ 	                                                 // Inscrição Estadual
									"|" +(cidadeEmpresaParticipante != null ? cidadeEmpresaParticipante.getIdIbge() : "")+ 	                                 // COD_MUN - Código do municipio pelo IBGE
									"|" + ((imEmpresaParticipante != null && !imEmpresaParticipante.trim().equals("")) ? imEmpresaParticipante : "")+	 // Inscrição Municipal                             - OC
									"|" +//Refere-se a SUFRAMA - A Empresa emissora da EFD não tem SUFRAMA
									"|" +"\r\n";
				nrLinhaBloco0++;
				conjuntoRegistro.put("0140", (((Integer) conjuntoRegistro.get("0140")) + 1));
		    }
		    entrouDs = false;
		    entrouDs2 = false;
			
			ArrayList<Integer> codigosParticipante = getParticipantesDoPeriodoContribuicoes(cdEmpresaParticipante, dtSpedInicial, dtSpedFinal);
			for(int i = 0; i < codigosParticipante.size(); i++){
				
				//Informações dos Participantes
				int cdParticipante = codigosParticipante.get(i);
				Pessoa pessoaParticipante = PessoaDAO.get(cdParticipante);
				PessoaJuridica pessoaJuridicaParticipanteParticipante = null;
				String cnpjParticipante = null;
				String ieParticipante = null;
				PessoaFisica   pessoaFisicaParticipante = null;
				String cpfParticipante = null;
				if(pessoaParticipante.getGnPessoa() == 0){
					pessoaJuridicaParticipanteParticipante = PessoaJuridicaDAO.get(cdParticipante);
					cnpjParticipante = Util.limparFormatos(pessoaJuridicaParticipanteParticipante.getNrCnpj()).trim();
					ieParticipante = Util.limparFormatos(pessoaJuridicaParticipanteParticipante.getNrInscricaoEstadual()).trim();
					ieParticipante = Util.apenasNumeros(ieParticipante);
				}
				else{
					pessoaFisicaParticipante = PessoaFisicaDAO.get(cdParticipante);
					cpfParticipante = Util.limparFormatos(pessoaFisicaParticipante.getNrCpf()).trim();
				}
				int codPais = 1058;
				PessoaEndereco pessoaEnderecoParticipante = PessoaEnderecoServices.getEnderecoPrincipal(cdParticipante);
				Cidade cidadeParticipante = null;
				
				if(pessoaEnderecoParticipante != null)
					cidadeParticipante = CidadeDAO.get(pessoaEnderecoParticipante.getCdCidade());
				String idIbgeParticipante = null;
				/************************************** VALIDACAO *******************************************************/
				//Endereço
				if(pessoaEnderecoParticipante == null)
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Endereço";
				else{
					//Cidade
					if(cidadeParticipante == null)
						dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Cidade";
					else 
						idIbgeParticipante = Util.limparFormatos(cidadeParticipante.getIdIbge()).trim();
					// Logradouro
				    if(pessoaEnderecoParticipante.getNmLogradouro()==null || pessoaEnderecoParticipante.getNmLogradouro().trim().equals(""))
				    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Logradouro ";
				}
				//IBGE
				if(idIbgeParticipante == null)
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" ID IBGE";
				//CNPJ
			    if(pessoaParticipante.getGnPessoa() == 0 && (cnpjParticipante == null || cnpjParticipante.equals("")))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CNPJ ";
			    //CPF
			    if(pessoaParticipante.getGnPessoa() == 1 && (cpfParticipante == null || cpfParticipante.equals("")))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CPF ";
			    if(!dsValidacao.equals("")){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados do participante "+pessoaParticipante.getNmPessoa()+" faltando: "+dsValidacao);
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	
			    	entrouDs = true;
			    }
			    /************************************** VALIDACAO 2 *******************************************************/
			    // CPF
			    if(cpfParticipante !=null && !Util.isCpfValido(cpfParticipante))
			    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" CPF do participante " + pessoaParticipante.getNmPessoa() + " (validação acusou ERRO nos digitos)";
			    // CNPJ
			    if(cnpjParticipante != null && !Util.isCNPJ(cnpjParticipante))
			    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" CNPJ do participante " + pessoaParticipante.getNmPessoa() + " (validação acusou ERRO nos digitos)";
			    // ID Municipio
			    if(idIbgeParticipante != null && idIbgeParticipante.length() != 7)
			    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" ID do Municipio do participante " + pessoaParticipante.getNmPessoa() + " (tamanho diferente de 7)";
			    if(pessoaEnderecoParticipante != null){
			    	// IE
			    	Cidade cidade = CidadeDAO.get(pessoaEnderecoParticipante.getCdCidade());
			    	Estado estado = null;
			    	if(cidade != null)
			    		estado = EstadoDAO.get(cidade.getCdEstado());
//			    	if(estado != null && estado.getSgEstado().equals("BA")){
//				    	if(estado != null && ieParticipante != null && !ieParticipante.trim().equals("") && estado.getSgEstado().equals("BA") && !Util.isIEBahia(ieParticipante))//Conferir para saber se a biblioteca faz a validação correta e colocar para outros estados
//					    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" IE do participante " + pessoaParticipante.getNmPessoa() + " (validação acusou ERRO nos digitos)";
//				    	if(estado == null && ieParticipante != null && !ieParticipante.trim().equals(""))//Conferir para saber se a biblioteca faz a validação correta e colocar para outros estados
//					    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" Estado do participante";
//			    	}
//			    	else{
//			    		ieParticipante = null;
//			    	}
				    // Numero do Endereço
				    if(pessoaEnderecoParticipante.getNrEndereco() != null && pessoaEnderecoParticipante.getNrEndereco().trim().length() > 10)
				    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" Nº do imóvel do participante " + pessoaParticipante.getNmPessoa() + " (tamanho maior do que 10)";
				    // Complemento
				    if(pessoaEnderecoParticipante.getNmComplemento()!=null && pessoaEnderecoParticipante.getNmComplemento().trim().length() > 60)
				    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" Complemento do Endereço do participante " + pessoaParticipante.getNmPessoa() + " (tamanho maior do que 60)";
				    // Bairro
				    if(pessoaEnderecoParticipante.getNmBairro()!=null && pessoaEnderecoParticipante.getNmBairro().trim().length() > 60)
				    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" Bairro do participante " + pessoaParticipante.getNmPessoa() + " (tamanho maior do que 60)";
			    }
			    if(!dsValidacao2.equals("")){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados do participante "+pessoaParticipante.getNmPessoa()+" inválidos: "+dsValidacao2);
			    	rsm.addRegister(register);
			    
			    	dsValidacao2 = "";
			    	
			    	entrouDs2 = true;
			    }
			    if(!entrouDs && !entrouDs2){
			    	/*
					 * REGISTRO 0150: TABELA DE CADASTRO DO PARTICIPANTE
					 */
			    	//Nível hierárquico - 3
			    	//Ocorrência  1:N
					registro += "|0150" + 															  //REG - Texto fixo contendo 0150
									 	  "|" +pessoaParticipante.getCdPessoa()+				  //COD_PART
										  "|" +((pessoaParticipante.getNmPessoa().trim().length() > 100) ? pessoaParticipante.getNmPessoa().trim().substring(0, 99) : pessoaParticipante.getNmPessoa().trim())+			  //NOME
										  "|" +codPais+			      							  //Cod país
										  ((pessoaParticipante.getGnPessoa() == 0) ?
										  "|" +cnpjParticipante + "|" : //CNPJ								        																																				  -OC
										  "||"+cpfParticipante	//CPF																																																  -OC
										  ) +
										  "|" +((ieParticipante != null) ? ieParticipante : "")+	//Inscrição estadual																																			  -OC
										  "|" +((idIbgeParticipante != null) ? idIbgeParticipante: "")+  //COD_MUN																																					  -OC
										  "|" +//Suframa																																																			  -OC
										  "|" +((pessoaEnderecoParticipante != null  && pessoaEnderecoParticipante.getNmLogradouro() != null ? (pessoaEnderecoParticipante.getNmLogradouro().trim().length() > 60) ? pessoaEnderecoParticipante.getNmLogradouro().trim().substring(0, 59) : pessoaEnderecoParticipante.getNmLogradouro().trim() : ""))+	  //Logradouro
										  "|" +((pessoaEnderecoParticipante != null  && pessoaEnderecoParticipante.getNrEndereco() != null) ? pessoaEnderecoParticipante.getNrEndereco().trim() : "")+	  //Numero do imovel												                                  -OC
										  "|" +((pessoaEnderecoParticipante != null  && pessoaEnderecoParticipante.getNmComplemento() != null) ? pessoaEnderecoParticipante.getNmComplemento().trim() : "")+  //Complemento														                          -OC
										  "|" +((pessoaEnderecoParticipante != null  && pessoaEnderecoParticipante.getNmBairro() != null) ? pessoaEnderecoParticipante.getNmBairro().trim() : "")+		  //Bairro  														                                  -OC
										  "|" +"\r\n";
					nrLinhaBloco0++;
					conjuntoRegistro.put("0150", (((Integer) conjuntoRegistro.get("0150")) + 1));
			    }
			    entrouDs  = false;
			    entrouDs2 = false;
			}
			
			
			/*
			 * REGISTRO 0190 - Identificação das unidades de medidas
			 */
			ResultSetMap resultadoUni = getUnidadesContribuicoes(cdEmpresaParticipante, dtSpedInicial, dtSpedFinal);
			while(resultadoUni.next()){
				UnidadeMedida unidadeMedida = UnidadeMedidaDAO.get(resultadoUni.getInt("cd_unidade_medida"));
				/************************************** VALIDACAO *******************************************************/
				//Descrição
				if(unidadeMedida.getNmUnidadeMedida()==null || unidadeMedida.getNmUnidadeMedida().trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Descrição ";
			    
			    if(!dsValidacao.equals("")){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados da unidade de medida "+unidadeMedida.getNmUnidadeMedida()+" faltando: "+dsValidacao);
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	
			    	entrouDs = true;
			    }
			    if(!entrouDs && !entrouDs2){
				    registro +=      	"|" +"0190" + 															  //REG - Texto fixo contendo 0190
											"|" +unidadeMedida.getSgUnidadeMedida()+				  //UNID - Codigo da unidade de medida
							  				"|" +"Unidade " + unidadeMedida.getSgUnidadeMedida() +			  //DESCR - Descrição da unidade de medida
							  				"|" +"\r\n";
					nrLinhaBloco0++;
					conjuntoRegistro.put("0190", (((Integer) conjuntoRegistro.get("0190")) + 1));
			    }
			    
			    entrouDs = false;
			}
			/*
			 * Registro 0200 - Tabela de identificação do item 
			 */		
			ResultSetMap rsmProds = getProdutosContribuicoes(cdEmpresaParticipante, dtSpedInicial, dtSpedFinal);
			while(rsmProds.next()){
				int cdProdutoServico = rsmProds.getInt("CD_PRODUTO_SERVICO");
				String codBarras     = rsmProds.getString("ID_PRODUTO_SERVICO");
				ProdutoServicoEmpresa produtoEmpresa = ProdutoServicoEmpresaDAO.get(cdEmpresaParticipante, cdProdutoServico);
				ProdutoServico produto = ProdutoServicoDAO.get(cdProdutoServico);
				Ncm ncm = NcmDAO.get(produto.getCdNcm());
				/************************************** VALIDACAO *******************************************************/
				//Unidade de Medida
				if(produtoEmpresa == null || produtoEmpresa.getCdUnidadeMedida()==0)
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Unidade de medida ";
			    
			    if(!dsValidacao.equals("")){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados do produto "+produto.getNmProdutoServico() + " - " + codBarras+" faltando: "+dsValidacao);
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	
			    	entrouDs = true;
			    }
			    /************************************** VALIDACAO 2 *******************************************************/
			    if(ncm != null && ncm.getNrNcm().length() != 8)
			    	dsValidacao2 += (dsValidacao2.equals("") ? "" : ", ")+" NCM do produto " + produto.getNmProdutoServico() + " - " + codBarras + " (Número de digitos é diferente de 8)";
			    //Codigo de Barras
			    if(!dsValidacao2.equals("")){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados dos produtos inválidos: "+dsValidacao2);
			    	rsm.addRegister(register);
			    
			    	dsValidacao2 = "";
			    	
			    	entrouDs2 = true;
			    }
			    if(!entrouDs && !entrouDs2){
					registro += 	        "|" +"0200" +															  //REG 
											"|" +(rsmProds.getString("COD_ITEM") != null ? rsmProds.getString("COD_ITEM") : produto.getCdProdutoServico())+	  //Cód produto
											"|" +((produto.getNmProdutoServico().trim().length() > 255) ? produto.getNmProdutoServico().trim().substring(0, 254) : produto.getNmProdutoServico().trim())+	  //DESCRICAO
											"|" +((produto.getIdProdutoServico() != null) ? produto.getIdProdutoServico().trim() : "")+	  //Código de barra																			- OC			
											"|" +	  							  //Código de barra anterior																													- OC
											"|" +UnidadeMedidaDAO.get(produtoEmpresa.getCdUnidadeMedida()).getSgUnidadeMedida()+	  //Unidade de medida
											"|" +"00"+//Mercadoria para Revenda - Ver Tabela	 							 //Tipo do item
											"|" +((ncm == null) ? "" : ncm.getNrNcm())+	 //Código da nomeclatura comum mercosul																			- OC
											"|" + 							 //Código da nomeclatura conforme TIPI																												- OC
											"|" +((ncm != null) ? ncm.getNrNcm().substring(0, 2) : "")+	 							 //Código do genero do item - Tabela                        								- OC
											"|" +	 							 //Código do serviço																															- OC
											"|" +	 							 //Aliquota do icms	                                       													- OC   
											"|" +"\r\n";
					nrLinhaBloco0++;
					conjuntoRegistro.put("0200", (((Integer) conjuntoRegistro.get("0200")) + 1));
			    }
				
			    entrouDs  = false;
			    entrouDs2 = false;
			    
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("CD_PRODUTO_SERVICO", Integer.toString(cdProdutoServico), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmProdGrup = ProdutoGrupoDAO.find(criterios);
				ArrayList<Integer> cdsGrupos = new ArrayList<Integer>();
				while(rsmProdGrup.next()){
					cdsGrupos.add(rsmProdGrup.getInt("cd_grupo"));
				}
				
				boolean isCombustivel = false;
				
				for(int j = 0; j < cdsGrupos.size(); j++){
					int cdGrupo = cdsGrupos.get(j);
					if(cdGrupo == com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0)){
						isCombustivel = true;
						break;
					}
				}
				
				if(isCombustivel){
					/*
					 * Registro 0206 - Código de produtos conforme tabela publicada pela ANP
					 */
					ProdutoServico 	produtoCombus	= produto;
					/************************************** VALIDACAO *******************************************************/
					//Codigo da tabela ANP
					if(produtoCombus.getIdProdutoServico()==null || produtoCombus.getIdProdutoServico().trim().equals(""))
				    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Codigo (ANP) ";
				    
				    if(!dsValidacao.equals("")){
				    	HashMap<String, Object> register = new HashMap<String, Object>();
				    	register.put("ERRO", "Dados do produto "+produtoCombus.getNmProdutoServico()+" faltando: "+dsValidacao);
				    	rsm.addRegister(register);
				    
				    	dsValidacao = "";
				    
				    	//entrouDs = true;
				    }
				    if(!entrouDs && !entrouDs2){
						registro += 	"|0206" +//Necessario vir logo depois do registro pai, por isso utilizar a mesma String de registro				  //REG 
												"|" +produtoCombus.getIdProdutoServico()+	  //Saber se o idProdutoServico para combustiveis esta de acordo a tabela ANP
												"|" +"\r\n";
						
						nrLinhaBloco0++;
						conjuntoRegistro.put("0206", (((Integer) conjuntoRegistro.get("0206")) + 1));
				    }
				    entrouDs  = false;			    
				}
			}
			
			
		}
		
		/*
		 * Registro 0990 - Encerramento do bloco 0
		 */
		
		nrLinhaBloco0++;//O registro 0990 também entra na conta
		
		registro +=  "|" +"0990" + 													//REG
							   "|" +nrLinhaBloco0+						//Quantidade de linhas
							   "|" +"\r\n";
		conjuntoRegistro.put("0990", (((Integer) conjuntoRegistro.get("0990")) + 1));
			
		
		// ******************************** BLOCO C ***************************************************************************************************************				
		
		/*
		 * BLOCO C - C001 - Abertura
		 * Nível hierárquico - 1
		   Ocorrência  um por arquivo
		 */
		//Buscar a indicação de movimento dinamicamente
//		int indicadorMovimento = getPossuiMovimentoC();
		int indicadorMovimento = 0;
		registro +=  "|" +"C001" + // REG - Texto fixo contendo C001
		   	         "|" +indicadorMovimento+// IND_MOV - indicador de movimento
					 "|" +"\r\n";
		nrLinhaBlocoC++;
		conjuntoRegistro.put("C001", (((Integer) conjuntoRegistro.get("C001")) + 1));

		for(int cdEmpresaParticipante : cdEmpresaRegister){
			
			PessoaJuridica pessoaJuridicaParticipante = PessoaJuridicaDAO.get(cdEmpresaParticipante);
			
			//Buscar indicador de escrituração dinamicamente
//			int indicadorEscrituracao = getEscrituracao(cdEmpresaParticipante);
			int indicadorEscrituracao = 1;
			String cnpjEmpresaParticipante = Util.limparFormatos(pessoaJuridicaParticipante.getNrCnpj());
			/*
			 * REGISTRO C010: IDENTIFICAÇÃO DO ESTABELECIMENTO
			 *  Nível hierárquico - 2
				Ocorrência  vários por arquivo
			 */
			registro +=  "|" +"C010" + // REG - Texto fixo contendo C010
						 "|" +cnpjEmpresaParticipante+		
			   	         "|" +indicadorEscrituracao+// IND_MOV - indicador de movimento
						 "|" +"\r\n";
			nrLinhaBlocoC++;
			conjuntoRegistro.put("C010", (((Integer) conjuntoRegistro.get("C010")) + 1));
		
			
			
			Result resultadoDocCont = getDocumentoNotaFiscalContribuicoes(cdEmpresa, dtSpedInicial, dtSpedFinal);
			
			HashMap<String, ArrayList<Integer>> conjuntoCodigos = (HashMap<String, ArrayList<Integer>>)resultadoDocCont.getObjects().get("resultado");
			
			
			ArrayList<Integer> codigosNfe = conjuntoCodigos.get("rsmNfe");
			ArrayList<String> registroNota = new ArrayList<String>();
			for(Integer cdNotaFiscal : codigosNfe){
				
				NotaFiscal nota = NotaFiscalDAO.get(cdNotaFiscal);
				//Numero do documento
				int nrDoc = 0;
				if(nota.getNrNotaFiscal() != null && !nota.getNrNotaFiscal().trim().equals("")){
					nrDoc = Integer.parseInt(Util.limparFormatos(nota.getNrNotaFiscal(), 'N'));
				}
				String nrDocumento = ((nrDoc > 999999999) ? String.valueOf(nrDoc).substring(0, 9) : String.valueOf(nrDoc));
				//Valor do documento
				String vlTotalDocumento = Util.formatNumber(nota.getVlTotalNota(), "#.##");
				//Valor de Desconto do documento
				String vlDescontos = Util.formatNumber(NotaFiscalServices.getVlDesconto(nota.getCdNotaFiscal()), "#.##");
				//Situação do documento
				String stDocumentoFiscal = ST_DOCUMENTO_REGULAR;
				
				if(nota.getStNotaFiscal() == NotaFiscalServices.CANCELADA){
					stDocumentoFiscal = ST_DOCUMENTO_CANCELADO;
				}
				else if(nota.getStNotaFiscal() == NotaFiscalServices.DENEGADA){
					stDocumentoFiscal = ST_NFE_DENEGADO;
				}		
				//Selecionar o Tipo de Pagamento
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("CD_NOTA_FISCAL", Integer.toString(nota.getCdNotaFiscal()), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmNotaDocVinc = NotaFiscalDocVinculadoDAO.find(criterios);
				int cdDocumentoSaida = 0;
				int cdDocumentoEntrada = 0;
				if(rsmNotaDocVinc.next()){
					cdDocumentoSaida = rsmNotaDocVinc.getInt("cd_documento_saida");
					cdDocumentoEntrada = rsmNotaDocVinc.getInt("cd_documento_entrada");
				}
				
				//Contas a Receber pertencentes ao documento
				ArrayList<Integer> contasAReceber  = getContasAReceber(cdDocumentoSaida);
				ArrayList<Integer> contasAPagar  = getContasAPagar(cdDocumentoEntrada);

				int tpPagamento = -1;
				
				if(contasAReceber.size() == 1 && cdDocumentoSaida > 0)
					tpPagamento = IND_PAGTO_AVISTA;
				else
					tpPagamento = IND_PAGTO_APRAZO;
				
				if(contasAPagar.size() == 1 && cdDocumentoEntrada > 0)
					tpPagamento = IND_PAGTO_AVISTA;
				else
					tpPagamento = IND_PAGTO_APRAZO;
			
				//Valor Total do Itens
				float vlProd = NotaFiscalServices.getValorAllItens(nota.getCdNotaFiscal());
				
				
				/************************************** VALIDACAO *******************************************************/
				//Codigo do cliente
				if(String.valueOf(nota.getCdDestinatario())==null || String.valueOf(nota.getCdDestinatario()).trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Cliente ";
				//Numero do Documento
				if(nota.getNrNotaFiscal() ==null || nota.getNrNotaFiscal().trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Numero do documento ";
				//Chave da Nota Fiscal Eletronica - APRESENTAR
				if(nota.getNrChaveAcesso() == null || nota.getNrChaveAcesso().trim().equals(""))
					dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Chave de Acesso da NFe ";
						
				//Data de Emissao do Documento
				if(nota.getDtEmissao() == null || String.valueOf(nota.getDtEmissao()).trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Data de emissão ";
				
				//Valor Total do Documento
				if(String.valueOf(nota.getVlTotalNota()) == null || String.valueOf(nota.getVlTotalNota()).trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor total ";
				
			    if(!dsValidacao.equals("")){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados da nota fiscal eletronica Nº " + nota.getNrNotaFiscal() + " faltando: "+dsValidacao);
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	//entrouDs = true;
			    }
			    
			    /*Validacao*/
			    boolean duplicidade = false;
			    String registroAtual = IND_OPER_SAIDA + nrDocumento + "55" + stDocumentoFiscal + nota.getNrSerie() + nota.getNrChaveAcesso().substring(3);
			    for(int ind = 0; ind < registroNota.size(); ind++){
			    	if(registroAtual.equals(registroNota.get(ind))){
			    		duplicidade = true;
			    		break;
			    	}
			    }
			    
			    if(duplicidade){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados da Nota Fiscal Eletrônica " + nota.getNrNotaFiscal() + ": Duplicidade com outra nota.");
			    	rsm.addRegister(register);
			    	//entrouDs = true;
			    }
			    else{
			    	registroNota.add(registroAtual);
			    }
			    
			    
			    GregorianCalendar data = new GregorianCalendar();
			    data.set(Calendar.YEAR, 2000);
			    data.set(Calendar.MONTH, 0);
			    data.set(Calendar.DAY_OF_MONTH, 1);
			    if(nota.getDtAutorizacao() == null || nota.getDtAutorizacao().before(data)){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados da Nota Fiscal Eletrônica " + nota.getNrNotaFiscal() + ": Data da nota fiscal eletrônica não pode ser anterior a 01/01/2000");
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	
			    	//entrouDs = true;
			    }
			    
			    if(nota.getDtAutorizacao() == null || nota.getDtEmissao() == null || nota.getDtAutorizacao().before(nota.getDtEmissao())){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados da Nota Fiscal Eletrônica " + nota.getNrNotaFiscal() + ": Data da nota fiscal eletrônica não pode ser anterior a Data de Emissão");
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	
			    	//entrouDs = true;
			    }
			    
			    if(nota.getDtAutorizacao() == null || nota.getDtEmissao() == null || dtSpedFinal.before(nota.getDtAutorizacao()) || dtSpedFinal.before(nota.getDtEmissao())){
					HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados da Nota Fiscal Eletrônica " + nota.getNrNotaFiscal() + ": Data menor do que a data final de emissão do SPED");
			    	rsm.addRegister(register);
			    
			    	//entrouDs = true;
			     }
			    
			    float vlBaseICMS = 0;
				float vlICMS = 0;
				float vlBaseICMSSub = 0;
				float vlICMSSub = 0;
				
				float vlBaseIPI = 0;
				float vlIPI = 0;
				float vlBaseIPISub = 0;
				float vlIPISub = 0;
				
				float vlBasePIS = 0;
				float vlPIS = 0;
				float vlBasePISSub = 0;
				float vlPISSub = 0;
				
				float vlBaseCOFINS = 0;
				float vlCOFINS = 0;
				float vlBaseCOFINSSub = 0;
				float vlCOFINSSub = 0;
			    
				int erroAliq = 0;
				
	//			ResultSetMap com os itens do documento
				ResultSetMap rsmProdutos = NotaFiscalServices.getAllItens(cdNotaFiscal);
	
			    if(rsmProdutos.next()){
					vlICMS  	  = rsmProdutos.getFloat("vl_icms_documento");
					vlBaseICMS 	  = rsmProdutos.getFloat("vl_base_icms_documento");
					vlBaseICMSSub = rsmProdutos.getFloat("vl_base_icms_substituto_documento");
					vlICMSSub 	  = rsmProdutos.getFloat("vl_icms_substituto_documento");
					
					vlBaseIPI 	  = rsmProdutos.getFloat("vl_base_ipi_documento");
					vlIPI 		  = rsmProdutos.getFloat("vl_ipi_documento");
					vlBaseIPISub  = rsmProdutos.getFloat("vl_base_ipi_substituto_documento");
					vlIPISub 	  = rsmProdutos.getFloat("vl_ipi_substituto_documento");
					
					vlBasePIS 	  = rsmProdutos.getFloat("vl_base_pis_documento");
					vlPIS 		  = rsmProdutos.getFloat("vl_pis_documento");
					vlBasePISSub  = rsmProdutos.getFloat("vl_base_pis_substituto_documento");
					vlPISSub 	  = rsmProdutos.getFloat("vl_pis_substituto_documento");
					
					vlBaseCOFINS 	  = rsmProdutos.getFloat("vl_base_cofins_documento");
					vlCOFINS 		  = rsmProdutos.getFloat("vl_cofins_documento");
					vlBaseCOFINSSub  = rsmProdutos.getFloat("vl_base_cofins_substituto_documento");
					vlCOFINSSub 	  = rsmProdutos.getFloat("vl_cofins_substituto_documento");
					
					erroAliq = rsmProdutos.getInt("ErroAliq");
				}
				else{
					HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Nota Fiscal Eletrônica Nº "+nota.getNrNotaFiscal()+" sem produtos!");
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	
			    	continue;
				}
			    
			    if(erroAliq > 0){
			    	String imposto = "";
			    	switch(erroAliq){
			    		case 1:
			    			imposto = "ICMS";
			    			break;
			    		case 2:
			    			imposto = "IPI";
			    			break;
			    		case 3:
			    			imposto = "II";
			    			break;
			    		case 4:
			    			imposto = "PIS";
			    			break;
			    		case 5:
			    			imposto = "COFINS";
			    			break;
			    	}
			    	
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados da nota fiscal eletrônica Nº " + nota.getNrNotaFiscal()+ ": Problema nas aliquotas do imposto " + imposto);
			    	rsm.addRegister(register);
			    
			    	//entrouDs = true;
			    }
			    
			    
			    
			    if(!entrouDs && !entrouDs2){
					registro += "|C100" + //REG
							  "|" +(cdDocumentoSaida > 0 ? IND_OPER_SAIDA : IND_OPER_ENTRADA)+
							  "|" +IND_EMIT_PROPRIA+
							  "|" +nota.getCdDestinatario()+                                                                 //Cód participante
							  "|" +"55"+	                                                        //Cód modelo do documento fiscal            - VERIFICAR PARA QUANDO NAO FOR 1A NEM Nfe
							  "|" +stDocumentoFiscal+										                                                                    //Cód da situação do documento fiscal       - Tabela 4.1.2
							  "|" +nota.getNrSerie()+	                                                                    //Serie do documento fiscal                 - OC
							  "|" +nrDocumento+	                                                        //Numero do documento fiscal            
							  "|" +nota.getNrChaveAcesso().substring(3)+                                    //Chave da nota fiscal online
							  "|" +Util.formatDateTime(nota.getDtEmissao(), "ddMMyyyy")+			                                                        //Data da emissão
							  "|" +Util.formatDateTime(nota.getDtMovimentacao(), "ddMMyyyy")+			                                                    //Data de entrada ou saida
							  "|" +vlTotalDocumento+                                    //Valor total do documento fiscal           
							  "|" +((tpPagamento == 1) ? IND_PAGTO_APRAZO : (tpPagamento == 0) ? IND_PAGTO_AVISTA : "")+		//Indicador do tipo de pagamento
							  "|" +(vlDescontos.equals("-0") ? "0" : vlDescontos)+		         							//Valor total do desconto
							  "|" +	//Valor abatimento fiscal                    - VERIFICAR
							  "|" +Util.formatNumber(vlProd, "#.##")+										                    //Valor total da mercadoria e serviços
							  "|" +IND_FRT_SEMFRETE+                                                                          //Indicador do frete
							  "|" +Util.formatNumber(nota.getVlFrete(), "#.##")+									                            //Valor do frete                             - No Momento SEM FRETE como padrão
							  "|" +Util.formatNumber(nota.getVlSeguro(), "#.##")+									                            //Valor do seguro do frete
							  "|" +Util.formatNumber(nota.getVlOutrasDespesas(), "#.##")+				   									                                                //Valor de outras despesas                   - SEM VALOR DE OUTRAS DESPESAS  
							  "|" +Util.formatNumber(vlBaseICMS, "#.##")+	                                    //Valor da base de calculo do icms 														-OC
							  "|" +Util.formatNumber(vlICMS, "#.##")+	                                    //Valor do icms 																		-OC
							  "|" +Util.formatNumber(vlBaseICMSSub, "#.##")+									    //Valor da base de calculo do icms substituicao tributaria                                                       - VERIFICAR SE EH PARA USAR 						-OC
							  "|" +Util.formatNumber(vlICMSSub, "#.##")+									    //Valor do ICMS retido por substituição tributaria                                                               - VERIFICAR SE EH PARA USAR 						-OC
							  "|" +Util.formatNumber(vlIPI, "#.##")+	                                    //Valor total do IPI 																			-OC
							  "|" +Util.formatNumber(vlPIS, "#.##")+									                                                                        //Valor total do PIS 																			-OC
							  "|" +Util.formatNumber(vlCOFINS, "#.##")+									                                                                        //Valor total da confins 																		-OC
							  "|" +Util.formatNumber(vlPISSub, "#.##")+									                                                                        //Valor total do pis retido por substituicao tributaria 										-OC
							  "|" +Util.formatNumber(vlCOFINSSub, "#.##")+									                                                                        //Valor total da CONFINS retido por substituicao tributaria 									-OC					                                                                        //Valor total da CONFINS retido por substituicao tributaria
							  "|" +"\r\n";
					
					nrLinhaBlocoC++;
					conjuntoRegistro.put("C100", (((Integer) conjuntoRegistro.get("C100")) + 1));
			    }
			    
			    //entrouDs = false;
				
				
				/*
				 * C110 - Informação complementar da nota fiscal - Referente a C100
				 */
			    if(nota.getTxtObservacao() != null && !nota.getTxtObservacao().equals("")){
			    	String txtComplementar = NfeServices.removeAcentos(nota.getTxtObservacao().trim());
			    	txtComplementar = (txtComplementar.length() > 255 ? txtComplementar.substring(0, 255) : txtComplementar);
					registro += "|C110" + //REG
									  "|" +nota.getCdNotaFiscal()+ //Código da informação complementar - Codigo deve ser o mesmo do registro 0450
									  "|" +txtComplementar.trim()+ //Descrição complementar do código de referência 
									  "|" +"\r\n";
						
					nrLinhaBlocoC++;
					conjuntoRegistro.put("C110", (((Integer) conjuntoRegistro.get("C110")) + 1));
			    }
				
				ArrayList<Integer> contasAReceberNota  = getAllContasAReceber(cdNotaFiscal);
				ArrayList<Integer> contasAPagarNota    = getAllContasAPagar(cdNotaFiscal);
				
				
				float valorReduzidoTotal = 0;
				float valorTotalItens = 0;
				float valorBaseTotal = 0;
						
				do{
					
					/*
					 * Registro C170 - Refernte a C100 - Itens do documento
					 */
					/************************************** VALIDACAO *******************************************************/
					//Quantidade do Item
					if(String.valueOf(rsmProdutos.getFloat("qt_tributario")) == null || String.valueOf(rsmProdutos.getFloat("qt_tributario")).trim().equals(""))
				    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Quantidade do item: " + rsmProdutos.getString("nm_produto_servico");
					
					//Unidade de Medida
					if(rsmProdutos.getString("nm_unidade_medida") == null || rsmProdutos.getString("nm_unidade_medida").trim().equals(""))
				    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Unidade de medida do item: " + rsmProdutos.getString("nm_produto_servico") + "(" + rsmProdutos.getString("id_produto_servico") + ")" + " - Nota Fiscal Eletronica Nº: " + nota.getNrNotaFiscal();
					//Valor Unitario
					if(String.valueOf(rsmProdutos.getFloat("vl_unitario")) == null || String.valueOf(rsmProdutos.getFloat("vl_unitario")).trim().equals(""))
				    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor unitario do item: " + rsmProdutos.getString("nm_produto_servico") + " - Nota Fiscal Eletronica Nº: " + nota.getNrNotaFiscal();
					//CFOP
					if(rsmProdutos.getString("nr_codigo_fiscal") == null || rsmProdutos.getString("nr_codigo_fiscal").trim().equals(""))
				    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CFOP do item: " + rsmProdutos.getString("nm_produto_servico") + " - Nota Fiscal Eletronica Nº: " + nota.getNrNotaFiscal();
				    if(!dsValidacao.equals(""))
				    	return new Result(-1, "Dados dos Itens faltando: "+dsValidacao);
					
				    valorTotalItens += (rsmProdutos.getFloat("qt_tributario") * rsmProdutos.getFloat("vl_unitario"));
				    valorBaseTotal  += rsmProdutos.getFloat("vl_base_calculo");
				    
				    
				    
				    vlICMS  	  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? 0 : rsmProdutos.getFloat("vl_icms"));
					vlBaseICMS 	  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? 0 : rsmProdutos.getFloat("vl_base_icms"));
					vlBaseICMSSub = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? rsmProdutos.getFloat("vl_base_icms") : 0);
					vlICMSSub 	  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? rsmProdutos.getFloat("vl_icms") : 0);
					
					vlBaseIPI 	  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? 0 : rsmProdutos.getFloat("vl_base_ipi"));
					vlIPI 		  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? 0 : rsmProdutos.getFloat("vl_ipi"));
					vlBaseIPISub  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? rsmProdutos.getFloat("vl_base_ipi") : 0);
					vlIPISub 	  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? rsmProdutos.getFloat("vl_ipi") : 0);
					
					vlBasePIS 	  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? 0 : rsmProdutos.getFloat("vl_base_pis"));
					vlPIS 		  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? 0 : rsmProdutos.getFloat("vl_pis"));
					vlBasePISSub  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? rsmProdutos.getFloat("vl_base_pis") : 0);
					vlPISSub 	  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? rsmProdutos.getFloat("vl_pis") : 0);
					
					vlBaseCOFINS 	  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? 0 : rsmProdutos.getFloat("vl_base_cofins"));
					vlCOFINS 		  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? 0 : rsmProdutos.getFloat("vl_cofins"));
					vlBaseCOFINSSub  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? rsmProdutos.getFloat("vl_base_cofins") : 0);
					vlCOFINSSub 	  = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? rsmProdutos.getFloat("vl_cofins") : 0);
					
				    float prAliquotaICMSSub = (rsmProdutos.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA ? rsmProdutos.getFloat("pr_icms") : 0);
				    float prAliquotaIPI 	= rsmProdutos.getFloat("pr_ipi");
				    float prAliquotaPIS 	= rsmProdutos.getFloat("pr_pis");
				    float prAliquotaCOFINS  = rsmProdutos.getFloat("pr_cofins");
					
	//			    vlDescontoItens += rsmProdutos.getFloat("vl_desconto");
				    
				    String cst = ((vlBaseICMS > 0 || vlBaseICMSSub > 0) && rsmProdutos.getString("nr_situacao_tributaria") != null && !rsmProdutos.getString("nr_situacao_tributaria").equals("") ? rsmProdutos.getString("nr_situacao_tributaria") : "041");
				    
				    String parteCst = cst.substring(1);
				    
				    if((parteCst.equals("00") || 
				    	parteCst.equals("10") || 
				    	parteCst.equals("20") || 
				    	parteCst.equals("70")) &&
				    	rsmProdutos.getFloat("pr_icms") == 0){
				    	
				    	HashMap<String, Object> register = new HashMap<String, Object>();
				    	register.put("AVISO", "Dados dos Itens do nota fiscal eletrônica Nº "+nota.getNrNotaFiscal()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : Se o CST termina em 00, 10, 20 ou 70 a Aliquota deve ser maior do que zero.");
				    	rsm2.addRegister(register);
				    
				    }
				    
				    if((vlBasePIS > 0 || vlBasePISSub > 0) && rsmProdutos.getString("nr_situacao_tributaria_pis") != null && !rsmProdutos.getString("nr_situacao_tributaria_pis").equals("") 
				    || (vlBaseCOFINS > 0 || vlBaseCOFINSSub > 0) && rsmProdutos.getString("nr_situacao_tributaria_cofins") != null && !rsmProdutos.getString("nr_situacao_tributaria_cofins").equals("")){
				    	if(vlBasePIS != vlBaseCOFINS){
				    		HashMap<String, Object> register = new HashMap<String, Object>();
					    	register.put("ERRO", "Dados dos Itens do nota fiscal eletrônica Nº "+nota.getNrNotaFiscal()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : Valor das bases de cáuculo do PIS e COFINS devem ser iguais.");
					    	rsm.addRegister(register);
				    	}
				    	else if(vlBasePISSub != vlBaseCOFINSSub){
				    		HashMap<String, Object> register = new HashMap<String, Object>();
					    	register.put("ERRO", "Dados dos Itens do nota fiscal eletrônica Nº "+nota.getNrNotaFiscal()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : Valor das bases de cáuculo do PIS e COFINS devem ser iguais.");
					    	rsm.addRegister(register);
				    	}
				    	
				    	if(rsmProdutos.getString("nr_situacao_tributaria_pis") == null || rsmProdutos.getString("nr_situacao_tributaria_pis").equals("")|| 
				    	   rsmProdutos.getString("nr_situacao_tributaria_cofins") == null || rsmProdutos.getString("nr_situacao_tributaria_cofins").equals("") || 
				    	  !rsmProdutos.getString("nr_situacao_tributaria_pis").equals(rsmProdutos.getString("nr_situacao_tributaria_cofins"))){
				    		HashMap<String, Object> register = new HashMap<String, Object>();
					    	register.put("ERRO", "Dados dos Itens do nota fiscal eletrônica Nº "+nota.getNrNotaFiscal()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : CST do PIS e COFINS devem ser iguais.");
					    	rsm.addRegister(register);
				    	}
				    }
				    
				    if((vlBasePIS > 0 || vlBasePISSub > 0) && rsmProdutos.getString("nr_situacao_tributaria_pis") != null && !rsmProdutos.getString("nr_situacao_tributaria_pis").equals("")){
				    	if(rsmProdutos.getString("nr_situacao_tributaria_pis").equals("01") && (!Util.formatNumber(prAliquotaPIS, "#.####").equals("1,65") && !Util.formatNumber(prAliquotaPIS, "#.####").equals("0,65"))){
				    		HashMap<String, Object> register = new HashMap<String, Object>();
					    	register.put("ERRO", "Dados dos Itens do nota fiscal eletrônica Nº "+nota.getNrNotaFiscal()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : Se o CST PIS for 01, a Aliquota deve ser 1,65 (Regime Não Cumulativo) ou 0,65 (Regime Cumulativo).");
					    	rsm.addRegister(register);
				    	}
				    }
				    
				    if((vlBaseCOFINS > 0 || vlBaseCOFINSSub > 0) && rsmProdutos.getString("nr_situacao_tributaria_cofins") != null && !rsmProdutos.getString("nr_situacao_tributaria_cofins").equals("")){
				    	if(rsmProdutos.getString("nr_situacao_tributaria_cofins").equals("01") && (!Util.formatNumber(prAliquotaCOFINS, "#.####").equals("7,6") && !Util.formatNumber(prAliquotaCOFINS, "#.####").equals("3,3"))){
				    		HashMap<String, Object> register = new HashMap<String, Object>();
					    	register.put("ERRO", "Dados dos Itens do nota fiscal eletrônica Nº "+nota.getNrNotaFiscal()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : Se o CST COFINS for 01, a Aliquota deve ser 7,60 (Regime Não Cumulativo) ou 3,3 (Regime Cumulativo).");
					    	rsm.addRegister(register);
				    	}
				    }
				    		
				    
					registro += "|C170"+
										  "|" +rsmProdutos.getInt("cd_item")+//Número sequencial do item no documento fiscal
										  "|" +rsmProdutos.getInt("cd_produto_servico")+//Código do item                                                                                              - Codigo proprio do EFD - Implementar
										  "|" +((rsmProdutos.getString("txt_produto_servico") != null) ? rsmProdutos.getString("txt_produto_servico").trim() : "")+//Descrição complementar do item como adotado no documento fiscal -OC
										  "|" +Util.formatNumber(rsmProdutos.getFloat("qt_tributario"), "#.##")+//Quantidade do item
										  "|" +((rsmProdutos.getString("cd_unidade_medida").trim().length() > 6 ) ? rsmProdutos.getString("cd_unidade_medida").trim().substring(0, 6) : rsmProdutos.getString("cd_unidade_medida").trim())+//Unidade do item
										  "|" +Util.formatNumber((rsmProdutos.getFloat("qt_tributario") * rsmProdutos.getFloat("vl_unitario") + rsmProdutos.getFloat("vl_acrescimo") - rsmProdutos.getFloat("vl_desconto")), "#.##")+//Valor total do item (mercadorias ou serviços)
										  "|" +((rsmProdutos.getFloat("vl_desconto") != 0) ? Util.formatNumber(rsmProdutos.getFloat("vl_desconto"), "#.##") : "0,00")+//Valor do desconto comercial 																					-OC
										  "|" +IND_MOV_SIM+//Movimentação física do ITEM/PRODUTO
							              "|" +(cst.length() < 3 ? "0" + cst : cst)+//Código da Situação Tributária referente ao ICMS, conforme a Tabela indicada no item 4.3.1 http://www.cosif.com.br/mostra.asp?arquivo=sinief-CST-anexo
							              "|" +rsmProdutos.getString("nr_codigo_fiscal")+//Código Fiscal de Operação e Prestação
							              "|" +//Código da natureza da operação                                                                                - Codigo do proprio EFD - IMplementar Campo 2 do Registro 0400
							              "|" +Util.formatNumber(vlBaseICMS, "#.##")+//Valor da base de cálculo do ICMS 																		-OC
							              "|" +((rsmProdutos.getFloat("pr_icms") != 0) ? Util.formatNumber(rsmProdutos.getFloat("pr_icms"), "#.##") : "0,00" )+//Alíquota do ICMS 																															-OC
							              "|" +Util.formatNumber(vlICMS, "#.##") +//Valor do ICMS creditado/debitado 											-OC
							              "|" +Util.formatNumber(vlBaseICMSSub, "#.##") +//"0,00" +//Valor da base de cálculo referente à substituição tributária 																																										-OC
							              "|" +Util.formatNumber(prAliquotaICMSSub, "#.##") +//Alíquota do ICMS da substituição tributária na unidade da federação de destino 																																					-OC
							              "|" +Util.formatNumber(vlICMSSub, "#.##") + //"0,00" +//Valor do ICMS referente à substituição tributária 																																													-OC
							              "|" +//Movimentação física do ITEM/PRODUTO 																																																-OC
							              "|" +((vlBaseIPI > 0 || vlBaseIPISub > 0) && rsmProdutos.getString("nr_situacao_tributaria_ipi") != null && !rsmProdutos.getString("nr_situacao_tributaria_ipi").equals("") ? rsmProdutos.getString("nr_situacao_tributaria_ipi") : "") +//Código da Situação Tributária referente ao IPI 																								-OC
							              "|" +//Código de enquadramento legal do IPI 																																																-OC
							              "|" +Util.formatNumber(vlBaseIPI + vlBaseIPISub, "#.##") + //"0,00" +//Valor da base de cálculo do IPI 																																																	-OC
							              "|" +Util.formatNumber(prAliquotaIPI, "#.##") +//Alíquota do IPI 																																																					-OC
							              "|" +Util.formatNumber(vlIPI + vlIPISub, "#.##") +//Valor do IPI creditado/debitado 																																																	-OC
							              "|" +((vlBasePIS > 0 || vlBasePISSub > 0) && rsmProdutos.getString("nr_situacao_tributaria_pis") != null && !rsmProdutos.getString("nr_situacao_tributaria_pis").equals("") ? rsmProdutos.getString("nr_situacao_tributaria_pis") : "99") +//Código da Situação Tributária referente ao PIS. 																																												-OC
							              "|" +Util.formatNumber(vlBasePIS + vlBasePISSub, "#.##") + //"0,00" +//Valor da base de cálculo do PIS 																																																	-OC
							              "|" +Util.formatNumber(prAliquotaPIS, "#.####") +//Alíquota do PIS (em percentual) 																																																	-OC
							              "|" + //"0,000" +//Quantidade  Base de cálculo PIS 																																																	-OC
							              "|" + //"0,0000" +//Alíquota do PIS (em reais) 																																																		-OC
							              "|" +Util.formatNumber(vlPIS + vlPISSub, "#.##") +// "0,00" +//Valor do PIS 																																																						-OC
							              "|" +((vlBaseCOFINS > 0 || vlBaseCOFINSSub > 0) && rsmProdutos.getString("nr_situacao_tributaria_cofins") != null && !rsmProdutos.getString("nr_situacao_tributaria_cofins").equals("")? rsmProdutos.getString("nr_situacao_tributaria_cofins") : "99") +//Código da Situação Tributária referente ao COFINS. 																																											-OC
							              "|" +Util.formatNumber(vlBaseCOFINS + vlBaseCOFINSSub, "#.###") + //"0,00" +//Valor da base de cálculo da COFINS 																																																-OC
							              "|" +Util.formatNumber(prAliquotaCOFINS, "#.####") +//Alíquota do COFINS (em percentual) 																																																-OC
							              "|" + //"0,000" +//Quantidade  Base de cálculo COFINS 																																																-OC
							              "|" + //"0,0000" +//Alíquota da COFINS (em reais) 																																																		-OC
							              "|" +Util.formatNumber(vlCOFINS + vlCOFINSSub, "#.##") + //"0,00" +//Valor da COFINS 																																																					-OC
							              "|" +//Código da conta analítica contábil debitada/creditada 																																												-OC			                                           	              
										  "|" +"\r\n";
					nrLinhaBlocoC++;
					conjuntoRegistro.put("C170", (((Integer) conjuntoRegistro.get("C170")) + 1));
					
				}while(rsmProdutos.next());
				
				
				ResultSetMap rsmCodigosItensNotaFiscal = getItensNotaFiscalContribuicoes(cdEmpresaParticipante, cdNotaFiscal, dtSpedInicial, dtSpedFinal);
				while(rsmCodigosItensNotaFiscal.next()){
					/*
					 * Registro C180 - CONSOLIDAÇÃO DE NOTAS FISCAIS ELETRÔNICAS EMITIDAS PELA PESSOA JURÍDICA (CÓDIGO 55)  OPERAÇÕES DE VENDAS
					 * Nível hierárquico - 3
					   Ocorrência - 1:N
					 */
					registro +=  "|" +"C180" + // REG - Texto fixo contendo C180
					   	         "|" +"55"+// COD_MOD
					   	         "|" +Util.formatDateTime(dtSpedInicial, "ddMMyyyy")+				 // DT_INI - Data inicial das informações
					   	         "|" +Util.formatDateTime(dtSpedFinal, "ddMMyyyy")+  				 // DT_FIN - Data final das informações
					   	         "|" + rsmCodigosItensNotaFiscal.getInt("cd_produto_servico") +
					   	         "|" + rsmCodigosItensNotaFiscal.getString("nr_ncm") +
					   	         "|" +//EX_IPI
					   	         "|" + Util.formatNumber(rsmCodigosItensNotaFiscal.getFloat("vl_total"), "#.####") +
								 "|" +"\r\n";
					nrLinhaBlocoC++;
					conjuntoRegistro.put("C180", (((Integer) conjuntoRegistro.get("C180")) + 1));
					
					
					ResultSetMap rsmCodigosItensPISNotaFiscal = getItensNotaFiscalPISContribuicoes(cdEmpresaParticipante, rsmCodigosItensNotaFiscal.getInt("cd_produto_servico"), dtSpedInicial, dtSpedFinal);
					
					while(rsmCodigosItensPISNotaFiscal.next()){
						/*
						 * Registro C181 - DETALHAMENTO DA CONSOLIDAÇÃO  OPERAÇÕES DE VENDAS  PIS/PASEP
						 * Nível hierárquico - 4
						   Ocorrência - 1:N
						 */
						registro +=  "|" +"C181" + // REG - Texto fixo contendo C181
						   	         "|" + rsmCodigosItensPISNotaFiscal.getString("nr_situacao_tributaria") +// CST_PIS
						   	         "|" + rsmCodigosItensPISNotaFiscal.getString("nr_codigo_fiscal") +//CFOP
						   	         "|" + Util.formatNumber(rsmCodigosItensNotaFiscal.getFloat("vl_total"), "#.####") +//VL_ITEM
						   	         "|" +"\r\n";
						nrLinhaBlocoC++;
						conjuntoRegistro.put("C181", (((Integer) conjuntoRegistro.get("C181")) + 1));
					}
					
					ResultSetMap rsmCodigosItensCOFINSNotaFiscal = getItensNotaFiscalCOFINSContribuicoes(cdEmpresaParticipante, rsmCodigosItensNotaFiscal.getInt("cd_produto_servico"), dtSpedInicial, dtSpedFinal);
					
					while(rsmCodigosItensCOFINSNotaFiscal.next()){
						/*
						 * Registro C185 - DETALHAMENTO DA CONSOLIDAÇÃO  OPERAÇÕES DE VENDAS  COFINS
						 * Nível hierárquico - 4
						   Ocorrência - 1:N
						 */
						registro +=  "|" +"C185" + // REG - Texto fixo contendo C185
									 "|" + rsmCodigosItensCOFINSNotaFiscal.getString("nr_situacao_tributaria") +// CST_COFINS
									 "|" + rsmCodigosItensCOFINSNotaFiscal.getString("nr_codigo_fiscal") +//CFOP
									 "|" + Util.formatNumber(rsmCodigosItensNotaFiscal.getFloat("vl_total"), "#.####") +//VL_ITEM
						   	         "|" +"\r\n";
						nrLinhaBlocoC++;
						conjuntoRegistro.put("C185", (((Integer) conjuntoRegistro.get("C185")) + 1));
					}
					
				}
				
			}
			
			
			ArrayList<Integer> codigosSaida = conjuntoCodigos.get("rsmSaida");
			ArrayList<String> registroSaida = new ArrayList<String>();
			for(Integer cdDocumentoSaida : codigosSaida){
				//Documento
				DocumentoSaida documento = DocumentoSaidaDAO.get(cdDocumentoSaida);
				//Numero do documento
				int nrDoc = 0;
				if(documento.getNrDocumentoSaida() != null && !documento.getNrDocumentoSaida().trim().equals("")){
					nrDoc = Integer.parseInt(Util.limparFormatos(documento.getNrDocumentoSaida(), 'N'));
				}
				String nrDocumento = ((nrDoc > 999999999) ? String.valueOf(nrDoc).substring(0, 9) : String.valueOf(nrDoc));
				//Valor do documento
				String vlTotalDocumento = Util.formatNumber(documento.getVlTotalDocumento(), "#.##");
				
				//Situação do documento
				String stDocumentoFiscal = ST_DOCUMENTO_REGULAR;
				if(documento.getStDocumentoSaida() == 2){
					stDocumentoFiscal = ST_DOCUMENTO_CANCELADO;
				}
				
				//Contas a Receber pertencentes ao documento
				ArrayList<Integer> contasAReceber  = getContasAReceber(cdDocumentoSaida);
				
				//Selecionar o Tipo de Pagamento
				int tpPagamento = -1;
				if(contasAReceber.size() == 1)
					tpPagamento = IND_PAGTO_AVISTA;
				else
					tpPagamento = IND_PAGTO_APRAZO;
				
				//Valor Total do Itens
				float vlProd = DocumentoSaidaServices.getValorAllItens(cdDocumentoSaida);
				
				//Valor de Desconto do documento
				float vlDescontos = DocumentoSaidaServices.getValorDescontosAllItens(cdDocumentoSaida);
				
				//Valor das Aliquotas, Base e ICMS
				float vlBaseICMS = 0;
				float vlICMS = 0;
				float vlBaseICMSSub = 0;
				float vlICMSSub = 0;
				
				float vlBaseIPI = 0;
				float vlIPI = 0;
				float vlBaseIPISub = 0;
				float vlIPISub = 0;
				
				float vlBasePIS = 0;
				float vlPIS = 0;
				float vlBasePISSub = 0;
				float vlPISSub = 0;
				
				float vlBaseCOFINS = 0;
				float vlCOFINS = 0;
				float vlBaseCOFINSSub = 0;
				float vlCOFINSSub = 0;
				
				//ResultSetMap com os itens do documento
				ResultSetMap rsmProdutos = DocumentoSaidaServices.getAllItens(cdDocumentoSaida, true);
				int erroAliq = 0;
				
				if(rsmProdutos.next()){
					vlICMS  	  = rsmProdutos.getFloat("vl_icms_documento");
					vlBaseICMS 	  = rsmProdutos.getFloat("vl_base_icms_documento");
					vlBaseICMSSub = rsmProdutos.getFloat("vl_base_icms_substituto_documento");
					vlICMSSub 	  = rsmProdutos.getFloat("vl_icms_substituto_documento");
					
					vlBaseIPI 	  = rsmProdutos.getFloat("vl_base_ipi_documento");
					vlIPI 		  = rsmProdutos.getFloat("vl_ipi_documento");
					vlBaseIPISub  = rsmProdutos.getFloat("vl_base_ipi_substituto_documento");
					vlIPISub 	  = rsmProdutos.getFloat("vl_ipi_substituto_documento");
					
					vlBasePIS 	  = rsmProdutos.getFloat("vl_base_pis_documento");
					vlPIS 		  = rsmProdutos.getFloat("vl_pis_documento");
					vlBasePISSub  = rsmProdutos.getFloat("vl_base_pis_substituto_documento");
					vlPISSub 	  = rsmProdutos.getFloat("vl_pis_substituto_documento");
					
					vlBaseCOFINS 	  = rsmProdutos.getFloat("vl_base_cofins_documento");
					vlCOFINS 		  = rsmProdutos.getFloat("vl_cofins_documento");
					vlBaseCOFINSSub  = rsmProdutos.getFloat("vl_base_cofins_substituto_documento");
					vlCOFINSSub 	  = rsmProdutos.getFloat("vl_cofins_substituto_documento");
					
					erroAliq = rsmProdutos.getInt("ErroAliq");
				}
				else{
					HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Documento Nº "+documento.getNrDocumentoSaida()+" sem produtos!");
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	
			    	continue;
				}
				
				/************************************** VALIDACAO *******************************************************/
				//Codigo do cliente
				if(String.valueOf(documento.getCdCliente())==null || String.valueOf(documento.getCdCliente()).trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Codigo do cliente ";
				//Numero do Documento
				if(documento.getNrDocumentoSaida() ==null || documento.getNrDocumentoSaida().trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Numero ";
				//Data de Emissao do Documento
				if(documento.getDtEmissao() == null || String.valueOf(documento.getDtEmissao()).trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Data de emissão ";
				
				//Valor Total do Documento
				if(String.valueOf(documento.getVlTotalDocumento()) == null || String.valueOf(documento.getVlTotalDocumento()).trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor total ";
				
				if(!dsValidacao.equals("")){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados do documento " + documento.getNrDocumentoSaida() + " faltando: "+dsValidacao);
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	
			    	//entrouDs = true;
			    }
			    
				
				
			    /*Validacao*/
			    boolean duplicidade = false;
			    String registroAtual = IND_OPER_SAIDA + nrDocumento + "01" + stDocumentoFiscal;
			    for(int ind = 0; ind < registroSaida.size(); ind++){
			    	if(registroAtual.equals(registroSaida.get(ind))){
			    		duplicidade = true;
			    		break;
			    	}
			    }
			    
			    if(duplicidade){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados do documento de saida " + documento.getNrDocumentoSaida() + ": Duplicidade com outro documento.");
			    	rsm.addRegister(register);
			    	//entrouDs = true;
			    }
			    else{
			    	registroSaida.add(registroAtual);
			    }
			    
			    GregorianCalendar data = new GregorianCalendar();
			    data.set(Calendar.YEAR, 2000);
			    data.set(Calendar.MONTH, 0);
			    data.set(Calendar.DAY_OF_MONTH, 1);
			    if(documento.getDtDocumentoSaida() == null || documento.getDtDocumentoSaida().before(data)){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados do documento de saida " + documento.getNrDocumentoSaida() + ": Data do documento não pode ser anterior a 01/01/2000");
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	
			    	//entrouDs = true;
			    }
			    
			    if(documento.getDtDocumentoSaida() == null || documento.getDtEmissao() == null || documento.getDtDocumentoSaida().before(documento.getDtEmissao())){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados do documento de saida " + documento.getNrDocumentoSaida() + ": Data do documento não pode ser anterior a Data de Emissão");
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	
			    	//entrouDs = true;
			    }
			    
			    if(documento.getDtDocumentoSaida() == null || documento.getDtEmissao() == null || dtSpedFinal.before(documento.getDtDocumentoSaida()) || dtSpedFinal.before(documento.getDtEmissao())){
					HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados do documento de saida " + documento.getNrDocumentoSaida() + ": Data menor do que a data final de emissão do SPED");
			    	rsm.addRegister(register);
			    
			    	//entrouDs = true;
			     }
			    
			    if(erroAliq > 0){
			    	String imposto = "";
			    	switch(erroAliq){
			    		case 1:
			    			imposto = "ICMS";
			    			break;
			    		case 2:
			    			imposto = "IPI";
			    			break;
			    		case 3:
			    			imposto = "II";
			    			break;
			    		case 4:
			    			imposto = "PIS";
			    			break;
			    		case 5:
			    			imposto = "COFINS";
			    			break;
			    	}
			    	
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados do documento de saida " + documento.getNrDocumentoSaida() + ": Problema nas aliquotas do imposto " + imposto);
			    	rsm.addRegister(register);
			    
			    	//entrouDs = true;
			    }
			    if(!entrouDs && !entrouDs2){
					registro += "|C100" + //REG
							  "|" +IND_OPER_SAIDA+
							  "|" +IND_EMIT_PROPRIA+
							  "|" +documento.getCdCliente()+                                                                 //Cód participante
		//					  "|" +((cdNota1A > 0) ? "01" : (cdNfe > 0) ? "55" : "")+	                                                        //Cód modelo do documento fiscal            - VERIFICAR PARA QUANDO NAO FOR 1A NEM Nfe
							  "|" +"01"+	                                                        //Cód modelo do documento fiscal            - VERIFICAR PARA QUANDO NAO FOR 1A NEM Nfe
							  "|" +stDocumentoFiscal+										                                                                    //Cód da situação do documento fiscal       - Tabela 4.1.2
		//					  "|" +((nfe != null) ? nfe.getNrSerie() : "")+	                                                                    //Serie do documento fiscal                 											   -OC
							  "|" + "001" + //Serie do documento
							  "|" +nrDocumento+	                                                        //Numero do documento fiscal            
		//					  "|" +((cdNfe > 0) ? Integer.parseInt(nfe.getNrChaveAcesso().substring(3)) : 0)+                                    //Chave da nota fiscal online
							  "|" + //chave de acesso
							  "|" +(documento.getDtEmissao() != null ? Util.formatDateTime(documento.getDtEmissao(), "ddMMyyyy") : "")+			                                                        //Data da emissão
							  "|" +(documento.getDtDocumentoSaida() != null ? Util.formatDateTime(documento.getDtDocumentoSaida(), "ddMMyyyy") : "")+			                                                    //Data de entrada ou saida 											   -OC
							  "|" +vlTotalDocumento+                                    //Valor total do documento fiscal           
							  "|" +((tpPagamento == 1) ? IND_PAGTO_APRAZO : (tpPagamento == 0) ? IND_PAGTO_AVISTA : "")+		//Indicador do tipo de pagamento
							  "|" +(Util.formatNumber((vlDescontos <= 0 ? 0 : vlDescontos), "#.##").equals("-0") ? "0" : Util.formatNumber((vlDescontos <= 0 ? 0 : vlDescontos), "#.##"))+		         							//Valor total do desconto 																															-OC
							  "|" +	//Valor abatimento fiscal                    - VERIFICAR 																																				-OC
							  "|" +Util.formatNumber(vlProd, "#.##")+										                    //Valor total da mercadoria e serviços 																		-OC
							  "|" +(documento.getTpFrete() == DocumentoSaidaServices.FRT_SEM_COBRANCA ? IND_FRT_SEMFRETE : documento.getTpFrete())+                                                                          //Indicador do frete
							  "|" +Util.formatNumber(documento.getVlFrete(), "#.##")+									                            //Valor do frete                             - No Momento SEM FRETE como padrão 		-OC
							  "|" +Util.formatNumber(documento.getVlSeguro(), "#.##")+									                            //Valor do seguro do frete 																-OC
							  "|" +"0,00"+				   									                                                //Valor de outras despesas                   - SEM VALOR DE OUTRAS DESPESAS  							-OC
							  "|" +Util.formatNumber(vlBaseICMS, "#.##")+	                                    //Valor da base de calculo do icms 														-OC
							  "|" +Util.formatNumber(vlICMS, "#.##")+	                                    //Valor do icms 																		-OC
							  "|" +Util.formatNumber(vlBaseICMSSub, "#.##")+									    //Valor da base de calculo do icms substituicao tributaria                                                       - VERIFICAR SE EH PARA USAR 						-OC
							  "|" +Util.formatNumber(vlICMSSub, "#.##")+									    //Valor do ICMS retido por substituição tributaria                                                               - VERIFICAR SE EH PARA USAR 						-OC
							  "|" +Util.formatNumber(vlIPI, "#.##")+	                                    //Valor total do IPI 																			-OC
							  "|" +Util.formatNumber(vlPIS, "#.##")+									                                                                        //Valor total do PIS 																			-OC
							  "|" +Util.formatNumber(vlCOFINS, "#.##")+									                                                                        //Valor total da confins 																		-OC
							  "|" +Util.formatNumber(vlPISSub, "#.##")+									                                                                        //Valor total do pis retido por substituicao tributaria 										-OC
							  "|" +Util.formatNumber(vlCOFINSSub, "#.##")+									                                                                        //Valor total da CONFINS retido por substituicao tributaria 									-OC
							  "|" +"\r\n";
					
					nrLinhaBlocoC++;
					conjuntoRegistro.put("C100", (((Integer) conjuntoRegistro.get("C100")) + 1));
			    }
				
				float vlDescontoItens = 0;
				int cdItem = 1;
				do{
					/*
					 * Registro C170 - Refernte a C100 - Itens do documento
					 */
					/************************************** VALIDACAO *******************************************************/
					//Quantidade do Item
					if(String.valueOf(rsmProdutos.getFloat("qt_saida")) == null || String.valueOf(rsmProdutos.getFloat("qt_saida")).trim().equals(""))
				    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Quantidade ";
					
					//Unidade de Medida
					if(rsmProdutos.getString("cd_unidade_medida") == null || rsmProdutos.getString("cd_unidade_medida").trim().equals("") || rsmProdutos.getInt("cd_unidade_medida") == 0){
						dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Unidade de medida ";
					}
					//Valor Unitario
					if(String.valueOf(rsmProdutos.getFloat("vl_unitario")) == null || String.valueOf(rsmProdutos.getFloat("vl_unitario")).trim().equals(""))
				    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor unitario do item ";
					//CFOP
					if(rsmProdutos.getString("nr_codigo_fiscal") == null || rsmProdutos.getString("nr_codigo_fiscal").trim().equals(""))
				    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CFOP ";
					//Tributação
				    if(!dsValidacao.equals("")){
				    	HashMap<String, Object> register = new HashMap<String, Object>();
				    	register.put("ERRO", "Dados dos Itens do documento de saída Nº "+documento.getNrDocumentoSaida()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" faltando: "+dsValidacao);
				    	rsm.addRegister(register);
				    
				    	dsValidacao = "";
				    	//entrouDs = true;
				    }
				    
				    vlICMS  	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_icms"));
					vlBaseICMS 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_base_icms"));
					vlBaseICMSSub = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_base_icms") : 0);
					vlICMSSub 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_icms") : 0);
					
					vlBaseIPI 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_base_ipi"));
					vlIPI 		  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_ipi"));
					vlBaseIPISub  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_base_ipi") : 0);
					vlIPISub 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_ipi") : 0);
					
					vlBasePIS 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_base_pis"));
					vlPIS 		  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_pis"));
					vlBasePISSub  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_base_pis") : 0);
					vlPISSub 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_pis") : 0);
					
					vlBaseCOFINS 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_base_cofins"));
					vlCOFINS 		  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_cofins"));
					vlBaseCOFINSSub  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_base_cofins") : 0);
					vlCOFINSSub 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_cofins") : 0);
					
					
					float prAliquotaICMSSub = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("pr_icms") : 0);
				    float prAliquotaIPI 	= rsmProdutos.getFloat("pr_ipi");
				    float prAliquotaPIS 	= rsmProdutos.getFloat("pr_pis");
				    float prAliquotaCOFINS  = rsmProdutos.getFloat("pr_cofins");
					
				    vlDescontoItens += rsmProdutos.getFloat("vl_desconto");
				    
				    String cst = ((vlBaseICMS > 0 || vlBaseICMSSub > 0) && rsmProdutos.getString("nr_situacao_tributaria_icms") != null && !rsmProdutos.getString("nr_situacao_tributaria_icms").equals("") ? rsmProdutos.getString("nr_situacao_tributaria_icms") : "041");
				    
				    String parteCst = cst.substring(1);
				    
				    if((parteCst.equals("00") || 
				    	parteCst.equals("10") || 
				    	parteCst.equals("20") || 
				    	parteCst.equals("70")) &&
				    	rsmProdutos.getFloat("pr_icms") == 0){
				    	
				    	HashMap<String, Object> register = new HashMap<String, Object>();
				    	register.put("AVISO", "Dados dos Itens do documento de saída Nº "+documento.getNrDocumentoSaida()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : Se o CST termina em 00, 10, 20 ou 70 a Aliquota deve ser maior do que zero.");
				    	rsm2.addRegister(register);
				    
				    }
				    
				    if((vlBasePIS > 0 || vlBasePISSub > 0) && rsmProdutos.getString("nr_situacao_tributaria_pis") != null && !rsmProdutos.getString("nr_situacao_tributaria_pis").equals("") 
				    || (vlBaseCOFINS > 0 || vlBaseCOFINSSub > 0) && rsmProdutos.getString("nr_situacao_tributaria_cofins") != null && !rsmProdutos.getString("nr_situacao_tributaria_cofins").equals("")){
				    	if(vlBasePIS != vlBaseCOFINS){
				    		HashMap<String, Object> register = new HashMap<String, Object>();
					    	register.put("ERRO", "Dados dos Itens do documento de saída Nº "+documento.getNrDocumentoSaida()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : Valor das bases de cáuculo do PIS e COFINS devem ser iguais.");
					    	rsm.addRegister(register);
				    	}
				    	else if(vlBasePISSub != vlBaseCOFINSSub){
				    		HashMap<String, Object> register = new HashMap<String, Object>();
					    	register.put("ERRO", "Dados dos Itens do documento de saída Nº "+documento.getNrDocumentoSaida()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : Valor das bases de cáuculo do PIS e COFINS devem ser iguais.");
					    	rsm.addRegister(register);
				    	}
				    	
				    	if(rsmProdutos.getString("nr_situacao_tributaria_pis") == null || rsmProdutos.getString("nr_situacao_tributaria_pis").equals("")|| 
				    	   rsmProdutos.getString("nr_situacao_tributaria_cofins") == null || rsmProdutos.getString("nr_situacao_tributaria_cofins").equals("") || 
				    	  !rsmProdutos.getString("nr_situacao_tributaria_pis").equals(rsmProdutos.getString("nr_situacao_tributaria_cofins"))){
				    		HashMap<String, Object> register = new HashMap<String, Object>();
					    	register.put("ERRO", "Dados dos Itens do documento de saída Nº "+documento.getNrDocumentoSaida()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : CST do PIS e COFINS devem ser iguais.");
					    	rsm.addRegister(register);
				    	}
				    }
				    
				    if(Util.arredondar(rsmProdutos.getFloat("qt_saida"), (rsmProdutos.getInt("qt_precisao_custo") > 5 ? 5 : rsmProdutos.getInt("qt_precisao_custo"))) == 0){
				    	HashMap<String, Object> register = new HashMap<String, Object>();
				    	register.put("ERRO", "Dados dos Itens do documento de saída Nº "+documento.getNrDocumentoSaida()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : Quantidade do item zerada.");
				    	rsm.addRegister(register);
				    }
				    
				    if((vlBasePIS > 0 || vlBasePISSub > 0) && rsmProdutos.getString("nr_situacao_tributaria_pis") != null && !rsmProdutos.getString("nr_situacao_tributaria_pis").equals("")){
				    	if(rsmProdutos.getString("nr_situacao_tributaria_pis").equals("01") && (!Util.formatNumber(prAliquotaPIS, "#.####").equals("1,65") && !Util.formatNumber(prAliquotaPIS, "#.####").equals("0,65"))){
				    		HashMap<String, Object> register = new HashMap<String, Object>();
					    	register.put("ERRO", "Dados dos Itens do documento de saída Nº "+documento.getNrDocumentoSaida()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : Se o CST PIS for 01, a Aliquota deve ser 1,65 (Regime Não Cumulativo) ou 0,65 (Regime Cumulativo).");
					    	rsm.addRegister(register);
				    	}
				    }
				    
				    if((vlBaseCOFINS > 0 || vlBaseCOFINSSub > 0) && rsmProdutos.getString("nr_situacao_tributaria_cofins") != null && !rsmProdutos.getString("nr_situacao_tributaria_cofins").equals("")){
				    	if(rsmProdutos.getString("nr_situacao_tributaria_cofins").equals("01") && (!Util.formatNumber(prAliquotaCOFINS, "#.####").equals("7,6") && !Util.formatNumber(prAliquotaPIS, "#.####").equals("3,3"))){
				    		HashMap<String, Object> register = new HashMap<String, Object>();
					    	register.put("ERRO", "Dados dos Itens do documento de saída Nº "+documento.getNrDocumentoSaida()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : Se o CST COFINS for 01, a Aliquota deve ser 7,60 (Regime Não Cumulativo) ou 3,3 (Regime Cumulativo).");
					    	rsm.addRegister(register);
				    	}
				    }
				    
				    if(!entrouDs && !entrouDs2){
					    registro += "|C170"+
											  "|" +cdItem+++//Número sequencial do item no documento fiscal
											  "|" +(rsmProdutos.getString("cd_produto_servico"))+//Código do item                                                                                              - Codigo proprio do EFD - Implementar
											  "|" +((rsmProdutos.getString("txt_produto_servico") != null) ? rsmProdutos.getString("txt_produto_servico").trim() : "")+//Descrição complementar do item como adotado no documento fiscal 												-OC
											  "|" +Util.formatNumber(rsmProdutos.getFloat("qt_saida"), (rsmProdutos.getInt("qt_precisao_custo") > 5 ? 5 : rsmProdutos.getInt("qt_precisao_custo")))+//Quantidade do item
											  "|" +((rsmProdutos.getString("sg_unidade_medida").trim().length() > 6 ) ? rsmProdutos.getString("sg_unidade_medida").trim().substring(0, 6) : rsmProdutos.getString("sg_unidade_medida").trim())+//Unidade do item
											  "|" +Util.formatNumber((rsmProdutos.getFloat("qt_saida") * rsmProdutos.getFloat("vl_unitario") + rsmProdutos.getFloat("vl_acrescimo") - rsmProdutos.getFloat("vl_desconto")), "#.##")+//Valor total do item (mercadorias ou serviços)
											  "|" +((rsmProdutos.getFloat("vl_desconto") != 0) ? Util.formatNumber(rsmProdutos.getFloat("vl_desconto"), "#.##") : "0,00")+//Valor do desconto comercial 																					-OC
											  "|" +IND_MOV_SIM+//Movimentação física do ITEM/PRODUTO
								              "|" +(cst.length() < 3 ? "0" + cst : cst)+//Código da Situação Tributária referente ao ICMS, conforme a Tabela indicada no item 4.3.1 http://www.cosif.com.br/mostra.asp?arquivo=sinief-CST-anexo
								              "|" +rsmProdutos.getString("nr_codigo_fiscal")+//Código Fiscal de Operação e Prestação
								              "|" +//Código da natureza da operação                                                                                - Codigo do proprio EFD - IMplementar Campo 2 do Registro 0400
								              "|" +Util.formatNumber(vlBaseICMS, "#.##")+//Valor da base de cálculo do ICMS 																		-OC
								              "|" +((rsmProdutos.getFloat("pr_icms") != 0) ? Util.formatNumber(rsmProdutos.getFloat("pr_icms"), "#.##") : "0,00" )+//Alíquota do ICMS 																															-OC
								              "|" +Util.formatNumber(vlICMS, "#.##") +//Valor do ICMS creditado/debitado 											-OC
								              "|" +Util.formatNumber(vlBaseICMSSub, "#.##") +//"0,00" +//Valor da base de cálculo referente à substituição tributária 																																										-OC
								              "|" +Util.formatNumber(prAliquotaICMSSub, "#.##") +//Alíquota do ICMS da substituição tributária na unidade da federação de destino 																																					-OC
								              "|" +Util.formatNumber(vlICMSSub, "#.##") + //"0,00" +//Valor do ICMS referente à substituição tributária 																																													-OC
								              "|" +//Movimentação física do ITEM/PRODUTO 																																																-OC
								              "|" +((vlBaseIPI > 0 || vlBaseIPISub > 0) && rsmProdutos.getString("nr_situacao_tributaria_ipi") != null && !rsmProdutos.getString("nr_situacao_tributaria_ipi").equals("") ? rsmProdutos.getString("nr_situacao_tributaria_ipi") : "") +//Código da Situação Tributária referente ao IPI 																								-OC
								              "|" +//Código de enquadramento legal do IPI 																																																-OC
								              "|" +Util.formatNumber(vlBaseIPI + vlBaseIPISub, "#.##") + //"0,00" +//Valor da base de cálculo do IPI 																																																	-OC
								              "|" +Util.formatNumber(prAliquotaIPI, "#.##") +//Alíquota do IPI 																																																					-OC
								              "|" +Util.formatNumber(vlIPI + vlIPISub, "#.##") +//Valor do IPI creditado/debitado 																																																	-OC
								              "|" +((vlBasePIS > 0 || vlBasePISSub > 0) && rsmProdutos.getString("nr_situacao_tributaria_pis") != null && !rsmProdutos.getString("nr_situacao_tributaria_pis").equals("") ? rsmProdutos.getString("nr_situacao_tributaria_pis") : "99") +//Código da Situação Tributária referente ao PIS. 																																												-OC
								              "|" +Util.formatNumber(vlBasePIS + vlBasePISSub, "#.##") + //"0,00" +//Valor da base de cálculo do PIS 																																																	-OC
								              "|" +Util.formatNumber(prAliquotaPIS, "#.####") +//Alíquota do PIS (em percentual) 																																																	-OC
								              "|" + //"0,000" +//Quantidade  Base de cálculo PIS 																																																	-OC
								              "|" + //"0,0000" +//Alíquota do PIS (em reais) 																																																		-OC
								              "|" +Util.formatNumber(vlPIS + vlPISSub, "#.##") +// "0,00" +//Valor do PIS 																																																						-OC
								              "|" +((vlBaseCOFINS > 0 || vlBaseCOFINSSub > 0) && rsmProdutos.getString("nr_situacao_tributaria_cofins") != null && !rsmProdutos.getString("nr_situacao_tributaria_cofins").equals("") ? rsmProdutos.getString("nr_situacao_tributaria_cofins") : "99") +//Código da Situação Tributária referente ao COFINS. 																																											-OC
								              "|" +Util.formatNumber(vlBaseCOFINS + vlBaseCOFINSSub, "#.###") + //"0,00" +//Valor da base de cálculo da COFINS 																																																-OC
								              "|" +Util.formatNumber(prAliquotaCOFINS, "#.####") +//Alíquota do COFINS (em percentual) 																																																-OC
								              "|" + //"0,000" +//Quantidade  Base de cálculo COFINS 																																																-OC
								              "|" + //"0,0000" +//Alíquota da COFINS (em reais) 																																																		-OC
								              "|" +Util.formatNumber(vlCOFINS + vlCOFINSSub, "#.##") + //"0,00" +//Valor da COFINS 																																																					-OC
								              "|" +//Código da conta analítica contábil debitada/creditada 																																												-OC			                                           	              
											  "|" +"\r\n";
						nrLinhaBlocoC++;
						conjuntoRegistro.put("C170", (((Integer) conjuntoRegistro.get("C170")) + 1));
				    }
				    
				    //entrouDs = false;
					
				}while(rsmProdutos.next());
				
			}
			
			
			
			
			ArrayList<Integer> codigosEntrada = conjuntoCodigos.get("rsmEntrada");
			ArrayList<String> registroEntrada = new ArrayList<String>();
			for(Integer cdDocumentoEntrada : codigosEntrada){
				//Documento
				DocumentoEntrada documento = DocumentoEntradaDAO.get(cdDocumentoEntrada);
				//Numero do documento
				int nrDoc = 0;
				if(documento.getNrDocumentoEntrada() != null && !documento.getNrDocumentoEntrada().trim().equals("")){
					nrDoc = Integer.parseInt(Util.limparFormatos(documento.getNrDocumentoEntrada(), 'N'));
				}
				String nrDocumento = ((nrDoc > 999999999) ? String.valueOf(nrDoc).substring(0, 9) : String.valueOf(nrDoc));
				//Valor do documento
				String vlTotalDocumento = Util.formatNumber(documento.getVlTotalDocumento(), "#.##");
				//Valor de Desconto do documento
				String vlDescontos = Util.formatNumber((documento.getVlDesconto() < 0 ? 0 : documento.getVlDesconto()), "#.##");
				//Situação do documento
				String stDocumentoFiscal = ST_DOCUMENTO_REGULAR;
				if(documento.getStDocumentoEntrada() == 2){
					stDocumentoFiscal = ST_DOCUMENTO_CANCELADO;
				}
				
				//Selecionar o Tipo de Pagamento
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				
				//Valor Total do Itens
				float vlProd = DocumentoEntradaServices.getValorAllItens(cdDocumentoEntrada);
				
				//Contas a Receber pertencentes ao documento
				ArrayList<Integer> contasAPagar  = getContasAPagar(cdDocumentoEntrada);
				
				int tpPagamento = -1;
				if(contasAPagar.size() == 1)
					tpPagamento = IND_PAGTO_AVISTA;
				else
					tpPagamento = IND_PAGTO_APRAZO;
							
				
				//ResultSetMap com os itens do documento
				ResultSetMap rsmProdutos = DocumentoEntradaServices.getAllItens(cdDocumentoEntrada, true);
				
				float vlBaseICMS = 0;
				float vlICMS = 0;
				float vlBaseICMSSub = 0;
				float vlICMSSub = 0;
				
				float vlBaseIPI = 0;
				float vlIPI = 0;
				float vlBaseIPISub = 0;
				float vlIPISub = 0;
				
				float vlBasePIS = 0;
				float vlPIS = 0;
				float vlBasePISSub = 0;
				float vlPISSub = 0;
				
				float vlBaseCOFINS = 0;
				float vlCOFINS = 0;
				float vlBaseCOFINSSub = 0;
				float vlCOFINSSub = 0;
				
				int erroAliq = 0;
				
				if(rsmProdutos.next()){
					vlICMS  	  = rsmProdutos.getFloat("vl_icms_documento");
					vlBaseICMS 	  = rsmProdutos.getFloat("vl_base_icms_documento");
					vlBaseICMSSub = rsmProdutos.getFloat("vl_base_icms_substituto_documento");
					vlICMSSub 	  = rsmProdutos.getFloat("vl_icms_substituto_documento");
					
					vlBaseIPI 	  = rsmProdutos.getFloat("vl_base_ipi_documento");
					vlIPI 		  = rsmProdutos.getFloat("vl_ipi_documento");
					vlBaseIPISub  = rsmProdutos.getFloat("vl_base_ipi_substituto_documento");
					vlIPISub 	  = rsmProdutos.getFloat("vl_ipi_substituto_documento");
					
					vlBasePIS 	  = rsmProdutos.getFloat("vl_base_pis_documento");
					vlPIS 		  = rsmProdutos.getFloat("vl_pis_documento");
					vlBasePISSub  = rsmProdutos.getFloat("vl_base_pis_substituto_documento");
					vlPISSub 	  = rsmProdutos.getFloat("vl_pis_substituto_documento");
					
					vlBaseCOFINS 	  = rsmProdutos.getFloat("vl_base_cofins_documento");
					vlCOFINS 		  = rsmProdutos.getFloat("vl_cofins_documento");
					vlBaseCOFINSSub  = rsmProdutos.getFloat("vl_base_cofins_substituto_documento");
					vlCOFINSSub 	  = rsmProdutos.getFloat("vl_cofins_substituto_documento");
					
					erroAliq = rsmProdutos.getInt("ErroAliq");
				}
				else{
					HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Documento de entrada Nº "+documento.getNrDocumentoEntrada()+" sem produtos!");
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	
			    	continue;
				}
				
				
				float result[] 		= getTributoOfDocumentoEntrada(documento.getCdDocumentoEntrada(), documento.getCdEmpresa());
				float vlIcms 		= result[0];
				float vlBaseCalculo = result[1];
				/************************************** VALIDACAO *******************************************************/
				//Numero do Documento
				if(documento.getNrDocumentoEntrada() ==null || documento.getNrDocumentoEntrada().trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Número ";
				//Codigo do cliente
				if(String.valueOf(documento.getCdFornecedor())==null || String.valueOf(documento.getCdFornecedor()).trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Fornecedor ";
				//Data de Emissao do Documento
				if(documento.getDtEmissao() == null || String.valueOf(documento.getDtEmissao()).trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Data de emissão ";
				
				//Valor Total do Documento
				if(String.valueOf(documento.getVlTotalDocumento()) == null || String.valueOf(documento.getVlTotalDocumento()).trim().equals(""))
			    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor total ";
				
			    if(!dsValidacao.equals("")){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados do documento de entrada " + documento.getNrDocumentoEntrada() + " faltando: "+dsValidacao);
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	//entrouDs = true;
			    }
			    if(documento.getDtDocumentoEntrada() == null || documento.getDtEmissao() == null || documento.getDtDocumentoEntrada().before(documento.getDtEmissao())){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados do documento de Entrada " + documento.getNrDocumentoEntrada() + " inválido: Data de Entrada é menor que a Data de Emissão");
			    	rsm.addRegister(register);
			    	//entrouDs = true;
			    }
			    
			    /*Validacao*/
			    boolean duplicidade = false;
			    String registroAtual = IND_OPER_ENTRADA + nrDocumento + "01" + stDocumentoFiscal;
			    for(int ind = 0; ind < registroEntrada.size(); ind++){
			    	if(registroAtual.equals(registroEntrada.get(ind))){
			    		duplicidade = true;
			    		break;
			    	}
			    }
			    
			    if(duplicidade){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados do documento de entrada " + documento.getNrDocumentoEntrada() + ": Duplicidade com outro documento.");
			    	rsm.addRegister(register);
			    	//entrouDs = true;
			    }
			    else{
			    	registroEntrada.add(registroAtual);
			    }
			    
			    GregorianCalendar data = new GregorianCalendar();
			    data.set(Calendar.YEAR, 2000);
			    data.set(Calendar.MONTH, 0);
			    data.set(Calendar.DAY_OF_MONTH, 1);
			    if(documento.getDtDocumentoEntrada() == null || documento.getDtEmissao() == null || documento.getDtDocumentoEntrada().before(data) || documento.getDtEmissao().before(data)){
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados do documento de entrada " + documento.getNrDocumentoEntrada() + ": Data do documento não pode ser anterior a 01/01/2000");
			    	rsm.addRegister(register);
			    
			    	dsValidacao = "";
			    	
			    	//entrouDs = true;
			    }
			    
			    if(documento.getDtDocumentoEntrada() == null || documento.getDtEmissao() == null || dtSpedFinal.before(documento.getDtDocumentoEntrada()) || dtSpedFinal.before(documento.getDtEmissao())){
					HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados do documento de entrada " + documento.getNrDocumentoEntrada() + ": Data menor do que a data final de emissão do SPED");
			    	rsm.addRegister(register);
			    
			    	//entrouDs = true;
			     }
			    
			    if(erroAliq > 0){
			    	String imposto = "";
			    	switch(erroAliq){
			    		case 1:
			    			imposto = "ICMS";
			    			break;
			    		case 2:
			    			imposto = "IPI";
			    			break;
			    		case 3:
			    			imposto = "II";
			    			break;
			    		case 4:
			    			imposto = "PIS";
			    			break;
			    		case 5:
			    			imposto = "COFINS";
			    			break;
			    	}
			    	
			    	HashMap<String, Object> register = new HashMap<String, Object>();
			    	register.put("ERRO", "Dados do documento de entrada " + documento.getNrDocumentoEntrada()+ ": Problema nas aliquotas do imposto " + imposto);
			    	rsm.addRegister(register);
			    
			    	//entrouDs = true;
			    }
			    
			    if(!entrouDs && !entrouDs2){
					registro += "|C100" + //REG
							  "|" +IND_OPER_ENTRADA+
							  "|" +IND_EMIT_PROPRIA+
							  "|" +documento.getCdFornecedor()+                                                                 //Cód participante
		//							  "|" +((cdNota1A > 0) ? "01" : (cdNfe > 0) ? "55" : "")+	                                                        //Cód modelo do documento fiscal            - VERIFICAR PARA QUANDO NAO FOR 1A NEM Nfe
							  "|" +"01"+	                                                        //Cód modelo do documento fiscal            - VERIFICAR PARA QUANDO NAO FOR 1A NEM Nfe
							  "|" +stDocumentoFiscal+										                                                                    //Cód da situação do documento fiscal       - Tabela 4.1.2
		//							  "|" +((nfe != null) ? nfe.getNrSerie() : "")+	                                                                    //Serie do documento fiscal                 - OC
							  "|" + "001" + //Serie do documento
							  "|" +nrDocumento+	                                                        //Numero do documento fiscal            
		//							  "|" +((cdNfe > 0) ? Integer.parseInt(nfe.getNrChaveAcesso().substring(3)) : 0)+                                    //Chave da nota fiscal online
							  "|" + //chave de acesso
							  "|" +(documento.getDtEmissao() != null ? Util.formatDateTime(documento.getDtEmissao(), "ddMMyyyy") : "")+			                                                        //Data da emissão
							  "|" +(documento.getDtDocumentoEntrada() != null ? Util.formatDateTime(documento.getDtDocumentoEntrada(), "ddMMyyyy") : "")+			                                                    //Data de entrada ou saida
							  "|" +vlTotalDocumento+                                    //Valor total do documento fiscal           
							  "|" +tpPagamento+		//Indicador do tipo de pagamento
							  "|" +vlDescontos+		         							//Valor total do desconto
							  "|" +	//Valor abatimento fiscal                    - VERIFICAR
							  "|" +Util.formatNumber(vlProd, "#.##")+										                    //Valor total da mercadoria e serviços
							  "|" +IND_FRT_SEMFRETE+                                                                          //Indicador do frete
							  "|" +Util.formatNumber(documento.getVlFrete(), "#.##")+									                            //Valor do frete                             - No Momento SEM FRETE como padrão
							  "|" +Util.formatNumber(documento.getVlSeguro(), "#.##")+									                            //Valor do seguro do frete
							  "|" +				   									                                                //Valor de outras despesas                   - SEM VALOR DE OUTRAS DESPESAS  
							  "|" +Util.formatNumber(vlBaseICMS, "#.##")+	                                    //Valor da base de calculo do icms 														-OC
							  "|" +Util.formatNumber(vlICMS, "#.##")+	                                    //Valor do icms 																		-OC
							  "|" +Util.formatNumber(vlBaseICMSSub, "#.##")+									    //Valor da base de calculo do icms substituicao tributaria                                                       - VERIFICAR SE EH PARA USAR 						-OC
							  "|" +Util.formatNumber(vlICMSSub, "#.##")+									    //Valor do ICMS retido por substituição tributaria                                                               - VERIFICAR SE EH PARA USAR 						-OC
							  "|" +Util.formatNumber(vlIPI, "#.##")+	                                    //Valor total do IPI 																			-OC
							  "|" +Util.formatNumber(vlPIS, "#.##")+									                                                                        //Valor total do PIS 																			-OC
							  "|" +Util.formatNumber(vlCOFINS, "#.##")+									                                                                        //Valor total da confins 																		-OC
							  "|" +Util.formatNumber(vlPISSub, "#.##")+									                                                                        //Valor total do pis retido por substituicao tributaria 										-OC
							  "|" +Util.formatNumber(vlCOFINSSub, "#.##")+									                                                                        //Valor total da CONFINS retido por substituicao tributaria 									-OC
							  "|" +"\r\n";
					
					nrLinhaBlocoC++;
					conjuntoRegistro.put("C100", (((Integer) conjuntoRegistro.get("C100")) + 1));
			    }
			    
			    //entrouDs = false;
			
				float vlDescontoItens = 0;
				
				int cdItem = 1;
				do{
					/*
					 * Registro C170 - Refernte a C100 - Itens do documento
					 */
					/************************************** VALIDACAO *******************************************************/
					//Quantidade do Item
					if(String.valueOf(rsmProdutos.getFloat("qt_entrada")) == null || String.valueOf(rsmProdutos.getFloat("qt_entrada")).trim().equals(""))
				    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Quantidade ";
					
					//Unidade de Medida
					if(rsmProdutos.getString("cd_unidade_medida") == null || rsmProdutos.getString("cd_unidade_medida").trim().equals("") || rsmProdutos.getInt("cd_unidade_medida") == 0)
				    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Unidade de medida ";
					//Valor Unitario
					if(String.valueOf(rsmProdutos.getFloat("vl_unitario")) == null || String.valueOf(rsmProdutos.getFloat("vl_unitario")).trim().equals(""))
				    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" Valor unitario ";
					//CFOP
					if(rsmProdutos.getString("nr_codigo_fiscal") == null || rsmProdutos.getString("nr_codigo_fiscal").trim().equals(""))
				    	dsValidacao += (dsValidacao.equals("") ? "" : ", ")+" CFOP ";
					//Tributação
					if(!dsValidacao.equals("")){
						HashMap<String, Object> register = new HashMap<String, Object>();
				    	register.put("ERRO", "Dados do Item "+rsmProdutos.getString("nm_produto_servico")+" no documento de entrada Nº "+documento.getNrDocumentoEntrada()+" faltando: "+dsValidacao);
				    	rsm.addRegister(register);
				    
				    	dsValidacao = "";
				    	//entrouDs = true;
					}

					vlICMS  	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_icms"));
					vlBaseICMS 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_base_icms"));
					vlBaseICMSSub = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_base_icms") : 0);
					vlICMSSub 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_icms") : 0);
					
					vlBaseIPI 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_base_ipi"));
					vlIPI 		  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_ipi"));
					vlBaseIPISub  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_base_ipi") : 0);
					vlIPISub 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_ipi") : 0);
					
					vlBasePIS 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_base_pis"));
					vlPIS 		  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_pis"));
					vlBasePISSub  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_base_pis") : 0);
					vlPISSub 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_pis") : 0);
					
					vlBaseCOFINS 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_base_cofins"));
					vlCOFINS 		  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? 0 : rsmProdutos.getFloat("vl_cofins"));
					vlBaseCOFINSSub  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_base_cofins") : 0);
					vlCOFINSSub 	  = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("vl_cofins") : 0);
					
					
					float prAliquotaICMSSub = (rsmProdutos.getInt("st_tributaria_icms") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA || rsmProdutos.getInt("lg_substituicao_icms") == 1 ? rsmProdutos.getFloat("pr_icms") : 0);
				    float prAliquotaIPI 	= rsmProdutos.getFloat("pr_ipi");
				    float prAliquotaPIS 	= rsmProdutos.getFloat("pr_pis");
				    float prAliquotaCOFINS  = rsmProdutos.getFloat("pr_cofins");
					
				    vlDescontoItens += rsmProdutos.getFloat("vl_desconto");
				    
				    
				    String cst = ((vlBaseICMS > 0 || vlBaseICMSSub > 0) && rsmProdutos.getString("nr_situacao_tributaria_icms") != null && !rsmProdutos.getString("nr_situacao_tributaria_icms").equals("") ? rsmProdutos.getString("nr_situacao_tributaria_icms") : "041");
				    
				    String parteCst = cst.substring(1);
				    
				    if((parteCst.equals("00") || 
				    	parteCst.equals("10") || 
				    	parteCst.equals("20") || 
				    	parteCst.equals("70")) &&
				    	rsmProdutos.getFloat("pr_icms") == 0){
				    	
				    	HashMap<String, Object> register = new HashMap<String, Object>();
				    	register.put("AVISO", "Dados dos Itens do documento de entrada Nº "+documento.getNrDocumentoEntrada()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : Se o CST termina em 00, 10, 20 ou 70 a Aliquota deve ser maior do que zero.");
				    	rsm2.addRegister(register);			    
				    }
				    
				    if((vlBasePIS > 0 || vlBasePISSub > 0) && rsmProdutos.getString("nr_situacao_tributaria_pis") != null && !rsmProdutos.getString("nr_situacao_tributaria_pis").equals("") 
				    || (vlBaseCOFINS > 0 || vlBaseCOFINSSub > 0) && rsmProdutos.getString("nr_situacao_tributaria_cofins") != null && !rsmProdutos.getString("nr_situacao_tributaria_cofins").equals("")){
				    	if(vlBasePIS != vlBaseCOFINS){
				    		HashMap<String, Object> register = new HashMap<String, Object>();
					    	register.put("ERRO", "Dados dos Itens do documento de entrada Nº "+documento.getNrDocumentoEntrada()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : Valor das bases de cáuculo do PIS e COFINS devem ser iguais.");
					    	rsm.addRegister(register);
				    	}
				    	else if(vlBasePISSub != vlBaseCOFINSSub){
				    		HashMap<String, Object> register = new HashMap<String, Object>();
					    	register.put("ERRO", "Dados dos Itens do documento de entrada Nº "+documento.getNrDocumentoEntrada()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : Valor das bases de cáuculo do PIS e COFINS devem ser iguais.");
					    	rsm.addRegister(register);
				    	}
				    	
				    	if(rsmProdutos.getString("nr_situacao_tributaria_pis") == null || rsmProdutos.getString("nr_situacao_tributaria_pis").equals("")|| 
				    	   rsmProdutos.getString("nr_situacao_tributaria_cofins") == null || rsmProdutos.getString("nr_situacao_tributaria_cofins").equals("") || 
				    	  !rsmProdutos.getString("nr_situacao_tributaria_pis").equals(rsmProdutos.getString("nr_situacao_tributaria_cofins"))){
				    		HashMap<String, Object> register = new HashMap<String, Object>();
					    	register.put("ERRO", "Dados dos Itens do documento de entrada Nº "+documento.getNrDocumentoEntrada()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : CST do PIS e COFINS devem ser iguais.");
					    	rsm.addRegister(register);
				    	}
				    }
				    
				    if(Util.arredondar(rsmProdutos.getFloat("qt_entrada"), (rsmProdutos.getInt("qt_precisao_custo") > 5 ? 5 : rsmProdutos.getInt("qt_precisao_custo"))) == 0){
				    	HashMap<String, Object> register = new HashMap<String, Object>();
				    	register.put("ERRO", "Dados dos Itens do documento de entrada Nº "+documento.getNrDocumentoEntrada()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : Quantidade do item zerada.");
				    	rsm.addRegister(register);
				    }
				    	
				    if((vlBasePIS > 0 || vlBasePISSub > 0) && rsmProdutos.getString("nr_situacao_tributaria_pis") != null && !rsmProdutos.getString("nr_situacao_tributaria_pis").equals("")){
				    	if(rsmProdutos.getString("nr_situacao_tributaria_pis").equals("01") && (!Util.formatNumber(prAliquotaPIS, "#.####").equals("1,65") && !Util.formatNumber(prAliquotaCOFINS, "#.####").equals("0,65"))){
				    		HashMap<String, Object> register = new HashMap<String, Object>();
					    	register.put("ERRO", "Dados dos Itens do documento de entrada Nº "+documento.getNrDocumentoEntrada()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : Se o CST PIS for 01, a Aliquota deve ser 1,65 (Regime Não Cumulativo) ou 0,65 (Regime Cumulativo).");
					    	rsm.addRegister(register);
				    	}
				    }
				    
				    if((vlBaseCOFINS > 0 || vlBaseCOFINSSub > 0) && rsmProdutos.getString("nr_situacao_tributaria_cofins") != null && !rsmProdutos.getString("nr_situacao_tributaria_cofins").equals("")){
				    	if(rsmProdutos.getString("nr_situacao_tributaria_cofins").equals("01") && (!Util.formatNumber(prAliquotaCOFINS, "#.####").equals("7,6") && !Util.formatNumber(prAliquotaCOFINS, "#.####").equals("3,3"))){
				    		HashMap<String, Object> register = new HashMap<String, Object>();
					    	register.put("ERRO", "Dados dos Itens do documento de entrada Nº "+documento.getNrDocumentoEntrada()+" para o produto "+rsmProdutos.getString("nm_produto_servico")+" : Se o CST COFINS for 01, a Aliquota deve ser 7,60 (Regime Não Cumulativo) ou 3,3 (Regime Cumulativo).");
					    	rsm.addRegister(register);
				    	}
				    }
				    
					if(!entrouDs && !entrouDs2){
						registro += "|C170"+
										  "|" +cdItem+++//Número sequencial do item no documento fiscal
										  "|" +(rsmProdutos.getString("cd_produto_servico"))+//Código do item 
										  "|" +((rsmProdutos.getString("txt_produto_servico") != null) ? rsmProdutos.getString("txt_produto_servico").trim() : "")+//Descrição complementar do item como adotado no documento fiscal 												-OC
										  "|" +Util.formatNumber(rsmProdutos.getFloat("qt_entrada"), 2)+//Quantidade do item
										  "|" +((rsmProdutos.getString("sg_unidade_medida").trim().length() > 6 ) ? rsmProdutos.getString("sg_unidade_medida").trim().substring(0, 6) : rsmProdutos.getString("sg_unidade_medida").trim())+//Unidade do item
										  "|" +Util.formatNumber((Util.arredondar(rsmProdutos.getFloat("qt_entrada"), (rsmProdutos.getInt("qt_precisao_custo") > 5 ? 5 : rsmProdutos.getInt("qt_precisao_custo"))) * rsmProdutos.getFloat("vl_unitario") + rsmProdutos.getFloat("vl_acrescimo") - rsmProdutos.getFloat("vl_desconto")), "#.##")+//Valor total do item (mercadorias ou serviços)
										  "|" +((rsmProdutos.getFloat("vl_desconto") != 0) ? Util.formatNumber(rsmProdutos.getFloat("vl_desconto"), "#.##") : "0,00")+//Valor do desconto comercial 																					-OC
										  "|" +IND_MOV_SIM+//Movimentação física do ITEM/PRODUTO
							              "|" +(cst.length() < 3 ? "0" + cst : cst)+//Código da Situação Tributária referente ao ICMS, conforme a Tabela indicada no item 4.3.1 http://www.cosif.com.br/mostra.asp?arquivo=sinief-CST-anexo
							              "|" +rsmProdutos.getString("nr_codigo_fiscal")+//Código Fiscal de Operação e Prestação
							              "|" +//Código da natureza da operação                                                                                - Codigo do proprio EFD - IMplementar Campo 2 do Registro 0400
							              "|" +Util.formatNumber(vlBaseICMS, "#.##")+//Valor da base de cálculo do ICMS 																		-OC
							              "|" +((rsmProdutos.getFloat("pr_icms") != 0) ? Util.formatNumber(rsmProdutos.getFloat("pr_icms"), "#.##") : "0,00" )+//Alíquota do ICMS 																															-OC
							              "|" +Util.formatNumber(vlICMS, "#.##") +//Valor do ICMS creditado/debitado 											-OC
							              "|" +Util.formatNumber(vlBaseICMSSub, "#.##") +//"0,00" +//Valor da base de cálculo referente à substituição tributária 																																										-OC
							              "|" +Util.formatNumber(prAliquotaICMSSub, "#.##") +//Alíquota do ICMS da substituição tributária na unidade da federação de destino 																																					-OC
							              "|" +Util.formatNumber(vlICMSSub, "#.##") + //"0,00" +//Valor do ICMS referente à substituição tributária 																																													-OC
							              "|" +//Movimentação física do ITEM/PRODUTO 																																																-OC
							              "|" +((vlBaseIPI > 0 || vlBaseIPISub > 0) && rsmProdutos.getString("nr_situacao_tributaria_ipi") != null && !rsmProdutos.getString("nr_situacao_tributaria_ipi").equals("") ? rsmProdutos.getString("nr_situacao_tributaria_ipi") : "") +//Código da Situação Tributária referente ao IPI 																								-OC
							              "|" +//Código de enquadramento legal do IPI 																																																-OC
							              "|" +Util.formatNumber(vlBaseIPI + vlBaseIPISub, "#.##") + //"0,00" +//Valor da base de cálculo do IPI 																																																	-OC
							              "|" +Util.formatNumber(prAliquotaIPI, "#.##") +//Alíquota do IPI 																																																					-OC
							              "|" +Util.formatNumber(vlIPI + vlIPISub, "#.##") +//Valor do IPI creditado/debitado 																																																	-OC
							              "|" +((vlBasePIS > 0 || vlBasePISSub > 0) && rsmProdutos.getString("nr_situacao_tributaria_pis") != null && !rsmProdutos.getString("nr_situacao_tributaria_pis").equals("") ? rsmProdutos.getString("nr_situacao_tributaria_pis") : "99") +//Código da Situação Tributária referente ao PIS. 																																												-OC
							              "|" +Util.formatNumber(vlBasePIS + vlBasePISSub, "#.##") + //"0,00" +//Valor da base de cálculo do PIS 																																																	-OC
							              "|" +Util.formatNumber(prAliquotaPIS, "#.####") +//Alíquota do PIS (em percentual) 																																																	-OC
							              "|" +//"0,000" +//Quantidade  Base de cálculo PIS 																																																	-OC
							              "|" + //"0,0000" +//Alíquota do PIS (em reais) 																																																		-OC
							              "|" +Util.formatNumber(vlPIS + vlPISSub, "#.##") +// "0,00" +//Valor do PIS 																																																						-OC
							              "|" +((vlBaseCOFINS > 0 || vlBaseCOFINSSub > 0) && rsmProdutos.getString("nr_situacao_tributaria_cofins") != null &&  !rsmProdutos.getString("nr_situacao_tributaria_cofins").equals("")? rsmProdutos.getString("nr_situacao_tributaria_cofins") : "99") +//Código da Situação Tributária referente ao COFINS. 																																											-OC
							              "|" +Util.formatNumber(vlBaseCOFINS + vlBaseCOFINSSub, "#.###") + //"0,00" +//Valor da base de cálculo da COFINS 																																																-OC
							              "|" +Util.formatNumber(prAliquotaCOFINS, "#.####") +//Alíquota do COFINS (em percentual) 																																																-OC
							              "|" +//"0,000" +//Quantidade  Base de cálculo COFINS 																																																-OC
							              "|" + //"0,0000" +//Alíquota da COFINS (em reais) 																																																		-OC
							              "|" +Util.formatNumber(vlCOFINS + vlCOFINSSub, "#.##") + //"0,00" +//Valor da COFINS 																																																					-OC
							              "|" +//Código da conta analítica contábil debitada/creditada 																																												-OC			                                           	              
										  "|" +"\r\n";
						nrLinhaBlocoC++;
						conjuntoRegistro.put("C170", (((Integer) conjuntoRegistro.get("C170")) + 1));
					}
					
					//entrouDs = false;
					
				}while(rsmProdutos.next());
			}
			
			
			//MODIFICAR PARA SOMENTE AQUELES RELACIONADOS A PIS/PASEP e COFINS
			try{
				Connection connect = Conexao.conectar();
				PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
											 "WHERE CAST(dt_registro_ecf AS DATE) BETWEEN ? AND ? " +
											 "  AND tp_registro_ecf = \'C400\' " +
											 "ORDER BY dt_registro_ecf, cd_registro_ecf ");
				pstmt.setTimestamp(1, new Timestamp(dtSpedInicial.getTimeInMillis()));
				pstmt.setTimestamp(2, new Timestamp(dtSpedFinal.getTimeInMillis()));
				
				PreparedStatement pstmtEcfSped = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
						                                                  "WHERE cd_registro_ecf = ?");
				
				PreparedStatement pstmtEcfSpedAux = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
																			"WHERE cd_registro_ecf > ? AND tp_registro_ecf LIKE 'C4%'");
				
				ResultSetMap rsmEcf = new ResultSetMap(pstmt.executeQuery());
				int cont = 0;
				while(rsmEcf.next()){
					int cdRegEcf = rsmEcf.getInt("cd_registro_ecf");
					String tokens[] = rsmEcf.getString("txt_registro_ecf").split("C405");
					String c400 = tokens[0].substring(0, tokens[0].length()-1);
		            String c405 = "|C405" + tokens[1];
					registro += c400 +"\r\n";
					registro += c405 +"\r\n";
					nrLinhaBlocoC++;
					nrLinhaBlocoC++;
					conjuntoRegistro.put("C400", (((Integer) conjuntoRegistro.get("C400")) + 1));
					conjuntoRegistro.put("C405", (((Integer) conjuntoRegistro.get("C405")) + 1));
					boolean proxEcf = false;
					while(!proxEcf){
						pstmtEcfSped.setInt(1, ++cdRegEcf);
						ResultSetMap rsmEcfAux = new ResultSetMap(pstmtEcfSped.executeQuery());
						if(rsmEcfAux.next()){
							if(rsmEcfAux.getString("tp_registro_ecf").startsWith("C4") && !rsmEcfAux.getString("tp_registro_ecf").equals("C400")){
								if((rsmEcfAux.getString("tp_registro_ecf").equals("C460") || rsmEcfAux.getString("tp_registro_ecf").equals("C470"))){
									if(tpPerfilEmpresa.equals(IND_PERFIL_A)){
										String regFinal = "";
										if(rsmEcfAux.getString("tp_registro_ecf").equals("C460")){
											String reg = rsmEcfAux.getString("txt_registro_ecf");
											String[] regCons = reg.split("\\|");
											if(regCons.length >= 9){
												boolean cpfCnpjInvalido = ((regCons[9].length() == 14 ? !Util.isCNPJ(regCons[9]) : true) && (regCons[9].length() == 11 ? !Util.isCpfValido(regCons[9]) : true));
												for(int i = 0; i < regCons.length; i++){
													if(i != 9 || !cpfCnpjInvalido)
														regFinal += regCons[i] + "|";
													else if(i == 9 && cpfCnpjInvalido){
														regFinal += "|";
													}
												}
											}
											else{
												regFinal = rsmEcfAux.getString("txt_registro_ecf");
											}
											char[] contagemPipes = regFinal.toCharArray();
											int contPipes = 0;
											for(int i = 0;i<contagemPipes.length;i++){
												if(contagemPipes[i] == '|'){
													contPipes++;
												}
											}
											if(contPipes <= 11){
												for(int i = 0; i < 11 - contPipes; i++){
													regFinal += "|";
												}
											}
										}
										else{
											regFinal = rsmEcfAux.getString("txt_registro_ecf");
										}
										registro += regFinal.replaceAll("00000000000000", "") +"\r\n";
										nrLinhaBlocoC++;
										conjuntoRegistro.put(rsmEcfAux.getString("tp_registro_ecf"), (((Integer) conjuntoRegistro.get(rsmEcfAux.getString("tp_registro_ecf"))) + 1));
									}
								}
								else if(rsmEcfAux.getString("tp_registro_ecf").equals("C425")){
									if(tpPerfilEmpresa.equals(IND_PERFIL_B)){
										registro += rsmEcfAux.getString("txt_registro_ecf") +"\r\n";
										nrLinhaBlocoC++;
										conjuntoRegistro.put(rsmEcfAux.getString("tp_registro_ecf"), (((Integer) conjuntoRegistro.get(rsmEcfAux.getString("tp_registro_ecf"))) + 1));
									}
								}
								else if(rsmEcfAux.getString("tp_registro_ecf").equals("C495")){
//									if(!hasC425){
//										registro += rsmEcfAux.getString("txt_registro_ecf") +"\r\n";
//										nrLinhaBlocoC++;
//										conjuntoRegistro.put(rsmEcfAux.getString("tp_registro_ecf"), (((Integer) conjuntoRegistro.get(rsmEcfAux.getString("tp_registro_ecf"))) + 1));
//									}
								}
								else{
									registro += rsmEcfAux.getString("txt_registro_ecf") +"\r\n";
									nrLinhaBlocoC++;
									conjuntoRegistro.put(rsmEcfAux.getString("tp_registro_ecf"), (((Integer) conjuntoRegistro.get(rsmEcfAux.getString("tp_registro_ecf"))) + 1));
								}
								
							}
							else
								proxEcf = true;
						}
						else{
							pstmtEcfSpedAux.setInt(1, cdRegEcf);
							ResultSetMap rsmEcfAuxAux = new ResultSetMap(pstmtEcfSpedAux.executeQuery());
							if(!rsmEcfAuxAux.next())
								proxEcf = true;
						}
					}
					
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			
			
			
			
			
		}

		nrLinhaBlocoC++;
		/*
		 * Registro C990 - ENCERRAMENTO DO BLOCO C
		 */
		registro          +=  "|C990" +
							 "|" + nrLinhaBlocoC+
							 "|" + "\r\n";
		
		conjuntoRegistro.put("C990", (((Integer) conjuntoRegistro.get("C990")) + 1));
		
		// ******************************** BLOCO D ***************************************************************************************************************				
		
		/*
		 * Abertura do Bloco D - Registro D001
		 */
		registro  += "|D001" + 													 // REG - Texto fixo contendo E001
				  			  "|" + IND_MOV_SEM_DADOS+							 // IND_MOV - indicador de movimento
				  			  "|" + "\r\n";
		nrLinhaBlocoD++;
		conjuntoRegistro.put("D001", (((Integer) conjuntoRegistro.get("D001")) + 1));
		
		nrLinhaBlocoD++;
		
		/*
		 * Registro D990 - ENCERRAMENTO DO BLOCO D
		 */
		registro          +=  "|D990" +
							 "|" + nrLinhaBlocoD+
							 "|" + "\r\n";
		
		conjuntoRegistro.put("D990", (((Integer) conjuntoRegistro.get("D990")) + 1));
					
					
				
		
		// ******************************** BLOCO F ***************************************************************************************************************				
		
		/*
		 * BLOCO F - F001 - Abertura
		 * Nível hierárquico - 1
		   Ocorrência  um por arquivo
		 */
//		indicadorMovimento = getPossuiMovimentoF();
		registro +=  "|" +"F001" + // REG - Texto fixo contendo F001
		   	         "|" +IND_MOV_SEM_DADOS+// IND_MOV - indicador de movimento
					 "|" +"\r\n";
		nrLinhaBlocoF++;
		conjuntoRegistro.put("F001", (((Integer) conjuntoRegistro.get("F001")) + 1));

		nrLinhaBlocoF++;
		
		/*
		 * Registro F990 - ENCERRAMENTO DO BLOCO F
		 */
		registro          +=  "|F990" +
							 "|" + nrLinhaBlocoF+
							 "|" + "\r\n";
		
		conjuntoRegistro.put("F990", (((Integer) conjuntoRegistro.get("F990")) + 1));
					
		
		// ******************************** BLOCO I ***************************************************************************************************************
		
		// ******************************** BLOCO M ***************************************************************************************************************
		/*
		 * BLOCO M - M001 - Abertura
		 * Nível hierárquico - 1
		   Ocorrência  um por arquivo
		 */
//		indicadorMovimento = getPossuiMovimentoM();
		registro +=  "|" +"M001" + // REG - Texto fixo contendo M001
		   	         "|" +indicadorMovimento +// IND_MOV - indicador de movimento
					 "|" +"\r\n";
		nrLinhaBlocoM++;
		conjuntoRegistro.put("M001", (((Integer) conjuntoRegistro.get("M001")) + 1));
		
		int cdTributoPIS 	= ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_PIS", 0);
		int cdTributoCOFINS = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_COFINS", 0);
//		
		ResultSetMap registroPisM100    = getDocumentoEntradaSaidaContribuicoesDoPeriodo(cdEmpresa, dtSpedInicial, dtSpedFinal, 0, cdTributoPIS);
		ResultSetMap registroPisM210    = getDocumentoEntradaSaidaContribuicoesDoPeriodo(cdEmpresa, dtSpedInicial, dtSpedFinal, 1, cdTributoPIS);
		ResultSetMap registroCofinsM500 = getDocumentoEntradaSaidaContribuicoesDoPeriodo(cdEmpresa, dtSpedInicial, dtSpedFinal, 0, cdTributoCOFINS);
		ResultSetMap registroCofinsM610 = getDocumentoEntradaSaidaContribuicoesDoPeriodo(cdEmpresa, dtSpedInicial, dtSpedFinal, 1, cdTributoCOFINS);
		while(registroPisM100.next()){
			registro +=  "|" +"M100" + // REG - Texto fixo contendo M100
			   	         "|" +registroPisM100.getString("nr_tipo_credito")+// COD_CRED
			   	         "|" + "0" +//IND_CRED_ORI
			   	         "|" + Util.formatNumber(registroPisM100.getFloat("vl_base_calculo"), "#.##") +
			   	         "|" + Util.formatNumber(registroPisM100.getFloat("pr_aliquota"), "#.####") +
			   	         "|" +
			   	         "|" +
			   	         "|" + Util.formatNumber(registroPisM100.getFloat("vl_base_calculo") * (registroPisM100.getFloat("pr_aliquota") / 100), "#.##") +
			   	         "|" + Util.formatNumber(0, "#.##") +
			   	         "|" + Util.formatNumber(0, "#.##") +
			   	         "|" + Util.formatNumber(0, "#.##") +
			   	         "|" + Util.formatNumber(registroPisM100.getFloat("vl_base_calculo") * (registroPisM100.getFloat("pr_aliquota") / 100), "#.##") +
			   	         "|" + "0" +
			   	         "|" + Util.formatNumber(0, "#.##") +
			   	         "|" + Util.formatNumber(0, "#.##") +
						 "|" +"\r\n";
			nrLinhaBlocoM++;
			conjuntoRegistro.put("M100", (((Integer) conjuntoRegistro.get("M100")) + 1));
		}
		
		registro +=  "|" +"M200" + //REG - Texto fixo contendo M200
		   	         "|" +"0,00"+//VL_TOT_CONT_NC_PER
		   	         "|" +"0,00"+//VL_TOT_CRED_DESC
		   	         "|" +"0,00"+//VL_TOT_CRED_DESC_ANT
		   	         "|" +"0,00"+//VL_TOT_CONT_NC_DEV
		   	         "|" +"0,00"+//VL_RET_NC
		   	         "|" +"0,00"+//VL_OUT_DED_NC
		   	         "|" +"0,00"+//VL_CONT_NC_REC
		   	         "|" +"0,00"+//VL_TOT_CONT_CUM_PER
		   	         "|" +"0,00"+//VL_RET_CUM
		   	         "|" +"0,00"+//VL_OUT_DED_CUM
		   	         "|" +"0,00"+//VL_CONT_CUM_REC
		   	         "|" +"0,00"+//VL_TOT_CONT_REC
					 "|" +"\r\n";
		nrLinhaBlocoM++;
		conjuntoRegistro.put("M200", (((Integer) conjuntoRegistro.get("M200")) + 1));
		
		while(registroPisM210.next()){
			registro +=  "|" +"M210" + // REG - Texto fixo contendo M210
						 "|01" + //Contribuição não-cumulativa apurada a alíquota básica
			   	         "|" + Util.formatNumber(registroPisM210.getFloat("vl_total_item"), "#.##") +//VL_REC_BRT
			   	         "|" + Util.formatNumber(registroPisM210.getFloat("vl_base_calculo"), "#.##") +//VL_BC_CONT
			   	         "|" + Util.formatNumber(registroPisM210.getFloat("pr_aliquota"), "#.####") +//VL_BC_CONT
			   	         "|" +
			   	         "|" + 
			   	         "|" + Util.formatNumber(0, "#.##") +
			   	         "|" + Util.formatNumber(0, "#.##") +
			   	         "|" + Util.formatNumber(0, "#.##") +
			   	         "|" + 
			   	         "|" + 
			   	         "|" + Util.formatNumber(0, "#.##") +
			   	         "|" +"\r\n";
			nrLinhaBlocoM++;
			conjuntoRegistro.put("M210", (((Integer) conjuntoRegistro.get("M210")) + 1));
		}
		
		while(registroCofinsM500.next()){
			registro +=  "|" +"M500" + // REG - Texto fixo contendo M500
						 "|" +registroCofinsM500.getString("nr_tipo_credito")+// COD_CRED
			   	         "|" + "0" +//IND_CRED_ORI
			   	         "|" + Util.formatNumber(registroCofinsM500.getFloat("vl_base_calculo"), "#.##") +
			   	         "|" + Util.formatNumber(registroCofinsM500.getFloat("pr_aliquota"), "#.####") +
			   	         "|" +
			   	         "|" +
			   	         "|" + Util.formatNumber(registroCofinsM500.getFloat("vl_base_calculo") * (registroCofinsM500.getFloat("pr_aliquota") / 100), "#.##") +
			   	         "|" + Util.formatNumber(0, "#.##") +
			   	         "|" + Util.formatNumber(0, "#.##") +
			   	         "|" + Util.formatNumber(0, "#.##") +
			   	         "|" + Util.formatNumber(registroCofinsM500.getFloat("vl_base_calculo") * (registroCofinsM500.getFloat("pr_aliquota") / 100), "#.##") +
			   	         "|" + "0" +
			   	         "|" + Util.formatNumber(0, "#.##") +
			   	         "|" + Util.formatNumber(0, "#.##") +
					 "|" +"\r\n";
			nrLinhaBlocoM++;
			conjuntoRegistro.put("M500", (((Integer) conjuntoRegistro.get("M500")) + 1));
		}
		
		registro +=  "|" +"M600" + //REG - Texto fixo contendo M600
		   	         "|" +"0,00"+//VL_TOT_CONT_NC_PER
		   	         "|" +"0,00"+//VL_TOT_CRED_DESC
		   	         "|" +"0,00"+//VL_TOT_CRED_DESC_ANT
		   	         "|" +"0,00"+//VL_TOT_CONT_NC_DEV
		   	         "|" +"0,00"+//VL_RET_NC
		   	         "|" +"0,00"+//VL_OUT_DED_NC
		   	         "|" +"0,00"+//VL_CONT_NC_REC
		   	         "|" +"0,00"+//VL_TOT_CONT_CUM_PER
		   	         "|" +"0,00"+//VL_RET_CUM
		   	         "|" +"0,00"+//VL_OUT_DED_CUM
		   	         "|" +"0,00"+//VL_CONT_CUM_REC
		   	         "|" +"0,00"+//VL_TOT_CONT_REC
					 "|" +"\r\n";
		nrLinhaBlocoM++;
		conjuntoRegistro.put("M600", (((Integer) conjuntoRegistro.get("M600")) + 1));
		
		
		while(registroCofinsM610.next()){
			registro +=  "|" +"M610" + // REG - Texto fixo contendo M610
						 "|01" + //Contribuição não-cumulativa apurada a alíquota básica
			   	         "|" + Util.formatNumber(registroCofinsM610.getFloat("vl_total_item"), "#.##") +//VL_REC_BRT
			   	         "|" + Util.formatNumber(registroCofinsM610.getFloat("vl_base_calculo"), "#.##") +//VL_BC_CONT
			   	         "|" + Util.formatNumber(registroCofinsM610.getFloat("pr_aliquota"), "#.####") +//VL_BC_CONT
			   	         "|" +
			   	         "|" + 
			   	         "|" + Util.formatNumber(0, "#.##") +
			   	         "|" + Util.formatNumber(0, "#.##") +
			   	         "|" + Util.formatNumber(0, "#.##") +
			   	         "|" + 
			   	         "|" + 
			   	         "|" + Util.formatNumber(0, "#.##") +
						 "|" +"\r\n";
			nrLinhaBlocoM++;
			conjuntoRegistro.put("M610", (((Integer) conjuntoRegistro.get("M610")) + 1));
		}
		
		nrLinhaBlocoM++;
		
		/*
		 * Registro M990 - ENCERRAMENTO DO BLOCO M
		 */
		registro          +=  "|M990" +
							 "|" + nrLinhaBlocoM+
							 "|" + "\r\n";
		
		conjuntoRegistro.put("M990", (((Integer) conjuntoRegistro.get("M990")) + 1));	
		
		
		// ******************************** BLOCO P ***************************************************************************************************************
//		indicadorMovimento = getPossuiMovimentoF();
		registro +=  "|" +"P001" + // REG - Texto fixo contendo P001
		   	         "|" +IND_MOV_SEM_DADOS+// IND_MOV - indicador de movimento
					 "|" +"\r\n";
		nrLinhaBlocoP++;
		conjuntoRegistro.put("P001", (((Integer) conjuntoRegistro.get("P001")) + 1));
				
		nrLinhaBlocoP++;
		/*
		 * Registro M990 - ENCERRAMENTO DO BLOCO M
		 */
		registro          +=  "|P990" +
							 "|" + nrLinhaBlocoP+
							 "|" + "\r\n";
		
		conjuntoRegistro.put("P990", (((Integer) conjuntoRegistro.get("P990")) + 1));	
		
		
		// ******************************** BLOCO 1 ***************************************************************************************************************
		/*
		 * Abertura do Bloco 1 - Registro 1001
		 */
		registro  += "|1001" + 													 // REG - Texto fixo contendo E001
				  			  "|" + IND_MOV_SEM_DADOS+							 // IND_MOV - indicador de movimento
				  			  "|" + "\r\n";
		nrLinhaBloco1++;
		conjuntoRegistro.put("1001", (((Integer) conjuntoRegistro.get("1001")) + 1));
		
		nrLinhaBloco1++;
		
		/*
		 * Registro 1990 - ENCERRAMENTO DO BLOCO 1
		 */
		registro          +=  "|1990" +
							 "|" + nrLinhaBloco1+
							 "|" + "\r\n";
		
		conjuntoRegistro.put("1990", (((Integer) conjuntoRegistro.get("1990")) + 1));
		
		
		// ******************************** BLOCO 9 ***************************************************************************************************************				
		
		/*
		 * Abertura Bloco 9 - Registro 9001
		 */
		registro        += "|9001" + 													 // REG - Texto fixo contendo 9001
	  			  			  "|" + IND_MOV_DADOS+							 // IND_MOV - indicador de movimento
	  			  			  "|" + "\r\n";
		nrLinhaBloco9++;
		conjuntoRegistro.put("9001", (((Integer) conjuntoRegistro.get("9001")) + 1));
		
		
		int quant9900 = 3;//Inicia de 3 pois depois dos registros que vêm antes de 9900 há mais 3 registros
		for(String key : conjuntoRegistro.keySet()){
			
//					if(key.equals("C495")){
//						if(!hasC425 && tpPerfilEmpresa.equals(IND_PERFIL_B)){
//							/*
//							 * Registro 9900 - Registros do Arquivo
//							 */
//							registro            += "|9900" + 													 // REG - Texto fixo contendo 9900 
//													  "|" + key+									 // Registro que será totalizado no próximo campo.
//													  "|" + conjuntoRegistro.get(key)+										 // Total de registros do tipo informado no campo anterior. 
//													  "|" + "\r\n";
//							nrLinhaBloco9++;
//							quant9900++;
//						}
//					}
//					else{
				/*
				 * Registro 9900 - Registros do Arquivo
				 */
				registro            += "|9900" + 													 // REG - Texto fixo contendo 9900 
										  "|" + key+									 // Registro que será totalizado no próximo campo.
										  "|" + conjuntoRegistro.get(key)+										 // Total de registros do tipo informado no campo anterior. 
										  "|" + "\r\n";
				nrLinhaBloco9++;
				quant9900++;
//					}
			
		}
		
		//Feito para contar quantos registros o registro 9900 tem no arquivo
		/*
		 * Registro 9900 - Registros do Arquivo
		 */
		registro            += "|9900" + 													 // REG - Texto fixo contendo 9900 
								  "|" + "9900"+									 // Registro que será totalizado no próximo campo.
								  "|" + quant9900+										 // Total de registros do tipo informado no campo anterior. 
								  "|" + "\r\n";
		nrLinhaBloco9++;
		
		/*
		 * Registro 9900 - Registros do Arquivo
		 */
		registro            += "|9900" + 													 // REG - Texto fixo contendo 9900 
								  "|" + "9990"+									 // Registro que será totalizado no próximo campo.
								  "|" + 1+										 // Total de registros do tipo informado no campo anterior. 
								  "|" + "\r\n";
		nrLinhaBloco9++;
		
		/*
		 * Registro 9900 - Registros do Arquivo
		 */
		registro            += "|9900" + 													 // REG - Texto fixo contendo 9900 
								  "|" + "9999"+									 // Registro que será totalizado no próximo campo.
								  "|" + 1+										 // Total de registros do tipo informado no campo anterior. 
								  "|" + "\r\n";
		nrLinhaBloco9++;
		
		
		/*
		 * Registro 9990 - Encerramento do Bloco 9
		 */
		nrLinhaBloco9++;
		nrLinhaBloco9++;
		registro           += "|9990" +
							  "|" + nrLinhaBloco9+
	  			  			  "|" + "\r\n";
		
		/*
		 * Registro 9999 - Encerramento do Arquivo Digital
		 */
		nrTotalLinha = nrLinhaBloco0 + nrLinhaBlocoA + nrLinhaBlocoC +  nrLinhaBlocoD +  nrLinhaBlocoF +  nrLinhaBlocoI +  nrLinhaBlocoM +  nrLinhaBlocoP +  nrLinhaBloco1 +  nrLinhaBloco9;
		
		registro          += "|9999" +
							  "|" + nrTotalLinha+
	  			  			  "|" + "\r\n";
		try{
			//
			FileOutputStream gravar;
			if ((nmPathArq == null) || (nmPathArq.equals(""))) {
				File arquivo = new File("teste.txt"); 
				//
				File diretorio = new File("C:\\TESTE_CONTRIB");
				if(!diretorio.exists()){
					diretorio.mkdir();
				}
				//
				gravar = new FileOutputStream("C:\\TESTE_CONTRIB\\" + arquivo);
				gravar.write(registro.getBytes());
				gravar.close();
			}else{
				gravar = new FileOutputStream(nmPathArq);
				gravar.write(registro.getBytes());
				gravar.close();
			}
		}
		catch(Exception e){System.out.println("ERRO na gravacao: " + e);}
		
		
		if(rsm.getLines().size()!=0){
			resultado.setCode(-1);
			resultado.setMessage("SPED contém Erros!");
			resultado.addObject("ERRO", rsm);
			resultado.addObject("AVISO", rsm2);
			return resultado;
			
		}
		else if(rsm2.getLines().size()!=0){
//			session.setAttribute("TXT_SPED", registro);
			resultado.setCode(1);
			resultado.addObject("ERRO", rsm);
			resultado.addObject("AVISO", rsm2);
			return resultado;
		}
		else{
			session.setAttribute("TXT_SPED", registro);
			return resultado;
		
		}
	}
	
	public static ArrayList<Integer> getParticipantesDoPeriodo(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		ArrayList<Integer> arrayDeCodigos = new ArrayList<Integer>();
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT cd_pessoa FROM grl_pessoa A " +
																		"WHERE EXISTS(SELECT cd_transportadora FROM alm_documento_saida B " +
																		"             WHERE   dt_documento_saida >= ?  " +
																		"			AND dt_documento_saida <= ?  " +
																		"			AND tp_documento_saida <> " + DocumentoSaidaServices.TP_DOC_NAO_FISCAL +
																		"			AND tp_documento_saida <> " + DocumentoSaidaServices.TP_CUPOM_FISCAL +
																		"			AND st_documento_saida =  " + DocumentoSaidaServices.ST_CONCLUIDO +
																		"			AND B.cd_empresa=? " +
																		"			AND A.cd_pessoa = B.cd_transportadora " +
																		"			group by cd_transportadora order by cd_transportadora " +
																		") " +
																		"OR EXISTS (SELECT cd_cliente FROM alm_documento_saida B " +
																		"             WHERE   dt_documento_saida >= ?  " +
																		"			AND dt_documento_saida <= ?  " +
																		"			AND tp_documento_saida <> " + DocumentoSaidaServices.TP_DOC_NAO_FISCAL +
																		"			AND tp_documento_saida <> " + DocumentoSaidaServices.TP_CUPOM_FISCAL +
																		"			AND st_documento_saida =  " + DocumentoSaidaServices.ST_CONCLUIDO +
																		"			AND B.cd_empresa=? " +
																		"			AND A.cd_pessoa = B.cd_cliente " +
																		"			group by cd_cliente order by cd_cliente " +
																		") " +
																		"OR EXISTS (SELECT cd_fornecedor FROM alm_documento_entrada B " +
																		"             WHERE   dt_documento_entrada >= ?  " +
																		"			AND dt_documento_entrada <= ?  " + 
																		"			AND tp_documento_entrada <> " + DocumentoEntradaServices.TP_DOC_NAO_FISCAL +
																		"			AND tp_documento_entrada <> " + DocumentoEntradaServices.TP_CUPOM_FISCAL +
																		"			AND tp_entrada <> " + DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO +
																		"			AND st_documento_entrada =  " + DocumentoEntradaServices.ST_LIBERADO +
																		"			AND B.cd_empresa=? " +
																		"			AND A.cd_pessoa = B.cd_fornecedor " +
																		"			group by cd_fornecedor order by cd_fornecedor " +
																		") " +
																		"OR EXISTS (SELECT cd_destinatario FROM fsc_nota_fiscal F " +
																		"             WHERE   dt_autorizacao >= ?  " +
																		"			AND dt_autorizacao <= ?  " +
																		"			AND st_nota_fiscal = " + NotaFiscalServices.AUTORIZADA + 
																		"			AND F.cd_empresa=? " +
																		"			AND A.cd_pessoa = F.cd_destinatario " +
																		"			group by cd_destinatario order by cd_destinatario " +
																		") " +
																		"OR EXISTS (SELECT B.cd_administrador FROM adm_conta_receber R  " +
																		"		  JOIN adm_forma_pagamento         C ON (R.cd_forma_pagamento = C.cd_forma_pagamento AND C.tp_forma_pagamento = "+FormaPagamentoServices.TEF+")  " +
																		"		  LEFT OUTER JOIN adm_forma_pagamento_empresa B ON (R.cd_forma_pagamento = B.cd_forma_pagamento  AND B.cd_empresa = ?)  " +
																		"		  LEFT OUTER  JOIN grl_pessoa                  D ON (D.cd_pessoa = B.cd_administrador) " +
																		"		  WHERE R.vl_recebido > 0 AND R.dt_recebimento >= ? AND R.dt_recebimento <= ? AND EXISTS (SELECT Y.cd_administrador FROM adm_forma_pagamento_empresa Y where Y.cd_administrador = D.cd_pessoa) AND A.cd_pessoa = B.cd_administrador" +
																		"		  GROUP BY cd_administrador " +
																		")");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(3, cdEmpresa);
			pstmt.setTimestamp(4, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(5, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(6, cdEmpresa);
			pstmt.setTimestamp(7, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(8, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(9, cdEmpresa);
			pstmt.setTimestamp(10, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(11, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(12, cdEmpresa);
			pstmt.setInt(13, cdEmpresa);
			pstmt.setTimestamp(14, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(15, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				arrayDeCodigos.add(rsm.getInt("cd_pessoa"));
			}
			
			return arrayDeCodigos;
		}
		catch(Exception e){
			System.out.println("ERRO getParticipantesDoPeriodo: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	public static Result getUnidadesMedidaDoPeriodo(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		ArrayList<Integer> arrayDeCodigos = new ArrayList<Integer>();
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
//			PreparedStatement pstmt = connection.prepareStatement("SELECT cd_unidade_medida FROM grl_unidade_medida A " +
//																		"WHERE EXISTS(SELECT B.cd_unidade_medida FROM alm_documento_saida_item B " +
//																		"JOIN alm_documento_saida C ON ( C.dt_documento_saida >= ?  AND C.dt_documento_saida <= ?  " +
//																		"									AND C.tp_documento_saida <> " + DocumentoSaidaServices.TP_DOC_NAO_FISCAL +
//																		"									AND C.tp_documento_saida <> " + DocumentoSaidaServices.TP_CUPOM_FISCAL +
//																		"								    AND C.st_documento_saida =  " + DocumentoSaidaServices.ST_CONCLUIDO +
//																		"									AND C.cd_empresa=? " +
//																		"									AND B.cd_documento_saida = C.cd_documento_saida) " +
//																		"			WHERE A.cd_unidade_medida = B.cd_unidade_medida " +
//																		"			GROUP BY B.cd_unidade_medida ORDER BY B.cd_unidade_medida " +
//																		") " +
//																		"OR EXISTS(SELECT B.cd_unidade_medida FROM alm_documento_entrada_item B " +
//																		"JOIN alm_documento_entrada C ON ( C.dt_documento_entrada >= ?  AND C.dt_documento_entrada <= ?  " +
//																		"									AND C.tp_documento_entrada <> " + DocumentoEntradaServices.TP_DOC_NAO_FISCAL +
//																		"									AND C.tp_documento_entrada <> " + DocumentoEntradaServices.TP_CUPOM_FISCAL +
//																		"								    AND C.st_documento_entrada =  " + DocumentoEntradaServices.ST_LIBERADO +
//																		"									AND C.cd_empresa=? " +
//																		"									AND B.cd_documento_entrada = C.cd_documento_entrada) " +
//																		"			WHERE A.cd_unidade_medida = B.cd_unidade_medida " +
//																		"			GROUP BY B.cd_unidade_medida ORDER BY B.cd_unidade_medida " +
//																		")" +
//																		" OR EXISTS(SELECT D.cd_unidade_medida FROM fsc_nota_fiscal_item B " +  
//																		" JOIN fsc_nota_fiscal C ON (C.dt_autorizacao >= ?  AND C.dt_autorizacao <= ? " + 
//																		"									AND C.st_nota_fiscal = " + NotaFiscalServices.AUTORIZADA +  
//																		"									AND C.cd_empresa=?  " + 
//																		"									AND B.cd_nota_fiscal = C.cd_nota_fiscal) " +  
//																		" JOIN grl_produto_servico_empresa D ON (B.cd_produto_servico = D.cd_produto_servico" +
//																		"											AND B.cd_empresa = D.cd_empresa) " +
//																		"			WHERE A.cd_unidade_medida = D.cd_unidade_medida  " + 
//																		"			GROUP BY D.cd_unidade_medida ORDER BY D.cd_unidade_medida " + 
//																		")" +
//																		" OR EXISTS(SELECT F.cd_unidade_medida FROM adm_conta_fechamento B " +
//																		" JOIN pcb_bico_encerrante C ON (B.cd_conta = C.cd_conta AND B.cd_fechamento = C.cd_fechamento) " +
//																		" JOIN pcb_bico D ON (C.cd_bico = D.cd_bico) " +
//																		" JOIN pcb_tanque E ON (E.cd_tanque = D.cd_tanque) " +
//																		" JOIN grl_produto_servico_empresa F ON (E.cd_produto_servico = F.cd_produto_servico) " +
//																		"			WHERE A.cd_unidade_medida = F.cd_unidade_medida  " +
//																		"			GROUP BY F.cd_unidade_medida ORDER BY F.cd_unidade_medida " +
//																		")");
//			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
//			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
//			pstmt.setInt(3, cdEmpresa);
//			pstmt.setTimestamp(4, new Timestamp(dtInicial.getTimeInMillis()));
//			pstmt.setTimestamp(5, new Timestamp(dtFinal.getTimeInMillis()));
//			pstmt.setInt(6, cdEmpresa);
//			pstmt.setTimestamp(7, new Timestamp(dtInicial.getTimeInMillis()));
//			pstmt.setTimestamp(8, new Timestamp(dtFinal.getTimeInMillis()));
//			pstmt.setInt(9, cdEmpresa);
//			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
//			rsm.beforeFirst();
//			while(rsm.next()){
//				arrayDeCodigos.add(rsm.getInt("cd_unidade_medida"));
//			}
//			rsm.beforeFirst();
			ResultSetMap rsmProd = getProdutosDoPeriodo(cdEmpresa, dtInicial, dtFinal, 0);
			while(rsmProd.next()){
				boolean achou = false;
				ProdutoServicoEmpresa produtoEmpresa = ProdutoServicoEmpresaDAO.get(cdEmpresa, rsmProd.getInt("cd_produto_servico"));
				for(int i = 0; i < arrayDeCodigos.size(); i++){
					if(produtoEmpresa.getCdUnidadeMedida()==arrayDeCodigos.get(i))
						achou = true;
				}
				if(!achou)
					arrayDeCodigos.add(produtoEmpresa.getCdUnidadeMedida());
				
			}
			rsmProd.beforeFirst();
			arrayDeCodigos = getUnidadeEcf(arrayDeCodigos, dtInicial, dtFinal);
			Result resultado = new Result(1, "Feito");
			resultado.addObject("ARRAY", arrayDeCodigos);
			resultado.addObject("RSM", rsmProd);
			return resultado;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	public static ArrayList<Integer> getUnidadeEcf(ArrayList<Integer> arrayDeCodigos, GregorianCalendar dtSpedInicial, GregorianCalendar dtSpedFinal){
		try{
			Connection connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
										 "WHERE CAST(dt_registro_ecf AS DATE) BETWEEN ? AND ? " +
										 "  AND tp_registro_ecf = \'C400\' " +
										 "ORDER BY dt_registro_ecf, cd_registro_ecf ");
			pstmt.setTimestamp(1, new Timestamp(dtSpedInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtSpedFinal.getTimeInMillis()));
			
			PreparedStatement pstmtEcfSped = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
					                                                  "WHERE cd_registro_ecf = ?");
			
			PreparedStatement pstmtEcfSpedAux = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
																		"WHERE cd_registro_ecf > ? AND tp_registro_ecf LIKE 'C4%'");
			
			ResultSetMap rsmEcf = new ResultSetMap(pstmt.executeQuery());
			while(rsmEcf.next()){
				int cdRegEcf = rsmEcf.getInt("cd_registro_ecf");
				boolean proxEcf = false;
				while(!proxEcf){
					pstmtEcfSped.setInt(1, ++cdRegEcf);
					ResultSetMap rsmEcfAux = new ResultSetMap(pstmtEcfSped.executeQuery());
					if(rsmEcfAux.next()){
						if(rsmEcfAux.getString("tp_registro_ecf").startsWith("C4") && !rsmEcfAux.getString("tp_registro_ecf").equals("C400")){
							if(rsmEcfAux.getString("tp_registro_ecf").equals("C470")){
								String[] tokens2 = rsmEcfAux.getString("txt_registro_ecf").split("\\|");
								String sgUnidadeMedida = tokens2[5];
								ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
								criterios.add(new ItemComparator("sg_unidade_medida", sgUnidadeMedida, ItemComparator.EQUAL, Types.VARCHAR));
								ResultSetMap rsmUnidade = UnidadeMedidaDAO.find(criterios);
								if(rsmUnidade.next()){
									boolean jaExiste = false;
									for(int i = 0; i < arrayDeCodigos.size(); i++){
										if(arrayDeCodigos.get(i) == rsmUnidade.getInt("cd_unidade_medida"))
											jaExiste = true;
									}
									if(!jaExiste)
										arrayDeCodigos.add(rsmUnidade.getInt("cd_unidade_medida"));
								}
							}
						}
						else
							proxEcf = true;
					}
					else{
						pstmtEcfSpedAux.setInt(1, cdRegEcf);
						ResultSetMap rsmEcfAuxAux = new ResultSetMap(pstmtEcfSpedAux.executeQuery());
						if(!rsmEcfAuxAux.next())
							proxEcf = true;
					}
				}
				
			}
			return arrayDeCodigos;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static ResultSetMap getProdutosDoPeriodo(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int tipo){
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.cd_produto_servico, id_reduzido, I.pr_aliquota, A.id_produto_servico FROM grl_produto_servico A " + 
																	"JOIN grl_produto_servico_empresa J ON (J.cd_empresa=? AND A.cd_produto_servico = J.cd_produto_servico) " +  
																	"LEFT OUTER JOIN adm_produto_servico_tributo H ON (H.cd_produto_servico = A.cd_produto_servico AND H.cd_tributo = ? AND H.cd_natureza_operacao = ? AND H.cd_classificacao_fiscal = A.cd_classificacao_fiscal) " +  
																	"LEFT OUTER JOIN adm_tributo_aliquota        I ON (I.cd_tributo_aliquota = H.cd_tributo_aliquota AND I.cd_tributo = ?)  " + 
																	"																		WHERE EXISTS(SELECT B.cd_produto_servico FROM alm_documento_saida_item B " +  
																	"																		JOIN alm_documento_saida C ON ( C.dt_documento_saida >= ?  AND C.dt_documento_saida <= ? " + 
																	"																											AND C.tp_documento_saida <> " + DocumentoSaidaServices.TP_DOC_NAO_FISCAL +
																	"																											AND C.tp_documento_saida <> " + DocumentoSaidaServices.TP_CUPOM_FISCAL +
																	"																											AND C.st_documento_saida =  " + DocumentoSaidaServices.ST_CONCLUIDO +
																	"																											AND C.cd_empresa=?  " + 
																	"																											AND B.cd_documento_saida = C.cd_documento_saida) " +  
																	"																					WHERE A.cd_produto_servico = B.cd_produto_servico  " + 
																	"																					GROUP BY B.cd_produto_servico order by B.cd_produto_servico " + 
																	"																		) " + 																	
																	"																		OR EXISTS(SELECT B.cd_produto_servico FROM alm_documento_entrada_item B " +  
																	"																		JOIN alm_documento_entrada C ON ( C.dt_documento_entrada >= ?  AND C.dt_documento_entrada <= ? " +  
																	"																											AND C.tp_documento_entrada <> " + DocumentoEntradaServices.TP_DOC_NAO_FISCAL +
																	"																											AND C.tp_documento_entrada <> " + DocumentoEntradaServices.TP_CUPOM_FISCAL +
																	"																											AND C.tp_entrada <> " + DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO +
																	"																										    AND C.st_documento_entrada =  " + DocumentoEntradaServices.ST_LIBERADO +
																	"																											AND C.cd_empresa=?  " + 
																	"																											AND B.cd_documento_entrada = C.cd_documento_entrada) " +  
																	"																					WHERE A.cd_produto_servico = B.cd_produto_servico  " + 
																	"																					GROUP BY B.cd_produto_servico order by B.cd_produto_servico " + 
																	"																		) " + 
																	"																		OR EXISTS(SELECT E.cd_produto_servico FROM adm_conta_fechamento B " +
																	"																		JOIN pcb_bico_encerrante C ON (B.cd_conta = C.cd_conta AND B.cd_fechamento = C.cd_fechamento) " +
																	"																		JOIN pcb_bico D ON (C.cd_bico = D.cd_bico) " +
																	"																		JOIN pcb_tanque E ON (E.cd_tanque = D.cd_tanque) " +
																	"																					WHERE A.cd_produto_servico = E.cd_produto_servico  " +
																	"																					GROUP BY E.cd_produto_servico order by E.cd_produto_servico " +
																	"																		)");
			
			
			pstmt.setInt(1, cdEmpresa);
			pstmt.setInt(2, ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0));
			pstmt.setInt(3, ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_VAREJO", 0, cdEmpresa));
			pstmt.setInt(4, ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0));
			pstmt.setTimestamp(5, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(6, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(7, cdEmpresa);
			pstmt.setTimestamp(8, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(9, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(10, cdEmpresa);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			rsm = getProdutosEcf(rsm, cdEmpresa, dtInicial, dtFinal);
			return rsm;
		}
		catch(Exception e){
			System.out.println("ERRO getProdutosDoPeriodo: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getProdutosEcf(ResultSetMap rsm, int cdEmpresa, GregorianCalendar dtSpedInicial, GregorianCalendar dtSpedFinal){
		try{
			Connection connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
										 "WHERE CAST(dt_registro_ecf AS DATE) BETWEEN ? AND ? " +
										 "  AND tp_registro_ecf = \'C400\' " +
										 "ORDER BY dt_registro_ecf, cd_registro_ecf ");
			pstmt.setTimestamp(1, new Timestamp(dtSpedInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtSpedFinal.getTimeInMillis()));
			
			PreparedStatement pstmtEcfSped = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
					                                                  "WHERE cd_registro_ecf = ?");
			
			PreparedStatement pstmtEcfSpedAux = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
																		"WHERE cd_registro_ecf > ? AND tp_registro_ecf LIKE 'C4%'");
			
			PreparedStatement pstmtEcfProduto = connect.prepareStatement("SELECT A.cd_produto_servico, id_reduzido, I.pr_aliquota, A.id_produto_servico FROM grl_produto_servico A " + 
																		"JOIN grl_produto_servico_empresa J ON (J.cd_empresa=? AND A.cd_produto_servico = J.cd_produto_servico) " +  
																		"LEFT OUTER JOIN adm_produto_servico_tributo H ON (H.cd_produto_servico = A.cd_produto_servico AND H.cd_tributo = ? AND H.cd_natureza_operacao = ? AND H.cd_classificacao_fiscal = A.cd_classificacao_fiscal) " +  
																		"LEFT OUTER JOIN adm_tributo_aliquota        I ON (I.cd_tributo_aliquota = H.cd_tributo_aliquota AND I.cd_tributo = ?)  WHERE A.cd_produto_servico = ?");
			
			ResultSetMap rsmEcf = new ResultSetMap(pstmt.executeQuery());
			while(rsmEcf.next()){
				int cdRegEcf = rsmEcf.getInt("cd_registro_ecf");
				boolean proxEcf = false;
				while(!proxEcf){
					pstmtEcfSped.setInt(1, ++cdRegEcf);
					ResultSetMap rsmEcfAux = new ResultSetMap(pstmtEcfSped.executeQuery());
					if(rsmEcfAux.next()){
						if(rsmEcfAux.getString("tp_registro_ecf").startsWith("C4") && !rsmEcfAux.getString("tp_registro_ecf").equals("C400")){
							if(rsmEcfAux.getString("tp_registro_ecf").equals("C470")){
								String[] tokens2 = rsmEcfAux.getString("txt_registro_ecf").split("\\|");
								String codItem = tokens2[2];
								ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
								criterios = new ArrayList<ItemComparator>();
								criterios.add(new ItemComparator("A.cd_produto_servico", (Util.isNumber(codItem) ? Util.limparFormatos(codItem) : "0"), ItemComparator.EQUAL, Types.VARCHAR));
								criterios.add(new ItemComparator("B.cd_empresa", "" + cdEmpresa, ItemComparator.EQUAL, Types.INTEGER));
								ResultSetMap rsmProduto = ProdutoServicoServices.find(criterios);
								if(rsmProduto.next()){
									pstmtEcfProduto.setInt(1, rsmProduto.getInt("cd_empresa"));
									pstmtEcfProduto.setInt(2, ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0));
									pstmtEcfProduto.setInt(3, ParametroServices.getValorOfParametroAsInteger("CD_NATUREZA_OPERACAO_VAREJO", 0, rsmProduto.getInt("cd_empresa")));
									pstmtEcfProduto.setInt(4, ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0));
									pstmtEcfProduto.setInt(5, rsmProduto.getInt("cd_produto_servico"));
									ResultSetMap rsmProd = new ResultSetMap(pstmtEcfProduto.executeQuery());
									if(rsmProd.next()){
										boolean jaExiste = false;
										while(rsm.next()){
											if(rsm.getInt("cd_produto_servico") == rsmProd.getInt("cd_produto_servico")){
												jaExiste = true;
											}
										}
										rsm.beforeFirst();
										if(!jaExiste){
											rsmProd.setValueToField("COD_ITEM", codItem);
											rsm.addRegister(rsmProd.getRegister());
										}
										
									}
								}
								
							}
						}
						else
							proxEcf = true;
					}
					else{
						pstmtEcfSpedAux.setInt(1, cdRegEcf);
						ResultSetMap rsmEcfAuxAux = new ResultSetMap(pstmtEcfSpedAux.executeQuery());
						if(!rsmEcfAuxAux.next())
							proxEcf = true;
					}
				}
				
			}
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static float getVlC490(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		try{
			Connection connect = Conexao.conectar();
			PreparedStatement pstmt = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
										 "WHERE CAST(dt_registro_ecf AS DATE) BETWEEN ? AND ? " +
										 "  AND tp_registro_ecf = \'C400\' " +
										 "ORDER BY dt_registro_ecf, cd_registro_ecf ");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			
			PreparedStatement pstmtEcfSped = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
					                                                  "WHERE cd_registro_ecf = ?");
			
			PreparedStatement pstmtEcfSpedAux = connect.prepareStatement("SELECT * FROM fsc_registro_ecf " +
																		"WHERE cd_registro_ecf > ? AND tp_registro_ecf LIKE 'C4%'");
			
			ResultSetMap rsmEcf = new ResultSetMap(pstmt.executeQuery());
			float vlTotal = 0;
			while(rsmEcf.next()){
				int cdRegEcf = rsmEcf.getInt("cd_registro_ecf");
				boolean proxEcf = false;
				while(!proxEcf){
					pstmtEcfSped.setInt(1, ++cdRegEcf);
					ResultSetMap rsmEcfAux = new ResultSetMap(pstmtEcfSped.executeQuery());
					if(rsmEcfAux.next()){
						if(rsmEcfAux.getString("tp_registro_ecf").startsWith("C4") && !rsmEcfAux.getString("tp_registro_ecf").equals("C400")){
							if(rsmEcfAux.getString("tp_registro_ecf").equals("C490")){
								String[] tokens2 = rsmEcfAux.getString("txt_registro_ecf").split("\\|");
								String cadeiaVlTotal = tokens2[7].replaceAll(",", ".");
								vlTotal += Float.parseFloat(cadeiaVlTotal);
							}
						}
						else
							proxEcf = true;
					}
					else{
						pstmtEcfSpedAux.setInt(1, cdRegEcf);
						ResultSetMap rsmEcfAuxAux = new ResultSetMap(pstmtEcfSpedAux.executeQuery());
						if(!rsmEcfAuxAux.next())
							proxEcf = true;
					}
				}
				
			}
			return vlTotal;
		}
		catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
	public static ArrayList<Integer> getNotasFiscaisDoPeriodo(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		ArrayList<Integer> arrayDeCodigos = new ArrayList<Integer>();
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT cd_nota_fiscal FROM fsc_nota_fiscal "
					+ "												WHERE dt_autorizacao >= ? "
					+ "													AND dt_autorizacao <= ? "
					+ "													AND cd_empresa = ? "
					+ "													AND st_nota_fiscal = " + NotaFiscalServices.AUTORIZADA);
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(3, cdEmpresa);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				arrayDeCodigos.add(rsm.getInt("cd_nota_fiscal"));
			}
			return arrayDeCodigos;
		}
		catch(Exception e){
			System.out.println("ERRO getNotasFiscaisDoPeriodo: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	public static ArrayList<Integer> getDocumentoEntradaSaida01DoPeriodo(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int tipo){
		ArrayList<Integer> arrayDeCodigos = new ArrayList<Integer>();
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			String tpMov = (tipo == 0) ? "saida" : "entrada";
			
			String sql = "";
			if(tipo == 0){
				sql = "SELECT cd_documento_saida FROM alm_documento_saida "
						+ "	WHERE dt_documento_saida >= ? "
						+ "   AND dt_documento_saida <= ? "
						+ "   AND cd_empresa = ? "
						+ "	  AND tp_documento_saida <> "+DocumentoSaidaServices.TP_DOC_NAO_FISCAL
						+ "   AND tp_documento_saida <> " + DocumentoSaidaServices.TP_NOTA_FISCAL_02  
						+ "   AND tp_documento_saida <> " + DocumentoSaidaServices.TP_CUPOM_FISCAL
						+ "   AND st_documento_saida = " +DocumentoSaidaServices.ST_CONCLUIDO;
			}
			else{
				sql = "SELECT cd_documento_entrada FROM alm_documento_entrada "
						+ "	WHERE dt_documento_entrada >= ? "
						+ "   AND dt_documento_entrada <= ? "
						+ "   AND cd_empresa = ? "
						+ "   AND tp_documento_entrada <> "+DocumentoEntradaServices.TP_DOC_NAO_FISCAL
						+ "   AND tp_documento_entrada <> " + DocumentoEntradaServices.TP_CUPOM_FISCAL
						+ "   AND tp_entrada <> " + DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO
						+ "   AND st_documento_entrada = " +DocumentoEntradaServices.ST_LIBERADO;
			}
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(3, cdEmpresa);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				arrayDeCodigos.add(rsm.getInt("cd_documento_"+tpMov));
			}
			return arrayDeCodigos;
		}
		catch(Exception e){
			System.out.println("ERRO getDocumentoEntradaSaida01DoPeriodo: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	public static ArrayList<Integer> getDocumentoSaida02DoPeriodo(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		ArrayList<Integer> arrayDeCodigos = new ArrayList<Integer>();
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT cd_documento_saida FROM alm_documento_saida WHERE dt_documento_saida >= ? AND dt_documento_saida <= ? AND cd_empresa = ? AND tp_documento_saida = "+DocumentoSaidaServices.TP_NOTA_FISCAL_02+" AND st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO);
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(3, cdEmpresa);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				arrayDeCodigos.add(rsm.getInt("cd_documento_saida"));
			}
			return arrayDeCodigos;
		}
		catch(Exception e){
			System.out.println("ERRO getDocumentoSaida02DoPeriodo: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	public static ArrayList<Integer> getContasAReceber(int cdDocumentoSaida){
		ArrayList<Integer> arrayDeCodigos = new ArrayList<Integer>();
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT cd_conta_receber FROM adm_conta_receber WHERE cd_documento_saida = ?");
			pstmt.setInt(1, cdDocumentoSaida);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				arrayDeCodigos.add(rsm.getInt("cd_conta_receber"));
			}
			return arrayDeCodigos;
		}
		catch(Exception e){
			System.out.println("ERRO getContasAReceber: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	public static ArrayList<Integer> getContasAPagar(int cdDocumentoEntrada){
		ArrayList<Integer> arrayDeCodigos = new ArrayList<Integer>();
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT cd_conta_pagar FROM adm_conta_pagar WHERE cd_documento_entrada = ?");
			pstmt.setInt(1, cdDocumentoEntrada);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				arrayDeCodigos.add(rsm.getInt("cd_conta_pagar"));
			}
			return arrayDeCodigos;
		}
		catch(Exception e){
			System.out.println("ERRO getContasAPagar: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getTributacao(int cdDocumento, int tpMov){
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			int cdTributoICMS    = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0, 0, connection);
			int cdTributoIPI     = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_IPI", 0, 0, connection);
			
			String sql = (tpMov == 0) ? "SELECT nr_situacao_tributaria, A.pr_aliquota, nr_codigo_fiscal, SUM(vl_base_calculo) AS vl_base_calculo, SUM((A.pr_aliquota / 100) * vl_base_calculo)  AS vl_icms, SUM(vl_unitario * qt_saida + D.vl_acrescimo - D.vl_desconto) AS vl_itens " +
										"FROM alm_documento_saida A0 " +
										"JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
										"JOIN alm_documento_saida_item D ON (A0.cd_documento_saida = D.cd_documento_saida) " +
										"JOIN adm_saida_item_aliquota A ON (A0.cd_documento_saida = A.cd_documento_saida "+ 
										"													 AND D.cd_produto_servico = A.cd_produto_servico"+ 
										"													 AND D.cd_item = A.cd_item) " +
										"JOIN adm_tributo_aliquota B ON (A.cd_tributo          = B.cd_tributo " +
										"						AND A.cd_tributo_aliquota = B.cd_tributo_aliquota) " +
										"JOIN fsc_situacao_tributaria  Q ON (B.cd_situacao_tributaria = Q.cd_situacao_tributaria" +
										"										AND Q.cd_tributo = B.cd_tributo) " +
										"WHERE A0.cd_documento_saida = ? " +
										"          AND B.cd_tributo           = "+cdTributoICMS +
										" GROUP BY 1, 2, 3" : (tpMov == 1) ?
											"SELECT nr_situacao_tributaria, A.pr_aliquota, nr_codigo_fiscal, SUM(vl_base_calculo) AS vl_base_calculo, SUM((A.pr_aliquota / 100) * vl_base_calculo)  AS vl_icms, SUM(D.vl_unitario * D.qt_entrada + D.vl_acrescimo - D.vl_desconto) AS vl_itens " +
											"FROM alm_documento_entrada A0 " +
											"JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
											"JOIN alm_documento_entrada_item D ON (A0.cd_documento_entrada = D.cd_documento_entrada) " +
											"JOIN adm_entrada_item_aliquota A ON (A0.cd_documento_entrada = A.cd_documento_entrada"+ 
											"														 AND D.cd_produto_servico = A.cd_produto_servico " +
											"													 	 AND D.cd_item = A.cd_item) " +
											"JOIN adm_tributo_aliquota B ON (A.cd_tributo          = B.cd_tributo " +
											"						AND A.cd_tributo_aliquota = B.cd_tributo_aliquota) " +
											"JOIN fsc_situacao_tributaria  Q ON (B.cd_situacao_tributaria = Q.cd_situacao_tributaria" +
										"										AND Q.cd_tributo = B.cd_tributo) " +
											"WHERE A0.cd_documento_entrada = ? " +
											"          AND B.cd_tributo           = "+cdTributoICMS +
											" GROUP BY 1, 2, 3" : 
												"SELECT nr_situacao_tributaria, A.pr_aliquota, nr_codigo_fiscal, SUM(vl_base_calculo) AS vl_base_calculo, SUM((A.pr_aliquota / 100) * vl_base_calculo)  AS vl_icms, SUM(vl_unitario * qt_tributario + D.vl_acrescimo - D.vl_desconto) AS vl_itens " +
												"FROM fsc_nota_fiscal A0, fsc_nota_fiscal_item_tributo A, adm_tributo_aliquota B, adm_natureza_operacao C, fsc_nota_fiscal_item D, fsc_situacao_tributaria Q " +
												"WHERE A0.cd_nota_fiscal = ? " +
												"AND A0.cd_nota_fiscal = A.cd_nota_fiscal " +
												"AND A.cd_tributo          = B.cd_tributo " +
												"AND A.cd_tributo_aliquota = B.cd_tributo_aliquota  " +
												"AND A0.cd_natureza_operacao = C.cd_natureza_operacao " +
												"AND A.cd_nota_fiscal = D.cd_nota_fiscal " +
												"AND A.cd_item = D.cd_item " +
												"AND B.cd_situacao_tributaria = Q.cd_situacao_tributaria " +
												"AND B.cd_tributo           = "+cdTributoICMS +
												"AND Q.cd_tributo = B.cd_tributo " +
												"GROUP BY 1, 2, 3";

			
			PreparedStatement pstmt = connection.prepareStatement(sql);
		
			pstmt.setInt(1, cdDocumento);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			if(tpMov == 0){
				while(rsm.next()){
					//Total Tributo ICMS
					
					ResultSetMap rsm4 = new ResultSetMap(connection.prepareStatement("SELECT st_tributaria, SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_icms, SUM(I.pr_aliquota) AS pr_icms, SUM(I.vl_base_calculo) AS vl_base_icms FROM adm_saida_item_aliquota I " +
																			 " 		  JOIN alm_documento_saida A0 ON (I.cd_documento_saida = A0.cd_documento_saida) " +
																			 "        JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
																			 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																			 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																			 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria" +
																			 "																	AND Q.cd_tributo = P.cd_tributo) " + 
																			 "        WHERE I.cd_documento_saida = " +cdDocumento +
																			 "          AND P.cd_tributo           = "+cdTributoICMS + 
																			 "			AND C.nr_codigo_fiscal = '" + rsm.getString("nr_codigo_fiscal") + "'" + 
																			 "			AND I.pr_aliquota = " + rsm.getFloat("pr_aliquota") +  
																			 "			AND Q.nr_situacao_tributaria = '" + rsm.getString("nr_situacao_tributaria") + "'" +  
																			 "        GROUP BY 1").executeQuery());
					while(rsm4.next()){
						if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
							rsm.setValueToField("vl_icms", rsm4.getFloat("vl_icms"));
							rsm.setValueToField("vl_base_icms", rsm4.getFloat("vl_base_icms"));
							rsm.setValueToField("pr_icms", rsm4.getFloat("pr_icms"));
							rsm.setValueToField("st_tributaria", rsm4.getInt("st_tributaria"));
//							rsm.setValueToField("nr_situacao_tributaria", rsm4.getString("nr_situacao_tributaria"));
						}
						else if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
							rsm.setValueToField("vl_icms_substituto", rsm4.getFloat("vl_icms"));
							rsm.setValueToField("vl_icms", 0);
							rsm.setValueToField("vl_base_icms_substituto", rsm4.getFloat("vl_base_icms"));
							rsm.setValueToField("vl_base_icms", 0);
							rsm.setValueToField("pr_icms_substituto", rsm4.getFloat("pr_icms"));
							rsm.setValueToField("pr_icms", 0);
						}
					}
					
					rsm4 = new ResultSetMap(connection.prepareStatement("SELECT st_tributaria, SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_ipi, SUM(I.pr_aliquota) AS pr_ipi, SUM(I.vl_base_calculo) AS vl_base_ipi FROM adm_saida_item_aliquota I " +
												 " 		  JOIN alm_documento_saida A0 ON (I.cd_documento_saida = A0.cd_documento_saida) " +
												 "        JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
												 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
												 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
												 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria" +
												 "																	AND Q.cd_tributo = P.cd_tributo) " + 
												 "        WHERE I.cd_documento_saida = " +cdDocumento +
												 "          AND P.cd_tributo           = "+cdTributoIPI + 
												 "			AND C.nr_codigo_fiscal = '" + rsm.getString("nr_codigo_fiscal") + "'" + 
												 "			AND I.pr_aliquota = " + rsm.getFloat("pr_aliquota") +  
												 "			AND Q.nr_situacao_tributaria = '" + rsm.getString("nr_situacao_tributaria") + "'" +  
												 "        GROUP BY 1").executeQuery());
					while(rsm4.next()){
						if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
							rsm.setValueToField("vl_ipi", rsm4.getFloat("vl_ipi"));
							rsm.setValueToField("vl_base_ipi", rsm4.getFloat("vl_base_ipi"));
							rsm.setValueToField("pr_ipi", rsm4.getFloat("pr_ipi"));
							rsm.setValueToField("st_tributaria", rsm4.getInt("st_tributaria"));
//							rsm.setValueToField("nr_situacao_tributaria", rsm4.getString("nr_situacao_tributaria"));
						}
						else if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
							rsm.setValueToField("vl_ipi_substituto", rsm4.getFloat("vl_ipi"));
							rsm.setValueToField("vl_ipi", 0);
							rsm.setValueToField("vl_base_ipi_substituto", rsm4.getFloat("vl_base_ipi"));
							rsm.setValueToField("vl_base_ipi", 0);
							rsm.setValueToField("pr_ipi_substituto", rsm4.getFloat("pr_ipi"));
							rsm.setValueToField("pr_ipi", 0);
						}
					}
					
				}
				rsm.beforeFirst();
								
				ResultSetMap rsm4 = new ResultSetMap(connection.prepareStatement("SELECT * FROM alm_documento_saida_item I " +
																		 " 		  JOIN alm_documento_saida A0 ON (I.cd_documento_saida = A0.cd_documento_saida) " +
																		 "        JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
																		 "        WHERE I.cd_documento_saida = " +cdDocumento + 
																		 "			AND NOT EXISTS (SELECT * FROM adm_saida_item_aliquota B WHERE I.cd_documento_saida = B.cd_documento_saida AND I.cd_produto_servico = B.cd_produto_servico AND I.cd_item = B.cd_item)").executeQuery());

				
				if(rsm4.next()){
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("ST_TRIBUTARIA", TributoAliquotaServices.ST_SEM_TRIBUTACAO);
					register.put("PR_ALIQUOTA", 0);
					DocumentoSaida doc = DocumentoSaidaDAO.get(cdDocumento);
					NaturezaOperacao natOper = NaturezaOperacaoDAO.get(doc.getCdNaturezaOperacao());
					register.put("NR_CODIGO_FISCAL", natOper.getNrCodigoFiscal());
					register.put("VL_BASE_CALCULO", 0);
					register.put("VL_ICMS", 0);
					register.put("VL_ITENS", (rsm4.getFloat("vl_unitario") * rsm4.getFloat("qt_saida") - rsm4.getFloat("vl_desconto") + rsm4.getFloat("vl_acrescimo")));
					register.put("VL_BASE_ICMS", 0);
					register.put("PR_ICMS", 0);
					register.put("NR_SITUACAO_TRIBUTARIA", "041");
					rsm.addRegister(register);
				}
			}
			
			else if(tpMov == 1){
				while(rsm.next()){
					//Total Tributo ICMS
					
					ResultSetMap rsm4 = new ResultSetMap(connection.prepareStatement("SELECT st_tributaria, SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_icms, SUM(I.pr_aliquota) AS pr_icms, SUM(I.vl_base_calculo) AS vl_base_icms FROM adm_entrada_item_aliquota I " +
																			 " 		  JOIN alm_documento_entrada A0 ON (I.cd_documento_entrada = A0.cd_documento_entrada) " +
																			 "        JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
																			 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																			 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																			 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria" +
																			 "																	AND Q.cd_tributo = P.cd_tributo) " + 
																			 "        WHERE I.cd_documento_entrada = " +cdDocumento +
																			 "          AND P.cd_tributo           = "+cdTributoICMS + 
																			 "			AND C.nr_codigo_fiscal = '" + rsm.getString("nr_codigo_fiscal") + "'" + 
																			 "			AND I.pr_aliquota = " + rsm.getFloat("pr_aliquota") +  
																			 "			AND Q.nr_situacao_tributaria = '" + rsm.getString("nr_situacao_tributaria") + "'" +  
																			 "        GROUP BY 1").executeQuery());
					while(rsm4.next()){
						if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
							rsm.setValueToField("vl_icms", rsm4.getFloat("vl_icms"));
							rsm.setValueToField("vl_base_icms", rsm4.getFloat("vl_base_icms"));
							rsm.setValueToField("pr_icms", rsm4.getFloat("pr_icms"));
							rsm.setValueToField("st_tributaria", rsm4.getInt("st_tributaria"));
//							rsm.setValueToField("nr_situacao_tributaria", rsm4.getString("nr_situacao_tributaria"));
						}
						else if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
							rsm.setValueToField("vl_icms_substituto", rsm4.getFloat("vl_icms"));
							rsm.setValueToField("vl_icms", 0);
							rsm.setValueToField("vl_base_icms_substituto", rsm4.getFloat("vl_base_icms"));
							rsm.setValueToField("vl_base_icms", 0);
							rsm.setValueToField("pr_icms_substituto", rsm4.getFloat("pr_icms"));
							rsm.setValueToField("pr_icms", 0);
						}
					}
					
					rsm4 = new ResultSetMap(connection.prepareStatement("SELECT st_tributaria, SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_ipi, SUM(I.pr_aliquota) AS pr_ipi, SUM(I.vl_base_calculo) AS vl_base_ipi FROM adm_entrada_item_aliquota I " +
												 " 		  JOIN alm_documento_entrada A0 ON (I.cd_documento_entrada = A0.cd_documento_entrada) " +
												 "        JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
												 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
												 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
												 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria" +
												 "																	AND Q.cd_tributo = P.cd_tributo) " + 
												 "        WHERE I.cd_documento_entrada = " +cdDocumento +
												 "          AND P.cd_tributo           = "+cdTributoIPI + 
												 "			AND C.nr_codigo_fiscal = '" + rsm.getString("nr_codigo_fiscal") + "'" + 
												 "			AND I.pr_aliquota = " + rsm.getFloat("pr_aliquota") +  
												 "			AND Q.nr_situacao_tributaria = '" + rsm.getString("nr_situacao_tributaria") + "'" +  
												 "        GROUP BY 1").executeQuery());
					while(rsm4.next()){
						if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
							rsm.setValueToField("vl_ipi", rsm4.getFloat("vl_ipi"));
							rsm.setValueToField("vl_base_ipi", rsm4.getFloat("vl_base_ipi"));
							rsm.setValueToField("pr_ipi", rsm4.getFloat("pr_ipi"));
							rsm.setValueToField("st_tributaria", rsm4.getInt("st_tributaria"));
//							rsm.setValueToField("nr_situacao_tributaria", rsm4.getString("nr_situacao_tributaria"));
						}
						else if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
							rsm.setValueToField("vl_ipi_substituto", rsm4.getFloat("vl_ipi"));
							rsm.setValueToField("vl_ipi", 0);
							rsm.setValueToField("vl_base_ipi_substituto", rsm4.getFloat("vl_base_ipi"));
							rsm.setValueToField("vl_base_ipi", 0);
							rsm.setValueToField("pr_ipi_substituto", rsm4.getFloat("pr_ipi"));
							rsm.setValueToField("pr_ipi", 0);
						}
					}
					
				}
				rsm.beforeFirst();
				
				ResultSetMap rsm4 = new ResultSetMap(connection.prepareStatement("SELECT * FROM alm_documento_entrada_item I " +
																		 " 		  JOIN alm_documento_entrada A0 ON (I.cd_documento_entrada = A0.cd_documento_entrada) " +
																		 "        JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
																		 "        WHERE I.cd_documento_entrada = " +cdDocumento + 
																		 "			AND NOT EXISTS (SELECT * FROM adm_entrada_item_aliquota B WHERE I.cd_documento_entrada = B.cd_documento_entrada AND I.cd_produto_servico = B.cd_produto_servico AND I.cd_item = B.cd_item)").executeQuery());

				
				if(rsm4.next()){
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("ST_TRIBUTARIA", TributoAliquotaServices.ST_SEM_TRIBUTACAO);
					register.put("PR_ALIQUOTA", 0);
					DocumentoEntrada doc = DocumentoEntradaDAO.get(cdDocumento);
					NaturezaOperacao natOper = NaturezaOperacaoDAO.get(doc.getCdNaturezaOperacao());
					register.put("NR_CODIGO_FISCAL", natOper.getNrCodigoFiscal());
					register.put("VL_BASE_CALCULO", 0);
					register.put("VL_ICMS", 0);
					register.put("VL_ITENS", (rsm4.getFloat("vl_unitario") * rsm4.getFloat("qt_entrada") - rsm4.getFloat("vl_desconto") + rsm4.getFloat("vl_acrescimo")));
					register.put("VL_BASE_ICMS", 0);
					register.put("PR_ICMS", 0);
					register.put("NR_SITUACAO_TRIBUTARIA", "041");
					rsm.addRegister(register);
				}
				
				
			}
			
				
//				if(rsm.next()){
//					ResultSetMap rsmEntradaTributo = new ResultSetMap(connection.prepareStatement("SELECT SUM(vl_retido + vl_tributo) AS vl_tributos FROM adm_entrada_tributo WHERE cd_documento_entrada = " + cdDocumento).executeQuery());
//					if(rsmEntradaTributo.next()){
//						rsm.setValueToField("vl_itens", rsm.getFloat("vl_itens") + rsmEntradaTributo.getFloat("vl_tributos"));
//					}
//				}
//				rsm.beforeFirst();
//				while(rsm.next()){
					//Total Tributo ICMS
					
//					ResultSetMap rsm4 = new ResultSetMap(connection.prepareStatement("SELECT st_tributaria, SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_icms, SUM(I.pr_aliquota) AS pr_icms, SUM(I.vl_base_calculo) AS vl_base_icms FROM adm_entrada_item_aliquota I " +
//																			 " 		  JOIN alm_documento_entrada A0 ON (I.cd_documento_entrada = A0.cd_documento_entrada) " +
//																			 "        JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
//																			 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
//																			 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
//																			 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria" +
//																			 "																	AND Q.cd_tributo = P.cd_tributo) " + 
//																			 "        WHERE I.cd_documento_entrada = " +cdDocumento +
//																			 "          AND P.cd_tributo           = "+cdTributoICMS + 
//																			 "			AND C.nr_codigo_fiscal = '" + rsm.getString("nr_codigo_fiscal") + "'" + 
//																			 "			AND I.pr_aliquota = " + rsm.getFloat("pr_aliquota") +  
//																			 "			AND Q.nr_situacao_tributaria = '" + rsm.getString("nr_situacao_tributaria") + "'" +  
//																			 "        GROUP BY 1").executeQuery());
//					while(rsm4.next()){
//						if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
//							rsm.setValueToField("vl_icms", rsm4.getFloat("vl_icms"));
//							rsm.setValueToField("vl_base_icms", rsm4.getFloat("vl_base_icms"));
//							rsm.setValueToField("pr_icms", rsm4.getFloat("pr_icms"));
//							rsm.setValueToField("st_tributaria", rsm4.getInt("st_tributaria"));
////							rsm.setValueToField("nr_situacao_tributaria", rsm4.getString("nr_situacao_tributaria"));
//						}
//						else if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
//							rsm.setValueToField("vl_icms_substituto", rsm4.getFloat("vl_icms"));
//							rsm.setValueToField("vl_icms", 0);
//							rsm.setValueToField("vl_base_icms_substituto", rsm4.getFloat("vl_base_icms"));
//							rsm.setValueToField("vl_base_icms", 0);
//							rsm.setValueToField("pr_icms_substituto", rsm4.getFloat("pr_icms"));
//							rsm.setValueToField("pr_icms", 0);
//						}
//					}
//					
//					rsm4 = new ResultSetMap(connection.prepareStatement("SELECT st_tributaria, SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_icms, SUM(I.pr_aliquota) AS pr_icms, SUM(I.vl_base_calculo) AS vl_base_icms FROM adm_entrada_item_aliquota I " +
//												 " 		  JOIN alm_documento_entrada A0 ON (I.cd_documento_entrada = A0.cd_documento_entrada) " +
//												 "        JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
//												 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
//												 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
//												 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria" +
//												 "																	AND Q.cd_tributo = P.cd_tributo) " + 
//												 "        WHERE I.cd_documento_entrada = " +cdDocumento +
//												 "          AND P.cd_tributo           = "+cdTributoIPI + 
//												 "			AND C.nr_codigo_fiscal = '" + rsm.getString("nr_codigo_fiscal") + "'" + 
//												 "			AND I.pr_aliquota = " + rsm.getFloat("pr_aliquota") +  
//												 "			AND Q.nr_situacao_tributaria = '" + rsm.getString("nr_situacao_tributaria") + "'" +  
//												 "        GROUP BY 1").executeQuery());
//					while(rsm4.next()){
//						if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
//							rsm.setValueToField("vl_ipi", rsm4.getFloat("vl_ipi"));
//							rsm.setValueToField("vl_base_ipi", rsm4.getFloat("vl_base_ipi"));
//							rsm.setValueToField("pr_ipi", rsm4.getFloat("pr_ipi"));
//							rsm.setValueToField("st_tributaria", rsm4.getInt("st_tributaria"));
////							rsm.setValueToField("nr_situacao_tributaria", rsm4.getString("nr_situacao_tributaria"));
//						}
//						else if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
//							rsm.setValueToField("vl_ipi_substituto", rsm4.getFloat("vl_ipi"));
//							rsm.setValueToField("vl_ipi", 0);
//							rsm.setValueToField("vl_base_ipi_substituto", rsm4.getFloat("vl_base_ipi"));
//							rsm.setValueToField("vl_base_ipi", 0);
//							rsm.setValueToField("pr_ipi_substituto", rsm4.getFloat("pr_ipi"));
//							rsm.setValueToField("pr_ipi", 0);
//						}
//					}
//					
//				}
//				rsm.beforeFirst();
//				
//				PreparedStatement pstmt4 = connection.prepareStatement("SELECT * FROM alm_documento_entrada_item I " +
//						 " 		  JOIN alm_documento_entrada A0 ON (I.cd_documento_entrada = A0.cd_documento_entrada) " +
//						 "        JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
//						 "        WHERE I.cd_documento_entrada = " +cdDocumento + 
//						 "			AND NOT EXISTS (SELECT * FROM adm_entrada_item_aliquota B WHERE I.cd_documento_entrada = B.cd_documento_entrada AND I.cd_produto_servico = B.cd_produto_servico AND I.cd_item = B.cd_item)");
//				
//				
//				ResultSetMap rsm4 = new ResultSetMap(pstmt4.executeQuery());
//
//				
//				
//				if(rsm4.next()){
//					HashMap<String, Object> register = new HashMap<String, Object>();
//					register.put("ST_TRIBUTARIA", TributoAliquotaServices.ST_SEM_TRIBUTACAO);
//					register.put("PR_ALIQUOTA", 0);
//					DocumentoEntrada doc = DocumentoEntradaDAO.get(cdDocumento);
//					NaturezaOperacao natOper = NaturezaOperacaoDAO.get(doc.getCdNaturezaOperacao());
//					register.put("NR_CODIGO_FISCAL", natOper.getNrCodigoFiscal());
//					register.put("VL_BASE_CALCULO", 0);
//					register.put("VL_ICMS", 0);
//					register.put("VL_ITENS", (rsm4.getFloat("vl_unitario") * rsm4.getFloat("qt_entrada") - rsm4.getFloat("vl_desconto") + rsm4.getFloat("vl_acrescimo")));
//					register.put("VL_BASE_ICMS", 0);
//					register.put("PR_ICMS", 0);
//					register.put("NR_SITUACAO_TRIBUTARIA", "041");
//					rsm.addRegister(register);
//				}
//			}
			else{
				while(rsm.next()){
					//Total Tributo ICMS
					
					PreparedStatement pstmt2 = connection.prepareStatement("SELECT st_tributaria, SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_icms, SUM(I.pr_aliquota) AS pr_icms, SUM(I.vl_base_calculo) AS vl_base_icms FROM fsc_nota_fiscal_item_tributo I " +
																	 " 		  JOIN fsc_nota_fiscal A0 ON (I.cd_nota_fiscal = A0.cd_nota_fiscal) " +
																	 "        JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
																	 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																	 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																	 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria" +
																	 "																	AND Q.cd_tributo = P.cd_tributo) " + 
																	 "        WHERE I.cd_nota_fiscal = " +cdDocumento +
																	 "          AND P.cd_tributo           = "+cdTributoICMS + 
																	 "			AND C.nr_codigo_fiscal = '" + rsm.getString("nr_codigo_fiscal") + "'" + 
																	 "			AND I.pr_aliquota = " + rsm.getFloat("pr_aliquota") +  
																	 "			AND Q.nr_situacao_tributaria = '" + rsm.getString("nr_situacao_tributaria") + "'" +  
																	 "        GROUP BY 1");
					ResultSetMap rsm4 = new ResultSetMap(pstmt2.executeQuery());
					while(rsm4.next()){
						if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
							rsm.setValueToField("vl_icms", rsm4.getFloat("vl_icms"));
							rsm.setValueToField("vl_base_icms", rsm4.getFloat("vl_base_icms"));
							rsm.setValueToField("pr_icms", rsm4.getFloat("pr_icms"));
							rsm.setValueToField("st_tributaria", rsm4.getInt("st_tributaria"));
//							rsm.setValueToField("nr_situacao_tributaria", rsm4.getString("nr_situacao_tributaria"));
						}
						else if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
							rsm.setValueToField("vl_icms_substituto", rsm4.getFloat("vl_icms"));
							rsm.setValueToField("vl_icms", 0);
							rsm.setValueToField("vl_base_icms_substituto", rsm4.getFloat("vl_base_icms"));
							rsm.setValueToField("vl_base_icms", 0);
							rsm.setValueToField("pr_icms_substituto", rsm4.getFloat("pr_icms"));
							rsm.setValueToField("pr_icms", 0);
						}
					}
					

					PreparedStatement pstmt3 = connection.prepareStatement("SELECT st_tributaria, SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_icms, SUM(I.pr_aliquota) AS pr_icms, SUM(I.vl_base_calculo) AS vl_base_icms FROM fsc_nota_fiscal_item_tributo I " +
							 " 		  JOIN fsc_nota_fiscal A0 ON (I.cd_nota_fiscal = A0.cd_nota_fiscal) " +
							 "        JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
							 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
							 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
							 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria" +
							 "																	AND Q.cd_tributo = P.cd_tributo) " + 
							 "        WHERE I.cd_nota_fiscal = " +cdDocumento +
							 "          AND P.cd_tributo           = "+cdTributoIPI + 
							 "			AND C.nr_codigo_fiscal = '" + rsm.getString("nr_codigo_fiscal") + "'" + 
							 "			AND I.pr_aliquota = " + rsm.getFloat("pr_aliquota") +  
							 "			AND Q.nr_situacao_tributaria = '" + rsm.getString("nr_situacao_tributaria") + "'" +  
							 "        GROUP BY 1");
					
					rsm4 = new ResultSetMap(pstmt3.executeQuery());
					
					
					while(rsm4.next()){
						if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
							rsm.setValueToField("vl_ipi", rsm4.getFloat("vl_ipi"));
							rsm.setValueToField("vl_base_ipi", rsm4.getFloat("vl_base_ipi"));
							rsm.setValueToField("pr_ipi", rsm4.getFloat("pr_ipi"));
							rsm.setValueToField("st_tributaria", rsm4.getInt("st_tributaria"));
//							rsm.setValueToField("nr_situacao_tributaria", rsm4.getString("nr_situacao_tributaria"));
						}
						else if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
							rsm.setValueToField("vl_ipi_substituto", rsm4.getFloat("vl_ipi"));
							rsm.setValueToField("vl_ipi", 0);
							rsm.setValueToField("vl_base_ipi_substituto", rsm4.getFloat("vl_base_ipi"));
							rsm.setValueToField("vl_base_ipi", 0);
							rsm.setValueToField("pr_ipi_substituto", rsm4.getFloat("pr_ipi"));
							rsm.setValueToField("pr_ipi", 0);
						}
					}
					
				}
				rsm.beforeFirst();
				
				
				PreparedStatement pstmt4 = connection.prepareStatement("SELECT * FROM fsc_nota_fiscal_item_tributo I " +
						 " 		  JOIN fsc_nota_fiscal_item I0 ON (I0.cd_nota_fiscal = I.cd_nota_fiscal" +
						 "											  AND I0.cd_item = I.cd_item) " +
						 " 		  JOIN fsc_nota_fiscal A0 ON (I.cd_nota_fiscal = A0.cd_nota_fiscal) " +
						 "        JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
						 "        WHERE I.cd_nota_fiscal = " +cdDocumento + 
						 "			AND NOT EXISTS (SELECT * FROM fsc_nota_fiscal_item_tributo B WHERE I.cd_nota_fiscal = B.cd_nota_fiscal AND I.cd_item = B.cd_item)");
				
				ResultSetMap rsm4 = new ResultSetMap(pstmt4.executeQuery());

				if(rsm4.next()){
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("ST_TRIBUTARIA", TributoAliquotaServices.ST_SEM_TRIBUTACAO);
					register.put("PR_ALIQUOTA", 0);
					NotaFiscal nota = NotaFiscalDAO.get(cdDocumento);
					NaturezaOperacao natOper = NaturezaOperacaoDAO.get(nota.getCdNaturezaOperacao());
					register.put("NR_CODIGO_FISCAL", natOper.getNrCodigoFiscal());
					register.put("VL_BASE_CALCULO", 0);
					register.put("VL_ICMS", 0);
					register.put("VL_ITENS", (rsm4.getFloat("vl_unitario") * rsm4.getFloat("qt_tributario") - rsm4.getFloat("vl_desconto") + rsm4.getFloat("vl_acrescimo")));
					register.put("VL_BASE_ICMS", 0);
					register.put("PR_ICMS", 0);
					register.put("NR_SITUACAO_TRIBUTARIA", "041");
					rsm.addRegister(register);
				}
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	
	public static ResultSetMap getTributacaoByEstado(int cdDocumento, int tpMov){
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			int cdTributoICMS    = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0, 0, connection);
			int cdTributoIPI     = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_IPI", 0, 0, connection);
			
			String sql = (tpMov == 0) ? "SELECT PEE.sg_estado, nr_situacao_tributaria, A.pr_aliquota, nr_codigo_fiscal, SUM(vl_base_calculo) AS vl_base_calculo, SUM((A.pr_aliquota / 100) * vl_base_calculo)  AS vl_icms, SUM(vl_unitario * qt_saida + D.vl_acrescimo - D.vl_desconto) AS vl_itens " +
										"FROM alm_documento_saida A0 " +
										"JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
										"JOIN alm_documento_saida_item D ON (A0.cd_documento_saida = D.cd_documento_saida) " +
										"JOIN adm_saida_item_aliquota A ON (A0.cd_documento_saida = A.cd_documento_saida "+ 
										"													 AND D.cd_produto_servico = A.cd_produto_servico) " +
										"JOIN adm_tributo_aliquota B ON (A.cd_tributo          = B.cd_tributo " +
										"						AND A.cd_tributo_aliquota = B.cd_tributo_aliquota) " +
										"JOIN fsc_situacao_tributaria  Q ON (B.cd_situacao_tributaria = Q.cd_situacao_tributaria" +
										"										AND Q.cd_tributo = B.cd_tributo) " +
										"JOIN grl_pessoa P ON (A0.cd_cliente = P.cd_pessoa) " +
										"JOIN grl_pessoa_endereco PE ON (P.cd_pessoa = PE.cd_pessoa " +
										"									AND PE.lg_principal = 1) " +
										"JOIN grl_cidade PEC ON (PE.cd_cidade = PEC.cd_cidade) " +
										"JOIN grl_estado PEE ON (PEE.cd_estado = PEC.cd_estado) " +
										"WHERE A0.cd_documento_saida = ? " +
										"          AND B.cd_tributo           = "+cdTributoICMS +
										" GROUP BY 1, 2, 3, 4" : (tpMov == 1) ?
											"SELECT PEE.sg_estado, nr_situacao_tributaria, A.pr_aliquota, nr_codigo_fiscal, SUM(vl_base_calculo) AS vl_base_calculo, SUM((A.pr_aliquota / 100) * vl_base_calculo)  AS vl_icms, SUM(vl_unitario * qt_entrada + D.vl_acrescimo - D.vl_desconto) AS vl_itens " +
											"FROM alm_documento_entrada A0 " +
											"JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
											"JOIN alm_documento_entrada_item D ON (A0.cd_documento_entrada = D.cd_documento_entrada) " +
											"JOIN adm_entrada_item_aliquota A ON (A0.cd_documento_entrada = A.cd_documento_entrada"+ 
											"														 AND D.cd_produto_servico = A.cd_produto_servico) " +
											"JOIN adm_tributo_aliquota B ON (A.cd_tributo          = B.cd_tributo " +
											"						AND A.cd_tributo_aliquota = B.cd_tributo_aliquota) " +
											"JOIN fsc_situacao_tributaria  Q ON (B.cd_situacao_tributaria = Q.cd_situacao_tributaria" +
										    "										AND Q.cd_tributo = B.cd_tributo) " +
										    "JOIN grl_pessoa P ON (A0.cd_fornecedor = P.cd_pessoa) " +
											"JOIN grl_pessoa_endereco PE ON (P.cd_pessoa = PE.cd_pessoa " +
											"									AND PE.lg_principal = 1) " +
											"JOIN grl_cidade PEC ON (PE.cd_cidade = PEC.cd_cidade) " +
											"JOIN grl_estado PEE ON (PEE.cd_estado = PEC.cd_estado) " +
											"WHERE A0.cd_documento_entrada = ? " +
											"          AND B.cd_tributo           = "+cdTributoICMS +
											" GROUP BY 1, 2, 3, 4" : 
												"SELECT PEE.sg_estado, nr_situacao_tributaria, A.pr_aliquota, nr_codigo_fiscal, SUM(vl_base_calculo) AS vl_base_calculo, SUM((A.pr_aliquota / 100) * vl_base_calculo)  AS vl_icms, SUM(vl_unitario * qt_tributario + D.vl_acrescimo - D.vl_desconto) AS vl_itens " +
												"FROM fsc_nota_fiscal A0, fsc_nota_fiscal_item_tributo A, adm_tributo_aliquota B, adm_natureza_operacao C, fsc_nota_fiscal_item D, fsc_situacao_tributaria Q, grl_pessoa P, grl_pessoa_endereco PE, grl_cidade PEC, grl_estado PEE " +
												"WHERE A0.cd_nota_fiscal = ? " +
												"AND A0.cd_destinatario = P.cd_pessoa " +
												"AND P.cd_pessoa = PE.cd_pessoa " +
												"AND PE.lg_principal = 1 " +
												"AND PE.cd_cidade = PEC.cd_cidade " +
												"AND PEE.cd_estado = PEC.cd_estado " +
												"AND A0.cd_nota_fiscal = A.cd_nota_fiscal " +
												"AND A.cd_tributo          = B.cd_tributo " +
												"AND A.cd_tributo_aliquota = B.cd_tributo_aliquota  " +
												"AND A0.cd_natureza_operacao = C.cd_natureza_operacao " +
												"AND A.cd_nota_fiscal = D.cd_nota_fiscal " +
												"AND A.cd_item = D.cd_item " +
												"AND B.cd_situacao_tributaria = Q.cd_situacao_tributaria " +
												"AND B.cd_tributo           = "+cdTributoICMS +
												"AND Q.cd_tributo = B.cd_tributo " +
												"GROUP BY 1, 2, 3, 4";

			
			PreparedStatement pstmt = connection.prepareStatement(sql);
		
			pstmt.setInt(1, cdDocumento);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			if(tpMov == 0){
				while(rsm.next()){
					//Total Tributo ICMS
					
					ResultSetMap rsm4 = new ResultSetMap(connection.prepareStatement("SELECT st_tributaria, SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_icms, SUM(I.pr_aliquota) AS pr_icms, SUM(I.vl_base_calculo) AS vl_base_icms FROM adm_saida_item_aliquota I " +
																			 " 		  JOIN alm_documento_saida A0 ON (I.cd_documento_saida = A0.cd_documento_saida) " +
																			 "        JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
																			 "		  JOIN grl_pessoa PES ON (A0.cd_cliente = PES.cd_pessoa) " +
																			 "		  JOIN grl_pessoa_endereco PE ON (PES.cd_pessoa = PE.cd_pessoa " +
																			 "									AND PE.lg_principal = 1) " +
																			 "		  JOIN grl_cidade PEC ON (PE.cd_cidade = PEC.cd_cidade) " +
																			 "		  JOIN grl_estado PEE ON (PEE.cd_estado = PEC.cd_estado) " +
																			 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																			 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																			 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria" +
																			 "																	AND Q.cd_tributo = P.cd_tributo) " + 
																			 "        WHERE I.cd_documento_saida = " +cdDocumento +
																			 "		    AND PEE.sg_estado = '" + rsm.getString("sg_estado") + "'" +
																			 "          AND P.cd_tributo           = "+cdTributoICMS + 
																			 "			AND C.nr_codigo_fiscal = '" + rsm.getString("nr_codigo_fiscal") + "'" + 
																			 "			AND I.pr_aliquota = " + rsm.getFloat("pr_aliquota") +  
																			 "			AND Q.nr_situacao_tributaria = '" + rsm.getString("nr_situacao_tributaria") + "'" +  
																			 "        GROUP BY 1").executeQuery());
					while(rsm4.next()){
						if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
							rsm.setValueToField("vl_icms", rsm4.getFloat("vl_icms"));
							rsm.setValueToField("vl_base_icms", rsm4.getFloat("vl_base_icms"));
							rsm.setValueToField("pr_icms", rsm4.getFloat("pr_icms"));
							rsm.setValueToField("st_tributaria", rsm4.getInt("st_tributaria"));
							rsm.setValueToField("nr_situacao_tributaria", rsm4.getString("nr_situacao_tributaria"));
						}
						else if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
							rsm.setValueToField("vl_icms_substituto", rsm4.getFloat("vl_icms"));
							rsm.setValueToField("vl_icms", 0);
							rsm.setValueToField("vl_base_icms_substituto", rsm4.getFloat("vl_base_icms"));
							rsm.setValueToField("vl_base_icms", 0);
							rsm.setValueToField("pr_icms_substituto", rsm4.getFloat("pr_icms"));
							rsm.setValueToField("pr_icms", 0);
						}
					}
					
					rsm4 = new ResultSetMap(connection.prepareStatement("SELECT st_tributaria, SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_ipi, SUM(I.pr_aliquota) AS pr_ipi, SUM(I.vl_base_calculo) AS vl_base_ipi FROM adm_saida_item_aliquota I " +
												 " 		  JOIN alm_documento_saida A0 ON (I.cd_documento_saida = A0.cd_documento_saida) " +
												 "        JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
												 "		  JOIN grl_pessoa PES ON (A0.cd_cliente = PES.cd_pessoa) " +
												 "		  JOIN grl_pessoa_endereco PE ON (PES.cd_pessoa = PE.cd_pessoa " +
												 "									AND PE.lg_principal = 1) " +
												 "		  JOIN grl_cidade PEC ON (PE.cd_cidade = PEC.cd_cidade) " +
												 "		  JOIN grl_estado PEE ON (PEE.cd_estado = PEC.cd_estado) " +
												 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
												 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
												 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria" +
												 "																	AND Q.cd_tributo = P.cd_tributo) " + 
												 "        WHERE I.cd_documento_saida = " +cdDocumento +
												 "		    AND PEE.sg_estado = '" + rsm.getString("sg_estado") + "'" +
												 "          AND P.cd_tributo           = "+cdTributoIPI + 
												 "			AND C.nr_codigo_fiscal = '" + rsm.getString("nr_codigo_fiscal") + "'" + 
												 "			AND I.pr_aliquota = " + rsm.getFloat("pr_aliquota") +  
												 "			AND Q.nr_situacao_tributaria = '" + rsm.getString("nr_situacao_tributaria") + "'" +  
												 "        GROUP BY 1").executeQuery());
					while(rsm4.next()){
						if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
							rsm.setValueToField("vl_ipi", rsm4.getFloat("vl_ipi"));
							rsm.setValueToField("vl_base_ipi", rsm4.getFloat("vl_base_ipi"));
							rsm.setValueToField("pr_ipi", rsm4.getFloat("pr_ipi"));
							rsm.setValueToField("st_tributaria", rsm4.getInt("st_tributaria"));
							rsm.setValueToField("nr_situacao_tributaria", rsm4.getString("nr_situacao_tributaria"));
						}
						else if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
							rsm.setValueToField("vl_ipi_substituto", rsm4.getFloat("vl_ipi"));
							rsm.setValueToField("vl_ipi", 0);
							rsm.setValueToField("vl_base_ipi_substituto", rsm4.getFloat("vl_base_ipi"));
							rsm.setValueToField("vl_base_ipi", 0);
							rsm.setValueToField("pr_ipi_substituto", rsm4.getFloat("pr_ipi"));
							rsm.setValueToField("pr_ipi", 0);
						}
					}
					
				}
				rsm.beforeFirst();
				
				ResultSetMap rsm4 = new ResultSetMap(connection.prepareStatement("SELECT I.*, PEE.sg_estado FROM alm_documento_saida_item I " +
																		 " 		  JOIN alm_documento_saida A0 ON (I.cd_documento_saida = A0.cd_documento_saida) " +
																		 "		  JOIN grl_pessoa P ON (A0.cd_cliente = P.cd_pessoa) " +
																		 "		  JOIN grl_pessoa_endereco PE ON (P.cd_pessoa = PE.cd_pessoa " +
																		 "									AND PE.lg_principal = 1) " +
																		 "		  JOIN grl_cidade PEC ON (PE.cd_cidade = PEC.cd_cidade) " +
																		 "		  JOIN grl_estado PEE ON (PEE.cd_estado = PEC.cd_estado) " +
																		 "        JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
																		 "        WHERE I.cd_documento_saida = " +cdDocumento + 
																		 "			AND NOT EXISTS (SELECT * FROM adm_saida_item_aliquota B WHERE I.cd_documento_saida = B.cd_documento_saida AND I.cd_produto_servico = B.cd_produto_servico AND I.cd_item = B.cd_item)").executeQuery());

				
				
				if(rsm4.next()){
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("ST_TRIBUTARIA", TributoAliquotaServices.ST_SEM_TRIBUTACAO);
					register.put("PR_ALIQUOTA", 0);
					DocumentoSaida doc = DocumentoSaidaDAO.get(cdDocumento);
					NaturezaOperacao natOper = NaturezaOperacaoDAO.get(doc.getCdNaturezaOperacao());
					register.put("NR_CODIGO_FISCAL", natOper.getNrCodigoFiscal());
					register.put("VL_BASE_CALCULO", 0);
					register.put("VL_ICMS", 0);
					register.put("VL_ITENS", 0);
					register.put("VL_BASE_ICMS", 0);
					register.put("PR_ICMS", 0);
					register.put("NR_SITUACAO_TRIBUTARIA", "041");
					register.put("SG_ESTADO", rsm4.getString("sg_estado"));
					rsm.addRegister(register);
				}
			}
			
			else if(tpMov == 1){
//				if(rsm.next()){
//					ResultSetMap rsmEntradaTributo = new ResultSetMap(connection.prepareStatement("SELECT SUM(vl_retido + vl_tributo) AS vl_tributos FROM adm_entrada_tributo WHERE cd_documento_entrada = " + cdDocumento).executeQuery());
//					if(rsmEntradaTributo.next()){
//						rsm.setValueToField("vl_itens", rsm.getFloat("vl_itens") + rsmEntradaTributo.getFloat("vl_tributos"));
//					}
//				}
//				rsm.beforeFirst();
				
				while(rsm.next()){
					//Total Tributo ICMS
					
					ResultSetMap rsm4 = new ResultSetMap(connection.prepareStatement("SELECT st_tributaria, SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_icms, SUM(I.pr_aliquota) AS pr_icms, SUM(I.vl_base_calculo) AS vl_base_icms FROM adm_entrada_item_aliquota I " +
																			 " 		  JOIN alm_documento_entrada A0 ON (I.cd_documento_entrada = A0.cd_documento_entrada) " +
																			 "        JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
																			 "		  JOIN grl_pessoa PP ON (A0.cd_fornecedor = PP.cd_pessoa) " +
																			 "		  JOIN grl_pessoa_endereco PE ON (PP.cd_pessoa = PE.cd_pessoa " +
																			 "									AND PE.lg_principal = 1) " +
																			 "		  JOIN grl_cidade PEC ON (PE.cd_cidade = PEC.cd_cidade) " +
																			 "		  JOIN grl_estado PEE ON (PEE.cd_estado = PEC.cd_estado) " +
																			 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																			 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																			 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria" +
																			 "																	AND Q.cd_tributo = P.cd_tributo) " + 
																			 "        WHERE I.cd_documento_entrada = " +cdDocumento +
																			 "		    AND PEE.sg_estado = '" + rsm.getString("sg_estado") + "'" +
																			 "          AND P.cd_tributo           = "+cdTributoICMS + 
																			 "			AND C.nr_codigo_fiscal = '" + rsm.getString("nr_codigo_fiscal") + "'" + 
																			 "			AND I.pr_aliquota = " + rsm.getFloat("pr_aliquota") +  
																			 "			AND Q.nr_situacao_tributaria = '" + rsm.getString("nr_situacao_tributaria") + "'" +  
																			 "        GROUP BY 1").executeQuery());
					while(rsm4.next()){
						if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
							rsm.setValueToField("vl_icms", rsm4.getFloat("vl_icms"));
							rsm.setValueToField("vl_base_icms", rsm4.getFloat("vl_base_icms"));
							rsm.setValueToField("pr_icms", rsm4.getFloat("pr_icms"));
							rsm.setValueToField("st_tributaria", rsm4.getInt("st_tributaria"));
							rsm.setValueToField("nr_situacao_tributaria", rsm4.getString("nr_situacao_tributaria"));
						}
						else if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
							rsm.setValueToField("vl_icms_substituto", rsm4.getFloat("vl_icms"));
							rsm.setValueToField("vl_icms", 0);
							rsm.setValueToField("vl_base_icms_substituto", rsm4.getFloat("vl_base_icms"));
							rsm.setValueToField("vl_base_icms", 0);
							rsm.setValueToField("pr_icms_substituto", rsm4.getFloat("pr_icms"));
							rsm.setValueToField("pr_icms", 0);
						}
					}
					
					rsm4 = new ResultSetMap(connection.prepareStatement("SELECT st_tributaria, SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_icms, SUM(I.pr_aliquota) AS pr_icms, SUM(I.vl_base_calculo) AS vl_base_icms FROM adm_entrada_item_aliquota I " +
												 " 		  JOIN alm_documento_entrada A0 ON (I.cd_documento_entrada = A0.cd_documento_entrada) " +
												 "        JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
												 "		  JOIN grl_pessoa PP ON (A0.cd_fornecedor = PP.cd_pessoa) " +
												 "		  JOIN grl_pessoa_endereco PE ON (PP.cd_pessoa = PE.cd_pessoa " +
												 "									AND PE.lg_principal = 1) " +
												 "		  JOIN grl_cidade PEC ON (PE.cd_cidade = PEC.cd_cidade) " +
												 "		  JOIN grl_estado PEE ON (PEE.cd_estado = PEC.cd_estado) " +
												 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
												 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
												 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria" +
												 "																	AND Q.cd_tributo = P.cd_tributo) " + 
												 "        WHERE I.cd_documento_entrada = " +cdDocumento +
												 "		    AND PEE.sg_estado = '" + rsm.getString("sg_estado") + "'" +
												 "          AND P.cd_tributo           = "+cdTributoIPI + 
												 "			AND C.nr_codigo_fiscal = '" + rsm.getString("nr_codigo_fiscal") + "'" + 
												 "			AND I.pr_aliquota = " + rsm.getFloat("pr_aliquota") +  
												 "			AND Q.nr_situacao_tributaria = '" + rsm.getString("nr_situacao_tributaria") + "'" +  
												 "        GROUP BY 1").executeQuery());
					while(rsm4.next()){
						if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
							rsm.setValueToField("vl_ipi", rsm4.getFloat("vl_ipi"));
							rsm.setValueToField("vl_base_ipi", rsm4.getFloat("vl_base_ipi"));
							rsm.setValueToField("pr_ipi", rsm4.getFloat("pr_ipi"));
							rsm.setValueToField("st_tributaria", rsm4.getInt("st_tributaria"));
							rsm.setValueToField("nr_situacao_tributaria", rsm4.getString("nr_situacao_tributaria"));
						}
						else if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
							rsm.setValueToField("vl_ipi_substituto", rsm4.getFloat("vl_ipi"));
							rsm.setValueToField("vl_ipi", 0);
							rsm.setValueToField("vl_base_ipi_substituto", rsm4.getFloat("vl_base_ipi"));
							rsm.setValueToField("vl_base_ipi", 0);
							rsm.setValueToField("pr_ipi_substituto", rsm4.getFloat("pr_ipi"));
							rsm.setValueToField("pr_ipi", 0);
						}
					}
					
				}
				rsm.beforeFirst();
				
				ResultSetMap rsm4 = new ResultSetMap(connection.prepareStatement("SELECT I.*, PEE.sg_estado FROM alm_documento_entrada_item I " +
																		 " 		  JOIN alm_documento_entrada A0 ON (I.cd_documento_entrada = A0.cd_documento_entrada) " +
																		 "        JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
																		 "		  JOIN grl_pessoa P ON (A0.cd_fornecedor = P.cd_pessoa) " +
																		 "		  JOIN grl_pessoa_endereco PE ON (P.cd_pessoa = PE.cd_pessoa " +
																		 "									AND PE.lg_principal = 1) " +
																		 "		  JOIN grl_cidade PEC ON (PE.cd_cidade = PEC.cd_cidade) " +
																		 "		  JOIN grl_estado PEE ON (PEE.cd_estado = PEC.cd_estado) " +
																		 "        WHERE I.cd_documento_entrada = " +cdDocumento +
																		 "			AND NOT EXISTS (SELECT * FROM adm_entrada_item_aliquota B WHERE I.cd_documento_entrada = B.cd_documento_entrada AND I.cd_produto_servico = B.cd_produto_servico AND I.cd_item = B.cd_item)").executeQuery());

				
				
				if(rsm4.next()){
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("ST_TRIBUTARIA", TributoAliquotaServices.ST_SEM_TRIBUTACAO);
					register.put("PR_ALIQUOTA", 0);
					DocumentoEntrada doc = DocumentoEntradaDAO.get(cdDocumento);
					NaturezaOperacao natOper = NaturezaOperacaoDAO.get(doc.getCdNaturezaOperacao());
					register.put("NR_CODIGO_FISCAL", natOper.getNrCodigoFiscal());
					register.put("VL_BASE_CALCULO", 0);
					register.put("VL_ICMS", 0);
					register.put("VL_ITENS", 0);
					register.put("VL_BASE_ICMS", 0);
					register.put("PR_ICMS", 0);
					register.put("NR_SITUACAO_TRIBUTARIA", "041");
					register.put("SG_ESTADO", rsm4.getString("sg_estado"));
					rsm.addRegister(register);
				}
				
				
			}
			else{
				while(rsm.next()){
					//Total Tributo ICMS
					PreparedStatement pstmt4 = connection.prepareStatement("SELECT st_tributaria, SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_icms, SUM(I.pr_aliquota) AS pr_icms, SUM(I.vl_base_calculo) AS vl_base_icms FROM fsc_nota_fiscal_item_tributo I " +
																			 " 		  JOIN fsc_nota_fiscal A0 ON (I.cd_nota_fiscal = A0.cd_nota_fiscal) " +
																			 "        JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
																			 "		  JOIN grl_pessoa PP ON (A0.cd_destinatario = PP.cd_pessoa) " +
																		     "		  JOIN grl_pessoa_endereco PE ON (PP.cd_pessoa = PE.cd_pessoa " +
																			 "									AND PE.lg_principal = 1) " +
																			 "		  JOIN grl_cidade PEC ON (PE.cd_cidade = PEC.cd_cidade) " +
																			 "		  JOIN grl_estado PEE ON (PEE.cd_estado = PEC.cd_estado) " +
																			 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
																			 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
																			 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria" +
																			 "																	AND Q.cd_tributo = P.cd_tributo) " + 
																			 "        WHERE I.cd_nota_fiscal = " +cdDocumento +
																			 "		    AND PEE.sg_estado = '" + rsm.getString("sg_estado") + "'" +
																			 "          AND P.cd_tributo           = "+cdTributoICMS + 
																			 "			AND C.nr_codigo_fiscal = '" + rsm.getString("nr_codigo_fiscal") + "'" + 
																			 "			AND I.pr_aliquota = " + rsm.getFloat("pr_aliquota") +  
																			 "			AND Q.nr_situacao_tributaria = '" + rsm.getString("nr_situacao_tributaria") + "'" +  
												 							 "        GROUP BY 1");
					ResultSetMap rsm4 = new ResultSetMap(pstmt4.executeQuery());
					while(rsm4.next()){
						if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
							rsm.setValueToField("vl_icms", rsm4.getFloat("vl_icms"));
							rsm.setValueToField("vl_base_icms", rsm4.getFloat("vl_base_icms"));
							rsm.setValueToField("pr_icms", rsm4.getFloat("pr_icms"));
							rsm.setValueToField("st_tributaria", rsm4.getInt("st_tributaria"));
							rsm.setValueToField("nr_situacao_tributaria", rsm4.getString("nr_situacao_tributaria"));
						}
						else if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
							rsm.setValueToField("vl_icms_substituto", rsm4.getFloat("vl_icms"));
							rsm.setValueToField("vl_icms", 0);
							rsm.setValueToField("vl_base_icms_substituto", rsm4.getFloat("vl_base_icms"));
							rsm.setValueToField("vl_base_icms", 0);
							rsm.setValueToField("pr_icms_substituto", rsm4.getFloat("pr_icms"));
							rsm.setValueToField("pr_icms", 0);
						}
					}
					
					rsm4 = new ResultSetMap(connection.prepareStatement("SELECT st_tributaria, SUM((I.pr_aliquota/100) * I.vl_base_calculo) AS vl_icms, SUM(I.pr_aliquota) AS pr_icms, SUM(I.vl_base_calculo) AS vl_base_icms FROM fsc_nota_fiscal_item_tributo I " +
												 " 		  JOIN fsc_nota_fiscal A0 ON (I.cd_nota_fiscal = A0.cd_nota_fiscal) " +
												 "        JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
												 "		  JOIN grl_pessoa PP ON (A0.cd_destinatario = PP.cd_pessoa) " +
											     "		  JOIN grl_pessoa_endereco PE ON (PP.cd_pessoa = PE.cd_pessoa " +
												 "									AND PE.lg_principal = 1) " +
												 "		  JOIN grl_cidade PEC ON (PE.cd_cidade = PEC.cd_cidade) " +
												 "		  JOIN grl_estado PEE ON (PEE.cd_estado = PEC.cd_estado) " +
												 "		  LEFT OUTER JOIN adm_tributo_aliquota     P ON (I.cd_tributo = P.cd_tributo " +
												 "        				                                    AND I.cd_tributo_aliquota = P.cd_tributo_aliquota) " +
												 "		  LEFT OUTER JOIN fsc_situacao_tributaria  Q ON (P.cd_situacao_tributaria = Q.cd_situacao_tributaria" +
												 "																	AND Q.cd_tributo = P.cd_tributo) " + 
												 "        WHERE I.cd_nota_fiscal = " +cdDocumento +
												 "		    AND PEE.sg_estado = '" + rsm.getString("sg_estado") + "'" +
												 "          AND P.cd_tributo           = "+cdTributoIPI + 
												 "			AND C.nr_codigo_fiscal = '" + rsm.getString("nr_codigo_fiscal") + "'" + 
												 "			AND I.pr_aliquota = " + rsm.getFloat("pr_aliquota") +  
												 "			AND Q.nr_situacao_tributaria = '" + rsm.getString("nr_situacao_tributaria") + "'" +  
												 "        GROUP BY 1").executeQuery());
					while(rsm4.next()){
						if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_TRIBUTACAO_INTEGRAL){
							rsm.setValueToField("vl_ipi", rsm4.getFloat("vl_ipi"));
							rsm.setValueToField("vl_base_ipi", rsm4.getFloat("vl_base_ipi"));
							rsm.setValueToField("pr_ipi", rsm4.getFloat("pr_ipi"));
							rsm.setValueToField("st_tributaria", rsm4.getInt("st_tributaria"));
							rsm.setValueToField("nr_situacao_tributaria", rsm4.getString("nr_situacao_tributaria"));
						}
						else if(rsm4.getInt("st_tributaria") == TributoAliquotaServices.ST_SUBSTITUICAO_TRIBUTARIA){
							rsm.setValueToField("vl_ipi_substituto", rsm4.getFloat("vl_ipi"));
							rsm.setValueToField("vl_ipi", 0);
							rsm.setValueToField("vl_base_ipi_substituto", rsm4.getFloat("vl_base_ipi"));
							rsm.setValueToField("vl_base_ipi", 0);
							rsm.setValueToField("pr_ipi_substituto", rsm4.getFloat("pr_ipi"));
							rsm.setValueToField("pr_ipi", 0);
						}
					}
					
				}
				rsm.beforeFirst();
				
				ResultSetMap rsm4 = new ResultSetMap(connection.prepareStatement("SELECT I.*, PEE.sg_estado FROM fsc_nota_fiscal_item_tributo I " +
																		 " 		  JOIN fsc_nota_fiscal A0 ON (I.cd_nota_fiscal = A0.cd_nota_fiscal) " +
																		 "        JOIN adm_natureza_operacao C ON (A0.cd_natureza_operacao = C.cd_natureza_operacao) " +
																		 "		  JOIN grl_pessoa P ON (A0.cd_destinatario = P.cd_pessoa) " +
																	     "		  JOIN grl_pessoa_endereco PE ON (P.cd_pessoa = PE.cd_pessoa " +
																		 "									AND PE.lg_principal = 1) " +
																		 "		  JOIN grl_cidade PEC ON (PE.cd_cidade = PEC.cd_cidade) " +
																		 "		  JOIN grl_estado PEE ON (PEE.cd_estado = PEC.cd_estado) " +
																		 "        WHERE I.cd_nota_fiscal = " +cdDocumento +
																		 "			AND NOT EXISTS (SELECT * FROM fsc_nota_fiscal_item_tributo B WHERE I.cd_nota_fiscal = B.cd_nota_fiscal AND I.cd_item = B.cd_item)").executeQuery());

				
				if(rsm4.next()){
					HashMap<String, Object> register = new HashMap<String, Object>();
					register.put("ST_TRIBUTARIA", TributoAliquotaServices.ST_SEM_TRIBUTACAO);
					register.put("PR_ALIQUOTA", 0);
					DocumentoSaida doc = DocumentoSaidaDAO.get(cdDocumento);
					NaturezaOperacao natOper = NaturezaOperacaoDAO.get(doc.getCdNaturezaOperacao());
					register.put("NR_CODIGO_FISCAL", natOper.getNrCodigoFiscal());
					register.put("VL_BASE_CALCULO", 0);
					register.put("VL_ICMS", 0);
					register.put("VL_ITENS", 0);
					register.put("VL_BASE_ICMS", 0);
					register.put("PR_ICMS", 0);
					register.put("NR_SITUACAO_TRIBUTARIA", "041");
					register.put("SG_ESTADO", rsm4.getString("sg_estado"));
					rsm.addRegister(register);
				}
			}
			rsm.beforeFirst();
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			System.out.println("ERRO getTributacaoByEstado: ");
			e.printStackTrace();
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	public static ArrayList<Integer> getTitulosDeCredito(int cdDocumentoSaida){
		ArrayList<Integer> arrayDeCodigos = new ArrayList<Integer>();
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT cd_conta_receber FROM adm_conta_receber WHERE cd_documento_saida = ?");
			pstmt.setInt(1, cdDocumentoSaida);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				
				ContaReceber contaR = ContaReceberDAO.get(rsm.getInt("cd_conta_receber"));
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("CD_CONTA_RECEBER", Integer.toString(contaR.getCdContaReceber()), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmTitulo = TituloCreditoDAO.find(criterios);
				if(rsmTitulo.next()){
					arrayDeCodigos.add(rsmTitulo.getInt("cd_titulo_credito"));
				}
				
			}
			return arrayDeCodigos;
		}
		catch(Exception e){
			System.out.println("ERRO getTitulosDeCredito: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	public static float getValorTotalContasAPagar(ArrayList<Integer> contasAPagar){
		
		try {
			float valorTotal = 0;
			for(int i = 0; i < contasAPagar.size(); i++){
				
				int cdContaPagar = contasAPagar.get(i);
				System.out.println("cdContaPagar: " + cdContaPagar);
				ContaPagar conta = ContaPagarDAO.get(cdContaPagar);
				System.out.println("conta: " + conta);
				valorTotal += conta.getVlConta();
				System.out.println("valorTotal: " + valorTotal);
			}
			
			return valorTotal;
		}
		catch(Exception e){
			System.out.println("ERRO getValorTotalContasAPagar: " + e);
			return 0;
		}
		
		
	}
	
	public static float getValorTotalTitulos(ArrayList<Integer> contasAReceber){
		
		try {
			float valorTotal = 0;
			for(int i = 0; i < contasAReceber.size(); i++){
				int cdContaReceber = contasAReceber.get(i);
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("CD_CONTA_RECEBER", Integer.toString(cdContaReceber), ItemComparator.EQUAL, Types.INTEGER));
				ResultSetMap rsmTitulo = TituloCreditoDAO.find(criterios);
				if(rsmTitulo.next()){
					valorTotal += rsmTitulo.getFloat("vl_titulo");
				}
			}
			
			return valorTotal;
		}
		catch(Exception e){
			System.out.println("ERRO getValorTotalTitulos: " + e);
			return 0;
		}
		
		
	}
	
	public static TipoDocumento getTipoDocumentoSAI(ArrayList<Integer> contasAReceber){
		try {
			int cdTipoDocumento = 0;
			
			for(int i = 0; i < contasAReceber.size(); i++){
				int cdContaReceber = contasAReceber.get(i);
				ContaReceber contaR = ContaReceberDAO.get(cdContaReceber);
				if(i == 0)
					cdTipoDocumento = contaR.getCdTipoDocumento();
				else
					if(cdTipoDocumento != contaR.getCdTipoDocumento())
						return null;
			}
			
			return TipoDocumentoDAO.get(cdTipoDocumento);
		}
		catch(Exception e){
			System.out.println("ERRO getTipoDocumentoSAI: " + e);
			return null;
		}
	}
	
	public static TipoDocumento getTipoDocumentoENT(ArrayList<Integer> contasAPagar){
		try {
			int cdTipoDocumento = 0;
			
			for(int i = 0; i < contasAPagar.size(); i++){
				int cdContaAPagar = contasAPagar.get(i);
				ContaPagar contaR = ContaPagarDAO.get(cdContaAPagar);
				if(i == 0)
					cdTipoDocumento = contaR.getCdTipoDocumento();
				else
					if(cdTipoDocumento != contaR.getCdTipoDocumento())
						return null;
			}
			
			return TipoDocumentoDAO.get(cdTipoDocumento);
		}
		catch(Exception e){
			System.out.println("ERRO getTipoDocumentoENT: " + e);
			return null;
		}
	}
	
	public static boolean isCombustivel(DocumentoSaida documento, int cdEmpresa){
		try {
			
			ResultSetMap rsmProdutos = DocumentoSaidaServices.getAllItens(documento.getCdDocumentoSaida(), false);
			while(rsmProdutos.next()){
				int cdProdutoServico = rsmProdutos.getInt("cd_produto_servico");
				ProdutoGrupo prodG = ProdutoGrupoDAO.get(cdProdutoServico, com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0), cdEmpresa);
				if(prodG != null)
					return true;
				
			}
			return false;
		}
		catch(Exception e){
			System.out.println("ERRO isCombustivel Sai: " + e);
			return false;
		}
	}
	
	public static boolean isCombustivel(DocumentoEntrada documento, int cdEmpresa){
		try {
			
			ResultSetMap rsmProdutos = DocumentoEntradaServices.getAllItens(documento.getCdDocumentoEntrada(), false);
			while(rsmProdutos.next()){
				int cdProdutoServico = rsmProdutos.getInt("cd_produto_servico");
				ProdutoGrupo prodG = ProdutoGrupoDAO.get(cdProdutoServico, com.tivic.manager.grl.ParametroServices.getValorOfParametroAsInteger("CD_GRUPO_COMBUSTIVEL", 0), cdEmpresa);
				if(prodG != null)
					return true;
				
			}
			return false;
		}
		catch(Exception e){
			System.out.println("ERRO isCombustivel Ent: " + e);
			return false;
		}
	}
	
	
	public static ResultSetMap getValorTotalCartaoNoPeriodo(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT ( " + 
																    "	SELECT SUM(A.vl_recebido) AS vl_total FROM adm_conta_receber A " + 
																	"		  JOIN adm_forma_pagamento         C ON (A.cd_forma_pagamento = C.cd_forma_pagamento AND C.tp_forma_pagamento = "+FormaPagamentoServices.TEF+") " + 
																	"		  LEFT OUTER JOIN adm_forma_pagamento_empresa B ON (B.qt_dias_credito > 2 AND A.cd_forma_pagamento = B.cd_forma_pagamento  AND B.cd_empresa = ? AND B.cd_administrador = Z.cd_pessoa) " + 
																	"		  WHERE A.dt_recebimento >= ? AND A.dt_recebimento <= ?" + 
																	" ) AS vl_credito, ( " + 
																	" SELECT SUM(A.vl_recebido) AS vl_total FROM adm_conta_receber A " + 
																	"		  JOIN adm_forma_pagamento         C ON (A.cd_forma_pagamento = C.cd_forma_pagamento AND C.tp_forma_pagamento = "+FormaPagamentoServices.TEF+") " + 
																	"		  LEFT OUTER JOIN adm_forma_pagamento_empresa B ON (B.qt_dias_credito <= 2 AND A.cd_forma_pagamento = B.cd_forma_pagamento  AND B.cd_empresa = ? AND B.cd_administrador = Z.cd_pessoa) " + 
																	"		  WHERE A.dt_recebimento >= ? AND A.dt_recebimento <= ? " + 
																	" ) AS vl_debito, Z.cd_pessoa FROM adm_conta_receber R  " +
																			"		  JOIN adm_forma_pagamento         C ON (R.cd_forma_pagamento = C.cd_forma_pagamento AND C.tp_forma_pagamento = "+FormaPagamentoServices.TEF+")  " +
																			"		  LEFT OUTER JOIN adm_forma_pagamento_empresa B ON (R.cd_forma_pagamento = B.cd_forma_pagamento  AND B.cd_empresa = ?)  " +
																			"		  LEFT OUTER  JOIN grl_pessoa                  Z ON (Z.cd_pessoa = B.cd_administrador) " +
																			"		  WHERE R.vl_recebido > 0 AND R.dt_recebimento >= ? AND R.dt_recebimento <= ? AND EXISTS (SELECT Y.cd_administrador FROM adm_forma_pagamento_empresa Y where Y.cd_administrador = Z.cd_pessoa)" +
																			"		  GROUP BY Z.cd_pessoa");
			
			pstmt.setInt(1, cdEmpresa);
			pstmt.setTimestamp(2, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(3, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(4, cdEmpresa);
			pstmt.setTimestamp(5, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(6, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(7, cdEmpresa);
			pstmt.setTimestamp(8, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(9, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			return rsm;
		}
		catch(Exception e){
			System.out.println("ERRO getValorTotalCartaoNoPeriodo: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	public static boolean getHas1600(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT B.cd_administrador FROM adm_conta_receber R " +
																	"  JOIN adm_forma_pagamento         C ON (R.cd_forma_pagamento = C.cd_forma_pagamento AND C.tp_forma_pagamento = "+FormaPagamentoServices.TEF+")   " +
																	"  LEFT OUTER JOIN adm_forma_pagamento_empresa B ON (R.cd_forma_pagamento = B.cd_forma_pagamento  AND B.cd_empresa = ?)   " +
																	"  LEFT OUTER  JOIN grl_pessoa                  D ON (D.cd_pessoa = B.cd_administrador)  " +
																	"  WHERE R.vl_recebido > 0 AND R.dt_recebimento >= ? AND R.dt_recebimento <= ? AND EXISTS (SELECT Y.cd_administrador FROM adm_forma_pagamento_empresa Y where Y.cd_administrador = D.cd_pessoa) " +
																	"  GROUP BY cd_administrador ");
			pstmt.setInt(1, cdEmpresa);
			pstmt.setTimestamp(2, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(3, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			return rsm.next();
		}
		catch(Exception e){
			System.out.println("ERRO getHas1600: " + e);
			return false;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	
	public static ResultSetMap getMovimentacaoDiariaCombustivel(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT D.cd_produto_servico, GPS.id_produto_servico, GPSE.id_reduzido, A.dt_fechamento, SUM(E.qt_volume) AS qt_estoque_inicial, " +
																	" 	(SELECT SUM(ADEI.qt_entrada) AS qt_entradas FROM alm_documento_entrada_item ADEI " +
																	"		JOIN alm_documento_entrada ADE ON (ADEI.cd_documento_entrada = ADE.cd_documento_entrada) " +
																	"		WHERE CAST(ADE.dt_documento_entrada AS DATE) = A.dt_fechamento " +
																	"			AND ADEI.cd_produto_servico = D.cd_produto_servico " +
																	"			AND ADE.cd_empresa = ?" +
																	"			AND ADE.st_documento_entrada = " + DocumentoEntradaServices.ST_LIBERADO +
																	"			AND ADE.tp_documento_entrada <> " + DocumentoEntradaServices.TP_DOC_NAO_FISCAL+
																	"	) AS qt_entradas " +
																	" 	FROM pcb_medicao_fisica E " + 
																	"	JOIN adm_conta_fechamento A ON (A.cd_conta = E.cd_conta " +
																	"												AND A.cd_fechamento = E.cd_fechamento" +
																	"												AND A.cd_turno = 1) " +
																	"	JOIN pcb_tanque D ON (D.cd_tanque = E.cd_tanque) " +
																	"   LEFT OUTER JOIN grl_produto_servico GPS ON (GPS.cd_produto_servico = D.cd_produto_servico) " +
																	"   LEFT OUTER JOIN grl_produto_servico_empresa GPSE ON (GPS.cd_produto_servico = GPSE.cd_produto_servico" +
																	"																AND GPSE.cd_empresa = "+cdEmpresa+") " +
																	"	WHERE A.dt_fechamento >= ? AND A.dt_fechamento <= ? " + 
																	"	GROUP BY D.cd_produto_servico, A.dt_fechamento, GPS.id_produto_servico, GPSE.id_reduzido " + 
																	"	ORDER BY A.dt_fechamento, D.cd_produto_servico");
			
			PreparedStatement pstmtSeg = connection.prepareStatement("SELECT SUM(E.qt_volume) AS qt_estoque_inicial " +
																" 	FROM pcb_medicao_fisica E " + 
																"	JOIN adm_conta_fechamento A ON (A.cd_conta = E.cd_conta " +
																"												AND A.cd_fechamento = E.cd_fechamento" +
																"												AND A.cd_turno = 1) " +
																"	JOIN pcb_tanque D ON (D.cd_tanque = E.cd_tanque) " +
																"	WHERE A.dt_fechamento = ? AND D.cd_produto_servico = ?");
			
			PreparedStatement pstmtBicoEncerrante = connection.prepareStatement("SELECT SUM(A.qt_litros) AS qt_saidas " +
																				" 	FROM pcb_bico_encerrante A " +
																				" 	JOIN adm_conta_fechamento C ON (A.cd_conta = C.cd_conta " +
																				"										AND A.cd_fechamento = C.cd_fechamento) " +
																				"	JOIN pcb_tanque B ON (B.cd_tanque = A.cd_tanque) " +
																				"	WHERE C.dt_fechamento = ? " +
																				"	AND B.cd_produto_servico = ?");

			
//			float qtEstoqueInicialAnt = 0;
//			float qtEntradasAnt		  = 0;
//			float qtSaidasAnt		  = 0;
//			HashMap<Integer, ArrayList<Float>> hashDiaAnt = new HashMap<Integer, ArrayList<Float>>();
			pstmt.setInt(1, cdEmpresa);
			pstmt.setTimestamp(2, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(3, new Timestamp(dtFinal.getTimeInMillis()));
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				//Descobrir as saídas do produto naquele dia
				pstmtBicoEncerrante.setTimestamp(1, rsm.getTimestamp("dt_fechamento"));
				pstmtBicoEncerrante.setInt(2, rsm.getInt("cd_produto_servico"));
				ResultSetMap rsmBicoEncerrante = new ResultSetMap(pstmtBicoEncerrante.executeQuery());
				while(rsmBicoEncerrante.next()){
					rsm.setValueToField("qt_saidas", rsmBicoEncerrante.getFloat("qt_saidas"));
				}
				rsm.setValueToField("qt_entradas", rsm.getFloat("qt_entradas"));
				
//				//Se for o primeiro dia do mes não há perdas nem sobras
//				if(rsm.getGregorianCalendar("dt_fechamento").get(Calendar.DAY_OF_MONTH) == 1){
				ArrayList<Float> arrayValores = new ArrayList<Float>();
				arrayValores.add(rsm.getFloat("qt_estoque_inicial"));
				arrayValores.add(rsm.getFloat("qt_entradas"));
				arrayValores.add(rsm.getFloat("qt_saidas"));
				
//					hashDiaAnt.put(rsm.getInt("cd_produto_servico"), arrayValores);
//					rsm.setValueToField("qt_perdas", 0.0);
//					rsm.setValueToField("qt_ganhos", 0.0);
				
				//Buscar qt_fechamento a partir da medicao fisica do dia seguinte
				GregorianCalendar diaSeguinte = rsm.getGregorianCalendar("dt_fechamento");
				diaSeguinte.add(Calendar.DAY_OF_MONTH, 1);
				pstmtSeg.setTimestamp(1, new Timestamp(diaSeguinte.getTimeInMillis()));
				pstmtSeg.setInt(2, rsm.getInt("cd_produto_servico"));
				ResultSet rsSeg = pstmtSeg.executeQuery();
				
				if(rsSeg.next()){
					rsm.setValueToField("qt_fechamento", rsSeg.getFloat("qt_estoque_inicial"));
				}
				
				float qtEstoqueInicial 	   = Float.parseFloat(Util.formatNumber(rsm.getFloat("qt_estoque_inicial"), "#.###").replaceAll(",", "."));
				float qtEstoqueInicialTemp = Float.parseFloat(Util.formatNumber((qtEstoqueInicial * 1000), "#").replaceAll(",", "."));
				float qtEntradas       	   = Float.parseFloat(Util.formatNumber(rsm.getFloat("qt_entradas"), "#.###").replaceAll(",", "."));
				float qtEntradasTemp 	   = Float.parseFloat(Util.formatNumber((qtEntradas * 1000), "#").replaceAll(",", "."));
				float qtSaidas             = Float.parseFloat(Util.formatNumber(rsm.getFloat("qt_saidas"), "#.###").replaceAll(",", "."));
				float qtSaidasTemp 		   = Float.parseFloat(Util.formatNumber((qtSaidas * 1000), "#").replaceAll(",", "."));
				float qtPrevisto 		   = (qtEstoqueInicialTemp + qtEntradasTemp - qtSaidasTemp) / 1000;
				qtPrevisto				   = Float.parseFloat(Util.formatNumber(qtPrevisto, "#.###").replaceAll(",", "."));
				float qtPrevistoTemp 	   = ((qtEstoqueInicialTemp + qtEntradasTemp - qtSaidasTemp));
				float qtFechamento 	       = Float.parseFloat(Util.formatNumber(rsm.getFloat("qt_fechamento"), "#.###").replaceAll(",", "."));
				float qtFechamentoTemp     = Float.parseFloat(Util.formatNumber((qtFechamento * 1000), "#").replaceAll(",", "."));
				float qtGanho			   = Float.parseFloat(Util.formatNumber((qtPrevistoTemp - qtFechamentoTemp), "#.###").replaceAll(",", ".")) / 1000;
				float qtPerda			   = Float.parseFloat(Util.formatNumber((qtFechamentoTemp - qtPrevistoTemp), "#.###").replaceAll(",", ".")) / 1000;
				
				
				if(qtPrevisto > qtFechamento){
					rsm.setValueToField("qt_perdas", qtGanho);
					rsm.setValueToField("qt_ganhos", 0.0);
				}
				
				else if(qtPrevisto < qtFechamento){
					rsm.setValueToField("qt_perdas", 0.0);
					rsm.setValueToField("qt_ganhos", qtPerda);
				}
				
				else{
					rsm.setValueToField("qt_perdas", 0.0);
					rsm.setValueToField("qt_ganhos", 0.0);
				}
//					continue;
//				}
//				else{
//					ArrayList<Float> arrayValores = hashDiaAnt.get(rsm.getInt("cd_produto_servico"));
//					
//					qtEstoqueInicialAnt = arrayValores.get(0);
//					qtEntradasAnt 		= arrayValores.get(1);
//					qtSaidasAnt 		= arrayValores.get(2);
//					
//				}
//				//Se o estoque final do dia anterior for maior que o estoque inicial desse dia, então houve perdas
//				if((qtEstoqueInicialAnt + qtEntradasAnt - qtSaidasAnt) > rsm.getFloat("qt_estoque_inicial")){
//					rsm.setValueToField("qt_perdas", ((qtEstoqueInicialAnt + qtEntradasAnt - qtSaidasAnt) - rsm.getFloat("qt_estoque_inicial")));
//					rsm.setValueToField("qt_ganhos", 0.0);
//				}
//				//Se o estoque final do dia anterior for menor que o estoque inicial desse dia, então houve ganhos
//				else if ((qtEstoqueInicialAnt + qtEntradasAnt - qtSaidasAnt) < rsm.getFloat("qt_estoque_inicial")){
//					rsm.setValueToField("qt_perdas", 0.0);
//					rsm.setValueToField("qt_ganhos", (rsm.getFloat("qt_estoque_inicial") - (qtEstoqueInicialAnt + qtEntradasAnt - qtSaidasAnt)));
//				}
//				//Se o estoque final do dia anterior for igual que o estoque inicial desse dia, então não houve alterações
//				else{
//					rsm.setValueToField("qt_perdas", 0.0);
//					rsm.setValueToField("qt_ganhos", 0.0);
//				}
				
				//Buscar qt_fechamento a partir da medicao fisica do dia seguinte
//				GregorianCalendar diaSeguinte = rsm.getGregorianCalendar("dt_fechamento");
//				diaSeguinte.add(Calendar.DAY_OF_MONTH, 1);
//				pstmtSeg.setTimestamp(1, new Timestamp(diaSeguinte.getTimeInMillis()));
//				pstmtSeg.setInt(2, rsm.getInt("cd_produto_servico"));
//				ResultSet rsSeg = pstmtSeg.executeQuery();
//				
//				if(rsSeg.next()){
//					rsm.setValueToField("qt_fechamento", rsSeg.getFloat("qt_estoque_inicial"));
//				}
//				
//				//Armazena o estoque final desse dia para comparar com o próximo dia
//				ArrayList<Float> arrayValores = new ArrayList<Float>();
//				arrayValores.add(rsm.getFloat("qt_estoque_inicial"));
//				arrayValores.add(rsm.getFloat("qt_entradas"));
//				arrayValores.add(rsm.getFloat("qt_saidas"));
//				hashDiaAnt.put(rsm.getInt("cd_produto_servico"), arrayValores);
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			System.out.println("ERRO getMovimentacaoDiariaCombustivel: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getMovimentacaoDiariaCombustivelPorTanque(int cdEmpresa, GregorianCalendar data, int cdProdutoServico){
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			PreparedStatement pstmt = connection.prepareStatement("SELECT D.cd_tanque, A.dt_fechamento, SUM(qt_volume) AS qt_estoque_inicial, " +
																	" 	(SELECT SUM(ADEI.qt_entrada) AS qt_entradas FROM alm_documento_entrada_item ADEI " +
																	"		JOIN alm_documento_entrada ADE ON (ADEI.cd_documento_entrada = ADE.cd_documento_entrada) " +
																	"		WHERE CAST(ADE.dt_documento_entrada AS DATE) = A.dt_fechamento " +
																	"			AND ADEI.cd_produto_servico = D.cd_produto_servico " +
																	"			AND ADE.cd_empresa = ?" +
																	"			AND ADE.st_documento_entrada = " + DocumentoEntradaServices.ST_LIBERADO +
																	"			AND ADE.tp_documento_entrada <> " + DocumentoEntradaServices.TP_DOC_NAO_FISCAL+
																	"	) AS qt_entradas " +
																	" 	FROM adm_conta_fechamento A " + 
																	"	LEFT OUTER JOIN pcb_medicao_fisica E ON (A.cd_conta = E.cd_conta" +
																	"												AND A.cd_fechamento = E.cd_fechamento" +
																	"												AND A.cd_turno = 1) " +
																	"	JOIN pcb_tanque D ON (D.cd_tanque = E.cd_tanque) " +
																	"	WHERE A.dt_fechamento = ? " +
																	"			AND D.cd_produto_servico = ?" + 
																	"	GROUP BY D.cd_tanque, D.cd_produto_servico, A.dt_fechamento " + 
																	"	ORDER BY A.dt_fechamento, D.cd_tanque");
			
			PreparedStatement pstmtBicoEncerrante = connection.prepareStatement("SELECT SUM(A.qt_litros) AS qt_saidas " +
																				" 	FROM pcb_bico_encerrante A " +
																				" 	JOIN adm_conta_fechamento C ON (A.cd_conta = C.cd_conta " +
																				"										AND A.cd_fechamento = C.cd_fechamento) " +
																				"	WHERE C.dt_fechamento = ? " +
																				"			AND A.cd_tanque = ?");
			
			PreparedStatement pstmtSeg = connection.prepareStatement("SELECT SUM(E.qt_volume) AS qt_estoque_inicial " +
																	" 	FROM pcb_medicao_fisica E " + 
																	"	JOIN adm_conta_fechamento A ON (A.cd_conta = E.cd_conta " +
																	"												AND A.cd_fechamento = E.cd_fechamento" +
																	"												AND A.cd_turno = 1) " +
																	"	JOIN pcb_tanque D ON (D.cd_tanque = E.cd_tanque) " +
																	"	WHERE A.dt_fechamento = ? AND D.cd_produto_servico = ? AND D.cd_tanque = ?");
			
//			float qtEstoqueInicialAnt = 0;
//			float qtEntradasAnt		  = 0;
//			float qtSaidasAnt		  = 0;
			pstmt.setInt(1, cdEmpresa);
			pstmt.setTimestamp(2, new Timestamp(data.getTimeInMillis()));
			pstmt.setInt(3, cdProdutoServico);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			rsm.beforeFirst();
			while(rsm.next()){
				//Descobrir as saídas do produto naquele dia
				pstmtBicoEncerrante.setTimestamp(1, rsm.getTimestamp("dt_fechamento"));
				pstmtBicoEncerrante.setInt(2, rsm.getInt("cd_tanque"));
				ResultSetMap rsmBicoEncerrante = new ResultSetMap(pstmtBicoEncerrante.executeQuery());
				while(rsmBicoEncerrante.next()){
					rsm.setValueToField("qt_saidas", rsmBicoEncerrante.getFloat("qt_saidas"));
				}
				rsm.setValueToField("qt_entradas", rsm.getFloat("qt_entradas"));
				
				GregorianCalendar diaSeguinte = (GregorianCalendar)data.clone();
				diaSeguinte.add(Calendar.DAY_OF_MONTH, 1);
				pstmtSeg.setTimestamp(1, new Timestamp(diaSeguinte.getTimeInMillis()));
				pstmtSeg.setInt(2, cdProdutoServico);
				pstmtSeg.setInt(3, rsm.getInt("cd_tanque"));
				ResultSetMap rsmSeg = new ResultSetMap(pstmtSeg.executeQuery());
				while(rsmSeg.next()){
					rsm.setValueToField("qt_fechamento", rsmSeg.getFloat("qt_estoque_inicial"));
				}
				
				float qtEstoqueInicial 	   = Float.parseFloat(Util.formatNumber(rsm.getFloat("qt_estoque_inicial"), "#.###").replaceAll(",", "."));
				float qtEstoqueInicialTemp = Float.parseFloat(Util.formatNumber((qtEstoqueInicial * 1000), "#").replaceAll(",", "."));
				float qtEntradas       	   = Float.parseFloat(Util.formatNumber(rsm.getFloat("qt_entradas"), "#.###").replaceAll(",", "."));
				float qtEntradasTemp 	   = Float.parseFloat(Util.formatNumber((qtEntradas * 1000), "#").replaceAll(",", "."));
				float qtSaidas             = Float.parseFloat(Util.formatNumber(rsm.getFloat("qt_saidas"), "#.###").replaceAll(",", "."));
				float qtSaidasTemp 		   = Float.parseFloat(Util.formatNumber((qtSaidas * 1000), "#").replaceAll(",", "."));
				float qtPrevisto 		   = (qtEstoqueInicialTemp + qtEntradasTemp - qtSaidasTemp) / 1000;
				qtPrevisto				   = Float.parseFloat(Util.formatNumber(qtPrevisto, "#.###").replaceAll(",", "."));
				float qtPrevistoTemp 	   = ((qtEstoqueInicialTemp + qtEntradasTemp - qtSaidasTemp));
				float qtFechamento 	       = Float.parseFloat(Util.formatNumber(rsm.getFloat("qt_fechamento"), "#.###").replaceAll(",", "."));
				float qtFechamentoTemp     = Float.parseFloat(Util.formatNumber((qtFechamento * 1000), "#").replaceAll(",", "."));
				float qtGanho			   = Float.parseFloat(Util.formatNumber((qtPrevistoTemp - qtFechamentoTemp), "#.###").replaceAll(",", ".")) / 1000;
				float qtPerda			   = Float.parseFloat(Util.formatNumber((qtFechamentoTemp - qtPrevistoTemp), "#.###").replaceAll(",", ".")) / 1000;
				
				
				if(qtPrevisto > qtFechamento){
					rsm.setValueToField("qt_perdas", qtGanho);
					rsm.setValueToField("qt_ganhos", 0.0);
				}
				
				else if(qtPrevisto < qtFechamento){
					rsm.setValueToField("qt_perdas", 0.0);
					rsm.setValueToField("qt_ganhos", qtPerda);
				}
				
				else{
					rsm.setValueToField("qt_perdas", 0.0);
					rsm.setValueToField("qt_ganhos", 0.0);
				}
				//Se for o primeiro dia do mes não há perdas nem sobras
//				if(rsm.getGregorianCalendar("dt_fechamento").get(Calendar.DAY_OF_MONTH) == 1){
//					ArrayList<Float> arrayValores = new ArrayList<Float>();
//					arrayValores.add(rsm.getFloat("qt_estoque_inicial"));
//					arrayValores.add(rsm.getFloat("qt_entradas"));
//					arrayValores.add(rsm.getFloat("qt_saidas"));
//					Tanque tanque = TanqueDAO.get(rsm.getInt("cd_tanque"));
//					String codProdTan = tanque.getCdProdutoServico() + "-" + tanque.getCdTanque();
//					hashDiaAntTanque.put(codProdTan, arrayValores);
//					rsm.setValueToField("qt_perdas", "0,00");
//					rsm.setValueToField("qt_ganhos", "0,00");
//					break;
//				}
//				else{
//					Tanque tanque = TanqueDAO.get(rsm.getInt("cd_tanque"));
//					String codProdTan = tanque.getCdProdutoServico() + "-" + tanque.getCdTanque();
//					ArrayList<Float> arrayValores = hashDiaAntTanque.get(codProdTan);
//					//Caso o tanque não tenha sido usado no primeiro dia
//					if(arrayValores == null){
//						arrayValores = new ArrayList<Float>();
//						arrayValores.add(rsm.getFloat("qt_estoque_inicial"));
//						arrayValores.add(rsm.getFloat("qt_entradas"));
//						arrayValores.add(rsm.getFloat("qt_saidas"));
//						hashDiaAntTanque.put(codProdTan, arrayValores);
//						rsm.setValueToField("qt_perdas", "0,00");
//						rsm.setValueToField("qt_ganhos", "0,00");
//					}
//					else{
//						qtEstoqueInicialAnt = arrayValores.get(0);
//						qtEntradasAnt 		= arrayValores.get(1);
//						qtSaidasAnt 		= arrayValores.get(2);
//					}
//				}
				//Se o estoque final do dia anterior for maior que o estoque inicial desse dia, então houve perdas
//				if((qtEstoqueInicialAnt + qtEntradasAnt - qtSaidasAnt) > rsm.getFloat("qt_estoque_inicial")){
//					rsm.setValueToField("qt_perdas", ((qtEstoqueInicialAnt + qtEntradasAnt - qtSaidasAnt) - rsm.getFloat("qt_estoque_inicial")));
//					rsm.setValueToField("qt_ganhos", "0,00");
//				}
//				//Se o estoque final do dia anterior for menor que o estoque inicial desse dia, então houve ganhos
//				else if ((qtEstoqueInicialAnt + qtEntradasAnt - qtSaidasAnt) < rsm.getFloat("qt_estoque_inicial")){
//					rsm.setValueToField("qt_perdas", "0,00");
//					rsm.setValueToField("qt_ganhos", (rsm.getFloat("qt_estoque_inicial") - (qtEstoqueInicialAnt + qtEntradasAnt - qtSaidasAnt)));
//				}
//				//Se o estoque final do dia anterior for igual que o estoque inicial desse dia, então não houve alterações
//				else{
//					rsm.setValueToField("qt_perdas", "0,00");
//					rsm.setValueToField("qt_ganhos", "0,00");
//				}
//				//Armazena o estoque final desse dia para comparar com o próximo dia
//				ArrayList<Float> arrayValores = new ArrayList<Float>();
//				arrayValores.add(rsm.getFloat("qt_estoque_inicial"));
//				arrayValores.add(rsm.getFloat("qt_entradas"));
//				arrayValores.add(rsm.getFloat("qt_saidas"));
//				Tanque tanque = TanqueDAO.get(rsm.getInt("cd_tanque"));
//				String codProdTan = tanque.getCdProdutoServico() + "-" + tanque.getCdTanque();
//				hashDiaAntTanque.put(codProdTan, arrayValores);
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			System.out.println("ERRO getMovimentacaoDiariaCombustivelPorTanque: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getMovimentacaoDiariaCombustivelPorBico(int cdEmpresa, GregorianCalendar data, int cdTanque){
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			PreparedStatement pstmt = connection.prepareStatement("SELECT qt_encerrante_inicial, qt_encerrante_final, vl_afericao, qt_litros, A.cd_bico " + 
																	" 	FROM pcb_bico_encerrante A  " + 
																	"	JOIN adm_conta_fechamento E ON (A.cd_conta = E.cd_conta " + 
																	"												AND A.cd_fechamento = E.cd_fechamento) " +  
																	"	JOIN pcb_tanque D ON (D.cd_tanque = A.cd_tanque)  " + 
																	"	WHERE E.dt_fechamento = ? " + 
																	"			AND D.cd_tanque = ?  " + 
																	"			AND cd_turno = 1 " + 
																	"	ORDER BY A.cd_bico");
			pstmt.setTimestamp(1, new Timestamp(data.getTimeInMillis()));
			pstmt.setInt(2, cdTanque);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				
				
			}
			rsm.beforeFirst();
			return rsm;
		}
		catch(Exception e){
			System.out.println("ERRO getMovimentacaoDiariaCombustivelPorBico: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	
	
	public static boolean getHas1300(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM pcb_bombas ");
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			
			return rsm.next();
		}
		catch(Exception e){
			System.out.println("ERRO getHas1300: " + e);
			return false;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	public static float[] getTributoOfDocumentoEntrada(int cdDocumentoEntrada, int cdEmpresa){
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT SUM(H.vl_base_calculo * pr_aliquota / 100) AS vl_tributo, SUM(H.vl_base_calculo) AS vl_base_calculo FROM adm_entrada_item_aliquota H " +
																	"		  WHERE H.cd_documento_entrada =  " + cdDocumentoEntrada +
																	"			AND cd_empresa = " + cdEmpresa);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			float result[] = new float[2];
			if(rsm.next()){
				result[0] = rsm.getFloat("vl_tributo");
				result[1] = rsm.getFloat("vl_base_calculo");
			}
			return result;
		}
		catch(Exception e){
			System.out.println("ERRO getTributoOfDocumentoEntrada: " + e);
			float erro[] = new float[2];
			erro[0] = 0;
			erro[1] = 0;
			return erro;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	public static ArrayList<Integer> getAllContasAReceber(int cdNotaFiscal){
		ArrayList<Integer> arrayDeCodigos = new ArrayList<Integer>();
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT cd_conta_receber FROM adm_conta_receber WHERE cd_documento_saida = ?");
			
			
			PreparedStatement pstmtDocSaida = connection.prepareStatement("SELECT cd_documento_saida FROM fsc_nota_fiscal_doc_vinculado WHERE cd_nota_fiscal = ?");
			pstmtDocSaida.setInt(1, cdNotaFiscal);
			ResultSetMap rsmDocSaida = new ResultSetMap(pstmtDocSaida.executeQuery());
			while(rsmDocSaida.next()){
				pstmt.setInt(1, rsmDocSaida.getInt("cd_documento_saida"));
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				while(rsm.next()){
					arrayDeCodigos.add(rsm.getInt("cd_conta_receber"));
				}
			}
			return arrayDeCodigos;
		}
		catch(Exception e){
			System.out.println("ERRO getContasAReceber: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	public static ArrayList<Integer> getAllContasAPagar(int cdNotaFiscal){
		ArrayList<Integer> arrayDeCodigos = new ArrayList<Integer>();
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT cd_conta_pagar FROM adm_conta_pagar WHERE cd_documento_entrada = ?");
			
			PreparedStatement pstmtDocEntrada = connection.prepareStatement("SELECT cd_documento_entrada FROM fsc_nota_fiscal_doc_vinculado WHERE cd_nota_fiscal = ?");
			pstmtDocEntrada.setInt(1, cdNotaFiscal);
			ResultSetMap rsmDocEntrada = new ResultSetMap(pstmtDocEntrada.executeQuery());
			while(rsmDocEntrada.next()){
				pstmt.setInt(1, rsmDocEntrada.getInt("cd_documento_entrada"));
				ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
				while(rsm.next()){
					arrayDeCodigos.add(rsm.getInt("cd_conta_pagar"));
				}
			}
			return arrayDeCodigos;
		}
		catch(Exception e){
			System.out.println("ERRO getContasAPagar: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getRegistrosEcf(GregorianCalendar dtInicial, GregorianCalendar dtFinal, int cdEmpresa){
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			dtInicial.set(Calendar.HOUR_OF_DAY, 0);
			dtInicial.set(Calendar.MINUTE, 0);
			dtInicial.set(Calendar.SECOND, 0);
			
			dtFinal.set(Calendar.HOUR_OF_DAY, 23);
			dtFinal.set(Calendar.MINUTE, 59);
			dtFinal.set(Calendar.SECOND, 59);
			
			
			ResultSetMap rsmFinal = new ResultSetMap();
			
			PreparedStatement pstmtImpressora = connection.prepareStatement("SELECT * FROM bpm_referencia");
			ResultSetMap rsmImpressora = new ResultSetMap(pstmtImpressora.executeQuery());
			
			PreparedStatement pstmtDocSaida = connection.prepareStatement("SELECT * FROM alm_documento_saida " +
																  "  WHERE tp_documento_saida = " + DocumentoSaidaServices.TP_CUPOM_FISCAL +
																  "    AND st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO + 
																  "    AND CAST(dt_documento_saida AS DATE) BETWEEN ? AND ? " +
																  "    AND cd_referencia_ecf = ? " +
																  "    ORDER BY dt_documento_saidaw");
			
			pstmtDocSaida.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmtDocSaida.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			
			while(rsmImpressora.next()){
				HashMap<String, Object> register = new HashMap<String, Object>();
				
				register.put("txt_registro_ecf", "|C400|2D|"+rsmImpressora.getString("nm_modelo")+"|"+rsmImpressora.getString("nr_serie")+"|001|");
				rsmFinal.addRegister(register);
				register = new HashMap<String, Object>();
				
				pstmtDocSaida.setInt(3, rsmImpressora.getInt("cd_referencia"));
				ResultSetMap rsmDocSaida = new ResultSetMap(pstmtDocSaida.executeQuery());
				GregorianCalendar dtAntiga = null;
				while(rsmDocSaida.next()){
					if(rsmDocSaida.getGregorianCalendar("dt_documento_saida") != dtAntiga){
						register.put("txt_registro_ecf", "|C405|"+Util.formatDate(rsmDocSaida.getGregorianCalendar("dt_documento_saida"), "ddMMyyyy")+"|2|000459|035161|3346446,93|7739,03|");
						rsmFinal.addRegister(register);
					}
				}
				
				
			}
			return rsmFinal;
		}
		catch(Exception e){
			System.out.println("ERRO getRegistrosEcf: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	//Corrigir para buscar apenas empresas que tem movimento no periodo
	public static ArrayList<Integer> getEmpresasDoPeriodo(GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			ResultSetMap rsm = EmpresaServices.getAll();
			ArrayList<Integer> codigos = new ArrayList<Integer>();
			while(rsm.next()){
				codigos.add(rsm.getInt("cd_empresa"));
			}
			
			return codigos;
		}
		catch(Exception e){
			System.out.println("ERRO getEmpresasDoPeriodo: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	
	public static ResultSetMap getDocumentoEntradaSaidaContribuicoesDoPeriodo(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal, int tipo, int cdTributo){
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			String sql = "";
			
			if(tipo == 0){
				sql = "SELECT A.cd_tipo_credito, D.nr_tipo_credito, C.pr_aliquota, SUM(C.vl_base_calculo) AS vl_base_calculo FROM alm_documento_entrada_item A"
						+ " JOIN alm_documento_entrada B ON (A.cd_documento_entrada = B.cd_documento_entrada)"
						+ " JOIN adm_entrada_item_aliquota C ON (A.cd_documento_entrada = C.cd_documento_entrada "
						+ "										AND A.cd_produto_servico = C.cd_produto_servico"
						+ "										AND C.cd_tributo = "+cdTributo+") "
						+ " JOIN fsc_tipo_credito D ON (A.cd_tipo_credito = D.cd_tipo_credito) "
						+ "	WHERE B.dt_documento_entrada >= ? "
						+ "   AND B.dt_documento_entrada <= ? "
						+ "   AND B.cd_empresa = ? "
						+ "   AND B.tp_documento_entrada <> "+DocumentoEntradaServices.TP_DOC_NAO_FISCAL
						+ "   AND B.tp_entrada <> " + DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO
						+ "   AND B.st_documento_entrada = " +DocumentoEntradaServices.ST_LIBERADO
						+ " GROUP BY A.cd_tipo_credito, D.nr_tipo_credito, C.pr_aliquota";
			}
			else{
				sql = "SELECT A.cd_tipo_contribuicao_social, D.nr_tipo_contribuicao_social, C.pr_aliquota, SUM(C.vl_base_calculo) AS vl_base_calculo, "
						+ "SUM(A.vl_unitario * A.qt_saida + A.vl_acrescimo - A.vl_desconto) AS vl_total_item FROM alm_documento_saida_item A"
						+ " JOIN alm_documento_saida B ON (A.cd_documento_saida = B.cd_documento_saida)"
						+ " JOIN adm_saida_item_aliquota C ON (A.cd_documento_saida = C.cd_documento_saida "
						+ "										AND A.cd_produto_servico = C.cd_produto_servico) "
						+ " LEFT OUTER JOIN fsc_tipo_contribuicao_social D ON (A.cd_tipo_contribuicao_social = D.cd_tipo_contribuicao_social) "
						+ "	WHERE B.dt_documento_saida >= ? "
						+ "   AND B.dt_documento_saida <= ? "
						+ "   AND B.cd_empresa = ? "
						+ "   AND B.tp_documento_saida <> "+DocumentoSaidaServices.TP_DOC_NAO_FISCAL
						+ "   AND B.tp_documento_saida <> "+DocumentoSaidaServices.TP_NOTA_FISCAL_02
						+ "   AND B.st_documento_saida = " +DocumentoSaidaServices.ST_CONCLUIDO
						+ "   AND EXISTS (SELECT * FROM adm_saida_item_aliquota D " +
						"				  WHERE D.cd_documento_saida = B.cd_documento_saida" +
						"					AND D.cd_tributo = "+cdTributo+")"
						+ " GROUP BY A.cd_tipo_contribuicao_social, D.nr_tipo_contribuicao_social, C.pr_aliquota";
			}
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(3, cdEmpresa);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e){
			System.out.println("ERRO getDocumentoEntradaContribuicoesDoPeriodo: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getProdutosContribuicoes(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			int cdTributoPIS 	= ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_PIS", 0);
			int cdTributoCOFINS = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_COFINS", 0);
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM grl_produto_servico A " +
								"WHERE EXISTS(SELECT C.cd_produto_servico FROM alm_documento_saida B, alm_documento_saida_item C " +
								"             WHERE   dt_documento_saida >= ?  " +
								"           AND B.cd_documento_saida = C.cd_documento_saida " +
								"			AND dt_documento_saida <= ?  " +
								"  	 	 	AND B.tp_documento_saida <> "+DocumentoSaidaServices.TP_DOC_NAO_FISCAL +
								"   	 	AND B.tp_documento_saida <> "+DocumentoSaidaServices.TP_NOTA_FISCAL_02 +
								"   	 	AND B.tp_documento_saida <> " + DocumentoSaidaServices.TP_CUPOM_FISCAL +
								"  		 	AND B.st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO + 
								"		 	AND EXISTS (SELECT * FROM adm_saida_item_aliquota E " +
								"						WHERE B.cd_documento_saida = E.cd_documento_saida" +
								"						  AND (E.cd_tributo = ? OR E.cd_tributo = ?))" +
								" 		    AND NOT EXISTS (SELECT * FROM fsc_nota_fiscal_doc_vinculado F" +
								"						 	WHERE F.cd_documento_saida = B.cd_documento_saida) " +
								"			AND B.cd_empresa=? " +
								"			AND A.cd_produto_servico = C.cd_produto_servico " +
								"			GROUP BY cd_produto_servico ORDER BY cd_produto_servico " +
								") " +
								"OR EXISTS (SELECT C.cd_produto_servico FROM alm_documento_saida B, alm_documento_saida_item C " +
								"             WHERE   dt_documento_saida >= ?  " +
								"           AND B.cd_documento_saida = C.cd_documento_saida " +
								"			AND dt_documento_saida <= ?  " +
								"  	 	 	AND B.tp_documento_saida <> "+DocumentoSaidaServices.TP_DOC_NAO_FISCAL +
								"   	 	AND B.tp_documento_saida <> "+DocumentoSaidaServices.TP_NOTA_FISCAL_02 +
								"   	 	AND B.tp_documento_saida <> " + DocumentoSaidaServices.TP_CUPOM_FISCAL +
								"  		 	AND B.st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO + 
								"		 	AND EXISTS (SELECT * FROM adm_saida_item_aliquota E " +
								"						WHERE B.cd_documento_saida = E.cd_documento_saida" +
								"						  AND (E.cd_tributo = ? OR E.cd_tributo = ?))" +
								" 		    AND NOT EXISTS (SELECT * FROM fsc_nota_fiscal_doc_vinculado F" +
								"						 	WHERE F.cd_documento_saida = B.cd_documento_saida) " +
								"			AND B.cd_empresa=? " +
								"			AND A.cd_produto_servico = C.cd_produto_servico " +
								"			GROUP BY cd_produto_servico ORDER BY cd_produto_servico " +
								") " +
								"OR EXISTS (SELECT C.cd_produto_servico FROM alm_documento_entrada B, alm_documento_entrada_item C " +
								"             WHERE   dt_documento_entrada >= ?  " +
								"           AND B.cd_documento_entrada = C.cd_documento_entrada " +
								"			AND dt_documento_entrada <= ?  " + 
								"   	 	AND B.tp_documento_entrada <> "+DocumentoEntradaServices.TP_DOC_NAO_FISCAL + 
								"   	 	AND B.tp_documento_entrada <> " + DocumentoEntradaServices.TP_CUPOM_FISCAL +
								"   	 	AND B.tp_entrada <> " + DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO +
								"  		 	AND B.st_documento_entrada = " + DocumentoEntradaServices.ST_LIBERADO + 
								"		 	AND EXISTS (SELECT * FROM adm_entrada_item_aliquota E " +
								"						WHERE B.cd_documento_entrada = E.cd_documento_entrada" +
								"						  AND (E.cd_tributo = ? OR E.cd_tributo = ?))" +
								" 		 	AND NOT EXISTS (SELECT * FROM fsc_nota_fiscal_doc_vinculado F" +
								"						 	WHERE F.cd_documento_entrada = B.cd_documento_entrada) " +
								"			AND st_documento_entrada =  " + DocumentoEntradaServices.ST_LIBERADO +
								"			AND B.cd_empresa=? " +
								"			AND A.cd_produto_servico = C.cd_produto_servico " +
								"			GROUP BY cd_produto_servico ORDER BY cd_produto_servico " +
								") " +
								"OR EXISTS (SELECT CE.cd_produto_servico FROM fsc_nota_fiscal_item A" +
								"		 		JOIN fsc_nota_fiscal B ON (A.cd_nota_fiscal = B.cd_nota_fiscal) " +
								"		 		JOIN grl_produto_servico C ON (A.cd_produto_servico = C.cd_produto_servico) " +
								"		 		JOIN grl_produto_servico_empresa CE ON (A.cd_produto_servico = C.cd_produto_servico" +
								"													AND CE.cd_empresa = ?) " +
								"	   		WHERE B.dt_autorizacao >= ? " +
								"		 		AND B.dt_autorizacao <= ? " +
								"		 		AND B.cd_empresa = ? " +
								"  		 		AND B.st_nota_fiscal = " + NotaFiscalServices.AUTORIZADA + 
								"		 		AND EXISTS (SELECT * FROM fsc_nota_fiscal_item_tributo E " +
								"						WHERE A.cd_nota_fiscal = E.cd_nota_fiscal" +
								"						  AND A.cd_item = E.cd_item" +
								"						  AND (E.cd_tributo = ? OR E.cd_tributo = ?))" +
								"			GROUP BY CE.cd_produto_servico ORDER BY cd_produto_servico " +
								") ");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(3, cdTributoPIS);
			pstmt.setInt(4, cdTributoCOFINS);
			pstmt.setInt(5, cdEmpresa);
			pstmt.setTimestamp(6, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(7, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(8, cdTributoPIS);
			pstmt.setInt(9, cdTributoCOFINS);
			pstmt.setInt(10, cdEmpresa);
			pstmt.setTimestamp(11, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(12, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(13, cdTributoPIS);
			pstmt.setInt(14, cdTributoCOFINS);
			pstmt.setInt(15, cdEmpresa);
			pstmt.setInt(16, cdEmpresa);
			pstmt.setTimestamp(17, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(18, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(19, cdEmpresa);
			pstmt.setInt(20, cdTributoPIS);
			pstmt.setInt(21, cdTributoCOFINS);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e){
			System.out.println("ERRO getItensNotaFiscalContribuicoes: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getUnidadesContribuicoes(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			int cdTributoPIS 	= ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_PIS", 0);
			int cdTributoCOFINS = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_COFINS", 0);
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM grl_unidade_medida A " +
																	"WHERE EXISTS(SELECT C.cd_unidade_medida FROM alm_documento_saida B, alm_documento_saida_item C " +
																	"             WHERE   dt_documento_saida >= ?  " +
																	"           AND B.cd_documento_saida = C.cd_documento_saida " +
																	"			AND dt_documento_saida <= ?  " +
																	"  	 	 	AND B.tp_documento_saida <> "+DocumentoSaidaServices.TP_DOC_NAO_FISCAL +
																	"   	 	AND B.tp_documento_saida <> "+DocumentoSaidaServices.TP_NOTA_FISCAL_02 +
																	"   	 	AND B.tp_documento_saida <> " + DocumentoSaidaServices.TP_CUPOM_FISCAL +
																	"  		 	AND B.st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO + 
																	"		 	AND EXISTS (SELECT * FROM adm_saida_item_aliquota E " +
																	"						WHERE B.cd_documento_saida = E.cd_documento_saida" +
																	"						  AND (E.cd_tributo = ? OR E.cd_tributo = ?))" +
																	" 		    AND NOT EXISTS (SELECT * FROM fsc_nota_fiscal_doc_vinculado F" +
																	"						 	WHERE F.cd_documento_saida = B.cd_documento_saida) " +
																	"			AND B.cd_empresa=? " +
																	"			AND A.cd_unidade_medida = C.cd_unidade_medida " +
																	"			GROUP BY cd_unidade_medida ORDER BY cd_unidade_medida " +
																	") " +
																	"OR EXISTS (SELECT C.cd_unidade_medida FROM alm_documento_saida B, alm_documento_saida_item C " +
																	"             WHERE   dt_documento_saida >= ?  " +
																	"           AND B.cd_documento_saida = C.cd_documento_saida " +
																	"			AND dt_documento_saida <= ?  " +
																	"  	 	 	AND B.tp_documento_saida <> "+DocumentoSaidaServices.TP_DOC_NAO_FISCAL +
																	"   	 	AND B.tp_documento_saida <> "+DocumentoSaidaServices.TP_NOTA_FISCAL_02 +
																	"   	 	AND B.tp_documento_saida <> " + DocumentoSaidaServices.TP_CUPOM_FISCAL +
																	"  		 	AND B.st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO + 
																	"		 	AND EXISTS (SELECT * FROM adm_saida_item_aliquota E " +
																	"						WHERE B.cd_documento_saida = E.cd_documento_saida" +
																	"						  AND (E.cd_tributo = ? OR E.cd_tributo = ?))" +
																	" 		    AND NOT EXISTS (SELECT * FROM fsc_nota_fiscal_doc_vinculado F" +
																	"						 	WHERE F.cd_documento_saida = B.cd_documento_saida) " +
																	"			AND B.cd_empresa=? " +
																	"			AND A.cd_unidade_medida = C.cd_unidade_medida " +
																	"			GROUP BY cd_unidade_medida ORDER BY cd_unidade_medida " +
																	") " +
																	"OR EXISTS (SELECT C.cd_unidade_medida FROM alm_documento_entrada B, alm_documento_entrada_item C " +
																	"             WHERE   dt_documento_entrada >= ?  " +
																	"           AND B.cd_documento_entrada = C.cd_documento_entrada " +
																	"			AND dt_documento_entrada <= ?  " + 
																	"   	 	AND B.tp_documento_entrada <> "+DocumentoEntradaServices.TP_DOC_NAO_FISCAL + 
																	"   	 	AND B.tp_documento_entrada <> " + DocumentoEntradaServices.TP_CUPOM_FISCAL +
																	"   	 	AND B.tp_entrada <> " + DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO +
																	"  		 	AND B.st_documento_entrada = " + DocumentoEntradaServices.ST_LIBERADO + 
																	"		 	AND EXISTS (SELECT * FROM adm_entrada_item_aliquota E " +
																	"						WHERE B.cd_documento_entrada = E.cd_documento_entrada" +
																	"						  AND (E.cd_tributo = ? OR E.cd_tributo = ?))" +
																	" 		 	AND NOT EXISTS (SELECT * FROM fsc_nota_fiscal_doc_vinculado F" +
																	"						 	WHERE F.cd_documento_entrada = B.cd_documento_entrada) " +
																	"			AND st_documento_entrada =  " + DocumentoEntradaServices.ST_LIBERADO +
																	"			AND B.cd_empresa=? " +
																	"			AND A.cd_unidade_medida = C.cd_unidade_medida " +
																	"			GROUP BY cd_unidade_medida ORDER BY cd_unidade_medida " +
																	") " +
																	"OR EXISTS (SELECT CE.cd_unidade_medida FROM fsc_nota_fiscal_item A" +
																	"		 		JOIN fsc_nota_fiscal B ON (A.cd_nota_fiscal = B.cd_nota_fiscal) " +
																	"		 		JOIN grl_produto_servico C ON (A.cd_produto_servico = C.cd_produto_servico) " +
																	"		 		JOIN grl_produto_servico_empresa CE ON (A.cd_produto_servico = C.cd_produto_servico" +
																	"													AND CE.cd_empresa = ?) " +
																	"	   		WHERE B.dt_autorizacao >= ? " +
																	"		 		AND B.dt_autorizacao <= ? " +
																	"		 		AND B.cd_empresa = ? " +
																	"  		 		AND B.st_nota_fiscal = " + NotaFiscalServices.AUTORIZADA + 
																	"		 		AND EXISTS (SELECT * FROM fsc_nota_fiscal_item_tributo E " +
																	"						WHERE A.cd_nota_fiscal = E.cd_nota_fiscal" +
																	"						  AND A.cd_item = E.cd_item" +
																	"						  AND (E.cd_tributo = ? OR E.cd_tributo = ?))" +
																	"			GROUP BY CE.cd_unidade_medida ORDER BY cd_unidade_medida " +
																	") ");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(3, cdTributoPIS);
			pstmt.setInt(4, cdTributoCOFINS);
			pstmt.setInt(5, cdEmpresa);
			pstmt.setTimestamp(6, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(7, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(8, cdTributoPIS);
			pstmt.setInt(9, cdTributoCOFINS);
			pstmt.setInt(10, cdEmpresa);
			pstmt.setTimestamp(11, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(12, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(13, cdTributoPIS);
			pstmt.setInt(14, cdTributoCOFINS);
			pstmt.setInt(15, cdEmpresa);
			pstmt.setInt(16, cdEmpresa);
			pstmt.setTimestamp(17, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(18, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(19, cdEmpresa);
			pstmt.setInt(20, cdTributoPIS);
			pstmt.setInt(21, cdTributoCOFINS);
			
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e){
			System.out.println("ERRO getItensNotaFiscalContribuicoes: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	
	public static Result getDocumentoNotaFiscalContribuicoes(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			int cdTributoPIS 	= ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_PIS", 0);
			int cdTributoCOFINS = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_COFINS", 0);
			
			HashMap<String, ArrayList<Integer>> resultado = new HashMap<String, ArrayList<Integer>>();
			
			//NFE
			PreparedStatement pstmtItensNfe = connection.prepareStatement("SELECT A.cd_nota_fiscal FROM fsc_nota_fiscal_item A" +
															"		 JOIN fsc_nota_fiscal B ON (A.cd_nota_fiscal = B.cd_nota_fiscal) " +
															"	   WHERE B.dt_autorizacao >= ? " +
															"		 AND B.dt_autorizacao <= ? " +
															"		 AND B.cd_empresa = ? " +
															"  		 AND B.st_nota_fiscal = " + NotaFiscalServices.AUTORIZADA + 
															"		 AND EXISTS (SELECT * FROM fsc_nota_fiscal_item_tributo E " +
															"						WHERE A.cd_nota_fiscal = E.cd_nota_fiscal" +
															"						  AND A.cd_item = E.cd_item" +
															"						  AND (E.cd_tributo = ? OR E.cd_tributo = ?))" +
															"	   GROUP BY 1");
			pstmtItensNfe.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmtItensNfe.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			pstmtItensNfe.setInt(3, cdEmpresa);
			pstmtItensNfe.setInt(4, cdTributoPIS);
			pstmtItensNfe.setInt(5, cdTributoCOFINS);
			
			ResultSetMap rsmItensNfe = new ResultSetMap(pstmtItensNfe.executeQuery());
			
			ArrayList<Integer> conjuntoNfe = new ArrayList<Integer>();
			
			while(rsmItensNfe.next()){
				conjuntoNfe.add(rsmItensNfe.getInt("cd_nota_fiscal"));
			}
			
			resultado.put("rsmNfe", conjuntoNfe);
			
			
			
			//Documento de Saida
			PreparedStatement pstmtItensSaida = connection.prepareStatement("SELECT A.cd_documento_saida FROM alm_documento_saida_item A" +
															"		 JOIN alm_documento_saida B ON (A.cd_documento_saida = B.cd_documento_saida) " +
															"	   WHERE B.dt_documento_saida >= ? " +
															"		 AND B.dt_documento_saida <= ? " +
															"		 AND B.cd_empresa = ? " +
															"  	 	 AND B.tp_documento_saida <> "+DocumentoSaidaServices.TP_DOC_NAO_FISCAL +
															"   	 AND B.tp_documento_saida <> "+DocumentoSaidaServices.TP_NOTA_FISCAL_02 +
															"  		 AND B.st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO + 
															"		 AND EXISTS (SELECT * FROM adm_saida_item_aliquota E " +
															"						WHERE A.cd_documento_saida = E.cd_documento_saida" +
															"						  AND A.cd_produto_servico  = E.cd_produto_servico " +
															"						  AND A.cd_item  = E.cd_item " +
															"						  AND (E.cd_tributo = ? OR E.cd_tributo = ?))" +
															" 		 AND NOT EXISTS (SELECT * FROM fsc_nota_fiscal_doc_vinculado F" +
															"						 	WHERE F.cd_documento_saida = B.cd_documento_saida) " +
															"	   GROUP BY 1");
			pstmtItensSaida.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmtItensSaida.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			pstmtItensSaida.setInt(3, cdEmpresa);
			pstmtItensSaida.setInt(4, cdTributoPIS);
			pstmtItensSaida.setInt(5, cdTributoCOFINS);
			System.out.println("pstmtItensSaida = " + pstmtItensSaida);
			ResultSetMap rsmItensSaida = new ResultSetMap(pstmtItensSaida.executeQuery());
			System.out.println("rsmItensSaida = " + rsmItensSaida);
			ArrayList<Integer> conjuntoSaida = new ArrayList<Integer>();
			while(rsmItensSaida.next()){
				conjuntoSaida.add(rsmItensSaida.getInt("cd_documento_saida"));
			}
			
			resultado.put("rsmSaida", conjuntoSaida);
			
			
			//Documento de Entrada
			PreparedStatement pstmtItensEntrada = connection.prepareStatement("SELECT A.cd_documento_entrada FROM alm_documento_entrada_item A" +
															"		 JOIN alm_documento_entrada B ON (A.cd_documento_entrada = B.cd_documento_entrada) " +
															"	   WHERE B.dt_documento_entrada >= ? " +
															"		 AND B.dt_documento_entrada <= ? " +
															"		 AND B.cd_empresa = ? " +
															"   	 AND B.tp_documento_entrada <> "+DocumentoEntradaServices.TP_DOC_NAO_FISCAL + 
															"   	 AND B.tp_entrada <> " + DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO +
															"  		 AND B.st_documento_entrada = " + DocumentoEntradaServices.ST_LIBERADO + 
															"		 AND EXISTS (SELECT * FROM adm_entrada_item_aliquota E " +
															"						WHERE A.cd_documento_entrada = E.cd_documento_entrada" +
															"						  AND A.cd_produto_servico  = E.cd_produto_servico " +
															"						  AND A.cd_item  = E.cd_item " +
															"						  AND (E.cd_tributo = ? OR E.cd_tributo = ?))" +
															" 		 AND NOT EXISTS (SELECT * FROM fsc_nota_fiscal_doc_vinculado F" +
															"						 	WHERE F.cd_documento_entrada = B.cd_documento_entrada) " +
															"	   GROUP BY 1");
			pstmtItensEntrada.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmtItensEntrada.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			pstmtItensEntrada.setInt(3, cdEmpresa);
			pstmtItensEntrada.setInt(4, cdTributoPIS);
			pstmtItensEntrada.setInt(5, cdTributoCOFINS);
			System.out.println("pstmtItensEntrada = " + pstmtItensEntrada);
			ResultSetMap rsmItensEntrada = new ResultSetMap(pstmtItensEntrada.executeQuery());
			System.out.println("rsmItensEntrada = " + rsmItensEntrada);
			ArrayList<Integer> conjuntoEntrada = new ArrayList<Integer>();
			while(rsmItensEntrada.next()){
				conjuntoEntrada.add(rsmItensEntrada.getInt("cd_documento_entrada"));
			}
			
			resultado.put("rsmEntrada", conjuntoEntrada);
			
			
			
			
			
			Result result = new Result(1);
			result.addObject("resultado", resultado);
			return result;
		}
		catch(Exception e){
			System.out.println("ERRO getItensNotaFiscalContribuicoes: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getItensNotaFiscalContribuicoes(int cdEmpresa, int cdNotaFiscal, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			int cdTributoPIS 	= ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_PIS", 0);
			int cdTributoCOFINS = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_COFINS", 0);
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT A.cd_produto_servico, D.nr_ncm, SUM(A.vl_unitario * A.qt_tributario + A.vl_acrescimo - A.vl_desconto) AS vl_total FROM fsc_nota_fiscal_item A" +
															"		 JOIN fsc_nota_fiscal B ON (A.cd_nota_fiscal = B.cd_nota_fiscal) " +
															"		 JOIN grl_produto_servico C ON (A.cd_produto_servico = C.cd_produto_servico) " +
															"		 JOIN grl_ncm D ON (C.cd_ncm = D.cd_ncm) " +
															"	   WHERE B.dt_autorizacao >= ? " +
															"		 AND B.dt_autorizacao <= ? " +
															"		 AND B.cd_empresa = ? " +
															"  		 AND B.st_nota_fiscal = " + NotaFiscalServices.AUTORIZADA +
															"        AND B.cd_nota_fiscal = ?" + 
															"		 AND EXISTS (SELECT * FROM fsc_nota_fiscal_item_tributo E " +
															"						WHERE A.cd_nota_fiscal = E.cd_nota_fiscal" +
															"						  AND A.cd_item = E.cd_item" +
															"						  AND (E.cd_tributo = ? OR E.cd_tributo = ?))" +
															"	   GROUP BY 1, 2");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(3, cdEmpresa);
			pstmt.setInt(4, cdNotaFiscal);
			pstmt.setInt(5, cdTributoPIS);
			pstmt.setInt(6, cdTributoCOFINS);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e){
			System.out.println("ERRO getItensNotaFiscalContribuicoes: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getItensNotaFiscalPISContribuicoes(int cdEmpresa, int cdProdutoServico, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			int cdTributoPIS 	= ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_PIS", 0);
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT H.nr_situacao_tributaria, F.nr_codigo_fiscal, E.pr_aliquota, SUM(A.vl_unitario * A.qt_tributario + A.vl_acrescimo - A.vl_desconto) AS vl_total FROM fsc_nota_fiscal_item A" +
															"		 JOIN fsc_nota_fiscal B ON (A.cd_nota_fiscal = B.cd_nota_fiscal) " +
															"		 JOIN grl_produto_servico C ON (A.cd_produto_servico = C.cd_produto_servico) " +
															"		 JOIN grl_ncm D ON (C.cd_ncm = D.cd_ncm) " +
															"        JOIN fsc_nota_fiscal_item_tributo E ON ( A.cd_nota_fiscal = E.cd_nota_fiscal" +
															"						  							AND A.cd_item = E.cd_item" +
															"						  							AND E.cd_tributo = ?) " +
															"		 JOIN adm_natureza_operacao F ON (A.cd_natureza_operacao = F.cd_natureza_operacao) " +	
															"        JOIN adm_tributo_aliquota G ON (E.cd_tributo = G.cd_tributo " +
															"											AND E.cd_tributo_aliquota = G.cd_tributo_aliquota) " +
															"        JOIN fsc_situacao_tributaria H ON (G.cd_situacao_tributaria = H.cd_situacao_tributaria) " +
															"	   WHERE B.dt_autorizacao >= ? " +
															"		 AND B.dt_autorizacao <= ? " +
															"		 AND B.cd_empresa = ? " +
															"  		 AND B.st_nota_fiscal = " + NotaFiscalServices.AUTORIZADA + 
															"		 AND A.cd_produto_servico = ?" +
															"	   GROUP BY 1, 2, 3");
			pstmt.setInt(1, cdTributoPIS);
			pstmt.setTimestamp(2, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(3, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(4, cdEmpresa);
			pstmt.setInt(5, cdProdutoServico);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e){
			System.out.println("ERRO getItensNotaFiscalPISContribuicoes: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	public static ResultSetMap getItensNotaFiscalCOFINSContribuicoes(int cdEmpresa, int cdProdutoServico, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			int cdTributoCOFINS 	= ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_COFINS", 0);
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT H.nr_situacao_tributaria, F.nr_codigo_fiscal, E.pr_aliquota, SUM(A.vl_unitario * A.qt_tributario + A.vl_acrescimo - A.vl_desconto) AS vl_total FROM fsc_nota_fiscal_item A" +
															"		 JOIN fsc_nota_fiscal B ON (A.cd_nota_fiscal = B.cd_nota_fiscal) " +
															"		 JOIN grl_produto_servico C ON (A.cd_produto_servico = C.cd_produto_servico) " +
															"		 JOIN grl_ncm D ON (C.cd_ncm = D.cd_ncm) " +
															"        JOIN fsc_nota_fiscal_item_tributo E ON ( A.cd_nota_fiscal = E.cd_nota_fiscal" +
															"						  							AND A.cd_item = E.cd_item" +
															"						  							AND E.cd_tributo = ?) " +
															"		 JOIN adm_natureza_operacao F ON (A.cd_natureza_operacao = F.cd_natureza_operacao) " +	
															"        JOIN adm_tributo_aliquota G ON (E.cd_tributo = G.cd_tributo " +
															"											AND E.cd_tributo_aliquota = G.cd_tributo_aliquota) " +
															"        JOIN fsc_situacao_tributaria H ON (G.cd_situacao_tributaria = H.cd_situacao_tributaria) " +
															"	   WHERE B.dt_autorizacao >= ? " +
															"		 AND B.dt_autorizacao <= ? " +
															"		 AND B.cd_empresa = ? " +
															"  		 AND B.st_nota_fiscal = " + NotaFiscalServices.AUTORIZADA + 
															"		 AND A.cd_produto_servico = ?" +
															"	   GROUP BY 1, 2, 3");
			pstmt.setInt(1, cdTributoCOFINS);
			pstmt.setTimestamp(2, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(3, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(4, cdEmpresa);
			pstmt.setInt(5, cdProdutoServico);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			return rsm;
		}
		catch(Exception e){
			System.out.println("ERRO getItensNotaFiscalCOFINSContribuicoes: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	public static ArrayList<Integer> getParticipantesDoPeriodoContribuicoes(int cdEmpresa, GregorianCalendar dtInicial, GregorianCalendar dtFinal){
		ArrayList<Integer> arrayDeCodigos = new ArrayList<Integer>();
		Connection connection = Conexao.conectar();
		
		boolean isConnectionNull = connection==null;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());
			
			int cdTributoPIS 	= ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_PIS", 0);
			int cdTributoCOFINS = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_COFINS", 0);
			
			
			PreparedStatement pstmt = connection.prepareStatement("SELECT cd_pessoa FROM grl_pessoa A " +
																		"WHERE EXISTS(SELECT cd_transportadora FROM alm_documento_saida B " +
																		"             WHERE   dt_documento_saida >= ?  " +
																		"			AND dt_documento_saida <= ?  " +
																		"  	 	 	AND B.tp_documento_saida <> "+DocumentoSaidaServices.TP_DOC_NAO_FISCAL +
																		"   	 	AND B.tp_documento_saida <> "+DocumentoSaidaServices.TP_NOTA_FISCAL_02 +
																		"  		 	AND B.st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO + 
																		"		 	AND EXISTS (SELECT * FROM adm_saida_item_aliquota E " +
																		"						WHERE B.cd_documento_saida = E.cd_documento_saida" +
																		"						  AND (E.cd_tributo = ? OR E.cd_tributo = ?))" +
																		" 		    AND NOT EXISTS (SELECT * FROM fsc_nota_fiscal_doc_vinculado F" +
																		"						 	WHERE F.cd_documento_saida = B.cd_documento_saida) " +
																		"			AND B.cd_empresa=? " +
																		"			AND A.cd_pessoa = B.cd_transportadora " +
																		"			group by cd_transportadora order by cd_transportadora " +
																		") " +
																		"OR EXISTS (SELECT cd_cliente FROM alm_documento_saida B " +
																		"             WHERE   dt_documento_saida >= ?  " +
																		"			AND dt_documento_saida <= ?  " +
																		"  	 	 	AND B.tp_documento_saida <> "+DocumentoSaidaServices.TP_DOC_NAO_FISCAL +
																		"   	 	AND B.tp_documento_saida <> "+DocumentoSaidaServices.TP_NOTA_FISCAL_02 +
																		"  		 	AND B.st_documento_saida = " + DocumentoSaidaServices.ST_CONCLUIDO + 
																		"		 	AND EXISTS (SELECT * FROM adm_saida_item_aliquota E " +
																		"						WHERE B.cd_documento_saida = E.cd_documento_saida" +
																		"						  AND (E.cd_tributo = ? OR E.cd_tributo = ?))" +
																		" 		    AND NOT EXISTS (SELECT * FROM fsc_nota_fiscal_doc_vinculado F" +
																		"						 	WHERE F.cd_documento_saida = B.cd_documento_saida) " +
																		"			AND B.cd_empresa=? " +
																		"			AND A.cd_pessoa = B.cd_cliente " +
																		"			group by cd_cliente order by cd_cliente " +
																		") " +
																		"OR EXISTS (SELECT cd_fornecedor FROM alm_documento_entrada B " +
																		"             WHERE   dt_documento_entrada >= ?  " +
																		"			AND dt_documento_entrada <= ?  " + 
																		"   	 	AND B.tp_documento_entrada <> "+DocumentoEntradaServices.TP_DOC_NAO_FISCAL + 
																		"   	 	AND B.tp_entrada <> " + DocumentoEntradaServices.ENT_ACERTO_CONSIGNACAO +
																		"  		 	AND B.st_documento_entrada = " + DocumentoEntradaServices.ST_LIBERADO + 
																		"		 	AND EXISTS (SELECT * FROM adm_entrada_item_aliquota E " +
																		"						WHERE B.cd_documento_entrada = E.cd_documento_entrada" +
																		"						  AND (E.cd_tributo = ? OR E.cd_tributo = ?))" +
																		" 		 	AND NOT EXISTS (SELECT * FROM fsc_nota_fiscal_doc_vinculado F" +
																		"						 	WHERE F.cd_documento_entrada = B.cd_documento_entrada) " +
																		"			AND st_documento_entrada =  " + DocumentoEntradaServices.ST_LIBERADO +
																		"			AND B.cd_empresa=? " +
																		"			AND A.cd_pessoa = B.cd_fornecedor " +
																		"			group by cd_fornecedor order by cd_fornecedor " +
																		") " +
																		"OR EXISTS (SELECT cd_destinatario FROM fsc_nota_fiscal F " +
																		"             WHERE   dt_autorizacao >= ?  " +
																		"			AND dt_autorizacao <= ?  " +
																		"		 	AND EXISTS (SELECT * FROM fsc_nota_fiscal_item_tributo E " +
																		"						WHERE F.cd_nota_fiscal = E.cd_nota_fiscal" +
																		"						  AND (E.cd_tributo = ? OR E.cd_tributo = ?))" +
																		"			AND st_nota_fiscal = " + NotaFiscalServices.AUTORIZADA + 
																		"			AND F.cd_empresa=? " +
																		"			AND A.cd_pessoa = F.cd_destinatario " +
																		"			group by cd_destinatario order by cd_destinatario " +
																		") ");
			pstmt.setTimestamp(1, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(2, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(3, cdTributoPIS);
			pstmt.setInt(4, cdTributoCOFINS);
			pstmt.setInt(5, cdEmpresa);
			pstmt.setTimestamp(6, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(7, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(8, cdTributoPIS);
			pstmt.setInt(9, cdTributoCOFINS);
			pstmt.setInt(10, cdEmpresa);
			pstmt.setTimestamp(11, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(12, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(13, cdTributoPIS);
			pstmt.setInt(14, cdTributoCOFINS);
			pstmt.setInt(15, cdEmpresa);
			pstmt.setTimestamp(16, new Timestamp(dtInicial.getTimeInMillis()));
			pstmt.setTimestamp(17, new Timestamp(dtFinal.getTimeInMillis()));
			pstmt.setInt(18, cdTributoPIS);
			pstmt.setInt(19, cdTributoCOFINS);
			pstmt.setInt(20, cdEmpresa);
			ResultSetMap rsm = new ResultSetMap(pstmt.executeQuery());
			while(rsm.next()){
				arrayDeCodigos.add(rsm.getInt("cd_pessoa"));
			}
			
			return arrayDeCodigos;
		}
		catch(Exception e){
			System.out.println("ERRO getParticipantesDoPeriodo: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
		
	
	/**
	 * Metodo PALIATIVO para corrigir documentos de saída e entrada sem tributação por falta de configuração prévia 
	 * @param cdEmpresa
	 * @return
	 */
	public static Result correcaoTributoDocumentosSaida(int cdEmpresa){
		Connection connection = Conexao.conectar();
		boolean isConnectionNull = true;
		try {
			connection = isConnectionNull ? Conexao.conectar() : connection;
			connection.setAutoCommit(isConnectionNull ? false : connection.getAutoCommit());

			//Busca todos os documento de saída que estejam concluidos, que sejam da empresa passada, e não tenham nenhum item com tributo calculado
			ResultSetMap rsmDocumentoSaida = new ResultSetMap(connection.prepareStatement("SELECT * FROM alm_documento_saida A " +
					"																		WHERE A.st_documento_saida = " +DocumentoSaidaServices.ST_CONCLUIDO + 
					"																		  AND A.cd_empresa = " + cdEmpresa + 
					"																		  AND NOT EXISTS (SELECT * FROM adm_saida_item_aliquota B " + 
					"																						   WHERE B.cd_documento_saida = A.cd_documento_saida)").executeQuery());
			
			while(rsmDocumentoSaida.next()){
			
				ResultSetMap rsmItens = DocumentoSaidaServices.getAllItens(rsmDocumentoSaida.getInt("cd_documento_saida"));
				while(rsmItens.next()){
					DocumentoSaidaItem item = DocumentoSaidaItemDAO.get(rsmItens.getInt("cd_documento_saida"), rsmItens.getInt("cd_produto_servico"), rsmItens.getInt("cd_empresa"), rsmItens.getInt("cd_item"), connection);
					ResultSetMap rsmTributos = SaidaItemAliquotaServices.calcTributos(item, connection);
					if (rsmTributos == null) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						com.tivic.manager.util.Util.registerLog(new Exception("Tributos nao calculados!"));
						return new Result(-1, "Erro ao tentar calcular tributos!");
					}
				}
			}
			
			
			//Busca todos os documento de entrada que estejam concluidos, que sejam da empresa passada, e não tenham nenhum item com tributo calculado
			ResultSetMap rsmDocumentoEntrada = new ResultSetMap(connection.prepareStatement("SELECT * FROM alm_documento_entrada A " +
					"																		WHERE A.st_documento_entrada = " +DocumentoEntradaServices.ST_LIBERADO + 
					"																		  AND A.cd_empresa = " + cdEmpresa + 
					"																		  AND NOT EXISTS (SELECT * FROM adm_entrada_item_aliquota B " + 
					"																						   WHERE B.cd_documento_entrada = A.cd_documento_entrada)").executeQuery());
			
			while(rsmDocumentoEntrada.next()){
			
				ResultSetMap rsmItens = DocumentoEntradaServices.getAllItens(rsmDocumentoEntrada.getInt("cd_documento_entrada"));
				while(rsmItens.next()){
					DocumentoEntradaItem item = DocumentoEntradaItemDAO.get(rsmItens.getInt("cd_documento_entrada"), rsmItens.getInt("cd_produto_servico"), rsmItens.getInt("cd_empresa"), rsmItens.getInt("cd_item"), connection);
					ResultSetMap rsmTributos = EntradaItemAliquotaServices.calcTributos(item, connection);
					if (rsmTributos == null) {
						if (isConnectionNull)
							Conexao.rollback(connection);
						com.tivic.manager.util.Util.registerLog(new Exception("Tributos nao calculados!"));
						return new Result(-1, "Erro ao tentar calcular tributos!");
					}
				}
			}
			
			if(isConnectionNull)
				connection.commit();
			
			return new Result(1);
		}
		catch(Exception e){
			System.out.println("ERRO getParticipantesDoPeriodo: " + e);
			return null;
		}
		finally{
			Conexao.desconectar(connection);
		}
	}
	
	/**
	 * Metodo usado para corrigir os tributos de documentos de entrada, saída e NFe. Apenas identificando as aliquotas 
	 * usadas e alterando o CST e o PR ALIQUOTA (no caso de saída) para aquele cadastrado no sistema
	 * @return
	 */
	public static Result atualizarTributos(){
		Connection connect = Conexao.conectar();
		try{
			connect.setAutoCommit(false);

			GregorianCalendar dtInicioJaneiro = new GregorianCalendar();
			dtInicioJaneiro.set(Calendar.DAY_OF_MONTH, 1);
			dtInicioJaneiro.set(Calendar.MONTH, 0);
			dtInicioJaneiro.set(Calendar.YEAR, 2016);

			
			ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
			System.out.println("1");
			//Entrada			
			criterios.add(new ItemComparator("CAST(dt_documento_entrada AS DATE)", Util.formatDate(dtInicioJaneiro, "dd/MM/yyyy"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
			criterios.add(new ItemComparator("CAST(dt_documento_entrada AS DATE)", Util.formatDate(Util.getDataAtual(), "dd/MM/yyyy"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
			ResultSetMap rsmDocumentoEntrada = DocumentoEntradaDAO.find(criterios, connect);
			ResultSetMap rsmDocumentoEntradaItem;
			Result resultado;
			while(rsmDocumentoEntrada.next()){
				rsmDocumentoEntradaItem = DocumentoEntradaServices.getAllItens(rsmDocumentoEntrada.getInt("cd_documento_entrada"), false, connect);
				if(rsmDocumentoEntradaItem.next()){
					do{
						resultado = calcTributosEntrada(DocumentoEntradaItemDAO.get(rsmDocumentoEntradaItem.getInt("cd_documento_entrada"), rsmDocumentoEntradaItem.getInt("cd_produto_servico"), rsmDocumentoEntradaItem.getInt("cd_empresa"), rsmDocumentoEntradaItem.getInt("cd_item"), connect), connect);
						if(resultado.getCode() <= 0){
							Conexao.rollback(connect);
							return new Result(-1, "Erro na tributacao de um item");
						}
					}while(rsmDocumentoEntradaItem.next());
				}
			}
			System.out.println("2");
			//Saida
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("CAST(dt_documento_saida AS DATE)", Util.formatDate(dtInicioJaneiro, "dd/MM/yyyy"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
			criterios.add(new ItemComparator("CAST(dt_documento_saida AS DATE)", Util.formatDate(Util.getDataAtual(), "dd/MM/yyyy"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
			ResultSetMap rsmDocumentoSaida = DocumentoSaidaDAO.find(criterios, connect);
			ResultSetMap rsmDocumentoSaidaItem;
			while(rsmDocumentoSaida.next()){
				rsmDocumentoSaidaItem = DocumentoSaidaServices.getAllItens(rsmDocumentoSaida.getInt("cd_documento_saida"), false, connect);
				if(rsmDocumentoSaidaItem.next()){
					do{
						resultado = calcTributosSaida(DocumentoSaidaItemDAO.get(rsmDocumentoSaidaItem.getInt("cd_documento_saida"), rsmDocumentoSaidaItem.getInt("cd_produto_servico"), rsmDocumentoSaidaItem.getInt("cd_empresa"), rsmDocumentoSaidaItem.getInt("cd_item"), connect), connect);
						if(resultado.getCode() <= 0){
							Conexao.rollback(connect);
							return new Result(-1, "Erro na tributacao de um item");
						}
					}while(rsmDocumentoSaidaItem.next());
				}
			}
			System.out.println("3");
			//NFe
			criterios = new ArrayList<ItemComparator>();
			criterios.add(new ItemComparator("CAST(dt_autorizacao AS DATE)", Util.formatDate(dtInicioJaneiro, "dd/MM/yyyy"), ItemComparator.GREATER_EQUAL, Types.TIMESTAMP));
			criterios.add(new ItemComparator("CAST(dt_autorizacao AS DATE)", Util.formatDate(Util.getDataAtual(), "dd/MM/yyyy"), ItemComparator.MINOR_EQUAL, Types.TIMESTAMP));
			ResultSetMap rsmNotaFiscalEletronica = NotaFiscalDAO.find(criterios, connect);
			ResultSetMap rsmNotaFiscalItem = new ResultSetMap();
			while(rsmNotaFiscalEletronica.next()){
				rsmNotaFiscalItem = NotaFiscalServices.getAllItens(rsmNotaFiscalEletronica.getInt("cd_nota_fiscal"), connect);
				if(rsmNotaFiscalItem.next()){
					do{
						resultado = calcTributosNfe(NotaFiscalItemDAO.get(rsmNotaFiscalItem.getInt("cd_nota_fiscal"), rsmNotaFiscalItem.getInt("cd_item"), connect), connect);
						if(resultado.getCode() <= 0){
							Conexao.rollback(connect);
							return new Result(-1, "Erro na tributacao de um item");
						}
					}while(rsmNotaFiscalItem.next());
				}
			}
			System.out.println("4");
			connect.commit();
			System.out.println("5");
			return new Result(1, "Sucesso");
		}
		
		catch(Exception e){
			Conexao.rollback(connect);
			return new Result(-1, "Erro");
		}
		
		finally{
			Conexao.desconectar(connect);
		}
	}
	
	
	public static Result calcTributosEntrada(DocumentoEntradaItem item, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			/*
			 *  Instanciando Documento de Saída
			 */
			DocumentoEntrada doc = DocumentoEntradaDAO.get(item.getCdDocumentoEntrada(), connect);
			
			/*
			 *  Descobrindo dados do endereço
			 */
			int cdCidade=0, cdEstado=0, cdPais=0;
			ResultSet rs = connect.prepareStatement("SELECT A.cd_cidade, B.cd_estado, C.cd_pais " +
													"FROM grl_pessoa_endereco A " +
													"LEFT OUTER JOIN grl_cidade B ON (A.cd_cidade = B.cd_cidade)" +
													"LEFT OUTER JOIN grl_estado C ON (B.cd_estado = C.cd_estado)" +
													"WHERE A.cd_pessoa    = "+doc.getCdFornecedor()+
													"  AND A.lg_principal = 1").executeQuery();
			if(rs.next())	{
				cdCidade = rs.getInt("cd_cidade");
				cdEstado = rs.getInt("cd_estado");
				cdPais   = rs.getInt("cd_pais");
			}
			else	{
				rs = connect.prepareStatement("SELECT D.cd_cidade, B.cd_estado, C.cd_pais " +
											  "FROM grl_empresa A " +
											  "LEFT OUTER JOIN grl_pessoa_endereco D ON (A.cd_empresa = D.cd_pessoa AND D.lg_principal = 1) " +
											  "LEFT OUTER JOIN grl_cidade B ON (D.cd_cidade = B.cd_cidade)" +
											  "LEFT OUTER JOIN grl_estado C ON (B.cd_estado = C.cd_estado)" +
											  "WHERE A.cd_empresa = "+doc.getCdEmpresa()).executeQuery();
				if(rs.next())	{
					cdCidade = rs.getInt("cd_cidade");
					cdEstado = rs.getInt("cd_estado");
					cdPais   = rs.getInt("cd_pais");
				}
			}
			
			/*
			 *  Tributos padrões
			 */
			int cdTributoICMS 	= ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0);
			int cdTributoIPI    = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_IPI", 0);
			int cdTributoPIS 	= ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_PIS", 0);
			int cdTributoCOFINS = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_COFINS", 0);
			int cdTributoII     = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_II", 0);
			
			/*
			 *  Pesquisa classificacao fiscal do produto/serviço
			 */
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_produto_servico " +
					                      								"WHERE cd_produto_servico = " +item.getCdProdutoServico()).executeQuery());
			int cdClassificacaoFiscal = 0;
			if(rsm.next())
				cdClassificacaoFiscal = rsm.getInt("cd_classificacao_fiscal");
			/*
			 *  Calcula base de cálculo
			 */
			float vlBaseCalculo = (item.getQtEntrada() * item.getVlUnitario()) - item.getVlDesconto() + item.getVlAcrescimo();
			
			//ICMS
			rsm = calcTributos(cdTributoICMS, TributoAliquotaServices.OP_COMPRA, doc.getCdNaturezaOperacao(),
					                                        cdClassificacaoFiscal, item.getCdProdutoServico(), cdPais, cdEstado, cdCidade,
					                                        vlBaseCalculo, connect);
			/*
			 *  Insere impostos calculados
			 */
			while(rsm.next())	{
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_produto_servico", "" + item.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_documento_entrada", "" + item.getCdDocumentoEntrada(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_empresa", "" + item.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tributo", "" + cdTributoICMS, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_item", "" + item.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
				
				ResultSetMap rsmEntradaItemAliquota = EntradaItemAliquotaDAO.find(criterios, connect);
				if(rsmEntradaItemAliquota.next()){
					EntradaItemAliquota entradaItemAliquota = EntradaItemAliquotaDAO.get(rsmEntradaItemAliquota.getInt("cd_produto_servico"), rsmEntradaItemAliquota.getInt("cd_documento_entrada"), rsmEntradaItemAliquota.getInt("cd_empresa"), rsmEntradaItemAliquota.getInt("cd_tributo_aliquota"), rsmEntradaItemAliquota.getInt("cd_tributo"), rsmEntradaItemAliquota.getInt("cd_item"), connect);
					entradaItemAliquota.setCdTributoAliquota(rsm.getInt("cd_tributo_aliquota"));
					entradaItemAliquota.setCdSituacaoTributaria(rsm.getInt("cd_situacao_tributaria"));
					if(EntradaItemAliquotaDAO.update(entradaItemAliquota, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar entrada item aliquota ICMS");
					}
					
				}
				
			}
			
			//IPI
			rsm = calcTributos(cdTributoIPI, TributoAliquotaServices.OP_COMPRA, doc.getCdNaturezaOperacao(),
					                                        cdClassificacaoFiscal, item.getCdProdutoServico(), cdPais, cdEstado, cdCidade,
					                                        vlBaseCalculo, connect);
			/*
			 *  Insere impostos calculados
			 */
			while(rsm.next())	{
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_produto_servico", "" + item.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_documento_entrada", "" + item.getCdDocumentoEntrada(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_empresa", "" + item.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tributo", "" + cdTributoIPI, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_item", "" + item.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
				
				ResultSetMap rsmEntradaItemAliquota = EntradaItemAliquotaDAO.find(criterios, connect);
				if(rsmEntradaItemAliquota.next()){
					EntradaItemAliquota entradaItemAliquota = EntradaItemAliquotaDAO.get(rsmEntradaItemAliquota.getInt("cd_produto_servico"), rsmEntradaItemAliquota.getInt("cd_documento_entrada"), rsmEntradaItemAliquota.getInt("cd_empresa"), rsmEntradaItemAliquota.getInt("cd_tributo_aliquota"), rsmEntradaItemAliquota.getInt("cd_tributo"), rsmEntradaItemAliquota.getInt("cd_item"), connect);
					entradaItemAliquota.setCdTributoAliquota(rsm.getInt("cd_tributo_aliquota"));
					entradaItemAliquota.setCdSituacaoTributaria(rsm.getInt("cd_situacao_tributaria"));
					if(EntradaItemAliquotaDAO.update(entradaItemAliquota, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar entrada item aliquota IPI");
					}
					
				}
			}
			
			//PIS
			rsm = calcTributos(cdTributoPIS, TributoAliquotaServices.OP_COMPRA, doc.getCdNaturezaOperacao(),
					                                        cdClassificacaoFiscal, item.getCdProdutoServico(), cdPais, cdEstado, cdCidade,
					                                        vlBaseCalculo, connect);
			
			System.out.println("rsm = " + rsm); 
			/*
			 *  Insere impostos calculados
			 */
			while(rsm.next())	{
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_produto_servico", "" + item.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_documento_entrada", "" + item.getCdDocumentoEntrada(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_empresa", "" + item.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tributo", "" + cdTributoPIS, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_item", "" + item.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
				
				ResultSetMap rsmEntradaItemAliquota = EntradaItemAliquotaDAO.find(criterios, connect);
				if(rsmEntradaItemAliquota.next()){
					EntradaItemAliquota entradaItemAliquota = EntradaItemAliquotaDAO.get(rsmEntradaItemAliquota.getInt("cd_produto_servico"), rsmEntradaItemAliquota.getInt("cd_documento_entrada"), rsmEntradaItemAliquota.getInt("cd_empresa"), rsmEntradaItemAliquota.getInt("cd_tributo_aliquota"), rsmEntradaItemAliquota.getInt("cd_tributo"), rsmEntradaItemAliquota.getInt("cd_item"), connect);
					entradaItemAliquota.setCdTributoAliquota(rsm.getInt("cd_tributo_aliquota"));
					entradaItemAliquota.setCdSituacaoTributaria(rsm.getInt("cd_situacao_tributaria"));
					if(EntradaItemAliquotaDAO.update(entradaItemAliquota, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar entrada item aliquota PIS");
					}
				}
				else{
					EntradaItemAliquota entradaItemAliquota = new EntradaItemAliquota(item.getCdProdutoServico(), item.getCdDocumentoEntrada(), item.getCdEmpresa(), rsm.getInt("cd_tributo_aliquota"), cdTributoPIS, item.getCdItem(), 0, vlBaseCalculo);
					int ret = EntradaItemAliquotaDAO.insert(entradaItemAliquota, connect);
					if(ret <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao inserir entrada item aliquota");
					}
				}
			}
			
			//COFINS
			rsm = calcTributos(cdTributoCOFINS, TributoAliquotaServices.OP_COMPRA, doc.getCdNaturezaOperacao(),
					                                        cdClassificacaoFiscal, item.getCdProdutoServico(), cdPais, cdEstado, cdCidade,
					                                        vlBaseCalculo, connect);
			/*
			 *  Insere impostos calculados
			 */
			while(rsm.next())	{
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_produto_servico", "" + item.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_documento_entrada", "" + item.getCdDocumentoEntrada(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_empresa", "" + item.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tributo", "" + cdTributoCOFINS, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_item", "" + item.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
				
				ResultSetMap rsmEntradaItemAliquota = EntradaItemAliquotaDAO.find(criterios, connect);
				if(rsmEntradaItemAliquota.next()){
					EntradaItemAliquota entradaItemAliquota = EntradaItemAliquotaDAO.get(rsmEntradaItemAliquota.getInt("cd_produto_servico"), rsmEntradaItemAliquota.getInt("cd_documento_entrada"), rsmEntradaItemAliquota.getInt("cd_empresa"), rsmEntradaItemAliquota.getInt("cd_tributo_aliquota"), rsmEntradaItemAliquota.getInt("cd_tributo"), rsmEntradaItemAliquota.getInt("cd_item"), connect);
					entradaItemAliquota.setCdTributoAliquota(rsm.getInt("cd_tributo_aliquota"));
					entradaItemAliquota.setCdSituacaoTributaria(rsm.getInt("cd_situacao_tributaria"));
					if(EntradaItemAliquotaDAO.update(entradaItemAliquota, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar entrada item aliquota COFINS");
					}
					
				}
				else{
					EntradaItemAliquota entradaItemAliquota = new EntradaItemAliquota(item.getCdProdutoServico(), item.getCdDocumentoEntrada(), item.getCdEmpresa(), rsm.getInt("cd_tributo_aliquota"), cdTributoCOFINS, item.getCdItem(), 0, vlBaseCalculo);
					if(EntradaItemAliquotaDAO.insert(entradaItemAliquota, connect) <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao inserir entrada item aliquota");
					}
				}
			}
			
			//II
			rsm = calcTributos(cdTributoII, TributoAliquotaServices.OP_COMPRA, doc.getCdNaturezaOperacao(),
					                                        cdClassificacaoFiscal, item.getCdProdutoServico(), cdPais, cdEstado, cdCidade,
					                                        vlBaseCalculo, connect);
			/*
			 *  Insere impostos calculados
			 */
			while(rsm.next())	{
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_produto_servico", "" + item.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_documento_entrada", "" + item.getCdDocumentoEntrada(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_empresa", "" + item.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tributo", "" + cdTributoII, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_item", "" + item.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
				
				ResultSetMap rsmEntradaItemAliquota = EntradaItemAliquotaDAO.find(criterios, connect);
				if(rsmEntradaItemAliquota.next()){
					EntradaItemAliquota entradaItemAliquota = EntradaItemAliquotaDAO.get(rsmEntradaItemAliquota.getInt("cd_produto_servico"), rsmEntradaItemAliquota.getInt("cd_documento_entrada"), rsmEntradaItemAliquota.getInt("cd_empresa"), rsmEntradaItemAliquota.getInt("cd_tributo_aliquota"), rsmEntradaItemAliquota.getInt("cd_tributo"), rsmEntradaItemAliquota.getInt("cd_item"), connect);
					entradaItemAliquota.setCdTributoAliquota(rsm.getInt("cd_tributo_aliquota"));
					entradaItemAliquota.setCdSituacaoTributaria(rsm.getInt("cd_situacao_tributaria"));
					if(EntradaItemAliquotaDAO.update(entradaItemAliquota, connect) < 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar entrada item aliquota II");
					}
					
				}
			}
			
			return new Result(1);
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
	
	
	public static Result calcTributosSaida(DocumentoSaidaItem item, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			/*
			 *  Instanciando Documento de Saída
			 */
			DocumentoSaida doc = DocumentoSaidaDAO.get(item.getCdDocumentoSaida(), connect);
			
			/*
			 *  Descobrindo dados do endereço
			 */
			int cdCidade=0, cdEstado=0, cdPais=0;
			ResultSet rs = connect.prepareStatement("SELECT A.cd_cidade, B.cd_estado, C.cd_pais " +
													"FROM grl_pessoa_endereco A " +
													"LEFT OUTER JOIN grl_cidade B ON (A.cd_cidade = B.cd_cidade)" +
													"LEFT OUTER JOIN grl_estado C ON (B.cd_estado = C.cd_estado)" +
													"WHERE A.cd_pessoa    = "+doc.getCdCliente()+
													"  AND A.lg_principal = 1").executeQuery();
			if(rs.next())	{
				cdCidade = rs.getInt("cd_cidade");
				cdEstado = rs.getInt("cd_estado");
				cdPais   = rs.getInt("cd_pais");
			}
			else	{
				rs = connect.prepareStatement("SELECT D.cd_cidade, B.cd_estado, C.cd_pais " +
											  "FROM grl_empresa A " +
											  "LEFT OUTER JOIN grl_pessoa_endereco D ON (A.cd_empresa = D.cd_pessoa AND D.lg_principal = 1) " +
											  "LEFT OUTER JOIN grl_cidade B ON (D.cd_cidade = B.cd_cidade)" +
											  "LEFT OUTER JOIN grl_estado C ON (B.cd_estado = C.cd_estado)" +
											  "WHERE A.cd_empresa = "+doc.getCdEmpresa()).executeQuery();
				if(rs.next())	{
					cdCidade = rs.getInt("cd_cidade");
					cdEstado = rs.getInt("cd_estado");
					cdPais   = rs.getInt("cd_pais");
				}
			}
			
			/*
			 *  Tributos padrões
			 */
			int cdTributoICMS 	= ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0);
			int cdTributoIPI    = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_IPI", 0);
			int cdTributoPIS 	= ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_PIS", 0);
			int cdTributoCOFINS = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_COFINS", 0);
			int cdTributoII     = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_II", 0);
			
			/*
			 *  Pesquisa classificacao fiscal do produto/serviço
			 */
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_produto_servico " +
					                      								"WHERE cd_produto_servico = " +item.getCdProdutoServico()).executeQuery());
			int cdClassificacaoFiscal = 0;
			if(rsm.next())
				cdClassificacaoFiscal = rsm.getInt("cd_classificacao_fiscal");
			/*
			 *  Calcula base de cálculo
			 */
			float vlBaseCalculo = (item.getQtSaida() * item.getVlUnitario()) - item.getVlDesconto() + item.getVlAcrescimo();
			
			//ICMS
			rsm = calcTributos(cdTributoICMS, TributoAliquotaServices.OP_VENDA, doc.getCdNaturezaOperacao(),
					                                        cdClassificacaoFiscal, item.getCdProdutoServico(), cdPais, cdEstado, cdCidade,
					                                        vlBaseCalculo, connect);
			/*
			 *  Insere impostos calculados
			 */
			while(rsm.next())	{
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_produto_servico", "" + item.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_documento_saida", "" + item.getCdDocumentoSaida(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_empresa", "" + item.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tributo", "" + cdTributoICMS, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_item", "" + item.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
				
				ResultSetMap rsmSaidaItemAliquota = SaidaItemAliquotaDAO.find(criterios, connect);
				if(rsmSaidaItemAliquota.next()){
					
					SaidaItemAliquota saidaItemAliquota = SaidaItemAliquotaDAO.get(rsmSaidaItemAliquota.getInt("cd_produto_servico"), rsmSaidaItemAliquota.getInt("cd_documento_saida"), rsmSaidaItemAliquota.getInt("cd_empresa"), rsmSaidaItemAliquota.getInt("cd_tributo_aliquota"), rsmSaidaItemAliquota.getInt("cd_tributo"), rsmSaidaItemAliquota.getInt("cd_item"), connect);
					saidaItemAliquota.setCdTributoAliquota(rsm.getInt("cd_tributo_aliquota"));
					saidaItemAliquota.setPrAliquota(rsm.getFloat("pr_aliquota"));
					if(SaidaItemAliquotaDAO.update(saidaItemAliquota, connect) <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar saida item aliquota");
					}
					
				}
			}
			
			//IPI
			rsm = calcTributos(cdTributoIPI, TributoAliquotaServices.OP_VENDA, doc.getCdNaturezaOperacao(),
					                                        cdClassificacaoFiscal, item.getCdProdutoServico(), cdPais, cdEstado, cdCidade,
					                                        vlBaseCalculo, connect);
			/*
			 *  Insere impostos calculados
			 */
			while(rsm.next())	{
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_produto_servico", "" + item.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_documento_saida", "" + item.getCdDocumentoSaida(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_empresa", "" + item.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tributo", "" + cdTributoIPI, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_item", "" + item.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
				
				ResultSetMap rsmSaidaItemAliquota = SaidaItemAliquotaDAO.find(criterios, connect);
				if(rsmSaidaItemAliquota.next()){
					SaidaItemAliquota saidaItemAliquota = SaidaItemAliquotaDAO.get(rsmSaidaItemAliquota.getInt("cd_produto_servico"), rsmSaidaItemAliquota.getInt("cd_documento_saida"), rsmSaidaItemAliquota.getInt("cd_empresa"), rsmSaidaItemAliquota.getInt("cd_tributo_aliquota"), rsmSaidaItemAliquota.getInt("cd_tributo"), rsmSaidaItemAliquota.getInt("cd_item"), connect);
					saidaItemAliquota.setCdTributoAliquota(rsm.getInt("cd_tributo_aliquota"));
					saidaItemAliquota.setPrAliquota(rsm.getFloat("pr_aliquota"));
					if(SaidaItemAliquotaDAO.update(saidaItemAliquota, connect) <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar saida item aliquota");
					}
					
				}
			}
			
			//PIS
			rsm = calcTributos(cdTributoPIS, TributoAliquotaServices.OP_VENDA, doc.getCdNaturezaOperacao(),
					                                        cdClassificacaoFiscal, item.getCdProdutoServico(), cdPais, cdEstado, cdCidade,
					                                        vlBaseCalculo, connect);
			/*
			 *  Insere impostos calculados
			 */
			while(rsm.next())	{
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_produto_servico", "" + item.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_documento_saida", "" + item.getCdDocumentoSaida(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_empresa", "" + item.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tributo", "" + cdTributoPIS, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_item", "" + item.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
				
				ResultSetMap rsmSaidaItemAliquota = SaidaItemAliquotaDAO.find(criterios, connect);
				if(rsmSaidaItemAliquota.next()){
					SaidaItemAliquota saidaItemAliquota = SaidaItemAliquotaDAO.get(rsmSaidaItemAliquota.getInt("cd_produto_servico"), rsmSaidaItemAliquota.getInt("cd_documento_saida"), rsmSaidaItemAliquota.getInt("cd_empresa"), rsmSaidaItemAliquota.getInt("cd_tributo_aliquota"), rsmSaidaItemAliquota.getInt("cd_tributo"), rsmSaidaItemAliquota.getInt("cd_item"), connect);
					saidaItemAliquota.setCdTributoAliquota(rsm.getInt("cd_tributo_aliquota"));
					saidaItemAliquota.setPrAliquota(rsm.getFloat("pr_aliquota"));
					if(SaidaItemAliquotaDAO.update(saidaItemAliquota, connect) <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar saida item aliquota");
					}
					
				}
			}
			
			//COFINS
			rsm = calcTributos(cdTributoCOFINS, TributoAliquotaServices.OP_VENDA, doc.getCdNaturezaOperacao(),
					                                        cdClassificacaoFiscal, item.getCdProdutoServico(), cdPais, cdEstado, cdCidade,
					                                        vlBaseCalculo, connect);
			/*
			 *  Insere impostos calculados
			 */
			while(rsm.next())	{
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_produto_servico", "" + item.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_documento_saida", "" + item.getCdDocumentoSaida(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_empresa", "" + item.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tributo", "" + cdTributoCOFINS, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_item", "" + item.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
				
				ResultSetMap rsmSaidaItemAliquota = SaidaItemAliquotaDAO.find(criterios, connect);
				if(rsmSaidaItemAliquota.next()){
					SaidaItemAliquota saidaItemAliquota = SaidaItemAliquotaDAO.get(rsmSaidaItemAliquota.getInt("cd_produto_servico"), rsmSaidaItemAliquota.getInt("cd_documento_saida"), rsmSaidaItemAliquota.getInt("cd_empresa"), rsmSaidaItemAliquota.getInt("cd_tributo_aliquota"), rsmSaidaItemAliquota.getInt("cd_tributo"), rsmSaidaItemAliquota.getInt("cd_item"), connect);
					saidaItemAliquota.setCdTributoAliquota(rsm.getInt("cd_tributo_aliquota"));
					saidaItemAliquota.setPrAliquota(rsm.getFloat("pr_aliquota"));
					if(SaidaItemAliquotaDAO.update(saidaItemAliquota, connect) <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar saida item aliquota");
					}
					
					
				}
			}
			
			//II
			rsm = calcTributos(cdTributoII, TributoAliquotaServices.OP_VENDA, doc.getCdNaturezaOperacao(),
					                                        cdClassificacaoFiscal, item.getCdProdutoServico(), cdPais, cdEstado, cdCidade,
					                                        vlBaseCalculo, connect);
			/*
			 *  Insere impostos calculados
			 */
			while(rsm.next())	{
				ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
				criterios.add(new ItemComparator("cd_produto_servico", "" + item.getCdProdutoServico(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_documento_saida", "" + item.getCdDocumentoSaida(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_empresa", "" + item.getCdEmpresa(), ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_tributo", "" + cdTributoII, ItemComparator.EQUAL, Types.INTEGER));
				criterios.add(new ItemComparator("cd_item", "" + item.getCdItem(), ItemComparator.EQUAL, Types.INTEGER));
				
				ResultSetMap rsmSaidaItemAliquota = SaidaItemAliquotaDAO.find(criterios, connect);
				if(rsmSaidaItemAliquota.next()){
					SaidaItemAliquota saidaItemAliquota = SaidaItemAliquotaDAO.get(rsmSaidaItemAliquota.getInt("cd_produto_servico"), rsmSaidaItemAliquota.getInt("cd_documento_saida"), rsmSaidaItemAliquota.getInt("cd_empresa"), rsmSaidaItemAliquota.getInt("cd_tributo_aliquota"), rsmSaidaItemAliquota.getInt("cd_tributo"), rsmSaidaItemAliquota.getInt("cd_item"), connect);
					saidaItemAliquota.setCdTributoAliquota(rsm.getInt("cd_tributo_aliquota"));
					saidaItemAliquota.setPrAliquota(rsm.getFloat("pr_aliquota"));
					if(SaidaItemAliquotaDAO.update(saidaItemAliquota, connect) <= 0){
						if(isConnectionNull)
							Conexao.rollback(connect);
						return new Result(-1, "Erro ao atualizar saida item aliquota");
					}
					
				}
			}
			
			return new Result(1);
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
	
	
	public static Result calcTributosNfe(NotaFiscalItem item, Connection connect) {
		boolean isConnectionNull = connect==null;
		try {
			connect = isConnectionNull ? Conexao.conectar() : connect;
			connect.setAutoCommit(isConnectionNull ? false : connect.getAutoCommit());

			/*
			 *  Instanciando Nota Fiscal
			 */
			NotaFiscal nota = NotaFiscalDAO.get(item.getCdNotaFiscal(), connect);
			
			/*
			 *  Descobrindo dados do endereço
			 */
			int cdCidade=0, cdEstado=0, cdPais=0;
			ResultSet rs = connect.prepareStatement("SELECT A.cd_cidade, B.cd_estado, C.cd_pais " +
													"FROM grl_pessoa_endereco A " +
													"LEFT OUTER JOIN grl_cidade B ON (A.cd_cidade = B.cd_cidade)" +
													"LEFT OUTER JOIN grl_estado C ON (B.cd_estado = C.cd_estado)" +
													"WHERE A.cd_pessoa    = "+nota.getCdDestinatario()+
													"  AND A.lg_principal = 1").executeQuery();
			if(rs.next())	{
				cdCidade = rs.getInt("cd_cidade");
				cdEstado = rs.getInt("cd_estado");
				cdPais   = rs.getInt("cd_pais");
			}
			else	{
				rs = connect.prepareStatement("SELECT D.cd_cidade, B.cd_estado, C.cd_pais " +
											  "FROM grl_empresa A " +
											  "LEFT OUTER JOIN grl_pessoa_endereco D ON (A.cd_empresa = D.cd_pessoa AND D.lg_principal = 1) " +
											  "LEFT OUTER JOIN grl_cidade B ON (D.cd_cidade = B.cd_cidade)" +
											  "LEFT OUTER JOIN grl_estado C ON (B.cd_estado = C.cd_estado)" +
											  "WHERE A.cd_empresa = "+nota.getCdEmpresa()).executeQuery();
				if(rs.next())	{
					cdCidade = rs.getInt("cd_cidade");
					cdEstado = rs.getInt("cd_estado");
					cdPais   = rs.getInt("cd_pais");
				}
			}
			
			/*
			 *  Tributos padrões
			 */
			int cdTributoICMS 	= ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_ICMS", 0);
			int cdTributoIPI    = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_IPI", 0);
			int cdTributoPIS 	= ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_PIS", 0);
			int cdTributoCOFINS = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_COFINS", 0);
			int cdTributoII     = ParametroServices.getValorOfParametroAsInteger("CD_TRIBUTO_II", 0);
			
			/*
			 *  Pesquisa classificacao fiscal do produto/serviço
			 */
			ResultSetMap rsm = new ResultSetMap(connect.prepareStatement("SELECT * FROM grl_produto_servico " +
					                      								"WHERE cd_produto_servico = " +item.getCdProdutoServico()).executeQuery());
			int cdClassificacaoFiscal = 0;
			if(rsm.next())
				cdClassificacaoFiscal = rsm.getInt("cd_classificacao_fiscal");
			/*
			 *  Calcula base de cálculo
			 */
			float vlBaseCalculo = (item.getQtTributario() * item.getVlUnitario()) - item.getVlDesconto() + item.getVlAcrescimo();
			
			//ICMS
			rsm = calcTributos(cdTributoICMS, (nota.getTpMovimento() == NotaFiscalServices.MOV_SAIDA ? TributoAliquotaServices.OP_VENDA : TributoAliquotaServices.OP_COMPRA), nota.getCdNaturezaOperacao(),
					                                        cdClassificacaoFiscal, item.getCdProdutoServico(), cdPais, cdEstado, cdCidade,
					                                        vlBaseCalculo, connect);
			/*
			 *  Insere impostos calculados
			 */
			while(rsm.next())	{
				NotaFiscalItemTributo notaItemTributo = NotaFiscalItemTributoDAO.get(item.getCdNotaFiscal(), item.getCdItem(), cdTributoICMS, connect);
				notaItemTributo.setCdTributoAliquota(rsm.getInt("cd_tributo_aliquota"));
				if(nota.getTpMovimento() == NotaFiscalServices.MOV_SAIDA)
					notaItemTributo.setPrAliquota(rsm.getFloat("pr_aliquota"));
				if(NotaFiscalItemTributoDAO.update(notaItemTributo, connect) <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar nota item tributo");
				}
			}
			
			//IPI
			rsm = calcTributos(cdTributoIPI, (nota.getTpMovimento() == NotaFiscalServices.MOV_SAIDA ? TributoAliquotaServices.OP_VENDA : TributoAliquotaServices.OP_COMPRA), nota.getCdNaturezaOperacao(),
					                                        cdClassificacaoFiscal, item.getCdProdutoServico(), cdPais, cdEstado, cdCidade,
					                                        vlBaseCalculo, connect);
			/*
			 *  Insere impostos calculados
			 */
			while(rsm.next())	{
				NotaFiscalItemTributo notaItemTributo = NotaFiscalItemTributoDAO.get(item.getCdNotaFiscal(), item.getCdItem(), cdTributoIPI, connect);
				notaItemTributo.setCdTributoAliquota(rsm.getInt("cd_tributo_aliquota"));
				if(nota.getTpMovimento() == NotaFiscalServices.MOV_SAIDA)
					notaItemTributo.setPrAliquota(rsm.getFloat("pr_aliquota"));
				if(NotaFiscalItemTributoDAO.update(notaItemTributo, connect) <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar nota item tributo");
				}
			}
			
			//PIS
			rsm = calcTributos(cdTributoPIS, (nota.getTpMovimento() == NotaFiscalServices.MOV_SAIDA ? TributoAliquotaServices.OP_VENDA : TributoAliquotaServices.OP_COMPRA), nota.getCdNaturezaOperacao(),
					                                        cdClassificacaoFiscal, item.getCdProdutoServico(), cdPais, cdEstado, cdCidade,
					                                        vlBaseCalculo, connect);
			/*
			 *  Insere impostos calculados
			 */
			while(rsm.next())	{
				NotaFiscalItemTributo notaItemTributo = NotaFiscalItemTributoDAO.get(item.getCdNotaFiscal(), item.getCdItem(), cdTributoPIS, connect);
				notaItemTributo.setCdTributoAliquota(rsm.getInt("cd_tributo_aliquota"));
				if(nota.getTpMovimento() == NotaFiscalServices.MOV_SAIDA)
					notaItemTributo.setPrAliquota(rsm.getFloat("pr_aliquota"));
				if(NotaFiscalItemTributoDAO.update(notaItemTributo, connect) <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar nota item tributo");
				}
			}
			
			//COFINS
			rsm = calcTributos(cdTributoCOFINS, (nota.getTpMovimento() == NotaFiscalServices.MOV_SAIDA ? TributoAliquotaServices.OP_VENDA : TributoAliquotaServices.OP_COMPRA), nota.getCdNaturezaOperacao(),
					                                        cdClassificacaoFiscal, item.getCdProdutoServico(), cdPais, cdEstado, cdCidade,
					                                        vlBaseCalculo, connect);
			/*
			 *  Insere impostos calculados
			 */
			while(rsm.next())	{
				NotaFiscalItemTributo notaItemTributo = NotaFiscalItemTributoDAO.get(item.getCdNotaFiscal(), item.getCdItem(), cdTributoCOFINS, connect);
				notaItemTributo.setCdTributoAliquota(rsm.getInt("cd_tributo_aliquota"));
				if(nota.getTpMovimento() == NotaFiscalServices.MOV_SAIDA)
					notaItemTributo.setPrAliquota(rsm.getFloat("pr_aliquota"));
				if(NotaFiscalItemTributoDAO.update(notaItemTributo, connect) <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar nota item tributo");
				}
			}
			
			//II
			rsm = calcTributos(cdTributoII, (nota.getTpMovimento() == NotaFiscalServices.MOV_SAIDA ? TributoAliquotaServices.OP_VENDA : TributoAliquotaServices.OP_COMPRA), nota.getCdNaturezaOperacao(),
					                                        cdClassificacaoFiscal, item.getCdProdutoServico(), cdPais, cdEstado, cdCidade,
					                                        vlBaseCalculo, connect);
			/*
			 *  Insere impostos calculados
			 */
			while(rsm.next())	{
				NotaFiscalItemTributo notaItemTributo = NotaFiscalItemTributoDAO.get(item.getCdNotaFiscal(), item.getCdItem(), cdTributoII, connect);
				notaItemTributo.setCdTributoAliquota(rsm.getInt("cd_tributo_aliquota"));
				if(nota.getTpMovimento() == NotaFiscalServices.MOV_SAIDA)
					notaItemTributo.setPrAliquota(rsm.getFloat("pr_aliquota"));
				if(NotaFiscalItemTributoDAO.update(notaItemTributo, connect) <= 0){
					if(isConnectionNull)
						Conexao.rollback(connect);
					return new Result(-1, "Erro ao atualizar nota item tributo");
				}
			}
			
			return new Result(1);
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
	
	public static ResultSetMap calcTributos(int cdTributo, int tpOperacao, int cdNaturezaOperacao, int cdClassificacaoFiscal, int cdProdutoServico,
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
					"  AND A.cd_tributo  = "+cdTributo+
					"  AND EXISTS (SELECT * FROM adm_produto_servico_tributo C " +
					"              WHERE A.cd_tributo  = C.cd_tributo " +
					"                AND B.cd_tributo_aliquota = C.cd_tributo_aliquota " +
					"                AND C.cd_natureza_operacao = "+cdNaturezaOperacao+
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
					"  AND A.cd_tributo  = "+cdTributo+
					"  AND EXISTS (SELECT * FROM adm_produto_servico_tributo C " +
					"              WHERE A.cd_tributo  = C.cd_tributo " +
					"                AND B.cd_tributo_aliquota = C.cd_tributo_aliquota " +
					"                AND C.cd_natureza_operacao = "+cdNaturezaOperacao+
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
}

