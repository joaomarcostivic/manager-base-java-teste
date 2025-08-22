package com.tivic.manager.ptc.builders;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.ptc.protocolos.DadosProtocoloDTO;

public class AitMovimentoBuilder {
	private AitMovimento movimento;
	private DadosProtocoloDTO dadosProtocolo;

	public AitMovimentoBuilder(DadosProtocoloDTO dadosProtocolo) {
		this.dadosProtocolo = dadosProtocolo;
	}

	public AitMovimentoBuilder movimento() {
		movimento = new AitMovimento();
		movimento.setCdMovimento(0);
		movimento.setCdAit(dadosProtocolo.getAit().getCdAit());
		movimento.setDtMovimento(new GregorianCalendar());
		movimento.setTpStatus(Integer.parseInt(dadosProtocolo.getTipoDocumento().getIdTipoDocumento()));
		movimento.setDsObservacao(dadosProtocolo.getTxtDocumento());
		movimento.setDtDigitacao(new GregorianCalendar());
		movimento.setCdUsuario(dadosProtocolo.getUsuario().getCdUsuario());
		movimento.setTpArquivo(dadosProtocolo.getFase().getCdFase());
		movimento.setNrProcesso(generateNrProcesso(dadosProtocolo));	
		movimento.setCdOcorrencia(dadosProtocolo.getMovimento().getCdOcorrencia());
		
		return this;
	}
	
	private String generateNrProcesso(DadosProtocoloDTO dadosProtocolo) {
		if (dadosProtocolo.getMovimento().getNrProcesso() != null) {
			return dadosProtocolo.getMovimento().getNrProcesso();
		} else {
			if (dadosProtocolo.getNrDocumento().length() <= 16)
				return dadosProtocolo.getNrDocumentoExterno();
			else
				return dadosProtocolo.getNrDocumento();
		}
	}
	public AitMovimento build() {
		return movimento;
	}
}
