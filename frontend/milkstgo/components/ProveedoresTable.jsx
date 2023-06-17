import "@/styles/proveedores-table.css"

export default function ProveedoresTable({proveedores = []}) {
    return (
        <table className="proveedores-table">
            <thead>
                <tr>
                    <th>Código</th>
                    <th>Nombre</th>
                    <th>Categoría</th>
                    <th>Afecto a Retención</th>
                </tr>
            </thead>
            <tbody>
                {proveedores.map((proveedor, index) => (
                    <tr key={index}>
                        <td>{proveedor.codigo}</td>
                        <td>{proveedor.nombre}</td>
                        <td>{proveedor.categoria}</td>
                        <td>{proveedor.retencion}</td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
}
