package com.example.trabalho2_150926.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.trabalho2_150926.Conexao.Conexao;
import com.example.trabalho2_150926.Model.Aluno;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlunoViewModel extends ViewModel {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final MutableLiveData<List<Aluno>> listaAlunosLiveData = new MutableLiveData<>();
    private final MutableLiveData<Aluno> alunoSelecionadoLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> operacaoSalvarSucesso = new MutableLiveData<>();
    private final MutableLiveData<String> mensagemErro = new MutableLiveData<>();

    public LiveData<List<Aluno>> getListaAlunos() { return listaAlunosLiveData; }
    public LiveData<Aluno> getAlunoSelecionado() { return alunoSelecionadoLiveData; }
    public LiveData<Boolean> getOperacaoSalvarSucesso() { return operacaoSalvarSucesso; }
    public LiveData<String> getMensagemErro() { return mensagemErro; }

    public void carregarAlunos() {
        executor.execute(() -> {
            try {
                String json = Conexao.requisitar(Conexao.BASE_URL, "GET", null);
                JSONArray array = new JSONArray(json);
                List<Aluno> lista = new ArrayList<>();

                for (int i = 0; i < array.length(); i++) {
                    int id = array.getJSONObject(i).getInt("id");
                    String jsonCompleto = Conexao.requisitar(Conexao.BASE_URL + id, "GET", null);
                    Aluno a = Aluno.fromJson(new JSONObject(jsonCompleto));
                    if (a != null) lista.add(a);
                }
                listaAlunosLiveData.postValue(lista);
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao carregar lista: " + e.getMessage());
            }
        });
    }

    public void carregarAlunoPorId(int id) {
        executor.execute(() -> {
            try {
                String json = Conexao.requisitar(Conexao.BASE_URL + id, "GET", null);
                JSONObject obj = new JSONObject(json);
                alunoSelecionadoLiveData.postValue(Aluno.fromJson(obj));
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao buscar estudante: " + e.getMessage());
            }
        });
    }

    public void deletarAluno(int id) {
        executor.execute(() -> {
            try {
                Conexao.requisitar(Conexao.BASE_URL + id, "DELETE", null);
                carregarAlunos();
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao deletar: " + e.getMessage());
            }
        });
    }

    public void atualizarAluno(Aluno aluno) {
        executor.execute(() -> {
            try {
                Conexao.requisitar(Conexao.BASE_URL + aluno.getId(), "PUT", aluno.toDtoJson().toString());
                operacaoSalvarSucesso.postValue(true);
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao salvar estudante: " + e.getMessage());
            }
        });
    }

    // 2. Libera a thread quando a ViewModel for descartada da memória
    @Override
    protected void onCleared() {
        super.onCleared();
        executor.shutdown();
    }
}