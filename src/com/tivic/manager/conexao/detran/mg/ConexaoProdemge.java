package com.tivic.manager.conexao.detran.mg;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.PessoaJuridica;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.orgao.IOrgaoService;
import com.tivic.manager.mob.pessoa.juridica.PessoaJuridicaRepository;
import com.tivic.sol.cdi.BeansFactory;

public class ConexaoProdemge {
	private IParametroRepository parametroRepository;
	private IOrgaoService orgaoService;
	private PessoaJuridicaRepository pessoaJuridicaRepository;
	
	public ConexaoProdemge() throws Exception {
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
		this.orgaoService = (IOrgaoService) BeansFactory.get(IOrgaoService.class);
		this.pessoaJuridicaRepository = (PessoaJuridicaRepository) BeansFactory.get(PessoaJuridicaRepository.class);
	}

	public HttpURLConnection conectar(String servico) {
		try {
			String URL_BASE = this.parametroRepository.getValorOfParametroAsString("URL_SICRONIZACAO_MARCA_MODELO_PRODEMGE");
			String request = URL_BASE + servico;
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
	
	private String getAuthData() throws Exception{
		String credentials = "";
		if (checkProducao()) {
			String login = getCnpjOrgao();
			String senha = this.parametroRepository.getValorOfParametroAsString("MOB_CD_ORGAO_SERVICO_CHAVE");
			credentials = login + ":" + senha;
		}
		return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
	}
	
	private boolean checkProducao() {
		return ManagerConf.getInstance().getAsBoolean("PRODEMGE_PRODUCAO", false);
	}
	
	private String getCnpjOrgao() throws Exception {
		Orgao orgao = this.orgaoService.getOrgaoUnico();
		if (orgao.getCdPessoaOrgao() <= 0) {
			throw new Exception("O código da pessoa do órgão é inválido ou não foi configurado.");
		}
		PessoaJuridica pessoaJuridica = this.pessoaJuridicaRepository.get(orgao.getCdPessoaOrgao());
		return pessoaJuridica.getNrCnpj();
	}
}