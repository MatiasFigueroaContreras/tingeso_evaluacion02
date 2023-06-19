"use client";

import "@/styles/upload-card.css";

import { useRef, useState } from "react";
import SelectQuincena from "./SelectQuincena";

function UploadZone({ onFileUpload }) {
    const [dragOver, setDragOver] = useState(false);
    const [fileName, setFileName] = useState("");
    const [uploadedFiles, setUploadedFiles] = useState(null);
    const fileRef = useRef(null);

    const handleDragOver = (e) => {
        e.preventDefault();
        setDragOver(true);
    };

    const handleDragLeave = (e) => {
        e.preventDefault();
        if (!uploadedFiles) {
            setDragOver(false);
        }
    };

    const handleDrop = (e) => {
        e.preventDefault();
        if (e.dataTransfer.files.length) {
            const files = e.dataTransfer.files;
            const file = files[0];
            fileRef.current.files = files;
            setFileName(file.name);
            setUploadedFiles(files);
            onFileUpload(file);
        }
    };

    const handleInputChange = (e) => {
        const files = e.target.files;
        const file = files[0];
        if (file) {
            setFileName(file.name);
            setDragOver(true);
            setUploadedFiles(files);
            onFileUpload(file);
        } else if (uploadedFiles) {
            fileRef.current.files = uploadedFiles;
        }
    };

    return (
        <div
            className={`upload-zone ${dragOver ? "dragover" : ""}`}
            onDragOver={handleDragOver}
            onDragLeave={handleDragLeave}
            onDrop={handleDrop}
        >
            <input
                ref={fileRef}
                id="file"
                name="file"
                type="file"
                accept=".xlsx"
                required
                onChange={handleInputChange}
            />
            <svg
                xmlns="http://www.w3.org/2000/svg"
                width="80"
                height="80"
                viewBox="0 0 24 24"
            >
                <path
                    fill="#e27120"
                    d="M13 20H6a1 1 0 0 1-1-1V5a1 1 0 0 1 1-1h5v3a3 3 0 0 0 3 3h3v2a1 1 0 0 0 2 0V8.94a1.31 1.31 0 0 0-.06-.27v-.09a1.07 1.07 0 0 0-.19-.28l-6-6a1.07 1.07 0 0 0-.28-.19a.32.32 0 0 0-.09 0a.88.88 0 0 0-.33-.11H6a3 3 0 0 0-3 3v14a3 3 0 0 0 3 3h7a1 1 0 0 0 0-2Zm0-14.59L15.59 8H14a1 1 0 0 1-1-1ZM8 8a1 1 0 0 0 0 2h1a1 1 0 0 0 0-2Zm6 4H8a1 1 0 0 0 0 2h6a1 1 0 0 0 0-2Zm6.71 5.29l-2-2a1 1 0 0 0-.33-.21a1 1 0 0 0-.76 0a1 1 0 0 0-.33.21l-2 2a1 1 0 0 0 1.42 1.42l.29-.3V21a1 1 0 0 0 2 0v-2.59l.29.3a1 1 0 0 0 1.42 0a1 1 0 0 0 0-1.42ZM12 18a1 1 0 0 0 0-2H8a1 1 0 0 0 0 2Z"
                />
            </svg>
            <div className="label">
                <h3>Suelte su archivo aquí o</h3>
                <label htmlFor="file">
                    <b>Examine</b>
                </label>
            </div>
            <p>{fileName ? fileName : "El archivo debe ser .xlsx"}</p>
        </div>
    );
}

export default function UploadCard({ title, startYear, onSubmit, isSubmitting }) {
    const [data, setData] = useState({
        file: null,
        year: null,
        month: null,
        fortnight: null,
    });

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(data);
    };

    const handleQuincenaChange = (year, month, fortnight) => {
        handleDataChange({
            year: year,
            month: month,
            fortnight: fortnight,
        });
    };

    const handleDataChange = (newValues) => {
        setData((data) => ({
            ...data,
            ...newValues,
        }));
    };

    return (
        <form
            onSubmit={handleSubmit}
            className="upload-card"
            encType="multipart/form-data"
        >
            <div className="top-content">
                <p>{title}</p>
                <div className="selects">
                    <SelectQuincena
                        startYear={startYear}
                        onChange={handleQuincenaChange}
                    />
                </div>
            </div>
            <UploadZone
                onFileUpload={(uploadedFile) =>
                    handleDataChange({ file: uploadedFile })
                }
            />
            <button
                className="button-style"
                type="submit"
                disabled={isSubmitting}
            >
                Subir archivo
            </button>
        </form>
    );
}
