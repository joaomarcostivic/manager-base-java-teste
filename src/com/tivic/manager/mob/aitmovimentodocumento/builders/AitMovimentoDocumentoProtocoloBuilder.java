package com.tivic.manager.mob.aitmovimentodocumento.builders;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.AitDAO;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoDocumentoDTO;
import com.tivic.manager.mob.IGerarNumeroProtocoloGenerator;
import com.tivic.manager.mob.NumeroProtocoloGeneratorFactory;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.ptc.DocumentoServices;
import com.tivic.manager.ptc.FaseDAO;
import com.tivic.manager.ptc.TipoDocumentoDAO;
import com.tivic.manager.ptc.fici.ApresentacaoCondutorDAO;
import com.tivic.sol.connection.CustomConnection;

public class AitMovimentoDocumentoProtocoloBuilder {
	private AitMovimentoDocumentoDTO dadosProtocolo;

	public AitMovimentoDocumentoProtocoloBuilder(AitMovimentoDocumentoDTO dadosProtocolo, CustomConnection customConnection) throws Exception {
		this.dadosProtocolo = dadosProtocolo;
		this.dadosProtocolo.getDocumento().setCdSituacaoDocumento(DocumentoServices.ST_ARQUIVADO);
		generateNrProtocolo(customConnection);
		generateDtProtocolo();
	}

	public AitMovimentoDocumentoProtocoloBuilder ait() {
		dadosProtocolo.setAit(AitDAO.get(dadosProtocolo.getAit().getCdAit()));

		return this;
	}

	public AitMovimentoDocumentoProtocoloBuilder tipoDocumento() {
		dadosProtocolo.setTipoDocumento(TipoDocumentoDAO.get(dadosProtocolo.getDocumento().getCdTipoDocumento()));

		return this;
	}

	public AitMovimentoDocumentoProtocoloBuilder fase() {
		dadosProtocolo.setFase(FaseDAO.get(dadosProtocolo.getDocumento().getCdFase()));

		return this;
	}

	public AitMovimentoDocumentoProtocoloBuilder movimento() {
		AitMovimento movimento = new AitMovimento();
		movimento.setCdMovimento(0);
		movimento.setCdAit(dadosProtocolo.getAit().getCdAit());
		movimento.setDtMovimento(dadosProtocolo.getDocumento().getDtProtocolo());
		movimento.setTpStatus(Integer.parseInt(dadosProtocolo.getTipoDocumento().getIdTipoDocumento()));
		movimento.setDsObservacao(dadosProtocolo.getDocumento().getTxtDocumento());
		movimento.setDtDigitacao(new GregorianCalendar());
		movimento.setCdUsuario(dadosProtocolo.getUsuario().getCdUsuario());
		movimento.setTpArquivo(dadosProtocolo.getFase().getCdFase());
		movimento.setNrProcesso(generateNrProcesso());
		
		if(isIndeferido(dadosProtocolo)) {
			movimento.setCdOcorrencia(dadosProtocolo.getMovimento().getCdOcorrencia());
		}
		
		dadosProtocolo.setMovimento(movimento);

		return this;
	}
	
	public AitMovimentoDocumentoProtocoloBuilder apresentacaoCondutor() {
		dadosProtocolo.setApresentacaoCondutor(ApresentacaoCondutorDAO.get(dadosProtocolo.getDocumento().getCdDocumento()));
		return this;
	}

	private String generateNrProcesso() {
		if (dadosProtocolo.getMovimento().getNrProcesso() != null) {
			return dadosProtocolo.getMovimento().getNrProcesso();
		} else {
			if (dadosProtocolo.getDocumento().getNrDocumento().length() <= 16)
				return dadosProtocolo.getDocumento().getNrDocumentoExterno();
			else
				return dadosProtocolo.getDocumento().getNrDocumento();
		}
	}
	
	public AitMovimentoDocumentoDTO build() {
		return dadosProtocolo;
	}
	
	private void generateNrProtocolo(CustomConnection customConnection) throws Exception {
		IGerarNumeroProtocoloGenerator numeroProtocoloGenerator = new NumeroProtocoloGeneratorFactory().gerarNumero(OrgaoServices.getCidadeOrgaoAutuador());
		numeroProtocoloGenerator.generate(dadosProtocolo.getDocumento(), dadosProtocolo.getAit().getCdAit(), customConnection);
	}
	
	private void generateDtProtocolo() {
		if(dadosProtocolo.getDocumento().getDtProtocolo() == null) {
			GregorianCalendar dataAtual = new GregorianCalendar();
			dataAtual.add(GregorianCalendar.HOUR, 3);
			dadosProtocolo.getDocumento().setDtProtocolo(dataAtual);
		}
	}
	
	private boolean isIndeferido(AitMovimentoDocumentoDTO dadosProtocolo) {
		int cdFaseIndeferida = ParametroServices.getValorOfParametroAsInteger("CD_FASE_DEFERIDA", 0, 0);
		return dadosProtocolo.getDocumento().getCdFase() == cdFaseIndeferida;
	}
}
