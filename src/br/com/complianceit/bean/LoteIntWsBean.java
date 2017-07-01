package br.com.complianceit.bean;

import br.com.complianceit.impl.JDBC;
import br.com.complianceit.pojo.LoteIntWs;
import java.io.File;
import java.io.IOException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mateusgobo
 */
public class LoteIntWsBean {

    private final Logger logger = Logger.getLogger(LoteIntWsBean.class);
    private final JDBC jdbc;
    private int count = 1;
    private String[] args;
    
    public LoteIntWsBean() {
        this.jdbc = new JDBC();
    }
    
    public LinkedHashSet<LoteIntWs> recuperarLoteIntWs(String[] args, Long... id){
        String msg = id != null && id.length > 0 && id[0] != null ? "RECUPERANDO LOTES, PARTINDO ID["+id[0]+"]" : "RECUPERANDO LOTES";
        this.logger.info(msg);
        this.args = args;
        LinkedHashSet<LoteIntWs> listLoteIntWs = null;
        String cmplQuery = "";
        try{
            int sizeArray = this.args.length;
            if(sizeArray >= 6){
                Long multOrgId      = new Long(args[0]);
                Long tipoObjIntegr  = new Long(args[1]);
                Long dmStProc       = new Long(args[2]);
                
                if(id != null && id.length > 0 && id[0] != null){
                    cmplQuery = " and l.id > "+id[0];
                }
                String sql = "select * " +
                            "from (select l.id ID_LOTE," +
                            "        l.multorg_id ID_MULTORG," +
                            "        l.tipoobjintegr_id ID_OBJINTEGR," +
                            "        l.dt_hr_receb RECEB," +
                            "        l.dm_st_proc SIT_PROC," +
                            "        l.xml_receb XML," +
                            "        l.dir_lote DIR_LOTE," +
                            "        l.dm_processa_xml SIT_PROCESSA," +
                            "        row_number() over (order by l.id asc) as seqnum" +
                            "  from lote_int_ws l" +
                            " where l.multorg_id = " +multOrgId+
                            "   and l.tipoobjintegr_id = " +tipoObjIntegr+
                            "   and l.dm_st_proc = " +dmStProc+
                            (sizeArray > 6 ? " and trunc(l.dt_hr_receb) >= trunc(sysdate - "+args[6]+") "
                                           + " and not exists (select 1 from log_generico lg where lg.referencia_id = l.id) " : "")+
                            "   " +cmplQuery+
                            " order by 1)" +
                            " where seqnum <= 100";
                this.jdbc.openConnection(this.args[3], this.args[4], this.args[5]);
                PreparedStatement pstmt = this.jdbc.getConnection().prepareStatement(sql);

                ResultSet result        = pstmt.executeQuery();
                listLoteIntWs = new LinkedHashSet<LoteIntWs>();
                while(result.next()){
                    LoteIntWs loteIntWs = new LoteIntWs();
                    loteIntWs.setId(result.getLong("ID_LOTE"));
                    loteIntWs.setMultOrgId(result.getLong("ID_MULTORG"));
                    loteIntWs.setTipoObjIntegrId(result.getLong("ID_OBJINTEGR"));
                    loteIntWs.setDtHrReceb(result.getDate("RECEB"));
                    loteIntWs.setDmStProc(result.getBigDecimal("SIT_PROC"));
                    loteIntWs.setXmlReceb(result.getBytes("XML"));
                    loteIntWs.setDirLote(result.getString("DIR_LOTE"));
                    loteIntWs.setDmProcessaXml(result.getBigDecimal("SIT_PROCESSA"));
                    listLoteIntWs.add(loteIntWs);
                }
                if(!listLoteIntWs.isEmpty()){
                    this.logger.info("LOTES RECUPERADOS PARA OS CRITERIOS, MULTORG_ID["+multOrgId+"],TIPOOBJ_ID["+tipoObjIntegr+"],DM_ST_PROC["+dmStProc+"]");
                }
                this.jdbc.closeConnection(pstmt, result);
            }else{
                this.logger.error("ERRO NA INICIALIZACAO DO PROGRAMA, INFORME O ID DA MULTORG, O ID DO TIPO DO OBJETO DE INTEGRACAO E DO DM_ST_PROC PARA RECUPERACAO DOS LOTE...");
            }
        }catch(SQLException e){
            this.logger.error("FALHA NA LEITURA DOS DADOS DA CONSULTA...",e);
        }
        return listLoteIntWs;
    }
    
    public Long registrarXmlLote(LinkedHashSet<LoteIntWs> result){
        Long lastId = 0l;
        try{
            for(LoteIntWs lote : result){
                this.logger.info("GERANDO LOTE["+lote.getId()+"]");
                String nameFile = lote.getId().toString()+"_"+lote.getMultOrgId()+".xml";
                String dir      = lote.getDirLote();
//                String dir = "/Users/mateusgobo/Desktop/Amazon/";
                File f          = new File(dir+nameFile);
                try {
                    FileUtils.writeStringToFile(f, new String(lote.getXmlReceb()));
                } catch (IOException ex) {
                    logger.error("FALHA AO REGISTAR ARQUIVO ["+nameFile+"]",ex);
                }
                lastId = lote.getId();
                this.count++;
            }
        }catch(Exception e){
            logger.error("PROBLEMAS NA LEITURA DOS DADOS DA BASE",e);
        }
        return lastId;
    }

    public int getCount() {
        return count;
    }

    public Logger getLogger() {
        return logger;
    }
    
    public static void main(String[] args) {
        LoteIntWsBean loteIntWsBean = new LoteIntWsBean();
        int count   = 1;
        Long lastId = null;
        while(count > 0){
            try {
                LinkedHashSet<LoteIntWs> result = loteIntWsBean.recuperarLoteIntWs(args, lastId);
                if(!result.isEmpty()){
                    lastId = loteIntWsBean.registrarXmlLote(result);
                }
                count = result.size();
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
        }
        loteIntWsBean.getLogger().info("TOTAL DE ARQUIVOS REGISTRADOS NO DIRETORIO ===> "+loteIntWsBean.getCount());
    }
}
