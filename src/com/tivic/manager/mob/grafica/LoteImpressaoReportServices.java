package com.tivic.manager.mob.grafica;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.ImageIcon;

import org.krysalis.barcode4j.impl.int2of5.Interleaved2Of5Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitDAO;
import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.mob.AitImagemDAO;
import com.tivic.manager.mob.AitImagemServices;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.AitReportErrorException;
import com.tivic.manager.mob.AitReportGetCriteriosCondutor;
import com.tivic.manager.mob.AitReportGetCriteriosNAI;
import com.tivic.manager.mob.AitReportGetCriteriosNIP;
import com.tivic.manager.mob.AitReportGetParamns;
import com.tivic.manager.mob.AitReportServices;
import com.tivic.manager.mob.AitReportServicesDAO;
import com.tivic.manager.mob.AitReportServicesNai;
import com.tivic.manager.mob.AitReportServicesNip;
import com.tivic.manager.mob.AitReportValidators;
import com.tivic.manager.mob.AitReportValidatorsNAI;
import com.tivic.manager.mob.AitReportValidatorsNIP;
import com.tivic.manager.mob.AitServices;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.Conexao;
import com.tivic.sol.report.ReportServices;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;
import sol.util.Result;

public class LoteImpressaoReportServices {
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

			if (isConnectionNull) {
				connect.commit();
			}
			
			return print;	
		}
		catch(AitReportErrorException e) {
			if(isConnectionNull)
				connect.rollback();
			throw new AitReportErrorException("Erro ao gerar NAI: " + e.getMessage());
		}
		catch (Exception e) {
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
	
	public static byte[] getNIPPrimeiraVia (int cdAit) throws Exception, ValidacaoException {
		return getNIPPrimeiraVia (cdAit, false, null);
	}
	
	@SuppressWarnings("static-access")
	public static byte[] getNIPPrimeiraVia (int cdAit, boolean lote, Connection connect) throws Exception, ValidacaoException {
		boolean isConnectionNull = connect==null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			
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
			

			if (paramns.get("NR_CD_FEBRABAN") != null) {	
				servicesNip.gerarCodigoBarras(rsm, paramns, connect);	 
			}
				
			byte[] print = new byte[] {};
			
			if (rsm != null && rsm.next() && paramns != null) {
				print = ReportServices.getPdfReport (reportName, paramns, rsm, connect);
			}
			
			print = gerarPrint(print);
			
			if (isConnectionNull) {
				connect.commit();
			}
			
			return print;
			
		}
		catch(AitReportErrorException e) {
			if(isConnectionNull)
				connect.rollback();
			throw new AitReportErrorException("Erro ao gerar NIP: " + e.getMessage());
		}
		catch (Exception e) {
			if(isConnectionNull)
				connect.rollback();
			throw new ValidacaoException("Erro ao gerar NIP: " + e.getMessage());
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	protected static void atualizarDadosAit( int cdAit, Connection connect) throws Exception {
		Ait ait = AitDAO.get(cdAit, connect);
		Result r = AitServices.updateDetran(ait, connect);
		if (r.getCode() < 0){
			throw new ValidacaoException ("Não foi possiveu atualizado os dados do AIT: " + cdAit);
		}
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

	public static ArrayList<ItemComparator> primeiraViaNAI (int cdAit, Connection connect) throws ValidacaoException  
	{
		
		AitReportServices aitReportServices = new AitReportServices();
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA"); 
		boolean isConnectionNull = connect==null;	
		if (isConnectionNull) 
			connect = Conexao.conectar();
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		
		if (lgBaseAntiga)
		{
			criterios.add(new ItemComparator("A.CODIGO_AIT", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.st_ait", String.valueOf(AitServices.ST_CONFIRMADO), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.nr_controle", "", ItemComparator.NOTISNULL, Types.INTEGER));
			criterios.add(new ItemComparator("A.nr_controle", "0", ItemComparator.GREATER, Types.INTEGER));
			criterios.add(new ItemComparator("A.NM_PROPRIETARIO", "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator(("A.NR_CPF_CNPJ_PROPRIETARIO").trim(), "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator("A.NR_CPF_CNPJ_PROPRIETARIO", "0", ItemComparator.GREATER, Types.CHAR));
			criterios.add(new ItemComparator("A.cod_bairro", "", ItemComparator.NOTISNULL, Types.INTEGER));
			criterios.add(new ItemComparator("A.DS_LOGRADOURO", "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator("A.cod_municipio", "", ItemComparator.NOTISNULL, Types.INTEGER));
			criterios.add(new ItemComparator("H.NM_MODELO", "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator("G.DS_ESPECIE", "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator("A.CD_RENAVAN", "", ItemComparator.NOTISNULL, Types.CHAR));
		}
		else
		{
		
			criterios.add(new ItemComparator("A.cd_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.st_ait", String.valueOf(AitServices.ST_CONFIRMADO), ItemComparator.EQUAL, Types.INTEGER));
			criterios.add(new ItemComparator("A.nr_controle", "", ItemComparator.NOTISNULL, Types.INTEGER));
			criterios.add(new ItemComparator("A.nr_controle", "0", ItemComparator.GREATER, Types.INTEGER));
			criterios.add(new ItemComparator("A.NM_PROPRIETARIO", "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator("A.NR_CPF_CNPJ_PROPRIETARIO", "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator("A.CD_BAIRRO", "", ItemComparator.NOTISNULL, Types.INTEGER));
			criterios.add(new ItemComparator("A.DS_LOGRADOURO", "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator("A.CD_CIDADE", "", ItemComparator.NOTISNULL, Types.INTEGER));
			//criterios.add(new ItemComparator("A.DS_NR_IMOVEL", "", ItemComparator.NOTISNULL, Types.CHAR));*Comentando a pedido de Anderson, vistoque que as vezes vem sem o numero como por exemplo em zonas rurais
			criterios.add(new ItemComparator("H.NM_MARCA", "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator("H.NM_MODELO", "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator("G.DS_ESPECIE", "", ItemComparator.NOTISNULL, Types.CHAR));
			criterios.add(new ItemComparator("A.NR_RENAVAN", "", ItemComparator.NOTISNULL, Types.CHAR));

		}
		if(!aitReportServices.verificarPrazoPandemia(cdAit, connect))
		{
			//não tiver passado 30 dias do cometimento da infracao
			GregorianCalendar dtInicial = new GregorianCalendar();
			dtInicial.set(Calendar.HOUR, 0);
			dtInicial.set(Calendar.MINUTE, 0);
			dtInicial.set(Calendar.SECOND, 0);
			dtInicial.add(Calendar.DATE, -30);
			criterios.add(new ItemComparator("A.dt_infracao", Util.convCalendarStringSql(dtInicial), ItemComparator.GREATER_EQUAL, Types.VARCHAR));
		}
		if (isConnectionNull)
			Conexao.desconectar(connect);
			
		return criterios;
	}
	
	public static ArrayList<ItemComparator> primeiraViaNip (int cdAit, Connection connect) throws ValidacaoException 
	{
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();

		criterios.add(new ItemComparator("A.codigo_ait", String.valueOf(cdAit), ItemComparator.EQUAL, Types.INTEGER));
		criterios.add(new ItemComparator("A.st_ait", String.valueOf(AitServices.ST_CONFIRMADO), ItemComparator.EQUAL, Types.INTEGER));
			
		if(isConnectionNull)
			Conexao.desconectar(connect);
			
		return criterios;
	}
	
	public static Image imagemVeiculo(ResultSetMap rsm, Connection connect) {
		
		boolean isConnectionNull = connect==null;
		if (isConnectionNull)
			connect = Conexao.conectar();
		
		Image img = null;
		while(rsm.next()) {
			ResultSetMap rsmImg = AitImagemServices.getFromAit(rsm.getInt("cd_ait"), connect);
			int cdImg = 0;
			if(rsmImg.next()) 
			{
				cdImg = rsmImg.getInt("cd_imagem");							
			}
			
			byte[] blbImagem = null;
			
			AitImagem aitImagem = AitImagemDAO.get(cdImg, rsm.getInt("cd_ait"), connect);
			if(aitImagem!=null) 
			{
				blbImagem = aitImagem.getBlbImagem();
				img = new ImageIcon(blbImagem).getImage();
			}
		}
		rsm.beforeFirst();
		
		if (isConnectionNull)
			Conexao.desconectar(connect);
		
		return img;
	}
	
	public static BufferedImage generateBarcodeImage(String barcodeText) {
		Interleaved2Of5Bean barcodeGenerator = new Interleaved2Of5Bean();
	    BitmapCanvasProvider canvas = 
	      new BitmapCanvasProvider(160, BufferedImage.TYPE_BYTE_BINARY, false, 0);
	    
	    barcodeGenerator.setFontSize(0.0);
	    barcodeGenerator.generateBarcode(canvas, barcodeText);
	    return canvas.getBufferedImage();
	}
}