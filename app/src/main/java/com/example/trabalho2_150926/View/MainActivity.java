package com.example.trabalho2_150926.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabalho2_150926.Model.Aluno;
import com.example.trabalho2_150926.R;
import com.example.trabalho2_150926.ViewModel.AlunoViewModel;

public class MainActivity extends AppCompatActivity {

    private AlunoViewModel viewModel;
    private RecyclerView rvAlunos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvAlunos = findViewById(R.id.rvAlunos);
        rvAlunos.setLayoutManager(new LinearLayoutManager(this));
        rvAlunos.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        findViewById(R.id.btnAbrirEstatisticas).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, EstatisticasActivity.class));
        });

        viewModel = new ViewModelProvider(this).get(AlunoViewModel.class);

        viewModel.getListaAlunos().observe(this, lista -> {
            AlunoAdapter adapter = new AlunoAdapter(lista, new AlunoAdapter.OnAcoesListener() {
                @Override
                public void onLongClickDetalhes(Aluno aluno) {
                    Intent it = new Intent(MainActivity.this, DetalhesAlunoActivity.class);
                    it.putExtra("ALUNO", aluno);
                    startActivity(it);
                }

                @Override
                public void onEditar(Aluno aluno) {
                    Intent it = new Intent(MainActivity.this, EditarAlunoActivity.class);
                    it.putExtra("ID_ALUNO", aluno.getId());
                    startActivity(it);
                }

                @Override
                public void onDeletar(Aluno aluno) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Confirmar Exclusão")
                            .setMessage("Deseja remover o estudante " + aluno.getNome() + "?")
                            .setPositiveButton("Sim, excluir", (dialog, which) -> viewModel.deletarAluno(aluno.getId()))
                            .setNegativeButton("CANCELAR", null)
                            .show();
                }
            });
            rvAlunos.setAdapter(adapter);
        });

        viewModel.getMensagemErro().observe(this, erro -> {
            if (erro != null) Toast.makeText(this, erro, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.carregarAlunos();
    }
}