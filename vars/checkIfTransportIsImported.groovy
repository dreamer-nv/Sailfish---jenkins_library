def call(Map args) {
	timeout(time: 95, unit: 'MINUTES') {
		for (i = 0; i < 90; i++) {
			sleep(time:60,unit:"SECONDS")
			
			def responseGET = httpRequest url: "https://${args.hostname}:${args.port}/sap/bc/${args.external_alias}/sync?", 
							   authentication: args.credentials, 
							   httpMode: 'GET',
							   validResponseCodes: '200:500',
							   customHeaders: [[maskValue: true, name: 'x-csrf-token', value: 'Fetch']]
		
			if (responseGET.status != 500 ) {
				def token = responseGET.getHeaders().'x-csrf-token'[0]
				def cookies = responseGET.getHeaders().'set-cookie'
	
				def cookieList = []
					cookies.each {cookie ->
					def splitCookie = cookie.split(';')[0]
					cookieList.add(splitCookie)
					}
				
				def responsePOST = httpRequest url: "https://${args.hostname}:${args.port}/sap/bc/${args.external_alias}/sync?trkorr=${args.transport}&phase=${args.build_phase}&pipeline_build_id=${args.pipelinename}",
									 authentication: args.credentials, 
									 httpMode: 'POST',
									 validResponseCodes: '200:500',
									 customHeaders: [[maskValue: true, name: 'x-csrf-token', value: token], [name: 'Cookie', value: cookieList.join('; ')]]
			
				if (responsePOST.status != 500) {
					def json = new groovy.json.JsonSlurper().parseText(responsePOST.content)
					generateLogOutput(json)
	
					if (json.RESULTS.STATE == "FAILED") {
						i = 90
					} else if (json.RESULTS.STATE == "FINISHED") {
						i = 90
					} 
				}
			}
			if (i == 89) {
				unstable('Timeout of TFC Import Time')	
			}			
		}
	}
	sleep(time:300,unit:"SECONDS")
}
