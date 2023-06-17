"use client";

import { useState, useEffect } from "react";
import PagosTable from "@/components/PagosTable";
import Title from "@/components/Title";
import PagoService from "@/services/PagoService";

export default function PagosPage() {
    const [pagos, setPagos] = useState([]);

    useEffect(() => {
        const fetchPagos = async () => {
            try {
                const response = await PagoService.getAll();
                if (response.data) {
                    setPagos(response.data);
                }
            } catch (error) {
                console.log(error);
                setPagos([]);
            }
        };

        fetchPagos();
    }, []);

    return (
        <>
            <Title title="Planilla de Pagos" />
            <PagosTable pagos={pagos} />
        </>
    );
}
