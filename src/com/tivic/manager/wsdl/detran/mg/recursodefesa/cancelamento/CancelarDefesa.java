package com.tivic.manager.wsdl.detran.mg.recursodefesa.cancelamento;

import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.detran.mg.recursodefesa.RecursoDefesaDadosEntrada;
import com.tivic.manager.wsdl.detran.mg.recursodefesa.RecursoDefesaDadosRetorno;
import com.tivic.manager.wsdl.interfaces.DetranRegistro;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class CancelarDefesa extends ServicoDetran {
	private DetranRegistro detranRegistro;
	
	public CancelarDefesa() throws Exception {
		this.detranRegistro = new DetranRegistro();
	}
	
	@Override
	public ServicoDetranObjeto executar(AitDetranObject aitDetranObject) {
		try{
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG();
			servicoDetranObjeto.setAit(aitDetranObject.getAit());
			servicoDetranObjeto.setAitMovimento(aitDetranObject.getAitMovimento());
			RecursoDefesaDadosEntrada defesaPreviaDadosEntrada = CancelarDefesaDadosEntradaFactory.fazerDadoEntrada(aitDetranObject, this.arquivoConfiguracao.getLgHomologacao());
			servicoDetranObjeto.setDadosEntrada(defesaPreviaDadosEntrada);
			RecursoDefesaDadosRetorno defesaPreviaDadosRetorno = (RecursoDefesaDadosRetorno) SenderDetran.send(ServicosDetranEnum.INCLUIR_DEFESA_AUTUACAO.getValue(), defesaPreviaDadosEntrada, this.arquivoConfiguracao, RecursoDefesaDadosRetorno.class);
			servicoDetranObjeto.setDadosRetorno(defesaPreviaDadosRetorno);
			detranRegistro.registrar(servicoDetranObjeto, this.arquivoConfiguracao.getLgHomologacao());
			return servicoDetranObjeto;
		}
		catch(Exception e){
			e.printStackTrace();
			return new ServicoDetranObjetoMG();
		}
	}
	
}
