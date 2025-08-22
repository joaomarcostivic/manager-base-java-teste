package com.tivic.manager.mob;

import java.sql.Connection;
import java.util.HashMap;

import com.tivic.manager.adapter.base.antiga.parametro.ParametroOldRepositoryDAO;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.CidadeDAO;
import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.grl.ParametroServices;
import com.tivic.manager.util.richtext.ConversorRichText;
import com.tivic.sol.connection.Conexao;

public class AitReportGetParamns {

	public HashMap<String, Object> getParamnsNAI() {
		return getParamnsNAI(null);
	}
	
	public HashMap<String, Object> getParamnsNAI(Connection connect) 
	{
		
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		boolean isConnectionNull = connect==null;
		AitReportServices reportServices = new AitReportServices();
		
		try {
			
			if (isConnectionNull)
				connect = Conexao.conectar();
		
			HashMap<String, Object> paramns = new HashMap<>();
			
			if (lgBaseAntiga){
				paramns.put("IS_BASE_OLD", true);
				paramns.put("DS_TITULO_1", ParametroServices.getValorOfParametro("NM_ORGAO_AUTUADOR"));
				paramns.put("DS_TITULO_2", ParametroServices.getValorOfParametro("NM_SUBORGAO"));
				paramns.put("DS_TITULO_3", ParametroServices.getValorOfParametro("NM_DEPARTAMENTO"));
				paramns.put("NR_TELEFONE", ParametroServices.getValorOfParametro("NR_TELEFONE"));
				paramns.put("NM_EMAIL", ParametroServices.getValorOfParametro("NM_EMAIL"));
				paramns.put("LOGO_1", reportServices.recoverLogosBaseOld(connect)[0]);
				paramns.put("LOGO_2", reportServices.recoverLogosBaseOld(connect)[1]);
				paramns.put("NM_CIDADE", ParametroServices.getValorOfParametro("nm_municipio"));			
				paramns.put("MOB_CD_ORGAO_AUTUADOR", ParametroServices.getValorOfParametro("cd_orgao_autuante"));
				paramns.put("MOB_CD_MUNICIPIO_ORGAO_AUTUADOR", ParametroServices.getValorOfParametro("cd_municipio_autuador"));
				paramns.put("MOB_IMPRESSOS_DS_TEXTO_NAI", ParametroServices.getValorOfParametro("txt_nai"));			
				paramns.put("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", ParametroServices.getValorOfParametro("MOB_BLB_MENSAGEM_EDUCATIVA_NAI"));
				paramns.put("NM_LOGRADOURO", ParametroServices.getValorOfParametro("NM_LOGRADOURO"));
				paramns.put("NR_ENDERECO", ParametroServices.getValorOfParametro("NR_ENDERECO"));
				paramns.put("NM_BAIRRO", ParametroServices.getValorOfParametro("NM_BAIRRO"));
				paramns.put("NR_CEP", ParametroServices.getValorOfParametro("NR_CEP"));
				paramns.put("MOB_CORREIOS_NR_CONTRATO", ParametroServices.getValorOfParametro("nr_correios"));
				paramns.put("SG_ORGAO", ParametroServices.getValorOfParametro("SG_ORGAO"));
				paramns.put("SG_DEPARTAMENTO", ParametroServices.getValorOfParametro("SG_DEPARTAMENTO"));		
				paramns.put("MOB_PRAZOS_NR_DEFESA_PREVIA", ParametroServices.getValorOfParametro("nr_dias_defesa_previa"));
				paramns.put("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", ParametroServices.getValorOfParametro("ds_mensagem_indeferido"));			
				paramns.put("DS_ENDERECO", ParametroServices.getValorOfParametro("nr_endereco"));
				paramns.put("NM_SUBORGAO", ParametroServices.getValorOfParametro("NM_SUBORGAO"));
				paramns.put("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS"));
				paramns.put("mob_local_impressao", ParametroServices.getValorOfParametro("mob_local_impressao"));
				paramns.put("mob_impressao_tp_modelo_nai", ParametroServices.getValorOfParametro("tp_nai"));
				paramns.put("mob_impressao_tp_modelo_nip", ParametroServices.getValorOfParametro("tp_nip"));
				paramns.put("MOB_APRESENTACAO_CONDUTOR", ParametroServices.getValorOfParametro("DS_TEXTO_FICI"));
				paramns.put("NM_COMPLEMENTO", ParametroServices.getValorOfParametro("NM_COMPLEMENTO"));	
				paramns.put("mob_apresentar_observacao", ParametroServices.getValorOfParametro("mob_apresentar_observacao"));
				paramns.put("SG_UF", getEstadoOrgaoAutuador());
				paramns.put("MOB_INFORMACOES_ADICIONAIS_NAI", ParametroServices.getValorOfParametro("MOB_INFORMACOES_ADICIONAIS_NAI"));
				paramns.put("NM_SISTEMA_PORTAL", new ParametroOldRepositoryDAO().getValorOfParametroAsString("NM_SISTEMA_PORTAL"));
				paramns.put("MOB_PERIODO_DIAS_EMISSAO_AIT", ParametroServices.getValorOfParametro("MOB_PERIODO_DIAS_EMISSAO_AIT"));
				return paramns;
			}
			else{	
				paramns.put("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1", connect));
				paramns.put("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2", connect));
				paramns.put("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3", connect));
				paramns.put("NR_TELEFONE", ParametroServices.getValorOfParametro("NR_TELEFONE", "+55 (99) 9999-9999", connect));
				paramns.put("NM_EMAIL", ParametroServices.getValorOfParametro("NM_EMAIL", "email@dominio.com.br", connect));
				paramns.put("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA", connect));
				paramns.put("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO", connect));
				paramns.put("NM_CIDADE", ParametroServices.getValorOfParametro("NM_CIDADE", "LOREM IPSUM DOLOR SIT AMET", connect));			
				paramns.put("MOB_CD_ORGAO_AUTUADOR", ParametroServices.getValorOfParametro("MOB_CD_ORGAO_AUTUADOR", connect));
				paramns.put("MOB_IMPRESSOS_DS_TEXTO_NAI", ParametroServices.getValorOfParametro("MOB_IMPRESSOS_DS_TEXTO_NAI", connect));
				paramns.put("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", ParametroServices.getValorOfParametro("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", connect));	
				paramns.put("NM_LOGRADOURO", ParametroServices.getValorOfParametro("NM_LOGRADOURO", connect));
				paramns.put("NR_ENDERECO", ParametroServices.getValorOfParametro("NR_ENDERECO", connect));
				paramns.put("NM_BAIRRO", ParametroServices.getValorOfParametro("NM_BAIRRO", connect));
				paramns.put("NR_CEP", ParametroServices.getValorOfParametro("NR_CEP", connect));
				paramns.put("MOB_CORREIOS_NR_CONTRATO", ParametroServices.getValorOfParametro("MOB_CORREIOS_NR_CONTRATO", connect));
				paramns.put("SG_ORGAO", ParametroServices.getValorOfParametro("SG_ORGAO", connect));
				paramns.put("SG_DEPARTAMENTO", ParametroServices.getValorOfParametro("SG_DEPARTAMENTO", connect));		
				paramns.put("MOB_PRAZOS_NR_DEFESA_PREVIA", ParametroServices.getValorOfParametro("MOB_PRAZOS_NR_DEFESA_PREVIA", connect));
				paramns.put("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", ParametroServices.getValorOfParametro("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", connect));			
				paramns.put("DS_ENDERECO", ParametroServices.getValorOfParametro("DS_ENDERECO", connect));
				paramns.put("NM_SUBORGAO", ParametroServices.getValorOfParametro("NM_SUBORGAO", "LOREM IPSUM DOLOR SIT AMET", connect));
				paramns.put("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", connect));
				paramns.put("mob_local_impressao", ParametroServices.getValorOfParametro("mob_local_impressao", 0, connect));	
				paramns.put("mob_impressao_tp_modelo_nai", ParametroServices.getValorOfParametro("mob_impressao_tp_modelo_nai", connect));
				paramns.put("mob_impressao_tp_modelo_nip", ParametroServices.getValorOfParametro("mob_impressao_tp_modelo_nip", connect));
				paramns.put("MOB_APRESENTACAO_CONDUTOR", ParametroServices.getValorOfParametro("MOB_APRESENTACAO_CONDUTOR", connect));	
				paramns.put("MOB_PUBLICAR_AITS_NAO_ENTREGUES", ParametroServices.getValorOfParametro("MOB_PUBLICAR_AITS_NAO_ENTREGUES", connect));	
				paramns.put("NM_COMPLEMENTO", ParametroServices.getValorOfParametro("NM_COMPLEMENTO", connect));
				paramns.put("mob_apresentar_observacao", ParametroServices.getValorOfParametro("mob_apresentar_observacao", connect));
				paramns.put("NM_SISTEMA_PORTAL", new ParametroOldRepositoryDAO().getValorOfParametroAsString("NM_SISTEMA_PORTAL"));
				return paramns;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public HashMap<String, Object> getParamnsNIP() {
		return getParamnsNIP(null);
	}
	
	public static HashMap<String, Object> getParamnsNIP(Connection connect) {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		boolean isConnectionNull = connect==null;
		AitReportServices reportServices = new AitReportServices();
		
		try 
		{
			if (isConnectionNull) 
			{
				connect = Conexao.conectar();
			}
				 
			HashMap<String, Object> paramns = new HashMap<>();
			
			if (lgBaseAntiga)
			{
				paramns.put("IS_BASE_OLD", true);
				paramns.put("LOGO_1", reportServices.recoverLogosBaseOld(connect)[0]);
				paramns.put("LOGO_2", reportServices.recoverLogosBaseOld(connect)[1]);
				paramns.put("DS_TITULO_1", ParametroServices.getValorOfParametro("NM_ORGAO_AUTUADOR"));
				paramns.put("DS_TITULO_2", ParametroServices.getValorOfParametro("NM_SUBORGAO"));
				paramns.put("DS_TITULO_3", ParametroServices.getValorOfParametro("NM_DEPARTAMENTO"));
				paramns.put("MOB_IMPRESSOS_DS_TEXTO_NIP", ParametroServices.getValorOfParametro("txt_nip", connect));
				paramns.put("MOB_CD_ORGAO_AUTUADOR", ParametroServices.getValorOfParametro("cd_orgao_autuante", connect));
				paramns.put("MOB_CD_MUNICIPIO_ORGAO_AUTUADOR", ParametroServices.getValorOfParametro("cd_municipio_autuador"));
				paramns.put("NM_CIDADE", ParametroServices.getValorOfParametro("nm_municipio", connect));			
				paramns.put("NR_BANCO_CREDITO", ParametroServices.getValorOfParametro("NR_BANCO_CREDITO", connect));
				paramns.put("NR_AGENCIA_CREDITO", ParametroServices.getValorOfParametro("NR_AGENCIA_CREDITO", connect));
				paramns.put("NR_CONTA_CREDITO", ParametroServices.getValorOfParametro("NR_CONTA_CREDITO", connect));
				paramns.put("MOB_CORREIOS_NR_CONTRATO", ParametroServices.getValorOfParametro("nr_correios", connect));
				paramns.put("SG_ORGAO", ParametroServices.getValorOfParametro("SG_ORGAO", connect));
				paramns.put("SG_DEPARTAMENTO", ParametroServices.getValorOfParametro("SG_DEPARTAMENTO", connect));		
				paramns.put("NM_LOGRADOURO", ParametroServices.getValorOfParametro("NM_LOGRADOURO", connect));
				paramns.put("NR_ENDERECO", ParametroServices.getValorOfParametro("NR_ENDERECO", connect));
				paramns.put("NM_BAIRRO", ParametroServices.getValorOfParametro("NM_BAIRRO", connect));
				paramns.put("NR_CEP", ParametroServices.getValorOfParametro("NR_CEP", connect));
				paramns.put("NR_TELEFONE", ParametroServices.getValorOfParametro("NR_TELEFONE", "+55 (99) 9999-9999", connect));
				paramns.put("NM_EMAIL", ParametroServices.getValorOfParametro("NM_EMAIL"));
				paramns.put("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", ParametroServices.getValorOfParametro("MOB_BLB_MENSAGEM_EDUCATIVA_NAI"));
				paramns.put("MOB_PRAZOS_NR_RECURSO_JARI", ParametroServices.getValorOfParametro("nr_dias_jari", connect));
				paramns.put("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", ParametroServices.getValorOfParametro("ds_mensagem_indeferido", connect));			
				paramns.put("MOB_NOME_COORDENADOR_IMPRESSAO", ParametroServices.getValorOfParametro("nm_coordenador", connect));
				paramns.put("NR_CD_FEBRABAN", ParametroServices.getValorOfParametro("NR_CD_FEBRABAN", connect));
				paramns.put("MOB_PRAZOS_NR_DEFESA_PREVIA", ParametroServices.getValorOfParametro("nr_dias_defesa_previa", connect));
				paramns.put("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", connect));
				paramns.put("mob_local_impressao", ParametroServices.getValorOfParametro("mob_local_impressao", 0, connect));	
				paramns.put("NM_COMPLEMENTO", ParametroServices.getValorOfParametro("NM_COMPLEMENTO", connect));
				paramns.put("MOB_NR_PRAZO_PENAL_SEM_ENTREGA", ParametroServices.getValorOfParametro("NR_PRAZO_PENAL_SEM_ENTREGA", 90, connect));
				paramns.put("SG_UF", getEstadoOrgaoAutuador());
				paramns.put("MOB_INFORMACOES_ADICIONAIS_NIP", ParametroServices.getValorOfParametro("MOB_INFORMACOES_ADICIONAIS_NIP"));
				paramns.put("NM_SISTEMA_PORTAL", new ParametroOldRepositoryDAO().getValorOfParametroAsString("NM_SISTEMA_PORTAL"));
				return paramns;
			}
			else
			{
				paramns.put("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA", connect));
				paramns.put("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO", connect));
				paramns.put("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1", connect));
				paramns.put("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2", connect));
				paramns.put("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3", connect));
				paramns.put("MOB_IMPRESSOS_DS_TEXTO_NIP", ParametroServices.getValorOfParametro("MOB_IMPRESSOS_DS_TEXTO_NIP", connect));
				paramns.put("MOB_CD_ORGAO_AUTUADOR", ParametroServices.getValorOfParametro("MOB_CD_ORGAO_AUTUADOR", connect));
				paramns.put("NM_CIDADE", ParametroServices.getValorOfParametro("NM_CIDADE", "LOREM IPSUM DOLOR SIT AMET", connect));			
				paramns.put("NR_BANCO_CREDITO", ParametroServices.getValorOfParametro("NR_BANCO_CREDITO", connect));
				paramns.put("NR_AGENCIA_CREDITO", ParametroServices.getValorOfParametro("NR_AGENCIA_CREDITO", connect));
				paramns.put("NR_CONTA_CREDITO", ParametroServices.getValorOfParametro("NR_CONTA_CREDITO", connect));
				paramns.put("MOB_CORREIOS_NR_CONTRATO", ParametroServices.getValorOfParametro("MOB_CORREIOS_NR_CONTRATO", connect));
				paramns.put("SG_ORGAO", ParametroServices.getValorOfParametro("SG_ORGAO", connect));
				paramns.put("SG_DEPARTAMENTO", ParametroServices.getValorOfParametro("SG_DEPARTAMENTO", connect));	
				paramns.put("NM_LOGRADOURO", ParametroServices.getValorOfParametro("NM_LOGRADOURO", connect));
				paramns.put("NR_ENDERECO", ParametroServices.getValorOfParametro("NR_ENDERECO", connect));
				paramns.put("NM_BAIRRO", ParametroServices.getValorOfParametro("NM_BAIRRO", connect));
				paramns.put("NR_CEP", ParametroServices.getValorOfParametro("NR_CEP", connect));
				paramns.put("NR_TELEFONE", ParametroServices.getValorOfParametro("NR_TELEFONE", "+55 (99) 9999-9999", connect));
				paramns.put("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", ParametroServices.getValorOfParametro("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", connect));	
				paramns.put("MOB_PRAZOS_NR_RECURSO_JARI", ParametroServices.getValorOfParametro("MOB_PRAZOS_NR_RECURSO_JARI", connect));
				paramns.put("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", ParametroServices.getValorOfParametro("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", connect));
				paramns.put("MOB_NOME_COORDENADOR_IMPRESSAO", ParametroServices.getValorOfParametro("MOB_NOME_COORDENADOR_IMPRESSAO", connect));
				paramns.put("NR_CD_FEBRABAN", ParametroServices.getValorOfParametro("NR_CD_FEBRABAN", connect));
				paramns.put("MOB_PRAZOS_NR_DEFESA_PREVIA", ParametroServices.getValorOfParametro("MOB_PRAZOS_NR_DEFESA_PREVIA", connect));
				paramns.put("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", connect));
				paramns.put("mob_local_impressao", ParametroServices.getValorOfParametro("mob_local_impressao", 0, connect));	
				paramns.put("NM_COMPLEMENTO", ParametroServices.getValorOfParametro("NM_COMPLEMENTO", connect));
				paramns.put("NM_SISTEMA_PORTAL", new ParametroOldRepositoryDAO().getValorOfParametroAsString("NM_SISTEMA_PORTAL"));
				
				return paramns;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public HashMap<String, Object> getParamnsCondutor() {
		return getParamnsCondutor(null);
	}
	
	public HashMap<String, Object> getParamnsCondutor(Connection connect) {
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		boolean isConnectionNull = connect==null;
		AitReportServices reportServices = new AitReportServices();
		
		try {
			if (isConnectionNull) {
				connect = Conexao.conectar();
			}
				
			HashMap<String, Object> paramns = new HashMap<>();
			
			if (lgBaseAntiga)
			{
				paramns.put("IS_BASE_OLD", true);
				paramns.put("LOGO_1", reportServices.recoverLogosBaseOld(connect)[0]);
				paramns.put("LOGO_2", reportServices.recoverLogosBaseOld(connect)[1]);
				paramns.put("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1", "LOREM IPSUM DOLOR SIT AMET", connect));
				paramns.put("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2", "CONSECTETUR ADIPISCING ELIT", connect));
				paramns.put("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3", "INCIDIDUNT UT LABORE ET DOLORE", connect));
			
				return paramns;
			}
			else
			{
				paramns.put("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA"));
				paramns.put("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO"));
				paramns.put("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1", "LOREM IPSUM DOLOR SIT AMET", connect));
				paramns.put("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2", "CONSECTETUR ADIPISCING ELIT", connect));
				paramns.put("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3", "INCIDIDUNT UT LABORE ET DOLORE", connect));

				return paramns;
			}

		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public HashMap<String, Object> getParamnsNic() 
	{
		return getParamnsNic(null);
	}
	
	public HashMap<String, Object> getParamnsNic(Connection connect) 
	{
		boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
		boolean isConnectionNull = connect==null;
		AitReportServices reportServices = new AitReportServices();
		
		try 
		{
			if (isConnectionNull) 
			{
				connect = Conexao.conectar();
			}
				 
			HashMap<String, Object> paramns = new HashMap<>();
			
			if (lgBaseAntiga)
			{
				paramns.put("IS_BASE_OLD", true);
				paramns.put("LOGO_1", reportServices.recoverLogosBaseOld(connect)[0]);
				paramns.put("LOGO_2", reportServices.recoverLogosBaseOld(connect)[1]);
				paramns.put("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1", connect));
				paramns.put("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2", connect));
				paramns.put("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3", connect));
				paramns.put("MOB_IMPRESSOS_DS_TEXTO_NIP", ParametroServices.getValorOfParametro("txt_nip", connect));
				paramns.put("MOB_CD_ORGAO_AUTUADOR", ParametroServices.getValorOfParametro("cd_orgao_autuante", connect));
				paramns.put("NM_CIDADE", ParametroServices.getValorOfParametro("nm_municipio", "LOREM IPSUM DOLOR SIT AMET", connect));			
				paramns.put("NR_BANCO_CREDITO", ParametroServices.getValorOfParametro("NR_BANCO_CREDITO", connect));
				paramns.put("NR_AGENCIA_CREDITO", ParametroServices.getValorOfParametro("NR_AGENCIA_CREDITO", connect));
				paramns.put("NR_CONTA_CREDITO", ParametroServices.getValorOfParametro("NR_CONTA_CREDITO", connect));
				paramns.put("MOB_CORREIOS_NR_CONTRATO", ParametroServices.getValorOfParametro("nr_correios", connect));
				paramns.put("SG_ORGAO", ParametroServices.getValorOfParametro("SG_ORGAO", connect));
				paramns.put("SG_DEPARTAMENTO", ParametroServices.getValorOfParametro("SG_DEPARTAMENTO", connect));		
				paramns.put("NM_LOGRADOURO", ParametroServices.getValorOfParametro("NM_LOGRADOURO", connect));
				paramns.put("NR_ENDERECO", ParametroServices.getValorOfParametro("NR_ENDERECO", connect));
				paramns.put("NM_BAIRRO", ParametroServices.getValorOfParametro("NM_BAIRRO", connect));
				paramns.put("NR_CEP", ParametroServices.getValorOfParametro("NR_CEP", connect));
				paramns.put("NR_TELEFONE", ParametroServices.getValorOfParametro("NR_TELEFONE", "+55 (99) 9999-9999", connect));
				paramns.put("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", ParametroServices.getValorOfParametro("ds_mensagem_nai", connect));	
				paramns.put("MOB_PRAZOS_NR_RECURSO_JARI", ParametroServices.getValorOfParametro("nr_dias_jari", connect));
				paramns.put("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", ParametroServices.getValorOfParametro("ds_mensagem_indeferido", connect));			
				paramns.put("MOB_NOME_COORDENADOR_IMPRESSAO", ParametroServices.getValorOfParametro("nm_coordenador", connect));
				paramns.put("NR_CD_FEBRABAN", ParametroServices.getValorOfParametro("NR_CD_FEBRABAN", connect));
				paramns.put("MOB_PRAZOS_NR_DEFESA_PREVIA", ParametroServices.getValorOfParametro("nr_dias_defesa_previa", connect));
				paramns.put("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", connect));
				paramns.put("mob_local_impressao", ParametroServices.getValorOfParametro("mob_local_impressao", 0, connect));	
				paramns.put("NM_COMPLEMENTO", ParametroServices.getValorOfParametro("NM_COMPLEMENTO", connect));
				
				return paramns;
			}
			else
			{
				paramns.put("LOGO_1", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_PREFEITURA", connect));
				paramns.put("LOGO_2", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_LOGO_DEPARTAMENTO", connect));
				paramns.put("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1", connect));
				paramns.put("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2", connect));
				paramns.put("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3", connect));
				paramns.put("MOB_IMPRESSOS_DS_TEXTO_NIP", ParametroServices.getValorOfParametro("MOB_IMPRESSOS_DS_TEXTO_NIP", connect));
				paramns.put("MOB_CD_ORGAO_AUTUADOR", ParametroServices.getValorOfParametro("MOB_CD_ORGAO_AUTUADOR", connect));
				paramns.put("NM_CIDADE", ParametroServices.getValorOfParametro("NM_CIDADE", "LOREM IPSUM DOLOR SIT AMET", connect));			
				paramns.put("NR_BANCO_CREDITO", ParametroServices.getValorOfParametro("NR_BANCO_CREDITO", connect));
				paramns.put("NR_AGENCIA_CREDITO", ParametroServices.getValorOfParametro("NR_AGENCIA_CREDITO", connect));
				paramns.put("NR_CONTA_CREDITO", ParametroServices.getValorOfParametro("NR_CONTA_CREDITO", connect));
				paramns.put("MOB_CORREIOS_NR_CONTRATO", ParametroServices.getValorOfParametro("MOB_CORREIOS_NR_CONTRATO", connect));
				paramns.put("SG_ORGAO", ParametroServices.getValorOfParametro("SG_ORGAO", connect));
				paramns.put("SG_DEPARTAMENTO", ParametroServices.getValorOfParametro("SG_DEPARTAMENTO", connect));	
				paramns.put("NM_LOGRADOURO", ParametroServices.getValorOfParametro("NM_LOGRADOURO", connect));
				paramns.put("NR_ENDERECO", ParametroServices.getValorOfParametro("NR_ENDERECO", connect));
				paramns.put("NM_BAIRRO", ParametroServices.getValorOfParametro("NM_BAIRRO", connect));
				paramns.put("NR_CEP", ParametroServices.getValorOfParametro("NR_CEP", connect));
				paramns.put("NR_TELEFONE", ParametroServices.getValorOfParametro("NR_TELEFONE", "+55 (99) 9999-9999", connect));
				paramns.put("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", ParametroServices.getValorOfParametro("MOB_BLB_MENSAGEM_EDUCATIVA_NAI", connect));	
				paramns.put("MOB_PRAZOS_NR_RECURSO_JARI", ParametroServices.getValorOfParametro("MOB_PRAZOS_NR_RECURSO_JARI", connect));
				paramns.put("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", ParametroServices.getValorOfParametro("MOB_BLB_DEFESA_PREVIA_INDEFERIDA", connect));
				paramns.put("MOB_NOME_COORDENADOR_IMPRESSAO", ParametroServices.getValorOfParametro("MOB_NOME_COORDENADOR_IMPRESSAO", connect));
				paramns.put("NR_CD_FEBRABAN", ParametroServices.getValorOfParametro("NR_CD_FEBRABAN", connect));
				paramns.put("MOB_PRAZOS_NR_DEFESA_PREVIA", ParametroServices.getValorOfParametro("MOB_PRAZOS_NR_DEFESA_PREVIA", connect));
				paramns.put("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", ParametroServices.RecImg("MOB_IMPRESSOS_BLB_CHANCELA_CORREIOS", connect));
				paramns.put("mob_local_impressao", ParametroServices.getValorOfParametro("mob_local_impressao", 0, connect));	
				paramns.put("NM_COMPLEMENTO", ParametroServices.getValorOfParametro("NM_COMPLEMENTO", connect));
				
				return paramns;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	public HashMap<String, Object> getParamnsAUTO() {
		return getParamnsNAI(null);
	}
	
	public HashMap<String, Object> getParamnsAUTO(Connection connect) 
	{
		
		boolean isConnectionNull = connect==null;
		AitReportServices reportServices = new AitReportServices();
		
		try 
		{
			
			if (isConnectionNull)
				connect = Conexao.conectar();
		
			HashMap<String, Object> paramns = new HashMap<>();
	
			paramns.put("DS_TITULO_1", ParametroServices.getValorOfParametro("DS_TITULO_1", connect));
			paramns.put("DS_TITULO_2", ParametroServices.getValorOfParametro("DS_TITULO_2", connect));
			paramns.put("DS_TITULO_3", ParametroServices.getValorOfParametro("DS_TITULO_3", connect));
			paramns.put("NR_TELEFONE", ParametroServices.getValorOfParametro("NR_TELEFONE", "+55 (99) 9999-9999", connect));
			paramns.put("NM_CIDADE", ParametroServices.getValorOfParametro("NM_CIDADE", "LOREM IPSUM DOLOR SIT AMET", connect));			
			paramns.put("MOB_CD_ORGAO_AUTUADOR", ParametroServices.getValorOfParametro("MOB_CD_ORGAO_AUTUADOR", connect));
			paramns.put("NM_LOGRADOURO", ParametroServices.getValorOfParametro("NM_LOGRADOURO", connect));
			paramns.put("NR_ENDERECO", ParametroServices.getValorOfParametro("NR_ENDERECO", connect));
			paramns.put("NM_BAIRRO", ParametroServices.getValorOfParametro("NM_BAIRRO", connect));
			paramns.put("NR_CEP", ParametroServices.getValorOfParametro("NR_CEP", connect));
			paramns.put("SG_ORGAO", ParametroServices.getValorOfParametro("SG_ORGAO", connect));
			paramns.put("SG_DEPARTAMENTO", ParametroServices.getValorOfParametro("SG_DEPARTAMENTO", connect));		
			paramns.put("DS_ENDERECO", ParametroServices.getValorOfParametro("DS_ENDERECO", connect));
			paramns.put("NM_SUBORGAO", ParametroServices.getValorOfParametro("NM_SUBORGAO", "LOREM IPSUM DOLOR SIT AMET", connect));
			paramns.put("NM_COMPLEMENTO", ParametroServices.getValorOfParametro("NM_COMPLEMENTO", connect));
			paramns.put("MOB_IMPRESSOS_NM_MUNICIPIO_AUTUADOR", ParametroServices.getValorOfParametro("MOB_IMPRESSOS_NM_MUNICIPIO_AUTUADOR", connect));

			return paramns;
			
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			if(isConnectionNull)
				Conexao.desconectar(connect);
		}
	}
	
	private static String getEstadoOrgaoAutuador() {
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Cidade cidade = CidadeDAO.get(orgao.getCdCidade());
		return EstadoDAO.get(cidade.getCdEstado()).getSgEstado();
	}
	
}
