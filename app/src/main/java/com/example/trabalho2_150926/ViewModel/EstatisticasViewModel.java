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

public class EstatisticasViewModel extends ViewModel {
    private final MutableLiveData<List<Aluno>> alunos = new MutableLiveData<>();
    public LiveData<List<Aluno>> getAlunos() { return alunos; }

    public void carregarAlunos() {
        RetrofitClient.getService().listAlunos().enqueue(new Callback<List<Aluno>>() {
            @Override
            public void onResponse(Call<List<Aluno>> call, Response<List<Aluno>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Aluno> base = response.body();
                    List<Aluno> completos = new ArrayList<>();
                    if (base.isEmpty()) {
                        alunos.postValue(completos);
                        return;
                    }
                    for (Aluno a : base) {
                        RetrofitClient.getService().getAlunoPorId(a.getId()).enqueue(new Callback<Aluno>() {
                            @Override
                            public void onResponse(Call<Aluno> c, Response<Aluno> r) {
                                if (r.isSuccessful() && r.body() != null) completos.add(r.body());
                                if (completos.size() == base.size()) alunos.postValue(completos);
                            }
                            @Override
                            public void onFailure(Call<Aluno> c, Throwable t) {
                                if (completos.size() == base.size()) alunos.postValue(completos);
                            }
                        });
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Aluno>> call, Throwable t) {}
        });
    }
}