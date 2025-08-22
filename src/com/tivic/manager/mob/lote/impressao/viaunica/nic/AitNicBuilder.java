package com.tivic.manager.mob.lote.impressao.viaunica.nic;

import java.util.List;
import java.util.StringJoiner;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.infracao.InfracaoRepository;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.exceptions.EnderecoOrgaoException;
import com.tivic.manager.mob.talonario.SituacaoTalaoEnum;
import com.tivic.manager.mob.talonario.TalonarioRepository;
import com.tivic.manager.mob.talonario.enuns.TipoTalaoEnum;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;

public class AitNicBuilder {
	private int multiplicadorMulta = 2;
	private Ait ait;
	private InfracaoRepository infracaoRepository;
	private TalonarioRepository talonarioRepository;
	private IParametroRepository parametroRepository;
	private AitRepository aitRepository;
	
	public AitNicBuilder(Ait ait, int cdUsuario, CustomConnection customConnection) throws Exception {
		infracaoRepository = (InfracaoRepository) BeansFactory.get(InfracaoRepository.class);
		talonarioRepository = (TalonarioRepository) BeansFactory.get(TalonarioRepository.class);
		parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.ait = new Ait();
		construirAit(ait, cdUsuario, customConnection);
	}
	
	private void construirAit(Ait ait, int cdUsuario, CustomConnection customConnection) throws Exception {
		this.ait.setCdInfracao(getParametroCdInfracaoNic());
		this.ait.setCdUsuario(cdUsuario);
		this.ait.setCdAitOrigem(ait.getCdAit());
		this.ait.setCdEquipamento(ait.getCdEquipamento());
		this.ait.setCdLogradouroInfracao(ait.getCdLogradouroInfracao());
		this.ait.setCdBairro(ait.getCdBairro());
		this.ait.setCdCategoriaVeiculo(ait.getCdCategoriaVeiculo());
		this.ait.setCdCidade(ait.getCdCidade());
		this.ait.setCdEndereco(ait.getCdEndereco());
		this.ait.setCdEnderecoCondutor(ait.getCdEnderecoCondutor());
		this.ait.setCdMarcaAutuacao(ait.getCdMarcaAutuacao());
		this.ait.setCdMarcaVeiculo(ait.getCdMarcaVeiculo());
		this.ait.setCdEspecieVeiculo(ait.getCdEspecieVeiculo());
		this.ait.setNrCpfCnpjProprietario(ait.getNrCpfCnpjProprietario());
		this.ait.setDtInfracao(DateUtil.getDataAtual());
		this.ait.setDsLocalInfracao(getLocalOrgao());
		this.ait.setDsPontoReferencia(getPontoReferenciaOrgao());
		this.ait.setDsLogradouro(ait.getDsLogradouro());
		this.ait.setDsNrImovel(ait.getDsNrImovel());
		this.ait.setDsObservacao(ait.getDsObservacao());
		this.ait.setNmProprietario(ait.getNmProprietario());
		this.ait.setNrPlaca(ait.getNrPlaca());
		this.ait.setNrRenainf(ait.getNrRenainf());
		this.ait.setNrRenavan(ait.getNrRenavan());
		this.ait.setUfCnhAutuacao(ait.getUfCnhAutuacao());
		this.ait.setVlVelocidadeAferida(ait.getVlVelocidadeAferida());
		this.ait.setVlVelocidadeAferida(ait.getVlVelocidadeAferida());
		this.ait.setVlVelocidadePenalidade(ait.getVlVelocidadePenalidade());
		this.ait.setVlVelocidadePermitida(ait.getVlVelocidadePermitida());
		this.ait.setStAit(ait.getStAit());
		this.ait.setNrCep(ait.getNrCep());
		this.ait.setCdCidadeProprietario(ait.getCdCidadeProprietario());
		this.ait.setSgUfVeiculo(ait.getSgUfVeiculo());
		this.ait.setTpPessoaProprietario(ait.getTpPessoaProprietario());
		this.ait.setCdCorVeiculo(ait.getCdCorVeiculo());
		this.ait.setCdTipoVeiculo(ait.getCdTipoVeiculo());
		this.ait.setTpDocumento(ait.getTpDocumento());
		this.ait.setNrDocumento(ait.getNrDocumento());
		this.ait.setDtDigitacao(DateUtil.getDataAtual());
		setVlMulta(ait);
		processarAit(customConnection);
	}
	
	private int getParametroCdInfracaoNic() throws ValidacaoException {
	    int cdInfracao = ParametroServices.getValorOfParametroAsInteger("MOB_CD_TIPO_INFRACAO_NIC", 0);
	    if (cdInfracao == 0) {
            throw new ValidacaoException("O parâmetro MOB_CD_TIPO_INFRACAO_NIC é obrigatório e não foi informado.");
        }
	    return cdInfracao;
	}
	
	private void setVlMulta(Ait ait) throws Exception {
		Infracao infracao = this.infracaoRepository.get(ait.getCdInfracao());
		double vlMultaInicial = infracao.getVlInfracao();
		double vlMultaAtual = vlMultaInicial * multiplicadorMulta;
		this.ait.setVlMulta(vlMultaAtual);
	}
	
	public void processarAit(CustomConnection customConnection) throws Exception {
		Talonario talonario = buscarTalaoDisponivel(customConnection);
		if (talonario == null) {
			throw new ValidacaoException("Nenhum talão disponível para a geração da NIC.");
		}
		atualizarTalonario(talonario, customConnection);
		String idAit = criarIdAit(talonario, customConnection);
		ait.setCdTalao(talonario.getCdTalao());
		ait.setIdAit(idAit);
		ait.setNrAit(talonario.getNrUltimoAit());
		ait.setCdAgente(talonario.getCdAgente());
	}
	    
	private void atualizarTalonario(Talonario talonario, CustomConnection customConnection) throws Exception {
		atualizarNrUltimoAit(talonario);
		atualizarStTalao(talonario);
		talonarioRepository.update(talonario, customConnection);
	}
	
	private void atualizarNrUltimoAit(Talonario talonario) throws Exception {
		int novoNrUltimoAit = talonario.getNrUltimoAit() <= 0 ? talonario.getNrInicial() : talonario.getNrUltimoAit() + 1;
		if (novoNrUltimoAit > talonario.getNrFinal()) {
			throw new ValidacaoException("Talonário atingiu o limite da faixa de numeração.");
		}
		talonario.setNrUltimoAit(novoNrUltimoAit);
	}
	
	private void atualizarStTalao(Talonario talonario) {
		if (talonario.getNrUltimoAit() == talonario.getNrFinal()) {
			talonario.setStTalao(SituacaoTalaoEnum.ST_TALAO_CONCLUIDO.getKey());
		}
	}
	
	private String criarIdAit(Talonario talonario, CustomConnection customConnection) throws Exception {
		String idAit;
		do {
			idAit = talonario.getSgTalao() + Util.fillNum(talonario.getNrUltimoAit(), 10 - talonario.getSgTalao().length());
			if (!aitRepository.hasAit(idAit, customConnection)) {
				return idAit;
			}
			talonario.setNrUltimoAit(talonario.getNrUltimoAit() + 1);
			talonarioRepository.update(talonario, customConnection);
			if (talonario.getNrUltimoAit() > talonario.getNrFinal()) {
				throw new ValidacaoException("Nenhuma numeração válida disponível na faixa do talonário.");
			}
		} while (true);
	}
	
	private Talonario buscarTalaoDisponivel(CustomConnection customConnection) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("tp_talao", TipoTalaoEnum.TP_TALONARIO_NIC.getKey());
		searchCriterios.addCriteriosEqualInteger("st_talao", SituacaoTalaoEnum.ST_TALAO_ATIVO.getKey());
		searchCriterios.setQtLimite(1);
		Search<Talonario> search = new SearchBuilder<Talonario>("mob_talonario")
				.searchCriterios(searchCriterios).customConnection(customConnection)
				.orderBy("cd_talao ASC")
			.build();
		List<Talonario> talonarioList = search.getList(Talonario.class);
		return talonarioList.stream().filter(t -> t.getNrUltimoAit() <= t.getNrFinal()).findFirst().orElse(null);
	}

	private String getLocalOrgao() throws Exception {
	    String nmLogradouroOrgao = parametroRepository.getValorOfParametroAsString("NM_LOGRADOURO");
	    String nrEnderecoOrgao = parametroRepository.getValorOfParametroAsString("NR_ENDERECO");
	    String nmBairroOrgao = parametroRepository.getValorOfParametroAsString("NM_BAIRRO");

	    validarParametro(nmLogradouroOrgao, "NM_LOGRADOURO");
	    validarParametro(nrEnderecoOrgao, "NR_ENDERECO");
	    validarParametro(nmBairroOrgao, "NM_BAIRRO");

	    StringJoiner joiner = new StringJoiner(", ");
	    joiner.add(nmLogradouroOrgao)
	          .add(nrEnderecoOrgao)
	          .add(nmBairroOrgao);
	    return joiner.toString();
	}

	private void validarParametro(String valor, String nomeParametro) throws Exception, EnderecoOrgaoException {
	    if (valor == null || valor.isEmpty()) {
	        throw new EnderecoOrgaoException("Parâmetro de endereço do órgão não configurado: " + nomeParametro);
	    }
	}
	
	private String getPontoReferenciaOrgao() throws Exception {
	    String nmPontoReferenciaOrgao = parametroRepository.getValorOfParametroAsString("NM_PONTO_REFERENCIA");
	    if (nmPontoReferenciaOrgao != null && !nmPontoReferenciaOrgao.isEmpty()) {
	        return nmPontoReferenciaOrgao;
	    }
	    return null;
	}
	
	public Ait build() {
		return this.ait;
	}
}
