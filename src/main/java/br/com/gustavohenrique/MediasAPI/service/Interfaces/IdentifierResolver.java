package br.com.gustavohenrique.MediasAPI.service.Interfaces;

@FunctionalInterface
public interface IdentifierResolver {
    double resolve(String identifier, Long projectionId);
}
