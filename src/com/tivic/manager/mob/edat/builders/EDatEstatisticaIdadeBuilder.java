package com.tivic.manager.mob.edat.builders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.tivic.manager.mob.edat.dto.EdatEstatisticaIdadeDTO;

public class EDatEstatisticaIdadeBuilder {
	
	private List<EdatEstatisticaIdadeDTO> list;
	private List<EdatEstatisticaIdadeDTO> currentList;
	
	public EDatEstatisticaIdadeBuilder(List<EdatEstatisticaIdadeDTO> list) {
		this.list = new ArrayList<EdatEstatisticaIdadeDTO>();
		this.currentList = list;
	}
	
	
	public EDatEstatisticaIdadeBuilder agrupar() {
		if(currentList.size() == 0) {
			return this;
		}
		
		int total = 0;
		for(EdatEstatisticaIdadeDTO item : currentList) {
			_verificarFaixaEtaria(item, 1, 17);
			_verificarFaixaEtaria(item, 18, 22);
			_verificarFaixaEtaria(item, 23, 27);
			_verificarFaixaEtaria(item, 28, 32);
			_verificarFaixaEtaria(item, 33, 37);
			_verificarFaixaEtaria(item, 38, 43);
			_verificarFaixaEtaria(item, 44, 48);
			_verificarFaixaEtaria(item, 49, 53);
			_verificarFaixaEtaria(item, 54, 59);
			_verificarFaixaEtaria(item, 60, 99);
			total += item.getQtIdade();
		}
		
		System.out.println(total);
		
		return this;
	}

	
	public List<EdatEstatisticaIdadeDTO> build() {		
		return list;		
	}
	
	private void _verificarFaixaEtaria(EdatEstatisticaIdadeDTO item, int nrInicial, int nrFinal) {		
		int idade = Integer.valueOf(item.getNmIdade());
		
		if(!(idade >= nrInicial && idade <= nrFinal))
			return;
		
		EdatEstatisticaIdadeDTO faixa;
		
		Optional<EdatEstatisticaIdadeDTO> find = list.stream()
				                        .filter(f -> f.getNmIdade().equals(nrInicial+"-"+nrFinal))
				                        .findFirst();
		
		if(!find.isPresent()) {
			faixa = new EdatEstatisticaIdadeDTO(item.getQtIdade(), nrInicial+"-"+nrFinal);
			list.add(faixa);
			return;
		}
		
		faixa = find.get();
		faixa.setQtIdade(faixa.getQtIdade() + item.getQtIdade());		
	}
}
