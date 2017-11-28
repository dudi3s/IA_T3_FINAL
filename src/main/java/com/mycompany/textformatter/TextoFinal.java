/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.textformatter;

import java.util.List;

/**
 *
 * @author Eduardo
 */
public class TextoFinal {

    String idTexto;
    List<Palavra> uni_grama;
    List<Palavra> bi_grama;
    List<Palavra> tri_grama;
    String classe;

    public TextoFinal(String idTexto, List<Palavra> n_grama, List<Palavra> bi_grama, List<Palavra> tri_grama, String classe) {
        this.idTexto = idTexto;
        this.uni_grama = n_grama;
        this.bi_grama = bi_grama;
        this.tri_grama = tri_grama;
        this.classe = classe;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getIdTexto() {
        return idTexto;
    }

    public void setIdTexto(String idTexto) {
        this.idTexto = idTexto;
    }

    public List<Palavra> getUni_grama() {
        return uni_grama;
    }

    public void setUni_grama(List<Palavra> uni_grama) {
        this.uni_grama = uni_grama;
    }

    public List<Palavra> getBi_grama() {
        return bi_grama;
    }

    public void setBi_grama(List<Palavra> bi_grama) {
        this.bi_grama = bi_grama;
    }

    public List<Palavra> getTri_grama() {
        return tri_grama;
    }

    public void setTri_grama(List<Palavra> tri_grama) {
        this.tri_grama = tri_grama;
    }

    @Override
    public String toString() {
        StringBuilder retorno = new StringBuilder();
        retorno.append(this.classe);
        retorno.append("\n");
        retorno.append(this.idTexto);
        retorno.append("\n");

        retorno.append("UNI_GRAMA: ");
        retorno.append("\n");
        for (Palavra pAux : this.uni_grama) {
            retorno.append(pAux.toString());
            retorno.append("\n");
        }

        retorno.append("\n");

        retorno.append("BI_GRAMA: ");
        retorno.append("\n");
        for (Palavra pAux : this.bi_grama) {
            retorno.append(pAux.toString());
            retorno.append("\n");
        }

        retorno.append("\n");

        retorno.append("TRI_GRAMA: ");
        retorno.append("\n");
        for (Palavra pAux : this.tri_grama) {
            retorno.append(pAux.toString());
            retorno.append("\n");
        }

        return retorno.toString();
    }

}
