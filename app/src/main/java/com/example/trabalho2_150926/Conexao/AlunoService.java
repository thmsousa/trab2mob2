package com.example.trabalho2_150926.Conexao;

import com.example.trabalho2_150926.Model.Aluno;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AlunoService {
    @GET("estudantes/")
    Call<List<Aluno>> listAlunos();

    @GET("estudantes/{id}")
    Call<Aluno> getAlunoPorId(@Path("id") int id);

    @PUT("estudantes/{id}")
    Call<Void> updateAluno(@Path("id") int id, @Body Aluno aluno);

    @DELETE("estudantes/{id}")
    Call<Void> deleteAluno(@Path("id") int id);
}