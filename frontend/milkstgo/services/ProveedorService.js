import axios from "axios";

const PROVEEDOR_API_URL = `${process.env.NEXT_PUBLIC_GATEWAY_URL}/proveedores`;

class ProveedorService {
    async create(codigo, nombre, categoria, retencion) {
        return axios.post(
            PROVEEDOR_API_URL,
            {
                codigo: codigo,
                nombre: nombre,
                categoria: categoria,
                retencion: retencion,
            },
            {
                headers: { "Content-Type": "multipart/form-data" },
            }
        );
    }

    async getAll() {
        return axios.get(PROVEEDOR_API_URL);
    }
}

export default new ProveedorService();
