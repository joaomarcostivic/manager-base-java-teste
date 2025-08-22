package com.tivic.manager.mob.aitmovimentodocumento;

import java.util.GregorianCalendar;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoDocumentoDTO;
import com.tivic.manager.mob.TipoStatusEnum;

public class ResultadoDefesaBuilder {
	private AitMovimentoDocumentoDTO documentoResultado;

	public ResultadoDefesaBuilder(AitMovimentoDocumentoDTO documentoResultado) {
		this.documentoResultado = documentoResultado;
	}

	public ResultadoDefesaBuilder movimento() throws BadRequestException, Exception {
		AitMovimento movimento = new AitMovimento();
		movimento.setCdMovimento(0);
		movimento.setCdAit(documentoResultado.getAit().getCdAit());
		movimento.setDtMovimento(documentoResultado.getDocumentoOcorrencia().getDtOcorrencia());
		movimento.setDsObservacao(documentoResultado.getDocumentoOcorrencia().getTxtOcorrencia());
		movimento.setDtDigitacao(new GregorianCalendar());
		movimento.setCdUsuario(documentoResultado.getUsuario().getCdUsuario());
		movimento.setTpArquivo(documentoResultado.getFase().getCdFase());
		movimento.setNrProcesso(generateNrProcesso());
		
		if (isDeferido(documentoResultado)) {
			movimento.setTpStatus(TipoStatusEnum.DEFESA_DEFERIDA.getKey());
		} else {
			movimento.setTpStatus(TipoStatusEnum.DEFESA_INDEFERIDA.getKey());
		}
		
		documentoResultado.setMovimento(movimento);

		return this;
	}
	
	private String generateNrProcesso() {
		if (documentoResultado.getMovimento().getNrProcesso() != null) {
			return documentoResultado.getMovimento().getNrProcesso();
		} else {
			if (documentoResultado.getDocumento().getNrDocumento().length() <= 16)
				return documentoResultado.getDocumento().getNrDocumentoExterno();
			else
				return documentoResultado.getDocumento().getNrDocumento();
		}
	}
	
	public AitMovimentoDocumentoDTO build() {
		return documentoResultado;
	}
	
	private boolean isDeferido(AitMovimentoDocumentoDTO documentoResultado) throws BadRequestException, Exception {
		return documentoResultado.getFase().getCdFase() ==  getCdFaseDeferida() ? true : false;
	}
	
	private int getCdFaseDeferida() throws BadRequestException, Exception {
		return ParametroServices.getValorOfParametroAsInteger("CD_FASE_DEFERIDA", 0, 0, null);
	}
}
