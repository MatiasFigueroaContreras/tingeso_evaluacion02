"use client"

import Title from "@/components/Title";
import UploadCard from "@/components/UploadCard";
import AcopioLecheService from "@/services/AcopioLecheService";

export default function ImportarAcopioPage() {
    const handleSubmit = async (data) => {
        console.log(data);
        try {
            const response = await AcopioLecheService.import(
                data.file,
                data.year,
                data.month,
                data.fortnight
            );
            console.log(response)
        } catch (e) {
            console.log(e);
        }
    };

    return (
        <>
            <Title title="Subir datos Acopios de Leche" />
            <UploadCard
                title="Subir archivo con los kilos de leche"
                startYear={2020}
                onSubmit={handleSubmit}
            />
        </>
    );
}
