package com.tivic.manager.mob.lote.impressao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.AitReportErrorException;
import com.tivic.manager.mob.GerarAitMovimentoNai;
import com.tivic.manager.mob.ServicoDetranDTO;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.AitInconsistencia.AitInconsistencia;
import com.tivic.manager.mob.inconsistencias.AitInconsistenciaBuilder;
import com.tivic.manager.mob.inconsistencias.IAitInconsistenciaService;
import com.tivic.manager.mob.lote.impressao.ServicoDetranDTOBuilder.ServicoDetranDTOBuilder;
import com.tivic.manager.mob.lote.impressao.validator.LoteNotificacaoNaiValidations;
import com.tivic.manager.mob.lote.impressao.validator.LoteNotificacaoValidations;
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class GeraMovimentoNotificacaoNicNaiViaUnica implements IGerarMovimentoNotificacao {
	
	private ServicoDetranServices servicoDetranServices;
	private ILoteNotificacaoService loteNotificacaoService;
	private IAitInconsistenciaService iAitInconsistenciaService;
	
	public GeraMovimentoNotificacaoNicNaiViaUnica() throws Exception {
		this.servicoDetranServices = ServicoDetranServicesFactory.gerarServico();
		this.loteNotificacaoService = (ILoteNotificacaoService) BeansFactory.get(ILoteNotificacaoService.class);
		this.iAitInconsistenciaService = (IAitInconsistenciaService) BeansFactory.get(IAitInconsistenciaService.class);
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
			for (Ait ait: listAits) {
				try {		
					validarMovimentoNai(ait, customConnection);
					aitMovimentoList.add(new GerarAitMovimentoNai().generate(ait.getCdAit(), cdUsuario, customConnection));
				}catch (LoteNotificacaoException e) {
					AitInconsistencia aitInconsistencia = new AitInconsistenciaBuilder().build(ait, e.getCodErro(), TipoStatusEnum.NAI_ENVIADO.getKey());
					this.iAitInconsistenciaService.salvarInconsistencia(aitInconsistencia);
					servicoDetranDTOListError.add(new ServicoDetranDTOBuilder().build(ait, e.getMessage()));
					throw new AitReportErrorException(e.getMessage());
				}
			}
			customConnection.finishConnection();
			servicoDetranDTOList = this.servicoDetranServices.remessa(aitMovimentoList);
			verificarEnvioNai(listAits.get(0), customConnection);
			return Stream.concat(servicoDetranDTOList.stream(), servicoDetranDTOListError.stream()).collect(Collectors.toList());
		}
		finally {
			customConnection.closeConnection();
		}
	}
	
	private void validarMovimentoNai(Ait ait, CustomConnection customConnection) throws Exception {
		try {
			validar(ait, customConnection);
		}
		catch (LoteNotificacaoException e) {
			ait = this.loteNotificacaoService.atualizarDadosAit(ait, customConnection);
			validar(ait, customConnection);
		}
	}
	
	private void validar(Ait ait, CustomConnection customConnection) throws Exception {
		new LoteNotificacaoValidations().validate(ait, customConnection);
		new LoteNotificacaoNaiValidations().validate(ait, customConnection);
	}
	
	private void verificarEnvioNai(Ait ait, CustomConnection customConnection) throws ValidacaoException {
		AitMovimento naiEnviada = AitMovimentoServices.getMovimentoTpStatus(ait.getCdAit(), TipoStatusEnum.NAI_ENVIADO.getKey(), customConnection.getConnection());
		if (naiEnviada.getLgEnviadoDetran() < TipoStatusEnum.ENVIADO_AO_DETRAN.getKey()) {
			throw new ValidacaoException("Não foi possível gerar a NAI, verifique os envios do movimento.");
		}
	}
	
	private void verificarGeracaoMovimento(List<Ait> listAits) throws ValidacaoException {
	    if (listAits.isEmpty()) {
	        throw new ValidacaoException("Não é possível gerar a NAI.");
	    }
	}

}