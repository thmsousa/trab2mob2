package com.example.trabalho2_150926.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabalho2_150926.Model.Aluno;
import com.example.trabalho2_150926.R;
import com.example.trabalho2_150926.ViewModel.AlunoViewModel;

import java.util.ArrayList;
import java.util.List;

public class EstatisticasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatisticas);

        TextView tvGeral = findViewById(R.id.tvGeral);
        RecyclerView rvAprovados = findViewById(R.id.rvAprovados);
        RecyclerView rvReprovados = findViewById(R.id.rvReprovados);
        rvAprovados.setLayoutManager(new LinearLayoutManager(this));
        rvReprovados.setLayoutManager(new LinearLayoutManager(this));

        AlunoViewModel vm = new ViewModelProvider(this).get(AlunoViewModel.class);

        vm.getListaAlunos().observe(this, lista -> {
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

                if (a.isAprovado()) {
                    aprovados.add(linha);
                } else {
                    reprovados.add(linha);
                }
            }

            tvGeral.setText(String.format("Média Geral: %.2f\nMédia Idade: %.1f anos\nMaior Nota: %s (%.2f)\nMenor Nota: %s (%.2f)",
                    (somaNotas / lista.size()),
                    ((double) somaIdades / lista.size()),
                    maior.getNome(), maior.getMediaNotas(),
                    menor.getNome(),
                    menor.getMediaNotas()));

            rvAprovados.setAdapter(new ListaTextoAdapter(aprovados));
            rvReprovados.setAdapter(new ListaTextoAdapter(reprovados));
        });

        vm.carregarAlunos();

        findViewById(R.id.btnVoltarEstatisticas).setOnClickListener(v -> finish());
    }

    private static class ListaTextoAdapter extends RecyclerView.Adapter<ListaTextoAdapter.VH> {
        private final List<String> dados;

        ListaTextoAdapter(List<String> dados) {
            this.dados = dados;
        }

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
        public int getItemCount() {
            return dados.size();
        }

        static class VH extends RecyclerView.ViewHolder {
            TextView tv;

            VH(View v) {
                super(v);
                tv = v.findViewById(android.R.id.text1);
            }
        }
    }
}