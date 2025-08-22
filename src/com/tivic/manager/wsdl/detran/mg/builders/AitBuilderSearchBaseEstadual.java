package com.tivic.manager.wsdl.detran.mg.builders;


import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.agente.IAgenteService;
import com.tivic.manager.mob.infracao.IInfracaoService;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.detran.mg.consultaautobaseestadual.ConsultaAutoBaseEstadualDadosRetorno;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.DadosRetorno;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.search.SearchCriterios;

public class AitBuilderSearchBaseEstadual extends AitBuilderSearchProdemge implements IAitBuilderSearchProdemge {

	private IInfracaoService infracaoService;
	private IAgenteService agenteService;
	
	public AitBuilderSearchBaseEstadual() throws Exception {
		this.infracaoService = (IInfracaoService) BeansFactory.get(IInfracaoService.class);
		this.agenteService = (IAgenteService) BeansFactory.get(IAgenteService.class);
	}
	
	public void build(Ait ait, DadosRetorno dadosRetorno) throws Exception{
		ConsultaAutoBaseEstadualDadosRetorno dados = (ConsultaAutoBaseEstadualDadosRetorno) dadosRetorno;
		
		if(validUpdate(ait.getNrRenavan(), dados.getCodigoRenavam())) 
			ait.setNrRenavan(dados.getCodigoRenavam());		
		
		if(validUpdate(ait.getNrPlaca(), dados.getPlaca()))
			ait.setNrPlaca(dados.getPlaca());
		
		if(validUpdate(ait.getSgUfVeiculo(), dados.getUfVeiculo()))
			ait.setSgUfVeiculo(dados.getUfVeiculo());
		
		if((ait.getNrRenainf() == "0" ||  ait.getNrRenainf() == null) && validString(dados.getCodigoRenainf()))
			ait.setNrRenainf(dados.getCodigoRenainf());
				
		if(validUpdate(ait.getNrControle(), dados.getNumeroProcessamento())) 
			ait.setNrControle(dados.getNumeroProcessamento());
		
		if(validUpdate(ait.getDsLocalInfracao(), dados.getLocalAit()))
			ait.setDsLocalInfracao(dados.getLocalAit());

		if(ait.getDtInfracao() == null && validString(dados.getDataAit()))
			ait.setDtInfracao(Util.convStringToCalendar5(dados.getDataAit()));
		
		if (ait.getDtPrazoDefesa() == null && validString(dados.getDataLimiteDefesa()))
			ait.setDtPrazoDefesa(Util.convStringToCalendar5(dados.getDataLimiteDefesa()));
		
		if (ait.getDtVencimento() == null && validString(dados.getDataLimiteRecurso())) {
			ait.setDtVencimento(Util.convStringToCalendar5(dados.getDataLimiteRecurso()));
		}
		
		if(validUpdate(ait.getNrDocumento(), dados.getNumeroCpfCnpj()))
			ait.setNrDocumento(dados.getNumeroCpfCnpj());
		
		if(validUpdate(ait.getNmProprietario(), dados.getNome()))
			ait.setNmProprietario(dados.getNome());
		
		if(validUpdate(ait.getDsLogradouro(), dados.getNomeLogradouro()))
			ait.setDsLogradouro(dados.getNomeLogradouro());
		
		if(validUpdate(ait.getNrCep(), dados.getCepImovel()))
			ait.setNrCep(dados.getCepImovel());
		
		if (dados.getCodigoInfracao() > 0) {
		    int codDetran = Integer.parseInt(dados.getCodigoInfracao() + "" + dados.getCodigoDesdobramento());
		    String[] parts = (dados.getDataAit()).split(" ");
		    String dataFormatada = parts[2] + "-" + parts[1] + "-" + parts[0];
		    int cdInfracao = (dataFormatada != null && !dataFormatada.isEmpty()) ? 
		                     getCdInfracaoPorData(codDetran, dataFormatada) : 
		                     getCdInfracao(codDetran);
		    ait.setCdInfracao(cdInfracao);
		}
		
		if(ait.getCdAgente() > 0 && validString(dados.getCodigoAgente()))
			ait.setCdAgente(getCdAgente(dados.getCodigoAgente()));	
		
		adicionarEnviadoNai(ait, dados);
		adicionarPostagemNai(ait, dados);
		adicionarPublicacaoNai(ait, dados);
		adicionarEnviadoNip(ait, dados);
		adicionarPostagemNip(ait, dados);
		adicionarPublicacaoNip(ait, dados);
	}

	private int getCdInfracao(int codDetran) throws Exception {
		Infracao infracao = infracaoService.getByCodDetran(codDetran);
		return infracao.getCdInfracao();
	}
	
	private int getCdInfracaoPorData(int codDetran, String dataEmissaoAutuacao) throws Exception, ValidacaoException {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("nr_cod_detran", codDetran, codDetran > 0);
		Infracao infracao = infracaoService.findInfracaoByData(searchCriterios, dataEmissaoAutuacao);
		if(infracao == null) {
			throw new ValidacaoException("Infração não encontrada no banco.");
		}
		return infracao.getCdInfracao();
	}
	
	private int getCdAgente(String nrMatricula) throws Exception {
		Agente agente = this.agenteService.getByMatricula(nrMatricula);
		return agente.getCdAgente();
	}

	private void adicionarEnviadoNai(Ait ait, ConsultaAutoBaseEstadualDadosRetorno dados) {
		if(dados.getDataEmissaoAutuacao() != null && !dados.getDataEmissaoAutuacao().equals("")) {
			AitMovimento aitMovimento = new AitMovimento();
			aitMovimento.setTpStatus(AitMovimentoServices.NAI_ENVIADO);
			aitMovimento.setDtMovimento(Util.convStringToCalendar5(dados.getDataEmissaoAutuacao()));
			aitMovimento.setLgEnviadoDetran(1);
			aitMovimento.setDtRegistroDetran(Util.convStringToCalendar5(dados.getDataEmissaoAutuacao()));
			ait.getMovimentos().add(aitMovimento);
		}
	}
	
	private void adicionarPostagemNai(Ait ait, ConsultaAutoBaseEstadualDadosRetorno dados) {
		if(dados.getDataPostagemAutuacao() != null && !dados.getDataPostagemAutuacao().equals("")) {
			AitMovimento aitMovimento = new AitMovimento();
			aitMovimento.setTpStatus(AitMovimentoServices.AR_NAI);
			aitMovimento.setDtMovimento(Util.convStringToCalendar5(dados.getDataPostagemAutuacao()));
			aitMovimento.setLgEnviadoDetran(1);
			aitMovimento.setDtRegistroDetran(Util.convStringToCalendar5(dados.getDataPostagemAutuacao()));
			ait.getMovimentos().add(aitMovimento);
		}
	}
	
	private void adicionarPublicacaoNai(Ait ait, ConsultaAutoBaseEstadualDadosRetorno dados) {
		if(dados.getDataPublicacaoAutuacao() != null && !dados.getDataPublicacaoAutuacao().equals("")) {
			AitMovimento aitMovimento = new AitMovimento();
			aitMovimento.setTpStatus(AitMovimentoServices.PUBLICACAO_NAI);
			aitMovimento.setDtMovimento(Util.convStringToCalendar5(dados.getDataPublicacaoAutuacao()));
			aitMovimento.setLgEnviadoDetran(1);
			aitMovimento.setDtRegistroDetran(Util.convStringToCalendar5(dados.getDataPublicacaoAutuacao()));
			aitMovimento.setDtPublicacaoDo(Util.convStringToCalendar5(dados.getDataPublicacaoAutuacao()));
			aitMovimento.setStPublicacaoDo(AitMovimentoServices.PUBLICADO_DO);
			ait.getMovimentos().add(aitMovimento);
		}
	}

	private void adicionarEnviadoNip(Ait ait, ConsultaAutoBaseEstadualDadosRetorno dados) {
		if(dados.getDataEmissaoPenalidade() != null && !dados.getDataEmissaoPenalidade().equals("")) {
			AitMovimento aitMovimento = new AitMovimento();
			aitMovimento.setTpStatus(AitMovimentoServices.NIP_ENVIADA);
			aitMovimento.setDtMovimento(Util.convStringToCalendar5(dados.getDataEmissaoPenalidade()));
			aitMovimento.setLgEnviadoDetran(1);
			aitMovimento.setDtRegistroDetran(Util.convStringToCalendar5(dados.getDataEmissaoPenalidade()));
			ait.getMovimentos().add(aitMovimento);
		}
	}
	
	private void adicionarPostagemNip(Ait ait, ConsultaAutoBaseEstadualDadosRetorno dados) {
		if(dados.getDataPostagemPenalidade() != null && !dados.getDataPostagemPenalidade().equals("")) {
			AitMovimento aitMovimento = new AitMovimento();
			aitMovimento.setTpStatus(AitMovimentoServices.AR_NIP);
			aitMovimento.setDtMovimento(Util.convStringToCalendar5(dados.getDataPostagemPenalidade()));
			aitMovimento.setLgEnviadoDetran(1);
			aitMovimento.setDtRegistroDetran(Util.convStringToCalendar5(dados.getDataPostagemPenalidade()));
			ait.getMovimentos().add(aitMovimento);
		}
	}
	
	private void adicionarPublicacaoNip(Ait ait, ConsultaAutoBaseEstadualDadosRetorno dados) {
		if(dados.getDataPublicacaoPenalidade() != null && !dados.getDataPublicacaoPenalidade().equals("")) {
			AitMovimento aitMovimento = new AitMovimento();
			aitMovimento.setTpStatus(AitMovimentoServices.PUBLICACAO_NIP);
			aitMovimento.setDtMovimento(Util.convStringToCalendar5(dados.getDataPublicacaoPenalidade()));
			aitMovimento.setLgEnviadoDetran(1);
			aitMovimento.setDtRegistroDetran(Util.convStringToCalendar5(dados.getDataPublicacaoPenalidade()));
			aitMovimento.setDtPublicacaoDo(Util.convStringToCalendar5(dados.getDataPublicacaoPenalidade()));
			aitMovimento.setStPublicacaoDo(AitMovimentoServices.PUBLICADO_DO);
			ait.getMovimentos().add(aitMovimento);
		}
	}
}
