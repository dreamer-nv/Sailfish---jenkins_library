def call(def tfc_id) {
	def pipelinename = "${env.JOB_BASE_NAME}_${env.BUILD_NUMBER}"
    def response = httpRequest url: "https://ldcipqp.wdf.sap.corp:44376/brlt/tfc_ss/async?tfc_task_id=${tfc_id}&pipeline_name=${pipelinename}",
							   authentication: 'ARES_BUILD'
							   
    echo response.content
					
	if (response.status == 201 ) {
		unstable(response.content)
	}   
}