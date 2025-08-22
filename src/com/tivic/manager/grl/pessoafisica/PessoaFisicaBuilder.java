package com.tivic.manager.grl.pessoafisica;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.PessoaFisica;

public class PessoaFisicaBuilder {
	private PessoaFisica pessoaFisica;
	
	
	public PessoaFisicaBuilder(){
		pessoaFisica = new PessoaFisica();
	}
	
	public PessoaFisicaBuilder setCdNaturalidade(int cdNaturalidade) {
        pessoaFisica.setCdNaturalidade(cdNaturalidade);
        return this;
    }

    public PessoaFisicaBuilder setCdEscolaridade(int cdEscolaridade) {
        pessoaFisica.setCdEscolaridade(cdEscolaridade);
        return this;
    }

    public PessoaFisicaBuilder setDtNascimento(GregorianCalendar dtNascimento) {
        pessoaFisica.setDtNascimento(dtNascimento);
        return this;
    }

    public PessoaFisicaBuilder setNrCpf(String nrCpf) {
        pessoaFisica.setNrCpf(nrCpf);
        return this;
    }

    public PessoaFisicaBuilder setSgOrgaoRg(String sgOrgaoRg) {
        pessoaFisica.setSgOrgaoRg(sgOrgaoRg);
        return this;
    }

    public PessoaFisicaBuilder setNmMae(String nmMae) {
        pessoaFisica.setNmMae(nmMae);
        return this;
    }

    public PessoaFisicaBuilder setNmPai(String nmPai) {
        pessoaFisica.setNmPai(nmPai);
        return this;
    }

    public PessoaFisicaBuilder setTpSexo(int tpSexo) {
        pessoaFisica.setTpSexo(tpSexo);
        return this;
    }

    public PessoaFisicaBuilder setStEstadoCivil(int stEstadoCivil) {
        pessoaFisica.setStEstadoCivil(stEstadoCivil);
        return this;
    }

    public PessoaFisicaBuilder setNrRg(String nrRg) {
        pessoaFisica.setNrRg(nrRg);
        return this;
    }

    public PessoaFisicaBuilder setNrCnh(String nrCnh) {
        pessoaFisica.setNrCnh(nrCnh);
        return this;
    }

    public PessoaFisicaBuilder setDtValidadeCnh(GregorianCalendar dtValidadeCnh) {
        pessoaFisica.setDtValidadeCnh(dtValidadeCnh);
        return this;
    }

    public PessoaFisicaBuilder setDtPrimeiraHabilitacao(GregorianCalendar dtPrimeiraHabilitacao) {
        pessoaFisica.setDtPrimeiraHabilitacao(dtPrimeiraHabilitacao);
        return this;
    }

    public PessoaFisicaBuilder setTpCategoriaHabilitacao(int tpCategoriaHabilitacao) {
        pessoaFisica.setTpCategoriaHabilitacao(tpCategoriaHabilitacao);
        return this;
    }

    public PessoaFisicaBuilder setTpRaca(int tpRaca) {
        pessoaFisica.setTpRaca(tpRaca);
        return this;
    }

    public PessoaFisicaBuilder setLgDeficienteFisico(int lgDeficienteFisico) {
        pessoaFisica.setLgDeficienteFisico(lgDeficienteFisico);
        return this;
    }

    public PessoaFisicaBuilder setNmFormaTratamento(String nmFormaTratamento) {
        pessoaFisica.setNmFormaTratamento(nmFormaTratamento);
        return this;
    }

    public PessoaFisicaBuilder setCdEstadoRg(int cdEstadoRg) {
        pessoaFisica.setCdEstadoRg(cdEstadoRg);
        return this;
    }

    public PessoaFisicaBuilder setDtEmissaoRg(GregorianCalendar dtEmissaoRg) {
        pessoaFisica.setDtEmissaoRg(dtEmissaoRg);
        return this;
    }

    public PessoaFisicaBuilder setBlbFingerprint(byte[] blbFingerprint) {
        pessoaFisica.setBlbFingerprint(blbFingerprint);
        return this;
    }

    public PessoaFisicaBuilder setCdConjuge(int cdConjuge) {
        pessoaFisica.setCdConjuge(cdConjuge);
        return this;
    }

    public PessoaFisicaBuilder setQtMembrosFamilia(int qtMembrosFamilia) {
        pessoaFisica.setQtMembrosFamilia(qtMembrosFamilia);
        return this;
    }

    public PessoaFisicaBuilder setVlRendaFamiliarPerCapta(float vlRendaFamiliarPerCapta) {
        pessoaFisica.setVlRendaFamiliarPerCapta(vlRendaFamiliarPerCapta);
        return this;
    }

    public PessoaFisicaBuilder setTpNacionalidade(int tpNacionalidade) {
        pessoaFisica.setTpNacionalidade(tpNacionalidade);
        return this;
    }

    public PessoaFisicaBuilder setTpFiliacaoPai(int tpFiliacaoPai) {
        pessoaFisica.setTpFiliacaoPai(tpFiliacaoPai);
        return this;
    }

    public PessoaFisicaBuilder setLgDocumentoAusente(int lgDocumentoAusente) {
        pessoaFisica.setLgDocumentoAusente(lgDocumentoAusente);
        return this;
    }
    
    public PessoaFisicaBuilder setCdPessoa(int cdPessoa) {
        pessoaFisica.setCdPessoa(cdPessoa);
        return this;
    }

    public PessoaFisicaBuilder setCdPessoaSuperior(int cdPessoaSuperior) {
        pessoaFisica.setCdPessoaSuperior(cdPessoaSuperior);
        return this;
    }

    public PessoaFisicaBuilder setCdPais(int cdPais) {
        pessoaFisica.setCdPais(cdPais);
        return this;
    }

    public PessoaFisicaBuilder setNmPessoa(String nmPessoa) {
        pessoaFisica.setNmPessoa(nmPessoa);
        return this;
    }

    public PessoaFisicaBuilder setNrTelefone1(String nrTelefone1) {
        pessoaFisica.setNrTelefone1(nrTelefone1);
        return this;
    }

    public PessoaFisicaBuilder setNrTelefone2(String nrTelefone2) {
        pessoaFisica.setNrTelefone2(nrTelefone2);
        return this;
    }

    public PessoaFisicaBuilder setNrCelular(String nrCelular) {
        pessoaFisica.setNrCelular(nrCelular);
        return this;
    }

    public PessoaFisicaBuilder setNrFax(String nrFax) {
        pessoaFisica.setNrFax(nrFax);
        return this;
    }

    public PessoaFisicaBuilder setNmEmail(String nmEmail) {
        pessoaFisica.setNmEmail(nmEmail);
        return this;
    }

    public PessoaFisicaBuilder setDtCadastro(GregorianCalendar dtCadastro) {
        pessoaFisica.setDtCadastro(dtCadastro);
        return this;
    }

    public PessoaFisicaBuilder setGnPessoa(int gnPessoa) {
        pessoaFisica.setGnPessoa(gnPessoa);
        return this;
    }

    public PessoaFisicaBuilder setImgFoto(byte[] imgFoto) {
        pessoaFisica.setImgFoto(imgFoto);
        return this;
    }

    public PessoaFisicaBuilder setStCadastro(int stCadastro) {
        pessoaFisica.setStCadastro(stCadastro);
        return this;
    }

    public PessoaFisicaBuilder setNmUrl(String nmUrl) {
        pessoaFisica.setNmUrl(nmUrl);
        return this;
    }

    public PessoaFisicaBuilder setNmApelido(String nmApelido) {
        pessoaFisica.setNmApelido(nmApelido);
        return this;
    }

    public PessoaFisicaBuilder setTxtObservacao(String txtObservacao) {
        pessoaFisica.setTxtObservacao(txtObservacao);
        return this;
    }

    public PessoaFisicaBuilder setLgNotificacao(int lgNotificacao) {
        pessoaFisica.setLgNotificacao(lgNotificacao);
        return this;
    }

    public PessoaFisicaBuilder setIdPessoa(String idPessoa) {
        pessoaFisica.setIdPessoa(idPessoa);
        return this;
    }

    public PessoaFisicaBuilder setCdClassificacao(int cdClassificacao) {
        pessoaFisica.setCdClassificacao(cdClassificacao);
        return this;
    }

    public PessoaFisicaBuilder setCdFormaDivulgacao(int cdFormaDivulgacao) {
        pessoaFisica.setCdFormaDivulgacao(cdFormaDivulgacao);
        return this;
    }

    public PessoaFisicaBuilder setDtChegadaPais(GregorianCalendar dtChegadaPais) {
        pessoaFisica.setDtChegadaPais(dtChegadaPais);
        return this;
    }

    public PessoaFisicaBuilder setCdUsuarioCadastro(int cdUsuarioCadastro) {
        pessoaFisica.setCdUsuarioCadastro(cdUsuarioCadastro);
        return this;
    }

    public PessoaFisicaBuilder setNrOab(String nrOab) {
        pessoaFisica.setNrOab(nrOab);
        return this;
    }

    public PessoaFisicaBuilder setNmParceiro(String nmParceiro) {
        pessoaFisica.setNmParceiro(nmParceiro);
        return this;
    }

    public PessoaFisicaBuilder setNrCelular2(String nrCelular2) {
        pessoaFisica.setNrCelular2(nrCelular2);
        return this;
    }
	
	public PessoaFisica build() {
		return pessoaFisica;
	}
}
