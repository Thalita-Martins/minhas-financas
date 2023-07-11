package com.tmartins.minhasfinancas.enumeration;

public enum StatusLancamento {

    CREDITO("CREDITO"),
    PENDENTE("PENDENTE"),
    CANCELADO("CANCELADO"),
    QUITADO("QUITADO");

    private final String status;

    StatusLancamento(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
