package com.tivic.manager.ptc.portal.jari.cartajulgamento;

import java.util.List;
import javax.ws.rs.BadRequestException;
import com.tivic.manager.grl.Cidade;
import com.tivic.manager.grl.cidade.CidadeRepository;
import com.tivic.manager.grl.estado.EstadoRepository;
import com.tivic.manager.grl.parametro.repository.IParametroRepository;
import com.tivic.manager.mob.Orgao;
import com.tivic.manager.mob.OrgaoServices;
import com.tivic.manager.mob.TipoStatusEnum;
import com.tivic.manager.ptc.protocolos.julgamento.DadosCartaJulgamento;
import com.tivic.manager.wsdl.exceptions.ValidacaoException;
import com.tivic.sol.cdi.BeansFactory;
import com.tivic.sol.connection.CustomConnection;
import com.tivic.sol.report.Report;
import com.tivic.sol.report.ReportBuilder;
import com.tivic.sol.report.ReportCriterios;
import com.tivic.sol.search.Search;
import com.tivic.sol.search.SearchBuilder;
import com.tivic.sol.search.SearchCriterios;
import com.tivic.sol.util.date.DateUtil;

public class GeraCartaJulgamentoJari implements IGeraCartaJulgamento{

	private CustomConnection customConnection;
	private CidadeRepository cidadeRepository;
	private EstadoRepository estadoRepository;
	private IParametroRepository parametroRepository;
	
	public GeraCartaJulgamentoJari() throws Exception {
		this.cidadeRepository = (CidadeRepository) BeansFactory.get(CidadeRepository.class);
		this.estadoRepository = (EstadoRepository) BeansFactory.get(EstadoRepository.class);
		this.parametroRepository = (IParametroRepository) BeansFactory.get(IParametroRepository.class);
	}
	
	@Override
	public byte[] gerar(int cdAit, int tpStatus, CustomConnection customConnection) throws Exception, ValidacaoException {
		this.customConnection = customConnection;
		ReportCriterios reportCriterios = new BuscaParametrosCartaJulgamento().buscar(customConnection);
		Report reportDocumentoJariIndeferida = findDocumentoJariIndeferida(cdAit, tpStatus, reportCriterios);
		byte[] content = reportDocumentoJariIndeferida.getReportPdf("ptc/carta_julgamento");
		customConnection.finishConnection();
		return content;
	}
	
	private Report findDocumentoJariIndeferida(int cdAit, int tpStatus, ReportCriterios reportCriterios) throws Exception {
		SearchCriterios searchCriterios = montarCriterios(cdAit);
		List<DadosCartaJulgamento> listDadosJulgamento = searchJariJulgamento(searchCriterios, tpStatus, this.customConnection).getList(DadosCartaJulgamento.class);
		if (listDadosJulgamento.isEmpty())
			throw new BadRequestException("O resultado deste julgamento não foi encontrado.");
		montarDadosDocumento(tpStatus, listDadosJulgamento, reportCriterios);
		Report report = new ReportBuilder()
				.list(listDadosJulgamento)
				.reportCriterios(reportCriterios)
				.build();
		return report;
	}
	
	private void montarDadosDocumento(int tpStatus, List<DadosCartaJulgamento> listDadosCartaJulgamento, ReportCriterios reportCriterios) throws Exception {
		for (DadosCartaJulgamento dadosCartaJulgamento : listDadosCartaJulgamento) {
			String dsSubtituloResultado = tpStatus == TipoStatusEnum.JARI_COM_PROVIMENTO.getKey() ? 
					"DEFERIMENTO DO RECURSO - 1ª INSTÂNCIA - JARI" : "INDEFERIMENTO DO RECURSO - 1ª INSTÂNCIA - JARI";
			reportCriterios.addParametros("MOB_BLB_MENSAGEM_RESULTADO", alteraTextoJariJulgamento(dadosCartaJulgamento, tpStatus));
			reportCriterios.addParametros("DS_SUBTITULO", dsSubtituloResultado);
		}
	}
	
	private String alteraTextoJariJulgamento(DadosCartaJulgamento dadosCartaJulgamento, int tpStatus) throws Exception {
		String nmParametroResultado = tpStatus == TipoStatusEnum.JARI_COM_PROVIMENTO.getKey() ? "MOB_BLB_JARI_COM_PROVIMENTO" : "MOB_BLB_JARI_SEM_PROVIMENTO";
		String textoResultado = this.parametroRepository.getValorOfParametroAsString(nmParametroResultado);
		textoResultado = textoResultado.replace("<#Nr AIT>", dadosCartaJulgamento.getIdAit());
		textoResultado = textoResultado.replace("<#Nr PROCESSO>", dadosCartaJulgamento.getNrProcesso());
		textoResultado = textoResultado.replace("<#DATA_RESULTADO>", DateUtil.formatDate((dadosCartaJulgamento.getDtMovimento()), "dd/MM/yyyy"));
		textoResultado = textoResultado.replace("<#DATA_PUBLICACAO>", DateUtil.formatDate((dadosCartaJulgamento.getDtPublicacaoDo()), "dd/MM/yyyy"));
		return textoResultado;
}

	private SearchCriterios montarCriterios(int cdAit) {
		SearchCriterios searchCriterios = new SearchCriterios();
		searchCriterios.addCriteriosEqualInteger("A.codigo_ait", cdAit, true);
		return searchCriterios;
	}
	
	private Search<DadosCartaJulgamento> searchJariJulgamento(SearchCriterios searchCriterios, int tpStatus, CustomConnection customConnection) throws Exception {
		Search<DadosCartaJulgamento> search = new SearchBuilder<DadosCartaJulgamento>("AIT A")
				.customConnection(customConnection)
				.fields(  " A.codigo_ait as cd_ait, A.nr_ait as id_ait, A.dt_infracao, A.nr_placa, A.nm_proprietario, A.nr_cpf_cnpj_proprietario, "
						+ " A.ds_logradouro, A.ds_nr_imovel, A.nr_cep, "
						+ " C.nm_municipio, C.nm_uf, "
						+ " K.dt_movimento, K.tp_status, K.nr_processo, "
						+ " P.nm_bairro,"
						+ " Q.cod_municipio, Q.nm_municipio AS CIDADE_PROPRIETARI, "
						+ " T.nr_processo as nr_documento ")
				.addJoinTable(" LEFT OUTER JOIN municipio 			   C  ON (A.cod_municipio = C.cod_municipio)")
				.addJoinTable(" JOIN ait_movimento	       K  ON (A.codigo_ait = K.codigo_ait AND K.tp_status = " + tpStatus + " )")
				.addJoinTable(" LEFT OUTER JOIN bairro                 P  ON (A.cod_bairro = P.cod_bairro)")
				.addJoinTable(" LEFT OUTER JOIN municipio              Q  ON (Q.cod_municipio = P.cod_municipio)")
				.addJoinTable(" LEFT OUTER JOIN str_processo           T  ON (T.cd_ait = A.codigo_ait AND T.tp_processo =  " + TipoStatusEnum.RECURSO_JARI.getKey() + ")")
				.searchCriterios(searchCriterios)
				.build();
		return search;
	}

	public String getEstado() throws Exception {
		Orgao orgao = OrgaoServices.getOrgaoUnico();
		Cidade cidade = cidadeRepository.get(orgao.getCdCidade());
		return estadoRepository.get(cidade.getCdEstado()).getSgEstado();
	}

}
