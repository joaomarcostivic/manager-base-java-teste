package com.tivic.manager.mob.lote.impressao.remessacorreios.builders;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem.DadosRetornoCorreioDto;
import com.tivic.manager.util.Util;

public class DadosRetornoCorreioAr2DBuilder implements IDadosRetornoBuilder {
	
	List<DadosRetornoCorreioDto> dadosRetornoCorreioDtoList;
	
	public DadosRetornoCorreioAr2DBuilder() {
		this.dadosRetornoCorreioDtoList = new ArrayList<DadosRetornoCorreioDto>();
	}

	@Override
	public void obterDados(BufferedReader reader) throws IOException {
		List<String> lines = reader.lines().collect(Collectors.toList());
		for (int i = 0; i < lines.size()-1; i++) {
			if (i > 0) {
				String line = lines.get(i);
				DadosRetornoCorreioDto dadosRetornoCorreioDto = new DadosRetornoCorreioDto();
				dadosRetornoCorreioDto.setSgServico(line.substring(13, 15));
				dadosRetornoCorreioDto.setNrEtiqueta(line.substring(15, 23));
				dadosRetornoCorreioDto.setDigitoVerificado(Integer.valueOf(line.substring(23, 24)));
				dadosRetornoCorreioDto.setSgPaisOrigem(line.substring(24, 26));
				dadosRetornoCorreioDto.setDataEntrega(Util.convStringSemFormatacaoReverseSToGregorianCalendar(line.substring(42, 50)));
				dadosRetornoCorreioDto.setStEntrega(Integer.valueOf(line.substring(112, 114)));
				this.dadosRetornoCorreioDtoList.add(dadosRetornoCorreioDto);
			}
		}
	}

	@Override
	public List<DadosRetornoCorreioDto> build() {
		return this.dadosRetornoCorreioDtoList;
	}

}
