package tingeso.laboratoriolecheservice.services;

import lombok.Generated;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import tingeso.laboratoriolecheservice.entities.LaboratorioLecheEntity;
import tingeso.laboratoriolecheservice.models.Quincena;
import tingeso.laboratoriolecheservice.repositories.LaboratorioLecheRepository;

import java.io.IOException;
import java.util.*;

@Service
public class LaboratorioLecheService {
    @Autowired
    LaboratorioLecheRepository laboratorioLecheRepository;
    @Autowired
    RestTemplate restTemplate;

    public LaboratorioLecheEntity getById(String id) {
        Optional<LaboratorioLecheEntity> laboratorioLeche = laboratorioLecheRepository.findById(id);
        if(!laboratorioLeche.isPresent()) {
            throw new IllegalArgumentException("No existen los datos de laboratorio de leche");
        }
        return laboratorioLeche.get();
    }

    public void guardarDatosLaboratorioLeche(LaboratorioLecheEntity laboratorioLeche) {
        String codigoProveedor = laboratorioLeche.getCodigoProveedor();
        String quincena = laboratorioLeche.getQuincena();
        String id = codigoProveedor + "-" + quincena;
        laboratorioLeche.setId(id);
        laboratorioLecheRepository.save(laboratorioLeche);
    }

    public void guardarListaDatosLaboratorioLeche(List<LaboratorioLecheEntity> laboratorioLecheList, String quincena) {
        for (LaboratorioLecheEntity laboratorioLeche : laboratorioLecheList) {
            laboratorioLeche.setQuincena(quincena);
            guardarDatosLaboratorioLeche(laboratorioLeche);
        }
    }

    public void validarListaDatosLaboratorioLeche(List<LaboratorioLecheEntity> laboratorioLecheList) {
        for (LaboratorioLecheEntity laboratorioLeche : laboratorioLecheList) {
            validarDatosLaboratorioLeche(laboratorioLeche);
        }
    }

    public void validarDatosLaboratorioLeche(LaboratorioLecheEntity laboratorioLeche) {
        String codigoProveedor = laboratorioLeche.getCodigoProveedor();
        Integer porcentajeGrasa = laboratorioLeche.getPorcentajeGrasa();
        Integer porcentajeSolidoTotal = laboratorioLeche.getPorcentajeSolidoTotal();
        Boolean existeProveedor = restTemplate.getForObject("http://proveedor-service/proveedores/exists/" + codigoProveedor, Boolean.class);

        if (porcentajeGrasa < 0 || porcentajeGrasa > 100) {
            throw new IllegalArgumentException("El porcentaje de grasa no es valido");
        }

        if (porcentajeSolidoTotal < 0 || porcentajeSolidoTotal > 100) {
            throw new IllegalArgumentException("El porcentaje de solido total no es valido");
        }

        if (!existeProveedor) {
            throw new IllegalArgumentException("Los proveedores tienen que estar registrados");
        }
    }

    public LaboratorioLecheEntity obtenerLaboratorioLechePorProveedorQuincena(String codigoProveedor, String quincena) {
        Optional<LaboratorioLecheEntity> laboratorioLeche =  laboratorioLecheRepository.findByCodigoProveedorAndQuincena(codigoProveedor, quincena);
        if(!laboratorioLeche.isPresent()){
            throw new IllegalArgumentException("No existe datos de grasa y solido total para un proveedor dada la quincena ingresada");
        }

        return laboratorioLeche.get();
    }

    public boolean existeLaboratorioLechePorQuincena(String quincena) {
        return laboratorioLecheRepository.existsByQuincena(quincena);
    }

    @Generated
    public List<LaboratorioLecheEntity> leerExcel(MultipartFile file) {
        List<LaboratorioLecheEntity> laboratorioLecheList = new ArrayList<>();
        String filename = file.getOriginalFilename();

        if (filename == null || !filename.endsWith(".xlsx")) {
            throw new IllegalArgumentException("El archivo ingresado no es un .xlsx");
        }
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            throw new IllegalArgumentException("El archivo ingresado no pudo ser leido");
        }
        XSSFSheet worksheet = workbook.getSheetAt(0);
        boolean rowVerification = true;
        for (Row row : worksheet) {
            if (rowVerification) {
                rowVerification = false;
                continue;
            }

            Iterator<Cell> cellItr = row.iterator();
            int iCell = 0;
            LaboratorioLecheEntity laboratorioLeche = new LaboratorioLecheEntity();
            while (cellItr.hasNext()) {
                Cell cell = cellItr.next();
                setValueByCell(laboratorioLeche, cell, iCell);
                iCell++;
            }
            if (iCell == 3) {
                laboratorioLecheList.add(laboratorioLeche);
            }
        }

        return laboratorioLecheList;
    }

    @Generated
    private void setValueByCell(LaboratorioLecheEntity laboratorioLeche, Cell cell, int iCell) {
        try {
            switch (iCell) {
                case 0 -> laboratorioLeche.setCodigoProveedor(getCodigoValuByCell(cell));
                case 1 -> laboratorioLeche.setPorcentajeGrasa((int) cell.getNumericCellValue());
                case 2 -> laboratorioLeche.setPorcentajeSolidoTotal((int) cell.getNumericCellValue());
                default -> {
                    //No pasa por aca
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("El Excel ingresado contiene datos no validos.");
        }
    }

    @Generated
    private String getCodigoValuByCell(Cell cell){
        try {
            return cell.getStringCellValue();
        }
        catch (IllegalStateException e) {
            int codigo = (int) cell.getNumericCellValue();
            return Integer.toString(codigo);
        }
    }

    public boolean existenPagosPorQuincena(Quincena quincena) {
        Map<String, String> params = quincena.toMap();
        return restTemplate.getForObject("http://pago-service/pagos/exists/byquincena?year={year}&mes={mes}&quincena={quincena}", Boolean.class, params);
    }
}
