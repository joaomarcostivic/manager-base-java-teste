package com.tivic.manager.mob;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.codehaus.jettison.json.JSONException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.exception.EmptyListException;
import com.tivic.manager.fta.Cor;
import com.tivic.manager.fta.CorDAO;
import com.tivic.manager.fta.EspecieVeiculo;
import com.tivic.manager.fta.EspecieVeiculoDAO;
import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.fta.MarcaModeloDAO;
import com.tivic.manager.grl.ArquivoServices;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.Estado;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.aitpagamento.enums.BancoEnum;
import com.tivic.manager.mob.grafica.LoteImpressaoTipo;
import com.tivic.manager.mob.lotes.dto.impressao.Notificacao;
import com.tivic.manager.mob.lotes.impressao.pix.AutenticacaoUsuarioPix;
import com.tivic.manager.mob.lotes.impressao.pix.DadosAutenticacaoEnvio;
import com.tivic.manager.mob.lotes.impressao.pix.DadosAutenticacaoRetorno;
import com.tivic.manager.mob.lotes.impressao.pix.ImagemQrCodePixGenerator;
import com.tivic.manager.mob.lotes.impressao.pix.builders.DadosAutenticacaoEnvioBuilder;
import com.tivic.manager.mob.templates.AitFindEmissao;
import com.tivic.manager.rest.request.filter.Criterios;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.UsuarioDAO;
import com.tivic.manager.util.ContextManager;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.Conexao;
import com.tivic.sol.report.ReportServices;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.ConfManager;
import sol.util.Result;

public class AitReportServices {
	public static final String REPORT_AIT_SEGUNDA_VIA = "mob//impressao_ait";
	public static final int MODELO_COM_AR = 0;
	public static final int MODELO_SEM_AR = 1;
	
	public static final int NAO_CALCULAR_JUROS_NIP = 0;
	public static final int CALCULAR_JUROS_NIP = 1;
	private static final Map<String, String> VIDEO_ASSINATURAS = new HashMap<>();
    static {
    	VIDEO_ASSINATURAS.put("66747970", "MP4 / MOV / M4V");
    	VIDEO_ASSINATURAS.put("52494646", "AVI");
    	VIDEO_ASSINATURAS.put("1A45DFA3", "MKV / WebM");
    	VIDEO_ASSINATURAS.put("3026B275", "WMV (ASF)");
    	VIDEO_ASSINATURAS.put("71742020", "MOV (QuickTime)");
    }
	
	static AitReportGetCriteriosNAI getCriteriosNai = new AitReportGetCriteriosNAI();
	static AitReportGetCriteriosNIP getCriteriosNip = new AitReportGetCriteriosNIP();
	static AitReportGetCriteriosCondutor getCriteriosCondutor = new AitReportGetCriteriosCondutor();
	static AitReportGetParamns getParamns = new AitReportGetParamns();

	public static byte[] getNAIPrimeiraVia (int cdAit, int cdUsuario) throws Exception {
		return getNAIPrimeiraVia (cdAit, null, cdUsuario, false);
	}
		
	public static byte[] getNAIPrimeiraVia (int cdAit, Connection connect, int cdUsuario, boolean isLote) throws Exception, ValidacaoException{
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
		 	
			boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");		
			AitReportServicesNai servicesNai = new AitReportServicesNai();
			AitReportServicesDAO aitReportServicesDao = new AitReportServicesDAO(connect);
				
			String reportName = servicesNai.nameReportNai ();
			int primeiraVia = 0;

			ResultSetMap rsm = new ResultSetMap();
			
			rsm  = aitReportServicesDao.find(AitReportGetCriteriosNAI.primeiraVia (cdAit, connect, isLote));

			if (!rsm.next() && !lgBaseAntiga){
				atualizarDadosAit(cdAit, connect);
				AitReportValidators.validar(cdAit, AitMovimentoServices.NAI_ENVIADO, connect);
				rsm  = aitReportServicesDao.find(AitReportGetCriteriosNAI.primeiraVia (cdAit, connect, isLote));
			}
			rsm.beforeFirst();
				
			setarDataEmissao(rsm);
			AitReportValidatorsNAI.tipoEquipamento (rsm);	
			servicesNai.pegarDataDefesaPrimeiraVia (rsm, connect);
			servicesNai.labelViaNAI (rsm, null);
			
			if(lgBaseAntiga)
			{
				inserirIdAitOld (rsm);
				inserirNrRenavamOld (rsm);
			}
		
			HashMap<String, Object> paramns = getParamns.getParamnsNAI (connect);
			paramns.put("REPORT_LOCALE", new Locale("pt", "BR"));
			
			paramns.put ("MOB_IMAGEM_VEICULO", servicesNai.imagemVeiculo(rsm, connect));
			
			int isAr = AitReportValidatorsNAI.verificarAr (paramns);
			AitReportValidatorsNAI.setarTxtCondutor (rsm, isAr, primeiraVia, connect);
				
			byte[] print = new byte[] {};
			
			if (rsm != null && rsm.next() && paramns != null)
			{
				print = ReportServices.getPdfReport (reportName, paramns, rsm, connect);	
			}

			print = gerarPrint(print);
				
			servicesNai.gerarMovimentoNai (cdAit, connect, cdUsuario);

			if (isConnectionNull) {
				connect.commit();
			}
			
			return print;	
		}
		catch(AitReportErrorException e) {
			System.out.println("Erro em AitReportServices > getNAIPrimeiraVia()");
			e.printStackTrace();
			if(isConnectionNull)
				connect.rollback();
			throw new AitReportErrorException("Erro ao gerar NAI: " + e.getMessage());
		}
		catch (Exception e) {
			System.out.println("Erro em AitReportServices > getNAIPrimeiraVia()");
			e.printStackTrace();
			if(isConnectionNull)
				connect.rollback();
			throw new ValidacaoException("Erro ao gerar NAI: " + e.getMessage());
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
    
	public static void setarDataEmissao (ResultSetMap rsm){
		if(rsm.next()) {
			rsm.setValueToField("DT_EMISSAO", Util.formatDate(Util.getDataAtual(), "dd/MM/yyyy"));
		}
		rsm.beforeFirst();
	}
	
	public static byte[] getNAISegundaVia (int cdAit) throws Exception {
		return getNAISegundaVia (cdAit, null);
	}
	
	public static byte[] getNAISegundaVia (int cdAit, Connection connect) throws Exception {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		AitReportServicesNai servicesNai = new AitReportServicesNai();
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
		{
			connect = Conexao.conectar();
		}
			
		AitReportServicesDAO aitReportServicesDao = new AitReportServicesDAO(connect);
		
		String reportName = lgBaseAntiga ? "mob/nai_str_v2" : "mob/nai_v2";
		int segundaVia = 1;
			
		ResultSetMap rsm  = aitReportServicesDao.find (getCriteriosNai.segundaVia (cdAit));
			
		if (!rsm.next())
		{
			throw new ValidacaoException ("O AIT não possui todos os dados necessários.");
		} else {
			pegarRenainfStr(cdAit, rsm, connect);
			pegarRenavanStr(cdAit, rsm, connect);
		}
		
		String cpfCnpj = rsm.getString("nr_cpf_cnpj_proprietario");
		validarCpfCnpj(cpfCnpj);
	    
		rsm.beforeFirst();
			
		pegarDataEmissao (rsm, cdAit, AitMovimentoServices.NAI_ENVIADO, connect);
		AitReportValidatorsNAI.tipoEquipamento (rsm);
		servicesNai.pegarDataDefesaSegundaVia (cdAit, rsm, connect);
		servicesNai.labelViaNAI (rsm, "2º Via");

		HashMap<String, Object> paramns = getParamns.getParamnsNAI (connect);
		paramns.put("REPORT_LOCALE", new Locale("pt", "BR"));
		paramns.put ("MOB_IMAGEM_VEICULO", servicesNai.imagemVeiculo(rsm, connect));
		
		int isAr = AitReportValidatorsNAI.verificarAr (paramns);
		AitReportValidatorsNAI.setarTxtCondutor (rsm, isAr, segundaVia, connect);
		
		byte[] print = new byte[] {};
			
		if (rsm != null && rsm.next() && paramns != null)
		{
			print = ReportServices.getPdfReport (reportName, paramns, rsm, connect);
		}
			
		print = gerarPrint(print);
				
		if (isConnectionNull)
		{
			Conexao.desconectar (connect);
		}
			
		return print;
	}
	
	private static void pegarDataEmissao (ResultSetMap rsm, int cdAit, int tpStatus, Connection connect) {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		GregorianCalendar dtEmissao = new GregorianCalendar();
		
		if (lgBaseAntiga) {
			com.tivic.manager.str.AitMovimento movimentoStr = com.tivic.manager.str.AitMovimentoServices.getMovimentoTpStatus(cdAit, tpStatus);
			dtEmissao = movimentoStr.getDtMovimento();
		} else {
			AitMovimento aitMovimento = AitMovimentoServices.getMovimentoTpStatus(cdAit, tpStatus, connect);
			dtEmissao = aitMovimento.getDtMovimento();
		}
		
		if (rsm.next()){
			rsm.setValueToField("DT_MOVIMENTO", Util.formatDate(dtEmissao, "dd/MM/yyyy"));
			rsm.setValueToField("ID_LOTE", Util.formatDate(dtEmissao, "yyMMdd"));
		}
		rsm.beforeFirst();
	}
	
	public static byte[] getNIPPrimeiraVia (int cdAit,  int cdUsuario) throws Exception, ValidacaoException {
		return getNIPPrimeiraVia (cdAit, false, null, cdUsuario);
	}
	
	public static byte[] getNIPPrimeiraVia (int cdAit, boolean lote, Connection connect, int cdUsuario) throws Exception, ValidacaoException {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
			System.out.println("Gerando movimento NIP AIT: " + cdAit);
			boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
			
			AitReportServicesNip servicesNip = new AitReportServicesNip();		
			AitReportServicesDAO aitReportServicesDao = new AitReportServicesDAO(connect);
			ResultSetMap rsm = new ResultSetMap();
			
			rsm  = aitReportServicesDao.find (AitReportGetCriteriosNIP.primeiraVia (cdAit, connect));

			if (!rsm.next() && !lgBaseAntiga) {
				atualizarDadosAit(cdAit, connect);
				rsm  = aitReportServicesDao.find (AitReportGetCriteriosNIP.primeiraVia (cdAit, connect));
				AitReportValidators.validar(cdAit, AitMovimentoServices.NIP_ENVIADA, connect);
			}
			rsm.beforeFirst();

			servicesNip.pegarDataVencimentoAit (cdAit, rsm);
			
			String reportName = nameReportNip (connect);
				
			servicesNip.calcularDesconto (rsm);
			AitReportValidatorsNIP.tipoEquipamento (rsm);
			AitReportValidatorsNIP.labelViaNIP (rsm, null);
			servicesNip.pegarBancoConveniado(connect, rsm);
			
			HashMap<String, Object> paramns = AitReportGetParamns.getParamnsNIP (connect);
			paramns.put("REPORT_LOCALE", new Locale("pt", "BR"));

			AitReportValidatorsNIP.trataDefesaPrevia (rsm, paramns, connect);
			
			if(lgBaseAntiga) {
				inserirIdAitOld (rsm);
				inserirNrRenavamOld (rsm);
			}
			
			AitMovimentoServices.imporPenalidade (cdAit, lote, cdUsuario, connect);

			if (paramns.get("NR_CD_FEBRABAN") != null) {	
				setarDataVencimentoBoleto(rsm);
				servicesNip.gerarCodigoBarras(rsm, paramns, connect);	 
			}
				
			byte[] print = new byte[] {};
			
			if (rsm != null && rsm.next() && paramns != null) {
				gerarQrCodePix(cdAit, rsm, paramns);
				print = ReportServices.getPdfReport (reportName, paramns, rsm, connect);
			}
			
			print = gerarPrint(print);
			
			if (isConnectionNull) {
				connect.commit();
			}
			
			return print;
			
		}
		catch(AitReportErrorException e) {
			System.out.println("Erro em AitReportServices > getNIPPrimeiraVia()");
			e.printStackTrace();
			if(isConnectionNull)
				connect.rollback();
			throw new AitReportErrorException("Erro ao gerar NIP: " + e.getMessage());
		}
		catch (Exception e) {
			System.out.println("Erro em AitReportServices > getNIPPrimeiraVia()");
			e.printStackTrace();
			if(isConnectionNull)
				connect.rollback();
			throw new ValidacaoException("Erro ao gerar NIP: " + e.getMessage());
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	
	}
	
	public static void gerarQrCodePix(int cdAit, ResultSetMap rsm, HashMap<String, Object> paramns) throws Exception {
		int orgaoOptanteRecebimentoViaPix = ParametroServices.getValorOfParametroAsInteger("LG_PIX", 0);
		String tokenAutenticacao = null;
		if(orgaoOptanteRecebimentoViaPix == 1) {
	    	tokenAutenticacao = obterTokenPix();
	    }
		if(tokenAutenticacao != null) {
		    ImagemQrCodePixGenerator generator = new ImagemQrCodePixGenerator();
		    Notificacao notificacao= new Notificacao();
		    GregorianCalendar dataVencimento = new GregorianCalendar();
		    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); 
		    notificacao.setCdAit(cdAit);
		    notificacao.setIdAit(rsm.getString("NR_AIT"));
			dataVencimento.setTime(formato.parse(rsm.getString("DT_VENCIMENTO")));
			notificacao.setDtVencimento(dataVencimento);
			notificacao.setVlMultaComDesconto(rsm.getDouble("VL_MULTA_DESCONTO"));
			notificacao.setNmProprietario(rsm.getString("NM_PROPRIETARIO"));
			notificacao.setNrCpfCnpjProprietario(rsm.getString("NR_CPF_CNPJ_PROPRIETARIO"));
		    generator.registrarArrecadacaoPix(notificacao, rsm.getString("CODIGO_BARRAS_COM_DV"), tokenAutenticacao);
		    if(notificacao.getPixQrCodeImage() != null) {
		    	paramns.put("PIX_QR_CODE_IMAGE", notificacao.getPixQrCodeImage());
		    }		    
		}
	}
	
	private static String obterTokenPix() {
	    try {
	        String email = ManagerConf.getInstance().get("EMAIL_AUTENTICACAO_PIX");
	        String senha = ManagerConf.getInstance().get("SENHA_AUTENTICACAO_PIX");
	        DadosAutenticacaoEnvio dadosEnvio = new DadosAutenticacaoEnvioBuilder()
	            .addEmail(email)
	            .addSenha(senha)
	            .build();
	        AutenticacaoUsuarioPix registro = new AutenticacaoUsuarioPix(dadosEnvio);
	        DadosAutenticacaoRetorno dadosRetorno = registro.autenticar();
	        return dadosRetorno.getToken();
	    } catch (Exception e) {
	        System.err.println("Erro ao obter token Pix: " + e.getMessage());
	        return null; 
	    }
	}
	
	public static byte[] getNIPSegundaVia (int cdAit) throws Exception 
	{
		return getNIPSegundaVia(cdAit, null);
	}
	
	@SuppressWarnings("static-access")
	public static byte[] getNIPSegundaVia(int cdAit, Connection connect) throws Exception {
		boolean isNipAdvertencia;
	    boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
	    boolean isConnectionNull = connect == null;
	    String reportName = lgBaseAntiga ? "mob/nip_str_v2" : "mob/nip";
	    AitReportServicesNip servicesNip = new AitReportServicesNip();
	    AitReportValidatorsNIP validatorsNip = new AitReportValidatorsNIP();

	    if (isConnectionNull) {
	        connect = Conexao.conectar();
	    }

	    AitReportServicesDAO aitReportServicesDao = new AitReportServicesDAO(connect);
	    ResultSetMap rsm = aitReportServicesDao.find(getCriteriosNip.segundaVia(cdAit));
	    
	    if (!rsm.next()) {
	        throw new ValidacaoException("O AIT não possui todos os dados necessários.");
	    } else {
	        pegarRenainfStr(cdAit, rsm, connect);
	        pegarRenavanStr(cdAit, rsm, connect);
	    }

	    String cpfCnpj = rsm.getString("nr_cpf_cnpj_proprietario");
	    validarCpfCnpj(cpfCnpj);

	    rsm.beforeFirst();

	    boolean nipVencida = validatorsNip.verificarVencimento(rsm);
    	if (nipVencida && ParametroServices.getValorOfParametroAsInteger("MOB_CALCULAR_JUROS_NIP", 0) > NAO_CALCULAR_JUROS_NIP) {
	        servicesNip.calcularJuros(rsm, cdAit, connect);
	    } else {
	        setarDataVencimentoBoleto(rsm);
	        servicesNip.calcularDesconto(rsm);
	    }	

	    AitReportValidatorsNIP.tipoEquipamento(rsm);
	    AitReportValidatorsNIP.labelViaNIP(rsm, "2º Via");
	    servicesNip.pegarBancoConveniado(connect, rsm);

	    HashMap<String, Object> paramns = getParamns.getParamnsNIP(connect);
	    paramns.put("REPORT_LOCALE", new Locale("pt", "BR"));
	    AitReportValidatorsNIP.trataDefesaPrevia(rsm, paramns, connect);

	    if (paramns.get("NR_CD_FEBRABAN") != null) {
	        setParametrosPagamentoOld(paramns, lgBaseAntiga, connect);
	        servicesNip.gerarCodigoBarras(rsm, paramns, connect);
	    }

	    setIdLote(rsm);
	    
	    isNipAdvertencia = isPenalidadeAdvertenciaNip(rsm);
	    if (isNipAdvertencia && lgBaseAntiga) {
	    	reportName = "mob/nip_advertencia_str_v2";
	    }

	    byte[] print = new byte[] {};

	    if (rsm != null && rsm.next() && paramns != null) {
	        print = ReportServices.getPdfReport(reportName, paramns, rsm, connect);
	    }

	    print = gerarPrint(print);

	    if (isConnectionNull) {
	        Conexao.desconectar(connect);
	    }

	    return print;
	}

	public static byte[] reimpressaoDadosNip (int cdAit) throws ValidacaoException, DocumentException, IOException, ParseException, JSONException, org.json.JSONException 
	{
		return reimpressaoDadosNip(cdAit, null);
	}
	
	private static  void setIdLote(ResultSetMap rsm) {
		GregorianCalendar dtEmissao = new GregorianCalendar();
		if(rsm.next()) {
			dtEmissao = rsm.getGregorianCalendar("DT_MOVIMENTO");
			rsm.setValueToField("ID_LOTE", Util.formatDate(dtEmissao, "yyMMdd"));
		}
		rsm.beforeFirst();
	}
	
	private static boolean isPenalidadeAdvertenciaNip(ResultSetMap rsm) {
		int NIP_COM_ADVERTENCIA = 1;
		boolean isAdvertencia = false;
	    if (rsm.next()) {
	    	isAdvertencia = rsm.getInt("lg_penalidade_advertencia_nip") == NIP_COM_ADVERTENCIA;
	    }
	    rsm.beforeFirst();
	    return isAdvertencia;
	}
	
	@SuppressWarnings("static-access")
	public static byte[] reimpressaoDadosNip (int cdAit, Connection connect) throws ValidacaoException, DocumentException, IOException, ParseException, JSONException, org.json.JSONException 
	{
		boolean isConnectionNull = connect==null;
		AitReportServicesNip servicesNip = new AitReportServicesNip();
		
		if (isConnectionNull) 
		{
			connect = Conexao.conectar();
		}
		
		AitReportServicesDAO aitReportServicesDao = new AitReportServicesDAO(connect);

		String reportName = nameReportNip (connect);

		ResultSetMap rsm  = aitReportServicesDao.find (getCriteriosNip.segundaVia (cdAit));
		
		servicesNip.calcularDesconto (rsm);
		AitReportValidatorsNIP.tipoEquipamento (rsm);
		AitReportValidatorsNIP.labelViaNIP (rsm, "2º Via");
		servicesNip.pegarBancoConveniado(connect, rsm);

		HashMap<String, Object> paramns = getParamns.getParamnsNIP (connect);
		paramns.put("REPORT_LOCALE", new Locale("pt", "BR"));
		AitReportValidatorsNIP.trataDefesaPrevia (rsm, paramns, connect);

		if (paramns.get("NR_CD_FEBRABAN") != null) 
		{	
			servicesNip.gerarCodigoBarras(rsm, paramns, connect);	 
		}

		byte[] print = new byte[] {};
			
		if (rsm != null && rsm.next() && paramns != null)
		{
			print = ReportServices.getPdfReport (reportName, paramns, rsm, connect);
		}
			
		print = gerarPrint(print);
			
		if (isConnectionNull)
		{
			Conexao.desconectar (connect);
		}
			
		return print;
	}
	
	@SuppressWarnings("static-access")
	public static boolean verificarVencimentoNip(int cdAit) throws ValidacaoException
	{
		AitServices aitServices = new AitServices();
		AitReportGetCriteriosNIP criteriosNip = new AitReportGetCriteriosNIP();
		AitReportValidatorsNIP validatorsNip = new AitReportValidatorsNIP();
		
		ResultSetMap rsm = aitServices.find(criteriosNip.criteriosVencimento(cdAit));
		boolean nipVencida = validatorsNip.verificarVencimento(rsm);
		
		return nipVencida;
	}
	
	
	public static byte[] getCondutor (int cdAit) throws ValidacaoException, SQLException 
	{
		return getCondutor (cdAit, null);
	}
	
	public static byte[] getCondutor (int cdAit, Connection connect) throws ValidacaoException, SQLException 
	{
		boolean isConnectionNull = connect==null;
		
		try 
		{
			if (isConnectionNull) 
				connect = Conexao.conectar();
			
			String reportName = "mob/condutor";
			
			ResultSetMap rsm  = AitServices.find (getCriteriosCondutor.getCriteriosCondutor (cdAit), connect);

			HashMap<String, Object> paramns = getParamns.getParamnsCondutor (connect);
			paramns.put("REPORT_LOCALE", new Locale("pt", "BR"));
		
			byte[] print = new byte[] {};

			if (rsm != null && rsm.next() && paramns != null)
				print = ReportServices.getPdfReport (reportName, paramns, rsm);			
				
			return print;
		}
		finally 
		{
			if (isConnectionNull)
				Conexao.desconectar (connect);
		}
	}
	
	public static byte[] getAitReport (Criterios criterios, Connection connection) throws Exception 
	{
		boolean isConnNull = connection == null;
		
		try 
		{			
			if (isConnNull)
				connection = Conexao.conectar();
			
			AitReportServicesDAO aitReportServicesDao = new AitReportServicesDAO(connection);
			
			ResultSetMap rsm = aitReportServicesDao.find (criterios);
			if (!rsm.next())
				throw new EmptyListException ("Nenhum AIT encontrado para os critérios indicados.");
			
			rsm.beforeFirst();
			
			while (rsm.next()) 
			{
				Infracao infracao = InfracaoDAO.get (rsm.getInt ("cd_infracao"));
				if(infracao != null) {
					String dsInfracao = infracao.getNrCodDetran()+" - "+infracao.getDsInfracao();
					rsm.setValueToField("DS_INFRACAO", dsInfracao);
				}		
				
				rsm.setValueToField("DS_DT_INFRACAO", Util.formatDate(rsm.getGregorianCalendar("dt_infracao"), "dd/MM/yyyy 'às' HH:mm"));
				
				AitMovimento movimento = AitMovimentoDAO.get(rsm.getInt("cd_movimento_atual"), rsm.getInt("cd_ait"));
				if(movimento != null) {
					StringBuilder dsMovimento = new StringBuilder();
					dsMovimento.append(Util.formatDate(movimento.getDtMovimento(), "dd/MM/yyyy"));
					dsMovimento.append(" - ");
					dsMovimento.append(TipoStatusEnum.valueOf(movimento.getTpStatus()));
					
					rsm.setValueToField("DS_ULTIMO_MOVIMENTO", dsMovimento.toString());
				}
				
			}
			rsm.beforeFirst();
			
			HashMap<String, Object> params =  new HashMap<String, Object>();			
			params.put("DS_TITULO_1", ParametroServices.getValorOfParametroAsString("DS_TITULO_1", "PREFEITURA MUNICIPAL DE TEÓFILO OTONI"));
			params.put("DS_TITULO_2", ParametroServices.getValorOfParametroAsString("DS_TITULO_2", "SECRETARIA MUNICIPAL DE PLANEJAMENTO"));
			params.put("DS_TITULO_3", ParametroServices.getValorOfParametroAsString("DS_TITULO_3", "DIVISÃO DE TRÂNSITO E TRANSPORTE"));
			params.put("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
			params.put("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
			params.put("DS_EMISSAO", "Emitido em "+Util.formatDate(new GregorianCalendar(), "dd/MM/yyyy 'às' HH:mm"));
			
			byte[] print = ReportServices.getPdfReport("mob/relatorio_ait", params, rsm);	
			
			return print;
		} catch(Exception ex) {
			System.out.println("Erro! AitReportServices.getAitReport");
			ex.printStackTrace(System.out);
			throw ex;
		} finally {
			if(isConnNull)
				Conexao.desconectar(connection);
		}
	}
	
	public static AitFindEmissao findeEmissao(String idAit) {
		return findeEmissao(idAit, null);
	}
	
	public static AitFindEmissao findeEmissao(String idAit, Connection connect) {
		AitFindEmissao ait = new AitFindEmissao();
		
		boolean isConnectionNull = connect==null;
		
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
			
			String sql =  "SELECT A.* ,B.nm_agente, C.ds_infracao, D.tp_status AS movimentoAtual " 
						+ "FROM mob_ait A "
						+ "INNER JOIN mob_agente B ON (A.cd_agente = B.cd_agente) "
						+ "INNER JOIN mob_infracao C ON (A.cd_infracao = C.cd_infracao) "
						+ "INNER JOIN mob_ait_movimento D ON (A.cd_movimento_atual = D.cd_movimento) "
						+ "WHERE id_ait = ? limit 1";
			
			PreparedStatement ps;
			ps = connect.prepareStatement(sql);
			ps.setString(1, idAit);
			ResultSetMap rsm = new ResultSetMap(ps.executeQuery());
			
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			DecimalFormat df = new DecimalFormat("#,###.00");
			
			while(rsm.next())
			{
				ait.setCdAit(rsm.getInt("CD_AIT"));
				ait.setIdAit(rsm.getString("ID_AIT"));
				ait.setLgAutoAssinado(rsm.getInt("lg_auto_assinado"));
				ait.setNmAgente(rsm.getString("NM_AGENTE"));
				ait.setDtInfracao(dateFormat.format(rsm.getTimestamp("DT_INFRACAO")));
				ait.setNrPlaca(rsm.getString("NR_PLACA"));
				ait.setDsInfracao(rsm.getString("DS_INFRACAO"));
				ait.setVlMulta(df.format(rsm.getDouble("VL_MULTA")));
				ait.setDsLocalInfracao(rsm.getString("DS_LOCAL_INFRACAO"));
				ait.setDsPontoReferencia(rsm.getString("DS_PONTO_REFERENCIA"));
				ait.setDsUltimoMovimento(rsm.getInt("movimentoAtual"));
			}
			rsm.beforeFirst();
		
		return ait;
	}
		catch (Exception e) 
		{
			e.printStackTrace();
			return null;
		}
		finally 
		{
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private static void inserirIdAitOld (ResultSetMap rsm)
	{
		String idAit = "";
		if(rsm.next())
		{
			idAit = rsm.getString("nr_ait");
			rsm.setValueToField("id_ait", idAit);
		}
		rsm.beforeFirst();
	}
	
	private static void inserirNrRenavamOld (ResultSetMap rsm)
	{
		String nrRenavam = "";
		if(rsm.next())
		{
			nrRenavam = rsm.getString("cd_renavan");
			rsm.setValueToField("NR_RENAVAN", nrRenavam);
		}
		rsm.beforeFirst();
	}
	
	public static String nameReportNip (Connection connect){
		String reportName = "";
		int localImpressao = LoteImpressaoTipo.IMPRESAO_LOCAL;
		localImpressao = ParametroServices.getValorOfParametroAsInteger("mob_local_impressao", 0);
		
		switch(localImpressao) {
			case LoteImpressaoTipo.IMPRESAO_LOCAL:
				reportName = "mob/nip";
				break;
			case LoteImpressaoTipo.IMPRESSAO_EXTERNO:
				reportName = "mob/nip";
				break;
			case LoteImpressaoTipo.IMPRESSAO_ECARTA:
				reportName = "mob/nip-ecarta";
				break;
			default:
				reportName = "mob/nip";		
		}
		return reportName;
	}
	
	public static String nameReportNic (Connection connect) {
		String reportName = "";
		int localImpressao = LoteImpressaoTipo.IMPRESAO_LOCAL;
		localImpressao = ParametroServices.getValorOfParametroAsInteger("mob_local_impressao", 0);
		switch(localImpressao) {
			case LoteImpressaoTipo.IMPRESAO_LOCAL:
				reportName = "mob/nic";
				break;
			case LoteImpressaoTipo.IMPRESSAO_EXTERNO:
				reportName = "mob/nic";
				break;
			case LoteImpressaoTipo.IMPRESSAO_ECARTA:
				reportName = "mob/nic-ecarta";
				break;
			default:
				reportName = "mob/nic";		
		}
		return reportName;
	}
	
	protected  byte[] getNICPrimeiraVia (int cdAit,  int cdUsuario) throws Exception, ValidacaoException
	{
		return getNICPrimeiraVia (cdAit, false, null, cdUsuario);
	}
	
	@SuppressWarnings("static-access")
	protected byte[] getNICPrimeiraVia (int cdAit, boolean isLote, Connection connect, int cdUsuario) throws Exception, ValidacaoException
	{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		boolean isConnectionNull = connect==null;
		
		AitReportServicesNic nicServices = new AitReportServicesNic();
		AitReportServicesNip servicesNip = new AitReportServicesNip();
		AitReportValidatorsNic validatorsNic = new AitReportValidatorsNic();
		
		if (isConnectionNull)
		{
			connect = Conexao.conectar();
		}
		
		AitReportServicesDAO aitReportServicesDao = new AitReportServicesDAO(connect);
		
		String reportName = nameReportNic (connect);
		ResultSetMap rsm = new ResultSetMap();

		rsm  = aitReportServicesDao.find (AitReportGetCriteriosNic.primeiraVia (cdAit, isLote, connect));
		
		validatorsNic.isNic(cdAit, connect);
		validatorsNic.verificarRegistroAIT(cdAit, connect);
		
		if (!rsm.next() && !lgBaseAntiga)
		{
			atualizarDadosAit(cdAit, connect);
			rsm  = AitServices.find (AitReportGetCriteriosNic.primeiraVia (cdAit, isLote, connect), connect);
		}
		rsm.beforeFirst();
		
		servicesNip.pegarDataVencimentoAit(cdAit, rsm, connect);
		
		setarDataEmissao(rsm);
		servicesNip.calcularDesconto (rsm);
		AitReportValidatorsNIP.labelViaNIP (rsm, null);
		servicesNip.pegarBancoConveniado(connect, rsm);
		
		HashMap<String, Object> paramns = getParamns.getParamnsNic(connect);
		paramns.put("REPORT_LOCALE", new Locale("pt", "BR"));
		
		if(lgBaseAntiga)
		{
			inserirIdAitOld (rsm);
			inserirNrRenavamOld (rsm);
		}
		
		if (paramns.get("NR_CD_FEBRABAN") != null) 
		{	
			servicesNip.gerarCodigoBarras(rsm, paramns, connect);	 
		}
		
		nicServices.imporPenalidade(cdAit, isLote, cdUsuario, connect);
			
		byte[] print = new byte[] {};
		
		if (rsm != null && rsm.next() && paramns != null)
		{
			print = ReportServices.getPdfReport (reportName, paramns, rsm, connect);
		}
		
		print = gerarPrint(print);
			
		if (isConnectionNull)
		{
			Conexao.desconectar (connect);
		}
				
		return print;
	}
	
	protected byte[] getNICSegundaVia (int cdAit) throws Exception, ValidacaoException
	{
		return getNICSegundaVia (cdAit, false, null);
	}
	
	@SuppressWarnings("static-access")
	protected  byte[] getNICSegundaVia (int cdAit, boolean isLote, Connection connect) throws Exception, ValidacaoException
	{
		boolean isConnectionNull = connect==null;
		
		AitReportValidatorsNIP movimentoNipEmNic = new AitReportValidatorsNIP();
		AitReportServicesNip servicesNip = new AitReportServicesNip();
		AitMovimentoServices movimentoServices = new AitMovimentoServices();
		AitReportGetCriteriosNic criteriosNic = new AitReportGetCriteriosNic();
		
		if (isConnectionNull)
		{
			connect = Conexao.conectar();
		}
		
		AitReportServicesDAO aitReportServicesDao = new AitReportServicesDAO(connect);
		
		String reportName = nameReportNic(connect);
		ResultSetMap rsm  = aitReportServicesDao.find(criteriosNic.segundaVia (cdAit));
		boolean nicVencida = movimentoNipEmNic.verificarVencimento(rsm);
		
		if (nicVencida)
		{		
			servicesNip.calcularJuros(rsm, cdAit, connect);
		}
		else
		{
			servicesNip.calcularDesconto(rsm);
		}
		pegarDataEmissao (rsm, cdAit, movimentoServices.NIP_ENVIADA, connect);

		movimentoNipEmNic.labelViaNIP (rsm, "2º Via");
		servicesNip.pegarBancoConveniado(connect, rsm);
		HashMap<String, Object> paramns = getParamns.getParamnsNic(connect);
		paramns.put("REPORT_LOCALE", new Locale("pt", "BR"));
		
		if (paramns.get("NR_CD_FEBRABAN") != null) 
		{	
			servicesNip.gerarCodigoBarras(rsm, paramns, connect);	 
		}
			
		byte[] print = new byte[] {};
		
		if (rsm != null && rsm.next() && paramns != null)
		{
			print = ReportServices.getPdfReport (reportName, paramns, rsm, connect);
		}
		
		print = gerarPrint(print);
			
		if (isConnectionNull)
		{
			Conexao.desconectar (connect);
		}
				
		return print;
	}
	
	public static byte[] reimpressaoDadosNic (int cdAit ) throws ValidacaoException, DocumentException, IOException, ParseException, JSONException, org.json.JSONException, SQLException 
	{
		return reimpressaoDadosNic(cdAit, false, null);
	}
	
	@SuppressWarnings("static-access")
	public static byte[] reimpressaoDadosNic (int cdAit, boolean isLote, Connection connect) throws ValidacaoException, DocumentException, IOException, ParseException, JSONException, org.json.JSONException, SQLException 
	{
		boolean isConnectionNull = connect==null;
		
		AitReportServicesNip servicesNip = new AitReportServicesNip();
		AitMovimentoServices movimentoServices = new AitMovimentoServices();
		AitReportGetCriteriosNic criteriosNic = new AitReportGetCriteriosNic();
		AitReportServicesDAO aitReportServicesDao = new AitReportServicesDAO(connect);
		
		if (isConnectionNull)
		{
			connect = Conexao.conectar();
		}
		
		String reportName = nameReportNic(connect);
		ResultSetMap rsm  =  aitReportServicesDao.find(criteriosNic.segundaVia (cdAit));	
		pegarDataEmissao (rsm, cdAit, movimentoServices.NIP_ENVIADA, connect);
		HashMap<String, Object> paramns = getParamns.getParamnsNic(connect);
		paramns.put("REPORT_LOCALE", new Locale("pt", "BR"));
		servicesNip.pegarBancoConveniado(connect, rsm);
		
		if (paramns.get("NR_CD_FEBRABAN") != null) 
		{	
			servicesNip.gerarCodigoBarras(rsm, paramns, connect);	 
		}
			
		byte[] print = new byte[] {};

		if (rsm != null && rsm.next() && paramns != null)
		{
			print = ReportServices.getPdfReport (reportName, paramns, rsm, connect);
		}
		
		print = gerarPrint(print);
			
		if (isConnectionNull)
		{
			Conexao.desconectar (connect);
		}
				
		return print;
	}
	
	public static byte[] getImpressaoAit(int cdAit) throws Exception {
		return getImpressaoAit(cdAit, null);
	}
	
	public static byte[] getImpressaoAit(int cdAit, Connection connect) throws Exception {
		boolean isConnectionNull = (connect == null);
		try {			
			HashMap<String, Object> params = new HashMap<String, Object>();
			
			if (isConnectionNull)
				connect = Conexao.conectar();
			
			String reportName = REPORT_AIT_SEGUNDA_VIA;
			Ait _ait = AitDAO.get(cdAit);
			ResultSetMap rsm  =  getMovimentosImpressaoAit(_ait);
			ResultSetMap imagensReport = new ResultSetMap();
			ResultSetMap dummyRsm = buildDummyRsm();
			
			params.put("cdAit", cdAit);
			
			setMovimentosReport(rsm, params);
			setImagensReport(imagensReport, params);
			setParamsAit(_ait, params);
			setParamsCondutor(_ait, params);
			setParamsVeiculo(_ait, params);	
			setEtiquetasReport(_ait, params);
			setPagamentoReport(_ait, params);
			setParamsReport(params, connect);
			
			byte[] print = ReportServices.getPdfReport(reportName, params, dummyRsm, connect);
			
			return print;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar (connect);
		}
	}
	
	private static ResultSetMap getMovimentosImpressaoAit(Ait _ait) {
		Criterios crt = new Criterios();
		crt.add("B.id_ait", _ait.getIdAit().toUpperCase(), ItemComparator.EQUAL, Types.VARCHAR);
		return AitMovimentoServices.find(crt);
	}
	
	private static void setMovimentosReport(ResultSetMap rsm, HashMap<String, Object> params) {
		while(rsm.next()) {
			Usuario usuario = UsuarioDAO.get(rsm.getInt("CD_USUARIO"));
			
			if(usuario != null)
				rsm.setValueToField("NM_USUARIO", valueNotNull(usuario.getNmLogin()));
			else
				rsm.setValueToField("NM_USUARIO", "Não Informado");	
			
			Criterios crtArquivos = new Criterios();
			crtArquivos.add("AIT_MOV.cd_movimento", String.valueOf(rsm.getInt("CD_MOVIMENTO")), ItemComparator.EQUAL, Types.INTEGER);
			crtArquivos.add("AIT_MOV.cd_ait", String.valueOf(rsm.getInt("CD_AIT")), ItemComparator.EQUAL, Types.INTEGER);
						
			ResultSetMap arquivos = ArquivoMovimentoServices.find(crtArquivos);
			if(arquivos.next()) {
				rsm.setValueToField("QTD_ARQUIVOS", arquivos.getLines().size() > 1 ? arquivos.getLines().size() + " Arquivos" : arquivos.getLines().size() + " Arquivo");
				arquivos.last();

				ErroRetorno retorno = ErroRetornoDAO.get(arquivos.getString("nr_erro"));
				if(retorno != null)
					rsm.setValueToField("DS_ERRO", valueNotNull(retorno.getDsErro()));
				else
					rsm.setValueToField("DS_ERRO", "Movimento enviado");
				
			} else {
				rsm.setValueToField("DS_ERRO", "Aguardando Envio");
				rsm.setValueToField("QTD_ARQUIVOS", "Nenhum arquivo");
			}
			
			rsm.setValueToField("TP_STATUS", TipoStatusEnum.valueOf(rsm.getInt("TP_STATUS")));
			rsm.setValueToField("DT_MOVIMENTO", rsm.getGregorianCalendar("DT_MOVIMENTO") != null ? Util.formatDate(rsm.getGregorianCalendar("DT_MOVIMENTO"), "dd/MM/yyyy") : "Não Informado");
			rsm.setValueToField("DT_REGISTRO_DETRAN", rsm.getGregorianCalendar("DT_REGISTRO_DETRAN") != null ? Util.formatDate(rsm.getGregorianCalendar("DT_REGISTRO_DETRAN"), "dd/MM/yyyy") : "Não Informado");
			rsm.setValueToField("DS_ENVIADO_DETRAN", TipoSituacaoAitEnum.valueOf(rsm.getInt("LG_ENVIADO_DETRAN")));		
			rsm.setValueToField("DT_PUBLICACAO_DO", rsm.getGregorianCalendar("DT_PUBLICACAO_DO") != null ? Util.formatDate(rsm.getGregorianCalendar("DT_PUBLICACAO_DO"), "dd/MM/yyyy") : "Não Publicado");

		}
		
		rsm.beforeFirst();			
		
		params.put("DATASET_MOVIMENTOS_AIT", new JRBeanCollectionDataSource(rsm.getLines()));
	}
	
	private static ResultSetMap getPagamentos(Ait ait) {
		Criterios crt = new Criterios();
		crt.add("cd_ait", String.valueOf(ait.getCdAit()), ItemComparator.EQUAL, Types.VARCHAR);
		return AitPagamentoDAO.find(crt);
	}
	
	private static void setPagamentoReport(Ait ait, HashMap<String, Object> params) {
		ResultSetMap rsmPagamentos = getPagamentos(ait);
		while(rsmPagamentos.next()) {
			String nrBancoString = rsmPagamentos.getString("NR_BANCO");
			int nrBanco = (nrBancoString != null) ? Integer.parseInt(nrBancoString) : 0;
			rsmPagamentos.setValueToField("NR_BANCO", (nrBanco != 0) ? BancoEnum.valueOf(nrBanco) : "Não Informado");
			rsmPagamentos.setValueToField("NR_AGENCIA", rsmPagamentos.getString("NR_AGENCIA") != null ? rsmPagamentos.getString("NR_AGENCIA") : "Não Informado");
			rsmPagamentos.setValueToField("DT_PAGAMENTO", rsmPagamentos.getGregorianCalendar("DT_PAGAMENTO") != null ? Util.formatDate(rsmPagamentos.getGregorianCalendar("DT_PAGAMENTO"), "dd/MM/yyyy") : "Não Informado");
			rsmPagamentos.setValueToField("DT_CREDITO", rsmPagamentos.getGregorianCalendar("DT_CREDITO") != null ? Util.formatDate(rsmPagamentos.getGregorianCalendar("DT_CREDITO"), "dd/MM/yyyy") : "Não Informado");
			rsmPagamentos.setValueToField("NR_CONTA_CREDITO", rsmPagamentos.getString("NR_CONTA_CREDITO") != null ? rsmPagamentos.getString("NR_CONTA_CREDITO") : "Não Informado");
			rsmPagamentos.setValueToField("NR_AGENCIA_CREDITO", rsmPagamentos.getString("NR_AGENCIA_CREDITO") != null ? rsmPagamentos.getString("NR_AGENCIA_CREDITO") : "Não Informado");
			Double vlPago = rsmPagamentos.getDouble("VL_PAGO");
			rsmPagamentos.setValueToField("VL_PAGO", vlPago != null ? vlPago : Double.valueOf("Não Informado"));
			rsmPagamentos.setValueToField("CD_ARQUIVO", rsmPagamentos.getInt("CD_ARQUIVO"));
		}
		rsmPagamentos.beforeFirst();
		params.put("DATASET_AIT_PAGAMENTO", new JRBeanCollectionDataSource(rsmPagamentos.getLines()));
		
	}
	
	private static ResultSetMap getEtiquetasImpressaoAit(Ait _ait) {
		Criterios crt = new Criterios();
		crt.add("A.cd_ait", String.valueOf(_ait.getCdAit()), ItemComparator.EQUAL, Types.VARCHAR);
		return CorreiosEtiquetaDAO.find(crt);
	}
	
	private static void setEtiquetasReport(Ait _ait, HashMap<String, Object> params) {
		ResultSetMap rsmCorreiosEtiqueta = getEtiquetasImpressaoAit(_ait);
		while(rsmCorreiosEtiqueta.next()) {
			rsmCorreiosEtiqueta.setValueToField("TP_STATUS", TipoStatusEnum.valueOf(rsmCorreiosEtiqueta.getInt("TP_STATUS")));
			rsmCorreiosEtiqueta.setValueToField("DT_ENVIO", rsmCorreiosEtiqueta.getGregorianCalendar("DT_ENVIO") != null ? Util.formatDate(rsmCorreiosEtiqueta.getGregorianCalendar("DT_ENVIO"), "dd/MM/yyyy") : "Não Informado");
			rsmCorreiosEtiqueta.setValueToField("SG_SERVICO", rsmCorreiosEtiqueta.getString("SG_SERVICO"));
			rsmCorreiosEtiqueta.setValueToField("NR_DIGITO_VERIFICADOR", String.valueOf(gerarDigitoVerificador(rsmCorreiosEtiqueta.getInt("NR_ETIQUETA"))));
		}
		rsmCorreiosEtiqueta.beforeFirst();
		params.put("DATASET_ETIQUETAS_AIT", new JRBeanCollectionDataSource(rsmCorreiosEtiqueta.getLines()));
	}
	
	private static int gerarDigitoVerificador(int numeroEtiqueta) {
		String numero = Integer.toString(numeroEtiqueta);
		String retorno = numero;
		String dv;
		Integer[] multiplicadores = { 8, 6, 4, 2, 3, 5, 9, 7 };
		Integer soma = 0;
		if (numero.length() < 8) {
			String zeros = "";
			int diferenca = 8 - numero.length();
			for (int i = 0; i < diferenca; i++) {
				zeros += "0";
			}
			retorno = zeros + numero;
		} else {
			retorno = numero.substring(0, 8);
		}

		for (int i = 0; i < 8; i++) {
			soma += new Integer(retorno.substring(i, (i + 1))) * multiplicadores[i];
		}
		Integer resto = soma % 11;
		if (resto == 0) {
			dv = "5";
		} else if (resto == 1) {
			dv = "0";
		} else {
			dv = new Integer(11 - resto).toString();
		}
		retorno += dv;
		return Integer.parseInt(dv);
	}
	
	
	private static void setImagensReport(ResultSetMap imagensReport, HashMap<String, Object> params) {
		ResultSetMap imagens = AitImagemServices.getFromAit((int) params.get("cdAit"));
		
		
		ArrayList<HashMap<String, Object>> _imagens = new ArrayList<>();
		
		while(imagens.next()) {
			_imagens.add(identificarFormatoVideo((byte[])imagens.getObject("BLB_IMAGEM")));
		}
		
		imagensReport.setLines(_imagens);
		imagensReport.beforeFirst();
		
		
		if(imagensReport.next()) {
			imagensReport.beforeFirst();
			params.put("DATASET_IMAGENS_AIT", new JRBeanCollectionDataSource(imagensReport.getLines()));
		} else {
			params.put("DATASET_IMAGENS_AIT", null);
		}
	}
	
	public static HashMap<String, Object> identificarFormatoVideo(byte[] fileBytes) {
        if (fileBytes.length < 9) return null;

        String primeiraAssinatura = bytesToHex(Arrays.copyOfRange(fileBytes, 0, 4));
        String segundaAssinatura = bytesToHex(Arrays.copyOfRange(fileBytes, 4, 8));

        if (VIDEO_ASSINATURAS.containsKey(primeiraAssinatura)) {
        	VIDEO_ASSINATURAS.get(primeiraAssinatura);
            return null;
        }
        if (VIDEO_ASSINATURAS.containsKey(segundaAssinatura)) {
        	VIDEO_ASSINATURAS.get(segundaAssinatura);
            return null;
        }
        HashMap<String, Object> _imagem = new HashMap<>();
        _imagem.put("BLB_IMAGEM", Base64.getEncoder().encodeToString(fileBytes));
        return _imagem;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }
	
	private static void setParamsReport(HashMap<String, Object> params, Connection connect) throws ValidacaoException {
		ConfManager conf = ManagerConf.getInstance();
		String reportPath = conf.getProps().getProperty("REPORT_PATH");
		String path = ContextManager.getRealPath()+"/"+reportPath;
		ArrayList<String> reportNames = new ArrayList<>();
		reportNames.add("mob//impressao_ait_subreport");
		reportNames.add("mob//impressao_ait_movimentos_subreport");
		reportNames.add("mob//impressao_ait_pagamento_subreport");
		reportNames.add("mob//impressao_ait_etiquetas_subreport");
		
		params.put("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO", connect));
		params.put("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA", connect));
		params.put("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1", connect));
		params.put("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2", connect));
		params.put("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3", connect));
		params.put("SUBREPORT_DIR", path+"//mob");
		params.put("REPORT_LOCALE", new Locale("pt", "BR"));		
		params.put("SUBREPORT_NAMES", reportNames);
	}
	
	private static void setParamsAit(Ait ait, HashMap<String, Object> params) {		
		try {
			Agente _agente = AgenteDAO.get(ait.getCdAgente());
			Infracao _infracao = InfracaoDAO.get(ait.getCdInfracao());
			AitMovimento _movimento = AitMovimentoDAO.get(ait.getCdMovimentoAtual(), ait.getCdAit());
			
			params.put("ID_AIT", valueNotNull(ait.getIdAit()));
			params.put("NR_CONTROLE", valueNotNull(ait.getNrControle()));
			params.put("DS_AUTO_ASSINADO", ait.getLgAutoAssinado() == 1 ? "Sim" : "Não");
			params.put("NR_MATRICULA", valueNotNull(_agente.getNrMatricula()));
			params.put("DT_INFRACAO", valueNotNull(Util.formatDate(ait.getDtInfracao(), "dd/MM/yyyy")));
			params.put("NR_PLACA", valueNotNull(ait.getNrPlaca()));
			params.put("NR_COD_DETRAN", valueNotNull(_infracao.getNrCodDetran()));
			params.put("DS_INFRACAO", valueNotNull(_infracao.getDsInfracao()));
			params.put("NR_ARTIGO", valueNotNull(_infracao.getNrArtigo()));
			params.put("NR_INCISO", valueNotNull(_infracao.getNrInciso()));
			params.put("NR_PARAGRAFO", valueNotNull(_infracao.getNrParagrafo()));
			params.put("NR_ALINEA", valueNotNull(_infracao.getNrAlinea()));
			params.put("NM_NATUREZA", valueNotNull(_infracao.getNmNatureza()));
			params.put("NR_PONTUACAO", valueNotNull(_infracao.getNrPontuacao()));
			params.put("VL_MULTA", valueNotNull(_infracao.getVlInfracao()));
			params.put("VL_VELOCIDADE_PERMITIDA", ait.getVlVelocidadePermitida());
			params.put("VL_VELOCIDADE_AFERIDA", ait.getVlVelocidadeAferida());
			params.put("DS_SITUACAO_ATUAL", TipoStatusEnum.valueOf(_movimento.getTpStatus()));
			params.put("DS_LOCAL_INFRACAO", valueNotNull(ait.getDsLocalInfracao()));
			params.put("DS_PONTO_REFERENCIA", valueNotNull(ait.getDsPontoReferencia()));
			params.put("DS_OBSERVACAO", valueNotNull(ait.getDsObservacao()));
			params.put("DT_HR_INFRACAO", valueNotNull(Util.formatDate(ait.getDtInfracao(), "dd/MM/yyyy HH:mm")));
			params.put("NR_DOCUMENTO_AUTUACAO", ait.getNrDocumentoAutuacao());
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
		}
	}
	
	private static void setParamsCondutor(Ait ait, HashMap<String, Object> params) {
		try {
			Cidade _cidade = CidadeDAO.get(ait.getCdCidadeCondutor());
			Estado _estado = new Estado();
			if(_cidade != null) {
				_estado = EstadoDAO.get(_cidade.getCdEstado());
				params.put("NM_CIDADE", _cidade.getNmCidade());
				params.put("SG_UF", _estado.getSgEstado());
			} else {
				params.put("NM_CIDADE", "Não informado");
				params.put("SG_UF", "Não informado");
			}
			
			params.put("DS_TP_DOCUMENTO", TipoDocumentoEnum.valueOf(ait.getTpDocumento() > 0 ? ait.getTpDocumento() : TipoDocumentoEnum.TP_DOCUMENTO_NAO_APRESENTOU.getKey()));
			params.put("NR_DOCUMENTO", valueNotNull(ait.getNrDocumento()));
			params.put("NR_CPF_CONDUTOR", ait.getNrCpfCondutor());
			params.put("DS_TP_CNH", TipoCnhEnum.valueOf(ait.getTpCnhCondutor() > 0 ? ait.getTpCnhCondutor() : TipoCnhEnum.NAO_INFORMADO.getKey()));
			params.put("NR_CNH_CONDUTOR", valueNotNull(ait.getNrCnhCondutor() != null ? valueNotNull(ait.getNrCnhCondutor().equals("") ? null : ait.getNrCnhCondutor()) : "Não informado"));
			params.put("UF_CNH_CONDUTOR", valueNotNull(ait.getUfCnhCondutor()));
			params.put("NM_CONDUTOR", ait.getNmCondutor() != null ? valueNotNull(ait.getNmCondutor().equals("") ? null : ait.getNmCondutor()) : "Não informado");
			params.put("DS_ENDERECO_CONDUTOR", valueNotNull(ait.getDsEnderecoCondutor()));
			params.put("NR_ENDERECO", valueNotNull(ait.getNrImovelCondutor()));
			params.put("DS_COMPLEMENTO", valueNotNull(ait.getDsComplementoCondutor()));
			params.put("DS_BAIRRO", valueNotNull(ait.getDsBairroCondutor()));
			params.put("NR_CEP", valueNotNull(ait.getNrCepCondutor()));
			
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
		}
	}
	
	private static void setParamsVeiculo(Ait ait, HashMap<String, Object> params) {
		try {
			MarcaModelo _marcaModelo = MarcaModeloDAO.get(ait.getCdMarcaVeiculo());
			EspecieVeiculo _especie = EspecieVeiculoDAO.get(ait.getCdEspecieVeiculo());
			Cor _cor = CorDAO.get(ait.getCdCorVeiculo());
			Cidade _cidade = CidadeDAO.get(ait.getCdCidade());
			Estado _estado = new Estado();
			if(_cidade != null)
				_estado = EstadoDAO.get(_cidade.getCdEstado());
			
			if(_cidade != null) {
				params.put("NM_CIDADE_PROPRIETARIO", valueNotNull(_cidade.getNmCidade()));
				params.put("SG_UF_PROPRIETARIO", valueNotNull(_estado.getSgEstado()));
			} else {
				params.put("NM_CIDADE_PROPRIETARIO", "Não informado");
				params.put("SG_UF_PROPRIETARIO", "Não informado");
			}
			
			if(_especie != null)
				params.put("DS_TP_VEICULO", valueNotNull(_especie.getDsEspecie()));
			else
				params.put("DS_TP_VEICULO", "Não informado");
			
			if(_marcaModelo != null) {
				params.put("NM_MARCA_MODELO", valueNotNull(_marcaModelo.getNmModelo()));
			} else {
				params.put("NM_MARCA_MODELO", "Não informado");
			}
			
			if(_cor != null)
				params.put("NM_COR", valueNotNull(_cor.getNmCor()));
			else
				params.put("NM_COR", "Não informado");
			
			params.put("ANO_FABRICACAO", valueNotNull(ait.getNrAnoFabricacao()));
			params.put("ANO_MODELO", valueNotNull(ait.getNrAnoModelo()));
			params.put("NM_PROPRIETARIO", valueNotNull(ait.getNmProprietario()));
			params.put("NR_CPF_CNPJ_PROPRIETARIO", ait.getNrCpfCnpjProprietario());
			params.put("NR_RENAVAM", ait.getNrRenavan());
			
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
		}
	}
	
	private static ResultSetMap buildDummyRsm() {
		HashMap<String, Object> line = new HashMap<>();
		ArrayList<HashMap<String, Object>> hashLines = new ArrayList<>();
		ResultSetMap lines = new ResultSetMap();
		
		line.put("", null);
		hashLines.add(line);			
		lines.setLines(hashLines);
		
		return lines;
	}
	
	private static Object valueNotNull(Object obj) {
		if(obj == null || obj.toString().trim().equals(""))
			return "Não informado";
		
		return obj;
	}

	protected static byte[] getNaiPandemia(int cdAit, int cdUsuario) throws Exception
	{
		return  getNaiPandemia(cdAit, null, cdUsuario);
	}
	
	@SuppressWarnings({ "static-access", "unused" })
	protected static byte[] getNaiPandemia(int cdAit, Connection connect, int cdUsuario) throws Exception
	{	
		Ait ait = AitDAO.get(cdAit, connect);
		AitMovimentoServices movimentoServices = new AitMovimentoServices();
		AitReportServicesNai reabrirPrazoNai = new AitReportServicesNai();
		AitReportGetCriteriosNAI criteriosNai = new AitReportGetCriteriosNAI();
		AitReportServicesDAO aitReportServicesDao = new AitReportServicesDAO(connect);
		
		boolean isConnectionNull = connect==null;	
		
		if (isConnectionNull) 
		{
			connect = Conexao.conectar();
		}

		ResultSetMap rsm = aitReportServicesDao.find (criteriosNai.primeiraVia (cdAit, connect, false));	
		AitMovimento movimento = movimentoServices.getMovimentoTpStatus(cdAit, movimentoServices.REGISTRO_INFRACAO);
		reabrirPrazoNai.reaberturaPrazoNai(ait, movimento);
		
		return getNAIPrimeiraVia (cdAit, connect,cdUsuario, false);
	}
	
	protected static void atualizarDadosAit( int cdAit, Connection connect) throws Exception {
		Ait ait = AitDAO.get(cdAit, connect);
		Result r = AitServices.updateDetran(ait, connect);
		if (r.getCode() < 0){
			throw new ValidacaoException ("Não foi possiveu atualizado os dados do AIT: " + cdAit);
		}
	}
	
	
	private static byte[] gerarPrint(byte[] print) throws DocumentException, IOException
	{
		Document document = new Document();
		ByteArrayOutputStream baout = new ByteArrayOutputStream();
		PdfCopy copy = new PdfSmartCopy(document, baout);
		document.open();
		PdfReader reader = null;
		
		reader = new PdfReader (print);
		copy.addDocument (reader);
		reader.close();
		
		reader.close();
		document.close();
		baout.close();
		
		return baout.toByteArray();	
	}
	
	protected String getIdEquipamento(int cdAit)
	{
		String idEquipamento = "";
		ResultSetMap rsm  = AitServices.find (idEquipamentoAit (cdAit));
		
		if (rsm.next())
		{
			idEquipamento = rsm.getString("ID_EQUIPAMENTO");
		}
		
		return idEquipamento;
	}
	
	private static ArrayList<ItemComparator> idEquipamentoAit(int cdAit)
	{
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();		
		criterios.add(new ItemComparator("A.cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		return criterios;
	}
		
	@SuppressWarnings("static-access")
	protected static byte[]  printDocumentosNaoEntregues (int tpMovimento, GregorianCalendar dtInicial, GregorianCalendar dtFinal) throws Exception, ValidacaoException
	{
		
		AitReportServicesNip servicesNip = new AitReportServicesNip();
		AitServices aitServices = new AitServices();
		Connection connect = Conexao.conectar();
		
		try
		{
			
			String reportName = "mob/docs-nao-entregues";
			ResultSetMap rsm = new ResultSetMap();
			rsm  =  aitServices.buscarDocumentosNaoEntregues( dtInicial,  dtFinal, tpMovimento, connect);		
			HashMap<String, Object> paramns = getParamns.getParamnsNAI (connect);		
			paramns.put("REPORT_LOCALE", new Locale("pt", "BR"));
			servicesNip.calcularDesconto (rsm);
			
			byte[] print = new byte[] {};
		
			if (rsm != null && rsm.next() && paramns != null)
			{
				print = ReportServices.getPdfReport (reportName, paramns, rsm, connect);	
			}
			rsm.beforeFirst();
			
			print = gerarPrint(print);
			
			return print;	
			
		}
		catch(Exception e)
		{
			System.out.println("Erro in printDocumentosNaoEntregues: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
		finally
		{
			Conexao.desconectar (connect);
		}
	}
	
	@SuppressWarnings({ "static-access", "resource" })
	protected byte[] gerarEditalAitsNaoEntregues(int tpAit, ResultSetMap rsm) throws ValidacaoException
	{
		
		XWPFDocument docx = new XWPFDocument();
		HashMap<String, Object> paramns = getParamns.getParamnsNAI();
		String paragrafoEdital = verificarTipoEdital(tpAit);
		String tipoEdital = verificarTipoDocumento(tpAit);
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String dataGeracao = df.format(new GregorianCalendar().getTime());
		ArquivoServices arquivoServices = new ArquivoServices();
		
		try
		{
			ParagraphAlignment alinhamento = null;
			
			XWPFParagraph paragrafo = docx.createParagraph();
			XWPFRun run = paragrafo.createRun();
			run.setText((String) paramns.get("DS_TITULO_1"));
			run.addBreak();
			run.setText((String) paramns.get("DS_TITULO_3") + " - " + (String) paramns.get("SG_DEPARTAMENTO"));
			run.addBreak();
			run.setText(paragrafoEdital);
			run.setFontSize(11);
			run.setBold(true);
			run.addBreak();
			run.addBreak();
			paragrafo.setAlignment(alinhamento.CENTER);
			
			XWPFParagraph paragrafo2 = docx.createParagraph();
			XWPFRun run2 = paragrafo2.createRun();
			run2.setText((String) paramns.get("MOB_PUBLICAR_AITS_NAO_ENTREGUES"));
			run2.addBreak();
			run2.addBreak();
			paragrafo2.setAlignment(alinhamento.BOTH);
			
			XWPFTable tableOne = docx.createTable();
			tableOne.setCellMargins(50, 425, 50, 425);

		    XWPFTableRow tableOneRowOne = tableOne.getRow(0);
		    tableOneRowOne.getCell(0).setText("PLACA");
		    tableOneRowOne.addNewTableCell().setText("Nº AIT");
		    tableOneRowOne.addNewTableCell().setText("DATA INFRAÇÃO");
		    tableOneRowOne.addNewTableCell().setText("CÓDIGO INFRAÇÃO"); 
		    tableOneRowOne.addNewTableCell().setText("VALOR MULTA"); 
		    
		    while (rsm.next()){
		    	XWPFTableRow tableOneRow = tableOne.createRow();
				tableOneRow.getCell(0).setText(rsm.getString("NR_PLACA"));
				tableOneRow.getCell(1).setText(rsm.getString("ID_AIT"));
				tableOneRow.getCell(2).setText( df.format(rsm.getGregorianCalendar("DT_INFRACAO").getTime()) );
				tableOneRow.getCell(3).setText( String.valueOf(rsm.getInt("NR_COD_DETRAN")) );
				tableOneRow.getCell(4).setText("R$ " + String.format ("%.2f", rsm.getDouble("VL_MULTA")) );
		    }
		    rsm.beforeFirst();
		    
			XWPFParagraph paragrafo3 = docx.createParagraph();
			XWPFRun run3 = paragrafo3.createRun();
			run3.addBreak();
			run3.setText("Tipo de documento " + tipoEdital + " - " + "Data da geração: " + dataGeracao + " - Total de registros: " + rsm.getLines().size());
			run3.addBreak();
			run3.addBreak();
			paragrafo3.setAlignment(alinhamento.CENTER);
		    
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    docx.write(baos);
		    
		    arquivoServices.salvarArquivoDePostagemDO(baos.toByteArray(), tipoEdital, tpAit);
		    
			return baos.toByteArray();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Error in gerarEditalAitsNaoEntregues: " + e.getMessage());
			return null;
		}
		catch (IOException e )
		{
			System.out.println("Error in gerarEditalAitsNaoEntregues: " + e.getMessage());
			return null;
		}
		
	}
	
	private static String verificarTipoEdital(int tpAit) {
		String paragrafoEdital = "";
		switch(tpAit) {
			case AitMovimentoServices.NAI_ENVIADO:
				paragrafoEdital = "EDITAL DA NOTIFICAÇÃO DE AUTUAÇÃO DE INFRAÇÕES DE TRÂNSITO.";
				break;
			case AitMovimentoServices.NIP_ENVIADA:
				paragrafoEdital = "EDITAL DA NOTIFICAÇÃO DE IMPOSIÇÃO DE PENALIDADE DE TRÂNSITO.";
				break;
		}
		return paragrafoEdital;
	}
	
	private static String verificarTipoDocumento(int tpAit){
		String tipoDocumento = "";
		switch(tpAit){
			case AitMovimentoServices.NAI_ENVIADO:
				tipoDocumento = "NAI";
				break;
			case AitMovimentoServices.NIP_ENVIADA:
				tipoDocumento = "NIP";
				break;
		}
		return tipoDocumento;
	}
	
	@SuppressWarnings({ "static-access" })
	public boolean verificarPrazoPandemia(int cdAit, Connection connect) throws ValidacaoException 
	{

		boolean isConnectionNull = connect==null;	
		AitDataPandemiaValidator aitDataPandemiaValidator = new AitDataPandemiaValidator();
		GregorianCalendar dtInfracao = new GregorianCalendar();
		
		if (isConnectionNull) 
		{
			connect = Conexao.conectar();
		}
		
		try 
		{

			Ait ait = new Ait();
			AitDAO aitSearch = new AitDAO();
			ait = aitSearch.get(cdAit, connect);
			dtInfracao = ait.getDtInfracao();

			boolean validado = aitDataPandemiaValidator.validPeriodoPandemia(dtInfracao);

			return validado;
			
		}
		catch (Exception e) 
		{
			e.printStackTrace(System.out);
			return false;
		}	
		finally 
		{
			if (isConnectionNull)
			{
				Conexao.desconectar(connect);
			}
		}
	}
	
	protected BufferedImage[] recoverLogosBaseOld(Connection connect)
	{
		
		AitReportServicesDAO reportServicesDAO = new AitReportServicesDAO(connect);
		
		try
		{
			BufferedImage[] newBi = new BufferedImage[2];
			ResultSet rsm = reportServicesDAO.getImgLogosBaseOlg();
			
			if (rsm.next())
			{
				byte[] blbImagemOrgao = rsm.getBytes("img_logo_orgao");
				byte[] blbImagemDepartamento = rsm.getBytes("img_logo_departamento");
				newBi[0] = toBufferedImage(blbImagemOrgao);
				newBi[1] = toBufferedImage(blbImagemDepartamento);
			}
			
			return newBi;
		}
		catch (Exception e)
		{
			System.out.println("Error in AitReportServices >  recoverLogoBaseOld()");
			System.out.println(e.getMessage());
			return null;
		}

	}
	
    private static BufferedImage toBufferedImage(byte[] bytes) throws IOException 
    {
    	InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage bi = ImageIO.read(is);
        return bi;
    }
    
	public static String verificarEstado(Connection connect){
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		boolean isConnectionNull = connect==null;
		if (isConnectionNull){
			connect = Conexao.conectar();
		}
		AitReportServicesDAO reportServicesDAO = new AitReportServicesDAO(connect);
		try {
			String sgEstado = lgBaseAntiga ? reportServicesDAO.getSgEstadoOrgaoBaseAntiga() : OrgaoServices.getSgEstadoOrgaoAutuador();
			return sgEstado;	
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			return null;
		}	
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public static byte[] getSegundaViaAIT (int cdAit) throws Exception 
	{
		return getSegundaViaAIT (cdAit, null);
	}
		
	public static byte[] getSegundaViaAIT (int cdAit, Connection connect) throws Exception, ValidacaoException
	{

		boolean isConnectionNull = connect==null;	
		
		if (isConnectionNull) 
		{
			connect = Conexao.conectar();
		}
		
		AitReportServicesDAO aitReportServicesDao = new AitReportServicesDAO(connect);
			
		String reportName = "mob/Auto-Segunda-Via";

		ResultSetMap rsm = new ResultSetMap();
		
		rsm  = aitReportServicesDao.find(AitReportGetCriteriosAIT.segundaVia (cdAit));
		
		HashMap<String, Object> paramns = getParamns.getParamnsAUTO (connect);
		paramns.put("REPORT_LOCALE", new Locale("pt", "BR"));
		paramns.put("MOB_TP_CONVENIO", AitServices.getConvenio(cdAit));
		paramns.put("DATA_IMPRESSAO", new Date());
			
		byte[] print = new byte[] {};
		
		if (rsm != null && rsm.next() && paramns != null)
		{
			print = ReportServices.getPdfReport (reportName, paramns, rsm, connect);	
		}

		print = gerarPrint(print);

		if (isConnectionNull)
		{
			Conexao.desconectar (connect);
		}
			
		return print;	
	}
	
	private static void pegarRenainfStr(int cdAit, ResultSetMap rsm, Connection connect) throws SQLException {
		PreparedStatement pstmt = connect.prepareStatement("SELECT nr_renainf from AIT WHERE codigo_ait = ?");
		pstmt.setInt(1, cdAit);
		ResultSet rsNrRenainf = pstmt.executeQuery();
		if (rsNrRenainf.next()) {
			String nrRenainf = rsNrRenainf.getBigDecimal("nr_renainf") != null ? rsNrRenainf.getBigDecimal("nr_renainf").toString() : "";
			rsm.setValueToField("nr_renainf", nrRenainf);
		}
	}
	
	private static void pegarRenavanStr(int cdAit, ResultSetMap rsm, Connection connect) throws SQLException {
		PreparedStatement pstmt = connect.prepareStatement("SELECT cd_renavan from AIT WHERE codigo_ait = ?");
		pstmt.setInt(1, cdAit);
		ResultSet rsNrRenavan = pstmt.executeQuery();
		if (rsNrRenavan.next()) {
			String nrRenainf = rsNrRenavan.getBigDecimal("cd_renavan") != null ? rsNrRenavan.getBigDecimal("cd_renavan").toString() : "";
			rsm.setValueToField("NR_RENAVAN", nrRenainf);
		}
	}
	
	public static void setarDataVencimentoBoleto(ResultSetMap rsm) {
        if(rsm.next()) {
            GregorianCalendar dtVencimento = rsm.getGregorianCalendar("DT_VENCIMENTO");
            GregorianCalendar dtEmissao = rsm.getGregorianCalendar("DT_MOVIMENTO");
            rsm.setValueToField("DT_VENCIMENTO_BOLETO", Util.convCalendarToTimestamp(dtVencimento));
            rsm.setValueToField("DT_EMISSAO", Util.convCalendarToTimestamp(dtEmissao));
        }
        rsm.beforeFirst();
    }
	
	private static void setParametrosPagamentoOld(Map<String, Object> paramns, boolean lgBaseAntiga, Connection connect) {
        if (lgBaseAntiga) {
            paramns.put("NR_BANCO_CREDITO", ParametroServices.getValorOfParametro("NR_BANCO_COD_BARRAS_MULTA", connect));
            paramns.put("NR_AGENCIA_CREDITO", ParametroServices.getValorOfParametro("NR_AGENCIA_COD_BARRAS_MULTA", connect));
            paramns.put("NR_CONTA_CREDITO", ParametroServices.getValorOfParametro("NR_CONTA_COD_BARRAS_MULTA", connect));
        }
    }
	
	public static void validarCpfCnpj(String cpfCnpj) throws ValidacaoException {
	    if (cpfCnpj != null && (cpfCnpj.length() != 11 && cpfCnpj.length() != 14)) {
	        throw new ValidacaoException(
	            "Não foi possível gerar a sua NA/NP, será necessário atualizar o número do CPF/CNPJ junto à Ciretran local.");
	    }
	}

}