package jolt.translator;

import com.bazaarvoice.jolt.exception.SpecException;
import jolt.Translator;

import javax.inject.Inject;
import java.util.Map;


public class TranslatorImpl extends Translator {

    /**
     * Configure an instance of Translator with a spec.
     *
     * @throws SpecException for a malformed spec or if there are issues
     */
    @Inject
    public TranslatorImpl(Object spec) {
        super(spec);
    }

    @Override
    public Map<String, Map<String, String>> obtenerHomologaciones(String appOrigen, String appDestino) {
        return null;
    }

}
