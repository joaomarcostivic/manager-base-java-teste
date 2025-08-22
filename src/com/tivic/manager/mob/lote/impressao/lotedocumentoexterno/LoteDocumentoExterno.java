package com.tivic.manager.mob.lote.impressao.lotedocumentoexterno;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoteDocumentoExterno {
	private int cdLoteImpressao;
	private int cdDocumento;
	private int cdDocumentoExterno;
	
	public LoteDocumentoExterno() {}
	
	public LoteDocumentoExterno(int cdLoteImpressao, int cdDocumento, int cdDocumentoExterno) {
		setCdLoteImpressao(cdLoteImpressao);
		setCdDocumento(cdDocumento);
		setCdDocumentoExterno(cdDocumentoExterno);
	}

	public int getCdLoteImpressao() {
		return cdLoteImpressao;
	}

	public void setCdLoteImpressao(int cdLoteImpressao) {
		this.cdLoteImpressao = cdLoteImpressao;
	}

	public int getCdDocumento() {
		return cdDocumento;
	}

	public void setCdDocumento(int cdDocumento) {
		this.cdDocumento = cdDocumento;
	}
	
	public int getCdDocumentoExterno() {
		return cdDocumentoExterno;
	}

	public void setCdDocumentoExterno(int cdDocumentoExterno) {
		this.cdDocumentoExterno = cdDocumentoExterno;
	}

	@Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "Não foi possível serializar o objeto informado";
        }
    }
}
