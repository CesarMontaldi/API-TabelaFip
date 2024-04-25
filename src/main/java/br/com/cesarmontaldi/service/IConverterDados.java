package br.com.cesarmontaldi.service;

public interface IConverterDados {
    <T> T obterDados(String json, Class<T> classe);
}
