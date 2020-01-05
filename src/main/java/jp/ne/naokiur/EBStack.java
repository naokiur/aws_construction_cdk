package jp.ne.naokiur;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.SecretValue;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.codepipeline.Artifact;
import software.amazon.awscdk.services.codepipeline.CfnPipeline;
import software.amazon.awscdk.services.codepipeline.StageOptions;
import software.amazon.awscdk.services.codepipeline.actions.Action;
import software.amazon.awscdk.services.codepipeline.actions.GitHubSourceAction;
import software.amazon.awscdk.services.codepipeline.actions.LambdaInvokeAction;
import software.amazon.awscdk.services.codepipeline.actions.S3DeployAction;
import software.amazon.awscdk.services.elasticbeanstalk.CfnApplication;
import software.amazon.awscdk.services.elasticbeanstalk.CfnEnvironment;
import software.amazon.awscdk.services.events.targets.CodePipeline;
import software.amazon.awscdk.services.iam.*;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.IBucket;

import java.util.ArrayList;
import java.util.Arrays;

public class EBStack extends Stack {
    public EBStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public EBStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        final String appName = "MyApp";
        final String platform = "arn:aws:elasticbeanstalk:ap-northeast-1::platform/Python 3.6 running on 64bit Amazon Linux/2.9.4";

        final CfnApplication app = CfnApplication.Builder
                .create(this, "Application")
                .applicationName(appName)
                .build();

        final CfnEnvironment env = CfnEnvironment.Builder
                .create(this, "Environment")
                .environmentName("MySampleEnvironment")
                .applicationName(app.getApplicationName())
                .platformArn(platform)
                .build();
//
//        // Source Stage
//        final Artifact sourceArtifact = Artifact.artifact("Source");
//
//        final SecretValue githubToken = SecretValue.ssmSecure(
//                "github-access-token-oauth",
//                "1"
//        );
//
//        final Action github = GitHubSourceAction.Builder
//                .create()
//                .actionName("Download From Github")
//                .oauthToken(githubToken)
//                .branch("master")
//                .repo("django_app")
//                .owner("naokiur") // lacking
//                .output(sourceArtifact) // lacking
//                .build();
//
//        final CfnPipeline.StageDeclarationProperty source = CfnPipeline.StageDeclarationProperty.builder()
//                .actions(new ArrayList<>(Arrays.asList(github)))
//                .name("source")
//                .build();
//
////        final StageOptions source = StageOptions.builder()
////                .stageName("source")
////                .actions(new ArrayList<>(Arrays.asList(github)))
////                .build();
//
//        // Deploy Stage
//        // TODO sample Lambda
////        final Function lambda = Function.Builder.create(this, "LambdaFunction")
////                .runtime(Runtime.PYTHON_3_6)
////                .code(Code.fromAsset("lambda"))
////                .handler("success.lambda_handler")
////                .build();
//
////        final Action deployByLambda = LambdaInvokeAction.Builder
////                .create()
////                .actionName("InvokeDeploying")
////                .lambda(lambda)
////                .inputs(new ArrayList<>(Arrays.asList(sourceArtifact)))
////                .build();
//        final IBucket bucket = Bucket.fromBucketArn(this, "existedBucket", "arn:aws:s3:::sample-650800120138");
//        final Action deployS3 = S3DeployAction.Builder
//                .create()
//                .actionName("Deploying")
//                .bucket(bucket)
//                .objectKey("test")
//                .input(sourceArtifact)
//                .build();
//
//        final CfnPipeline.StageDeclarationProperty deploy = CfnPipeline.StageDeclarationProperty.builder()
////                .actions(new ArrayList<>(Arrays.asList(deployByLambda)))
//                .actions(new ArrayList<>(Arrays.asList(deployS3)))
//                .name("deploy")
//                .build();
////        final StageOptions deploy = StageOptions.builder()
////                .stageName("deploy")
////                .actions(new ArrayList<>(Arrays.asList(deployByLambda)))
////                .build();
//
//        final IManagedPolicy pipelinePolicy = ManagedPolicy.fromAwsManagedPolicyName("AWSCodePipelineFullAccess");
////        final IManagedPolicy pipelinePolicy = ManagedPolicy.fromAwsManagedPolicyName("arn:aws:iam::aws:policy/AWSCodePipelineFullAccess");
//        final IPrincipal pipelinePrincipal = new ServicePrincipal("codepipeline.amazonaws.com");
//        final Role pipelineRole = Role.Builder
//                .create(this, "PipelineRole")
//                .roleName("PipelineRole")
//                .assumedBy(pipelinePrincipal)
//                .managedPolicies(new ArrayList<>(Arrays.asList(pipelinePolicy)))
//                .build();
//
//        final CfnPipeline pipeline = CfnPipeline.Builder
//                .create(this, "MyFirstPipeline")
//                .roleArn(pipelineRole.getRoleArn())
//                .stages(new ArrayList<>(Arrays.asList(source, deploy)))
//                .build();
    }
}
