package com.tivic.manager.grl.parametro.builders;

import com.tivic.manager.grl.Parametro;
import com.tivic.manager.grl.ParametroValor;

public class ParametroBuilder {
	
	   private Parametro parametro;

	    public ParametroBuilder() {
	        parametro = new Parametro();
	    }

	    public ParametroBuilder setCdParametro(int cdParametro) {
	        parametro.setCdParametro(cdParametro);
	        return this;
	    }

	    public ParametroBuilder setCdEmpresa(int cdEmpresa) {
	        parametro.setCdEmpresa(cdEmpresa);
	        return this;
	    }

	    public ParametroBuilder setNmParametro(String nmParametro) {
	        parametro.setNmParametro(nmParametro);
	        return this;
	    }

	    public ParametroBuilder setTpDado(int tpDado) {
	        parametro.setTpDado(tpDado);
	        return this;
	    }

	    public ParametroBuilder setTpApresentacao(int tpApresentacao) {
	        parametro.setTpApresentacao(tpApresentacao);
	        return this;
	    }

	    public ParametroBuilder setNmRotulo(String nmRotulo) {
	        parametro.setNmRotulo(nmRotulo);
	        return this;
	    }

	    public ParametroBuilder setTxtUrlFiltro(String txtUrlFiltro) {
	        parametro.setTxtUrlFiltro(txtUrlFiltro);
	        return this;
	    }

	    public ParametroBuilder setCdPessoa(int cdPessoa) {
	        parametro.setCdPessoa(cdPessoa);
	        return this;
	    }

	    public ParametroBuilder setTpParametro(int tpParametro) {
	        parametro.setTpParametro(tpParametro);
	        return this;
	    }

	    public ParametroBuilder setCdModulo(int cdModulo) {
	        parametro.setCdModulo(cdModulo);
	        return this;
	    }

	    public ParametroBuilder setCdSistema(int cdSistema) {
	        parametro.setCdSistema(cdSistema);
	        return this;
	    }

	    public ParametroBuilder setTpNivelAcesso(int tpNivelAcesso) {
	        parametro.setTpNivelAcesso(tpNivelAcesso);
	        return this;
	    }

	    public ParametroBuilder setValores(ParametroValor[] valores) {
	        parametro.setValores(valores);
	        return this;
	    }

	    public Parametro build() {
	        return parametro;
	    }
	    
}
