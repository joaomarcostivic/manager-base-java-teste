package com.tivic.manager.tasks.limpeza.lotes;

import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.sol.util.date.DateUtil;
import com.tivic.sol.util.date.conversors.SqlFormat;

public class LimpezaLotesDTOBuilder {

	private LimpezaLotesDTO limpezaLotesDTO;
	
	public LimpezaLotesDTOBuilder() {
		this.limpezaLotesDTO = new LimpezaLotesDTO();
	}
	
	public LimpezaLotesDTOBuilder codigosLote(List<Integer> codigosLote) {
		this.limpezaLotesDTO.setCodigosLote(codigosLote);
		return this;
	}

	public LimpezaLotesDTOBuilder dataCriacao(String dataCriacao) throws Exception {
		if(dataCriacao != null) {
			GregorianCalendar dtCriacao = DateUtil.convStringToCalendar(dataCriacao, new SqlFormat());
			if(dtCriacao == null)
				throw new Exception("Data de criação não está no formato correto (yyyy-MM-dd HH:mm:ss)");
			this.limpezaLotesDTO.setDtCriacao(dtCriacao);
		}
		return this;
	}

	public LimpezaLotesDTOBuilder limite(int limite) {
		this.limpezaLotesDTO.setLimite(limite);
		return this;
	}
	
	public LimpezaLotesDTO build() throws Exception{
		if(this.limpezaLotesDTO.getCodigosLote().isEmpty() && this.limpezaLotesDTO.getDtCriacao() == null) {
			throw new Exception("Necessário passar ou algum código de lote ou a data de criação");
		}
		return this.limpezaLotesDTO;
	}
}
