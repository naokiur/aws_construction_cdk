# Welcome to my CDK Java project! and CloudFormation.

## Prepare
* Need github access token in AWS secret manager.

This is a blank project for Java development with CDK.

The `cdk.json` file tells the CDK Toolkit how to execute your app.

It is a [Maven](https://maven.apache.org/) based project, so you can open this project with any Maven compatible Java IDE to build and run tests.

## Useful commands

### AWS CDK
 * `mvn package`     compile and run tests
 * `cdk ls`          list all stacks in the app
 * `cdk synth`       emits the synthesized CloudFormation template
 * `cdk deploy`      deploy this stack to your default AWS account/region
 * `cdk diff`        compare deployed stack with current state
 * `cdk docs`        open CDK documentation

### ElasticBeanstalk
* `$ aws elasticbeanstalk list-platform-versions --query 'PlatformSummaryList[].PlatformArn'`

### CloudFormation
* `aws cloudformation delete-stack --stack-name django-app-not-cd-eb-deploy-stack`
* `aws cloudformation create-stack --stack-name django-app-not-cd-eb-deploy-stack --template-body file://deploy_eb.yml --capabilities CAPABILITY_NAMED_IAM`
* `aws cloudformation create-change-set --template-body file://deploy_eb.yml --stack-name django-app-not-cd-eb-deploy-stack --change-set-name django-app-not-cd-eb-deploy-stack-$(date "+%Y%m%d-%H%M%S") --capabilities CAPABILITY_NAMED_IAM`
* `aws cloudformation execute-change-set --change-set-name`

## refs
### AWS CDK
* https://github.com/aws-samples/aws-cdk-examples/tree/master/typescript/elasticbeanstalk/elasticbeanstalk-environment
* https://docs.aws.amazon.com/cli/latest/reference/elasticbeanstalk/list-platform-versions.html
* https://github.com/aws-samples/aws-cdk-examples/tree/master/typescript/elasticbeanstalk/elasticbeanstalk-bg-pipeline

### AWS CloudFormation
* https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-codebuild-project-source.html
* https://docs.amazonaws.cn/en_us/codebuild/latest/userguide/sample-bitbucket-pull-request.html#sample-bitbucket-pull-request-filter-webhook-events-cfn
* https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-codepipeline-pipeline.html
* https://docs.aws.amazon.com/ja_jp/AWSCloudFormation/latest/UserGuide/aws-properties-s3-bucket-versioningconfig.html
* https://github.com/aws-quickstart/quickstart-codepipeline-bluegreen-deployment/tree/master/templates
