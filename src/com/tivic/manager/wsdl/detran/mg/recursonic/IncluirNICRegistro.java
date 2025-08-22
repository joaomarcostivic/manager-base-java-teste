package com.tivic.manager.wsdl.detran.mg.recursonic;

import java.util.GregorianCalendar;

import com.tivic.manager.crt.PessoaServices;
import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.fta.MarcaModeloDAO;
import com.tivic.manager.fta.MarcaModeloServices;
import com.tivic.manager.grl.Bairro;
import com.tivic.manager.grl.BairroServices;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.grl.cidade.cidadeproprietario.ICorretorIDCidade;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.ArquivoMovimento;
import com.tivic.manager.mob.AitInconsistencia.AitInconsistencia;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.arquivomovimento.ArquivoMovimentoRepository;
import com.tivic.manager.mob.inconsistencias.AitInconsistenciaBuilder;
import com.tivic.manager.mob.inconsistencias.AitInconsistenciaService;
import com.tivic.manager.mob.lote.impressao.viaunica.nic.ListMovimentosNicBuilder;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.TabelasAuxiliaresMG;
import com.tivic.manager.wsdl.interfaces.DetranRegistro;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.log.ManagerLog;

import sol.util.Result;

public class IncluirNICRegistro extends DetranRegistro {
	private AitRepository aitRepository;
	private AitMovimentoRepository aitMovimentoRepository;
	private ArquivoMovimentoRepository arquivoMovimentoRepository;
	private ICorretorIDCidade cidadeProprietario;
	private ManagerLog managerLog;

	public IncluirNICRegistro() throws Exception {
		aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		arquivoMovimentoRepository = (ArquivoMovimentoRepository) BeansFactory.get(ArquivoMovimentoRepository.class);
		cidadeProprietario = (ICorretorIDCidade) BeansFactory.get(ICorretorIDCidade.class);
		managerLog = (ManagerLog) BeansFactory.get(ManagerLog.class);
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
		RecursoNICDadosRetorno recursoNICDadosRetorno = (RecursoNICDadosRetorno) servicoDetranObjetoMg.getDadosRetorno();
		if(recursoNICDadosRetorno.getCodigoRetorno() == 0){
			Ait ait = this.atualizarEnvioDetran(servicoDetranObjetoMg, aitMovimento, customConnection);
			incluirDadosRegistro(servicoDetranObjetoMg, ait, customConnection);
			new ListMovimentosNicBuilder().gerarMovimentoRegistro(ait, aitMovimento, customConnection);
			return ait;
		}
		else{
			Ait ait = this.atualizarErro(servicoDetranObjetoMg, aitMovimento, customConnection);
			salvarInconsistenciaNIC(ait, aitMovimento.getTpStatus());
			recuperarPrazoDefesa(servicoDetranObjetoMg, ait);
			return ait;
		}
	}
	
	private void incluirDadosRegistro(ServicoDetranObjetoMG servicoDetranObjetoMG, Ait ait, CustomConnection customConnection) throws Exception {
		RecursoNICDadosRetorno recursoNICDadosRetorno = (RecursoNICDadosRetorno) servicoDetranObjetoMG.getDadosRetorno();
		ait.setNrControle(recursoNICDadosRetorno.getNumeroProcessamento());
		adicionarRenainf(ait, recursoNICDadosRetorno);
		ait.setSgUfVeiculo(recursoNICDadosRetorno.getUfJurisdicaoVeiculo());
		adicionarCidade(ait, recursoNICDadosRetorno, customConnection);
		adicionarMarcaModelo(ait, recursoNICDadosRetorno, customConnection);
		ait.setNrRenavan(recursoNICDadosRetorno.getCodigoRenavam());
		ait.setCdTipoVeiculo(recursoNICDadosRetorno.getCodigoTipo());
		ait.setCdEspecieVeiculo(recursoNICDadosRetorno.getCodigoEspecie());
		ait.setCdCategoriaVeiculo(recursoNICDadosRetorno.getCodigoCategoria());
		ait.setCdCorVeiculo(recursoNICDadosRetorno.getCodigoCor());
		ait.setTpPessoaProprietario(recursoNICDadosRetorno.getOrigemPossuidorVeiculo());
		ait.setTpDocumento(recursoNICDadosRetorno.getTipoDocumentoPossuidor());
		ait.setNrDocumento(recursoNICDadosRetorno.getNumeroIdentificacaoPossuidor());
		ait.setNmProprietario(recursoNICDadosRetorno.getNomePossuidor());
		ait.setDsLogradouro(recursoNICDadosRetorno.getNomeLogradouroPossuidor());
		ait.setDsNrImovel(recursoNICDadosRetorno.getNumeroImovelPossuidor());
		ait.setNmComplemento(recursoNICDadosRetorno.getComplementoImovelPossuidor());
		ait.setNrCep(recursoNICDadosRetorno.getCepImovelPossuidor());
		ait.setDtPrazoDefesa(recursoNICDadosRetorno.getPrazoDefesa());
		adicionarCidadeProprietario(ait, recursoNICDadosRetorno, customConnection);
		adicionarBairroProprietario(ait, recursoNICDadosRetorno, customConnection);
		adicionarTipoPessoa(ait, recursoNICDadosRetorno, customConnection);
		servicoDetranObjetoMG.getAitMovimento().setDtAdesaoSne(recursoNICDadosRetorno.getSneDataAdesaoVeiculo());
		servicoDetranObjetoMG.getAitMovimento().setStAdesaoSne(recursoNICDadosRetorno.getSneIndicadorAdesaoVeiculo());
	}
	
	
	private void adicionarRenainf(Ait ait, RecursoNICDadosRetorno recursoNICDadosRetorno){
		try{
			ait.setNrRenainf(recursoNICDadosRetorno.getCodigoRenainf());
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
	}
	
	private void adicionarCidade(Ait ait, RecursoNICDadosRetorno recursoNICDadosRetorno, CustomConnection customConnection) throws Exception{
		Cidade cidade =  this.cidadeProprietario.getCidadeById(recursoNICDadosRetorno.getCodigoMunicipioEmplacamento());
		if(cidade != null){
			ait.setCdCidade(cidade.getCdCidade());
		}
	}
	
	private void adicionarMarcaModelo(Ait ait, RecursoNICDadosRetorno recursoNICDadosRetorno, CustomConnection customConnection){
		MarcaModelo marcaModelo = MarcaModeloServices.getByNrMarca(String.valueOf(recursoNICDadosRetorno.getCodigoMarcaModelo()));
		if(marcaModelo==null)
			marcaModelo = MarcaModeloDAO.get(recursoNICDadosRetorno.getCodigoMarcaModelo(), customConnection.getConnection());
		
		if(marcaModelo != null){
			ait.setCdMarcaVeiculo(marcaModelo.getCdMarca());
		}
	}

	private void adicionarCidadeProprietario(Ait ait, RecursoNICDadosRetorno recursoNICDadosRetorno, CustomConnection customConnection) throws Exception {
		Cidade cidade = this.cidadeProprietario.getCidadeById(recursoNICDadosRetorno.getCodigoMunicipioPossuidor());
		if(cidade != null)
			ait.setCdCidadeProprietario(cidade.getCdCidade());
	}
	
	private void adicionarBairroProprietario(Ait ait, RecursoNICDadosRetorno recursoNICDadosRetorno, CustomConnection customConnection){
		Bairro bairro = BairroServices.getBairroByNomeCidade(recursoNICDadosRetorno.getBairroImovelPossuidor(), ait.getCdCidade(), customConnection.getConnection());
		if(bairro != null)
			ait.setCdBairro(bairro.getCdBairro());
		else {
			bairro = new Bairro();
			bairro.setCdCidade(ait.getCdCidade());
			bairro.setNmBairro(recursoNICDadosRetorno.getBairroImovelPossuidor());
			Result result = BairroServices.save(bairro, customConnection.getConnection());
			if(result.getCode() > 0) {
				ait.setCdBairro(bairro.getCdBairro());
			}
		}
	}

	private void adicionarTipoPessoa(Ait ait, RecursoNICDadosRetorno recursoNICDadosRetorno, CustomConnection customConnection){
		if(recursoNICDadosRetorno.getTipoDocumentoPossuidor() == TabelasAuxiliaresMG.TP_DOCUMENTO_CPF
				|| recursoNICDadosRetorno.getTipoDocumentoPossuidor() == TabelasAuxiliaresMG.TP_DOCUMENTO_RG)
			ait.setTpPessoaProprietario(PessoaServices.FISICA);
		else if(recursoNICDadosRetorno.getTipoDocumentoPossuidor() == TabelasAuxiliaresMG.TP_DOCUMENTO_CNPJ)
			ait.setTpPessoaProprietario(PessoaServices.JURIDICA);
		
		if(recursoNICDadosRetorno.getTipoDocumentoPossuidor() == TabelasAuxiliaresMG.TP_DOCUMENTO_CPF
				|| recursoNICDadosRetorno.getTipoDocumentoPossuidor() == TabelasAuxiliaresMG.TP_DOCUMENTO_CNPJ)
			ait.setNrCpfCnpjProprietario(recursoNICDadosRetorno.getNumeroIdentificacaoPossuidor());
	}
	
	private void salvarInconsistenciaNIC(Ait ait, int tpStatus) throws Exception {
		managerLog.info("Salvando inconsistencia: ", ait.getIdAit());
		int tpErroRegistroNIC = ParametroServices.getValorOfParametroAsInteger("MOB_INCONSISTENCIA_PROPRIEDADE_VEICULAR_NIC", 0);
		AitInconsistencia aitInconsistencia = new AitInconsistenciaBuilder().build(ait, tpErroRegistroNIC, tpStatus);
		new AitInconsistenciaService().salvarInconsistencia(aitInconsistencia);
	}
		
	private void recuperarPrazoDefesa(ServicoDetranObjetoMG servicoDetranObjetoMG, Ait ait) {
		RecursoNICDadosRetorno recursoNICDadosRetorno = (RecursoNICDadosRetorno) servicoDetranObjetoMG.getDadosRetorno();
		GregorianCalendar prazoDefesa = recursoNICDadosRetorno.getPrazoDefesa();
		if(prazoDefesa != null)
			ait.setDtPrazoDefesa(prazoDefesa);
	}

}
