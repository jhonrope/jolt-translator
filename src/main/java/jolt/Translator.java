package jolt;

import com.bazaarvoice.jolt.SpecDriven;
import com.bazaarvoice.jolt.Transform;
import com.bazaarvoice.jolt.exception.SpecException;
import com.bazaarvoice.jolt.exception.TransformException;
import jolt.translator.Homologador;
import jolt.translator.Key;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public abstract class Translator implements SpecDriven, Transform, Homologador {

    public interface WildCards {
        String STAR = "*";
        String OR = "|";
        String ARRAY = "[]";
    }

    private final Key mapRoot;
    private final Key arrayRoot;
    Map<String, Map<String, String>> translations;

    /**
     * Configure an instance of Defaultr with a spec.
     *
     * @throws SpecException for a malformed spec or if there are issues
     */
    public Translator(Object spec) {
        String rootString = "root";

        // Due to defaultr's array syntax, we can't actually express that we expect the top level of the defaultee to be an array, until we see the input.
        //  Thus, in order to have parsed the spec so that we can perform many transforms, we create two specs, one where the root of the input
        //   is a map, and the other where the root of the input is an array.
        // TODO : Handle arrays better, maybe by having a parent reference in the keys, or ditch the feature of having input that is at top level an array

        {
            Map<String, Object> rootSpec = new LinkedHashMap<>();
            rootSpec.put(rootString, spec);
            mapRoot = Key.parseSpec(rootSpec).iterator().next();
        }

        //  Thus we check the top level type of the input.
        {
            Map<String, Object> rootSpec = new LinkedHashMap<>();
            rootSpec.put(rootString + com.bazaarvoice.jolt.Defaultr.WildCards.ARRAY, spec);
            Key tempKey = null;
            try {
                tempKey = Key.parseSpec(rootSpec).iterator().next();
            } catch (NumberFormatException nfe) {
                // this is fine, it means the top level spec has non numeric keys
                //  if someone passes a top level array as input later we will error then
            }
            arrayRoot = tempKey;
        }


    }

    /**
     * Top level standalone Defaultr method.
     *
     * @param input JSON object to have defaults applied to. This will be modified.
     * @return the modified input
     */
    @Override
    public Object transform(Object input) {

        if (input == null) {
            // if null, assume HashMap
            input = new HashMap();
        }

        if ( translations == null ){
            throw new TransformException("El mapa de traducciones no fue cargado correctamente.");
        }

        // TODO : Make copy of the defaultee or like shiftr create a new output object
        if (input instanceof List) {
            if (arrayRoot == null) {
                throw new TransformException("The Spec provided can not handle input that is a top level Json Array.");
            }
            arrayRoot.applyChildren(input, translations);
        } else {
            mapRoot.applyChildren(input, translations);
        }

        return input;
    }

    public void setHomologaciones(Map<String, Map<String, String>> mapa) {
        this.translations = mapa;
    }

}
