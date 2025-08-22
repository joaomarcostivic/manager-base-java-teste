package com.tivic.manager.mob;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.CodigoCancelamentoMovimentoMap;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.lote.impressao.AitMovimentoPenalidadeBuilder;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class GerarAitMovimentoNip implements IGerarAitMovimento{
	
	private AitRepository aitRepository;
	private IAitMovimentoService aitMovimentoServices;
	private AitMovimentoRepository movimentoRepository;
	
	public GerarAitMovimentoNip() throws Exception {
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.movimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
	}
	
	@Override
	public AitMovimento generate(int cdAit, int cdUsuario, CustomConnection customConnection) throws ValidacaoException, Exception {
		AitMovimento aitMovimentoCancelamento= this.aitMovimentoServices.getMovimentoTpStatus(cdAit, new CodigoCancelamentoMovimentoMap().get(TipoStatusEnum.NIP_ENVIADA.getKey()));
		AitMovimento aitMovimentoNIP = this.aitMovimentoServices.getMovimentoTpStatus(cdAit, TipoStatusEnum.NIP_ENVIADA.getKey());
		if(aitMovimentoCancelamento.getCdMovimento() > 0) {
			return aitMovimentoCancelamento.getDtMovimento().before(aitMovimentoNIP.getDtMovimento()) ? aitMovimentoNIP 
					: criarMovimento(cdAit, cdUsuario, customConnection);
		}
		return aitMovimentoNIP.getCdMovimento() > 0 ? aitMovimentoNIP 
				: criarMovimento(cdAit, cdUsuario, customConnection);
	}

	private AitMovimento criarMovimento(int cdAit, int cdUsuario, CustomConnection customConnection) throws Exception {
		AitMovimento aitMovimentoPenalidade = new AitMovimentoPenalidadeBuilder(cdAit, cdUsuario).build();
		this.movimentoRepository.insert(aitMovimentoPenalidade, customConnection);
		Ait ait =  this.aitRepository.get(cdAit, customConnection);
		ait.setNrNotificacaoNip(getNrNotificacaoNip(customConnection));
		this.aitRepository.update(ait, customConnection);
		return aitMovimentoPenalidade;
	}
	
	private int getNrNotificacaoNip(CustomConnection customConnection) throws ValidacaoException {
		int nrNotificacaoNip = Integer.valueOf(ParametroServices.getValorOfParametro("MOB_SEQUENCIA_NR_NIP", customConnection.getConnection())) + 1;		
		new AitReportNipDAO(customConnection.getConnection()).updateParamNrSequenceNip(nrNotificacaoNip);
		return nrNotificacaoNip;
	}
}
