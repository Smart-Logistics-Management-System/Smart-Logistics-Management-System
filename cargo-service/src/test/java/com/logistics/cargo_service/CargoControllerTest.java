package com.logistics.cargo_service;

import com.logistics.cargo_service.model.Cargo;
import com.logistics.cargo_service.model.CargoStatus;
import com.logistics.cargo_service.service.ICargoService;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.ArgumentMatchers.any;

public class CargoControllerTest {
    @Autowired
    private MockMvc mockMvc; // Postman gibi HTTP istekleri atmamızı sağlar

    @MockitoBean
    private ICargoService cargoService; // Servisimizi taklit (mock) ediyoruz

    @Test
    public void shouldCreateCargoAndReturn201Created() throws Exception {
        // 1. Arrange (Hazırlık) - Servisimizin döneceği sahte kargo yanıtını hazırlıyoruz
        Cargo mockResponse = new Cargo();
        mockResponse.setId(1L);
        mockResponse.setTrackingNumber("TR-998877");
        mockResponse.setStatus(CargoStatus.PENDING);
        mockResponse.setSenderId(10L);
        mockResponse.setReceiverId(20L);
        mockResponse.setWeight(15.5);

        // Servisin createCargo metodu çağrıldığında bu sahte yanıtı dönmesini söylüyoruz
        Mockito.when(cargoService.createCargo(any(Cargo.class))).thenReturn(mockResponse);

        // Frontend'den (veya Postman'den) gelecek olan örnek JSON isteği
        String requestBody = """
                {
                    "senderId": 10,
                    "receiverId": 20,
                    "weight": 15.5
                }
                """;

        // 2 & 3. Act & Assert (Eylem ve Doğrulama)
        mockMvc.perform(post("/api/cargo")
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON))
                        .content(requestBody))
                .andExpect(status().isCreated()) // Biz Controller'da .build() ile 201 döndüğümüz için Created bekliyoruz
                .andExpect(jsonPath("$.trackingNumber").value("TR-998877"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }


}
