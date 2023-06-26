import axios from "axios";

const PAGO_API_URL = `${process.env.NEXT_PUBLIC_GATEWAY_URL}/pagos`;

class PagoService {
    async calcular(year, mes, quincena) {
        return axios.post(
            PAGO_API_URL + "/calcular",
            {
                year: year,
                mes: mes,
                quincena: quincena,
            },
            {
                headers: { "Content-Type": "multipart/form-data" },
            }
        );
    }

    async getAll() {
        return axios.get(PAGO_API_URL);
    }

    async getAllByQuincena(year, mes, quincena) {
        return axios.get(PAGO_API_URL + "/byquincena", {
            headers: { "Content-Type": "multipart/form-data" },
            params: {
                year: year,
                mes: mes,
                quincena: quincena,
            },
        });
    }
}

export default new PagoService();
