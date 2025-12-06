
package EVelazquez.ProgramacionNCapasNoviembre25.DAO;

import EVelazquez.ProgramacionNCapasNoviembre25.ML.Result;
import EVelazquez.ProgramacionNCapasNoviembre25.ML.Usuario;
import java.util.List;


public interface IUsuario {

    public Result GetAll();

    public Result Add(Usuario usario);
    
    public Result GetById(int idUsuario);
    
    public Result addAll(List<Usuario> usuarios);
    
    public Result Update(Usuario usaurio);
    
   // public Result eliminarusuario(int idUsuario);
    
    public Result BuscarUsuario (String nombre, String apellidoPaterno, String apellidoMaterno, String rol);
    

}
