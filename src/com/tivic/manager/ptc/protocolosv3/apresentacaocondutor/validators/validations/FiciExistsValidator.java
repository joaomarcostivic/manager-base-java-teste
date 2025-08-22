package com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.validations;

import java.util.List;
import com.tivic.manager.exception.ValidationException;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoDocumento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimentodocumento.AitMovimentoDocumentoRepository;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.documento.DocumentoRepository;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.ApresentacaoCondutorDTO;
import com.tivic.manager.ptc.protocolosv3.apresentacaocondutor.validators.IApresentacaoCondutorValidator;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class FiciExistsValidator implements IApresentacaoCondutorValidator {

	private AitMovimentoRepository aitMovimentoRepository;
	private DocumentoRepository documentoRepository;
	private AitMovimentoDocumentoRepository aitMovimentoDocumentoRepository;
	
	public FiciExistsValidator () throws Exception {
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.documentoRepository = (DocumentoRepository) BeansFactory.get(DocumentoRepository.class);
		this.aitMovimentoDocumentoRepository = (AitMovimentoDocumentoRepository) BeansFactory.get(AitMovimentoDocumentoRepository.class);
	}
	
	@Override
	public void validate(ApresentacaoCondutorDTO apresentacaoCondutor, CustomConnection connection) throws Exception, ValidacaoException {
		List<AitMovimento> movimentos = getMovimentosFICI(apresentacaoCondutor.getAit().getCdAit());
		if(checkExisteFiciAtiva(apresentacaoCondutor, movimentos))
			throw new ValidationException("Esse AIT já possui Apresentação de Condutor lançada");
	}
	
	private List<AitMovimento> getMovimentosFICI(int cdAit) throws Exception {
		SearchCriterios criterios = new SearchCriterios();
		criterios.addCriteriosEqualInteger("cd_ait", cdAit, cdAit > 0);
		criterios.addCriteriosEqualInteger("tp_status", TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey(), true);
		return aitMovimentoRepository.find(criterios);
	}
	
	private boolean checkExisteFiciAtiva(ApresentacaoCondutorDTO apresentacaoCondutor, List<AitMovimento> movimentos) throws Exception {
		for(AitMovimento movimento : movimentos) {
			return verificarCancelamento(apresentacaoCondutor, movimento) ? false : true;
		}
		return false;
	}
	
	private boolean verificarCancelamento(ApresentacaoCondutorDTO apresentacaoCondutor, AitMovimento movimento) throws Exception {
		SearchCriterios criterios = criteriosDocumentoMovimento(movimento);
		List<AitMovimentoDocumento> aitMovimentoDocumentoList = this.aitMovimentoDocumentoRepository.find(criterios);
		Documento documento = this.documentoRepository.get(aitMovimentoDocumentoList.get(0).getCdDocumento());
		int codigoCancelamento = ParametroServices.getValorOfParametroAsInteger("CD_FASE_CANCELADO", 0);
		return documento.getCdFase() == codigoCancelamento ? true : false;
	}
	
	private SearchCriterios criteriosDocumentoMovimento(AitMovimento aitMovimento) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", aitMovimento.getCdAit());
		searchCriterios.addCriteriosEqualInteger("cd_movimento", aitMovimento.getCdMovimento());
		return searchCriterios;
	}

}
