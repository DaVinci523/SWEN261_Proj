package com.ufund.api.ufundapi.service;

import java.io.IOException;

import com.ufund.api.ufundapi.model.Need;

public interface NeedsCupboardService {
    Need getNeedById(int id) throws IOException;

    Need[] getAllNeeds() throws IOException;

    Need[] searchNeeds(String name) throws IOException;

    Need[] searchNeedsType(String text) throws IOException;

    Need[] searchNeedsQuantity(int qnt) throws IOException;

    Need[] searchNeedsValue(double val) throws IOException;

    Need createNeed(Need need) throws IOException;

    Need updateNeed(Need need) throws IOException;

    boolean deleteNeedById(int id) throws IOException;

    boolean deleteNeedByType(String type) throws IOException;

    boolean deleteAllNeeds() throws IOException;

    boolean deleteNeedByName(String name) throws IOException;

    

}
