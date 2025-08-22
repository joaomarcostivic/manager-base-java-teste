package com.tivic.manager.mob.ait;

import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitServices;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.infracao.IInfracaoService;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.detran.mg.consultaautobaseestadual.ConsultaAutoBaseEstadualDadosRetorno;
import com.tivic.sol.cdi.BeansFactory;

public class AitConsultaBaseEstadualBuilder implements AitBuilder {

	private Ait ait;
	private IInfracaoService infracaoService;
	
	public AitConsultaBaseEstadualBuilder(ConsultaAutoBaseEstadualDadosRetorno retorno) throws Exception {
		this.infracaoService = (IInfracaoService) BeansFactory.get(IInfracaoService.class);
		
		ait.setIdAit(retorno.getAit());
		ait.setNrControle(retorno.getNumeroProcessamento().replaceAll("\\b0+(?!\\b)", ""));
		ait.setNrPlaca(retorno.getPlaca());
		ait.setDtInfracao(Util.stringToCalendar(retorno.getDataAit()));
		ait.setDtPrazoDefesa(Util.stringToCalendar(retorno.getDataLimiteDefesa()));
		ait.setNrRenainf(retorno.getCodigoRenainf());
		if (isRenavamValido(retorno.getCodigoRenavam()) && isNrRenavamPreenchido(retorno.getCodigoRenavam()))
			ait.setNrRenavan(retorno.getCodigoRenavam());
		ait.setSgUfVeiculo(retorno.getUfVeiculo());
		adicionarInfracao(retorno);
		adicionarVlMulta(retorno);
		ait.setDsLocalInfracao(retorno.getLocalAit());
		ait.setNmCondutor(retorno.getNomeCondutor());
		if (isCnhValida(retorno.getCnhCondutor()) && isCnhPreenchida(retorno.getCnhCondutor()))
			ait.setNrCnhCondutor(retorno.getCnhCondutor());
		ait.setDsObservacao(retorno.getObservacaoAit());
		ait.setTpPessoaProprietario(retorno.getTipoDocumentoCpfCnpj());
		ait.setNrCpfCnpjProprietario(retorno.getNumeroCpfCnpj());
		ait.setNmProprietario(retorno.getNome());
		ait.setDsLogradouro(retorno.getNomeLogradouro());
		ait.setDsNrImovel(retorno.getNumeroImovel());
		ait.setNrCep(retorno.getCepImovel());
		ait.setTpOrigem(AitServices.TP_IMPORTACAO_SYNC_AUTO);
	}
	
	private void adicionarInfracao(ConsultaAutoBaseEstadualDadosRetorno retorno) throws Exception {
		int cdInfracao = Integer.parseInt(retorno.getCodigoInfracao() + "" + retorno.getCodigoDesdobramento());
		Infracao infracao = this.infracaoService.getByCodDetran(cdInfracao);
		ait.setCdInfracao(infracao.getCdInfracao());
	}
	
	private void adicionarVlMulta(ConsultaAutoBaseEstadualDadosRetorno retorno) {
		StringBuilder valorInfracao = new StringBuilder(retorno.getValorInfracao());
		valorInfracao.insert(retorno.getValorInfracao().length() - 2, '.');
		ait.setVlMulta(Double.valueOf(valorInfracao.toString().replaceAll("\\b0+(?!\\b)", "")));
	}

	private static boolean isCnhValida(String nrCnh) {
		return !nrCnh.equals("00000000000");
	}
	
	private static boolean isCnhPreenchida(String nrCnh) {
		return !nrCnh.trim().equals("");
	}
	
	private static boolean isRenavamValido(String nrRenavan) {
		return !nrRenavan.equals("00000000000");
	}
	
	private static boolean isNrRenavamPreenchido(String nrRenavan) {
		return !nrRenavan.trim().equals("");
	}

	@Override
	public Ait build() {
		return this.ait;
	}

}
