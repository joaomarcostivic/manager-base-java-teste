package com.tivic.manager.mob;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarDeserialize;
import com.tivic.sol.serializer.CalendarSerializer;

public class Declarante {

	private int cdDeclarante;
	private String nmDeclarante;
	private String nrCpf;
	@JsonSerialize(converter = CalendarSerializer.class)
	@JsonDeserialize(converter = CalendarDeserialize.class)
	private GregorianCalendar dtNascimento;
	private int tpSexo;
	private String nrTelefone;
	private String nrCelular;
	private String nmEmail;
	private String nrCnh;
	private String sgUfCnh;
	private String nmProfissao;

	public Declarante() { }

	public Declarante(int cdDeclarante,
			String nmDeclarante,
			String nrCpf,
			GregorianCalendar dtNascimento,
			int tpSexo,
			String nrTelefone,
			String nrCelular,
			String nmEmail,
			String nrCnh,
			String sgUfCnh,
			String nmProfissao) {
		setCdDeclarante(cdDeclarante);
		setNmDeclarante(nmDeclarante);
		setNrCpf(nrCpf);
		setDtNascimento(dtNascimento);
		setTpSexo(tpSexo);
		setNrTelefone(nrTelefone);
		setNrCelular(nrCelular);
		setNmEmail(nmEmail);
		setNrCnh(nrCnh);
		setSgUfCnh(sgUfCnh);
		setNmProfissao(nmProfissao);
	}
	public void setCdDeclarante(int cdDeclarante){
		this.cdDeclarante=cdDeclarante;
	}
	public int getCdDeclarante(){
		return this.cdDeclarante;
	}
	public void setNmDeclarante(String nmDeclarante){
		this.nmDeclarante=nmDeclarante;
	}
	public String getNmDeclarante(){
		return this.nmDeclarante;
	}
	public void setNrCpf(String nrCpf){
		this.nrCpf=nrCpf;
	}
	public String getNrCpf(){
		return this.nrCpf;
	}
	public void setDtNascimento(GregorianCalendar dtNascimento){
		this.dtNascimento=dtNascimento;
	}
	public GregorianCalendar getDtNascimento(){
		return this.dtNascimento;
	}
	public void setTpSexo(int tpSexo){
		this.tpSexo=tpSexo;
	}
	public int getTpSexo(){
		return this.tpSexo;
	}
	public void setNrTelefone(String nrTelefone){
		this.nrTelefone=nrTelefone;
	}
	public String getNrTelefone(){
		return this.nrTelefone;
	}
	public void setNrCelular(String nrCelular){
		this.nrCelular=nrCelular;
	}
	public String getNrCelular(){
		return this.nrCelular;
	}
	public void setNmEmail(String nmEmail){
		this.nmEmail=nmEmail;
	}
	public String getNmEmail(){
		return this.nmEmail;
	}
	public void setNrCnh(String nrCnh) {
		this.nrCnh=nrCnh;
	}
	public String getNrCnh(){
		return this.nrCnh;
	}
	public void setSgUfCnh(String sgUfCnh){
		this.sgUfCnh=sgUfCnh;
	}
	public String getSgUfCnh(){
		return this.sgUfCnh;
	}
	public void setNmProfissao(String nmProfissao){
		this.nmProfissao=nmProfissao;
	}
	public String getNmProfissao(){
		return this.nmProfissao;
	}
	public String toString() {
		String valueToString = "";
		valueToString += "cdDeclarante: " +  getCdDeclarante();
		valueToString += ", nmDeclarante: " +  getNmDeclarante();
		valueToString += ", nrCpf: " +  getNrCpf();
		valueToString += ", dtNascimento: " +  sol.util.Util.formatDateTime(getDtNascimento(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", tpSexo: " +  getTpSexo();
		valueToString += ", nrTelefone: " +  getNrTelefone();
		valueToString += ", nrCelular: " +  getNrCelular();
		valueToString += ", nmEmail: " +  getNmEmail();
		valueToString += ", nrCnh: " +  getNrCnh();
		valueToString += ", sgUfCnh: " +  getSgUfCnh();
		valueToString += ", nmProfissao: " +  getNmProfissao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Declarante(getCdDeclarante(),
			getNmDeclarante(),
			getNrCpf(),
			getDtNascimento()==null ? null : (GregorianCalendar)getDtNascimento().clone(),
			getTpSexo(),
			getNrTelefone(),
			getNrCelular(),
			getNmEmail(),
			getNrCnh(),
			getSgUfCnh(),
			getNmProfissao());
	}

}