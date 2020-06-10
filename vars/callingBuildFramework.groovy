def call(Map args) {
	args.put("mode", "sync")
	def response = callAndResponde(args)

	def json = new groovy.json.JsonSlurper().parseText(response)
	generateLogOutput(json)
}
