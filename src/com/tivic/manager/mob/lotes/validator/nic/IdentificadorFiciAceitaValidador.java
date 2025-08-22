package com.tivic.manager.mob.lotes.validator.nic;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.ptc.documento.Documento;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class IdentificadorFiciAceitaValidador implements INICValidador {
	private AitMovimentoRepository aitMovimentoRepository;
	private IParametroRepository parametroRepository;
	
	public IdentificadorFiciAceitaValidador() throws Exception {
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}
	
	@Override
	public boolean validate(Ait ait, CustomConnection customConnection) throws Exception, ValidacaoException {
		return !(existeApresentacaoCondutorDeferido(ait, customConnection));
	}
	
	private boolean existeApresentacaoCondutorDeferido(Ait ait, CustomConnection customConnection) throws Exception {
		for (AitMovimento aitMovimento : getMovimentoList(ait.getCdAit(), customConnection)) {
			if(aitMovimento.getTpStatus() == TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey())
				return condutorDeferido(ait.getCdAit(), aitMovimento.getCdMovimento());			
		}
		return false;
	}
	
	private List<AitMovimento> getMovimentoList(int cdAit, CustomConnection customConnection) throws Exception {
		List<AitMovimento> aitMovimentoList = this.aitMovimentoRepository.find(montaSearchCriterios(cdAit), customConnection);
		return aitMovimentoList;
	}
	
	private boolean condutorDeferido(int cdAit, int cdMovimento) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("A.cd_movimento", cdMovimento);
		Search<Documento> search = new SearchBuilder<Documento>("mob_ait_movimento_documento A")
			.addField("B.cd_situacao_documento")
			.addJoinTable("JOIN ptc_documento B ON (A.cd_documento = B.cd_documento)")
			.searchCriterios(searchCriterios)
			.orderBy("B.dt_protocolo DESC")
			.build();
		Documento documento = search.getList(Documento.class).get(0);		
		return documento.getCdSituacaoDocumento() == getCdDocumentoDeferido();
	}
	
	private int getCdDocumentoDeferido() throws Exception {
		int cdParametro = parametroRepository.getValorOfParametroAsInt("CD_SITUACAO_DOCUMENTO_DEFERIDO");
		if(cdParametro <= 0)
			throw new BadRequestException("O parâmetro CD_SITUACAO_DOCUMENTO_DEFERIDO não foi configurado.");	
		return cdParametro;
	}
	
	private SearchCriterios montaSearchCriterios(int cdAit) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", cdAit);
		return searchCriterios;
	}
}
