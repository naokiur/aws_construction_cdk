package jp.ne.naokiur;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Duration;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.stepfunctions.*;
import software.amazon.awscdk.services.stepfunctions.tasks.InvokeFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class StepFunctionsStacksStack extends Stack {
    private Function resultFunction;

    public StepFunctionsStacksStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public StepFunctionsStacksStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);
        this.resultFunction = Function.Builder.create(this, "ResultFunction")
                .runtime(Runtime.PYTHON_3_6)
                .code(Code.fromAsset("lambda"))
                .handler("result.lambda_handler")
                .build();

        createStateMachinePatternErrorInParallel();
        createStateMachinePatternNested();
        createStateMachinePatternErrorByLambda();
    }

    private void createStateMachinePatternNested() {
        final Function errorHandleFunction = Function.Builder.create(this, "NestedErrorHandleFunction")
                .runtime(Runtime.PYTHON_3_6)
                .code(Code.fromAsset("lambda"))
                .handler("error_handle.lambda_handler")
                .build();

        final Function errorFunction = Function.Builder.create(this, "NestedErrorFunction")
                .runtime(Runtime.PYTHON_3_6)
                .code(Code.fromAsset("lambda"))
                .handler("error.lambda_handler")
                .build();

        final Function successFunction = Function.Builder.create(this, "NestedSuccessFunction")
                .runtime(Runtime.PYTHON_3_6)
                .code(Code.fromAsset("lambda"))
                .handler("success.lambda_handler")
                .build();

        final Task errorTask = Task.Builder.create(this, "NestedErrorTask")
                .task(InvokeFunction.Builder.create(errorFunction).build())
                .build()
                .addRetry(
                        RetryProps.builder()
                                .errors(new ArrayList<>(Collections.singletonList("States.ALL")))
                                .interval(Duration.millis(1000))
                                .maxAttempts(2)
                                .build()
                );

        final Task errorHandle = Task.Builder.create(this, "ErrorHandleTask")
                .task(InvokeFunction.Builder.create(errorHandleFunction).build())
                .build();

        final Fail errorFailTask = Fail.Builder.create(this, "errorFailTask")
                .build();

        final Task successTask = Task.Builder.create(this, "NestedSuccessTask")
                .task(InvokeFunction.Builder.create(successFunction).build())
                .build()
                .addRetry(
                        RetryProps.builder()
                                .errors(new ArrayList<>(Collections.singletonList("States.ALL")))
                                .interval(Duration.millis(1000))
                                .maxAttempts(2)
                                .build()
                );

        final Task successHandle = Task.Builder.create(this, "SuccessHandleTask")
                .task(InvokeFunction.Builder.create(errorHandleFunction).build())
                .build();

        final Fail successFailTask = Fail.Builder.create(this, "successFailTask")
                .build();

        final Task successTask2 = Task.Builder.create(this, "NestedSuccessTask2")
                .task(InvokeFunction.Builder.create(successFunction).build())
                .build()
                .addRetry(
                        RetryProps.builder()
                                .errors(new ArrayList<>(Collections.singletonList("States.ALL")))
                                .interval(Duration.millis(1000))
                                .maxAttempts(2)
                                .build()
                );

        final Task successHandle2 = Task.Builder.create(this, "SuccessHandleTask2")
                .task(InvokeFunction.Builder.create(errorHandleFunction).build())
                .build();

        final Fail successFailTask2 = Fail.Builder.create(this, "successFailTask2")
                .build();


        final Task resultTask = Task.Builder.create(this, "NestedResultTask")
                .task(InvokeFunction.Builder.create(this.resultFunction).build())
                .build()
                .addRetry(
                        RetryProps.builder()
                                .errors(new ArrayList<>(Collections.singletonList("States.ALL")))
                                .interval(Duration.millis(1000))
                                .maxAttempts(2)
                                .build()
                );

        final Parallel parallelError = Parallel.Builder.create(this, "NestedParallelError")
                .build()
                .branch(errorTask)
                .addCatch(
                        errorHandle.next(errorFailTask),
                        CatchProps.builder()
                                .errors(new ArrayList<>(Collections.singletonList("States.ALL")))
                                .build()
                );

        final Parallel parallelSuccess = Parallel.Builder.create(this, "NestedParallelSuccess")
                .build()
                .branch(successTask)
                .addCatch(
                        successHandle.next(successFailTask),
                        CatchProps.builder()
                                .errors(new ArrayList<>(Collections.singletonList("States.ALL")))
                                .build()
                );

        final Parallel parallelSuccess2 = Parallel.Builder.create(this, "NestedParallelSuccess2")
                .build()
                .branch(successTask2)
                .addCatch(
                        successHandle2.next(successFailTask2),
                        CatchProps.builder()
                                .errors(new ArrayList<>(Collections.singletonList("States.ALL")))
                                .build()
                );

        final Parallel parallelTask = Parallel.Builder.create(this, "NestedParallelTask")
                .build()
                .branch(parallelSuccess)
                .branch(parallelError)
                .branch(parallelSuccess2);

        final StateMachine machine = StateMachine.Builder.create(this, "PatternNested")
                .definition(parallelTask.next(resultTask))
                .build();
    }

    private void createStateMachinePatternErrorInParallel() {
        final Function errorFunction = Function.Builder.create(this, "ErrorFunction")
                .runtime(Runtime.PYTHON_3_6)
                .code(Code.fromAsset("lambda"))
                .handler("error.lambda_handler")
                .build();

        final Function successFunction = Function.Builder.create(this, "SuccessFunction")
                .runtime(Runtime.PYTHON_3_6)
                .code(Code.fromAsset("lambda"))
                .handler("success.lambda_handler")
                .build();

        final Task errorTask = Task.Builder.create(this, "ErrorTask")
                .task(InvokeFunction.Builder.create(errorFunction).build())
                .build();

        final Task successTask = Task.Builder.create(this, "SuccessTask")
                .task(InvokeFunction.Builder.create(successFunction).build())
                .build();

        final Task resultTask = Task.Builder.create(this, "ErrorInParallelResultTask")
                .task(InvokeFunction.Builder.create(this.resultFunction).build())
                .build();

        final Parallel parallelTask = Parallel.Builder.create(this, "ErrorInParallelParallelTask")
                .build()
                .branch(successTask)
                .branch(errorTask)
                .addCatch(
                        resultTask,
                        CatchProps.builder()
                                .errors(new ArrayList<>(Collections.singletonList("States.ALL")))
                                .build()
                );

        final StateMachine machine = StateMachine.Builder.create(this, "SimpleStateMachine")
                .definition(parallelTask.next(resultTask))
                .build();
    }

    private void createStateMachinePatternErrorByLambda() {

        final Function first = Function.Builder.create(this, "first")
                .runtime(Runtime.PYTHON_3_6)
                .code(Code.fromAsset("lambda"))
                .handler("task_first.lambda_handler")
                .build();

        final Function second = Function.Builder.create(this, "second")
                .runtime(Runtime.PYTHON_3_6)
                .code(Code.fromAsset("lambda"))
                .handler("task_second.lambda_handler")
                .build();

        final Function third = Function.Builder.create(this, "third")
                .runtime(Runtime.PYTHON_3_6)
                .code(Code.fromAsset("lambda"))
                .handler("task_third_error.lambda_handler")
                .build();

        final Task firstTask = Task.Builder.create(this, "FirstTask")
                .task(InvokeFunction.Builder.create(first).build())
                .build();

        final Task secondTask = Task.Builder.create(this, "SecondTask")
                .task(InvokeFunction.Builder.create(second).build())
                .build();

        final Task thirdTask = Task.Builder.create(this, "ThirdTask")
                .task(InvokeFunction.Builder.create(third).build())
                .build();

        final Parallel parallelTask = Parallel.Builder.create(this, "ErrorByLambdaParallelTask")
                .build()
                .branch(firstTask)
                .branch(secondTask)
                .branch(thirdTask);

        final Task resultTask = Task.Builder.create(this, "ErrorByLambdaResultTask")
                .task(InvokeFunction.Builder.create(this.resultFunction).build())
                .build();

        final StateMachine machine = StateMachine.Builder.create(this, "PatternErrorByLambda")
                .definition(parallelTask.next(resultTask))
                .build();

    }
}
