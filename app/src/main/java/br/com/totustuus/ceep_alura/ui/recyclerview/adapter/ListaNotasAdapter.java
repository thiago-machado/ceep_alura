package br.com.totustuus.ceep_alura.ui.recyclerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.totustuus.ceep_alura.R;
import br.com.totustuus.ceep_alura.model.Nota;

public class ListaNotasAdapter extends RecyclerView.Adapter {

    private List<Nota> notas;
    private Context context;

    public ListaNotasAdapter(Context context, List<Nota> notas) {
        this.context = context;
        this.notas = notas;
    }

    /*
    Cria as Views diretamente. A diferença é que
    serão criados ViewHolders — lembrando que trabalharemos com Views
    limitadas.

    A princípio, quando enviamos os itens, onCreateViewHolder() será chamado,
    método responsável pela criação de visualizações, ou os ViewHolders,
    que representarão as informações em contêineres e limitarão a
    quantidade de Views durante a implementação.
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        /*
        Responsável pela criação de contêineres das Views que ficam visíveis na tela.
        Pensando nisso, aplicaremos o mesmo processo que aplicaríamos no baseAdapter.
        Isto é, faremos o inflate do layout que queremos exibir.
         */
        final View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_nota, parent, false);
        return new ListNotaHolder(viewCriada);
    }

    /*
    Processa o bind das Views criadas, inserindo as informações de um objeto nas Views.
    No caso da aplicação que estamos desenvolvendo, o processo de bind pega uma nota a
    partir de uma posição (position) e a atrela a uma View recebida, inserindo título
    e descrição - essa é a ideia desse método.

    Com os ViewHolders criados, onBindViewHolder() entrará em ação, realizando o processo
    de bind. Ou seja, vinculará objetos a ViewHolder, inserindo as informações — como
    título e descrição, entre outras — dentro dele.

    O processo de bind funciona a partir de uma posição (position). Conferida a posição
    do elemento, serão inseridos nela os respectivos valores.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        /*
        De forma semelhante ao que fizemos no Adapter do ListView, acessaremos uma
        View por meio de NotaViewHolder().

        No parâmetro de onBindViewHolder(), perceba que RecyclerView.ViewHolder holder
        recebe um holder. A partir dele, temos acesso a itemView, referente à View que criamos.

        Com uma referência, podemos pegar uma View por meio de findViewById()
         */
        Nota nota = notas.get(position);
        TextView titulo = holder.itemView.findViewById(R.id.item_nota_titulo);
        titulo.setText(nota.getTitulo());

        TextView descricao = holder.itemView.findViewById(R.id.item_nota_descricao);
        descricao.setText(nota.getDescricao());
    }

    @Override
    public int getItemCount() {
        return notas.size();
    }

    /*
    Note que os métodos citados giram em torno de ViewHolder.
    Portanto, nosso primeiro passo será implementá-lo.
    Para isso, criaremos uma classe que faz extensão de ViewHolder do RecyclerView.
    Poderíamos adicionar um novo arquivo, no entanto é comum criarmos uma classe
    interna representando o ViewHolder.
    Ou seja, desenvolver uma classe dentro do próprio Adapter, para representar o RecyclerView.

    Por ser a abordagem mais utilizada e pelo fato do seu uso ser restrito ao Adapter;
    portanto, não há necessidade de criá-la de forma pública, para que fique acessível por fora.

    Mas fique à vontade para criar um arquivo à parte, se preferir.
     */
    class ListNotaHolder extends RecyclerView.ViewHolder {

        /*
        A implementação de NotaViewHolder recebe View, que representa
        cada item da View (itemView) e chama super para enviar itemView
        ao ViewHolder do RecyclerView.
         */
        public ListNotaHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
