
package EVelazquez.ProgramacionNCapasNoviembre25.DAO;

import EVelazquez.ProgramacionNCapasNoviembre25.ML.Colonia;
import EVelazquez.ProgramacionNCapasNoviembre25.ML.Result;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


    
    @Repository
public class ColoniaDAOImplementation implements IColonia {

    @Autowired
    private JdbcTemplate jdbcTemplate;
  
    @Override
    public Result GetColoniaByMunicipio(int IdMunicipio){
        Result result = new Result();
        try {
            result.Correct = jdbcTemplate.execute("{CALL GetColoniaByMunicipio (?, ?)}", (CallableStatementCallback<Boolean>)callableStatement->{
                callableStatement.setInt(1, IdMunicipio);
                callableStatement.registerOutParameter(2, java.sql.Types.REF_CURSOR);
                callableStatement.execute();
                
                ResultSet resultSet = (ResultSet) callableStatement.getObject(2);
                result.Objects = new ArrayList<>();
                
                while(resultSet.next()){
                    Colonia colonia = new Colonia();
                    colonia.setIdColonia(resultSet.getInt("IdColonia"));
                    colonia.setNombre(resultSet.getString("Nombre"));
                    
                }
                return true;
                
            });
        }
        catch (Exception ex){
            result.Correct = false;
            result.ErrorMessage = ex.getLocalizedMessage();
            result.ex=ex;
    }
        return result;

}

}

