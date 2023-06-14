package tingeso.pagoservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso.pagoservice.config.RestTemplateConfig;
import tingeso.pagoservice.entities.*;
import tingeso.pagoservice.repositories.DatosCentroAcopioRepository;

import java.util.*;

@Service
public class DatosCentroAcopioService {
    @Autowired
    DatosCentroAcopioRepository datosCentroAcopioRepository;
    @Autowired
    RestTemplateConfig restTemplate;


    public void guardarDatosCA(DatosCentroAcopioEntity datosCentroAcopio) {
        String id = datosCentroAcopio.getCodigoProveedor() + "-" + datosCentroAcopio.getQuincena().toString();
        datosCentroAcopio.setId(id);
        datosCentroAcopioRepository.save(datosCentroAcopio);
    }

    public void guardarListaDatosCA(List<DatosCentroAcopioEntity> listaDatosCa) {
        for (DatosCentroAcopioEntity datosCa : listaDatosCa) {
            guardarDatosCA(datosCa);
        }
    }

    public DatosCentroAcopioEntity obtenerDatosCAPorProveedorQuincena(String proveedor, String quincena) {
        Optional<DatosCentroAcopioEntity> datosCa = datosCentroAcopioRepository.findByCodigoProveedorAndQuincena(proveedor, quincena);
        if (!datosCa.isPresent()) {
            throw new IllegalArgumentException("No se encontraron los datos del centro de acopio, para el proveedor y quincena dados");
        }
        return datosCa.get();
    }

    public boolean existenDatosCAParaCalculoPorQuincena(String quincena) {
        return acopioLecheService.existenAcopiosLechePorQuincena(quincena) &&
                grasaSolidoTotalService.existeGrasaSolidoTotalPorQuincena(quincena);
    }

    public DatosCentroAcopioEntity calcularDatosCAPorProveedorQuincena(String codigoProveedor, String quincena) {
        DatosCentroAcopioEntity datosCentroAcopio = new DatosCentroAcopioEntity();
        datosCentroAcopio.setCodigoProveedor(codigoProveedor);
        datosCentroAcopio.setQuincena(quincena);
        calcularDatosAcopioLeche(datosCentroAcopio);
        GrasaSolidoTotalEntity grasaSolidoTotal;
        if(datosCentroAcopio.getTotalKlsLeche() == 0){
            //Se asigna 0 porcentajes si no han entregado datos para algun proveedor, considerando que este no envio leche.
            grasaSolidoTotalService.guardarGrasaSolidoTotal(new GrasaSolidoTotalEntity("", 0, 0, codigoProveedor, quincena));
        }
        grasaSolidoTotal = grasaSolidoTotalService.obtenerGrasaSolidoTotalPorProveedorQuincena(proveedor, quincena);
        datosCentroAcopio.setGrasaSolidoTotal(grasaSolidoTotal);
        calcularVariacionesDatosCA(datosCentroAcopio);
        return datosCentroAcopio;
    }

    public List<DatosCentroAcopioEntity> calcularDatosCAPorQuincena(QuincenaEntity quincena) {
        List<Proveedor> proveedores = proveedorService.obtenerProveedores();
        List<DatosCentroAcopioEntity> listaDatosCa = new ArrayList<>();
        for (ProveedorEntity proveedor : proveedores) {
            DatosCentroAcopioEntity datosCaProveedor = calcularDatosCAPorProveedorQuincena(proveedor, quincena);
            listaDatosCa.add(datosCaProveedor);
        }

        return listaDatosCa;
    }

    public void calcularDatosAcopioLeche(DatosCentroAcopioEntity datosCentroAcopio) {
        List<AcopioLecheEntity> acopiosLeche = acopioLecheService.obtenerAcopiosLechePorProveedorQuincena(datosCentroAcopio.getProveedor(), datosCentroAcopio.getQuincena());
        Integer nDiasEnvioMT = 0;
        Integer nDiasEnvioM = 0;
        Integer nDiasEnvioT;
        Integer totalKlsLeche = 0;
        HashMap<Integer, Boolean> diasEnviosM = new HashMap<>();
        HashMap<Integer, Boolean> diasEnviosT = new HashMap<>();
        for (AcopioLecheEntity acopioLeche : acopiosLeche) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(acopioLeche.getFecha());
            Integer dia = calendar.get(Calendar.DAY_OF_MONTH);
            String turno = acopioLeche.getTurno();
            if (turno.equals("M")) {
                diasEnviosM.put(dia, Boolean.TRUE);
            } else {
                diasEnviosT.put(dia, Boolean.TRUE);
            }
            totalKlsLeche += acopioLeche.getCantidadLeche();
        }

        for (Integer diaEnvioM : diasEnviosM.keySet()) {
            if (diasEnviosT.containsKey(diaEnvioM)) {
                nDiasEnvioMT++;
            } else {
                nDiasEnvioM++;
            }
        }
        nDiasEnvioT = diasEnviosT.size() - nDiasEnvioMT;
        datosCentroAcopio.setDiasEnvioMyT(nDiasEnvioMT);
        datosCentroAcopio.setDiasEnvioM(nDiasEnvioM);
        datosCentroAcopio.setDiasEnvioT(nDiasEnvioT);
        datosCentroAcopio.setTotalKlsLeche(totalKlsLeche);
    }

    public void calcularVariacionesDatosCA(DatosCentroAcopioEntity datosCentroAcopio) {
        Integer variacionLeche = 0;
        Integer variacionGrasa = 0;
        Integer variacionSolidoTotal = 0;
        GrasaSolidoTotalEntity grasaSolidoTotal = datosCentroAcopio.getGrasaSolidoTotal();
        QuincenaEntity quincenaAnterior = datosCentroAcopio.getQuincena().obtenerQuincenaAnterior();
        try {
            DatosCentroAcopioEntity datosCaAnterior = obtenerDatosCAPorProveedorQuincena(datosCentroAcopio.getProveedor(), quincenaAnterior);
            GrasaSolidoTotalEntity grasaStAnterior = datosCaAnterior.getGrasaSolidoTotal();
            if(datosCaAnterior.getTotalKlsLeche() != 0){
                variacionLeche = (datosCentroAcopio.getTotalKlsLeche() / datosCaAnterior.getTotalKlsLeche() - 1) * 100;
            }
            if(grasaStAnterior.getPorcentajeGrasa() != 0){
                variacionGrasa = (grasaSolidoTotal.getPorcentajeGrasa() / grasaStAnterior.getPorcentajeGrasa() - 1) * 100;
            }
            if(grasaStAnterior.getPorcentajeSolidoTotal() != 0){
                variacionSolidoTotal = (grasaSolidoTotal.getPorcentajeSolidoTotal() / grasaStAnterior.getPorcentajeSolidoTotal() - 1) * 100;
            }
        } catch (Exception e) {
            //No existen datos del centro acopio anteriormente.
            if (acopioLecheService.existenAcopiosLechePorQuincena(quincenaAnterior) ||
                    grasaSolidoTotalService.existeGrasaSolidoTotalPorQuincena(quincenaAnterior)) {
                throw new IllegalArgumentException("Existen pagos del centro de acopio no calculados para la quincena anterior");
            }
        }
        datosCentroAcopio.setVariacionLeche(variacionLeche);
        datosCentroAcopio.setVariacionGrasa(variacionGrasa);
        datosCentroAcopio.setVariacionSolidoTotal(variacionSolidoTotal);
    }
}
