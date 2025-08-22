package com.tivic.manager.ptc.protocolosv3.builders;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.IGerarNumeroProtocoloGenerator;
import com.tivic.manager.mob.NumeroProtocoloGeneratorFactory;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.ptc.fase.IFaseRepository;
import com.tivic.manager.ptc.protocolosv3.InsertProtocoloDTOBuilder;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.ProtocoloInsertDTO;
import com.tivic.manager.ptc.tipodocumento.TipoDocumentoRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class BaseProtocoloDTOBuilder {
	private ProtocoloDTO dadosProtocolo;
	private AitRepository aitRepository;
	private TipoDocumentoRepository tipoDocumentoRepository;
	private IFaseRepository faseRepository;
	private ProtocoloInsertDTO protocoloInsertDTO;

	public BaseProtocoloDTOBuilder(ProtocoloInsertDTO dadosProtocolo, CustomConnection customConnection) throws Exception {
		aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		tipoDocumentoRepository = (TipoDocumentoRepository) BeansFactory.get(TipoDocumentoRepository.class);
		faseRepository = (IFaseRepository) BeansFactory.get(IFaseRepository.class);
		this.dadosProtocolo = dadosProtocoloConverter(dadosProtocolo);
		this.protocoloInsertDTO = dadosProtocolo;
		generateNrProtocolo(customConnection);
	}

	private ProtocoloDTO dadosProtocoloConverter(ProtocoloInsertDTO dadosProtocolo) throws Exception {
		ProtocoloDTO protocoloAdaptado = new ProtocoloDTO();
		protocoloAdaptado = new InsertProtocoloDTOBuilder(dadosProtocolo)
				.setAit()
				.setTipoDocumento()
				.setMovimento()
				.setDocumento()
				.setUsuario()
				.setArquivos()
				.build();
		return protocoloAdaptado;
	}
	
	public BaseProtocoloDTOBuilder ait() throws Exception{
		dadosProtocolo.setAit(aitRepository.get(dadosProtocolo.getAit().getCdAit()));

		return this;
	}

	public BaseProtocoloDTOBuilder tipoDocumento() throws Exception{
		dadosProtocolo.setTipoDocumento(tipoDocumentoRepository.get(dadosProtocolo.getDocumento().getCdTipoDocumento()));

		return this;
	}

	public BaseProtocoloDTOBuilder fase(int cdSituacaoDocumento) throws Exception{
		if(cdSituacaoDocumento == getParamValue("CD_SITUACAO_PENDENTE")) {	
			int cdFasePendente = getParamValue("CD_FASE_PENDENTE");
			dadosProtocolo.setFase(faseRepository.get(cdFasePendente));
			dadosProtocolo.getDocumento().setCdFase(cdFasePendente);
		} else {
			int cdFaseJulgado = getParamValue("CD_FASE_JULGADO");
			dadosProtocolo.setFase(faseRepository.get(cdFaseJulgado));
			dadosProtocolo.getDocumento().setCdFase(cdFaseJulgado);
		}

		return this;
	}

	public BaseProtocoloDTOBuilder movimento() throws Exception {
		AitMovimento movimento = new AitMovimento();
		movimento.setCdAit(dadosProtocolo.getAit().getCdAit());
		movimento.setDtMovimento(dadosProtocolo.getDocumento().getDtProtocolo());
		movimento.setTpStatus(Integer.parseInt(dadosProtocolo.getTipoDocumento().getIdTipoDocumento()));
		movimento.setDsObservacao(dadosProtocolo.getDocumento().getTxtDocumento());
		movimento.setDtDigitacao(new GregorianCalendar());
		movimento.setCdUsuario(dadosProtocolo.getUsuario().getCdUsuario());
		movimento.setNrProcesso(generateNrProcesso());
		movimento.setCdOcorrencia(new ProtocoloCodigoOcorrenciaFactory().strategy(dadosProtocolo));
		
		dadosProtocolo.setAitMovimento(movimento);
		return this;
	}

	private String generateNrProcesso() {
		if (dadosProtocolo.getAitMovimento().getNrProcesso() != null) {
			return dadosProtocolo.getAitMovimento().getNrProcesso();
		} else 
			return dadosProtocolo.getDocumento().getNrDocumento();
	}
	
	public ProtocoloDTO build() {
		return dadosProtocolo;
	}
	
	private void generateNrProtocolo(CustomConnection customConnection) throws Exception {
		IGerarNumeroProtocoloGenerator numeroProtocoloGenerator = new NumeroProtocoloGeneratorFactory().gerarNumero(OrgaoServices.getCidadeOrgaoAutuador());
		numeroProtocoloGenerator.generate(dadosProtocolo.getDocumento(), this.protocoloInsertDTO.getCdAit(), customConnection);
	}
	
	private int getParamValue(String paramValue) throws Exception {
		int value = ParametroServices.getValorOfParametroAsInteger(paramValue, 0);
		
		if(value <= 0) 
			throw new Exception("Parâmetro "+ paramValue +" não configurado");
		
		return value;
	}
	
}
