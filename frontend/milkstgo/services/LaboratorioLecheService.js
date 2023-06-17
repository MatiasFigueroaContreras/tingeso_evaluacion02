import axios from "axios";

const LABORTORIO_LECHE_API_URL = "http://localhost:8080/laboratorio-leche";

class LaboratorioLecheService {
    async import(file, year, mes, quincena) {
        return axios.post(
            LABORTORIO_LECHE_API_URL + "/importar",
            {
                file: file,
                year: year,
                mes: mes,
                quincena: quincena,
            },
            {
                headers: { "Content-Type": "multipart/form-data" },
            }
        );
    }
}

export default new LaboratorioLecheService();
