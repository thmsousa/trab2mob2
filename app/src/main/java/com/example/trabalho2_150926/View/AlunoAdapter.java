package com.example.trabalho2_150926.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.trabalho2_150926.Model.Aluno;
import com.example.trabalho2_150926.R;
import java.util.ArrayList;
import java.util.List;

public class AlunoAdapter extends RecyclerView.Adapter<AlunoAdapter.ViewHolder> {

    public interface OnAlunoClickListener {
        void onEditar(Aluno aluno);
        void onDeletar(Aluno aluno);
        void onDetalhes(Aluno aluno);
    }

    private List<Aluno> lista = new ArrayList<>();
    private final OnAlunoClickListener listener;

    public AlunoAdapter(OnAlunoClickListener listener) {
        this.listener = listener;
    }

    public void setLista(List<Aluno> novaLista) {
        this.lista = novaLista != null ? novaLista : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aluno, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Aluno aluno = lista.get(position);
        holder.tvNome.setText(aluno.getNome());
        holder.tvIdade.setText("Idade: " + aluno.getIdade());
        holder.tvMedia.setText(String.format("Média: %.2f", aluno.getMediaNotas()));
        holder.tvPresenca.setText(String.format("Presença: %.0f%%", aluno.getPercentualPresenca()));

        holder.btnEditar.setOnClickListener(v -> listener.onEditar(aluno));
        holder.btnDeletar.setOnClickListener(v -> listener.onDeletar(aluno));
        holder.itemView.setOnClickListener(v -> listener.onDetalhes(aluno));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome, tvIdade, tvMedia, tvPresenca;
        Button btnEditar, btnDeletar;

        ViewHolder(View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNomeAluno);
            tvIdade = itemView.findViewById(R.id.tvIdadeAluno);
            tvMedia = itemView.findViewById(R.id.tvMediaAluno);
            tvPresenca = itemView.findViewById(R.id.tvPresencaAluno);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnDeletar = itemView.findViewById(R.id.btnDeletar);
        }
    }
}