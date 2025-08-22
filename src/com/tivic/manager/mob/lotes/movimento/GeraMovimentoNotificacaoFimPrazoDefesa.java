package com.tivic.manager.mob.lotes.movimento;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitReportErrorException;
import com.tivic.manager.mob.ServicoDetranDTO;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.AitInconsistencia.AitInconsistencia;
import com.tivic.manager.mob.inconsistencias.AitInconsistenciaBuilder;
import com.tivic.manager.mob.inconsistencias.IAitInconsistenciaService;
import com.tivic.manager.mob.lote.impressao.GerarMovimentoFimPrazoDefeza;
import com.tivic.manager.mob.lotes.builders.impressao.ServicoDetranDTOFactory;
import com.tivic.manager.mob.lotes.service.impressao.exceptions.LoteNotificacaoException;
import com.tivic.manager.mob.lotes.validator.LoteNotificacaoNipValidations;
import com.tivic.manager.mob.lotes.validator.LoteNotificacaoValidations;
import com.tivic.manager.mob.lotes.validator.LoteNotificacoesNipsValidations;
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;

public class GeraMovimentoNotificacaoFimPrazoDefesa implements IGeraMovimentoNotificacao {

	private ServicoDetranServices servicoDetranServices;
	private IAitInconsistenciaService iAitInconsistenciaService;
	private ManagerLog managerLog;
	
	public GeraMovimentoNotificacaoFimPrazoDefesa() throws Exception {
		this.servicoDetranServices = ServicoDetranServicesFactory.gerarServico();
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
				this.managerLog.info("Gerando Movimento FIM PRAZO DEFESA AIT: ", ait.getIdAit());
				try {		
					validar(ait, customConnection);
					aitMovimentoList.add(new GerarMovimentoFimPrazoDefeza().generate(ait.getCdAit(), cdUsuario, customConnection));
				}
				catch (AitReportErrorException e) {
					this.managerLog.info("Erro de validação comum para o AIT: " + ait.getCdAit() + " - ",  e.getMessage());
					servicoDetranDTOListError.add(ServicoDetranDTOFactory.create(ait, e.getMessage()));
					continue;
				}
				catch (LoteNotificacaoException e) {
					this.managerLog.info("Erro de validação inconsistencia para o AIT: " + ait.getCdAit() + " - ", e.getMessage());
					AitInconsistencia aitInconsistencia = new AitInconsistenciaBuilder().build(ait, e.getCodErro(), TipoStatusEnum.FIM_PRAZO_DEFESA.getKey());
					this.iAitInconsistenciaService.salvarInconsistencia(aitInconsistencia, customConnection);
					servicoDetranDTOListError.add(ServicoDetranDTOFactory.create(ait, e.getMessage()));
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
		new LoteNotificacaoNipValidations().validate(ait, customConnection);
		new LoteNotificacoesNipsValidations().validate(ait, customConnection);
	}
}
