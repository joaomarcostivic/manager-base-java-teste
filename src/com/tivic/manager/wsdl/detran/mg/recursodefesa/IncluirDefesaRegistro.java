package com.tivic.manager.wsdl.detran.mg.recursodefesa;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.AitMovimentoDetranServices;
import com.tivic.manager.mob.ArquivoMovimento;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.interfaces.DetranRegistro;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;
import com.tivic.sol.connection.CustomConnection;

public class IncluirDefesaRegistro extends DetranRegistro {

	int fimPrazoDefesaRegistrada = 115;
	int envioComSucesso = 0;
	
	public IncluirDefesaRegistro() throws Exception {
		super();
	}

	@Override
	public void registrar(ServicoDetranObjeto servicoDetranObjeto, boolean lgHomologacao) throws Exception {
		CustomConnection customConnection = new CustomConnection();
		try{
			customConnection.initConnection(true);
			ServicoDetranObjetoMG servicoDetranObjetoMg = (ServicoDetranObjetoMG)servicoDetranObjeto;
			AitMovimento aitMovimento = servicoDetranObjetoMg.getAitMovimento();
			Ait ait =  atualizarAit(servicoDetranObjetoMg, aitMovimento, customConnection);
			ArquivoMovimento arquivoMovimento = gerarArquivoMovimento(servicoDetranObjetoMg, aitMovimento);
			AitMovimentoDetranServices.save(new AitDetranObject(ait, aitMovimento, arquivoMovimento), null, customConnection.getConnection());
			customConnection.finishConnection();
		} catch(Exception e){
			e.printStackTrace();
			System.out.println(e.getMessage());
		} finally{
			customConnection.closeConnection();
		}
	}

	private Ait atualizarAit(ServicoDetranObjetoMG servicoDetranObjetoMg, AitMovimento aitMovimento, CustomConnection customConnection) throws Exception{
		RecursoDefesaDadosRetorno defesaPreviaDadosRetorno = (RecursoDefesaDadosRetorno) servicoDetranObjetoMg.getDadosRetorno();
		if(defesaPreviaDadosRetorno.getCodigoRetorno() == envioComSucesso || isFimPrazoDefesaHasRegister(aitMovimento, defesaPreviaDadosRetorno)){
			Ait ait = this.atualizarEnvioDetran(servicoDetranObjetoMg, aitMovimento, customConnection);
			setDataLimiteRecurso(ait, aitMovimento, servicoDetranObjetoMg);
			return ait;
		}
		else{
			return this.atualizarErro(servicoDetranObjetoMg, aitMovimento, customConnection);
		}
	}
		
	private void setDataLimiteRecurso(Ait ait, AitMovimento aitMovimento, ServicoDetranObjetoMG servicoDetranObjetoMG) {
		if (aitMovimento.getTpStatus() == TipoStatusEnum.FIM_PRAZO_DEFESA.getKey()) {
			RecursoDefesaDadosRetorno recurso = (RecursoDefesaDadosRetorno) servicoDetranObjetoMG.getDadosRetorno();
			ait.setDtVencimento(recurso.getDataLimiteRecurso());
		}
	}
	
	private boolean isFimPrazoDefesaHasRegister(AitMovimento aitMovimento, RecursoDefesaDadosRetorno defesaPreviaDadosRetorno ) {
		return aitMovimento.getTpStatus() == TipoStatusEnum.FIM_PRAZO_DEFESA.getKey() 
				&& defesaPreviaDadosRetorno.getCodigoRetorno() == fimPrazoDefesaRegistrada;
	}
	
}
