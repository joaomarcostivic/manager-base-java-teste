package com.tivic.manager.wsdl.detran.mg.recursofici.externo;

import java.util.GregorianCalendar;

import com.tivic.manager.grl.EstadoDAO;
import com.tivic.manager.grl.FormularioAtributoValorServices;
import com.tivic.manager.mob.Ait;
import com.tivic.manager.mob.AitMovimento;
import com.tivic.manager.mob.Infracao;
import com.tivic.manager.mob.InfracaoDAO;
import com.tivic.manager.mob.Ocorrencia;
import com.tivic.manager.mob.OcorrenciaDAO;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.ptc.Documento;
import com.tivic.manager.ptc.FaseServices;
import com.tivic.manager.util.Util;
import com.tivic.manager.wsdl.ProtocoloExternoDetranObject;
import com.tivic.manager.wsdl.detran.mg.TabelasAuxiliaresMG;
import com.tivic.manager.wsdl.detran.mg.recursofici.RecursoFiciDadosEntrada;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.manager.wsdl.interfaces.TabelasAuxiliares;

public class IncluirFiciExternoDadosEntradaFactory {

	private static TabelasAuxiliares tabelasAuxiliares = new TabelasAuxiliaresMG();

	public static RecursoFiciDadosEntrada fazerDadoEntrada(ProtocoloExternoDetranObject object, boolean lgHomologacao) throws ValidacaoException{
		Ait ait = object.getAit();
		AitMovimento aitMovimento = object.getAitMovimento();
		Documento documento = object.getDocumento();
		Infracao infracao = InfracaoDAO.get(ait.getCdInfracao());
		if(infracao == null)
			throw new ValidacaoException("Movimento sem infração");
		Orgao orgao = OrgaoServices.getOrgaoUnico();	
		Ocorrencia ocorrencia = OcorrenciaDAO.get(ait.getCdOcorrencia());
		if(ocorrencia == null)
			throw new ValidacaoException("Movimento sem ocorrência");

		RecursoFiciDadosEntrada incluirFiciDadosEntrada = new RecursoFiciDadosEntrada();
		incluirFiciDadosEntrada.setOrigemSolicitacao(3);//Prefeitura
		incluirFiciDadosEntrada.setOrgao(lgHomologacao ? 253710 : orgao.getCdOrgao());
		incluirFiciDadosEntrada.setPlaca(ait.getNrPlaca());
		incluirFiciDadosEntrada.setAit(ait.getIdAit());
		incluirFiciDadosEntrada.setNumeroProcessamento(formatNrProcesso(documento.getNrDocumento()));
		incluirFiciDadosEntrada.setCodigoRenainf(String.valueOf(ait.getNrRenainf()));
		incluirFiciDadosEntrada.setCodigoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(0, 4));
		incluirFiciDadosEntrada.setCodigoDesdobramentoInfracao(String.valueOf(infracao.getNrCodDetran()).substring(4));
		incluirFiciDadosEntrada.setCodigoMovimentacao(1);//Inclusão
		incluirCnh(documento, incluirFiciDadosEntrada);
		incluirCondutor(documento, incluirFiciDadosEntrada);
		incluirFiciDadosEntrada.setNumeroProtocolo(object.getDocumento().getNrDocumento());
		incluirFiciDadosEntrada.setParecerFici(setParecer(documento));
		incluirFiciDadosEntrada.setDataParecerFici(new GregorianCalendar());
		incluirFiciDadosEntrada.setCodigoParecerFici(Util.fillNum(ocorrencia.getIdMovimentacao(), 3));
		incluirFiciDadosEntrada.setDataProtocolo(new GregorianCalendar());
		incluirFiciDadosEntrada.setNmServico("INCLUIR FICI EXTERNO");
		return incluirFiciDadosEntrada;
	}

	private static void incluirCnh(Documento documento, RecursoFiciDadosEntrada incluirFiciDadosEntrada) throws ValidacaoException{		
		System.out.println(FormularioAtributoValorServices.getValorByDocumentoAtributo(documento.getCdDocumento(), "modelCnhCondutor") + " " + FormularioAtributoValorServices.getValorByDocumentoAtributo(documento.getCdDocumento(), "modelCnhCondutor").toString());
		if(FormularioAtributoValorServices.getValorByDocumentoAtributo(documento.getCdDocumento(), "modelCnhCondutor") != null) {
			if(FormularioAtributoValorServices.getValorByDocumentoAtributo(documento.getCdDocumento(), "modelCnhCondutor").toString() != "")            
				incluirFiciDadosEntrada.setModeloCnh(tabelasAuxiliares.getTipoCnh(Integer.parseInt(isParamNull(FormularioAtributoValorServices.getValorByDocumentoAtributo(documento.getCdDocumento(), "modelCnhCondutor")))));
			else
				incluirFiciDadosEntrada.setModeloCnh(2);

			if(incluirFiciDadosEntrada.getModeloCnh() == 3) {
				if(FormularioAtributoValorServices.getValorByDocumentoAtributo(documento.getCdDocumento(), "paisCnhCondutor") != null && FormularioAtributoValorServices.getValorByDocumentoAtributo(documento.getCdDocumento(), "paisCnhCondutor") != "")
					incluirFiciDadosEntrada.setCodigoPaisCnh(Integer.parseInt(FormularioAtributoValorServices.getValorByDocumentoAtributo(documento.getCdDocumento(), "paisCnhCondutor").toString()));

				incluirFiciDadosEntrada.setUfCnh("EX");
			} else {
				incluirFiciDadosEntrada.setUfCnh(EstadoDAO.get(Integer.parseInt(FormularioAtributoValorServices.getValorByDocumentoAtributo(documento.getCdDocumento(), "sgUfCnhCondutor").toString())).getSgEstado());
				incluirFiciDadosEntrada.setNumeroCnh(isParamNull(FormularioAtributoValorServices.getValorByDocumentoAtributo(documento.getCdDocumento(), "nrCnhCondutor")));
			}
		}		
	}

	private static void incluirCondutor(Documento documento, RecursoFiciDadosEntrada incluirFiciDadosEntrada) throws ValidacaoException{
		incluirFiciDadosEntrada.setNomeCondutor(FormularioAtributoValorServices.getValorByDocumentoAtributo(documento.getCdDocumento(), "nmCondutor").toString());

		if(FormularioAtributoValorServices.getValorByDocumentoAtributo(documento.getCdDocumento(), "nrRgCondutor") != null && FormularioAtributoValorServices.getValorByDocumentoAtributo(documento.getCdDocumento(), "nrRgCondutor") != "")
			incluirFiciDadosEntrada.setRgCondutor(FormularioAtributoValorServices.getValorByDocumentoAtributo(documento.getCdDocumento(), "nrRgCondutor").toString());

		if(FormularioAtributoValorServices.getValorByDocumentoAtributo(documento.getCdDocumento(), "nrCpfCondutor") != null && FormularioAtributoValorServices.getValorByDocumentoAtributo(documento.getCdDocumento(), "nrCpfCondutor") != "")
			incluirFiciDadosEntrada.setCpfCondutor(FormularioAtributoValorServices.getValorByDocumentoAtributo(documento.getCdDocumento(), "nrCpfCondutor").toString().replaceAll("-", "").replaceAll(".", ""));

	}

	private static String formatNrProcesso(String entry) throws ValidacaoException {
		String num = entry.replaceAll("/", "");

		if(!Util.isNumber(num))
			throw new ValidacaoException("Nº do protocolo é inválido");

		if(entry.length() <= 16)
			return Util.fillLong(Long.parseLong(num), 16);
		else
			return entry;
	}

	private static String setParecer (Documento _documento) {		
		int cdDeferido = FaseServices.getCdFaseByNome("Deferido", null);
		int cdIndeferido = FaseServices.getCdFaseByNome("Indeferido", null);

		if(_documento.getCdFase() == cdDeferido)
			return "A";

		if(_documento.getCdFase() == cdIndeferido)
			return "R";

		return "";
	}

	private static String isParamNull(Object param) {
		if (param != null)
			return param.toString();
		else
			return null;
	}

}
