package com.nemuel.estoque.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GeoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Obter coordenadas a partir do CEP
    public Map<String, Double> getCoordenadasPorCep(String cep) {
        String viaCepUrl = "https://viacep.com.br/ws/" + cep + "/json/";
        Map<String, Object> viaCepResponse = restTemplate.getForObject(viaCepUrl, Map.class);

        if (viaCepResponse == null || viaCepResponse.get("erro") != null) {
            throw new RuntimeException("CEP inválido ou não encontrado: " + cep);
        }

        String cidade = viaCepResponse.get("localidade").toString();
        String estado = viaCepResponse.get("uf").toString();
        String query = cidade + ", " + estado;

        String openCageUrl = "https://api.opencagedata.com/geocode/v1/json?q=" + query + "&key=ce6e17e6631f4cef9b74bb09f901e2c4";


        try {
            String jsonResponse = restTemplate.getForObject(openCageUrl, String.class);
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            JsonNode resultsNode = rootNode.path("results");
            if (!resultsNode.isArray() || resultsNode.isEmpty()) {
                throw new RuntimeException("Nenhum resultado encontrado para o endereço: " + query);
            }

            JsonNode geometryNode = resultsNode.get(0).path("geometry");
            if (geometryNode.isMissingNode()) {
                throw new RuntimeException("Nenhuma coordenada encontrada para o endereço: " + query);
            }

            double latitude = geometryNode.path("lat").asDouble();
            double longitude = geometryNode.path("lng").asDouble();

            return Map.of("latitude", latitude, "longitude", longitude);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao acessar a API do OpenCage: " + e.getMessage(), e);
        }
    }

    // Método para calcular a distância entre dois pontos geográficos (fórmula de Haversine)
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
