package com.tivic.manager.mob.aitmovimento;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.ait.relatorios.quantitativo.RelatorioQuantitativoDTO;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchCriterios;

public class AitMovimentoRepositoryTest implements AitMovimentoRepository{

	private List<AitMovimento> movimentos;
	
	public AitMovimentoRepositoryTest() {
		this.movimentos = new ArrayList<AitMovimento>();
	}
	
	private int getProximoCdMovimento(int cdAit) {
		int cdMovimento = 1;
		for(int i = 0; i < this.movimentos.size(); i++) {
			AitMovimento movimentoFromList = this.movimentos.get(i);
			if(movimentoFromList.getCdAit() == cdAit) {
				cdMovimento++;
			}
		}
		return cdMovimento;
	}
	
	@Override
	public int insert(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		aitMovimento.setCdMovimento(getProximoCdMovimento(aitMovimento.getCdAit()));
		this.movimentos.add(aitMovimento);
		return aitMovimento.getCdMovimento();
	}

	@Override
	public void update(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		for(int i = 0; i < this.movimentos.size(); i++) {
			AitMovimento movimentoFromList = this.movimentos.get(i);
			if(movimentoFromList.getCdAit() == aitMovimento.getCdAit() && movimentoFromList.getCdMovimento() == aitMovimento.getCdMovimento()) {
				this.movimentos.set(i, aitMovimento);
				break;
			}
		}
	}

	@Override
	public AitMovimento get(int cdMovimento, int cdAit) throws Exception {
		return get(cdMovimento, cdAit, new CustomConnection());
	}

	@Override
	public AitMovimento get(int cdMovimento, int cdAit, CustomConnection customConnection) throws Exception {
		for(int i = 0; i < this.movimentos.size(); i++) {
			AitMovimento movimentoFromList = this.movimentos.get(i);
			if(movimentoFromList.getCdAit() == cdAit && movimentoFromList.getCdMovimento() == cdMovimento) {
				return movimentoFromList;
			}
		}
		return null;
	}

	@Override
	public List<AitMovimento> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<AitMovimento> find(SearchCriterios searchCriterios, CustomConnection customConnection)
			throws Exception {
		return this.movimentos;
	}

	@Override
	public AitMovimento getByStatus(int cdAit, int tpStatus) throws Exception {
		return getByStatus(cdAit, tpStatus);
	}

	@Override
	public AitMovimento getByStatus(int cdAit, int tpStatus, CustomConnection customConnection) throws Exception {
		return getByStatus(cdAit, tpStatus, customConnection);
	}

	@Override
	public Search<RelatorioQuantitativoDTO> findQuantitativoMovimentos(SearchCriterios searchCriterios)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Search<RelatorioQuantitativoDTO> findQuantitativoMovimentos(SearchCriterios searchCriterios,
			CustomConnection customConnection) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Search<RelatorioQuantitativoDTO> findQuantitativoPagamentoNip(SearchCriterios searchCriterios,
			int tpMovimentoQuantitativo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Search<RelatorioQuantitativoDTO> findQuantitativoPagamentoNip(SearchCriterios searchCriterios,
			int tpMovimentoQuantitativo, CustomConnection customConnection) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Search<RelatorioQuantitativoDTO> findQuantitativoPagamentosRecebidos(SearchCriterios searchCriterios)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Search<RelatorioQuantitativoDTO> findQuantitativoPagamentosRecebidos(SearchCriterios searchCriterios,
			CustomConnection customConnection) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
