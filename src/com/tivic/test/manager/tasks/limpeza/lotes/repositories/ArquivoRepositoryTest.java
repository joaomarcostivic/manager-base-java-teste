package com.tivic.test.manager.tasks.limpeza.lotes.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class ArquivoRepositoryTest implements IArquivoRepository {

	private List<Arquivo> listArquivo;
	
	public ArquivoRepositoryTest() {
		listArquivo = new ArrayList<>();
		listArquivo.add(criarArquivo1());
		listArquivo.add(criarArquivo2());
		listArquivo.add(criarArquivo3());
		listArquivo.add(criarArquivo4());
		listArquivo.add(criarArquivo5());
		listArquivo.add(criarArquivo6());
		listArquivo.add(criarArquivo7());
		listArquivo.add(criarArquivo8());
		listArquivo.add(criarArquivo9());
	}
	
	private Arquivo criarArquivo1() {
		Arquivo arquivo = new Arquivo();
		arquivo.setCdArquivo(1);
		return arquivo;
	}

	private Arquivo criarArquivo2() {
		Arquivo arquivo = new Arquivo();
		arquivo.setCdArquivo(2);
		return arquivo;
	}

	private Arquivo criarArquivo3() {
		Arquivo arquivo = new Arquivo();
		arquivo.setCdArquivo(3);
		return arquivo;
	}

	private Arquivo criarArquivo4() {
		Arquivo arquivo = new Arquivo();
		arquivo.setCdArquivo(4);
		return arquivo;
	}

	private Arquivo criarArquivo5() {
		Arquivo arquivo = new Arquivo();
		arquivo.setCdArquivo(5);
		return arquivo;
	}

	private Arquivo criarArquivo6() {
		Arquivo arquivo = new Arquivo();
		arquivo.setCdArquivo(6);
		return arquivo;
	}

	private Arquivo criarArquivo7() {
		Arquivo arquivo = new Arquivo();
		arquivo.setCdArquivo(7);
		return arquivo;
	}

	private Arquivo criarArquivo8() {
		Arquivo arquivo = new Arquivo();
		arquivo.setCdArquivo(8);
		return arquivo;
	}

	private Arquivo criarArquivo9() {
		Arquivo arquivo = new Arquivo();
		arquivo.setCdArquivo(9);
		return arquivo;
	}
	
	@Override
	public Arquivo insert(Arquivo arquivo, CustomConnection customConnection) throws Exception {
		this.listArquivo.add(arquivo);
		return arquivo;
	}

	@Override
	public Arquivo update(Arquivo arquivo, CustomConnection customConnection) throws Exception {
		for(int i = 0; i < listArquivo.size(); i++) {
			Arquivo arquivoLista = listArquivo.get(i);
			if(arquivoLista.getCdArquivo() == arquivo.getCdArquivo()) { 
				listArquivo.set(i, arquivo);
			}
		}
		return arquivo;
	}

	@Override
	public void delete(Integer cdArquivo, CustomConnection customConnection) throws Exception {
		for(int i = 0; i < listArquivo.size(); i++) {
			Arquivo arquivoLista = listArquivo.get(i);
			if(arquivoLista.getCdArquivo() == cdArquivo) { 
				listArquivo.remove(i);
			}
		}
	}

	@Override
	public Arquivo get(int cdArquivo) throws Exception {
		return get(cdArquivo, new CustomConnection());
	}

	@Override
	public Arquivo get(int cdArquivo, CustomConnection customConnection) throws Exception {
		for(int i = 0; i < listArquivo.size(); i++) {
			Arquivo arquivoLista = listArquivo.get(i);
			if(arquivoLista.getCdArquivo() == cdArquivo) { 
				return arquivoLista;
			}
		}
		return null;
	}

	@Override
	public List<Arquivo> find(SearchCriterios searchCriterios) throws IllegalArgumentException, Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<Arquivo> find(SearchCriterios searchCriterios, CustomConnection customConnection)
			throws Exception {
		List<Arquivo> arquivoFilter = new ArrayList<Arquivo>(this.listArquivo);
		for(ItemComparator itemComparator : searchCriterios.getCriterios()) {
			if(itemComparator.getColumn().equals("cd_arquivo")) {
				if(itemComparator.getTypeComparation() == ItemComparator.EQUAL) {
					arquivoFilter = arquivoFilter.stream().filter(arquivo -> arquivo.getCdArquivo() == Integer.parseInt(itemComparator.getValue())).collect(Collectors.toList());
				} 
			} 
		}
		return arquivoFilter;
	}


	@Override
	public void insertCodeSync(Arquivo arquivo, CustomConnection customConnection) throws Exception {
		// TODO Auto-generated method stub
		
	}


}
