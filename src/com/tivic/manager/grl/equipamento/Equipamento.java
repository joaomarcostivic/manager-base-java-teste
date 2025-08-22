package com.tivic.manager.grl.equipamento;

import java.util.GregorianCalendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tivic.sol.serializer.CalendarSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Equipamento {

	private int cdEquipamento;
	private String nmEquipamento;
	private String idEquipamento;
	private int tpEquipamento;
	private String txtObservacao;
	private String nmMarca;
	private String nmModelo;
	private int stEquipamento;
	private int cdLogradouro;
	private Double vlLatitude;
	private Double vlLongitude;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtInicial;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtFinal;
	
	private int cdOrgao;
	private String nmHost;
	private int nrPort;
	private String nmPwd;
	private int nrChannel;
	private String nmLogin;
	private String nmUrlSnapshot;
	private String nmUrlStream;
	private String dsLocal;
	private int lgCriptografia;
	private int lgSyncFtp;
	
	@JsonSerialize(converter = CalendarSerializer.class)
	private GregorianCalendar dtAfericao;
	private String nrLacre;
	private String nrInventarioInmetro;

	public Equipamento() { }
	
	public Equipamento(int cdEquipamento,
			String nmEquipamento,
			String idEquipamento,
			int tpEquipamento,
			String txtObservacao,
			String nmMarca,
			String nmModelo,
			int stEquipamento,
			int cdLogradouro,
			double vlLatitude,
			double vlLongitude){
		setCdEquipamento(cdEquipamento);
		setNmEquipamento(nmEquipamento);
		setIdEquipamento(idEquipamento);
		setTpEquipamento(tpEquipamento);
		setTxtObservacao(txtObservacao);
		setNmMarca(nmMarca);
		setNmModelo(nmModelo);
		setStEquipamento(stEquipamento);
		setCdLogradouro(cdLogradouro);
		setVlLatitude(vlLatitude);
		setVlLongitude(vlLongitude);
	}
	
	public Equipamento(int cdEquipamento,
			String nmEquipamento,
			String idEquipamento,
			int tpEquipamento,
			String txtObservacao,
			String nmMarca,
			String nmModelo,
			int stEquipamento,
			int cdLogradouro,
			double vlLatitude,
			double vlLongitude,
			int cdOrgao,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal){
		setCdEquipamento(cdEquipamento);
		setNmEquipamento(nmEquipamento);
		setIdEquipamento(idEquipamento);
		setTpEquipamento(tpEquipamento);
		setTxtObservacao(txtObservacao);
		setNmMarca(nmMarca);
		setNmModelo(nmModelo);
		setStEquipamento(stEquipamento);
		setCdLogradouro(cdLogradouro);
		setVlLatitude(vlLatitude);
		setVlLongitude(vlLongitude);
		setCdOrgao(cdOrgao);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
	}

	public Equipamento(int cdEquipamento,
			String nmEquipamento,
			String idEquipamento,
			int tpEquipamento,
			String txtObservacao,
			String nmMarca,
			String nmModelo,
			int stEquipamento,
			int cdLogradouro,
			Double vlLatitude,
			Double vlLongitude,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal,
			int cdOrgao,
			String nmHost,
			int nrPort,
			String nmPwd,
			int nrChannel,
			String nmLogin,
			String nmUrlSnapshot,
			String nmUrlStream,
			String dsLocal,
			int lgCriptografia,
			int lgSyncFtp,
			GregorianCalendar dtAfericao,
			String nrLacre,
			String nrInventarioInmetro) {
		setCdEquipamento(cdEquipamento);
		setNmEquipamento(nmEquipamento);
		setIdEquipamento(idEquipamento);
		setTpEquipamento(tpEquipamento);
		setTxtObservacao(txtObservacao);
		setNmMarca(nmMarca);
		setNmModelo(nmModelo);
		setStEquipamento(stEquipamento);
		setCdLogradouro(cdLogradouro);
		setVlLatitude(vlLatitude);
		setVlLongitude(vlLongitude);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
		setCdOrgao(cdOrgao);
		setNmHost(nmHost);
		setNrPort(nrPort);
		setNmPwd(nmPwd);
		setNrChannel(nrChannel);
		setNmLogin(nmLogin);
		setNmUrlSnapshot(nmUrlSnapshot);
		setNmUrlStream(nmUrlStream);
		setDsLocal(dsLocal);
		setLgCriptografia(lgCriptografia);
		setLgSyncFtp(lgSyncFtp);
		setDtAfericao(dtAfericao);
		setNrLacre(nrLacre);
		setNrInventarioInmetro(nrInventarioInmetro);
	}

	public Equipamento(int cdEquipamento,
			String nmEquipamento,
			String idEquipamento,
			int tpEquipamento,
			String txtObservacao,
			String nmMarca,
			String nmModelo,
			int stEquipamento,
			int cdLogradouro,
			Double vlLatitude,
			Double vlLongitude,
			GregorianCalendar dtInicial,
			GregorianCalendar dtFinal,
			int cdOrgao) {
		setCdEquipamento(cdEquipamento);
		setNmEquipamento(nmEquipamento);
		setIdEquipamento(idEquipamento);
		setTpEquipamento(tpEquipamento);
		setTxtObservacao(txtObservacao);
		setNmMarca(nmMarca);
		setNmModelo(nmModelo);
		setStEquipamento(stEquipamento);
		setCdLogradouro(cdLogradouro);
		setVlLatitude(vlLatitude);
		setVlLongitude(vlLongitude);
		setDtInicial(dtInicial);
		setDtFinal(dtFinal);
		setCdOrgao(cdOrgao);
	}
	public void setCdEquipamento(int cdEquipamento){
		this.cdEquipamento=cdEquipamento;
	}
	public int getCdEquipamento(){
		return this.cdEquipamento;
	}
	public void setNmEquipamento(String nmEquipamento){
		this.nmEquipamento=nmEquipamento;
	}
	public String getNmEquipamento(){
		return this.nmEquipamento;
	}
	public void setIdEquipamento(String idEquipamento){
		this.idEquipamento=idEquipamento;
	}
	public String getIdEquipamento(){
		return this.idEquipamento;
	}
	public void setTpEquipamento(int tpEquipamento){
		this.tpEquipamento=tpEquipamento;
	}
	public int getTpEquipamento(){
		return this.tpEquipamento;
	}
	public void setTxtObservacao(String txtObservacao){
		this.txtObservacao=txtObservacao;
	}
	public String getTxtObservacao(){
		return this.txtObservacao;
	}
	public void setNmMarca(String nmMarca){
		this.nmMarca=nmMarca;
	}
	public String getNmMarca(){
		return this.nmMarca;
	}
	public void setNmModelo(String nmModelo){
		this.nmModelo=nmModelo;
	}
	public String getNmModelo(){
		return this.nmModelo;
	}
	public void setStEquipamento(int stEquipamento){
		this.stEquipamento=stEquipamento;
	}
	public int getStEquipamento(){
		return this.stEquipamento;
	}
	public void setCdLogradouro(int cdLogradouro){
		this.cdLogradouro=cdLogradouro;
	}
	public int getCdLogradouro(){
		return this.cdLogradouro;
	}
	public void setVlLatitude(Double vlLatitude){
		this.vlLatitude=vlLatitude;
	}
	public Double getVlLatitude(){
		return this.vlLatitude;
	}
	public void setVlLongitude(Double vlLongitude){
		this.vlLongitude=vlLongitude;
	}
	public Double getVlLongitude(){
		return this.vlLongitude;
	}
	public void setDtInicial(GregorianCalendar dtInicial){
		this.dtInicial=dtInicial;
	}
	public GregorianCalendar getDtInicial(){
		return this.dtInicial;
	}
	public void setDtFinal(GregorianCalendar dtFinal){
		this.dtFinal=dtFinal;
	}
	public GregorianCalendar getDtFinal(){
		return this.dtFinal;
	}
	public void setCdOrgao(int cdOrgao){
		this.cdOrgao=cdOrgao;
	}
	public int getCdOrgao(){
		return this.cdOrgao;
	}
	public void setNmHost(String nmHost){
		this.nmHost=nmHost;
	}
	public String getNmHost(){
		return this.nmHost;
	}
	public void setNrPort(int nrPort){
		this.nrPort=nrPort;
	}
	public int getNrPort(){
		return this.nrPort;
	}
	public void setNmPwd(String nmPwd){
		this.nmPwd=nmPwd;
	}
	public String getNmPwd(){
		return this.nmPwd;
	}
	public void setNrChannel(int nrChannel){
		this.nrChannel=nrChannel;
	}
	public int getNrChannel(){
		return this.nrChannel;
	}
	public void setNmLogin(String nmLogin){
		this.nmLogin=nmLogin;
	}
	public String getNmLogin(){
		return this.nmLogin;
	}
	public void setNmUrlSnapshot(String nmUrlSnapshot){
		this.nmUrlSnapshot=nmUrlSnapshot;
	}
	public String getNmUrlSnapshot(){
		return this.nmUrlSnapshot;
	}
	public void setNmUrlStream(String nmUrlStream){
		this.nmUrlStream=nmUrlStream;
	}
	public String getNmUrlStream(){
		return this.nmUrlStream;
	}
	public void setDsLocal(String dsLocal){
		this.dsLocal=dsLocal;
	}
	public String getDsLocal(){
		return this.dsLocal;
	}
	public void setLgCriptografia(int lgCriptografia){
		this.lgCriptografia=lgCriptografia;
	}
	public int getLgCriptografia(){
		return this.lgCriptografia;
	}
	public void setLgSyncFtp(int lgSyncFtp){
		this.lgSyncFtp=lgSyncFtp;
	}
	public int getLgSyncFtp(){
		return this.lgSyncFtp;
	}
	public void setDtAfericao(GregorianCalendar dtAfericao){
		this.dtAfericao=dtAfericao;
	}
	public GregorianCalendar getDtAfericao(){
		return this.dtAfericao;
	}
	public void setNrLacre(String nrLacre){
		this.nrLacre=nrLacre;
	}
	public String getNrLacre(){
		return this.nrLacre;
	}
	public void setNrInventarioInmetro(String nrInventarioInmetro){
		this.nrInventarioInmetro=nrInventarioInmetro;
	}
	public String getNrInventarioInmetro(){
		return this.nrInventarioInmetro;
	}
	@Override
	public String toString() {
		String valueToString = "";
		valueToString += "cdEquipamento: " +  getCdEquipamento();
		valueToString += ", nmEquipamento: " +  getNmEquipamento();
		valueToString += ", idEquipamento: " +  getIdEquipamento();
		valueToString += ", tpEquipamento: " +  getTpEquipamento();
		valueToString += ", txtObservacao: " +  getTxtObservacao();
		valueToString += ", nmMarca: " +  getNmMarca();
		valueToString += ", nmModelo: " +  getNmModelo();
		valueToString += ", stEquipamento: " +  getStEquipamento();
		valueToString += ", cdLogradouro: " +  getCdLogradouro();
		valueToString += ", vlLatitude: " +  getVlLatitude();
		valueToString += ", vlLongitude: " +  getVlLongitude();
		valueToString += ", dtInicial: " +  sol.util.Util.formatDateTime(getDtInicial(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtFinal: " +  sol.util.Util.formatDateTime(getDtFinal(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdOrgao: " +  getCdOrgao();
		valueToString += ", nmHost: " +  getNmHost();
		valueToString += ", nrPort: " +  getNrPort();
		valueToString += ", nmPwd: " +  getNmPwd();
		valueToString += ", nrChannel: " +  getNrChannel();
		valueToString += ", nmLogin: " +  getNmLogin();
		valueToString += ", nmUrlSnapshot: " +  getNmUrlSnapshot();
		valueToString += ", nmUrlStream: " +  getNmUrlStream();
		valueToString += ", dsLocal: " +  getDsLocal();
		valueToString += ", lgCriptografia: " +  getLgCriptografia();
		valueToString += ", lgSyncFtp: " +  getLgSyncFtp();
		valueToString += ", dtAfericao: " +  sol.util.Util.formatDateTime(getDtAfericao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrLacre: " +  getNrLacre();
		valueToString += ", nrInventarioInmetro: " +  getNrInventarioInmetro();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new Equipamento(getCdEquipamento(),
			getNmEquipamento(),
			getIdEquipamento(),
			getTpEquipamento(),
			getTxtObservacao(),
			getNmMarca(),
			getNmModelo(),
			getStEquipamento(),
			getCdLogradouro(),
			getVlLatitude(),
			getVlLongitude(),
			getDtInicial()==null ? null : (GregorianCalendar)getDtInicial().clone(),
			getDtFinal()==null ? null : (GregorianCalendar)getDtFinal().clone(),
			getCdOrgao(),
			getNmHost(),
			getNrPort(),
			getNmPwd(),
			getNrChannel(),
			getNmLogin(),
			getNmUrlSnapshot(),
			getNmUrlStream(),
			getDsLocal(),
			getLgCriptografia(),
			getLgSyncFtp(),
			getDtAfericao()==null ? null : (GregorianCalendar)getDtAfericao().clone(),
			getNrLacre(),
			getNrInventarioInmetro());
	}

}