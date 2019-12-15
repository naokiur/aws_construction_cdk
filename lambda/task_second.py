from logging import getLogger, INFO

logger = getLogger()
logger.setLevel(INFO)


def lambda_handler(event, context):
	logger.info('Lambda: task second Begin.')
	logger.info('Lambda: task second End.')

	return {
		'code': 0,
		'functionName': 'task_second',
		'message': 'task second done'
	}
