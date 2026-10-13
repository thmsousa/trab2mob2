package com.example.trabalho2_150926.View.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.trabalho2_150926.Model.Aluno;
import com.example.trabalho2_150926.R;
import com.example.trabalho2_150926.ViewModel.EditarAlunoViewModel;

public class EditarAlunoFragment extends Fragment {

    private int alunoId;
    private EditarAlunoViewModel viewModel;
    private Aluno alunoEmEdicao;

    private EditText etNome, etIdade, etNota1, etNota2, etNota3;
    private LinearLayout containerPresencas;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            alunoId = getArguments().getInt("ALUNO_ID", 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_editar_aluno, container, false);

        etNome = v.findViewById(R.id.etEditNome);
        etIdade = v.findViewById(R.id.etEditIdade);
        etNota1 = v.findViewById(R.id.etNota1);
        etNota2 = v.findViewById(R.id.etNota2);
        etNota3 = v.findViewById(R.id.etNota3);
        containerPresencas = v.findViewById(R.id.containerPresencas);

        viewModel = new ViewModelProvider(this).get(EditarAlunoViewModel.class);

        viewModel.getAluno().observe(getViewLifecycleOwner(), aluno -> {
            if (aluno != null) {
                alunoEmEdicao = aluno;
                etNome.setText(aluno.getNome());
                etIdade.setText(String.valueOf(aluno.getIdade()));

                // Exibe apenas as notas existentes na lista do servidor
                if (aluno.getNotas() != null) {
                    if (aluno.getNotas().size() > 0) {
                        etNota1.setVisibility(View.VISIBLE);
                        etNota1.setText(String.valueOf(aluno.getNotas().get(0)));
                    }
                    if (aluno.getNotas().size() > 1) {
                        etNota2.setVisibility(View.VISIBLE);
                        etNota2.setText(String.valueOf(aluno.getNotas().get(1)));
                    }
                    if (aluno.getNotas().size() > 2) {
                        etNota3.setVisibility(View.VISIBLE);
                        etNota3.setText(String.valueOf(aluno.getNotas().get(2)));
                    }
                }

                // Cria as checkboxes de presença para cada aula existente
                containerPresencas.removeAllViews();
                if (aluno.getPresencas() != null) {
                    for (int i = 0; i < aluno.getPresencas().size(); i++) {
                        final int idx = i;
                        CheckBox cb = new CheckBox(getContext());
                        cb.setText("Aula " + (i + 1));
                        cb.setChecked(Boolean.TRUE.equals(aluno.getPresencas().get(i)));
                        cb.setOnCheckedChangeListener((btn, isChecked) -> {
                            alunoEmEdicao.getPresencas().set(idx, isChecked);
                        });
                        containerPresencas.addView(cb);
                    }
                }
            }
        });

        // Salvar via PUT
        v.findViewById(R.id.btnSalvarAlteracoes).setOnClickListener(btn -> {
            if (alunoEmEdicao != null) {
                try {
                    alunoEmEdicao.setNome(etNome.getText().toString().trim());
                    alunoEmEdicao.setIdade(Integer.parseInt(etIdade.getText().toString().trim()));

                    if (alunoEmEdicao.getNotas().size() > 0 && !etNota1.getText().toString().isEmpty()) {
                        alunoEmEdicao.getNotas().set(0, Double.parseDouble(etNota1.getText().toString().trim()));
                    }
                    if (alunoEmEdicao.getNotas().size() > 1 && !etNota2.getText().toString().isEmpty()) {
                        alunoEmEdicao.getNotas().set(1, Double.parseDouble(etNota2.getText().toString().trim()));
                    }
                    if (alunoEmEdicao.getNotas().size() > 2 && !etNota3.getText().toString().isEmpty()) {
                        alunoEmEdicao.getNotas().set(2, Double.parseDouble(etNota3.getText().toString().trim()));
                    }

                    viewModel.salvar(alunoEmEdicao);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Verifique os valores inseridos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.getSalvarSucesso().observe(getViewLifecycleOwner(), sucesso -> {
            if (Boolean.TRUE.equals(sucesso)) {
                Toast.makeText(getContext(), "Alterações salvas!", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(EditarAlunoFragment.this).popBackStack();
            }
        });

        viewModel.carregarAlunoPorId(alunoId);
        return v;
    }
}