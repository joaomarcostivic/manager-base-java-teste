package com.tivic.manager.mob.aitmovimento.cancelamentomovimentos;

import java.sql.Types;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.ServicoDetranDTO;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.aitmovimento.aitmovimentobuilder.AitMovimentoBuilder;
import com.tivic.manager.wsdl.ServicoDetranServicesFactory;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetranServices;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class CancelaPenalidade {
	private AitMovimentoRepository aitMovimentoRepository;
	private IAitMovimentoService aitMovimentoService;
	private CustomConnection customConnection;
	private AitMovimento aitMovimento;
	private ServicoDetranServices servicoDetranServices;
	
	public CancelaPenalidade(AitMovimento aitMovimento, int cdUsuario, CustomConnection customConnection) throws Exception {
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
		this.customConnection = customConnection;
		this.aitMovimento = aitMovimento;
		gerenciaCancelamentoNip(cdUsuario);
	}
	
	private void gerenciaCancelamentoNip(int cdUsuario) throws Exception {
		List<AitMovimento> movimentos = this.aitMovimentoService.find(montarCriterios());
		if (!movimentos.isEmpty()) {
	    	atualizarLgEnviado(movimentos);			
	    } else {
	        cancelarNip(cdUsuario);	
	        setMovimentoNipCancelada();
	        enviarMovimentoCancelamentoDetran(this.aitMovimento.getCdAit());
	    }
	}
	
	private SearchCriterios montarCriterios() {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("cd_ait", this.aitMovimento.getCdAit(), true);
		String status = String.valueOf(TipoStatusEnum.NIP_ENVIADA.getKey()) + "," 
		+ String.valueOf(TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey()) + "," 
		+ String.valueOf(TipoStatusEnum.FIM_PRAZO_DEFESA.getKey() + ","
		+ String.valueOf(TipoStatusEnum.CANCELAMENTO_NIP.getKey()));
		searchCriterios.addCriteriosEqualInteger("lg_enviado_detran", TipoLgEnviadoDetranEnum.NAO_ENVIADO.getKey(), true);
		searchCriterios.addCriterios("tp_status", status, ItemComparator.IN, Types.INTEGER);
		return searchCriterios;
	}
	
	private void atualizarLgEnviado(List<AitMovimento> movimentos) throws Exception {
        List<Integer> tipoStatus = new ArrayList<>();
        tipoStatus.add(TipoStatusEnum.NIP_ENVIADA.getKey());
        tipoStatus.add(TipoStatusEnum.FIM_PRAZO_DEFESA.getKey());
        tipoStatus.add(TipoStatusEnum.CANCELAMENTO_NIP.getKey());
        tipoStatus.add(TipoStatusEnum.NOTIFICACAO_ADVERTENCIA.getKey());
        
        for (AitMovimento movimento : movimentos) {
            int status = movimento.getTpStatus();
            if (tipoStatus.contains(status)) {
                if (movimento.getLgEnviadoDetran() == TipoLgEnviadoDetranEnum.NAO_ENVIADO.getKey()) {
                    movimento.setLgEnviadoDetran(TipoLgEnviadoDetranEnum.NAO_ENVIAR_CANCELADO.getKey());
                    aitMovimentoRepository.update(movimento, this.customConnection);
                }
            }
        }
	}
	
	private void cancelarNip(int cdUsuario) throws Exception {
		AitMovimento cancelamentoNip = new AitMovimentoBuilder()
				.setCdAit(this.aitMovimento.getCdAit())
				.setCdOcorrencia(ParametroServices.getValorOfParametroAsInteger("CD_OCORRENCIA_CANCELAMENTO_NIP", 0))
				.setDtMovimento(new GregorianCalendar())
				.setTpStatus(TipoStatusEnum.CANCELAMENTO_NIP.getKey())
				.setLgEnviadoDetran(TipoLgEnviadoDetranEnum.NAO_ENVIADO.getKey())
				.setDtDigitacao(new GregorianCalendar())
				.setCdUsuario(cdUsuario)
			.build();
		this.aitMovimentoRepository.insert(cancelamentoNip, this.customConnection);
		this.aitMovimento.setLgEnviadoDetran(TipoLgEnviadoDetranEnum.REGISTRO_CANCELADO.getKey());	
		this.aitMovimentoRepository.update(this.aitMovimento, this.customConnection);
	}
	
	private void setMovimentoNipCancelada() throws Exception {
		AitMovimento movimentoCancelado = this.aitMovimentoRepository.getByStatus(this.aitMovimento.getCdAit(),
				TipoStatusEnum.CANCELAMENTO_NIP.getKey(), this.customConnection);
		this.aitMovimento.setCdMovimentoCancelamento(movimentoCancelado.getCdMovimento());	
		this.aitMovimento.setLgEnviadoDetran(TipoLgEnviadoDetranEnum.REGISTRO_CANCELADO.getKey());	
		this.aitMovimentoRepository.update(this.aitMovimento, this.customConnection);
	}
	
	private void enviarMovimentoCancelamentoDetran(int cdAit) throws Exception {
		this.servicoDetranServices = ServicoDetranServicesFactory.gerarServico();
		List<AitMovimento> aitMovimentoList = new ArrayList<AitMovimento>();
		AitMovimento aitMovimento = aitMovimentoRepository.getByStatus(cdAit, TipoStatusEnum.CANCELAMENTO_NIP.getKey());
		if(aitMovimento == null) {
			throw new Exception("Não há movimento de cancelamento de NIP.");
		}
		aitMovimentoList.add(aitMovimento);
		List<ServicoDetranDTO> remessa = servicoDetranServices.remessa(aitMovimentoList);
		verificarRetornoDetran(remessa.get(0));
	}
	
	private void verificarRetornoDetran(ServicoDetranDTO servicoDetranObjeto) throws ValidacaoException {
		if (servicoDetranObjeto.getCodigoRetorno() > 0)
			throw new ValidacaoException("Erro ao enviar movimento. Por favor verifique os envios do Detran.");
	}
}
