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

/*
Algo importante e que deve ficar bem claro é que o cast que aplicamos é opcional.

Sabemos que essa técnica traz riscos, pois podemos assumir que é outra classe, sendo que
na verdade a responsabilidade é do método, o que pode gerar exceptions.

Para evitar esse tipo de situação, existe uma alternativa de implementação do RecyclerView.Adapter que
recebe generics, dispensando-se a utilização do cast.

O nosso objetivo será estabelecer que o RecyclerView.Adapter trabalhará em cima de um ViewHolder
implementado.
Aplicaremos essa ideia ao código, adicionando <ListaNotasAdapter.NotaViewHolder> após RecyclerView.Adapter,
na declaração da classe ListaNotasAdapter.


 */
public class ListaNotasAdapter extends RecyclerView.Adapter<ListaNotasAdapter.NotaViewHolder> {

    private final List<Nota> notas;
    private final Context context;

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
    public ListaNotasAdapter.NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        /*
        Responsável pela criação de contêineres das Views que ficam visíveis na tela.
        Pensando nisso, aplicaremos o mesmo processo que aplicaríamos no BaseAdapter.
        Isto é, faremos o inflate do layout que queremos exibir.
         */
        final View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_nota, parent, false);
        return new NotaViewHolder(viewCriada);
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
    public void onBindViewHolder(@NonNull ListaNotasAdapter.NotaViewHolder holder, int position) {

        /*
        ---------------
        Antes da alteração de RecyclerView.Adapter para RecyclerView.Adapter<ListaNotasAdapter.NotaViewHolder>,
        estava funcionando como comentado abaixo:

        De forma semelhante ao que fizemos no Adapter do ListView, acessaremos uma
        View por meio de NotaViewHolder().

        No parâmetro de onBindViewHolder(), perceba que RecyclerView.ViewHolder holder
        recebe um holder. A partir dele, temos acesso a itemView, referente à View que criamos.

        Com uma referência, podemos pegar uma View por meio de findViewById()
        ---------------
         */
        Nota nota = notas.get(position);
        holder.vincula(nota);
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
    class NotaViewHolder extends RecyclerView.ViewHolder {

        private final TextView titulo;
        private final TextView descricao;

        /*
        A implementação de NotaViewHolder recebe View, que representa
        cada item da View (itemView) e chama super para enviar itemView
        ao ViewHolder do RecyclerView.

        Esse construtor será chamado toda vez que onCreateViewHolder()
        for chamado. Ou seja, será chamado pouquíssimas vezes, já que
        o número de ViewHolder's é limitado.
         */
        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.item_nota_titulo);
            descricao = itemView.findViewById(R.id.item_nota_descricao);
        }

        public void vincula(Nota nota) {
            titulo.setText(nota.getTitulo());
            descricao.setText(nota.getDescricao());
        }

    }

    /*
    Pegaremos a referência da lista por meio de notas, e adicionaremos a nova com add(nota).
    Após a adição, notificaremos o Adapter da alteração, da mesma forma que fizemos anteriormente.

    Qualquer manipulação em cima da lista — o chamado dataset do Adapter — precisa ser notificada.
    Sendo assim, acrescentaremos notifyDataSetChanged().

    Assim, o Adapter torna-se responsável por se notificar, e por todos os processos.
    Com a lógica implementada, a nota é recebida, o Adapter a serializa e se altera.

    Essa abordagem é bem bacana pois não temos que limpar a lista e adicionar todos os elementos de uma vez.
    São executados somente os processos necessários para uma nota ser adicionada ao Adapter.
     */
    public void adiciona(Nota nota) {
        notas.add(nota);

        /*
         Adapter vai analisar a lsita que tem, o que mudou e renderizar as views com base no que mudou.

         O método adicionado fará com que o Adapter notifique as alterações.
         No momento em que é chamado, ele analisa a lista interna, o que foi alterado e o que
         deve ser renderizado, conforme as mudanças conferidas.

         */
        notifyDataSetChanged();
    }
}
