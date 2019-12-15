from logging import getLogger, INFO

logger = getLogger()
logger.setLevel(INFO)


def lambda_handler(event, context):
	try:
		logger.info('Lambda: task third Begin.')
		raise Exception('error occurred.')

		logger.info('Lambda: task third End.')

	except Exception as e:
		return {
			'code': 1,
			'functionName': 'task_third',
			'message': 'task third done'
		}

	return {
		'code': 0,
		'functionName': 'task_third',
		'message': 'task third done'
	}
