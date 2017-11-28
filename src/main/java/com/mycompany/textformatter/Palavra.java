/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.textformatter;

import java.util.Objects;

/**
 *
 * @author Eduardo
 */
public class Palavra implements Comparable<Palavra> {
    String palavra;
    String classe;
    int frequencia;

    public Palavra(String palavra, String classe, int frequencia) {
        this.palavra = palavra;
        this.classe = classe;
        this.frequencia = frequencia;
    }

    public Palavra(String palavra) {
        this.palavra = palavra;
        this.classe = "";
        this.frequencia = 1;
    }

    public String getPalavra() {
        return palavra;
    }

    public void setPalavra(String palavra) {
        this.palavra = palavra;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public int getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(int frequencia) {
        this.frequencia = frequencia;
    }

    @Override
    public String toString() {
        return "{" + palavra + ", " + classe + ", " + frequencia + "}";
    }

    @Override
    public int compareTo(Palavra outraPal) {
        if (this.frequencia > outraPal.frequencia) return -1;
        if (this.frequencia < outraPal.frequencia) return 1;
        return 0;
    }
    
    /**
     *
     * @param String a ser comparada com a palavra atual;
     * @return true se a palavra atual for igual ao parâmetro ou false caso contrário;
     */
    @Override
    public boolean equals(Object o){
        Palavra p = (Palavra) o;
        if (this.getPalavra().equals(p.getPalavra())) return true;
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.palavra);
        return hash;
    }
}
