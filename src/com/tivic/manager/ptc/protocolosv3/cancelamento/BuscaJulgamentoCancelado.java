package com.tivic.manager.ptc.protocolosv3.cancelamento;

import java.util.HashMap;
import java.util.LinkedHashMap;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.ptc.protocolosv3.ProtocoloDTO;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;

public class BuscaJulgamentoCancelado {
	
	private ProtocoloDTO protocolo;
	private HashMap<Integer, Integer> julgamentoDeferidoMap;
	private HashMap<Integer, Integer> julgamentoIndeferidoMap;
	private IAitMovimentoService aitMovimentoServices;
	
	public BuscaJulgamentoCancelado(ProtocoloDTO protocolo) throws Exception {
		this.protocolo = protocolo;
		this.julgamentoDeferidoMap = new LinkedHashMap<Integer, Integer>();
		this.julgamentoIndeferidoMap = new LinkedHashMap<Integer, Integer>();
		this.aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		initCodigoCancelamentoMovimentoDeferido();
		initCodigoCancelamentoMovimentoIndeferido();
	}
	
	public void pegarDados() throws Exception {
		AitMovimento aitMovimento = buscarMovimentoJulgamento();
		this.protocolo.setAitMovimento(aitMovimento);
		this.protocolo.getTipoDocumento().setIdTipoDocumento(String.valueOf(aitMovimento.getTpStatus()));
	}
	
	private void initCodigoCancelamentoMovimentoDeferido() {
		this.julgamentoDeferidoMap.put(TipoStatusEnum.DEFESA_PREVIA.getKey(), TipoStatusEnum.DEFESA_DEFERIDA.getKey());
		this.julgamentoDeferidoMap.put(TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey(), TipoStatusEnum.ADVERTENCIA_DEFESA_DEFERIDA.getKey());
		this.julgamentoDeferidoMap.put(TipoStatusEnum.RECURSO_JARI.getKey(), TipoStatusEnum.JARI_COM_PROVIMENTO.getKey());
		this.julgamentoDeferidoMap.put(TipoStatusEnum.RECURSO_CETRAN.getKey(), TipoStatusEnum.CETRAN_DEFERIDO.getKey());
	}
	
	private void initCodigoCancelamentoMovimentoIndeferido() {
		this.julgamentoIndeferidoMap.put(TipoStatusEnum.DEFESA_PREVIA.getKey(), TipoStatusEnum.DEFESA_INDEFERIDA.getKey());
		this.julgamentoIndeferidoMap.put(TipoStatusEnum.ADVERTENCIA_DEFESA_ENTRADA.getKey(), TipoStatusEnum.ADVERTENCIA_DEFESA_INDEFERIDA.getKey());
		this.julgamentoIndeferidoMap.put(TipoStatusEnum.RECURSO_JARI.getKey(), TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey());
		this.julgamentoIndeferidoMap.put(TipoStatusEnum.RECURSO_CETRAN.getKey(), TipoStatusEnum.CETRAN_INDEFERIDO.getKey());
	}
	
	private AitMovimento buscarMovimentoJulgamento() throws Exception {
		int statusJulgamento = identificarSituacaoDocumento();
		AitMovimento aitMovimento = this.aitMovimentoServices.getMovimentoTpStatus(this.protocolo.getAit().getCdAit(), statusJulgamento);
		if (aitMovimento.getCdMovimento() <= 0) {
			throw new ValidacaoException("O movimento de julgamento deste protocolo não foi encontrado.");
		}
		return aitMovimento;
	}
	
	private int identificarSituacaoDocumento() {
		int statusDocumento = Integer.valueOf(this.protocolo.getTipoDocumento().getIdTipoDocumento());
		int situacaoDocumento = this.protocolo.getSituacaoDocumento().getCdSituacaoDocumento();
		int codigoSituacaoDeferido = ParametroServices.getValorOfParametroAsInteger("CD_SITUACAO_DOCUMENTO_DEFERIDO", 0);
		int statusJulgamento = situacaoDocumento == codigoSituacaoDeferido ? this.julgamentoDeferidoMap.get(statusDocumento) : this.julgamentoIndeferidoMap.get(statusDocumento);
		return statusJulgamento;
	}
	
}
