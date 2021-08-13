/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

/**
 *
 * @author Diessy
 */
public class Fatura implements Serializable {

    // id para serializacao //
    private static final long serialVersionUID = 808002L;

    // informações da fatura // 
    private Date dataFechamento;
    private Date dataVencimento;
    private float valorFatura;
    private float valorPago;

    // itens da fatura //
    private ArrayList<ItemFatura> itens;
    
    //---------------- FUNCOES ----------------//
    
    public boolean temItens(){
        return (itens != null && itens.size() > 0);
    }
   
    public void calcularValor() {
        if (itens != null) {
            for (ItemFatura item : itens) {
                this.valorFatura += item.isParcelado() ? item.getParcela(): item.getValor();
            }
        }
    }
    
    public float getValorPendente(){
        return valorFatura - valorPago;
    }
    
    public boolean faturaPendente(){
        return (valorFatura - valorPago) != 0;
    }
    
    //---------------- SET & GET ----------------//

    public double getValorFatura() {
        return valorFatura;
    }

    public void setValorFatura(float valorFatura) {
        this.valorFatura = valorFatura;
    }

    public double getValorPago() {
        return valorPago;
    }

    public void setValorPago(float valorPago) {
        this.valorPago = valorPago;
    }

    public ArrayList<ItemFatura> getItens() {
        return itens;
    }

    public void setItens(ArrayList<ItemFatura> itens) {
        this.itens = itens;
    }

    public Date getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(Date dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    @Override
    public String toString() {
        String format = "%-40s%s%n";
        return String.format(format, "Data: " + new SimpleDateFormat("dd-MM-yyyy").format(dataVencimento), "Valor: " + getValorPendente());
    }
    
}
