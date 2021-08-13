package entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class CartaoCredito implements Serializable {

    private final int INTERVALO_VENC_FECHA = 7;
    private static final long serialVersionUID = 808000L;

    // ENUMS
    public enum CartaoStatus {
        AVAILABLE, LOCK, CANCELED
    };

    public enum CartaoCategoria {
        NACIONAL, INTERNACIONAL
    };

    public enum CartaoBandeira {
        VISA, MASTERCARD, DINERS, AMEX,
        HIPERCARD, ELO, AURA, DISCOVER
    };

    public enum CartaoVariante {
        GOLD, PLANTIUM, BLACK
    };

    // informaçoes do cartao //
    private String numeroCartao;
    private String senha;
    private Date dataValidade;
    private String CodigoSeguranca;
    private float limite;
    private boolean virtual;
    private int diaFaturaVencimento;
    private CartaoCategoria categoria;
    private CartaoBandeira bandeira;
    private CartaoVariante variante;
    private CartaoStatus status;

    // faturas //
    private ArrayList<Fatura> faturas;

    //---------------- FUNCOES ----------------//
    public float valorPendeteFatura() {
        float response = 0;

        for (Fatura fatura : faturas) {
            if (fatura.faturaPendente()) {
                response = fatura.getValorPendente();
            }
        }
        return response;
    }

    public void gerarNumeroCartao() {
        String randNumberCartao = "";
        int number;
        int temp = 0;
        for (int i = 0; i < 16; i++) {
            temp++;
            number = (int) (Math.random() * ((9 - 0) + 1)) + 0;
            randNumberCartao += Integer.toString(number);

            if (temp == 4 && i < 15) {
                randNumberCartao += "-";
                temp = 0;
            }
        }
        numeroCartao = randNumberCartao;
    }

    public void gerarCVV() {
        String randNumberCartao = "";
        int number;
        for (int i = 0; i < 3; i++) {
            number = (int) (Math.random() * ((9 - 0) + 1)) + 0;
            randNumberCartao += Integer.toString(number);
        }
        CodigoSeguranca = randNumberCartao;
    }

    //Date dateNow = new Date(); ---------------------------------------------
    private static Date removeOneMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -7);
        return cal.getTime();
    }

    private static Date addOneMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();
    }

    private static Date trim(Date date) {

        date = addOneMonth(date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 17);
        return calendar.getTime();
    }

    private void criarFatura() {
        Date date = new Date();
        Fatura fatura = new Fatura();

        fatura.setValorPago(0);
        fatura.setValorFatura(0);

        date = trim(date);

        System.out.println("Vencimento :" + date);

        date = removeOneMonth(date);

        System.out.println("fechamento :" + date);
    }

    public void pushFaturaEdit(Fatura fatura) {
        if (faturas != null) {
            for (int i = 0; i < 36; i++) {
                if (fatura.getDataFechamento() == faturas.get(i).getDataFechamento()
                        && fatura.getDataVencimento() == faturas.get(i).getDataVencimento()) {
                    faturas.set(i, fatura);
                }
            }
        }
    }

    //public boolean inserirItem(){----------------------------------------
    //}
    //---------------- SET & GET ----------------//
    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Date getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(Date dataValidade) {
        this.dataValidade = dataValidade;
    }

    public String getCodigoSeguranca() {
        return CodigoSeguranca;
    }

    public void setCodigoSeguranca(String CodigoSeguranca) {
        this.CodigoSeguranca = CodigoSeguranca;
    }

    public CartaoCategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(CartaoCategoria categoria) {
        this.categoria = categoria;
    }

    public CartaoBandeira getBandeira() {
        return bandeira;
    }

    public void setBandeira(CartaoBandeira bandeira) {
        this.bandeira = bandeira;
    }

    public CartaoVariante getVariante() {
        return variante;
    }

    public void setVariante(CartaoVariante variante) {
        this.variante = variante;
    }

    public CartaoStatus getStatus() {
        return status;
    }

    public void setStatus(CartaoStatus status) {
        this.status = status;
    }

    public float getLimite() {
        return limite;
    }

    public void setLimite(float limite) {
        this.limite = limite;
    }

    public boolean isVirtual() {
        return virtual;
    }

    public void setVirtual(boolean virtual) {
        this.virtual = virtual;
    }

    public ArrayList<Fatura> getFaturas() {
        return faturas;
    }

    public void setFaturas(ArrayList<Fatura> faturas) {
        this.faturas = faturas;
    }

    public int getDiaVencimento() {
        return diaFaturaVencimento;
    }

    public void setDiaVencimento(int diaVencimento) {
        this.diaFaturaVencimento = diaVencimento;
    }

}
