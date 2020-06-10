def call(Map args) {
	args.put("mode", "sync")
	def response = callAndResponde(args)
	
	def json = new groovy.json.JsonSlurper().parseText(response)
	generateLogOutput(json)
	
	for ( item in json.RESULTS.RUNTIME_VALUES ) {
		if (item.KEY == "TEST_TRANSPORT") {
			return item.VALUE
		}
	}
}
