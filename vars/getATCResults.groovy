def call(Map args) {

    echo "Inside getATCResults"
    def toolURL = 'http://'+args.hostname+':'+args.port+'/sap/bc/sut/abapcheck/GET_RESULTS/'+args.atcresultKey
    echo 'tool url : '+toolURL	
    String ABAPURL = toolURL.toURL()
    def resultXML = 'results/ABAP_'+args.hostname+'_atc_result_'+env.BUILD_NUMBER+'.xml'
    def resp = httpRequest authentication: args.credentials, url: ABAPURL, outputFile: resultXML
    echo 'ATC Results : '+ resp.content
    //junit testResults:'results/ABAP_'+args.hostname+'_aunit_result_*.xml'
    //checkstyle canComputeNew: false, defaultEncoding: '', unstableTotalHigh: '', healthy: '', pattern: 'results/ABAP_'+args.hostname+'_atc_result_*.xml', unHealthy: ''
    recordIssues(tools: [checkStyle(pattern: 'results/ABAP_'+args.hostname+'_atc_result_'+env.BUILD_NUMBER+'.xml')])
}
