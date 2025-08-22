package com.tivic.manager.wsdl.detran.mg.incluirautoinfracao;

import java.util.GregorianCalendar;

import com.tivic.manager.crt.PessoaServices;
import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.fta.MarcaModeloDAO;
import com.tivic.manager.fta.MarcaModeloServices;
import com.tivic.manager.grl.Bairro;
import com.tivic.manager.grl.BairroServices;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.cidade.cidadeproprietario.ICorretorIDCidade;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.ArquivoMovimento;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.arquivomovimento.ArquivoMovimentoRepository;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.TabelasAuxiliaresMG;
import com.tivic.manager.wsdl.interfaces.DetranRegistro;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

import sol.util.Result;

public class IncluirAutoInfracaoRegistro extends DetranRegistro {

	private AitRepository aitRepository;
	private AitMovimentoRepository aitMovimentoRepository;
	private ArquivoMovimentoRepository arquivoMovimentoRepository;
	private ICorretorIDCidade cidadeProprietario;
	
	public IncluirAutoInfracaoRegistro() throws Exception {
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.arquivoMovimentoRepository = (ArquivoMovimentoRepository) BeansFactory.get(ArquivoMovimentoRepository.class);
		this.cidadeProprietario = (ICorretorIDCidade) BeansFactory.get(ICorretorIDCidade.class);
	}
	
	@Override
	public void registrar(ServicoDetranObjeto servicoDetranObjeto, boolean lgHomologacao) throws Exception{
		CustomConnection customConnection = new CustomConnection();
		try{
			customConnection.initConnection(true);
			ServicoDetranObjetoMG servicoDetranObjetoMg = (ServicoDetranObjetoMG)servicoDetranObjeto;
			AitMovimento aitMovimento = servicoDetranObjetoMg.getAitMovimento();
			Ait ait =  atualizarAit(servicoDetranObjetoMg, aitMovimento, customConnection);
			ArquivoMovimento arquivoMovimento = gerarArquivoMovimento(servicoDetranObjetoMg, aitMovimento);
			this.aitRepository.update(ait, customConnection);
			this.aitMovimentoRepository.update(aitMovimento, customConnection);
			this.arquivoMovimentoRepository.insert(arquivoMovimento, customConnection);
			customConnection.finishConnection();
		} catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		} finally{
			customConnection.closeConnection();
		}
	}
	
	private Ait atualizarAit(ServicoDetranObjetoMG servicoDetranObjetoMg, AitMovimento aitMovimento, CustomConnection customConnection) throws Exception{
		IncluirAutoInfracaoDadosRetorno incluirAutoInfracaoDadosRetorno = (IncluirAutoInfracaoDadosRetorno) servicoDetranObjetoMg.getDadosRetorno();
		if(incluirAutoInfracaoDadosRetorno.getCodigoRetorno() == 0){
			Ait ait = this.atualizarEnvioDetran(servicoDetranObjetoMg, aitMovimento, customConnection);
			incluirDadosRegistro(servicoDetranObjetoMg, ait, customConnection);
			return ait;
		}
		else{
			Ait ait = this.atualizarErro(servicoDetranObjetoMg, aitMovimento, customConnection);
			recuperarPrazoDefesa(servicoDetranObjetoMg, ait);
			return ait;
		}
	}
		
	private void incluirDadosRegistro(ServicoDetranObjetoMG servicoDetranObjetoMG, Ait ait, CustomConnection customConnection) throws Exception {
		IncluirAutoInfracaoDadosRetorno incluirAutoInfracaoDadosRetorno = (IncluirAutoInfracaoDadosRetorno) servicoDetranObjetoMG.getDadosRetorno();
		ait.setNrControle(incluirAutoInfracaoDadosRetorno.getNumeroProcessamento());
		adicionarRenainf(ait, incluirAutoInfracaoDadosRetorno);
		ait.setSgUfVeiculo(incluirAutoInfracaoDadosRetorno.getUfJurisdicaoVeiculo());
		adicionarCidade(ait, incluirAutoInfracaoDadosRetorno, customConnection);
		adicionarMarcaModelo(ait, incluirAutoInfracaoDadosRetorno, customConnection);
		ait.setNrRenavan(incluirAutoInfracaoDadosRetorno.getCodigoRenavam());
		ait.setCdTipoVeiculo(incluirAutoInfracaoDadosRetorno.getCodigoTipo());
		ait.setCdEspecieVeiculo(incluirAutoInfracaoDadosRetorno.getCodigoEspecie());
		ait.setCdCategoriaVeiculo(incluirAutoInfracaoDadosRetorno.getCodigoCategoria());
		ait.setCdCorVeiculo(incluirAutoInfracaoDadosRetorno.getCodigoCor());
		ait.setTpPessoaProprietario(incluirAutoInfracaoDadosRetorno.getOrigemPossuidorVeiculo());
		ait.setTpDocumento(incluirAutoInfracaoDadosRetorno.getTipoDocumentoPossuidor());
		ait.setNrDocumento(incluirAutoInfracaoDadosRetorno.getNumeroIdentificacaoPossuidor());
		ait.setNmProprietario(incluirAutoInfracaoDadosRetorno.getNomePossuidor());
		ait.setDsLogradouro(incluirAutoInfracaoDadosRetorno.getNomeLogradouroPossuidor());
		ait.setDsNrImovel(incluirAutoInfracaoDadosRetorno.getNumeroImovelPossuidor());
		ait.setNmComplemento(incluirAutoInfracaoDadosRetorno.getComplementoImovelPossuidor());
		ait.setNrCep(incluirAutoInfracaoDadosRetorno.getCepImovelPossuidor());
		ait.setDtPrazoDefesa(incluirAutoInfracaoDadosRetorno.getPrazoDefesa());
		adicionarCidadeProprietario(ait, incluirAutoInfracaoDadosRetorno, customConnection);
		adicionarBairroProprietario(ait, incluirAutoInfracaoDadosRetorno, customConnection);
		adicionarTipoPessoa(ait, incluirAutoInfracaoDadosRetorno, customConnection);
		servicoDetranObjetoMG.getAitMovimento().setDtAdesaoSne(incluirAutoInfracaoDadosRetorno.getSneDataAdesaoVeiculo());
		servicoDetranObjetoMG.getAitMovimento().setStAdesaoSne(incluirAutoInfracaoDadosRetorno.getSneIndicadorAdesaoVeiculo());
	}
	
	private void adicionarRenainf(Ait ait, IncluirAutoInfracaoDadosRetorno incluirAutoInfracaoDadosRetorno){
		try{
			ait.setNrRenainf(incluirAutoInfracaoDadosRetorno.getCodigoRenainf());
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
	}
	
	private void adicionarCidade(Ait ait, IncluirAutoInfracaoDadosRetorno incluirAutoInfracaoDadosRetorno, CustomConnection customConnection) throws Exception{
		Cidade cidade =  this.cidadeProprietario.getCidadeById(incluirAutoInfracaoDadosRetorno.getCodigoMunicipioEmplacamento());
		if(cidade != null){
			ait.setCdCidade(cidade.getCdCidade());
		}
	}
	
	private void adicionarMarcaModelo(Ait ait, IncluirAutoInfracaoDadosRetorno incluirAutoInfracaoDadosRetorno, CustomConnection customConnection){
		MarcaModelo marcaModelo = MarcaModeloServices.getByNrMarca(String.valueOf(incluirAutoInfracaoDadosRetorno.getCodigoMarcaModelo()));
		if(marcaModelo==null)
			marcaModelo = MarcaModeloDAO.get(incluirAutoInfracaoDadosRetorno.getCodigoMarcaModelo(), customConnection.getConnection());
		
		if(marcaModelo != null){
			ait.setCdMarcaVeiculo(marcaModelo.getCdMarca());
		}
	}

	private void adicionarCidadeProprietario(Ait ait, IncluirAutoInfracaoDadosRetorno incluirAutoInfracaoDadosRetorno, CustomConnection customConnection) throws Exception {
		Cidade cidade = this.cidadeProprietario.getCidadeById(incluirAutoInfracaoDadosRetorno.getCodigoMunicipioPossuidor());
		if(cidade != null)
			ait.setCdCidadeProprietario(cidade.getCdCidade());
	}
	
	private void adicionarBairroProprietario(Ait ait, IncluirAutoInfracaoDadosRetorno incluirAutoInfracaoDadosRetorno, CustomConnection customConnection){
		Bairro bairro = BairroServices.getBairroByNomeCidade(incluirAutoInfracaoDadosRetorno.getBairroImovelPossuidor(), ait.getCdCidade(), customConnection.getConnection());
		if(bairro != null)
			ait.setCdBairro(bairro.getCdBairro());
		else {
			bairro = new Bairro();
			bairro.setCdCidade(ait.getCdCidade());
			bairro.setNmBairro(incluirAutoInfracaoDadosRetorno.getBairroImovelPossuidor());
			Result result = BairroServices.save(bairro, customConnection.getConnection());
			if(result.getCode() > 0) {
				ait.setCdBairro(bairro.getCdBairro());
			}
		}
	}

	private void adicionarTipoPessoa(Ait ait, IncluirAutoInfracaoDadosRetorno incluirAutoInfracaoDadosRetorno, CustomConnection customConnection){
		if(incluirAutoInfracaoDadosRetorno.getTipoDocumentoPossuidor() == TabelasAuxiliaresMG.TP_DOCUMENTO_CPF
				|| incluirAutoInfracaoDadosRetorno.getTipoDocumentoPossuidor() == TabelasAuxiliaresMG.TP_DOCUMENTO_RG)
			ait.setTpPessoaProprietario(PessoaServices.FISICA);
		else if(incluirAutoInfracaoDadosRetorno.getTipoDocumentoPossuidor() == TabelasAuxiliaresMG.TP_DOCUMENTO_CNPJ)
			ait.setTpPessoaProprietario(PessoaServices.JURIDICA);
		
		if(incluirAutoInfracaoDadosRetorno.getTipoDocumentoPossuidor() == TabelasAuxiliaresMG.TP_DOCUMENTO_CPF
				|| incluirAutoInfracaoDadosRetorno.getTipoDocumentoPossuidor() == TabelasAuxiliaresMG.TP_DOCUMENTO_CNPJ)
			ait.setNrCpfCnpjProprietario(incluirAutoInfracaoDadosRetorno.getNumeroIdentificacaoPossuidor());
	}
		
	private void recuperarPrazoDefesa(ServicoDetranObjetoMG servicoDetranObjetoMG, Ait ait) {
		IncluirAutoInfracaoDadosRetorno incluirAutoInfracaoDadosRetorno = (IncluirAutoInfracaoDadosRetorno) servicoDetranObjetoMG.getDadosRetorno();
		GregorianCalendar prazoDefesa = incluirAutoInfracaoDadosRetorno.getPrazoDefesa();
		if(prazoDefesa != null)
			ait.setDtPrazoDefesa(prazoDefesa);
	}
	
}
