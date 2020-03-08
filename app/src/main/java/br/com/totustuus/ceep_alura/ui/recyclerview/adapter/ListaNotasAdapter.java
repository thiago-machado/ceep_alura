package br.com.totustuus.ceep_alura.ui.recyclerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import br.com.totustuus.ceep_alura.R;
import br.com.totustuus.ceep_alura.model.Nota;
import br.com.totustuus.ceep_alura.ui.recyclerview.adapter.listener.OnItemClickListener;

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

    /*
    Criamos essa interface para que fosse possível a implementação de qualquer ação
    de clique (em cada ViewHolder) para quem utilizar nosso ListaNotasAdapter.
     */
    private OnItemClickListener onItemClickListener;

    public ListaNotasAdapter(Context context, List<Nota> notas) {
        this.context = context;
        this.notas = notas;
        Log.i("notas", "construtor adapter notas size: " + notas.size());
    }

    /*
    Quem utiliza nosso Adapter, precisa chamar esse método para poder implementar uma ação
    de click para cada ViewHolder.
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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

    public void altera(int posicao, Nota nota) {
        Log.i("notas", "altera adapter notas size: " + notas.size());
        notas.set(posicao, nota);
        notifyDataSetChanged();

        /*
         É indicada a posição em que ocorreu a mudança. Teríamos um efeito similar
         na parte de movimentação . Contudo, como estamos fazendo a alteração em outra
         Activity, não é necessário chamar esse método.
         */
        //notifyItemChanged(posicao);
    }

    public void remove(int posicao) {
        notas.remove(posicao);

        //notifyDataSetChanged();
        /*
        Estamos notificando a remoção de um item a partir de uma posicao
        e dessa forma será colocada uma transição mais fluida.
        O notifyDataSetChanged() exerce bem sua função, mas a animação é muito rápida.
         */
        notifyItemRemoved(posicao);
    }

    public void troca(int posicaoInicial, int posicaoFinal) {
        Collections.swap(notas, posicaoInicial, posicaoFinal);
        //notifyDataSetChanged();

        /*
        Usaremo a notificação específica notifyItemMoved(), que necessita
        de uma posicaoInicial e posicaoFinal
        Com isso, poderemos mover melhor uma view. Antes, podíamos mover uma
        nota acima ou abaixo dela um nível. Agora, podemos mover a quantos níveis
        acima e abaixo quisermos.
         */
        notifyItemMoved(posicaoInicial, posicaoFinal);
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
        private Nota nota;

        /*
        A implementação de NotaViewHolder recebe View, que representa
        cada item da View (itemView) e chama super para enviar itemView ao ViewHolder do RecyclerView.

        Esse construtor será chamado toda vez que onCreateViewHolder()
        for chamado. Ou seja, será chamado pouquíssimas vezes, já que o número de ViewHolder's é limitado.
         */
        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);

            Log.i("viewholder", "criando view holder...");
            titulo = itemView.findViewById(R.id.item_nota_titulo);
            descricao = itemView.findViewById(R.id.item_nota_descricao);

            /*
            Podemos muito bem fazer a implememtação de click nesse método
            onClick() de itemView.setOnClickListener.

            Contudo, essa implementação ficaria engessada, não permitindo que
            outras ações fossem permitidas.

            Por exemplo: se tivermos duas Activities e ambas
            utilizassem este nosso Adapter, mas cuja função de click fosse
            diferente em cada caso, isso não seria possível. Pois já implementamos
            a ação de click dentro do nosso Adapter.

            Para resolver isso, basta criarmos uma interface (de nome OnItemClickListener
            no nosso caso), com uma função de click (onItemClick no nosso caso).

            Com a interface criada, criamos um atributo da mesma no nosso Apdater e
            fazemos a chamada do método no nosso onClick().

            Precisamos permitir para quem utiilizar nosso Adapter, que possa implementar nosso
            OnItemClickListener. Para isso, basta criarmos no nosso Adapter um método setter.

            Feito isso, quem for utilizar nosso Adapter, poderá chamar o setter do nosso
            OnItemClickListener e implementar a ação onClick(). Ou seja, QUEM CHAMA é quem implementa
            a ação, possibilitando maior diversificação de ação.
             */
            Log.i("viewholder", "nota: " + nota);
            itemView.setOnClickListener(new View.OnClickListener() {

                /*
                A implementação de onclick pode ficar aqui pois já atende a todas as views.
                Essa nota à princípio será nula, mas com o processo de bind, ela deixará de ser nula.

                A utilização do método getAdapterPosition() é uma das técnicas utilizadas
                para acessar a posição dos elementos no RecyclerView.
                Ou seja, através de getAdapterPosition(), o próprio ViewHolder sabe qual a posição dele.
                Assim podemos pegar a posição dos elementos dentro do RecyclerView.

                 Além do ViewHolder possuir as informações necessárias para manter as views,
                 ele ainda possui dados sobre o relacionamento mantido com o adapter.

                 Isso significa ele ele mesmo sabe sobre sua própria posição e possui um
                 método específico para indicá-la.
                 */
                @Override
                public void onClick(View v) {
                    Log.i("click", "onclick do ViewHolder...");
                    Log.i("click", "posicao: " + getAdapterPosition());
                    onItemClickListener.onItemClick(nota, getAdapterPosition());
                }
            });
        }

        /*
        No momento em que chamar vincula, pegamos a nota e atribuímos ao nosso atributo "nota"
        do ViewHolder.
        E dessa maneira, temos acesso a nota no onItemClick.
        Então, toda vez que o processo de bind for chamado, vamos enviar a nota com base na posição,
         da maneira que esperamos.

         Perceba que a nota é enviada por meio do atributo do ViewHolder e, no momento que é
         necessário mudar a referência da mesma, é feita outra atribuição no bind.
         */
        public void vincula(Nota nota) {

            this.nota = nota;

            Log.i("viewholder", "realizando bind...");
            Log.i("viewholder", "bind nota: " + nota);

            titulo.setText(nota.getTitulo());
            descricao.setText(nota.getDescricao());
        }

    }

    public void adiciona(Nota nota) {
        notas.add(nota);

        /*
         Adapter vai analisar a lsita que tem, o que mudou e renderizar as views com base no que mudou.
         */
        notifyDataSetChanged();
    }
}
