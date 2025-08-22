package com.tivic.manager.mob.processamento.conversao.builders;

import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.grl.equipamento.IEquipamentoService;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.TipoConveniosEnum;
import com.tivic.manager.mob.TipoEvento;
import com.tivic.manager.mob.processamento.conversao.dtos.AitParamsDTO;
import com.tivic.manager.mob.processamento.conversao.factories.conversao.ConversorBaseAntigaNovaFactory;
import com.tivic.manager.mob.processamento.conversao.factories.infracao.InfracaoFactory;
import com.tivic.manager.mob.talonario.ITalonarioService;
import com.tivic.manager.mob.tipoevento.ITipoEventoService;
import com.tivic.manager.util.detran.ConsultaPlacaClient;
import com.tivic.manager.util.detran.VeiculoDTO;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;

public class AitParamsDTOBuilder {
	
	private ManagerLog managerLog;
	private AitParamsDTO aitParams = new AitParamsDTO();
	private CustomConnection customConnection;
	private IEquipamentoService equipamentoService;
	private ITalonarioService talonarioService;
	private ITipoEventoService tipoEventoService;
	private IParametroRepository parametroRepository;
	private ConversorBaseAntigaNovaFactory conversorBaseAntigaNovaFactory;
	
	public AitParamsDTOBuilder(CustomConnection customConnection) throws Exception {
		this.customConnection = customConnection;
		this.managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
		this.equipamentoService = (IEquipamentoService) BeansFactory.get(IEquipamentoService.class);
		this.talonarioService = (ITalonarioService) BeansFactory.get(ITalonarioService.class);
		this.tipoEventoService = (ITipoEventoService) BeansFactory.get(ITipoEventoService.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	    this.conversorBaseAntigaNovaFactory = (ConversorBaseAntigaNovaFactory) BeansFactory.get(ConversorBaseAntigaNovaFactory.class);
	}
	
	public AitParamsDTOBuilder setEvento(EventoEquipamento evento) {
		aitParams.setEvento(evento);
		return this;
	}
	
	public AitParamsDTOBuilder setOrgao(Orgao orgao) {
		aitParams.setOrgao(orgao);
		return this;
	}
	
	public AitParamsDTOBuilder setCdUsuario(int cdUsuario) {
		aitParams.setCdUsuario(cdUsuario);
		return this;
	}
	
	public AitParamsDTOBuilder setTalao() throws Exception {
	    Talonario talao = determinarTalao();
	    aitParams.setTalao(talao);
	    return this;
	}
	
	public AitParamsDTOBuilder setCdAgente() throws Exception {
		int cdAgente = determinarCdAgente();
		aitParams.setCdAgente(cdAgente);
		return this;
	}
	
	public AitParamsDTOBuilder setTpConvenio() throws Exception {
		int tpConvenio = determinarTpConvenio();
		aitParams.setTpConvenio(tpConvenio);
		return this;
	}
	
	public AitParamsDTOBuilder setVeiculo() throws Exception {
		try {
			managerLog.info("Consulta de placa", "Realizando consulta de placa.");
			int vlParametro = conversorBaseAntigaNovaFactory.getParametroRepository().getOrgaoAutuanteParamValue();
			VeiculoDTO veiculo = ConsultaPlacaClient.consultar(vlParametro, aitParams.getEvento().getNrPlaca());
			validateVeiculo(veiculo);
			aitParams.setVeiculo(veiculo);
			managerLog.info("Consulta de placa", "Consulta de placa realizada com sucesso.");
			return this;
		} catch (Exception e) {
			managerLog.showLog(e);
			throw new Exception("Erro ao realizar consulta de placa: " + e.getMessage());
		}
	}
	
	private void validateVeiculo(VeiculoDTO veiculo) {
		if (veiculo == null) {
			throw new IllegalArgumentException();
		}
		
		if (veiculo.getMarcaModelo() == null) {
            throw new IllegalArgumentException("Marca modelo é inválida.");
        }
		
		if (veiculo.getEspecieVeiculo() == null) {
			throw new IllegalArgumentException("Espécie do veículo é inválida.");
        }
		
		if (veiculo.getCor() == null) {
			throw new IllegalArgumentException("Cor do veículo é inválida.");
        }
	}
	
	public AitParamsDTOBuilder setTipoEvento() throws Exception {
		TipoEvento tipoEvento = tipoEventoService.get(aitParams.getEvento().getCdTipoEvento(), customConnection);
		aitParams.setTipoEvento(tipoEvento);
		return this;
	}
	
	public AitParamsDTOBuilder setInfracao() throws Exception {
		Infracao infracao = new InfracaoFactory(aitParams.getEvento())
				.getStrategy(aitParams.getTipoEvento())
				.getInfracao(customConnection);
		aitParams.setInfracao(infracao);
		return this;
	}
	
	public AitParamsDTOBuilder setEquipamento() throws Exception {
		Equipamento equipamento = equipamentoService.get(aitParams.getEvento().getCdEquipamento(), customConnection);
		aitParams.setEquipamento(equipamento);
		return this;
	}
	
	public AitParamsDTO build() throws Exception {
		return aitParams;
	}
	
	private Talonario determinarTalao() throws Exception {
	    if (isEventoDeEstacionamentoDigital() && aitParams.getCdUsuario() > 0) {
	        return talonarioService.getByCdEquipamento(
	            buscarCdAgenteByCdUsuario(aitParams.getCdUsuario(), customConnection),
	            aitParams.getEvento().getCdEquipamento(),
	            customConnection
	        );
	    }
	    return talonarioService.getByCdEquipamento(
	        aitParams.getOrgao().getCdAgenteResponsavel(),
	        aitParams.getEvento().getCdEquipamento(),
	        customConnection
	    );
	}
	
	private int determinarCdAgente() throws Exception {
	    int cdAgente = (isEventoDeEstacionamentoDigital() && aitParams.getCdUsuario() > 0) 
	        ? buscarCdAgenteByCdUsuario(aitParams.getCdUsuario(), customConnection) 
	        : aitParams.getOrgao().getCdAgenteResponsavel();
	    
	    if (cdAgente <= 0) throw new Exception("Código de agente inválido.");
	    
	    return cdAgente;
	}
	
	private int determinarTpConvenio() throws Exception {
	    if (isEventoDeEstacionamentoDigital()) {
	        int key = parametroRepository.getValorOfParametroAsInt("TP_CONVENIO_PADRAO");
	        if (key <= 0 || TipoConveniosEnum.valueOf(key) == null)
	            throw new Exception("Valor do parâmetro TP_CONVENIO_PADRAO não encontrado");
	        return key;
	    }
	    return 0;
	}
	
	private boolean isEventoDeEstacionamentoDigital() throws Exception {
	    int cdTipoEventoEstacionamentoDigital = parametroRepository.getValorOfParametroAsInt("MOB_EVENTO_ESTACIONAMENTO_DIGITAL");
	    return aitParams.getEvento().getCdTipoEvento() == cdTipoEventoEstacionamentoDigital && cdTipoEventoEstacionamentoDigital > 0;
	}

	private int buscarCdAgenteByCdUsuario(int cdUsuario, CustomConnection customConnection) throws Exception {
		Agente agente = conversorBaseAntigaNovaFactory.getAgenteRepository().getByCdUsuario(cdUsuario);
		return agente.getCdAgente();
	}
}
