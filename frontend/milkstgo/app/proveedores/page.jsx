"use client";

import ProveedoresTable from "@/components/ProveedoresTable";
import Title from "@/components/Title";
import ProveedorService from "@/services/ProveedorService";
import { useState, useEffect, Suspense } from "react";
import Loading from "./loading";

export default function ProveedoresPage() {
    const [proveedores, setProveedores] = useState([]);

    useEffect(() => {
        const fetchProveedores = async () => {
            try {
                const response = await ProveedorService.getAll();
                setProveedores(response.data);
            } catch (error) {
                console.log(error);
                setProveedores([]);
            }
        };

        fetchProveedores();
    }, []);

    return (
        <>
            <Title title="Proveedores Registrados" />
            <Suspense fallback={<Loading />}>
                <ProveedoresTable proveedores={proveedores} />
            </Suspense>
        </>
    );
}
