"use client";

import "@/styles/register-page.css";

import Title from "@/components/Title";
import InputField from "@/components/InputField";
import SelectField from "@/components/SelectField";
import ProveedorService from "@/services/ProveedorService";
import { useState } from "react";

export default function RegisterPage() {
    const [nombre, setNombre] = useState("");
    const [codigo, setCodigo] = useState("");
    const [categoria, setCategoria] = useState("");
    const [retencion, setRetencion] = useState("");

    const categorias = ["A", "B", "C", "D"];
    const retenciones = ["Si", "No"];
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await ProveedorService.create(
                codigo,
                nombre,
                categoria,
                retencion
            );
            // Cambiar
            console.log(response);
            alert("Proveedor registrado correctamente!");
        } catch (e) {
            // Cambiar
            console.log(e);
        }
    };

    return (
        <>
            <Title title="Registro de Proveedor" />
            <form onSubmit={handleSubmit} className="form">
                <InputField
                    name="nombre"
                    type="text"
                    placeholder="Nombre"
                    onChange={(e) => setNombre(e.target.value)}
                />
                <InputField
                    name="codigo"
                    type="text"
                    placeholder="Código"
                    minLength="5"
                    maxLength="5"
                    onChange={(e) => setCodigo(e.target.value)}
                />
                <div className="selects">
                    <SelectField
                        name="categoria"
                        placeholder="Categoría"
                        options={categorias}
                        onChange={(e) => setCategoria(e.target.value)}
                    />
                    <SelectField
                        name="retencion"
                        placeholder="Afecto a Retención"
                        options={retenciones}
                        onChange={(e) => setRetencion(e.target.value)}
                    />
                </div>
                <button className="button-style" type="submit">
                    Registrar
                </button>
            </form>
        </>
    );
}
