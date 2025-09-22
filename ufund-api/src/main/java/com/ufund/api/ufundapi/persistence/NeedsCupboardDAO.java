package com.ufund.api.ufundapi.persistence;

import java.io.IOException;
import com.ufund.api.ufundapi.model.Need;

/**
 * Interfacing for Need object
 * @author CTRL
 */
public interface NeedsCupboardDAO {

    Need[] getNeeds() throws IOException;

    Need[] searchNeeds() throws IOException;

    Need getNeed(int id) throws IOException;

    Need createdNeed(Need need) throws IOException;

    Need updateNeed(Need need) throws IOException;

    boolean deleteNeed(int id) throws IOException;






















}