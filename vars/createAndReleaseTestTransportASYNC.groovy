def call(Map args) {
	args.put("mode", "async")
	def build_id = callAndResponde(args)
	
	timeout(time: 120, unit: 'MINUTES') {  
		for (int i = 0; i < 480; i++) {
			sleep(time:60,unit:"SECONDS")
			
			def response = httpRequest url: "https://${args.hostname}:${args.port}/sap/bc/${args.external_alias}/sync?build_id=${build_id}&pipeline_build_id=${args.pipelinename}",
									   authentication: args.credentials, 
									   httpMode: 'GET',
									   validResponseCodes: '200:500'
			
			if (response.status != 500 ) {
				def json = new groovy.json.JsonSlurper().parseText(response.content)
				
				if (json.RESULTS.STATE == 'FAILED') {
					i = 480
					generateLogOutput(json)
				} else if (json.RESULTS.STATE == 'FINISHED') {
					i = 480
					generateLogOutput(json)
					for ( item in json.RESULTS.RUNTIME_VALUES ) {
						if (item.KEY == "TEST_TRANSPORT") {
							return item.VALUE
						}
					}				
				}
				if (i == 470) {
					unstable('Timeout')	
				}
			}
		}
	}
}
