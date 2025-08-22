package com.tivic.manager.wsdl.detran.mg.alterardatalimiterecurso;

import com.tivic.manager.wsdl.AitDetranObject;
import com.tivic.manager.wsdl.detran.mg.SenderDetran;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoMG;
import com.tivic.manager.wsdl.detran.mg.ServicoDetranObjetoValidacaoBuilder;
import com.tivic.manager.wsdl.detran.mg.ServicosDetranEnum;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.ServicoDetran;
import com.tivic.manager.wsdl.interfaces.ServicoDetranObjeto;
import com.tivic.sol.cdi.BeansFactory;

public class AlterarDataLimiteRecurso extends ServicoDetran {
	
	private IAlterarDataLimiteRecursoDadosEntradaFactory alterarDataLimiteRecursoDadosEntradaFactory;
	
	public AlterarDataLimiteRecurso() throws Exception {
		alterarDataLimiteRecursoDadosEntradaFactory = (IAlterarDataLimiteRecursoDadosEntradaFactory) BeansFactory
				.get(IAlterarDataLimiteRecursoDadosEntradaFactory.class);
	}
	
	@Override
	public ServicoDetranObjeto executar(AitDetranObject aitDetranObject) {
		try {
			ServicoDetranObjeto servicoDetranObjeto = new ServicoDetranObjetoMG();
			servicoDetranObjeto.setAit(aitDetranObject.getAit());
			AlterarDataLimiteRecursoDadosEntrada alterarDataRecursoDadosEntrada = alterarDataLimiteRecursoDadosEntradaFactory
					.fazerDadoEntrada(aitDetranObject.getAit());
			servicoDetranObjeto.setDadosEntrada(alterarDataRecursoDadosEntrada);
			AlterarDataLimiteRecursoDadosRetorno alterarDataRecursoDadosRetorno = (AlterarDataLimiteRecursoDadosRetorno) SenderDetran
					.send(ServicosDetranEnum.ALTERAR_DATA_RECURSO.getValue(), alterarDataRecursoDadosEntrada, this.arquivoConfiguracao, AlterarDataLimiteRecursoDadosRetorno.class);
			servicoDetranObjeto.setDadosRetorno(alterarDataRecursoDadosRetorno);
			return servicoDetranObjeto;
		}
		catch(ValidacaoException ve) {
			ve.printStackTrace(System.out);
			return new ServicoDetranObjetoValidacaoBuilder(aitDetranObject, ve.getMessage()).build();
		}
		catch(Exception e){
			e.printStackTrace();
			return new ServicoDetranObjetoValidacaoBuilder(aitDetranObject, "Erro de sistema ao enviar").build();
		}
	}

}
