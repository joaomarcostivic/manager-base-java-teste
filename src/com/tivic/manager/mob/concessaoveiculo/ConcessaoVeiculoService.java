package com.tivic.manager.mob.concessaoveiculo;

import java.sql.Connection;
import java.util.GregorianCalendar;

import com.tivic.manager.fta.Veiculo;
import com.tivic.sol.connection.Conexao;
import com.tivic.manager.mob.ConcessaoServices;
import com.tivic.manager.mob.ConcessaoVeiculoDAO;
import com.tivic.manager.mob.ConcessaoVeiculoDTO;
import com.tivic.manager.mob.ConcessaoVeiculoServices;
import com.tivic.manager.mob.VistoriaServices;
import com.tivic.manager.mob.concessaoveiculo.pessoa.ConcessaoPessoaFactory;
import com.tivic.manager.mob.concessaoveiculo.pessoa.ConcessaoVeiculoPessoa;
import com.tivic.manager.mob.concessaoveiculo.validations.SaveValidationConcessaoVeiculo;
import com.tivic.manager.mob.veiculo.VeiculoService;
import com.tivic.manager.util.Util;

import sol.util.Result;

public class ConcessaoVeiculoService {

	VeiculoService veiculoService;

	public ConcessaoVeiculoService() {
		this.veiculoService = new VeiculoService();
	}

	public ConcessaoVeiculoDTO desvincularVeiculo(ConcessaoVeiculoDTO concessaoVeiculoDTO, Connection connect)
			throws Exception {
		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}

			concessaoVeiculoDTO.getConcessaoVeiculo().setStConcessaoVeiculo(ConcessaoVeiculoServices.ST_DESVINCULADO);
			GregorianCalendar dtFinalOperacao = Util.getDataAtual();
			concessaoVeiculoDTO.getConcessaoVeiculo().setDtFinalOperacao(dtFinalOperacao);

			update(concessaoVeiculoDTO, connect);

			if (isConnectionNull)
				connect.commit();

			return concessaoVeiculoDTO;
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	public void save(ConcessaoVeiculoDTO concessaoVeiculoDTO) throws Exception {
		save(concessaoVeiculoDTO, null);
	}

	public void save(ConcessaoVeiculoDTO concessaoVeiculoDTO, Connection connect) throws Exception {
		boolean isConnectionNull = connect == null;

		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			new SaveValidationConcessaoVeiculo(concessaoVeiculoDTO).validate(connect);
			ConcessaoVeiculoPessoa concessaoVeiculoPessoa = new ConcessaoPessoaFactory()
					.build(concessaoVeiculoDTO.getNrCpfCnpj());
			concessaoVeiculoPessoa.save(concessaoVeiculoDTO, connect);
			saveVeiculo(concessaoVeiculoDTO, connect);
			if (concessaoVeiculoDTO.getConcessaoVeiculo().getCdConcessaoVeiculo() == 0) {
				insert(concessaoVeiculoDTO, connect);
			} else {
				update(concessaoVeiculoDTO, connect);
			}

			if (isConnectionNull) {
				connect.commit();
			}

		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	private void saveVeiculo(ConcessaoVeiculoDTO concessaoVeiculoDTO, Connection connect) throws Exception {
		Veiculo veiculo = concessaoVeiculoDTO.getVeiculo();
		veiculoService.save(veiculo, connect);
		concessaoVeiculoDTO.getConcessaoVeiculo().setCdVeiculo(veiculo.getCdVeiculo());
	}

	private void insert(ConcessaoVeiculoDTO concessaoVeiculoDTO, Connection connect) throws Exception {

		boolean isConnectionNull = connect == null;
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			if (concessaoVeiculoDTO.getConcessao().getTpConcessao() == ConcessaoServices.TP_COLETIVO_URBANO) {
				concessaoVeiculoDTO.getConcessaoVeiculo().setStConcessaoVeiculo(ConcessaoVeiculoServices.ST_NAO_VINCULADO);
			} else {
				concessaoVeiculoDTO.getConcessaoVeiculo().setStConcessaoVeiculo(ConcessaoVeiculoServices.ST_VINCULADO);
			}

			int cdConcessaoVeiculo = ConcessaoVeiculoDAO.insert(concessaoVeiculoDTO.getConcessaoVeiculo(), connect);

			if (cdConcessaoVeiculo < 0) {
				throw new Exception("Erro na inserção da Concessão do veículo.");
			}
			concessaoVeiculoDTO.getConcessaoVeiculo().setCdConcessaoVeiculo(cdConcessaoVeiculo);
			saveVistoria(concessaoVeiculoDTO, connect);
			if (isConnectionNull) {
				connect.commit();
			}
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}

	private void saveVistoria(ConcessaoVeiculoDTO concessaoVeiculoDTO, Connection connect) throws Exception {
		if (concessaoVeiculoDTO.getVistoria() != null) {
			Result resultVistoria = VistoriaServices.save(concessaoVeiculoDTO.getVistoria(), connect);
			if (resultVistoria.getCode() <= 0) {
				throw new Exception("Erro ao agendar vistoria.");
			}
		}
	}

	private void update(ConcessaoVeiculoDTO concessaoVeiculoDTO, Connection connect) throws Exception {

		boolean isConnectionNull = connect == null;

		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
				connect.setAutoCommit(false);
			}
			int retorno = ConcessaoVeiculoDAO.update(concessaoVeiculoDTO.getConcessaoVeiculo(), connect);
			if (retorno < 0) {
				throw new Exception("Erro na atualização da Concessão do veículo.");
			}
			if (isConnectionNull) {
				connect.commit();
			}
		} finally {
			if (isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
}