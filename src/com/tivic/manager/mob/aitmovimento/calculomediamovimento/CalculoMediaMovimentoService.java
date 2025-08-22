package com.tivic.manager.mob.aitmovimento.calculomediamovimento;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.talonario.enuns.TipoTalaoEnum;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class CalculoMediaMovimentoService implements ICalculoMediaMovimentoService{
	
	private final IParametroRepository parametroRepository;
	
	public CalculoMediaMovimentoService() throws Exception {
		parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}

	@Override
	public CalculoMediaDTO mediaJulgamento() throws Exception {
		CalculoMediaDTO calculoMediaDTO = new CalculoMediaDTO();
		List<AitMovimento> listAitDefesa = searchJulgamentoDefesa(montarCriteriosBuscaJulgamento(this.parametroRepository.getValorOfParametroAsInt("MOB_PERIODICIDADE_JULGAMENTO_DEFESA")));
		calculaMediaJulgamentoDefesa(listAitDefesa, calculoMediaDTO);
		List<AitMovimento> listAitJari = searchJulgamentoJari(montarCriteriosBuscaJulgamento(this.parametroRepository.getValorOfParametroAsInt("MOB_PERIODICIDADE_JULGAMENTO_JARI")));
		calculaMediaJulgamentoJari(listAitJari, calculoMediaDTO);
		return calculoMediaDTO;
	}
	
	public SearchCriterios montarCriteriosBuscaJulgamento(int periodo) {
		SearchCriterios searchCriterios = new SearchCriterios();
		String dtMovimentoInicial = getDtInicial(periodo);
		String dtMovimentoFinal = getDtFinal();
		searchCriterios.addCriteriosGreaterDate("A.dt_movimento", dtMovimentoInicial, dtMovimentoInicial != null);
		searchCriterios.addCriteriosMinorDate("A.dt_movimento", dtMovimentoFinal, dtMovimentoFinal != null);
		return searchCriterios;
	}
	
	private List<AitMovimento> searchJulgamentoDefesa(SearchCriterios searchCriterios) throws Exception {
		Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento A")
				.fields("A.cd_ait, A.dt_movimento, A.tp_status")
				.additionalCriterias("A.tp_status in (" + TipoStatusEnum.DEFESA_PREVIA.getKey() + "," + TipoStatusEnum.DEFESA_DEFERIDA.getKey() + "," + TipoStatusEnum.DEFESA_INDEFERIDA.getKey() +")")
				.additionalCriterias("A.cd_ait IN ( SELECT B.cd_ait FROM mob_ait_movimento B WHERE B.tp_status = " +  TipoStatusEnum.DEFESA_PREVIA.getKey() +
							" AND EXISTS (" +
								"SELECT 1 FROM mob_ait_movimento C WHERE C.cd_ait = B.cd_ait AND C.tp_status IN ("+ TipoStatusEnum.DEFESA_DEFERIDA.getKey() + "," + TipoStatusEnum.DEFESA_INDEFERIDA.getKey() + ")" +
						    ")" +
						")")
				.searchCriterios(searchCriterios)
				.orderBy("A.dt_movimento")
				.build();
		return search.getList(AitMovimento.class).isEmpty() ? Collections.emptyList() : search.getList(AitMovimento.class);
	}

	private CalculoMediaDTO calculaMediaJulgamentoDefesa(List<AitMovimento> listAitMovimento, CalculoMediaDTO calculoMediaDTO) {
		Map<Integer, List<AitMovimento>> agrupados = listAitMovimento.stream().collect(Collectors.groupingBy(AitMovimento::getCdAit));
		
		List<Long> diferencasDias = agrupados.entrySet().stream()
                .map(entry -> {
                    List<AitMovimento> listaMovimentos = entry.getValue();

                    Optional<AitMovimento> entrada = listaMovimentos.stream()
                            .filter(m -> m.getTpStatus() == TipoStatusEnum.DEFESA_PREVIA.getKey())
                            .findFirst();

                    Optional<AitMovimento> julgamento = listaMovimentos.stream()
                            .filter(m -> m.getTpStatus() == TipoStatusEnum.DEFESA_DEFERIDA.getKey() || m.getTpStatus() == TipoStatusEnum.DEFESA_INDEFERIDA.getKey())
                            .findFirst();

                    if (entrada.isPresent() && julgamento.isPresent()) {
                        LocalDate dataEntrada = entrada.get().getDtMovimento()
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();

                        LocalDate dataJulgamento = julgamento.get().getDtMovimento()
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();

                        long dias = ChronoUnit.DAYS.between(dataEntrada, dataJulgamento);
                        return dias;
                    } else {
                        return 0L;
                    }
                })
                .filter(dias -> dias > 0)
                .collect(Collectors.toList());
		
		long soma = diferencasDias.stream()
                .mapToLong(Long::longValue)
                .sum();

        double media = diferencasDias.isEmpty() ? 0 : (double) soma / diferencasDias.size();

        calculoMediaDTO.setMediaDiasDefesa(media);
        return calculoMediaDTO;
	}
	
	private List<AitMovimento> searchJulgamentoJari(SearchCriterios searchCriterios) throws Exception {
		Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento A")
				.fields("A.cd_ait, A.dt_movimento, A.tp_status")
				.additionalCriterias("A.tp_status in (" + TipoStatusEnum.RECURSO_JARI.getKey() + "," + TipoStatusEnum.JARI_COM_PROVIMENTO.getKey() + "," + TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey() +")")
				.additionalCriterias("A.cd_ait IN ( SELECT B.cd_ait FROM mob_ait_movimento B WHERE B.tp_status = " +  TipoStatusEnum.RECURSO_JARI.getKey() +
							" AND EXISTS (" +
								"SELECT 1 FROM mob_ait_movimento C WHERE C.cd_ait = B.cd_ait AND C.tp_status IN ("+ TipoStatusEnum.JARI_COM_PROVIMENTO.getKey() + "," + TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey() + ")" +
						    ")" +
						")")
				.searchCriterios(searchCriterios)
				.orderBy("A.dt_movimento")
				.build();
		return search.getList(AitMovimento.class).isEmpty() ? Collections.emptyList() : search.getList(AitMovimento.class);
	}

	private CalculoMediaDTO calculaMediaJulgamentoJari(List<AitMovimento> listAitMovimento, CalculoMediaDTO calculoMediaDTO ) {
		Map<Integer, List<AitMovimento>> agrupados = listAitMovimento.stream().collect(Collectors.groupingBy(AitMovimento::getCdAit));
		
		List<Long> diferencasDias = agrupados.entrySet().stream()
                .map(entry -> {
                    List<AitMovimento> listaMovimentos = entry.getValue();

                    Optional<AitMovimento> entrada = listaMovimentos.stream()
                            .filter(m -> m.getTpStatus() == TipoStatusEnum.RECURSO_JARI.getKey())
                            .findFirst();

                    Optional<AitMovimento> julgamento = listaMovimentos.stream()
                            .filter(m -> m.getTpStatus() == TipoStatusEnum.JARI_COM_PROVIMENTO.getKey() || m.getTpStatus() == TipoStatusEnum.JARI_SEM_PROVIMENTO.getKey())
                            .findFirst();

                    if (entrada.isPresent() && julgamento.isPresent()) {
                        LocalDate dataEntrada = entrada.get().getDtMovimento()
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();

                        LocalDate dataJulgamento = julgamento.get().getDtMovimento()
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();

                        long dias = ChronoUnit.DAYS.between(dataEntrada, dataJulgamento);
                        return dias;
                    } else {
                        return 0L;
                    }
                })
                .filter(dias -> dias > 0)
                .collect(Collectors.toList());
		
		long soma = diferencasDias.stream()
                .mapToLong(Long::longValue)
                .sum();

        double media = diferencasDias.isEmpty() ? 0 : (double) soma / diferencasDias.size();
        calculoMediaDTO.setMediaDiasJari(media);
        return calculoMediaDTO;
	}
	
	@Override
	public CalculoMediaDTO mediaEspera() throws Exception {
		CalculoMediaDTO calculoMediaDTO = new CalculoMediaDTO();
		List<AitMovimento> listEsperaJari = searchEspera(montarCriteriosJari(this.parametroRepository.getValorOfParametroAsInt("MOB_PERIODICIDADE_ESPERA_JARI")));
		calculaMediaEsperaJulgamento(listEsperaJari, calculoMediaDTO, getCdParametro("MOB_CD_TIPO_DOCUMENTO_JARI"));
		List<AitMovimento> listEsperaDefesa = searchEspera(montarCriteriosDefesa(this.parametroRepository.getValorOfParametroAsInt("MOB_PERIODICIDADE_ESPERA_DEFESA")));
		calculaMediaEsperaJulgamento(listEsperaDefesa, calculoMediaDTO, getCdParametro("MOB_CD_TIPO_DOCUMENTO_DEFESA_PREVIA"));
		return calculoMediaDTO;
	}
	
	private SearchCriterios montarCriteriosJari(int periodo) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("D.cd_tipo_documento", getCdParametro("MOB_CD_TIPO_DOCUMENTO_JARI"));
		searchCriterios.addCriteriosEqualInteger("C.cd_situacao_documento", getCdParametro("CD_SITUACAO_PENDENTE"));
		String dtMovimentoInicial = getDtInicial(periodo);
		String dtMovimentoFinal = getDtFinal();
		searchCriterios.addCriteriosGreaterDate("A.dt_movimento", dtMovimentoInicial, dtMovimentoInicial != null);
		searchCriterios.addCriteriosMinorDate("A.dt_movimento", dtMovimentoFinal, dtMovimentoFinal != null);
		return searchCriterios;
	}
	
	private SearchCriterios montarCriteriosDefesa(int periodo) throws Exception {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("D.cd_tipo_documento", getCdParametro("MOB_CD_TIPO_DOCUMENTO_DEFESA_PREVIA"));
		searchCriterios.addCriteriosEqualInteger("C.cd_situacao_documento", getCdParametro("CD_SITUACAO_PENDENTE"));
		String dtMovimentoInicial = getDtInicial(periodo);
		String dtMovimentoFinal = getDtFinal();
		searchCriterios.addCriteriosGreaterDate("A.dt_movimento", dtMovimentoInicial, dtMovimentoInicial != null);
		searchCriterios.addCriteriosMinorDate("A.dt_movimento", dtMovimentoFinal, dtMovimentoFinal != null);
		return searchCriterios;
	}
	
	private List<AitMovimento> searchEspera(SearchCriterios searchCriterios) throws Exception {
		Search<AitMovimento> search = new SearchBuilder<AitMovimento>("mob_ait_movimento A")
				.fields("DISTINCT ON(A.cd_ait) A.cd_ait, A.dt_movimento, A.tp_status")
				.addJoinTable("JOIN mob_ait_movimento_documento B ON (A.cd_ait = B.cd_ait)")
				.addJoinTable("JOIN ptc_documento C ON (B.cd_documento = C.cd_documento)")
				.addJoinTable("JOIN gpn_tipo_documento D ON (D.cd_tipo_documento = C.cd_tipo_documento)")
				.searchCriterios(searchCriterios)
				.orderBy("A.cd_ait, A.dt_movimento")
				.build();
		return search.getList(AitMovimento.class).isEmpty() ? Collections.emptyList() : search.getList(AitMovimento.class);
	}
	
	private int getCdParametro(String nmParametro) throws Exception {
		int cdParametro = this.parametroRepository.getValorOfParametroAsInt(nmParametro);
		if(cdParametro == 0) {
			throw new Exception("O parâmetro " + nmParametro + " não foi configurado.");
		}
		return cdParametro;
	}
	
	private CalculoMediaDTO calculaMediaEsperaJulgamento(List<AitMovimento> listEspera, CalculoMediaDTO calculoMediaDTO, int tpDocumento) throws Exception {
		Map<Integer, List<AitMovimento>> agrupados = listEspera.stream()
                .collect(Collectors.groupingBy(AitMovimento::getCdAit));

        List<Long> diferencasDias = agrupados.values().stream()
                .map(listaMovimentos -> {
                    return listaMovimentos.stream()
                            .min(Comparator.comparing(AitMovimento::getDtMovimento))
                            .map(movimento -> {
                                
                            	LocalDate dataEntrada = movimento.getDtMovimento()
                                        .toInstant()
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate();
                                
                            	LocalDate dataAtual = LocalDate.now();
                                
                            	long dias = ChronoUnit.DAYS.between(dataEntrada, dataAtual);
                                return dias;
                            })
                            .orElse(0L);
                })
                .filter(dias -> dias > 0)
                .collect(Collectors.toList());
        
        long soma = diferencasDias.stream()
                .mapToLong(Long::longValue)
                .sum();

        double media = diferencasDias.isEmpty() ? 0 : (double) soma / diferencasDias.size();
        if(tpDocumento == getCdParametro("MOB_CD_TIPO_DOCUMENTO_DEFESA_PREVIA")) {
        	calculoMediaDTO.setMediaDiasEsperaDefesa(media);
        } else if(tpDocumento == getCdParametro("MOB_CD_TIPO_DOCUMENTO_JARI")) {
        	calculoMediaDTO.setMediaDiasEsperaJari(media);
        }
        	
		
		return calculoMediaDTO;
	}
	
	public String getDtInicial(int periodo) {
		int periodoDefault = 3;
		LocalDate hoje = LocalDate.now();
		LocalDate periodoAnos = hoje.minusYears(periodo > 0 ? periodo : periodoDefault);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dataFormatada = periodoAnos.format(formatter);
		return dataFormatada;
	}
	
	public String getDtFinal() {
		LocalDate hoje = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dataFormatada = hoje.format(formatter);
		return dataFormatada;
	}
	
	@Override
	public CalculoMediaDTO qtTaloesDisponiveis() throws Exception {
	    CalculoMediaDTO calculoMediaDTO = new CalculoMediaDTO();
	    List<Talonario> listTaloesRadar = searchTalao(montarCriteriosRadar());
	    calcularTaloesDisponiveis(listTaloesRadar, calculoMediaDTO, true);
	    List<Talonario> listTotalTaloes = searchTalao(montarCriteriosEletronico());
	    calcularTaloesDisponiveis(listTotalTaloes, calculoMediaDTO, false);
	    
	    return calculoMediaDTO;
	}

	private SearchCriterios montarCriteriosRadar() {
	    SearchCriterios searchCriterios = new SearchCriterios();
	    searchCriterios.addCriteriosEqualInteger("A.tp_talao", TipoTalaoEnum.TP_TALONARIO_RADAR_FIXO.getKey());
	    return searchCriterios;
	}
	
	private SearchCriterios montarCriteriosEletronico() {
	    SearchCriterios searchCriterios = new SearchCriterios();
	    searchCriterios.addCriteriosEqualInteger("A.tp_talao", TipoTalaoEnum.TP_TALONARIO_ELETRONICO_TRANSITO.getKey());
	    return searchCriterios;
	}

	private List<Talonario> searchTalao(SearchCriterios searchCriterios) throws Exception {
	    Search<Talonario> search = new SearchBuilder<Talonario>("mob_talonario A")
	        .fields("A.cd_talao, A.nr_inicial, A.nr_final")
	        .searchCriterios(searchCriterios)
	        .build();
	    return search.getList(Talonario.class).isEmpty() ? Collections.emptyList() : search.getList(Talonario.class);
	}

	private void calcularTaloesDisponiveis(List<Talonario> listTalonario, CalculoMediaDTO calculoMediaDTO, boolean isRadar) {
	    Map<Integer, List<Talonario>> agrupados = listTalonario.stream()
	        .collect(Collectors.groupingBy(Talonario::getCdTalao));

	    List<Long> taloesDisponiveis = agrupados.values().stream()
	        .map(listaTaloes -> listaTaloes.stream()
	            .mapToLong(taloes -> {
	                int nrInicial = taloes.getNrInicial();
	                int nrFinal = taloes.getNrFinal();
	                return Math.abs(nrFinal - nrInicial);
	            })
	            .sum())
	        .collect(Collectors.toList());
	    
	    long soma = taloesDisponiveis.stream()
	        .mapToLong(Long::longValue)
	        .sum();

	    if (isRadar) {
	        calculoMediaDTO.setQtTalaoRadarDisponivel(soma);
	    } else {
	        calculoMediaDTO.setQtTalaoDisponivel(soma);
	    }
	}

}