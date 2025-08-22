package com.tivic.manager.mob.colaborador;
import com.tivic.manager.grl.Arquivo;
import com.tivic.manager.grl.IPessoaRepository;
import com.tivic.manager.grl.Pessoa;
import com.tivic.manager.grl.PessoaFisica;
import com.tivic.manager.grl.Vinculo;
import com.tivic.manager.grl.arquivo.IArquivoRepository;
import com.tivic.manager.grl.pessoafisica.IPessoaFisicaService;
import com.tivic.manager.grl.vinculo.VinculoRepository;
import com.tivic.manager.mob.Agente;
import com.tivic.manager.mob.agente.AgenteRepository;
import com.tivic.manager.seg.Usuario;
import com.tivic.manager.seg.usuario.repositories.IUsuarioRepository;
import com.tivic.sol.cdi.BeansFactory;

public class ColaboradorDTOBuilder {
	private IPessoaFisicaService pessoaFisicaService;
	private IPessoaRepository pessoaRepository;
	private AgenteRepository agenteRepository;
	private IUsuarioRepository usuarioRepository;
	private VinculoRepository vinculoRepository;
	private ColaboradorDTO colaboradorDTO;
	private IArquivoRepository arquivoRepository;
	
	public ColaboradorDTOBuilder(ColaboradorDTO colaboradorDTO) throws Exception {
		pessoaRepository = (IPessoaRepository) BeansFactory.get(IPessoaRepository.class);
		pessoaFisicaService = (IPessoaFisicaService) BeansFactory.get(IPessoaFisicaService.class);
		agenteRepository = (AgenteRepository) BeansFactory.get(AgenteRepository.class);
		usuarioRepository = (IUsuarioRepository) BeansFactory.get(IUsuarioRepository.class);
		vinculoRepository = (VinculoRepository) BeansFactory.get(VinculoRepository.class);
		arquivoRepository = (IArquivoRepository) BeansFactory.get(IArquivoRepository.class);
		
		this.colaboradorDTO = colaboradorDTO;
	}
	
	public ColaboradorDTOBuilder populate() throws Exception{
		pessoa();
		pessoaFisica();
		agente();
		usuario();
		vinculo();
		arquivo();
		return this;
	}
	
	private void pessoa() throws Exception {
		Pessoa pessoa = this.pessoaRepository.get(colaboradorDTO.getCdPessoa());
		this.colaboradorDTO.setPessoa(pessoa);
		return;
	}
	private void pessoaFisica() throws Exception {
		PessoaFisica pessoaFisica = this.pessoaFisicaService.getPessoaFisica(colaboradorDTO.getCdPessoa());
		this.colaboradorDTO.setPessoaFisica(pessoaFisica);
		return;
	}
	
	private void agente() throws Exception {
		Agente agente = this.agenteRepository.get(colaboradorDTO.getCdAgente());
		this.colaboradorDTO.setAgente(agente);
	}
	
	private void usuario() throws Exception {
		Usuario usuario = this.usuarioRepository.get(colaboradorDTO.getCdUsuario());
		this.colaboradorDTO.setUsuario(usuario);
	}
	
	private void vinculo() throws Exception {
		Vinculo vinculo = this.vinculoRepository.get(colaboradorDTO.getCdVinculo());
		this.colaboradorDTO.setVinculo(vinculo);
	}
	
	private void arquivo() throws Exception {
		Arquivo arquivo = this.arquivoRepository.get(colaboradorDTO.getCdArquivo());
		this.colaboradorDTO.setArquivo(arquivo);
	}
	
	public ColaboradorDTO build() {
		return colaboradorDTO;
	}

}
