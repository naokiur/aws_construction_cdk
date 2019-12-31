package jp.ne.naokiur;

import software.amazon.awscdk.core.Construct;
import software.amazon.awscdk.core.Stack;
import software.amazon.awscdk.core.StackProps;
import software.amazon.awscdk.services.elasticbeanstalk.CfnApplication;
import software.amazon.awscdk.services.elasticbeanstalk.CfnEnvironment;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;

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
    }
}
