public class Usuario {
    private int id;
    private String nombre;
    private String rol;
    private String estadoAcceso; // "Autorizado" o "Denegado"

    public Usuario(int id, String nombre, String rol, String estadoAcceso) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
        this.estadoAcceso = estadoAcceso;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getRol() { return rol; }
    public String getEstadoAcceso() { return estadoAcceso; }

    @Override
    public String toString() {
        return "[ID " + id + "] \"" + nombre + "\" - " + rol
                + " [Acceso: " + estadoAcceso + "]";
    }
}
