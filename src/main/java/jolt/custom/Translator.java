package jolt.custom;

import com.bazaarvoice.jolt.SpecDriven;
import com.bazaarvoice.jolt.Transform;
import com.bazaarvoice.jolt.exception.SpecException;
import com.bazaarvoice.jolt.exception.TransformException;
import jolt.custom.translator.Key;

import javax.inject.Inject;
import java.util.*;


public class Translator implements SpecDriven, Transform {

    public interface WildCards {
        String STAR = "*";
        String OR = "|";
        String ARRAY = "[]";
    }

    private final Key mapRoot;
    private final Key arrayRoot;
    private final Map<String, Map<String, String>> homologaciones;

    /**
     * Configure an instance of Defaultr with a spec.
     *
     * @throws SpecException for a malformed spec or if there are issues
     */
    @Inject
    public Translator(Object spec) {
        String HOMOLOGACIONES_KEY = "_homologaciones";
        String rootString = "root";


        Map<String, Object> newSpec = (LinkedHashMap<String, Object>) spec;

        {
            if (newSpec.containsKey(HOMOLOGACIONES_KEY)) {

                homologaciones = (Map<String, Map<String, String>>) newSpec.get(HOMOLOGACIONES_KEY);

                newSpec.remove(HOMOLOGACIONES_KEY);

            } else {
                throw new TransformException("The Spec provided doesn't have " + HOMOLOGACIONES_KEY + " object.");
            }
        }


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

        // TODO : Make copy of the defaultee or like shiftr create a new output object
        if (input instanceof List) {
            if (arrayRoot == null) {
                throw new TransformException("The Spec provided can not handle input that is a top level Json Array.");
            }
            arrayRoot.applyChildren(input, homologaciones);
        } else {
            mapRoot.applyChildren(input, homologaciones);
        }

        return input;
    }
}
