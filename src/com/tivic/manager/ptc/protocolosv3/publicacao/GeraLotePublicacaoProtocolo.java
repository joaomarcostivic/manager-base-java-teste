package com.tivic.manager.ptc.protocolosv3.publicacao;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.lotes.builders.LoteBuilder;
import com.tivic.manager.mob.lotes.builders.publicacao.LotePublicacaoAitBuilder;
import com.tivic.manager.mob.lotes.builders.publicacao.LotePublicacaoBuilder;
import com.tivic.manager.mob.lotes.enums.impressao.StatusLoteImpressaoEnum;
import com.tivic.manager.mob.lotes.model.Lote;
import com.tivic.manager.mob.lotes.model.publicacao.LotePublicacao;
import com.tivic.manager.mob.lotes.model.publicacao.LotePublicacaoAit;
import com.tivic.manager.mob.lotes.repository.LoteRepository;
import com.tivic.manager.mob.lotes.service.publicacao.ILotePublicacaoService;
import com.tivic.manager.util.Util;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.util.date.DateUtil;

public class GeraLotePublicacaoProtocolo {
	
	private ILotePublicacaoService lotePublicacaoService;
	private int cdUsuario;
	private String idTipoDocumento;
	private List<ProtocoloPublicacaoPendenteDto> publicaProtocoloDtoList;
	private LoteRepository loteRepository;

	public GeraLotePublicacaoProtocolo(int cdUsuario, String idTipoDocumento, List<ProtocoloPublicacaoPendenteDto> publicaProtocoloDtoList) throws Exception {
		lotePublicacaoService = (ILotePublicacaoService) BeansFactory.get(ILotePublicacaoService.class);
		loteRepository = (LoteRepository) BeansFactory.get(LoteRepository.class);
		this.cdUsuario = cdUsuario;
		this.idTipoDocumento = idTipoDocumento;
		this.publicaProtocoloDtoList = publicaProtocoloDtoList;
	}

    public LotePublicacao gerarLoteImpressao(CustomConnection customConnection) throws Exception {
    	Lote lote = criarLote(customConnection);
        LotePublicacao lotePublicacao = new LotePublicacaoBuilder()
        		.setCdLote(lote.getCdLote())
        		.setTpPublicacao(new DocumentoPublicacaoMap().getCodPublicacao(Integer.valueOf(this.idTipoDocumento)))
                .build();
        List<LotePublicacaoAit> listLoteImpressaoAit = gerarLotePublicacaoAits(publicaProtocoloDtoList);
        lotePublicacao.setAits(listLoteImpressaoAit);
        return this.lotePublicacaoService.save(lotePublicacao, customConnection);
    }
    
    private Lote criarLote(CustomConnection customConnection) throws Exception {
	    Lote lote = new LoteBuilder()
	        .setDtCriacao(DateUtil.getDataAtual())
	        .setCdCriador(this.cdUsuario)
	        .setIdLote(Util.generateRandomString(5))
	        .build();
	    loteRepository.insert(lote, customConnection);
	    return lote;
	}

    private List<LotePublicacaoAit> gerarLotePublicacaoAits(List<ProtocoloPublicacaoPendenteDto> aitList) {
        List<LotePublicacaoAit> listLotePublicacaoAit = new ArrayList<>();

        for (ProtocoloPublicacaoPendenteDto notificacaoPublicacaoPendenteDto : aitList) {
        	LotePublicacaoAit lotePublicacaoAit = new LotePublicacaoAitBuilder()
    				.setCdAit(notificacaoPublicacaoPendenteDto.getCdAit())
    				.build();
        	listLotePublicacaoAit.add(lotePublicacaoAit);
        }
        return listLotePublicacaoAit;
    }
	
}
