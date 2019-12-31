# Welcome to your CDK Java project!

This is a blank project for Java development with CDK.

The `cdk.json` file tells the CDK Toolkit how to execute your app.

It is a [Maven](https://maven.apache.org/) based project, so you can open this project with any Maven compatible Java IDE to build and run tests.

## Useful commands

 * `mvn package`     compile and run tests
 * `cdk ls`          list all stacks in the app
 * `cdk synth`       emits the synthesized CloudFormation template
 * `cdk deploy`      deploy this stack to your default AWS account/region
 * `cdk diff`        compare deployed stack with current state
 * `cdk docs`        open CDK documentation

Enjoy!

* `$ aws elasticbeanstalk list-platform-versions --query 'PlatformSummaryList[].PlatformArn'`
* refs
    * https://github.com/aws-samples/aws-cdk-examples/tree/master/typescript/elasticbeanstalk/elasticbeanstalk-environment
    * https://docs.aws.amazon.com/cli/latest/reference/elasticbeanstalk/list-platform-versions.html
    * https://github.com/aws-samples/aws-cdk-examples/tree/master/typescript/elasticbeanstalk/elasticbeanstalk-bg-pipeline