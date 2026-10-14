package br.com.systemgs.loterias.mapper;

import br.com.systemgs.loterias.dto.LotofacilApiDTO;
import br.com.systemgs.loterias.model.Lotofacil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Mapper responsável pela conversão entre DTOs da API e entidades
 */
@Component
public class LotofacilMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Converte DTO da API para entidade Lotofacil
     * @param dto DTO da API da Caixa
     * @return Entidade Lotofacil
     */
    public Lotofacil toEntity(LotofacilApiDTO dto) {
        if (dto == null) {
            return null;
        }

        Lotofacil lotofacil = new Lotofacil();
        lotofacil.setConcurso(dto.getNumero());
        lotofacil.setData(parseData(dto.getDataApuracao()));
        lotofacil.setDezenas(formatarDezenas(dto.getListaDezenas()));

        // Extrair informações do rateio de prêmios
        if (dto.getListaRateioPremio() != null) {
            for (LotofacilApiDTO.RateioPremio rateio : dto.getListaRateioPremio()) {
                switch (rateio.getFaixa()) {
                    case 1 -> { // 15 acertos
                        lotofacil.setAcertadores_15(rateio.getNumeroDeGanhadores());
                        lotofacil.setAcumulou_15(rateio.getValorPremio());
                    }
                    case 2 -> { // 14 acertos
                        lotofacil.setAcertadores_14(rateio.getNumeroDeGanhadores());
                        lotofacil.setAcumulou_14(rateio.getValorPremio());
                    }
                    case 3 -> { // 13 acertos
                        lotofacil.setAcertadores_13(rateio.getNumeroDeGanhadores());
                        lotofacil.setAcumulou_13(rateio.getValorPremio());
                    }
                    case 4 -> { // 12 acertos
                        lotofacil.setAcertadores_12(rateio.getNumeroDeGanhadores());
                        lotofacil.setAcumulou_12(rateio.getValorPremio());
                    }
                    case 5 -> { // 11 acertos
                        lotofacil.setAcertadores_11(rateio.getNumeroDeGanhadores());
                        lotofacil.setAcumulou_11(rateio.getValorPremio());
                    }
                }
            }
        }

        lotofacil.setArrecadacao_total(dto.getValorArrecadado());
        lotofacil.setEstimativa_premio(dto.getValorEstimadoProximoConcurso());
        lotofacil.setAcumulada_especial(dto.getValorAcumuladoConcursoEspecial());
        lotofacil.setLocal_sorteio(dto.getLocalSorteio());
        lotofacil.setObservacao(dto.getObservacao());
        lotofacil.setAcumulado(dto.getAcumulado());

        return lotofacil;
    }

    /**
     * Converte lista de DTOs da API para lista de entidades
     * @param dtos Lista de DTOs da API
     * @return Lista de entidades Lotofacil
     */
    public List<Lotofacil> toEntityList(List<LotofacilApiDTO> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return List.of();
        }

        return dtos.stream()
                .map(this::toEntity)
                .toList();
    }

    private LocalDate parseData(String dataStr) {
        if (dataStr == null || dataStr.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dataStr, DATE_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    private String formatarDezenas(List<String> dezenas) {
        if (dezenas == null || dezenas.isEmpty()) {
            return "";
        }
        return dezenas.stream()
                .map(Integer::parseInt)
                .sorted()
                .map(n -> String.format("%02d", n))
                .collect(java.util.stream.Collectors.joining(","));
    }
}