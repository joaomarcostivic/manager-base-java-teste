package com.tivic.manager.mob.lote.impressao.remessacorreios.builders;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.tivic.manager.mob.lote.impressao.remessacorreios.arquivospostagem.DadosRetornoCorreioDto;
import com.tivic.manager.util.Util;

public class DadosRetornoCorreioArBuilder implements IDadosRetornoBuilder{
	
	private List<DadosRetornoCorreioDto> dadosRetornoCorreioDtoList;
	
	public DadosRetornoCorreioArBuilder() {
		this.dadosRetornoCorreioDtoList = new ArrayList<DadosRetornoCorreioDto>();
	}

	@Override
	public void obterDados(BufferedReader reader) throws IOException {
		List<String> lines = reader.lines().collect(Collectors.toList());
		for (int i = 0; i < lines.size()-1; i++) {
			if (i > 0) {
				String line = lines.get(i);
				DadosRetornoCorreioDto dadosRetornoCorreioDto = new DadosRetornoCorreioDto();
				dadosRetornoCorreioDto.setSgServico(line.substring(7, 9));
				dadosRetornoCorreioDto.setNrEtiqueta(line.substring(9, 17));
				dadosRetornoCorreioDto.setDigitoVerificado(Integer.valueOf(line.substring(17, 18)));
				dadosRetornoCorreioDto.setSgPaisOrigem(line.substring(18, 20));
				dadosRetornoCorreioDto.setDataEntrega(Util.convStringSemFormatacaoReverseSToGregorianCalendar(line.substring(36, 44)));
				dadosRetornoCorreioDto.setStEntrega(Integer.valueOf(line.substring(45, 46)));
				this.dadosRetornoCorreioDtoList.add(dadosRetornoCorreioDto);
			}
		}
	}

	@Override
	public List<DadosRetornoCorreioDto> build() {
		return this.dadosRetornoCorreioDtoList;
	}
	
}
