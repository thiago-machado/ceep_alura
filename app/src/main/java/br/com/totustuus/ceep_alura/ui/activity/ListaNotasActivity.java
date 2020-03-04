package br.com.totustuus.ceep_alura.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import br.com.totustuus.ceep_alura.R;
import br.com.totustuus.ceep_alura.dao.NotaDAO;
import br.com.totustuus.ceep_alura.model.Nota;
import br.com.totustuus.ceep_alura.ui.adapter.ListaNotasAdapter;

public class ListaNotasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        ListView listaNotas = findViewById(R.id.listView);

        NotaDAO dao = new NotaDAO();
        dao.insere(new Nota("Primeira nota",
                "Primeira descrição"));
        List<Nota> todasNotas = dao.todos();

        listaNotas.setAdapter(new ListaNotasAdapter(this, todasNotas));

    }
}
