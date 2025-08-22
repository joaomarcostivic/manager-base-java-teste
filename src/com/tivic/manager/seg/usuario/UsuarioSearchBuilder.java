package com.tivic.manager.seg.usuario;

import java.sql.Types;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.sol.search.SearchCriterios;

import sol.dao.ItemComparator;

public class UsuarioSearchBuilder {
	private SearchCriterios searchCriterios;
	private boolean lgBaseAntiga = ManagerConf.getInstance().getAsBoolean("STR_BASE_ANTIGA");
	
	UsuarioSearchBuilder() {
		searchCriterios = new SearchCriterios();
	}
	
	UsuarioSearchBuilder setNmPessoa(String nmPessoa) {
		searchCriterios.addCriteriosLikeAnyString("B.nm_pessoa", nmPessoa, nmPessoa != null);
		return this;
	}
	
	UsuarioSearchBuilder setCdPessoa(int cdPessoa) {
		searchCriterios.addCriteriosEqualInteger(lgBaseAntiga ? "A.cod_usuario" : "A.cd_pessoa", cdPessoa, cdPessoa > 0);
		return this;
	}
	
	UsuarioSearchBuilder setStLogin(int stLogin) {
		searchCriterios.addCriteriosEqualInteger("A.st_login", stLogin, stLogin > -1);
		return this;
	}
	
	UsuarioSearchBuilder setStLoginEquipamento(int stLogin) {
		searchCriterios.addCriteriosEqualInteger("A.st_login", stLogin, stLogin > -1);
		searchCriterios.addCriterios("A.cd_equipamento", null, ItemComparator.NOTISNULL, Types.INTEGER, stLogin > -1);
		return this;
	}
	
	UsuarioSearchBuilder setNmLogin(String nmLogin) {
		searchCriterios.addCriteriosLikeAnyString(lgBaseAntiga ? "A.nm_nick" : "A.nm_login", nmLogin, nmLogin != null);
		return this;
	}
	
	UsuarioSearchBuilder setStUsuario(int stUsuario) {
		searchCriterios.addCriteriosEqualInteger("A.st_usuario", stUsuario, stUsuario > -1);
		return this;
	}
	
	UsuarioSearchBuilder setQtDelocamento(int limit, int page) {
		searchCriterios.setQtDeslocamento((limit*page)-limit);
		return this;		
	}
	
	UsuarioSearchBuilder setQtLimit(int limit) {
		searchCriterios.setQtLimite(limit);
		return this;		
	}
	
	public SearchCriterios build() {
		return this.searchCriterios;
	}
}
