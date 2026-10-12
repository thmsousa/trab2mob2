package com.example.trabalho2_150926.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.trabalho2_150926.Conexao.RetrofitClient;
import com.example.trabalho2_150926.Model.Aluno;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarAlunoViewModel extends ViewModel {
    private final MutableLiveData<Aluno> aluno = new MutableLiveData<>();
    private final MutableLiveData<Boolean> salvarSucesso = new MutableLiveData<>();
    private final MutableLiveData<String> erro = new MutableLiveData<>();

    public LiveData<Aluno> getAluno() { return aluno; }
    public LiveData<Boolean> getSalvarSucesso() { return salvarSucesso; }
    public LiveData<String> getErro() { return erro; }

    public void carregarAlunoPorId(int id) {
        RetrofitClient.getService().getAlunoPorId(id).enqueue(new Callback<Aluno>() {
            @Override
            public void onResponse(Call<Aluno> call, Response<Aluno> response) {
                if (response.isSuccessful()) {
                    aluno.postValue(response.body());
                } else {
                    erro.postValue("Falha ao carregar aluno: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Aluno> call, Throwable t) {
                erro.postValue("Falha de rede: " + t.getMessage());
            }
        });
    }

    public void salvar(Aluno alunoAtualizado) {
        RetrofitClient.getService().updateAluno(alunoAtualizado.getId(), alunoAtualizado).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    salvarSucesso.postValue(true);
                } else {
                    erro.postValue("Erro ao atualizar dados: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                erro.postValue("Erro de conexão: " + t.getMessage());
            }
        });
    }
}