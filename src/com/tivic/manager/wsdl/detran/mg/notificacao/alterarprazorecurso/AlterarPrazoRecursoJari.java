package com.tivic.manager.wsdl.detran.mg.notificacao.alterarprazorecurso;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.IAitService;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class AlterarPrazoRecursoJari implements IAlterarPrazoRecurso {
	
	private IAitService aitService;
	private AitMovimentoRepository aitMovimentoRepository;
	private IAitMovimentoService aitMovimentoServices;
	
	public AlterarPrazoRecursoJari() throws Exception {
		this.aitService = (IAitService) BeansFactory.get(IAitService.class);
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.aitMovimentoServices = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class); 
	}

	@Override
	public AitDetranObject alterar(AlteraPrazoRecursoDTO alteraPrazoRecursoDTO) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		AitMovimento aitMovimento;
		try {
			customConnection.initConnection(true);
			Ait ait = aitService.get(alteraPrazoRecursoDTO.getCdAit(), customConnection);
			ait.setDtVencimento(alteraPrazoRecursoDTO.getNovoPrazoRecurso());
			aitMovimento = this.aitMovimentoServices
					.getMovimentoTpStatus(alteraPrazoRecursoDTO.getCdAit(), TipoStatusEnum.NOVO_PRAZO_JARI.getKey());
			if (aitMovimento.getCdMovimento() <= 0) {
				aitMovimento = new MovimentoNovoPrazoRecursoBuilder(alteraPrazoRecursoDTO, TipoStatusEnum.NOVO_PRAZO_JARI.getKey())
						.build();
				aitMovimentoRepository.insert(aitMovimento, customConnection);
			}
			AitDetranObject aitDetranObject = new AitDetranObject(ait, aitMovimento);
			customConnection.finishConnection();
			return aitDetranObject;
		} finally {
			customConnection.closeConnection();
		}
	}

}
