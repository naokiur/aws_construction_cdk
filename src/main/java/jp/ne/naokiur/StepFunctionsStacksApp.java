package jp.ne.naokiur;

import software.amazon.awscdk.core.App;

public class StepFunctionsStacksApp {
    public static void main(final String[] args) {
        App app = new App();

        new StepFunctionsStacksStack(app, "StepFunctionsStacksStack");

        app.synth();
    }
}
