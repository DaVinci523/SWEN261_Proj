package com.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ufund.api.ufundapi.model.Need;
import com.ufund.api.ufundapi.persistence.NeedsCupboardDAO;


/**
 * Service layer between the controller and DAO
 */
@Service
public class NeedsCupboardServiceImplementation implements NeedsCupboardService{
    private static final Logger LOG = Logger.getLogger(NeedsCupboardServiceImplementation.class.getName());

    private final NeedsCupboardDAO needsCupboardDAO;

    @Autowired
    public NeedsCupboardServiceImplementation(NeedsCupboardDAO needsCupboardDAO){
        this.needsCupboardDAO = needsCupboardDAO;
    }

    @Override
    public Need getNeedById(int id) throws IOException {
        return needsCupboardDAO.getNeed(id);
    }

    @Override
    public List<Need> getAllNeeds() throws IOException {
        return Arrays.asList(needsCupboardDAO.getNeeds());
    }

    @Override
    public List<Need> searchNeeds(String text) throws IOException {
        return Arrays.asList(needsCupboardDAO.searchNeeds(text));
    }

    @Override
    public Need createNeed(Need need) throws IOException {
        return needsCupboardDAO.createdNeed(need);
    }

    @Override
    public Need updateNeed(Need need) throws IOException {
        return needsCupboardDAO.updateNeed(need);
    }

    @Override
    public boolean deleteNeedById(int id) throws IOException {
        return needsCupboardDAO.deleteNeed(id);
    }

     @Override
    public boolean deleteNeedByType(String type) throws IOException {
        return needsCupboardDAO.deleteNeedType(type);
    }

     @Override
    public boolean deleteAllNeeds() throws IOException {
        return needsCupboardDAO.deleteAll();
    }

     @Override
    public boolean deleteNeedByName(String name) throws IOException {
        return needsCupboardDAO.deleteNeedName(name);
    }

}
