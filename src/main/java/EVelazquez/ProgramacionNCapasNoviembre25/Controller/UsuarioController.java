package EVelazquez.ProgramacionNCapasNoviembre25.Controller;

import EVelazquez.ProgramacionNCapasNoviembre25.DAO.PaisDAOImplementation;
import EVelazquez.ProgramacionNCapasNoviembre25.DAO.RolDAOImplementation;
import EVelazquez.ProgramacionNCapasNoviembre25.DAO.MunicipioDAOImplementation;
import EVelazquez.ProgramacionNCapasNoviembre25.DAO.EstadoDAOImplementation;
import EVelazquez.ProgramacionNCapasNoviembre25.DAO.ColoniaDAOImplementation;
import EVelazquez.ProgramacionNCapasNoviembre25.DAO.UsuarioDAOIMplementation;
import EVelazquez.ProgramacionNCapasNoviembre25.ML.Colonia;
import EVelazquez.ProgramacionNCapasNoviembre25.ML.Direccion;
import EVelazquez.ProgramacionNCapasNoviembre25.ML.ErrorCarga;
import EVelazquez.ProgramacionNCapasNoviembre25.ML.Usuario;
import EVelazquez.ProgramacionNCapasNoviembre25.ML.Result;
import EVelazquez.ProgramacionNCapasNoviembre25.ML.Rol;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import EVelazquez.ProgramacionNCapasNoviembre25.Service.ValidationService;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

    @Autowired
    private UsuarioDAOIMplementation usuarioDAOImplementation; // Corregí mayúscula inicial variable

    @Autowired
    private RolDAOImplementation rolDAOImplementation;

    @Autowired
    private PaisDAOImplementation paisDAOImplementation;

    @Autowired
    private MunicipioDAOImplementation municipioDAOImplemetation;

    @Autowired
    private EstadoDAOImplementation estadoDAOImplementation;

    @Autowired
    private ColoniaDAOImplementation coloniaDAOImplementation;

    @Autowired
    private ValidationService validatorService;

    @GetMapping("Inicio")
    public String GetAll(Model model) {
        Result result = usuarioDAOImplementation.GetAll();
        model.addAttribute("Usuarios", result.Objects);
        return "Index";
    }

    @GetMapping("form")
    public String Form(@RequestParam(required = false) Integer idUsuario, Model model) {
        // Carga de listas para los DropDowns
        Result resultRoles = rolDAOImplementation.GetAll();
        model.addAttribute("Roles", resultRoles.Objects);

        Result resultPaises = paisDAOImplementation.GetALL();
        model.addAttribute("Pais", resultPaises.Objects);

        Usuario usuario = new Usuario();

        if (idUsuario != null && idUsuario > 0) {
            // --- MODO EDICIÓN ---
            // Aquí llamamos al GetById que mapea la dirección para que el HTML la lea
            Result resultUsuario = usuarioDAOImplementation.GetById(idUsuario);

            if (resultUsuario.Correct && resultUsuario.Object != null) {
                usuario = (Usuario) resultUsuario.Object;
            } else {
                model.addAttribute("mensaje", "No se pudo cargar el usuario");
            }
        } else {
            // --- MODO NUEVO ---
            // Inicializamos listas para evitar errores de NullPointer
            usuario.Rol = new EVelazquez.ProgramacionNCapasNoviembre25.ML.Rol();
            usuario.Direcciones = new ArrayList<>();
            Direccion dir = new Direccion();
            dir.Colonia = new Colonia();
            usuario.Direcciones.add(dir);
        }

        model.addAttribute("usuario", usuario);
        return "UsuarioForm";
    }

    // --- AQUÍ ESTABA EL PROBLEMA ---
    // He modificado este método para que detecte si es Update o Add
    @PostMapping("add")
    public String Add(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("Roles", rolDAOImplementation.GetAll().Objects);
            model.addAttribute("Pais", paisDAOImplementation.GetALL().Objects);
            model.addAttribute("usuario", usuario);
            return "UsuarioForm";
        } else {
            // VERIFICACIÓN CLAVE:
            if (usuario.getIdUsuario() > 0) {
                // Si trae ID, llamamos al método UPDATE que creamos hoy
                usuarioDAOImplementation.Update(usuario);
            } else {
                // Si ID es 0 o nulo, es NUEVO
                usuarioDAOImplementation.Add(usuario);
            }
        }
        return "redirect:/usuario/Inicio";
    }

    @GetMapping("detail/{IdUsuario}")
    public String Detail(@PathVariable("IdUsuario") int IdUsuario, Model model) {
        Result result = usuarioDAOImplementation.GetById(IdUsuario);

        Result resultRoles = rolDAOImplementation.GetAll();
        Result resultPaises = paisDAOImplementation.GetALL();

        Usuario usuario = (result.Object != null) ? (Usuario) result.Object : new Usuario();

        if (usuario.Direcciones == null) {
            usuario.Direcciones = new ArrayList<>();
        }

        model.addAttribute("usuario", usuario);
        model.addAttribute("Roles", resultRoles.Objects);
        model.addAttribute("Pais", resultPaises.Objects);

        return "IdUsuario";
    }

    // --- API JSON PARA LOS COMBOS EN CASCADA ---
    @GetMapping("getEstadosByPais/{idPais}")
    @ResponseBody
    public Result EstadosByPais(@PathVariable("idPais") int idPais) {
        return estadoDAOImplementation.GetEstadoByPais(idPais);
    }

    @GetMapping("MunicipioByEstado/{idEstado}")
    @ResponseBody
    public Result MunicipioByEstado(@PathVariable("idEstado") int idEstado) {
        return municipioDAOImplemetation.GetMunicipioByEstado(idEstado);
    }

    @GetMapping("ColoniaByMunicipio/{IdMunicipio}")
    @ResponseBody
    public Result ColoniaByMunicipio(@PathVariable("IdMunicipio") int IdMunicipio) {
        return coloniaDAOImplementation.GetColoniaByMunicipio(IdMunicipio);
    }

    // --- MÉTODOS DE CARGA MASIVA Y EXTRAS ---
    
    @GetMapping("/formEditable")
    public String FormEditable(@RequestParam("IdUsuario") int IdUsuario, @RequestParam(required = false) Integer IdDireccion, Model model) {
        if (IdDireccion == null) {
            Result result = usuarioDAOImplementation.GetById(IdUsuario);
            Result resultRoles = rolDAOImplementation.GetAll();
            model.addAttribute("Roles", resultRoles.Objects);
            model.addAttribute("usuario", result.Object);
            return "UsuarioForm";
        } else if (IdDireccion == 0) {
            Usuario usuario = new Usuario();
            usuario.setIdUsuario(IdUsuario);
            usuario.Direcciones = new ArrayList<>();
            Direccion dir = new Direccion();
            dir.setIdDireccion(0);
            dir.Colonia = new Colonia();
            usuario.Direcciones.add(dir);
            model.addAttribute("Paises", paisDAOImplementation.GetALL().Objects);
            model.addAttribute("usuario", usuario);
            return "UsuarioForm";
        } else {
            Usuario usuario = new Usuario();
            usuario.Direcciones = new ArrayList<>();
            Direccion dir = new Direccion();
            dir.setIdDireccion(IdDireccion);
            dir.setCalle("Calle Simulada");
            dir.Colonia = new Colonia();
            usuario.Direcciones.add(dir);
            model.addAttribute("usuario", usuario);
            model.addAttribute("Paises", paisDAOImplementation.GetALL().Objects);
            return "UsuarioForm";
        }
    }

    @GetMapping("CargaMasiva")
    public String CargaMasiva() {
        return "CargaMasiva";
    }

    @PostMapping("CargaMasiva")
    public String CargaMasiva(@ModelAttribute MultipartFile archivo, Model model, HttpSession session) throws IOException {
        String extencion = archivo.getOriginalFilename().split("\\.")[1];
        String path = System.getProperty("user.dir");
        String pathArchivo = "src\\main\\resources\\archivos";
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String rutaabsoluta = path + "/" + pathArchivo + "/" + fecha + archivo.getOriginalFilename();
        archivo.transferTo(new File(rutaabsoluta));
        
        List<Usuario> usuarios = new ArrayList<>();
        if (extencion.equals("txt")) {
            usuarios = LecturaArchivo(new File(rutaabsoluta));
        } else {
            usuarios = LecturaArchivoExcel(new File(rutaabsoluta));
        }

        List<ErrorCarga> errores = ValidarDatosTxt(usuarios);
        if (errores != null || errores.isEmpty()) {
            model.addAttribute("listaErrores", errores);
            session.setAttribute("archivoCargaMasiva", rutaabsoluta); 
        } else {
            model.addAttribute("listaErrores", errores);
        }
        return "CargaMasiva";
    }

    public List<Usuario> LecturaArchivo(File archivo) {
        List<Usuario> usuarios = new ArrayList<>();
        try (InputStream inputStream = new FileInputStream(archivo); BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));) {
            bufferedReader.readLine();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                String[] datos = line.split("\\|");
                Usuario usuario = new Usuario();
                usuario.setNombre(datos[0]);
                usuario.setApellidoPaterno(datos[1]);
                usuario.setApellidoMaterno(datos[2]);
                usuario.Rol = new Rol();
                usuario.Rol.setRol(datos[3]);
                usuario.setEmail(datos[4]);
                usuario.setPassword(datos[5]);
                usuario.setSexo(datos[6]);
                usuario.setTelefono(datos[7]);
                usuario.setCelular(datos[8]);
                usuario.setCurp(datos[9]);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date FechaNacimiento = sdf.parse(datos[10]);
                usuario.setFechaNacimiento(FechaNacimiento);
                usuario.setUsername(datos[11]);
                usuario.Direcciones = new ArrayList<>();
                usuario.Direcciones.add(new Direccion());
                Direccion direccion = new Direccion();
                direccion.setCalle(datos[12]);
                direccion.setNumeroExterior(datos[13]);
                direccion.setNumeroInterior(datos[14]);
                usuarios.add(usuario);
            }
        } catch (Exception ex) {
            usuarios = null;
        }
        return usuarios;
    }

    public List<Usuario> LecturaArchivoExcel(File archivo) {
        List<Usuario> usuarios = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(archivo)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                Usuario usuario = new Usuario();
                usuario.setNombre(row.getCell(0).toString());
                usuario.setApellidoPaterno(row.getCell(1).toString());
                usuario.setApellidoMaterno(row.getCell(2).toString());
                usuario.Rol = new Rol();
                usuario.Rol.setRol(row.getCell(3).toString());
                usuario.setEmail(row.getCell(4).toString());
                usuario.setPassword(row.getCell(5).toString());
                usuario.setSexo(row.getCell(6).toString());
                usuario.setTelefono(row.getCell(7).toString());
                usuario.setCelular(row.getCell(8).toString());
                usuario.setCurp(row.getCell(9).toString());
                System.out.println(row.getCell(10).toString());
                Date fechaNac = new Date(row.getCell(10).toString());
                usuario.setFechaNacimiento(fechaNac);
                usuario.setUsername(row.getCell(11).toString());
                usuario.Direcciones = new ArrayList<>();
                Direccion direccion = new Direccion();
                direccion.setCalle(row.getCell(12).toString());
                direccion.setNumeroInterior(row.getCell(13).toString());
                direccion.setNumeroExterior(row.getCell(14).toString());
                usuario.Direcciones.add(direccion);
                usuarios.add(usuario);
            }
        } catch (Exception ex) {
            usuarios = null;
        }
        return usuarios;
    }

    public List<ErrorCarga> ValidarDatosTxt(List<Usuario> usuarios) {
        List<ErrorCarga> erroresCarga = new ArrayList<>();
        int LineaError = 0;
        for (Usuario usuario : usuarios) {
            List<ObjectError> errors = new ArrayList<>();
            LineaError++;
            BindingResult bindingResultUsuario = validatorService.validateObjects(usuario);
            if (bindingResultUsuario.hasErrors()) {
                errors.addAll(bindingResultUsuario.getAllErrors());
            }
            if (usuario.Rol != null) {
                BindingResult bindingRol = validatorService.validateObjects(usuario.Rol);
                if (bindingRol.hasErrors()) {
                    errors.addAll(bindingRol.getAllErrors());
                }
            }
            for (ObjectError error : errors) {
                FieldError fieldError = (FieldError) error;
                ErrorCarga errorCarga = new ErrorCarga();
                errorCarga.linea = LineaError;
                errorCarga.Campo = fieldError.getField();
                errorCarga.Descripcion = fieldError.getDefaultMessage();
                erroresCarga.add(errorCarga);
            }
        }
        return erroresCarga;
    }

    @GetMapping("/CargaMasiva/procesar")
    public String ProcesarArchivo(HttpSession sesion, Model model) {
        String path = sesion.getAttribute("archivoCargaMasiva").toString();
        String extensionArchivo = new File(path).getName().split("\\.")[1];
        Result result;
        if (extensionArchivo.equals("txt")) {
            List<Usuario> usuarios = LecturaArchivo(new File(path));
            result = usuarioDAOImplementation.addAll(usuarios);
            model.addAttribute("Usuarios", result.Objects);
        } else {
            List<Usuario> usuarios = LecturaArchivoExcel(new File(path));
            result = usuarioDAOImplementation.addAll(usuarios);
            model.addAttribute("Usuarios", result.Objects);
        }
        sesion.removeAttribute("archivoCargaMasiva");
        new File(path).delete();
        return "UsuarioIndex";
    }

}
