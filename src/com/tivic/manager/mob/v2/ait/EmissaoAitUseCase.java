package com.tivic.manager.mob.v2.ait;

import java.util.ArrayList;

import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.grl.equipamento.repository.EquipamentoRepository;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.AgenteDAO;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitEvento;
import com.tivic.manager.mob.AitEventoServices;
import com.tivic.manager.mob.AitImagem;
import com.tivic.manager.mob.AitProprietario;
import com.tivic.manager.mob.AitServices;
import com.tivic.manager.mob.AitVeiculo;
import com.tivic.manager.mob.EventoEquipamento;
import com.tivic.manager.mob.Talonario;
import com.tivic.manager.mob.TalonarioServices;
import com.tivic.manager.mob.eventoequipamento.EventoEquipamentoRepository;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

import sol.util.Result;

public class EmissaoAitUseCase {

	private EventoEquipamentoRepository eventoEquipamentoRepository;
	private EquipamentoRepository equipamentoRepository;
	
	public EmissaoAitUseCase() throws Exception {
		this.eventoEquipamentoRepository = (EventoEquipamentoRepository) BeansFactory.get(EventoEquipamentoRepository.class);
		this.equipamentoRepository = (EquipamentoRepository) BeansFactory.get(EquipamentoRepository.class);
	}
	
	public Result execute(InformacoesAitDTO informacoesAitDTO, InformacoesVeiculoDTO informacoesVeiculoDTO, ArrayList<AitImagem> imagens) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try {
			customConnection.initConnection(true);
			EventoEquipamento eventoEquipamento = this.eventoEquipamentoRepository.get(informacoesAitDTO.getCdEvento(), customConnection);
			eventoEquipamento.confirmarEvento(ConfirmacaoEventoFactory.factory(informacoesAitDTO));
			this.eventoEquipamentoRepository.update(eventoEquipamento, customConnection);
			Equipamento equipamento = this.equipamentoRepository.get(eventoEquipamento.getCdEquipamento(), customConnection);
			Agente agente = AgenteDAO.get(informacoesAitDTO.getCdAgente(), customConnection.getConnection());
			if (agente == null)
				throw new AgenteInformadoNaoExisteException("O agente informado não existe");
			Talonario talonario = TalonarioServices.getTalaoRadar(agente, equipamento, customConnection.getConnection());
			Ait ait = new AitConfirmadoEventoBuilder()
				.preencherComEquipamento(equipamento)
				.preencherComEventoEquipamento(eventoEquipamento)
				.preencherComInformacoesAit(informacoesAitDTO)
				.preencherComTalonario(talonario)
				.preencherComInformacoesVeiculo(informacoesVeiculoDTO)
			.build();
			criarAit(ait, customConnection);
			indicarPrimeiraImagemComoPrincipal(imagens);
			criarImagens(ait, imagens, customConnection);
			criarRelacaoComEvento(ait, eventoEquipamento, customConnection);
			customConnection.finishConnection();
			return new Result(1, "Ait cadastrado com sucesso", "AIT", ait);
		} finally {
			customConnection.closeConnection();
		}
	}

	private void criarAit(Ait ait, CustomConnection customConnection) throws Exception {
		AitVeiculo aitVeiculo = AitVeiculoFactory.factory(ait);
		AitProprietario aitProprietario = AitProprietarioFactory.factory(ait);
		Result result = AitServices.save(ait, null, aitVeiculo, aitProprietario, null, customConnection.getConnection());
		if (result.getCode() < 0) {
			throw new Exception("Erro! EmissaoAitUseCase.criarAit: " + result.getMessage());
		}
		ait.setCdAit(((Ait) result.getObjects().get("AIT")).getCdAit());
	}
	
	private void indicarPrimeiraImagemComoPrincipal(ArrayList<AitImagem> imagens) {
		imagens.get(0).tornarImagemImpressao();
	}
	
	private void criarImagens(Ait ait, ArrayList<AitImagem> imagens, CustomConnection customConnection) throws Exception {
		Result result = AitServices.salvarAitImagem(ait.getCdAit(), imagens, customConnection.getConnection());
		if (result == null || result.getCode() < 0) {
			throw new Exception("Erro! EmissaoAitUseCase.criarImagens: " + result.getMessage());
		}
	}
	
	private void criarRelacaoComEvento(Ait ait, EventoEquipamento eventoEquipamento,
			CustomConnection customConnection) throws Exception {
		AitEvento aitEvento = new AitEvento(ait.getCdAit(), eventoEquipamento.getCdEvento());
		Result result = AitEventoServices.save(aitEvento, null, customConnection.getConnection());
		if (result == null || result.getCode() < 0) {
			throw new Exception("Erro! EmissaoAitUseCase.criarRelacaoComEvento: " + result.getMessage());
		}
	}

}
