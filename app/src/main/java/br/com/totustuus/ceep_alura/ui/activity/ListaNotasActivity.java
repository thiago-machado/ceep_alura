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

        List<Nota> todasNotas = configuraNotasExemplo();
        configuraRecyclerView(todasNotas);
    }

    private List<Nota> configuraNotasExemplo() {

        NotaDAO dao = new NotaDAO();

        for (int i = 1; i <= 10000; i++) {
            dao.insere(new Nota("Título " + i, "Descrição " + i));
        }

        return dao.todos();
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {

        RecyclerView listaNotas = findViewById(R.id.lista_notas_recycler_view);
        configuraAdapter(todasNotas, listaNotas);
        /*
        A configuração do LayoutManager está sendo definida agora no layout.xml
        com o atributo: app:layoutManager="LinearLayoutManager"
         */
        //configuraLayoutManager(listaNotas);

    }

    private void configuraLayoutManager(RecyclerView listaNotas) {

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

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        listaNotas.setAdapter(new ListaNotasAdapter(this, todasNotas));
    }
}
