package com.tivic.manager.ptc.protocolosv3.resultado.builders;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.protocolosv3.resultado.ResultadoDTO;

public class ResultadoMovimentoBuilder {
	private AitMovimento movimento;

	public ResultadoMovimentoBuilder() {
		movimento = new AitMovimento();
	}
	
	public ResultadoMovimentoBuilder dadosBase(ResultadoDTO documentoResultado) {
		movimento.setCdAit(documentoResultado.getCdAit());
		movimento.setDtMovimento(documentoResultado.getDtOcorrencia());
		movimento.setDsObservacao(documentoResultado.getTxtOcorrencia());
		movimento.setDtDigitacao(new GregorianCalendar());
		movimento.setCdUsuario(documentoResultado.getCdUsuario());
		movimento.setTpStatus(documentoResultado.getTpStatus());
		return this;
	}

	public ResultadoMovimentoBuilder nrProcesso(AitMovimento movimentoAnterior, Documento documento) {
		movimento.setNrProcesso(generateNrProcesso(movimentoAnterior, documento));
		return this;
	}
	
	private String generateNrProcesso(AitMovimento movimentoAnterior, Documento documento) {
		if (movimentoAnterior.getNrProcesso() != null) {
			return movimentoAnterior.getNrProcesso();
		} else {
			return documento.getNrDocumento();
		}
	}
	
	public AitMovimento build() {
		return movimento;
	}
}
