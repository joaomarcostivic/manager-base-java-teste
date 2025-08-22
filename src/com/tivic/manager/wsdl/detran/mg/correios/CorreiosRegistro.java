package com.tivic.manager.wsdl.detran.mg.correios;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoServices;
import com.tivic.manager.mob.ArquivoMovimento;
import com.tivic.manager.mob.ait.AitRepository;
import com.tivic.manager.mob.aitmovimento.AitMovimentoRepository;
import com.tivic.manager.mob.arquivomovimento.ArquivoMovimentoRepository;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.interfaces.DetranRegistro;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;

public class CorreiosRegistro extends DetranRegistro{

	private AitRepository aitRepository;
	private AitMovimentoRepository aitMovimentoRepository;
	private ArquivoMovimentoRepository arquivoMovimentoRepository;
	
	public CorreiosRegistro() throws Exception {
		this.aitRepository = (AitRepository) BeansFactory.get(AitRepository.class);
		this.aitMovimentoRepository = (AitMovimentoRepository) BeansFactory.get(AitMovimentoRepository.class);
		this.arquivoMovimentoRepository = (ArquivoMovimentoRepository) BeansFactory.get(ArquivoMovimentoRepository.class);
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
		CorreiosDadosRetorno publicacaoDadosRetorno = (CorreiosDadosRetorno) servicoDetranObjetoMg.getDadosRetorno();
		if(publicacaoDadosRetorno.getCodigoRetorno() == 0){
			Ait ait = this.atualizarEnvioDetran(servicoDetranObjetoMg, aitMovimento, customConnection);
			return ait;
		}
		else{
			Ait ait = this.atualizarErro(servicoDetranObjetoMg, aitMovimento, customConnection);
			return ait;
		}
	}
}
