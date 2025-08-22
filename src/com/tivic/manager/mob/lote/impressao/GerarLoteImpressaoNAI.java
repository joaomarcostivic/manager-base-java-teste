package com.tivic.manager.mob.lote.impressao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.grafica.LoteImpressaoAitSituacao;
import com.tivic.manager.mob.grafica.LoteImpressaoSituacao;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoAitBuilder.LoteImpressaoAitBuilder;
import com.tivic.manager.util.Util;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class GerarLoteImpressaoNAI implements IGerarLoteImpressao{
	private ILoteNotificacaoService loteNotificacaoService;
	
	public GerarLoteImpressaoNAI() throws Exception {
		loteNotificacaoService = (ILoteNotificacaoService) BeansFactory.get(ILoteNotificacaoService.class);
	}
	
	public LoteImpressao gerarLoteImpressao(List<Ait> aitList, int cdUsuario, CustomConnection customConnection) throws Exception {
		LocalDateTime now = LocalDateTime.now();
		String idLoteImpressao = String.format("NAI/%s", now.format(DateTimeFormatter.ofPattern("MMddHHmm")));
		LoteImpressao loteImpressao = new LoteImpressaoBuilder()
				.addCdUsuario(cdUsuario)
				.addDtCriacao(new GregorianCalendar())
				.addIdLoteImpressao(idLoteImpressao)
				.addQtdDocumentos(aitList.size())
				.addStLoteImpressao(LoteImpressaoSituacao.AGUARDANDO_IMPRESSAO)
				.addTpDocumento(TipoLoteNotificacaoEnum.LOTE_NAI.getKey())
				.build();
		List<LoteImpressaoAit> listLoteImpressaoAit = new ArrayList<LoteImpressaoAit>();
		for (Ait ait: aitList) {
			LoteImpressaoAit loteImpressaoAit = new LoteImpressaoAitBuilder()
					.addCdAit(ait.getCdAit())
					.addDtInclusao(new GregorianCalendar())
					.addIdLoteImpressaoAit(Util.generateRandomString(5))
					.addStImpressao(LoteImpressaoAitSituacao.AGUARDANDO_GERACAO)
					.build();
			listLoteImpressaoAit.add(loteImpressaoAit);	
		}
		loteImpressao.setAits(listLoteImpressaoAit);
		return loteNotificacaoService.save(loteImpressao, customConnection);
	}
}
