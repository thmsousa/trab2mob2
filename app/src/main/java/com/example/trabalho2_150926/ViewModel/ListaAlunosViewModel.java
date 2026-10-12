package com.example.trabalho2_150926.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.trabalho2_150926.Conexao.RetrofitClient;
import com.example.trabalho2_150926.Model.Aluno;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaAlunosViewModel extends ViewModel {
    private final MutableLiveData<List<Aluno>> listaAlunos = new MutableLiveData<>();
    private final MutableLiveData<String> erro = new MutableLiveData<>();

    public LiveData<List<Aluno>> getListaAlunos() { return listaAlunos; }
    public LiveData<String> getErro() { return erro; }

    public void carregarAlunos() {
        RetrofitClient.getService().listAlunos().enqueue(new Callback<List<Aluno>>() {
            @Override
            public void onResponse(Call<List<Aluno>> call, Response<List<Aluno>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Aluno> base = response.body();
                    List<Aluno> completos = new ArrayList<>();
                    if (base.isEmpty()) {
                        listaAlunos.postValue(completos);
                        return;
                    }
                    for (Aluno aluno : base) {
                        RetrofitClient.getService().getAlunoPorId(aluno.getId()).enqueue(new Callback<Aluno>() {
                            @Override
                            public void onResponse(Call<Aluno> c, Response<Aluno> r) {
                                if (r.isSuccessful() && r.body() != null) completos.add(r.body());
                                if (completos.size() == base.size()) listaAlunos.postValue(completos);
                            }
                            @Override
                            public void onFailure(Call<Aluno> c, Throwable t) {
                                completos.add(aluno);
                                if (completos.size() == base.size()) listaAlunos.postValue(completos);
                            }
                        });
                    }
                } else {
                    erro.postValue("Erro ao carregar dados: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<Aluno>> call, Throwable t) {
                erro.postValue("Falha de rede: " + t.getMessage());
            }
        });
    }

    public void deletarAluno(int id) {
        RetrofitClient.getService().deleteAluno(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                carregarAlunos();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                erro.postValue("Erro ao deletar: " + t.getMessage());
            }
        });
    }
}