package com.tivic.manager.mob.lote.impressao.viaunica.nip;

import java.sql.Types;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.grl.pessoa.TipoPessoaEnum;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.TipoCnhEnum;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.ait.SituacaoAitEnum;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.lote.impressao.viaunica.nip.enums.TipoDocumentoCondutorEnum;
import com.tivic.manager.ptc.fici.ApresentacaoCondutor;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.ServicoDetranConsultaFactory;
import com.tivic.manager.wsdl.ServicoDetranFactory;
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.detran.mg.consultainfracoes.ConsultaInfracaoOcorrenciaDTO;
import com.tivic.manager.wsdl.detran.mg.consultainfracoes.ConsultarInfracoesDadosRetorno;
import com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor.ConsultarPontuacaoDadosCondutorDadosRetorno;
import com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor.DadosCondutorDocumentoEntrada;
import com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor.PontuacaoDadosCondutorDTO;
import com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor.PontuacaoDadosCondutorDTOBuilder;
import com.tivic.manager.wsdl.detran.mg.consultapontuacaodadoscondutor.TipoDocumentoEnum;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class VerificaInfracaoAdvertencia implements IVerificaInfracaoAdvertencia {
	private String documentoCondutor;
	private Integer tipoDocumento;
	private LocalDate dtFinal;
	private LocalDate dtInicial;
	private DateTimeFormatter formatter;
	private String dtInicialFormatada;
	private String dtFinalFormatada;
	private AitRepository aitRepository;
	private IParametroRepository parametroRepository;
	private boolean condutorApresentadoMaisDeUmaVez;
	private boolean proprietarioJaComAdvertencia;
	private boolean motoristaBeneficiado;
	private IAitMovimentoService aitMovimentoService;

	public VerificaInfracaoAdvertencia() throws Exception {
		dtFinal = LocalDate.now();
		dtInicial = LocalDate.now().minusMonths(12).plusDays(1);
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		dtInicialFormatada = dtInicial.format(formatter);
		dtFinalFormatada = dtFinal.format(formatter);
		
		aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		condutorApresentadoMaisDeUmaVez = false;
		proprietarioJaComAdvertencia = false;
		motoristaBeneficiado = false;
	}
	
	@Override
	public boolean isPassivelAdvertencia(int cdAit) throws Exception {
		boolean orgaoOptanteNpAdvertencia = parametroRepository.getValorOfParametroAsBoolean("MOB_OPTANTE_NP_ADVERTENCIA");
		if(!orgaoOptanteNpAdvertencia) {
			return false;
		}
		if (verificarDefesaAdvertenciaDeferidaSemCancelamento(cdAit)) {
	        return true;
	    }
		Ait ait = aitRepository.get(cdAit);
		boolean passivelAdvertencia = infracaoPassivelAdvertencia(ait.getCdAit());
		if(passivelAdvertencia) {
			ConsultarInfracoesDadosRetorno resultadoConsulta = consultaInfracoes(ait);
			if (resultadoConsulta.getQtdeOcorrencias() == 0 && resultadoConsulta.getCodigoRetorno() == 0 
					&& resultadoConsulta.getMensagemRetorno() != null) {
					String nrRenachCondutor = consultarCondutor();
					documentoCondutor = null;
					tipoDocumento = null;
		            return nrRenachCondutor != null ? true : false;
		    }
			documentoCondutor = null;
			tipoDocumento = null;
			passivelAdvertencia = resultadoConsulta.getQtdeOcorrencias() == 1 && resultadoConsulta.getCodigoRetorno() == 0 
					&& resultadoConsulta.getMensagemRetorno() != null; 
			for (ConsultaInfracaoOcorrenciaDTO infracao : resultadoConsulta.getListConsultaInfracaoOcorrenciaDTO()) {
	            if (infracao.getAit().equals(ait.getIdAit()) && passivelAdvertencia) {
	                return true;
	            }
	        }
		}
		return false;
	}
	
	private boolean verificarDefesaAdvertenciaDeferidaSemCancelamento(int cdAit) throws Exception {
	    AitMovimento aitMovimento = aitMovimentoService.getStatusMovimento(cdAit, TipoStatusEnum.ADVERTENCIA_DEFESA_DEFERIDA.getKey());
	    if (aitMovimento != null && aitMovimento.getLgEnviadoDetran() == TipoLgEnviadoDetranEnum.REGISTRADO.getKey()) {
	        AitMovimento aitMovimentoCancelado = aitMovimentoService.getStatusMovimento(cdAit, TipoStatusEnum.CANCELAMENTO_ADVERTENCIA_DEFESA_DEFERIDA.getKey());
	        return aitMovimentoCancelado == null || aitMovimentoCancelado.getLgEnviadoDetran() != TipoLgEnviadoDetranEnum.REGISTRADO.getKey();
	    }
	    return false;
	}
	
	private boolean infracaoPassivelAdvertencia(int cdAit) throws Exception {
		Search<Infracao> search = new SearchBuilder<Infracao>("mob_ait A, mob_infracao B")
				.fields("B.*")
				.searchCriterios(criteriosInfracao(cdAit))
				.additionalCriterias("A.cd_infracao =  B.cd_infracao")
				.build();
		return search.getList(Infracao.class).size() > 0;
	}
	
	private SearchCriterios criteriosInfracao(int cdAit) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit, true);
		searchCriterios.addCriterios("B.nm_natureza", "'LEVE', 'MEDIA', 'MÉDIA'", ItemComparator.IN, Types.VARCHAR);
		return searchCriterios;
	}

	public ConsultarInfracoesDadosRetorno consultaInfracoes(Ait ait) throws Exception {
		ConsultarInfracoesDadosRetorno resultadoConsulta = new ConsultarInfracoesDadosRetorno();
		getInfratorCondutorAbordado(ait);
		if (condutorApresentadoMaisDeUmaVez || motoristaBeneficiado) {
			condutorApresentadoMaisDeUmaVez = false;
			motoristaBeneficiado = false;
			documentoCondutor = null;
			tipoDocumento = null;
			return resultadoConsulta;
		}
		if (documentoCondutor == null) {
			getInfratorProprietario(ait);
		}
	    if (proprietarioJaComAdvertencia || motoristaBeneficiado) {
	    	proprietarioJaComAdvertencia = false;
	    	motoristaBeneficiado = false;
	    	documentoCondutor = null;
			tipoDocumento = null;
	        return resultadoConsulta;
	    }
		if(documentoCondutor != null) {
			ServicoDetranServices servicoDetranServices = ServicoDetranServicesFactory.gerarServico();
			resultadoConsulta = servicoDetranServices.consultarInfrator(documentoCondutor, tipoDocumento, dtInicialFormatada, dtFinalFormatada);
		}
		return resultadoConsulta;
	}
	
	public String consultarCondutor() throws Exception {
		if (tipoDocumento == TipoDocumentoCondutorEnum.TP_DOCUMENTO_CPF.getKey()) {
			PontuacaoDadosCondutorDTO pontuacaoDadosCondutorDTO = consultarPontuacaoDadosCondutor(documentoCondutor, TipoDocumentoEnum.CPF.getKey());
			return !pontuacaoDadosCondutorDTO.getRenachCondutor().isEmpty() ? pontuacaoDadosCondutorDTO.getRenachCondutor() : null;
		}
		return null;
	}
	
	private PontuacaoDadosCondutorDTO consultarPontuacaoDadosCondutor(String documento, int tpDocumento) throws Exception {
		AitDetranObject aitDetranObject = new AitDetranObject();
		aitDetranObject.setDadosCondutorDocumentoEntrada(new DadosCondutorDocumentoEntrada(documento, tpDocumento));
		ServicoDetranObjeto servicoDetranObjeto = ServicoDetranConsultaFactory
				.gerarServico(ServicoDetranFactory.MG, ServicoDetranConsultaFactory.PONTUACAO_DADOS_CONDUTOR)
				.executar(aitDetranObject);
		ConsultarPontuacaoDadosCondutorDadosRetorno dadosRetorno = (ConsultarPontuacaoDadosCondutorDadosRetorno) servicoDetranObjeto
				.getDadosRetorno();
		return new PontuacaoDadosCondutorDTOBuilder(dadosRetorno).build();
	}
	
	private void getInfratorCondutorAbordado(Ait ait) throws Exception {
		if (ait.getNrCnhCondutor() != null && !ait.getNrCnhCondutor().equals("")) {
			documentoCondutor = ait.getNrCnhCondutor();
			tipoDocumento = TipoDocumentoCondutorEnum.TP_DOCUMENTO_RENACH.getKey();
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualString("A.nr_cnh", ait.getNrCnhCondutor(), true);
			motoristaBeneficiado = verificaMotoristaIndicadoFici(searchCriterios);
		} else {
			getCnhCondutorFici(ait.getCdAit());
		}
	}
	
	private void getCnhCondutorFici(int cdAit) throws Exception {
		int codigoAceito = ParametroServices.getValorOfParametroAsInteger("CD_OCORRENCIA_APRESENTACAO_CONDUTOR_ACEITO",	0);
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.cd_ait", cdAit);
		searchCriterios.addCriteriosEqualInteger("A.tp_status", TipoStatusEnum.APRESENTACAO_CONDUTOR.getKey());
		searchCriterios.addCriteriosEqualInteger("A.cd_ocorrencia", codigoAceito, true);
		searchCriterios.addCriterios("D.tp_modelo_cnh", String.valueOf(TipoCnhEnum.TP_CNH_HABILITACAO_ESTRANGEIRA.getKey()), ItemComparator.NOTIN, Types.INTEGER, true);
		Search<ApresentacaoCondutor> condutorSearch = new SearchBuilder<ApresentacaoCondutor>("mob_ait_movimento A")
				.fields("D.nr_cnh")
				.addJoinTable("JOIN mob_ait_movimento_documento B ON (A.cd_ait = B.cd_ait)")
				.addJoinTable("JOIN ptc_documento C ON (B.cd_documento = C.cd_documento)")
				.addJoinTable("JOIN ptc_apresentacao_condutor D ON (C.cd_documento = D.cd_documento)")
				.orderBy("D.nr_cnh IS NOT NULL")
				.searchCriterios(searchCriterios)
				.build();
		List<ApresentacaoCondutor> condutorDocumento = condutorSearch.getList(ApresentacaoCondutor.class);
		if (!condutorDocumento.isEmpty()) {
	        documentoCondutor = condutorDocumento.get(0).getNrCnh();
	        tipoDocumento = TipoDocumentoCondutorEnum.TP_DOCUMENTO_RENACH.getKey();
			condutorApresentadoMaisDeUmaVez = verificarCondutorApresentadoEmMaisFici(cdAit);
	    }
	}
	
	private boolean verificarCondutorApresentadoEmMaisFici(int cdAit) throws Exception {
		int codigoAceito = ParametroServices.getValorOfParametroAsInteger("CD_OCORRENCIA_APRESENTACAO_CONDUTOR_ACEITO",	0);
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualString("A.nr_cnh", documentoCondutor, true);
		searchCriterios.addCriteriosEqualInteger("C.cd_ocorrencia", codigoAceito, true);
		searchCriterios.addCriterios("D.st_ait",  String.valueOf(SituacaoAitEnum.ST_CANCELADO.getKey()), ItemComparator.NOTIN, Types.INTEGER, true);
		searchCriterios.addCriterios("D.cd_ait",  String.valueOf(cdAit), ItemComparator.NOTIN, Types.INTEGER, true);
		searchCriterios.addCriteriosGreaterDate("D.dt_infracao", dtInicialFormatada, dtInicialFormatada != null);
		searchCriterios.addCriteriosMinorDate("D.dt_infracao", dtFinalFormatada, dtFinalFormatada != null);
		Search<ApresentacaoCondutor> condutorSearch = new SearchBuilder<ApresentacaoCondutor>("ptc_apresentacao_condutor A")
				.fields("A.nr_cnh, B.cd_ait")
				.addJoinTable("JOIN mob_ait_movimento_documento B ON (A.cd_documento = B.cd_documento)")
				.addJoinTable("JOIN mob_ait_movimento C ON (B.cd_movimento = C.cd_movimento and C.cd_ait = B.cd_ait)")
				.addJoinTable("JOIN mob_ait D ON (D.cd_ait = C.cd_ait)")
				.additionalCriterias(" EXISTS ( "
						+ " SELECT 1 FROM mob_ait_movimento C2 "
						+ "		WHERE C2.cd_ait = C.cd_ait AND C2.tp_status IN ( "
						+ 			TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey() + ", " + TipoStatusEnum.ADVERTENCIA_DEFESA_DEFERIDA.getKey()
						+ " 	) "
						+ ")")
				.searchCriterios(searchCriterios)
				.build();
		List<ApresentacaoCondutor> apresentacaoCondutorList = condutorSearch.getList(ApresentacaoCondutor.class);
		if (!apresentacaoCondutorList.isEmpty()) {
			return true;
		}
		return false;
	}

	private void getInfratorProprietario(Ait ait) throws Exception {
		if(ait.getNrCpfCnpjProprietario() != null && ait.getTpPessoaProprietario() == TipoPessoaEnum.FISICA.getKey()
				&& Util.isCpfValido(ait.getNrCpfCnpjProprietario())) {
			documentoCondutor = ait.getNrCpfCnpjProprietario();
			tipoDocumento = TipoDocumentoCondutorEnum.TP_DOCUMENTO_CPF.getKey();
			proprietarioJaComAdvertencia = verificarProprietarioJaComAdvertencia(ait.getCdAit());
			SearchCriterios searchCriterios = new SearchCriterios();
			searchCriterios.addCriteriosEqualString("A.nr_Cpf_Cnpj", ait.getNrCpfCnpjProprietario(), true);
			motoristaBeneficiado = verificaMotoristaIndicadoFici(searchCriterios);
		}
	}
	
	private boolean verificarProprietarioJaComAdvertencia(int cdAit) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriterios("A.cd_ait",  String.valueOf(cdAit), ItemComparator.NOTIN, Types.INTEGER, true);
		searchCriterios.addCriteriosEqualString("A.nr_cpf_cnpj_proprietario", documentoCondutor, true);
		searchCriterios.addCriteriosEqualInteger("B.lg_enviado_detran", TipoLgEnviadoDetranEnum.REGISTRADO.getKey(), true);
		searchCriterios.addCriterios("B.tp_status", String.valueOf(TipoStatusEnum.ADVERTENCIA_DEFESA_DEFERIDA.getKey()) + ", " +
	            String.valueOf(TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey()), ItemComparator.IN, Types.INTEGER);
		searchCriterios.addCriteriosGreaterDate("A.dt_infracao", dtInicialFormatada, dtInicialFormatada != null);
		searchCriterios.addCriteriosMinorDate("A.dt_infracao", dtFinalFormatada, dtFinalFormatada != null);
		Search<Ait> aitSearch = new SearchBuilder<Ait>("mob_ait A")
				.fields("A.cd_ait, A.nr_cpf_cnpj_proprietario")
				.addJoinTable("JOIN mob_ait_movimento B ON (A.cd_ait = B.cd_ait)")
				.searchCriterios(searchCriterios)
				.build();
		List<Ait> aitList = aitSearch.getList(Ait.class);
		if(!aitList.isEmpty()) {
			return true;
		}
		return false;
	}
	
	private boolean verificaMotoristaIndicadoFici(SearchCriterios searchCriterios) throws Exception {
		int codigoAceito = ParametroServices.getValorOfParametroAsInteger("CD_OCORRENCIA_APRESENTACAO_CONDUTOR_ACEITO",	0);
		searchCriterios.addCriteriosEqualInteger("C.cd_ocorrencia", codigoAceito, true);
		searchCriterios.addCriterios("D.st_ait",  String.valueOf(SituacaoAitEnum.ST_CANCELADO.getKey()), ItemComparator.NOTIN, Types.INTEGER, true);
		searchCriterios.addCriteriosGreaterDate("D.dt_infracao", dtInicialFormatada, dtInicialFormatada != null);
		searchCriterios.addCriteriosMinorDate("D.dt_infracao", dtFinalFormatada, dtFinalFormatada != null);
		Search<ApresentacaoCondutor> condutorSearch = new SearchBuilder<ApresentacaoCondutor>("ptc_apresentacao_condutor A")
				.fields("A.nr_cnh, A.nr_Cpf_Cnpj")
				.addJoinTable("JOIN mob_ait_movimento_documento B ON (A.cd_documento = B.cd_documento)")
				.addJoinTable("JOIN mob_ait_movimento C ON (B.cd_movimento = C.cd_movimento and C.cd_ait = B.cd_ait)")
				.addJoinTable("JOIN mob_ait D ON (D.cd_ait = C.cd_ait)")
				.additionalCriterias(" EXISTS ( "
						+ " SELECT 1 FROM mob_ait_movimento C2 "
						+ "		WHERE C2.cd_ait = C.cd_ait AND C2.tp_status IN ( "
						+ 			TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey() + ", " + TipoStatusEnum.ADVERTENCIA_DEFESA_DEFERIDA.getKey()
						+ " 	) "
						+ ")")
				.searchCriterios(searchCriterios)
				.build();
		List<ApresentacaoCondutor> apresentacaoCondutorList = condutorSearch.getList(ApresentacaoCondutor.class);
		if (!apresentacaoCondutorList.isEmpty()) {
			return true;
		}
		return false;
	}
}
