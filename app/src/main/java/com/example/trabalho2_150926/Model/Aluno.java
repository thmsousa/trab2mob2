package com.example.trabalho2_150926.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Aluno implements Serializable {
    private int id;
    private String nome;
    private int idade;
    private List<Double> notas;

    @SerializedName(value = "presenca", alternate = {"presencas"})
    private List<Boolean> presencas;

    public Aluno() {
        this.notas = new ArrayList<>();
        this.presencas = new ArrayList<>();
    }

    public Aluno(int id, String nome, int idade, List<Double> notas, List<Boolean> presencas) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.notas = notas != null ? new ArrayList<>(notas) : new ArrayList<>();
        this.presencas = presencas != null ? new ArrayList<>(presencas) : new ArrayList<>();
    }

    public double getMediaNotas() {
        if (notas == null || notas.isEmpty()) return 0.0;
        double soma = 0;
        for (double n : notas) soma += n;
        return soma / notas.size();
    }

    public double getPercentualPresenca() {
        if (presencas == null || presencas.isEmpty()) return 0.0;
        int confirmados = 0;
        for (Boolean p : presencas) {
            if (Boolean.TRUE.equals(p)) confirmados++;
        }
        return ((double) confirmados / presencas.size()) * 100.0;
    }

    public boolean isAprovado() {
        return getMediaNotas() >= 6.0 && getPercentualPresenca() >= 75.0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public List<Double> getNotas() {
        return notas != null ? notas : (notas = new ArrayList<>());
    }

    public void setNotas(List<Double> notas) {
        this.notas = notas;
    }

    public List<Boolean> getPresencas() {
        return presencas != null ? presencas : (presencas = new ArrayList<>());
    }

    public void setPresencas(List<Boolean> presencas) {
        this.presencas = presencas;
    }
}