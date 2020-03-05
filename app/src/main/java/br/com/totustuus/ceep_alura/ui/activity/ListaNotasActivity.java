package br.com.totustuus.ceep_alura.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import java.util.List;

import br.com.totustuus.ceep_alura.R;
import br.com.totustuus.ceep_alura.dao.NotaDAO;
import br.com.totustuus.ceep_alura.model.Nota;
import br.com.totustuus.ceep_alura.ui.recyclerview.adapter.ListaNotasAdapter;

public class ListaNotasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        RecyclerView listaNotas = findViewById(R.id.lista_notas_recycler_view);

        NotaDAO dao = new NotaDAO();

        for (int i = 1; i <= 10000; i++) {
            dao.insere(new Nota("Título " + i, "Descrição " + i));
        }

        List<Nota> todasNotas = dao.todos();

        listaNotas.setAdapter(new ListaNotasAdapter(this, todasNotas));

        /*
        O layout não será apresentado exatamente da forma definida em item_nota.
        Levando isso em consideração, adicionaremos um Layout Manager (Gerenciador de Layout, em português)
        e a partir do gerenciador apresentaremos a View.

        Por padrão, o RecyclerView implementa alguns Layout Managers.
        Utilizaremos LinearLayoutManager, que é bem similar ao do ListView.
        Ele segue o padrão de preencher a largura da tela, criando o aspecto
        visual que conferimos inicialmente.
         */
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listaNotas.setLayoutManager(layoutManager);
    }
}
