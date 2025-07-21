package com.napier.sem.tests;

import com.napier.sem.App;
import com.napier.sem.CityReport;
import com.napier.sem.CountryReport;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;


class AppTest {

    @Test
    void testPrintCityReport() {
        // Arrange
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        CityReport report = new CityReport(
                "Shanghai",
                "China",
                "Shanghai",
                24256800
        );

        App app = new App(null);

        // Act
        app.PrintCityReport(report);

        // Assert
        String expected = "Shanghai | China | Shanghai | 24256800" + System.lineSeparator();
        assertEquals(expected, outputStreamCaptor.toString());
    }

    @Test
    void testPrintCountryReport() {
        // Arrange
        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));


        CountryReport report = new CountryReport(
                "CHN",
                "China",
                "Asia",
                "Eastern Asia",
                1400000000,
                "Beijing"
        );
        App app = new App(null);

        // Act
        app.PrintCountryReport(report);

        // Assert
        String expected = "CHN | China | Asia | Eastern Asia | 1400000000 | Beijing" + System.lineSeparator();
        assertEquals(expected, outputStreamCaptor.toString());
    }


}