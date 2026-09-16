package com.agencia.config;

import com.agencia.model.*;
import com.agencia.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final DestinoRepository destinoRepository;
    private final ResponsableABordoRepository responsableRepository;
    private final TransporteRepository transporteRepository;
    private final ViajeRepository viajeRepository;

    public DataInitializer(DestinoRepository destinoRepository,
                           ResponsableABordoRepository responsableRepository,
                           TransporteRepository transporteRepository,
                           ViajeRepository viajeRepository) {
        this.destinoRepository = destinoRepository;
        this.responsableRepository = responsableRepository;
        this.transporteRepository = transporteRepository;
        this.viajeRepository = viajeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Destinos
        Destino bariloche = destinoRepository.save(new Destino("Bariloche", 1500.0f));
        Destino marDelPlata = destinoRepository.save(new Destino("Mar del Plata", 400.0f));
        Destino tigres = destinoRepository.save(new Destino("Tigre", 35.0f));

        // 2. Responsables
        ResponsableABordo chofer1 = responsableRepository.save(new ResponsableABordo("Carlos Pérez", "30123456", 25000.0f));
        ResponsableABordo chofer2 = responsableRepository.save(new ResponsableABordo("Ana Gómez", "32987654", 30000.0f));

        // 3. Transportes
        Auto auto1 = transporteRepository.save(new Auto("AB123CD", 100.0f, 5000.0f, 150.0f));
        Combi combi1 = transporteRepository.save(new Combi("CD456EF", 90.0f, 10000.0f, 80.0f));
        ColectivoCocheCama colectivo1 = transporteRepository.save(new ColectivoCocheCama("EF789GH", 80.0f, 120.0f, 200.0f));

        // 4. Viajes
        CortaDistancia viajeEscapada = new CortaDistancia("Escapada a Tigre", tigres, 3, auto1);
        viajeEscapada.AgregarResponsable(chofer1);
        viajeRepository.save(viajeEscapada);

        LargaDistancia viajeNieve = new LargaDistancia("Aventura en Bariloche", bariloche, 20, colectivo1);
        viajeNieve.AgregarResponsable(chofer2);
        viajeRepository.save(viajeNieve);

        System.out.println("=========================================");
        System.out.println(">>> DATOS INICIALES CARGADOS EN LA BD <<<");
        System.out.println("=========================================");
    }
}