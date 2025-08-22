package com.tivic.manager.ptc.protocolosv3.builders;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.IGerarNumeroProtocoloGenerator;
import com.tivic.manager.mob.NumeroProtocoloGeneratorFactory;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.ptc.fase.IFaseRepository;
import com.tivic.manager.ptc.protocolosv3.InsertProtocoloDTOBuilder;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.ptc.protocolosv3.ProtocoloInsertDTO;
import com.tivic.manager.ptc.tipodocumento.TipoDocumentoRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class BasePtcDTOPortalBuilder {
	
	private ProtocoloDTO protocoloDTO;
	private AitRepository aitRepository;
	private ProtocoloInsertDTO protocoloInsertDTO;
	private TipoDocumentoRepository tipoDocumentoRepository;
	private IFaseRepository faseRepository;
	
	public BasePtcDTOPortalBuilder(ProtocoloInsertDTO dadosProtocolo, CustomConnection customConnection) throws Exception {
		this.protocoloDTO = dadosProtocoloConverter(dadosProtocolo);
		aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.tipoDocumentoRepository = (TipoDocumentoRepository) BeansFactory.get(TipoDocumentoRepository.class);
		this.faseRepository = (IFaseRepository) BeansFactory.get(IFaseRepository.class);
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
	
	public BasePtcDTOPortalBuilder ait() throws Exception{
		this.protocoloDTO.setAit(aitRepository.get(this.protocoloDTO.getAit().getCdAit()));
		return this;
	}

	public BasePtcDTOPortalBuilder tipoDocumento() throws Exception{
		this.protocoloDTO.setTipoDocumento(tipoDocumentoRepository.get(this.protocoloDTO.getDocumento().getCdTipoDocumento()));

		return this;
	}
	
	public BasePtcDTOPortalBuilder movimento() throws Exception {
		AitMovimento movimento = new AitMovimento();
		movimento.setCdAit(this.protocoloDTO.getAit().getCdAit());
		movimento.setDtMovimento(new GregorianCalendar());
		movimento.setTpStatus(TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey());
		movimento.setDtDigitacao(new GregorianCalendar());
		movimento.setLgEnviadoDetran(TipoLgEnviadoDetranEnum.NAO_ENVIAR_CANCELADO.getKey());
		movimento.setCdUsuario(this.protocoloDTO.getUsuario().getCdUsuario());
		movimento.setNrProcesso(generateNrProcesso());
		this.protocoloDTO.setAitMovimento(movimento);
		
		return this;
	}
	
	public BasePtcDTOPortalBuilder fase(int cdSituacaoDocumento) throws Exception{
		int cdFasePendente = getParamValue("CD_FASE_PENDENTE");
		this.protocoloDTO.setFase(faseRepository.get(cdFasePendente));
		this.protocoloDTO.getDocumento().setCdFase(cdFasePendente);
		return this;
	}
	
	private String generateNrProcesso() {
		if(this.protocoloDTO.getDocumento().getNrDocumento() != null 
				&& this.protocoloDTO.getDocumento().getNrDocumento().length() > 16) {
			return this.protocoloDTO.getDocumento().getNrDocumento();
		} else 
			return this.protocoloDTO.getDocumento().getNrDocumentoExterno();
	}
	
	private void generateNrProtocolo(CustomConnection customConnection) throws Exception {
		IGerarNumeroProtocoloGenerator numeroProtocoloGenerator = new NumeroProtocoloGeneratorFactory().gerarNumero(OrgaoServices.getCidadeOrgaoAutuador());
		numeroProtocoloGenerator.generate(this.protocoloDTO.getDocumento(), this.protocoloInsertDTO.getCdAit(), customConnection);
	}
	
	private int getParamValue(String paramValue) throws Exception {
		int value = ParametroServices.getValorOfParametroAsInteger(paramValue, 0);
		
		if(value <= 0) 
			throw new Exception("Parâmetro "+ paramValue +" não configurado");
		
		return value;
	}
	
	public ProtocoloDTO build() {
		return this.protocoloDTO;
	}
}
