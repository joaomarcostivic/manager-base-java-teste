package com.tivic.manager.seg;

public class Cadastro	{
    private int cdCadastro;
    private int cdSistema;
    private String nmCadastro;
    private String idCadastro;

    public Cadastro(int cdCadastro, int cdSistema, String nmCadastro, String idCadastro)    {
        setCdCadastro(cdCadastro);
        setCdSistema(cdSistema);
        setNmCadastro(nmCadastro);
        setIdCadastro(idCadastro);
    }

    public void setCdCadastro(int cdCadastro)    {
        this.cdCadastro = cdCadastro;
    }

    public int getCdCadastro()    {
        return cdCadastro;
    }

    public void setCdSistema(int cdSistema)    {
        this.cdSistema = cdSistema;
    }

    public int getCdSistema()    {
        return cdSistema;
    }

    public void setNmCadastro(String nmCadastro)
    {
        this.nmCadastro = nmCadastro;
    }

    public String getNmCadastro()
    {
        return nmCadastro;
    }

    public void setIdCadastro(String idCadastro){
        this.idCadastro = idCadastro;
    }

    public String getIdCadastro()    {
        return idCadastro;
    }
}
