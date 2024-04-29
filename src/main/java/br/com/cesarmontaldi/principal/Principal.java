package br.com.cesarmontaldi.principal;

import br.com.cesarmontaldi.model.Dados;
import br.com.cesarmontaldi.model.Modelo;
import br.com.cesarmontaldi.model.Veiculo;
import br.com.cesarmontaldi.service.ConsumoAPI;
import br.com.cesarmontaldi.service.ConverterDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverterDados converter = new ConverterDados();


    private final String URL_API = "https://parallelum.com.br/fipe/api/v1/";
    //private final String URL_API = "https://fipe.parallelum.com.br/api/v2/";
    public  void exibirMenu() {
        System.out.println();
        System.out.print("""
                Digite 1 para carros
                Digite 2 para motos
                Digite 3 para caminhões
                Qual o tipo de veículo que deseja consultar:  """);

        var tipoVeiculo = scanner.nextLine();

        switch (tipoVeiculo) {
            case "1" -> tipoVeiculo = "carros";
            case "2" -> tipoVeiculo = "motorcycles";
            case "3" -> tipoVeiculo = "trucks";
        }
        System.out.println();
        String json = consumoAPI.obterDados(URL_API + tipoVeiculo + "/marcas");

        var marcas = converter.obterLista(json, Dados.class);

        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .collect(Collectors.toList())
                .forEach(m -> System.out.println("Código: " + m.codigo() + " => "+"Marca: " + m.nome()));

        System.out.println();
        System.out.print("Digite o código da marca para consulta: ");
        var codigoMarca = scanner.nextLine();

        json = consumoAPI.obterDados(URL_API + tipoVeiculo + "/marcas/" + codigoMarca + "/modelos");

        var modelosLista = converter.obterDados(json, Modelo.class);

        System.out.println("Modelos da marca: ");
        modelosLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::nome))
                .collect(Collectors.toList())
                .forEach(m -> System.out.println("Código: " + m.codigo() + " => " + "Modelo: " + m.nome()));

        System.out.print("\nDigite o nome do modelo para consulta: ");
        var modelo = scanner.nextLine().toUpperCase();
        System.out.println();

        System.out.println("Modelos disponiveis: ");
        List<Dados> carros = modelosLista.modelos().stream()
                .filter(m -> m.nome().toUpperCase().contains(modelo.toUpperCase()))
                .collect(Collectors.toList());

        carros.forEach(c -> System.out.println("Código: " + c.codigo() + " => " + "Modelo: " + c.nome()));

        System.out.print("\nDigite o codigo do modelo para consulta: ");
        var codigoAno = scanner.nextLine().toUpperCase();

        json = consumoAPI.obterDados(URL_API + tipoVeiculo + "/marcas/" + codigoMarca + "/modelos/" + codigoAno + "/anos");
        var anos = converter.obterLista(json, Dados.class);


        List<Veiculo> veiculos = new ArrayList<>();
        for (int i = 0; i < anos.size(); i++) {
              json = consumoAPI.obterDados(URL_API + tipoVeiculo + "/marcas/" + codigoMarca + "/modelos/" + codigoAno + "/anos/" + anos.get(i).codigo());
            Veiculo veiculo = converter.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }

        veiculos.stream().sorted(Comparator.comparing(Veiculo::ano));


        System.out.println("\n" + veiculos.stream().count() + " Modelos disponiveis: \n");
        for (int i = 0; i < veiculos.size(); i++) {
            System.out.println("Modelo: " + veiculos.get(i).modelo());
            System.out.println("Marca: " + veiculos.get(i).marca());
            System.out.println("Valor: " + veiculos.get(i).valor());
            if (veiculos.get(i).ano() == 32000) {
                System.out.println("Ano: ZERO KM" );
            } else {
                System.out.println("Ano: " + veiculos.get(i).ano());
            }
            System.out.println("Combustivel: " + veiculos.get(i).combustivel());
            System.out.println("------------------------------------------------");
        }
    }
}
