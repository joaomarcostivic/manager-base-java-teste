package com.tivic.manager.mob;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoAdvertenciaBuilder;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.CodigoCancelamentoMovimentoMap;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class GeraAitMovimentoAdvertencia implements IGerarAitMovimento {
	private AitRepository aitRepository;
	private AitMovimentoRepository aitMovimentoRepository;
	private IAitMovimentoService aitMovimentoService;
	
	public GeraAitMovimentoAdvertencia() throws Exception {
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}

	@Override
	public AitMovimento generate(int cdAit, int cdUsuario, CustomConnection customConnection) throws Exception {
		AitMovimento aitMovimentoCancelamento = this.aitMovimentoService.getMovimentoTpStatus(cdAit, new CodigoCancelamentoMovimentoMap().get(TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey()));
		AitMovimento aitMovimentoAdvertencia = this.aitMovimentoService.getMovimentoTpStatus(cdAit, TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey());
		if(aitMovimentoCancelamento.getCdMovimento() > 0) {
			return aitMovimentoCancelamento.getDtMovimento().before(aitMovimentoAdvertencia.getDtMovimento()) ? aitMovimentoAdvertencia
					: criaMovimento(cdAit, cdUsuario, customConnection);
		}
		return  aitMovimentoAdvertencia.getCdMovimento() > 0 ? aitMovimentoAdvertencia 
				: criaMovimento(cdAit, cdUsuario, customConnection);
	}
	
	private AitMovimento criaMovimento(int cdAit, int cdUsuario, CustomConnection customConnection) throws Exception {
		AitMovimento aitMovimentoAdvertencia = new AitMovimentoAdvertenciaBuilder(cdAit, cdUsuario).build();
		this.aitMovimentoRepository.insert(aitMovimentoAdvertencia, customConnection);
		this.atualizaAit(cdAit, customConnection);
		return aitMovimentoAdvertencia;
	}
	
	private void atualizaAit(int cdAit, CustomConnection customConnection) throws Exception {
		Ait ait =  this.aitRepository.get(cdAit, customConnection);
		ait.setNrNotificacaoNip(getNrNotificacaoNip(customConnection));
		this.aitRepository.update(ait, customConnection);
	}
	
	private int getNrNotificacaoNip(CustomConnection customConnection) throws ValidacaoException {
		int nrNotificacaoNip = Integer.valueOf(ParametroServices.getValorOfParametro("MOB_SEQUENCIA_NR_NIP", customConnection.getConnection())) + 1;		
		new AitReportNipDAO(customConnection.getConnection()).updateParamNrSequenceNip(nrNotificacaoNip);
		return nrNotificacaoNip;
	}
}
