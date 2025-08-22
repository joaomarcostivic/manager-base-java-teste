package com.tivic.manager.tasks.sincronizacao.tabelas.builders.caracteristicas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tivic.manager.mob.Infracao;
import com.tivic.sol.util.date.DateUtil;
import com.tivic.sol.util.date.conversors.IsoFormat;

public class InfracaoListBuild implements ISincronizadorJsonBuilder<Infracao>{

private List<Infracao> infracoes;
	
	public InfracaoListBuild() {
		infracoes = new ArrayList<Infracao>();
	}
	
	@Override
	public InfracaoListBuild process(JSONArray data) throws Exception{
		for(int i = 0; i < data.length(); i++) {
			JSONObject infracaoJson = data.getJSONObject(i);
			Infracao infracao = new Infracao();
			infracao.setCdInfracao(infracaoJson.getInt("cdInfracao"));
			infracao.setDsInfracao(infracaoJson.getString("dsInfracao"));
			infracao.setNrPontuacao(infracaoJson.getInt("nrPontuacao"));
			infracao.setNrCodDetran(infracaoJson.getInt("nrCodDetran"));
			
			if (infracaoJson.has("vlUfir") && !infracaoJson.isNull("vlUfir")) 
				infracao.setVlUfir(infracaoJson.getDouble("vlUfir"));
			else 
				infracao.setVlUfir(0.0);
			
			infracao.setNmNatureza(infracaoJson.getString("nmNatureza"));
			infracao.setNrArtigo(infracaoJson.getString("nrArtigo"));
			
			if (infracaoJson.has("nrParagrafo") && !infracaoJson.isNull("nrParagrafo")) 
				infracao.setNrParagrafo(infracaoJson.getString("nrParagrafo"));
			
			if (infracaoJson.has("nrInciso") && !infracaoJson.isNull("nrInciso")) 
				infracao.setNrInciso(infracaoJson.getString("nrInciso"));
			
			if (infracaoJson.has("nrAlinea") && !infracaoJson.isNull("nrAlinea")) 
				infracao.setNrAlinea(infracaoJson.getString("nrAlinea"));
			
			infracao.setTpCompetencia(infracaoJson.getInt("tpCompetencia"));
			infracao.setLgPrioritaria(infracaoJson.getInt("lgPrioritaria"));
			
			if (infracaoJson.has("dtFimVigencia") && !infracaoJson.isNull("dtFimVigencia")) {
				;
		        infracao.setDtFimVigencia(DateUtil.convStringToCalendar(infracaoJson.getString("dtFimVigencia"), new IsoFormat()));
			}
				
			infracao.setVlInfracao(infracaoJson.getDouble("vlInfracao"));
			infracao.setTpResponsabilidade(infracaoJson.getInt("tpResponsabilidade"));
			
			if (infracaoJson.has("lgSuspensaoCnh") && !infracaoJson.isNull("lgSuspensaoCnh"))
		        infracao.setLgSuspensaoCnh(infracaoJson.getInt("lgSuspensaoCnh"));
			
			if (infracaoJson.has("dtInicioVigencia") && !infracaoJson.isNull("dtInicioVigencia")) {
		        infracao.setDtFimVigencia(DateUtil.convStringToCalendar(infracaoJson.getString("dtInicioVigencia"), new IsoFormat()));
			}
	
			infracoes.add(infracao);
		}
		return this;
	}
	
	@Override
	public List<Infracao> build() {
		return infracoes;
	}

}
