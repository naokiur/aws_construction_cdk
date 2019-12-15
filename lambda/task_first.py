from logging import getLogger, INFO

logger = getLogger()
logger.setLevel(INFO)


def lambda_handler(event, context):
	logger.info('Lambda: task first Begin.')
	logger.info('Lambda: task first End.')

	return {
		'code': 0,
		'functionName': 'task_first',
		'message': 'task first done'
	}
