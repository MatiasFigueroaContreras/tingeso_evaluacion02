"use client";

import Title from "@/components/Title";
import UploadCard from "@/components/UploadCard";
import LaboratorioLecheService from "@/services/LaboratorioLecheService";

export default function ImportarLaboratorioLeche() {
    const handleSubmit = async (data) => {
        console.log(data);
        try{
            const response = await LaboratorioLecheService.import(
                data.file,
                data.year,
                data.month,
                data.fortnight
            );
            console.log(response);
        }
        catch(e) {
            console.log(e)
        }

    };

    return (
        <>
            <Title title="Subir datos Grasas y Solidos Totales" />
            <UploadCard
                title="Subir archivo con grasa y solidos totales"
                startYear={2020}
                onSubmit={handleSubmit}
            />
        </>
    );
}
