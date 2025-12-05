
package EVelazquez.ProgramacionNCapasNoviembre25.ML;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

/**
 *
 * @author Alien 9
 */

public class Rol {
    private int Id;
    @NotEmpty(message = "El Rol es requerido")
    @Pattern(regexp = "^(administrador|Usuario)$", message = "El Rol solo puede ser Administrador o Usuario")
    private String Rol;
    public Rol(){
        
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getRol() {
        return Rol;
    }

    public void setRol(String Rol) {
        this.Rol = Rol;
    }

}
