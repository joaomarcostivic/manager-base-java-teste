package com.tivic.manager.mob.lote.impressao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
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
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;

public class GerarMovimentoNotificacaoNaiLote implements IGerarMovimentoNotificacao {

	ServicoDetranServices servicoDetranServices;
	ILoteNotificacaoService loteNotificacaoService;
	private IAitInconsistenciaService iAitInconsistenciaService;
	private ManagerLog managerLog;
	
	public GerarMovimentoNotificacaoNaiLote() throws Exception {
		servicoDetranServices = ServicoDetranServicesFactory.gerarServico();
		loteNotificacaoService = (ILoteNotificacaoService) BeansFactory.get(ILoteNotificacaoService.class);
		this.iAitInconsistenciaService = (IAitInconsistenciaService) BeansFactory.get(IAitInconsistenciaService.class);
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
	}

	@Override
	public List<ServicoDetranDTO> gerarMovimentos(List<Ait> listAits, int cdUsuario) throws Exception {
		List<AitMovimento> aitMovimentoList = new ArrayList<AitMovimento>();
		List<ServicoDetranDTO> servicoDetranDTOList = new ArrayList<ServicoDetranDTO>();
		List<ServicoDetranDTO> servicoDetranDTOListError = new ArrayList<ServicoDetranDTO>();
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			for (Ait ait: listAits) {	
				try {		
					this.managerLog.info("Gerando Movimento NAI AIT: ", ait.getIdAit());
					validar(ait, customConnection);
					aitMovimentoList.add(new GerarAitMovimentoNai().generate(ait.getCdAit(), cdUsuario, customConnection));
				}
				catch (AitReportErrorException e) {
					this.managerLog.info("Erro de validação comum para o AIT: " + ait.getCdAit() + " - ",  e.getMessage());
					servicoDetranDTOListError.add(new ServicoDetranDTOBuilder().build(ait, e.getMessage()));
					continue;
				}catch (LoteNotificacaoException e) {
					this.managerLog.info("Erro de validação inconsistencia para o AIT: " + ait.getCdAit() + " - ", e.getMessage());
					AitInconsistencia aitInconsistencia = new AitInconsistenciaBuilder().build(ait, e.getCodErro(), TipoStatusEnum.NAI_ENVIADO.getKey());
					this.iAitInconsistenciaService.salvarInconsistencia(aitInconsistencia, customConnection);
					servicoDetranDTOListError.add(new ServicoDetranDTOBuilder().build(ait, e.getMessage()));
					continue;
				}
			}
			customConnection.finishConnection();
			servicoDetranDTOList = servicoDetranServices.remessa(aitMovimentoList);
			return Stream.concat(servicoDetranDTOList.stream(), servicoDetranDTOListError.stream()).collect(Collectors.toList());
		}
		finally {
			customConnection.closeConnection();
		}
	}
	
	private void validar(Ait ait, CustomConnection customConnection) throws Exception {
		new LoteNotificacaoValidations().validate(ait, customConnection);
		new LoteNotificacaoNaiValidations().validate(ait, customConnection);
	}

}
