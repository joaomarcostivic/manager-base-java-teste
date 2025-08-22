package com.tivic.manager.ptc.protocolos;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.AitDAO;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.IGerarNumeroProtocoloGenerator;
import com.tivic.manager.mob.NumeroProtocoloGeneratorFactory;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.ptc.DocumentoServices;
import com.tivic.manager.ptc.FaseDAO;
import com.tivic.manager.ptc.FaseServices;
import com.tivic.manager.ptc.TipoDocumentoDAO;
import com.tivic.manager.ptc.fici.ApresentacaoCondutorDAO;
import com.tivic.sol.connection.CustomConnection;

public class DadosProtocoloBuilder {
	private DadosProtocoloDTO dadosProtocolo;

	public DadosProtocoloBuilder(DadosProtocoloDTO dadosProtocolo, CustomConnection customConnection) throws Exception {
		this.dadosProtocolo = dadosProtocolo;
		this.dadosProtocolo.setCdSituacaoDocumento(DocumentoServices.ST_ARQUIVADO);
		generateNrProtocolo(dadosProtocolo.getAit().getCdAit(), customConnection);
		generateDtProtocolo();
	}

	public DadosProtocoloBuilder ait() {
		dadosProtocolo.setAit(AitDAO.get(dadosProtocolo.getAit().getCdAit()));

		return this;
	}

	public DadosProtocoloBuilder tipoDocumento() {
		dadosProtocolo.setTipoDocumento(TipoDocumentoDAO.get(dadosProtocolo.getCdTipoDocumento()));

		return this;
	}

	public DadosProtocoloBuilder fase() {
		dadosProtocolo.setFase(FaseDAO.get(dadosProtocolo.getCdFase()));

		return this;
	}

	public DadosProtocoloBuilder movimento() {
		AitMovimento movimento = new AitMovimento();
		movimento.setCdMovimento(0);
		movimento.setCdAit(dadosProtocolo.getAit().getCdAit());
		movimento.setDtMovimento(dadosProtocolo.getDtProtocolo());
		movimento.setTpStatus(Integer.parseInt(dadosProtocolo.getTipoDocumento().getIdTipoDocumento()));
		movimento.setDsObservacao(dadosProtocolo.getTxtDocumento());
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
	
	public DadosProtocoloBuilder apresentacaoCondutor() {
		dadosProtocolo.setApresentacaoCondutor(ApresentacaoCondutorDAO.get(dadosProtocolo.getCdDocumento()));
		return this;
	}

	private String generateNrProcesso() {
		if (dadosProtocolo.getMovimento().getNrProcesso() != null) {
			return dadosProtocolo.getMovimento().getNrProcesso();
		} else {
			if (dadosProtocolo.getNrDocumento().length() <= 16)
				return dadosProtocolo.getNrDocumentoExterno();
			else
				return dadosProtocolo.getNrDocumento();
		}
	}
	
	public DadosProtocoloDTO build() {
		return dadosProtocolo;
	}
	
	private void generateNrProtocolo(int cdAit, CustomConnection customConnection) throws Exception {
		IGerarNumeroProtocoloGenerator numeroProtocoloGenerator = new NumeroProtocoloGeneratorFactory().gerarNumero(OrgaoServices.getCidadeOrgaoAutuador());
		numeroProtocoloGenerator.generate(dadosProtocolo, cdAit, customConnection);
	}
	
	private void generateDtProtocolo() {
		if(dadosProtocolo.getDtProtocolo() == null) {
			GregorianCalendar dataAtual = new GregorianCalendar();
			dataAtual.add(GregorianCalendar.HOUR, 3);
			dadosProtocolo.setDtProtocolo(dataAtual);
		}
	}
	
	private boolean isIndeferido(DadosProtocoloDTO dadosProtocolo) {
		return dadosProtocolo.getCdFase() == FaseServices.getCdFaseByNome("indeferido");
	}

}
