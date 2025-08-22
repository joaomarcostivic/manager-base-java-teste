package com.tivic.manager.mob.lote.impressao.remessacorreios.builders;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import com.tivic.manager.mob.lote.impressao.TipoArDigitalEnum;
import com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem.DadosRetornoCorreioDto;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;

public class RetornoCorreios implements IProcessaDadosRetorno {

	@Override
	public List<DadosRetornoCorreioDto> obterDados(int tpRemessa, BufferedReader reader) throws ValidacaoException, IOException {
		DirectorRetorno directorRetorno = new DirectorRetorno();
		IDadosRetornoBuilder dadosRetornoBuilder = getBuilderDadosRetorno(tpRemessa);
		directorRetorno.construtuctorDadosRetorno(dadosRetornoBuilder, reader);
		List<DadosRetornoCorreioDto> dadosRetornoCorreioDtoList = dadosRetornoBuilder.build();
		return dadosRetornoCorreioDtoList;
	}

	private IDadosRetornoBuilder getBuilderDadosRetorno(int tpRemessa) throws ValidacaoException {
		if (tpRemessa == TipoArDigitalEnum.AR_DIGITAL.getKey()) {
			return new DadosRetornoCorreioArBuilder();
		}
		else if (tpRemessa == TipoArDigitalEnum.AR_DIGITAL_2D.getKey()) {
			return new DadosRetornoCorreioAr2DBuilder();
		} else {
			throw new ValidacaoException("Tipo de retorno não encontrado");
		}
	}

}
