"use client"

import "@/styles/calcular-pagos.css"

import SelectQuincena from "@/components/SelectQuincena";
import Title from "@/components/Title";
import PagosTable from "@/components/PagosTable";
import PagoService from "@/services/PagoService";
import { useState } from "react";

export default function CalcularPagosPage() {
    const [pagos, setPagos] = useState([])
    const [quincena, setQuincena] = useState({})

    const handleQuincenaChange = (year, month, fortnight) => {
        setQuincena({year, month, fortnight})
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try{
            const resCalcular = await PagoService.calcular(quincena.year, quincena.month, quincena.fortnight)
            console.log(resCalcular); // Implementar alerta
            const resPagos = await PagoService.getAllByQuincena(quincena.year, quincena.month, quincena.fortnight)
            if(resPagos.data) {
                setPagos(resPagos.data);
            }
        }
        catch(error) {
            console.log(error); // Implementar alerta
            setPagos([])
        }
    }

    return (
        <>
            <Title title="Calcular Planilla de Pagos" />
            <div className="top-content">
                <form className="select-quincena" onSubmit={handleSubmit}>
                    <SelectQuincena
                        startYear={2020}
                        onChange={handleQuincenaChange}
                    />
                    <button type="submit">Calcular</button>
                </form>
            </div>
            <PagosTable pagos={pagos}/>
        </>
    );
}
