package com.napier.sem.unittests;

import com.napier.sem.App;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class AppIntegrationTest {
    static App app;

    @BeforeAll
    static void init() {
        //not working yet problem connection to db
       // app = App.getConnection();
    }


        @Test
    void testWorldPopulation() {

      //  long population = app.getWorldPopulation();
      //  assertEquals(6078749450L, population);

    }
}
