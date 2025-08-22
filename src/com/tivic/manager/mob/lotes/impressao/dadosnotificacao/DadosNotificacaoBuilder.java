package com.tivic.manager.mob.lotes.impressao.dadosnotificacao;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.GregorianCalendar;

import com.google.zxing.WriterException;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.CorreiosEtiqueta;
import com.tivic.manager.mob.lotes.dto.impressao.Notificacao;
import com.tivic.manager.util.Util;
import com.tivic.sol.connection.CustomConnection;

public class DadosNotificacaoBuilder {
	private Notificacao dadosNotificacao;
	
	public DadosNotificacaoBuilder(Notificacao dados, CorreiosEtiqueta correiosEtiqueta) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		dadosNotificacao = dados;
		String nrEtiqueta = formarNrEtiqueta(correiosEtiqueta);
		correiosEtiqueta.setCdAit(dados.getCdAit());
		correiosEtiqueta.setDtEnvio(new GregorianCalendar());
		addCorreiosEtiqueta(correiosEtiqueta);
		addSgServico(correiosEtiqueta);
		addBarCode(nrEtiqueta, customConnection);
		addCodeSG(dados, nrEtiqueta);
		addBarCode2D(dados, nrEtiqueta);
		addQrCode(dados, nrEtiqueta, customConnection);
	}
	
	public DadosNotificacaoBuilder addCorreiosEtiqueta(CorreiosEtiqueta correiosEtiqueta) {
		dadosNotificacao.setCorreiosEtiqueta(correiosEtiqueta);
		return this;
	}
	
	public DadosNotificacaoBuilder addSgServico(CorreiosEtiqueta correiosEtiqueta) {
		dadosNotificacao.setSgServico(correiosEtiqueta.getSgServico());
		return this;
	}
	
	public DadosNotificacaoBuilder addBarCode(String nrEtiqueta, CustomConnection customConnection) throws IOException {
	    String barCode = "AR" + nrEtiqueta + ParametroServices.getValorOfParametro("MOB_CORREIOS_SG_CLIENTE", customConnection.getConnection());
	    dadosNotificacao.setBarCodeImage(new ImagemGenerator().gerar(barCode));
	    dadosNotificacao.setBarCode(barCode);
	    return this;
	}
	
	public DadosNotificacaoBuilder addCodeSG(Notificacao dados, String nrEtiqueta) throws IOException {
		String codeSG = dados.getSgServico() + nrEtiqueta + "BR";
	    dadosNotificacao.setCodeSgImage(new ImagemGenerator().gerar(codeSG));
	    dadosNotificacao.setCodeSG(codeSG);
	    return this;
	}
	
	public DadosNotificacaoBuilder addBarCode2D(Notificacao dados, String nrEtiqueta) throws IOException {
		String barCode2D = dados.getSgServico() + nrEtiqueta + "AA";
	    dadosNotificacao.setBarCode2DImage(new ImagemGenerator().gerar(barCode2D));
		dadosNotificacao.setBarCode2D(barCode2D);
		return this;
	}
		
	public DadosNotificacaoBuilder addQrCode(Notificacao dados, String nrEtiqueta, CustomConnection customConnection) throws WriterException {
	    String qrCodeContent = dados.getSgServico() + nrEtiqueta + "AA" + ParametroServices.getValorOfParametro("MOB_CORREIOS_SG_CLIENTE", customConnection.getConnection());
	    int datamatrixSize = 18;
	    BufferedImage qrCodeImage = new ImagemGenerator().gerarQrCodeDataMatrix(qrCodeContent, datamatrixSize);
	    dadosNotificacao.setQrCodeImage(qrCodeImage);
	    return this;
	}

	private String formarNrEtiqueta(CorreiosEtiqueta correiosEtiqueta) throws Exception {
		int qtdEtiqueta = 8;
		return Util.fillNum(correiosEtiqueta.getNrEtiqueta(), qtdEtiqueta) 
				+ correiosEtiqueta.getNrDigitoVerificador();		
	}

	public Notificacao build() {
		return dadosNotificacao;
	}
}
