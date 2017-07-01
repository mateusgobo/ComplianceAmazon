/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.complianceit.pojo;

import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author mateusgobo
 */
public class LoteIntWs {
    
    private Long id;
    private Long multOrgId;
    private Long tipoObjIntegrId;
    private byte[] xmlReceb;
    private Date dtHrReceb;
    private BigDecimal dmStProc;
    private String dirLote;
    private BigDecimal dmProcessaXml;

    public LoteIntWs() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMultOrgId() {
        return multOrgId;
    }

    public void setMultOrgId(Long multOrgId) {
        this.multOrgId = multOrgId;
    }

    public Long getTipoObjIntegrId() {
        return tipoObjIntegrId;
    }

    public void setTipoObjIntegrId(Long tipoObjIntegrId) {
        this.tipoObjIntegrId = tipoObjIntegrId;
    }

    public byte[] getXmlReceb() {
        return xmlReceb;
    }

    public void setXmlReceb(byte[] xmlReceb) {
        this.xmlReceb = xmlReceb;
    }

    public Date getDtHrReceb() {
        return dtHrReceb;
    }

    public void setDtHrReceb(Date dtHrReceb) {
        this.dtHrReceb = dtHrReceb;
    }

    public BigDecimal getDmStProc() {
        return dmStProc;
    }

    public void setDmStProc(BigDecimal dmStProc) {
        this.dmStProc = dmStProc;
    }

    public String getDirLote() {
        return dirLote;
    }

    public void setDirLote(String dirLote) {
        this.dirLote = dirLote;
    }

    public BigDecimal getDmProcessaXml() {
        return dmProcessaXml;
    }

    public void setDmProcessaXml(BigDecimal dmProcessaXml) {
        this.dmProcessaXml = dmProcessaXml;
    }
}
