def call(json) {
	echo json.RESULTS.ID
	echo json.RESULTS.STATE
	
	def log = 'Log Output'
	json.RESULTS.LOG.each {
		log = log + '\n' + it.LINE
	}
	echo log
	
	if (json.RESULTS.STATE == "FINISHED") {
		echo 'Step performed without issues'
	} else {
		def summary = 'Error Output'
		json.RESULTS.SUMMARY.each {
			summary = summary + '\n' + it.LINE
		}
		unstable(summary)
	}
}

