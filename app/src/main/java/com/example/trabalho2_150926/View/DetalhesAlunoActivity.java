package com.example.trabalho2_150926.View;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trabalho2_150926.Model.Aluno;
import com.example.trabalho2_150926.R;

public class DetalhesAlunoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_aluno);

        Aluno aluno = (Aluno) getIntent().getSerializableExtra("ALUNO");
        if (aluno != null) {
            ((TextView) findViewById(R.id.tvNome)).setText(aluno.getNome());
            ((TextView) findViewById(R.id.tvMedia)).setText(String.format("Nota final: %.2f", aluno.getMediaNotas()));
            ((TextView) findViewById(R.id.tvPresenca)).setText(String.format("Presença: %.1f%%", aluno.getPercentualPresenca()));

            TextView tvSituacao = findViewById(R.id.tvSituacao);
            boolean aprovado = aluno.isAprovado();
            tvSituacao.setText("Situação: " + (aprovado ? "APROVADO" : "REPROVADO"));
            tvSituacao.setTextColor(aprovado ? Color.parseColor("#2E7D32") : Color.RED);
        }
        findViewById(R.id.btnVoltarDetalhes).setOnClickListener(v -> finish());
    }
}