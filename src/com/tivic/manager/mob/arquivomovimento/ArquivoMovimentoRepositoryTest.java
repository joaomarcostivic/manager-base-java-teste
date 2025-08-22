package com.tivic.manager.mob.arquivomovimento;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.mob.ArquivoMovimento;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.SearchCriterios;

public class ArquivoMovimentoRepositoryTest implements ArquivoMovimentoRepository {

	private List<ArquivoMovimento> arquivos;
	
	public ArquivoMovimentoRepositoryTest() {
		this.arquivos = new ArrayList<ArquivoMovimento>();
	}

	private int getProximoCdMovimento(int cdMovimento, int cdAit) {
		int cdArquivo = 1;
		for(int i = 0; i < this.arquivos.size(); i++) {
			ArquivoMovimento arquivoFromList = this.arquivos.get(i);
			if(arquivoFromList.getCdAit() == cdAit && arquivoFromList.getCdMovimento() == cdMovimento) {
				cdArquivo++;
			}
		}
		return cdArquivo;
	}
	
	@Override
	public void insert(ArquivoMovimento arquivoMovimento, CustomConnection customConnection) throws Exception {
		arquivoMovimento.setCdArquivoMovimento(getProximoCdMovimento(arquivoMovimento.getCdMovimento(), arquivoMovimento.getCdAit()));
		this.arquivos.add(arquivoMovimento);
	}

	@Override
	public void update(ArquivoMovimento arquivoMovimento, CustomConnection customConnection) throws Exception {
		for(int i = 0; i < this.arquivos.size(); i++) {
			ArquivoMovimento arquivoFromList = this.arquivos.get(i);
			if(arquivoFromList.getCdAit() == arquivoMovimento.getCdAit() 
				&& arquivoFromList.getCdMovimento() == arquivoMovimento.getCdMovimento()
				&& arquivoFromList.getCdArquivoMovimento() == arquivoMovimento.getCdArquivoMovimento()) {
				this.arquivos.set(i, arquivoMovimento);
			}
		}
	}

	@Override
	public ArquivoMovimento get(int cdArquivoMovimento, int cdMovimento, int cdAit) throws Exception {
		return get(cdArquivoMovimento, cdMovimento, cdAit, new CustomConnection());
	}

	@Override
	public ArquivoMovimento get(int cdArquivoMovimento, int cdMovimento, int cdAit, CustomConnection customConnection)
			throws Exception {
		for(int i = 0; i < this.arquivos.size(); i++) {
			ArquivoMovimento arquivoFromList = this.arquivos.get(i);
			if(arquivoFromList.getCdAit() == cdAit
				&& arquivoFromList.getCdMovimento() == cdMovimento
				&& arquivoFromList.getCdArquivoMovimento() == cdArquivoMovimento) {
				return arquivoFromList;
			}
		}	
		return null;
	}

	@Override
	public List<ArquivoMovimento> find(SearchCriterios searchCriterios) throws Exception {
		return find(searchCriterios, new CustomConnection());
	}

	@Override
	public List<ArquivoMovimento> find(SearchCriterios searchCriterios, CustomConnection customConnection)
			throws Exception {
		return this.arquivos;
	}

}
