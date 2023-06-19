"use client";

import { useState, useEffect } from "react";
import PagosTable from "@/components/PagosTable";
import Title from "@/components/Title";
import PagoService from "@/services/PagoService";
import FeedbackAlert from "@/components/FeedbackAlert";
import Loading from "./loading";
import { feedbackTypes } from "@/components/FeedbackAlert";

export default function PagosPage() {
    const [pagos, setPagos] = useState([]);
    const [feedback, setFeedback] = useState("");
    const [alertType, setAlertType] = useState("");
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        setFeedback("");
        setAlertType("");
        const fetchPagos = async () => {
            try {
                const response = await PagoService.getAll();
                if (response.data) {
                    setPagos(response.data);
                } else {
                    setPagos([]);
                    setFeedback("Aun no se han calculado pagos");
                    setAlertType(feedbackTypes.Informative);
                }
            } catch (error) {
                setPagos([]);
                setFeedback("Ocurrio un error al intentar buscar los pagos");
                setAlertType(feedbackTypes.Error);
            }
            setLoading(false);
        };

        fetchPagos();
    }, []);

    return (
        <>
            <Title title="Planilla de Pagos" />
            {loading ? (
                <Loading />
            ) : (
                <>
                    {feedback ? (
                        <FeedbackAlert feedback={feedback} type={alertType} />
                    ) : (
                        <PagosTable pagos={pagos} />
                    )}
                </>
            )}
        </>
    );
}
