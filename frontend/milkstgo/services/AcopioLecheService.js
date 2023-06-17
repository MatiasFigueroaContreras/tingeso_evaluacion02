import axios from "axios";

const ACOPIO_LECHE_API_URL = "http://localhost:8080/acopio-leche";

class AcopioLecheService {
    async import(file, year, mes, quincena) {
        return axios.post(
            ACOPIO_LECHE_API_URL + "/importar",
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

export default new AcopioLecheService();
