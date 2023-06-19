"use client";

import Title from "@/components/Title";
import UploadCard from "@/components/UploadCard";
import AcopioLecheService from "@/services/AcopioLecheService";
import FeedbackAlert from "@/components/FeedbackAlert";
import { feedbackTypes } from "@/components/FeedbackAlert";
import { useState } from "react";

export default function ImportarAcopioPage() {
    const [feedback, setFeedback] = useState("");
    const [alertType, setAlertType] = useState("");
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleSubmit = async (data) => {
        try {
            await AcopioLecheService.import(
                data.file,
                data.year,
                data.month,
                data.fortnight
            );
            setFeedback("");
            setTimeout(() => {
                setFeedback("Se importaron correctamente los datos!");
                setIsSubmitting(false);
            }, 400);
            setAlertType(feedbackTypes.Success);
        } catch (error) {
            setFeedback("");
            setAlertType(feedbackTypes.Error);
            if (error.response.status >= 500) {
                setTimeout(() => {
                    setFeedback("Ocurrió un error al intentar subir los datos");
                    setIsSubmitting(false);
                }, 400);
            } else {
                setTimeout(() => {
                    setFeedback(error.response.data);
                    setIsSubmitting(false);
                }, 400);
            }
        }
    };

    return (
        <>
            <Title title="Subir datos Acopios de Leche" />
            {feedback ? (
                <FeedbackAlert feedback={feedback} type={alertType} />
            ) : null}
            <UploadCard
                title="Subir archivo con los kilos de leche"
                startYear={2020}
                onSubmit={handleSubmit}
            />
        </>
    );
}
