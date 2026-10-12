package com.example.trabalho2_150926.View.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabalho2_150926.Model.Aluno;
import com.example.trabalho2_150926.R;
import com.example.trabalho2_150926.ViewModel.EstatisticasViewModel;

import java.util.ArrayList;
import java.util.List;

public class EstatisticasFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_estatisticas, container, false);

        TextView tvGeral = v.findViewById(R.id.tvGeral);
        RecyclerView rvAprovados = v.findViewById(R.id.rvAprovados);
        RecyclerView rvReprovados = v.findViewById(R.id.rvReprovados);
        rvAprovados.setLayoutManager(new LinearLayoutManager(getContext()));
        rvReprovados.setLayoutManager(new LinearLayoutManager(getContext()));

        EstatisticasViewModel vm = new ViewModelProvider(this).get(EstatisticasViewModel.class);
        vm.getAlunos().observe(getViewLifecycleOwner(), lista -> {
            if (lista == null || lista.isEmpty()) return;

            double somaNotas = 0;
            int somaIdades = 0;
            Aluno maior = lista.get(0);
            Aluno menor = lista.get(0);

            List<String> aprovados = new ArrayList<>();
            List<String> reprovados = new ArrayList<>();

            for (Aluno a : lista) {
                double media = a.getMediaNotas();
                somaNotas += media;
                somaIdades += a.getIdade();

                if (media > maior.getMediaNotas()) maior = a;
                if (media < menor.getMediaNotas()) menor = a;

                String linha = a.getNome() + " (Média: " + String.format("%.2f", media) + " | Presença: " + String.format("%.0f%%", a.getPercentualPresenca()) + ")";
                if (a.isAprovado()) aprovados.add(linha);
                else reprovados.add(linha);
            }

            tvGeral.setText(String.format("Média Geral: %.2f\nMédia Idade: %.1f anos\nMaior Nota: %s (%.2f)\nMenor Nota: %s (%.2f)",
                    (somaNotas / lista.size()), ((double) somaIdades / lista.size()), maior.getNome(), maior.getMediaNotas(), menor.getNome(), menor.getMediaNotas()));

            rvAprovados.setAdapter(new ListaTextoAdapter(aprovados));
            rvReprovados.setAdapter(new ListaTextoAdapter(reprovados));
        });

        vm.carregarAlunos();
        return v;
    }

    private static class ListaTextoAdapter extends RecyclerView.Adapter<ListaTextoAdapter.VH> {
        private final List<String> dados;
        ListaTextoAdapter(List<String> dados) { this.dados = dados; }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            holder.tv.setText(dados.get(position));
        }

        @Override
        public int getItemCount() { return dados.size(); }

        static class VH extends RecyclerView.ViewHolder {
            TextView tv;
            VH(View v) { super(v); tv = v.findViewById(android.R.id.text1); }
        }
    }
}