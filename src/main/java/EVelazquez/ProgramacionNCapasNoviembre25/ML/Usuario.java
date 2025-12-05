package EVelazquez.ProgramacionNCapasNoviembre25.ML;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public class Usuario {

    private int IdUsuario;
    @NotEmpty(message = "El nombre es requerido")
    @Size(min = 2, max = 20, message = "El tamaño minimo es de 2 y maximo 20")
    private String nombre;
    @NotEmpty(message = "El apellido paterno es requerido")
    @Size(min = 2, max = 20, message = "El tamaño minimo es de 2 y maximo 20")
    private String apellidoPaterno;
    @NotEmpty(message = "El apellido materno es requerido")
    @Size(min = 2, max = 20, message = "El tamaño minimo es de 2 y maximo 20")
    private String apellidoMaterno;
    @NotEmpty(message = "El email es requerido")
    @Email(message = "Ingresa un formato de correo válido")
    private String Email;
    public Rol Rol;
    @NotEmpty(message = "La contraseña es requerida")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).+$", message = "La contraseña debe tener al menos una letra mayuscula un numero y un caracter especial")
    private String Password;
    @NotEmpty(message = "El sexo es requerido")
    private String Sexo;
    @NotEmpty(message = "El Teléfono es requerido")
    @Pattern(regexp = "^[0-9]{10}$", message = "Ingresa un número de Teléfono válido (10 dígitos)")
    private String Telefono;
    @Pattern(regexp = "^[0-9]{10}$", message = "Ingresa un número de Celular válido (10 dígitos)")
    private String Celular;
    @NotEmpty(message = "El Curp es requerido")
    @Pattern(regexp = "[A-Z]{1}[AEIOU]{1}[A-Z]{2}[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|1[0-9]|2[0-9]|3[0-1])[HM]{1}(AS|BC|BS|CC|CS|CH|CL|CM|DF|DG|GT|GR|HG|JC|MC|MN|MS|NT|NL|OC|PL|QT|QR|SP|SL|SR|TC|TS|TL|VZ|YN|ZS|NE)[B-DF-HJ-NP-TV-Z]{3}[0-9A-Z]{1}[0-9]{1}", message = "El curp no es valido")
    private String Curp;

    @NotNull(message = "La Fecha de Nacimiento es requerida")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date FechaNacimiento;

    @NotEmpty(message = "El Username es requerido")
    private String Username;
    public List<Direccion> Direcciones;

    public Usuario() {

    }

    public List<Direccion> getDirecciones() {
        return Direcciones;
    }

    public void setDirecciones(List<Direccion> Direcciones) {
        this.Direcciones = Direcciones;
    }

    public void setIdUsuario(int IdUsuario) {
        this.IdUsuario = IdUsuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setRol(Rol Rol) {
        this.Rol = Rol;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public void setSexo(String Sexo) {
        this.Sexo = Sexo;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public void setCelular(String Celular) {
        this.Celular = Celular;
    }

    public void setCurp(String Curp) {
        this.Curp = Curp;
    }

    public void setFechaNacimiento(Date FechaNacimiento) {
        this.FechaNacimiento = FechaNacimiento;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public String getEmail() {
        return Email;
    }

    public Rol getRol() {
        return Rol;
    }

    public String getPassword() {
        return Password;
    }

    public String getSexo() {
        return Sexo;
    }

    public String getTelefono() {
        return Telefono;
    }

    public String getCelular() {
        return Celular;
    }

    public String getCurp() {
        return Curp;
    }

    public Date getFechaNacimiento() {
        return FechaNacimiento;
    }

    public String getUsername() {
        return Username;
    }

 

}
