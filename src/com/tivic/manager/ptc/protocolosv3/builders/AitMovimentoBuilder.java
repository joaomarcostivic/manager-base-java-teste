package com.tivic.manager.ptc.protocolosv3.builders;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;

public class AitMovimentoBuilder {
	private AitMovimento movimento;
	private ProtocoloDTO dadosProtocolo;

	public AitMovimentoBuilder(ProtocoloDTO dadosProtocolo) {
		this.dadosProtocolo = dadosProtocolo;
	}

	public AitMovimentoBuilder movimento() {
		movimento = new AitMovimento();
		movimento.setCdMovimento(0);
		movimento.setCdAit(dadosProtocolo.getAit().getCdAit());
		movimento.setDtMovimento(new GregorianCalendar());
		movimento.setTpStatus(Integer.parseInt(dadosProtocolo.getTipoDocumento().getIdTipoDocumento()));
		movimento.setDsObservacao(dadosProtocolo.getDocumento().getTxtDocumento());
		movimento.setDtDigitacao(new GregorianCalendar());
		movimento.setCdUsuario(dadosProtocolo.getUsuario().getCdUsuario());
		movimento.setNrProcesso(generateNrProcesso(dadosProtocolo));	
		movimento.setCdOcorrencia(dadosProtocolo.getAitMovimento().getCdOcorrencia());
		movimento.setCdMovimentoCancelamento(dadosProtocolo.getAitMovimento().getCdMovimento());
		
		return this;
	}
	
	private String generateNrProcesso(ProtocoloDTO dadosProtocolo) {
		if (dadosProtocolo.getAitMovimento().getNrProcesso() != null) {
			return dadosProtocolo.getAitMovimento().getNrProcesso();
		} else {
			if (dadosProtocolo.getDocumento().getNrDocumento().length() <= 16)
				return dadosProtocolo.getDocumento().getNrDocumentoExterno();
			else
				return dadosProtocolo.getDocumento().getNrDocumento();
		}
	}
	public AitMovimento build() {
		return movimento;
	}
}
