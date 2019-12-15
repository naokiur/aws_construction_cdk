package jp.ne.naokiur;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.stepfunctions.Chain;
import software.amazon.awscdk.services.stepfunctions.Parallel;
import software.amazon.awscdk.services.stepfunctions.StateMachine;
import software.amazon.awscdk.services.stepfunctions.Task;
import software.amazon.awscdk.services.stepfunctions.tasks.InvokeFunction;

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
        createStateMachinePatternErrorByLambda();
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


        final Parallel parallelTask = Parallel.Builder.create(this, "ErrorInParallelParallelTask")
                .build()
                .branch(successTask)
                .branch(errorTask);

        final Task resultTask = Task.Builder.create(this, "ErrorInParallelResultTask")
                .task(InvokeFunction.Builder.create(this.resultFunction).build())
                .build();

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
