package com.tivic.manager.mob;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.GregorianCalendar;

import com.tivic.sol.connection.Conexao;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.util.Util;

import sol.util.Result;

public class InfracaoSyncMg implements InfracaoSync {
	
	public static final int CD_INFRACAO				= 0;
	public static final int CD_DESDOBRAMENTO		= 1;
	public static final int DESCRICAO_INFRACAO		= 2;
	public static final int VALOR_INFRACAO			= 3;
	public static final int AMPARO_LEGAL			= 4;
	public static final int RESP_INFRACAO			= 5;
	public static final int PONTOS_INFRACAO			= 6;
	public static final int DT_INICIO_VIGENCIA		= 7;
	public static final int COMP_INFRACAO			= 8;
	
	ArrayList<Integer> infracoes = new ArrayList<Integer>(); 
	
	public Result sync() {
		Connection connect = Conexao.conectar();
		try {
			HttpURLConnection conn = getConn();			
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String lineInfracao = in.readLine();

			while(lineInfracao != null) {
				lineInfracao = in.readLine();
				
				if(lineInfracao == null)
					break;
				
				String[] dadosInfracao = lineInfracao.split(";");
				
				Infracao infracao = mountInfracao(dadosInfracao, connect);
				infracoes.add(infracao.getCdInfracao());
				
				infracaoExists(infracao, dadosInfracao[DT_INICIO_VIGENCIA], connect);
				
				InfracaoServices.save(infracao, connect);
			}
			updateAllNaoVigentes(connect);
			conn.disconnect();
			return new Result(1, "Infrações importadas com sucesso");
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return new Result(-1, "Erro no processo de importação das infrações");
		} finally {
			Conexao.desconectar(connect);
		}
	}
	
	private HttpURLConnection getConn() {
		try {
			String request = "http://gestaodeinfracoes.prodemge.gov.br/sram/servicos-arquivos/baixar-infracoes";
			URL url = new URL(request);
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setInstanceFollowRedirects(false);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", getAuthData());
			conn.setUseCaches(false);
			
			return conn;
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
			return null;
		}
	}
	
	private String getAuthData() {
		String credentials = "";
		
		if(checkProducao()) {
			OrgaoServico _servico = getOrgaoServico();
			credentials = (_servico.getNmLogin() + ":" + _servico.getNmSenha());
		} else {
			credentials = "18404780000109:Q2TZq1Cn81ecRZlfWb1cud2WJ76qQ84C";
		}
		
		return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
	}
	
	private boolean checkProducao() {
		return ManagerConf.getInstance().getAsBoolean("PRODEMGE_PRODUCAO", false);
	}
	
	private OrgaoServico getOrgaoServico() {
		OrgaoServicoServices _service = new OrgaoServicoServices();
		OrgaoServico _servico = _service.getByNmServico("PRODEMGE");
		return _servico;
	}
	
	private Infracao mountInfracao(String[] dadosInfracao, Connection connect) {
		Infracao infracao = new Infracao();
		infracao.setNrCodDetran(getCodDetran(dadosInfracao[CD_INFRACAO], dadosInfracao[CD_DESDOBRAMENTO]));
		infracao.setCdInfracao(InfracaoServices.getCodInfracao(infracao.getNrCodDetran(), connect));
		infracao.setDsInfracao(dadosInfracao[DESCRICAO_INFRACAO]);
		infracao.setVlInfracao(Double.valueOf(dadosInfracao[VALOR_INFRACAO]));
		infracao.setTpResponsabilidade(getTpResponsabilidadeInfracao(dadosInfracao[RESP_INFRACAO]));
		infracao.setNrPontuacao(Integer.valueOf(dadosInfracao[PONTOS_INFRACAO]));
		infracao.setTpCompetencia(getTpCompetenciaInfracao(dadosInfracao[COMP_INFRACAO]));
		infracao.setDtFimVigencia(null);
		setAmparoLegal(infracao, dadosInfracao[AMPARO_LEGAL]);
		
		return infracao;
	}
	
	private int getCodDetran(String cdInfracao, String cdDesdobramento) {
		return Integer.valueOf(cdInfracao + cdDesdobramento);
	}
	
	private int getTpResponsabilidadeInfracao(String dsResponsabilidade) {
		if(dsResponsabilidade.trim().equalsIgnoreCase("PRO"))
			return InfracaoServices.MULTA_RESPONSABILIDADE_PROPRIETARIO;
		
		else if (dsResponsabilidade.trim().equalsIgnoreCase("CON"))
			return InfracaoServices.MULTA_RESPONSABILIDADE_CONDUTOR;
		
		else
			return -1;
	}
	
	private int getTpCompetenciaInfracao(String dsCompetencia) {
		if(dsCompetencia.contains("MUNICIPAL"))
			return InfracaoServices.MULTA_COMPETENCIA_MUNICIPAL;
		
		else if(dsCompetencia.contains("ESTADUAL"))
			return InfracaoServices.MULTA_COMPETENCIA_ESTADUAL;
		
		else
			return -1;
	}
	
	private void updateAllNaoVigentes(Connection connect) {
		try {
			String cdInfracoes = buildStringInfracoes();
			String query = "UPDATE MOB_INFRACAO SET DT_FIM_VIGENCIA = ? WHERE CD_INFRACAO NOT IN (" + cdInfracoes + ")";
			
			System.out.println(query);
			
			PreparedStatement pstmt = connect.prepareStatement(query);
			pstmt.setTimestamp(1, new Timestamp(new GregorianCalendar().getTimeInMillis()));
			
			int updates = pstmt.executeUpdate();
			System.out.println(updates);
			
		} catch(Exception ex) {
			ex.printStackTrace(System.out);
		}
	}
	
	private String buildStringInfracoes() {
		String codInfracoes = "";
		
		for(int cdInfracao: infracoes) {
			codInfracoes += String.valueOf(cdInfracao) + ", ";
		}
		System.out.println(infracoes.size());
		return codInfracoes.substring(0, codInfracoes.length() - 2);
	}
	
	private void setAmparoLegal(Infracao infracao, String amparoLegal) {
		
		if(amparoLegal.contains("ART") || amparoLegal.contains("pr") || amparoLegal.contains("-")) {
			if(amparoLegal.contains("ART.")) {
				String[] itensAmparo = amparoLegal.split("[.]");
				String dadosAmparo = itensAmparo[1].trim();
			
				if(dadosAmparo.length() == 1){
					if(dadosAmparo.contains(",")) {
						itensAmparo = dadosAmparo.split(",");
						infracao.setNrArtigo(itensAmparo[0]);
						
						if(itensAmparo.length > 1)
							infracao.setNrInciso(itensAmparo[1]);
						
						if(itensAmparo.length > 2)
							infracao.setNrAlinea(itensAmparo[2]);
					}
				}
			} else if(amparoLegal.contains("ART ")) {
				String[] itensAmparo = amparoLegal.split(" ");
				String dadosAmparo = itensAmparo[1].trim();
				if(dadosAmparo.length() == 1){
					if(dadosAmparo.contains(",")) {
						itensAmparo = dadosAmparo.split(",");
						infracao.setNrArtigo(itensAmparo[0]);
						
						if(itensAmparo.length > 1)
							infracao.setNrInciso(itensAmparo[1]);
						
						if(itensAmparo.length > 2)
							infracao.setNrAlinea(itensAmparo[2]);
					}
				}
			} else if(amparoLegal.contains("pr")) {
				if(amparoLegal.contains("*")) {
					String[] itensAmparo = amparoLegal.split(" ");
					infracao.setNrArtigo(itensAmparo[0]);
					
					if(!itensAmparo[2].contains("*"))
						infracao.setNrInciso(itensAmparo[2]);
				}
			} else if(amparoLegal.contains("-")) {
				if(amparoLegal.contains("*")) {
					String[] itensAmparo = amparoLegal.split(" ");
					infracao.setNrArtigo(itensAmparo[0]);
					
					if(itensAmparo.length > 3)
						infracao.setNrAlinea(itensAmparo[3].split("[-]")[1]);
				}
				String[] itensAmparo = amparoLegal.split("[-]");
				infracao.setNrArtigo(itensAmparo[0]);
				
				if(itensAmparo.length > 1)
					infracao.setNrAlinea(itensAmparo[1]);
				
			} else {
				System.out.println("Formatação não coberta: " + amparoLegal);
			}
		} else {
			String[] itensAmparo = amparoLegal.split("[*]");
			infracao.setNrArtigo(itensAmparo[0]);
			
			if(itensAmparo.length > 1)
				infracao.setNrInciso(itensAmparo[1]);
			
			if(itensAmparo.length > 2)
				infracao.setNrAlinea(itensAmparo[2]);
		}
	}
	
	private Infracao infracaoExists(Infracao infracao, String dtInicioVigencia, Connection connect) {
		if(infracao.getCdInfracao() == 0) {
			return infracao;
		}
		
		Infracao infracaoValidacao = InfracaoServices.getVigente(infracao.getCdInfracao(), connect);
		boolean updateInfracao = true;
		
		if(infracaoValidacao == null) {
			return infracao;
		}
		
		if(infracao.getDsInfracao() != null && !infracao.getDsInfracao().equalsIgnoreCase(infracaoValidacao.getDsInfracao()))
			updateInfracao = false;
			
		if(infracao.getNrPontuacao() != infracaoValidacao.getNrPontuacao())
			updateInfracao = false;
		
		if(infracao.getNrCodDetran() != infracaoValidacao.getNrCodDetran())
			updateInfracao = false;
		
		if(infracao.getVlInfracao().doubleValue() != infracaoValidacao.getVlInfracao().doubleValue())
			updateInfracao = false;
		
		if(infracao.getNmNatureza() != null && !infracao.getNmNatureza().equalsIgnoreCase(infracaoValidacao.getNmNatureza()))
			updateInfracao = false;
		
		if(infracao.getNrArtigo() != null && !infracao.getNrArtigo().equalsIgnoreCase(infracaoValidacao.getNrArtigo()))
			updateInfracao = false;

		if(infracao.getNrParagrafo() != null && !infracao.getNrParagrafo().equalsIgnoreCase(infracaoValidacao.getNrParagrafo()))
			updateInfracao = false;
		
		if(infracao.getNrInciso() != null && !infracao.getNrInciso().equalsIgnoreCase(infracaoValidacao.getNrInciso()))
			updateInfracao = false;
		
		if(infracao.getNrAlinea() != null && !infracao.getNrAlinea().equalsIgnoreCase(infracaoValidacao.getNrAlinea()))
			updateInfracao = false;
		
		if(infracao.getTpCompetencia() != infracaoValidacao.getTpCompetencia())
			updateInfracao = false;
		
		if(infracao.getLgPrioritaria() != infracaoValidacao.getLgPrioritaria())
			updateInfracao = false;
		
		if(infracao.getTpResponsabilidade() != infracaoValidacao.getTpResponsabilidade())
			updateInfracao = false;
		
		if(!updateInfracao) {
			infracaoValidacao.setDtFimVigencia(Util.convStringToGregorianTimestampFormat(dtInicioVigencia));
			InfracaoServices.save(infracaoValidacao);
			infracoes.add(infracaoValidacao.getCdInfracao());
			infracao.setCdInfracao(0);
			return infracao;
		}		
		
		return infracao;
	}

}
