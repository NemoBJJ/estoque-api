package com.nemuel.estoque.api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GeoService {

    private final RestTemplate restTemplate = new RestTemplate();

    // Obter coordenadas a partir do CEP (Exemplo: API ViaCEP)
    public Map<String, Double> getCoordenadasPorCep(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response == null || response.get("erro") != null) {
            throw new RuntimeException("CEP inválido ou não encontrado: " + cep);
        }

        // Para simplificação, retorna coordenadas fictícias
        return Map.of("latitude", -23.55052, "longitude", -46.633308); // São Paulo como exemplo
    }

    // Calcular distância entre duas coordenadas usando a fórmula de Haversine
    public double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371; // Raio da Terra em km

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}
