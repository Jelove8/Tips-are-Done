package com.example.tipsaredone.model

import java.time.LocalDate

class MockData() {

    private val brandon = Employee("00000001","Brandon Lane")
    private val brandonTipReports: MutableList<IndividualTipReport> = mutableListOf(
        IndividualTipReport("Brandon Lane","00000001",25.16,37.00,LocalDate.parse("2022-07-01"),LocalDate.parse("2022-07-08"),null,true),
        IndividualTipReport("Brandon Lane","00000001",23.76,32.00,LocalDate.parse("2022-07-09"),LocalDate.parse("2022-07-16"),null,true),
        IndividualTipReport("Brandon Lane","00000001",31.76,55.00,LocalDate.parse("2022-07-17"),LocalDate.parse("2022-07-24"),null,false),
        IndividualTipReport("Brandon Lane","00000001",26.54,39.00,LocalDate.parse("2022-07-25"),LocalDate.parse("2022-08-01"),null,false),
    )

    private val jane = Employee("00000000","Jane Lane")
    private val janeTipReports: MutableList<IndividualTipReport> = mutableListOf(
        IndividualTipReport("Jane Lane","00000000",42.31,63.00,LocalDate.parse("2022-07-01"),LocalDate.parse("2022-07-08"),null,true),
        IndividualTipReport("Jane Lane","00000000",40.43,54.00,LocalDate.parse("2022-07-09"),LocalDate.parse("2022-07-16"),null,true),
        IndividualTipReport("Jane Lane","00000000",46.89,81.00,LocalDate.parse("2022-07-17"),LocalDate.parse("2022-07-24"),null,true),
        IndividualTipReport("Jane Lane","00000000",42.78,63.00,LocalDate.parse("2022-07-25"),LocalDate.parse("2022-08-01"),null,true),
    )

    private val tahir = Employee("00000003","Tahir Atkinson")
    private val tahirTipReports: MutableList<IndividualTipReport> = mutableListOf(
        IndividualTipReport("Tahir Atkinson","00000003",16.29,24.00,LocalDate.parse("2022-07-01"),LocalDate.parse("2022-07-08"),null,true),
        IndividualTipReport("Tahir Atkinson","00000003",16.08,21.00,LocalDate.parse("2022-07-09"),LocalDate.parse("2022-07-16"),null,true),
        IndividualTipReport("Tahir Atkinson","00000003",25.09,43.00,LocalDate.parse("2022-07-17"),LocalDate.parse("2022-07-24"),null,true),
        IndividualTipReport("Tahir Atkinson","00000003",17.88,26.00,LocalDate.parse("2022-07-25"),LocalDate.parse("2022-08-01"),null,true),
    )

    private val taja = Employee("00000002","Taja Parker")
    private val tajaTipReports: MutableList<IndividualTipReport> = mutableListOf(
        IndividualTipReport("Taja Parker","00000002",40.15,60.00,LocalDate.parse("2022-07-01"),LocalDate.parse("2022-07-08"),null,true),
        IndividualTipReport("Taja Parker","00000002",38.79,52.00,LocalDate.parse("2022-07-09"),LocalDate.parse("2022-07-16"),null,true),
        IndividualTipReport("Taja Parker","00000002",42.46,73.00,LocalDate.parse("2022-07-17"),LocalDate.parse("2022-07-24"),null,true),
        IndividualTipReport("Taja Parker","00000002",41.17,60.00,LocalDate.parse("2022-07-25"),LocalDate.parse("2022-08-01"),null,false),
    )

    fun getMockEmployees(): MutableList<Employee> {
        brandonTipReports.forEach {
            brandon.addTipReport(it)
        }
        janeTipReports.forEach {
            jane.addTipReport(it)
        }
        tahirTipReports.forEach {
            tahir.addTipReport(it)
        }
        tajaTipReports.forEach {
            taja.addTipReport(it)
        }

        return mutableListOf(brandon,jane,tahir,taja)
    }

}