package tingeso.acopiolecheservice.services;

import lombok.Generated;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import tingeso.acopiolecheservice.entities.AcopioLecheEntity;
import tingeso.acopiolecheservice.repositories.AcopioLecheRepository;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class AcopioLecheService {
    @Autowired
    AcopioLecheRepository acopioLecheRepository;
    @Autowired
    RestTemplate restTemplate;

    public void guardarAcopioLeche(AcopioLecheEntity acopioLeche) {
        String codigoProveedor = acopioLeche.getCodigoProveedor();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String fecha = dateFormat.format(acopioLeche.getFecha());
        String turno = acopioLeche.getTurno();
        String id = codigoProveedor + "-" + fecha + "-" + turno;
        acopioLeche.setId(id);
        acopioLecheRepository.save(acopioLeche);
    }

    public void guardarAcopiosLeches(List<AcopioLecheEntity> acopiosLeche) {
        for (AcopioLecheEntity acopioLeche : acopiosLeche) {
            guardarAcopioLeche(acopioLeche);
        }
    }

    public List<AcopioLecheEntity> obtenerAcopiosLechePorProveedorQuincena(String codigoProveedor, String quincena) {
        // Revisar
        return acopioLecheRepository.findAllByCodigoProveedorAndQuincena(codigoProveedor, quincena);
    }

    public boolean existenAcopiosLechePorQuincena(String quincena) {
        // Revisar
        return acopioLecheRepository.existsByQuincena(quincena);
    }

    public void validarListaAcopioLecheQuincena(List<AcopioLecheEntity> acopiosLeche, String quincena) {
        for (AcopioLecheEntity acopioLeche : acopiosLeche) {
            acopioLeche.setQuincena(quincena);
            validarAcopioLeche(acopioLeche);
        }
    }

    public void validarAcopioLeche(AcopioLecheEntity acopioLeche) {
        String turno = acopioLeche.getTurno();
        Integer klsLeche = acopioLeche.getCantidadLeche();
        Date fecha = acopioLeche.getFecha();
        String codigoProveedor = acopioLeche.getCodigoProveedor();
        Boolean existeProveedor = restTemplate.getForObject("http://proveedor-service/proveedores/exists/" + codigoProveedor, Boolean.class);
        // !!! Ver si usar un package para todos con las funciones de la quincena o un modelo en cada servicio
        // QuincenaEntity quincena = acopioLeche.getQuincena();
        if (!turno.equals("M") && !turno.equals("T")) {
            throw new IllegalArgumentException("Algun turno no es valido, debe ser M o T");
        }

        if (klsLeche < 0) {
            throw new IllegalArgumentException("Los kilos de leche tienen que ser positivos");
        }

        /*
        !!! Ver si usar un package para todos con las funciones de la quincena o un modelo en cada servicio
        if (!quincena.estaDentroQuincena(fecha)) {
            throw new IllegalArgumentException("Las fechas ingresadas tienen que coincidir con la quincena");
        }

         */

        if (!existeProveedor) {
            throw new IllegalArgumentException("Los proveedores tienen que estar registrados");
        }
    }

    @Generated
    public List<AcopioLecheEntity> leerExcel(MultipartFile file) {
        List<AcopioLecheEntity> acopiosLeche = new ArrayList<>();
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.endsWith(".xlsx")) {
            throw new IllegalArgumentException("El archivo ingresado no es un .xlsx");
        }

        XSSFWorkbook workBook;
        try {
            workBook = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException("El archivo ingresado no pudo ser leido");
        }

        XSSFSheet workSheet = workBook.getSheetAt(0);
        boolean rowVerification = true;

        for (Row row : workSheet) {
            if (rowVerification) {
                rowVerification = false;
                continue;
            }

            Iterator<Cell> cellIterator = row.iterator();
            int iCell = 0;
            AcopioLecheEntity acopioLeche = new AcopioLecheEntity();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                setValoresPorCelda(acopioLeche, cell, iCell);
                iCell++;
            }
            if (iCell == 4) {
                acopiosLeche.add(acopioLeche);
            }
        }

        return acopiosLeche;
    }

    @Generated
    private void setValoresPorCelda(AcopioLecheEntity acopioLeche, Cell cell, int iCell) {
        try {
            switch (iCell) {
                case 0 -> acopioLeche.setFecha(cell.getDateCellValue());
                case 1 -> acopioLeche.setTurno(cell.getStringCellValue());
                case 2 -> acopioLeche.setCodigoProveedor(getCodigoPorCelda(cell));
                case 3 -> acopioLeche.setCantidadLeche((int) cell.getNumericCellValue());
                default -> {
                    //No pasa por aqui
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("El Excel ingresado contiene datos no validos");
        }
    }

    @Generated
    private String getCodigoPorCelda(Cell cell){
        try {
            return cell.getStringCellValue();
        }
        catch (IllegalStateException e) {
            int codigo = (int) cell.getNumericCellValue();
            return Integer.toString(codigo);
        }
    }
}
