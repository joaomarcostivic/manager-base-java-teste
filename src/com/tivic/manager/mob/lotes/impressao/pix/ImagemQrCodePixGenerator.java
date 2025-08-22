package com.tivic.manager.mob.lotes.impressao.pix;

import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.GregorianCalendar;
import java.util.List;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.lotes.dto.impressao.Notificacao;
import com.tivic.manager.mob.lotes.impressao.pix.builders.AitPixBuilder;
import com.tivic.manager.mob.lotes.impressao.pix.builders.DadosArrecadacaoEnvioBuilder;
import com.tivic.manager.mob.lotes.impressao.pix.model.AitPix;
import com.tivic.manager.mob.lotes.impressao.pix.repository.AitPixRepository;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;

import io.jsonwebtoken.io.IOException;

public class ImagemQrCodePixGenerator {
	
	private AitPixRepository aitPixRepository;
	
	public ImagemQrCodePixGenerator() throws Exception {
		this.aitPixRepository = (AitPixRepository) BeansFactory.get(AitPixRepository.class);
	}

	public Notificacao registrarArrecadacaoPix(Notificacao notificacao, String codigoDeBarras, String tokenAutenticacao) throws Exception {
		getQrCodePixAit(notificacao);
		if(notificacao.getPixQrCodeImage() != null) {
			return notificacao;
		}
    	String linkLancamentoRegistroPix =  "http://host.tivic.com.br:9547/registro/arrecadacoes";
	    LocalDate dataAtual = LocalDate.now();
	    GregorianCalendar dataVencimentoGC = notificacao.getDtVencimento();
	    LocalDate dataVencimento = dataVencimentoGC.toInstant()
	        .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
	    long diasParaVencimento = ChronoUnit.DAYS.between(dataAtual, dataVencimento);

	    String codigoOrgao = ParametroServices.getValorOfParametro("MOB_CD_ORGAO_AUTUADOR");
	    String chavePixOrgao = ParametroServices.getValorOfParametro("NM_CHAVE_PIX");

	    DadosArrecadacaoEnvio dadosEnvio = new DadosArrecadacaoEnvioBuilder()
	    	.addUrl(linkLancamentoRegistroPix)
	        .addNrChavePix(chavePixOrgao)
	        .addDiasVencimento(String.valueOf(diasParaVencimento))
	        .addCodigoBarras(codigoDeBarras)
	        .addVlCobranca(notificacao.getVlMultaComDesconto() != null ? notificacao.getVlMultaComDesconto() : notificacao.getVlMulta())
	        .addTxtDescricao(codigoOrgao + " - AIT: " + notificacao.getIdAit())
	        .addNmDevedor(notificacao.getNmProprietario())
	        .addCpfDevedor(notificacao.getNrCpfCnpjProprietario())
	        .addCnpjDevedor(notificacao.getNrCpfCnpjProprietario())
	        .build();

	    if(tokenAutenticacao == null) {
	    	return null;
	    }
	    RegistroArrecadacao registro = new RegistroArrecadacao(dadosEnvio, tokenAutenticacao);
	    DadosArrecadacaoRetorno retorno = registro.executarRegistro();
	    if(retorno == null || retorno.getQrCode() == null) {
	    	return null;
	    }
	    insertAitPix(retorno, notificacao.getCdAit());
	    String qrCodeData = retorno.getQrCode();
	    setPixQrCodeImage(qrCodeData, notificacao);
		return notificacao;
	}
	
	private void setPixQrCodeImage(String pixPayload, Notificacao notificacao) throws WriterException, IOException {
	    int size = 250;
	    QRCodeWriter qrCodeWriter = new QRCodeWriter();
	    BitMatrix bitMatrix = qrCodeWriter.encode(pixPayload, BarcodeFormat.QR_CODE, size, size);
	
	    BufferedImage pixQrCodeImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
	    for (int x = 0; x < size; x++) {
	        for (int y = 0; y < size; y++) {
	            pixQrCodeImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
	        }
	    }
	    notificacao.setPixQrCodeImage(pixQrCodeImage);
	}
	
	private void getQrCodePixAit(Notificacao notificacao) throws Exception, ValidacaoException {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", notificacao.getCdAit());
		searchCriterios.addCriteriosGreaterDate("dt_vencimento", new SimpleDateFormat("yyyy-MM-dd").format(DateUtil.getDataAtual().getTime()));
		searchCriterios.setOrderBy("dt_criacao DESC");
		List<AitPix> aitPixList = this.aitPixRepository.find(searchCriterios);
	    if (aitPixList.isEmpty()) {
	        return;
	    }
	    String dsQrCode = aitPixList.get(0).getDsQrCode() != null ? aitPixList.get(0).getDsQrCode() : null;
	    if(dsQrCode != null) {
	    	setPixQrCodeImage(dsQrCode, notificacao);
	    }
	    return;
	}
	
	private void insertAitPix(DadosArrecadacaoRetorno dadosArrecadacaoRetorno, int cdAit) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			AitPix aitPix = new AitPixBuilder()
			    .setCdAit(cdAit)
			    .setCdCobranca(dadosArrecadacaoRetorno.getCdCobranca())
			    .setTxtTxid(dadosArrecadacaoRetorno.getTxtTxid())
			    .setStCobranca(dadosArrecadacaoRetorno.getStCobranca())
			    .setDtCriacao(dadosArrecadacaoRetorno.getDtCriacao())
			    .setDtVencimento(dadosArrecadacaoRetorno.getDtVencimento())
			    .setVlCobranca(dadosArrecadacaoRetorno.getVlCobranca())
			    .setTxtObservacao(dadosArrecadacaoRetorno.getTxtObservacao())
			    .setTpCobranca(dadosArrecadacaoRetorno.getTpCobranca())
			    .setNrCodigoGuiaRecebimento(dadosArrecadacaoRetorno.getNrCodigoGuiaRecebimento())
			    .setNmDevedor(dadosArrecadacaoRetorno.getDevedor().getNmDevedor())
			    .setNrCpfCnpj(dadosArrecadacaoRetorno.getDevedor().getNrCpfCnpj())
			    .setDsQrCode(dadosArrecadacaoRetorno.getQrCode())
			    .build();
			    
		    this.aitPixRepository.insert(aitPix, customConnection);
		    customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}
	    
}
