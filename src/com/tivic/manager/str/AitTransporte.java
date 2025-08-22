package com.tivic.manager.str;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class AitTransporte {

	private int cdAit;
	private String nmEmpresa;
	private int cdEmpresa;
	private int cdInfracao;
	private String dsProvidencia;
	private GregorianCalendar dtPrazo;
	private int cdAgente;
	private String nmTestemunha1;
	private String nrRgTestemunha1;
	private String nmEnderecoTestemunha1;
	private String nmTestemunha2;
	private String nrRgTestemunha2;
	private String nmEnderecoTestemunha2;
	private String nmPreposto;
	private String nrAit;
	private String dsObservacao;
	private GregorianCalendar dtInfracao;
	private String nrProcesso1;
	private String nrProcesso2;
	private GregorianCalendar dtProcesso1;
	private GregorianCalendar dtProcesso2;
	private int stProcesso1;
	private int stProcesso2;
	private GregorianCalendar dtNotificacao1;
	private GregorianCalendar dtNotificacao2;
	private GregorianCalendar dtLimite;
	private GregorianCalendar dtEmissaoNip;
	private int nrViaNip;
	private int stAit;
	private int cdPermissionario;
	private int cdMotivo;
	private int cdUsuarioCancelamento;
	private int cdOcorrencia;
	private double vlLongitude;
	private double vlLatitude;
	private int cdCidade;
	private int cdEquipamento;
	private String nmLinhaPrefixo;
	private String nmVeiculoPrefixo;
	private String nrPonto;
	private String dsLocalInfracao;
	private int lgReincidencia;
	private int tpAit;
	private int cdTalao;
	private GregorianCalendar dtEmissao;

	private ArrayList<AitTransporteImagem> imagens;

	public AitTransporte(){ }

	public AitTransporte(int cdAit,
			String nmEmpresa,
			int cdEmpresa,
			int cdInfracao,
			String dsProvidencia,
			GregorianCalendar dtPrazo,
			int cdAgente,
			String nmTestemunha1,
			String nrRgTestemunha1,
			String nmEnderecoTestemunha1,
			String nmTestemunha2,
			String nrRgTestemunha2,
			String nmEnderecoTestemunha2,
			String nmPreposto,
			String nrAit,
			String dsObservacao,
			GregorianCalendar dtInfracao,
			String nrProcesso1,
			String nrProcesso2,
			GregorianCalendar dtProcesso1,
			GregorianCalendar dtProcesso2,
			int stProcesso1,
			int stProcesso2,
			GregorianCalendar dtNotificacao1,
			GregorianCalendar dtNotificacao2,
			GregorianCalendar dtLimite,
			GregorianCalendar dtEmissaoNip,
			int nrViaNip,
			int stAit,
			int cdPermissionario,
			int cdMotivo,
			int cdUsuarioCancelamento,
			int cdOcorrencia,
			double vlLongitude,
			double vlLatitude,
			int cdCidade,
			int cdEquipamento,
			String nmLinhaPrefixo,
			String nmVeiculoPrefixo,
			String nrPonto,
			String dsLocalInfracao,
			int lgReincidencia,
			int tpAit,
			int cdTalao,
			GregorianCalendar dtEmissao){
		setCdAit(cdAit);
		setNmEmpresa(nmEmpresa);
		setCdEmpresa(cdEmpresa);
		setCdInfracao(cdInfracao);
		setDsProvidencia(dsProvidencia);
		setDtPrazo(dtPrazo);
		setCdAgente(cdAgente);
		setNmTestemunha1(nmTestemunha1);
		setNrRgTestemunha1(nrRgTestemunha1);
		setNmEnderecoTestemunha1(nmEnderecoTestemunha1);
		setNmTestemunha2(nmTestemunha2);
		setNrRgTestemunha2(nrRgTestemunha2);
		setNmEnderecoTestemunha2(nmEnderecoTestemunha2);
		setNmPreposto(nmPreposto);
		setNrAit(nrAit);
		setDsObservacao(dsObservacao);
		setDtInfracao(dtInfracao);
		setNrProcesso1(nrProcesso1);
		setNrProcesso2(nrProcesso2);
		setDtProcesso1(dtProcesso1);
		setDtProcesso2(dtProcesso2);
		setStProcesso1(stProcesso1);
		setStProcesso2(stProcesso2);
		setDtNotificacao1(dtNotificacao1);
		setDtNotificacao2(dtNotificacao2);
		setDtLimite(dtLimite);
		setDtEmissaoNip(dtEmissaoNip);
		setNrViaNip(nrViaNip);
		setStAit(stAit);
		setCdPermissionario(cdPermissionario);
		setCdMotivo(cdMotivo);
		setCdUsuarioCancelamento(cdUsuarioCancelamento);
		setCdOcorrencia(cdOcorrencia);
		setVlLongitude(vlLongitude);
		setVlLatitude(vlLatitude);
		setCdCidade(cdCidade);
		setCdEquipamento(cdEquipamento);
		setNmLinhaPrefixo(nmLinhaPrefixo);
		setNmVeiculoPrefixo(nmVeiculoPrefixo);
		setNrPonto(nrPonto);
		setDsLocalInfracao(dsLocalInfracao);
		setLgReincidencia(lgReincidencia);
		setTpAit(tpAit);
		setCdTalao(cdTalao);
		setDtEmissao(dtEmissao);
	}
	public void setCdAit(int cdAit){
		this.cdAit=cdAit;
	}
	public int getCdAit(){
		return this.cdAit;
	}
	public void setNmEmpresa(String nmEmpresa){
		this.nmEmpresa=nmEmpresa;
	}
	public String getNmEmpresa(){
		return this.nmEmpresa;
	}
	public void setCdEmpresa(int cdEmpresa){
		this.cdEmpresa=cdEmpresa;
	}
	public int getCdEmpresa(){
		return this.cdEmpresa;
	}
	public void setCdInfracao(int cdInfracao){
		this.cdInfracao=cdInfracao;
	}
	public int getCdInfracao(){
		return this.cdInfracao;
	}
	public void setDsProvidencia(String dsProvidencia){
		this.dsProvidencia=dsProvidencia;
	}
	public String getDsProvidencia(){
		return this.dsProvidencia;
	}
	public void setDtPrazo(GregorianCalendar dtPrazo){
		this.dtPrazo=dtPrazo;
	}
	public GregorianCalendar getDtPrazo(){
		return this.dtPrazo;
	}
	public void setDtEmissao(GregorianCalendar dtEmissao){
		this.dtEmissao=dtEmissao;
	}
	public GregorianCalendar getDtEmissao(){
		return this.dtEmissao;
	}	
	public void setCdAgente(int cdAgente){
		this.cdAgente=cdAgente;
	}
	public int getCdAgente(){
		return this.cdAgente;
	}
	public void setNmTestemunha1(String nmTestemunha1){
		this.nmTestemunha1=nmTestemunha1;
	}
	public String getNmTestemunha1(){
		return this.nmTestemunha1;
	}
	public void setNrRgTestemunha1(String nrRgTestemunha1){
		this.nrRgTestemunha1=nrRgTestemunha1;
	}
	public String getNrRgTestemunha1(){
		return this.nrRgTestemunha1;
	}
	public void setNmEnderecoTestemunha1(String nmEnderecoTestemunha1){
		this.nmEnderecoTestemunha1=nmEnderecoTestemunha1;
	}
	public String getNmEnderecoTestemunha1(){
		return this.nmEnderecoTestemunha1;
	}
	public void setNmTestemunha2(String nmTestemunha2){
		this.nmTestemunha2=nmTestemunha2;
	}
	public String getNmTestemunha2(){
		return this.nmTestemunha2;
	}
	public void setNrRgTestemunha2(String nrRgTestemunha2){
		this.nrRgTestemunha2=nrRgTestemunha2;
	}
	public String getNrRgTestemunha2(){
		return this.nrRgTestemunha2;
	}
	public void setNmEnderecoTestemunha2(String nmEnderecoTestemunha2){
		this.nmEnderecoTestemunha2=nmEnderecoTestemunha2;
	}
	public String getNmEnderecoTestemunha2(){
		return this.nmEnderecoTestemunha2;
	}
	public void setNmPreposto(String nmPreposto){
		this.nmPreposto=nmPreposto;
	}
	public String getNmPreposto(){
		return this.nmPreposto;
	}
	public void setNrAit(String nrAit){
		this.nrAit=nrAit;
	}
	public String getNrAit(){
		return this.nrAit;
	}
	public void setDsObservacao(String dsObservacao){
		this.dsObservacao=dsObservacao;
	}
	public String getDsObservacao(){
		return this.dsObservacao;
	}
	public void setDtInfracao(GregorianCalendar dtInfracao){
		this.dtInfracao=dtInfracao;
	}
	public GregorianCalendar getDtInfracao(){
		return this.dtInfracao;
	}
	public void setNrProcesso1(String nrProcesso1){
		this.nrProcesso1=nrProcesso1;
	}
	public String getNrProcesso1(){
		return this.nrProcesso1;
	}
	public void setNrProcesso2(String nrProcesso2){
		this.nrProcesso2=nrProcesso2;
	}
	public String getNrProcesso2(){
		return this.nrProcesso2;
	}
	public void setDtProcesso1(GregorianCalendar dtProcesso1){
		this.dtProcesso1=dtProcesso1;
	}
	public GregorianCalendar getDtProcesso1(){
		return this.dtProcesso1;
	}
	public void setDtProcesso2(GregorianCalendar dtProcesso2){
		this.dtProcesso2=dtProcesso2;
	}
	public GregorianCalendar getDtProcesso2(){
		return this.dtProcesso2;
	}
	public void setStProcesso1(int stProcesso1){
		this.stProcesso1=stProcesso1;
	}
	public int getStProcesso1(){
		return this.stProcesso1;
	}
	public void setStProcesso2(int stProcesso2){
		this.stProcesso2=stProcesso2;
	}
	public int getStProcesso2(){
		return this.stProcesso2;
	}
	public void setDtNotificacao1(GregorianCalendar dtNotificacao1){
		this.dtNotificacao1=dtNotificacao1;
	}
	public GregorianCalendar getDtNotificacao1(){
		return this.dtNotificacao1;
	}
	public void setDtNotificacao2(GregorianCalendar dtNotificacao2){
		this.dtNotificacao2=dtNotificacao2;
	}
	public GregorianCalendar getDtNotificacao2(){
		return this.dtNotificacao2;
	}
	public void setDtLimite(GregorianCalendar dtLimite){
		this.dtLimite=dtLimite;
	}
	public GregorianCalendar getDtLimite(){
		return this.dtLimite;
	}
	public void setDtEmissaoNip(GregorianCalendar dtEmissaoNip){
		this.dtEmissaoNip=dtEmissaoNip;
	}
	public GregorianCalendar getDtEmissaoNip(){
		return this.dtEmissaoNip;
	}
	public void setNrViaNip(int nrViaNip){
		this.nrViaNip=nrViaNip;
	}
	public int getNrViaNip(){
		return this.nrViaNip;
	}
	public void setStAit(int stAit){
		this.stAit=stAit;
	}
	public int getStAit(){
		return this.stAit;
	}
	public void setCdPermissionario(int cdPermissionario){
		this.cdPermissionario=cdPermissionario;
	}
	public int getCdPermissionario(){
		return this.cdPermissionario;
	}
	public void setCdMotivo(int cdMotivo){
		this.cdMotivo=cdMotivo;
	}
	public int getCdMotivo(){
		return this.cdMotivo;
	}
	public void setCdUsuarioCancelamento(int cdUsuarioCancelamento){
		this.cdUsuarioCancelamento=cdUsuarioCancelamento;
	}
	public int getCdUsuarioCancelamento(){
		return this.cdUsuarioCancelamento;
	}
	public void setCdOcorrencia(int cdOcorrencia){
		this.cdOcorrencia=cdOcorrencia;
	}
	public int getCdOcorrencia(){
		return this.cdOcorrencia;
	}
	public void setVlLongitude(double vlLongitude){
		this.vlLongitude=vlLongitude;
	}
	public double getVlLongitude(){
		return this.vlLongitude;
	}
	public void setVlLatitude(double vlLatitude){
		this.vlLatitude=vlLatitude;
	}
	public double getVlLatitude(){
		return this.vlLatitude;
	}
	public void setCdCidade(int cdCidade){
		this.cdCidade=cdCidade;
	}
	public int getCdCidade(){
		return this.cdCidade;
	}
	public void setCdEquipamento(int cdEquipamento){
		this.cdEquipamento=cdEquipamento;
	}
	public int getCdEquipamento(){
		return this.cdEquipamento;
	}
	public void setNmLinhaPrefixo(String nmLinhaPrefixo){
		this.nmLinhaPrefixo=nmLinhaPrefixo;
	}
	public String getNmLinhaPrefixo(){
		return this.nmLinhaPrefixo;
	}
	public void setNmVeiculoPrefixo(String nmVeiculoPrefixo){
		this.nmVeiculoPrefixo=nmVeiculoPrefixo;
	}
	public String getNmVeiculoPrefixo(){
		return this.nmVeiculoPrefixo;
	}
	public void setNrPonto(String nrPonto){
		this.nrPonto=nrPonto;
	}
	public String getNrPonto(){
		return this.nrPonto;
	}
	public void setDsLocalInfracao(String dsLocalInfracao){
		this.dsLocalInfracao=dsLocalInfracao;
	}
	public String getDsLocalInfracao(){
		return this.dsLocalInfracao;
	}
	public void setLgReincidencia(int lgReincidencia){
		this.lgReincidencia=lgReincidencia;
	}
	public int getLgReincidencia(){
		return this.lgReincidencia;
	}
	public void setTpAit(int tpAit){
		this.tpAit=tpAit;
	}
	public int getTpAit(){
		return this.tpAit;
	}
	public void setCdTalao(int cdTalao){
		this.cdTalao=cdTalao;
	}
	public int getCdTalao(){
		return this.cdTalao;
	}

	public ArrayList<AitTransporteImagem> getImagens() {
		return imagens;
	}
	public void setImagens(ArrayList<AitTransporteImagem> imagens) {
		this.imagens = imagens;
	}
	
	public String toString() {
		String valueToString = "";
		valueToString += "cdAit: " +  getCdAit();
		valueToString += ", nmEmpresa: " +  getNmEmpresa();
		valueToString += ", cdEmpresa: " +  getCdEmpresa();
		valueToString += ", cdInfracao: " +  getCdInfracao();
		valueToString += ", dsProvidencia: " +  getDsProvidencia();
		valueToString += ", dtPrazo: " +  sol.util.Util.formatDateTime(getDtPrazo(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", cdAgente: " +  getCdAgente();
		valueToString += ", nmTestemunha1: " +  getNmTestemunha1();
		valueToString += ", nrRgTestemunha1: " +  getNrRgTestemunha1();
		valueToString += ", nmEnderecoTestemunha1: " +  getNmEnderecoTestemunha1();
		valueToString += ", nmTestemunha2: " +  getNmTestemunha2();
		valueToString += ", nrRgTestemunha2: " +  getNrRgTestemunha2();
		valueToString += ", nmEnderecoTestemunha2: " +  getNmEnderecoTestemunha2();
		valueToString += ", nmPreposto: " +  getNmPreposto();
		valueToString += ", nrAit: " +  getNrAit();
		valueToString += ", dsObservacao: " +  getDsObservacao();
		valueToString += ", dtInfracao: " +  sol.util.Util.formatDateTime(getDtInfracao(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrProcesso1: " +  getNrProcesso1();
		valueToString += ", nrProcesso2: " +  getNrProcesso2();
		valueToString += ", dtProcesso1: " +  sol.util.Util.formatDateTime(getDtProcesso1(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtProcesso2: " +  sol.util.Util.formatDateTime(getDtProcesso2(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", stProcesso1: " +  getStProcesso1();
		valueToString += ", stProcesso2: " +  getStProcesso2();
		valueToString += ", dtNotificacao1: " +  sol.util.Util.formatDateTime(getDtNotificacao1(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtNotificacao2: " +  sol.util.Util.formatDateTime(getDtNotificacao2(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtLimite: " +  sol.util.Util.formatDateTime(getDtLimite(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", dtEmissaoNip: " +  sol.util.Util.formatDateTime(getDtEmissaoNip(), "dd/MM/yyyy HH:mm:ss:SSS", "");
		valueToString += ", nrViaNip: " +  getNrViaNip();
		valueToString += ", stAit: " +  getStAit();
		valueToString += ", cdPermissionario: " +  getCdPermissionario();
		valueToString += ", cdMotivo: " +  getCdMotivo();
		valueToString += ", cdUsuarioCancelamento: " +  getCdUsuarioCancelamento();
		valueToString += ", cdOcorrencia: " +  getCdOcorrencia();
		valueToString += ", vlLongitude: " +  getVlLongitude();
		valueToString += ", vlLatitude: " +  getVlLatitude();
		valueToString += ", cdCidade: " +  getCdCidade();
		valueToString += ", cdEquipamento: " +  getCdEquipamento();
		valueToString += ", nmLinhaPrefixo: " +  getNmLinhaPrefixo();
		valueToString += ", nmVeiculoPrefixo: " +  getNmVeiculoPrefixo();
		valueToString += ", nrPonto: " +  getNrPonto();
		valueToString += ", dsLocalInfracao: " +  getDsLocalInfracao();
		valueToString += ", lgReincidencia: " +  getLgReincidencia();
		valueToString += ", tpAit: " +  getTpAit();
		valueToString += ", cdTalao: " +  getCdTalao();
		valueToString += ", dtEmissao: " +  getDtEmissao();
		return "{" + valueToString + "}";
	}

	public Object clone() {
		return new AitTransporte(getCdAit(),
			getNmEmpresa(),
			getCdEmpresa(),
			getCdInfracao(),
			getDsProvidencia(),
			getDtPrazo()==null ? null : (GregorianCalendar)getDtPrazo().clone(),
			getCdAgente(),
			getNmTestemunha1(),
			getNrRgTestemunha1(),
			getNmEnderecoTestemunha1(),
			getNmTestemunha2(),
			getNrRgTestemunha2(),
			getNmEnderecoTestemunha2(),
			getNmPreposto(),
			getNrAit(),
			getDsObservacao(),
			getDtInfracao()==null ? null : (GregorianCalendar)getDtInfracao().clone(),
			getNrProcesso1(),
			getNrProcesso2(),
			getDtProcesso1()==null ? null : (GregorianCalendar)getDtProcesso1().clone(),
			getDtProcesso2()==null ? null : (GregorianCalendar)getDtProcesso2().clone(),
			getStProcesso1(),
			getStProcesso2(),
			getDtNotificacao1()==null ? null : (GregorianCalendar)getDtNotificacao1().clone(),
			getDtNotificacao2()==null ? null : (GregorianCalendar)getDtNotificacao2().clone(),
			getDtLimite()==null ? null : (GregorianCalendar)getDtLimite().clone(),
			getDtEmissaoNip()==null ? null : (GregorianCalendar)getDtEmissaoNip().clone(),
			getNrViaNip(),
			getStAit(),
			getCdPermissionario(),
			getCdMotivo(),
			getCdUsuarioCancelamento(),
			getCdOcorrencia(),
			getVlLongitude(),
			getVlLatitude(),
			getCdCidade(),
			getCdEquipamento(),
			getNmLinhaPrefixo(),
			getNmVeiculoPrefixo(),
			getNrPonto(),
			getDsLocalInfracao(),
			getLgReincidencia(),
			getTpAit(),
			getCdTalao(),
			getDtEmissao()==null ? null : (GregorianCalendar)getDtEmissao().clone());
	}

}