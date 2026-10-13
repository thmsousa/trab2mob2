package com.example.trabalho2_150926.View.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabalho2_150926.Model.Aluno;
import com.example.trabalho2_150926.R;
import com.example.trabalho2_150926.View.AlunoAdapter;
import com.example.trabalho2_150926.ViewModel.ListaAlunosViewModel;

public class ListaAlunosFragment extends Fragment {

    private ListaAlunosViewModel viewModel;
    private AlunoAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lista_alunos, container, false);
        RecyclerView rv = v.findViewById(R.id.rvAlunos);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel = new ViewModelProvider(this).get(ListaAlunosViewModel.class);

        adapter = new AlunoAdapter(new AlunoAdapter.OnAlunoClickListener() {
            @Override
            public void onEditar(Aluno aluno) {
                Bundle b = new Bundle();
                b.putInt("ALUNO_ID", aluno.getId());
                NavHostFragment.findNavController(ListaAlunosFragment.this)
                        .navigate(R.id.action_lista_to_editar, b);
            }

            @Override
            public void onDeletar(Aluno aluno) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Excluir")
                        .setMessage("Deseja realmente remover " + aluno.getNome() + "?")
                        .setPositiveButton("Sim", (dialog, which) -> viewModel.deletarAluno(aluno.getId()))
                        .setNegativeButton("Não", null)
                        .show();
            }

            @Override
            public void onDetalhes(Aluno aluno) {
                Bundle b = new Bundle();
                b.putSerializable("ALUNO", aluno);
                NavHostFragment.findNavController(ListaAlunosFragment.this)
                        .navigate(R.id.action_listaAlunosFragment_to_detalhesAlunoFragment, b);
            }
        });

        rv.setAdapter(adapter);

        viewModel.getListaAlunos().observe(getViewLifecycleOwner(), adapter::setLista);
        viewModel.getErro().observe(getViewLifecycleOwner(), err -> {
            if (err != null) Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
        });

        viewModel.carregarAlunos();
        return v;
    }
}