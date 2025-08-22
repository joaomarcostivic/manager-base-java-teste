package com.tivic.manager.mob.mobile.builders;

import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.common.BitMatrix;
import com.tivic.manager.mob.mobile.dto.GeradorQrCodeDTO;
import com.tivic.manager.mob.mobile.utils.QrCodeImagemUtils;
import com.tivic.manager.util.ContextManager;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class GeradorImagemQrCode {

    public GeradorQrCodeDTO gerarQrCode(GeradorQrCodeDTO geradorQrCodeDTO) throws Exception {
        String jsonParametros = gerarJsonParametros(geradorQrCodeDTO);
        BitMatrix bitMatrix = QrCodeImagemUtils.gerarQrCode(jsonParametros);
        BufferedImage qrCodeImage = QrCodeImagemUtils.renderizarQrCode(bitMatrix);
        QrCodeImagemUtils.adicionarLogoNoQrCode(qrCodeImage, carregarLogo());
        geradorQrCodeDTO.setQrCodeImage(qrCodeImage);
        return geradorQrCodeDTO;
    }

    private String gerarJsonParametros(GeradorQrCodeDTO geradorQrCodeDTO) throws Exception {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("lgApiSsl", geradorQrCodeDTO.getLgApiSsl());
        jsonMap.put("nmApiHost", geradorQrCodeDTO.getNmApiHost());
        jsonMap.put("nmApiPort", geradorQrCodeDTO.getNmApiPort());
        jsonMap.put("nmApiContext", geradorQrCodeDTO.getNmApiContext());
        jsonMap.put("nmApiRoot", geradorQrCodeDTO.getNmApiRoot());
        jsonMap.put("dsDeviceUuid", geradorQrCodeDTO.getDsDeviceUuid());
        jsonMap.put("nmEquipamento", geradorQrCodeDTO.getNmEquipamento());
        if (geradorQrCodeDTO.getCdEquipamento() > 0) {
            jsonMap.put("cdEquipamento", geradorQrCodeDTO.getCdEquipamento());
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(jsonMap);
    }

    private BufferedImage carregarLogo() throws Exception, ValidacaoException {
    	Path caminhoLogo = Paths.get(ContextManager.getRealPath(), "imagens", "logo_icon_tivic.png");
        if (!Files.exists(caminhoLogo)) {
            throw new ValidacaoException("Imagem (logo_icon_tivic.png) não encontrada no caminho: " + caminhoLogo.toString());
        }
        return ImageIO.read(caminhoLogo.toFile());
    }  
    
}
