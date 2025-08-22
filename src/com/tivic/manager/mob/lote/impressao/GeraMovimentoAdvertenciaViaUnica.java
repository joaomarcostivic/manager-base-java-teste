package com.tivic.manager.mob.lote.impressao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitReportErrorException;
import com.tivic.manager.mob.GeraAitMovimentoAdvertencia;
import com.tivic.manager.mob.ServicoDetranDTO;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.AitInconsistencia.AitInconsistencia;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.inconsistencias.AitInconsistenciaBuilder;
import com.tivic.manager.mob.inconsistencias.IAitInconsistenciaService;
import com.tivic.manager.mob.lote.impressao.ServicoDetranDTOBuilder.ServicoDetranDTOBuilder;
import com.tivic.manager.mob.lote.impressao.validator.LoteNotificacaoAdvertenciaValidations;
import com.tivic.manager.mob.lote.impressao.validator.LoteNotificacaoValidations;
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class GeraMovimentoAdvertenciaViaUnica implements IGerarMovimentoNotificacao {
	private ServicoDetranServices servicoDetranServices;
	private ILoteNotificacaoService loteNotificacaoService;
	private IAitInconsistenciaService aitInconsistenciaService;
	private IAitMovimentoService aitMovimentoService;

	public GeraMovimentoAdvertenciaViaUnica() throws Exception {
		this.servicoDetranServices = ServicoDetranServicesFactory.gerarServico();
		this.loteNotificacaoService = (ILoteNotificacaoService) BeansFactory.get(ILoteNotificacaoService.class);
		this.aitInconsistenciaService = (IAitInconsistenciaService) BeansFactory.get(IAitInconsistenciaService.class);
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}

	@Override
	public List<ServicoDetranDTO> gerarMovimentos(List<Ait> listAits, int cdUsuario) throws Exception {
		List<AitMovimento> aitMovimentoList = new ArrayList<AitMovimento>();
		List<ServicoDetranDTO> servicoDetranDTOList = new ArrayList<ServicoDetranDTO>();
		List<ServicoDetranDTO> servicoDetranDTOListError = new ArrayList<ServicoDetranDTO>();
		CustomConnection customConnection = new CustomConnection();
		verificarGeracaoMovimento(listAits);
		try {
			customConnection.initConnection(true);
			for (Ait ait : listAits) {
				try {
					validarMovimento(ait, customConnection);
					aitMovimentoList.add(new GeraAitMovimentoAdvertencia().generate(ait.getCdAit(), cdUsuario, customConnection));
				} catch (LoteNotificacaoException e) {
					AitInconsistencia aitInconsistencia = new AitInconsistenciaBuilder().build(ait, e.getCodErro(),	TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey());
					this.aitInconsistenciaService.salvarInconsistencia(aitInconsistencia);
					servicoDetranDTOListError.add(new ServicoDetranDTOBuilder().build(ait, e.getMessage()));
					throw new AitReportErrorException(e.getMessage());
				}
			}
			customConnection.finishConnection();
			servicoDetranDTOList = this.servicoDetranServices.remessa(aitMovimentoList);
			verificarEnvioAdvertencia(listAits.get(0), customConnection);
			return Stream.concat(servicoDetranDTOList.stream(), servicoDetranDTOListError.stream()).collect(Collectors.toList());
		} finally {
			customConnection.closeConnection();
		}
	}

	private void validarMovimento(Ait ait, CustomConnection customConnection) throws Exception {
		try {
			validar(ait, customConnection);
		} catch (LoteNotificacaoException e) {
			this.loteNotificacaoService.atualizarDadosAit(ait, customConnection);
			validar(ait, customConnection);
		}
	}

	private void validar(Ait ait, CustomConnection customConnection) throws Exception {
		new LoteNotificacaoValidations().validate(ait, customConnection);
		new LoteNotificacaoAdvertenciaValidations().validate(ait, customConnection);
	}

	private void verificarEnvioAdvertencia(Ait ait, CustomConnection customConnection) throws ValidacaoException, Exception {
		AitMovimento notificacaoAdvertencia = this.aitMovimentoService.getMovimentoTpStatus(ait.getCdAit(), TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey());
		if (notificacaoAdvertencia.getLgEnviadoDetran() < TipoStatusEnum.ENVIADO_AO_DETRAN.getKey()) {
			throw new ValidacaoException("Não foi possível gerar a notificação de advertência, verifique os envios do movimento.");
		}
	}

	private void verificarGeracaoMovimento(List<Ait> listAits) throws ValidacaoException {
		if (listAits.isEmpty()) {
			throw new ValidacaoException("Não é possível gerar a notificação de advertencia.");
		}
	}
}
