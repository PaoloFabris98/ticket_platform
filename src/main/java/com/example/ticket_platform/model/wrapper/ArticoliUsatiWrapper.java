package com.example.ticket_platform.model.wrapper;

import java.util.List;

import com.example.ticket_platform.model.dto.ArticoliUsatiDTO;

public class ArticoliUsatiWrapper {
    private List<ArticoliUsatiDTO> articoliUsati;

    public List<ArticoliUsatiDTO> getArticoliUsati() {
        return articoliUsati;
    }

    public void setArticoliUsati(List<ArticoliUsatiDTO> articoliUsati) {
        this.articoliUsati = articoliUsati;
    }
}