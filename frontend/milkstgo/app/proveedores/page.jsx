"use client";

import { useState, useEffect, Suspense } from "react";
import ProveedoresTable from "@/components/ProveedoresTable";
import Title from "@/components/Title";
import ProveedorService from "@/services/ProveedorService";
import FeedbackAlert from "@/components/FeedbackAlert";
import { feedbackTypes } from "@/components/FeedbackAlert";
import Loading from "./loading";

export default function ProveedoresPage() {
    const [proveedores, setProveedores] = useState([]);

    const [feedback, setFeedback] = useState("");
    const [alertType, setAlertType] = useState("");
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchProveedores = async () => {
            try {
                const response = await ProveedorService.getAll();
                if (response.data) {
                    setProveedores(response.data);
                    setFeedback("");
                    setAlertType("");
                } else {
                    setProveedores([]);
                    setFeedback("Aun no se han registrado proveedores");
                    setAlertType(feedbackTypes.Informative);
                }
            } catch (error) {
                setProveedores([]);
                setFeedback(
                    "Ocurrio un error al intentar buscar los proveedores"
                );
                setAlertType(feedbackTypes.Error);
            }
            setLoading(false);
        };

        fetchProveedores();
    }, []);

    return (
        <>
            <Title title="Proveedores Registrados" />
            {loading ? (
                <Loading />
            ) : (
                <>
                    {feedback ? (
                        <FeedbackAlert feedback={feedback} type={alertType} />
                    ) : (
                        <ProveedoresTable proveedores={proveedores} />
                    )}
                </>
            )}
        </>
    );
}
