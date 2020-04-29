package fun.diasonti.autochessweb.data.pojo;

import java.util.*;

public class ValidationErrors {

    private final Map<String, Set<String>> errors = new HashMap<>();

    public Map<String, Set<String>> getErrors() {
        return Collections.unmodifiableMap(errors);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public void addError(String field, String error) {
        final Set<String> fieldErrors = errors.computeIfAbsent(field, key -> new HashSet<>());
        fieldErrors.add(error);
    }

}
