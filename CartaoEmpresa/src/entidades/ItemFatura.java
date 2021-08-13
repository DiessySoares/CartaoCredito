/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Diessy
 */
public class ItemFatura implements Serializable {
    
    // id para serializacao //
    private static final long serialVersionUID = 808003L;

    // informações da compra //
    private String descricao;
    private float valor;
    private Date dataCompra;

    // parcelamento //
    private boolean parcelado;
    private float parcela;
    private int vezes;


    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public Date getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(Date dataCompra) {
        this.dataCompra = dataCompra;
    }

    public boolean isParcelado() {
        return parcelado;
    }

    public void setParcelado(boolean parcelado) {
        this.parcelado = parcelado;
    }

    public float getParcela() {
        return parcela;
    }

    public void setParcela(float parcela) {
        this.parcela = parcela;
    }

    public int getVezes() {
        return vezes;
    }

    public void setVezes(int vezes) {
        this.vezes = vezes;
    }

}
