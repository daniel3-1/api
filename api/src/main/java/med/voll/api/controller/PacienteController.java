package med.voll.api.controller;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pacientes")
@SecurityRequirement(name = "bearer-key")
public class PacienteController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @PostMapping
    @Transactional
    public void registrarPaciente(@RequestBody @Valid DatosRegistroPaciente datos){
        pacienteRepository.save(new Paciente(datos));
    }

    @GetMapping
    public Page<DatosListadoPaciente> listadoPacientes(@PageableDefault(size = 3) Pageable paginacion){
        return pacienteRepository.findAll(paginacion).map(DatosListadoPaciente::new);
    }

    @PutMapping
    @Transactional
    public void actualizar(@RequestBody @Valid DatosActualizacionPaciente datos) {
        var paciente = pacienteRepository.getReferenceById(datos.id());
        paciente.actualizarInformacion(datos);

    }
        @DeleteMapping("/{id}")
        @Transactional
        public void remover(@PathVariable Long id){
            var paciente = pacienteRepository.getReferenceById(id);
            paciente.inactivar();
    }
}
