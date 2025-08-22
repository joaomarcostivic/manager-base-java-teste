package com.tivic.manager.mob;

import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.codehaus.jettison.json.JSONException;
import org.krysalis.barcode4j.impl.int2of5.Interleaved2Of5Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.BancoServices;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.connection.Conexao;

import sol.dao.ItemComparator;
import sol.dao.ResultSetMap;

public class AitReportServicesNip {
	
	public void gerarCodigoBarras(ResultSetMap rsm, HashMap<String, Object> paramns, Connection connect) throws ValidacaoException, ParseException, JSONException, org.json.JSONException
	{
		AitReportCodigoBarrasNip codigoBarrasNip = new AitReportCodigoBarrasNip();
		IAitReportCriarCodigoBarrasNip estrategiaCriarCodigoBarras  = codigoBarrasNip.getEstrategiaCodigoBarras(AitReportServices.verificarEstado(connect));
		String barras = estrategiaCriarCodigoBarras.criarCodigoBarras(rsm, paramns, connect);	
		String barrasDV = gerarBarrasDV (barras);
		if (rsm.next()) {
			rsm.setValueToField ("LINHA_DIGITAVEL", barrasDV);
			rsm.setValueToField ("CODIGO_BARRAS_COM_DV", barras);
		}
		rsm.beforeFirst();
		paramns.put ("BARRAS", generateBarcodeImage(barras));
	}
	
	private String gerarBarrasDV (String barras) {
		String strBarras = barras;
		String bloco1 = strBarras.substring (0, 11);
		String bloco2 = strBarras.substring (11, 22);
		String bloco3 = strBarras.substring (22, 33);
		String bloco4 = strBarras.substring (33, 44);
		String bloco1DV = bloco1 + "-" + Integer.toString (Util.getDvMod10 (bloco1));
		String bloco2DV = bloco2 + "-" + Integer.toString (Util.getDvMod10 (bloco2));
		String bloco3DV = bloco3 + "-" + Integer.toString (Util.getDvMod10 (bloco3));
		String bloco4DV = bloco4 + "-" + Integer.toString (Util.getDvMod10 (bloco4));
		strBarras = bloco1DV + " " + bloco2DV + " " + bloco3DV + " " + bloco4DV;
		return strBarras;
	}
	
	private BufferedImage generateBarcodeImage(String barcodeText) {
	    Interleaved2Of5Bean barcodeGenerator = new Interleaved2Of5Bean();
	    float scale = 2.0f;
	    int resolucao = 300;
	    BitmapCanvasProvider canvas = new BitmapCanvasProvider(
	            resolucao * Math.round(scale),
	            BufferedImage.TYPE_BYTE_GRAY,
	            false,
	            0);
	    barcodeGenerator.setFontSize(0);
	    barcodeGenerator.generateBarcode(canvas, barcodeText);
	    return canvas.getBufferedImage();
	}
	
	public void calcularJuros(ResultSetMap rsm, int cdAit, Connection connect) throws Exception {
		AitReportJurosNip jurosNip = new AitReportJurosNip();
		IAitReportCalcularJurosNip estrategiaDeCalculo = jurosNip.getEstrategiaCalcularJuros(AitReportServices.verificarEstado(connect));
		estrategiaDeCalculo.calularJurosNip(rsm, cdAit, connect);
	}
	
	public void calcularDesconto(ResultSetMap rsm){
		double vlMultaDesconto = 0;
		double porcentagemDesconto = 20;
		double vlDesconto = 0;
		while(rsm.next()){	
			vlDesconto = ((porcentagemDesconto * rsm.getDouble("VL_MULTA")) / 100); 		
			vlMultaDesconto = rsm.getDouble("VL_MULTA") - vlDesconto;
			rsm.setValueToField("VL_MULTA_DESCONTO", vlMultaDesconto);
		}
		rsm.beforeFirst();
	}

	public void pegarBancoConveniado(Connection connect, ResultSetMap rsm) {
		String bancosConveniados = "";
		ResultSetMap rsmBancosConveniados = BancoServices.find(criteriosBancoConveniado(), connect);
		while (rsmBancosConveniados.next()) {
			if(rsmBancosConveniados.getPosition() == 0) {
				bancosConveniados += rsmBancosConveniados.getString("nm_banco");
			} else {
				bancosConveniados += ", " + rsmBancosConveniados.getString("nm_banco");
			}
		}
		if(bancosConveniados.isEmpty()) bancosConveniados = null;
		if (rsm.next()) {
			rsm.setValueToField("BANCOS_CONVENIADOS", bancosConveniados);
		}
		rsm.beforeFirst();
	}
	
	private static ArrayList<ItemComparator> criteriosBancoConveniado() {
		ArrayList<ItemComparator> criterios = new ArrayList<ItemComparator>();
		criterios.add(new ItemComparator("banco_conveniado", String.valueOf(1), ItemComparator.EQUAL, Types.INTEGER));
		return criterios;
	}
	
	protected int insertNrNotificacaoNip(Connection connect) throws ValidacaoException {
		boolean isConnectionNull = connect==null;
		if (isConnectionNull) {
			connect = Conexao.conectar();
		}
		
		try {
			AitReportNipDAO reportNipDao = new AitReportNipDAO(connect);
			int nrNotificacaoNip = ParametroServices.getValorOfParametroAsInteger("MOB_SEQUENCIA_NR_NIP", 0, 0, connect) + 1;		
			reportNipDao.updateParamNrSequenceNip(nrNotificacaoNip);
			return nrNotificacaoNip;
		}
		catch (Exception e) {
			e.printStackTrace (System.out);
			throw new  ValidacaoException("Erro ao tentar gerar o numero de notificação para a NIP");
		}	
		finally {
			if (isConnectionNull) {
				Conexao.desconectar(connect);
			}
		}
	}
	
	protected void insertNrNotificacaoNipOld(int cdAit, Connection connect)
	{
		
		AitReportNipDAO reportNipDao = new AitReportNipDAO(connect);
		
		boolean isConnectionNull = connect==null;
		
		if (isConnectionNull)
		{
			connect = Conexao.conectar();
		}
		
		try
		{
			int nrNotificaoNip = reportNipDao.getMaxNrNotificationNipOld() + 1;
			reportNipDao.updateNrNotificationNip(nrNotificaoNip, cdAit);
		}
		catch (Exception e) 
		{
			e.printStackTrace(System.out);
		}	
		finally 
		{
			if (isConnectionNull)
			{
				Conexao.desconectar(connect);
			}
		}
		
	}
	
	public static void pegarDataVencimentoAit(int cdAit, ResultSetMap rsm) throws ValidacaoException {
		pegarDataVencimentoAit(cdAit, rsm, null);
	}
	
	public static void pegarDataVencimentoAit(int cdAit, ResultSetMap rsm, Connection connect) throws ValidacaoException {

		boolean isConnectionNull = connect==null;
		
		try {
			
			if (isConnectionNull){
				connect = Conexao.conectar();
			}
		
			Ait ait = AitDAO.get(cdAit, connect);
		
			if (ait.getDtVencimento() == null){
				factoryVencimento(ait, rsm, connect);
			}
		
		}
		catch (Exception e) {
			System.out.println("Erro em AitReportServicesNip > factoryVencimento()");
			e.printStackTrace();
			throw new ValidacaoException("Erro ao gerar data de vencimento para NIP");
		}
		finally {
			if(isConnectionNull){
				Conexao.desconectar(connect);
			}
		}
		
	}
	
	private static void factoryVencimento(Ait ait, ResultSetMap rsm, Connection connect) throws ValidacaoException, AitReportErrorException {
		
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
			
		AitReportDataJariNIP jariNip = new AitReportDataJariNIP();
		IAitReportCalcularPrazoNIP estrategiaJari = jariNip.getEstrategiaJariNIP(AitReportServices.verificarEstado(connect));
		AitReportPegarJariNIP pegarJariNip = new AitReportPegarJariNIP(ait.getCdAit(), connect);
		GregorianCalendar dtVencimento = pegarJariNip.pegar(estrategiaJari);
			
		ait.setDtVencimento(dtVencimento);
		
		Timestamp data = new Timestamp(dtVencimento.getTimeInMillis());
			
		if (lgBaseAntiga){
			atualizarVencimentoBaseAntiga(ait.getCdAit(), dtVencimento, connect);
		}
		else{
			AitDAO.update(ait, connect);
		}
			
		if(rsm.next()){
			rsm.setValueToField("DT_VENCIMENTO", data);
		}
		rsm.beforeFirst();
		
	}
	
	private static void atualizarVencimentoBaseAntiga(int cdAit, GregorianCalendar vencimento, Connection connect)
	{
		
		boolean isConnectionNull = connect==null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		if (isConnectionNull)
		{
			connect = Conexao.conectar();
		}
		
		AitReportNipDAO reportNipDao = new AitReportNipDAO(connect);
		String dtVencimento = dateFormat.format(new Timestamp(vencimento.getTimeInMillis()));
		
		try
		{
			reportNipDao.updateDataVencimentoAitOld(dtVencimento, cdAit);
		}
		catch (Exception e) 
		{
			e.printStackTrace(System.out);
		}	
		finally 
		{
			if (isConnectionNull)
			{
				Conexao.desconectar(connect);
			}
		}
	}
}