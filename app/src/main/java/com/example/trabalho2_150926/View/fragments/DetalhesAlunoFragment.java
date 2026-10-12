package com.example.trabalho2_150926.View.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.trabalho2_150926.Model.Aluno;
import com.example.trabalho2_150926.R;

public class DetalhesAlunoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detalhes_aluno, container, false);

        if (getArguments() != null) {
            Aluno aluno = (Aluno) getArguments().getSerializable("ALUNO");
            if (aluno != null) {
                ((TextView) v.findViewById(R.id.tvNomeDetalhes)).setText(aluno.getNome());
                ((TextView) v.findViewById(R.id.tvMediaDetalhes)).setText(String.format("Nota final: %.2f", aluno.getMediaNotas()));
                ((TextView) v.findViewById(R.id.tvPresencaDetalhes)).setText(String.format("Presença: %.1f%%", aluno.getPercentualPresenca()));

                TextView tvSituacao = v.findViewById(R.id.tvSituacaoDetalhes);
                boolean aprovado = aluno.isAprovado();
                tvSituacao.setText("Situação: " + (aprovado ? "APROVADO" : "REPROVADO"));
                tvSituacao.setTextColor(aprovado ? Color.parseColor("#2E7D32") : Color.RED);
            }
        }
        return v;
    }
}