package com.tivic.manager.wsdl.detran.mg.geradorlotenotificacao;

import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.infracao.InfracaoRepository;
import com.tivic.manager.mob.lotes.enums.impressao.TipoLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.factory.impressao.ListAitsCandidatosNaiFactory;
import com.tivic.manager.mob.lotes.factory.impressao.ListAitsCandidatosNipFactory;
import com.tivic.manager.mob.lotes.model.impressao.LoteImpressao;
import com.tivic.manager.mob.lotes.service.impressao.ILoteImpressaoService;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class GeraLoteNotificacaoImport implements IGeraLoteNotificacaoImport {
	
	private Ait ait;
	private CustomConnection customConnection;
	private ILoteImpressaoService loteImpressaoService;
	private InfracaoRepository infracaoRepository;
	private AitRepository aitRepository;
	private int tpStatus;
	
	public GeraLoteNotificacaoImport(Ait ait, int tpStatus, CustomConnection customConnection) throws Exception {
		this.ait = ait;
		this.tpStatus = tpStatus;
		this.customConnection = customConnection;
		this.loteImpressaoService = (ILoteImpressaoService) BeansFactory.get(ILoteImpressaoService.class);
		this.infracaoRepository = (InfracaoRepository) BeansFactory.get(InfracaoRepository.class);
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
	}

	@Override
	public void build() throws Exception {
		if(TipoLoteImpressaoEnum.LOTE_NAI_VIA_UNICA.getKey() == this.tpStatus) {
			List<Ait> aitList = new ListAitsCandidatosNaiFactory()
					.getEstrategiaListAits(TipoLoteImpressaoEnum.LOTE_NAI_VIA_UNICA.getKey(), new SearchCriterios())
					.build(searchNotificacaoViaUnica().getCriterios(), customConnection.getConnection());
			LoteImpressao loteNotificacaoNaiViaUnica = this.loteImpressaoService.gerarLoteNotificacaoAitViaUnica(aitList, ait.getCdUsuario(), TipoLoteImpressaoEnum.LOTE_NAI.getKey());
			pegarValorInfracao();
			this.loteImpressaoService.gerarDocsLoteAitViaUnica(loteNotificacaoNaiViaUnica, ait.getCdUsuario(), customConnection);
		} else if(TipoLoteImpressaoEnum.LOTE_NIP_VIA_UNICA.getKey() == this.tpStatus) {
			List<Ait> aitList = new ListAitsCandidatosNipFactory()
					.getEstrategiaListAits(TipoLoteImpressaoEnum.LOTE_NIP_VIA_UNICA.getKey(), searchNotificacaoViaUnica(), customConnection)
					.build();
			LoteImpressao loteNotificacaoNipViaUnica = this.loteImpressaoService.gerarLoteNotificacaoAitViaUnica(aitList, this.ait.getCdUsuario(), TipoLoteImpressaoEnum.LOTE_NIP.getKey());
			pegarValorInfracao();
			this.loteImpressaoService.gerarDocsLoteAitViaUnica(loteNotificacaoNipViaUnica, this.ait.getCdUsuario(), customConnection);
		}
	}
	
	private SearchCriterios searchNotificacaoViaUnica() {
		SearchCriterios search = new SearchCriterios();
		search.addCriteriosEqualInteger("A.cd_ait", this.ait.getCdAit(), true);
		return search;
	}
	
	private void pegarValorInfracao() throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			if (this.ait.getVlMulta() == null) {
				Infracao infracao = this.infracaoRepository.get(this.ait.getCdInfracao());
				this.ait = this.aitRepository.get(this.ait.getCdAit());
				this.ait.setVlMulta(infracao.getVlInfracao());
				this.aitRepository.update(ait, customConnection);
			}
			customConnection.finishConnection();
		} finally {
			customConnection.closeConnection();
		}
	}

}
