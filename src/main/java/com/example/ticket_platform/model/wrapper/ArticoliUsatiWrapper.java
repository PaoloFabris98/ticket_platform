package com.example.ticket_platform.model.wrapper;

import java.io.Serializable;
import java.util.List;

import com.example.ticket_platform.model.dto.ArticoliUsatiDTO;

public class ArticoliUsatiWrapper implements Serializable {
    private List<ArticoliUsatiDTO> articoliUsati;

    public List<ArticoliUsatiDTO> getArticoliUsati() {
        return articoliUsati;
    }

    public void setArticoliUsati(List<ArticoliUsatiDTO> articoliUsati) {
        this.articoliUsati = articoliUsati;
    }

    public Integer getListLenght() {
        if (this.getArticoliUsati() == null) {
            return 0;
        }
        return this.articoliUsati.size();
    }
}