package com.tivic.manager.mob.colaborador;
import com.tivic.manager.grl.pessoavinculohistorico.PessoaVinculoHistorico;
import com.tivic.manager.grl.pessoavinculohistorico.PessoaVinculoHistoricoDTO;
import com.tivic.manager.grl.pessoavinculohistorico.repository.PessoaVinculoHistoricoRepository;
import com.tivic.sol.cdi.BeansFactory;

public class PessoaVinculoHistoricoDTOBuilder {
	private PessoaVinculoHistoricoRepository pessoaVinculoHistoricoRepository;
	private PessoaVinculoHistoricoDTO pessoaVinculoHistoricoDTO;
	
	public PessoaVinculoHistoricoDTOBuilder(PessoaVinculoHistoricoDTO pessoaVinculoHistorico) throws Exception {
		pessoaVinculoHistoricoRepository = (PessoaVinculoHistoricoRepository) BeansFactory.get(PessoaVinculoHistoricoRepository.class);
		this.pessoaVinculoHistoricoDTO = pessoaVinculoHistorico;
	}
	
	public PessoaVinculoHistoricoDTOBuilder populate() throws Exception{
		pessoaVinculoHistorico();
		return this;
	}
	
	private void pessoaVinculoHistorico() throws Exception {
		PessoaVinculoHistorico pessoaVinculoHistorico = this.pessoaVinculoHistoricoRepository.get(pessoaVinculoHistoricoDTO.getCdPessoa());
		this.pessoaVinculoHistoricoDTO.setCdVinculo(pessoaVinculoHistorico.getCdVinculo());
		this.pessoaVinculoHistoricoDTO.setCdPessoa(pessoaVinculoHistorico.getCdPessoa());
		this.pessoaVinculoHistoricoDTO.setDtVinculoHistorico(pessoaVinculoHistorico.getDtVinculoHistorico());
		this.pessoaVinculoHistoricoDTO.setStVinculo(pessoaVinculoHistorico.getStVinculo());
		return;
	}
	
	public PessoaVinculoHistoricoDTO build() {
		return pessoaVinculoHistoricoDTO;
	}

}
