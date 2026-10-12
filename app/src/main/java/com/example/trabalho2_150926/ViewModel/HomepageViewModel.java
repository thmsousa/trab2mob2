package com.example.trabalho2_150926.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class HomepageViewModel extends ViewModel {
    private final MutableLiveData<String> mensagem = new MutableLiveData<>(
            "Bem-vindo ao Sistema Acadêmico!\n\nAbra o menu lateral (gaveta) para consultar a lista de estudantes ou conferir as estatísticas gerais da turma."
    );

    public LiveData<String> getMensagem() {
        return mensagem;
    }
}
