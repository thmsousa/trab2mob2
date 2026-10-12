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

import java.util.List;

public class AlunoAdapter extends RecyclerView.Adapter<AlunoAdapter.ViewHolder> {

    public interface OnAcoesListener {
        void onLongClickDetalhes(Aluno aluno);

        void onEditar(Aluno aluno);

        void onDeletar(Aluno aluno);
    }

    private final List<Aluno> alunos;
    private final OnAcoesListener listener;

    public AlunoAdapter(List<Aluno> alunos, OnAcoesListener listener) {
        this.alunos = alunos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aluno, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Aluno aluno = alunos.get(position);
        holder.tvNome.setText(aluno.getNome());

        holder.itemView.setOnLongClickListener(v -> {
            listener.onLongClickDetalhes(aluno);
            return true;
        });

        holder.btnEditar.setOnClickListener(v -> listener.onEditar(aluno));
        holder.btnDeletar.setOnClickListener(v -> listener.onDeletar(aluno));
    }

    @Override
    public int getItemCount() {
        return alunos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNome;
        Button btnEditar, btnDeletar;

        ViewHolder(View itemView) {
            super(itemView);
            tvNome = itemView.findViewById(R.id.tvNomeAluno);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnDeletar = itemView.findViewById(R.id.btnDeletar);
        }
    }
}