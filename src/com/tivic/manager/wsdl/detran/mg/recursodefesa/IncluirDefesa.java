package com.tivic.manager.wsdl.detran.mg.recursodefesa;

import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;

public class IncluirDefesa extends ServicoDetran {
	private IncluirDefesaRegistro incluirDefesaRegistro;
	
	public IncluirDefesa() throws Exception {
		this.incluirDefesaRegistro = new IncluirDefesaRegistro();
	}
	
	@Override
	public ServicoDetranObjeto executar(AitDetranObject aitDetranObject) {
		try{
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG();
			servicoDetranObjeto.setAit(aitDetranObject.getAit());
			servicoDetranObjeto.setAitMovimento(aitDetranObject.getAitMovimento());
			RecursoDefesaDadosEntrada defesaPreviaDadosEntrada = IncluirDefesaDadosEntradaFactory.fazerDadoEntrada(aitDetranObject, this.arquivoConfiguracao.getLgHomologacao());
			servicoDetranObjeto.setDadosEntrada(defesaPreviaDadosEntrada);
			RecursoDefesaDadosRetorno defesaPreviaDadosRetorno = (RecursoDefesaDadosRetorno) SenderDetran.send(ServicosDetranEnum.INCLUIR_DEFESA_AUTUACAO.getValue(), defesaPreviaDadosEntrada, this.arquivoConfiguracao, RecursoDefesaDadosRetorno.class);
			servicoDetranObjeto.setDadosRetorno(defesaPreviaDadosRetorno);
			this.incluirDefesaRegistro.registrar(servicoDetranObjeto, this.arquivoConfiguracao.getLgHomologacao());
			return servicoDetranObjeto;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return new ServicoDetranObjetoMG();
		}
	}
	
}
