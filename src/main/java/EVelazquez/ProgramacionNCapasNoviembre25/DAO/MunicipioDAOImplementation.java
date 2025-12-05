
package EVelazquez.ProgramacionNCapasNoviembre25.DAO;

import EVelazquez.ProgramacionNCapasNoviembre25.ML.Municipio;
import EVelazquez.ProgramacionNCapasNoviembre25.ML.Result;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;



/**
 *
 * @author Alien 9
 */
@Repository
public class MunicipioDAOImplementation implements IMunicipio {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public Result GetMunicipioByEstado(int IdEstado) {
        Result result = new Result();
        try{
            result.Correct = jdbcTemplate.execute("{CALL GetMunicipioByEstado (?,?)}",(CallableStatementCallback<Boolean>)callableStatement -> {
                callableStatement.setInt(1, IdEstado);
                callableStatement.registerOutParameter(2, java.sql.Types.REF_CURSOR);
                callableStatement.execute();
                
                ResultSet resultSet = (ResultSet) callableStatement.getObject(2);
                
                        result.Objects = new ArrayList<>();
                        
                        while (resultSet.next()){
                            Municipio municipio = new Municipio();
                            municipio.setIdMunicipio(resultSet.getInt("IdMunicipio"));
                            municipio.setNombre(resultSet.getString("Nombre"));
                            
                            result.Objects.add(municipio);
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

