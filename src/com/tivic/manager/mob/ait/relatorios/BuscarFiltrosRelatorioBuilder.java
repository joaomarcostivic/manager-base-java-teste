package com.tivic.manager.mob.ait.relatorios;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.tivic.manager.mob.TipoConveniosEnum;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.EquipamentoEnum.EquipamentoEnum;
import com.tivic.manager.mob.agente.AgenteRepository;
import com.tivic.manager.mob.infracao.TipoCompetenciaEnum;
import com.tivic.manager.mob.pessoa.PessoaRepository;
import com.tivic.manager.mob.talonario.enuns.TipoTalaoEnum;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;
import com.tivic.sol.util.date.conversors.SqlFormat;

import sol.dao.ItemComparator;

public class BuscarFiltrosRelatorioBuilder {
	private SearchCriterios searchCriterios;
	private List<String> filtros;
	private HashMap<String, Object> hashDatas;
	AgenteRepository agenteRepository;
	PessoaRepository pessoaRepository;
	 
	public BuscarFiltrosRelatorioBuilder (SearchCriterios searchCriterios, boolean LgExcetoCanceladas, Integer tpEquipamento) throws Exception {
		this.searchCriterios = searchCriterios;
		this.filtros = new ArrayList<String>();
		this.hashDatas = new HashMap<String, Object>();
		agenteRepository = (AgenteRepository) BeansFactory.get(AgenteRepository.class);
		pessoaRepository = (PessoaRepository) BeansFactory.get(PessoaRepository.class);
	}
	
	public BuscarFiltrosRelatorioBuilder construirFiltro(boolean LgExcetoCanceladas, Integer tpEquipamento) {
		searchCriterios.getCriterios().forEach(criterio -> {
			if(criterio.getValue() == null && criterio.getValue().trim().equals("")) {
				return;
			}
			checkTpStatus(criterio);
			checkTpStatusContendoMovimento(criterio);
			checkDtInicialMovimento(criterio);
			checkDtFinalMovimento(criterio);
			checkUsuario(criterio);
			checkDtDigitacao(criterio);
			checkNrPlaca(criterio);
			checkNrCpfCnpjProprietario(criterio);
			checkNrCpfCondutor(criterio);
			checkTpTalao(criterio);
			checkDtInfracao(criterio);
			checkTpEquipamento(tpEquipamento);
			checkTpCompetencia(criterio);
			checkAgente(criterio);
			checkNrCodDetran(criterio);
			checkTpConvenio(criterio);
			checkNrRenavan(criterio);
			checkLgExcetoCanceladas(LgExcetoCanceladas);
		});
		
		if (!this.hashDatas.isEmpty())
			getFiltroDtInfracao();
			getFiltroDtDigitacao();
		return this;
	}

	private void checkTpStatus(ItemComparator criterio) {
		if(criterio.getColumn().equals("A.tp_status"))
			filtros.add("Situação atual: " + TipoStatusEnum.valueOf(Integer.valueOf(criterio.getValue())));	
	}
	
	private void checkTpStatusContendoMovimento(ItemComparator criterio) {
		if(criterio.getColumn().equals("B.tp_status") && Integer.valueOf(criterio.getValue()) != TipoStatusEnum.SITUACAO_NAO_DEFINIDA.getKey())
			filtros.add("Contendo movimento: " + TipoStatusEnum.valueOf(Integer.valueOf(criterio.getValue())));	
	}
	
	private void checkDtInicialMovimento(ItemComparator criterio) {
		if(criterio.getColumn().equals("B.dt_movimento_inicial"))
			filtros.add("Data inicial de movimento: " + DateUtil.formatDate(DateUtil.convStringToCalendar(criterio.getValue(), new SqlFormat()), "dd/MM/yyyy"));	
	}
	
	private void checkDtFinalMovimento(ItemComparator criterio) {
		if(criterio.getColumn().equals("B.dt_movimento_final"))
			filtros.add("Data final de movimento: " + DateUtil.formatDate(DateUtil.convStringToCalendar(criterio.getValue(), new SqlFormat()), "dd/MM/yyyy"));	
	}
	
	private void checkUsuario(ItemComparator criterio) {
		if(criterio.getColumn().equals("G.cd_pessoa"))
			try {
				filtros.add("Usuário: " + pessoaRepository.get(Integer.valueOf(criterio.getValue()), new CustomConnection()).getNmPessoa());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	private void getFiltroDtDigitacao() {
		if (hashDatas.get("DATA_INICIAL_DIGITACAO") != null)
			this.filtros.add(String.valueOf(hashDatas.get("DATA_INICIAL_DIGITACAO")));
		if (hashDatas.get("DATA_FINAL_DIGITACAO") != null)
			this.filtros.add(String.valueOf(hashDatas.get("DATA_FINAL_DIGITACAO")));
	}
	
	private void checkDtDigitacao(ItemComparator criterio) {
		if(criterio.getColumn().equals("A.dt_digitacao") && hashDatas.get("DATA_INICIAL_DIGITACAO") == null) {
			this.hashDatas.put("DATA_INICIAL_DIGITACAO", "Data inicial de digitação: " + DateUtil.formatDate(DateUtil.convStringToCalendar(criterio.getValue(), new SqlFormat()), "dd/MM/yyyy"));	
		}
		else if(criterio.getColumn().equals("A.dt_digitacao") && hashDatas.get("DATA_FINAL_DIGITACAO") == null) {
			this.hashDatas.put("DATA_FINAL_DIGITACAO", "Data final de digitação: " + DateUtil.formatDate(DateUtil.convStringToCalendar(criterio.getValue(), new SqlFormat()), "dd/MM/yyyy"));	
		}
	}
	
	private void checkNrPlaca(ItemComparator criterio) {
		if(criterio.getColumn().equals("A.nr_placa"))
			filtros.add("Placa: " + criterio.getValue());	
	}
	
	private void checkNrCpfCnpjProprietario(ItemComparator criterio) {
		if(criterio.getColumn().equals("A.nr_cpf_cnpj_proprietario"))
			filtros.add("CPF/CNPJ Proprietário: " + criterio.getValue());	
	}

	private void checkNrCpfCondutor(ItemComparator criterio) {
		if(criterio.getColumn().equals("A.nr_cpf_condutor"))
			filtros.add("CPF condutor " + criterio.getValue());	
	}
	
	private void checkTpTalao(ItemComparator criterio) {
		if(criterio.getColumn().equals("E.tp_talao"))
			filtros.add("Tipo de AIT: " + TipoTalaoEnum.valueOf(Integer.valueOf(criterio.getValue())));	
	}
	
	private void getFiltroDtInfracao() {
		if (hashDatas.get("DATA_INICIAL_INFRACAO") != null)
			this.filtros.add(String.valueOf(hashDatas.get("DATA_INICIAL_INFRACAO")));
		if (hashDatas.get("DATA_FINAL_INFRACAO") != null)
			this.filtros.add(String.valueOf(hashDatas.get("DATA_FINAL_INFRACAO")));
	}
	
	private void checkDtInfracao(ItemComparator criterio) {
		if(criterio.getColumn().equals("A.dt_infracao") && hashDatas.get("DATA_INICIAL_INFRACAO") == null) {
			this.hashDatas.put("DATA_INICIAL_INFRACAO", "Data inicial de autuacao: " + DateUtil.formatDate(DateUtil.convStringToCalendar(criterio.getValue(), new SqlFormat()), "dd/MM/yyyy"));	
		}
		else if(criterio.getColumn().equals("A.dt_infracao") && hashDatas.get("DATA_FINAL_INFRACAO") == null) {
			this.hashDatas.put("DATA_FINAL_INFRACAO", "Data final de autuacao: " + DateUtil.formatDate(DateUtil.convStringToCalendar(criterio.getValue(), new SqlFormat()), "dd/MM/yyyy"));	
		}
	}

	private void checkTpEquipamento(Integer tpEquipamento) {
		if (tpEquipamento != null && !filtros.contains("Tipo de Equipamento: " + EquipamentoEnum.valueOf(Integer.valueOf(tpEquipamento)))) {
			filtros.add("Tipo de Equipamento: " + EquipamentoEnum.valueOf(tpEquipamento));	
		}
	}
	
	private void checkTpCompetencia(ItemComparator criterio) {
		if(criterio.getColumn().equals("D.tp_competencia"))
			filtros.add("Competência: " + TipoCompetenciaEnum.valueOf(Integer.valueOf(criterio.getValue())));	
	}
	
	private void checkAgente(ItemComparator criterio) {
		if(criterio.getColumn().equals("A.cd_agente"))
			try {
				filtros.add("Agente: " + agenteRepository.get(Integer.valueOf(criterio.getValue())).getNmAgente());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

	}
	
	private void checkNrCodDetran(ItemComparator criterio) {
		if(criterio.getColumn().equals("D.nr_cod_detran"))
			filtros.add("Cód. Infração: " + criterio.getValue());	
	} 
	
	private void checkTpConvenio(ItemComparator criterio) {
		if(criterio.getColumn().equals("A.tp_convenio"))
			filtros.add("Convênio: " + TipoConveniosEnum.valueOf(Integer.valueOf(criterio.getValue())));
	} 
	 
	private void checkNrRenavan(ItemComparator criterio) {
		if(criterio.getColumn().equals("A.nr_renavan"))
			filtros.add("Renavam: " + criterio.getValue());	
	}
	
	private void checkLgExcetoCanceladas(boolean lgExcetoCanceladas) {
	    if (lgExcetoCanceladas && !filtros.contains("Exceto as Canceladas")) {
	        filtros.add("Exceto as Canceladas");
	    }
	}
	
	public List<String> build(){
		return filtros;
	}

}
