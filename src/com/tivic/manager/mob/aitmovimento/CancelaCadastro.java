package com.tivic.manager.mob.aitmovimento;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.TipoSituacaoAitEnum;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.AitMovimentoBuilder;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class CancelaCadastro {
	
	private AitMovimentoRepository aitMovimentoRepository;
	private IAitMovimentoService aitMovimentoService;
	private AitRepository aitRepository;
	private CustomConnection customConnection;
	private Ait ait;
	private int cdOcorrencia;
	private int cdUsuario;

	public CancelaCadastro(Ait ait, int cdOcorrencia, int cdUsuario, CustomConnection customConnection) throws Exception {
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.customConnection = customConnection;
		this.ait = ait;
		this.cdOcorrencia = cdOcorrencia;
		this.cdUsuario = cdUsuario;
		this.cancelar();
	}
	
	private void cancelar() throws Exception {
		
		AitMovimento cancelamentoCadastro = new AitMovimentoBuilder()
				.setCdAit(ait.getCdAit())
				.setCdOcorrencia(this.cdOcorrencia)
				.setDtMovimento(new GregorianCalendar())
				.setTpStatus(TipoStatusEnum.CADASTRO_CANCELADO.getKey())
				.setLgEnviadoDetran(TipoSituacaoAitEnum.MOV_SEM_ENVIO.getKey())
				.setDtDigitacao(new GregorianCalendar())
				.setCdUsuario(this.cdUsuario)
			.build();
		
		this.aitMovimentoRepository.insert(cancelamentoCadastro, customConnection);
		List<AitMovimento> movimentoRegistroList = this.aitMovimentoService.find(getCriteriosMovimentoResgistro(ait.getCdAit()), customConnection);

		if(movimentoRegistroList.size() > 0) {
			AitMovimento movimentoRegistro = movimentoRegistroList.get(0);
			movimentoRegistro.setLgEnviadoDetran(AitMovimentoServices.NAO_ENVIAR);
			this.aitMovimentoRepository.update(movimentoRegistro, customConnection);
			ait.setCdMovimentoAtual(cancelamentoCadastro.getCdMovimento());
			ait.setTpStatus(cancelamentoCadastro.getTpStatus());
			this.aitRepository.update(ait, customConnection);
		}
	}
	
	private SearchCriterios getCriteriosMovimentoResgistro(int cdAit) {
		SearchCriterios searchCriteios = new SearchCriterios();
		searchCriteios.addCriteriosEqualInteger("cd_ait", cdAit, true);
		searchCriteios.addCriteriosEqualInteger("tp_status", TipoStatusEnum.REGISTRO_INFRACAO.getKey(), true);
		return searchCriteios;
	}
}
