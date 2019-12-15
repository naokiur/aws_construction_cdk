package com.myorg;

import software.amazon.awscdk.core.App;

import java.util.Arrays;

public class StepFunctionsStacksApp {
    public static void main(final String[] args) {
        App app = new App();

        new StepFunctionsStacksStack(app, "StepFunctionsStacksStack");

        app.synth();
    }
}
