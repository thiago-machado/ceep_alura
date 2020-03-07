package br.com.totustuus.ceep_alura.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import br.com.totustuus.ceep_alura.model.Nota;

public class NotaDAO {

    private final ArrayList<Nota> notas = new ArrayList<>();

    public List<Nota> todos() {
        return (List<Nota>) notas.clone();
    }

    public void insere(Nota nota) {
        notas.add(nota);
    }

    public void altera(int posicao, Nota nota) {
        notas.set(posicao, nota);
    }

    public void remove(int posicao) {
        notas.remove(posicao);
    }

    public void troca(int posicaoInicio, int posicaoFim) {
        Collections.swap(notas, posicaoInicio, posicaoFim);
    }

    public void removeTodos() {
        notas.clear();
    }
}
