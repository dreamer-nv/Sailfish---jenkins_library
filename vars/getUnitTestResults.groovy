def call(Map args) {

    echo "Inside getUnitTestResults"
    def toolURL = 'http://'+args.hostname+':'+args.port+'/sap/bc/sut/aunit/GET_RESULTS/'+args.aunitresultKey
    echo 'tool url : '+toolURL	
    String ABAPURL = toolURL.toURL()
    def resultXML = 'results/ABAP_'+args.hostname+'_aunit_result_'+env.BUILD_NUMBER+'.xml'
    def resp = httpRequest authentication: args.credentials, url: ABAPURL, outputFile: resultXML
    echo 'Unit Test Results : '+ resp.content
    echo 'Before Junit CurrentBuild.result: '+ currentBuild.result
    junit allowEmptyResults: true, testResults:'results/ABAP_'+args.hostname+'_aunit_result_*.xml'
    echo 'After Junit CurrentBuild.result: '+ currentBuild.result	
    if (currentBuild.result == 'UNSTABLE')
	  {
		echo 'Some of the Unit Tests Failed, please check the Test Results in the Build Log '
		currentBuild.result = 'FAILURE'
    		error "Unit Tests failed"
	  }
}
