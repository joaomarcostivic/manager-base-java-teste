package com.tivic.manager.fix.ptc.protocoloexterno;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.tivic.manager.grl.FormularioAtributoValor;
import com.tivic.manager.ptc.protocolosv3.externo.ProtocoloExternoDTO;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;

public class FixProtocoloExterno {

	public static final int NR_CNH_CONDUTOR = 1;
	public static final int UF_CNH_CONDUTOR = 2;
	public static final int NM_REQUERENTE = 3;
	public static final int TP_CNH_CONDUTOR = 4;
	public static final int TP_REQUERENTE = 5;
	public static final int DS_ENDERECO_REQUERENTE = 6;
	public static final int NR_ENDERECO_REQUERENTE = 7;
	public static final int DS_COMPLEMENTO_ENDERECO_REQUERENTE = 8;
	public static final int NM_CIDADE_REQUERENTE = 9;
	public static final int NR_TELEFONE_1_REQUERENTE = 10;
	public static final int NR_TELEFONE_2_REQUERENTE = 11;
	public static final int NR_CPF_REQUERENTE = 12;
	public static final int NR_CNPJ_REQUERENTE = 13;
	public static final int NR_RG_REQUERENTE = 14;
	public static final int NR_CNH_REQUERENTE = 15;
	public static final int UF_CNH_REQUERENTE = 16;
	public static final int NM_CONDUTOR = 17;
	public static final int DS_ENDERECO_CONDUTOR = 18;
	public static final int NR_ENDERECO_CONDUTOR = 19;
	public static final int DS_COMPLEMENTO_ENDERECO_CONDUTOR = 20;
	public static final int NM_CIDADE_CONDUTOR = 21;
	public static final int NR_TELEFONE_1_CONDUTOR = 22;
	public static final int NR_TELEFONE_2_CONDUTOR = 23;
	public static final int NR_CPF_CONDUTOR = 24;
	public static final int NR_CNPJ_CONDUTOR = 25;
	public static final int NR_RG_CONDUTOR = 26;
	public static final int TXT_REQUISICAO = 27;
	public static final int MODELO_CNH_CONDUTOR = 28;
	public static final int PAIS_CNH_CONDUTOR = 29;
	public static final int ID_AIT = 30;
	public static final int NR_CONTROLE = 31;
	public static final int CD_INFRACAO = 32;
	public static final int LG_EQUIPAMENTO_ELETRONICO = 33;
	public static final int NR_MATRICULA_AGENTE = 34;
	public static final int DS_LOCAL_INFRACAO = 35;
	public static final int DT_INFRACAO = 36;
	public static final int NR_RENAINF = 37;
	public static final int NR_PLACA = 38;
	public static final int CD_COR = 39;
	public static final int SG_UF_VEICULO = 40;
	public static final int CD_MARCA_MODELO = 41;
	public static final int CD_TIPO_DOCUMENTO = 42;
	public static final int CD_FASE = 43;
	public static final int NR_DOCUMENTO = 44;
	public static final int DT_PROTOCOLO = 45;
	public static final int NM_REQUERENTE_EXTERNO = 46;
	public static final int DS_REQUISICAO = 47;
	public static final int NM_CONDUTOR_EXTERNO = 48;
	public static final int NR_RG_CONDUTOR_EXTERNO = 49;
	public static final int NR_CPF_CONDUTOR_EXTERNO = 50;
	public static final int NR_CNPJ_CONDUTOR_EXTERNO = 51;
	public static final int NR_CNH_CONDUTOR_EXTERNO = 52;
	public static final int SG_UF_CNH_CONDUTOR_EXTERNO = 53;
	public static final int TP_CATEGORIA_CNH_CONDUTOR_EXTERNO = 54;
	public static final int MODELO_CNH_CONDUTOR_EXTERNO = 55;
	public static final int PAIS_CNH_CONDUTOR_EXTERNO = 56;
	public static final int NR_TELEFONE_CONDUTOR = 57;
	public static final int DS_ENDERECO_CONDUTOR_EXTERNO = 58;
	public static final int NR_ENDERECO_CONDUTOR_EXTERNO = 59;
	public static final int DS_COMPLEMENTO_ENDERECO_CONDUTOR_EXTERNO = 60;
	public static final int NM_CIDADE_CONDUTOR_EXTERNO = 61;
	public static final int DT_ENTRADA_RECURSO = 62;
	public static final int CD_OCORRENCIA = 63;

	public static void fixProtocoloExterno(Integer a) {
		try {
			CustomConnection customConnection = new CustomConnection();
			SearchCriterios searchCriterios = new SearchCriterios();
			List<FormularioAtributoValor> cdDocumento = listCdDocumento(customConnection);

			for (FormularioAtributoValor valor : cdDocumento) {
				searchCriterios.addCriteriosEqualInteger("cd_documento", valor.getCdDocumento(),
						valor.getCdDocumento() > 0);
				List<FormularioAtributoValor> listAtributoValor = searchAtributoValor(searchCriterios, customConnection);
				searchCriterios.getAndRemoveCriterio("cd_documento");

				createObject(listAtributoValor, customConnection);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static List<FormularioAtributoValor> listCdDocumento(CustomConnection customConnection) throws Exception {
		try {
			SearchCriterios searchCriterios = new SearchCriterios();
			customConnection.initConnection(false);
			Search<FormularioAtributoValor> search = new SearchBuilder<FormularioAtributoValor>("grl_formulario_atributo_valor")
					.fields("DISTINCT cd_documento")
					.searchCriterios(searchCriterios)
					.orderBy("cd_documento")		
					.build();
			List<FormularioAtributoValor> list = search.getList(FormularioAtributoValor.class);
			customConnection.finishConnection();
			return list;
		} finally {
			customConnection.finishConnection();
		}
	}

	private static List<FormularioAtributoValor> searchAtributoValor(SearchCriterios searchCriterios,
			CustomConnection customConnection) throws Exception {
		try {
			customConnection.initConnection(false);
			Search<FormularioAtributoValor> search = new SearchBuilder<FormularioAtributoValor>("grl_formulario_atributo_valor")
					.searchCriterios(searchCriterios)
					.build();
			List<FormularioAtributoValor> list = search.getList(FormularioAtributoValor.class);
			customConnection.finishConnection();
			return list;
		} finally {
			customConnection.finishConnection();
		}
	}

	private static void createObject(List<FormularioAtributoValor> listAtributoValor, CustomConnection customConnection) throws Exception {

		ProtocoloExternoDTO protocoloExternoDTO = new ProtocoloExternoDTO();

		for (FormularioAtributoValor atributoValor : listAtributoValor) {
			int cdFormularioAtributo = atributoValor.getCdFormularioAtributo();
			protocoloExternoDTO.setCdDocumento(atributoValor.getCdDocumento());
			switch (cdFormularioAtributo) {
				case FixProtocoloExterno.ID_AIT:
					protocoloExternoDTO.setIdAit(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.NR_PLACA:
					protocoloExternoDTO.setNrPlaca(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.NR_RENAINF:
					protocoloExternoDTO.setNrRenainf(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.NM_REQUERENTE_EXTERNO:
					protocoloExternoDTO.setNmRequerente(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.CD_INFRACAO:
					protocoloExternoDTO.setCdInfracao(formatInt(atributoValor.getTxtAtributoValor()));
					break;
				case FixProtocoloExterno.CD_TIPO_DOCUMENTO:
					protocoloExternoDTO.setCdTipoDocumento(formatInt(atributoValor.getTxtAtributoValor()));
					break;
				case FixProtocoloExterno.NR_DOCUMENTO:
					protocoloExternoDTO.setNrDocumento(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.NM_REQUERENTE:
					protocoloExternoDTO.setNmRequerente(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.DS_REQUISICAO:
					protocoloExternoDTO.setTxtObservacao(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.TXT_REQUISICAO:
					protocoloExternoDTO.setTxtObservacao(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.DT_ENTRADA_RECURSO:
					protocoloExternoDTO.setDtEntrada(formatDate(atributoValor.getTxtAtributoValor()));
					break;
				case FixProtocoloExterno.DT_PROTOCOLO:
					protocoloExternoDTO.setDtProtocolo(formatDate(atributoValor.getTxtAtributoValor()));
					break;
				case FixProtocoloExterno.NM_CONDUTOR:
					protocoloExternoDTO.setNmCondutor(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.NM_CONDUTOR_EXTERNO:
					protocoloExternoDTO.setNmCondutor(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.NR_RG_CONDUTOR:
					protocoloExternoDTO.setNrRg(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.NR_RG_CONDUTOR_EXTERNO:
					protocoloExternoDTO.setNrRg(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.NR_CPF_CONDUTOR:
					if(atributoValor.getTxtAtributoValor() != null && !atributoValor.getTxtAtributoValor().isEmpty())
						protocoloExternoDTO.setNrCpfCnpj(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.NR_CPF_CONDUTOR_EXTERNO:
					if(atributoValor.getTxtAtributoValor() != null && !atributoValor.getTxtAtributoValor().isEmpty())
						protocoloExternoDTO.setNrCpfCnpj(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.NR_CNPJ_CONDUTOR:
					if(atributoValor.getTxtAtributoValor() != null && !atributoValor.getTxtAtributoValor().isEmpty())
						protocoloExternoDTO.setNrCpfCnpj(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.NR_CNPJ_CONDUTOR_EXTERNO:
					if(atributoValor.getTxtAtributoValor() != null && !atributoValor.getTxtAtributoValor().isEmpty())
						protocoloExternoDTO.setNrCpfCnpj(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.NR_CNH_CONDUTOR:
					protocoloExternoDTO.setNrCnh(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.NR_CNH_CONDUTOR_EXTERNO:
					protocoloExternoDTO.setNrCnh(atributoValor.getTxtAtributoValor());
					break;	
				case FixProtocoloExterno.SG_UF_CNH_CONDUTOR_EXTERNO:
					protocoloExternoDTO.setUfCnh(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.TP_CATEGORIA_CNH_CONDUTOR_EXTERNO:
					protocoloExternoDTO.setTpCategoriaCnh(formatInt(atributoValor.getTxtAtributoValor()));
					break;
				case FixProtocoloExterno.MODELO_CNH_CONDUTOR:
					protocoloExternoDTO.setTpModeloCnh(formatInt(atributoValor.getTxtAtributoValor()));
					break;
				case FixProtocoloExterno.MODELO_CNH_CONDUTOR_EXTERNO:
					protocoloExternoDTO.setTpModeloCnh(formatInt(atributoValor.getTxtAtributoValor()));
					break;
				case FixProtocoloExterno.PAIS_CNH_CONDUTOR:
					protocoloExternoDTO.setIdPaisCnh(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.PAIS_CNH_CONDUTOR_EXTERNO:
					protocoloExternoDTO.setIdPaisCnh(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.NR_TELEFONE_CONDUTOR:
					protocoloExternoDTO.setNrTelefone1(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.DS_ENDERECO_CONDUTOR:
					protocoloExternoDTO.setDsEndereco(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.DS_ENDERECO_CONDUTOR_EXTERNO:
					protocoloExternoDTO.setDsEndereco(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.NR_ENDERECO_CONDUTOR:
					protocoloExternoDTO.setNrEndereco(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.NR_ENDERECO_CONDUTOR_EXTERNO:
					protocoloExternoDTO.setNrEndereco(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.DS_COMPLEMENTO_ENDERECO_CONDUTOR:
					protocoloExternoDTO.setDsComplementoEndereco(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.DS_COMPLEMENTO_ENDERECO_CONDUTOR_EXTERNO:
					protocoloExternoDTO.setDsComplementoEndereco(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.NM_CIDADE_CONDUTOR:
					protocoloExternoDTO.setNmCidade(atributoValor.getTxtAtributoValor());
					break;
				case FixProtocoloExterno.NM_CIDADE_CONDUTOR_EXTERNO:
					protocoloExternoDTO.setNmCidade(atributoValor.getTxtAtributoValor());
					break;
			}
		}

		System.out.println("-----------------------------------------------" + protocoloExternoDTO.getCdDocumento() + "---------------------------------------------------------------");
		System.out.println("protocoloExternoDTO: " + protocoloExternoDTO);
		FixProtocoloExternoSave fixProtocoloExternoSave = new FixProtocoloExternoSave();
		fixProtocoloExternoSave.saveProtocolo(protocoloExternoDTO, customConnection);
	}

	private static int formatInt(String valor) {
		if(valor != null && !valor.isEmpty()) {
			return Integer.parseInt(valor);
		}
		return 0;
	}
	
	private static GregorianCalendar formatDate(String date) {
		try {
			if(date != null && !date.isEmpty()) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date data = sdf.parse(date);
	
				GregorianCalendar gregorianCalendar = new GregorianCalendar();
				gregorianCalendar.setTime(data);
				return gregorianCalendar;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
