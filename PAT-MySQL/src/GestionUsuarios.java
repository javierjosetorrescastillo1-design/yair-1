import java.sql.*;
import java.util.*;

public class GestionUsuarios {
    // Datos de conexión al servidor MySQL (se crean con setup.sh)
    private static final String URL =
            "jdbc:mysql://localhost:3306/pat_db"
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USUARIO_BD = "pat";
    private static final String CLAVE_BD = "pat123";
    private Connection conexion;

    public GestionUsuarios() throws SQLException {
        conexion = DriverManager.getConnection(URL, USUARIO_BD, CLAVE_BD);
        crearTabla();
        cargarDatosIniciales();
    }

    private void crearTabla() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS usuario ("
                + "id_usuario INT PRIMARY KEY AUTO_INCREMENT, "
                + "nombre VARCHAR(50) NOT NULL, "
                + "rol VARCHAR(30) NOT NULL, "
                + "estado_acceso VARCHAR(20) NOT NULL)";
        try (Statement st = conexion.createStatement()) {
            st.execute(sql);
        }
    }

    // Solo inserta los 5 usuarios la primera vez (si la tabla está vacía)
    private void cargarDatosIniciales() throws SQLException {
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM usuario")) {
            if (rs.next() && rs.getInt(1) > 0) return;
        }
        agregar("Carlos", "Administrador", "Autorizado");
        agregar("María", "Usuario", "Denegado");
        agregar("Andrés", "Administrador", "Autorizado");
        agregar("Laura", "Gestor", "Denegado");
        agregar("Sofía", "Usuario", "Autorizado");
    }

    // INSERT
    public void agregar(String nombre, String rol, String estado) throws SQLException {
        String sql = "INSERT INTO usuario (nombre, rol, estado_acceso) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, rol);
            ps.setString(3, estado);
            ps.executeUpdate();
        }
    }

    // SELECT de todos
    public List<Usuario> mostrarTodos() throws SQLException {
        return consultar("SELECT * FROM usuario ORDER BY id_usuario", null);
    }

    // SELECT ... WHERE rol = ?
    public List<Usuario> buscarPorRol(String rol) throws SQLException {
        return consultar("SELECT * FROM usuario WHERE rol = ? ORDER BY id_usuario", rol);
    }

    private List<Usuario> consultar(String sql, String parametro) throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            if (parametro != null) ps.setString(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("nombre"),
                            rs.getString("rol"),
                            rs.getString("estado_acceso")));
                }
            }
        }
        return lista;
    }

    // SELECT DISTINCT rol
    public List<String> rolesDisponibles() throws SQLException {
        List<String> roles = new ArrayList<>();
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery("SELECT DISTINCT rol FROM usuario")) {
            while (rs.next()) roles.add(rs.getString("rol"));
        }
        return roles;
    }

    // UPDATE: corrección del profesor (cambiar Autorizado <-> Denegado)
    public boolean cambiarEstado(int id, String nuevoEstado) throws SQLException {
        String sql = "UPDATE usuario SET estado_acceso = ? WHERE id_usuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0; // true si encontró el usuario
        }
    }

    // Alterna: si estaba Autorizado pasa a Denegado y viceversa
    public boolean alternarEstado(int id) throws SQLException {
        String sql = "UPDATE usuario SET estado_acceso = "
                + "CASE estado_acceso WHEN 'Autorizado' THEN 'Denegado' ELSE 'Autorizado' END "
                + "WHERE id_usuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public void cerrar() {
        try { if (conexion != null) conexion.close(); } catch (SQLException e) { /* nada */ }
    }
}
