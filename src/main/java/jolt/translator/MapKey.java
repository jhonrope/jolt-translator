/*
 * Copyright 2013 Bazaarvoice, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jolt.translator;

import com.bazaarvoice.jolt.common.DeepCopy;
import com.bazaarvoice.jolt.exception.SpecException;

import java.util.*;
import java.util.stream.Collectors;

public class MapKey extends Key {

    public MapKey(String jsonKey, Object spec) {
        super(jsonKey, spec);
    }

    @Override
    protected int getLiteralIntKey() {
        throw new UnsupportedOperationException("Shouldn't be be asking a MapKey for int getLiteralIntKey().");
    }

    @Override
    protected void applyChild(Object container, Map<String, Map<String, String>> translations) {

        if (container instanceof Map) {
            Map<String, Object> defaulteeMap = (Map<String, Object>) container;

            // Find all defaultee keys that match the childKey spec.  Simple for Literal keys, more work for * and |.
            for (String literalKey : determineMatchingContainerKeys(defaulteeMap)) {
                applyLiteralKeyToContainer(literalKey, defaulteeMap, translations);
            }
        }
        // Else there is disagreement (with respect to Array vs Map) between the data in
        //  the Container vs the Defaultr Spec type for this key.  Container wins, so do nothing.
    }

    private void applyLiteralKeyToContainer(String literalKey, Map<String, Object> container, Map<String, Map<String, String>> translations) {

        Object defaulteeValue = container.get(literalKey);

        if (children == null) {
            if (defaulteeValue != null) {

                if (! (literalValue instanceof String)) {
                    throw new SpecException("Se requiere que el valor del lado derecho sea String.");
                }

                Map<String, String> keyHomologaciones = translations.get(literalValue);

                if (defaulteeValue instanceof String) {
                    String newValue = keyHomologaciones.getOrDefault(defaulteeValue.toString(), defaulteeValue.toString());

                    container.put(literalKey, DeepCopy.simpleDeepCopy(newValue));
                } else {
                    List<Object> list = (List<Object>) defaulteeValue;
                    List<Object> newList = list.stream().map(elm -> keyHomologaciones.getOrDefault(elm.toString(), elm.toString())).collect(Collectors.toList());

                    container.put(literalKey, DeepCopy.simpleDeepCopy(newList));
                }
            }
        } else {

            applyChildren(defaulteeValue, translations);
        }
    }


    private Collection<String> determineMatchingContainerKeys(Map<String, Object> container) {

        switch (getOp()) {
            case LITERAL:
                // the container should get these literal values added to it
                return keyStrings;
            case STAR:
                // Identify all its keys
                return container.keySet();
            case OR:
                // Identify the intersection between its keys and the OR values
                Set<String> intersection = new HashSet<>(container.keySet());
                intersection.retainAll(keyStrings);
                return intersection;
            default:
                throw new IllegalStateException("Someone has added an op type without changing this method.");
        }
    }
}
