package com.example.trabalho2_150926.View;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.trabalho2_150926.Model.Aluno;
import com.example.trabalho2_150926.R;
import com.example.trabalho2_150926.ViewModel.AlunoViewModel;

import java.util.ArrayList;
import java.util.List;

public class EditarAlunoActivity extends AppCompatActivity {

    private Aluno alunoAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_aluno);

        EditText etNome = findViewById(R.id.etNome);
        EditText etIdade = findViewById(R.id.etIdade);
        EditText etNotas = findViewById(R.id.etNotas);

        AlunoViewModel vm = new ViewModelProvider(this).get(AlunoViewModel.class);

        int id = getIntent().getIntExtra("ID_ALUNO", -1);
        if (id != -1) vm.carregarAlunoPorId(id);

        vm.getAlunoSelecionado().observe(this, aluno -> {
            if (aluno != null) {
                alunoAtual = aluno;
                etNome.setText(aluno.getNome());
                etIdade.setText(String.valueOf(aluno.getIdade()));
                List<String> sNotas = new ArrayList<>();
                for (Double n : aluno.getNotas()) sNotas.add(String.valueOf(n));
                etNotas.setText(TextUtils.join(", ", sNotas));
            }
        });

        vm.getOperacaoSalvarSucesso().observe(this, ok -> {
            if (ok) finish();
        });

        findViewById(R.id.btnSalvar).setOnClickListener(v -> {
            if (alunoAtual != null) {
                alunoAtual.setNome(etNome.getText().toString());
                try {
                    alunoAtual.setIdade(Integer.parseInt(etIdade.getText().toString().trim()));
                } catch (Exception ignored) {}

                List<Double> novasNotas = new ArrayList<>();
                String str = etNotas.getText().toString().trim();
                if (!str.isEmpty()) {
                    for (String pedaco : str.split(",")) {
                        try {
                            novasNotas.add(Double.parseDouble(pedaco.trim()));
                        } catch (Exception ignored) {}
                    }
                }
                alunoAtual.setNotas(novasNotas);
                vm.atualizarAluno(alunoAtual);
            }
        });
        findViewById(R.id.btnVoltarEdicao).setOnClickListener(v -> finish());
    }
}