def call(Map args) {
	echo "callingBuildFrameworkHTTP Function (ex. callingBuildFrameworkHTTPIscoper)"
	def responseGET = httpRequest url: "http://${args.hostname}:${args.port}/sap/bc/${args.external_alias}/sync?", 
							   authentication: args.credentials, 
							   httpMode: 'GET', 
							   customHeaders: [[maskValue: true, name: 'x-csrf-token', value: 'Fetch']]
					
	def token = responseGET.getHeaders().'x-csrf-token'[0]
	def cookies = responseGET.getHeaders().'set-cookie'

	def cookieList = []
		cookies.each {cookie ->
		def splitCookie = cookie.split(';')[0]
		cookieList.add(splitCookie)
		}
			
	def url_value = ""
	if (args.transport != "") {
		url_value = "http://${args.hostname}:${args.port}/sap/bc/${args.external_alias}/sync?trkorr=${args.transport}&phase=${args.build_phase}&pipeline_build_id=${args.pipelinename}"
	}  
	else if (args.package != "") {
		url_value = "http://${args.hostname}:${args.port}/sap/bc/${args.external_alias}/sync?package=${args.package}&phase=${args.build_phase}&pipeline_build_id=${args.pipelinename}"
	}
	else if (args.software_component != "") {
		url_value = "http://${args.hostname}:${args.port}/sap/bc/${args.external_alias}/sync?swc=${args.software_component}&phase=${args.build_phase}&pipeline_build_id=${args.pipelinename}"
	}
		
	def responsePost = httpRequest url: url_value,
					     authentication: args.credentials, 
						 httpMode: 'POST', 
						 customHeaders: [[maskValue: true, name: 'x-csrf-token', value: token], [name: 'Cookie', value: cookieList.join('; ')]]
						 
	def json = new groovy.json.JsonSlurper().parseText(responsePost.content)
	generateLogOutput(json)						 
}
