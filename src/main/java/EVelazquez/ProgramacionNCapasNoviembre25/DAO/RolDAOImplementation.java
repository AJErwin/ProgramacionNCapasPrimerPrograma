
package EVelazquez.ProgramacionNCapasNoviembre25.DAO;

import EVelazquez.ProgramacionNCapasNoviembre25.ML.Result;
import EVelazquez.ProgramacionNCapasNoviembre25.ML.Rol;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Alien 9
 */
@Repository
public class RolDAOImplementation implements IRol{
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override 
    public Result GetAll(){
        Result result = new Result();
        
        try{
            result.Correct = jdbcTemplate.execute("{Call GetAllRol(?)} ", (CallableStatementCallback<Boolean>) callableStatement ->{
                callableStatement.registerOutParameter(1, java.sql.Types.REF_CURSOR);
                callableStatement.execute();
                
                ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
                result.Objects = new ArrayList<>();
                
                while(resultSet.next()){
                    Rol rol = new Rol();
                    rol.setId(resultSet.getInt("Id"));
                    rol.setRol(resultSet.getString("Rol") );
                    result.Objects.add(rol);
                }
                        
                      return true;  
                        
            });
        }catch(Exception ex){
            result.Correct = false;
            result.ErrorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        
            return result;
        }
    }
    

