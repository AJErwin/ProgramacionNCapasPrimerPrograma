package EVelazquez.ProgramacionNCapasNoviembre25.DAO;

import java.sql.CallableStatement;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import EVelazquez.ProgramacionNCapasNoviembre25.ML.Colonia;
import EVelazquez.ProgramacionNCapasNoviembre25.ML.Direccion;
import EVelazquez.ProgramacionNCapasNoviembre25.ML.Estado;
import EVelazquez.ProgramacionNCapasNoviembre25.ML.Municipio;
import EVelazquez.ProgramacionNCapasNoviembre25.ML.Pais;
import EVelazquez.ProgramacionNCapasNoviembre25.ML.Usuario;
import EVelazquez.ProgramacionNCapasNoviembre25.ML.Result;
import EVelazquez.ProgramacionNCapasNoviembre25.ML.Rol;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioDAOIMplementation implements IUsuario {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Result GetAll() {
        Result result = new Result();
        try {
            result.Correct = jdbcTemplate.execute("{CALL UsuarioDireccionGetAll(?)}", (CallableStatementCallback<Boolean>) callableStatement -> {
                callableStatement.registerOutParameter(1, java.sql.Types.REF_CURSOR);
                callableStatement.execute();
                ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
                result.Objects = new ArrayList<>();

                while (resultSet.next()) {
                    int IdUsuario = resultSet.getInt("IdUsuario");

                    if (!result.Objects.isEmpty() && ((Usuario) result.Objects.get(result.Objects.size() - 1)).getIdUsuario() == IdUsuario) {
                        Usuario usuario = ((Usuario) result.Objects.get(result.Objects.size() - 1));

                        int idDireccion = resultSet.getInt("IdDireccion");
                        if (idDireccion > 0) {
                            Direccion direccion = new Direccion();
                            direccion.setIdDireccion(idDireccion);
                            direccion.setCalle(resultSet.getString("Calle"));
                            direccion.setNumeroInterior(resultSet.getString("NumeroInterior"));
                            direccion.setNumeroExterior(resultSet.getString("NumeroExterior"));

                            direccion.Colonia = new Colonia();
                            direccion.Colonia.setIdColonia(resultSet.getInt("IdColonia"));
                            direccion.Colonia.setNombre(resultSet.getString("NombreColonia"));

                            usuario.Direcciones.add(direccion);
                        }
                    } else {
                        Usuario usuario = new Usuario();
                        usuario.setIdUsuario(IdUsuario);
                        usuario.setNombre(resultSet.getString("Nombre"));
                        usuario.setApellidoPaterno(resultSet.getString("ApellidoPaterno"));
                        usuario.setApellidoMaterno(resultSet.getString("ApellidoMaterno"));
                        usuario.setEmail(resultSet.getString("Email"));
                        usuario.setSexo(resultSet.getString("Sexo"));
                        usuario.setTelefono(resultSet.getString("Telefono"));
                        usuario.setCelular(resultSet.getString("Celular"));
                        usuario.setCurp(resultSet.getString("Curp"));
                        usuario.setFechaNacimiento(resultSet.getDate("FechaNacimiento"));
                        usuario.setUsername(resultSet.getString("Username"));
                        usuario.Rol = new Rol();
                        try {
                            usuario.Rol.setId(resultSet.getInt("IdRol"));
                        } catch (Exception e) {
                        }
                        try {
                            usuario.Rol.setRol(resultSet.getString("NombreRol"));
                        } catch (Exception e) {
                        }

                        usuario.Direcciones = new ArrayList<>();
                        int IdDireccion = resultSet.getInt("IdDireccion");

                        if (IdDireccion != 0) {
                            Direccion direccion = new Direccion();
                            direccion.setIdDireccion(IdDireccion);
                            direccion.setCalle(resultSet.getString("Calle"));
                            direccion.setNumeroInterior(resultSet.getString("NumeroInterior"));
                            direccion.setNumeroExterior(resultSet.getString("NumeroExterior"));

                            direccion.Colonia = new Colonia();
                            direccion.Colonia.setIdColonia(resultSet.getInt("IdColonia"));
                            direccion.Colonia.setNombre(resultSet.getString("NombreColonia"));

                            usuario.Direcciones.add(direccion);
                        }
                        result.Objects.add(usuario);
                    }
                }
                return true;
            });
        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

    @Override
    public Result Add(Usuario usuario) {
        Result result = new Result();
        try {
            result.Correct = jdbcTemplate.execute("{CALL UsuarioDireccionAdd(?,?,?,?,?,?,?,?,?,?,?,?,?)}", (CallableStatementCallback<Boolean>) callableStatement -> {
                callableStatement.setString(1, usuario.getNombre());
                callableStatement.setString(2, usuario.getApellidoPaterno());
                callableStatement.setString(3, usuario.getApellidoMaterno());
                callableStatement.setString(4, usuario.getEmail());
                callableStatement.setString(5, usuario.getPassword());
                callableStatement.setString(6, usuario.getFechaNacimiento() != null ? usuario.getFechaNacimiento().toString() : null);
                callableStatement.setString(7, usuario.getSexo());
                callableStatement.setString(8, usuario.getTelefono());
                callableStatement.setString(9, usuario.getCelular());
                callableStatement.setString(10, usuario.getCurp());
                callableStatement.setInt(11, usuario.getRol().getId());

                if (usuario.Direcciones != null && !usuario.Direcciones.isEmpty()) {
                    callableStatement.setString(12, usuario.Direcciones.get(0).getCalle());
                    callableStatement.setString(13, usuario.Direcciones.get(0).getNumeroInterior());
                    callableStatement.setString(14, usuario.Direcciones.get(0).getNumeroExterior());
                    callableStatement.setInt(15, usuario.Direcciones.get(0).Colonia.getIdColonia());
                } else {
                    callableStatement.setString(12, "");
                    callableStatement.setString(13, "");
                    callableStatement.setString(14, "");
                    callableStatement.setInt(15, 0);
                }

                callableStatement.executeUpdate();
                return true;
            });
        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

    @Override
    public Result GetById(int IdUsuario) {
        Result result = new Result();
        try {
            result.Correct = jdbcTemplate.execute("{CALL UsuarioGetById(?,?)}", (CallableStatementCallback<Boolean>) callableStatement -> {
                callableStatement.registerOutParameter(1, java.sql.Types.REF_CURSOR);
                callableStatement.setInt(2, IdUsuario);
                callableStatement.execute();

                ResultSet resultSet = (ResultSet) callableStatement.getObject(1);

                if (resultSet.next()) {
                    Usuario usuario = new Usuario();

                    usuario.setIdUsuario(resultSet.getInt("IdUsuario"));
                    usuario.setNombre(resultSet.getString("Nombre"));
                    usuario.setApellidoPaterno(resultSet.getString("ApellidoPaterno"));
                    usuario.setApellidoMaterno(resultSet.getString("ApellidoMaterno"));
                    usuario.setEmail(resultSet.getString("Email"));
                    usuario.setPassword(resultSet.getString("Password"));
                    usuario.setFechaNacimiento(resultSet.getDate("FechaNacimiento"));
                    usuario.setSexo(resultSet.getString("Sexo"));
                    usuario.setTelefono(resultSet.getString("Telefono"));
                    usuario.setCelular(resultSet.getString("Celular"));
                    usuario.setCurp(resultSet.getString("Curp"));

                    usuario.Rol = new Rol();
                    usuario.Rol.setId(resultSet.getInt("IdRol"));
                    try {
                        usuario.Rol.setRol(resultSet.getString("NombreRol"));
                    } catch (Exception e) {
                    }

                    usuario.Direcciones = new ArrayList<>();

                    int idDireccion = 0;
                    try {
                        idDireccion = resultSet.getInt("IdDireccion");
                    } catch (Exception e) {
                    }

                    if (idDireccion > 0) {
                        Direccion dir = new Direccion();
                        dir.setIdDireccion(idDireccion);
                        dir.setCalle(resultSet.getString("Calle"));
                        dir.setNumeroInterior(resultSet.getString("NumeroInterior"));
                        dir.setNumeroExterior(resultSet.getString("NumeroExterior"));

                        dir.Colonia = new Colonia();
                        dir.Colonia.setIdColonia(resultSet.getInt("IdColonia"));
                        dir.Colonia.setNombre(resultSet.getString("NombreColonia"));

                        dir.Colonia.Municipio = new Municipio();
                        dir.Colonia.Municipio.setIdMunicipio(resultSet.getInt("IdMunicipio"));
                        dir.Colonia.Municipio.setNombre(resultSet.getString("NombreMunicipio"));

                        dir.Colonia.Municipio.Estado = new Estado();
                        dir.Colonia.Municipio.Estado.setIdEstado(resultSet.getInt("IdEstado"));
                        dir.Colonia.Municipio.Estado.setNombre(resultSet.getString("NombreEstado"));

                        dir.Colonia.Municipio.Estado.Pais = new Pais();
                        dir.Colonia.Municipio.Estado.Pais.setIdPais(resultSet.getInt("IdPais"));
                        dir.Colonia.Municipio.Estado.Pais.setNombre(resultSet.getString("NombrePais"));

                        usuario.Direcciones.add(dir);
                    }

                    result.Object = usuario;
                }
                return true;
            });
        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = ex.getLocalizedMessage();
            result.ex = ex;
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result addAll(List<Usuario> usuarios) {
        Result result = new Result();
        System.out.println(usuarios.size());

        try {
            jdbcTemplate.batchUpdate("{CALL ADDUSUARIOS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", usuarios, usuarios.size(), (CallableStatement, usuario) -> {
                CallableStatement.setString(1, usuario.getNombre());
                CallableStatement.setString(2, usuario.getApellidoMaterno());
                CallableStatement.setString(3, usuario.getApellidoPaterno());
                CallableStatement.setString(4, usuario.Rol.getRol());
                CallableStatement.setString(5, usuario.getEmail());
                CallableStatement.setString(6, usuario.getPassword());
                CallableStatement.setString(7, usuario.getSexo());
                CallableStatement.setString(8, usuario.getTelefono());
                CallableStatement.setString(9, usuario.getCelular());
                CallableStatement.setString(10, usuario.getCurp());
                java.sql.Date fechaNac = new java.sql.Date(usuario.getFechaNacimiento().getTime());
                CallableStatement.setDate(11, fechaNac);
                CallableStatement.setString(12, usuario.getUsername());
                CallableStatement.setString(13, usuario.Direcciones.get(0).getCalle());
                CallableStatement.setString(14, usuario.Direcciones.get(0).getNumeroExterior());
                CallableStatement.setString(15, usuario.Direcciones.get(0).getNumeroInterior());
                CallableStatement.setString(16, usuario.Direcciones.get(0).getColonia().toString());
            });
        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = ex.getLocalizedMessage();
        }
        return result;
    }

    @Override

    public Result Update(Usuario usuario) {
        Result result = new Result();
        try {
            jdbcTemplate.update("{call SP_UpdateUsuarioCompleto(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}", ps -> {
                ps.setInt(1, usuario.getIdUsuario());
                ps.setString(2, usuario.getNombre());
                ps.setString(3, usuario.getApellidoPaterno());
                ps.setString(4, usuario.getApellidoMaterno());
                ps.setInt(5, usuario.getRol().getId());
                ps.setString(6, usuario.getEmail());
                ps.setString(7, usuario.getPassword());
                ps.setString(8, usuario.getSexo());
                ps.setString(9, usuario.getTelefono());
                ps.setString(10, usuario.getCelular());
                ps.setString(11, usuario.getCurp());

                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                String fechaStr = (usuario.getFechaNacimiento() != null) ? sdf.format(usuario.getFechaNacimiento()) : null;
                ps.setString(12, fechaStr);

                ps.setString(13, usuario.getUsername());

                if (usuario.getDirecciones() != null && !usuario.getDirecciones().isEmpty()) {
                    ps.setString(14, usuario.getDirecciones().get(0).getCalle());
                    ps.setString(15, usuario.getDirecciones().get(0).getNumeroInterior());
                    ps.setString(16, usuario.getDirecciones().get(0).getNumeroExterior());
                    ps.setInt(17, usuario.getDirecciones().get(0).getColonia().getIdColonia());
                } else {
                    ps.setNull(14, java.sql.Types.VARCHAR);
                    ps.setNull(15, java.sql.Types.VARCHAR);
                    ps.setNull(16, java.sql.Types.VARCHAR);
                    ps.setNull(17, java.sql.Types.NUMERIC);
                }
            });
            result.Correct = true;
        } catch (Exception ex) {
            result.Correct = false;
            result.ErrorMessage = ex.getLocalizedMessage();
        }
        return result;
    }

 @Override
    public Result BuscarUsuario(String nombre, String apellidoPaterno, String apellidoMaterno, String rol) {
 
        Result resultsearch = new Result();
 
        try {
            resultsearch.Correct = jdbcTemplate.execute(
                    "{CALL BusquedaAlumnoDireccionGetAll(?,?,?,?,?)}",
                    (CallableStatementCallback<Boolean>) callableStatement -> {
 
                        // Entradas
                        callableStatement.setString(1, nombre);
                        callableStatement.setString(2, apellidoPaterno);
                        callableStatement.setString(3, apellidoMaterno);
                        callableStatement.setString(4, rol);
 
                        // Salida (cursor)
                        callableStatement.registerOutParameter(5, java.sql.Types.REF_CURSOR);
 
                        callableStatement.execute();
 
                        ResultSet resultSet = (ResultSet) callableStatement.getObject(5);
 
                        resultsearch.Objects = new ArrayList<>();
 
                        while (resultSet.next()) {
 
                            Usuario usuario = new Usuario();
                            usuario.setNombre(resultSet.getString("NombreUsuario"));
                            usuario.setApellidoPaterno(resultSet.getString("ApellidoPaterno"));
                            usuario.setApellidoMaterno(resultSet.getString("ApellidoMaterno"));
                            usuario.setEmail(resultSet.getString("Email"));
                            usuario.setTelefono(resultSet.getString("Telefono"));
                            usuario.setCurp(resultSet.getString("Curp"));
                            usuario.setUsername(resultSet.getString("Username"));
                            usuario.setFechaNacimiento(resultSet.getDate("FechaNacimiento"));
                            usuario.setSexo(resultSet.getString("Sexo"));
                            usuario.setCelular(resultSet.getString("Celular"));
 
                            // direcciones
                            int IdDireccion = resultSet.getInt("IdDireccion");
                            if (IdDireccion != 0) {
                                usuario.Direcciones = new ArrayList<>();
                                Direccion direccion = new Direccion();
                                direccion.setCalle(resultSet.getString("Calle"));
                                direccion.setNumeroInterior(resultSet.getString("NumeroInterior"));
                                direccion.setNumeroExterior(resultSet.getString("NumeroExterior"));
 
                                Colonia colonia = new Colonia();
                                colonia.setIdColonia(resultSet.getInt("IdColonia"));
                                colonia.setNombre(resultSet.getString("NombreColonia"));
                                colonia.setCodigoPostal(resultSet.getString("CodigoPostal"));
                                direccion.Colonia = colonia;
 
                                usuario.Direcciones.add(direccion);
                            }
 
                            resultsearch.Objects.add(usuario);
                        }
                        return true;
                    });
 
        } catch (Exception ex) {
            resultsearch.Correct = false;
            resultsearch.ErrorMessage = ex.getLocalizedMessage();
            resultsearch.ex = ex;
        }
 
        return resultsearch;
    }
}
