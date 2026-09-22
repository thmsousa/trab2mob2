package com.example.trabalho2_150926.Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Aluno implements Serializable {
    private int id;
    private String nome;
    private int idade;
    private List<Double> notas;
    private List<Boolean> presencas;

    public Aluno(int id, String nome, int idade, List<Double> notas, List<Boolean> presencas) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.notas = notas != null ? notas : new ArrayList<>();
        this.presencas = presencas != null ? presencas : new ArrayList<>();
    }

    public static Aluno fromJson(JSONObject obj) {
        try {
            int id = obj.optInt("id", 0);
            String nome = obj.optString("nome", "");
            int idade = obj.optInt("idade", 0);

            List<Double> notas = new ArrayList<>();
            JSONArray arrNotas = obj.optJSONArray("notas");
            if (arrNotas != null) {
                for (int i = 0; i < arrNotas.length(); i++) {
                    notas.add(arrNotas.getDouble(i));
                }
            }

            List<Boolean> presencas = new ArrayList<>();
            JSONArray arrPresencas = obj.optJSONArray("presenca");
            if (arrPresencas == null) {
                arrPresencas = obj.optJSONArray("presencas");
            }
            if (arrPresencas != null) {
                for (int i = 0; i < arrPresencas.length(); i++) {
                    presencas.add(arrPresencas.getBoolean(i));
                }
            }
            return new Aluno(id, nome, idade, notas, presencas);
        } catch (Exception e) {
            return null;
        }
    }

    public JSONObject toDtoJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("nome", this.nome);
            obj.put("idade", this.idade);

            JSONArray arrNotas = new JSONArray();
            for (Double n : this.notas) arrNotas.put(n);
            obj.put("notas", arrNotas);

            JSONArray arrPresencas = new JSONArray();
            for (Boolean p : this.presencas) arrPresencas.put(p);
            obj.put("presenca", arrPresencas);
        } catch (Exception ignored) {
        }
        return obj;
    }

    public double getMediaNotas() {
        if (notas.isEmpty()) return 0.0;
        double soma = 0;
        for (double n : notas) soma += n;
        return soma / notas.size();
    }

    public double getPercentualPresenca() {
        if (presencas.isEmpty()) return 0.0;
        int presencasConfirmadas = 0;
        for (boolean p : presencas) {
            if (p) presencasConfirmadas++;
        }
        return ((double) presencasConfirmadas / presencas.size()) * 100.0;
    }

    public boolean isAprovado() {
        return getMediaNotas() >= 6.0 && getPercentualPresenca() >= 75.0;
    }

    public int getId() {
        return id;
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
        return notas;
    }

    public void setNotas(List<Double> notas) {
        this.notas = notas;
    }

    public List<Boolean> getPresencas() {
        return presencas;
    }

    public void setPresencas(List<Boolean> presencas) {
        this.presencas = presencas;
    }
}