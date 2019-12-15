from logging import getLogger, INFO

logger = getLogger()
logger.setLevel(INFO)


def lambda_handler(event, context):
	logger.info('Lambda: test Success Begin.')
	logger.info('Lambda: test Success End.')

	return 'Hello from test Success'
