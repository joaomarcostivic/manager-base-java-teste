package com.tivic.manager.mob.lotes.impressao.pix;

public class Devedor {

    private int cdDevedor;
    private String nmDevedor;
    private String nrCpfCnpj;
    private String nmEmail;
    private String nrTelefone;

    public int getCdDevedor() {
        return cdDevedor;
    }

    public void setCdDevedor(int cdDevedor) {
        this.cdDevedor = cdDevedor;
    }

    public String getNmDevedor() {
        return nmDevedor;
    }

    public void setNmDevedor(String nmDevedor) {
        this.nmDevedor = nmDevedor;
    }

    public String getNrCpfCnpj() {
        return nrCpfCnpj;
    }

    public void setNrCpfCnpj(String nrCpfCnpj) {
        this.nrCpfCnpj = nrCpfCnpj;
    }

    public String getNmEmail() {
        return nmEmail;
    }

    public void setNmEmail(String nmEmail) {
        this.nmEmail = nmEmail;
    }

    public String getNrTelefone() {
        return nrTelefone;
    }

    public void setNrTelefone(String nrTelefone) {
        this.nrTelefone = nrTelefone;
    }

    @Override
    public String toString() {
        return "{" 
        	+ "\"cdDevedor\": " + getCdDevedor() + "\"" 
            + ", \"nmDevedor\": \"" + getNmDevedor() + "\""
            + ", \"nrCpfCnpj\": \"" + getNrCpfCnpj() + "\"" 
            + ", \"nmEmail\": \"" + getNmEmail() + "\"" 
            + ", \"nrTelefone\": \"" + getNrTelefone() + "\"" 
            + "}";
    }
}
