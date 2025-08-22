package com.tivic.manager.wsdl.detran.mg.builders;

import com.tivic.manager.fta.CategoriaVeiculo;
import com.tivic.manager.fta.Cor;
import com.tivic.manager.fta.EspecieVeiculo;
import com.tivic.manager.fta.MarcaModelo;
import com.tivic.manager.fta.TipoVeiculo;
import com.tivic.manager.fta.categoriaveiculo.ICategoriaVeiculoService;
import com.tivic.manager.fta.cor.ICorService;
import com.tivic.manager.fta.especieveiculo.IEspecieVeiculoService;
import com.tivic.manager.fta.marcamodelo.IMarcaModeloService;
import com.tivic.manager.fta.tipoveiculo.ITipoVeiculoService;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.cidade.ICidadeService;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.wsdl.detran.mg.consultaautobasenacional.ConsultaAutoBaseNacionalDadosRetorno;
import com.tivic.manager.wsdl.interfaces.DadosRetorno;
import com.tivic.sol.cdi.BeansFactory;

public class AitBuilderSearchBaseNacional extends AitBuilderSearchProdemge implements IAitBuilderSearchProdemge {

	IMarcaModeloService marcaModeloService;
	ICorService corService;
	ITipoVeiculoService tipoVeiculoService;
	ICategoriaVeiculoService categoriaVeiculoService;
	IEspecieVeiculoService especieVeiculoService;
	ICidadeService cidadeService;
	
	public AitBuilderSearchBaseNacional() throws Exception{
		this.marcaModeloService = (IMarcaModeloService) BeansFactory.get(IMarcaModeloService.class);
		this.corService = (ICorService) BeansFactory.get(ICorService.class);
		this.tipoVeiculoService = (ITipoVeiculoService) BeansFactory.get(ITipoVeiculoService.class);
		this.categoriaVeiculoService = (ICategoriaVeiculoService) BeansFactory.get(ICategoriaVeiculoService.class);
		this.especieVeiculoService = (IEspecieVeiculoService) BeansFactory.get(IEspecieVeiculoService.class);
		this.cidadeService = (ICidadeService) BeansFactory.get(ICidadeService.class);
	}
	
	public void build(Ait ait, DadosRetorno dadosRetorno) {
		ConsultaAutoBaseNacionalDadosRetorno retorno = (ConsultaAutoBaseNacionalDadosRetorno) dadosRetorno;
		
		if(validUpdate(ait.getNrCpfCnpjProprietario(), retorno.getCpfCnpjPossuidor()))
			ait.setNrCpfCnpjProprietario(retorno.getCpfCnpjPossuidor());
		
		if (validString(retorno.getMarcaModelo()))
			setDadosMarcaModelo(ait, retorno);

		if (validString(retorno.getCor()))
			setDadosCor(ait, retorno);
		
		if (validString(retorno.getTipoVeiculo()))
			setDadosTipoVeiculo(ait, retorno);
				
		if (validString(retorno.getEspecie()))
			setDadosEspecie(ait, retorno);

		if (validString(retorno.getCategoria()))
			setDadosCategoria(ait, retorno);
	}
	
	private void setDadosMarcaModelo(Ait ait, ConsultaAutoBaseNacionalDadosRetorno retorno) {
		try {
			String[] strMarca = retorno.getMarcaModelo().split("-");
			MarcaModelo marca = this.marcaModeloService.getByNrMarca(strMarca[0].trim());
			ait.setCdMarcaVeiculo(marca.getCdMarca());
		} catch(Exception e) {}
	}
	
	private void setDadosCor(Ait ait, ConsultaAutoBaseNacionalDadosRetorno retorno) {
		try {
			String[] strCor = retorno.getCor().split("-");
			Cor cor = this.corService.getByNome(strCor[strCor.length-1].trim());
			ait.setCdCorVeiculo(cor.getCdCor());
		} catch(Exception e) {}
	}
	
	private void setDadosTipoVeiculo(Ait ait, ConsultaAutoBaseNacionalDadosRetorno retorno) {
		try {
			String[] strTipo = retorno.getTipoVeiculo().split("-");
			TipoVeiculo tipoVeiculo = this.tipoVeiculoService.getByNmTipoVeiculo(strTipo[strTipo.length-1].trim());
			ait.setCdTipoVeiculo(tipoVeiculo.getCdTipoVeiculo());
		} catch(Exception e) {}
	}
	
	private void setDadosEspecie(Ait ait, ConsultaAutoBaseNacionalDadosRetorno retorno) {
		try {
			String[] strEspecie = retorno.getEspecie().split("-");
			EspecieVeiculo especie = this.especieVeiculoService.getByNome(strEspecie[strEspecie.length-1].trim());
			ait.setCdEspecieVeiculo(especie.getCdEspecie());
		} catch(Exception e) {}
	}
	
	private void setDadosCategoria(Ait ait, ConsultaAutoBaseNacionalDadosRetorno retorno) {
		try {
			String[] strCategoria = retorno.getCategoria().split("-");
			CategoriaVeiculo categoriaVeiculo = this.categoriaVeiculoService.getByNome(strCategoria[strCategoria.length-1].trim());
			ait.setCdCategoriaVeiculo(categoriaVeiculo.getCdCategoria());
		} catch(Exception e) {}
	}
}
