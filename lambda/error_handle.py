from logging import getLogger, INFO

logger = getLogger()
logger.setLevel(INFO)


def lambda_handler(event, context):
	logger.info('Lambda: error handle Begin.')
	logger.info(event)
	logger.info(context)
	logger.info('Lambda: error handle End.')

	return 'error handle Done'
