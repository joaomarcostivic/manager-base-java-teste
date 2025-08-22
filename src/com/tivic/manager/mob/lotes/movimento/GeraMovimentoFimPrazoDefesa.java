package com.tivic.manager.mob.lotes.movimento;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.IGerarAitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.CodigoCancelamentoMovimentoMap;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.lotes.builders.impressao.MovimentoFimPrazoDefesaBuilder;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class GeraMovimentoFimPrazoDefesa implements IGerarAitMovimento {
	private AitMovimentoRepository movimentoRepository;
	private AitRepository aitRepository;
	private IAitMovimentoService aitMovimentoServices;
	
	public GeraMovimentoFimPrazoDefesa() throws Exception {
		this.movimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}

	@Override
	public AitMovimento generate(int cdAit, int cdUsuario, CustomConnection customConnection) throws Exception {
		AitMovimento aitMovimentoCancelamento= this.aitMovimentoServices.getMovimentoTpStatus(cdAit, new CodigoCancelamentoMovimentoMap().get(TipoStatusEnum.FIM_PRAZO_DEFESA.getKey()));
		AitMovimento aitMovimentoFimPrazoDefesa = this.aitMovimentoServices.getMovimentoTpStatus(cdAit, TipoStatusEnum.FIM_PRAZO_DEFESA.getKey());
		if(aitMovimentoCancelamento.getCdMovimento() > 0) {
			return aitMovimentoCancelamento.getDtMovimento().before(aitMovimentoFimPrazoDefesa.getDtMovimento()) ? aitMovimentoFimPrazoDefesa 
					: criarMovimento(cdAit, cdUsuario, customConnection);
		}
		return aitMovimentoFimPrazoDefesa.getCdMovimento() > 0 ? aitMovimentoFimPrazoDefesa 
				: criarMovimento(cdAit, cdUsuario, customConnection);
	}

	private AitMovimento criarMovimento(int cdAit, int cdUsuario, CustomConnection customConnection) throws Exception {
		Ait ait = this.aitRepository.get(cdAit, customConnection);
		AitMovimento movimentoFimPrazoDefesa = new MovimentoFimPrazoDefesaBuilder(ait, cdUsuario).build();
		this.movimentoRepository.insert(movimentoFimPrazoDefesa, customConnection);
		return movimentoFimPrazoDefesa;
	}
}
