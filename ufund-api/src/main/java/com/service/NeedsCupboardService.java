package com.service;

import java.io.IOException;
import java.util.List;

import com.ufund.api.ufundapi.model.Need;

public interface NeedsCupboardService {
    Need getNeedById(int id) throws IOException;

    Need[] getAllNeeds() throws IOException;

    Need[] searchNeeds(String name) throws IOException;

    Need createNeed(Need need) throws IOException;

    Need updateNeed(Need need) throws IOException;

    boolean deleteNeedById(int id) throws IOException;

    boolean deleteNeedByType(String type) throws IOException;

    boolean deleteAllNeeds() throws IOException;

    boolean deleteNeedByName(String name) throws IOException;
}
