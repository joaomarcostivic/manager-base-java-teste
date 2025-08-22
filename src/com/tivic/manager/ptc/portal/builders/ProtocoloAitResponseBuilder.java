package com.tivic.manager.ptc.portal.builders;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.ptc.portal.response.AitProtocoloResponse;
import com.tivic.manager.ptc.tipodocumento.ITipoDocumentoService;
import com.tivic.manager.ptc.tipodocumento.TipoProtocolo;
import com.tivic.sol.cdi.BeansFactory;

public class ProtocoloAitResponseBuilder {
	private final AitProtocoloResponse aitProtocolo;
	private ITipoDocumentoService documentoService;
	private IParametroRepository parametroRepository;
	
	
	public ProtocoloAitResponseBuilder(AitProtocoloResponse aitProtocolo) throws BadRequestException, Exception {	
		this.documentoService = (ITipoDocumentoService) BeansFactory.get(ITipoDocumentoService.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		this.aitProtocolo = aitProtocolo;
		setProtocoloDisponivel();
	}
	
	private void setProtocoloDisponivel() throws BadRequestException, Exception {
		aitProtocolo.setSolicitacaoJariDisponivel(verificarProtocoloDisponivel(TipoStatusEnum.RECURSO_JARI.getKey()));
		aitProtocolo.setSolicitacaoDefesaPreviaDisponivel(verificarProtocoloDisponivel(TipoStatusEnum.DEFESA_PREVIA.getKey()));
		aitProtocolo.setSolicitacaoCetranDisponivel(verificarProtocoloDisponivel(TipoStatusEnum.RECURSO_CETRAN.getKey()));
		aitProtocolo.setSolicitacaoFiciDisponivel(verificarDisponibilidadeFici());
	}
	
	private boolean verificarDisponibilidadeFici() throws BadRequestException, Exception {
		GregorianCalendar dataAtual = new GregorianCalendar();
		if(aitProtocolo.getDtPrazoDefesa() == null) {
			throw new Exception("Não existe prazo de defesa, verifique os dados do AIT.");
		}
		GregorianCalendar dataPrazoDefesa = aitProtocolo.getDtPrazoDefesa();
		dataPrazoDefesa.set(Calendar.HOUR, 23);
		dataPrazoDefesa.set(Calendar.MINUTE, 59);
		dataPrazoDefesa.set(Calendar.SECOND, 59);
		
		Boolean aceitaFiciForaDoPrazo = this.parametroRepository.getValorOfParametroAsBoolean("LG_FICI_FORA_DO_PRAZO");
		if(dataAtual.before(dataPrazoDefesa) || aceitaFiciForaDoPrazo) {
			return verificarProtocoloDisponivel(TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey());
        }
		return false;	
	}
	
	private boolean verificarProtocoloDisponivel(int tpProtocolo) throws BadRequestException, Exception {
		List<TipoProtocolo> protocolosDisponiveis = documentoService.getTiposDisponiveis(aitProtocolo.getCdAit());
		return protocolosDisponiveis.stream()
                .anyMatch(protocolo -> protocolo.getTpStatus() == tpProtocolo);
	}
	
	public AitProtocoloResponse build() {
		return aitProtocolo;
	}

	
}
