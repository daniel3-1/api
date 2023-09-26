package med.voll.api.domain.consulta;

import med.voll.api.domain.consulta.validaciones.ValidadorDeConsultas;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.infra.errores.ValidacionDeIntegridad;
import med.voll.api.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultaService {

    @Autowired
    private PacienteRepository pacienteRepository;
    @Autowired
    private MedicoRepository medicoRepository;
    @Autowired
    private ConsultaRepository consultaRepository;
    @Autowired
    List<ValidadorDeConsultas> validadores;
    public void agendar(DatosAgendarConsulta datos){

        if (!pacienteRepository.findById(datos.idPaciente()).isPresent()){
            throw new ValidacionDeIntegridad("Este id para paciente no fue encontrado");
        }

        if (datos.idMedico() != null && !medicoRepository.existsById(datos.idMedico())){
            throw new ValidacionDeIntegridad("Este id para medico no fue encontrado");
        }

        validadores.forEach(v -> v.validar(datos)); //Recorre la lista de validadores y escoge segun sea necesario

        var paciente = pacienteRepository.findById(datos.idPaciente()).get();
        var medico = seleccionarMedico(datos);
        var consulta = new Consulta(null, medico, paciente, datos.fecha());
        consultaRepository.save(consulta);

    }

    private Medico seleccionarMedico(DatosAgendarConsulta datos) {
        if (datos.idMedico() != null){
            return medicoRepository.getReferenceById(datos.idMedico());
        }

        if (datos.especialidad() == null){
            throw new ValidacionDeIntegridad("Debe seleccionarse una especialidad para medico");
        }

        return medicoRepository.seleccionarMedicoConEspecialidadEnFecha(datos.especialidad(), datos.fecha());
    }
}
