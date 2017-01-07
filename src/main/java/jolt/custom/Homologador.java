package jolt.custom;

import java.util.Map;


public interface Homologador {

    Map<String, Map<String, String>> obtenerHomologaciones(String appOrigen, String appDestino);
}
