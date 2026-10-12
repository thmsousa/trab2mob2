package com.example.trabalho2_150926.View.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.trabalho2_150926.Model.Aluno;
import com.example.trabalho2_150926.R;
import com.example.trabalho2_150926.ViewModel.EditarAlunoViewModel;

public class EditarAlunoFragment extends Fragment {

    private int alunoId;
    private EditarAlunoViewModel viewModel;
    private Aluno alunoEmEdicao;

    private EditText etNome, etIdade, etNovaNota;
    private TextView tvNotas, tvPresencas;
    private RadioGroup rgStatusPresenca;

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
        etNovaNota = v.findViewById(R.id.etNovaNota);
        tvNotas = v.findViewById(R.id.tvNotasAtuais);
        tvPresencas = v.findViewById(R.id.tvPresencasAtuais);
        rgStatusPresenca = v.findViewById(R.id.rgStatusPresenca);

        viewModel = new ViewModelProvider(this).get(EditarAlunoViewModel.class);

        viewModel.getAluno().observe(getViewLifecycleOwner(), aluno -> {
            if (aluno != null) {
                alunoEmEdicao = aluno;
                etNome.setText(aluno.getNome());
                etIdade.setText(String.valueOf(aluno.getIdade()));
                atualizarExibicao();
            }
        });

        // Adicionar nota
        v.findViewById(R.id.btnAddNota).setOnClickListener(btn -> {
            String valor = etNovaNota.getText().toString().trim();
            if (!valor.isEmpty() && alunoEmEdicao != null) {
                try {
                    double n = Double.parseDouble(valor);
                    if (n >= 0 && n <= 10) {
                        alunoEmEdicao.getNotas().add(n);
                        etNovaNota.setText("");
                        atualizarExibicao();
                    } else {
                        Toast.makeText(getContext(), "Nota deve ser entre 0 e 10", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Nota inválida", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adicionar presença/frequência
        v.findViewById(R.id.btnAddPresenca).setOnClickListener(btn -> {
            if (alunoEmEdicao != null) {
                boolean presente = (rgStatusPresenca.getCheckedRadioButtonId() == R.id.rbPresente);
                alunoEmEdicao.getPresencas().add(presente);
                atualizarExibicao();
                Toast.makeText(getContext(), presente ? "Presença computada!" : "Falta computada!", Toast.LENGTH_SHORT).show();
            }
        });

        // Salvar via PUT
        v.findViewById(R.id.btnSalvarAlteracoes).setOnClickListener(btn -> {
            if (alunoEmEdicao != null) {
                alunoEmEdicao.setNome(etNome.getText().toString().trim());
                try {
                    alunoEmEdicao.setIdade(Integer.parseInt(etIdade.getText().toString().trim()));
                    viewModel.salvar(alunoEmEdicao);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Idade inválida", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.getSalvarSucesso().observe(getViewLifecycleOwner(), sucesso -> {
            if (Boolean.TRUE.equals(sucesso)) {
                Toast.makeText(getContext(), "Alterações salvas!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(v).popBackStack();
            }
        });

        viewModel.carregarAlunoPorId(alunoId);
        return v;
    }

    private void atualizarExibicao() {
        tvNotas.setText("Notas: " + alunoEmEdicao.getNotas().toString());
        tvPresencas.setText(String.format("Frequência: %d registros (%.1f%% de presença)",
                alunoEmEdicao.getPresencas().size(), alunoEmEdicao.getPercentualPresenca()));
    }
}