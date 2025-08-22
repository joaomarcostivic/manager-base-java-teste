package com.tivic.manager.mob;

import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.lote.impressao.builders.AitMovimentoNAIBuilder;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class GerarAitMovimentoNai implements IGerarAitMovimento{
	
	private IAitMovimentoService aitMovimentoServices;
	private AitMovimentoRepository movimentoRepository;
	
	public GerarAitMovimentoNai() throws Exception {
		this.aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.movimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
	}
	
	public AitMovimento generate(int cdAit, int cdUsuario, CustomConnection customConnection) throws Exception {
		AitMovimento aitMovimento = aitMovimentoServices.getMovimentoTpStatus(cdAit, TipoStatusEnum.NAI_ENVIADO.getKey());
		if (aitMovimento.getCdMovimento() > 0) {
			return aitMovimento;
		}
		AitMovimento movimentoNAI = new AitMovimentoNAIBuilder(cdAit, cdUsuario).build();
		this.movimentoRepository.insert(movimentoNAI, customConnection);
		return movimentoNAI;
	}
	
}
