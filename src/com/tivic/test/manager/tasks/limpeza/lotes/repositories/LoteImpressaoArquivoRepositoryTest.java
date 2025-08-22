package com.tivic.test.manager.tasks.limpeza.lotes.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.tivic.manager.mob.lote.impressao.ILoteImpressaoArquivoRepository;
import com.tivic.manager.mob.lote.impressao.LoteImpressaoArquivo;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class LoteImpressaoArquivoRepositoryTest implements ILoteImpressaoArquivoRepository {

	private List<LoteImpressaoArquivo> listLoteImpressaoArquivo;
	
	public LoteImpressaoArquivoRepositoryTest() {
		listLoteImpressaoArquivo = new ArrayList<>();
		listLoteImpressaoArquivo.add(criarLoteImpressaoArquivo1());
		listLoteImpressaoArquivo.add(criarLoteImpressaoArquivo2());
		listLoteImpressaoArquivo.add(criarLoteImpressaoArquivo3());
	}
	
	private LoteImpressaoArquivo criarLoteImpressaoArquivo1() {
		LoteImpressaoArquivo loteImpressaoArquivo = new LoteImpressaoArquivo();
		loteImpressaoArquivo.setCdLoteImpressao(1);
		loteImpressaoArquivo.setCdArquivo(1);
		return loteImpressaoArquivo;
	}

	private LoteImpressaoArquivo criarLoteImpressaoArquivo2() {
		LoteImpressaoArquivo loteImpressaoArquivo = new LoteImpressaoArquivo();
		loteImpressaoArquivo.setCdLoteImpressao(2);
		loteImpressaoArquivo.setCdArquivo(2);
		return loteImpressaoArquivo;
	}

	private LoteImpressaoArquivo criarLoteImpressaoArquivo3() {
		LoteImpressaoArquivo loteImpressaoArquivo = new LoteImpressaoArquivo();
		loteImpressaoArquivo.setCdLoteImpressao(3);
		loteImpressaoArquivo.setCdArquivo(3);
		return loteImpressaoArquivo;
	}
	
	@Override
	public LoteImpressaoArquivo insert(LoteImpressaoArquivo loteImpressaoArquivo, CustomConnection customConnection) throws Exception {
		this.listLoteImpressaoArquivo.add(loteImpressaoArquivo);
		return loteImpressaoArquivo;
	}

	@Override
	public LoteImpressaoArquivo update(LoteImpressaoArquivo loteImpressaoArquivo, CustomConnection customConnection) throws Exception {
		for(int i = 0; i < listLoteImpressaoArquivo.size(); i++) {
			LoteImpressaoArquivo loteImpressaoArquivoLista = listLoteImpressaoArquivo.get(i);
			if(loteImpressaoArquivoLista.getCdLoteImpressao() == loteImpressaoArquivo.getCdLoteImpressao() && loteImpressaoArquivoLista.getCdArquivo() == loteImpressaoArquivo.getCdArquivo()) { 
				listLoteImpressaoArquivo.set(i, loteImpressaoArquivo);
			}
		}
		return loteImpressaoArquivo;
	}

	@Override
	public LoteImpressaoArquivo delete(LoteImpressaoArquivo loteImpressaoArquivo, CustomConnection customConnection) throws Exception {
		for(int i = 0; i < listLoteImpressaoArquivo.size(); i++) {
			LoteImpressaoArquivo loteImpressaoArquivoLista = listLoteImpressaoArquivo.get(i);
			if(loteImpressaoArquivoLista.getCdLoteImpressao() == loteImpressaoArquivo.getCdLoteImpressao() && loteImpressaoArquivoLista.getCdArquivo() == loteImpressaoArquivo.getCdArquivo()) { 
				listLoteImpressaoArquivo.remove(i);
			}
		}
		return loteImpressaoArquivo;
	}

	@Override
	public LoteImpressaoArquivo get(int cdLoteImpressao, int cdArquivo) throws Exception {
		return get(cdLoteImpressao, cdArquivo, new CustomConnection());
	}


	@Override
	public LoteImpressaoArquivo get(int cdLoteImpressao, int cdAit, CustomConnection customConnection) throws Exception {
		for(int i = 0; i < listLoteImpressaoArquivo.size(); i++) {
			LoteImpressaoArquivo loteImpressaoArquivoLista = listLoteImpressaoArquivo.get(i);
			if(loteImpressaoArquivoLista.getCdLoteImpressao() == cdLoteImpressao && loteImpressaoArquivoLista.getCdArquivo() == cdAit) { 
				return loteImpressaoArquivoLista;
			}
		}
		return null;
	}

	@Override
	public List<LoteImpressaoArquivo> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<LoteImpressaoArquivo> find(SearchCriterios searchCriterios, CustomConnection customConnection)
			throws Exception {
		List<LoteImpressaoArquivo> loteImpressaoArquivoFilter = new ArrayList<LoteImpressaoArquivo>(this.listLoteImpressaoArquivo);
		for(ItemComparator itemComparator : searchCriterios.getCriterios()) {
			if(itemComparator.getColumn().equals("cd_lote_impressao")) {
				if(itemComparator.getTypeComparation() == ItemComparator.EQUAL) {
					loteImpressaoArquivoFilter = loteImpressaoArquivoFilter.stream().filter(loteImpressaoArquivo -> loteImpressaoArquivo.getCdLoteImpressao() == Integer.parseInt(itemComparator.getValue())).collect(Collectors.toList());
				} 
			} 
		}
		return loteImpressaoArquivoFilter;
	}


}
