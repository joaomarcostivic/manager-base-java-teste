package com.tivic.manager.wsdl.detran.mg.recursofici;

import java.util.List;

import javax.ws.rs.BadRequestException;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoDocumento;
import com.tivic.manager.mob.AitMovimentoDocumentoServices;
import com.tivic.manager.mob.aitmovimentodocumento.AitMovimentoDocumentoRepository;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.ptc.fici.ApresentacaoCondutorRepository;
import com.tivic.manager.wsdl.detran.mg.TabelasAuxiliaresMG;
import com.tivic.manager.wsdl.detran.mg.validators.IncluirFiciValidators;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class DadosCondutorBuilder {
	private final String UF_ESTRANGEIRA = "EX";
	private RecursoFiciDadosEntrada incluirFiciDadosEntrada;
	private TabelasAuxiliaresMG tabelasAuxiliares;
	private AitMovimentoDocumentoServices aitMovimentoDocumentoServices;
	private AitMovimentoDocumentoRepository aitMovimentoDocumentoRepository;
	private ApresentacaoCondutorRepository apresentacaoCondutorRepository;

	public DadosCondutorBuilder(RecursoFiciDadosEntrada dadosFici) throws Exception{
		this.incluirFiciDadosEntrada = dadosFici;
		this.tabelasAuxiliares = new TabelasAuxiliaresMG();
		this.aitMovimentoDocumentoServices = (AitMovimentoDocumentoServices) BeansFactory.get(AitMovimentoDocumentoServices.class);
		this.apresentacaoCondutorRepository = (ApresentacaoCondutorRepository) BeansFactory.get(ApresentacaoCondutorRepository.class);
		this.aitMovimentoDocumentoRepository = (AitMovimentoDocumentoRepository) BeansFactory.get(AitMovimentoDocumentoRepository.class);
	}

	public DadosCondutorBuilder dadosFormulario(AitMovimento aitMovimento) throws ValidacaoException, Exception {
		incluirCnhFormulario(aitMovimento);
		incluirCondutorFormulario(aitMovimento);

		return this;
	}
	
	public DadosCondutorBuilder dadosApresentacaoCondutor(AitMovimento aitMovimento) throws ValidacaoException, Exception{
		ApresentacaoCondutor apresentacaoCondutor = getApresentacaoCondutor(aitMovimento);
		validateIncluirFici(apresentacaoCondutor, new CustomConnection());
		incluirCnhApresentacaoCondutor(apresentacaoCondutor);
		incluirCondutorApresentacaoCondutor(apresentacaoCondutor);
		
		return this;
	}
	private void validateIncluirFici(ApresentacaoCondutor documento, CustomConnection connection) throws ValidacaoException, Exception {
		new IncluirFiciValidators().validate(documento, connection);
	}
	
	public RecursoFiciDadosEntrada build() {
		return incluirFiciDadosEntrada;
	}

	private void incluirCnhApresentacaoCondutor(ApresentacaoCondutor apresentacaoCondutor) throws BadRequestException, Exception {
		incluirFiciDadosEntrada = new DadosCnhFactory(apresentacaoCondutor, incluirFiciDadosEntrada).strategy();
    }
	
	private void incluirCondutorApresentacaoCondutor(ApresentacaoCondutor apresentacaoCondutor) {
		incluirFiciDadosEntrada.setNomeCondutor(apresentacaoCondutor.getNmCondutor());
		incluirFiciDadosEntrada.setRgCondutor(apresentacaoCondutor.getNrRg());
		incluirFiciDadosEntrada.setCpfCondutor(apresentacaoCondutor.getNrCpfCnpj());
	}
	
	private ApresentacaoCondutor getApresentacaoCondutor(AitMovimento aitMovimento) throws ValidacaoException, Exception{
		AitMovimentoDocumento aitMovimentoDocumento = aitMovimentoDocumentoRepository.getAit(aitMovimento.getCdAit(), aitMovimento.getCdMovimento());
		
		SearchCriterios criterios = new SearchCriterios();
		criterios.addCriteriosEqualInteger("cd_documento", aitMovimentoDocumento.getCdDocumento(), aitMovimentoDocumento.getCdDocumento() > 0);
		List<ApresentacaoCondutor> ficis = apresentacaoCondutorRepository.find(criterios);
		
		if(ficis.isEmpty()) {
			throw new ValidacaoException("Não foi encontrado Apresentação de Condutor");
		}
		
		return ficis.get(0);
	}
	
	private String isParamNull(Object param) {
		if (param != null)
			return param.toString();
		else
			return null;
	}

	private void incluirCnhFormulario(AitMovimento aitMovimento) throws ValidacaoException, Exception{
		if (aitMovimentoDocumentoServices.getAtributoByMovimento(aitMovimento, "modelCnhCondutor") != null) {
			if (aitMovimentoDocumentoServices.getAtributoByMovimento(aitMovimento, "modelCnhCondutor").toString() != "")
				incluirFiciDadosEntrada.setModeloCnh(tabelasAuxiliares.getTipoCnh(Integer.parseInt(isParamNull(
						aitMovimentoDocumentoServices.getAtributoByMovimento(aitMovimento, "modelCnhCondutor")))));
			else
				incluirFiciDadosEntrada.setModeloCnh(TabelasAuxiliaresMG.TP_CNH_RENACH);

			if (incluirFiciDadosEntrada.getModeloCnh() == TabelasAuxiliaresMG.TP_CNH_HABILITACAO_ESTRANGEIRA) {
				if (aitMovimentoDocumentoServices.getAtributoByMovimento(aitMovimento, "paisCnhCondutor") != null
						&& aitMovimentoDocumentoServices.getAtributoByMovimento(aitMovimento, "paisCnhCondutor") != "")
					incluirFiciDadosEntrada.setCodigoPaisCnh(Integer.parseInt(aitMovimentoDocumentoServices
							.getAtributoByMovimento(aitMovimento, "paisCnhCondutor").toString()));

				incluirFiciDadosEntrada.setUfCnh(UF_ESTRANGEIRA);
			} else {
				incluirFiciDadosEntrada.setUfCnh(isParamNull(
						aitMovimentoDocumentoServices.getAtributoByMovimento(aitMovimento, "ufCnhCondutor")));
				incluirFiciDadosEntrada.setNumeroCnh(isParamNull(
						aitMovimentoDocumentoServices.getAtributoByMovimento(aitMovimento, "nrCnhCondutor")));
			}
		}
	}
	
	private void incluirCondutorFormulario(AitMovimento aitMovimento) throws ValidacaoException, Exception{
		incluirFiciDadosEntrada.setNomeCondutor(aitMovimentoDocumentoServices.getAtributoByMovimento(aitMovimento, "nmCondutor").toString());	
		if(aitMovimentoDocumentoServices.getAtributoByMovimento(aitMovimento, "nrRgCondutor") != null && aitMovimentoDocumentoServices.getAtributoByMovimento(aitMovimento, "nrRgCondutor") != "")
			incluirFiciDadosEntrada.setRgCondutor(aitMovimentoDocumentoServices.getAtributoByMovimento(aitMovimento, "nrRgCondutor").toString());

		if(aitMovimentoDocumentoServices.getAtributoByMovimento(aitMovimento, "nrCpfCondutor") != null && aitMovimentoDocumentoServices.getAtributoByMovimento(aitMovimento, "nrCpfCondutor") != "")
			incluirFiciDadosEntrada.setCpfCondutor(aitMovimentoDocumentoServices.getAtributoByMovimento(aitMovimento, "nrCpfCondutor").toString().replaceAll("-", "").replaceAll(".", ""));
		
	}

}
