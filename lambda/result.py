from logging import getLogger, INFO

logger = getLogger()
logger.setLevel(INFO)


def lambda_handler(event, context):
	logger.info('Lambda: result Begin.')
	logger.info(event)
	logger.info(context)
	logger.info('Lambda: result End.')

	return 'Hello from result'
