package testcases.E012_dependency_visibility;

import com.google.common.truth.Truth;
import common.MissingDependenciesSubject;
import motif.ir.graph.errors.GraphErrors;
import motif.ir.graph.errors.MissingDependenciesError;

import java.util.List;

public class Test {

    public static GraphErrors errors;

    public static void run() {
        List<MissingDependenciesError> errors = Test.errors.getMissingDependenciesErrors();
        Truth.assertThat(errors).hasSize(1);
        MissingDependenciesError error = errors.get(0);
        MissingDependenciesSubject.assertThat(error)
                .matches(Child.class, String.class);
    }
}
