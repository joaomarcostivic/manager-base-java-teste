package com.tivic.manager.seg;


public class Atividade
{
    private int cdAtividade;
    private int cdCadastro;
    private int cdSistema;
    private String nmAtividade;
    private String idAtividade;

    public Atividade(int cdAtividade, int cdCadastro, int cdSistema, String nmAtividade, String idAtividade)
    {
        setCdAtividade(cdAtividade);
        setCdCadastro(cdCadastro);
        setCdSistema(cdSistema);
        setNmAtividade(nmAtividade);
        setIdAtividade(idAtividade);
    }

    public void setCdAtividade(int cdAtividade)
    {
        this.cdAtividade = cdAtividade;
    }

    public int getCdAtividade()
    {
        return cdAtividade;
    }

    public void setCdCadastro(int cdCadastro)
    {
        this.cdCadastro = cdCadastro;
    }

    public int getCdCadastro()
    {
        return cdCadastro;
    }

    public void setCdSistema(int cdSistema)
    {
        this.cdSistema = cdSistema;
    }

    public int getCdSistema()
    {
        return cdSistema;
    }

    public void setNmAtividade(String nmAtividade)
    {
        this.nmAtividade = nmAtividade;
    }

    public String getNmAtividade()
    {
        return nmAtividade;
    }

    public void setIdAtividade(String idAtividade)
    {
        this.idAtividade = idAtividade;
    }

    public String getIdAtividade()
    {
        return idAtividade;
    }
}
