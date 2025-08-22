package com.tivic.manager.mob.lote.impressao.publicacao.service;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import com.tivic.manager.mob.grafica.LoteImpressao;
import com.tivic.manager.mob.grafica.LoteImpressaoAit;
import com.tivic.manager.mob.grafica.LoteImpressaoAitSituacao;
import com.tivic.manager.mob.grafica.LoteImpressaoSituacao;
import com.tivic.manager.mob.lote.impressao.ILoteNotificacaoService;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoBuilder;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoAitBuilder.LoteImpressaoAitBuilder;
import com.tivic.manager.mob.lote.impressao.publicacao.dto.NotificacaoPublicacaoPendenteDto;
import com.tivic.manager.util.Util;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class GeraLotePublicacaoNotificacao {

	private ILoteNotificacaoService loteNotificacaoService;
	private int cdUsuario;
	private int tpDocumento;
	private List<NotificacaoPublicacaoPendenteDto> notificacaoPublicacaoPendenteDtos;
	
	public GeraLotePublicacaoNotificacao(int cdUsuario, int tpDocumento, List<NotificacaoPublicacaoPendenteDto> notificacaoPublicacaoPendenteDtos) throws Exception {
		loteNotificacaoService = (ILoteNotificacaoService) BeansFactory.get(ILoteNotificacaoService.class);
		this.cdUsuario = cdUsuario;
		this.tpDocumento = tpDocumento;
		this.notificacaoPublicacaoPendenteDtos = notificacaoPublicacaoPendenteDtos;
	}
	
	public LoteImpressao gerarLoteImpressao(CustomConnection customConnection) throws Exception {
		LoteImpressao loteImpressao = new LoteImpressaoBuilder()
				.addCdUsuario(this.cdUsuario)
				.addDtCriacao(new GregorianCalendar())
				.addIdLoteImpressao(Util.generateRandomString(5))
				.addQtdDocumentos(this.notificacaoPublicacaoPendenteDtos.size())
				.addStLoteImpressao(LoteImpressaoSituacao.ARQUIVO_PUBLICACAO_DIARIO_OFICIAL_GERADO)
				.addTpDocumento(this.tpDocumento)
				.build();
		
		List<LoteImpressaoAit> listLoteImpressaoAit = new ArrayList<LoteImpressaoAit>();
		for (NotificacaoPublicacaoPendenteDto publicaNotificacaoDto: notificacaoPublicacaoPendenteDtos) {
			LoteImpressaoAit loteImpressaoAit = new LoteImpressaoAitBuilder()
					.addCdAit(publicaNotificacaoDto.getCdAit())
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
