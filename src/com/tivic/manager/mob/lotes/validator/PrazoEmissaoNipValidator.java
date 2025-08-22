package com.tivic.manager.mob.lotes.validator;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

import com.tivic.manager.grl.parametro.IParametroService;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.Inconsistencia;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.inconsistencias.InconsistenciaRepository;
import com.tivic.manager.mob.lotes.service.impressao.exceptions.LoteNotificacaoException;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.util.date.DateUtil;

public class PrazoEmissaoNipValidator implements IValidator<Ait> {
	
	private AitRepository aitRepository; 
	private InconsistenciaRepository inconsistenciaRepository;
	private IParametroService parametroService;
	private IAitMovimentoService aitMovimentoService;
	private static final int seisMeses = 180;
	private static final int dozeMeses = 360;
	
	public PrazoEmissaoNipValidator() throws Exception {
		aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.inconsistenciaRepository = (InconsistenciaRepository) BeansFactory.get(InconsistenciaRepository.class);
		parametroService = (IParametroService) BeansFactory.get(IParametroService.class);
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}

	@Override
	public void validate(Ait object, CustomConnection customConnection) throws Exception {
		GregorianCalendar dtInfracao = aitRepository.get(object.getCdAit(), customConnection).getDtInfracao();
		GregorianCalendar inicioVigenciaPrazo = formatDatePrazoLegal();
		int tempoParaEmissao = aitMovimentoService.getAllDefesas(object.getCdAit()).isEmpty() ? seisMeses : dozeMeses;
		if (object.getDtInfracao().after(inicioVigenciaPrazo)){
			dtInfracao.add(Calendar.DATE, tempoParaEmissao);
			if (dtInfracao.before(DateUtil.getDataAtual())){
				int cdInconsistencia = parametroService.getValorOfParametroAsInt("MOB_INCONSISTENCIA_NIP_INTEMPESTIVO").getNrValorParametro();
				if (cdInconsistencia <= 0) {
				    throw new ValidacaoException("Parâmetro MOB_INCONSISTENCIA_NIP_INTEMPESTIVO não configurado.");
				}
				Inconsistencia inconsistencia = inconsistenciaRepository.get(cdInconsistencia);
				throw new LoteNotificacaoException(inconsistencia.getCdInconsistencia(), inconsistencia.getNmInconsistencia());
			}
		}
	}
	
	private GregorianCalendar formatDatePrazoLegal() throws Exception {
		String inicioPrazoLegal = parametroService.getValorOfParametroAsString("MOB_INFORMACOES_ADICIONAIS_DATA_INICIO_EMISSAO_NP").getTxtValorParametro();
		if(inicioPrazoLegal == null || Objects.equals(inicioPrazoLegal, "0")) {
			throw new ValidacaoException("O parâmetro MOB_INFORMACOES_ADICIONAIS_DATA_INICIO_EMISSAO_NP é obrigatório e não foi informado.");
		}
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate data = LocalDate.parse(inicioPrazoLegal, inputFormatter);
		GregorianCalendar dataFormatada = GregorianCalendar.from(data.atStartOfDay(ZoneId.systemDefault()));
		return dataFormatada;
	}
}
