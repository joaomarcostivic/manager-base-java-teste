package com.tivic.manager.mob.colaborador;

import java.util.ArrayList;
import java.util.List;

import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.pessoafisica.IPessoaFisicaService;
import com.tivic.sol.cdi.BeansFactory;

public class ColaboradorDTOPageBuilder {
	private List<Pessoa> listPessoa;
	private List<ColaboradorDTO> listRelatorDTO;
	private IPessoaFisicaService pessoaFisicaService;

	public ColaboradorDTOPageBuilder(List<Pessoa> listPessoa) throws Exception {
		pessoaFisicaService = (IPessoaFisicaService) BeansFactory.get(IPessoaFisicaService.class);
		listRelatorDTO = new ArrayList<ColaboradorDTO>();
		this.listPessoa = listPessoa;
		pupulate();
	}
	
	private void pupulate() throws Exception {
		for (Pessoa pessoa: listPessoa) {
			ColaboradorDTO relator = new ColaboradorDTO();
			relator.setPessoa(pessoa);
			relator.setPessoaFisica(pessoaFisicaService.get(pessoa.getCdPessoa()));
			this.listRelatorDTO.add(relator);
		}
	}
	
	public List<ColaboradorDTO> build() {
		return this.listRelatorDTO;
	}
	
}
