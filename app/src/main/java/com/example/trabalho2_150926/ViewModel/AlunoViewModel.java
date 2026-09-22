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
import java.util.concurrent.Executors;

public class AlunoViewModel extends ViewModel {

    private final MutableLiveData<List<Aluno>> listaAlunosLiveData = new MutableLiveData<>();
    private final MutableLiveData<Aluno> alunoSelecionadoLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> operacaoSalvarSucesso = new MutableLiveData<>();
    private final MutableLiveData<String> mensagemErro = new MutableLiveData<>();

    public LiveData<List<Aluno>> getListaAlunos() { return listaAlunosLiveData; }
    public LiveData<Aluno> getAlunoSelecionado() { return alunoSelecionadoLiveData; }
    public LiveData<Boolean> getOperacaoSalvarSucesso() { return operacaoSalvarSucesso; }
    public LiveData<String> getMensagemErro() { return mensagemErro; }

    public void carregarAlunos() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                // Obtém a lista dos DTOs com IDs
                String json = Conexao.requisitar(Conexao.BASE_URL, "GET", null);
                JSONArray array = new JSONArray(json);
                List<Aluno> lista = new ArrayList<>();

                // Busca cada estudante pelo ID para recuperar as listas de notas e presença
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
        Executors.newSingleThreadExecutor().execute(() -> {
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
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Conexao.requisitar(Conexao.BASE_URL + id, "DELETE", null);
                carregarAlunos();
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao deletar: " + e.getMessage());
            }
        });
    }

    public void atualizarAluno(Aluno aluno) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Conexao.requisitar(Conexao.BASE_URL + aluno.getId(), "PUT", aluno.toDtoJson().toString());
                operacaoSalvarSucesso.postValue(true);
            } catch (Exception e) {
                mensagemErro.postValue("Erro ao salvar estudante: " + e.getMessage());
            }
        });
    }
}