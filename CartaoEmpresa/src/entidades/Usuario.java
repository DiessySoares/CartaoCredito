package entidades;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {

    // posibilidade de status //
    public enum ContaStatus {
        PENDENTE, ACEITA, REJEITADA
    };

    // id para serializacao //
    private static final long serialVersionUID = 808004L;

    // Informação pessoal //
    private String nome;
    private String CPF;
    private String senha;
    private String senhaCartao;
    private String telefone;
    private float rendaMensal;
    private Endereco endereco;

    // Status da conta //
    private ContaStatus status;

    // Cartões da conta //
    private ArrayList<CartaoCredito> cartoes;

    public CartaoCredito getCartaoFisicoAtivo() {
        if (cartoes != null && !cartoes.isEmpty()) {
            for (CartaoCredito cartao : cartoes) {
                if (cartao.getStatus() == CartaoCredito.CartaoStatus.AVAILABLE && !cartao.isVirtual()) {
                    return cartao;
                }
            }
        }
        return null;
    }

    public CartaoCredito getCartaoVirtualAtivo() {
        if (cartoes != null && !cartoes.isEmpty()) {
            for (CartaoCredito cartao : cartoes) {
                if (cartao.getStatus() == CartaoCredito.CartaoStatus.AVAILABLE && cartao.isVirtual()) {
                    return cartao;
                }
            }
        }
        return null;
    }
    
    
    
    public boolean pushEditedCartao(CartaoCredito cartaoIn) {
        for (int i = 0; i < (cartoes.size() - 1); i++) {
            if (cartoes.get(i).getNumeroCartao().equals(cartaoIn.getNumeroCartao())) {
                cartoes.set(i, cartaoIn);
                return true;
            }
        }
        return false;
    } 

    public Usuario() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public float getRendaMensal() {
        return rendaMensal;
    }

    public void setRendaMensal(float rendaMensal) {
        this.rendaMensal = rendaMensal;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSenhaCartao() {
        return senhaCartao;
    }

    public void setSenhaCartao(String senhaCartao) {
        this.senhaCartao = senhaCartao;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public ContaStatus getStatus() {
        return status;
    }

    public void setStatus(ContaStatus status) {
        this.status = status;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public ArrayList<CartaoCredito> getCartoes() {
        return cartoes;
    }

    public void setCartoes(ArrayList<CartaoCredito> cartoes) {
        this.cartoes = cartoes;
    }

    @Override
    public String toString() {
        int espacamento = 110 - getNome().length();
        String format = "Nome: %-" + espacamento + "sStatus: %s%n";
        return String.format(format, getNome(), getStatus().name());
    }

}
