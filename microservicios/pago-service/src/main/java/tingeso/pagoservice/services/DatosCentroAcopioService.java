package tingeso.pagoservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tingeso.pagoservice.entities.*;
import tingeso.pagoservice.models.AcopioLeche;
import tingeso.pagoservice.models.LaboratorioLeche;
import tingeso.pagoservice.models.Proveedor;
import tingeso.pagoservice.models.Quincena;
import tingeso.pagoservice.repositories.DatosCentroAcopioRepository;

import java.util.*;

@Service
public class DatosCentroAcopioService {
    @Autowired
    DatosCentroAcopioRepository datosCentroAcopioRepository;
    @Autowired
    RestTemplate restTemplate;
    String LABORATORIO_LECHE_URL = "http://laboratorio-leche-service/laboratorio-leche";
    String PROVEEDORES_URL = "http://proveedor-service/proveedores";
    String ACOPIO_LECHE_URL = "http://acopio-leche-service/acopio-leches";


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
        Quincena quincenaObj = Quincena.stringToQuincena(quincena);
        Map<String, String> params = quincenaObj.toMap();
        Boolean existeAcopio = restTemplate.getForObject(ACOPIO_LECHE_URL + "/exists/byquincena?year={year}&mes={mes}&quincena={quincena}", Boolean.class, params);
        Boolean existeLaboratorio = restTemplate.getForObject(LABORATORIO_LECHE_URL + "/exists/byquincena?year={year}&mes={mes}&quincena={quincena}", Boolean.class, params);
        return existeAcopio && existeLaboratorio;
    }

    public DatosCentroAcopioEntity calcularDatosCAPorProveedorQuincena(Proveedor proveedor, String quincena) {
        DatosCentroAcopioEntity datosCentroAcopio = new DatosCentroAcopioEntity();
        datosCentroAcopio.setProveedor(proveedor);
        datosCentroAcopio.setCodigoProveedor(proveedor.getCodigo());
        datosCentroAcopio.setQuincena(quincena);
        calcularDatosAcopioLeche(datosCentroAcopio);
        LaboratorioLeche laboratorioLeche;
        if(datosCentroAcopio.getTotalKlsLeche() == 0){
            //Se asigna 0 porcentajes si no han entregado datos para algun proveedor, considerando que este no envio leche.
            LaboratorioLeche labLeche = new LaboratorioLeche("", 0, 0, proveedor.getCodigo(), quincena);
            HttpEntity<LaboratorioLeche> request = new HttpEntity<>(labLeche);
            restTemplate.postForObject(LABORATORIO_LECHE_URL, request, String.class);
        }
        Map<String, String> params = Quincena.stringToQuincena(quincena).toMap();
        params.put("codigoProveedor", proveedor.getCodigo());
        laboratorioLeche = restTemplate.getForObject(LABORATORIO_LECHE_URL + "/byproveedor-quincena?codigoProveedor={codigoProveedor}&year={year}&mes={mes}&quincena={quincena}", LaboratorioLeche.class, params);
        datosCentroAcopio.setIdLaboratorioLeche(laboratorioLeche.getId());
        datosCentroAcopio.setLaboratorioLeche(laboratorioLeche);
        calcularVariacionesDatosCA(datosCentroAcopio);
        return datosCentroAcopio;
    }

    public List<DatosCentroAcopioEntity> calcularDatosCAPorQuincena(String quincena) {
        Proveedor[] proveedores = restTemplate.getForObject(PROVEEDORES_URL, Proveedor[].class);

        List<DatosCentroAcopioEntity> listaDatosCa = new ArrayList<>();
        for (Proveedor proveedor : proveedores) {
            DatosCentroAcopioEntity datosCaProveedor = calcularDatosCAPorProveedorQuincena(proveedor, quincena);
            listaDatosCa.add(datosCaProveedor);
        }

        return listaDatosCa;
    }

    public void calcularDatosAcopioLeche(DatosCentroAcopioEntity datosCentroAcopio) {
        Map<String, String> params = Quincena.stringToQuincena(datosCentroAcopio.getQuincena()).toMap();
        params.put("codigoProveedor", datosCentroAcopio.getCodigoProveedor());
        AcopioLeche[] acopiosLeche = restTemplate.getForObject(ACOPIO_LECHE_URL + "/byproveedor-quincena?codigoProveedor={codigoProveedor}&year={year}&mes={mes}&quincena={quincena}", AcopioLeche[].class, params);
        Integer nDiasEnvioMT = 0;
        Integer nDiasEnvioM = 0;
        Integer nDiasEnvioT;
        Integer totalKlsLeche = 0;
        HashMap<Integer, Boolean> diasEnviosM = new HashMap<>();
        HashMap<Integer, Boolean> diasEnviosT = new HashMap<>();
        for (AcopioLeche acopioLeche : acopiosLeche) {
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
        LaboratorioLeche laboratorioLeche = datosCentroAcopio.getLaboratorioLeche();
        Quincena quincena = Quincena.stringToQuincena(datosCentroAcopio.getQuincena());
        Quincena quincenaAnterior = quincena.obtenerQuincenaAnterior();
        try {
            DatosCentroAcopioEntity datosCaAnterior = obtenerDatosCAPorProveedorQuincena(datosCentroAcopio.getCodigoProveedor(), quincenaAnterior.toString());
            LaboratorioLeche laboratorioLecheAnterior = restTemplate.getForObject(LABORATORIO_LECHE_URL + "/" + datosCaAnterior.getIdLaboratorioLeche(), LaboratorioLeche.class);
            if(datosCaAnterior.getTotalKlsLeche() != 0){
                variacionLeche = (datosCentroAcopio.getTotalKlsLeche() / datosCaAnterior.getTotalKlsLeche() - 1) * 100;
            }
            if(laboratorioLecheAnterior.getPorcentajeGrasa() != 0){
                variacionGrasa = (laboratorioLeche.getPorcentajeGrasa() / laboratorioLecheAnterior.getPorcentajeGrasa() - 1) * 100;
            }
            if(laboratorioLecheAnterior.getPorcentajeSolidoTotal() != 0){
                variacionSolidoTotal = (laboratorioLeche.getPorcentajeSolidoTotal() / laboratorioLecheAnterior.getPorcentajeSolidoTotal() - 1) * 100;
            }
        } catch (Exception e) {
            //No existen datos del centro acopio anteriormente.
            Map<String, String> params = quincenaAnterior.toMap();
            Boolean existeAcopio = restTemplate.getForObject(ACOPIO_LECHE_URL + "/exists/byquincena?year={year}&mes={mes}&quincena={quincena}", Boolean.class, params);
            Boolean existeLaboratorio = restTemplate.getForObject(LABORATORIO_LECHE_URL + "/exists/byquincena?year={year}&mes={mes}&quincena={quincena}", Boolean.class, params);
            if (existeAcopio || existeLaboratorio) {
                throw new IllegalArgumentException("Existen pagos del centro de acopio no calculados para la quincena anterior");
            }
        }
        datosCentroAcopio.setVariacionLeche(variacionLeche);
        datosCentroAcopio.setVariacionGrasa(variacionGrasa);
        datosCentroAcopio.setVariacionSolidoTotal(variacionSolidoTotal);
    }
}
