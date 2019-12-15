from logging import getLogger, INFO

logger = getLogger()
logger.setLevel(INFO)


def lambda_handler(event, context):
	logger.info('Lambda: test Begin.')

	raise Exception('Lambda raise exception')

	return 'Hello from Lambda'
