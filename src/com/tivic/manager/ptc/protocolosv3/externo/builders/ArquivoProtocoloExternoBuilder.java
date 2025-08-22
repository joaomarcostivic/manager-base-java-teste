package com.tivic.manager.ptc.protocolosv3.externo.builders;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.ArquivoBuilder.ArquivoBuilder;
import com.tivic.manager.ptc.protocolosv3.externo.ProtocoloExternoDTO;
import java.util.GregorianCalendar;

public class ArquivoProtocoloExternoBuilder extends ArquivoBuilder {
	
	public ArquivoProtocoloExternoBuilder(byte[] bytePdfDocumentoExterno, ProtocoloExternoDTO protocoloExternoDTO, int cdLoteImpressao) {
        super();
        this.setBlbArquivo(bytePdfDocumentoExterno)
            .setCdUsuario(protocoloExternoDTO.getCdUsuario())
            .setNmArquivo("LOTE_OFICIO_DOCUMENTO_EXTERNO_" + cdLoteImpressao + ".pdf")
            .setNmDocumento("Lote de Ofício de Protocolo Externo")
            .setDtArquivamento(new GregorianCalendar())
            .setDtCriacao(new GregorianCalendar())
            .setCdTipoArquivo(Integer.valueOf(ParametroServices.getValorOfParametro("MOB_OFICIO_DOCUMENTO_EXTERNO")));
    }
}