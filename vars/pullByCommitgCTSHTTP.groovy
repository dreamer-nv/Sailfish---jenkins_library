def call(Map args) {

    echo "Inside pullByCommit Function"
    def trkorr = ""
    def toolURL = 'http://'+args.hostname+':'+args.port+'/sap/bc/cts_abapvcs/repository/'+args.repo_id+'/pullByCommit?request='+args.commit_id
    echo 'tool url : '+toolURL	
    String ABAPURL = toolURL.toURL()
    def resultJSON = 'results/ABAP_'+args.hostname+'_gCTS_result_'+env.BUILD_NUMBER+'.json'
    def resp = httpRequest authentication: args.credentials, url: ABAPURL, outputFile: resultJSON
    echo 'gCTS PullByCommit Log : '+ resp.content
    try{
	    def gCTSMap = new groovy.json.JsonSlurper().parseText(resp.content)
	    trkorr = gCTSMap.trkorr
     } 
     catch (exc)
      {
         echo 'xml parser failed'
         echo 'exception : '+exc
      }
     
    echo 'trkorr is :'+ trkorr
    if (trkorr == null)
    {
      echo 'No new changes to ABAP objects'
      currentBuild.result = 'FAILURE'
      error "trkorr is null"
    }
    return trkorr
}
