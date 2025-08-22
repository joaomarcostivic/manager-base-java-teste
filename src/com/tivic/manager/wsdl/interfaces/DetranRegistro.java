package com.tivic.manager.wsdl.interfaces;

import java.util.GregorianCalendar;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.ArquivoMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoAtualizaStatusAit;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.aitmovimento.IAitMovimentoService;
import com.tivic.manager.mob.aitmovimento.MovimentoNaoAtualizaStatusAit;
import com.tivic.manager.mob.aitmovimento.TipoLgEnviadoDetranEnum;
import com.tivic.manager.mob.arquivomovimento.ArquivoMovimentoRepository;
import com.tivic.manager.wsdl.detran.mg.DadosRetornoMG;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class DetranRegistro {

	private AitRepository aitRepository;
	private AitMovimentoRepository aitMovimentoRepository;
	private ArquivoMovimentoRepository arquivoMovimentoRepository;
	private MovimentoNaoAtualizaStatusAit movimentoNaoAtualizaStatusAit;
	private AitMovimentoAtualizaStatusAit aitMovimentoAtualizaStatusAit;
	private IAitMovimentoService aitMovimentoService;
	
	public DetranRegistro() throws Exception {
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.arquivoMovimentoRepository = (ArquivoMovimentoRepository) BeansFactory.get(ArquivoMovimentoRepository.class);
		this.movimentoNaoAtualizaStatusAit = new MovimentoNaoAtualizaStatusAit();
		this.aitMovimentoAtualizaStatusAit = new AitMovimentoAtualizaStatusAit();
		this.aitMovimentoService = (IAitMovimentoService) BeansFactory.get(IAitMovimentoService.class);
	}
	
	public void registrar(ServicoDetranObjeto servicoDetranObjeto, boolean lgHomologacao) throws Exception{
		CustomConnection customConnection = new CustomConnection();
		try{
			customConnection.initConnection(true);
			ServicoDetranObjetoMG servicoDetranObjetoMg = (ServicoDetranObjetoMG) servicoDetranObjeto;
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
	
	public ArquivoMovimento gerarArquivoMovimento(ServicoDetranObjeto servicoDetranObjeto, AitMovimento aitMovimento) throws Exception{
		ArquivoMovimento arquivoMovimento = new ArquivoMovimento();
		arquivoMovimento.setCdMovimento(aitMovimento.getCdMovimento());
		arquivoMovimento.setCdAit(aitMovimento.getCdAit());
		arquivoMovimento.setTpArquivo(aitMovimento.getTpArquivo());
		arquivoMovimento.setNrRemessa(aitMovimento.getNrRemessa());
		arquivoMovimento.setNrSequencial(aitMovimento.getNrSequencial());
		arquivoMovimento.setTpStatus(aitMovimento.getTpStatus());
		arquivoMovimento.setNrErro(aitMovimento.getNrErro());
		arquivoMovimento.setDsEntrada(servicoDetranObjeto.getDadosEntrada().toString().replaceAll("\n", "").replaceAll("\r", ""));
		arquivoMovimento.setDsSaida(servicoDetranObjeto.getDadosRetorno().toString().replaceAll("\n", "").replaceAll("\r", ""));
		arquivoMovimento.setTpOrigem(1);//:TODO Saber o que eh o tipo de origem
		arquivoMovimento.setDtArquivo(new GregorianCalendar());
		return arquivoMovimento;
	}

	private Ait atualizarAit(ServicoDetranObjetoMG servicoDetranObjetoMg, AitMovimento aitMovimento, CustomConnection connection) throws Exception{
		DadosRetornoMG dadosRetorno = servicoDetranObjetoMg.getDadosRetorno();
		if(dadosRetorno.getCodigoRetorno() == 0){
			return atualizarEnvioDetran(servicoDetranObjetoMg, aitMovimento, connection);
		}
		else{
			return atualizarErro(servicoDetranObjetoMg, aitMovimento, connection);
		}
	}
	
	protected Ait atualizarEnvioDetran(ServicoDetranObjetoMG servicoDetranObjetoMG, AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
	    this.setAitMovimentoDetran(aitMovimento);
	    this.setEnvioDetranConformeStatus(aitMovimento, customConnection);
	    
	    Ait ait = servicoDetranObjetoMG.getAit();
	    this.setAitComDadosRetorno(ait, servicoDetranObjetoMG);

	    if (!movimentoNaoAtualizaStatusAit.verificar(aitMovimento.getTpStatus())) {
	        this.setAitComMovimento(ait, aitMovimento);
	    } else {
	        if (validaStatusAit(aitMovimento)) {
	            return atualizaStatusAit(aitMovimento, customConnection);                
	        }
	    }

	    return ait;
	}

	private void setAitMovimentoDetran(AitMovimento aitMovimento) {
	    aitMovimento.setLgEnviadoDetran(AitMovimentoServices.REGISTRADO);
	    aitMovimento.setDtRegistroDetran(new GregorianCalendar());
	}

	private void setEnvioDetranConformeStatus(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
	    if (aitMovimento.getTpStatus() == TipoStatusEnum.CANCELAMENTO_PAGAMENTO.getKey() || 
	    		aitMovimento.getTpStatus() == TipoStatusEnum.CANCELAMENTO_NIP.getKey()) {
	    	this.atualizaEnvioDetran(aitMovimento, customConnection);
	    }
	}

	private void setAitComDadosRetorno(Ait ait, ServicoDetranObjetoMG servicoDetranObjetoMG) {
	    ait.setNrErro(String.valueOf(servicoDetranObjetoMG.getDadosRetorno().getCodigoRetorno()));
	    ait.setLgEnviadoDetran(AitMovimentoServices.REGISTRADO);
	    ait.setDtRegistroDetran(new GregorianCalendar());
	}

	private void setAitComMovimento(Ait ait, AitMovimento aitMovimento) {
	    ait.setTpStatus(aitMovimento.getTpStatus());
	    ait.setCdMovimentoAtual(aitMovimento.getCdMovimento());
	}

	private boolean validaStatusAit(AitMovimento aitMovimento) {
	    int tpStatus = aitMovimento.getTpStatus();
	    return tpStatus == TipoStatusEnum.CANCELAMENTO_PAGAMENTO.getKey() 
	    		|| tpStatus == TipoStatusEnum.CANCELAMENTO_NIP.getKey() 
	    		|| tpStatus == TipoStatusEnum.CANCELAMENTO_DIVIDA_ATIVA.getKey();
	}
	
	
	private void atualizaEnvioDetran(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		aitMovimento = this.aitMovimentoService.getMovimentoTpStatus(aitMovimento.getCdAit(), getStatus(aitMovimento.getTpStatus()));
		if(aitMovimento.getLgEnviadoDetran() == TipoLgEnviadoDetranEnum.REGISTRADO.getKey()) {
			aitMovimento.setLgEnviadoDetran(TipoLgEnviadoDetranEnum.REGISTRO_CANCELADO.getKey());
			this.aitMovimentoRepository.update(aitMovimento, customConnection);
		}
	}
	
	private int getStatus(int tpStatus) {
		return tpStatus == TipoStatusEnum.CANCELAMENTO_NIP.getKey() ? TipoStatusEnum.NIP_ENVIADA.getKey() : TipoStatusEnum.MULTA_PAGA.getKey();
	}
	
	private Ait atualizaStatusAit(AitMovimento aitMovimento, CustomConnection customConnection) throws Exception {
		Ait ait = new Ait();
		ait = this.aitMovimentoAtualizaStatusAit.atualizaCancelado(aitMovimento, customConnection);
		return ait;
	}
	
	protected Ait atualizarErro(ServicoDetranObjetoMG servicoDetranObjetoMG, AitMovimento aitMovimento, CustomConnection connection) throws Exception{
		Ait ait = servicoDetranObjetoMG.getAit();
		int envioDetran = AitMovimentoServices.getStatusEnvio(servicoDetranObjetoMG.getDadosRetorno().getCodigoRetorno(), connection.getConnection());
		
		if (envioDetran == TipoStatusEnum.ENVIADO_AO_DETRAN.getKey() && !this.movimentoNaoAtualizaStatusAit.verificar(aitMovimento.getTpStatus())) {
			ait.setTpStatus(aitMovimento.getTpStatus());
			ait.setCdMovimentoAtual(aitMovimento.getCdMovimento());
		}
		
		aitMovimento.setLgEnviadoDetran(envioDetran);		
		aitMovimento.setNrErro(String.valueOf(servicoDetranObjetoMG.getDadosRetorno().getCodigoRetorno()));
		
		ait.setLgEnviadoDetran(envioDetran);
		ait.setNrErro(String.valueOf(servicoDetranObjetoMG.getDadosRetorno().getCodigoRetorno()));
		
		return ait;
	}

}
