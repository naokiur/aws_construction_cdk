package jp.ne.naokiur;

import software.amazon.awscdk.core.*;
import software.amazon.awscdk.services.codebuild.PipelineProject;
import software.amazon.awscdk.services.codepipeline.Artifact;
import software.amazon.awscdk.services.codepipeline.Pipeline;
import software.amazon.awscdk.services.codepipeline.StageProps;
import software.amazon.awscdk.services.codepipeline.actions.Action;
import software.amazon.awscdk.services.codepipeline.actions.CodeBuildAction;
import software.amazon.awscdk.services.codepipeline.actions.GitHubSourceAction;
import software.amazon.awscdk.services.codepipeline.actions.S3DeployAction;
import software.amazon.awscdk.services.s3.Bucket;

import java.util.ArrayList;
import java.util.Arrays;

public class CICDStack extends Stack {

    public CICDStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public CICDStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        final Artifact sourceArtifact = Artifact.artifact("Source");

//        final SecretValue githubToken = SecretValue.ssmSecure(
//                "github-access-token-oauth",
//                "1"
//        );
        final SecretsManagerSecretOptions secretOptions = SecretsManagerSecretOptions.builder()
                .jsonField("github-token")
                .build();
        final SecretValue githubToken = SecretValue.secretsManager(
                "naokiur-secret",
                secretOptions
        );

        final Action github = GitHubSourceAction.Builder
                .create()
                .actionName("DownloadFromGithub")
                .oauthToken(githubToken)
                .branch("master")
                .repo("django_app")
                .owner("naokiur")
                .output(sourceArtifact)
                .build();

        final StageProps source = StageProps.builder()
                .stageName("DownloadSourceFromGithub")
                .actions(new ArrayList<>(Arrays.asList(github)))
                .build();

        // build
        final PipelineProject codeBuildProject = PipelineProject.Builder
                .create(this, "GithubSourceBuild")
                .projectName("codeBuildAction")
                .build();

        final Artifact buildArtifact = Artifact.artifact("Build");
        final Action codeBuildAction = CodeBuildAction.Builder
                .create()
                .actionName("BuildSource")
                .project(codeBuildProject)
                .input(sourceArtifact)
                .outputs(new ArrayList<>(Arrays.asList(buildArtifact)))
                .build();

        final StageProps build = StageProps.builder()
                .stageName("BuildSource")
                .actions(new ArrayList<>(Arrays.asList(codeBuildAction)))
                .build();

        // deploy
        final Bucket deployBucket = Bucket.Builder
                .create(this, "deployS3")
                .bucketName("naokiur-deploy-bucket")
                .versioned(true)
                .build();

        final Action s3 = S3DeployAction.Builder
                .create()
                .bucket(deployBucket)
                .actionName("DeploySourceToS3")
                .input(buildArtifact)
                .build();

        final StageProps deploy = StageProps.builder()
                .stageName("SaveSourceToS3")
                .actions(new ArrayList<>(Arrays.asList(s3)))
                .build();

        final Pipeline saveToS3Pipeline = Pipeline.Builder
                .create(this, "saveSourceToS3")
                .pipelineName("saveSourceToS3")
                .stages(new ArrayList<>(Arrays.asList(source, build, deploy)))
                .build();
    }
}
