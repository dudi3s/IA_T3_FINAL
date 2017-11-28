/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.textformatter;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eduardo
 */
public class Texto {
    String id;
    List<Palavra> palavrasComPREP;
    List<Palavra> palavrasSemPREP;

    public Texto(String id, List<Palavra> palavrasComPREP, List<Palavra> palavrasSemPREP) {
        this.id = id;
        this.palavrasComPREP = palavrasComPREP;
        this.palavrasSemPREP = palavrasSemPREP;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Palavra> getPalavrasComPREP() {
        return palavrasComPREP;
    }

    public void setPalavrasComPREP(List<Palavra> palavrasComPREP) {
        this.palavrasComPREP = palavrasComPREP;
    }

    public List<Palavra> getPalavrasSemPREP() {
        return palavrasSemPREP;
    }

    public void setPalavrasSemPREP(List<Palavra> palavrasSemPREP) {
        this.palavrasSemPREP = palavrasSemPREP;
    }

    @Override
    public String toString() {
        return "Texto{" + "id=" + id + ", palavrasComPREP=" + palavrasComPREP + ", palavrasSemPREP=" + palavrasSemPREP + '}';
    }
}
