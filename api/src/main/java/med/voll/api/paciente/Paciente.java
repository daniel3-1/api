package med.voll.api.paciente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.domain.direccion.Direccion;

@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Paciente")
@Table(name = "pacientes")

public class Paciente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String nombre;
    private String email;
    private String documentoIdentidad;
    private String telefono;

    @Embedded
    private Direccion direccion;

    private Boolean activo;
    public Paciente(DatosRegistroPaciente datos){
        this.activo = true;
        this.nombre = datos.nombre();
        this.email = datos.email();
        this.documentoIdentidad = datos.documentoIdentidad();
        this.telefono = datos.telefono();
        this.direccion = new Direccion(datos.direccion());

    }

    public void atualizarInformacion(DatosActualizacionPaciente datos) {
        if (datos.nombre() != null)
            this.nombre = datos.nombre();

        if (datos.telefono() != null)
            this.telefono = datos.telefono();

        if (datos.direccion() != null)
            direccion.atualizarInformacion(datos.direccion());
    }

    public void inactivar() {
        this.activo = false;
    }

    public void actualizarInformacion(DatosActualizacionPaciente datos) {
    }
}
