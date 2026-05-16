package com.map.parking_project.services;

import com.map.parking_project.repositories.IMapServiceRepository; // Ajusta según tu nombre de repo
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MapServicesServTest {

    @Mock
    private IMapServicesService mapRepo; // Asegúrate que el nombre coincida con tu @Autowired

    @InjectMocks
    private MapServicesServ mapServices;

    @Test
    void testServiceLogic() {
        // Aquí probamos los métodos de MapServicesServ
        // Si tiene un findAll o save, usa la misma lógica de los anteriores
        assertNotNull(mapServices);
    }
}