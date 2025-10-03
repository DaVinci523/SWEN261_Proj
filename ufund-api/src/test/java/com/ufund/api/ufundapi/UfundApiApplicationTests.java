package com.ufund.api.ufundapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class UfundApiApplicationTests {

	 @MockBean
    private com.ufund.api.ufundapi.persistence.NeedsCupboardDAO needsCupboardDAO;

    @org.junit.jupiter.api.Test
	void contextLoads() {
	}

}
