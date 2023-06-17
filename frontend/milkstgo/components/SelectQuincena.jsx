"use client";

import { useEffect, useState } from "react";

export default function SelectQuincena({ startYear, onChange }) {
    const currentDate = new Date();
    const currentYear = currentDate.getFullYear();
    const currentMonth = currentDate.getMonth() + 1;
    const currentDay = currentDate.getDate();
    const standardMonthOptions = getMonthOptions(12);
    const standardFortnightsOptions = getFortnightOptions(2);
    const finalMonth =
        currentDay < 13
            ? currentMonth == 1
                ? 12
                : currentMonth - 1
            : currentMonth;
    const currentMonthOptions = getMonthOptions(finalMonth);
    const finalFortnight = currentDay >= 27 || currentDay < 13 ? 2 : 1;
    const currentFortnightOptions = getFortnightOptions(finalFortnight);

    // States
    const [year, setYear] = useState(currentYear);
    const [month, setMonth] = useState(finalMonth);
    const [fortnight, setFortnight] = useState(finalFortnight);
    const [monthOptions, setMonthOptions] = useState(currentMonthOptions);
    const [fortnightOptions, setFortnightOptions] = useState(
        currentFortnightOptions
    );
    
    useEffect(() => {
        onChange(year, month, fortnight);
    }, [])

    // Recibe el numero del mes (1-12) y lo transforma a texto
    function getMonthName(monthNumber) {
        const date = new Date();
        date.setDate(1); // Para prevenir error con meses
        date.setMonth(monthNumber - 1);
        return date.toLocaleString("es-US", { month: "long" });
    }

    function getFortnightOptions(finalFortnight) {
        const possibleFortnights = ["Primera", "Segunda"];
        const fortnights = [];
        for (let fortnight = 1; fortnight <= finalFortnight; fortnight++) {
            fortnights.push(
                <option key={fortnight} value={fortnight}>
                    {possibleFortnights[fortnight - 1]}
                </option>
            );
        }

        return fortnights;
    }

    function getMonthOptions(finalMonth) {
        const months = [];
        for (let month = 1; month <= finalMonth; month++) {
            months.push(
                <option key={month} value={month}>
                    {getMonthName(month)}
                </option>
            );
        }
        return months;
    }

    const handleYearChange = (e) => {
        const yearSelected = Number(e.target.value);
        if (yearSelected == currentYear) {
            setMonthOptions(currentMonthOptions);
            if (finalMonth <= month) {
                setMonth(finalMonth);
                setFortnightOptions(currentFortnightOptions);
            }

        } else {
            setMonthOptions(standardMonthOptions);
            setFortnightOptions(standardFortnightsOptions)
        }

        setYear(yearSelected);
        onChange(yearSelected, month, fortnight);
    };

    const handleMonthChange = (e) => {
        const monthSelected = Number(e.target.value);
        if (monthSelected == finalMonth && year == currentYear) {
            setFortnightOptions(currentFortnightOptions);
            setFortnight(finalFortnight);
        } else {
            setFortnightOptions(standardFortnightsOptions);
        }

        setMonth(monthSelected);
        onChange(year, monthSelected, fortnight);
    };

    const handleFortnightChange = (e) => {
        const fortnightSelected = Number(e.target.value);
        setFortnight(fortnightSelected);
        onChange(year, month, fortnightSelected);
    }

    const renderYearOptions = () => {
        const yearOptions = [];
        for (let year = currentYear; startYear <= year; year--) {
            yearOptions.push(
                <option key={year} value={year}>
                    {year}
                </option>
            );
        }
        return yearOptions;
    };

    return (
        <>
            <select
                name="year"
                id="year"
                required
                onChange={handleYearChange}
                value={year}
            >
                <option value="" disabled>
                    Año
                </option>
                {renderYearOptions()}
            </select>
            <select
                name="mes"
                id="mes"
                required
                onChange={handleMonthChange}
                value={month}
            >
                <option value="" disabled>
                    Mes
                </option>
                {monthOptions}
            </select>
            <select
                name="quincena"
                id="quincena"
                required
                onChange={handleFortnightChange}
                value={fortnight}
            >
                <option value="" disabled>
                    Quincena
                </option>
                {fortnightOptions}
            </select>
        </>
    );
}
