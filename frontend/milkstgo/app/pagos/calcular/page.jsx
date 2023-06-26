"use client";

import "@/styles/calcular-pagos.css";

import SelectQuincena from "@/components/SelectQuincena";
import Title from "@/components/Title";
import PagosTable from "@/components/PagosTable";
import PagoService from "@/services/PagoService";
import FeedbackAlert from "@/components/FeedbackAlert";
import { feedbackTypes } from "@/components/FeedbackAlert";
import { useState } from "react";
import Loading from "../loading";

export default function CalcularPagosPage() {
    const [pagos, setPagos] = useState([]);
    const [quincena, setQuincena] = useState({});

    const [feedback, setFeedback] = useState("");
    const [alertType, setAlertType] = useState("");
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [loading, setLoading] = useState(false);

    const handleQuincenaChange = (year, month, fortnight) => {
        setQuincena({ year, month, fortnight });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setFeedback("");
        setLoading(true);
        try {
            const resCalcular = await PagoService.calcular(
                quincena.year,
                quincena.month,
                quincena.fortnight
            );

            setTimeout(() => {
                setFeedback(resCalcular.data);
                setAlertType(feedbackTypes.Informative);
                setIsSubmitting(false);
                setLoading(false);
            }, 500);

            const resPagos = await PagoService.getAllByQuincena(
                quincena.year,
                quincena.month,
                quincena.fortnight
            );

            if (resPagos.data) {
                setPagos(resPagos.data);
            }
        } catch (error) {
            setPagos([]);
            setAlertType(feedbackTypes.Error);
            if (error.response === undefined || error.response.status >= 500) {
                setTimeout(() => {
                    setFeedback(
                        "Ocurrió un error al intentar calcular los pagos"
                    );
                    setIsSubmitting(false);
                    setLoading(false);
                }, 500);
            } else {
                setTimeout(() => {
                    setFeedback(error.response.data);
                    setIsSubmitting(false);
                    setLoading(false);
                }, 500);
            }
        }
    };

    return (
        <>
            <Title title="Calcular Planilla de Pagos" />
            {feedback ? (
                <FeedbackAlert feedback={feedback} type={alertType} />
            ) : null}
            <div className="top-content">
                <form className="select-quincena" onSubmit={handleSubmit}>
                    <SelectQuincena
                        startYear={2020}
                        onChange={handleQuincenaChange}
                    />
                    <button type="submit">Calcular</button>
                </form>
            </div>
            {loading ? (
                <Loading />
            ) : (
                <>{pagos.length > 0 ? <PagosTable pagos={pagos} /> : null}</>
            )}
        </>
    );
}
